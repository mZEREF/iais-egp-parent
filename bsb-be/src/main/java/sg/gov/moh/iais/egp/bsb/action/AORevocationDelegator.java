package sg.gov.moh.iais.egp.bsb.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.exception.IaisRuntimeException;
import com.ecquaria.cloud.moh.iais.common.utils.LogUtil;
import com.ecquaria.cloud.moh.iais.common.utils.MaskUtil;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.dto.LoginContext;
import com.ecquaria.cloud.moh.iais.helper.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import sg.gov.moh.iais.egp.bsb.client.RoutingHistoryClient;
import sg.gov.moh.iais.egp.bsb.constant.RevocationConstants;
import sg.gov.moh.iais.egp.bsb.constant.ValidationConstants;
import sg.gov.moh.iais.egp.bsb.dto.ValidationResultDto;
import sg.gov.moh.iais.egp.bsb.dto.entity.RoutingHistoryDto;
import sg.gov.moh.iais.egp.bsb.dto.file.DocRecordInfo;
import sg.gov.moh.iais.egp.bsb.dto.file.PrimaryDocDto;
import sg.gov.moh.iais.egp.bsb.dto.revocation.*;
import sg.gov.moh.iais.egp.bsb.client.RevocationClient;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.*;

import static sg.gov.moh.iais.egp.bsb.constant.AuditConstants.KEY_APP_ID;
import static sg.gov.moh.iais.egp.bsb.constant.AuditConstants.KEY_TASK_ID;
import static sg.gov.moh.iais.egp.bsb.constant.RevocationConstants.*;
import static sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants.KEY_ROUTING_HISTORY_LIST;

/**
 * @author Zhu Tangtang
 */
@Slf4j
@Delegator(value = "AORevocationDelegator")
public class AORevocationDelegator {
    private static final String ACTION_TYPE_ROUTE_BACK = "routeBack";
    private static final String ACTION_TYPE_ROUTE_TO_HM = "routeToHM";
    private static final String ACTION_TYPE_REJECT = "reject";
    private static final String ACTION_TYPE_APPROVE = "approve";
    private static final String ACTION_TYPE_PREPARE = "prepare";
    private static final String ACTION_TYPE = "action_type";

    private final RevocationClient revocationClient;
    private final RoutingHistoryClient routingHistoryClient;

    public AORevocationDelegator(RevocationClient revocationClient, RoutingHistoryClient routingHistoryClient) {
        this.revocationClient = revocationClient;
        this.routingHistoryClient = routingHistoryClient;
    }

    public void start(BaseProcessClass bpc) throws IllegalAccessException {
        AuditTrailHelper.auditFunction(MODULE_REVOCATION, FUNCTION_REVOCATION);
        HttpServletRequest request = bpc.request;
        IaisEGPHelper.clearSessionAttr(request, RevocationConstants.class);
        ParamUtil.setSessionAttr(request, PARAM_APPLICATION_SEARCH, null);
        ParamUtil.setSessionAttr(request, PARAM_REVOCATION_DETAIL, null);
        ParamUtil.setSessionAttr(request, PARAM_REVOKE_DTO, null);
    }

    public void prepareData(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        ParamUtil.setSessionAttr(request, FROM, null);
        ParamUtil.setSessionAttr(request, KEY_CAN_UPLOAD, "N");

        SubmitRevokeDto revokeDto = getRevokeDto(request);
        if (!StringUtils.hasLength(revokeDto.getAppId())) {
            String maskedAppId = ParamUtil.getString(request, KEY_APP_ID);
            String maskedTaskId = ParamUtil.getString(request, KEY_TASK_ID);
            if (log.isInfoEnabled()) {
                log.info("masked application id: [{}]", LogUtil.escapeCrlf(maskedAppId));
                log.info("masked task id: [{}]", LogUtil.escapeCrlf(maskedTaskId));
            }
            String appId = MaskUtil.unMaskValue("id", maskedAppId);
            String taskId = MaskUtil.unMaskValue("id", maskedTaskId);
            if (appId == null || appId.equals(maskedAppId)) {
                throw new IaisRuntimeException("Invalid masked application ID");
            }
            if (taskId == null || taskId.equals(maskedTaskId)) {
                throw new IaisRuntimeException("Invalid masked task ID");
            }
            revokeDto = revocationClient.getSubmitRevokeDtoByAppId(appId).getEntity();
            revokeDto.setTaskId(taskId);
            //show routingHistory list
            List<RoutingHistoryDto> routingHistoryDtoList = routingHistoryClient.getRoutingHistoryListByAppNo(revokeDto.getApplicationNo()).getEntity();
            ParamUtil.setSessionAttr(request, KEY_ROUTING_HISTORY_LIST, (Serializable) routingHistoryDtoList);
        }
        setRevocationDoc(request,revokeDto);
        ParamUtil.setSessionAttr(request, FROM, REVOCATION_TASK_LIST);
        ParamUtil.setSessionAttr(request, PARAM_REVOKE_DTO, revokeDto);
    }

    public void preConfirm(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        SubmitRevokeDto revokeDto = getRevokeDto(request);
        String reason = ParamUtil.getString(request, PARAM_REASON);
        String remarks = ParamUtil.getString(request, PARAM_AO_REMARKS);
        String aoDecision = ParamUtil.getString(request, PARAM_AO_DECISION);

        String[] strArr = reason.split(";");
        String a = strArr[strArr.length - 1];
        char[] charStr = a.toCharArray();
        StringBuilder sb = new StringBuilder();
        for (char c : charStr) {
            sb.append(c);
        }
        boolean contains = reason.contains(sb.toString());
        //get user name
        if (!contains) {
            revokeDto.setReason(PARAM_REASON_TYPE_AO);
            revokeDto.setReasonContent(reason);
        }
        LoginContext loginContext = (LoginContext) ParamUtil.getSessionAttr(request, AppConsts.SESSION_ATTR_LOGIN_USER);
        revokeDto.setLoginUser(loginContext.getUserName());
        revokeDto.setRemarks(remarks);
        revokeDto.setAoRemarks(remarks);
        revokeDto.setAoDecision(aoDecision);
        revokeDto.setModule("aoRevoke");
        setRevocationDoc(request,revokeDto);
        validateData(revokeDto, request);
        ParamUtil.setSessionAttr(request, PARAM_REVOKE_DTO, revokeDto);
    }

    public void approve(BaseProcessClass bpc) {
        SubmitRevokeDto submitRevokeDto = getRevokeDto(bpc.request);
        submitRevokeDto.setStatus(PARAM_APPLICATION_STATUS_APPROVED);
        revocationClient.updateRevokeApplication(submitRevokeDto);
    }

    public void reject(BaseProcessClass bpc) {
        SubmitRevokeDto submitRevokeDto = getRevokeDto(bpc.request);
        submitRevokeDto.setStatus(PARAM_APPLICATION_STATUS_REJECTED);
        revocationClient.updateRevokeApplication(submitRevokeDto);
    }

    public void routebackToDO(BaseProcessClass bpc) {
        SubmitRevokeDto submitRevokeDto = getRevokeDto(bpc.request);
        submitRevokeDto.setStatus(PARAM_APPLICATION_STATUS_PENDING_DO);
        revocationClient.updateRevokeApplication(submitRevokeDto);
    }

    public void routeToHM(BaseProcessClass bpc) {
        SubmitRevokeDto submitRevokeDto = getRevokeDto(bpc.request);
        submitRevokeDto.setStatus(PARAM_APPLICATION_STATUS_PENDING_HM);
        revocationClient.updateRevokeApplication(submitRevokeDto);
    }

    private SubmitRevokeDto getRevokeDto(HttpServletRequest request) {
        SubmitRevokeDto searchDto = (SubmitRevokeDto) ParamUtil.getSessionAttr(request, PARAM_REVOKE_DTO);
        return searchDto == null ? getDefaultRevokeDto() : searchDto;
    }

    private SubmitRevokeDto getDefaultRevokeDto() {
        return new SubmitRevokeDto();
    }

    public static void setRevocationDoc(HttpServletRequest request, SubmitRevokeDto revokeDto) {
        if (!CollectionUtils.isEmpty(revokeDto.getDocRecordInfos())) {
            PrimaryDocDto primaryDocDto = new PrimaryDocDto();
            primaryDocDto.setSavedDocMap(sg.gov.moh.iais.egp.bsb.util.CollectionUtils.uniqueIndexMap(revokeDto.getDocRecordInfos(), DocRecordInfo::getRepoId));
            Map<String, List<DocRecordInfo>> saveFiles = primaryDocDto.getExistDocTypeMap();
            Set<String> docTypes = saveFiles.keySet();
            ParamUtil.setRequestAttr(request, "docTypes", docTypes);
            ParamUtil.setRequestAttr(request, "savedFiles", saveFiles);
            ParamUtil.setSessionAttr(request, "primaryDocDto", primaryDocDto);
        }
    }

    private void validateData(SubmitRevokeDto dto, HttpServletRequest request){
        //validation
        String actionType = null;
        ValidationResultDto validationResultDto = revocationClient.validateRevoke(dto);
        if (!validationResultDto.isPass()){
            ParamUtil.setRequestAttr(request, ValidationConstants.KEY_VALIDATION_ERRORS, validationResultDto.toErrorMsg());
            actionType = ACTION_TYPE_PREPARE;
        }else {
            if (dto.getAoDecision().equals("MOHPRO008")){
                actionType = ACTION_TYPE_ROUTE_BACK;
            } else if (dto.getAoDecision().equals("MOHPRO009")){
                actionType = ACTION_TYPE_ROUTE_TO_HM;
            } else if (dto.getAoDecision().equals("MOHPRO003")){
                actionType = ACTION_TYPE_REJECT;
            } else if (dto.getAoDecision().equals("MOHPRO007")){
                actionType = ACTION_TYPE_APPROVE;
            }
        }
        ParamUtil.setRequestAttr(request, ACTION_TYPE, actionType);
    }

}

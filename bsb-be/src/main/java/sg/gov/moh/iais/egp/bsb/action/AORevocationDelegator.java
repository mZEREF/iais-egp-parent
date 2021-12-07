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
import sg.gov.moh.iais.egp.bsb.client.DocClient;
import sg.gov.moh.iais.egp.bsb.constant.RevocationConstants;
import sg.gov.moh.iais.egp.bsb.constant.ValidationConstants;
import sg.gov.moh.iais.egp.bsb.dto.audit.AuditDocDto;
import sg.gov.moh.iais.egp.bsb.dto.revocation.*;
import sg.gov.moh.iais.egp.bsb.client.RevocationClient;
import sg.gov.moh.iais.egp.bsb.entity.*;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

import static sg.gov.moh.iais.egp.bsb.constant.AuditConstants.KEY_APP_ID;
import static sg.gov.moh.iais.egp.bsb.constant.AuditConstants.KEY_TASK_ID;
import static sg.gov.moh.iais.egp.bsb.constant.RevocationConstants.*;

/**
 * @author Zhu Tangtang
 */
@Slf4j
@Delegator(value = "AORevocationDelegator")
public class AORevocationDelegator {
    private final RevocationClient revocationClient;
    private final DocClient docClient;

    public AORevocationDelegator(RevocationClient revocationClient, DocClient docClient) {
        this.revocationClient = revocationClient;
        this.docClient = docClient;
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
        ParamUtil.setSessionAttr(request, BACK, null);
        ParamUtil.setSessionAttr(request, AUDIT_DOC_DTO, null);

        SubmitRevokeDto revokeDto = getRevokeDto(request);
        Boolean needShowError = (Boolean) ParamUtil.getRequestAttr(request, ValidationConstants.KEY_SHOW_ERROR_SWITCH);
        if (Boolean.TRUE.equals(needShowError)) {
            ParamUtil.setRequestAttr(request, ValidationConstants.KEY_VALIDATION_ERRORS, revokeDto.retrieveValidationResult());
        }
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
            AuditDocDto auditDocDto = new AuditDocDto();
            List<FacilityDoc> facilityDocList = docClient.getFacilityDocByFacId(revokeDto.getFacId()).getEntity();
            if (!CollectionUtils.isEmpty(facilityDocList)) {
                for (FacilityDoc facilityDoc : facilityDocList) {
                    String submitByName = IaisEGPHelper.getCurrentAuditTrailDto().getMohUserId();
                    facilityDoc.setSubmitByName(submitByName);
                }
            }
            auditDocDto.setFacilityDocs(facilityDocList);
            ParamUtil.setSessionAttr(request, AUDIT_DOC_DTO, auditDocDto);
        }
        ParamUtil.setSessionAttr(request, BACK, REVOCATION_TASK_LIST);
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
        revokeDto.setAppId(revokeDto.getAppId());
        revokeDto.setApprovalId(revokeDto.getApprovalId());
        revokeDto.setProcessType(revokeDto.getProcessType());
        revokeDto.setAoDecision(aoDecision);
        revokeDto.setModule("aoRevoke");
        doValidation(revokeDto, request);
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

    /**
     * just a method to do simple valid,maybe update in the future
     * doValidation
     */
    private void doValidation(SubmitRevokeDto dto, HttpServletRequest request) {
        if (dto.doValidation()) {
            ParamUtil.setRequestAttr(request, ValidationConstants.IS_VALID, ValidationConstants.YES);
        } else {
            ParamUtil.setRequestAttr(request, ValidationConstants.IS_VALID, ValidationConstants.NO);
            ParamUtil.setRequestAttr(request, ValidationConstants.KEY_SHOW_ERROR_SWITCH, Boolean.TRUE);
        }
    }

}

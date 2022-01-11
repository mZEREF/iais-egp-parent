package sg.gov.moh.iais.egp.bsb.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.exception.IaisRuntimeException;
import com.ecquaria.cloud.moh.iais.common.utils.LogUtil;
import com.ecquaria.cloud.moh.iais.common.utils.MaskUtil;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.dto.LoginContext;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import sg.gov.moh.iais.egp.bsb.client.AuditClientBE;
import sg.gov.moh.iais.egp.bsb.constant.AuditConstants;
import sg.gov.moh.iais.egp.bsb.constant.ValidationConstants;
import sg.gov.moh.iais.egp.bsb.dto.ResponseDto;
import sg.gov.moh.iais.egp.bsb.dto.ValidationResultDto;
import sg.gov.moh.iais.egp.bsb.dto.audit.OfficerProcessAuditDto;
import sg.gov.moh.iais.egp.bsb.dto.file.DocRecordInfo;
import sg.gov.moh.iais.egp.bsb.dto.file.PrimaryDocDto;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static sg.gov.moh.iais.egp.bsb.constant.AuditConstants.*;
import static sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants.*;

/**
 * @author Zhu Tangtang
 */
@Slf4j
@Delegator(value = "selfAuditDelegator")
public class SelfAuditDelegator {
    private static final String ACTION_TYPE_VERIFIED = "doVerified";
    private static final String ACTION_TYPE_PREPARE = "prepare";
    private static final String ACTION_TYPE_APPROVE = "doApprove";
    private static final String ACTION_TYPE_REJECT = "doReject";
    private static final String ACTION_TYPE_INTERNAL = "doRouteBack";
    private static final String ACTION_TYPE_RFI = "doRequestInfo";
    private static final String ACTION_TYPE = "action_type";

    private final AuditClientBE auditClientBE;

    public SelfAuditDelegator(AuditClientBE auditClientBE) {
        this.auditClientBE = auditClientBE;
    }

    public void start(BaseProcessClass bpc) throws IllegalAccessException {
        AuditTrailHelper.auditFunction(MODULE_AUDIT, FUNCTION_AUDIT);
        HttpServletRequest request = bpc.request;
        IaisEGPHelper.clearSessionAttr(request, AuditConstants.class);
        ParamUtil.setSessionAttr(request, PARAM_AUDIT_SEARCH, null);
        ParamUtil.setSessionAttr(request, KEY_OFFICER_PROCESS_DATA, null);
    }

    public void prepareProcessSelfAuditData(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        OfficerProcessAuditDto dto = getProcessDto(request);
        Boolean needShowError = (Boolean) ParamUtil.getRequestAttr(request, ValidationConstants.KEY_SHOW_ERROR_SWITCH);
        if(Boolean.TRUE.equals(needShowError)){
            ParamUtil.setRequestAttr(request,ValidationConstants.KEY_VALIDATION_ERRORS,dto.retrieveValidationResult());
        }
        if (!StringUtils.hasLength(dto.getTaskId())) {
            //get needed data by appId(contain:audit,auditApp,Facility)
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

            ResponseDto<OfficerProcessAuditDto> responseDto = auditClientBE.getOfficerProcessDataByAppId(appId);
            if (responseDto.ok()) {
                dto = responseDto.getEntity();
                dto.setTaskId(taskId);
                ParamUtil.setSessionAttr(request, KEY_OFFICER_PROCESS_DATA, dto);
            } else {
                log.warn("get audit API doesn't return ok, the response is {}", responseDto);
                ParamUtil.setRequestAttr(request, KEY_OFFICER_PROCESS_DATA, new OfficerProcessAuditDto());
            }
        }
        setSelfAuditDoc(request,dto);
        ParamUtil.setSessionAttr(request, KEY_OFFICER_PROCESS_DATA, dto);
    }

    public void doValidate(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        OfficerProcessAuditDto dto = getProcessDto(request);
        LoginContext loginContext = (LoginContext)ParamUtil.getSessionAttr(request, AppConsts.SESSION_ATTR_LOGIN_USER);
        String auditOutCome = ParamUtil.getRequestString(request,AUDIT_OUTCOME);
        String doRemark = ParamUtil.getRequestString(request,PARAM_DO_REMARKS);
        String finalRemark = ParamUtil.getRequestString(request,FINAL_REMARK);//on or null
        String doDecision = ParamUtil.getRequestString(request,PARAM_DO_DECISION);
        //
        dto.setAuditOutCome(auditOutCome);
        dto.setDoRemarks(doRemark);
        dto.setDoDecision(doDecision);
        dto.setActionBy(loginContext.getUserName());
        dto.setModule("doProcessSelfAudit");
        if (finalRemark==null) {
            dto.setFinalRemarks("No");
        }else {
            dto.setFinalRemarks("Yes");
        }
        doValidateData(dto,request);
        ParamUtil.setSessionAttr(request, KEY_OFFICER_PROCESS_DATA, dto);
    }

    public void aoValidate(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        OfficerProcessAuditDto dto = getProcessDto(request);
        LoginContext loginContext = (LoginContext)ParamUtil.getSessionAttr(request, AppConsts.SESSION_ATTR_LOGIN_USER);
        String auditOutCome = ParamUtil.getRequestString(request,AUDIT_OUTCOME);
        String aoRemark = ParamUtil.getRequestString(request,AO_REMARKS);
        String finalRemark = ParamUtil.getRequestString(request,FINAL_REMARK);//on or null
        String aoDecision = ParamUtil.getRequestString(request,PARAM_AO_DECISION);
        //
        dto.setAuditOutCome(auditOutCome);
        dto.setAoRemarks(aoRemark);
        dto.setAoDecision(aoDecision);
        dto.setActionBy(loginContext.getUserName());
        dto.setModule("aoProcessSelfAudit");
        if (finalRemark==null) {
            dto.setFinalRemarks("No");
        }else {
            dto.setFinalRemarks("Yes");
        }
        aoValidateData(dto,request);
        ParamUtil.setSessionAttr(request, KEY_OFFICER_PROCESS_DATA, dto);
    }

    public void doVerified(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        OfficerProcessAuditDto dto = getProcessDto(request);
        dto.setProcessDecision(dto.getDoDecision());
        dto.setAuditAppStatus(PARAM_AUDIT_STATUS_PENDING_AO);
        dto.setAppStatus(APP_STATUS_PEND_AO);
        auditClientBE.officerProcessSelfAudit(dto);
    }

    public void doRequestForInformation(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        OfficerProcessAuditDto dto = getProcessDto(request);
        dto.setProcessDecision(dto.getDoDecision());
        dto.setAuditStatus(PARAM_AUDIT_STATUS_PENDING_APPLICANT_INPUT);
        dto.setAuditAppStatus(PARAM_AUDIT_STATUS_PENDING_APPLICANT_INPUT);
        dto.setAppStatus(APP_STATUS_PEND_INPUT);
        auditClientBE.officerProcessSelfAudit(dto);
    }

    public void doReject(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        OfficerProcessAuditDto dto = getProcessDto(request);
        dto.setProcessDecision(dto.getDoDecision());
        dto.setAuditAppStatus(PARAM_AUDIT_STATUS_PENDING_AO);
        dto.setAppStatus(APP_STATUS_PEND_AO);
        auditClientBE.officerProcessSelfAudit(dto);
    }

    public void aoInternalClarifications(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        OfficerProcessAuditDto dto = getProcessDto(request);
        dto.setProcessDecision(dto.getAoDecision());
        dto.setAuditStatus(PARAM_AUDIT_STATUS_PENDING_DO);
        dto.setAuditAppStatus(PARAM_AUDIT_STATUS_PENDING_DO);
        dto.setAppStatus(APP_STATUS_PEND_DO);
        auditClientBE.officerProcessSelfAudit(dto);
    }

    public void aoApproved(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        OfficerProcessAuditDto dto = getProcessDto(request);
        dto.setProcessDecision(dto.getAoDecision());
        dto.setAuditStatus(PARAM_AUDIT_STATUS_COMPLETED);
        dto.setAuditAppStatus(PARAM_AUDIT_STATUS_COMPLETED);
        dto.setAppStatus(APP_STATUS_APPROVED);
        auditClientBE.officerProcessSelfAudit(dto);
    }

    private OfficerProcessAuditDto getProcessDto(HttpServletRequest request) {
        OfficerProcessAuditDto searchDto = (OfficerProcessAuditDto) ParamUtil.getSessionAttr(request, KEY_OFFICER_PROCESS_DATA);
        return searchDto == null ? getDefaultProcessDto() : searchDto;
    }

    private OfficerProcessAuditDto getDefaultProcessDto() {
        return new OfficerProcessAuditDto();
    }

    public static void setSelfAuditDoc(HttpServletRequest request, OfficerProcessAuditDto auditDto) {
        PrimaryDocDto primaryDocDto = new PrimaryDocDto();
        primaryDocDto.setSavedDocMap(sg.gov.moh.iais.egp.bsb.util.CollectionUtils.uniqueIndexMap(auditDto.getDocRecordInfos(), DocRecordInfo::getRepoId));
        Map<String, List<DocRecordInfo>> saveFiles = primaryDocDto.getExistDocTypeMap();
        Set<String> docTypes = saveFiles.keySet();
        ParamUtil.setRequestAttr(request, "docTypes", docTypes);
        ParamUtil.setRequestAttr(request, "savedFiles", saveFiles);
        ParamUtil.setSessionAttr(request, "primaryDocDto", primaryDocDto);
    }

    private void doValidateData(OfficerProcessAuditDto dto, HttpServletRequest request){
        //validation
        String actionType = null;
        ValidationResultDto validationResultDto = auditClientBE.validateOfficerAuditDt(dto);
        if (!validationResultDto.isPass()){
            ParamUtil.setRequestAttr(request, ValidationConstants.KEY_VALIDATION_ERRORS, validationResultDto.toErrorMsg());
            actionType = ACTION_TYPE_PREPARE;
        }else {
            if (dto.getAoDecision().equals("MOHPRO003")){
                actionType = ACTION_TYPE_REJECT;
            } else if (dto.getAoDecision().equals("MOHPRO002")){
                actionType = ACTION_TYPE_RFI;
            } else if (dto.getAoDecision().equals("MOHPRO010")){
                actionType = ACTION_TYPE_VERIFIED;
            }
        }
        ParamUtil.setRequestAttr(request, ACTION_TYPE, actionType);
    }

    private void aoValidateData(OfficerProcessAuditDto dto, HttpServletRequest request){
        //validation
        String actionType = null;
        ValidationResultDto validationResultDto = auditClientBE.validateOfficerAuditDt(dto);
        if (!validationResultDto.isPass()){
            ParamUtil.setRequestAttr(request, ValidationConstants.KEY_VALIDATION_ERRORS, validationResultDto.toErrorMsg());
            actionType = ACTION_TYPE_PREPARE;
        }else {
            if (dto.getAoDecision().equals("MOHPRO011")){
                actionType = ACTION_TYPE_INTERNAL;
            } else if (dto.getAoDecision().equals("MOHPRO007")){
                actionType = ACTION_TYPE_APPROVE;
            }
        }
        ParamUtil.setRequestAttr(request, ACTION_TYPE, actionType);
    }
}

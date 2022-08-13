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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import sg.gov.moh.iais.egp.bsb.client.AuditClientBE;
import sg.gov.moh.iais.egp.bsb.constant.AuditConstants;
import sg.gov.moh.iais.egp.bsb.constant.ValidationConstants;
import sg.gov.moh.iais.egp.bsb.dto.ResponseDto;
import sg.gov.moh.iais.egp.bsb.dto.validation.ValidationResultDto;
import sg.gov.moh.iais.egp.bsb.dto.audit.OfficerProcessAuditDto;
import sg.gov.moh.iais.egp.bsb.service.ProcessHistoryService;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import static com.ecquaria.cloud.moh.iais.common.constant.BsbAuditTrailConstants.FUNCTION_SELF_AUDIT;
import static com.ecquaria.cloud.moh.iais.common.constant.BsbAuditTrailConstants.MODULE_AUDIT;
import static sg.gov.moh.iais.egp.bsb.constant.AuditConstants.AO_REMARKS;
import static sg.gov.moh.iais.egp.bsb.constant.AuditConstants.AUDIT_OUTCOME;
import static sg.gov.moh.iais.egp.bsb.constant.AuditConstants.FINAL_REMARK;
import static sg.gov.moh.iais.egp.bsb.constant.AuditConstants.KEY_APP_ID;
import static sg.gov.moh.iais.egp.bsb.constant.AuditConstants.KEY_OFFICER_PROCESS_DATA;
import static sg.gov.moh.iais.egp.bsb.constant.AuditConstants.KEY_TASK_ID;
import static sg.gov.moh.iais.egp.bsb.constant.AuditConstants.PARAM_AO_DECISION;
import static sg.gov.moh.iais.egp.bsb.constant.AuditConstants.PARAM_AUDIT_SEARCH;
import static sg.gov.moh.iais.egp.bsb.constant.AuditConstants.PARAM_AUDIT_STATUS_COMPLETED;
import static sg.gov.moh.iais.egp.bsb.constant.AuditConstants.PARAM_AUDIT_STATUS_PENDING_AO;
import static sg.gov.moh.iais.egp.bsb.constant.AuditConstants.PARAM_AUDIT_STATUS_PENDING_APPLICANT_INPUT;
import static sg.gov.moh.iais.egp.bsb.constant.AuditConstants.PARAM_AUDIT_STATUS_PENDING_DO;
import static sg.gov.moh.iais.egp.bsb.constant.AuditConstants.PARAM_DO_DECISION;
import static sg.gov.moh.iais.egp.bsb.constant.AuditConstants.PARAM_DO_REMARKS;
import static sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants.APP_STATUS_APPROVED;
import static sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants.APP_STATUS_PEND_AO_APPROVAL;
import static sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants.APP_STATUS_PEND_APPLICANT_INPUT;
import static sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants.APP_STATUS_PEND_DO_RECOMMENDATION;
import static sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants.MOH_PROCESS_DECISION_APPROVE;
import static sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants.MOH_PROCESS_DECISION_REJECT;
import static sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants.MOH_PROCESS_DECISION_REQUEST_FOR_INFORMATION;
import static sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants.KEY_TAB_DOCUMENT_SUPPORT_DOC_LIST;

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
    private final ProcessHistoryService processHistoryService;

    @Autowired
    public SelfAuditDelegator(AuditClientBE auditClientBE, ProcessHistoryService processHistoryService) {
        this.auditClientBE = auditClientBE;
        this.processHistoryService = processHistoryService;
    }

    public void start(BaseProcessClass bpc) throws IllegalAccessException {
        AuditTrailHelper.auditFunction(MODULE_AUDIT, FUNCTION_SELF_AUDIT);
        HttpServletRequest request = bpc.request;
        IaisEGPHelper.clearSessionAttr(request, AuditConstants.class);
        HttpSession session = request.getSession();
        session.removeAttribute(PARAM_AUDIT_SEARCH);
        session.removeAttribute(KEY_OFFICER_PROCESS_DATA);
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
                //show routingHistory list
                processHistoryService.getAndSetHistoryInSession(dto.getApplicationNo(), request);
                ParamUtil.setSessionAttr(request, KEY_OFFICER_PROCESS_DATA, dto);
                ParamUtil.setRequestAttr(request, KEY_TAB_DOCUMENT_SUPPORT_DOC_LIST, dto.getDisplayDtos());
            } else {
                log.warn("get audit API doesn't return ok, the response is {}", responseDto);
                ParamUtil.setRequestAttr(request, KEY_OFFICER_PROCESS_DATA, new OfficerProcessAuditDto());
            }
        }
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
        dto.setAppStatus(APP_STATUS_PEND_AO_APPROVAL);
        auditClientBE.officerProcessSelfAudit(dto);
    }

    public void doRequestForInformation(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        OfficerProcessAuditDto dto = getProcessDto(request);
        dto.setProcessDecision(dto.getDoDecision());
        dto.setAuditStatus(PARAM_AUDIT_STATUS_PENDING_APPLICANT_INPUT);
        dto.setAuditAppStatus(PARAM_AUDIT_STATUS_PENDING_APPLICANT_INPUT);
        // TODO: check this app status
        dto.setAppStatus(APP_STATUS_PEND_APPLICANT_INPUT);
        auditClientBE.officerProcessSelfAudit(dto);
    }

    public void doReject(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        OfficerProcessAuditDto dto = getProcessDto(request);
        dto.setProcessDecision(dto.getDoDecision());
        dto.setAuditAppStatus(PARAM_AUDIT_STATUS_PENDING_AO);
        dto.setAppStatus(APP_STATUS_PEND_AO_APPROVAL);
        auditClientBE.officerProcessSelfAudit(dto);
    }

    public void aoInternalClarifications(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        OfficerProcessAuditDto dto = getProcessDto(request);
        dto.setProcessDecision(dto.getAoDecision());
        dto.setAuditStatus(PARAM_AUDIT_STATUS_PENDING_DO);
        dto.setAuditAppStatus(PARAM_AUDIT_STATUS_PENDING_DO);
        dto.setAppStatus(APP_STATUS_PEND_DO_RECOMMENDATION);
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

    private void doValidateData(OfficerProcessAuditDto dto, HttpServletRequest request){
        //validation
        String actionType = null;
        ValidationResultDto validationResultDto = auditClientBE.validateOfficerAuditDt(dto);
        if (!validationResultDto.isPass()){
            ParamUtil.setRequestAttr(request, ValidationConstants.KEY_VALIDATION_ERRORS, validationResultDto.toErrorMsg());
            actionType = ACTION_TYPE_PREPARE;
        }else {
            // TODO: check these decision
            if (dto.getDoDecision().equals(MOH_PROCESS_DECISION_REJECT)){
                actionType = ACTION_TYPE_REJECT;
            } else if (dto.getDoDecision().equals(MOH_PROCESS_DECISION_REQUEST_FOR_INFORMATION)){
                actionType = ACTION_TYPE_RFI;
            } else if (dto.getDoDecision().equals("")){
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
            // TODO: check these decision
            if (dto.getAoDecision().equals("")){
                actionType = ACTION_TYPE_INTERNAL;
            } else if (dto.getAoDecision().equals(MOH_PROCESS_DECISION_APPROVE)){
                actionType = ACTION_TYPE_APPROVE;
            }
        }
        ParamUtil.setRequestAttr(request, ACTION_TYPE, actionType);
    }
}

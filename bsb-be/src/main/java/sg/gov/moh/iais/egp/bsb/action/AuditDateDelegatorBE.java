package sg.gov.moh.iais.egp.bsb.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.exception.IaisRuntimeException;
import com.ecquaria.cloud.moh.iais.common.utils.LogUtil;
import com.ecquaria.cloud.moh.iais.common.utils.MaskUtil;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
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
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;

import static com.ecquaria.cloud.moh.iais.common.constant.BsbAuditTrailConstants.FUNCTION_AUDIT_DATE;
import static com.ecquaria.cloud.moh.iais.common.constant.BsbAuditTrailConstants.MODULE_AUDIT;
import static sg.gov.moh.iais.egp.bsb.constant.AuditConstants.KEY_APP_ID;
import static sg.gov.moh.iais.egp.bsb.constant.AuditConstants.KEY_OFFICER_PROCESS_DATA;
import static sg.gov.moh.iais.egp.bsb.constant.AuditConstants.KEY_TASK_ID;
import static sg.gov.moh.iais.egp.bsb.constant.AuditConstants.PARAM_AO_DECISION;
import static sg.gov.moh.iais.egp.bsb.constant.AuditConstants.PARAM_AO_REASON;
import static sg.gov.moh.iais.egp.bsb.constant.AuditConstants.PARAM_AO_REMARKS;
import static sg.gov.moh.iais.egp.bsb.constant.AuditConstants.PARAM_AUDIT_SEARCH;
import static sg.gov.moh.iais.egp.bsb.constant.AuditConstants.PARAM_AUDIT_STATUS_CANCELLED;
import static sg.gov.moh.iais.egp.bsb.constant.AuditConstants.PARAM_AUDIT_STATUS_COMPLETED;
import static sg.gov.moh.iais.egp.bsb.constant.AuditConstants.PARAM_AUDIT_STATUS_PENDING_AO;
import static sg.gov.moh.iais.egp.bsb.constant.AuditConstants.PARAM_AUDIT_STATUS_PENDING_APPLICANT_INPUT;
import static sg.gov.moh.iais.egp.bsb.constant.AuditConstants.PARAM_DO_DECISION;
import static sg.gov.moh.iais.egp.bsb.constant.AuditConstants.PARAM_DO_REASON;
import static sg.gov.moh.iais.egp.bsb.constant.AuditConstants.PARAM_DO_REMARKS;
import static sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants.APP_STATUS_PEND_AO_APPROVAL;
import static sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants.APP_STATUS_PEND_APPLICANT_INPUT;

/**
 * @author Zhu Tangtang
 */
@Slf4j
@Delegator(value = "auditDateDelegator")
public class AuditDateDelegatorBE {
    private static final String ACTION_TYPE_REJECT = "doReject";
    private static final String ACTION_TYPE_APPROVE = "doApprove";
    private static final String ACTION_TYPE_PREPARE = "prepare";
    private static final String ACTION_TYPE = "action_type";

    private final AuditClientBE auditClientBE;

    @Autowired
    public AuditDateDelegatorBE(AuditClientBE auditClientBE){
        this.auditClientBE = auditClientBE;
    }

    public void start(BaseProcessClass bpc) throws IllegalAccessException {
        AuditTrailHelper.auditFunction(MODULE_AUDIT, FUNCTION_AUDIT_DATE);
        HttpServletRequest request = bpc.request;
        IaisEGPHelper.clearSessionAttr(request, AuditConstants.class);
        ParamUtil.setSessionAttr(request, PARAM_AUDIT_SEARCH, null);
        ParamUtil.setSessionAttr(request, KEY_OFFICER_PROCESS_DATA, null);
    }

    /**
     * AutoStep: prepareData
     * MohDOCheckAuditDt
     * MohAOCheckAuditDt
     */
    public void prepareDOAndAOReviewData(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        OfficerProcessAuditDto dto = getProcessDto(request);
        Boolean needShowError = (Boolean) ParamUtil.getRequestAttr(request, ValidationConstants.KEY_SHOW_ERROR_SWITCH);
        if(Boolean.TRUE.equals(needShowError)){
            ParamUtil.setRequestAttr(request,ValidationConstants.KEY_VALIDATION_ERRORS,dto.retrieveValidationResult());
        }
        //Check if it is the first time you have entered this method
        if (!StringUtils.hasLength(dto.getTaskId())){
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
            //
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
        ParamUtil.setSessionAttr(request, KEY_OFFICER_PROCESS_DATA, dto);
    }

    public void doValidate(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        String doRemark = ParamUtil.getRequestString(request,PARAM_DO_REMARKS);
        String doReason = ParamUtil.getRequestString(request,PARAM_DO_REASON);
        String doDecision = ParamUtil.getRequestString(request,PARAM_DO_DECISION);

        OfficerProcessAuditDto dto = getProcessDto(request);
        dto.setDoRemarks(doRemark);
        dto.setDoReason(doReason);
        dto.setDoDecision(doDecision);
        dto.setModule("doProcessAuditDt");
        doValidateData(dto,request);
        ParamUtil.setSessionAttr(request, KEY_OFFICER_PROCESS_DATA, dto);
    }

    public void aoValidate(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        String aoRemark = ParamUtil.getRequestString(request,PARAM_AO_REMARKS);
        String aoReason = ParamUtil.getRequestString(request,PARAM_AO_REASON);
        String aoDecision = ParamUtil.getRequestString(request,PARAM_AO_DECISION);

        OfficerProcessAuditDto dto = getProcessDto(request);
        dto.setAoRemarks(aoRemark);
        dto.setAoReason(aoReason);
        dto.setAoDecision(aoDecision);
        dto.setModule("aoProcessAuditDt");
        aoValidateData(dto,request);
        ParamUtil.setSessionAttr(request, KEY_OFFICER_PROCESS_DATA, dto);
    }

    /**
     * MohDOCheckAuditDt
     */
    public void doVerifiedAuditDate(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        OfficerProcessAuditDto dto = getProcessDto(request);
        dto.setAuditAppStatus(PARAM_AUDIT_STATUS_PENDING_AO);
        // TODO: check this app status
        dto.setAppStatus(APP_STATUS_PEND_AO_APPROVAL);
        auditClientBE.officerProcessAuditDt(dto);
    }

    /**
     * MohDOCheckAuditDt
     */
    public void doRejectAuditDate(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        OfficerProcessAuditDto dto = getProcessDto(request);
        dto.setAuditAppStatus(PARAM_AUDIT_STATUS_PENDING_AO);
        // TODO: check this app status
        dto.setAppStatus(APP_STATUS_PEND_AO_APPROVAL);
        auditClientBE.officerProcessAuditDt(dto);
    }

    /**
     * MohAOCheckAuditDt
     */
    public void aoApprovalAuditDate(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        OfficerProcessAuditDto dto = getProcessDto(request);
        dto.setAuditAppStatus(PARAM_AUDIT_STATUS_COMPLETED);
        dto.setAuditStatus(PARAM_AUDIT_STATUS_PENDING_APPLICANT_INPUT);
        // TODO: check this app status
        dto.setAppStatus(APP_STATUS_PEND_APPLICANT_INPUT);
        auditClientBE.officerProcessAuditDt(dto);
    }

    /**
     * MohAOCheckAuditDt
     */
    public void aoRejectAuditDate(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        OfficerProcessAuditDto dto = getProcessDto(request);
        dto.setAuditAppStatus(PARAM_AUDIT_STATUS_CANCELLED);
        dto.setAuditStatus(PARAM_AUDIT_STATUS_PENDING_APPLICANT_INPUT);
        // TODO: check this app status
        dto.setAppStatus(APP_STATUS_PEND_APPLICANT_INPUT);
        auditClientBE.officerProcessAuditDt(dto);
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
            if (dto.getDoDecision().equals("MOHPRO003")){
                actionType = ACTION_TYPE_REJECT;
            } else if (dto.getDoDecision().equals("MOHPRO010")){
                actionType = ACTION_TYPE_APPROVE;
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
            if (dto.getAoDecision().equals("MOHPRO003")){
                actionType = ACTION_TYPE_REJECT;
            } else if (dto.getAoDecision().equals("MOHPRO007")){
                actionType = ACTION_TYPE_APPROVE;
            }
        }
        ParamUtil.setRequestAttr(request, ACTION_TYPE, actionType);
    }
}

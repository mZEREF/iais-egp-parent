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
import sg.gov.moh.iais.egp.bsb.dto.audit.OfficerProcessAuditDto;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;

import static sg.gov.moh.iais.egp.bsb.constant.AuditConstants.*;
import static sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants.APP_STATUS_PEND_AO;
import static sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants.APP_STATUS_PEND_INPUT;

/**
 * @author Zhu Tangtang
 */
@Slf4j
@Delegator(value = "auditDateDelegator")
public class AuditDateDelegatorBE {

    private final AuditClientBE auditClientBE;

    @Autowired
    public AuditDateDelegatorBE(AuditClientBE auditClientBE){
        this.auditClientBE = auditClientBE;
    }

    public void start(BaseProcessClass bpc) throws IllegalAccessException {
        AuditTrailHelper.auditFunction(MODULE_AUDIT, FUNCTION_AUDIT);
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

    public void preConfirm(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        String doRemark = ParamUtil.getRequestString(request,PARAM_DO_REMARKS);
        String doReason = ParamUtil.getRequestString(request,PARAM_DO_REASON);
        String doDecision = ParamUtil.getRequestString(request,PARAM_DO_DECISION);
        String aoRemark = ParamUtil.getRequestString(request,PARAM_AO_REMARKS);
        String aoReason = ParamUtil.getRequestString(request,PARAM_AO_REASON);
        String aoDecision = ParamUtil.getRequestString(request,PARAM_AO_DECISION);
        String moduleType = ParamUtil.getRequestString(request,PARAM_MODULE_TYPE);

        OfficerProcessAuditDto dto = getProcessDto(request);
        if (!StringUtils.hasLength(dto.getDoDecision())){
            dto.setDoRemarks(doRemark);
            dto.setDoReason(doReason);
            dto.setDoDecision(doDecision);
        }
        dto.setAoRemarks(aoRemark);
        dto.setAoReason(aoReason);
        dto.setAoDecision(aoDecision);
        dto.setModule(moduleType);
        doValidation(dto,request);
        ParamUtil.setSessionAttr(request, KEY_OFFICER_PROCESS_DATA, dto);
    }

    /**
     * MohDOCheckAuditDt
     */
    public void doVerifiedAuditDate(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        OfficerProcessAuditDto dto = getProcessDto(request);
        dto.setAuditAppStatus(PARAM_AUDIT_STATUS_PENDING_AO);
        dto.setAppStatus(APP_STATUS_PEND_AO);
        auditClientBE.officerProcessAuditDt(dto);
    }

    /**
     * MohDOCheckAuditDt
     */
    public void doRejectAuditDate(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        OfficerProcessAuditDto dto = getProcessDto(request);
        dto.setAuditAppStatus(PARAM_AUDIT_STATUS_PENDING_AO);
        dto.setAppStatus(APP_STATUS_PEND_AO);
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
        dto.setAppStatus(APP_STATUS_PEND_INPUT);
        //
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
        dto.setAppStatus(APP_STATUS_PEND_INPUT);
        auditClientBE.officerProcessAuditDt(dto);
    }

    private OfficerProcessAuditDto getProcessDto(HttpServletRequest request) {
        OfficerProcessAuditDto searchDto = (OfficerProcessAuditDto) ParamUtil.getSessionAttr(request, KEY_OFFICER_PROCESS_DATA);
        return searchDto == null ? getDefaultProcessDto() : searchDto;
    }

    private OfficerProcessAuditDto getDefaultProcessDto() {
        return new OfficerProcessAuditDto();
    }

    /**
     * just a method to do simple valid,maybe update in the future
     * doValidation
     * */
    private void doValidation(OfficerProcessAuditDto dto, HttpServletRequest request){
        if(dto.doValidation()){
            ParamUtil.setRequestAttr(request, ValidationConstants.IS_VALID,ValidationConstants.YES);
        }else{
            ParamUtil.setRequestAttr(request, ValidationConstants.IS_VALID,ValidationConstants.NO);
            ParamUtil.setRequestAttr(request,ValidationConstants.KEY_SHOW_ERROR_SWITCH,Boolean.TRUE);
        }
    }
}

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
import sg.gov.moh.iais.egp.bsb.client.AuditClientBE;
import sg.gov.moh.iais.egp.bsb.constant.AuditConstants;
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
    }

    /**
     * AutoStep: prepareData
     * MohDOCheckAuditDt
     * MohAOCheckAuditDt
     */
    public void prepareDOAndAOReviewData(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        ParamUtil.setSessionAttr(request, FACILITY_AUDIT_APP, null);
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
            OfficerProcessAuditDto dto = responseDto.getEntity();
            dto.setTaskId(taskId);
            ParamUtil.setSessionAttr(request, KEY_OFFICER_PROCESS_DATA, dto);
        } else {
            log.warn("get audit API doesn't return ok, the response is {}", responseDto);
            ParamUtil.setRequestAttr(request, KEY_OFFICER_PROCESS_DATA, new OfficerProcessAuditDto());
        }
    }

    /**
     * MohDOCheckAuditDt
     */
    public void doVerifiedAuditDate(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        OfficerProcessAuditDto dto = (OfficerProcessAuditDto)ParamUtil.getSessionAttr(request,KEY_OFFICER_PROCESS_DATA);
        String remark = ParamUtil.getRequestString(request,PARAM_REMARKS);
        String decision = ParamUtil.getRequestString(request,PARAM_DECISION);
        //
        dto.setDoRemarks(remark);
        dto.setDoDecision(decision);
        dto.setAuditAppStatus(PARAM_AUDIT_STATUS_PENDING_AO);
        dto.setAppStatus(APP_STATUS_PEND_AO);
        auditClientBE.officerProcessAuditDt(dto);
    }

    /**
     * MohDOCheckAuditDt
     */
    public void doRejectAuditDate(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        OfficerProcessAuditDto dto = (OfficerProcessAuditDto)ParamUtil.getSessionAttr(request,KEY_OFFICER_PROCESS_DATA);
        String remark = ParamUtil.getRequestString(request,PARAM_REMARKS);
        String reason = ParamUtil.getRequestString(request,PARAM_REASON);
        String decision = ParamUtil.getRequestString(request,PARAM_DECISION);
        //
        dto.setDoRemarks(remark);
        dto.setDoReason(reason);
        dto.setDoDecision(decision);
        dto.setAuditAppStatus(PARAM_AUDIT_STATUS_PENDING_AO);
        dto.setAppStatus(APP_STATUS_PEND_AO);
        auditClientBE.officerProcessAuditDt(dto);
    }

    /**
     * MohAOCheckAuditDt
     */
    public void aoApprovalAuditDate(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        OfficerProcessAuditDto dto = (OfficerProcessAuditDto)ParamUtil.getSessionAttr(request,KEY_OFFICER_PROCESS_DATA);
        String remark = ParamUtil.getRequestString(request,PARAM_REMARKS);
        //
        dto.setAoRemarks(remark);
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
        OfficerProcessAuditDto dto = (OfficerProcessAuditDto)ParamUtil.getSessionAttr(request,KEY_OFFICER_PROCESS_DATA);
        String remark = ParamUtil.getRequestString(request,PARAM_REMARKS);
        String reason = ParamUtil.getRequestString(request,PARAM_REASON);
        //
        dto.setAoRemarks(remark);
        dto.setAoReason(reason);
        dto.setAuditAppStatus(PARAM_AUDIT_STATUS_CANCELLED);
        dto.setAuditStatus(PARAM_AUDIT_STATUS_PENDING_APPLICANT_INPUT);
        dto.setAppStatus(APP_STATUS_PEND_INPUT);
        auditClientBE.officerProcessAuditDt(dto);
    }
}

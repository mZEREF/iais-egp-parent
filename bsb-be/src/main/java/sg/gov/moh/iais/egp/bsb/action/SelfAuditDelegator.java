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
import sg.gov.moh.iais.egp.bsb.client.AuditClientBE;
import sg.gov.moh.iais.egp.bsb.constant.AuditConstants;
import sg.gov.moh.iais.egp.bsb.dto.ResponseDto;
import sg.gov.moh.iais.egp.bsb.dto.audit.OfficerProcessAuditDto;
import sg.gov.moh.iais.egp.bsb.entity.*;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;

import static sg.gov.moh.iais.egp.bsb.constant.AuditConstants.*;
import static sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants.*;

/**
 * @author Zhu Tangtang
 */
@Slf4j
@Delegator(value = "selfAuditDelegator")
public class SelfAuditDelegator {

    @Autowired
    private AuditClientBE auditClientBE;

    public void start(BaseProcessClass bpc) throws IllegalAccessException {
        AuditTrailHelper.auditFunction(MODULE_AUDIT, FUNCTION_AUDIT);
        HttpServletRequest request = bpc.request;
        IaisEGPHelper.clearSessionAttr(request, AuditConstants.class);
        ParamUtil.setSessionAttr(request, PARAM_AUDIT_SEARCH, null);
    }

    public void prepareProcessSelfAuditData(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
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

    public void doVerified(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        String decision = ParamUtil.getRequestString(request,PARAM_DECISION);
        OfficerProcessAuditDto dto = bindParam(request);
        dto.setProcessDecision(decision);
        dto.setDoDecision(decision);
        dto.setAuditAppStatus(PARAM_AUDIT_STATUS_PENDING_AO);
        dto.setAppStatus(APP_STATUS_PEND_AO);
        auditClientBE.officerProcessSelfAudit(dto);
    }

    public void doRequestForInformation(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        String decision = ParamUtil.getRequestString(request,PARAM_DECISION);
        OfficerProcessAuditDto dto = bindParam(request);
        dto.setProcessDecision(decision);
        dto.setDoDecision(decision);
        dto.setAuditStatus(PARAM_AUDIT_STATUS_PENDING_APPLICANT_INPUT);
        dto.setAuditAppStatus(PARAM_AUDIT_STATUS_PENDING_APPLICANT_INPUT);
        dto.setAppStatus(APP_STATUS_PEND_INPUT);
        auditClientBE.officerProcessSelfAudit(dto);
    }

    public void doReject(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        String decision = ParamUtil.getRequestString(request,PARAM_DECISION);
        OfficerProcessAuditDto dto = bindParam(request);
        dto.setProcessDecision(decision);
        dto.setDoDecision(decision);
        dto.setAuditAppStatus(PARAM_AUDIT_STATUS_PENDING_AO);
        dto.setAppStatus(APP_STATUS_PEND_AO);
        auditClientBE.officerProcessSelfAudit(dto);
    }

    public void aoInternalClarifications(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        String decision = ParamUtil.getRequestString(request,PARAM_DECISION);
        OfficerProcessAuditDto dto = bindParam(request);
        dto.setProcessDecision(decision);
        dto.setAuditStatus(PARAM_AUDIT_STATUS_PENDING_DO);
        dto.setAuditAppStatus(PARAM_AUDIT_STATUS_PENDING_DO);
        dto.setAppStatus(APP_STATUS_PEND_DO);
        auditClientBE.officerProcessSelfAudit(dto);
    }

    public void aoApproved(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        String decision = ParamUtil.getRequestString(request,PARAM_DECISION);
        OfficerProcessAuditDto dto = bindParam(request);
        dto.setProcessDecision(decision);
        dto.setAuditStatus(PARAM_AUDIT_STATUS_COMPLETED);
        dto.setAuditAppStatus(PARAM_AUDIT_STATUS_COMPLETED);
        dto.setAppStatus(APP_STATUS_APPROVED);
        auditClientBE.officerProcessSelfAudit(dto);
    }

    private OfficerProcessAuditDto bindParam(HttpServletRequest request){
        OfficerProcessAuditDto dto = (OfficerProcessAuditDto)ParamUtil.getSessionAttr(request, KEY_OFFICER_PROCESS_DATA);
        LoginContext loginContext = (LoginContext)ParamUtil.getSessionAttr(request, AppConsts.SESSION_ATTR_LOGIN_USER);
        String auditOutCome = ParamUtil.getRequestString(request,AUDIT_OUTCOME);
        String remark = ParamUtil.getRequestString(request,PARAM_REMARKS);
        String aoRemark = ParamUtil.getRequestString(request,AO_REMARKS);
        String finalRemark = ParamUtil.getRequestString(request,FINAL_REMARK);//on or null

        dto.setAuditOutCome(auditOutCome);
        dto.setDoRemarks(remark);
        dto.setAoRemarks(aoRemark);
        if (finalRemark==null) {
            dto.setFinalRemarks("No");
        }else {
            dto.setFinalRemarks("Yes");
        }
        dto.setActionBy(loginContext.getUserName());
        return dto;
    }
}

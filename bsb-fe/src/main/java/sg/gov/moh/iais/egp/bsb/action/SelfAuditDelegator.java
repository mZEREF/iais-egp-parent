package sg.gov.moh.iais.egp.bsb.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.dto.LoginContext;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sg.gov.moh.iais.egp.bsb.client.AuditClient;
import sg.gov.moh.iais.egp.bsb.client.DocClient;
import sg.gov.moh.iais.egp.bsb.constant.AuditConstants;
import sg.gov.moh.iais.egp.bsb.dto.audit.AuditDocDto;
import sg.gov.moh.iais.egp.bsb.entity.*;
import sg.gov.moh.iais.egp.bsb.util.TableDisplayUtil;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Zhu Tangtang
 */
@Slf4j
@Delegator(value = "selfAuditDelegator")
public class SelfAuditDelegator {

    @Autowired
    private AuditClient auditClient;

    @Autowired
    private DocClient docClient;

    /**
     * StartStep: startStep
     *
     * @param bpc
     * @throws IllegalAccessException
     */
    public void start(BaseProcessClass bpc) throws IllegalAccessException {
        AuditTrailHelper.auditFunction(AuditConstants.MODULE_AUDIT, AuditConstants.FUNCTION_AUDIT);
        HttpServletRequest request = bpc.request;
        IaisEGPHelper.clearSessionAttr(request, AuditConstants.class);
        ParamUtil.setSessionAttr(request, AuditConstants.PARAM_AUDIT_SEARCH, null);
    }

    /**
     * AutoStep: prepareData
     *
     * @param bpc
     */
    public void prepareFacilitySelfAuditData(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        ParamUtil.setSessionAttr(request,AuditConstants.FACILITY,null);
        ParamUtil.setSessionAttr(request,AuditConstants.AUDIT_DOC_DTO, null);

        String auditId = ParamUtil.getMaskedString(request, AuditConstants.AUDIT_ID);
        FacilityAudit facilityAudit = auditClient.getFacilityAuditById(auditId).getEntity();

        Facility facility = facilityAudit.getFacility();
        String facilityAddress = getFacAddress(facility);
        facility.setFacilityAddress(facilityAddress);

        List<FacilityDoc> facilityDocList = docClient.getFacilityDocByFacId(facility.getId()).getEntity();
        List<FacilityDoc> docList = new ArrayList<>();
        for (FacilityDoc facilityDoc : facilityDocList) {
            //todo You can only get the current user name
            String submitByName = IaisEGPHelper.getCurrentAuditTrailDto().getMohUserId();
            facilityDoc.setSubmitByName(submitByName);
            docList.add(facilityDoc);
        }
        AuditDocDto auditDocDto = new AuditDocDto();
        auditDocDto.setFacilityDocs(docList);

        ParamUtil.setSessionAttr(request,AuditConstants.FACILITY,facility);
        ParamUtil.setRequestAttr(request, AuditConstants.FACILITY_AUDIT, facilityAudit);
        ParamUtil.setSessionAttr(request,AuditConstants.AUDIT_DOC_DTO, auditDocDto);
    }

    /**
     * AutoStep: submit
     *
     * @param bpc
     */
    public void submitSelfAuditReport(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        String auditId = ParamUtil.getMaskedString(request, AuditConstants.AUDIT_ID);
        String scenarioCategory = ParamUtil.getRequestString(request,AuditConstants.PARAM_SCENARIO_CATEGORY);
        FacilityAudit audit = new FacilityAudit();
        audit.setScenarioCategory(scenarioCategory);
        audit.setId(auditId);
        audit.setStatus(AuditConstants.PARAM_AUDIT_STATUS_PENDING_DO);
        FacilityAuditApp facilityAuditApp = auditClient.saveSelfAuditReport(audit).getEntity();
    }

    private FacilityAuditApp before(HttpServletRequest request){
        String auditAppId = ParamUtil.getMaskedString(request,AuditConstants.AUDIT_APP_ID);
        String auditOutCome = ParamUtil.getRequestString(request,AuditConstants.AUDIT_OUTCOME);
        String remark = ParamUtil.getRequestString(request,AuditConstants.PARAM_REMARKS);
        String aoRemark = ParamUtil.getRequestString(request,AuditConstants.AO_REMARKS);
        String finalRemark = ParamUtil.getRequestString(request,AuditConstants.FINAL_REMARK);//on or null
        String decision = ParamUtil.getRequestString(request,AuditConstants.PARAM_DECISION);

        FacilityAudit audit = new FacilityAudit();
        FacilityAuditApp auditApp = new FacilityAuditApp();
        auditApp.setId(auditAppId);
        auditApp.setDoRemarks(remark);
        auditApp.setAoRemarks(aoRemark);
        auditApp.setDoDecision(decision);
        audit.setAuditOutcome(auditOutCome);
        audit.setRemarks(aoRemark);
        if (finalRemark==null) {
            audit.setFinalRemarks("No");
        }else {
            audit.setFinalRemarks("Yes");
        }
        auditApp.setFacilityAudit(audit);
        return auditApp;
    }

    private FacilityAuditAppHistory abHistory(HttpServletRequest request){
        FacilityAuditApp auditApp = before(request);
        LoginContext loginContext = (LoginContext)ParamUtil.getSessionAttr(request, AppConsts.SESSION_ATTR_LOGIN_USER);
        FacilityAuditAppHistory auditAppHistory = new FacilityAuditAppHistory();
        auditAppHistory.setActionBy(loginContext.getUserName());
        auditAppHistory.setProcessDecision(auditApp.getDoDecision());
        auditAppHistory.setAuditAppId(auditApp.getId());
        if (StringUtil.isNotEmpty(auditApp.getDoRemarks())) {
            auditAppHistory.setInternalRemarks(auditApp.getDoRemarks());
        }
        if (StringUtil.isNotEmpty(auditApp.getAoRemarks())) {
            auditAppHistory.setInternalRemarks(auditApp.getAoRemarks());
        }

        return auditAppHistory;
    }

    private String getFacAddress(Facility facility){
        return TableDisplayUtil.getOneLineAddress(facility.getBlkNo(),facility.getStreetName(),
                facility.getFloorNo(),facility.getUnitNo(),facility.getPostalCode());
    }
}

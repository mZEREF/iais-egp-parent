package sg.gov.moh.iais.egp.bsb.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import sg.gov.moh.iais.egp.bsb.client.AuditClientBE;
import sg.gov.moh.iais.egp.bsb.constant.AuditConstants;
import sg.gov.moh.iais.egp.bsb.constant.RevocationConstants;
import sg.gov.moh.iais.egp.bsb.entity.*;
import sg.gov.moh.iais.egp.bsb.util.TableDisplayUtil;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

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
     * MohDOCheckAuditDt
     * MohAOCheckAuditDt
     * @param bpc
     */
    public void prepareDOAndAOReviewData(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        ParamUtil.setSessionAttr(request, AuditConstants.FACILITY_AUDIT_APP, null);

        String auditAppId = "8DD7CCC0-52A2-4204-A0BA-F7A99CB18980";
        FacilityAuditApp facilityAuditApp = auditClientBE.getFacilityAuditAppById(auditAppId).getEntity();
        Approval approval = facilityAuditApp.getFacilityAudit().getApproval();
        Facility facility = new Facility();
        if (approval.getProcessType().equals(RevocationConstants.PARAM_PROCESS_TYPE_FACILITY_REGISTRATION)) {
            //join with activity
            List<FacilityActivity> activities = approval.getFacilityActivities();
            if (!activities.isEmpty()) {
                facility = activities.get(0).getFacility();
            }
        } else if (approval.getProcessType().equals(RevocationConstants.PARAM_PROCESS_TYPE_AFC_REGISTRATION)) {
            //join with bsb_facility_certifier_reg
        } else {
            //join with BA/T
            List<FacilityBiologicalAgent> agents = approval.getFacilityBiologicalAgents();
            if (!agents.isEmpty()) {
                facility = agents.get(0).getFacility();
            }
        }
        if (!StringUtils.isEmpty(facility)) {
            String address = TableDisplayUtil.getOneLineAddress(facility.getBlkNo(), facility.getStreetName(), facility.getFloorNo(),
                    facility.getUnitNo(), facility.getPostalCode());
            facility.setFacilityAddress(address);
        }
        ParamUtil.setRequestAttr(request,AuditConstants.FACILITY,facility);

        ParamUtil.setSessionAttr(request, AuditConstants.FACILITY_AUDIT_APP, facilityAuditApp);
    }

    /**
     * MohDOCheckAuditDt
     * @param bpc
     */
    public void doVerifiedAuditDate(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        FacilityAuditApp facilityAuditApp = (FacilityAuditApp)ParamUtil.getSessionAttr(request,AuditConstants.FACILITY_AUDIT_APP);
        String remark = ParamUtil.getRequestString(request,AuditConstants.PARAM_REMARKS);
        String decision = ParamUtil.getRequestString(request,AuditConstants.PARAM_DECISION);
        //
        facilityAuditApp.setDoRemarks(remark);
        facilityAuditApp.setDoDecision(decision);
        facilityAuditApp.setStatus(AuditConstants.PARAM_AUDIT_STATUS_PENDING_AO);
        auditClientBE.processAuditDate(facilityAuditApp);
    }

    /**
     * MohDOCheckAuditDt
     * @param bpc
     */
    public void doRejectAuditDate(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        FacilityAuditApp facilityAuditApp = (FacilityAuditApp)ParamUtil.getSessionAttr(request,AuditConstants.FACILITY_AUDIT_APP);
        String remark = ParamUtil.getRequestString(request,AuditConstants.PARAM_REMARKS);
        String reason = ParamUtil.getRequestString(request,AuditConstants.PARAM_REASON);
        String decision = ParamUtil.getRequestString(request,AuditConstants.PARAM_DECISION);
        //
        facilityAuditApp.setDoRemarks(remark);
        facilityAuditApp.setDoDecision(decision);
        facilityAuditApp.setDoReason(reason);
        facilityAuditApp.setStatus(AuditConstants.PARAM_AUDIT_STATUS_PENDING_AO);
        auditClientBE.processAuditDate(facilityAuditApp);
    }

    /**
     * MohAOCheckAuditDt
     * @param bpc
     */
    public void aoApprovalAuditDate(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        FacilityAuditApp facilityAuditApp = (FacilityAuditApp)ParamUtil.getSessionAttr(request,AuditConstants.FACILITY_AUDIT_APP);
        String remark = ParamUtil.getRequestString(request,AuditConstants.PARAM_REMARKS);
        //
        facilityAuditApp.setAoRemarks(remark);
        facilityAuditApp.setStatus(AuditConstants.PARAM_AUDIT_STATUS_COMPLETED);
        //
        facilityAuditApp.getFacilityAudit().setStatus(AuditConstants.PARAM_AUDIT_STATUS_PENDING_APPLICANT_INPUT);
        facilityAuditApp.getFacilityAudit().setAuditDt(facilityAuditApp.getRequestAuditDt());
        auditClientBE.processAuditDate(facilityAuditApp);
    }

    /**
     * MohAOCheckAuditDt
     * @param bpc
     */
    public void aoRejectAuditDate(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        FacilityAuditApp facilityAuditApp = (FacilityAuditApp)ParamUtil.getSessionAttr(request,AuditConstants.FACILITY_AUDIT_APP);
        String remark = ParamUtil.getRequestString(request,AuditConstants.PARAM_REMARKS);
        String reason = ParamUtil.getRequestString(request,AuditConstants.PARAM_REASON);
        //
        facilityAuditApp.setAoRemarks(remark);
        facilityAuditApp.setAoReason(reason);
        facilityAuditApp.setStatus(AuditConstants.PARAM_AUDIT_STATUS_CANCELLED);
        auditClientBE.processAuditDate(facilityAuditApp);
    }
}

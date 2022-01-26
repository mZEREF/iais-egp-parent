package sg.gov.moh.iais.egp.bsb.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import lombok.extern.slf4j.Slf4j;
import sg.gov.moh.iais.egp.bsb.service.IncidentMOHProcessService;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;


import static sg.gov.moh.iais.egp.bsb.constant.module.ProcessContants.MODULE_NAME;

/**
 * @author YiMing
 * @version 2021/12/29 13:06
 **/
@Delegator(value = "incidentMOHProcessDelegator")
@Slf4j
public class IncidentMOHProcessDelegator {
    private static final String FUNCTION_NAME_DO = "DO Processing";
    private static final String FUNCTION_NAME_AO = "AO Processing";
    private static final String FUNCTION_NAME_HM = "HM Processing";
    private static final String PARAM_PROCESS_KEY = "key";
    private static final String PARAM_MODULE_KEY = "module";

    private final IncidentMOHProcessService mohProcessService;

    public IncidentMOHProcessDelegator(IncidentMOHProcessService mohProcessService) {
        this.mohProcessService = mohProcessService;
    }

    public void startDO(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        mohProcessService.clearSession(request);
        ParamUtil.setSessionAttr(request,PARAM_PROCESS_KEY,"DO");
        AuditTrailHelper.auditFunction(MODULE_NAME, FUNCTION_NAME_DO);
    }

    public void startAO(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        mohProcessService.clearSession(request);
        ParamUtil.setSessionAttr(request,PARAM_PROCESS_KEY,"AO");
        AuditTrailHelper.auditFunction(MODULE_NAME, FUNCTION_NAME_AO);
    }

    public void startHM(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        mohProcessService.clearSession(request);
        ParamUtil.setSessionAttr(request,PARAM_PROCESS_KEY,"HM");
        AuditTrailHelper.auditFunction(MODULE_NAME, FUNCTION_NAME_HM);
    }


    public void preIncidentProcessingData(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        ParamUtil.setRequestAttr(request,PARAM_MODULE_KEY,"notification");
        mohProcessService.preIncidentProcessingData(request);
    }

    public void preInvestProcessingData(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        ParamUtil.setRequestAttr(request,PARAM_MODULE_KEY,"investigation");
        mohProcessService.preInvestProcessingData(request);
    }

    public void preFollowup1AProcessingData(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        ParamUtil.setRequestAttr(request,PARAM_MODULE_KEY,"followup1A");
        mohProcessService.preInvestProcessingData(request);
    }

    public void preFollowup1BProcessingData(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        ParamUtil.setRequestAttr(request,PARAM_MODULE_KEY,"followup1B");
        mohProcessService.preInvestProcessingData(request);
    }



    public void preViewNotification(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        mohProcessService.preViewIncidentNotification(request);
    }

    public void preViewInvestReport(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        mohProcessService.preViewInvestReport(request);
    }

    public void handleProcessing(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        mohProcessService.handleProcessing(request);
    }

    public void doDOProcessing(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        mohProcessService.doDOProcessing(request);
    }

    public void doAOProcessing(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        mohProcessService.doAOProcessing(request);
    }

    public void doHMProcessing(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        mohProcessService.doHMProcessing(request);
    }

    public void doDOInvestProcessing(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        mohProcessService.doDOInvestProcessing(request);
    }

    public void doAOInvestProcessing(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        mohProcessService.doAOInvestProcessing(request);
    }

    public void doHMInvestProcessing(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        mohProcessService.doHMInvestProcessing(request);
    }


}

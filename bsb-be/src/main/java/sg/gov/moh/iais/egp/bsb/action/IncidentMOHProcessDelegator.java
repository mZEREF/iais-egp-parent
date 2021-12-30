package sg.gov.moh.iais.egp.bsb.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import io.jsonwebtoken.lang.Assert;
import lombok.extern.slf4j.Slf4j;
import sg.gov.moh.iais.egp.bsb.client.IncidentProcessClient;
import sg.gov.moh.iais.egp.bsb.constant.IncidentProcessConstants;
import sg.gov.moh.iais.egp.bsb.dto.incident.IncidentNotificationDto;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;

import static sg.gov.moh.iais.egp.bsb.constant.ProcessContants.MODULE_NAME;

/**
 * @author YiMing
 * @version 2021/12/29 13:06
 **/
@Delegator(value = "incidentMOHProcessDelegator")
@Slf4j
public class IncidentMOHProcessDelegator {
    private static final String FUNCTION_NAME = "DO Processing";
    private static final String PARAM_INCIDENT_PROCESS_DTO = "processDto";
    private final IncidentProcessClient incidentProcessClient;

    public IncidentMOHProcessDelegator(IncidentProcessClient incidentProcessClient) {
        this.incidentProcessClient = incidentProcessClient;
    }


    public void start(BaseProcessClass bpc) {
        AuditTrailHelper.auditFunction(MODULE_NAME, FUNCTION_NAME);
    }

    public void preScreeningData(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        ParamUtil.setRequestAttr(request,PARAM_INCIDENT_PROCESS_DTO,getIncidentNotificationDto(request));
    }

    public void handleScreening(BaseProcessClass bpc) {

    }

    public void doDOProcessing(BaseProcessClass bpc) {

    }

    public void preAOProcessingData(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        ParamUtil.setRequestAttr(request,PARAM_INCIDENT_PROCESS_DTO,getIncidentNotificationDto(request));
    }

    public void handleAOProcessing(BaseProcessClass bpc) {

    }

    public void doAOProcessing(BaseProcessClass bpc) {

    }

    public void preHMProcessingData(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        ParamUtil.setRequestAttr(request,PARAM_INCIDENT_PROCESS_DTO,getIncidentNotificationDto(request));
    }

    public void handleHMProcessing(BaseProcessClass bpc) {

    }

    public void doHMProcessing(BaseProcessClass bpc) {

    }

    public IncidentNotificationDto getIncidentNotificationDto(HttpServletRequest request){
        String appId = ParamUtil.getString(request, IncidentProcessConstants.KEY_APP_ID);
        Assert.hasLength(appId,"application id is null");
        return incidentProcessClient.getIncidentNotificationByAppId(appId).getEntity();
    }
}

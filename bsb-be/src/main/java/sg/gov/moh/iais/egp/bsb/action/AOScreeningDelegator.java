package sg.gov.moh.iais.egp.bsb.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.AuditTrailConsts;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sg.gov.moh.iais.egp.bsb.client.ProcessClient;
import sg.gov.moh.iais.egp.bsb.client.RevocationClient;
import sg.gov.moh.iais.egp.bsb.constant.ProcessContants;
import sg.gov.moh.iais.egp.bsb.entity.*;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * @author : LiRan
 * @date : 2021/8/20
 */
@Delegator(value = "aoScreeningDelegator")
@Slf4j
public class AOScreeningDelegator {
    private final ProcessClient processClient;

    private final RevocationClient revocationClient;

    @Autowired
    public AOScreeningDelegator(ProcessClient processClient, RevocationClient revocationClient) {
        this.processClient = processClient;
        this.revocationClient = revocationClient;
    }

    public void start(BaseProcessClass bpc) throws IllegalAccessException {
        AuditTrailHelper.auditFunction(AuditTrailConsts.MODULE_SYSTEM_CONFIG,
                AuditTrailConsts.FUNCTION_ERROR_MESSAGES_MANAGEMENT);
        HttpServletRequest request = bpc.request;
        IaisEGPHelper.clearSessionAttr(request, ProcessContants.class);
    }

    public void prepareData(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        Application application = processClient.getApplicationById("A0C45E68-F506-EC11-BE6E-000C298D317C").getEntity();
        List<FacilitySchedule> facilityScheduleList = application.getFacility().getFacilitySchedules();
        List<Biological> biologicalList = new ArrayList<>();
        if (facilityScheduleList != null && facilityScheduleList.size() > 0){
            for (int i = 0; i < facilityScheduleList.size(); i++) {
                List<FacilityBiologicalAgent> facilityBiologicalAgentList = facilityScheduleList.get(i).getFacilityBiologicalAgents();
                if (facilityBiologicalAgentList != null && facilityBiologicalAgentList.size() > 0){
                    for (int j = 0; j < facilityBiologicalAgentList.size(); j++) {
                        String biologicalId = facilityBiologicalAgentList.get(j).getBiologicalId();
                        biologicalList.add(processClient.getBiologicalById(biologicalId).getEntity());
                    }
                }
            }
        }
        application.setBiologicalList(biologicalList);
        List<RoutingHistory> historyDtoList = revocationClient.getAllHistory().getEntity();
        ParamUtil.setRequestAttr(request, ProcessContants.PARAM_PROCESSING_HISTORY,historyDtoList);
        ParamUtil.setSessionAttr(request, ProcessContants.APPLICATION_INFO_ATTR, application);
    }

    public void approvalForInspection(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        String status = "BSBAPST002";
    }

    public void reject(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        String status = "BSBAPST002";
    }

    public void routeBackToDO(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        String status = "BSBAPST002";
    }

    public void routeToHM(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        String status = "BSBAPST002";
    }
}

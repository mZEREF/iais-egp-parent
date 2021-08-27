package sg.gov.moh.iais.egp.bsb.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.AuditTrailConsts;
import com.ecquaria.cloud.moh.iais.common.utils.Formatter;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.dto.LoginContext;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sg.gov.moh.iais.egp.bsb.client.ProcessClient;
import sg.gov.moh.iais.egp.bsb.client.RevocationClient;
import sg.gov.moh.iais.egp.bsb.constant.ProcessContants;
import sg.gov.moh.iais.egp.bsb.dto.ResponseDto;
import sg.gov.moh.iais.egp.bsb.dto.process.DoScreeningDto;
import sg.gov.moh.iais.egp.bsb.entity.*;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author : LiRan
 * @date : 2021/8/20
 */
@Delegator(value = "DOScreeningDelegator")
@Slf4j
public class DOScreeningDelegator {
    private final ProcessClient processClient;

    private final RevocationClient revocationClient;

    @Autowired
    public DOScreeningDelegator(ProcessClient processClient,RevocationClient revocationClient) {
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
        Application application = processClient.getApplicationById("05EF1B40-E3E2-EB11-8B7D-000C293F0C88").getEntity();
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

    public void screenedByDO(BaseProcessClass bpc) throws ParseException{
        HttpServletRequest request = bpc.request;
        String status = "BSBAPST002";
        Application application = getApplicationByForm(bpc);
        application.setStatus(status);
        FeignResponseEntity<Application> applicationResult = revocationClient.saveApplication(application);

        DoScreeningDto doScreeningDtoByForm = getDoScreeningDtoByForm(bpc);
        ResponseDto<DoScreeningDto> doScreeningDtoResponseDto = processClient.updateFacilityByMohProcess(doScreeningDtoByForm);
        //TODO valid

        ApplicationMisc applicationMisc = getApplicationMiscByForm(bpc);
        application.setId(applicationResult.getEntity().getId());
        applicationMisc.setApplication(application);
        revocationClient.saveApplicationMisc(applicationMisc);

        RoutingHistory routingHistory = getRoutingHistoryByForm(bpc);
        routingHistory.setAppStatus(status);
        routingHistory.setApplicationNo(applicationResult.getEntity().getApplicationNo());
        revocationClient.saveHistory(routingHistory);
    }

    public void reject(BaseProcessClass bpc) throws ParseException{
        HttpServletRequest request = bpc.request;
        String status = "BSBAPST008";
        Application application = getApplicationByForm(bpc);
        application.setStatus(status);
        FeignResponseEntity<Application> applicationResult = revocationClient.saveApplication(application);

        DoScreeningDto doScreeningDtoByForm = getDoScreeningDtoByForm(bpc);
        ResponseDto<DoScreeningDto> doScreeningDtoResponseDto = processClient.updateFacilityByMohProcess(doScreeningDtoByForm);
        //TODO valid

        ApplicationMisc applicationMisc = getApplicationMiscByForm(bpc);
        application.setId(applicationResult.getEntity().getId());
        applicationMisc.setApplication(application);
        revocationClient.saveApplicationMisc(applicationMisc);

        RoutingHistory routingHistory = getRoutingHistoryByForm(bpc);
        routingHistory.setAppStatus(status);
        routingHistory.setApplicationNo(applicationResult.getEntity().getApplicationNo());
        revocationClient.saveHistory(routingHistory);
    }

    public void requestForInformation(BaseProcessClass bpc) throws ParseException{
        HttpServletRequest request = bpc.request;
        String status = "BSBAPST004";
        Application application = getApplicationByForm(bpc);
        application.setStatus(status);
        FeignResponseEntity<Application> applicationResult = revocationClient.saveApplication(application);

        DoScreeningDto doScreeningDtoByForm = getDoScreeningDtoByForm(bpc);
        ResponseDto<DoScreeningDto> doScreeningDtoResponseDto = processClient.updateFacilityByMohProcess(doScreeningDtoByForm);
        //TODO valid

        ApplicationMisc applicationMisc = getApplicationMiscByForm(bpc);
        application.setId(applicationResult.getEntity().getId());
        applicationMisc.setApplication(application);
        revocationClient.saveApplicationMisc(applicationMisc);

        RoutingHistory routingHistory = getRoutingHistoryByForm(bpc);
        routingHistory.setAppStatus(status);
        routingHistory.setApplicationNo(applicationResult.getEntity().getApplicationNo());
        revocationClient.saveHistory(routingHistory);
    }

    private Application getApplicationByForm(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        Application oldApplication = (Application)ParamUtil.getSessionAttr(request, ProcessContants.APPLICATION_INFO_ATTR);
        String facilityId = oldApplication.getFacility().getId();
        String appType = oldApplication.getAppType();
        String processType = oldApplication.getProcessType();
        Application newApplication = new Application();
        Facility facility = new Facility();
        facility.setId(facilityId);
        newApplication.setFacility(facility);
        newApplication.setAppType(appType);
        newApplication.setProcessType(processType);
        newApplication.setApplicationDt(new Date());
        newApplication.setApprovalDate(new Date());
        return newApplication;
    }

    private DoScreeningDto getDoScreeningDtoByForm(BaseProcessClass bpc) throws ParseException{
        HttpServletRequest request = bpc.request;
        Application oldApplication = (Application)ParamUtil.getSessionAttr(request, ProcessContants.APPLICATION_INFO_ATTR);
        String facilityId = oldApplication.getFacility().getId();
        DoScreeningDto doScreeningDto = new DoScreeningDto();
        String riskLevel = ParamUtil.getString(request, ProcessContants.RISK_LEVEL);
        String commentsOnRiskLevelAssessment = ParamUtil.getString(request, ProcessContants.COMMENTS_ON_RISK_LEVEL_ASSESSMENT);
        String erpReport = ParamUtil.getString(request, ProcessContants.ERP_REPORT);
        String redTeamingReport = ParamUtil.getString(request, ProcessContants.RED_TEAMING_REPORT);
        String lentivirusReport = ParamUtil.getString(request, ProcessContants.LENTIVIRUS_REPORT);
        String internalInspectionReport = ParamUtil.getString(request, ProcessContants.INTERNAL_INSPECTION_REPORT);
        String selectedApprovedFacilityCertifier = ParamUtil.getString(request, ProcessContants.SELECTED_APPROVED_FACILITY_CERTIFIER);
        String validityStartDate = ParamUtil.getString(request, ProcessContants.VALIDITY_START_DATE);
        String validityEndDate = ParamUtil.getString(request, ProcessContants.VALIDITY_END_DATE);
        doScreeningDto.setFacilityId(facilityId);
        doScreeningDto.setRiskLevel(riskLevel);
        doScreeningDto.setRiskLevelComments(commentsOnRiskLevelAssessment);
        doScreeningDto.setErpReportDt(Formatter.parseDate(erpReport));
        doScreeningDto.setRedTeamingReportDt(Formatter.parseDate(redTeamingReport));
        doScreeningDto.setLentivirusReportDt(Formatter.parseDate(lentivirusReport));
        doScreeningDto.setInternalInspectionReportDt(Formatter.parseDate(internalInspectionReport));
        doScreeningDto.setValidityStartDt(Formatter.parseDate(validityStartDate));
        doScreeningDto.setValidityEndDt(Formatter.parseDate(validityEndDate));
        doScreeningDto.setSelectedAfc(selectedApprovedFacilityCertifier);
        return doScreeningDto;
    }

    private ApplicationMisc getApplicationMiscByForm(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        String remarks = ParamUtil.getString(request, ProcessContants.REMARKS);
        ApplicationMisc applicationMisc = new ApplicationMisc();
        applicationMisc.setRemarks(remarks);
        applicationMisc.setReasonContent("null");
        return applicationMisc;
    }

    private RoutingHistory getRoutingHistoryByForm(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        LoginContext loginContext = (LoginContext)ParamUtil.getSessionAttr(request, AppConsts.SESSION_ATTR_LOGIN_USER);
        String processingDecision = ParamUtil.getString(request, ProcessContants.PROCESSING_DECISION);
        String remarks = ParamUtil.getString(request, ProcessContants.REMARKS);
        RoutingHistory routingHistory = new RoutingHistory();
        routingHistory.setInternalRemarks(remarks);
        routingHistory.setProcessDecision(processingDecision);
        routingHistory.setActionBy(loginContext.getUserName());
        return routingHistory;
    }
}

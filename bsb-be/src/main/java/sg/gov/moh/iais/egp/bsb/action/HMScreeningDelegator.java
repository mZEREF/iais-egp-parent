package sg.gov.moh.iais.egp.bsb.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.AuditTrailConsts;
import com.ecquaria.cloud.moh.iais.common.utils.Formatter;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.dto.LoginContext;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sg.gov.moh.iais.egp.bsb.client.ProcessClient;
import sg.gov.moh.iais.egp.bsb.constant.ProcessContants;
import sg.gov.moh.iais.egp.bsb.dto.process.DoScreeningDto;
import sg.gov.moh.iais.egp.bsb.entity.Application;
import sg.gov.moh.iais.egp.bsb.entity.Biological;
import sg.gov.moh.iais.egp.bsb.entity.FacilitySchedule;
import sg.gov.moh.iais.egp.bsb.entity.RoutingHistory;
import sg.gov.moh.iais.egp.bsb.util.JoinBiologicalName;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.util.List;

/**
 * @author : LiRan
 * @date : 2021/8/20
 */
@Delegator(value = "hmScreeningDelegator")
@Slf4j
public class HMScreeningDelegator {
    private final ProcessClient processClient;

    @Autowired
    public HMScreeningDelegator(ProcessClient processClient) {
        this.processClient = processClient;
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
        List<Biological> biologicalList = JoinBiologicalName.getBioListByFacilityScheduleList(facilityScheduleList,processClient);
        application.setBiologicalList(biologicalList);
        List<RoutingHistory> historyDtoList = processClient.getRoutingHistoriesByApplicationNo(application.getApplicationNo()).getEntity();
        ParamUtil.setRequestAttr(request, ProcessContants.PARAM_PROCESSING_HISTORY,historyDtoList);
        ParamUtil.setSessionAttr(request, ProcessContants.APPLICATION_INFO_ATTR, application);
    }

    public void reject(BaseProcessClass bpc) throws ParseException{
        HttpServletRequest request = bpc.request;
        DoScreeningDto doScreeningDtoByForm = getDtoByForm(bpc);
        String status = "BSBAPST008";
        doScreeningDtoByForm.setStatus(status);
        processClient.updateFacilityByMohProcess(doScreeningDtoByForm);
    }

    public void approve(BaseProcessClass bpc) throws ParseException{
        HttpServletRequest request = bpc.request;
        DoScreeningDto doScreeningDtoByForm = getDtoByForm(bpc);
        String status = "BSBAPST001";
        doScreeningDtoByForm.setStatus(status);
        processClient.updateFacilityByMohProcess(doScreeningDtoByForm);
    }

    private DoScreeningDto getDtoByForm(BaseProcessClass bpc) throws ParseException {
        HttpServletRequest request = bpc.request;
        Application application = (Application)ParamUtil.getSessionAttr(request, ProcessContants.APPLICATION_INFO_ATTR);
        //facility info
        String facilityId = application.getFacility().getId();
        String erpReport = ParamUtil.getString(request, ProcessContants.ERP_REPORT);
        String redTeamingReport = ParamUtil.getString(request, ProcessContants.RED_TEAMING_REPORT);
        String lentivirusReport = ParamUtil.getString(request, ProcessContants.LENTIVIRUS_REPORT);
        String internalInspectionReport = ParamUtil.getString(request, ProcessContants.INTERNAL_INSPECTION_REPORT);
        String selectedApprovedFacilityCertifier = ParamUtil.getString(request, ProcessContants.SELECTED_APPROVED_FACILITY_CERTIFIER);
        String validityStartDate = ParamUtil.getString(request, ProcessContants.VALIDITY_START_DATE);
        String validityEndDate = ParamUtil.getString(request, ProcessContants.VALIDITY_END_DATE);
        //application info
        String applicationNo = application.getApplicationNo();
        String applicationId = application.getId();
        //history info
        LoginContext loginContext = (LoginContext)ParamUtil.getSessionAttr(request, AppConsts.SESSION_ATTR_LOGIN_USER);
        String processingDecision = ParamUtil.getString(request, ProcessContants.PROCESSING_DECISION);
        String remarks = ParamUtil.getString(request, ProcessContants.REMARKS);
        String appStatus = application.getStatus();
        //set field value
        DoScreeningDto doScreeningDto = new DoScreeningDto();
        doScreeningDto.setFacilityId(facilityId);
        doScreeningDto.setRiskLevel(application.getFacility().getRiskLevel());
        doScreeningDto.setRiskLevelComments(application.getFacility().getRiskLevelComments());
        doScreeningDto.setErpReportDt(Formatter.parseDate(erpReport));
        doScreeningDto.setRedTeamingReportDt(Formatter.parseDate(redTeamingReport));
        doScreeningDto.setLentivirusReportDt(Formatter.parseDate(lentivirusReport));
        doScreeningDto.setInternalInspectionReportDt(Formatter.parseDate(internalInspectionReport));
        doScreeningDto.setValidityStartDt(Formatter.parseDate(validityStartDate));
        doScreeningDto.setValidityEndDt(Formatter.parseDate(validityEndDate));
        doScreeningDto.setSelectedAfc(selectedApprovedFacilityCertifier);
        doScreeningDto.setApplicationId(applicationId);
        doScreeningDto.setRemarks(remarks);
        doScreeningDto.setAppStatus(appStatus);
        doScreeningDto.setProcessDecision(processingDecision);
        doScreeningDto.setActionBy(loginContext.getUserName());
        doScreeningDto.setApplicationNo(applicationNo);
        return doScreeningDto;
    }
}

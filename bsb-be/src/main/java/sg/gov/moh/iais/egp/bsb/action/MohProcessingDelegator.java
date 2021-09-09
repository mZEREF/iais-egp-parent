package sg.gov.moh.iais.egp.bsb.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.AuditTrailConsts;
import com.ecquaria.cloud.moh.iais.common.utils.Formatter;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.validation.dto.ValidationResult;
import com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant;
import com.ecquaria.cloud.moh.iais.dto.LoginContext;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.helper.WebValidationHelper;
import lombok.extern.slf4j.Slf4j;
import sg.gov.moh.iais.egp.bsb.client.ProcessClient;
import sg.gov.moh.iais.egp.bsb.constant.ProcessContants;
import sg.gov.moh.iais.egp.bsb.dto.process.DoScreeningDto;
import sg.gov.moh.iais.egp.bsb.entity.*;
import sg.gov.moh.iais.egp.bsb.util.JoinBiologicalName;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author : LiRan
 * @date : 2021/8/20
 */
@Delegator
@Slf4j
public class MohProcessingDelegator {
    private final ProcessClient processClient;

    public MohProcessingDelegator(ProcessClient processClient) {
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
        String crudActionType = ParamUtil.getString(request,ProcessContants.CRUD_ACTION_TYPE);
        String appId = "";
        if (crudActionType == null || crudActionType == ""){
            appId = ParamUtil.getString(request,ProcessContants.PARAM_APP_ID);
        }else if (crudActionType.equals(ProcessContants.ACTION_TYPE_1)){
            appId = ParamUtil.getMaskedString(request, ProcessContants.PARAM_APP_ID);
        }
        Application application = processClient.getApplicationById(appId).getEntity();
        ApplicationMisc applicationMisc = new ApplicationMisc();
        if (application.getStatus().equals(ProcessContants.APPLICATION_STATUS_2)){
            applicationMisc = processClient.getApplicationMiscByApplicationIdAndAndReason(application.getId(), ProcessContants.APPLICATION_STATUS_1).getEntity();
        }else if (application.getStatus().equals(ProcessContants.APPLICATION_STATUS_3)){
            applicationMisc = processClient.getApplicationMiscByApplicationIdAndAndReason(application.getId(), ProcessContants.APPLICATION_STATUS_2).getEntity();
        }
        List<FacilitySchedule> facilityScheduleList = application.getFacility().getFacilitySchedules();
        List<Biological> biologicalList = JoinBiologicalName.getBioListByFacilityScheduleList(facilityScheduleList,processClient);
        application.setBiologicalList(biologicalList);
        List<RoutingHistory> historyDtoList = processClient.getRoutingHistoriesByApplicationNo(application.getApplicationNo()).getEntity();
        ParamUtil.setRequestAttr(request, ProcessContants.PARAM_APP_ID,appId);
        ParamUtil.setRequestAttr(request, ProcessContants.APPLICATION_MISC,applicationMisc);
        ParamUtil.setRequestAttr(request, ProcessContants.PARAM_PROCESSING_HISTORY,historyDtoList);
        ParamUtil.setSessionAttr(request, ProcessContants.APPLICATION_ATTR, application);
    }

    public void prepareSwitch(BaseProcessClass bpc) throws ParseException{
        HttpServletRequest request = bpc.request;
        DoScreeningDto dtoByForm = getDtoByForm(bpc);
        String processingDecision = dtoByForm.getProcessDecision();
        String crudActionType = "";
        ValidationResult vResult = WebValidationHelper.validateProperty(dtoByForm,"A");
        if(vResult != null && vResult.isHasErrors()){
            Map<String,String> errorMap = vResult.retrieveAll();
            String s = WebValidationHelper.generateJsonStr(errorMap);
            ParamUtil.setRequestAttr(request, IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errorMap));
            crudActionType = "prepareData";
        }else if (processingDecision.equals("DOSPD001")) {
             crudActionType = "screenedByDO";
        } else if (processingDecision.equals("DOSPD002") || processingDecision.equals("DOPPD003")) {
             crudActionType = "requestForInformation";
        } else if (processingDecision.equals("DOSPD003")) {
             crudActionType = "doReject";
        } else if (processingDecision.equals("DOPPD001")) {
             crudActionType = "recommendApproval";
        } else if (processingDecision.equals("DOPPD002")) {
             crudActionType = "recommendRejection";
        } else if (processingDecision.equals("AOSPD001")) {
             crudActionType = "approvalForInspection";
        } else if (processingDecision.equals("AOSPD002") || processingDecision.equals("AOPPD002")) {
             crudActionType = "aoReject";
        } else if (processingDecision.equals("AOSPD003") || processingDecision.equals("AOPPD003")) {
             crudActionType = "routeBackToDO";
        } else if (processingDecision.equals("AOSPD004") || processingDecision.equals("AOPPD004")) {
             crudActionType = "routeToHM";
        } else if (processingDecision.equals("AOPPD001")) {
             crudActionType = "aoApproved";
        } else if (processingDecision.equals("HMSPD001")) {
             crudActionType = "hmApprove";
        } else if (processingDecision.equals("HMSPD002")) {
             crudActionType = "hmReject";
        }
        ParamUtil.setRequestAttr(request,"crud_action_type",crudActionType);

    }

    public void screenedByDO(BaseProcessClass bpc) throws ParseException{
        String status = ProcessContants.APPLICATION_STATUS_2;
        DoScreeningDto doScreeningDtoByForm = getDtoByForm(bpc);
        doScreeningDtoByForm.setStatus(status);
        processClient.updateFacilityByMohProcess(doScreeningDtoByForm);
        //TODO validate
    }

    public void doReject(BaseProcessClass bpc) throws ParseException, IOException {
        String status = ProcessContants.APPLICATION_STATUS_8;
        DoScreeningDto doScreeningDtoByForm = getDtoByForm(bpc);
        doScreeningDtoByForm.setStatus(status);
        processClient.updateFacilityByMohProcess(doScreeningDtoByForm);
    }

    public void requestForInformation(BaseProcessClass bpc) throws ParseException{
        String status = ProcessContants.APPLICATION_STATUS_4;
        DoScreeningDto doScreeningDtoByForm = getDtoByForm(bpc);
        doScreeningDtoByForm.setStatus(status);
        processClient.updateFacilityByMohProcess(doScreeningDtoByForm);
    }

    public void approvalForInspection(BaseProcessClass bpc) throws ParseException{
        String status = ProcessContants.APPLICATION_STATUS_5;
        DoScreeningDto doScreeningDtoByForm = getDtoByForm(bpc);
        doScreeningDtoByForm.setStatus(status);
        processClient.updateFacilityByMohProcess(doScreeningDtoByForm);
    }

    public void aoReject(BaseProcessClass bpc) throws ParseException{
        String status = ProcessContants.APPLICATION_STATUS_8;
        DoScreeningDto doScreeningDtoByForm = getDtoByForm(bpc);
        doScreeningDtoByForm.setStatus(status);
        processClient.updateFacilityByMohProcess(doScreeningDtoByForm);
    }

    public void routeBackToDO(BaseProcessClass bpc) throws ParseException{
        String status = ProcessContants.APPLICATION_STATUS_1;
        DoScreeningDto doScreeningDtoByForm = getDtoByForm(bpc);
        doScreeningDtoByForm.setStatus(status);
        processClient.updateFacilityByMohProcess(doScreeningDtoByForm);
    }

    public void routeToHM(BaseProcessClass bpc) throws ParseException{
        String status = ProcessContants.APPLICATION_STATUS_3;
        DoScreeningDto doScreeningDtoByForm = getDtoByForm(bpc);
        doScreeningDtoByForm.setStatus(status);
        processClient.updateFacilityByMohProcess(doScreeningDtoByForm);
    }

    public void hmReject(BaseProcessClass bpc) throws ParseException{
        String status = ProcessContants.APPLICATION_STATUS_2;
        DoScreeningDto doScreeningDtoByForm = getDtoByForm(bpc);
        doScreeningDtoByForm.setStatus(status);
        processClient.updateFacilityByMohProcess(doScreeningDtoByForm);
    }

    public void hmApprove(BaseProcessClass bpc) throws ParseException{
        String status = ProcessContants.APPLICATION_STATUS_2;
        DoScreeningDto doScreeningDtoByForm = getDtoByForm(bpc);
        doScreeningDtoByForm.setStatus(status);
        processClient.updateFacilityByMohProcess(doScreeningDtoByForm);
    }

    public void recommendApproval(BaseProcessClass bpc) throws ParseException{
        String status = ProcessContants.APPLICATION_STATUS_2;
        DoScreeningDto doScreeningDtoByForm = getDtoByForm(bpc);
        doScreeningDtoByForm.setStatus(status);
        processClient.updateFacilityByMohProcess(doScreeningDtoByForm);
    }

    public void recommendRejection(BaseProcessClass bpc) throws ParseException{
        String status = ProcessContants.APPLICATION_STATUS_2;
        DoScreeningDto doScreeningDtoByForm = getDtoByForm(bpc);
        doScreeningDtoByForm.setStatus(status);
        processClient.updateFacilityByMohProcess(doScreeningDtoByForm);
    }

    public void aoApproved(BaseProcessClass bpc) throws ParseException{
        String status = ProcessContants.APPLICATION_STATUS_9;
        Date approvalDate = new Date();
        DoScreeningDto doScreeningDtoByForm = getDtoByForm(bpc);
        doScreeningDtoByForm.setStatus(status);
        doScreeningDtoByForm.setApprovalDate(approvalDate);
        processClient.updateFacilityByMohProcess(doScreeningDtoByForm);
    }

    private DoScreeningDto getDtoByForm(BaseProcessClass bpc) throws ParseException {
        HttpServletRequest request = bpc.request;
        Application application = (Application)ParamUtil.getSessionAttr(request, ProcessContants.APPLICATION_ATTR);
        //facility info
        String facilityId = application.getFacility().getId();
        String riskLevel = ParamUtil.getString(request, ProcessContants.RISK_LEVEL);
        String commentsOnRiskLevelAssessment = ParamUtil.getString(request, ProcessContants.COMMENTS_ON_RISK_LEVEL_ASSESSMENT);
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
        String appStatus = application.getStatus();
        //application misc info
        String remarks = ParamUtil.getString(request, ProcessContants.REMARKS);
        String finalRemarks = ParamUtil.getString(request, ProcessContants.FINAL_REMARKS);
        //set field value
        DoScreeningDto doScreeningDto = new DoScreeningDto();
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
        doScreeningDto.setApplicationId(applicationId);
        doScreeningDto.setRemarks(remarks);
        doScreeningDto.setAppStatus(appStatus);
        doScreeningDto.setProcessDecision(processingDecision);
        doScreeningDto.setActionBy(loginContext.getUserName());
        doScreeningDto.setApplicationNo(applicationNo);
        doScreeningDto.setFinalRemarks(finalRemarks);
        doScreeningDto.setReason(appStatus);
        return doScreeningDto;
    }
}

package sg.gov.moh.iais.egp.bsb.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.utils.Formatter;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.validation.dto.ValidationResult;
import com.ecquaria.cloud.moh.iais.dto.LoginContext;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.helper.MasterCodeUtil;
import com.ecquaria.cloud.moh.iais.helper.WebValidationHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sg.gov.moh.iais.egp.bsb.client.DocClient;
import sg.gov.moh.iais.egp.bsb.client.ProcessClient;
import sg.gov.moh.iais.egp.bsb.constant.EmailConstants;
import sg.gov.moh.iais.egp.bsb.constant.ProcessContants;
import sg.gov.moh.iais.egp.bsb.constant.RevocationConstants;
import sg.gov.moh.iais.egp.bsb.dto.BsbEmailParam;
import sg.gov.moh.iais.egp.bsb.dto.Notification;
import sg.gov.moh.iais.egp.bsb.dto.audit.AuditDocDto;
import sg.gov.moh.iais.egp.bsb.dto.process.DoScreeningDto;
import sg.gov.moh.iais.egp.bsb.entity.*;
import sg.gov.moh.iais.egp.bsb.helper.BsbNotificationHelper;
import sg.gov.moh.iais.egp.bsb.util.JoinBiologicalName;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author : LiRan
 * @date : 2021/8/20
 */
@Delegator
@Slf4j
public class MohProcessingDelegator {
    @Autowired
    private ProcessClient processClient;

    @Autowired
    private BsbNotificationHelper bsbNotificationHelper;

    @Autowired
    private DocClient docClient;

    public void start(BaseProcessClass bpc) throws IllegalAccessException {
        AuditTrailHelper.auditFunction(ProcessContants.MODULE_SYSTEM_CONFIG,
                ProcessContants.FUNCTION_ERROR_MESSAGES_MANAGEMENT);
        HttpServletRequest request = bpc.request;
        IaisEGPHelper.clearSessionAttr(request, ProcessContants.class);
    }

    public void prepareData(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        String crudActionType = ParamUtil.getString(request,ProcessContants.CRUD_ACTION_TYPE);
        String appId = "";
        if (crudActionType == null || crudActionType == ""){
            appId = ParamUtil.getString(request,ProcessContants.PARAM_APP_ID);
        }else if (crudActionType.equals(ProcessContants.CRUD_ACTION_TYPE_1)){
            appId = ParamUtil.getMaskedString(request, ProcessContants.PARAM_APP_ID);
        }
        Application application = processClient.getApplicationById(appId).getEntity();
        ApplicationMisc applicationMisc = new ApplicationMisc();
        if (application.getStatus().equals(ProcessContants.APPLICATION_STATUS_2)){
            applicationMisc = processClient.getApplicationMiscByApplicationIdAndAndReason(application.getId(), ProcessContants.APPLICATION_STATUS_1).getEntity();
        }else if (application.getStatus().equals(ProcessContants.APPLICATION_STATUS_3)){
            applicationMisc = processClient.getApplicationMiscByApplicationIdAndAndReason(application.getId(), ProcessContants.APPLICATION_STATUS_2).getEntity();
        }
        FacilityActivity facilityActivity = processClient.getFacilityActivityByApplicationId(appId).getEntity();
        application.getFacility().setActiveType(facilityActivity.getActivityType());
        List<FacilitySchedule> facilityScheduleList = facilityActivity.getFacilitySchedules();
        List<Biological> biologicalList = JoinBiologicalName.getBioListByFacilityScheduleList(facilityScheduleList,processClient);
        application.setBiologicalList(biologicalList);
        List<RoutingHistory> historyDtoList = processClient.getRoutingHistoriesByApplicationNo(application.getApplicationNo()).getEntity();
        List<FacilityDoc> facilityDocList = docClient.getFacilityDocByFacId(application.getFacility().getId()).getEntity();
        List<FacilityDoc> docList = new ArrayList<>();
        for (FacilityDoc facilityDoc : facilityDocList) {
//            String submitByName = IaisEGPHelper.getCurrentAuditTrailDto().getMohUserId();
//            facilityDoc.setSubmitByName(submitByName);
            docList.add(facilityDoc);
        }
        AuditDocDto auditDocDto = new AuditDocDto();
        auditDocDto.setFacilityDocs(docList);
        ParamUtil.setSessionAttr(request, RevocationConstants.AUDIT_DOC_DTO, auditDocDto);
        ParamUtil.setRequestAttr(request, ProcessContants.PARAM_APP_ID,appId);
        ParamUtil.setRequestAttr(request, ProcessContants.APPLICATION_MISC,applicationMisc);
        ParamUtil.setRequestAttr(request, ProcessContants.PARAM_PROCESSING_HISTORY,historyDtoList);
        ParamUtil.setSessionAttr(request, ProcessContants.APPLICATION_ATTR, application);
    }

    public void prepareSwitch(BaseProcessClass bpc) throws ParseException{
        HttpServletRequest request = bpc.request;
        Application application = (Application)ParamUtil.getSessionAttr(request, ProcessContants.APPLICATION_ATTR);
        DoScreeningDto dtoByForm = getDtoByForm(bpc);
        String processingDecision = dtoByForm.getProcessDecision();
        String crudActionType = "";
        //validate by different application status and type
        String validateStatus = "";
        if (application.getStatus().equals(ProcessContants.APPLICATION_STATUS_1)){
            if (application.getProcessType().equals(ProcessContants.APPLICATION_PROCESS_TYPE_1)){
                validateStatus = ProcessContants.VALIDATE_STATUS_1;
            }else{
                validateStatus = ProcessContants.VALIDATE_STATUS_2;
            }
        }else if (application.getStatus().equals(ProcessContants.APPLICATION_STATUS_2)){
            if (application.getProcessType().equals(ProcessContants.APPLICATION_PROCESS_TYPE_1)){
                validateStatus = ProcessContants.VALIDATE_STATUS_3;
            }else{
                validateStatus = ProcessContants.VALIDATE_STATUS_4;
            }
        }else if (application.getStatus().equals(ProcessContants.APPLICATION_STATUS_3)){
            validateStatus = ProcessContants.VALIDATE_STATUS_5;
        }
        ValidationResult vResult = WebValidationHelper.validateProperty(dtoByForm,validateStatus);
        if(vResult != null && vResult.isHasErrors()){
            Map<String,String> errorMap = vResult.retrieveAll();
            String doProcess = "Y";
            ParamUtil.setRequestAttr(request, ProcessContants.DO_PROCESS, doProcess);
            ParamUtil.setRequestAttr(request, ProcessContants.ERRORMSG, WebValidationHelper.generateJsonStr(errorMap));
            crudActionType = ProcessContants.CRUD_ACTION_TYPE_2;
        }else if (processingDecision.equals(ProcessContants.DO_PROCESS_DECISION_1)) {
             crudActionType = ProcessContants.CRUD_ACTION_TYPE_3;
        } else if (processingDecision.equals(ProcessContants.DO_PROCESS_DECISION_2) || processingDecision.equals(ProcessContants.DO_PROCESS_DECISION_6)) {
             crudActionType = ProcessContants.CRUD_ACTION_TYPE_4;
        } else if (processingDecision.equals(ProcessContants.DO_PROCESS_DECISION_3)) {
            crudActionType = ProcessContants.CRUD_ACTION_TYPE_5;
        } else if (processingDecision.equals(ProcessContants.DO_PROCESS_DECISION_4)) {
            crudActionType = ProcessContants.CRUD_ACTION_TYPE_6;
        } else if (processingDecision.equals(ProcessContants.DO_PROCESS_DECISION_5)) {
            crudActionType = ProcessContants.CRUD_ACTION_TYPE_7;
        } else if (processingDecision.equals(ProcessContants.AO_PROCESS_DECISION_1)) {
            crudActionType = ProcessContants.CRUD_ACTION_TYPE_8;
        } else if (processingDecision.equals(ProcessContants.AO_PROCESS_DECISION_2) || processingDecision.equals(ProcessContants.AO_PROCESS_DECISION_6)) {
            crudActionType = ProcessContants.CRUD_ACTION_TYPE_9;
        } else if (processingDecision.equals(ProcessContants.AO_PROCESS_DECISION_3) || processingDecision.equals(ProcessContants.AO_PROCESS_DECISION_7)) {
            crudActionType = ProcessContants.CRUD_ACTION_TYPE_10;
        } else if (processingDecision.equals(ProcessContants.AO_PROCESS_DECISION_4) || processingDecision.equals(ProcessContants.AO_PROCESS_DECISION_8)) {
            crudActionType = ProcessContants.CRUD_ACTION_TYPE_11;
        } else if (processingDecision.equals(ProcessContants.AO_PROCESS_DECISION_5)) {
            crudActionType = ProcessContants.CRUD_ACTION_TYPE_12;
        } else if (processingDecision.equals(ProcessContants.HM_PROCESS_DECISION_1)) {
            crudActionType = ProcessContants.CRUD_ACTION_TYPE_13;
        } else if (processingDecision.equals(ProcessContants.HM_PROCESS_DECISION_2)) {
            crudActionType = ProcessContants.CRUD_ACTION_TYPE_14;
        }
        ParamUtil.setRequestAttr(request,ProcessContants.CRUD_ACTION_TYPE,crudActionType);
        ParamUtil.setRequestAttr(request,ProcessContants.DO_SCREENING_DTO,dtoByForm);
    }

    public void screenedByDO(BaseProcessClass bpc) throws ParseException{
        String status = ProcessContants.APPLICATION_STATUS_2;
        DoScreeningDto doScreeningDtoByForm = getDtoByForm(bpc);
        doScreeningDtoByForm.setStatus(status);
        processClient.updateFacilityByMohProcess(doScreeningDtoByForm);
    }

    public void doReject(BaseProcessClass bpc) throws ParseException, IOException {
        HttpServletRequest request = bpc.request;
        String status = ProcessContants.APPLICATION_STATUS_8;
        DoScreeningDto doScreeningDtoByForm = getDtoByForm(bpc);
        doScreeningDtoByForm.setStatus(status);
        processClient.updateFacilityByMohProcess(doScreeningDtoByForm);
        //send email notification to Applicant
        Application application = (Application)ParamUtil.getSessionAttr(request, ProcessContants.APPLICATION_ATTR);
        BsbEmailParam bsbEmailParam = new BsbEmailParam();
        bsbEmailParam.setMsgTemplateId(EmailConstants.MSG_TEMPLATE_NEW_APP_REJECT);
        bsbEmailParam.setRefId(application.getApplicationNo());
        bsbEmailParam.setRefIdType("appNo");
        bsbEmailParam.setQueryCode("1");
        bsbEmailParam.setReqRefNum("1");
        Map contentMap = new HashMap();
        contentMap.put("applicationNo", application.getApplicationNo());
        Map subjectMap = new HashMap();
        subjectMap.put("applicationNo", application.getApplicationNo());
        bsbEmailParam.setMsgSubject(subjectMap);
        bsbEmailParam.setMsgContent(contentMap);
        bsbNotificationHelper.sendNotification(bsbEmailParam);
    }

    public void requestForInformation(BaseProcessClass bpc) throws ParseException{
        HttpServletRequest request = bpc.request;
        String status = ProcessContants.APPLICATION_STATUS_4;
        DoScreeningDto doScreeningDtoByForm = getDtoByForm(bpc);
        doScreeningDtoByForm.setStatus(status);
        processClient.updateFacilityByMohProcess(doScreeningDtoByForm);
        //send email notification to Applicant
        Application application = (Application)ParamUtil.getSessionAttr(request, ProcessContants.APPLICATION_ATTR);
        BsbEmailParam bsbEmailParam = new BsbEmailParam();
        bsbEmailParam.setMsgTemplateId(EmailConstants.MSG_TEMPLATE_NEW_APP_REQUEST_FOR_INFO);
        bsbEmailParam.setRefId(application.getApplicationNo());
        bsbEmailParam.setRefIdType("appNo");
        bsbEmailParam.setQueryCode("1");
        bsbEmailParam.setReqRefNum("1");
        Map contentMap = new HashMap();
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        String dateString = formatter.format(doScreeningDtoByForm.getValidityEndDt());
        contentMap.put("applicationNo", application.getApplicationNo());
        contentMap.put("Date",dateString);
        contentMap.put("AdditionalInfo", "additionalInfo");
        Map subjectMap = new HashMap();
        subjectMap.put("RFITitle", "title");
        /*Map attachmentMap = new HashMap();
        attachmentMap.put(null);*/
        bsbEmailParam.setMsgSubject(subjectMap);
        bsbEmailParam.setMsgContent(contentMap);
        /*bsbEmailParam.setAttachments(attachmentMap);*/
        bsbNotificationHelper.sendNotification(bsbEmailParam);
    }

    public void approvalForInspection(BaseProcessClass bpc) throws ParseException{
        String status = ProcessContants.APPLICATION_STATUS_5;
        DoScreeningDto doScreeningDtoByForm = getDtoByForm(bpc);
        doScreeningDtoByForm.setStatus(status);
        processClient.updateFacilityByMohProcess(doScreeningDtoByForm);
    }

    public void aoReject(BaseProcessClass bpc) throws ParseException{
        HttpServletRequest request = bpc.request;
        String status = ProcessContants.APPLICATION_STATUS_8;
        DoScreeningDto doScreeningDtoByForm = getDtoByForm(bpc);
        doScreeningDtoByForm.setStatus(status);
        processClient.updateFacilityByMohProcess(doScreeningDtoByForm);
        //send email notification to Applicant
        Application application = (Application)ParamUtil.getSessionAttr(request, ProcessContants.APPLICATION_ATTR);
        BsbEmailParam bsbEmailParam = new BsbEmailParam();
        bsbEmailParam.setMsgTemplateId(EmailConstants.MSG_TEMPLATE_NEW_APP_REJECT);
        bsbEmailParam.setRefId(application.getApplicationNo());
        bsbEmailParam.setRefIdType("appNo");
        bsbEmailParam.setQueryCode("1");
        bsbEmailParam.setReqRefNum("1");
        Map contentMap = new HashMap();
        contentMap.put("applicationNo", application.getApplicationNo());
        Map subjectMap = new HashMap();
        subjectMap.put("applicationNo", application.getApplicationNo());
        bsbEmailParam.setMsgSubject(subjectMap);
        bsbEmailParam.setMsgContent(contentMap);
        bsbNotificationHelper.sendNotification(bsbEmailParam);
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
        HttpServletRequest request = bpc.request;
        String status = ProcessContants.APPLICATION_STATUS_9;
        Date approvalDate = new Date();
        DoScreeningDto doScreeningDtoByForm = getDtoByForm(bpc);
        doScreeningDtoByForm.setStatus(status);
        doScreeningDtoByForm.setApprovalDate(approvalDate);
        processClient.updateFacilityByMohProcess(doScreeningDtoByForm);
        //send email notification to Applicant
        Application application = (Application)ParamUtil.getSessionAttr(request, ProcessContants.APPLICATION_ATTR);
        BsbEmailParam bsbEmailParam = new BsbEmailParam();
        bsbEmailParam.setMsgTemplateId(EmailConstants.MSG_TEMPLATE_NEW_APP_APPROVAL);
        bsbEmailParam.setRefId(application.getApplicationNo());
        bsbEmailParam.setRefIdType("appNo");
        bsbEmailParam.setQueryCode("1");
        bsbEmailParam.setReqRefNum("1");
        Map contentMap = new HashMap();
        contentMap.put("applicationType",MasterCodeUtil.getCodeDesc(application.getAppType()));
        contentMap.put("applicationNo", application.getApplicationNo());
        Map subjectMap = new HashMap();
        subjectMap.put("applicationNo", application.getApplicationNo());
        bsbEmailParam.setMsgSubject(subjectMap);
        bsbEmailParam.setMsgContent(contentMap);
        bsbNotificationHelper.sendNotification(bsbEmailParam);
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

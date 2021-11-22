package sg.gov.moh.iais.egp.bsb.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.utils.MaskUtil;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.dto.LoginContext;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sg.gov.moh.iais.egp.bsb.client.DocClient;
import sg.gov.moh.iais.egp.bsb.client.ProcessClient;
import sg.gov.moh.iais.egp.bsb.constant.ProcessContants;
import sg.gov.moh.iais.egp.bsb.constant.RevocationConstants;
import sg.gov.moh.iais.egp.bsb.dto.ValidationResultDto;
import sg.gov.moh.iais.egp.bsb.dto.audit.AuditDocDto;
import sg.gov.moh.iais.egp.bsb.dto.process.MohProcessDto;
import sg.gov.moh.iais.egp.bsb.dto.process.SubmitDetailsDto;
import sg.gov.moh.iais.egp.bsb.entity.*;
import sg.gov.moh.iais.egp.bsb.helper.BsbNotificationHelper;
import sg.gov.moh.iais.egp.bsb.util.TableDisplayUtil;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.util.*;

/**
 * @author : LiRan
 * @date : 2021/8/20
 */
@Delegator
@Slf4j
public class MohProcessingDelegator {
    private static final String FACILITY = "facility";

    private static final String NOTIFICATION_VIRTUAL_DATA = "fillData";
    private static final String NOTIFICATION_APP_NO = "applicationNo";
    private static final String NOTIFICATION_APP_TYPE = "applicationType";
    private static final String NOTIFICATION_DATE = "Date";
    private static final String NOTIFICATION_ADDITIONAL_INFO = "AdditionalInfo";
    private static final String NOTIFICATION_RFI_TITLE = "RFITitle";

    private static final String KEY_VALIDATION_ERRORS = "errorMsg";

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
        //validation failure,get facilityActivity from session
        SubmitDetailsDto submitDetailsDto = (SubmitDetailsDto)ParamUtil.getSessionAttr(request, ProcessContants.SUBMITDETAILS_ATTR);
        //first log in,get facilityActivity from db
        if (submitDetailsDto == null){
            String appId = MaskUtil.unMaskValue("id", ParamUtil.getString(request,ProcessContants.PARAM_APP_ID));
            String taskId = MaskUtil.unMaskValue("taskId", ParamUtil.getString(request,ProcessContants.PARAM_TASK_ID));
            //get submitDetailsDto
            submitDetailsDto = processClient.getSubmitDetailsByAppId(appId).getEntity();
            submitDetailsDto.setTaskId(taskId);
            setField(submitDetailsDto);
            ParamUtil.setSessionAttr(request, ProcessContants.SUBMITDETAILS_ATTR, submitDetailsDto);
            //uploadDocuments need facility
            ParamUtil.setSessionAttr(request, FACILITY, submitDetailsDto.getFacilityActivity().getFacility());
        }
        //show facilityDoc
        List<FacilityDoc> facilityDocList = submitDetailsDto.getFacilityActivity().getFacility().getDocs();
        AuditDocDto auditDocDto = new AuditDocDto();
        auditDocDto.setFacilityDocs(facilityDocList);
        ParamUtil.setSessionAttr(request, RevocationConstants.AUDIT_DOC_DTO, auditDocDto);
        //
        MohProcessDto mohProcessDto = (MohProcessDto)ParamUtil.getRequestAttr(request, ProcessContants.MOH_PROCESS_ATTR);
        ParamUtil.setRequestAttr(request,ProcessContants.MOH_PROCESS_ATTR,mohProcessDto);
    }

    public void prepareSwitch(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        MohProcessDto mohProcessDto = getDtoByForm(bpc);
        String processingDecision = mohProcessDto.getProcessDecision();
        String crudActionType = "";
        //validate by different application status and processType
        SubmitDetailsDto submitDetailsDto = (SubmitDetailsDto)ParamUtil.getSessionAttr(request, ProcessContants.SUBMITDETAILS_ATTR);
        String appStatus = submitDetailsDto.getFacilityActivity().getApplication().getStatus();
        String validateStatus = "";
        //validation
        if (appStatus.equals(ProcessContants.APPLICATION_STATUS_1)){
            validateStatus = ProcessContants.VALIDATE_STATUS_2;
        }else if (appStatus.equals(ProcessContants.APPLICATION_STATUS_2)){
            validateStatus = ProcessContants.VALIDATE_STATUS_4;
        }
        mohProcessDto.setProfiles(validateStatus);
        ValidationResultDto validationResultDto = processClient.validateMohProcessDto(mohProcessDto);
        if (!validationResultDto.isPass()){
            String doProcess = "Y";
            ParamUtil.setRequestAttr(request, ProcessContants.MOH_PROCESS, doProcess);
            ParamUtil.setRequestAttr(request, KEY_VALIDATION_ERRORS, validationResultDto.toErrorMsg());
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
        ParamUtil.setRequestAttr(request,ProcessContants.MOH_PROCESS_ATTR,mohProcessDto);
    }

    public void screenedByDO(BaseProcessClass bpc){
        String status = ProcessContants.APPLICATION_STATUS_2;
        MohProcessDto mohProcessDto = getDtoByForm(bpc);
        mohProcessDto.setProcessStatus(status);
        processClient.saveMohProcess(mohProcessDto);
    }

    public void doReject(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        String status = ProcessContants.APPLICATION_STATUS_8;
        MohProcessDto mohProcessDto = getDtoByForm(bpc);
        mohProcessDto.setProcessStatus(status);
        processClient.saveMohProcess(mohProcessDto);
        /*//send email notification to Applicant
        SubmitDetailsDto submitDetailsDto = (SubmitDetailsDto)ParamUtil.getSessionAttr(request, ProcessContants.SUBMITDETAILS_ATTR);
        String applicationNo = submitDetailsDto.getApplicationNo();
        BsbEmailParam bsbEmailParam = new BsbEmailParam();
        bsbEmailParam.setMsgTemplateId(EmailConstants.MSG_TEMPLATE_NEW_APP_REJECT);
        bsbEmailParam.setRefId(applicationNo);
        bsbEmailParam.setRefIdType(NOTIFICATION_VIRTUAL_DATA);
        bsbEmailParam.setQueryCode(NOTIFICATION_VIRTUAL_DATA);
        bsbEmailParam.setReqRefNum(NOTIFICATION_VIRTUAL_DATA);
        Map<String,Object> contentMap = new HashMap<>();
        contentMap.put(NOTIFICATION_APP_NO, applicationNo);
        Map<String,Object> subjectMap = new HashMap<>();
        subjectMap.put(NOTIFICATION_APP_NO, applicationNo);
        bsbEmailParam.setMsgSubject(subjectMap);
        bsbEmailParam.setMsgContent(contentMap);
        bsbNotificationHelper.sendNotification(bsbEmailParam);*/
    }

    public void requestForInformation(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        String status = ProcessContants.APPLICATION_STATUS_4;
        MohProcessDto mohProcessDto = getDtoByForm(bpc);
        mohProcessDto.setProcessStatus(status);
        processClient.saveMohProcess(mohProcessDto);
        /*//send email notification to Applicant
        SubmitDetailsDto submitDetailsDto = (SubmitDetailsDto)ParamUtil.getSessionAttr(request, ProcessContants.SUBMITDETAILS_ATTR);
        String applicationNo = submitDetailsDto.getApplicationNo();
        BsbEmailParam bsbEmailParam = new BsbEmailParam();
        bsbEmailParam.setMsgTemplateId(EmailConstants.MSG_TEMPLATE_NEW_APP_REQUEST_FOR_INFO);
        bsbEmailParam.setRefId(applicationNo);
        bsbEmailParam.setRefIdType(NOTIFICATION_VIRTUAL_DATA);
        bsbEmailParam.setQueryCode(NOTIFICATION_VIRTUAL_DATA);
        bsbEmailParam.setReqRefNum(NOTIFICATION_VIRTUAL_DATA);
        Map<String,Object> contentMap = new HashMap<>();
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        String dateString = formatter.format(mohProcessDto.getValidityEndDt());
        contentMap.put(NOTIFICATION_APP_NO, applicationNo);
        contentMap.put(NOTIFICATION_DATE,dateString);
        contentMap.put(NOTIFICATION_ADDITIONAL_INFO, NOTIFICATION_VIRTUAL_DATA);
        Map<String,Object> subjectMap = new HashMap<>();
        subjectMap.put(NOTIFICATION_RFI_TITLE, NOTIFICATION_VIRTUAL_DATA);
        bsbEmailParam.setMsgSubject(subjectMap);
        bsbEmailParam.setMsgContent(contentMap);
        bsbNotificationHelper.sendNotification(bsbEmailParam);*/
    }

    public void approvalForInspection(BaseProcessClass bpc){
        String status = ProcessContants.APPLICATION_STATUS_5;
        MohProcessDto mohProcessDto = getDtoByForm(bpc);
        mohProcessDto.setProcessStatus(status);
        processClient.saveMohProcess(mohProcessDto);
    }

    public void aoReject(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        String status = ProcessContants.APPLICATION_STATUS_8;
        MohProcessDto mohProcessDto = getDtoByForm(bpc);
        mohProcessDto.setProcessStatus(status);
        processClient.saveMohProcess(mohProcessDto);
        /*//send email notification to Applicant
        SubmitDetailsDto submitDetailsDto = (SubmitDetailsDto)ParamUtil.getSessionAttr(request, ProcessContants.SUBMITDETAILS_ATTR);
        String applicationNo = submitDetailsDto.getApplicationNo();
        BsbEmailParam bsbEmailParam = new BsbEmailParam();
        bsbEmailParam.setMsgTemplateId(EmailConstants.MSG_TEMPLATE_NEW_APP_REJECT);
        bsbEmailParam.setRefId(applicationNo);
        bsbEmailParam.setRefIdType(NOTIFICATION_VIRTUAL_DATA);
        bsbEmailParam.setQueryCode(NOTIFICATION_VIRTUAL_DATA);
        bsbEmailParam.setReqRefNum(NOTIFICATION_VIRTUAL_DATA);
        Map<String,Object> contentMap = new HashMap<>();
        contentMap.put(NOTIFICATION_APP_NO, applicationNo);
        Map<String,Object> subjectMap = new HashMap<>();
        subjectMap.put(NOTIFICATION_APP_NO, applicationNo);
        bsbEmailParam.setMsgSubject(subjectMap);
        bsbEmailParam.setMsgContent(contentMap);
        bsbNotificationHelper.sendNotification(bsbEmailParam);*/
    }

    public void routeBackToDO(BaseProcessClass bpc){
        String status = ProcessContants.APPLICATION_STATUS_1;
        MohProcessDto mohProcessDto = getDtoByForm(bpc);
        mohProcessDto.setProcessStatus(status);
        processClient.saveMohProcess(mohProcessDto);
    }

    public void routeToHM(BaseProcessClass bpc){
        String status = ProcessContants.APPLICATION_STATUS_3;
        MohProcessDto mohProcessDto = getDtoByForm(bpc);
        mohProcessDto.setProcessStatus(status);
        processClient.saveMohProcess(mohProcessDto);
    }

    public void hmReject(BaseProcessClass bpc){
        String status = ProcessContants.APPLICATION_STATUS_2;
        MohProcessDto mohProcessDto = getDtoByForm(bpc);
        mohProcessDto.setProcessStatus(status);
        processClient.saveMohProcess(mohProcessDto);
    }

    public void hmApprove(BaseProcessClass bpc){
        String status = ProcessContants.APPLICATION_STATUS_2;
        MohProcessDto mohProcessDto = getDtoByForm(bpc);
        mohProcessDto.setProcessStatus(status);
        processClient.saveMohProcess(mohProcessDto);
    }

    public void recommendApproval(BaseProcessClass bpc){
        String status = ProcessContants.APPLICATION_STATUS_2;
        MohProcessDto mohProcessDto = getDtoByForm(bpc);
        mohProcessDto.setProcessStatus(status);
        processClient.saveMohProcess(mohProcessDto);
    }

    public void recommendRejection(BaseProcessClass bpc){
        String status = ProcessContants.APPLICATION_STATUS_2;
        MohProcessDto mohProcessDto = getDtoByForm(bpc);
        mohProcessDto.setProcessStatus(status);
        processClient.saveMohProcess(mohProcessDto);
    }

    public void aoApproved(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        String status = ProcessContants.APPLICATION_STATUS_9;
        Date approvalDate = new Date();
        MohProcessDto mohProcessDto = getDtoByForm(bpc);
        mohProcessDto.setProcessStatus(status);
        mohProcessDto.setApprovalDate(approvalDate);
        processClient.saveMohProcess(mohProcessDto);
        /*//send email notification to Applicant
        SubmitDetailsDto submitDetailsDto = (SubmitDetailsDto)ParamUtil.getSessionAttr(request, ProcessContants.SUBMITDETAILS_ATTR);
        String applicationNo = submitDetailsDto.getApplicationNo();
        String applicationType = submitDetailsDto.getApplicationType();
        BsbEmailParam bsbEmailParam = new BsbEmailParam();
        bsbEmailParam.setMsgTemplateId(EmailConstants.MSG_TEMPLATE_NEW_APP_APPROVAL);
        bsbEmailParam.setRefId(applicationNo);
        bsbEmailParam.setRefIdType(NOTIFICATION_VIRTUAL_DATA);
        bsbEmailParam.setQueryCode(NOTIFICATION_VIRTUAL_DATA);
        bsbEmailParam.setReqRefNum(NOTIFICATION_VIRTUAL_DATA);
        Map<String,Object> contentMap = new HashMap<>();
        contentMap.put(NOTIFICATION_APP_TYPE,MasterCodeUtil.getCodeDesc(applicationType));
        contentMap.put(NOTIFICATION_APP_NO, applicationNo);
        Map<String,Object> subjectMap = new HashMap<>();
        subjectMap.put(NOTIFICATION_APP_NO, applicationNo);
        bsbEmailParam.setMsgSubject(subjectMap);
        bsbEmailParam.setMsgContent(contentMap);
        bsbNotificationHelper.sendNotification(bsbEmailParam);*/
    }

    /**
     * This method is for get doScreeningDto from page
     * @param bpc
     * @return
     * @throws ParseException
     */
    private MohProcessDto getDtoByForm(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        SubmitDetailsDto submitDetailsDto = (SubmitDetailsDto)ParamUtil.getSessionAttr(request, ProcessContants.SUBMITDETAILS_ATTR);
        //application info
        String applicationNo = submitDetailsDto.getFacilityActivity().getApplication().getApplicationNo();
        String applicationId = submitDetailsDto.getFacilityActivity().getApplication().getId();
        String taskId = submitDetailsDto.getTaskId();
        //history info
        LoginContext loginContext = (LoginContext)ParamUtil.getSessionAttr(request, AppConsts.SESSION_ATTR_LOGIN_USER);
        String processingDecision = ParamUtil.getString(request, ProcessContants.PROCESSING_DECISION);
        String appStatus = submitDetailsDto.getFacilityActivity().getApplication().getStatus();
        //application misc info
        String remarks = ParamUtil.getString(request, ProcessContants.REMARKS);
        String finalRemarks = ParamUtil.getString(request, ProcessContants.FINAL_REMARKS);
        String riskLevel = ParamUtil.getString(request, ProcessContants.RISK_LEVEL);
        String commentsOnRiskLevelAssessment = ParamUtil.getString(request, ProcessContants.COMMENTS_ON_RISK_LEVEL_ASSESSMENT);
        String erpReport = ParamUtil.getString(request, ProcessContants.ERP_REPORT);
        String redTeamingReport = ParamUtil.getString(request, ProcessContants.RED_TEAMING_REPORT);
        String lentivirusReport = ParamUtil.getString(request, ProcessContants.LENTIVIRUS_REPORT);
        String internalInspectionReport = ParamUtil.getString(request, ProcessContants.INTERNAL_INSPECTION_REPORT);
        String selectedApprovedFacilityCertifier = ParamUtil.getString(request, ProcessContants.SELECTED_APPROVED_FACILITY_CERTIFIER);
        String validityStartDate = ParamUtil.getString(request, ProcessContants.VALIDITY_START_DATE);
        String validityEndDate = ParamUtil.getString(request, ProcessContants.VALIDITY_END_DATE);
        //approval info
        String processType = submitDetailsDto.getProcessType();
        //set field value
        MohProcessDto mohProcessDto = new MohProcessDto();
        mohProcessDto.setTaskId(taskId);
        mohProcessDto.setRiskLevel(riskLevel);
        mohProcessDto.setRiskLevelComments(commentsOnRiskLevelAssessment);
        mohProcessDto.setErpReportDt(erpReport);
        mohProcessDto.setRedTeamingReportDt(redTeamingReport);
        mohProcessDto.setLentivirusReportDt(lentivirusReport);
        mohProcessDto.setInternalInspectionReportDt(internalInspectionReport);
        mohProcessDto.setValidityStartDt(validityStartDate);
        mohProcessDto.setValidityEndDt(validityEndDate);
        mohProcessDto.setSelectedAfc(selectedApprovedFacilityCertifier);
        mohProcessDto.setApplicationId(applicationId);
        mohProcessDto.setRemarks(remarks);
        mohProcessDto.setAppStatus(appStatus);
        mohProcessDto.setProcessDecision(processingDecision);
        mohProcessDto.setActionBy(loginContext.getUserName());
        mohProcessDto.setApplicationNo(applicationNo);
        mohProcessDto.setFinalRemarks(finalRemarks);
        mohProcessDto.setReason(appStatus);
        mohProcessDto.setProcessType(processType);
        return mohProcessDto;
    }

    /**
     * This method is for get some facility,application,activity info from facilityActivity to display
     * @param submitDetailsDto
     */
    private void setField(SubmitDetailsDto submitDetailsDto){
        Application application = submitDetailsDto.getFacilityActivity().getApplication();
        Facility facility = submitDetailsDto.getFacilityActivity().getFacility();
        String appStatus = application.getStatus();
        String applicationNo = application.getApplicationNo();
        submitDetailsDto.setApplicationNo(applicationNo);
        submitDetailsDto.setApplicationType(application.getAppType());
        submitDetailsDto.setProcessType(application.getProcessType());
        submitDetailsDto.setFacilityType(submitDetailsDto.getFacilityActivity().getActivityType());
        submitDetailsDto.setFacilityName(facility.getFacilityName());
        String facilityAddress = TableDisplayUtil.getOneLineAddress(facility.getBlkNo(), facility.getStreetName(), facility.getFloorNo(), facility.getUnitNo(), facility.getPostalCode());
        submitDetailsDto.setFacilityAddress(facilityAddress);
        submitDetailsDto.setApplicationDt(application.getApplicationDt());
        submitDetailsDto.setApplicationStatus(appStatus);
        /*List<RoutingHistory> historyDtoList = processClient.getRoutingHistoriesByApplicationNo(applicationNo).getEntity();
        submitDetailsDto.setRoutingHistories(historyDtoList);*/
        String reason = "";
        if (appStatus.equals(ProcessContants.APPLICATION_STATUS_2)){
            reason =  ProcessContants.APPLICATION_STATUS_1;
        }else if (appStatus.equals(ProcessContants.APPLICATION_STATUS_3)){
            reason = ProcessContants.APPLICATION_STATUS_2;
        }
        ApplicationMisc applicationMisc = processClient.getAppMiscByAppIdAndReasonAndLatestDate(application.getId(),reason).getEntity();
        submitDetailsDto.setApplicationMisc(applicationMisc);
    }
}
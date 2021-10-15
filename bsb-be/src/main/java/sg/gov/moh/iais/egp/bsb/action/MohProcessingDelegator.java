package sg.gov.moh.iais.egp.bsb.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.utils.Formatter;
import com.ecquaria.cloud.moh.iais.common.utils.MaskUtil;
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
import sg.gov.moh.iais.egp.bsb.dto.audit.AuditDocDto;
import sg.gov.moh.iais.egp.bsb.dto.process.DoScreeningDto;
import sg.gov.moh.iais.egp.bsb.dto.process.SubmitDetailsDto;
import sg.gov.moh.iais.egp.bsb.entity.*;
import sg.gov.moh.iais.egp.bsb.helper.BsbNotificationHelper;
import sg.gov.moh.iais.egp.bsb.util.TableDisplayUtil;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
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

    private static final String FACILITY = "facility";

    private static final String NOTIFICATION_VIRTUAL_DATA = "fillData";
    private static final String NOTIFICATION_APP_NO = "applicationNo";
    private static final String NOTIFICATION_APP_TYPE = "applicationType";
    private static final String NOTIFICATION_DATE = "Date";
    private static final String NOTIFICATION_ADDITIONAL_INFO = "AdditionalInfo";
    private static final String NOTIFICATION_RFI_TITLE = "RFITitle";

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
            //get submitDetailsDto
            submitDetailsDto = processClient.getSubmitDetailsByAppId(appId).getEntity();
            setField(submitDetailsDto);
            ParamUtil.setSessionAttr(request, ProcessContants.SUBMITDETAILS_ATTR, submitDetailsDto);
            //uploadDocuments need facility
            ParamUtil.setSessionAttr(request, FACILITY, submitDetailsDto.getFacilityActivity().getFacility());
            //TODO get applicationMisc
            /*application = processClient.getApplicationById(appId).getEntity();
            //get applicationMisc
            ApplicationMisc applicationMisc = new ApplicationMisc();
            if (application.getStatus().equals(ProcessContants.APPLICATION_STATUS_2)){
                applicationMisc = processClient.getApplicationMiscByApplicationIdAndAndReason(application.getId(), ProcessContants.APPLICATION_STATUS_1).getEntity();
            }else if (application.getStatus().equals(ProcessContants.APPLICATION_STATUS_3)){
                applicationMisc = processClient.getApplicationMiscByApplicationIdAndAndReason(application.getId(), ProcessContants.APPLICATION_STATUS_2).getEntity();
            }
            ParamUtil.setSessionAttr(request, ProcessContants.APPLICATION_MISC,applicationMisc);*/
        }
        //show facilityDoc
        List<FacilityDoc> facilityDocList = submitDetailsDto.getFacilityActivity().getFacility().getDocs();
        AuditDocDto auditDocDto = new AuditDocDto();
        auditDocDto.setFacilityDocs(facilityDocList);
        ParamUtil.setSessionAttr(request, RevocationConstants.AUDIT_DOC_DTO, auditDocDto);

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

    public void doReject(BaseProcessClass bpc) throws ParseException {
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
        bsbEmailParam.setRefIdType(NOTIFICATION_VIRTUAL_DATA);
        bsbEmailParam.setQueryCode(NOTIFICATION_VIRTUAL_DATA);
        bsbEmailParam.setReqRefNum(NOTIFICATION_VIRTUAL_DATA);
        Map<String,Object> contentMap = new HashMap<>();
        contentMap.put(NOTIFICATION_APP_NO, application.getApplicationNo());
        Map<String,Object> subjectMap = new HashMap<>();
        subjectMap.put(NOTIFICATION_APP_NO, application.getApplicationNo());
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
        bsbEmailParam.setRefIdType(NOTIFICATION_VIRTUAL_DATA);
        bsbEmailParam.setQueryCode(NOTIFICATION_VIRTUAL_DATA);
        bsbEmailParam.setReqRefNum(NOTIFICATION_VIRTUAL_DATA);
        Map<String,Object> contentMap = new HashMap<>();
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        String dateString = formatter.format(doScreeningDtoByForm.getValidityEndDt());
        contentMap.put(NOTIFICATION_APP_NO, application.getApplicationNo());
        contentMap.put(NOTIFICATION_DATE,dateString);
        contentMap.put(NOTIFICATION_ADDITIONAL_INFO, NOTIFICATION_VIRTUAL_DATA);
        Map<String,Object> subjectMap = new HashMap<>();
        subjectMap.put(NOTIFICATION_RFI_TITLE, NOTIFICATION_VIRTUAL_DATA);
        bsbEmailParam.setMsgSubject(subjectMap);
        bsbEmailParam.setMsgContent(contentMap);
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
        bsbEmailParam.setRefIdType(NOTIFICATION_VIRTUAL_DATA);
        bsbEmailParam.setQueryCode(NOTIFICATION_VIRTUAL_DATA);
        bsbEmailParam.setReqRefNum(NOTIFICATION_VIRTUAL_DATA);
        Map<String,Object> contentMap = new HashMap<>();
        contentMap.put(NOTIFICATION_APP_NO, application.getApplicationNo());
        Map<String,Object> subjectMap = new HashMap<>();
        subjectMap.put(NOTIFICATION_APP_NO, application.getApplicationNo());
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
        bsbEmailParam.setRefIdType(NOTIFICATION_VIRTUAL_DATA);
        bsbEmailParam.setQueryCode(NOTIFICATION_VIRTUAL_DATA);
        bsbEmailParam.setReqRefNum(NOTIFICATION_VIRTUAL_DATA);
        Map<String,Object> contentMap = new HashMap<>();
        contentMap.put(NOTIFICATION_APP_TYPE,MasterCodeUtil.getCodeDesc(application.getAppType()));
        contentMap.put(NOTIFICATION_APP_NO, application.getApplicationNo());
        Map<String,Object> subjectMap = new HashMap<>();
        subjectMap.put(NOTIFICATION_APP_NO, application.getApplicationNo());
        bsbEmailParam.setMsgSubject(subjectMap);
        bsbEmailParam.setMsgContent(contentMap);
        bsbNotificationHelper.sendNotification(bsbEmailParam);
    }

    /**
     * This method is for get doScreeningDto from page
     * @param bpc
     * @return
     * @throws ParseException
     */
    private DoScreeningDto getDtoByForm(BaseProcessClass bpc) throws ParseException {
        HttpServletRequest request = bpc.request;
        Application application = (Application)ParamUtil.getSessionAttr(request, ProcessContants.APPLICATION_ATTR);
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

    /**
     * This method is for get some facility,application,activity info from facilityActivity to display
     * @param submitDetailsDto
     */
    private static void setField(SubmitDetailsDto submitDetailsDto){
        submitDetailsDto.setApplicationNo(submitDetailsDto.getFacilityActivity().getApplication().getApplicationNo());
        submitDetailsDto.setApplicationType(submitDetailsDto.getFacilityActivity().getApplication().getAppType());
        submitDetailsDto.setProcessType(submitDetailsDto.getFacilityActivity().getApplication().getProcessType());
        submitDetailsDto.setFacilityType(submitDetailsDto.getFacilityActivity().getActivityType());
        submitDetailsDto.setFacilityName(submitDetailsDto.getFacilityActivity().getFacility().getFacilityName());
        String facilityAddress = TableDisplayUtil.getOneLineAddress(
                submitDetailsDto.getFacilityActivity().getFacility().getBlkNo(),
                submitDetailsDto.getFacilityActivity().getFacility().getStreetName(),
                submitDetailsDto.getFacilityActivity().getFacility().getFloorNo(),
                submitDetailsDto.getFacilityActivity().getFacility().getUnitNo(),
                submitDetailsDto.getFacilityActivity().getFacility().getPostalCode());
        submitDetailsDto.setFacilityAddress(facilityAddress);
        submitDetailsDto.setApplicationDt(submitDetailsDto.getFacilityActivity().getApplication().getApplicationDt());
        submitDetailsDto.setApplicationStatus(submitDetailsDto.getFacilityActivity().getApplication().getStatus());
    }
}
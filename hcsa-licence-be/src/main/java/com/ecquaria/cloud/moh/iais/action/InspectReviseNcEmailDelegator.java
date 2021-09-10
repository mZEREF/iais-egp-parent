package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.config.SystemParamConfig;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.constant.AuditTrailConsts;
import com.ecquaria.cloud.moh.iais.common.constant.HcsaConsts;
import com.ecquaria.cloud.moh.iais.common.constant.application.AppServicesConsts;
import com.ecquaria.cloud.moh.iais.common.constant.appointment.AppointmentConstants;
import com.ecquaria.cloud.moh.iais.common.constant.inspection.InspectionConstants;
import com.ecquaria.cloud.moh.iais.common.constant.message.MessageConstants;
import com.ecquaria.cloud.moh.iais.common.constant.role.RoleConsts;
import com.ecquaria.cloud.moh.iais.common.constant.sample.DemoConstants;
import com.ecquaria.cloud.moh.iais.common.constant.systemadmin.MsgTemplateConstants;
import com.ecquaria.cloud.moh.iais.common.constant.task.TaskConsts;
import com.ecquaria.cloud.moh.iais.common.dto.AuditTrailDto;
import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.application.ApplicationViewDto;
import com.ecquaria.cloud.moh.iais.common.dto.appointment.ApptNonWorkingDateDto;
import com.ecquaria.cloud.moh.iais.common.dto.appointment.ApptUserCalendarDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesInspecApptDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesRecommendationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesRoutingHistoryDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcVehicleDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationGroupDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenseeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcStageWorkingGroupDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.AdCheckListShowDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.AppInspectionStatusDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspectionEmailTemplateDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspectionFDtosDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspectionFillCheckListDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.NcAnswerDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.OrgUserDto;
import com.ecquaria.cloud.moh.iais.common.dto.task.TaskDto;
import com.ecquaria.cloud.moh.iais.common.dto.templates.MsgTemplateDto;
import com.ecquaria.cloud.moh.iais.common.utils.Formatter;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.constant.HmacConstants;
import com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant;
import com.ecquaria.cloud.moh.iais.dto.CheckListVadlidateDto;
import com.ecquaria.cloud.moh.iais.dto.LoginContext;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.helper.MasterCodeUtil;
import com.ecquaria.cloud.moh.iais.helper.NotificationHelper;
import com.ecquaria.cloud.moh.iais.helper.WebValidationHelper;
import com.ecquaria.cloud.moh.iais.service.AppPremisesRoutingHistoryService;
import com.ecquaria.cloud.moh.iais.service.ApplicationService;
import com.ecquaria.cloud.moh.iais.service.ApplicationViewService;
import com.ecquaria.cloud.moh.iais.service.InspEmailService;
import com.ecquaria.cloud.moh.iais.service.InspectionService;
import com.ecquaria.cloud.moh.iais.service.client.AppInspectionStatusClient;
import com.ecquaria.cloud.moh.iais.service.client.ApplicationClient;
import com.ecquaria.cloud.moh.iais.service.client.AppointmentClient;
import com.ecquaria.cloud.moh.iais.service.client.HcsaConfigClient;
import com.ecquaria.cloud.moh.iais.service.client.InspectionTaskClient;
import com.ecquaria.cloud.moh.iais.service.client.OrganizationClient;
import com.ecquaria.cloud.moh.iais.util.WorkDayCalculateUtil;
import com.ecquaria.cloud.moh.iais.validation.InspectionCheckListItemValidate;
import com.ecquaria.cloud.moh.iais.validation.InspectionCheckListValidation;
import com.ecquaria.sz.commons.util.MsgUtil;
import freemarker.template.TemplateException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import sop.servlet.webflow.HttpHandler;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * InspectReviseNcEmailDelegator
 *
 * @author junyu
 * @date 2019/12/11
 */
@Delegator("InspectReviseNcEmailDelegator")
@Slf4j
public class InspectReviseNcEmailDelegator extends InspectionCheckListCommonMethodDelegator{
    @Autowired
    InspEmailService inspEmailService;
    @Autowired
    InspectionService inspectionService;

    @Autowired
    ApplicationViewService applicationViewService;
    @Autowired
    private AppPremisesRoutingHistoryService appPremisesRoutingHistoryService;

    @Autowired
    private HcsaConfigClient hcsaConfigClient;
    @Autowired
    AppInspectionStatusClient appInspectionStatusClient;
    @Autowired
    ApplicationService applicationService;
    @Autowired
    OrganizationClient organizationClient;
    @Autowired
    NotificationHelper notificationHelper;
    @Autowired
    private SystemParamConfig systemParamConfig;
    @Autowired
    private AppointmentClient appointmentClient;
    @Autowired
    ApplicationClient applicationClient;
    @Autowired
    InspectionTaskClient inspectionTaskClient;
    @Value("${easmts.vehicle.sperate.flag}")
    private String vehicleOpenFlag;
    private static final String ADCHK_DTO="adchklDto";
    private static final String TASK_DTO="taskDto";
    private static final String APP_VIEW_DTO="applicationViewDto";
    private static final String COM_DTO="commonDto";
    private static final String MSG_CON="messageContent";
    private static final String EMAIL_VIEW="emailView";
    private static final String INS_EMAIL_DTO="insEmailDto";
    private static final String AC_DTO="acDto";
    private static final String TD="</td><td>";
    private static final String SUBJECT="subject";
    private static final String SER_LIST_DTO= "serListDto";
    private static final String DRA_EMA_ID="draftEmailId";

    public void start(BaseProcessClass bpc){
        log.info("=======>>>>>startStep>>>>>>>>>>>>>>>>emailRequest");
        HttpServletRequest request = bpc.request;
        clearSessionForStartCheckList(request);
        String taskId = verifyTaskId(bpc);
        if(StringUtil.isEmpty(taskId)){
            return;
        }
        TaskDto taskDto = taskService.getTaskById(taskId);
        if( taskDto == null) {
            return;
        }
        AuditTrailHelper.auditFunctionWithAppNo(AuditTrailConsts.MODULE_INSPECTION, AuditTrailConsts.FUNCTION_INSPECTION_MAIL,taskDto.getApplicationNo());
        setCheckDataHaveFinished(request,taskDto);
        ParamUtil.setSessionAttr(request,MSG_CON, null);
        ParamUtil.setSessionAttr(request,INS_EMAIL_DTO, null);
        request.setAttribute(IaisEGPConstant.CRUD_ACTION_TYPE, EMAIL_VIEW);
        //get selections dd hh
        setSelectionsForDDMMAndAuditRiskSelect(request);
        SearchParam searchParamGroup = (SearchParam)ParamUtil.getSessionAttr(bpc.request, "backendinboxSearchParam");
        ParamUtil.setSessionAttr(bpc.request,"backSearchParamFromHcsaApplication",searchParamGroup);
    }


    public void prepareData(BaseProcessClass bpc) {
        log.info("=======>>>>>prepareData>>>>>>>>>>>>>>>>emailRequest");
        HttpServletRequest request = bpc.request;
        request.setAttribute(IaisEGPConstant.CRUD_ACTION_TYPE, StringUtil.isEmpty(ParamUtil.getString(request,IaisEGPConstant.CRUD_ACTION_TYPE)) ? EMAIL_VIEW : ParamUtil.getString(request,IaisEGPConstant.CRUD_ACTION_TYPE));

    }
    public void emailSubmitStep(BaseProcessClass bpc){
        log.info("=======>>>>>emailSubmitStep>>>>>>>>>>>>>>>>emailRequest");
        HttpServletRequest request = bpc.request;
        InspectionEmailTemplateDto inspectionEmailTemplateDto = (InspectionEmailTemplateDto) ParamUtil.getSessionAttr(bpc.request,INS_EMAIL_DTO);
        String content=ParamUtil.getString(request,MSG_CON);
        ParamUtil.setSessionAttr(request,MSG_CON,content);
        ParamUtil.setSessionAttr(request,INS_EMAIL_DTO, inspectionEmailTemplateDto);
        ParamUtil.setRequestAttr(request,IaisEGPConstant.CRUD_ACTION_TYPE_VALUE,EMAIL_VIEW);
    }

    public void previewEmail(BaseProcessClass bpc){
        log.info("=======>>>>>previewEmail>>>>>>>>>>>>>>>>emailRequest");
        HttpServletRequest request = bpc.request;
        String currentAction = ParamUtil.getString(request, IaisEGPConstant.CRUD_ACTION_TYPE);
        if(!"preview".equals(currentAction)){
            return;
        }
        String content=ParamUtil.getString(request,MSG_CON);
        content=StringUtil.removeNonUtf8(content);
        String subject=ParamUtil.getString(request,SUBJECT);
        InspectionEmailTemplateDto inspectionEmailTemplateDto= (InspectionEmailTemplateDto) ParamUtil.getSessionAttr(request,INS_EMAIL_DTO);
        inspectionEmailTemplateDto.setMessageContent(content);
        inspectionEmailTemplateDto.setSubject(subject);
        inspectionEmailTemplateDto.setMessageContent(StringUtil.removeNonUtf8(inspectionEmailTemplateDto.getMessageContent()));
        ParamUtil.setSessionAttr(request,INS_EMAIL_DTO,inspectionEmailTemplateDto);
        ParamUtil.setSessionAttr(request,SUBJECT, subject);
        ParamUtil.setSessionAttr(request,MSG_CON, content);
        ParamUtil.setRequestAttr(request,IaisEGPConstant.CRUD_ACTION_TYPE_VALUE,currentAction);

    }

    public void sendEmail(BaseProcessClass bpc) {

        log.info("=======>>>>>sendEmail>>>>>>>>>>>>>>>>emailRequest");
        HttpServletRequest request = bpc.request;
        LoginContext loginContext = (LoginContext) ParamUtil.getSessionAttr(bpc.request, AppConsts.SESSION_ATTR_LOGIN_USER);
        String userId = loginContext.getUserId();
        ApplicationViewDto applicationViewDto= (ApplicationViewDto) ParamUtil.getSessionAttr(request,APP_VIEW_DTO);
        String serviceId=applicationViewDto.getApplicationDto().getServiceId();
        TaskDto taskDto= (TaskDto) ParamUtil.getSessionAttr(request,TASK_DTO);
        TaskDto taskDtoNew = taskService.getTaskById(taskDto.getId());
        if(taskDtoNew.getTaskStatus().equals(TaskConsts.TASK_STATUS_COMPLETED)){
            ParamUtil.setRequestAttr(request,"COMPLETED",Boolean.TRUE);
            return;
        }
        String decision=ParamUtil.getString(request,"decision");

        InspectionEmailTemplateDto inspectionEmailTemplateDto= (InspectionEmailTemplateDto) ParamUtil.getSessionAttr(request,INS_EMAIL_DTO);
        inspectionEmailTemplateDto.setSubject(ParamUtil.getString(request,SUBJECT));
        inspectionEmailTemplateDto.setRemarks(ParamUtil.getString(request, "Remarks"));
        inspectionEmailTemplateDto.setMessageContent(ParamUtil.getString(request,MSG_CON));
        if (inspectionEmailTemplateDto.getSubject().isEmpty()){
            Map<String,String> errorMap = IaisCommonUtils.genNewHashMap();
            ParamUtil.setRequestAttr(request, DemoConstants.ERRORMAP,errorMap);
        }
        if (inspectionEmailTemplateDto.getMessageContent().isEmpty()){
            Map<String,String> errorMap = IaisCommonUtils.genNewHashMap();
            ParamUtil.setRequestAttr(request, DemoConstants.ERRORMAP,errorMap);
        }
        if (decision.equals(InspectionConstants.PROCESS_DECI_ROTE_EMAIL_AO1_REVIEW)){
            applicationViewDto.getApplicationDto().setStatus(ApplicationConsts.APPLICATION_STATUS_PENDING_EMAIL_REVIEW);
            applicationViewService.updateApplicaiton(applicationViewDto.getApplicationDto());
            try {
                applicationService.updateFEApplicaitons(Collections.singletonList(applicationViewDto.getApplicationDto()));
            }catch (Exception e){
                log.info(e.getMessage(),e);
            }
            AppInspectionStatusDto appInspectionStatusDto1 = appInspectionStatusClient.getAppInspectionStatusByPremId(applicationViewDto.getAppPremisesCorrelationId()).getEntity();
            appInspectionStatusDto1.setStatus(InspectionConstants.INSPECTION_STATUS_PENDING_AO1_EMAIL_VERIFY);
            appInspectionStatusDto1.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
            appInspectionStatusClient.update(appInspectionStatusDto1);
            taskDto.setTaskKey(HcsaConsts.ROUTING_STAGE_INS);
            inspEmailService.completedTask(taskDto);

            AppPremisesRoutingHistoryDto appPremisesRoutingHistoryDto= appPremisesRoutingHistoryService.getAppPremisesRoutingHistoryForCurrentStage(applicationViewDto.getApplicationDto().getApplicationNo(),HcsaConsts.ROUTING_STAGE_INS,RoleConsts.USER_ROLE_AO1);
            HcsaSvcStageWorkingGroupDto hcsaSvcStageWorkingGroupDto = new HcsaSvcStageWorkingGroupDto();
            if(applicationViewDto.getApplicationDto().getRoutingServiceId()!=null){
                serviceId=applicationViewDto.getApplicationDto().getRoutingServiceId();
            }
            hcsaSvcStageWorkingGroupDto.setServiceId(serviceId);
            hcsaSvcStageWorkingGroupDto.setType(applicationViewDto.getApplicationDto().getApplicationType());
            hcsaSvcStageWorkingGroupDto.setStageId(HcsaConsts.ROUTING_STAGE_INS);
            hcsaSvcStageWorkingGroupDto.setOrder(2);
            TaskDto taskDto1=new TaskDto();
            taskDto1.setApplicationNo(applicationViewDto.getApplicationDto().getApplicationNo());
            taskDto1.setRefNo(taskDto.getRefNo());
            taskDto1.setTaskType(taskDto.getTaskType());
            taskDto1.setRoleId(RoleConsts.USER_ROLE_AO1);
            taskDto1.setProcessUrl(TaskConsts.TASK_PROCESS_URL_INSPECTION_AO1_VALIDATE_NCEMAIL);
            taskDto1.setTaskKey(HcsaConsts.ROUTING_STAGE_INS);
            taskDto1.setWkGrpId(hcsaConfigClient.getHcsaSvcStageWorkingGroupDto(hcsaSvcStageWorkingGroupDto).getEntity().getGroupId());
            taskDto1.setUserId(appPremisesRoutingHistoryDto.getActionby());
            List<TaskDto> taskDtos = prepareTaskList(taskDto1,applicationViewDto.getApplicationDto());
            taskService.createTasks(taskDtos);
            taskDto1.setWkGrpId(taskDto.getWkGrpId());
            createAppPremisesRoutingHistory(applicationViewDto.getApplicationDto().getApplicationNo(), ApplicationConsts.APPLICATION_STATUS_PENDING_EMAIL_REVIEW, InspectionConstants.PROCESS_DECI_ROTE_EMAIL_AO1_REVIEW,taskDto, userId,inspectionEmailTemplateDto.getRemarks());
            createAppPremisesRoutingHistory(applicationViewDto.getApplicationDto().getApplicationNo(), ApplicationConsts.APPLICATION_STATUS_PENDING_EMAIL_REVIEW,ApplicationConsts.APPLICATION_STATUS_PENDING_EMAIL_REVIEW, taskDto, userId,"");
        }
        if (decision.equals(InspectionConstants.PROCESS_DECI_ROTE_EMAIL_INSPECTION_LEAD_REVIEW)){
            applicationViewDto.getApplicationDto().setStatus(ApplicationConsts.APPLICATION_STATUS_PENDING_EMAIL_SENDING);
            applicationViewService.updateApplicaiton(applicationViewDto.getApplicationDto());
            try {
                applicationService.updateFEApplicaitons(Collections.singletonList(applicationViewDto.getApplicationDto()));
            }catch (Exception e){
                log.info(e.getMessage(),e);
            }
            AppInspectionStatusDto appInspectionStatusDto1 = appInspectionStatusClient.getAppInspectionStatusByPremId(applicationViewDto.getAppPremisesCorrelationId()).getEntity();
            appInspectionStatusDto1.setStatus(InspectionConstants.INSPECTION_STATUS_PENDING_JOB_CREATE_TASK_TO_LEADER);
            appInspectionStatusDto1.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
            appInspectionStatusClient.update(appInspectionStatusDto1);
            taskDto.setTaskKey(HcsaConsts.ROUTING_STAGE_INS);
            inspEmailService.completedTask(taskDto);
            createAppPremisesRoutingHistory(applicationViewDto.getApplicationDto().getApplicationNo(), ApplicationConsts.APPLICATION_STATUS_PENDING_RE_DRAFT_LETTER,InspectionConstants.PROCESS_DECI_ROTE_EMAIL_INSPECTION_LEAD_REVIEW, taskDto, userId,inspectionEmailTemplateDto.getRemarks());
            createAppPremisesRoutingHistory(applicationViewDto.getApplicationDto().getApplicationNo(), ApplicationConsts.APPLICATION_STATUS_PENDING_RE_DRAFT_LETTER,ApplicationConsts.APPLICATION_STATUS_PENDING_EMAIL_SENDING, taskDto, userId,"");

        }
        inspEmailService.updateEmailDraft(inspectionEmailTemplateDto);
        ParamUtil.setSessionAttr(request,INS_EMAIL_DTO, inspectionEmailTemplateDto);

    }
    public void doRecallEmail(BaseProcessClass bpc) {
        log.info("=======>>>>>doRecallEmail>>>>>>>>>>>>>>>>emailRequest");
        HttpServletRequest request = bpc.request;
        request.setAttribute(IaisEGPConstant.CRUD_ACTION_TYPE, EMAIL_VIEW);
    }

    public void preCheckList(BaseProcessClass bpc) {

        log.info("=======>>>>>preCheckList>>>>>>>>>>>>>>>>emailRequest");
        ParamUtil.setRequestAttr(bpc.request,IaisEGPConstant.CRUD_ACTION_TYPE_VALUE,"checkList");
    }
    public void checkListNext(BaseProcessClass bpc)  {
        log.info("=======>>>>>checkListNext>>>>>>>>>>>>>>>>emailRequest");
        HttpServletRequest request = bpc.request;
        ParamUtil.setRequestAttr(request,IaisEGPConstant.CRUD_ACTION_TYPE, ParamUtil.getSessionAttr(request, IaisEGPConstant.CRUD_ACTION_TYPE+"Step2"));
        String saveFlag = ParamUtil.getString(request,"saveflag");
        if(StringUtil.isEmpty( saveFlag)){
            ParamUtil.setRequestAttr(request, IaisEGPConstant.ISVALID, IaisEGPConstant.YES);
            return;
        }
        InspectionFDtosDto serListDto = (InspectionFDtosDto)ParamUtil.getSessionAttr(request,SER_LIST_DTO);
        String viewChkFlag = ParamUtil.getString(request,"viewchk");
        if("uploadFileLetter".equalsIgnoreCase(viewChkFlag)){
            serListDto.setCheckListTab("chkList");
            ParamUtil.setSessionAttr(request,SER_LIST_DTO,serListDto);
            ParamUtil.setRequestAttr(request, IaisEGPConstant.ISVALID, IaisEGPConstant.NO);
        }else {
            TaskDto taskDto = (TaskDto)ParamUtil.getSessionAttr(request,TASK_DTO);
            InspectionFillCheckListDto commonDto = (InspectionFillCheckListDto)ParamUtil.getSessionAttr(request,COM_DTO);
            AdCheckListShowDto adchklDto = (AdCheckListShowDto)ParamUtil.getSessionAttr(request,ADCHK_DTO);
            List<NcAnswerDto> ncDtoList = insepctionNcCheckListService.getNcAnswerDtoList(taskDto.getRefNo());
            InspectionCheckListValidation inspectionCheckListValidation = new InspectionCheckListValidation();
            Map<String, String> errMap = inspectionCheckListValidation.validate(request);
            ParamUtil.setSessionAttr(request,AC_DTO, (Serializable) ncDtoList);
            if(!errMap.isEmpty()){
                ParamUtil.setRequestAttr(request, IaisEGPConstant.ISVALID, IaisEGPConstant.NO);
                ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errMap));
            }else{
                ParamUtil.setRequestAttr(request, IaisEGPConstant.ISVALID, IaisEGPConstant.YES);
                saveCheckList(request,commonDto,adchklDto,serListDto,taskDto.getRefNo());
                ApplicationViewDto appViewDto =(ApplicationViewDto) ParamUtil.getSessionAttr(request,APP_VIEW_DTO);
                insepctionNcCheckListService.saveLicPremisesAuditDtoByApplicationViewDto(appViewDto);
                ParamUtil.setSessionAttr(request,SER_LIST_DTO,serListDto);
            }
        }

    }
    public void preEmailView(BaseProcessClass bpc)  {
        log.info("=======>>>>>preEmailView>>>>>>>>>>>>>>>>emailRequest");
        HttpServletRequest request = bpc.request;
        TaskDto taskDto = (TaskDto) ParamUtil.getSessionAttr(bpc.request, TASK_DTO);
        String correlationId = taskDto.getRefNo();
        InspectionEmailTemplateDto inspectionEmailTemplateDto;
        if(ParamUtil.getSessionAttr(request,INS_EMAIL_DTO)!=null){
            inspectionEmailTemplateDto= (InspectionEmailTemplateDto) ParamUtil.getSessionAttr(request,INS_EMAIL_DTO);
        }
        else {
            inspectionEmailTemplateDto= inspEmailService.getInsertEmail(correlationId);
        }
        inspectionEmailTemplateDto.setMessageContent(StringUtil.removeNonUtf8(inspectionEmailTemplateDto.getMessageContent()));
        ParamUtil.setSessionAttr(request,DRA_EMA_ID,inspectionEmailTemplateDto.getId());
        ParamUtil.setSessionAttr(request,INS_EMAIL_DTO, inspectionEmailTemplateDto);
        ParamUtil.setRequestAttr(request,IaisEGPConstant.CRUD_ACTION_TYPE_VALUE,"emailView");
    }

    public void preProcess(BaseProcessClass bpc)  {
        log.info("=======>>>>>preProcess>>>>>>>>>>>>>>>>emailRequest");
        HttpServletRequest request = bpc.request;
        TaskDto taskDto = (TaskDto) ParamUtil.getSessionAttr(bpc.request, TASK_DTO);
        LoginContext loginContext = (LoginContext) ParamUtil.getSessionAttr(bpc.request, AppConsts.SESSION_ATTR_LOGIN_USER);
        String userId = loginContext.getUserId();
        String correlationId = taskDto.getRefNo();
        InspectionEmailTemplateDto inspectionEmailTemplateDto;
        if(ParamUtil.getSessionAttr(request,INS_EMAIL_DTO)!=null){
            inspectionEmailTemplateDto= (InspectionEmailTemplateDto) ParamUtil.getSessionAttr(request,INS_EMAIL_DTO);
        }
        else {
            inspectionEmailTemplateDto= inspEmailService.getInsertEmail(correlationId);
        }
        ApplicationViewDto applicationViewDto = inspEmailService.getAppViewByCorrelationId(correlationId);
        List<AppPremisesRoutingHistoryDto> appPremisesRoutingHistoryDtos= appPremisesRoutingHistoryService.getAppPremisesRoutingHistoryDtosByAppNo(applicationViewDto.getApplicationDto().getApplicationNo());
        for(AppPremisesRoutingHistoryDto appPremisesRoutingHistoryDto1:appPremisesRoutingHistoryDtos){

            if(!StringUtil.isEmpty(appPremisesRoutingHistoryDto1.getWrkGrpId())) {
                appPremisesRoutingHistoryDto1.setWrkGrpId(applicationViewService.getWrkGrpName(appPremisesRoutingHistoryDto1.getWrkGrpId()));
            }
            if(StringUtil.isEmpty(appPremisesRoutingHistoryDto1.getActionby())){
                appPremisesRoutingHistoryDto1.setActionby("-");
            }
            else {
                appPremisesRoutingHistoryDto1.setActionby(applicationViewService.getUserNameById(new ArrayList<String>(Collections.singleton(appPremisesRoutingHistoryDto1.getActionby()))).get(0).getDisplayName());
            }
            if(StringUtil.isEmpty(appPremisesRoutingHistoryDto1.getProcessDecision())){
                if(StringUtil.isEmpty(appPremisesRoutingHistoryDto1.getAppStatus())){
                    appPremisesRoutingHistoryDto1.setProcessDecision(MasterCodeUtil.retrieveOptionsByCodes(new String[]{appPremisesRoutingHistoryDto1.getAppStatus()}).get(0).getText());
                }else {
                    appPremisesRoutingHistoryDto1.setProcessDecision("-");
                }
            }else {
                appPremisesRoutingHistoryDto1.setProcessDecision(MasterCodeUtil.retrieveOptionsByCodes(new String[]{appPremisesRoutingHistoryDto1.getProcessDecision()}).get(0).getText());
            }
        }
        List<SelectOption> appTypeOption=MasterCodeUtil.retrieveOptionsByCodes(new String[]{InspectionConstants.PROCESS_DECI_ROTE_EMAIL_INSPECTION_LEAD_REVIEW});
        AppPremisesRoutingHistoryDto appPremisesRoutingHistoryDto= appPremisesRoutingHistoryService.getAppPremisesRoutingHistoryForCurrentStage(applicationViewDto.getApplicationDto().getApplicationNo(),HcsaConsts.ROUTING_STAGE_INS,RoleConsts.USER_ROLE_INSPECTION_LEAD);
        AppPremisesRoutingHistoryDto appPremisesRoutingHistoryDto1= appPremisesRoutingHistoryService.getAppPremisesRoutingHistoryForCurrentStage(applicationViewDto.getApplicationDto().getApplicationNo(),HcsaConsts.ROUTING_STAGE_INS,RoleConsts.USER_ROLE_AO1);


        if(appPremisesRoutingHistoryDto==null){
            appTypeOption = MasterCodeUtil.retrieveOptionsByCodes(new String[]{InspectionConstants.PROCESS_DECI_ROTE_EMAIL_AO1_REVIEW});
        }
        if (appPremisesRoutingHistoryDto1==null) {
            appTypeOption=MasterCodeUtil.retrieveOptionsByCodes(new String[]{InspectionConstants.PROCESS_DECI_ROTE_EMAIL_INSPECTION_LEAD_REVIEW});
        }
        if(appPremisesRoutingHistoryDto!=null&&appPremisesRoutingHistoryDto1!=null){
            try {
                String ao1=new SimpleDateFormat(AppConsts.DEFAULT_DATE_TIME_FORMAT).format(appPremisesRoutingHistoryDto1.getUpdatedDt());
                String lead=new SimpleDateFormat(AppConsts.DEFAULT_DATE_TIME_FORMAT).format(appPremisesRoutingHistoryDto.getUpdatedDt());
                if(lead.compareTo(ao1) <= 0) {
                    appTypeOption = MasterCodeUtil.retrieveOptionsByCodes(new String[]{InspectionConstants.PROCESS_DECI_ROTE_EMAIL_AO1_REVIEW});
                }
            }catch (Exception e){
                log.error(e.getMessage(), e);
            }
        }


        String content= (String) ParamUtil.getSessionAttr(request,MSG_CON);
        if(content!=null){
            inspectionEmailTemplateDto.setMessageContent(content);
        }
        inspectionEmailTemplateDto.setAppStatus(MasterCodeUtil.retrieveOptionsByCodes(new String[]{applicationViewDto.getApplicationDto().getStatus()}).get(0).getText());
        inspectionEmailTemplateDto.setMessageContent(StringUtil.removeNonUtf8(inspectionEmailTemplateDto.getMessageContent()));
        ParamUtil.setRequestAttr(request,"appPremisesRoutingHistoryDtos", appPremisesRoutingHistoryDtos);
        ParamUtil.setRequestAttr(request,"appTypeOption", appTypeOption);
        ParamUtil.setSessionAttr(request,DRA_EMA_ID,inspectionEmailTemplateDto.getId());
        ParamUtil.setSessionAttr(request,INS_EMAIL_DTO, inspectionEmailTemplateDto);
        ParamUtil.setRequestAttr(request,IaisEGPConstant.CRUD_ACTION_TYPE_VALUE,"processing");
    }

    public void doProcessing(BaseProcessClass bpc){
        log.info("=======>>>>>doProcessing>>>>>>>>>>>>>>>>emailRequest");
        HttpServletRequest request = bpc.request;
        ParamUtil.setRequestAttr(request,IaisEGPConstant.CRUD_ACTION_TYPE_VALUE,"processing");
        InspectionEmailTemplateDto inspectionEmailTemplateDto= (InspectionEmailTemplateDto) ParamUtil.getSessionAttr(request,INS_EMAIL_DTO);
        inspectionEmailTemplateDto.setRemarks(ParamUtil.getString(request, "Remarks"));
        ParamUtil.setSessionAttr(request,INS_EMAIL_DTO,inspectionEmailTemplateDto);
    }

    public void emailView(BaseProcessClass bpc) {
        log.info("=======>>>>>emailView>>>>>>>>>>>>>>>>emailRequest");
        HttpServletRequest request = bpc.request;
        request.setAttribute(IaisEGPConstant.CRUD_ACTION_TYPE, EMAIL_VIEW);
    }


    @GetMapping(value = "/reload-rev-email")
    public @ResponseBody
    String reloadRevEmail(HttpServletRequest request) throws IOException, TemplateException {
        //String templateId="08BDA324-5D13-EA11-BE78-000C29D29DB0";
        TaskDto taskDto = (TaskDto) ParamUtil.getSessionAttr(request, TASK_DTO);
        String correlationId = taskDto.getRefNo();
        ApplicationViewDto applicationViewDto = inspEmailService.getAppViewByCorrelationId(correlationId);
        String appNo=applicationViewDto.getApplicationDto().getApplicationNo();
        String licenseeId=applicationViewDto.getApplicationGroupDto().getLicenseeId();
        LicenseeDto licenseeDto=inspEmailService.getLicenseeDtoById(licenseeId);
        String applicantName=licenseeDto.getName();
        InspectionEmailTemplateDto inspectionEmailTemplateDto = new InspectionEmailTemplateDto();
        inspectionEmailTemplateDto.setAppPremCorrId(correlationId);
        inspectionEmailTemplateDto.setApplicantName(applicantName);
        inspectionEmailTemplateDto.setApplicationNumber(appNo);
        inspectionEmailTemplateDto.setHciCode(applicationViewDto.getHciCode());
        inspectionEmailTemplateDto.setHciNameOrAddress(applicationViewDto.getHciName()+"/"+applicationViewDto.getHciAddress());
        HcsaServiceDto hcsaServiceDto=inspectionService.getHcsaServiceDtoByServiceId(applicationViewDto.getApplicationDto().getServiceId());
        inspectionEmailTemplateDto.setServiceName(hcsaServiceDto.getSvcName());
        List<NcAnswerDto> ncAnswerDtos=insepctionNcCheckListService.getNcAnswerDtoList(correlationId);
        AppPremisesRecommendationDto appPreRecommentdationDto =insepctionNcCheckListService.getAppRecomDtoByAppCorrId(correlationId,InspectionConstants.RECOM_TYPE_TCU);
        String mesContext;
        {
            List<String> leads = organizationClient.getInspectionLead(taskDto.getWkGrpId()).getEntity();
            List<TaskDto> taskScoreDtos = taskService.getTaskDtoScoresByWorkGroupId(taskDto.getWkGrpId());
            String lead = inspEmailService.getLeadWithTheFewestScores(taskScoreDtos, leads);
            OrgUserDto leadDto=organizationClient.retrieveOrgUserAccountById(lead).getEntity();
            String loginUrl = HmacConstants.HTTPS +"://" + systemParamConfig.getInterServerName() + MessageConstants.MESSAGE_INBOX_URL_INTER_LOGIN;
            MsgTemplateDto msgTemplateDto= notificationHelper.getMsgTemplate(MsgTemplateConstants.MSG_TEMPLATE_EN_INS_002_INSPECTOR_EMAIL);
            Map<String,Object> mapTemplate=IaisCommonUtils.genNewHashMap();
            mapTemplate.put("inspection_lead", leadDto.getDisplayName());
            ApplicationGroupDto applicationGroupDto = applicationClient.getAppById(applicationViewDto.getApplicationDto().getAppGrpId()).getEntity();
            if (applicationGroupDto != null){
                OrgUserDto orgUserDto = organizationClient.retrieveOrgUserAccountById(applicationGroupDto.getSubmitBy()).getEntity();
                if (orgUserDto != null){
                    mapTemplate.put("ApplicantName", orgUserDto.getDisplayName());
                }
            }
            mapTemplate.put("ApplicationType", MasterCodeUtil.retrieveOptionsByCodes(new String[]{applicationViewDto.getApplicationDto().getApplicationType()}).get(0).getText());
            mapTemplate.put("ApplicationNumber", applicationViewDto.getApplicationDto().getApplicationNo());
            applicationViewDto.setSubmissionDate(IaisEGPHelper.parseToString(IaisEGPHelper.parseToDate( applicationViewDto.getSubmissionDate(),"dd/MM/yyyy HH:mm:ss"),"dd/MM/yyyy"));
            mapTemplate.put("ApplicationDate", applicationViewDto.getSubmissionDate());
            mapTemplate.put("systemLink", loginUrl);
            mapTemplate.put("HCI_CODE", applicationViewDto.getHciCode());
            mapTemplate.put("HCI_NAME",applicationViewDto.getHciName());
            mapTemplate.put("Address", applicationViewDto.getHciAddress());
            mapTemplate.put("HCI_Postal_Code", applicationViewDto.getHciPostalCode());
            mapTemplate.put("LicenseeName", licenseeDto.getName());
            AppPremisesRecommendationDto appPreRecommentdationDtoInspectionDate =insepctionNcCheckListService.getAppRecomDtoByAppCorrId(correlationId,InspectionConstants.RECOM_TYPE_INSEPCTION_DATE);
            if(appPreRecommentdationDtoInspectionDate!=null){
                mapTemplate.put("InspectionDate", Formatter.formatDate(appPreRecommentdationDtoInspectionDate.getRecomInDate()));
            }else {
                mapTemplate.put("InspectionDate", "-");
            }
//cancel old calendar
            AppPremisesInspecApptDto appPremisesInspecApptDto=inspectionTaskClient.getSpecificDtoByAppPremCorrId(correlationId).getEntity();
            ApptUserCalendarDto cancelCalendarDto = new ApptUserCalendarDto();
            AuditTrailDto auditTrailDto = IaisEGPHelper.getCurrentAuditTrailDto();
            if(appPremisesInspecApptDto!=null&&appPremisesInspecApptDto.getApptRefNo()!=null){
                cancelCalendarDto.setApptRefNo(appPremisesInspecApptDto.getApptRefNo());
                cancelCalendarDto.setAuditTrailDto(auditTrailDto);
                cancelCalendarDto.setStatus(AppointmentConstants.CALENDAR_STATUS_RESERVED);
                try {
                    List<ApptUserCalendarDto> apptUserCalendarDtos= appointmentClient.getCalenderByApptRefNoAndStatus(cancelCalendarDto).getEntity();
                    if(apptUserCalendarDtos!=null&&!apptUserCalendarDtos.isEmpty()&&apptUserCalendarDtos.get(0).getEndSlot()!=null){
                        Date startDt= apptUserCalendarDtos.get(0).getStartSlot().get(0);
                        Date endDt= apptUserCalendarDtos.get(0).getEndSlot().get(0);
                        Calendar cal = Calendar.getInstance();
                        cal.setTime(startDt);
                        cal.add(Calendar.DAY_OF_MONTH, 1);
                        String inspStartDate_1 = Formatter.formatDateTime(cal.getTime());
                        String inspEndDate = Formatter.formatDateTime(endDt);
                        if(inspStartDate_1.compareTo(inspEndDate)>0){
                            mapTemplate.put("InspectionEndDate", Formatter.formatDate(endDt));
                        }
                    }
                }catch (Exception e){
                    log.info(e.getMessage(),e);
                }
            }
            Map<String,Object> mapTableTemplate=IaisCommonUtils.genNewHashMap();
            MsgTemplateDto msgTableTemplateDto= notificationHelper.getMsgTemplate(MsgTemplateConstants.MSG_TEMPLATE_EN_INS_002_TABLE_12);

            if(ncAnswerDtos.size()!=0){
                StringBuilder stringBuilder=new StringBuilder();
                stringBuilder.append("<tr><td colspan=\"6\"><b>").append(applicationViewDto.getServiceType()).append("</b></td></tr>");
                int i=0;
                for (NcAnswerDto ncAnswerDto:ncAnswerDtos
                ) {
                    stringBuilder.append("<tr><td>").append(++i);
                    //EAS or MTS
                    if(vehicleOpenFlag.equals(InspectionConstants.SWITCH_ACTION_YES)
                            &&applicationViewDto.getAppSvcVehicleDtos()!=null
                            &&(applicationViewDto.getSvcCode().equals(AppServicesConsts.SERVICE_CODE_EMERGENCY_AMBULANCE_SERVICE)
                            ||applicationViewDto.getSvcCode().equals(AppServicesConsts.SERVICE_CODE_MEDICAL_TRANSPORT_SERVICE))){
                        boolean isDisplayName=false;
                        for (AppSvcVehicleDto asvd:applicationViewDto.getAppSvcVehicleDtos()
                             ) {
                            if(asvd.getVehicleName().equals(ncAnswerDto.getVehicleName())){
                                stringBuilder.append(TD).append(StringUtil.viewHtml(asvd.getDisplayName()));
                                isDisplayName=true;
                                break;
                            }
                        }
                        if(!isDisplayName){
                            stringBuilder.append(TD).append(StringUtil.viewHtml(ncAnswerDto.getVehicleName()));
                        }
                    }else {
                        stringBuilder.append(TD).append(StringUtil.viewHtml(ncAnswerDto.getType()));
                    }
                    stringBuilder.append(TD).append(StringUtil.viewHtml(ncAnswerDto.getItemQuestion()));
                    stringBuilder.append(TD).append(StringUtil.viewHtml(ncAnswerDto.getNcs()));
                    stringBuilder.append(TD).append(StringUtil.viewHtml(ncAnswerDto.getRemark()));
                    stringBuilder.append(TD).append(StringUtil.viewHtml("1".equals(ncAnswerDto.getRef())?"Yes":"No"));
                    stringBuilder.append("</td></tr>");
                }
                mapTableTemplate.put("NC_DETAILS",StringUtil.viewHtml(stringBuilder.toString()));
            }
            String observation = fillupChklistService.getObservationByAppPremCorrId(taskDto.getRefNo());
            if(appPreRecommentdationDto!=null&&(appPreRecommentdationDto.getBestPractice()!=null||observation!=null)){
                int sn=1;
                String[] observations=new String[]{};
                if(observation!=null){
                    observations=observation.split("\n");
                }
                String[] recommendations=new String[]{};
                if(appPreRecommentdationDto.getBestPractice()!=null){
                    recommendations=appPreRecommentdationDto.getBestPractice().split("\n");
                }
                StringBuilder stringBuilder=new StringBuilder();
                if(recommendations.length>=observations.length){
                    for (int i=0;i<recommendations.length;i++){
                        if(i<observations.length){
                            stringBuilder.append("<tr><td>").append(sn).append(TD).append(StringUtil.viewHtml(observations[i])).append(TD).append(StringUtil.viewHtml(recommendations[i])).append("</td></tr>");
                        }else {
                            stringBuilder.append("<tr><td>").append(sn).append(TD).append(StringUtil.viewHtml("")).append(TD).append(StringUtil.viewHtml(recommendations[i])).append("</td></tr>");
                        }
                        sn++;

                    }
                }else {
                    for (int i=0;i<observations.length;i++){
                        if(i<recommendations.length){
                            stringBuilder.append("<tr><td>").append(sn).append(TD).append(StringUtil.viewHtml(observations[i])).append(TD).append(StringUtil.viewHtml(recommendations[i])).append("</td></tr>");
                        }else {
                            stringBuilder.append("<tr><td>").append(sn).append(TD).append(StringUtil.viewHtml(observations[i])).append(TD).append(StringUtil.viewHtml("")).append("</td></tr>");
                        }
                        sn++;
                    }
                }
                mapTableTemplate.put("Observation_Recommendation",StringUtil.viewHtml(stringBuilder.toString()));
            }
            msgTableTemplateDto.setMessageContent(MsgUtil.getTemplateMessageByContent(msgTableTemplateDto.getMessageContent(),mapTableTemplate));

            mapTemplate.put("NC_DETAILS_AND_Observation_Recommendation",msgTableTemplateDto.getMessageContent());
            HcsaServiceDto svcDto = hcsaConfigClient.getHcsaServiceDtoByServiceId(applicationViewDto.getApplicationDto().getServiceId()).getEntity();
            if("NSH".equals(svcDto.getSvcCode())){
                mapTemplate.put("SVC_NSH","is Nursing Home");
            }
            mapTemplate.put("HALP", AppConsts.MOH_SYSTEM_NAME);
            AppPremisesRecommendationDto appPreRecommentdationDtoInspDate =insepctionNcCheckListService.getAppRecomDtoByAppCorrId(applicationViewDto.getAppPremisesCorrelationId(),InspectionConstants.RECOM_TYPE_INSEPCTION_DATE);
            List<Date> holidays = appointmentClient.getHolidays().getEntity();
            String groupName = organizationClient.getWrkGrpById(taskDto.getWkGrpId()).getEntity().getGroupName();
            List<ApptNonWorkingDateDto> nonWorkingDateListByWorkGroupId = appointmentClient.getNonWorkingDateListByWorkGroupId(AppConsts.MOH_IAIS_SYSTEM_APPT_CLIENT_KEY,groupName).getEntity();
            Date addTenDays= WorkDayCalculateUtil.getDate(appPreRecommentdationDtoInspDate.getRecomInDate(),systemParamConfig.getRectificateDay(),holidays,nonWorkingDateListByWorkGroupId);
            mapTemplate.put("DDMMYYYY", StringUtil.viewHtml(Formatter.formatDateTime(addTenDays,Formatter.DATE)));
            mapTemplate.put("Inspector_mail_Address", leadDto.getEmail());
            mapTemplate.put("InspectorDID", leadDto.getOfficeTelNo());
            mapTemplate.put("MOH_AGENCY_NAME", "<b>"+AppConsts.MOH_AGENCY_NAME+"</b>");
            msgTemplateDto.setMessageContent(MsgUtil.getTemplateMessageByContent(msgTemplateDto.getMessageContent(),mapTemplate));

            int index = 1;
            String replaceStr = "num_rep";
            while(msgTemplateDto.getMessageContent().contains(replaceStr)){
                msgTemplateDto.setMessageContent(msgTemplateDto.getMessageContent().replaceFirst(replaceStr,  index + "."));
                index++;
            }
            mesContext= msgTemplateDto.getMessageContent();
            inspectionEmailTemplateDto.setSubject(MsgUtil.getTemplateMessageByContent(msgTemplateDto.getTemplateName(),mapTemplate));
        }

        // mesContext= MsgUtil.getTemplateMessageByContent(inspectionEmailTemplateDto.getMessageContent(),map);

        inspectionEmailTemplateDto.setMessageContent(mesContext);
        String draftEmailId= (String) ParamUtil.getSessionAttr(request,DRA_EMA_ID);
        inspectionEmailTemplateDto.setId(draftEmailId);
        inspEmailService.updateEmailDraft(inspectionEmailTemplateDto);
        ParamUtil.setSessionAttr(request,INS_EMAIL_DTO, inspectionEmailTemplateDto);
        return mesContext;
    }
    public CheckListVadlidateDto getValueFromPage(HttpServletRequest request) throws IOException{
        MultipartHttpServletRequest mulReq = (MultipartHttpServletRequest)request.getAttribute(HttpHandler.SOP6_MULTIPART_REQUEST);
        CheckListVadlidateDto dto = new CheckListVadlidateDto();
        getDataFromPage(request);
        getOtherInfo(mulReq);
        getCommonDataFromPage(request);
        getAdhocDtoFromPage(request);
        return dto;
    }

    private AppPremisesRoutingHistoryDto createAppPremisesRoutingHistory(String appNo, String appStatus, String decision,
                                                                         TaskDto taskDto, String userId, String remarks) {
        AppPremisesRoutingHistoryDto appPremisesRoutingHistoryDto = new AppPremisesRoutingHistoryDto();
        appPremisesRoutingHistoryDto.setApplicationNo(appNo);
        appPremisesRoutingHistoryDto.setStageId(HcsaConsts.ROUTING_STAGE_INS);
        appPremisesRoutingHistoryDto.setProcessDecision(decision);
        appPremisesRoutingHistoryDto.setAppStatus(appStatus);
        appPremisesRoutingHistoryDto.setActionby(userId);
        appPremisesRoutingHistoryDto.setInternalRemarks(remarks);
        appPremisesRoutingHistoryDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        appPremisesRoutingHistoryDto.setRoleId(taskDto.getRoleId());
        appPremisesRoutingHistoryDto.setWrkGrpId(taskDto.getWkGrpId());
        appPremisesRoutingHistoryDto.setSubStage(HcsaConsts.ROUTING_STAGE_POT);
        appPremisesRoutingHistoryDto = appPremisesRoutingHistoryService.createAppPremisesRoutingHistory(appPremisesRoutingHistoryDto);
        return appPremisesRoutingHistoryDto;
    }



    private List<TaskDto> prepareTaskList(TaskDto taskDto, ApplicationDto applicationDto)  {
        List<TaskDto> list = IaisCommonUtils.genNewArrayList();
        List<ApplicationDto> applicationDtos= IaisCommonUtils.genNewArrayList();
        applicationDtos.add(applicationDto);
        List<HcsaSvcStageWorkingGroupDto> hcsaSvcStageWorkingGroupDtos = inspEmailService.generateHcsaSvcStageWorkingGroupDtos(applicationDtos, HcsaConsts.ROUTING_STAGE_INS);
        hcsaSvcStageWorkingGroupDtos = taskService.getTaskConfig(hcsaSvcStageWorkingGroupDtos);
        Integer count = hcsaSvcStageWorkingGroupDtos.get(0).getCount();

        taskDto.setId(null);
        if (StringUtil.isEmpty(taskDto.getUserId())) {
            taskDto.setDateAssigned(null);
        } else {
            taskDto.setDateAssigned(new Date());
        }
        taskDto.setSlaDateCompleted(null);
        taskDto.setSlaRemainInDays(null);
        taskDto.setScore(count);
        taskDto.setSlaAlertInDays(2);
        taskDto.setPriority(0);
        taskDto.setSlaInDays(5);
        taskDto.setTaskStatus(TaskConsts.TASK_STATUS_PENDING);
        taskDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        list.add(taskDto);
        return list;
    }




    public void doCheckList(BaseProcessClass bpc){
        log.info("=======>>>>>doCheckList>>>>>>>>>>>>>>>>doCheckList");
        HttpServletRequest request = bpc.request;
        setCheckListData(request);
        InspectionFDtosDto serListDto = (InspectionFDtosDto)ParamUtil.getSessionAttr(request,SER_LIST_DTO);
        String doSubmitAction = ParamUtil.getString(request,"doSubmitAction");
        if(InspectionCheckListItemValidate.NEXT_ACTION.equalsIgnoreCase(doSubmitAction)) {
            InspectionCheckListItemValidate inspectionCheckListItemValidate = new InspectionCheckListItemValidate();
            Map errMap = inspectionCheckListItemValidate.validate(request);
            if (!errMap.isEmpty()) {
                ParamUtil.setRequestAttr(request, IaisEGPConstant.ISVALID, IaisEGPConstant.NO);
                serListDto.setCheckListTab("chkList");
                ParamUtil.setSessionAttr(request, SER_LIST_DTO, serListDto);
                ParamUtil.setRequestAttr(request, IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errMap));
            } else {
                serListDto.setCheckListTab("chkList");
                ParamUtil.setSessionAttr(request, SER_LIST_DTO, serListDto);
                ParamUtil.setRequestAttr(request, IaisEGPConstant.ISVALID, IaisEGPConstant.YES);
            }
            setChangeTabForChecklist(request);
        }else {
            serListDto.setCheckListTab("chkList");
            ParamUtil.setSessionAttr(request,SER_LIST_DTO,serListDto);
            ParamUtil.setRequestAttr(request, IaisEGPConstant.ISVALID, IaisEGPConstant.YES);
        }
    }


    public void preViewCheckList(BaseProcessClass bpc) throws IOException{
        log.info("=======>>>>>preViewCheckList>>>>>>>>>>>>>>>>preViewCheckList");
        MultipartHttpServletRequest mulReq = (MultipartHttpServletRequest) bpc.request.getAttribute(HttpHandler.SOP6_MULTIPART_REQUEST);
        HttpServletRequest request  = bpc.request;
        String crudActionType = mulReq.getParameter(IaisEGPConstant.CRUD_ACTION_TYPE);
        ParamUtil.setRequestAttr(request, IaisEGPConstant.CRUD_ACTION_TYPE, crudActionType);
        ParamUtil.setSessionAttr(request, IaisEGPConstant.CRUD_ACTION_TYPE+"Step2", crudActionType);
        getOtherInfo(mulReq);
    }


}

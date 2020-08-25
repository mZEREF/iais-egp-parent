package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.config.SystemParamConfig;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.constant.HcsaConsts;
import com.ecquaria.cloud.moh.iais.common.constant.appointment.AppointmentConstants;
import com.ecquaria.cloud.moh.iais.common.constant.inspection.InspectionConstants;
import com.ecquaria.cloud.moh.iais.common.constant.intranetUser.IntranetUserConstant;
import com.ecquaria.cloud.moh.iais.common.constant.message.MessageConstants;
import com.ecquaria.cloud.moh.iais.common.constant.role.RoleConsts;
import com.ecquaria.cloud.moh.iais.common.constant.sample.DemoConstants;
import com.ecquaria.cloud.moh.iais.common.constant.systemadmin.MsgTemplateConstants;
import com.ecquaria.cloud.moh.iais.common.constant.task.TaskConsts;
import com.ecquaria.cloud.moh.iais.common.dto.AuditTrailDto;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.application.ApplicationViewDto;
import com.ecquaria.cloud.moh.iais.common.dto.appointment.ApptUserCalendarDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.appeal.AppPremisesSpecialDocDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesInspecApptDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesRecommendationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesRoutingHistoryDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenseeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcStageWorkingGroupDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.AdCheckListShowDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.AdhocNcCheckItemDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.AppInspectionStatusDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspectionCheckQuestionDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspectionEmailTemplateDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspectionFDtosDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspectionFillCheckListDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.LicPremisesAuditDto;
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
import com.ecquaria.cloud.moh.iais.service.FillupChklistService;
import com.ecquaria.cloud.moh.iais.service.InsepctionNcCheckListService;
import com.ecquaria.cloud.moh.iais.service.InspEmailService;
import com.ecquaria.cloud.moh.iais.service.InspectionService;
import com.ecquaria.cloud.moh.iais.service.TaskService;
import com.ecquaria.cloud.moh.iais.service.client.AppInspectionStatusClient;
import com.ecquaria.cloud.moh.iais.service.client.AppointmentClient;
import com.ecquaria.cloud.moh.iais.service.client.HcsaConfigClient;
import com.ecquaria.cloud.moh.iais.service.client.InspectionTaskClient;
import com.ecquaria.cloud.moh.iais.service.client.OrganizationClient;
import com.ecquaria.cloud.moh.iais.util.LicenceUtil;
import com.ecquaria.cloud.moh.iais.validation.InspectionCheckListItemValidate;
import com.ecquaria.cloud.moh.iais.validation.InspectionCheckListValidation;
import com.ecquaria.sz.commons.util.FileUtil;
import com.ecquaria.sz.commons.util.MsgUtil;
import freemarker.template.TemplateException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import sop.servlet.webflow.HttpHandler;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
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
public class InspectReviseNcEmailDelegator {
    @Autowired
    InspEmailService inspEmailService;
    @Autowired
    InspectionService inspectionService;
    @Autowired
    private TaskService taskService;
    @Autowired
    ApplicationViewService applicationViewService;
    @Autowired
    private AppPremisesRoutingHistoryService appPremisesRoutingHistoryService;
    @Autowired
    InsepctionNcCheckListService insepctionNcCheckListService;
    @Autowired
    FillupChklistService fillupChklistService;
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
    InspectionTaskClient inspectionTaskClient;
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
        String taskId = ParamUtil.getMaskedString(request,"taskId");
        AuditTrailHelper.auditFunction("Checklist Management", "Post Inspection Task");
        TaskDto  taskDto = fillupChklistService.getTaskDtoById(taskId);
        String appPremCorrId = taskDto.getRefNo();
        List<InspectionFillCheckListDto> cDtoList = fillupChklistService.getInspectionFillCheckListDtoListForReview(taskId,"service");
        List<InspectionFillCheckListDto> commonList = fillupChklistService.getInspectionFillCheckListDtoListForReview(taskId,"common");
        InspectionFillCheckListDto commonDto = null;
        List<InspectionFillCheckListDto> inspectionFillCheckListDtos = new ArrayList<>(2);
        if(commonList!=null && !commonList.isEmpty()){
            commonDto = commonList.get(0);
            commonDto.setCommonConfig(true);
            inspectionFillCheckListDtos.add(commonDto);
        }
        InspectionFDtosDto serListDto =  fillupChklistService.getInspectionFDtosDto(appPremCorrId,taskDto,cDtoList);
        AdCheckListShowDto adchklDto =insepctionNcCheckListService.getAdhocCheckListDto(appPremCorrId);
        ApplicationViewDto appViewDto = fillupChklistService.getAppViewDto(taskId);
        appViewDto.setCurrentStatus(MasterCodeUtil.retrieveOptionsByCodes(new String[]{appViewDto.getApplicationDto().getStatus()}).get(0).getText());

        // change common data;
        insepctionNcCheckListService.getInspectionFillCheckListDtoForShow(commonDto);
        //  change service checklist data
        if(serListDto != null){
            List<InspectionFillCheckListDto> fdtoList = serListDto.getFdtoList();
            if(fdtoList != null && fdtoList.size() >0){
                inspectionFillCheckListDtos.addAll(fdtoList);
                for(InspectionFillCheckListDto inspectionFillCheckListDto : fdtoList) {
                    insepctionNcCheckListService.getInspectionFillCheckListDtoForShow(inspectionFillCheckListDto);
                }
            }
        }
        //set num
        fillupChklistService.getRateOfCheckList(serListDto,adchklDto,commonDto);
        //
        //comparative data for sef and check list nc.
        fillupChklistService.changeDataForNc(inspectionFillCheckListDtos,appPremCorrId);
        ParamUtil.setSessionAttr(request,ADCHK_DTO,adchklDto);
        ParamUtil.setSessionAttr(request,TASK_DTO,taskDto);
        ParamUtil.setSessionAttr(request,MSG_CON, null);
        ParamUtil.setSessionAttr(request,COM_DTO,commonDto);
        ParamUtil.setSessionAttr(request,SER_LIST_DTO,serListDto);
        ParamUtil.setSessionAttr(request,APP_VIEW_DTO,appViewDto);
        ParamUtil.setSessionAttr(request,INS_EMAIL_DTO, null);
        request.setAttribute(IaisEGPConstant.CRUD_ACTION_TYPE, EMAIL_VIEW);
        //get selections dd hh
        ParamUtil.setSessionAttr(request,"hhSelections",(Serializable) IaisCommonUtils.getHHOrDDSelectOptions(true));
        ParamUtil.setSessionAttr(request,"ddSelections",(Serializable) IaisCommonUtils.getHHOrDDSelectOptions(false));
        ParamUtil.setSessionAttr(request,"frameworknOption",(Serializable) LicenceUtil.getIncludeRiskTypes());
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
        String decision=ParamUtil.getString(request,"decision");

        InspectionEmailTemplateDto inspectionEmailTemplateDto= (InspectionEmailTemplateDto) ParamUtil.getSessionAttr(request,INS_EMAIL_DTO);
        inspectionEmailTemplateDto.setSubject(ParamUtil.getString(request,SUBJECT));
        inspectionEmailTemplateDto.setRemarks(ParamUtil.getString(request, IntranetUserConstant.INTRANET_REMARKS));
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
            applicationService.updateFEApplicaitons(Collections.singletonList(applicationViewDto.getApplicationDto()));
            AppInspectionStatusDto appInspectionStatusDto1 = appInspectionStatusClient.getAppInspectionStatusByPremId(applicationViewDto.getAppPremisesCorrelationId()).getEntity();
            appInspectionStatusDto1.setStatus(InspectionConstants.INSPECTION_STATUS_PENDING_AO1_EMAIL_VERIFY);
            appInspectionStatusDto1.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
            appInspectionStatusClient.update(appInspectionStatusDto1);
            taskDto.setTaskKey(HcsaConsts.ROUTING_STAGE_INS);
            completedTask(taskDto);

            AppPremisesRoutingHistoryDto appPremisesRoutingHistoryDto= appPremisesRoutingHistoryService.getAppPremisesRoutingHistoryForCurrentStage(applicationViewDto.getApplicationDto().getApplicationNo(),HcsaConsts.ROUTING_STAGE_AO1);
            HcsaSvcStageWorkingGroupDto hcsaSvcStageWorkingGroupDto = new HcsaSvcStageWorkingGroupDto();
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
            taskDto1.setTaskKey(HcsaConsts.ROUTING_STAGE_AO1);
            taskDto1.setWkGrpId(hcsaConfigClient.getHcsaSvcStageWorkingGroupDto(hcsaSvcStageWorkingGroupDto).getEntity().getGroupId());
            taskDto1.setUserId(appPremisesRoutingHistoryDto.getActionby());
            List<TaskDto> taskDtos = prepareTaskList(taskDto1,hcsaSvcStageWorkingGroupDto);
            taskService.createTasks(taskDtos);
            taskDto1.setWkGrpId(taskDto.getWkGrpId());
            createAppPremisesRoutingHistory(applicationViewDto.getApplicationDto().getApplicationNo(), ApplicationConsts.APPLICATION_STATUS_PENDING_EMAIL_REVIEW, InspectionConstants.PROCESS_DECI_ROTE_EMAIL_AO1_REVIEW,taskDto1, userId,inspectionEmailTemplateDto.getRemarks());
            createAppPremisesRoutingHistory(applicationViewDto.getApplicationDto().getApplicationNo(), ApplicationConsts.APPLICATION_STATUS_PENDING_EMAIL_REVIEW,ApplicationConsts.APPLICATION_STATUS_PENDING_EMAIL_REVIEW, taskDto, userId,"");
        }
        if (decision.equals(InspectionConstants.PROCESS_DECI_ROTE_EMAIL_INSPECTION_LEAD_REVIEW)){
            applicationViewDto.getApplicationDto().setStatus(ApplicationConsts.APPLICATION_STATUS_PENDING_EMAIL_SENDING);
            applicationViewService.updateApplicaiton(applicationViewDto.getApplicationDto());
            applicationService.updateFEApplicaitons(Collections.singletonList(applicationViewDto.getApplicationDto()));
            AppInspectionStatusDto appInspectionStatusDto1 = appInspectionStatusClient.getAppInspectionStatusByPremId(applicationViewDto.getAppPremisesCorrelationId()).getEntity();
            appInspectionStatusDto1.setStatus(InspectionConstants.INSPECTION_STATUS_PENDING_JOB_CREATE_TASK_TO_LEADER);
            appInspectionStatusDto1.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
            appInspectionStatusClient.update(appInspectionStatusDto1);
            taskDto.setTaskKey(HcsaConsts.ROUTING_STAGE_INS);
            completedTask(taskDto);
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
        TaskDto taskDto = (TaskDto)ParamUtil.getSessionAttr(request,TASK_DTO);
        InspectionFillCheckListDto commonDto = (InspectionFillCheckListDto)ParamUtil.getSessionAttr(request,COM_DTO);
        AdCheckListShowDto adchklDto = (AdCheckListShowDto)ParamUtil.getSessionAttr(request,ADCHK_DTO);
        InspectionFDtosDto serListDto = (InspectionFDtosDto)ParamUtil.getSessionAttr(request,SER_LIST_DTO);
        ParamUtil.setSessionAttr(request,ADCHK_DTO,adchklDto);
        ParamUtil.setSessionAttr(request,COM_DTO,commonDto);
        ParamUtil.setSessionAttr(request,SER_LIST_DTO,serListDto);
        List<NcAnswerDto> ncDtoList = insepctionNcCheckListService.getNcAnswerDtoList(taskDto.getRefNo());
        InspectionCheckListValidation inspectionCheckListValidation = new InspectionCheckListValidation();
        Map<String, String> errMap = inspectionCheckListValidation.validate(request);
        ParamUtil.setSessionAttr(request,AC_DTO, (Serializable) ncDtoList);
        if(!errMap.isEmpty()){
            ParamUtil.setRequestAttr(request, IaisEGPConstant.ISVALID, IaisEGPConstant.NO);
            ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errMap));
        }else{
            ParamUtil.setRequestAttr(request, IaisEGPConstant.ISVALID, IaisEGPConstant.YES);
            insepctionNcCheckListService.submit(commonDto,adchklDto,serListDto,taskDto.getRefNo());
            ApplicationViewDto appViewDto =(ApplicationViewDto) ParamUtil.getSessionAttr(request,APP_VIEW_DTO);
            insepctionNcCheckListService.saveLicPremisesAuditDtoByApplicationViewDto(appViewDto);
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
        AppPremisesRoutingHistoryDto appPremisesRoutingHistoryDto= appPremisesRoutingHistoryService.getAppPremisesRoutingHistoryForCurrentStage(applicationViewDto.getApplicationDto().getApplicationNo(),HcsaConsts.ROUTING_STAGE_INS);
        AppPremisesRoutingHistoryDto appPremisesRoutingHistoryDto1= appPremisesRoutingHistoryService.getAppPremisesRoutingHistoryForCurrentStage(applicationViewDto.getApplicationDto().getApplicationNo(),HcsaConsts.ROUTING_STAGE_AO1);

        try {
            if(appPremisesRoutingHistoryDto.getUpdatedDt().compareTo(appPremisesRoutingHistoryDto1.getUpdatedDt()) <= 0) {
                appTypeOption = MasterCodeUtil.retrieveOptionsByCodes(new String[]{InspectionConstants.PROCESS_DECI_ROTE_EMAIL_AO1_REVIEW});
            }
        }catch (Exception e){
            log.error(e.getMessage(), e);
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
        inspectionEmailTemplateDto.setRemarks(ParamUtil.getString(request, IntranetUserConstant.INTRANET_REMARKS));
        ParamUtil.setSessionAttr(request,INS_EMAIL_DTO,inspectionEmailTemplateDto);
    }

    public void emailView(BaseProcessClass bpc) {
        log.info("=======>>>>>emailView>>>>>>>>>>>>>>>>emailRequest");
        HttpServletRequest request = bpc.request;
        request.setAttribute(IaisEGPConstant.CRUD_ACTION_TYPE, EMAIL_VIEW);
    }

    public InspectionFillCheckListDto getCommonDataFromPage(HttpServletRequest request){
        InspectionFillCheckListDto cDto = (InspectionFillCheckListDto)ParamUtil.getSessionAttr(request,COM_DTO);
        if(cDto!=null&&cDto.getCheckList()!=null&&!cDto.getCheckList().isEmpty()) {
            List<InspectionCheckQuestionDto> checkListDtoList = cDto.getCheckList();
            for (InspectionCheckQuestionDto temp : checkListDtoList) {
                String answer = ParamUtil.getString(request, temp.getSectionNameShow() + temp.getItemId() + "comrad");
                temp.setChkanswer(answer);
                String remark = ParamUtil.getString(request, temp.getSectionNameShow() + temp.getItemId() + "comremark");
                String rectified = ParamUtil.getString(request, temp.getSectionNameShow() + temp.getItemId() + "comrec");
                temp.setRectified(!StringUtil.isEmpty(rectified) && "No".equals(answer));
                temp.setRemark(remark);
            }
            fillupChklistService.fillInspectionFillCheckListDto(cDto);
        }
        return cDto;
    }

    public InspectionFDtosDto getDataFromPage(HttpServletRequest request){
        InspectionFDtosDto serListDto = (InspectionFDtosDto)ParamUtil.getSessionAttr(request,SER_LIST_DTO);
        String tcu = ParamUtil.getString(request,"tuc");
        String bestpractice = ParamUtil.getString(request,"bestpractice");
        String tcuremark = ParamUtil.getString(request,"tcuRemark");
        serListDto.setTcuRemark(tcuremark);
        serListDto.setTuc(tcu);
        serListDto.setBestPractice(bestpractice);
        return serListDto;
    }

    public AdCheckListShowDto getAdhocDtoFromPage(HttpServletRequest request){
        AdCheckListShowDto showDto = (AdCheckListShowDto)ParamUtil.getSessionAttr(request,ADCHK_DTO);
        List<AdhocNcCheckItemDto> itemDtoList = showDto.getAdItemList();
        if(itemDtoList!=null && !itemDtoList.isEmpty()){
            for(AdhocNcCheckItemDto temp:itemDtoList){
                String answer = ParamUtil.getString(request,temp.getId()+"adhocrad");
                String remark = ParamUtil.getString(request,temp.getId()+"adhocremark");
                String rec = ParamUtil.getString(request,temp.getId()+"adhocrec");
                temp.setAdAnswer(answer);
                temp.setRemark(remark);
                temp.setRectified("No".equals(answer)&&!StringUtil.isEmpty(rec));
            }
        }
        showDto.setAdItemList(itemDtoList);
        return showDto;
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
        if(appPreRecommentdationDto!=null&&appPreRecommentdationDto.getBestPractice()!=null){
            inspectionEmailTemplateDto.setBestPractices(appPreRecommentdationDto.getBestPractice());
        }
        //Map<String,Object> map=IaisCommonUtils.genNewHashMap();
        //InspecEmailDelegator.makeEmail(inspectionEmailTemplateDto, map);
//        if(!ncAnswerDtos.isEmpty()){
//            StringBuilder stringBuilder=new StringBuilder();
//            int i=0;
//            for (NcAnswerDto ncAnswerDto:ncAnswerDtos
//            ) {
//                stringBuilder.append("<tr><td>").append(++i);
//                stringBuilder.append(TD).append(StringUtil.viewHtml(ncAnswerDto.getItemQuestion()));
//                stringBuilder.append(TD).append(StringUtil.viewHtml(ncAnswerDto.getClause()));
//                stringBuilder.append(TD).append(StringUtil.viewHtml(ncAnswerDto.getRemark()));
//                stringBuilder.append("</td></tr>");
//            }
//            map.put("NC_DETAILS",stringBuilder.toString());
//        }
//        if(inspectionEmailTemplateDto.getBestPractices()!=null){
//            map.put("BEST_PRACTICE",inspectionEmailTemplateDto.getBestPractices());
//        }
        //map.put("MOH_NAME", AppConsts.MOH_AGENCY_NAME);
        String mesContext;
        {
            List<String> leads = organizationClient.getInspectionLead(taskDto.getWkGrpId()).getEntity();
            OrgUserDto leadDto=organizationClient.retrieveOrgUserAccountById(leads.get(0)).getEntity();
            String loginUrl = HmacConstants.HTTPS +"://" + systemParamConfig.getInterServerName() + MessageConstants.MESSAGE_INBOX_URL_INTER_INBOX;
            MsgTemplateDto msgTemplateDto= notificationHelper.getMsgTemplate(MsgTemplateConstants.MSG_TEMPLATE_EN_INS_002_INSPECTOR_EMAIL);
            Map<String,Object> mapTemplate=IaisCommonUtils.genNewHashMap();
            mapTemplate.put("inspection_lead", leadDto.getDisplayName());
            mapTemplate.put("ApplicantName", licenseeDto.getName());
            mapTemplate.put("ApplicationType", MasterCodeUtil.retrieveOptionsByCodes(new String[]{applicationViewDto.getApplicationDto().getApplicationType()}).get(0).getText());
            mapTemplate.put("ApplicationNumber", applicationViewDto.getApplicationDto().getApplicationNo());
            mapTemplate.put("ApplicationDate", applicationViewDto.getSubmissionDate());
            mapTemplate.put("systemLink", loginUrl);
            mapTemplate.put("HCI_CODE", applicationViewDto.getHciCode());
            mapTemplate.put("Address", applicationViewDto.getHciAddress());
            mapTemplate.put("HCI_Postal_Code", applicationViewDto.getHciPostalCode());
            mapTemplate.put("LicenseeName", licenseeDto.getName());
            AppPremisesRecommendationDto appPreRecommentdationDtoInspectionDate =insepctionNcCheckListService.getAppRecomDtoByAppCorrId(correlationId,InspectionConstants.RECOM_TYPE_INSEPCTION_DATE);
            mapTemplate.put("InspectionDate", StringUtil.viewHtml(Formatter.formatDate(appPreRecommentdationDtoInspectionDate.getRecomInDate())));
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
                    mapTemplate.put("InspectionEndDate", Formatter.formatDate(apptUserCalendarDtos.get(0).getEndSlot().get(0)));
                }catch (Exception e){
                    log.info(e.getMessage(),e);
                }
            }
            Map<String,Object> mapTableTemplate=IaisCommonUtils.genNewHashMap();
            MsgTemplateDto msgTableTemplateDto= notificationHelper.getMsgTemplate(MsgTemplateConstants.MSG_TEMPLATE_EN_INS_002_TABLE_12);

            if(ncAnswerDtos.size()!=0){
                StringBuilder stringBuilder=new StringBuilder();
                stringBuilder.append("<tr>").append(applicationViewDto.getServiceType()).append("</tr>");
                int i=0;
                for (NcAnswerDto ncAnswerDto:ncAnswerDtos
                ) {
                    stringBuilder.append("<tr><td>").append(++i);
                    stringBuilder.append(TD).append(StringUtil.viewHtml(ncAnswerDto.getType()));
                    stringBuilder.append(TD).append(StringUtil.viewHtml(ncAnswerDto.getItemQuestion()));
                    stringBuilder.append(TD).append(StringUtil.viewHtml(ncAnswerDto.getClause()));
                    stringBuilder.append(TD).append(StringUtil.viewHtml(ncAnswerDto.getRemark()));
                    stringBuilder.append(TD).append(StringUtil.viewHtml("1".equals(ncAnswerDto.getRef())?"Yes":"No"));
                    stringBuilder.append("</td></tr>");
                }
                mapTableTemplate.put("NC_DETAILS",StringUtil.viewHtml(stringBuilder.toString()));
            }
            //mapTemplate.put("ServiceName", applicationViewDto.getServiceType());
            if(appPreRecommentdationDto!=null&&(appPreRecommentdationDto.getBestPractice()!=null||appPreRecommentdationDto.getRemarks()!=null)){
                String stringBuilder = "<tr><td>" + 1 +
                        TD + StringUtil.viewHtml(appPreRecommentdationDto.getBestPractice()) +
                        TD + StringUtil.viewHtml(appPreRecommentdationDto.getRemarks()) +
                        "</td></tr>";
                mapTableTemplate.put("Observation_Recommendation",StringUtil.viewHtml(stringBuilder));
            }
            msgTableTemplateDto.setMessageContent(MsgUtil.getTemplateMessageByContent(msgTableTemplateDto.getMessageContent(),mapTableTemplate));

            mapTemplate.put("NC_DETAILS_AND_Observation_Recommendation",msgTableTemplateDto.getMessageContent());
            mapTemplate.put("HALP", AppConsts.MOH_SYSTEM_NAME);
            mapTemplate.put("DDMMYYYY", StringUtil.viewHtml(Formatter.formatDate(new Date())));
            mapTemplate.put("Inspector_mail_Address", leadDto.getEmail());
            mapTemplate.put("InspectorDID", leadDto.getMobileNo());
            mapTemplate.put("MOH_AGENCY_NAME", AppConsts.MOH_AGENCY_NAME);
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
    private List<TaskDto> prepareTaskList(TaskDto taskDto, HcsaSvcStageWorkingGroupDto hcsaSvcStageWorkingGroupDto)  {
        List<TaskDto> list = IaisCommonUtils.genNewArrayList();
        List<HcsaSvcStageWorkingGroupDto> listhcsaSvcStageWorkingGroupDto = hcsaConfigClient.getSvcWorkGroup(hcsaSvcStageWorkingGroupDto).getEntity();
        Integer count = listhcsaSvcStageWorkingGroupDto.get(0).getCount();
        taskDto.setWkGrpId(hcsaConfigClient.getHcsaSvcStageWorkingGroupDto(hcsaSvcStageWorkingGroupDto).getEntity().getGroupId());

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
    private TaskDto completedTask(TaskDto taskDto) {
        taskDto.setTaskStatus(TaskConsts.TASK_STATUS_COMPLETED);
        taskDto.setSlaDateCompleted(new Date());
        taskDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        return taskService.updateTask(taskDto);
    }



    public void doCheckList(BaseProcessClass bpc){
        log.info("=======>>>>>doCheckList>>>>>>>>>>>>>>>>doCheckList");
        HttpServletRequest request = bpc.request;
        InspectionFDtosDto serListDto = getServiceCheckListDataFormViewPage(request);
        InspectionFillCheckListDto commonDto= getCommonDataFromPage(request);
        AdCheckListShowDto adchklDto = getAdhocDtoFromPage(request);
        ParamUtil.setSessionAttr(request,ADCHK_DTO,adchklDto);
        ParamUtil.setSessionAttr(request,COM_DTO,commonDto);
        ParamUtil.setSessionAttr(request,SER_LIST_DTO,serListDto);
        InspectionCheckListItemValidate inspectionCheckListItemValidate = new InspectionCheckListItemValidate();
        Map errMap =  inspectionCheckListItemValidate.validate(request);
        if(!errMap.isEmpty()){
            fillupChklistService.getRateOfCheckList(serListDto,adchklDto,commonDto);
            ParamUtil.setRequestAttr(request, IaisEGPConstant.ISVALID, IaisEGPConstant.NO);
            serListDto.setCheckListTab("chkList");
           ParamUtil.setRequestAttr(request,IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errMap));
        }else {
            serListDto.setCheckListTab("chkList");
            fillupChklistService.getRateOfCheckList(serListDto,adchklDto,commonDto);
            ParamUtil.setSessionAttr(request,ADCHK_DTO,adchklDto);
            ParamUtil.setSessionAttr(request,COM_DTO,commonDto);
            ParamUtil.setSessionAttr(request,SER_LIST_DTO,serListDto);
            ParamUtil.setRequestAttr(request, IaisEGPConstant.ISVALID, IaisEGPConstant.YES);
        }

        String errTab = (String) ParamUtil.getSessionAttr(request,"errorTab");
        if( !StringUtil.isEmpty(errTab)){
            ParamUtil.setSessionAttr(request,"errorTab",null);
            ParamUtil.setRequestAttr(request, "nowComTabIn",  errTab);
        }
    }

    private InspectionFDtosDto getOtherInfo(MultipartHttpServletRequest request) throws IOException {
        InspectionFDtosDto serListDto = (InspectionFDtosDto)ParamUtil.getSessionAttr(request,SER_LIST_DTO);
        String tcuflag = ParamUtil.getString(request,"tcuType");
        String tcu = null;
        if(!StringUtil.isEmpty(tcuflag)){
            tcu = ParamUtil.getString(request,"tuc");
        }
        String bestpractice = ParamUtil.getString(request,"bestpractice");
        String tcuremark = ParamUtil.getString(request,"tcuRemark");
        String otherOfficers = ParamUtil.getString(request,"otherinspector");

        //startHour   startHourMin  endHour endHourMin
        String inspectionDate = ParamUtil.getString(request,"inspectionDate");
        String startHour = ParamUtil.getString(request,"startHour");
        String startMin = ParamUtil.getString(request,"startHourMin");
        String endHour = ParamUtil.getString(request,"endHour");
        String endMin = ParamUtil.getString(request,"endHourMin");
        String startTime = startHour+" : "+startMin;
        String endTime =  endHour+" : "+endMin;
        serListDto.setStartTime(startTime);
        serListDto.setEndTime(endTime);
        serListDto.setStartHour(startHour);
        serListDto.setEndHour(endHour);
        serListDto.setStartMin(startMin);
        serListDto.setEndMin(endMin);
        serListDto.setInspectionDate(inspectionDate);
        serListDto.setOtherinspectionofficer(otherOfficers);
        serListDto.setTcuRemark(tcuremark);
        if(!StringUtil.isEmpty(tcuflag)){
            serListDto.setTcuFlag(true);
            serListDto.setTuc(tcu);
        }else{
            serListDto.setTcuFlag(false);
            serListDto.setTuc(null);
        }
        serListDto.setBestPractice(bestpractice);

        // set litter file
        String litterFile =  ParamUtil.getString(request,"litterFile" );
        if(!StringUtil.isEmpty(litterFile)){
            String litterFileId =  ParamUtil.getString(request,"litterFileId" );
            CommonsMultipartFile file= (CommonsMultipartFile) request.getFile("selectedFileView");
            if(StringUtil.isEmpty(litterFileId) && file != null && file.getSize() != 0){
                if (!StringUtil.isEmpty(file.getOriginalFilename())) {
                    file.getFileItem().setFieldName("selectedFile");
                    TaskDto taskDto = (TaskDto) ParamUtil.getSessionAttr(request, TASK_DTO);
                    String correlationId = taskDto.getRefNo();
                    AppPremisesSpecialDocDto appIntranetDocDto = new AppPremisesSpecialDocDto();
                    appIntranetDocDto.setDocName(litterFile);
                    appIntranetDocDto.setAppPremCorreId(correlationId);
                    appIntranetDocDto.setMd5Code(FileUtil.genMd5FileChecksum(file.getBytes()));
                    long size = file.getSize()/1024;
                    if(size <= Integer.MAX_VALUE ){
                        appIntranetDocDto.setDocSize((int)size);
                    }else {
                        appIntranetDocDto.setDocSize(Integer.MAX_VALUE);
                    }
                    //delete file
                    insepctionNcCheckListService.deleteInvalidFile(serListDto);
                    //save file
                    if( size <= 10240)
                    appIntranetDocDto.setFileRepoId(insepctionNcCheckListService.saveFiles(file));
                    serListDto.setAppPremisesSpecialDocDto(appIntranetDocDto);
                }
            }
        }else {
            //delete file
            insepctionNcCheckListService.deleteInvalidFile(serListDto);
            serListDto.setAppPremisesSpecialDocDto(null);
            // serListDto.setFile(null);
        }

        ParamUtil.setSessionAttr(request,SER_LIST_DTO,serListDto);
        getAuditData(request);
        return serListDto;
    }
    private void  getAuditData(MultipartHttpServletRequest request) {
        ApplicationViewDto appViewDto =(ApplicationViewDto) ParamUtil.getSessionAttr(request,APP_VIEW_DTO);
        if (appViewDto != null && appViewDto.getLicPremisesAuditDto() != null){
            LicPremisesAuditDto licPremisesAuditDto =  appViewDto.getLicPremisesAuditDto();
            String framework = ParamUtil.getString(request,"framework");
            String periods = ParamUtil.getString(request,"periods");
            String frameworkRemarks = ParamUtil.getString(request,"frameworkRemarks");
            if( !StringUtil.isEmpty(framework) && framework.equalsIgnoreCase("0")){
                licPremisesAuditDto.setInRiskSocre(0);
                if(!StringUtil.isEmpty(periods)){
                    licPremisesAuditDto.setIncludeRiskType(periods);
                    if(periods.equalsIgnoreCase(ApplicationConsts.INCLUDE_RISK_TYPE_LEADERSHIP_KEY))
                        licPremisesAuditDto.setLgrRemarks(frameworkRemarks );
                    else
                        licPremisesAuditDto.setLgrRemarks(null);
                } else {
                    licPremisesAuditDto.setIncludeRiskType(null);
                    licPremisesAuditDto.setLgrRemarks(null);
                }
            }else {
                licPremisesAuditDto.setInRiskSocre(1);
                licPremisesAuditDto.setIncludeRiskType(null);
                licPremisesAuditDto.setLgrRemarks(null);
            }
            ParamUtil.setSessionAttr(request,APP_VIEW_DTO,appViewDto);
        }
    }
    public void preViewCheckList(BaseProcessClass bpc) throws IOException{
        log.info("=======>>>>>preViewCheckList>>>>>>>>>>>>>>>>preViewCheckList");
        MultipartHttpServletRequest mulReq = (MultipartHttpServletRequest) bpc.request.getAttribute(HttpHandler.SOP6_MULTIPART_REQUEST);
        String crudActionType = mulReq.getParameter(IaisEGPConstant.CRUD_ACTION_TYPE);
        ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE, crudActionType);
        ParamUtil.setSessionAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE+"Step2", crudActionType);
        getOtherInfo(mulReq);
    }

    public InspectionFDtosDto getServiceCheckListDataFormViewPage(HttpServletRequest request){
        InspectionFDtosDto serListDto = (InspectionFDtosDto)ParamUtil.getSessionAttr(request,SER_LIST_DTO);
        if(!IaisCommonUtils.isEmpty(serListDto.getFdtoList())){
            for(InspectionFillCheckListDto fdto:serListDto.getFdtoList()){
                if(fdto!=null&&!IaisCommonUtils.isEmpty(fdto.getCheckList())){
                    List<InspectionCheckQuestionDto> checkListDtoList = fdto.getCheckList();
                    for(InspectionCheckQuestionDto temp:checkListDtoList){
                        getServiceData(temp,fdto,request);
                    }
                    fillupChklistService.fillInspectionFillCheckListDto(fdto);
                }
            }
        }
        return serListDto;
    }

    public void getServiceData(InspectionCheckQuestionDto temp,InspectionFillCheckListDto fdto,HttpServletRequest request){
        String answer = ParamUtil.getString(request,fdto.getSubName()+temp.getSectionNameShow()+temp.getItemId()+"rad");
        temp.setChkanswer(answer);
        String remark = ParamUtil.getString(request,fdto.getSubName()+temp.getSectionNameShow()+temp.getItemId()+"remark");
        String rectified = ParamUtil.getString(request,fdto.getSubName()+temp.getSectionNameShow()+temp.getItemId()+"rec");
        if(!StringUtil.isEmpty(rectified)&&"No".equals(answer)){
            temp.setRectified(true);
        }else{
            temp.setRectified(false);
        }
        temp.setRemark(remark);
    }

}

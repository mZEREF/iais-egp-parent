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
import com.ecquaria.cloud.moh.iais.helper.InspectionHelper;
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
import com.ecquaria.cloud.moh.iais.validation.InspectionCheckListValidation;
import com.ecquaria.sz.commons.util.MsgUtil;
import freemarker.template.TemplateException;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import sop.servlet.webflow.HttpHandler;
import sop.webflow.rt.api.BaseProcessClass;

/**
 * ValidateEmailDelegator
 *
 * @author junyu
 * @date 2019/12/3
 */
@Delegator("validateEmailDelegator")
@Slf4j
public class InspectEmailAo1Delegator  extends InspectionCheckListCommonMethodDelegator{
    @Autowired
    InspEmailService inspEmailService;
    @Autowired
    InspectionService inspectionService;

    @Autowired
    ApplicationViewService applicationViewService;
    @Autowired
    private HcsaConfigClient hcsaConfigClient;

    @Autowired
    private AppPremisesRoutingHistoryService appPremisesRoutingHistoryService;
    @Autowired
    AppInspectionStatusClient appInspectionStatusClient;

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
    @Autowired
    ApplicationClient applicationClient;
    @Autowired
    ApplicationService applicationService;
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
    private static final String SER_LIST_DTO= "serListDto";
    private static final String TD="</td><td>";
    private static final String SUBJECT="subject";
    private static final String DRA_EMA_ID="draftEmailId";
    private static final String ROLLBACK_OPTIONS="rollBackOptions";
    private static final String ROLLBACK_VALUE_MAP="rollBackValueMap";

    public void start(BaseProcessClass bpc){

        log.info("=======>>>>>startStep>>>>>>>>>>>>>>>>emailRequest");

        log.info("=======>>>>>initStep>>>>>>>>>>>>>>>>initRequest");
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
        //init rollBack params
        ApplicationViewDto applicationViewDto= (ApplicationViewDto) ParamUtil.getSessionAttr(request,APP_VIEW_DTO);
        Map<String, AppPremisesRoutingHistoryDto> rollBackValueMap = IaisCommonUtils.genNewHashMap();
        List<SelectOption> rollBackStage = inspectionService.getRollBackSelectOptions(applicationViewDto.getRollBackHistroyList(), rollBackValueMap, taskDto.getRoleId());
        ParamUtil.setSessionAttr(bpc.request, ROLLBACK_OPTIONS, (Serializable) rollBackStage);
        ParamUtil.setSessionAttr(bpc.request, ROLLBACK_VALUE_MAP, (Serializable) rollBackValueMap);
    }


    public void prepareData(BaseProcessClass bpc) {
        log.info("=======>>>>>prepareData>>>>>>>>>>>>>>>>emailRequest");
        HttpServletRequest request = bpc.request;
        request.setAttribute(IaisEGPConstant.CRUD_ACTION_TYPE, StringUtil.isEmpty(ParamUtil.getString(request,IaisEGPConstant.CRUD_ACTION_TYPE)) ? EMAIL_VIEW : ParamUtil.getString(request,IaisEGPConstant.CRUD_ACTION_TYPE));
        //Can edit application
        InspectionHelper.checkForEditingApplication(bpc.request);
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
    public void emailSubmitStep(BaseProcessClass bpc){
        log.info("=======>>>>>emailSubmitStep>>>>>>>>>>>>>>>>emailRequest");
        HttpServletRequest request = bpc.request;
        InspectionEmailTemplateDto inspectionEmailTemplateDto = (InspectionEmailTemplateDto) ParamUtil.getSessionAttr(bpc.request,INS_EMAIL_DTO);
        String content=ParamUtil.getString(request,MSG_CON);
        ParamUtil.setSessionAttr(request,MSG_CON,content);
        ParamUtil.setSessionAttr(request,INS_EMAIL_DTO, inspectionEmailTemplateDto);
        ParamUtil.setRequestAttr(request,IaisEGPConstant.CRUD_ACTION_TYPE_VALUE,EMAIL_VIEW);
    }
    public void doProcessing(BaseProcessClass bpc){
        log.info("=======>>>>>doProcessing>>>>>>>>>>>>>>>>emailRequest");
        HttpServletRequest request = bpc.request;
        ParamUtil.setRequestAttr(request,IaisEGPConstant.CRUD_ACTION_TYPE_VALUE,"processing");
        InspectionEmailTemplateDto inspectionEmailTemplateDto= (InspectionEmailTemplateDto) ParamUtil.getSessionAttr(request,INS_EMAIL_DTO);
        inspectionEmailTemplateDto.setRemarks(ParamUtil.getString(request, "Remarks"));
        ParamUtil.setSessionAttr(request,INS_EMAIL_DTO,inspectionEmailTemplateDto);
        ParamUtil.setRequestAttr(request,
                "selectVerified",ParamUtil.getString(request, "verified")==null
                        ? "AO1"
                        :ParamUtil.getString(request, "verified"));
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


    }

    public void sendEmail(BaseProcessClass bpc)   {

        log.info("=======>>>>>sendEmail>>>>>>>>>>>>>>>>emailRequest");
        HttpServletRequest request = bpc.request;
        LoginContext loginContext = (LoginContext) ParamUtil.getSessionAttr(bpc.request, AppConsts.SESSION_ATTR_LOGIN_USER);
        String userId = loginContext.getUserId();
        ApplicationViewDto applicationViewDto= (ApplicationViewDto) ParamUtil.getSessionAttr(request,APP_VIEW_DTO);
        TaskDto taskDto= (TaskDto) ParamUtil.getSessionAttr(request,TASK_DTO);
        TaskDto taskDtoNew = taskService.getTaskById(taskDto.getId());
        if(taskDtoNew.getTaskStatus().equals(TaskConsts.TASK_STATUS_COMPLETED)){
            ParamUtil.setRequestAttr(request,"COMPLETED",Boolean.TRUE);
            return;
        }
        String serviceId=applicationViewDto.getApplicationDto().getServiceId();
        String currentAction = ParamUtil.getString(request, IaisEGPConstant.CRUD_ACTION_TYPE);
        if(!"send".equals(currentAction)){
            return;
        }
        String decision=ParamUtil.getString(request,"decision");
        if("Select".equals(decision)){decision=InspectionConstants.PROCESS_DECI_ACKNOWLEDGE_EMAIL_CONTENT;}

        InspectionEmailTemplateDto inspectionEmailTemplateDto= (InspectionEmailTemplateDto) ParamUtil.getSessionAttr(request,INS_EMAIL_DTO);
        inspectionEmailTemplateDto.setSubject(ParamUtil.getString(request,SUBJECT));
        inspectionEmailTemplateDto.setMessageContent(ParamUtil.getString(request,MSG_CON));
        inspectionEmailTemplateDto.setRemarks(ParamUtil.getString(request, "Remarks"));

        if (ApplicationConsts.PROCESSING_DECISION_ROUTE_LATERALLY.equals(decision)){
            String lrSelect = ParamUtil.getRequestString(bpc.request, "lrSelect");
            log.info(StringUtil.changeForLog("The lrSelect is -->:"+lrSelect));
            if(StringUtil.isNotEmpty(lrSelect)){
                String[] lrSelects =  lrSelect.split("_");
                String aoWorkGroupId = lrSelects[0];
                String aoUserId = lrSelects[1];
                inspEmailService.completedTask(taskDto);
                List<TaskDto> taskDtos = IaisCommonUtils.genNewArrayList();
                taskDto.setUserId(aoUserId);
                taskDto.setDateAssigned(new Date());
                taskDto.setId(null);
                taskDto.setWkGrpId(aoWorkGroupId);
                taskDto.setSlaDateCompleted(null);
                taskDto.setTaskStatus(TaskConsts.TASK_STATUS_PENDING);
                taskDtos.add(taskDto);
                taskService.createTasks(taskDtos);
                createAppPremisesRoutingHistory(applicationViewDto.getApplicationDto().getApplicationNo(), ApplicationConsts.APPLICATION_STATUS_PENDING_EMAIL_REVIEW, ApplicationConsts.PROCESSING_DECISION_ROUTE_LATERALLY,taskDto,userId,inspectionEmailTemplateDto.getRemarks(),HcsaConsts.ROUTING_STAGE_INS);
                createAppPremisesRoutingHistory(applicationViewDto.getApplicationDto().getApplicationNo(), ApplicationConsts.APPLICATION_STATUS_PENDING_EMAIL_REVIEW,ApplicationConsts.APPLICATION_STATUS_PENDING_EMAIL_REVIEW, taskDto,userId,"",HcsaConsts.ROUTING_STAGE_INS);
                ParamUtil.setRequestAttr(request,"LATERALLY",AppConsts.TRUE);
            }

            return;
        } else if (InspectionConstants.PROCESS_DECI_ROLL_BACK.equals(decision)){
            Map<String, AppPremisesRoutingHistoryDto> rollBackValueMap = (Map<String, AppPremisesRoutingHistoryDto>) ParamUtil.getSessionAttr(request, ROLLBACK_VALUE_MAP);
            String rollBackTo = ParamUtil.getRequestString(request, "rollBackTo");
            inspectionService.rollBack(bpc, taskDto, applicationViewDto, rollBackValueMap.get(rollBackTo), inspectionEmailTemplateDto.getRemarks());
            ParamUtil.setRequestAttr(request,"isRollBack",AppConsts.TRUE);
            return;
        } else if (decision.equals(InspectionConstants.PROCESS_DECI_ACKNOWLEDGE_EMAIL_CONTENT)){
            applicationViewDto.getApplicationDto().setStatus(ApplicationConsts.APPLICATION_STATUS_PENDING_EMAIL_SENDING);
            applicationViewService.updateApplicaiton(applicationViewDto.getApplicationDto());
            try {
                applicationService.updateFEApplicaitons(Collections.singletonList(applicationViewDto.getApplicationDto()));
            }catch (Exception e){
                log.info(e.getMessage(),e);
            }
            AppInspectionStatusDto appInspectionStatusDto = appInspectionStatusClient.getAppInspectionStatusByPremId(applicationViewDto.getAppPremisesCorrelationId()).getEntity();
            appInspectionStatusDto.setStatus(InspectionConstants.INSPECTION_STATUS_PENDING_JOB_CREATE_TASK_TO_LEADER);
            appInspectionStatusDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
            appInspectionStatusClient.update(appInspectionStatusDto);
            taskDto.setTaskKey(HcsaConsts.ROUTING_STAGE_INS);
            inspEmailService.completedTask(taskDto);
            createAppPremisesRoutingHistory(applicationViewDto.getApplicationDto().getApplicationNo(), ApplicationConsts.APPLICATION_STATUS_PENDING_EMAIL_REVIEW,InspectionConstants.PROCESS_DECI_ACKNOWLEDGE_EMAIL_CONTENT, taskDto, userId,inspectionEmailTemplateDto.getRemarks(),HcsaConsts.ROUTING_STAGE_POT);
            taskDto.setRoleId(RoleConsts.USER_ROLE_INSPECTION_LEAD);
            createAppPremisesRoutingHistory(applicationViewDto.getApplicationDto().getApplicationNo(), ApplicationConsts.APPLICATION_STATUS_PENDING_EMAIL_REVIEW,ApplicationConsts.APPLICATION_STATUS_PENDING_EMAIL_SENDING, taskDto, userId,"",HcsaConsts.ROUTING_STAGE_POT);
        } else {
            applicationViewDto.getApplicationDto().setStatus(ApplicationConsts.APPLICATION_STATUS_PENDING_RE_DRAFT_LETTER);
            applicationViewService.updateApplicaiton(applicationViewDto.getApplicationDto());
            try {
                applicationService.updateFEApplicaitons(Collections.singletonList(applicationViewDto.getApplicationDto()));
            }catch (Exception e){
                log.info(e.getMessage(),e);
            }
            AppInspectionStatusDto appInspectionStatusDto = appInspectionStatusClient.getAppInspectionStatusByPremId(applicationViewDto.getAppPremisesCorrelationId()).getEntity();
            appInspectionStatusDto.setStatus(InspectionConstants.INSPECTION_STATUS_PENDING_CHECKLIST_VERIFY);
            appInspectionStatusDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
            appInspectionStatusClient.update(appInspectionStatusDto);
            taskDto.setTaskKey(HcsaConsts.ROUTING_STAGE_INS);
            inspEmailService.completedTask(taskDto);


            AppPremisesRoutingHistoryDto appPremisesRoutingHistoryDto= appPremisesRoutingHistoryService.getAppPremisesRoutingHistoryForCurrentStage(applicationViewDto.getApplicationDto().getApplicationNo(),HcsaConsts.ROUTING_STAGE_INS,RoleConsts.USER_ROLE_INSPECTIOR);
            HcsaSvcStageWorkingGroupDto hcsaSvcStageWorkingGroupDto = new HcsaSvcStageWorkingGroupDto();
            hcsaSvcStageWorkingGroupDto.setType(applicationViewDto.getApplicationDto().getApplicationType());
            if(applicationViewDto.getApplicationDto().getRoutingServiceId()!=null){
                serviceId=applicationViewDto.getApplicationDto().getRoutingServiceId();
            }
            hcsaSvcStageWorkingGroupDto.setServiceId(serviceId);
            hcsaSvcStageWorkingGroupDto.setStageId(HcsaConsts.ROUTING_STAGE_INS);
            hcsaSvcStageWorkingGroupDto.setOrder(1);
            TaskDto taskDto1=new TaskDto();
            taskDto1.setApplicationNo(applicationViewDto.getApplicationDto().getApplicationNo());
            taskDto1.setRefNo(taskDto.getRefNo());
            taskDto1.setTaskKey(HcsaConsts.ROUTING_STAGE_INS);
            taskDto1.setUserId(appPremisesRoutingHistoryDto.getActionby());
            taskDto1.setWkGrpId(hcsaConfigClient.getHcsaSvcStageWorkingGroupDto(hcsaSvcStageWorkingGroupDto).getEntity().getGroupId());
            taskDto1.setProcessUrl(TaskConsts.TASK_PROCESS_URL_INSPECTION_REVISE_NCEMAIL);
            taskDto1.setRoleId(RoleConsts.USER_ROLE_INSPECTIOR);
            taskDto1.setTaskType(taskDto.getTaskType());
            List<TaskDto> taskDtos = prepareTaskList(taskDto1,applicationViewDto.getApplicationDto());
            taskService.createTasks(taskDtos);

            createAppPremisesRoutingHistory(applicationViewDto.getApplicationDto().getApplicationNo(), ApplicationConsts.APPLICATION_STATUS_PENDING_EMAIL_REVIEW,InspectionConstants.PROCESS_DECI_REVISE_EMAIL_CONTENT, taskDto, userId,inspectionEmailTemplateDto.getRemarks(),HcsaConsts.ROUTING_STAGE_INP);
            createAppPremisesRoutingHistory(applicationViewDto.getApplicationDto().getApplicationNo(), ApplicationConsts.APPLICATION_STATUS_PENDING_RE_DRAFT_LETTER, ApplicationConsts.APPLICATION_STATUS_PENDING_RE_DRAFT_LETTER,taskDto, userId,"",HcsaConsts.ROUTING_STAGE_INP);

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
            ParamUtil.setRequestAttr(request, "isValid", "Y");
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
            ParamUtil.setRequestAttr(request, "isValid", "N");
            ParamUtil.setRequestAttr(bpc.request, "errorMsg", WebValidationHelper.generateJsonStr(errMap));
        }else{
            ParamUtil.setRequestAttr(request, "isValid", "Y");
            insepctionNcCheckListService.submit(commonDto,adchklDto,serListDto,taskDto.getRefNo());
            ApplicationViewDto appViewDto =(ApplicationViewDto) ParamUtil.getSessionAttr(request,APP_VIEW_DTO);
            insepctionNcCheckListService.saveLicPremisesAuditDtoByApplicationViewDto(appViewDto);
        }

    }
    public void preEmailView(BaseProcessClass bpc) throws IOException, TemplateException {
        log.info("=======>>>>>preEmailView>>>>>>>>>>>>>>>>emailRequest");
        HttpServletRequest request = bpc.request;
        TaskDto taskDto = (TaskDto) ParamUtil.getSessionAttr(bpc.request, TASK_DTO);
        String correlationId = taskDto.getRefNo();
        InspectionEmailTemplateDto inspectionEmailTemplateDto=new InspectionEmailTemplateDto();
        if(ParamUtil.getSessionAttr(request,INS_EMAIL_DTO)!=null){
            inspectionEmailTemplateDto= (InspectionEmailTemplateDto) ParamUtil.getSessionAttr(request,INS_EMAIL_DTO);
        }
        else {
            try{
                inspectionEmailTemplateDto= inspEmailService.getInsertEmail(correlationId);
            }catch (Exception e){
                ApplicationViewDto applicationViewDto = fillupChklistService.getAppViewDto(taskDto.getId());
                String appNo=applicationViewDto.getApplicationDto().getApplicationNo();
                String licenseeId=applicationViewDto.getApplicationGroupDto().getLicenseeId();
                LicenseeDto licenseeDto=inspEmailService.getLicenseeDtoById(licenseeId);
                String applicantName=licenseeDto.getName();
                inspectionEmailTemplateDto.setAppPremCorrId(correlationId);
                inspectionEmailTemplateDto.setApplicantName(applicantName);
                inspectionEmailTemplateDto.setApplicationNumber(appNo);
                inspectionEmailTemplateDto.setHciCode(applicationViewDto.getHciCode());
                HcsaServiceDto hcsaServiceDto=inspectionService.getHcsaServiceDtoByServiceId(applicationViewDto.getApplicationDto().getServiceId());
                inspectionEmailTemplateDto.setServiceName(hcsaServiceDto.getSvcName());
                AppPremisesRecommendationDto appPreRecommentdationDto =insepctionNcCheckListService.getAppRecomDtoByAppCorrId(correlationId,InspectionConstants.RECOM_TYPE_TCU);
                List<NcAnswerDto> ncAnswerDtos=insepctionNcCheckListService.getNcAnswerDtoList(correlationId);
//
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
                    mapTemplate.put("LicenseeName", applicationViewDto.getSubLicenseeDto().getLicenseeName());
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
                        }catch (Exception e1){
                            log.info(e1.getMessage(),e1);
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
                            if(vehicleOpenFlag.equals(InspectionConstants.SWITCH_ACTION_YES)&&applicationViewDto.getAppSvcVehicleDtos()!=null&&(applicationViewDto.getSvcCode().equals(AppServicesConsts.SERVICE_CODE_EMERGENCY_AMBULANCE_SERVICE)||applicationViewDto.getSvcCode().equals(AppServicesConsts.SERVICE_CODE_MEDICAL_TRANSPORT_SERVICE))){
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
                                    stringBuilder.append(TD).append(StringUtil.viewHtml(ncAnswerDto.getType()));
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

                String content= (String) ParamUtil.getSessionAttr(request,MSG_CON);
                if(content!=null){
                    mesContext=content;
                }
                inspectionEmailTemplateDto.setMessageContent(mesContext);
                inspEmailService.insertEmailDraft(inspectionEmailTemplateDto);
                inspectionEmailTemplateDto= inspEmailService.getInsertEmail(correlationId);
            }
        }
        inspectionEmailTemplateDto.setMessageContent(StringUtil.removeNonUtf8(inspectionEmailTemplateDto.getMessageContent()));
        ParamUtil.setSessionAttr(request,DRA_EMA_ID,inspectionEmailTemplateDto.getId());
        ParamUtil.setSessionAttr(request,INS_EMAIL_DTO, inspectionEmailTemplateDto);
        ParamUtil.setRequestAttr(request,IaisEGPConstant.CRUD_ACTION_TYPE_VALUE,"emailView");
    }
    public void preProcess(BaseProcessClass bpc)  {
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
        String[] processDess = new String[]{InspectionConstants.PROCESS_DECI_ACKNOWLEDGE_EMAIL_CONTENT,InspectionConstants.PROCESS_DECI_REVISE_EMAIL_CONTENT,ApplicationConsts.PROCESSING_DECISION_ROUTE_LATERALLY};
        String appType = applicationViewDto.getApplicationDto().getApplicationType();
        if (!(ApplicationConsts.APPLICATION_TYPE_POST_INSPECTION.equals(appType) || ApplicationConsts.APPLICATION_TYPE_CREATE_AUDIT_TASK.equals(appType) || ApplicationConsts.APPLICATION_TYPE_CESSATION.equals(appType))) {
            processDess = new String[]{InspectionConstants.PROCESS_DECI_ACKNOWLEDGE_EMAIL_CONTENT,InspectionConstants.PROCESS_DECI_REVISE_EMAIL_CONTENT,InspectionConstants.PROCESS_DECI_ROLL_BACK,ApplicationConsts.PROCESSING_DECISION_ROUTE_LATERALLY};
        }
        List<SelectOption> appTypeOption = MasterCodeUtil.retrieveOptionsByCodes(processDess);
        String content= (String) ParamUtil.getSessionAttr(request,MSG_CON);
        if(content!=null){
            inspectionEmailTemplateDto.setMessageContent(content);
        }
        List<SelectOption> routingStage = IaisCommonUtils.genNewArrayList();
        routingStage.add(new SelectOption(RoleConsts.USER_ROLE_AO1, RoleConsts.USER_ROLE_AO1_SHOW));
//set verified values
        applicationViewDto.setVerified(routingStage);
        ParamUtil.setSessionAttr(request, "verifiedValues", (Serializable) routingStage);
        inspectionEmailTemplateDto.setAppStatus(MasterCodeUtil.retrieveOptionsByCodes(new String[]{applicationViewDto.getApplicationDto().getStatus()}).get(0).getText());
        ParamUtil.setRequestAttr(request,"appTypeOption", appTypeOption);
        ParamUtil.setRequestAttr(request,"appPremisesRoutingHistoryDtos", appPremisesRoutingHistoryDtos);
        ParamUtil.setSessionAttr(request,DRA_EMA_ID,inspectionEmailTemplateDto.getId());
        inspectionEmailTemplateDto.setMessageContent(StringUtil.removeNonUtf8(inspectionEmailTemplateDto.getMessageContent()));
        ParamUtil.setSessionAttr(request,INS_EMAIL_DTO, inspectionEmailTemplateDto);
        ParamUtil.setRequestAttr(request,IaisEGPConstant.CRUD_ACTION_TYPE_VALUE,"processing");
    }

    public void emailView(BaseProcessClass bpc) {
        log.info("=======>>>>>emailView>>>>>>>>>>>>>>>>emailRequest");
        HttpServletRequest request = bpc.request;
        request.setAttribute(IaisEGPConstant.CRUD_ACTION_TYPE, EMAIL_VIEW);
    }

    private AppPremisesRoutingHistoryDto createAppPremisesRoutingHistory(String appNo, String appStatus, String decision,
                                                                         TaskDto taskDto, String userId, String remarks,String subStage) {
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
        appPremisesRoutingHistoryDto.setSubStage(subStage);
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
        InspectionFDtosDto serListDto = (InspectionFDtosDto)ParamUtil.getSessionAttr(request,SER_LIST_DTO);
        serListDto.setCheckListTab("chkList");
        ParamUtil.setSessionAttr(request,SER_LIST_DTO,serListDto);
    }

    public void preViewCheckList(BaseProcessClass bpc) throws IOException{
        log.info("=======>>>>>preViewCheckList>>>>>>>>>>>>>>>>preViewCheckList");
        MultipartHttpServletRequest mulReq = (MultipartHttpServletRequest) bpc.request.getAttribute(HttpHandler.SOP6_MULTIPART_REQUEST);
        String crudActionType = mulReq.getParameter(IaisEGPConstant.CRUD_ACTION_TYPE);
        ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE, crudActionType);
        ParamUtil.setSessionAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE+"Step2", crudActionType);
    }


}

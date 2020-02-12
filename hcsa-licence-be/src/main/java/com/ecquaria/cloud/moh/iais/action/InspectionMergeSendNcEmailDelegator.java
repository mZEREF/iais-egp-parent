package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.constant.HcsaConsts;
import com.ecquaria.cloud.moh.iais.common.constant.inspection.InspectionConstants;
import com.ecquaria.cloud.moh.iais.common.constant.role.RoleConsts;
import com.ecquaria.cloud.moh.iais.common.constant.sample.DemoConstants;
import com.ecquaria.cloud.moh.iais.common.constant.task.TaskConsts;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.application.ApplicationViewDto;
import com.ecquaria.cloud.moh.iais.common.dto.emailsms.EmailDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesCorrelationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesRoutingHistoryDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcStageWorkingGroupDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.AppInspectionStatusDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspectionEmailTemplateDto;
import com.ecquaria.cloud.moh.iais.common.dto.task.TaskDto;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant;
import com.ecquaria.cloud.moh.iais.dto.LoginContext;
import com.ecquaria.cloud.moh.iais.helper.AccessUtil;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.helper.MasterCodeUtil;
import com.ecquaria.cloud.moh.iais.service.AppPremisesRoutingHistoryService;
import com.ecquaria.cloud.moh.iais.service.ApplicationService;
import com.ecquaria.cloud.moh.iais.service.ApplicationViewService;
import com.ecquaria.cloud.moh.iais.service.InspEmailService;
import com.ecquaria.cloud.moh.iais.service.InspectionPreTaskService;
import com.ecquaria.cloud.moh.iais.service.InspectionService;
import com.ecquaria.cloud.moh.iais.service.TaskService;
import com.ecquaria.cloud.moh.iais.service.client.AppInspectionStatusClient;
import com.ecquaria.cloud.moh.iais.service.client.EmailClient;
import com.ecquaria.cloud.moh.iais.service.client.HcsaConfigClient;
import com.ecquaria.cloudfeign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.time.DurationFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * InspectionMergeSendNcEmailDelegator
 *
 * @author junyu
 * @date 2019/12/17
 */
@Delegator("inspectionMergeSendNcEmailDelegator")
@Slf4j
public class InspectionMergeSendNcEmailDelegator {
    @Autowired
    InspEmailService inspEmailService;
    @Autowired
    InspectionService inspectionService;
    @Autowired
    private TaskService taskService;
    @Autowired
    InspectionPreTaskService inspectionPreTaskService;
    @Autowired
    ApplicationService applicationService;
    @Autowired
    ApplicationViewService applicationViewService;
    @Autowired
    private AppPremisesRoutingHistoryService appPremisesRoutingHistoryService;
    @Autowired
    private HcsaConfigClient hcsaConfigClient;
    @Autowired
    EmailClient emailClient;
    @Autowired
    AppInspectionStatusClient appInspectionStatusClient;
    private static final String INS_EMAIL_DTO="insEmailDto";
    private static final String TASK_DTO="taskDto";
    private static final String SUBJECT="subject";
    private static final String MSG_CON="messageContent";

    public void start(BaseProcessClass bpc){
        log.info("=======>>>>>startStep>>>>>>>>>>>>>>>>emailRequest");
        HttpServletRequest request=bpc.request;
        AccessUtil.initLoginUserInfo(bpc.request);
        ParamUtil.setSessionAttr(bpc.request, TASK_DTO, null);
        ParamUtil.setSessionAttr(request,"appPremCorrId",null);
        ParamUtil.setSessionAttr(request,MSG_CON, null);
        ParamUtil.setSessionAttr(request,"applicationViewDto",null);
        ParamUtil.setSessionAttr(request,INS_EMAIL_DTO, null);

    }

    public void prepareData(BaseProcessClass bpc)  {
        log.info("=======>>>>>prepareData>>>>>>>>>>>>>>>>emailRequest");
        HttpServletRequest request = bpc.request;
        String taskId = ParamUtil.getRequestString(request,"taskId");
//        if(StringUtil.isEmpty(taskId)){
//            taskId= "6E5A002D-9437-EA11-BE7E-000C29F371DC";
//        }
        TaskDto taskDto ;
        if(StringUtil.isEmpty(taskId)){
            taskDto= (TaskDto) ParamUtil.getSessionAttr(request,TASK_DTO);
        }
        else {
            taskDto= taskService.getTaskById(taskId);
        }
        String correlationId = taskDto.getRefNo();
        ApplicationViewDto applicationViewDto = inspEmailService.getAppViewByCorrelationId(correlationId);

        List<AppPremisesCorrelationDto> appPremisesCorrelationDtos=inspEmailService.getAppPremisesCorrelationsByPremises(correlationId);
        StringBuilder mesContext=new StringBuilder();
        String oneEmail="";
        for (AppPremisesCorrelationDto aDto:appPremisesCorrelationDtos
        ) {
            oneEmail=inspEmailService.getInsertEmail(aDto.getId()).getMessageContent();
            if(oneEmail.contains("Below are the review outcome")){
                mesContext.append(oneEmail.substring(0,oneEmail.indexOf("Below are the review outcome")));
                break;
            }
        }

        List<String> appPremCorrIds=new ArrayList<>();
        List<String> svcNames=new ArrayList<>();

        for (AppPremisesCorrelationDto appPremisesCorrelationDto:appPremisesCorrelationDtos
        ) {
            String ncEmail= inspEmailService.getInsertEmail(appPremisesCorrelationDto.getId()).getMessageContent();
            appPremCorrIds.add(appPremisesCorrelationDto.getId());
            ApplicationViewDto appViewDto = inspEmailService.getAppViewByCorrelationId(appPremisesCorrelationDto.getId());
            svcNames.add(inspectionService.getHcsaServiceDtoByServiceId(appViewDto.getApplicationDto().getServiceId()).getSvcName());
            if(oneEmail.contains("Below are the review outcome") && oneEmail.contains("<p>Thank you</p>")){
                mesContext.append(ncEmail.substring(ncEmail.indexOf("Below are the review outcome"),ncEmail.indexOf("<p>Thank you</p>")));
            }
            else {
                mesContext.append(ncEmail);
            }
        }
        for (AppPremisesCorrelationDto aDto:appPremisesCorrelationDtos
        ) {
            oneEmail=inspEmailService.getInsertEmail(aDto.getId()).getMessageContent();
            if(oneEmail.contains("<p>Thank you</p>")){
                mesContext.append(oneEmail.substring(oneEmail.indexOf("<p>Thank you</p>")));
                break;
            }
        }
        InspectionEmailTemplateDto inspectionEmailTemplateDto= new InspectionEmailTemplateDto();
        inspectionEmailTemplateDto.setAppPremCorrId(applicationViewDto.getAppPremisesCorrelationId());
        inspectionEmailTemplateDto.setMessageContent(mesContext.toString());
        inspectionEmailTemplateDto.setSubject("MOH IAIS – Review Outcome of Non-Compliance / Best Practices");

        List<SelectOption> appTypeOption = MasterCodeUtil.retrieveOptionsByCodes(new String[]{InspectionConstants.PROCESS_DECI_REVISE_EMAIL_CONTENT,InspectionConstants.PROCESS_DECI_SENDS_EMAIL_APPLICANT});

        ParamUtil.setSessionAttr(bpc.request, TASK_DTO, taskDto);
        ParamUtil.setSessionAttr(request,"appPremCorrIds", (Serializable) appPremCorrIds);
        ParamUtil.setRequestAttr(request,"appTypeOption", appTypeOption);
        ParamUtil.setSessionAttr(request,MSG_CON, mesContext.toString());
        ParamUtil.setRequestAttr(request,"svcNames",svcNames);
        ParamUtil.setSessionAttr(request,"applicationViewDto",applicationViewDto);
        ParamUtil.setSessionAttr(request,INS_EMAIL_DTO, inspectionEmailTemplateDto);
    }

    public void emailSubmitStep(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        InspectionEmailTemplateDto inspectionEmailTemplateDto = (InspectionEmailTemplateDto) ParamUtil.getSessionAttr(bpc.request,INS_EMAIL_DTO);
        ParamUtil.setSessionAttr(request,INS_EMAIL_DTO, inspectionEmailTemplateDto);
        String crudAction = ParamUtil.getString(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE);
        log.debug("*******************crudAction-->:" + crudAction);
    }

    public void previewEmail(BaseProcessClass bpc){
        log.info("=======>>>>>previewEmail>>>>>>>>>>>>>>>>emailRequest");
        HttpServletRequest request = bpc.request;
        String currentAction = ParamUtil.getString(request, IaisEGPConstant.CRUD_ACTION_TYPE);
        if(!"preview".equals(currentAction)){
            return;
        }
        String content=ParamUtil.getString(request,MSG_CON);
        String subject=ParamUtil.getString(request,SUBJECT);
        ParamUtil.setRequestAttr(request,SUBJECT, subject);
        ParamUtil.setRequestAttr(request,MSG_CON, content);
    }

    public void sendEmail(BaseProcessClass bpc) throws FeignException {

        log.info("=======>>>>>sendEmail>>>>>>>>>>>>>>>>emailRequest");
        HttpServletRequest request = bpc.request;
        LoginContext loginContext = (LoginContext) ParamUtil.getSessionAttr(bpc.request, AppConsts.SESSION_ATTR_LOGIN_USER);
        String userId = loginContext.getUserId();
        ApplicationViewDto applicationViewDto= (ApplicationViewDto) ParamUtil.getSessionAttr(request,"applicationViewDto");
        TaskDto taskDto= (TaskDto) ParamUtil.getSessionAttr(request,TASK_DTO);

        InspectionEmailTemplateDto inspectionEmailTemplateDto= (InspectionEmailTemplateDto) ParamUtil.getSessionAttr(request,INS_EMAIL_DTO);
        inspectionEmailTemplateDto.setSubject(ParamUtil.getString(request,SUBJECT));
        inspectionEmailTemplateDto.setMessageContent(ParamUtil.getString(request,"messageContent"));
        String decision=ParamUtil.getString(request,"decision");
        if(decision.equals("Select")){decision=InspectionConstants.PROCESS_DECI_SENDS_EMAIL_APPLICANT;}
        List<String>appPremCorrIds= (List<String>) ParamUtil.getSessionAttr(request,"appPremCorrIds");

        List<AppPremisesCorrelationDto> appPremisesCorrelationDtos=inspEmailService.getAppPremisesCorrelationsByAppGroupId(applicationViewDto.getApplicationDto().getAppGrpId());


        if (inspectionEmailTemplateDto.getSubject().isEmpty()){
            Map<String,String> errorMap = new HashMap<>();
            ParamUtil.setRequestAttr(request, DemoConstants.ERRORMAP,errorMap);
        }
        if (inspectionEmailTemplateDto.getMessageContent().isEmpty()){
            Map<String,String> errorMap = new HashMap<>();
            ParamUtil.setRequestAttr(request, DemoConstants.ERRORMAP,errorMap);
        }
        if (decision.equals(InspectionConstants.PROCESS_DECI_REVISE_EMAIL_CONTENT)){
            for(int i=1;i<=appPremCorrIds.size();i++){
                String param="revise"+i;
                if(ParamUtil.getString(request, param)==null){
                    appPremCorrIds.set(i-1,"");
                }
            }
            applicationViewDto.getApplicationDto().setStatus(ApplicationConsts.APPLICATION_STATUS_PENDING_RE_DRAFT_LETTER);
            applicationViewService.updateApplicaiton(applicationViewDto.getApplicationDto());
            AppInspectionStatusDto appInspectionStatusDto = appInspectionStatusClient.getAppInspectionStatusByPremId(applicationViewDto.getAppPremisesCorrelationId()).getEntity();
            appInspectionStatusDto.setStatus(InspectionConstants.INSPECTION_STATUS_PENDING_REVIEW_CHECKLIST_EMAIL);
            appInspectionStatusDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
            appInspectionStatusClient.update(appInspectionStatusDto);
            taskDto.setTaskKey(HcsaConsts.ROUTING_STAGE_INS);
            taskDto.setRoleId(RoleConsts.USER_ROLE_INSPECTION_LEAD);
            completedTask(taskDto);
            createAppPremisesRoutingHistory(applicationViewDto.getAppPremisesCorrelationId(), ApplicationConsts.APPLICATION_STATUS_PENDING_EMAIL_SENDING,InspectionConstants.PROCESS_DECI_ACKNOWLEDGE_EMAIL_CONTENT,taskDto,HcsaConsts.ROUTING_STAGE_POT,userId);

            for(int i=0;i<appPremCorrIds.size();i++){
                if(appPremCorrIds.get(i).equals(appPremisesCorrelationDtos.get(i).getId())){
                    ApplicationViewDto applicationViewDto1=applicationViewService.searchByCorrelationIdo(appPremCorrIds.get(i));
                    List<AppPremisesRoutingHistoryDto> appPremisesRoutingHistoryDtos= appPremisesRoutingHistoryService.getAppPremisesRoutingHistoryDtosByAppId(applicationViewDto1.getApplicationDto().getId());
                    AppPremisesRoutingHistoryDto appPremisesRoutingHisDto= appPremisesRoutingHistoryDtos.get(0);
                    String upDt=appPremisesRoutingHistoryDtos.get(0).getUpdatedDt();
                    for(AppPremisesRoutingHistoryDto appPremisesRoutingHistoryDto1:appPremisesRoutingHistoryDtos){
                        if(appPremisesRoutingHistoryDto1.getUpdatedDt().compareTo(upDt)>=0&&appPremisesRoutingHistoryDto1.getRoleId().equals(RoleConsts.USER_ROLE_INSPECTIOR)){
                            appPremisesRoutingHisDto=appPremisesRoutingHistoryDto1;
                        }
                        upDt=appPremisesRoutingHistoryDto1.getUpdatedDt();
                    }

                    applicationViewDto1.getApplicationDto().setStatus(ApplicationConsts.APPLICATION_STATUS_PENDING_RE_DRAFT_LETTER);
                    applicationViewService.updateApplicaiton(applicationViewDto1.getApplicationDto());
                    AppInspectionStatusDto appInspectionStatusDto1 = appInspectionStatusClient.getAppInspectionStatusByPremId(appPremCorrIds.get(i)).getEntity();
                    appInspectionStatusDto1.setStatus(InspectionConstants.INSPECTION_STATUS_PENDING_CHECKLIST_VERIFY);
                    appInspectionStatusDto1.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
                    appInspectionStatusClient.update(appInspectionStatusDto1);

                    String serviceId=applicationViewDto1.getApplicationDto().getServiceId();
                    HcsaSvcStageWorkingGroupDto hcsaSvcStageWorkingGroupDto = new HcsaSvcStageWorkingGroupDto();
                    hcsaSvcStageWorkingGroupDto.setType(applicationViewDto1.getApplicationDto().getApplicationType());
                    hcsaSvcStageWorkingGroupDto.setServiceId(serviceId);
                    hcsaSvcStageWorkingGroupDto.setStageId(HcsaConsts.ROUTING_STAGE_INS);
                    hcsaSvcStageWorkingGroupDto.setOrder(1);
                    TaskDto taskDto2=new TaskDto();
                    taskDto2.setRefNo(appPremCorrIds.get(i));
                    taskDto2.setTaskType(taskDto.getTaskType());
                    taskDto2.setTaskKey(HcsaConsts.ROUTING_STAGE_INS);
                    taskDto2.setUserId(appPremisesRoutingHisDto.getActionby());
                    taskDto2.setProcessUrl(TaskConsts.TASK_PROCESS_URL_INSPECTION_REVISE_NCEMAIL);
                    taskDto2.setRoleId(RoleConsts.USER_ROLE_INSPECTIOR);
                    taskDto2.setWkGrpId(hcsaConfigClient.getHcsaSvcStageWorkingGroupDto(hcsaSvcStageWorkingGroupDto).getEntity().getGroupId());

                    List<TaskDto> taskDtos = prepareTaskList(taskDto2,hcsaSvcStageWorkingGroupDto);
                    taskService.createTasks(taskDtos);
                    createAppPremisesRoutingHistory(applicationViewDto1.getAppPremisesCorrelationId(), ApplicationConsts.APPLICATION_STATUS_PENDING_RE_DRAFT_LETTER, InspectionConstants.PROCESS_DECI_ACKNOWLEDGE_EMAIL_CONTENT,taskDto2,HcsaConsts.ROUTING_STAGE_POT,taskDto2.getUserId());

                }
            }
        }
        else {
            applicationViewDto.getApplicationDto().setStatus(ApplicationConsts.APPLICATION_STATUS_PENDING_INSPECTION_REPORT);
            applicationViewService.updateApplicaiton(applicationViewDto.getApplicationDto());
            AppInspectionStatusDto appInspectionStatusDto1 = appInspectionStatusClient.getAppInspectionStatusByPremId(applicationViewDto.getAppPremisesCorrelationId()).getEntity();
            appInspectionStatusDto1.setStatus(InspectionConstants.INSPECTION_STATUS_PENDING_NC_RECTIFICATION_EMAIL);
            appInspectionStatusDto1.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
            appInspectionStatusClient.update(appInspectionStatusDto1);
            taskDto.setTaskKey(HcsaConsts.ROUTING_STAGE_INS);
            taskDto.setRoleId(RoleConsts.USER_ROLE_INSPECTION_LEAD);
            completedTask(taskDto);
            createAppPremisesRoutingHistory(applicationViewDto.getAppPremisesCorrelationId(), ApplicationConsts.APPLICATION_STATUS_PENDING_EMAIL_SENDING,InspectionConstants.PROCESS_DECI_ACKNOWLEDGE_EMAIL_CONTENT, taskDto,HcsaConsts.ROUTING_STAGE_POT,userId);


            for(int i=0;i<appPremCorrIds.size();i++){
                ApplicationViewDto applicationViewDto1=applicationViewService.searchByCorrelationIdo(appPremCorrIds.get(i));
                List<AppPremisesRoutingHistoryDto> appPremisesRoutingHistoryDtos= appPremisesRoutingHistoryService.getAppPremisesRoutingHistoryDtosByAppId(applicationViewDto1.getApplicationDto().getId());
                AppPremisesRoutingHistoryDto appPremisesRoutingHisDto= appPremisesRoutingHistoryDtos.get(0);
                String upDt=appPremisesRoutingHistoryDtos.get(0).getUpdatedDt();
                for(AppPremisesRoutingHistoryDto appPremisesRoutingHistoryDto:appPremisesRoutingHistoryDtos){
                    if(appPremisesRoutingHistoryDto.getUpdatedDt().compareTo(upDt)>=0 &&appPremisesRoutingHistoryDto.getRoleId().equals(RoleConsts.USER_ROLE_INSPECTIOR)){
                        appPremisesRoutingHisDto=appPremisesRoutingHistoryDto;
                    }
                    upDt=appPremisesRoutingHistoryDto.getUpdatedDt();
                }

                applicationViewDto1.getApplicationDto().setStatus(ApplicationConsts.APPLICATION_STATUS_PENDING_INSPECTION_REPORT);
                applicationViewService.updateApplicaiton(applicationViewDto1.getApplicationDto());
                AppInspectionStatusDto appInspectionStatusDto2 = appInspectionStatusClient.getAppInspectionStatusByPremId(appPremCorrIds.get(i)).getEntity();
                appInspectionStatusDto2.setStatus(InspectionConstants.INSPECTION_STATUS_PENDING_CHECKLIST_VERIFY);
                appInspectionStatusDto2.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
                appInspectionStatusClient.update(appInspectionStatusDto2);

                String serviceId=applicationViewDto1.getApplicationDto().getServiceId();
                HcsaSvcStageWorkingGroupDto hcsaSvcStageWorkingGroupDto = new HcsaSvcStageWorkingGroupDto();
                hcsaSvcStageWorkingGroupDto.setType(applicationViewDto1.getApplicationDto().getApplicationType());
                hcsaSvcStageWorkingGroupDto.setServiceId(serviceId);
                hcsaSvcStageWorkingGroupDto.setStageId(HcsaConsts.ROUTING_STAGE_INS);
                hcsaSvcStageWorkingGroupDto.setOrder(1);
                TaskDto taskDto2=new TaskDto();
                taskDto2.setRefNo(appPremCorrIds.get(i));
                taskDto2.setTaskType(taskDto.getTaskType());
                taskDto2.setTaskKey(HcsaConsts.ROUTING_STAGE_INS);
                taskDto2.setUserId(appPremisesRoutingHisDto.getActionby());
                taskDto2.setProcessUrl(TaskConsts.TASK_PROCESS_URL_INSPECTION_REPORT);
                taskDto2.setRoleId(RoleConsts.USER_ROLE_INSPECTIOR);
                taskDto2.setWkGrpId(hcsaConfigClient.getHcsaSvcStageWorkingGroupDto(hcsaSvcStageWorkingGroupDto).getEntity().getGroupId());

                List<TaskDto> taskDtos = prepareTaskList(taskDto2,hcsaSvcStageWorkingGroupDto);
                taskService.createTasks(taskDtos);
                createAppPremisesRoutingHistory(applicationViewDto1.getAppPremisesCorrelationId(), ApplicationConsts.APPLICATION_STATUS_PENDING_INSPECTION_REPORT, InspectionConstants.PROCESS_DECI_ACKNOWLEDGE_EMAIL_CONTENT,taskDto2,HcsaConsts.ROUTING_STAGE_POT,taskDto2.getUserId());

            }
            EmailDto emailDto=new EmailDto();
            emailDto.setContent(inspectionEmailTemplateDto.getMessageContent());
            emailDto.setSubject(inspectionEmailTemplateDto.getSubject());
            emailDto.setSender(AppConsts.MOH_AGENCY_NAME);

            //String requestRefNum = emailClient.sendNotification(emailDto).getEntity();
        }
        ParamUtil.setSessionAttr(request,INS_EMAIL_DTO, inspectionEmailTemplateDto);

    }

    private AppPremisesRoutingHistoryDto createAppPremisesRoutingHistory(String appPremisesCorrelationId, String appStatus,String decision,
                                                                         TaskDto taskDto,String subStage,String userId ) {
        AppPremisesRoutingHistoryDto appPremisesRoutingHistoryDto = new AppPremisesRoutingHistoryDto();
        appPremisesRoutingHistoryDto.setAppPremCorreId(appPremisesCorrelationId);
        appPremisesRoutingHistoryDto.setStageId(taskDto.getTaskKey());
        appPremisesRoutingHistoryDto.setProcessDecision(decision);
        appPremisesRoutingHistoryDto.setAppStatus(appStatus);
        appPremisesRoutingHistoryDto.setActionby(userId);

        appPremisesRoutingHistoryDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        appPremisesRoutingHistoryDto.setRoleId(taskDto.getRoleId());
        appPremisesRoutingHistoryDto.setWrkGrpId(taskDto.getWkGrpId());
        appPremisesRoutingHistoryDto.setSubStage(subStage);
        appPremisesRoutingHistoryDto = appPremisesRoutingHistoryService.createAppPremisesRoutingHistory(appPremisesRoutingHistoryDto);
        return appPremisesRoutingHistoryDto;
    }
    private List<TaskDto> prepareTaskList(TaskDto taskDto, HcsaSvcStageWorkingGroupDto hcsaSvcStageWorkingGroupDto) {
        List<TaskDto> list = new ArrayList<>();
        List<HcsaSvcStageWorkingGroupDto> listhcsaSvcStageWorkingGroupDto = hcsaConfigClient.getSvcWorkGroup(hcsaSvcStageWorkingGroupDto).getEntity();
        String schemeType = listhcsaSvcStageWorkingGroupDto.get(0).getSchemeType();
        Integer count = listhcsaSvcStageWorkingGroupDto.get(0).getCount();
        taskDto.setWkGrpId(hcsaConfigClient.getHcsaSvcStageWorkingGroupDto(hcsaSvcStageWorkingGroupDto).getEntity().getGroupId());

        taskDto.setId(null);
        taskDto.setDateAssigned(new Date());
        taskDto.setSlaDateCompleted(null);
        taskDto.setSlaRemainInDays(null);
        taskDto.setScore(count);
        taskDto.setSlaAlertInDays(2);
        taskDto.setPriority(0);
        taskDto.setSlaInDays(5);
        // taskDto.setTaskType(schemeType);
        taskDto.setTaskStatus(TaskConsts.TASK_STATUS_PENDING);
        taskDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        list.add(taskDto);
        return list;
    }
    private TaskDto completedTask(TaskDto taskDto) {
        taskDto.setTaskStatus(TaskConsts.TASK_STATUS_COMPLETED);
        taskDto.setSlaDateCompleted(new Date());
        taskDto.setSlaRemainInDays(remainDays(taskDto));
        taskDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        return taskService.updateTask(taskDto);
    }
    private int remainDays(TaskDto taskDto) {
        int result = 0;
        String resultStr = DurationFormatUtils.formatPeriod(taskDto.getDateAssigned().getTime(), taskDto.getSlaDateCompleted().getTime(), "d");
        log.debug(StringUtil.changeForLog("The resultStr is -->:") + resultStr);
        return result;
    }

    public void doRecallEmail(BaseProcessClass bpc) {
        log.info("=======>>>>>doRecallEmail>>>>>>>>>>>>>>>>emailRequest");
    }

}

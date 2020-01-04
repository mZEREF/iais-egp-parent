package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.constant.HcsaConsts;
import com.ecquaria.cloud.moh.iais.common.constant.inspection.InspectionConstants;
import com.ecquaria.cloud.moh.iais.common.constant.role.RoleConsts;
import com.ecquaria.cloud.moh.iais.common.constant.sample.DemoConstants;
import com.ecquaria.cloud.moh.iais.common.constant.task.TaskConsts;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.application.ApplicationViewDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesCorrelationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesRoutingHistoryDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcStageWorkingGroupDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspectionEmailTemplateDto;
import com.ecquaria.cloud.moh.iais.common.dto.task.TaskDto;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.helper.MasterCodeUtil;
import com.ecquaria.cloud.moh.iais.service.AppPremisesRoutingHistoryService;
import com.ecquaria.cloud.moh.iais.service.ApplicationService;
import com.ecquaria.cloud.moh.iais.service.ApplicationViewService;
import com.ecquaria.cloud.moh.iais.service.InspEmailService;
import com.ecquaria.cloud.moh.iais.service.InspectionPreTaskService;
import com.ecquaria.cloud.moh.iais.service.InspectionService;
import com.ecquaria.cloud.moh.iais.service.TaskService;
import com.ecquaria.cloud.moh.iais.service.client.HcsaConfigClient;
import com.ecquaria.cloudfeign.FeignException;
import freemarker.template.TemplateException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.time.DurationFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
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

    public void start(BaseProcessClass bpc){
        log.info("=======>>>>>startStep>>>>>>>>>>>>>>>>emailRequest");
        //AccessUtil.initLoginUserInfo(bpc.request);

    }

    public void prepareData(BaseProcessClass bpc) throws IOException, TemplateException {
        log.info("=======>>>>>prepareData>>>>>>>>>>>>>>>>emailRequest");
        HttpServletRequest request = bpc.request;
        String taskId = ParamUtil.getString(request,"taskId");
        TaskDto taskDto = taskService.getTaskById(taskId);
        if(StringUtil.isEmpty(taskDto)){
            taskDto= (TaskDto) ParamUtil.getSessionAttr(request,"taskDto");
        }
        String correlationId = taskDto.getRefNo();
        ApplicationViewDto applicationViewDto = inspEmailService.getAppViewByCorrelationId(correlationId);
        List<AppPremisesCorrelationDto> appPremisesCorrelationDtos=inspEmailService.getAppPremisesCorrelationsByAppId(applicationViewDto.getApplicationDto().getId());
        StringBuilder mesContext=new StringBuilder();
        String oneEmail=inspEmailService.getInsertEmail(appPremisesCorrelationDtos.get(0).getId()).getMessageContent();
        mesContext.append(oneEmail.substring(0,oneEmail.indexOf("Below are the review outcome")));
        List<String> appIds=new ArrayList<>();
        for (AppPremisesCorrelationDto appPremisesCorrelationDto:appPremisesCorrelationDtos
                ) {
            String ncEmail= inspEmailService.getInsertEmail(appPremisesCorrelationDto.getId()).getMessageContent();
            appIds.add(appPremisesCorrelationDto.getApplicationId());
            mesContext.append(ncEmail.substring(ncEmail.indexOf("Below are the review outcome"),ncEmail.indexOf("<p>Thank you</p>")));
        }
        mesContext.append(oneEmail.substring(oneEmail.indexOf("<p>Thank you</p>")));
        InspectionEmailTemplateDto inspectionEmailTemplateDto= new InspectionEmailTemplateDto();
        inspectionEmailTemplateDto.setAppPremCorrId(applicationViewDto.getAppPremisesCorrelationId());
        inspectionEmailTemplateDto.setMessageContent(mesContext.toString());
        inspectionEmailTemplateDto.setSubject("Inspection NC / BP Outcome");

        List<SelectOption> appTypeOption = MasterCodeUtil.retrieveOptionsByCodes(new String[]{InspectionConstants.PROCESS_DECI_REVISE_EMAIL_CONTENT,InspectionConstants.PROCESS_DECI_SENDS_EMAIL_APPLICANT});

        ParamUtil.setSessionAttr(bpc.request, "taskDto", taskDto);
        ParamUtil.setSessionAttr(request,"appIds", (Serializable) appIds);
        ParamUtil.setRequestAttr(request,"appTypeOption", appTypeOption);
        ParamUtil.setSessionAttr(request,"mesContext", mesContext.toString());
        ParamUtil.setSessionAttr(request,"applicationViewDto",applicationViewDto);
        ParamUtil.setSessionAttr(request,"insEmailDto", inspectionEmailTemplateDto);
    }

    public void emailSubmitStep(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        InspectionEmailTemplateDto inspectionEmailTemplateDto = (InspectionEmailTemplateDto) ParamUtil.getSessionAttr(bpc.request,"insEmailDto");
        ParamUtil.setSessionAttr(request,"insEmailDto", inspectionEmailTemplateDto);
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
        String content=ParamUtil.getString(request,"messageContent");
        ParamUtil.setRequestAttr(request,"content", content);
    }

    public void sendEmail(BaseProcessClass bpc) throws FeignException {

        log.info("=======>>>>>sendEmail>>>>>>>>>>>>>>>>emailRequest");
        HttpServletRequest request = bpc.request;
        ApplicationViewDto applicationViewDto= (ApplicationViewDto) ParamUtil.getSessionAttr(request,"applicationViewDto");
        TaskDto taskDto= (TaskDto) ParamUtil.getSessionAttr(request,"taskDto");
        String serviceId=applicationViewDto.getApplicationDto().getServiceId();

        InspectionEmailTemplateDto inspectionEmailTemplateDto= (InspectionEmailTemplateDto) ParamUtil.getSessionAttr(request,"insEmailDto");
        inspectionEmailTemplateDto.setSubject(ParamUtil.getString(request,"subject"));
        inspectionEmailTemplateDto.setMessageContent(ParamUtil.getString(request,"messageContent"));
        String currentAction = ParamUtil.getString(request, IaisEGPConstant.CRUD_ACTION_TYPE);
        if(!"send".equals(currentAction)){
            return;
        }
        String decision=ParamUtil.getString(request,"decision");
        if(decision.equals("Please select")){decision=InspectionConstants.PROCESS_DECI_SENDS_EMAIL_APPLICANT;}
        List<String>appIds= (List<String>) ParamUtil.getSessionAttr(request,"appIds");
        for(int i=1;i<=appIds.size();i++){
            String param="revise"+String.valueOf(i);
            if(ParamUtil.getString(request, param)==null){
                appIds.set(i-1,"");
            }
        }
        List<ApplicationDto> applicationDtos= applicationService.getApplicaitonsByAppGroupId(applicationViewDto.getApplicationDto().getAppGrpId());

        if (inspectionEmailTemplateDto.getSubject().isEmpty()){
            Map<String,String> errorMap = new HashMap<>();
            ParamUtil.setRequestAttr(request, DemoConstants.ERRORMAP,errorMap);
        }
        if (inspectionEmailTemplateDto.getMessageContent().isEmpty()){
            Map<String,String> errorMap = new HashMap<>();
            ParamUtil.setRequestAttr(request, DemoConstants.ERRORMAP,errorMap);
        }
        if (decision.equals(InspectionConstants.PROCESS_DECI_REVISE_EMAIL_CONTENT)){

            applicationViewDto.getApplicationDto().setStatus(ApplicationConsts.APPLICATION_GROUP_STATUS_ROLL_BACK);
            applicationViewService.updateApplicaiton(applicationViewDto.getApplicationDto());

            String taskKey = taskDto.getTaskKey();
            createAppPremisesRoutingHistory(applicationViewDto.getAppPremisesCorrelationId(), ApplicationConsts.APPLICATION_STATUS_APPROVED,InspectionConstants.PROCESS_DECI_ACKNOWLEDGE_EMAIL_CONTENT, taskKey,taskDto.getRoleId(),taskDto.getWkGrpId());

            for(int i=0;i<appIds.size();i++){
                if(appIds.get(i).equals(applicationDtos.get(i))){
                    AppPremisesRoutingHistoryDto appPremisesRoutingHisDto= new AppPremisesRoutingHistoryDto();
                    List<AppPremisesRoutingHistoryDto> appPremisesRoutingHistoryDtos= appPremisesRoutingHistoryService.getAppPremisesRoutingHistoryDtosByAppId(applicationViewDto.getApplicationDto().getId());
                    String upDt=appPremisesRoutingHistoryDtos.get(0).getUpdatedDt();
                    for(AppPremisesRoutingHistoryDto appPremisesRoutingHistoryDto1:appPremisesRoutingHistoryDtos){
                        if(appPremisesRoutingHistoryDto1.getUpdatedDt().compareTo(upDt)<0){
                            upDt=appPremisesRoutingHistoryDto1.getUpdatedDt();
                            appPremisesRoutingHisDto=appPremisesRoutingHistoryDto1;
                        }
                    }
                    applicationDtos.get(i).setStatus(ApplicationConsts.APPLICATION_STATUS_ROLL_BACK);
                    applicationViewService.updateApplicaiton(applicationDtos.get(i));

                    taskKey = HcsaConsts.ROUTING_STAGE_INS;
                    createAppPremisesRoutingHistory(applicationViewDto.getAppPremisesCorrelationId(), ApplicationConsts.APPLICATION_STATUS_APPROVED,InspectionConstants.PROCESS_DECI_ACKNOWLEDGE_EMAIL_CONTENT, taskKey,taskDto.getRoleId(),taskDto.getWkGrpId());

                    HcsaSvcStageWorkingGroupDto hcsaSvcStageWorkingGroupDto = new HcsaSvcStageWorkingGroupDto();
                    hcsaSvcStageWorkingGroupDto.setServiceId(serviceId);
                    hcsaSvcStageWorkingGroupDto.setStageId(taskKey);
                    hcsaSvcStageWorkingGroupDto.setOrder(1);
                    TaskDto taskDto2=taskDto;
                    taskDto2.setTaskKey(taskKey);
                    taskDto2.setUserId(appPremisesRoutingHisDto.getActionby());
                    taskDto2.setProcessUrl(TaskConsts.TASK_PROCESS_URL_INSPECTION_REVISE_NCEMAIL);
                    taskDto2.setRoleId(RoleConsts.USER_ROLE_INSPECTIOR);

                    completedTask(taskDto2);
                    List<TaskDto> taskDtos = prepareTaskList(taskDto2,hcsaSvcStageWorkingGroupDto);
                    taskService.createTasks(taskDtos);
                    createAppPremisesRoutingHistory(applicationViewDto.getAppPremisesCorrelationId(), ApplicationConsts.APPLICATION_STATUS_APPROVED, InspectionConstants.PROCESS_DECI_ACKNOWLEDGE_EMAIL_CONTENT,taskDto2.getTaskKey(),taskDto2.getRoleId(),taskDto2.getWkGrpId());

                }
            }
        }
        else {
            applicationViewDto.getApplicationDto().setStatus(ApplicationConsts.APPLICATION_GROUP_STATUS_SUBMITED);
            applicationViewService.updateApplicaiton(applicationViewDto.getApplicationDto());
            String taskKey = HcsaConsts.ROUTING_STAGE_INS;
            createAppPremisesRoutingHistory(applicationViewDto.getAppPremisesCorrelationId(), ApplicationConsts.APPLICATION_STATUS_APPROVED,InspectionConstants.PROCESS_DECI_ACKNOWLEDGE_EMAIL_CONTENT, taskKey,taskDto.getRoleId(),taskDto.getWkGrpId());

            HcsaSvcStageWorkingGroupDto hcsaSvcStageWorkingGroupDto = new HcsaSvcStageWorkingGroupDto();
            hcsaSvcStageWorkingGroupDto.setServiceId(serviceId);
            hcsaSvcStageWorkingGroupDto.setStageId(taskKey);
            hcsaSvcStageWorkingGroupDto.setOrder(1);
            TaskDto taskDto2=taskDto;
            taskDto2.setTaskKey(taskKey);
            taskDto2.setProcessUrl(TaskConsts.TASK_PROCESS_URL_INSPECTION_REVISE_NCEMAIL);
            taskDto2.setRoleId(RoleConsts.USER_ROLE_INSPECTIOR);

            completedTask(taskDto2);
            List<TaskDto> taskDtos = prepareTaskList(taskDto2,hcsaSvcStageWorkingGroupDto);
            taskService.createTasks(taskDtos);
            createAppPremisesRoutingHistory(applicationViewDto.getAppPremisesCorrelationId(), ApplicationConsts.APPLICATION_STATUS_APPROVED, InspectionConstants.PROCESS_DECI_ACKNOWLEDGE_EMAIL_CONTENT,taskDto2.getTaskKey(),taskDto2.getRoleId(),taskDto2.getWkGrpId());

            inspEmailService.insertEmailTemplate(inspectionEmailTemplateDto);
        }
        ParamUtil.setSessionAttr(request,"insEmailDto", inspectionEmailTemplateDto);

    }

    private AppPremisesRoutingHistoryDto createAppPremisesRoutingHistory(String appPremisesCorrelationId, String appStatus,String decision,
                                                                         String stageId,String roleId,String wrkGrpId ) {
        AppPremisesRoutingHistoryDto appPremisesRoutingHistoryDto = new AppPremisesRoutingHistoryDto();
        appPremisesRoutingHistoryDto.setAppPremCorreId(appPremisesCorrelationId);
        appPremisesRoutingHistoryDto.setStageId(stageId);
        appPremisesRoutingHistoryDto.setProcessDecision(decision);
        appPremisesRoutingHistoryDto.setAppStatus(appStatus);
        //appPremisesRoutingHistoryDto.setActionby(IaisEGPHelper.getCurrentAuditTrailDto().getMohUserGuid());
        appPremisesRoutingHistoryDto.setActionby("C55C9E62-750B-EA11-BE7D-000C29F371DC");
        appPremisesRoutingHistoryDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        appPremisesRoutingHistoryDto.setRoleId(roleId);
        appPremisesRoutingHistoryDto.setWrkGrpId(wrkGrpId);
        appPremisesRoutingHistoryDto = appPremisesRoutingHistoryService.createAppPremisesRoutingHistory(appPremisesRoutingHistoryDto);
        return appPremisesRoutingHistoryDto;
    }
    private List<TaskDto> prepareTaskList(TaskDto taskDto, HcsaSvcStageWorkingGroupDto hcsaSvcStageWorkingGroupDto) {
        List<TaskDto> list = new ArrayList<>();
        List<HcsaSvcStageWorkingGroupDto> listhcsaSvcStageWorkingGroupDto = hcsaConfigClient.getSvcWorkGroup(hcsaSvcStageWorkingGroupDto).getEntity();
        String schemeType = listhcsaSvcStageWorkingGroupDto.get(0).getSchemeType();
        Integer count = listhcsaSvcStageWorkingGroupDto.get(0).getCount();

        taskDto.setId(null);
        taskDto.setDateAssigned(null);
        taskDto.setSlaDateCompleted(null);
        taskDto.setSlaRemainInDays(null);
        taskDto.setScore(count);

        taskDto.setTaskType(schemeType);
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

package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.common.config.SystemParamConfig;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.constant.HcsaConsts;
import com.ecquaria.cloud.moh.iais.common.constant.inspection.InspectionConstants;
import com.ecquaria.cloud.moh.iais.common.constant.message.MessageConstants;
import com.ecquaria.cloud.moh.iais.common.constant.role.RoleConsts;
import com.ecquaria.cloud.moh.iais.common.constant.task.TaskConsts;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.application.ApplicationViewDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesCorrelationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesRecommendationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesRoutingHistoryDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcStageWorkingGroupDto;
import com.ecquaria.cloud.moh.iais.common.dto.inbox.InterMessageDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.AppInspectionStatusDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspectionPreTaskDto;
import com.ecquaria.cloud.moh.iais.common.dto.task.TaskDto;
import com.ecquaria.cloud.moh.iais.dto.LoginContext;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.helper.MasterCodeUtil;
import com.ecquaria.cloud.moh.iais.service.ApplicationViewService;
import com.ecquaria.cloud.moh.iais.service.InboxMsgService;
import com.ecquaria.cloud.moh.iais.service.InspectionRectificationProService;
import com.ecquaria.cloud.moh.iais.service.TaskService;
import com.ecquaria.cloud.moh.iais.service.client.AppInspectionStatusClient;
import com.ecquaria.cloud.moh.iais.service.client.AppPremisesCorrClient;
import com.ecquaria.cloud.moh.iais.service.client.AppPremisesRoutingHistoryClient;
import com.ecquaria.cloud.moh.iais.service.client.InsRepClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author Shicheng
 * @date 2019/12/18 14:27
 **/
@Service
public class InspectionRectificationProImpl implements InspectionRectificationProService {

    @Autowired
    private AppPremisesRoutingHistoryClient appPremisesRoutingHistoryClient;

    @Autowired
    private TaskService taskService;

    @Autowired
    private ApplicationViewService applicationViewService;

    @Autowired
    private AppPremisesCorrClient appPremisesCorrClient;

    @Autowired
    private AppInspectionStatusClient appInspectionStatusClient;

    @Autowired
    private InsRepClient insRepClient;

    @Autowired
    private InboxMsgService inboxMsgService;

    @Autowired
    private SystemParamConfig systemParamConfig;

    @Override
    public AppPremisesRoutingHistoryDto getAppHistoryByTask(String appId, String stageId) {
        AppPremisesRoutingHistoryDto appPremisesRoutingHistoryDto = appPremisesRoutingHistoryClient.getAppPremisesRoutingHistorysByAppIdAndStageId(appId, stageId).getEntity();
        if(appPremisesRoutingHistoryDto == null){
            appPremisesRoutingHistoryDto = new AppPremisesRoutingHistoryDto();
        }
        return appPremisesRoutingHistoryDto;
    }

    @Override
    public List<SelectOption> getProcessRecDecOption() {
        String[] processDecArr = new String[]
                {InspectionConstants.PROCESS_DECI_REQUEST_FOR_INFORMATION, InspectionConstants.PROCESS_DECI_ACCEPTS_RECTIFICATION,
                 InspectionConstants.PROCESS_DECI_ACCEPTS_RECTIFICATION_CONDITION};
        List<SelectOption> processDecOption = MasterCodeUtil.retrieveOptionsByCodes(processDecArr);
        return processDecOption;
    }

    @Override
    public void routingTaskToReport(TaskDto taskDto, InspectionPreTaskDto inspectionPreTaskDto, ApplicationViewDto applicationViewDto, LoginContext loginContext) {
        ApplicationDto applicationDto = applicationViewDto.getApplicationDto();
        List<ApplicationDto> applicationDtos = new ArrayList<>();
        applicationDtos.add(applicationDto);
        List<HcsaSvcStageWorkingGroupDto> hcsaSvcStageWorkingGroupDtos = generateHcsaSvcStageWorkingGroupDtos(applicationDtos,HcsaConsts.ROUTING_STAGE_INS);
        hcsaSvcStageWorkingGroupDtos = taskService.getTaskConfig(hcsaSvcStageWorkingGroupDtos);
        taskDto.setSlaDateCompleted(new Date());
        taskDto.setSlaRemainInDays(taskService.remainDays(taskDto));
        taskDto.setTaskStatus(TaskConsts.TASK_STATUS_COMPLETED);
        taskDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        taskService.updateTask(taskDto);
        updateInspectionStatus(applicationDto, InspectionConstants.INSPECTION_STATUS_PENDING_PREPARE_REPORT);
        if(InspectionConstants.PROCESS_DECI_ACCEPTS_RECTIFICATION.equals(inspectionPreTaskDto.getSelectValue())){
            createAppPremisesRoutingHistory(applicationViewDto.getAppPremisesCorrelationId(),applicationDto.getStatus(),taskDto.getTaskKey(),inspectionPreTaskDto.getInternalMarks(), InspectionConstants.PROCESS_DECI_ACCEPTS_RECTIFICATION, RoleConsts.USER_ROLE_INSPECTIOR, HcsaConsts.ROUTING_STAGE_POT);
            createTaskForReport(hcsaSvcStageWorkingGroupDtos, taskDto, loginContext);
            ApplicationDto applicationDto1 = updateApplication(applicationDto, ApplicationConsts.APPLICATION_STATUS_PENDING_INSPECTION_REPORT);
            applicationViewDto.setApplicationDto(applicationDto1);
            createAppPremisesRoutingHistory(applicationViewDto.getAppPremisesCorrelationId(),applicationDto1.getStatus(), HcsaConsts.ROUTING_STAGE_INS,null, null, RoleConsts.USER_ROLE_INSPECTIOR, HcsaConsts.ROUTING_STAGE_POT);
        } else if (InspectionConstants.PROCESS_DECI_ACCEPTS_RECTIFICATION_CONDITION.equals(inspectionPreTaskDto.getSelectValue())){
            createAppPremisesRoutingHistory(applicationViewDto.getAppPremisesCorrelationId(),applicationDto.getStatus(),taskDto.getTaskKey(),inspectionPreTaskDto.getInternalMarks(), InspectionConstants.PROCESS_DECI_ACCEPTS_RECTIFICATION_CONDITION, RoleConsts.USER_ROLE_INSPECTIOR, HcsaConsts.ROUTING_STAGE_POT);
            createRecommendation(inspectionPreTaskDto, applicationViewDto);
            createTaskForReport(hcsaSvcStageWorkingGroupDtos, taskDto, loginContext);
            ApplicationDto applicationDto1 = updateApplication(applicationDto, ApplicationConsts.APPLICATION_STATUS_PENDING_INSPECTION_REPORT);
            applicationViewDto.setApplicationDto(applicationDto1);
            createAppPremisesRoutingHistory(applicationViewDto.getAppPremisesCorrelationId(),applicationDto1.getStatus(), HcsaConsts.ROUTING_STAGE_INS,null, null, RoleConsts.USER_ROLE_INSPECTIOR, HcsaConsts.ROUTING_STAGE_POT);
        } else if (InspectionConstants.PROCESS_DECI_REQUEST_FOR_INFORMATION.equals(inspectionPreTaskDto.getSelectValue())){
            createAppPremisesRoutingHistory(applicationViewDto.getAppPremisesCorrelationId(),applicationDto.getStatus(),taskDto.getTaskKey(),inspectionPreTaskDto.getInternalMarks(), InspectionConstants.PROCESS_DECI_ACCEPTS_RECTIFICATION_CONDITION, RoleConsts.USER_ROLE_INSPECTIOR, HcsaConsts.ROUTING_STAGE_POT);
            ApplicationDto applicationDto1 = updateApplication(applicationDto, ApplicationConsts.APPLICATION_STATUS_PENDING_NC_RECTIFICATION);
            updateInspectionStatus(applicationDto, InspectionConstants.INSPECTION_STATUS_PENDING_REQUEST_FOR_INFORMATION);
            InterMessageDto interMessageDto = new InterMessageDto();
            interMessageDto.setSrcSystemId(AppConsts.MOH_IAIS_SYSTEM_SRC_ID);
            interMessageDto.setSubject(MessageConstants.MESSAGE_SUBJECT_REQUEST_FOR_INFORMATION);
            interMessageDto.setMessageType(MessageConstants.MESSAGE_TYPE_NOTIFICATION);
            String mesNO = inboxMsgService.getMessageNo();
            interMessageDto.setRefNo(mesNO);
            interMessageDto.setService_id(applicationDto1.getServiceId());
            String url = systemParamConfig.getInterServerName() +
                    MessageConstants.MESSAGE_CALL_BACK_URL_NEWAPPLICATION +
                    applicationDto1.getApplicationNo();
            interMessageDto.setProcessUrl(url);
            interMessageDto.setStatus(AppConsts.COMMON_STATUS_ACTIVE);
            interMessageDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
            inboxMsgService.saveInterMessage(interMessageDto);
        }


    }

    private List<HcsaSvcStageWorkingGroupDto> generateHcsaSvcStageWorkingGroupDtos(List<ApplicationDto> applicationDtos, String stageId){
        List<HcsaSvcStageWorkingGroupDto> hcsaSvcStageWorkingGroupDtos = new ArrayList();
        for(ApplicationDto applicationDto : applicationDtos){
            HcsaSvcStageWorkingGroupDto hcsaSvcStageWorkingGroupDto = new HcsaSvcStageWorkingGroupDto();
            hcsaSvcStageWorkingGroupDto.setStageId(stageId);
            hcsaSvcStageWorkingGroupDto.setServiceId(applicationDto.getServiceId());
            hcsaSvcStageWorkingGroupDto.setType(applicationDto.getApplicationType());
            hcsaSvcStageWorkingGroupDtos.add(hcsaSvcStageWorkingGroupDto);
        }
        return hcsaSvcStageWorkingGroupDtos;
    }

    private void createRecommendation(InspectionPreTaskDto inspectionPreTaskDto, ApplicationViewDto applicationViewDto) {
        AppPremisesRecommendationDto appPremisesRecommendationDto = new AppPremisesRecommendationDto();
        AppPremisesCorrelationDto appPremisesCorrelationDto = getAppPreCorrDtoByAppDto(applicationViewDto.getApplicationDto());
        appPremisesRecommendationDto.setId(null);
        appPremisesRecommendationDto.setAppPremCorreId(appPremisesCorrelationDto.getId());
        appPremisesRecommendationDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        appPremisesRecommendationDto.setBestPractice(null);
        appPremisesRecommendationDto.setRecomType(InspectionConstants.RECOM_TYPE_INSPECTYPE);
        appPremisesRecommendationDto.setChronoUnit(null);
        appPremisesRecommendationDto.setRecomDecision(inspectionPreTaskDto.getSelectValue());
        appPremisesRecommendationDto.setRecomInDate(new Date());
        appPremisesRecommendationDto.setVersion(1);
        appPremisesRecommendationDto.setStatus(AppConsts.COMMON_STATUS_ACTIVE);
        appPremisesRecommendationDto.setRemarks(inspectionPreTaskDto.getAccCondMarks());
        appPremisesRecommendationDto.setRecomInNumber(null);
        insRepClient.saveData(appPremisesRecommendationDto);
    }

    private void createTaskForReport(List<HcsaSvcStageWorkingGroupDto> hcsaSvcStageWorkingGroupDtos, TaskDto td, LoginContext loginContext) {
        TaskDto taskDto = new TaskDto();
        List<TaskDto> taskDtos = new ArrayList<>();
        taskDto.setId(null);
        taskDto.setTaskStatus(TaskConsts.TASK_STATUS_PENDING);
        taskDto.setPriority(td.getPriority());
        taskDto.setRefNo(td.getRefNo());
        taskDto.setScore(hcsaSvcStageWorkingGroupDtos.get(0).getCount());
        taskDto.setSlaAlertInDays(td.getSlaAlertInDays());
        taskDto.setSlaDateCompleted(null);
        taskDto.setSlaInDays(td.getSlaInDays());
        taskDto.setSlaRemainInDays(null);
        taskDto.setTaskKey(td.getTaskKey());
        taskDto.setTaskType(td.getTaskType());
        taskDto.setWkGrpId(td.getWkGrpId());
        taskDto.setUserId(loginContext.getUserId());
        taskDto.setDateAssigned(new Date());
        taskDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        taskDto.setProcessUrl(TaskConsts.TASK_PROCESS_URL_INSPECTION_REPORT);
        taskDto.setRoleId(RoleConsts.USER_ROLE_INSPECTIOR);
        taskDtos.add(taskDto);
        taskService.createTasks(taskDtos);
    }
    private AppPremisesCorrelationDto getAppPreCorrDtoByAppDto(ApplicationDto applicationDto){
        List<AppPremisesCorrelationDto> appPremisesCorrelationDtos =  appPremisesCorrClient.getAppPremisesCorrelationsByAppId(applicationDto.getId()).getEntity();
        AppPremisesCorrelationDto appPremisesCorrelationDto = new AppPremisesCorrelationDto();
        if(appPremisesCorrelationDtos != null && !(appPremisesCorrelationDtos.isEmpty())){
            appPremisesCorrelationDto = appPremisesCorrelationDtos.get(0);
        }
        return appPremisesCorrelationDto;
    }
    private void updateInspectionStatus(ApplicationDto applicationDto, String status) {
        AppPremisesCorrelationDto appPremisesCorrelationDto = getAppPreCorrDtoByAppDto(applicationDto);
        AppInspectionStatusDto appInspectionStatusDto = appInspectionStatusClient.getAppInspectionStatusByPremId(appPremisesCorrelationDto.getId()).getEntity();
        appInspectionStatusDto.setStatus(status);
        appInspectionStatusDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        appInspectionStatusClient.update(appInspectionStatusDto);
    }

    private ApplicationDto updateApplication(ApplicationDto applicationDto, String appStatus) {
        applicationDto.setStatus(appStatus);
        applicationDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        return applicationViewService.updateApplicaiton(applicationDto);
    }

    private AppPremisesRoutingHistoryDto createAppPremisesRoutingHistory(String appPremisesCorrelationId, String appStatus, String stageId,
                                                                         String internalRemarks, String processDec, String roleId, String subStage){
        AppPremisesRoutingHistoryDto appPremisesRoutingHistoryDto = new AppPremisesRoutingHistoryDto();
        appPremisesRoutingHistoryDto.setAppPremCorreId(appPremisesCorrelationId);
        appPremisesRoutingHistoryDto.setStageId(stageId);
        appPremisesRoutingHistoryDto.setInternalRemarks(internalRemarks);
        appPremisesRoutingHistoryDto.setAppStatus(appStatus);
        appPremisesRoutingHistoryDto.setActionby(IaisEGPHelper.getCurrentAuditTrailDto().getMohUserGuid());
        appPremisesRoutingHistoryDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        appPremisesRoutingHistoryDto.setProcessDecision(processDec);
        appPremisesRoutingHistoryDto.setRoleId(roleId);
        appPremisesRoutingHistoryDto.setSubStage(subStage);
        appPremisesRoutingHistoryDto = appPremisesRoutingHistoryClient.createAppPremisesRoutingHistory(appPremisesRoutingHistoryDto).getEntity();
        return appPremisesRoutingHistoryDto;
    }

}

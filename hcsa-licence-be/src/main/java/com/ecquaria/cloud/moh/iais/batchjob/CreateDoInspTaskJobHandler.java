package com.ecquaria.cloud.moh.iais.batchjob;

import com.ecquaria.cloud.job.executor.biz.model.ReturnT;
import com.ecquaria.cloud.job.executor.handler.IJobHandler;
import com.ecquaria.cloud.job.executor.handler.annotation.JobHandler;
import com.ecquaria.cloud.job.executor.log.JobLogger;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.constant.HcsaConsts;
import com.ecquaria.cloud.moh.iais.common.constant.inspection.InspectionConstants;
import com.ecquaria.cloud.moh.iais.common.constant.role.RoleConsts;
import com.ecquaria.cloud.moh.iais.common.constant.task.TaskConsts;
import com.ecquaria.cloud.moh.iais.common.dto.AuditTrailDto;
import com.ecquaria.cloud.moh.iais.common.dto.application.ApplicationViewDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesCorrelationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesRecommendationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesRoutingHistoryDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcStageWorkingGroupDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.AppInspectionStatusDto;
import com.ecquaria.cloud.moh.iais.common.dto.task.TaskDto;
import com.ecquaria.cloud.moh.iais.common.utils.Formatter;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.service.TaskService;
import com.ecquaria.cloud.moh.iais.service.client.AppInspectionStatusClient;
import com.ecquaria.cloud.moh.iais.service.client.AppPremisesRoutingHistoryClient;
import com.ecquaria.cloud.moh.iais.service.client.ApplicationClient;
import com.ecquaria.cloud.moh.iais.service.client.FillUpCheckListGetAppClient;
import com.ecquaria.cloud.moh.iais.service.client.InspectionTaskClient;
import com.ecquaria.cloud.moh.iais.service.client.OrganizationClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

/**
 *
 * @author Shicheng
 * @date 2020/4/19 12:08
 **/
@JobHandler(value="createDoInspTaskJobHandler")
@Component
@Slf4j
public class CreateDoInspTaskJobHandler extends IJobHandler {

    @Autowired
    private InspectionTaskClient inspectionTaskClient;

    @Autowired
    private OrganizationClient organizationClient;

    @Autowired
    private TaskService taskService;

    @Autowired
    private ApplicationClient applicationClient;

    @Autowired
    private AppInspectionStatusClient appInspectionStatusClient;

    @Autowired
    private FillUpCheckListGetAppClient fillUpCheckListGetAppClient;

    @Autowired
    private AppPremisesRoutingHistoryClient appPremisesRoutingHistoryClient;


    @Override
    public ReturnT<String> execute(String s) throws Exception {
        try {
            logAbout("Create Checklist By Inspection Date");
            List<AppPremisesRecommendationDto> appPremisesRecommendationDtos = IaisCommonUtils.genNewArrayList();
            List<ApplicationDto> applicationDtoList = applicationClient.getApplicationByStatus(ApplicationConsts.APPLICATION_STATUS_PENDING_INSPECTION).getEntity();
            if(!IaisCommonUtils.isEmpty(applicationDtoList)){
                for(ApplicationDto applicationDto : applicationDtoList){
                    AppPremisesCorrelationDto appPremisesCorrelationDto = applicationClient.getAppPremisesCorrelationDtosByAppId(applicationDto.getId()).getEntity();
                    AppPremisesRecommendationDto appPremisesRecommendationDto = fillUpCheckListGetAppClient.getAppPremRecordByIdAndType(appPremisesCorrelationDto.getId(), InspectionConstants.RECOM_TYPE_INSEPCTION_DATE).getEntity();
                    if(appPremisesRecommendationDto != null) {
                        appPremisesRecommendationDtos.add(appPremisesRecommendationDto);
                    }
                }
            }
            if(IaisCommonUtils.isEmpty(appPremisesRecommendationDtos)){
                return ReturnT.SUCCESS;
            }
            AuditTrailDto intranet = AuditTrailHelper.getBatchJobDto(AppConsts.DOMAIN_INTRANET);
            for(AppPremisesRecommendationDto aRecoDto:appPremisesRecommendationDtos){
                if(aRecoDto.getRecomInDate() != null && aRecoDto.getStatus().equals(AppConsts.COMMON_STATUS_ACTIVE)){
                    Date today = new Date();
                    String inspecDateStr = Formatter.formatDateTime(aRecoDto.getRecomInDate(), Formatter.DATE);
                    String todayStr = Formatter.formatDateTime(today, Formatter.DATE);
                    if(todayStr.equals(inspecDateStr)) {
                        ApplicationDto applicationDto = inspectionTaskClient.getApplicationByCorreId(aRecoDto.getAppPremCorreId()).getEntity();
                        AppInspectionStatusDto appInspectionStatusDto = appInspectionStatusClient.getAppInspectionStatusByPremId(aRecoDto.getAppPremCorreId()).getEntity();
                        if(InspectionConstants.INSPECTION_STATUS_PENDING_INSPECTION.equals(appInspectionStatusDto.getStatus())) {
                            List<ApplicationDto> applicationDtos = IaisCommonUtils.genNewArrayList();
                            applicationDtos.add(applicationDto);
                            List<HcsaSvcStageWorkingGroupDto> hcsaSvcStageWorkingGroupDtos = generateHcsaSvcStageWorkingGroupDtos(applicationDtos, HcsaConsts.ROUTING_STAGE_INS);
                            hcsaSvcStageWorkingGroupDtos = taskService.getTaskConfig(hcsaSvcStageWorkingGroupDtos);
                            log.info(StringUtil.changeForLog("Current Application No. = " + applicationDto.getApplicationNo()));
                            JobLogger.log(StringUtil.changeForLog("Current Application No. = " + applicationDto.getApplicationNo()));
                            List<TaskDto> taskDtos = getTaskByHistoryTasks(applicationDto.getApplicationNo());
                            createTasksByHistory(taskDtos, intranet, hcsaSvcStageWorkingGroupDtos.get(0).getCount(), aRecoDto.getAppPremCorreId());
                            updateInspectionStatus(aRecoDto.getAppPremCorreId(), InspectionConstants.INSPECTION_STATUS_PENDING_CHECKLIST_VERIFY, intranet);
                        }
                    }
                }
            }
            return ReturnT.SUCCESS;
        } catch (Throwable e) {
            log.error(e.getMessage(), e);
            JobLogger.log(e);
            return ReturnT.FAIL;
        }
    }

    private void logAbout(String methodName){
        log.info(StringUtil.changeForLog("****The****" + methodName +" *****Start****"));
        JobLogger.log(StringUtil.changeForLog("****The****" + methodName +" *****Start****"));
    }

    private void updateInspectionStatus(String appPremCorreId, String status, AuditTrailDto intranet) {
        AppInspectionStatusDto appInspectionStatusDto = appInspectionStatusClient.getAppInspectionStatusByPremId(appPremCorreId).getEntity();
        appInspectionStatusDto.setStatus(status);
        appInspectionStatusDto.setAuditTrailDto(intranet);
        appInspectionStatusClient.update(appInspectionStatusDto);
    }

    private List<HcsaSvcStageWorkingGroupDto> generateHcsaSvcStageWorkingGroupDtos(List<ApplicationDto> applicationDtos, String stageId){
        List<HcsaSvcStageWorkingGroupDto> hcsaSvcStageWorkingGroupDtos = IaisCommonUtils.genNewArrayList();
        for(ApplicationDto applicationDto : applicationDtos){
            HcsaSvcStageWorkingGroupDto hcsaSvcStageWorkingGroupDto = new HcsaSvcStageWorkingGroupDto();
            hcsaSvcStageWorkingGroupDto.setStageId(stageId);
            hcsaSvcStageWorkingGroupDto.setServiceId(applicationDto.getServiceId());
            hcsaSvcStageWorkingGroupDto.setType(applicationDto.getApplicationType());
            hcsaSvcStageWorkingGroupDtos.add(hcsaSvcStageWorkingGroupDto);
        }
        return hcsaSvcStageWorkingGroupDtos;
    }

    private void createTasksByHistory(List<TaskDto> taskDtos, AuditTrailDto intranet, Integer score, String appPremCorrId) {
        List<TaskDto> taskDtoList = IaisCommonUtils.genNewArrayList();
        log.info(StringUtil.changeForLog("Current taskDtos Size = " + taskDtos.size()));
        JobLogger.log(StringUtil.changeForLog("Current taskDtos Size = " + taskDtos.size()));
        if(!IaisCommonUtils.isEmpty(taskDtos)) {
            for (TaskDto td : taskDtos) {
                TaskDto taskDto = new TaskDto();
                taskDto.setId(null);
                taskDto.setTaskStatus(TaskConsts.TASK_STATUS_PENDING);
                taskDto.setPriority(td.getPriority());
                taskDto.setRefNo(appPremCorrId);
                taskDto.setSlaAlertInDays(td.getSlaAlertInDays());
                taskDto.setSlaDateCompleted(null);
                taskDto.setSlaInDays(td.getSlaInDays());
                taskDto.setSlaRemainInDays(null);
                taskDto.setTaskKey(td.getTaskKey());
                taskDto.setTaskType(td.getTaskType());
                taskDto.setWkGrpId(td.getWkGrpId());
                taskDto.setUserId(td.getUserId());
                taskDto.setDateAssigned(new Date());
                taskDto.setRoleId(RoleConsts.USER_ROLE_INSPECTIOR);
                taskDto.setAuditTrailDto(intranet);
                taskDto.setApplicationNo(td.getApplicationNo());
                taskDto.setProcessUrl(TaskConsts.TASK_PROCESS_URL_INSPECTION_CHECKLIST_VERIFY);
                taskDto.setScore(score);
                taskDtoList.add(taskDto);
                ApplicationViewDto applicationViewDto = applicationClient.getAppViewByCorrelationId(appPremCorrId).getEntity();
                ApplicationDto applicationDto = applicationViewDto.getApplicationDto();
                createAppPremisesRoutingHistory(intranet, applicationDto.getApplicationNo(), applicationDto.getStatus(), taskDto.getTaskKey(), null, InspectionConstants.PROCESS_DECI_PENDING_INSPECTION, RoleConsts.USER_ROLE_INSPECTIOR, HcsaConsts.ROUTING_STAGE_INP, taskDto.getWkGrpId());
            }
            taskService.createTasks(taskDtoList);
        }
    }

    private List<TaskDto> getTaskByHistoryTasks(String appNo)  {
        List<TaskDto> taskDtos = organizationClient.getTaskByAppNoStatus(appNo, TaskConsts.TASK_STATUS_COMPLETED, TaskConsts.TASK_PROCESS_URL_PRE_INSPECTION).getEntity();
        if(taskDtos == null || taskDtos.isEmpty()){
            return null;
        }
        List<TaskDto> taskDtoList = IaisCommonUtils.genNewArrayList();
        for(TaskDto tDto:taskDtos){
            taskDtoList.add(tDto);
        }
        return taskDtoList;
    }

    public AppPremisesRoutingHistoryDto createAppPremisesRoutingHistory(AuditTrailDto intranet, String appNo, String status, String stageId, String internalRemarks,
                                                                        String processDec, String roleId, String subStage, String workGroupId) {
        AppPremisesRoutingHistoryDto appPremisesRoutingHistoryDto = new AppPremisesRoutingHistoryDto();
        appPremisesRoutingHistoryDto.setApplicationNo(appNo);
        appPremisesRoutingHistoryDto.setStageId(stageId);
        appPremisesRoutingHistoryDto.setInternalRemarks(internalRemarks);
        appPremisesRoutingHistoryDto.setAppStatus(status);
        appPremisesRoutingHistoryDto.setActionby(intranet.getMohUserGuid());
        appPremisesRoutingHistoryDto.setAuditTrailDto(intranet);
        appPremisesRoutingHistoryDto.setProcessDecision(processDec);
        appPremisesRoutingHistoryDto.setRoleId(roleId);
        appPremisesRoutingHistoryDto.setSubStage(subStage);
        appPremisesRoutingHistoryDto.setWrkGrpId(workGroupId);
        appPremisesRoutingHistoryDto = appPremisesRoutingHistoryClient.createAppPremisesRoutingHistory(appPremisesRoutingHistoryDto).getEntity();
        return appPremisesRoutingHistoryDto;
    }
}

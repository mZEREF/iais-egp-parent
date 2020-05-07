package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
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
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.service.TaskService;
import com.ecquaria.cloud.moh.iais.service.client.AppInspectionStatusClient;
import com.ecquaria.cloud.moh.iais.service.client.AppPremisesRoutingHistoryClient;
import com.ecquaria.cloud.moh.iais.service.client.ApplicationClient;
import com.ecquaria.cloud.moh.iais.service.client.FillUpCheckListGetAppClient;
import com.ecquaria.cloud.moh.iais.service.client.InspectionTaskClient;
import com.ecquaria.cloud.moh.iais.service.client.OrganizationClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sop.webflow.rt.api.BaseProcessClass;

import java.util.Date;
import java.util.List;

/**
 * @author Shicheng
 * @date 2019/12/21 15:07
 **/
@Delegator("inspectionCreTaskByInspDateDelegator")
@Slf4j
public class InspectionCreTaskByInspDateDelegator {

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

    @Autowired
    private InspectionCreTaskByInspDateDelegator(TaskService taskService, OrganizationClient organizationClient, InspectionTaskClient inspectionTaskClient){
        this.organizationClient = organizationClient;
        this.inspectionTaskClient = inspectionTaskClient;
        this.taskService = taskService;
    }

    /**
     * StartStep: mohCreTaskByInspecDateStart
     *
     * @param bpc
     * @throws
     */
    public void mohCreTaskByInspecDateStart(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the mohCreTaskByInspecDateStart start ...."));
    }

    /**
     * StartStep: mohCreTaskByInspecDatePre
     *
     * @param bpc
     * @throws
     */
    public void mohCreTaskByInspecDatePre(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the mohCreTaskByInspecDatePre start ...."));
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
            return;
        }
        AuditTrailDto intranet = AuditTrailHelper.getBatchJobDto(AppConsts.DOMAIN_INTRANET);
        for(AppPremisesRecommendationDto aRecoDto:appPremisesRecommendationDtos){
            if(aRecoDto.getRecomInDate() != null && aRecoDto.getStatus().equals(AppConsts.COMMON_STATUS_ACTIVE)){
                ApplicationDto applicationDto = inspectionTaskClient.getApplicationByCorreId(aRecoDto.getAppPremCorreId()).getEntity();
                if(ApplicationConsts.APPLICATION_STATUS_PENDING_INSPECTION.equals(applicationDto.getStatus())) {
                    List<ApplicationDto> applicationDtos = IaisCommonUtils.genNewArrayList();
                    applicationDtos.add(applicationDto);
                    List<HcsaSvcStageWorkingGroupDto> hcsaSvcStageWorkingGroupDtos = generateHcsaSvcStageWorkingGroupDtos(applicationDtos, HcsaConsts.ROUTING_STAGE_INS);
                    hcsaSvcStageWorkingGroupDtos = taskService.getTaskConfig(hcsaSvcStageWorkingGroupDtos);
                    List<TaskDto> taskDtos = getTaskByHistoryTasks(aRecoDto.getAppPremCorreId());
                    createTasksByHistory(taskDtos, intranet, hcsaSvcStageWorkingGroupDtos.get(0).getCount(), aRecoDto.getAppPremCorreId());
                    updateInspectionStatus(aRecoDto.getAppPremCorreId(), InspectionConstants.INSPECTION_STATUS_PENDING_CHECKLIST_VERIFY);
                }
            }
        }
    }

    private void updateInspectionStatus(String appPremCorreId, String status) {
        AppInspectionStatusDto appInspectionStatusDto = appInspectionStatusClient.getAppInspectionStatusByPremId(appPremCorreId).getEntity();
        appInspectionStatusDto.setStatus(status);
        appInspectionStatusDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
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
        for(TaskDto td:taskDtos){
            TaskDto taskDto = new TaskDto();
            taskDto.setId(null);
            taskDto.setTaskStatus(TaskConsts.TASK_STATUS_PENDING);
            taskDto.setPriority(td.getPriority());
            taskDto.setRefNo(td.getRefNo());
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
            taskDto.setProcessUrl(TaskConsts.TASK_PROCESS_URL_INSPECTION_CHECKLIST_VERIFY);
            taskDto.setScore(score);
            taskDtoList.add(taskDto);
            ApplicationViewDto applicationViewDto = applicationClient.getAppViewByCorrelationId(appPremCorrId).getEntity();
            ApplicationDto applicationDto = applicationViewDto.getApplicationDto();
            createAppPremisesRoutingHistory(intranet, applicationDto.getApplicationNo(),applicationDto.getStatus(),taskDto.getTaskKey(),null, InspectionConstants.PROCESS_DECI_ACCEPTS_RECTIFICATION_CONDITION, RoleConsts.USER_ROLE_INSPECTIOR, HcsaConsts.ROUTING_STAGE_INP, taskDto.getWkGrpId());
        }
        taskService.createTasks(taskDtoList);
    }

    private List<TaskDto> getTaskByHistoryTasks(String appCorrId) {
        List<TaskDto> taskDtos = organizationClient.getTaskByAppNo(appCorrId).getEntity();
        if(taskDtos == null || taskDtos.isEmpty()){
            return null;
        }
        List<TaskDto> taskDtoList = IaisCommonUtils.genNewArrayList();
        for(TaskDto tDto:taskDtos){
            if(tDto.getTaskStatus().equals(TaskConsts.TASK_STATUS_COMPLETED) && tDto.getRoleId().equals(RoleConsts.USER_ROLE_INSPECTIOR)){
                taskDtoList.add(tDto);
            }
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

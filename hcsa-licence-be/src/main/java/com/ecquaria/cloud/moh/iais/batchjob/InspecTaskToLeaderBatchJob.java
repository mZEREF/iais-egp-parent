package com.ecquaria.cloud.moh.iais.batchjob;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.HcsaConsts;
import com.ecquaria.cloud.moh.iais.common.constant.inspection.InspectionConstants;
import com.ecquaria.cloud.moh.iais.common.constant.role.RoleConsts;
import com.ecquaria.cloud.moh.iais.common.constant.task.TaskConsts;
import com.ecquaria.cloud.moh.iais.common.dto.AuditTrailDto;
import com.ecquaria.cloud.moh.iais.common.dto.application.ApplicationViewDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcStageWorkingGroupDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.AppInspectionStatusDto;
import com.ecquaria.cloud.moh.iais.common.dto.task.TaskDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.service.TaskService;
import com.ecquaria.cloud.moh.iais.service.client.AppInspectionStatusClient;
import com.ecquaria.cloud.moh.iais.service.client.ApplicationClient;
import com.ecquaria.cloud.moh.iais.service.client.HcsaConfigClient;
import com.ecquaria.cloud.moh.iais.service.client.OrganizationClient;
import java.util.Date;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sop.webflow.rt.api.BaseProcessClass;

/**
 * @Process: MohInspTaskToLeader
 *
 * @author Shicheng
 * @date 2020/2/8 11:22
 **/
@Delegator("inspecTaskToLeaderBatchJob")
@Slf4j
public class InspecTaskToLeaderBatchJob {

    @Autowired
    private AppInspectionStatusClient appInspectionStatusClient;

    @Autowired
    private OrganizationClient organizationClient;

    @Autowired
    private TaskService taskService;

    @Autowired
    private HcsaConfigClient hcsaConfigClient;

    @Autowired
    private ApplicationClient applicationClient;

    /**
     * StartStep: inspTaskToLeaderStart
     *
     * @param bpc
     * @throws
     */
    public void inspTaskToLeaderStart(BaseProcessClass bpc){
        logAbout("Inspection Create Task To Leader");
    }

    /**
     * StartStep: inspTaskToLeaderJob
     *
     * @param bpc
     * @throws
     */
    public void inspTaskToLeaderJob(BaseProcessClass bpc){
        logAbout("inspTaskToLeaderJob");
        List<TaskDto> taskDtoList = organizationClient.getTaskForCompLeader().getEntity();
        if (IaisCommonUtils.isEmpty(taskDtoList)) {
            return;
        }

        Map<String, List<AppInspectionStatusDto>> map = appInspectionStatusClient.getPremisesAndApplicationCorr(taskDtoList).getEntity();
        if(map != null){
            createTaskByMap(map);
        } else {
            return;
        }
    }

    private void createTaskByMap(Map<String, List<AppInspectionStatusDto>> map) {
        for(Map.Entry<String, List<AppInspectionStatusDto>> m : map.entrySet()){
            List<AppInspectionStatusDto> appInspectionStatusDtos = m.getValue();
            if(!IaisCommonUtils.isEmpty(appInspectionStatusDtos)){
                int report = 0;
                int leadTask = 0;
                int allApp = appInspectionStatusDtos.size();
                for(AppInspectionStatusDto appInsStatusDto : appInspectionStatusDtos){
                    if (appInsStatusDto == null) {
                        continue;
                    }
                    if(InspectionConstants.INSPECTION_STATUS_PENDING_JOB_CREATE_TASK_TO_LEADER.equals(appInsStatusDto.getStatus())){
                        leadTask = leadTask + 1;
                    } else if(InspectionConstants.INSPECTION_STATUS_PENDING_PREPARE_REPORT.equals(appInsStatusDto.getStatus()) ||
                              InspectionConstants.INSPECTION_STATUS_PENDING_AO1_RESULT.equals(appInsStatusDto.getStatus()) ||
                              InspectionConstants.INSPECTION_STATUS_PENDING_AO2_RESULT.equals(appInsStatusDto.getStatus())) {
                        report = report + 1;
                    }
                }
                createTask(report, leadTask, allApp, appInspectionStatusDtos);
            }
        }
    }

    private void createTask(int report, int leadTask, int allApp, List<AppInspectionStatusDto> appInspectionStatusDtos) {
        int all = report + leadTask;
        AuditTrailDto intranet = AuditTrailHelper.getBatchJobDto(AppConsts.DOMAIN_INTRANET);
        if(all == allApp){
            AppInspectionStatusDto appInsStatusDto = null;
            for(AppInspectionStatusDto appInspStatusDto : appInspectionStatusDtos){
                if (appInspStatusDto == null) {
                    continue;
                }
                if(InspectionConstants.INSPECTION_STATUS_PENDING_JOB_CREATE_TASK_TO_LEADER.equals(appInspStatusDto.getStatus())){
                    appInsStatusDto = appInspStatusDto;
                    appInspStatusDto.setStatus(InspectionConstants.INSPECTION_STATUS_PENDING_REVIEW_CHECKLIST_EMAIL);
                    appInspStatusDto.setAuditTrailDto(intranet);
                    appInspectionStatusClient.update(appInspStatusDto);
                }
            }
            if(appInsStatusDto != null){
                ApplicationViewDto applicationViewDto = applicationClient.getAppViewByCorrelationId(appInsStatusDto.getAppPremCorreId()).getEntity();
                HcsaSvcStageWorkingGroupDto hcsaSvcStageWorkingGroupDto = new HcsaSvcStageWorkingGroupDto();
                hcsaSvcStageWorkingGroupDto.setServiceId(applicationViewDto.getApplicationDto().getServiceId());
                hcsaSvcStageWorkingGroupDto.setType(applicationViewDto.getApplicationDto().getApplicationType());
                hcsaSvcStageWorkingGroupDto.setStageId(HcsaConsts.ROUTING_STAGE_INS);
                hcsaSvcStageWorkingGroupDto.setOrder(3);
                List<TaskDto> taskDtoList = organizationClient.getTaskByAppNo(appInsStatusDto.getAppPremCorreId()).getEntity();
                if(!IaisCommonUtils.isEmpty(taskDtoList)){
                    TaskDto taskDto = taskDtoList.get(0);
                    TaskDto taskDto1 = new TaskDto();
                    taskDto1.setRefNo(appInsStatusDto.getAppPremCorreId());
                    taskDto1.setTaskType(taskDto.getTaskType());
                    taskDto1.setTaskKey(HcsaConsts.ROUTING_STAGE_INS);
                    taskDto1.setRoleId(RoleConsts.USER_ROLE_INSPECTION_LEAD);
                    taskDto1.setProcessUrl(TaskConsts.TASK_PROCESS_URL_INSPECTION_MERGE_NCEMAIL);
                    taskDto1.setWkGrpId(hcsaConfigClient.getHcsaSvcStageWorkingGroupDto(hcsaSvcStageWorkingGroupDto).getEntity().getGroupId());
                    taskDto1.setUserId(organizationClient.getInspectionLead(taskDto1.getWkGrpId()).getEntity().get(0));
                    List<TaskDto> taskDtos = prepareTaskList(taskDto1,hcsaSvcStageWorkingGroupDto);
                    taskService.createTasks(taskDtos);
                }
            }
        }
    }

    private List<TaskDto> prepareTaskList(TaskDto taskDto, HcsaSvcStageWorkingGroupDto hcsaSvcStageWorkingGroupDto) {
        List<TaskDto> list = IaisCommonUtils.genNewArrayList();
        List<HcsaSvcStageWorkingGroupDto> listhcsaSvcStageWorkingGroupDto = hcsaConfigClient.getSvcWorkGroup(hcsaSvcStageWorkingGroupDto).getEntity();
        Integer count = listhcsaSvcStageWorkingGroupDto.get(0).getCount();
        taskDto.setWkGrpId(hcsaConfigClient.getHcsaSvcStageWorkingGroupDto(hcsaSvcStageWorkingGroupDto).getEntity().getGroupId());
        AuditTrailDto intranet = AuditTrailHelper.getBatchJobDto(AppConsts.DOMAIN_INTRANET);
        taskDto.setId(null);
        taskDto.setDateAssigned(new Date());
        taskDto.setSlaDateCompleted(null);
        taskDto.setSlaRemainInDays(null);
        taskDto.setScore(count);
        taskDto.setSlaAlertInDays(2);
        taskDto.setPriority(0);
        taskDto.setSlaInDays(5);
        taskDto.setTaskStatus(TaskConsts.TASK_STATUS_PENDING);
        taskDto.setAuditTrailDto(intranet);
        list.add(taskDto);
        return list;
    }

    private void logAbout(String methodName){
        log.debug(StringUtil.changeForLog("****The***** " + methodName +" ******Start ****"));
    }
}

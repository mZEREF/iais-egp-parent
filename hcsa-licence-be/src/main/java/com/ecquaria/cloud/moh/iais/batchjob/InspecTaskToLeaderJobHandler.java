package com.ecquaria.cloud.moh.iais.batchjob;

import com.ecquaria.cloud.job.executor.biz.model.ReturnT;
import com.ecquaria.cloud.job.executor.handler.IJobHandler;
import com.ecquaria.cloud.job.executor.handler.annotation.JobHandler;
import com.ecquaria.cloud.job.executor.log.JobLogger;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.HcsaConsts;
import com.ecquaria.cloud.moh.iais.common.constant.inspection.InspectionConstants;
import com.ecquaria.cloud.moh.iais.common.constant.role.RoleConsts;
import com.ecquaria.cloud.moh.iais.common.constant.task.TaskConsts;
import com.ecquaria.cloud.moh.iais.common.dto.AuditTrailDto;
import com.ecquaria.cloud.moh.iais.common.dto.application.ApplicationViewDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
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
import com.ecquaria.cloud.moh.iais.service.client.InspectionTaskClient;
import com.ecquaria.cloud.moh.iais.service.client.OrganizationClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Process: MohInspTaskToLeader
 *
 * @author Shicheng
 * @date 2020/2/8 11:22
 **/
@JobHandler(value="inspecTaskToLeaderJobHandler")
@Component
@Slf4j
public class InspecTaskToLeaderJobHandler extends IJobHandler {

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

    @Autowired
    private InspectionTaskClient inspectionTaskClient;

    @Override
    public ReturnT<String> execute(String s) throws Exception {
        try {
            logAbout("inspTaskToLeaderJob");
            List<AppInspectionStatusDto> appInspectionStatusDtos = appInspectionStatusClient.getAppInspectionStatusByStatus(InspectionConstants.INSPECTION_STATUS_PENDING_JOB_CREATE_TASK_TO_LEADER).getEntity();
            if (IaisCommonUtils.isEmpty(appInspectionStatusDtos)) {
                return ReturnT.SUCCESS;
            } else {
                List<TaskDto> taskDtos = IaisCommonUtils.genNewArrayList();
                for(AppInspectionStatusDto appInspectionStatusDto : appInspectionStatusDtos){
                    TaskDto taskDto = new TaskDto();
                    taskDto.setRefNo(appInspectionStatusDto.getAppPremCorreId());
                    taskDtos.add(taskDto);
                }
                Map<String, List<AppInspectionStatusDto>> map = appInspectionStatusClient.getPremisesAndApplicationCorr(taskDtos).getEntity();
                if(map != null){
                    createTaskByMap(map);
                } else {
                    return ReturnT.SUCCESS;
                }
            }
            return ReturnT.SUCCESS;
        } catch (Throwable e) {
            log.error(e.getMessage(), e);
            JobLogger.log(e);
            return ReturnT.FAIL;
        }
    }

    private void createTaskByMap(Map<String, List<AppInspectionStatusDto>> map) {
        for(Map.Entry<String, List<AppInspectionStatusDto>> m : map.entrySet()){
            log.info(StringUtil.changeForLog("Premises Id = " + m.getKey()));
            JobLogger.log(StringUtil.changeForLog("Premises Id = " + m.getKey()));
            List<AppInspectionStatusDto> appInspectionStatusDtos = m.getValue();
            if(!IaisCommonUtils.isEmpty(appInspectionStatusDtos)){
                int report = 0;
                int leadTask = 0;
                for(int i = 0; i < appInspectionStatusDtos.size(); i++){
                    //in ASO/PSO
                    if (appInspectionStatusDtos.get(i) == null) {
                        continue;
                    }
                    String status = appInspectionStatusDtos.get(i).getStatus();
                    String appPremCorrId = appInspectionStatusDtos.get(i).getAppPremCorreId();
                    //SKIP Inspection
                    if(StringUtil.isEmpty(appPremCorrId) && !StringUtil.isEmpty(status)){
                        if(InspectionConstants.INSPECTION_STATUS_PENDING_PREPARE_REPORT.equals(status) ||
                                InspectionConstants.INSPECTION_STATUS_PENDING_AO1_RESULT.equals(status) ||
                                InspectionConstants.INSPECTION_STATUS_PENDING_AO2_RESULT.equals(status)) {
                            report = report + 1;
                        }
                        continue;
                        //in ASO/PSO or SKIP Inspection
                    } else if(StringUtil.isEmpty(appPremCorrId) && StringUtil.isEmpty(status)) {
                        continue;
                    }
                    ApplicationDto applicationDto = inspectionTaskClient.getApplicationByCorreId(appPremCorrId).getEntity();
                    //Fast Tracking
                    if(applicationDto.isFastTracking()) {
                        List<AppInspectionStatusDto> fastInspectionList = IaisCommonUtils.genNewArrayList();
                        fastInspectionList.add(appInspectionStatusDtos.get(i));
                        int allApp = fastInspectionList.size();
                        createTask(0, 1, allApp, fastInspectionList);
                        appInspectionStatusDtos.remove(i);
                        i--;
                        //Other Application
                    } else {
                        if(InspectionConstants.INSPECTION_STATUS_PENDING_JOB_CREATE_TASK_TO_LEADER.equals(status)){
                            leadTask = leadTask + 1;
                        } else if(InspectionConstants.INSPECTION_STATUS_PENDING_PREPARE_REPORT.equals(status) ||
                                InspectionConstants.INSPECTION_STATUS_PENDING_AO1_RESULT.equals(status) ||
                                InspectionConstants.INSPECTION_STATUS_PENDING_AO2_RESULT.equals(status)) {
                            report = report + 1;
                        }
                    }
                }
                int allApp = appInspectionStatusDtos.size();
                log.info(StringUtil.changeForLog("report = " + report));
                JobLogger.log(StringUtil.changeForLog("report = " + report));
                log.info(StringUtil.changeForLog("leadTask = " + leadTask));
                JobLogger.log(StringUtil.changeForLog("leadTask = " + leadTask));
                log.info(StringUtil.changeForLog("allApp = " + allApp));
                JobLogger.log(StringUtil.changeForLog("allApp = " + allApp));
                createTask(report, leadTask, allApp, appInspectionStatusDtos);
            }
        }
    }

    private void createTask(int report, int leadTask, int allApp, List<AppInspectionStatusDto> appInspectionStatusDtos) {
        int all = report + leadTask;
        AuditTrailDto intranet = AuditTrailHelper.getBatchJobDto(AppConsts.DOMAIN_INTRANET);
        if(all == allApp){
            for(AppInspectionStatusDto appInspStatusDto : appInspectionStatusDtos){
                if (appInspStatusDto == null || StringUtil.isEmpty(appInspStatusDto.getAppPremCorreId())) {
                    continue;
                }
                if(InspectionConstants.INSPECTION_STATUS_PENDING_JOB_CREATE_TASK_TO_LEADER.equals(appInspStatusDto.getStatus())){
                    ApplicationViewDto applicationViewDto = applicationClient.getAppViewByCorrelationId(appInspStatusDto.getAppPremCorreId()).getEntity();
                    ApplicationDto applicationDto = applicationViewDto.getApplicationDto();
                    HcsaSvcStageWorkingGroupDto hcsaSvcStageWorkingGroupDto = new HcsaSvcStageWorkingGroupDto();
                    hcsaSvcStageWorkingGroupDto.setServiceId(applicationDto.getServiceId());
                    hcsaSvcStageWorkingGroupDto.setType(applicationDto.getApplicationType());
                    hcsaSvcStageWorkingGroupDto.setStageId(HcsaConsts.ROUTING_STAGE_INS);
                    hcsaSvcStageWorkingGroupDto.setOrder(3);
                    String workGroupId = hcsaConfigClient.getHcsaSvcStageWorkingGroupDto(hcsaSvcStageWorkingGroupDto).getEntity().getGroupId();
                    //get task type
                    List<TaskDto> taskDtoList = organizationClient.getTaskByAppNoStatus(
                            applicationDto.getApplicationNo(), TaskConsts.TASK_STATUS_COMPLETED, TaskConsts.TASK_PROCESS_URL_PRE_INSPECTION).getEntity();
                    List<TaskDto> createTasks = IaisCommonUtils.genNewArrayList();
                    if(!IaisCommonUtils.isEmpty(taskDtoList)){
                        List<String> leads = organizationClient.getInspectionLead(workGroupId).getEntity();
                        if(!IaisCommonUtils.isEmpty(leads)) {
                            TaskDto taskDto = taskDtoList.get(0);
                            for(String lead : leads) {
                                TaskDto taskDto1 = new TaskDto();
                                taskDto1.setRefNo(appInspStatusDto.getAppPremCorreId());
                                taskDto1.setTaskType(taskDto.getTaskType());
                                taskDto1.setTaskKey(HcsaConsts.ROUTING_STAGE_INS);
                                taskDto1.setRoleId(RoleConsts.USER_ROLE_INSPECTION_LEAD);
                                taskDto1.setProcessUrl(TaskConsts.TASK_PROCESS_URL_INSPECTION_MERGE_NCEMAIL);
                                taskDto1.setWkGrpId(workGroupId);
                                taskDto1.setUserId(lead);
                                taskDto1.setApplicationNo(taskDto.getApplicationNo());
                                createTasks = prepareTaskList(taskDto1, hcsaSvcStageWorkingGroupDto, intranet, createTasks);
                            }
                        }
                        if(!IaisCommonUtils.isEmpty(createTasks)) {
                            taskService.createTasks(createTasks);
                        }
                    }
                    appInspStatusDto.setStatus(InspectionConstants.INSPECTION_STATUS_PENDING_REVIEW_CHECKLIST_EMAIL);
                    appInspStatusDto.setAuditTrailDto(intranet);
                    appInspectionStatusClient.update(appInspStatusDto);
                }
            }
        }
    }

    private List<TaskDto> prepareTaskList(TaskDto taskDto, HcsaSvcStageWorkingGroupDto hcsaSvcStageWorkingGroupDto,
                                          AuditTrailDto intranet, List<TaskDto> createTasks) {
        List<HcsaSvcStageWorkingGroupDto> listHcsaSvcStageWorkingGroupDto = hcsaConfigClient.getSvcWorkGroup(hcsaSvcStageWorkingGroupDto).getEntity();
        Integer count = listHcsaSvcStageWorkingGroupDto.get(0).getCount();
        taskDto.setWkGrpId(hcsaConfigClient.getHcsaSvcStageWorkingGroupDto(hcsaSvcStageWorkingGroupDto).getEntity().getGroupId());
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
        createTasks.add(taskDto);
        return createTasks;
    }

    private void logAbout(String methodName){
        log.info(StringUtil.changeForLog("****The****" + methodName +" *****Start****"));
        JobLogger.log(StringUtil.changeForLog("****The****" + methodName +" *****Start****"));
    }
}

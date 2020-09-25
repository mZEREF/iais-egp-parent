package com.ecquaria.cloud.moh.iais.batchjob;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.job.executor.biz.model.ReturnT;
import com.ecquaria.cloud.job.executor.log.JobLogger;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.constant.HcsaConsts;
import com.ecquaria.cloud.moh.iais.common.constant.role.RoleConsts;
import com.ecquaria.cloud.moh.iais.common.constant.task.TaskConsts;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.appeal.AppPremiseMiscDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesRoutingHistoryDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.task.TaskDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.dto.TaskHistoryDto;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.service.AppPremisesRoutingHistoryService;
import com.ecquaria.cloud.moh.iais.service.ApplicationService;
import com.ecquaria.cloud.moh.iais.service.TaskService;
import com.ecquaria.cloud.moh.iais.service.client.ApplicationClient;
import com.ecquaria.cloud.moh.iais.service.client.CessationClient;
import com.ecquaria.cloud.moh.iais.service.client.OrganizationClient;
import com.ecquaria.cloudfeign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sop.webflow.rt.api.BaseProcessClass;

import java.util.List;

@Delegator("approvewithdrawalDelegator")
@Slf4j
public class ApproveWdAppBatchJob {

    @Autowired
    private ApplicationClient applicationClient;

    @Autowired
    private TaskService taskService;

    @Autowired
    private AppPremisesRoutingHistoryService appPremisesRoutingHistoryService;

    @Autowired
    CessationClient cessationClient;

    @Autowired
    private ApplicationService applicationService;

    @Autowired
    private OrganizationClient organizationClient;


    public void startStep(BaseProcessClass bpc) {

    }

    public void approveWithdrawalStep(BaseProcessClass bpc) {
        try {
            List<ApplicationDto> applicationDtoList = applicationClient.saveWithdrawn().getEntity();
            log.error("**** The withdraw Application List size : "+applicationDtoList.size());
            //get old application
            if (applicationDtoList != null){
                applicationDtoList.forEach(h -> {
                    applicationService.updateFEApplicaiton(h);
                });
                log.error(StringUtil.changeForLog("**** The withdraw Application List size"+applicationDtoList.size()));
                List<String> oldAppGroupExcuted = IaisCommonUtils.genNewArrayList();
                if(!IaisCommonUtils.isEmpty(applicationDtoList)){
                    for(ApplicationDto oldApplicationDto : applicationDtoList){
                        doWithdrawal(oldApplicationDto,oldAppGroupExcuted);
                    }
                }
                JobLogger.log(StringUtil.changeForLog("The withdraw Application List" + applicationDtoList.size()));
            }else{
                JobLogger.log(StringUtil.changeForLog("The withdraw Application List is null *****"));
            }
        }catch (Exception e){
            log.error(e.getMessage(), e);
            JobLogger.log(e);
        }
    }
    private void doWithdrawal(ApplicationDto oldApplicationDto, List<String> oldAppGroupExcuted) throws FeignException {
        log.info(StringUtil.changeForLog("withdrawal old application id : " + oldApplicationDto.getId()));
        if(oldApplicationDto != null){
            String oldAppGrpId = oldApplicationDto.getAppGrpId();
            String currentOldApplicationNo = oldApplicationDto.getApplicationNo();
            List<ApplicationDto> applicationDtoList = applicationService.getApplicaitonsByAppGroupId(oldAppGrpId);
            if(IaisCommonUtils.isEmpty(applicationDtoList) || applicationDtoList.size() == 1){
                return;
            }else{
                if (!oldAppGroupExcuted.contains(oldAppGrpId)) {
                    List<ApplicationDto> ao1AppList = getStatusAppList(applicationDtoList, ApplicationConsts.APPLICATION_STATUS_PENDING_APPROVAL01, RoleConsts.USER_ROLE_AO1,currentOldApplicationNo);
                    List<ApplicationDto> ao2AppList = getStatusAppList(applicationDtoList, ApplicationConsts.APPLICATION_STATUS_PENDING_APPROVAL02,RoleConsts.USER_ROLE_AO2,currentOldApplicationNo);
                    List<ApplicationDto> ao3AppList = getStatusAppList(applicationDtoList, ApplicationConsts.APPLICATION_STATUS_PENDING_APPROVAL03,RoleConsts.USER_ROLE_AO3,currentOldApplicationNo);
                    if(!IaisCommonUtils.isEmpty(ao1AppList)){
                        return;
                    }else{
                        //ao1 == null
                        if(!IaisCommonUtils.isEmpty(ao2AppList) && ao2AppList.size()>0){
                            //create task
                            createTaskAndHistory(ao2AppList, HcsaConsts.ROUTING_STAGE_AO2,RoleConsts.USER_ROLE_AO2,oldAppGroupExcuted,oldAppGrpId);
                        }else{
                            if(!IaisCommonUtils.isEmpty(ao3AppList) && ao3AppList.size()>0) {
                                //create task
                                createTaskAndHistory(ao3AppList,HcsaConsts.ROUTING_STAGE_AO3,RoleConsts.USER_ROLE_AO3,oldAppGroupExcuted,oldAppGrpId);
                            }else{
                                return;
                            }
                        }
                    }
                }
            }
        }
    }

    private void createTaskAndHistory( List<ApplicationDto> creatTaskApplicationList, String stageId, String roleId, List<String> oldAppGroupExcuted, String oldAppGrpId) throws FeignException {
        TaskHistoryDto taskHistoryDto = taskService.getRoutingTaskOneUserForSubmisison(creatTaskApplicationList,
                stageId, roleId, IaisEGPHelper.getCurrentAuditTrailDto());
        if(taskHistoryDto != null){
            List<TaskDto> taskDtos = taskHistoryDto.getTaskDtoList();
            List<AppPremisesRoutingHistoryDto> appPremisesRoutingHistoryDtos = taskHistoryDto.getAppPremisesRoutingHistoryDtos();
            taskService.createTasks(taskDtos);
            appPremisesRoutingHistoryService.createHistorys(appPremisesRoutingHistoryDtos);
            oldAppGroupExcuted.add(oldAppGrpId);
        }
    }

    private List<ApplicationDto> getOldApplicationDtoList(List<ApplicationDto> withdrawalApplicationDtoList){
        if(IaisCommonUtils.isEmpty(withdrawalApplicationDtoList)){
            return null;
        }
        List<ApplicationDto> oldApplicationList = IaisCommonUtils.genNewArrayList();
        for(ApplicationDto withdrawalApplicationDto : withdrawalApplicationDtoList){
            AppPremiseMiscDto premiseMiscDto = cessationClient.getAppPremiseMiscDtoByAppId(withdrawalApplicationDto.getId()).getEntity();
            if(premiseMiscDto != null) {
                String oldAppId = premiseMiscDto.getRelateRecId();
                log.info(StringUtil.changeForLog("withdrawal old application id : " + oldAppId));
                ApplicationDto oldApplication = applicationClient.getApplicationById(oldAppId).getEntity();
                if(oldApplication != null){
                    oldApplicationList.add(oldApplication);
                }
            }
        }
        return oldApplicationList;
    }

    private List<ApplicationDto> getStatusAppList(List<ApplicationDto> applicationDtos, String status, String roleId, String currentAppNo){
        if(IaisCommonUtils.isEmpty(applicationDtos) || StringUtil.isEmpty(status) || StringUtil.isEmpty(roleId) || StringUtil.isEmpty(currentAppNo)){
            return null;
        }
        List<ApplicationDto> applicationDtoList = null;
        for(ApplicationDto applicationDto : applicationDtos){
            if(currentAppNo.equals(applicationDto.getApplicationNo())){
                continue;
            }
            //have uncompleted task
            boolean hasNoTask = false;
            List<TaskDto> pendingTaskDtos = organizationClient.getTaskByApplicationNoAndRoleIdAndStatus(applicationDto.getApplicationNo(), roleId, TaskConsts.TASK_STATUS_PENDING).getEntity();
            List<TaskDto> readTaskDtos = organizationClient.getTaskByApplicationNoAndRoleIdAndStatus(applicationDto.getApplicationNo(), roleId, TaskConsts.TASK_STATUS_READ).getEntity();
            if(IaisCommonUtils.isEmpty(pendingTaskDtos) && IaisCommonUtils.isEmpty(readTaskDtos)){
                hasNoTask = true;
            }
            if(hasNoTask){
                if(status.equals(applicationDto.getStatus())) {
                    if (applicationDtoList == null) {
                        applicationDtoList = IaisCommonUtils.genNewArrayList();
                        applicationDtoList.add(applicationDto);
                    } else {
                        applicationDtoList.add(applicationDto);
                    }
                }
            }
        }
        return applicationDtoList;
    }


}

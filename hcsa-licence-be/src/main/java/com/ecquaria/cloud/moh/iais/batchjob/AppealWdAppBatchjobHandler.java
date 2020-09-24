package com.ecquaria.cloud.moh.iais.batchjob;


import com.ecquaria.cloud.job.executor.biz.model.ReturnT;
import com.ecquaria.cloud.job.executor.handler.IJobHandler;
import com.ecquaria.cloud.job.executor.handler.annotation.JobHandler;
import com.ecquaria.cloud.job.executor.log.JobLogger;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.constant.HcsaConsts;
import com.ecquaria.cloud.moh.iais.common.constant.role.RoleConsts;
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
import com.ecquaria.cloudfeign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@JobHandler(value="approveWithdrawalJobHandler")
@Component
@Slf4j
public class AppealWdAppBatchjobHandler extends IJobHandler {

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

    @Override
    public ReturnT<String> execute(String s) {
        try {
            List<ApplicationDto> applicationDtoList = applicationClient.saveWithdrawn().getEntity();
            if (applicationDtoList != null){
                applicationDtoList.forEach(h -> {
                    applicationService.updateFEApplicaiton(h);
                });
                log.error(StringUtil.changeForLog("**** The withdraw Application List size"+applicationDtoList.size()));
                List<String> oldAppGroupExcuted = IaisCommonUtils.genNewArrayList();
                for(ApplicationDto applicationDto : applicationDtoList){
                    doWithdrawal(applicationDto.getId(),oldAppGroupExcuted);
                }
                JobLogger.log(StringUtil.changeForLog("The withdraw Application List" + applicationDtoList.size()));
            }else{
                JobLogger.log(StringUtil.changeForLog("The withdraw Application List is null *****"));
            }
        }catch (Exception e){
            log.error(e.getMessage(), e);
            JobLogger.log(e);
            return ReturnT.FAIL;
        }
        return ReturnT.SUCCESS;
    }

    private void doWithdrawal(String appId, List<String> oldAppGroupExcuted) throws FeignException {
        AppPremiseMiscDto premiseMiscDto = cessationClient.getAppPremiseMiscDtoByAppId(appId).getEntity();
        if(premiseMiscDto != null){
            String oldAppId = premiseMiscDto.getRelateRecId();
            log.info(StringUtil.changeForLog("withdrawal old application id : " + oldAppId));
            ApplicationDto oldApplication = applicationClient.getApplicationById(oldAppId).getEntity();
            if(oldApplication != null){
                String oldAppGrpId = oldApplication.getAppGrpId();
                String currentOldApplicationNo = oldApplication.getApplicationNo();
                String currentOldApplicationStatus = oldApplication.getStatus();
                List<ApplicationDto> applicationDtoList = applicationService.getApplicaitonsByAppGroupId(oldAppGrpId);
                if(IaisCommonUtils.isEmpty(applicationDtoList) || applicationDtoList.size() == 1){
                    return;
                }else{
                    boolean isAllSubmitAO3 = applicationService.isOtherApplicaitonSubmit(applicationDtoList,currentOldApplicationNo,
                            ApplicationConsts.APPLICATION_STATUS_PENDING_APPROVAL03);
                    if(!(ApplicationConsts.APPLICATION_STATUS_PENDING_APPROVAL02.equals(currentOldApplicationStatus) || ApplicationConsts.APPLICATION_STATUS_PENDING_APPROVAL03.equals(currentOldApplicationStatus))
                            || (isAllSubmitAO3 && (ApplicationConsts.APPLICATION_STATUS_PENDING_APPROVAL02.equals(currentOldApplicationStatus)))) {
                        boolean isAllSubmit = applicationService.isOtherApplicaitonSubmit(applicationDtoList, currentOldApplicationNo,
                                ApplicationConsts.APPLICATION_STATUS_PENDING_APPROVAL03, ApplicationConsts.APPLICATION_STATUS_PENDING_APPROVAL02);
                        if (isAllSubmit && !oldAppGroupExcuted.contains(oldAppGrpId)) {
                            String stageId = HcsaConsts.ROUTING_STAGE_AO3;
                            String roleId = RoleConsts.USER_ROLE_AO3;
                            List<ApplicationDto> ao2AppList = getStatusAppList(applicationDtoList, ApplicationConsts.APPLICATION_STATUS_PENDING_APPROVAL02);
                            List<ApplicationDto> ao3AppList = getStatusAppList(applicationDtoList, ApplicationConsts.APPLICATION_STATUS_PENDING_APPROVAL03);
                            List<ApplicationDto> creatTaskApplicationList = ao2AppList;
                            if (IaisCommonUtils.isEmpty(ao2AppList) && !IaisCommonUtils.isEmpty(ao3AppList)) {
                                creatTaskApplicationList = ao3AppList;
                            } else {
                                stageId = HcsaConsts.ROUTING_STAGE_AO2;
                                roleId = RoleConsts.USER_ROLE_AO2;
                            }
                            updateCurrentApplicationStatus(applicationDtoList, oldAppId, ApplicationConsts.APPLICATION_STATUS_WITHDRAWN);
                            // send the task to Ao2  or Ao3
                            TaskHistoryDto taskHistoryDto = taskService.getRoutingTaskOneUserForSubmisison(creatTaskApplicationList,
                                    stageId, roleId, IaisEGPHelper.getCurrentAuditTrailDto());
                            List<TaskDto> taskDtos = taskHistoryDto.getTaskDtoList();
                            List<AppPremisesRoutingHistoryDto> appPremisesRoutingHistoryDtos = taskHistoryDto.getAppPremisesRoutingHistoryDtos();
                            taskService.createTasks(taskDtos);
                            appPremisesRoutingHistoryService.createHistorys(appPremisesRoutingHistoryDtos);
                            oldAppGroupExcuted.add(oldAppGrpId);
                        }
                    }
                }
            }
        }
    }

    private void updateCurrentApplicationStatus(List<ApplicationDto> applicationDtos,String applicationId,String status){
        if(!IaisCommonUtils.isEmpty(applicationDtos) && !StringUtil.isEmpty(applicationId)){
            for (ApplicationDto applicationDto : applicationDtos){
                if(applicationId.equals(applicationDto.getId())){
                    applicationDto.setStatus(status);
                }
            }
        }
    }

    private List<ApplicationDto> getStatusAppList(List<ApplicationDto> applicationDtos, String status){
        if(IaisCommonUtils.isEmpty(applicationDtos) || StringUtil.isEmpty(status)){
            return null;
        }
        List<ApplicationDto> applicationDtoList = null;
        for(ApplicationDto applicationDto : applicationDtos){
            if(status.equals(applicationDto.getStatus())){
                if(applicationDtoList == null){
                    applicationDtoList = IaisCommonUtils.genNewArrayList();
                    applicationDtoList.add(applicationDto);
                }else{
                    applicationDtoList.add(applicationDto);
                }
            }
        }

        return applicationDtoList;
    }
}

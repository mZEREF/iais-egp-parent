package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.common.constant.HcsaConsts;
import com.ecquaria.cloud.moh.iais.common.constant.rest.RestApiUrlConsts;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcStageWorkingGroupDto;
import com.ecquaria.cloud.moh.iais.common.dto.task.TaskDto;
import com.ecquaria.cloud.moh.iais.common.dto.task.TaskScoreDto;
import com.ecquaria.cloud.moh.iais.common.utils.RestApiUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.common.utils.TaskUtil;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.service.TaskScoreService;
import com.ecquaria.cloud.moh.iais.service.TaskService;
import com.ecquaria.cloud.moh.iais.service.WorkGroupService;
import com.ecquaria.cloudfeign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sop.rbac.user.User;
import sop.usergroup.UserGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * TaskServiceImpl
 *
 * @author suocheng
 * @date 11/20/2019
 */
@Service
@Slf4j
public class TaskServiceImpl implements TaskService {
    @Autowired
    private TaskScoreService taskScoreService;

    @Override
    public List<TaskDto> createTasks(List<TaskDto> taskDtos) {
        return RestApiUtil.save( RestApiUrlConsts.IAIS_TASK,taskDtos,List.class);
    }

    @Override
    public List<HcsaSvcStageWorkingGroupDto> getTaskConfig(List<HcsaSvcStageWorkingGroupDto> hcsaSvcStageWorkingGroupDtos) {
        return RestApiUtil.postGetList(RestApiUrlConsts.GET_HCSA_WORK_GROUP,hcsaSvcStageWorkingGroupDtos,HcsaSvcStageWorkingGroupDto.class);
    }
    @Override
    public void routingTask(ApplicationDto applicationDto, String stageId) {
      if(applicationDto != null && !StringUtil.isEmpty(stageId) ){


      }else{
          log.error(StringUtil.changeForLog("The applicationDto or stageId is null ... "));
      }
    }

    @Override
    public void routingAdminScranTask(List<ApplicationDto> applicationDtos) throws FeignException {
        log.debug(StringUtil.changeForLog("the do routingAdminScranTask start ...."));
        if(applicationDtos != null && applicationDtos.size() > 0){
            List<HcsaSvcStageWorkingGroupDto> hcsaSvcStageWorkingGroupDtos = new ArrayList();
            for(ApplicationDto applicationDto : applicationDtos){
                HcsaSvcStageWorkingGroupDto hcsaSvcStageWorkingGroupDto = new HcsaSvcStageWorkingGroupDto();
                hcsaSvcStageWorkingGroupDto.setStageId(HcsaConsts.ROUTING_STAGE_ASO);
                hcsaSvcStageWorkingGroupDto.setServiceId(applicationDto.getServiceId());
                hcsaSvcStageWorkingGroupDto.setType(applicationDto.getApplicationType());
                hcsaSvcStageWorkingGroupDtos.add(hcsaSvcStageWorkingGroupDto);
            }
            hcsaSvcStageWorkingGroupDtos = this.getTaskConfig(hcsaSvcStageWorkingGroupDtos);
            if(hcsaSvcStageWorkingGroupDtos!= null && hcsaSvcStageWorkingGroupDtos.size() > 0){
                String workGroupName = hcsaSvcStageWorkingGroupDtos.get(0).getGroupShortName();
                TaskScoreDto taskScoreDto = getUserIdForWorkGroup(workGroupName);
                List<TaskDto> taskDtos = new ArrayList<>();
                for(ApplicationDto applicationDto : applicationDtos){
                    TaskDto taskDto = TaskUtil.getAsoTaskDto(
                            applicationDto.getApplicationNo(),workGroupName,
                            taskScoreDto.getUserId(),taskScoreDto.getUserDomain(),
                            IaisEGPHelper.getCurrentAuditTrailDto());
                    taskDtos.add(taskDto);
                    int score = taskScoreDto.getScore();
                    score = score + getConfigScoreForService(hcsaSvcStageWorkingGroupDtos,applicationDto.getServiceId(),
                            HcsaConsts.ROUTING_STAGE_ASO,applicationDto.getApplicationType());
                    taskScoreDto.setScore(score);
                    this.createTasks(taskDtos);
                    taskScoreService.createTaskScore(taskScoreDto);
                }
            }else{
                log.error(StringUtil.changeForLog("can not get the HcsaSvcStageWorkingGroupDto ..."));
            }
        }else{
            log.error(StringUtil.changeForLog("The applicationDtos is null"));
        }

        log.debug(StringUtil.changeForLog("the do routingAdminScranTask end ...."));
    }



    private int getConfigScoreForService(List<HcsaSvcStageWorkingGroupDto> hcsaSvcStageWorkingGroupDtos,String serviceId,
                                         String stageId,String appType){
        int result = 0;
        if(StringUtil.isEmpty(serviceId) || StringUtil.isEmpty(stageId) || StringUtil.isEmpty(appType)){
          return result;
        }
        for (HcsaSvcStageWorkingGroupDto hcsaSvcStageWorkingGroupDto :hcsaSvcStageWorkingGroupDtos){
            if(serviceId.equals(hcsaSvcStageWorkingGroupDto.getServiceId())
                    && stageId.equals(hcsaSvcStageWorkingGroupDto.getStageId())
                    && appType.equals(hcsaSvcStageWorkingGroupDto.getType())){
                result = hcsaSvcStageWorkingGroupDto.getCount();
            }
        }
        return result;
    }

    private TaskScoreDto getUserIdForWorkGroup(String workGroupName) throws FeignException {
        log.debug(StringUtil.changeForLog("the do getUserIdForWorkGroup start ...."));
        TaskScoreDto result = null;
        if(StringUtil.isEmpty(workGroupName)){
            return result;
        }
        List<UserGroup> userGroups = WorkGroupService.getInstance().retrieveAgencyWorkingGroup(workGroupName);
        if(userGroups!=null && userGroups.size()>0){
            List<User> users = WorkGroupService.getInstance().getUsersByGroupNo(userGroups.get(0).getGroupNo());
            List<TaskScoreDto> taskScoreDtos = taskScoreService.getTaskScores(workGroupName);
            result = taskScoreService.getLowestTaskScore(taskScoreDtos,users);
        }
        if(result != null && StringUtil.isEmpty(result.getGroupShortName())){
            result.setGroupShortName(workGroupName);
        }
        log.debug(StringUtil.changeForLog("the do getUserIdForWorkGroup end ...."));
        return result;
    }

}

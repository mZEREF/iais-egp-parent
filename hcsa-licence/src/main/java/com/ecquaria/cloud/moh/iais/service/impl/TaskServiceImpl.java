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
        return RestApiUtil.save(RestApiUrlConsts.SYSTEM_ADMIN_SERVICE + RestApiUrlConsts.IAIS_TASK,taskDtos,List.class);
    }

    @Override
    public HcsaSvcStageWorkingGroupDto getTaskConfig(HcsaSvcStageWorkingGroupDto hcsaSvcStageWorkingGroupDto) {
        return RestApiUtil.postGetObject(RestApiUrlConsts.GET_HCSA_WORK_GROUP,hcsaSvcStageWorkingGroupDto,HcsaSvcStageWorkingGroupDto.class);
    }

    @Override
    public void routingAdminScranTask(List<ApplicationDto> applicationDtos) throws FeignException {
        log.debug(StringUtil.changeForLog("the do routingTask start ...."));
        List<TaskDto> taskDtos = new ArrayList<>();
        HcsaSvcStageWorkingGroupDto hcsaSvcStageWorkingGroupDto = new HcsaSvcStageWorkingGroupDto();
        hcsaSvcStageWorkingGroupDto.setStageId(HcsaConsts.ROUTING_STAGE_ASO);
        hcsaSvcStageWorkingGroupDto = this.getTaskConfig(hcsaSvcStageWorkingGroupDto);
        TaskScoreDto userIdentifier = getUserIdForWorkGroup(hcsaSvcStageWorkingGroupDto.getGroupShortName());
        if(applicationDtos != null && applicationDtos.size() > 0){
            for(ApplicationDto applicationDto : applicationDtos){
                TaskDto taskDto = TaskUtil.getAsoTaskDto(applicationDto.getServiceId(),
                        applicationDto.getApplicationNo(),hcsaSvcStageWorkingGroupDto.getGroupShortName(),
                        userIdentifier.getId(),userIdentifier.getUserDomain(),
                        IaisEGPHelper.getCurrentAuditTrailDto() );
                taskDtos.add(taskDto);
            }
        }else{
            log.error(StringUtil.changeForLog("The applicationDtos is null"));
        }
        this.createTasks(taskDtos);
        log.debug(StringUtil.changeForLog("the do routingTask end ...."));
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
            result = new TaskScoreDto();
            result.setId(users.get(0).getUserIdentifier().getId());
            result.setUserDomain(users.get(0).getUserIdentifier().getUserDomain());
            List<TaskScoreDto> taskScoreDtos = taskScoreService.getTaskScores(workGroupName);

        }
        log.debug(StringUtil.changeForLog("the do getUserIdForWorkGroup end ...."));
        return result;
    }

}

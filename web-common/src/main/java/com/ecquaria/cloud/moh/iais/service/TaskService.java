package com.ecquaria.cloud.moh.iais.service;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesCorrelationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesRoutingHistoryDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcStageWorkingGroupDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.OrgUserDto;
import com.ecquaria.cloud.moh.iais.common.dto.task.TaskDto;
import com.ecquaria.cloudfeign.FeignException;
import java.util.List;

/**
 * TaskService
 *
 * @author suocheng
 * @date 11/20/2019
 */
public interface TaskService {
    //create Tasks
    List<TaskDto> createTasks(List<TaskDto> taskDtos);
    TaskDto updateTask(TaskDto taskDto);
    //get TaskConfig
    List<HcsaSvcStageWorkingGroupDto> getTaskConfig(List<HcsaSvcStageWorkingGroupDto> hcsaSvcStageWorkingGroupDtos);
    //get Task
    TaskDto getTaskById(String taskId);
    //rounting Task
     void routingTaskOneUserForSubmisison(List<ApplicationDto> applicationDtos, String stage) throws FeignException;
     void routingTask(ApplicationDto applicationDto, String statgId) throws FeignException;
     //get users
    List<OrgUserDto> getUsersByWorkGroupId(String workGroupId, String status);
    //get Task Score
    List<TaskDto> getTaskDtoScoresByWorkGroupId(String workGroupId);
    TaskDto getLowestTaskScore(List<TaskDto> taskScoreDtos, List<OrgUserDto> users);

    //other method help task.
    public List<AppPremisesCorrelationDto> getAppPremisesCorrelationByAppGroupId(String appGroupId);
    public List<AppPremisesRoutingHistoryDto> createHistorys(List<AppPremisesRoutingHistoryDto>  appPremisesRoutingHistoryDtoList);
}

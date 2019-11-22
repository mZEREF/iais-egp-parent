package com.ecquaria.cloud.moh.iais.service;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcStageWorkingGroupDto;
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
    List<TaskDto> createTasks(List<TaskDto> taskDtos);
    List<HcsaSvcStageWorkingGroupDto> getTaskConfig(List<HcsaSvcStageWorkingGroupDto> hcsaSvcStageWorkingGroupDtos);
    public void routingAdminScranTask(List<ApplicationDto> applicationDtos) throws FeignException;
}

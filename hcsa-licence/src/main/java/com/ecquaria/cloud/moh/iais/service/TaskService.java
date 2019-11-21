package com.ecquaria.cloud.moh.iais.service;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcStageWorkingGroupDto;
import com.ecquaria.cloud.moh.iais.common.dto.task.TaskDto;

import java.util.List;

/**
 * TaskService
 *
 * @author suocheng
 * @date 11/20/2019
 */
public interface TaskService {
    List<TaskDto> createTasks(List<TaskDto> taskDtos);
    HcsaSvcStageWorkingGroupDto getTaskConfig(HcsaSvcStageWorkingGroupDto hcsaSvcStageWorkingGroupDto);
}

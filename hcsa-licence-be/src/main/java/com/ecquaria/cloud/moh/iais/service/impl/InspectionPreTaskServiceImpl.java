package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.task.TaskDto;
import com.ecquaria.cloud.moh.iais.service.InspectionPreTaskService;
import com.ecquaria.cloud.moh.iais.service.TaskService;
import com.ecquaria.cloud.moh.iais.service.client.ApplicationClient;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @author Shicheng
 * @date 2019/12/9 9:58
 **/
public class InspectionPreTaskServiceImpl implements InspectionPreTaskService {

    @Autowired
    private TaskService taskService;

    @Autowired
    private ApplicationClient applicationClient;

    @Override
    public String getAppStatusByTaskId(String taskId) {
        TaskDto taskDto = getTaskDtoByTaskId(taskId);
        ApplicationDto applicationDto = applicationClient.getAppByNo(taskDto.getRefNo()).getEntity();
        return applicationDto.getStatus();
    }

    @Override
    public List<SelectOption> getProcessDecOption() {
        String[] processDecArr = new String[]{};
        return null;
    }

    private TaskDto getTaskDtoByTaskId(String taskId) {
        return taskService.getTaskById(taskId);
    }
}

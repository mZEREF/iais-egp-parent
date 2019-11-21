package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.common.constant.rest.RestApiUrlConsts;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcStageWorkingGroupDto;
import com.ecquaria.cloud.moh.iais.common.dto.task.TaskDto;
import com.ecquaria.cloud.moh.iais.common.utils.RestApiUtil;
import com.ecquaria.cloud.moh.iais.service.TaskService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * TaskServiceImpl
 *
 * @author suocheng
 * @date 11/20/2019
 */
@Service
public class TaskServiceImpl implements TaskService {
    @Override
    public List<TaskDto> createTasks(List<TaskDto> taskDtos) {
        return RestApiUtil.save(RestApiUrlConsts.SYSTEM_ADMIN_SERVICE + RestApiUrlConsts.IAIS_TASK,taskDtos,List.class);
    }

    @Override
    public HcsaSvcStageWorkingGroupDto getTaskConfig(HcsaSvcStageWorkingGroupDto hcsaSvcStageWorkingGroupDto) {
        return RestApiUtil.postGetObject(RestApiUrlConsts.GET_HCSA_WORK_GROUP,hcsaSvcStageWorkingGroupDto,HcsaSvcStageWorkingGroupDto.class);
    }

}

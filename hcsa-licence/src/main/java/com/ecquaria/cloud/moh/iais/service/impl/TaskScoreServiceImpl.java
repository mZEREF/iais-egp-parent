package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.common.dto.task.TaskScoreDto;
import com.ecquaria.cloud.moh.iais.common.utils.RestApiUtil;
import com.ecquaria.cloud.moh.iais.service.TaskScoreService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import sop.rbac.user.User;

import java.util.List;

/**
 * TaskScoreServiceImpl
 *
 * @author suocheng
 * @date 11/21/2019
 */
@Service
@Slf4j
public class TaskScoreServiceImpl implements TaskScoreService {
    @Override
    public List<TaskScoreDto> getTaskScores(String workGroupName) {
        return RestApiUtil.getListByPathParam("",workGroupName,TaskScoreDto.class);
    }

    @Override
    public TaskScoreDto getLowestTaskScore(List<TaskScoreDto> taskScoreDtos, List<User> users) {
        return null;
    }
}

package com.ecquaria.cloud.moh.iais.service;

import com.ecquaria.cloud.moh.iais.common.dto.task.TaskScoreDto;
import sop.rbac.user.User;

import java.util.List;

/**
 * TaskScoreService
 *
 * @author suocheng
 * @date 11/21/2019
 */
public interface TaskScoreService {
    List<TaskScoreDto> getTaskScores(String workGroupName);
    TaskScoreDto getLowestTaskScore(List<TaskScoreDto> taskScoreDtos,List<User> users);
}

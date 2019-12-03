package com.ecquaria.cloud.moh.iais.service.client;

import com.ecquaria.cloud.moh.iais.common.dto.task.TaskDto;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import org.springframework.http.HttpHeaders;

/**
 * @Author: jiahao
 * @Date: 2019/11/29 9:33
 */
public class FillUpCheckListGetTaskCilentFallBack implements FillUpCheckListGetTaskCilent {
    @Override
    public FeignResponseEntity<TaskDto> getTaskDtoByTaskId(String taskId) {
        FeignResponseEntity entity = new FeignResponseEntity<>();
        HttpHeaders headers = new HttpHeaders();
        entity.setHeaders(headers);
        return entity;
    }
}

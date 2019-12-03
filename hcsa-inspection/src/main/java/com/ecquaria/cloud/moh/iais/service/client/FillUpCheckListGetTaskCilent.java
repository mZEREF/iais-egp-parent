package com.ecquaria.cloud.moh.iais.service.client;

import com.ecquaria.cloud.moh.iais.common.dto.task.TaskDto;
import com.ecquaria.cloudfeign.FeignConfiguration;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @Author: jiahao
 * @Date: 2019/11/29 9:31
 */
@FeignClient(name = "iais-organization", configuration = FeignConfiguration.class,
        fallback = FillUpCheckListGetTaskCilentFallBack.class)
public interface FillUpCheckListGetTaskCilent {
    @GetMapping(path = "/iais-task/{taskId}", produces = { MediaType.APPLICATION_JSON_VALUE },
            consumes = {MediaType.APPLICATION_JSON_VALUE})
    FeignResponseEntity<TaskDto> getTaskDtoByTaskId(@PathVariable(value = "taskId") String taskId);
}

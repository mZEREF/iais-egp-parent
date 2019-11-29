package com.ecquaria.cloud.moh.iais.service.client;

import com.ecquaria.cloud.moh.iais.common.dto.task.TaskDto;
import com.ecquaria.cloudfeign.FeignConfiguration;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * @author Shicheng
 * @date 2019/11/26 13:50
 **/
@FeignClient(name = "organization", configuration = FeignConfiguration.class,
        fallback = CommonPoolTaskClientFallback.class)
public interface CommonPoolTaskClient {
    @RequestMapping(path = "/iais-task/commpool/{workGroupId}",method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE },
            consumes = {MediaType.APPLICATION_JSON_VALUE})
    FeignResponseEntity<List<TaskDto>> getCommPoolByGroupWordId(@PathVariable(value = "workGroupId") String workGroupId);

    @PutMapping(path = "/iais-task", produces = { MediaType.APPLICATION_JSON_VALUE },
            consumes = {MediaType.APPLICATION_JSON_VALUE})
    FeignResponseEntity<TaskDto> updateAndAssignTask(@PathVariable TaskDto taskDto);

    @PostMapping(path = "/iais-task",                                                                                          produces = { MediaType.APPLICATION_JSON_VALUE },
            consumes = {MediaType.APPLICATION_JSON_VALUE})
    FeignResponseEntity<TaskDto> createAndAssignTask(@PathVariable TaskDto taskDto);
}

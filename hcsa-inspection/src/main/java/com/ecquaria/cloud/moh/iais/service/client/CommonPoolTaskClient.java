package com.ecquaria.cloud.moh.iais.service.client;

import com.ecquaria.cloud.moh.iais.common.dto.task.TaskDto;
import com.ecquaria.cloudfeign.FeignConfiguration;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
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
    @RequestMapping(path = "/iais-task/commpool/{workGroupId}",method = RequestMethod.POST, produces = { MediaType.APPLICATION_JSON_VALUE },
            consumes = {MediaType.APPLICATION_JSON_VALUE}, value = "{workGroupId}")
    FeignResponseEntity<List<TaskDto>> getCommPoolByGroupWordId(@PathVariable String workGroupId);
}

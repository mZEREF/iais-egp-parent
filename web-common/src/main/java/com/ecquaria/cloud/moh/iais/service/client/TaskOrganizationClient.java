package com.ecquaria.cloud.moh.iais.service.client;

import com.ecquaria.cloud.moh.iais.common.dto.organization.OrgUserDto;
import com.ecquaria.cloud.moh.iais.common.dto.task.TaskDto;
import com.ecquaria.cloudfeign.FeignConfiguration;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import java.util.List;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * TaskOrganizationClient
 *
 * @author suocheng
 * @date 12/4/2019
 */
@FeignClient(name = "iais-organization", configuration = FeignConfiguration.class,
        fallback = TaskOrganizationClientFallback.class)
public interface TaskOrganizationClient {
    @PostMapping(path = "/iais-task",produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<TaskDto>> createTask(@RequestBody List<TaskDto> taskDtos);

    @PutMapping(path = "/iais-task",produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<TaskDto> updateTask(@RequestBody TaskDto taskDto);

    @GetMapping(path = "/iais-task/{taskId}",produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<TaskDto> getTaskById(@PathVariable(name="taskId") String taskId);

    @GetMapping(path = "/iais-workgroup/orguseraccount/{workGroupId}/{status}",produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<OrgUserDto>> getUsersByWorkGroupName(@PathVariable(name = "workGroupId") String workGroupId,
                                                                  @PathVariable(name = "status") String staus);

    @GetMapping(path = "/iais-task/taskscores/{workGroupId}",produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<TaskDto>> getTaskScores(@PathVariable(name = "workGroupId") String workGroupId);

    @GetMapping(path = "/iais-task/commpool/{workGroupId}",produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<TaskDto>> getCommPoolTaskByWorkGroupId(@PathVariable(name = "workGroupId") String workGroupId);
}

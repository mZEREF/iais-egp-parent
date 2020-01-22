package com.ecquaria.cloud.moh.iais.service.client;

import com.ecquaria.cloud.moh.iais.common.dto.organization.OrgUserDto;
import com.ecquaria.cloud.moh.iais.common.dto.task.TaskDto;
import com.ecquaria.cloudfeign.FeignConfiguration;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import java.util.List;
import java.util.Map;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * TaskOrganizationClient
 *
 * @author suocheng
 * @date 12/4/2019
 */
@FeignClient(name = "iais-organization", configuration = FeignConfiguration.class,
        fallback = TaskOrganizationClientFallback.class)
public interface TaskOrganizationClient {
    @RequestMapping(path = "/iais-task",method = RequestMethod.POST,produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<TaskDto>> createTask(@RequestBody List<TaskDto> taskDtos);

    @RequestMapping(path = "/iais-task",method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<TaskDto> updateTask(@RequestBody TaskDto taskDto);

    @RequestMapping(path = "/iais-task/{taskId}",method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<TaskDto> getTaskById(@PathVariable(name="taskId") String taskId);

    @RequestMapping(path = "/iais-workgroup/orguseraccount/{workGroupId}/{status}"
            ,method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<OrgUserDto>> getUsersByWorkGroupName(@PathVariable(name = "workGroupId") String workGroupId,
                                                                  @PathVariable(name = "status") String staus);

    @RequestMapping(path = "/iais-task/taskscores/{workGroupId}",method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<TaskDto>> getTaskScores(@PathVariable(name = "workGroupId") String workGroupId);

    @RequestMapping(path = "/iais-task/commpool/{workGroupId}",method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<TaskDto>> getCommPoolTaskByWorkGroupId(@PathVariable(name = "workGroupId") String workGroupId);

    @GetMapping(path = "/iais-task/tasks-notify-all",produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<Map<String, Object>> getEmailNotifyList();

    @GetMapping(path = "/iais-task/inspector-task/{appCorrId}",produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<String>> getInspectorByAppCorrId(@PathVariable(name = "appCorrId") String appCorrId);


}

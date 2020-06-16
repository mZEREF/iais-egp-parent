package com.ecquaria.cloud.moh.iais.service.client;

import com.ecquaria.cloud.moh.iais.common.dto.organization.OrgUserDto;
import com.ecquaria.cloud.moh.iais.common.dto.task.TaskDto;
import com.ecquaria.cloud.moh.iais.common.dto.task.TaskEmailDto;
import com.ecquaria.cloudfeign.FeignConfiguration;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * TaskOrganizationClient
 *
 * @author suocheng
 * @date 12/4/2019
 */
@FeignClient(name = "iais-organization",configuration = FeignConfiguration.class,
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
    FeignResponseEntity<List<TaskEmailDto>> getEmailNotifyList();

    @PostMapping(value = "/iais-task/allWorkGroupMembers", consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity <Map<String, List<String>>> getAllWorkGroupMembers(@RequestBody List<String> groupId);

    @GetMapping(value = "/iais-task/inspector-task/{applicationNo}",produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<String>> getInspectorByAppNo(@PathVariable(name = "applicationNo") String applicationNo);

    @GetMapping(value = "/iais-task/corrid-inspectors",produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<Set<String>> getInspectors(@RequestParam(name = "appNo") String appNo, @RequestParam(name = "processUrl") String processUrl, @RequestParam(name = "roleId") String roleId);

    @RequestMapping(path = "/iais-task/Tasks/date",method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<TaskDto>> getTaskDtoByDate(@RequestParam(name ="date") String date);
}

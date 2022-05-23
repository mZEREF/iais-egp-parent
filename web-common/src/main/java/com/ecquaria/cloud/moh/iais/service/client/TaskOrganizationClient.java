package com.ecquaria.cloud.moh.iais.service.client;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesCorrelationDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.OrgUserDto;
import com.ecquaria.cloud.moh.iais.common.dto.task.TaskDto;
import com.ecquaria.cloud.moh.iais.common.dto.task.TaskEmailDto;
import com.ecquaria.cloudfeign.FeignConfiguration;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

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

    @PostMapping(value = "/taskList",produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<TaskDto>> getTaskList(@RequestBody List<String> taskIds);

    @RequestMapping(path = "/iais-workgroup/orguseraccount/{workGroupId}/{status}"
            ,method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<OrgUserDto>> getUsersByWorkGroupName(@PathVariable(name = "workGroupId") String workGroupId,
                                                                  @PathVariable(name = "status") String staus);

    @RequestMapping(path = "/iais-workgroup/orguseraccountExceptLeader/{workGroupId}/{status}"
            ,method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<OrgUserDto>> getUsersByWorkGroupNameExceptLeader(@PathVariable(name = "workGroupId") String workGroupId,
                                                                  @PathVariable(name = "status") String staus);

    @RequestMapping(path = "/iais-task/taskscores/{workGroupId}/{days}",method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<TaskDto>> getTaskScores(@PathVariable(name = "workGroupId") String workGroupId,@PathVariable(name = "days") String days);

    @GetMapping(value = "/iais-orgUserRole/user-by-id/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<OrgUserDto> getUserById(@PathVariable("userId") String userId);
    @RequestMapping(path = "/iais-task/commpool/{workGroupId}",method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<TaskDto>> getCommPoolTaskByWorkGroupId(@PathVariable(name = "workGroupId") String workGroupId);

    @GetMapping(path = "/iais-task/tasks-notify-all",produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<TaskEmailDto>> getEmailNotifyList();

    @GetMapping(path = "/iais-task/tasks-notify-leader/{taskId}",produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<OrgUserDto>> getEmailNotifyLeader(@PathVariable(name = "taskId") String taskId);

    @PostMapping(value = "/iais-task/allWorkGroupMembers", consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity <Map<String, List<String>>> getAllWorkGroupMembers(@RequestBody List<String> groupId);

    @GetMapping(value = "/iais-task/inspector-task/{applicationNo}",produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<String>> getInspectorByAppNo(@PathVariable(name = "applicationNo") String applicationNo);

    @GetMapping(value = "/iais-task/refNo-processUrl",produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<TaskDto>> getTaskByUrlAndRefNo(@RequestParam(name = "refNo") String refNo, @RequestParam(name = "processUrl") String processUrl);

    @GetMapping(value = "/iais-task/corrid-inspectors",produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<Set<String>> getInspectors(@RequestParam(name = "appNo") String appNo, @RequestParam(name = "processUrl") String processUrl, @RequestParam(name = "roleId") String roleId);

    @RequestMapping(path = "/iais-task/Tasks/date",method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<TaskDto>> getTaskDtoByDate(@RequestParam(name ="date") String date,@RequestParam(name ="isRouting") boolean isRouting);

    @PostMapping(value = "/iais-orgUserRole/user-by-roles", consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<OrgUserDto>> retrieveOrgUserByroleId(@RequestBody List<String> roleId);

    @PostMapping(value = "/iais-orgUserRole/users-by-id",consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<OrgUserDto>> retrieveOrgUsers(@RequestBody Collection<String> userIds);

    @GetMapping(value = "/iais-task/get-task-by-application-no",produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<TaskDto>> getTaskbyApplicationNo(@RequestParam("applicationNo") String applicationNo);

    @GetMapping(value = "/iais-orguser-be/OrgUsers/{roleId}")
    FeignResponseEntity<List<OrgUserDto>> retrieveOrgUserAccountByRoleId(@PathVariable("roleId") String roleId);
    @GetMapping(value = "/iais-task/get-rfi-task-application-no",produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<TaskDto>> getTaskRfi(@RequestParam("applicationNo") String applicationNo);

    @GetMapping(value = "/iais-task/get-rollback-inspector",produces = MediaType.APPLICATION_JSON_VALUE,consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<String> getRollBackInspector(@RequestParam("refNo") String refNo);

    @PostMapping(value = "/iais-task/assigned/current-task/inspector-info",consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<OrgUserDto>> getCurrentTaskAssignedInspectorInfo(@RequestBody Map<String, Object> params);

    @PostMapping(value = "/iais-task/tasks/app-correlations",consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<TaskDto>> updateTasks(@RequestBody List<AppPremisesCorrelationDto> appPremisesCorrelations);

}

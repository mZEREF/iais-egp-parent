package com.ecquaria.cloud.moh.iais.service.client;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenseeDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspecTaskCreAndAssDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.BroadcastOrganizationDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.OrgUserDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.OrganizationDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.UserGroupCorrelationDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.WorkingGroupDto;
import com.ecquaria.cloud.moh.iais.common.dto.task.TaskDto;
import com.ecquaria.cloudfeign.FeignConfiguration;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author Wenkang
 * @date 2019/12/4 15:13
 */
@FeignClient(name = "iais-organization", configuration = FeignConfiguration.class,
        fallback = OrganizationMainClientFallback.class)
public interface OrganizationMainClient {
    @RequestMapping(value = "/iais-orguser-be/users-by-ids",method = RequestMethod.POST,produces = { MediaType.APPLICATION_JSON_VALUE },
            consumes = {MediaType.APPLICATION_JSON_VALUE})
    FeignResponseEntity<List<OrgUserDto>> retrieveOrgUserAccount(@RequestBody List<String> ids);

    @RequestMapping(path = "/iais-task",method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<TaskDto> updateTask(@RequestBody TaskDto taskDto);

    @RequestMapping(value = "/iais-orguser-be/users-account/{id}",method = RequestMethod.GET,produces = { MediaType.APPLICATION_JSON_VALUE },
            consumes = {MediaType.APPLICATION_JSON_VALUE})
    FeignResponseEntity<OrgUserDto> retrieveOrgUserAccountById(@PathVariable(value = "id") String id);

    @RequestMapping(value = "/iais-orgUserRole/users-by-loginId/{user_id}",method = RequestMethod.GET,produces = { MediaType.APPLICATION_JSON_VALUE },
            consumes = {MediaType.APPLICATION_JSON_VALUE})
    FeignResponseEntity<OrgUserDto> retrieveOneOrgUserAccount(@PathVariable(value = "user_id") String user_id);

    @RequestMapping(path = "/iais-task/super/{workGroupId}",method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE },
            consumes = {MediaType.APPLICATION_JSON_VALUE})
    FeignResponseEntity<List<TaskDto>> getSupervisorPoolByGroupWordId(@PathVariable(value = "workGroupId") String workGroupId);

    @RequestMapping(path = "/iais-task/Tasks/{userId}",method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE },
            consumes = {MediaType.APPLICATION_JSON_VALUE})
    FeignResponseEntity<List<TaskDto>> getTasksByUserId(@PathVariable(value = "userId") String userId);

    @RequestMapping(path = "/iais-task/TasksByRoleId/{userId}/{roleId}",method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE },
            consumes = {MediaType.APPLICATION_JSON_VALUE})
    FeignResponseEntity<List<TaskDto>> getTasksByUserIdAndRole(@PathVariable(value = "userId") String userId,@PathVariable(value = "roleId") String roleId);

    @GetMapping(path = "/iais-workgroup/orguseraccount/{workGroupId}/{status}", produces = { MediaType.APPLICATION_JSON_VALUE })
    FeignResponseEntity<List<OrgUserDto>> getUsersByWorkGroupName(@PathVariable(value = "workGroupId") String workGroupId, @PathVariable(name = "status") String status);

    @GetMapping(path = "/iais-task/{taskId}",produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<TaskDto> getTaskById(@PathVariable(name="taskId") String taskId);

    @RequestMapping(path = "/iais-workgroup",method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<WorkingGroupDto> createWorkGroup(@RequestBody WorkingGroupDto workingGroupDto);

    @RequestMapping(path = "/iais-broadcast",method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<BroadcastOrganizationDto> createBroadcastOrganization(@RequestBody BroadcastOrganizationDto broadcastOrganizationDto);

    @RequestMapping(path = "/iais-workgroup/usergrocorrd/{userId}",method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE },
            consumes = {MediaType.APPLICATION_JSON_VALUE})
    FeignResponseEntity<List<UserGroupCorrelationDto>> getUserGroupCorreByUserId(@PathVariable(value = "userId") String userId);

    @RequestMapping(path = "/iais-broadcast/{groupName}/{groupDomain}",method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE )
    FeignResponseEntity<BroadcastOrganizationDto> getBroadcastOrganizationDto(@PathVariable(name = "groupName") String groupName,
                                                                              @PathVariable(name = "groupDomain") String groupDomain);

    @PostMapping(value = "/iais-task/ins-kpi-task", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<TaskDto>> getInsKpiTask(@RequestBody InspecTaskCreAndAssDto inspecTaskCreAndAssDto);

    @PostMapping(value = "/iais-task/other-kpi-task", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<TaskDto>> getOtherKpiTask(@RequestBody TaskDto taskDto);

    @GetMapping(value = "/iais-workgroup/workGrop/{id}",produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<WorkingGroupDto> getWrkGrpById(@PathVariable(name = "id") String workGroupId);

    @GetMapping(value = "/organization/organization-id",produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<OrganizationDto> getOrganizationById(@RequestParam("id") String id);

    @GetMapping(value = "/iais-task/task-read",produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<String> taskRead(@RequestParam("id") String id);

    @GetMapping(value = "/iais-licensee-be/licensee-by-id/{id}")
    FeignResponseEntity<LicenseeDto> getLicenseeDtoById (@PathVariable("id") String id);

    @GetMapping(value = "/iais-task/stage-task-status",produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<TaskDto>> getTaskByAppNoStatus(@RequestParam("appNo") String appNo, @RequestParam("taskStatus") String taskStatus, @RequestParam("processUrl") String processUrl);

    @GetMapping(value = "/iais-licensee-be/licensee-by-user-info/{userAccountString}")
    FeignResponseEntity<LicenseeDto> getLicenseeByUserAccountInfo (@PathVariable("userAccountString") String userAccountString);

    @PostMapping(value = "/iais-workgroup/patch-user-group", produces = MediaType.APPLICATION_JSON_VALUE,consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<Void> patchWorkingGrpForRoles(@RequestBody String body);

    @GetMapping(value = "/iais-workgroup/group-lead/{workGroupId}")
    FeignResponseEntity<List<String>> getInspectionLead(@PathVariable(name = "workGroupId") String workGroupId);

    @PostMapping(path = "/iais-task/assign", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<InspecTaskCreAndAssDto> assignCommonPool(@RequestBody InspecTaskCreAndAssDto inspecTaskCreAndAssDto);

    @PutMapping(value = "/iais-task/u-a-task", consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<TaskDto> updateTaskForAssign(@RequestBody TaskDto taskDto);

    @GetMapping(value = "/iais-workgroup/group-role-user/{workGroupId}/{roleId}")
    FeignResponseEntity<List<OrgUserDto>> activeUsersByWorkGroupAndRole(@PathVariable(name = "workGroupId") String workGroupId,
                                                                        @PathVariable(name = "roleId") String roleId);

    @GetMapping(value = "/iais-workgroup/work-group-by-group-domain", produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<WorkingGroupDto>> getWorkingGroup(@RequestParam("uerDomain") String uerDomain);

    @GetMapping(value = "/iais-workgroup/work-group/name-hcsa",produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<WorkingGroupDto>> getHcsaWorkGroupsByName(@RequestParam("workGroupName") String workGroupName);

    @GetMapping(value = "/iais-task/curr-tasks/{refNo}",produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = {MediaType.APPLICATION_JSON_VALUE})
    FeignResponseEntity<List<TaskDto>> getCurrTaskByRefNo(@PathVariable(name = "refNo") String refNo);

    @GetMapping(value = "/iais-workgroup/wrkgroups/{userId}",produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<String>> getWorkGrpsByUserId(@PathVariable(name = "userId") String userId);
}

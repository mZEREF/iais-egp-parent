package com.ecquaria.cloud.moh.iais.service.client;

import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspecTaskCreAndAssDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspectionTaskPoolListDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.BroadcastOrganizationDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.OrgUserDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.OrgUserRoleDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.OrganizationDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.UserGroupCorrelationDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.WorkingGroupDto;
import com.ecquaria.cloud.moh.iais.common.dto.task.TaskDto;
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

/**
 * @author Wenkang
 * @date 2019/12/4 15:13
 */
@FeignClient(name = "iais-organization", configuration = FeignConfiguration.class,
        fallback = OrganizationClientFallback.class)
public interface OrganizationClient {
    @RequestMapping(value = "/iais-orguser-be/users-by-ids",method = RequestMethod.POST,produces = { MediaType.APPLICATION_JSON_VALUE },
            consumes = {MediaType.APPLICATION_JSON_VALUE})
    FeignResponseEntity<List<OrgUserDto>> retrieveOrgUserAccount(@RequestBody List<String> ids);

    @RequestMapping(value = "/iais-orguser-be/users-account/{id}",method = RequestMethod.GET,produces = { MediaType.APPLICATION_JSON_VALUE },
            consumes = {MediaType.APPLICATION_JSON_VALUE})
    FeignResponseEntity<OrgUserDto> retrieveOrgUserAccountById(@PathVariable(value = "id") String id);

    @RequestMapping(value = "/iais-orguser-be/users-by-loginId/{user_id}",method = RequestMethod.GET,produces = { MediaType.APPLICATION_JSON_VALUE },
            consumes = {MediaType.APPLICATION_JSON_VALUE})
    FeignResponseEntity<OrgUserDto> retrieveOneOrgUserAccount(@PathVariable(value = "user_id") String user_id);

    @RequestMapping(path = "/iais-task/super/{workGroupId}",method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE },
            consumes = {MediaType.APPLICATION_JSON_VALUE})
    FeignResponseEntity<List<TaskDto>> getSupervisorPoolByGroupWordId(@PathVariable(value = "workGroupId") String workGroupId);

    @RequestMapping(path = "/iais-task/Tasks/{userId}",method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE },
            consumes = {MediaType.APPLICATION_JSON_VALUE})
    FeignResponseEntity<List<TaskDto>> getTasksByUserId(@PathVariable(value = "userId") String userId);

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

    @PostMapping(path = "/iais-task/assign", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<InspecTaskCreAndAssDto> assignCommonPool(@RequestBody InspecTaskCreAndAssDto inspecTaskCreAndAssDto);

    @PostMapping(path = "/iais-task/sup-assign", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<InspectionTaskPoolListDto> assignSupTasks(@RequestBody InspectionTaskPoolListDto inspectionTaskPoolListDto);

    @GetMapping(value = "/iais-task/reassign-task-scores/{workGroupId}",produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<TaskDto>>getReassignTaskByWkId(@PathVariable(name = "workGroupId") String workGroupId);

    @GetMapping(value = "/iais-task/insert-inpor",produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<InspectionTaskPoolListDto> filterInspectorOption(@RequestBody InspectionTaskPoolListDto inspectionTaskPoolListDto);

    @GetMapping(value = "/iais-workgroup/inspection-lead/{workGroupId}")
    FeignResponseEntity<List<String>> getInspectionLead(@PathVariable(name = "workGroupId") String workGroupId);

    @GetMapping(value = "/iais-task/history-tasks/{appNo}",produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<TaskDto>> getTaskByAppNo(@PathVariable(name = "appNo") String appNo);

    @GetMapping(value = "/iais-workgroup/workGrop/{id}",produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<WorkingGroupDto> getWrkGrpById(@PathVariable(name = "id") String workGroupId);

    @GetMapping(value = "/organization/organization-id",produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<OrganizationDto> getOrganizationById(@RequestParam("id") String id);

    @GetMapping(value = "/iais-orguser-be/organization-user-admin-or",produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity< List<OrgUserRoleDto>> getSendEmailUser(@RequestParam("organizationId") String organizationId);
}

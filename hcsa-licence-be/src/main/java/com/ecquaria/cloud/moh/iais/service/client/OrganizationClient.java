package com.ecquaria.cloud.moh.iais.service.client;

import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.EventInspRecItemNcDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenseeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenseeKeyApptPersonDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspecTaskCreAndAssDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspectionTaskPoolListDto;
import com.ecquaria.cloud.moh.iais.common.dto.monitoringExcel.MonitoringSheetsDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.BroadcastOrganizationDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.LicenseeQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.OrgUserDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.OrgUserRoleDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.OrganizationDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.OrganizationLicDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.RimRiskCountDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.SuperPoolTaskQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.UserGroupCorrelationDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.WorkingGroupDto;
import com.ecquaria.cloud.moh.iais.common.dto.task.TaskDto;
import com.ecquaria.cloudfeign.FeignConfiguration;
import com.ecquaria.cloudfeign.FeignResponseEntity;
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

import java.util.List;
import java.util.Map;
import java.util.Set;

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

    @RequestMapping(value = "/iais-orgUserRole/users-by-loginId/{user_id}",method = RequestMethod.GET,produces = { MediaType.APPLICATION_JSON_VALUE },
            consumes = {MediaType.APPLICATION_JSON_VALUE})
    FeignResponseEntity<OrgUserDto> retrieveOneOrgUserAccount(@PathVariable(value = "user_id") String user_id);

    @GetMapping(value = "/iais-orgUserRole/user-account/info/{role_id}")
    FeignResponseEntity<List<OrgUserDto>> retrieveUserRoleByRoleId(@PathVariable("role_id")String roleId);

    @RequestMapping(path = "/iais-task/super/{workGroupId}",method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE },
            consumes = {MediaType.APPLICATION_JSON_VALUE})
    FeignResponseEntity<List<TaskDto>> getSupervisorPoolByGroupWordId(@PathVariable(value = "workGroupId") String workGroupId);

    @RequestMapping(path = "/iais-task/Tasks/{userId}",method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE },
            consumes = {MediaType.APPLICATION_JSON_VALUE})
    FeignResponseEntity<List<TaskDto>> getTasksByUserId(@PathVariable(value = "userId") String userId);

    @GetMapping(path = "/iais-workgroup/orguseraccount/{workGroupId}/{status}", produces = { MediaType.APPLICATION_JSON_VALUE })
    FeignResponseEntity<List<OrgUserDto>> getUsersByWorkGroupName(@PathVariable(value = "workGroupId") String workGroupId, @PathVariable(name = "status") String status);

    @GetMapping(path = "/iais-workgroup/orguseraccountnotvailable/{workGroupId}/{status}", produces = { MediaType.APPLICATION_JSON_VALUE })
    FeignResponseEntity<List<OrgUserDto>>  getUsersByWorkGroupNameNotAvailable(@PathVariable(value = "workGroupId") String workGroupId, @PathVariable(name = "status") String status);

    @GetMapping(path = "/iais-task/{taskId}",produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<TaskDto> getTaskById(@PathVariable(name="taskId") String taskId);

    @RequestMapping(path = "/iais-workgroup",method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<WorkingGroupDto> createWorkGroup(@RequestBody WorkingGroupDto workingGroupDto);

    @RequestMapping(path = "/iais-broadcast",method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<BroadcastOrganizationDto> createBroadcastOrganization(@RequestBody BroadcastOrganizationDto broadcastOrganizationDto);

    @RequestMapping(path = "/iais-workgroup/usergrocorrd/{userId}",method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE },
            consumes = {MediaType.APPLICATION_JSON_VALUE})
    FeignResponseEntity<List<UserGroupCorrelationDto>> getUserGroupCorreByUserId(@PathVariable(value = "userId") String userId);

    @GetMapping(value = "/iais-workgroup/group-role-lead/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<UserGroupCorrelationDto>> getUserGroupLeadByUserId(@PathVariable(name = "userId") String userId);

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

    @GetMapping(value = "/iais-workgroup/group-lead/{workGroupId}")
    FeignResponseEntity<List<String>> getInspectionLead(@PathVariable(name = "workGroupId") String workGroupId);

    @GetMapping(value = "/iais-task/history-tasks/{refNo}",produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = {MediaType.APPLICATION_JSON_VALUE})
    FeignResponseEntity<List<TaskDto>> getTasksByRefNo(@PathVariable(name = "refNo") String refNo);

    @GetMapping(value = "/iais-workgroup/workGrop/{id}",produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<WorkingGroupDto> getWrkGrpById(@PathVariable(name = "id") String workGroupId);

    @GetMapping(value = "/organization/organization-id",produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<OrganizationDto> getOrganizationById(@RequestParam("id") String id);

    @GetMapping(value = "/iais-orguser-be/organization-user-admin-or",produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity< List<OrgUserRoleDto>> getSendEmailUser(@RequestParam("organizationId") String organizationId);

    @GetMapping(value = "/iais-licensee-be/licensee-by-id/{id}")
    FeignResponseEntity<LicenseeDto> getLicenseeDtoById (@PathVariable("id") String id);

    @PostMapping(value = "/iais-licensee/getAllLicenseeIdName", consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<Map<String, String>> getAllLicenseeIdName(@RequestBody Set<String> licenseeIdList);

    @GetMapping(value = "/iais-licensee/licensee-overtime/{days}")
    FeignResponseEntity<List<LicenseeDto>> getLicenseeDtoOvertime (@PathVariable("days") String days);

    @GetMapping(value = "/iais-licensee-be/licenseeDto-by-uenNo/{uenNo}")
    FeignResponseEntity<List<LicenseeDto>> getLicenseeDtoByUen(@PathVariable(name = "uenNo") String uenNo);

    @RequestMapping(path = "/iais-task/TasksByRoleId/{userId}/{roleId}",method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE },
            consumes = {MediaType.APPLICATION_JSON_VALUE})
    FeignResponseEntity<List<TaskDto>> getTasksByUserIdAndRole(@PathVariable(value = "userId") String userId,@PathVariable(value = "roleId") String roleId);

    @GetMapping(value = "/iais-task/task-email-comp",produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<TaskDto>> getTaskForCompLeader();

    @PostMapping(value = "/iais-licensee-be/search-licenseeIds-param" ,consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<SearchResult<LicenseeQueryDto>> searchLicenseeIdsParam(@RequestBody SearchParam searchParam);

    @GetMapping(value = "/iais-task/curr-tasks/{refNo}",produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = {MediaType.APPLICATION_JSON_VALUE})
    FeignResponseEntity<List<TaskDto>> getCurrTaskByRefNo(@PathVariable(name = "refNo") String refNo);

    @GetMapping(value = "/iais-workgroup/work-group-by-group-domain", produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<WorkingGroupDto>> getWorkingGroup(@RequestParam("uerDomain") String uerDomain);

    @GetMapping(value = "/iais-workgroup/group-role-user/{workGroupId}/{roleId}")
    FeignResponseEntity<List<OrgUserDto>> activeUsersByWorkGroupAndRole(@PathVariable(name = "workGroupId") String workGroupId,
                                                                          @PathVariable(name = "roleId") String roleId);

    @GetMapping(value = "/iais-licensee-be/OrganizationLicDto/{licenseeId}",produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<OrganizationLicDto> getOrganizationLicDtoByLicenseeId(@PathVariable(name = "licenseeId") String licenseeId);

    @GetMapping(value = "/iais-task/com-tasks/{appNo}",produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<TaskDto>> getCompleteTaskByAppNo(@PathVariable(name = "appNo") String appNo);

    @PostMapping(value = "/iais-task/rec-event-task", produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<EventInspRecItemNcDto> getEventInspRecItemNcTaskByCorrIds(@RequestBody EventInspRecItemNcDto eventInspRecItemNcDto);

    @PostMapping(value = "/iais-task/super-second/results", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<SearchResult<SuperPoolTaskQueryDto>> supervisorSecondSearch(@RequestBody SearchParam searchParam);

    @GetMapping(value = "/iais-task/stage-task-status",produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<TaskDto>> getTaskByAppNoStatus(@RequestParam("appNo") String appNo, @RequestParam("taskStatus") String taskStatus, @RequestParam("processUrl") String processUrl);

    @GetMapping(value = "/iais-licensee-be/licenseeKeyApptPersonByLicId/{licenseeId}",produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<LicenseeKeyApptPersonDto>> getLicenseeKeyApptPersonDtoListByLicenseeId(@PathVariable("licenseeId") String licenseeId);


    @GetMapping(value = "/iais-licensee-be/licenseesByOrgId/{orgId}",produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<LicenseeDto>> getLicenseeByOrgId(@PathVariable(name = "orgId") String orgId);


    @PostMapping(value = "/iais-task/ins-kpi-task", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<TaskDto>> getInsKpiTask(@RequestBody InspecTaskCreAndAssDto inspecTaskCreAndAssDto);

    @PostMapping(value = "/iais-task/other-kpi-task", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<TaskDto>> getOtherKpiTask(@RequestBody TaskDto taskDto);

    @GetMapping(value = "/iais-task/effective-task-url",produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<TaskDto>> getActiveTaskByUrl(@RequestParam("processUrl") String processUrl);

    @GetMapping(value = "/iais-task/kpi-task-col")
    FeignResponseEntity<List<TaskDto>> getKpiTaskByStatus();

    @RequestMapping(value = "/iais-orguser-be/intranet-user-role-list/{userAccId}",method = RequestMethod.GET,produces = { MediaType.APPLICATION_JSON_VALUE },
            consumes = {MediaType.APPLICATION_JSON_VALUE})
    FeignResponseEntity<List<OrgUserRoleDto>> getUserRoleByUserAccId(@RequestParam("userAccId") String userAccId);

    @GetMapping(value = "/iais-rimriskcount/get-rim-risk-count-uen/{licenseeId}")
    FeignResponseEntity<RimRiskCountDto> getUenRimRiskCountDtoByLicenseeId(@PathVariable("licenseeId") String licenseId);

    @PostMapping(value = "/iais-rimriskcount/saveRimRiskCount",consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<RimRiskCountDto> doRimRiskCountSave(@RequestBody RimRiskCountDto rimRiskCountDto);
    @GetMapping(value = "/iais-orguser-be/org-user-account-sample-by-organization-id",produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<OrgUserDto>> getOrgUserAccountSampleDtoByOrganizationId(@RequestParam("organizationId") String organizationId);
    @GetMapping(value = "/iais-orguser-be/licensee-ke-appt-person-by-liceseeId",produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<LicenseeKeyApptPersonDto>> getLicenseeKeyApptPersonByLiceseeId(@RequestParam("liceseeId") String liceseeId);

    @PutMapping(value = "/iais-task/u-a-task", consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<TaskDto> updateTaskForAssign(@RequestBody TaskDto taskDto);

    @PostMapping(value = "/organization/individual-send-mail", consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<Void> updateIndividualFlag(@RequestParam("id") String id);

    @GetMapping(value = "/iais-orguser-be/OrgUsers/{roleId}")
    FeignResponseEntity<List<OrgUserDto>> retrieveOrgUserAccountByRoleId(@PathVariable("roleId") String roleId);

    @GetMapping(value = "/iais-task/get-task-by-application-and-role-id-and-status",produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<TaskDto>> getTaskByApplicationNoAndRoleIdAndStatus(@RequestParam("applicationNo") String applicationNo,@RequestParam("roleId") String roleId,@RequestParam("status") String status);

    @GetMapping(value = "/iais-workgroup/work-group/name-hcsa",produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<WorkingGroupDto>> getHcsaWorkGroupsByName(@RequestParam("workGroupName") String workGroupName);

    @GetMapping(value = "/iais-licensee/licensee-fromSingpass")
    FeignResponseEntity<List<LicenseeDto>> getLicenseeDtoFromSingpass();

    @GetMapping(value = "/iais-workgroup/wrkgroups/{userId}",produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<String>> getWorkGrpsByUserId(@PathVariable(name = "userId") String userId);

    @GetMapping(value = "/iais-orguser-be/monitoring-user-sheet",produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<MonitoringSheetsDto> getMonitoringUserSheetsDto();
}

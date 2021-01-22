package com.ecquaria.cloud.moh.iais.service.client;

import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.organization.*;
import com.ecquaria.cloud.moh.iais.common.dto.task.TaskDto;
import com.ecquaria.cloudfeign.FeignConfiguration;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author weilu
 * @date 2019/12/25 15:45
 */
@FeignClient(name = "iais-organization", configuration = FeignConfiguration.class, fallback = IntranetUserClientFallback.class)
public interface IntranetUserClient {

    @PostMapping(value = "/iais-orguser-be/user-management", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<Void> createOrgUserDto(@RequestBody OrgUserDto user);

    @PostMapping(value = "/iais-orguser-be/users-management", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<Void> createOrgUserDtos(@RequestBody List<OrgUserDto> orgUserDtos);

    @PostMapping(value = "/iais-orguser-be/intranet-user-param", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<SearchResult<OrgUserQueryDto>> doQuery(@RequestBody SearchParam searchParam);

    @PutMapping(value = "/iais-orguser-be" ,consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<OrgUserDto> updateOrgUserDto(@RequestBody OrgUserDto orgUserDto);

    @DeleteMapping(value = "/iais-orguser-be/{id}")
    FeignResponseEntity<Void> delOrgUser (@PathVariable("id")String id);

    @GetMapping(value = "/iais-orguser-be/users-account/{id}",produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<OrgUserDto> findIntranetUserById(@PathVariable("id")String id);

    @PostMapping(value = "/iais-task//task/results", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<SearchResult<TaskDto>> getTaskListBySearchParam(@RequestBody SearchParam searchParam);

    @RequestMapping(value = "/iais-orgUserRole/users-by-loginId/{user_id}",method = RequestMethod.GET,produces = { MediaType.APPLICATION_JSON_VALUE },
            consumes = {MediaType.APPLICATION_JSON_VALUE})
    FeignResponseEntity<OrgUserDto> retrieveOneOrgUserAccount(@PathVariable(value = "user_id") String user_id);

    @PostMapping(value = "/iais-workgroup/work-group/results", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<SearchResult<WorkingGroupQueryDto>> getWorkingGroupBySearchParam(@RequestBody SearchParam searchParam);

    @GetMapping(value = "/iais-task/corrids-workgid/{workGroupId}",produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<TaskDto>> getCorrIdsByWorkGroupId(@PathVariable("workGroupId")String workGroupId);

    @RequestMapping(path = "/iais-workgroup/usergrocorrd/{userId}",method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE },
            consumes = {MediaType.APPLICATION_JSON_VALUE})
    FeignResponseEntity<List<UserGroupCorrelationDto>> getUserGroupCorreByUserId(@PathVariable(value = "userId") String userId);

    @GetMapping(value = "/iais-orguser-be/users-by-userId/{user_id}")
    FeignResponseEntity<OrgUserDto> getOrgUserAccountByUserId(@PathVariable("user_id") String userId);

    @PostMapping(value = "/iais-orguser-be/intranet-user-assign-role", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<OrgUserRoleDto>> assignRole(@RequestBody List<OrgUserRoleDto> orgUserRoleDtos);

    @DeleteMapping(value = "/iais-orguser-be/removeRole")
    FeignResponseEntity<String> removeRole(@RequestBody List<OrgUserRoleDto> orgUserRoleDtos);

    @GetMapping(value = "/iais-orguser-be/intranet-user-role-list/{userAccId}",produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<OrgUserRoleDto>> retrieveRolesByuserAccId(@RequestParam("userAccId") String userAccId);

    @PostMapping(value = "/iais-orguser-be/user-role-ids", consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<OrgUserRoleDto>> getUserRoleByIds(@RequestBody List<String> ids);

    @PostMapping(value = "/iais-workgroup/user-group-ids", consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<UserGroupCorrelationDto>> createUserGroupCorrelation(@RequestBody List<UserGroupCorrelationDto> userGroupCorrelationDtos);

    @GetMapping(value = "/iais-workgroup/user-groups/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<UserGroupCorrelationDto>> getUserGroupsByUserId(@PathVariable(name = "userId") String userId);

    @GetMapping(value = "/iais-workgroup/workGrop/{id}",produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<WorkingGroupDto> getWrkGrpById(@PathVariable(name = "id") String workGroupId);

    @PostMapping(value = "/iais-orguser-be/user-role-exist", consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<Boolean> checkRoleIsExist(@RequestBody OrgUserRoleDto orgUserRoleDto);

    @PostMapping(value = "/iais-workgroup/user-groups-groupId", produces = MediaType.APPLICATION_JSON_VALUE,consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<UserGroupCorrelationDto>> getUserGroupsByUserIdAndWorkGroups(@RequestBody UserGroupCorrelationDto userGroupCorrelationDto);

    @GetMapping(value = "/iais-orgUserRole/user-roles/{user_id}")
    FeignResponseEntity<List<String>> retrieveUserRoles(@PathVariable("user_id") String userId);

    @GetMapping(value = "/iais-orguser-be/further-active-user", produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<OrgUserDto>> searchActiveBeUser();

    @GetMapping(value = "/iais-orguser-be/inActive-user", produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<OrgUserDto>> searchInActiveBeUser();
}

package com.ecquaria.cloud.moh.iais.service.client;

import com.ecquaria.cloud.moh.iais.common.dto.organization.OrgUserDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.UserGroupCorrelationDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.WorkingGroupDto;
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

    @RequestMapping(path = "/iais-workgroup/usergrocorrd/{userId}",method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE },
            consumes = {MediaType.APPLICATION_JSON_VALUE})
    FeignResponseEntity<List<UserGroupCorrelationDto>> getUserGroupCorreByUserId(@PathVariable(value = "userId") String userId);

    @GetMapping(path = "/iais-workgroup/orguseraccount/{workGroupId}/{status}", produces = { MediaType.APPLICATION_JSON_VALUE })
    FeignResponseEntity<List<OrgUserDto>> getUsersByWorkGroupName(@PathVariable(value = "workGroupId") String workGroupId, @PathVariable(name = "status") String status);

    @GetMapping(value = "/iais-workgroup/workGrop/{id}",produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<WorkingGroupDto> getWrkGrpById(@PathVariable(name = "id") String workGroupId);

    @PostMapping(value = "/iais-orgUserRole/getEmailByRoleId", produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<String>> getEmailByRoleId(@RequestParam("roleId") String roleId);

    @PostMapping(value = "/iais-orgUserRole/getSmsByRoleId", produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<String>> getSmsByRoleId(@RequestParam("roleId") String roleId);
}

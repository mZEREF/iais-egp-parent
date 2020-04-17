package com.ecquaria.cloud.moh.iais.service.client;


import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.inbox.InterInboxUserDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.FeUserDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.FeUserQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.OrgUserRoleDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.OrganizationDto;
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

@FeignClient(name = "iais-organization",configuration = FeignConfiguration.class,fallback = FeUserClientFallback.class)
public interface FeUserClient {
    @RequestMapping(path = "/iais-internet-user/getFeUserList",method = RequestMethod.POST, produces = { MediaType.APPLICATION_JSON_VALUE },
            consumes = {MediaType.APPLICATION_JSON_VALUE})
    FeignResponseEntity<SearchResult<FeUserQueryDto>> getFeUserList(SearchParam searchParam);

    @GetMapping(value = "/iais-internet-user/organization/{uen}",produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<OrganizationDto> findOrganizationByUen(@PathVariable("uen") String uen);

    @GetMapping(value = "/iais-internet-user/user-account-userid",produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<FeUserDto> getUserAccount(@RequestParam("id") String id);

    @GetMapping(value = "/iais-internet-user/{nric}/uen-list/")
    FeignResponseEntity<List<String>> getUenListByNric(@PathVariable("nric") String nric);

    @GetMapping(value = "/iais-internet-user/user-account/{nric}")
    FeignResponseEntity<FeUserDto> getInternetUserByNric(@PathVariable("nric") String nric);

    @RequestMapping(path = "/iais-internet-user/edit-user-account",method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<FeUserDto> editUserAccount(@RequestBody FeUserDto feUserDto);

    @RequestMapping(path = "/iais-internet-user/add-user-role",method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<OrgUserRoleDto> addUserRole(@RequestBody OrgUserRoleDto orgUserRoleDto);

    @PostMapping(path = "/iais-internet-user/user-account/", consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
	FeignResponseEntity<FeUserDto> createSingpassAccount(@RequestBody OrganizationDto organizationDto);

    @GetMapping(value = "/iais-internet-user/organization/{uen}/user/{nric}",produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<FeUserDto> getUserByNricAndUen(@PathVariable(value = "uen") String uen, @PathVariable(value = "nric") String nric);

    @PostMapping(path = "/iais-internet-user/organization/user-account/", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<FeUserDto> createCropUser(@RequestBody OrganizationDto organizationDto);

    @GetMapping(value = "/iais-internet-user/user-info/{userId}")
    FeignResponseEntity<InterInboxUserDto> findUserInfoByUserId(@PathVariable("userId")String UserId);

}
package com.ecquaria.cloud.moh.iais.service.client;


import com.ecquaria.cloud.moh.iais.common.dto.IaisApiResult;
import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.inbox.InterInboxUserDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.*;
import com.ecquaria.cloudfeign.FeignConfiguration;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@FeignClient(name = "iais-organization",configuration = FeignConfiguration.class,fallback = FeUserClientFallback.class)
public interface FeUserClient {
    @RequestMapping(path = "/iais-internet-user/getFeUserList",method = RequestMethod.POST, produces = { MediaType.APPLICATION_JSON_VALUE },
            consumes = {MediaType.APPLICATION_JSON_VALUE})
    FeignResponseEntity<SearchResult<FeUserQueryDto>> getFeUserList(SearchParam searchParam);

    @GetMapping(value = "/iais-internet-user/user-account-userid",produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<FeUserDto> getUserAccount(@RequestParam("id") String id);

    @RequestMapping(path = "/iais-internet-user/edit-user-account",method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<FeUserDto> editUserAccount(@RequestBody FeUserDto feUserDto);

    @RequestMapping(path = "/iais-internet-user/add-user-role",method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<OrgUserRoleDto> addUserRole(@RequestBody OrgUserRoleDto orgUserRoleDto);

    @PostMapping(path = "/iais-internet-user/user-account/", consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
	FeignResponseEntity<IaisApiResult<List<String>>> singPassLoginFe(@RequestBody String jsonOpt);

    @GetMapping(value = "/iais-internet-user/organization/{uen}/user/{nric}",produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<Map<String, Object>> getUserByNricAndUen(@PathVariable(value = "uen") String uen, @PathVariable(value = "nric") String nric);

    @PostMapping(path = "/iais-internet-user/organization/user-account/", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<IaisApiResult<String>> createCropUser(@RequestBody String json);

    @GetMapping(value = "/iais-internet-user/user-info/{userId}")
    FeignResponseEntity<InterInboxUserDto> findUserInfoByUserId(@PathVariable("userId")String UserId);

}
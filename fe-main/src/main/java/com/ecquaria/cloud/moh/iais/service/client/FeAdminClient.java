package com.ecquaria.cloud.moh.iais.service.client;


import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.organization.FeAdminDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.FeAdminQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.OrganizationDto;
import com.ecquaria.cloudfeign.FeignConfiguration;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "iais-organization",configuration = FeignConfiguration.class,fallback = FeAdminClientFallback.class)
public interface FeAdminClient {
    @RequestMapping(path = "/iais-internet-user/feAdminlist",method = RequestMethod.POST, produces = { MediaType.APPLICATION_JSON_VALUE },
            consumes = {MediaType.APPLICATION_JSON_VALUE})
    FeignResponseEntity<SearchResult<FeAdminQueryDto>> getFeAdminList(SearchParam searchParam);

    @GetMapping(value = "/iais-internet-user/organization-id",produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<OrganizationDto> getOrganizationById(@RequestParam("id") String id);

    @RequestMapping(path = "/iais-internet-user/add-admin-account",method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<FeAdminDto> addAdminAccount(@RequestBody FeAdminDto feAdminDto);

    @GetMapping(value = "/iais-internet-user/change-active-status",produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<String> ChangeActiveStatus(@RequestParam("userId") String id,@RequestParam("targetStatus") String targetStatus);

}
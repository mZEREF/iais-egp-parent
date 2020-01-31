package com.ecquaria.cloud.moh.iais.service.client;


import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.organization.FeUserDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.FeUserQueryDto;
import com.ecquaria.cloudfeign.FeignConfiguration;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "iais-organization",configuration = FeignConfiguration.class,fallback = FeUserClientFallback.class)
public interface FeUserClient {
    @RequestMapping(path = "/hcsa-User-fe/getFeUserList",method = RequestMethod.POST, produces = { MediaType.APPLICATION_JSON_VALUE },
            consumes = {MediaType.APPLICATION_JSON_VALUE})
    FeignResponseEntity<SearchResult<FeUserQueryDto>> getFeUserList(SearchParam searchParam);

    @GetMapping(value = "/hcsa-User-fe/user-account-userid",produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<FeUserDto> getUserAccount(@RequestParam("id") String id);

    @RequestMapping(path = "/hcsa-User-fe/edit-user-account",method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<FeUserDto> editUserAccount(@RequestBody FeUserDto feUserDto);

}
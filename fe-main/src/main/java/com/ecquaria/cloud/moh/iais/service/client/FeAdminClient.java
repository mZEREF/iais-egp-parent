package com.ecquaria.cloud.moh.iais.service.client;


import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenseeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenseeKeyApptPersonDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.FeAdminQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.FeUserDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.OrganizationDto;
import com.ecquaria.cloudfeign.FeignConfiguration;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "iais-organization",configuration = FeignConfiguration.class,fallback = FeAdminClientFallback.class)
public interface FeAdminClient {
    @PostMapping(path = "/iais-internet-user/feAdminlist", produces = { MediaType.APPLICATION_JSON_VALUE },
            consumes = {MediaType.APPLICATION_JSON_VALUE})
    FeignResponseEntity<SearchResult<FeAdminQueryDto>> getFeAdminList(SearchParam searchParam);

    @GetMapping(value = "/iais-internet-user/organization-id",produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<OrganizationDto> getOrganizationById(@RequestParam("id") String id);

    @GetMapping(path = "/iais-internet-user/user-account-orgid", produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<FeUserDto>> getAccountByOrgId(@RequestParam(value = "orgId")String orgId);

    @PostMapping(path = "/iais-internet-user/add-admin-account",consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<FeUserDto> addAdminAccount(@RequestBody FeUserDto feUserDto);

    @GetMapping(value = "/iais-internet-user/change-active-status",produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<String> ChangeActiveStatus(@RequestParam("userId") String id,@RequestParam("targetStatus") String targetStatus);

    @GetMapping(value = "/iais-licensee/licensee/{id}",produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<LicenseeDto> getLicenseeById(@PathVariable(name = "id") String id);

    @GetMapping(value = "/iais-licensee/licenseesByOrgId/{orgId}",produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<LicenseeDto>> getLicenseeByOrgId(@PathVariable(name = "orgId") String orgId);

    @GetMapping(value = "/iais-licensee/licenseeKeyApptPersonByLicId/{licenseeId}",produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<LicenseeKeyApptPersonDto>> getLicenseeKeyApptPersonDtoListByLicenseeId(@PathVariable("licenseeId") String licenseeId);

    @GetMapping(value = "/iais-licensee/getPersonByid/{id}",produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<LicenseeKeyApptPersonDto>> getPersonByid(@PathVariable(name = "id") String id);

    @PostMapping(path = "/iais-licensee/refreshLicensee", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<Void> updateLicence(@RequestBody LicenseeDto licenseeDto);

    @GetMapping(value = "/iais-licensee/licensee-by-user-info/{userAccountString}")
    FeignResponseEntity<LicenseeDto> getLicenseeByUserAccountInfo (@PathVariable("userAccountString") String userAccountString);
}
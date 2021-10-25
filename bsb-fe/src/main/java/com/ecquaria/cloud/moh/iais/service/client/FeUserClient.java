package com.ecquaria.cloud.moh.iais.service.client;


import com.ecquaria.cloud.moh.iais.common.dto.IaisApiResult;
import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenseeDto;
import com.ecquaria.cloud.moh.iais.common.dto.inbox.InterInboxUserDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.FeUserDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.FeUserQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.OrgUserRoleDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.OrganizationDto;
import com.ecquaria.cloudfeign.FeignConfiguration;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "iais-organization",configuration = FeignConfiguration.class,fallback = FeUserClientFallback.class)
public interface FeUserClient {
    @RequestMapping(path = "/iais-internet-user/getFeUserList",method = RequestMethod.POST, produces = { MediaType.APPLICATION_JSON_VALUE },
            consumes = {MediaType.APPLICATION_JSON_VALUE})
    FeignResponseEntity<SearchResult<FeUserQueryDto>> getFeUserList(SearchParam searchParam);

    @GetMapping(value = "/iais-internet-user/organization/{uen}",produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<OrganizationDto> findOrganizationByUen(@PathVariable("uen") String uen);

    @GetMapping(value = "/iais-internet-user/user-account-userid",produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<FeUserDto> getUserAccount(@RequestParam("id") String id);

    @GetMapping(value = "/iais-internet-user/{nric}/{idType}/uen-list/")
    FeignResponseEntity<List<String>> getUenListByIdAndType(@PathVariable("nric") String nric, @PathVariable("idType") String idType);

    @GetMapping(value = "/iais-internet-user/user-account/{nric}/{idType}")
    FeignResponseEntity<FeUserDto> getInternetUserByNricAndIdType(@PathVariable("nric") String nric, @PathVariable("idType") String idType);

    @RequestMapping(path = "/iais-internet-user/edit-user-account",method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<FeUserDto> editUserAccount(@RequestBody FeUserDto feUserDto);

    @RequestMapping(path = "/iais-internet-user/add-user-role",method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<OrgUserRoleDto> addUserRole(@RequestBody OrgUserRoleDto orgUserRoleDto);

    @GetMapping(path = "/iais-internet-user/user-account-orgid", produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<FeUserDto>> getAccountByOrgId(@RequestParam(value = "orgId")String orgId);

    @GetMapping(path = "/iais-internet-user/user-admin-account-orgid", produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<FeUserDto>> getAdminAccountByOrgId(@RequestParam(value = "orgId")String orgId);

    @PostMapping(path = "/iais-internet-user/user-account/", consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<OrganizationDto> createHalpAccount(@RequestBody OrganizationDto organizationDto);

    @GetMapping(value = "/iais-internet-user/organization/{uen}/user/{nric}",produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<FeUserDto> getUserByNricAndUen(@PathVariable(value = "uen") String uen, @PathVariable(value = "nric") String nric);

    @GetMapping(value = "/iais-internet-user/user-info/{userId}")
    FeignResponseEntity<InterInboxUserDto> findUserInfoByUserId(@PathVariable("userId")String UserId);

    @GetMapping(path = "/iais-licensee/licensee-by-id/{id}",produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<LicenseeDto> getLicenseeById(@PathVariable(name = "id") String id);

    @GetMapping(path = "/iais-internet-user/validate-singpass")
    FeignResponseEntity<IaisApiResult<Void>> validateSingpassAccount(@RequestParam(value = "idNo")String idNo, @RequestParam(value = "idType") String idType);

    @GetMapping(path = "/iais-licensee/getLicenseeNoUen",produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<LicenseeDto>> getLicenseeNoUen();

    @PostMapping(path = "/iais-internet-user/validate-pwd", consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<Boolean> validatePwd(@RequestBody  FeUserDto feUserDto);

    @GetMapping(path = "/iais-internet-user/not-exist/user-account/in/{orgId}")
    FeignResponseEntity<Boolean> isNotExistUserAccount(@PathVariable("orgId") String orgId);

    @GetMapping(path = "/iais-internet-user/uen-track/{uen}/{nricNumber}/permit-status/{isPermit}")
    FeignResponseEntity<Boolean> setPermitLoginStatusInUenTrack(@PathVariable("uen") String uen,
                                                             @PathVariable("nricNumber") String nricNumber,
                                                             @PathVariable("isPermit") Boolean isPermit);

    @GetMapping(path = "/iais-internet-user/expire/singpass/list", produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<String> getExpireSingPassList();
}
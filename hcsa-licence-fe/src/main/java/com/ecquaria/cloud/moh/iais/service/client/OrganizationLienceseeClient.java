package com.ecquaria.cloud.moh.iais.service.client;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicPremisesReqForInfoDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenseeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenseeIndividualDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenseeKeyApptPersonDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.FeUserDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.OrgGiroAccountInfoDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.OrgUserDto;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * OrganizationLienceseeClient
 *
 * @author caijing
 * @date 2020/1/15
 */

@FeignClient(name = "iais-organization", configuration = FeignConfiguration.class,
        fallback = OrganizationLienceseeClientFallback.class)
public interface OrganizationLienceseeClient {
    @GetMapping(value = "/iais-licensee/licenseeKeyApptPersonByUenNo/{uenNo}",produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<LicenseeKeyApptPersonDto>> getLicenseeKeyApptPersonDtoListByUen(@PathVariable("uenNo") String uenNo);

    @GetMapping(value = "/iais-licensee/licenseeKeyApptPersonByLicId/{licenseeId}",produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<LicenseeKeyApptPersonDto>> getLicenseeKeyApptPersonDtoListByLicenseeId(@PathVariable("licenseeId") String licenseeId);

    @GetMapping(value = "/iais-licensee/licenseeIndividual/{nric}",produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<LicenseeIndividualDto> getlicIndByNric(@PathVariable("nric") String nric);

    @GetMapping(path = "/iais-licensee/licensee-by-id/{id}",produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<LicenseeDto> getLicenseeById(@PathVariable(name = "id") String id);

    @GetMapping(path = "/iais-licensee/licenseeDto/{uenNo}",produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<LicenseeDto> getLicenseeByUenNo(@PathVariable(name = "uenNo") String uenNo);

    @GetMapping(value = "/iais-licensee/licensee-by-org-id/{orgId}")
    FeignResponseEntity<LicenseeDto> getLicenseeByOrgId(@PathVariable("orgId") String orgId);

    @RequestMapping(value = "/iais-orguser-be/users-by-ids",method = RequestMethod.POST,produces = { MediaType.APPLICATION_JSON_VALUE },
            consumes = {MediaType.APPLICATION_JSON_VALUE})
    FeignResponseEntity<List<OrgUserDto>> retrieveOrgUserAccount(@RequestBody List<String> ids);

    @PostMapping(value = "/hcsa-resForInfo-fe/licence-accept-rfi",consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<LicPremisesReqForInfoDto> acceptLicPremisesReqForInfo(@RequestBody LicPremisesReqForInfoDto licPremisesReqForInfoDto);

    @PostMapping(value = "/iais-orgUserRole/getAdminEmailAdd",produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<String>> getAdminEmailAdd(@RequestParam("orgId") String orgId);

    @PostMapping(value = "/iais-orgUserRole/getAdminOfficerEmailAdd",produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<String>> getAdminOfficerEmailAdd(@RequestParam("orgId") String orgId);

    @GetMapping(path = "/iais-internet-user/user-account-orgid", produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<FeUserDto>> getAccountByOrgId(@RequestParam(value = "orgId")String orgId);

    @GetMapping(path = "/iais-internet-user/user-info-list", produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<FeUserDto>> getFeUserDtoByLicenseeId(@RequestParam(value = "licenseeId") String licenseeId);

    @GetMapping(value = "/iais-licensee/licensee-by-id/{id}")
    FeignResponseEntity<LicenseeDto> getLicenseeDtoById (@PathVariable("id") String id);

    @RequestMapping(value = "/iais-orgUserRole/users-account/{id}",method = RequestMethod.GET,produces = { MediaType.APPLICATION_JSON_VALUE },
            consumes = {MediaType.APPLICATION_JSON_VALUE})
    FeignResponseEntity<OrgUserDto> retrieveOneOrgUserAccount(@PathVariable(value = "id") String user_id);
    @GetMapping(value = "/iais-internet-user/giro-account-info")
    FeignResponseEntity<List<OrgGiroAccountInfoDto>> getGiroAccByLicenseeId(@RequestParam(value = "licenseeId") String licenseeId);

    @GetMapping(value = "/iais-internet-user/organization/{uen}",produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<OrganizationDto> findOrganizationByUen(@PathVariable("uen") String uen);

    @GetMapping(value = "/iais-licensee/organizationDto/{licenseeId}",produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<OrganizationDto> getOrganizationDtoByLicenseeId(@PathVariable(name = "licenseeId") String licenseeId);
}

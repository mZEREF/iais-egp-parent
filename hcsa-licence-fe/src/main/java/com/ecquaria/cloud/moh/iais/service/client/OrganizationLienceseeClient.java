package com.ecquaria.cloud.moh.iais.service.client;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicPremisesReqForInfoDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenseeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenseeIndividualDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenseeKeyApptPersonDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.OrgUserDto;
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

    @RequestMapping(value = "/iais-orguser-be/users-by-ids",method = RequestMethod.POST,produces = { MediaType.APPLICATION_JSON_VALUE },
            consumes = {MediaType.APPLICATION_JSON_VALUE})
    FeignResponseEntity<List<OrgUserDto>> retrieveOrgUserAccount(@RequestBody List<String> ids);

    @PostMapping(value = "/hcsa-resForInfo-fe/licence-accept-rfi",consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<LicPremisesReqForInfoDto> acceptLicPremisesReqForInfo(@RequestBody LicPremisesReqForInfoDto licPremisesReqForInfoDto);

    @PostMapping(value = "/iais-orgUserRole/getAdminEmailAdd",produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<String>> getAdminEmailAdd(@RequestParam("orgId") String orgId);


}

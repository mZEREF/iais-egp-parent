package com.ecquaria.cloud.moh.iais.service.client;

import com.ecquaria.cloud.moh.iais.common.dto.arcaUen.GenerateUENDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenseeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenseeKeyApptPersonDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.OrgUserDto;
import com.ecquaria.cloudfeign.FeignConfiguration;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * TaskOrganizationClient
 *
 * @author suocheng
 * @date 12/4/2019
 */
@FeignClient(name = "iais-organization", configuration = FeignConfiguration.class,
        fallback = LicenseeClientFallback.class)
public interface LicenseeClient {
    @GetMapping(value = "/iais-licensee/licensee-by-id/{id}")
    FeignResponseEntity<LicenseeDto> getLicenseeDtoById (@PathVariable("id") String id);

    @GetMapping(value = "/iais-licensee/user-email-addrs/{licenseeId}")
    ResponseEntity<List<String>> getLicenseeEmails(@PathVariable(name = "licenseeId") String licenseeId);

    @PostMapping(value = "/iais-licensee/user-account/licenseeId/roleIds")
    ResponseEntity<List<OrgUserDto>> getLicenseeAccountByRolesAndLicenseeId(@RequestParam(name = "licenseeId") String licenseeId, @RequestBody List<String> roleIds);

    @GetMapping(value = "/iais-licensee/user-mobile-nos/{licenseeId}")
    ResponseEntity<List<String>> getLicenseeMobiles(@PathVariable(name = "licenseeId") String licenseeId);

    @PostMapping(value = "/iais-licensee/user-account/email-address", consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<String>> getEmailAddressListByLicenseeId(@RequestBody List<String> licenseeIdList);

    @GetMapping(value = "/iais-licensee/getPersonByid/{id}")
    FeignResponseEntity<List<LicenseeKeyApptPersonDto>> getPersonByid(@PathVariable(name = "id") String id);

    @PutMapping(path = "/iais-acraUen-fe/entity/{uen}")
    FeignResponseEntity<GenerateUENDto> getEntityByUEN(@PathVariable("uen") String uen);

    @GetMapping(path = "/iais-acraUen-fe/entity-info/{uen}")
    FeignResponseEntity<String> getEntityInfoByUEN(@PathVariable("uen") String uen);



    @PostMapping(value = "/imaginary/licensee/")
    FeignResponseEntity<Void> imaginaryLicenseeByOrgId(@RequestParam(value = "orgnId") String orgnId);
}

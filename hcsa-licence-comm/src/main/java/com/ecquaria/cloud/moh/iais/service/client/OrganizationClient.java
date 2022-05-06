package com.ecquaria.cloud.moh.iais.service.client;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenseeDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.FeUserDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.OrgGiroAccountInfoDto;
import com.ecquaria.cloud.moh.iais.service.callback.OrganizationClientFallback;
import com.ecquaria.cloudfeign.FeignConfiguration;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @Auther chenlei on 5/4/2022.
 */
@FeignClient(name = "iais-organization", configuration = FeignConfiguration.class, fallback = OrganizationClientFallback.class)
public interface OrganizationClient {

    @GetMapping(path = "/iais-licensee/user-info-list", produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<FeUserDto>> getFeUserDtoByLicenseeId(@RequestParam(value = "licenseeId") String licenseeId);

    @GetMapping(path = "/iais-licensee/licensee-by-id/{id}",produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<LicenseeDto> getLicenseeById(@PathVariable(name = "id") String id);

    @GetMapping(value = "/iais-internet-user/giro-account-info")
    FeignResponseEntity<List<OrgGiroAccountInfoDto>> getGiroAccByLicenseeId(@RequestParam(value = "licenseeId") String licenseeId);

}

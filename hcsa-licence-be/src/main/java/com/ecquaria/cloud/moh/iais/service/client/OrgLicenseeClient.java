package com.ecquaria.cloud.moh.iais.service.client;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenseeDto;
import com.ecquaria.cloudfeign.FeignConfiguration;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * OrgLicenseeClient
 *
 * @author junyu
 * @date 2019/12/21
 */
@FeignClient(name = "iais-organization", configuration = FeignConfiguration.class,
        fallback = OrgLicenseeClientFallback.class)
public interface OrgLicenseeClient {
    @GetMapping(value = "/iais-licensee/{id}",produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<LicenseeDto> getLicenseeDtoById (@PathVariable("id") String id);

}

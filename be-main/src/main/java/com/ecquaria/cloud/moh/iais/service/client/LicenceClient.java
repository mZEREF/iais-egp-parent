package com.ecquaria.cloud.moh.iais.service.client;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenceViewDto;
import com.ecquaria.cloudfeign.FeignConfiguration;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;


@FeignClient(name = "hcsa-licence",configuration = FeignConfiguration.class,fallback = LicenceInFallback.class)
public interface LicenceClient {

    @GetMapping(value = "/licence-app-id",produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<LicenceDto>  getLicenceByAppId(@RequestParam("appId") String appId);

    @GetMapping(value = "/hcsa-licence/licenceViewDto",produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<LicenceViewDto> getLicenceViewDtoByLicPremCorrId(@RequestParam(value ="licPremCorrId") String licPremCorrId);

    @GetMapping(value = "/hcsa-licence/licence/{licenceId}",produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<LicenceDto> getLicBylicId(@PathVariable(name="licenceId") String licenceId);

}
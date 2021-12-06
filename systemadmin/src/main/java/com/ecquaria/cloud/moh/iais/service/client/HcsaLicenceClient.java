package com.ecquaria.cloud.moh.iais.service.client;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenceDto;
import com.ecquaria.cloudfeign.FeignConfiguration;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author Wenkang
 * @date 2019/12/4 16:02
 */
@FeignClient(name = "hcsa-licence", configuration = FeignConfiguration.class,
        fallback = HcsaLicenceClientFallback.class)
public interface HcsaLicenceClient {
    @GetMapping(value = "/hcsa-key-personnel/getEmailByRole/{role}")
    FeignResponseEntity<List<String>> getEmailByRole(@PathVariable(name = "role") String role);

    @GetMapping(value = "/hcsa-key-personnel/getMobileByRole/{role}")
    FeignResponseEntity<List<String>> getMobileByRole(@PathVariable(name = "role") String role);

    @GetMapping(value = "/hcsa-licence/licence-dto-svc-name",produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<LicenceDto>> getLicenceDtosBySvcName(@RequestParam("svcName") String svcName);

    @GetMapping(value = "/lic-common/lic-licenseeId",produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<LicenceDto>> getActiveLicencesByLicenseeId(@RequestParam("licenseeId") String licenseeId);
}

package com.ecquaria.cloud.moh.iais.service.client;

/**
 * @author weilu
 * @date 2020/2/7 13:24
 */

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppInsRepDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.cessation.AppCessLicDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.cessation.AppCessMiscDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.withdrawn.WithdrawnDto;
import com.ecquaria.cloudfeign.FeignConfiguration;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "hcsa-application", configuration = {FeignConfiguration.class},
        fallback =CessationClientFallback.class)
public interface CessationClient {

    @PostMapping(value = "/appeal/application-cessation",consumes = MediaType.APPLICATION_JSON_VALUE,produces =MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<String>> saveCessation(@RequestBody List<AppCessMiscDto> appCessMiscDtos);

    @PostMapping(value = "/appeal/application-fe-withdrawal",consumes = MediaType.APPLICATION_JSON_VALUE,produces =MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<String> saveWithdrawn(@RequestBody WithdrawnDto withdrawnDto);

    @PostMapping(value = "/appeal/application-cessation-list",consumes = MediaType.APPLICATION_JSON_VALUE,produces =MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity <List<AppCessLicDto>> getCessationByLicIds(@RequestBody List<String> licIds);

    @GetMapping(value = "/appeal/listHciName",produces =MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<String>> listHciNames();
}

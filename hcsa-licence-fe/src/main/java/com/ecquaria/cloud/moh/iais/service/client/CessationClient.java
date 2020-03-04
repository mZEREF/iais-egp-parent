package com.ecquaria.cloud.moh.iais.service.client;

/**
 * @author weilu
 * @date 2020/2/7 13:24
 */

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppInsRepDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.cessation.AppCessMiscDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.withdrawn.WithdrawnDto;
import com.ecquaria.cloudfeign.FeignConfiguration;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "hcsa-application", configuration = {FeignConfiguration.class},
        fallback =CessationClientFallback.class)
        public interface CessationClient {
    @RequestMapping(path = "/application-number-grp-premiese/{appPremCorreId}",method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<AppInsRepDto> getAppCessationDto(@PathVariable("appNo") String appNo);

    @PostMapping(value = "/appeal/application-cessation",consumes = MediaType.APPLICATION_JSON_VALUE,produces =MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<String> saveCessation(@RequestBody List<AppCessMiscDto> appCessMiscDtos);

    @PostMapping(value = "/appeal/list-cessation-corrIds",consumes = MediaType.APPLICATION_JSON_VALUE,produces =MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<AppCessMiscDto>> getAppCessMiscDtosByCorrIds(@RequestBody List<String> corrIds);

    @PostMapping(value = "/appeal/application-cessation-update",consumes = MediaType.APPLICATION_JSON_VALUE,produces =MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<String> updateCessation(@RequestBody List<AppCessMiscDto> appCessMiscDtos);

    @PostMapping(value = "/appeal/application-fe-withdrawal",consumes = MediaType.APPLICATION_JSON_VALUE,produces =MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<String> saveWithdrawn(@RequestBody WithdrawnDto withdrawnDto);

    @GetMapping(value = "/appeal/application-cessation-list/{type}/{status}/{licId}")
    FeignResponseEntity<AppCessMiscDto> getCessationByLicId(@PathVariable(name = "type") String type, @PathVariable(name = "status") String status, @PathVariable(name = "licId") String licId);
}

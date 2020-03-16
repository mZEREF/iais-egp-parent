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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@FeignClient(name = "hcsa-application", configuration = {FeignConfiguration.class},
        fallback =CessationClientFallback.class)
        public interface CessationClient {
    @RequestMapping(path = "/application-number-grp-premiese/{appPremCorreId}",method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<AppInsRepDto> getAppCessationDto(@PathVariable("appNo") String appNo);

    @PostMapping(value = "/iais-cessation/application-cessation",consumes = MediaType.APPLICATION_JSON_VALUE,produces =MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<String> saveCessation(@RequestBody List<AppCessMiscDto> appCessMiscDtos);

    @PostMapping(value = "/appeal/list-cessation-corrIds",consumes = MediaType.APPLICATION_JSON_VALUE,produces =MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<AppCessMiscDto>> getAppCessMiscDtosByCorrIds(@RequestBody List<String> corrIds);

    @PostMapping(value = "/iais-cessation/application-cessation-update",consumes = MediaType.APPLICATION_JSON_VALUE,produces =MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<String> updateCessation(@RequestBody List<AppCessMiscDto> appCessMiscDtos);

    @PostMapping(value = "/application-fe-withdrawal",consumes = MediaType.APPLICATION_JSON_VALUE,produces =MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<Void> saveWithdrawn(@RequestBody WithdrawnDto withdrawnDto);

    @GetMapping(value = "iais-cessation/application-cessation-list/{type}/{status}/{licId}")
    FeignResponseEntity<AppCessMiscDto> getCessationByLicId(@PathVariable(name = "type") String type, @PathVariable(name = "status") String status, @PathVariable(name = "licId") String licId);

    @PostMapping(value = "iais-cessation/list-cessation-licIds",consumes = MediaType.APPLICATION_JSON_VALUE,produces =MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<String>> getlicIdToCessation(@RequestBody List<String> licIds);

}

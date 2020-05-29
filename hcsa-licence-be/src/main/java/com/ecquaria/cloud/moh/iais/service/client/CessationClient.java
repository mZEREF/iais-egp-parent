package com.ecquaria.cloud.moh.iais.service.client;

/**
 * @author weilu
 * @date 2020/2/7 13:24
 */

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.appeal.AppPremiseMiscDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppGrpPremisesDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppInsRepDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationGroupDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.cessation.AppCessMiscDto;
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
    @RequestMapping(path = "/application-number-grp-premiese/{appPremCorreId}",method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<AppInsRepDto> getAppCessationDto(@PathVariable("appNo") String appNo);

    @PostMapping(value = "/iais-cessation/application-cessation",consumes = MediaType.APPLICATION_JSON_VALUE,produces =MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<String>> saveCessation(@RequestBody List<AppCessMiscDto> appCessMiscDtos);

    @GetMapping(value = "/iais-cessation/application-cessation-list/{type}/{status}/{licId}")
    FeignResponseEntity<AppCessMiscDto> getCessationByLicId(@PathVariable(name = "type") String type, @PathVariable(name = "status") String status, @PathVariable(name = "licId") String licId);

    @PostMapping(value = "/iais-cessation/list-cessation-licIds",consumes = MediaType.APPLICATION_JSON_VALUE,produces =MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<String>> getlicIdToCessation(@RequestBody List<String> licIds);

    @GetMapping(value = "/iais-cessation/listHciName",produces =MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<String>> listHciNames();

    @GetMapping(value = "/iais-cessation/isCeasedResult",produces =MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<Boolean> isCeased(@RequestParam("licId") String licId);

    @GetMapping(value = "/iais-cessation/list-appGrp",produces =MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<ApplicationGroupDto>> listAppGrpForCess();

    @PostMapping(value = "/iais-cessation/list-cessation-corrIds",consumes = MediaType.APPLICATION_JSON_VALUE,produces =MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<AppCessMiscDto>> getAppCessMiscDtosByCorrIds(@RequestBody List<String> corrIds);

    @GetMapping(value = "/iais-cessation/appId-misc-cessation",produces =MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<AppPremiseMiscDto> getAppPremiseMiscDtoByAppId(@RequestParam("appId") String appId);

    @GetMapping(value = "/iais-cessation/appId-premise-cessation",produces =MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<AppGrpPremisesDto> getAppGrpPremisesDtoByAppId(@RequestParam("appId") String appId);

    @PostMapping(value = "/iais-cessation/list-can-ceased",consumes = MediaType.APPLICATION_JSON_VALUE,produces =MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<Boolean> listCanCeased(@RequestBody List<String> licIds);
}

package com.ecquaria.cloud.moh.iais.service.client;

/**
 * @author weilu
 * @date 2020/2/7 13:24
 */

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.appeal.AppPremiseMiscDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppGrpPremisesDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.cessation.AppCessLicDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.cessation.AppCessMiscDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.withdrawn.WithdrawnDto;
import com.ecquaria.cloudfeign.FeignConfiguration;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

@FeignClient(name = "hcsa-application", configuration = {FeignConfiguration.class},
        fallback =CessationClientFallback.class)
public interface CessationClient {

    @PostMapping(value = "/appeal/application-cessation",consumes = MediaType.APPLICATION_JSON_VALUE,produces =MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<String>> saveCessation(@RequestBody List<AppCessMiscDto> appCessMiscDtos);

    @PostMapping(value = "/appeal/application-fe-withdrawal",consumes = MediaType.APPLICATION_JSON_VALUE,produces =MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<String>> saveWithdrawn(@RequestBody List<WithdrawnDto> withdrawnDtoList);

    @PostMapping(value = "/appeal/application-cessation-list",consumes = MediaType.APPLICATION_JSON_VALUE,produces =MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity <List<AppCessLicDto>> getCessationByLicIds(@RequestBody List<String> licIds);

    @GetMapping(value = "/appeal/isCeasedResult",produces =MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<Boolean> isCeased(@RequestParam("licId") String licId);

    @GetMapping(value = "/appeal/appId-premise-cessation",produces =MediaType.APPLICATION_JSON_VALUE,consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<AppGrpPremisesDto> getAppGrpPremisesDtoByAppId(@RequestParam("appId") String appId);

    @PostMapping(value = "/appeal/list-can-ceased",consumes = MediaType.APPLICATION_JSON_VALUE,produces =MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<Map<String,Boolean>> listCanCeased(@RequestBody List<String> licIds);

    @GetMapping(value = "/appeal/appId-misc-list-cessation",produces =MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<AppPremiseMiscDto>> getAppPremiseMiscDtoListByAppId(@RequestParam("appId") String appId);

    @GetMapping(value = "/appeal/appId-misc-cessation",produces =MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<AppCessMiscDto> getAppMiscDtoByAppId(@RequestParam("appId") String appId);

    @GetMapping(value = "/appeal/app-baseNo",produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<ApplicationDto>> getAppByBaseAppNo(@RequestParam("appNo") String appNo);
}

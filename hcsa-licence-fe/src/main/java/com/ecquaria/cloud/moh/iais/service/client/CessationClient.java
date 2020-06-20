package com.ecquaria.cloud.moh.iais.service.client;

/**
 * @author weilu
 * @date 2020/2/7 13:24
 */

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppGrpPremisesDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppInsRepDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.cessation.AppCessLicDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.cessation.AppCessMiscDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.withdrawn.WithdrawnDto;
import com.ecquaria.cloudfeign.FeignConfiguration;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping(value = "/appeal/listHciName",produces =MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<String>> listHciNames();

    @GetMapping(value = "/appeal/isCeasedResult",produces =MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<Boolean> isCeased(@RequestParam("licId") String licId);

    @GetMapping(value = "/appeal/appId-premise-cessation",produces =MediaType.APPLICATION_JSON_VALUE,consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<AppGrpPremisesDto> getAppGrpPremisesDtoByAppId(@RequestParam("appId") String appId);

    @PostMapping(value = "/appeal/list-can-ceased",consumes = MediaType.APPLICATION_JSON_VALUE,produces =MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<Map<String,Boolean>> listCanCeased(@RequestBody List<String> licIds);

    @PostMapping(value = "/appeal/is-all-ceased", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<String>> isAllCeased(@RequestBody List<String> licIds);
}

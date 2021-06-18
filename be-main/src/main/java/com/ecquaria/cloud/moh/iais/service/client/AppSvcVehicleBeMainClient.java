package com.ecquaria.cloud.moh.iais.service.client;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcVehicleDto;
import com.ecquaria.cloudfeign.FeignConfiguration;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * @author Shicheng
 * @date 2021/6/8 10:16
 **/
@FeignClient(name = "hcsa-application", configuration = FeignConfiguration.class,
        fallback = AppSvcVehicleBeMainClientFallBack.class)
public interface AppSvcVehicleBeMainClient {

    @PostMapping(value = "/halp-be-svc-vehicle", consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<AppSvcVehicleDto>> createAppSvcVehicleDtoList(@RequestBody List<AppSvcVehicleDto> appSvcVehicleDtos);

    @PutMapping(value = "/halp-be-svc-vehicle", consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<AppSvcVehicleDto> updateAppSvcVehicleDto(@RequestBody AppSvcVehicleDto appSvcVehicleDto);

    @GetMapping(value = "/halp-be-svc-vehicle/svc-vehicles/{appPremCorrId}",produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<AppSvcVehicleDto>> getAppSvcVehicleDtoListByCorrId(@PathVariable(name = "appPremCorrId") String appPremCorrId);

    @GetMapping(value = "/svc-vehicles-status/{appPremCorrId}/{status}",produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<AppSvcVehicleDto>> getSvcVehicleDtoListByCorrIdStatus(@PathVariable(name = "appPremCorrId") String appPremCorrId,
                                                                                   @PathVariable(name = "status") String status);
}

package com.ecquaria.cloud.moh.iais.service.client;


import com.ecquaria.cloud.moh.iais.common.dto.hcsa.fee.HcsaFeeBundleItemDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.*;
import com.ecquaria.cloudfeign.FeignConfiguration;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Wenkang
 * @date 2019/12/4 15:27
 */
@FeignClient(name = "hcsa-config", configuration = FeignConfiguration.class,
        fallback = HcsaConfigClientFallback.class)
public interface HcsaConfigClient {
    @GetMapping(value = "/iais-hcsa-service/active-service",produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<HcsaServiceDto>> getActiveServices();

    @GetMapping(value = "/iais-hcsa-service/all-service", produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<HcsaServiceDto>> allHcsaService();

    @PostMapping(value = "/iais-hcsa-service/service-by-name", consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<HcsaServiceDto>> getHcsaServiceByNames(@RequestBody List<String> svcNames);
    @GetMapping(value = "/iais-hcsa-fee/active-bundle-item", consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<HcsaFeeBundleItemDto>> getActiveBundleDtoList();
    @GetMapping(path = "/iais-hcsa-service/servicedto-by-name/{svcName}")
    FeignResponseEntity<HcsaServiceDto> getServiceDtoByName(@PathVariable(name = "svcName") String svcName);

    @PostMapping(value = "/iais-hcsa-service/hcsaSvcSpecifiedCorrelationDtos", consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<HcsaSvcSpecifiedCorrelationDto>> getHcsaSvcSpecifiedCorrelationDtos(@RequestBody String svcCode,String premisesType);
}
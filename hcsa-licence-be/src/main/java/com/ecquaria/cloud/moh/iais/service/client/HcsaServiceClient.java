package com.ecquaria.cloud.moh.iais.service.client;

/**
 * @author Shicheng
 * @date 2019/11/26 13:36
 **/

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceDto;
import com.ecquaria.cloudfeign.FeignConfiguration;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "hcsa-config", configuration = FeignConfiguration.class,
        fallback = HcsaServiceClientFallback.class)
public interface HcsaServiceClient {
    @GetMapping(path = "/iais-hcsa-service/one-of-hcsa-service/{serviceId}", produces = { MediaType.APPLICATION_JSON_VALUE },
            consumes = {MediaType.APPLICATION_JSON_VALUE})
    FeignResponseEntity<HcsaServiceDto> getHcsaServiceDtoByServiceId(@PathVariable(value = "serviceId") String serviceId);
}

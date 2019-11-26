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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(name = "hcsa-config", configuration = FeignConfiguration.class,
        fallback = HcsaServiceClientFallback.class)
public interface HcsaServiceClient {
    @RequestMapping(path = "/iais-hcsa-service/one-of-hcsa-service/{serviceId}",method = RequestMethod.POST, produces = { MediaType.APPLICATION_JSON_VALUE },
            consumes = {MediaType.APPLICATION_JSON_VALUE}, value = "{serviceId}")
    FeignResponseEntity<HcsaServiceDto> getHcsaServiceDtoByServiceId(@PathVariable String serviceId);
}

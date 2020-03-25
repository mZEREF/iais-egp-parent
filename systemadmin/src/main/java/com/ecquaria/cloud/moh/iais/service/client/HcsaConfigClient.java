package com.ecquaria.cloud.moh.iais.service.client;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcPersonnelDto;
import com.ecquaria.cloudfeign.FeignConfiguration;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

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

    @RequestMapping(path = "/iais-hcsa-service/personnel-service",method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<HcsaSvcPersonnelDto>> getServiceType(@RequestParam("serviceId") String serviceId, @RequestParam("status") String status);


}

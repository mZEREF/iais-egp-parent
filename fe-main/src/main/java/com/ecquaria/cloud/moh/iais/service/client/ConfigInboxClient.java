package com.ecquaria.cloud.moh.iais.service.client;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceDto;
import com.ecquaria.cloudfeign.FeignConfiguration;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * @Author: Hc
 * @Program: iais-egp
 * @Create: 2019-12-20 16:30
 **/

@FeignClient(name = "hcsa-config",configuration = FeignConfiguration.class,fallback = ConfigInboxFallBack.class)
public interface ConfigInboxClient {

    @RequestMapping(path = "/iais-hcsa-service/{serviceId}",method = RequestMethod.GET)
    FeignResponseEntity<String> getServiceNameById(@PathVariable("serviceId")String serviceId);

    @GetMapping(value = "/iais-hcsa-service/active-service",produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<HcsaServiceDto>> getActiveServices();

}

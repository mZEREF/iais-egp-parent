package com.ecquaria.cloud.moh.iais.service.client;

import com.ecquaria.cloudfeign.FeignResponseEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.FeignClientsConfiguration;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @Author: Hc
 * @Program: iais-egp
 * @Create: 2019-12-20 16:30
 **/

@FeignClient(name = "hcsa-config",configuration = FeignClientsConfiguration.class,fallback = ConfigInboxFallBack.class)
public interface ConfigInboxClient {

    @RequestMapping(path = "/iais-hcsa-service/{serviceId}",method = RequestMethod.GET)
    FeignResponseEntity<String> getServiceNameById(@PathVariable("serviceId")String serviceId);

}

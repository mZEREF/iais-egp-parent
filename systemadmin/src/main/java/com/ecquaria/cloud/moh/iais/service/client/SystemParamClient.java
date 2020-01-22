package com.ecquaria.cloud.moh.iais.service.client;

import com.ecquaria.cloudfeign.FeignConfiguration;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * SystemParamClient
 *
 * @author Jinhua
 * @date 2019/11/23 16:15
 */
@FeignClient(name = "iais-config", configuration = FeignConfiguration.class,
        fallback = SystemParamClientFallback.class)
public interface SystemParamClient {
    @RequestMapping(path = "/actuator/bus-refresh",method = RequestMethod.POST,
            produces = {MediaType.APPLICATION_JSON_VALUE}, consumes = {MediaType.APPLICATION_JSON_VALUE})
    FeignResponseEntity<String> refreshConfiguration();

}

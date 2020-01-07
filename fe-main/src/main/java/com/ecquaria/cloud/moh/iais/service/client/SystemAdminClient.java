package com.ecquaria.cloud.moh.iais.service.client;

import com.ecquaria.cloudfeign.FeignConfiguration;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;


@FeignClient(name = "system-admin", configuration = FeignConfiguration.class,
        fallback = SystemAdminClientFallback.class)
public interface SystemAdminClient  {
    @RequestMapping(path = "/application-number/{type}")
    FeignResponseEntity<String> applicationNumber(@PathVariable(name = "type") String applicationType);

}

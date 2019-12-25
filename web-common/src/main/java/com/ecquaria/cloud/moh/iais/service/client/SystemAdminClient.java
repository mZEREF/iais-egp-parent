package com.ecquaria.cloud.moh.iais.service.client;

import com.ecquaria.cloudfeign.FeignConfiguration;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * SystemParamClient
 *
 * @author Jinhua
 * @date 2019/11/23 16:15
 */
@FeignClient(name = "system-admin", configuration = FeignConfiguration.class,
        fallback = SystemAdminClientFallback.class)
public interface SystemAdminClient {
    @RequestMapping(path = "/system-parameter/new-sequence-id",method = RequestMethod.GET)
    FeignResponseEntity<String> getSeqId();

}

package com.ecquaria.cloud.moh.iais.service.client;

import com.ecquaria.cloudfeign.FeignConfiguration;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * SystemAdminClient
 *
 * @author suocheng
 * @date 12/19/2019
 */
@FeignClient(name = "system-admin", configuration = FeignConfiguration.class,
        fallback = SystemAdminClientFallback.class)
public interface SystemAdminClient {
    @GetMapping(value = "/system-parameter/new-sequence-id")
    FeignResponseEntity<String> getSeqId();
}

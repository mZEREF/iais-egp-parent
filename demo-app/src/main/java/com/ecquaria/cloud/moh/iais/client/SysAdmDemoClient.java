package com.ecquaria.cloud.moh.iais.client;

import com.ecquaria.cloudfeign.FeignConfiguration;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * SysAdmDemoClient
 *
 * @author Jinhua
 * @date 2020/1/8 14:47
 */
@FeignClient(name = "system-admin", configuration = FeignConfiguration.class,
        fallback = SysAdmDemoClientFallback.class)
public interface SysAdmDemoClient {
    @GetMapping(value = "/system-parameter/new-sequence-id")
    FeignResponseEntity<String> getSeqId();
}

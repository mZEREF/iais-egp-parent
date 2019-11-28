package com.ecquaria.cloud.moh.iais.service.client;

import com.ecquaria.cloudfeign.FeignConfiguration;
import org.springframework.cloud.netflix.feign.FeignClient;

/**
 * ApplicationClient
 *
 * @author suocheng
 * @date 11/26/2019
 */
@FeignClient(name = "iais-application", configuration = FeignConfiguration.class,
        fallback = ApplicationClientFallback.class)
public interface ApplicationClient {

}

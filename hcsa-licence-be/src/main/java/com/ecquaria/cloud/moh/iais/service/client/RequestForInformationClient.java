package com.ecquaria.cloud.moh.iais.service.client;

import com.ecquaria.cloudfeign.FeignConfiguration;
import org.springframework.cloud.netflix.feign.FeignClient;

/**
 * RequestForInformationClient
 *
 * @author junyu
 * @date 2019/12/18
 */
@FeignClient(name = "hcsa-application", configuration = FeignConfiguration.class,
        fallback = RequestForInformationClientFallback.class)
public interface RequestForInformationClient {

}

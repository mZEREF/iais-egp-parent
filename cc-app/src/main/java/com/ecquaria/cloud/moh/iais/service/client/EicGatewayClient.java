package com.ecquaria.cloud.moh.iais.service.client;

import com.ecquaria.cloudfeign.FeignConfiguration;
import org.springframework.cloud.netflix.feign.FeignClient;

/**
 * EicGatewayClient
 *
 * @author Jinhua
 * @date 2019/12/3 17:33
 */
@FeignClient(value = "eicgate", configuration = FeignConfiguration.class,
        fallback = ComSystemAdminClientFallback.class)
public interface EicGatewayClient {

}

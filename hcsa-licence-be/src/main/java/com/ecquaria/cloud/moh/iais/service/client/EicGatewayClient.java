package com.ecquaria.cloud.moh.iais.service.client;

import com.ecquaria.cloud.moh.iais.config.FeignMultipartConfig;
import org.springframework.cloud.netflix.feign.FeignClient;

/**
 * @author Wenkang
 * @date 2019/12/28 10:33
 */
@FeignClient(value = "eicgate", url="${iais.intra.gateway.url}", configuration = {FeignMultipartConfig.class},
        fallback = ComSystemAdminClientFallback.class)
public interface EicGatewayClient {
}

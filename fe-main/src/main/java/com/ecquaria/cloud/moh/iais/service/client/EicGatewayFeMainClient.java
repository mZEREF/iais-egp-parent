package com.ecquaria.cloud.moh.iais.service.client;


import com.ecquaria.cloudfeign.FeignConfiguration;
import org.springframework.cloud.netflix.feign.FeignClient;

@FeignClient(value = "eicgate", url="${iais.inter.gateway.url}", configuration = {FeignConfiguration.class},
        fallback = EicGatewayFeMainClient.class)
public interface EicGatewayFeMainClient {
}

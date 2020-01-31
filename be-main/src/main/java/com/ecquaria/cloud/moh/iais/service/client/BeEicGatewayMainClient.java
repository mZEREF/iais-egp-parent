package com.ecquaria.cloud.moh.iais.service.client;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloudfeign.FeignConfiguration;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * EicGatewayClient
 *
 * @author suocheng
 * @date 2019/12/14 17:33
 */
@FeignClient(value = "eicgate", url="${iais.intra.gateway.url}", configuration = {FeignConfiguration.class},
        fallback = BeEicGatewayMainClientFallback.class)
public interface BeEicGatewayMainClient {
     @RequestMapping(value = "/v1/iais-application/",method = RequestMethod.PUT,consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<ApplicationDto> updateApplication(@RequestBody ApplicationDto applicationDto,
                                                          @RequestHeader("date") String date,
                                                          @RequestHeader("authorization") String authorization,
                                                          @RequestHeader("date-Secondary") String dateSec,
                                                          @RequestHeader("authorization-Secondary") String authorizationSec);


}

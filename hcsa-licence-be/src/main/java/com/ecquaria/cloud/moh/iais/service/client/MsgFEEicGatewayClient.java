package com.ecquaria.cloud.moh.iais.service.client;

import com.ecquaria.cloud.moh.iais.common.dto.inbox.InterMessageDto;
import com.ecquaria.cloud.moh.iais.config.FeignMultipartConfig;
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
@FeignClient(value = "eicgate", url="${iais.msg.url}", configuration = {FeignMultipartConfig.class},
        fallback = MsgFEEicGatewayClientFallback.class)
public interface MsgFEEicGatewayClient {

    @RequestMapping(value = "/message",method = RequestMethod.PUT,consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<InterMessageDto> saveInboxMessage( @RequestBody InterMessageDto interInboxDto,
                                                             @RequestHeader("date") String date,
                                                             @RequestHeader("authorization") String authorization);

}

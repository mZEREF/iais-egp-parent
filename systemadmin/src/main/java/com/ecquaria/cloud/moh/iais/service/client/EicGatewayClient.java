package com.ecquaria.cloud.moh.iais.service.client;

import com.ecquaria.cloud.moh.iais.common.dto.message.MessageDto;
import com.ecquaria.cloud.moh.iais.common.dto.parameter.SystemParameterDto;
import com.ecquaria.cloudfeign.FeignConfiguration;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

/**
 * @author: yichen
 * @date time:2/25/2020 1:08 PM
 * @description:
 */
@FeignClient(value = "eicgate", url="${iais.intra.gateway.url}", configuration = {FeignConfiguration.class},
		fallback = EicGatewayClientFallBack.class)
public interface EicGatewayClient {
	@PutMapping(value = "/v1/sys-params",consumes = MediaType.APPLICATION_JSON_VALUE)
	FeignResponseEntity<String> saveSystemParameterFe(@RequestBody SystemParameterDto systemParameterDto,
	                                                      @RequestHeader("date") String date,
	                                                      @RequestHeader("authorization") String authorization,
	                                                      @RequestHeader("date-Secondary") String dateSec,
	                                                      @RequestHeader("authorization-Secondary") String authorizationSec);

	@PostMapping(value = "/v1/message-configs",consumes = MediaType.APPLICATION_JSON_VALUE)
	FeignResponseEntity<MessageDto> syncMessageToFe(@RequestBody MessageDto messageDto,
															  @RequestHeader("date") String date,
															  @RequestHeader("authorization") String authorization,
															  @RequestHeader("date-Secondary") String dateSec,
															  @RequestHeader("authorization-Secondary") String authorizationSec);
}

package com.ecquaria.cloud.moh.iais.service.client;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.parameter.SystemParameterDto;
import com.ecquaria.cloudfeign.FeignConfiguration;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
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
	@PutMapping(value = "/iais/intra-out-dev/api/v1/sys-params",consumes = MediaType.APPLICATION_JSON_VALUE)
	FeignResponseEntity<ApplicationDto> saveSystemParameterFe(@RequestBody SystemParameterDto systemParameterDto,
	                                                      @RequestHeader("date") String date,
	                                                      @RequestHeader("authorization") String authorization,
	                                                      @RequestHeader("date-Secondary") String dateSec,
	                                                      @RequestHeader("authorization-Secondary") String authorizationSec);
}

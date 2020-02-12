package com.ecquaria.cloud.moh.iais.client;

import com.ecquaria.cloud.moh.iais.common.dto.parameter.SystemParameterDto;
import com.ecquaria.cloudfeign.FeignConfiguration;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

/**
 * @author: yichen
 * @date time:2/8/2020 12:07 PM
 * @description:
 */

@FeignClient(name = "system-admin", configuration = FeignConfiguration.class,
		fallback = IaisSystemClientFallback.class)
public interface IaisSystemClient {

	@GetMapping(path = "/system-parameter/results", produces = MediaType.APPLICATION_JSON_VALUE)
	FeignResponseEntity<List<SystemParameterDto>> receiveAllSystemParam();

}

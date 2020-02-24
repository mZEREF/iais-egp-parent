package com.ecquaria.cloud.moh.iais.service.client;

import com.ecquaria.cloudfeign.FeignConfiguration;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * @author: yichen
 * @date time:2/24/2020 2:49 PM
 * @description:
 */
@FeignClient(name = "iais-config",  configuration = FeignConfiguration.class,
		fallback = ConfigClientFallBack.class)
public interface ConfigClient {
	@PostMapping(value = "/actuator/bus-refresh", consumes = MediaType.APPLICATION_JSON_VALUE)
	FeignResponseEntity<Void> refreshConfig();
}

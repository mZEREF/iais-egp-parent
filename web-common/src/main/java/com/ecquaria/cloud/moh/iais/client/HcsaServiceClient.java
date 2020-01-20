package com.ecquaria.cloud.moh.iais.client;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceDto;
import com.ecquaria.cloudfeign.FeignConfiguration;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

/**
 * @author: yichen
 * @date time:1/20/2020 1:11 PM
 * @description:
 */

@FeignClient(name = "hcsa-config", configuration = FeignConfiguration.class,
		fallback = HcsaServiceClientFallBack.class)
public interface HcsaServiceClient {
	@GetMapping(value = "/iais-hcsa-service/active-service",produces = MediaType.APPLICATION_JSON_VALUE)
	FeignResponseEntity<List<HcsaServiceDto>> getActiveServices();
}

package com.ecquaria.cloud.moh.iais.service.client;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceSubTypeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcSpePremisesTypeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcSpecifiedCorrelationDto;
import com.ecquaria.cloudfeign.FeignConfiguration;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

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

	@GetMapping(path = "/iais-hcsa-service/one-of-hcsa-service/{serviceId}", produces = { MediaType.APPLICATION_JSON_VALUE },
			consumes = {MediaType.APPLICATION_JSON_VALUE})
	FeignResponseEntity<HcsaServiceDto> getHcsaServiceDtoByServiceId(@PathVariable(value = "serviceId") String serviceId);

	@GetMapping(value = "/iais-hcsa-service/hcsaSvcSpecifiedCorrelationDtos", produces = MediaType.APPLICATION_JSON_VALUE)
	FeignResponseEntity<List<HcsaSvcSpecifiedCorrelationDto>> getHcsaSvcSpecifiedCorrelationDtos(@RequestParam(value = "svcName", required = false) String svcName,
																								 @RequestParam(value = "serviceId", required = false) String serviceId,
																								 @RequestParam(value = "premisesType", required = false) String premisesType);

	@PostMapping(value = "/iais-hcsa-service/hcsaServiceSubTypeDtos-ids", consumes = MediaType.APPLICATION_JSON_VALUE)
	FeignResponseEntity<List<HcsaServiceSubTypeDto>> getHcsaServiceSubTypeDtosByIds(@RequestBody List<String> ids);

	@PostMapping(value = "/iais-hcsa-service/hcsa-service-code", consumes = MediaType.APPLICATION_JSON_VALUE)
	FeignResponseEntity<List<HcsaServiceDto>> getHcsaServiceDtoByCode(@RequestBody List<String> code);

	@GetMapping(path = "/iais-hcsa-service/hcsaSvcSpePremisesTypeDtos", produces = { MediaType.APPLICATION_JSON_VALUE },
			consumes = {MediaType.APPLICATION_JSON_VALUE})
	FeignResponseEntity<List<HcsaSvcSpePremisesTypeDto>> getHcsaSvcSpePremisesTypeDtos(@RequestParam(value = "svcName", required = false) String svcName,
																					   @RequestParam(value = "serviceId", required = false) String serviceId);
}

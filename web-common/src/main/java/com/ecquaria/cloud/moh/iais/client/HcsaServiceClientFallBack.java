package com.ecquaria.cloud.moh.iais.client;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceDto;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import java.util.List;
import org.springframework.http.HttpHeaders;

/**
 * @author: yichen
 * @date time:1/20/2020 1:11 PM
 * @description:
 */

public class HcsaServiceClientFallBack implements HcsaServiceClient {
	@Override
	public FeignResponseEntity<List<HcsaServiceDto>> getActiveServices() {
		FeignResponseEntity entity = new FeignResponseEntity<>();
		HttpHeaders headers = new HttpHeaders();
		entity.setHeaders(headers);
		return entity;
	}

	@Override
	public FeignResponseEntity<HcsaServiceDto> getHcsaServiceDtoByServiceId(String serviceId) {
		FeignResponseEntity entity = new FeignResponseEntity<>();
		HttpHeaders headers = new HttpHeaders();
		entity.setHeaders(headers);
		return entity;
	}
}

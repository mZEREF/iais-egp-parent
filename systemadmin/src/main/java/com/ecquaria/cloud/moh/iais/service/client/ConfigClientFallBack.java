package com.ecquaria.cloud.moh.iais.service.client;

import com.ecquaria.cloudfeign.FeignResponseEntity;
import org.springframework.http.HttpHeaders;

/**
 * @author: yichen
 * @date time:2/24/2020 2:50 PM
 * @description:
 */

public class ConfigClientFallBack implements ConfigClient {

	@Override
	public FeignResponseEntity<Void> refreshConfig() {
		FeignResponseEntity entity = new FeignResponseEntity<>();
		HttpHeaders headers = new HttpHeaders();
		entity.setHeaders(headers);
		return entity;
	}
}

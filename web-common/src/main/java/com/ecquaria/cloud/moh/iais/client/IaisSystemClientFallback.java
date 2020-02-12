package com.ecquaria.cloud.moh.iais.client;

import com.ecquaria.cloud.moh.iais.common.dto.parameter.SystemParameterDto;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import org.springframework.http.HttpHeaders;

import java.util.List;

/**
 * @author: yichen
 * @date time:2/8/2020 12:07 PM
 * @description:
 */

public class IaisSystemClientFallback implements IaisSystemClient {

	@Override
	public FeignResponseEntity<List<SystemParameterDto>> receiveAllSystemParam() {
		FeignResponseEntity entity = new FeignResponseEntity<>();
		HttpHeaders headers = new HttpHeaders();
		entity.setHeaders(headers);
		return entity;
	}
}

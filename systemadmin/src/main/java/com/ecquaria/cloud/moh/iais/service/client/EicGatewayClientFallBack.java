package com.ecquaria.cloud.moh.iais.service.client;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.parameter.SystemParameterDto;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import org.springframework.http.HttpHeaders;

/**
 * @author: yichen
 * @date time:2/25/2020 1:08 PM
 * @description:
 */

public class EicGatewayClientFallBack implements EicGatewayClient {
	@Override
	public FeignResponseEntity<ApplicationDto> saveSystemParameterFe(SystemParameterDto systemParameterDto, String date, String authorization, String dateSec, String authorizationSec) {
		FeignResponseEntity entity = new FeignResponseEntity<>();
		HttpHeaders headers = new HttpHeaders();
		entity.setHeaders(headers);
		return entity;
	}
}

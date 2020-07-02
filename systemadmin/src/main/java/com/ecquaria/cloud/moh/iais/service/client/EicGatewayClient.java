package com.ecquaria.cloud.moh.iais.service.client;

import com.ecquaria.cloud.moh.iais.common.dto.message.MessageDto;
import com.ecquaria.cloud.moh.iais.common.dto.parameter.SystemParameterDto;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 * @author: yichen
 * @date time:2/25/2020 1:08 PM
 * @description:
 */
@Component
public class EicGatewayClient {
	@Autowired
	private RestTemplate restTemplate;
	@Value("${iais.intra.gateway.url}")
	private String gateWayUrl;

	public FeignResponseEntity<String> saveSystemParameterFe(SystemParameterDto systemParameterDto, String date,
	                                                      String authorization, String dateSec, String authorizationSec) {
		HttpHeaders header = IaisEGPHelper.getHttpHeadersForEic(MediaType.APPLICATION_JSON, date, authorization,
				dateSec, authorizationSec);
		HttpEntity<SystemParameterDto> jsonPart = new HttpEntity<>(systemParameterDto, header);
		ResponseEntity response = restTemplate.exchange(restTemplate + "/v1/sys-params", HttpMethod.PUT,
				jsonPart, String.class);
		FeignResponseEntity<String> resEnt = IaisEGPHelper.genFeignRespFromResp(response);

		return resEnt;
	}

	FeignResponseEntity<MessageDto> syncMessageToFe(MessageDto messageDto, String date, String authorization,
												  String dateSec, String authorizationSec) {
		HttpHeaders header = IaisEGPHelper.getHttpHeadersForEic(MediaType.APPLICATION_JSON, date, authorization,
				dateSec, authorizationSec);
		HttpEntity<MessageDto> jsonPart = new HttpEntity<>(messageDto, header);
		ResponseEntity response = restTemplate.exchange(restTemplate + "/v1/message-configs", HttpMethod.POST,
				jsonPart, String.class);
		FeignResponseEntity<MessageDto> resEnt = IaisEGPHelper.genFeignRespFromResp(response);

		return resEnt;
	}
}

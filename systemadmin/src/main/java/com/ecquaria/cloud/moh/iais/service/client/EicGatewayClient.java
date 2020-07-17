package com.ecquaria.cloud.moh.iais.service.client;

import com.ecquaria.cloud.moh.iais.common.dto.inbox.InterMessageDto;
import com.ecquaria.cloud.moh.iais.common.dto.message.MessageDto;
import com.ecquaria.cloud.moh.iais.common.dto.parameter.SystemParameterDto;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

/**
 * @author: yichen
 * @date time:2/25/2020 1:08 PM
 * @description:
 */
@Component
public class EicGatewayClient {
	@Value("${iais.intra.gateway.url}")
	private String gateWayUrl;

	public FeignResponseEntity<String> saveSystemParameterFe(SystemParameterDto systemParameterDto, String date,
	                                                      String authorization, String dateSec, String authorizationSec) {
		return IaisEGPHelper.callEicGatewayWithBody(gateWayUrl + "/v1/sys-params", HttpMethod.PUT, systemParameterDto,
				MediaType.APPLICATION_JSON, date, authorization,
				dateSec, authorizationSec, String.class);
	}

	public FeignResponseEntity<MessageDto> syncMessageToFe(MessageDto messageDto, String date, String authorization,
												  String dateSec, String authorizationSec) {
		return IaisEGPHelper.callEicGatewayWithBody(gateWayUrl + "/v1/message-configs", HttpMethod.POST, messageDto,
				MediaType.APPLICATION_JSON, date, authorization,
				dateSec, authorizationSec, MessageDto.class);
	}

	public FeignResponseEntity<InterMessageDto> saveInboxMessage(InterMessageDto interInboxDto,
																 String date, String authorization, String dateSec, String authorizationSec) {
		return IaisEGPHelper.callEicGatewayWithBody(gateWayUrl + "/v1/iais-inter-inbox-message", HttpMethod.POST, interInboxDto,
				MediaType.APPLICATION_JSON, date, authorization, dateSec, authorizationSec, InterMessageDto.class);
	}
}

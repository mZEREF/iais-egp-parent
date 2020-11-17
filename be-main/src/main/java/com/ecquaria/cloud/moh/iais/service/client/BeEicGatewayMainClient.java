package com.ecquaria.cloud.moh.iais.service.client;

import com.ecquaria.cloud.moh.iais.common.dto.application.AppReturnFeeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.inbox.InterMessageDto;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * EicGatewayClient
 *
 * @author suocheng
 * @date 2019/12/14 17:33
 */
@Component
public class BeEicGatewayMainClient {
    @Value("${iais.intra.gateway.url}")
    private String gateWayUrl;

    public FeignResponseEntity<ApplicationDto> updateApplication(ApplicationDto applicationDto,
                                                          String date, String authorization, String dateSec,
                                                          String authorizationSec) {
         return IaisEGPHelper.callEicGatewayWithBody(gateWayUrl + "/v1/iais-application", HttpMethod.PUT, applicationDto,
                 MediaType.APPLICATION_JSON, date, authorization, dateSec, authorizationSec, ApplicationDto.class);
     }

    public FeignResponseEntity<InterMessageDto> saveInboxMessage(InterMessageDto interInboxDto,
                                                          String date, String authorization, String dateSec,
                                                          String authorizationSec) {
        return IaisEGPHelper.callEicGatewayWithBody(gateWayUrl + "/v1/iais-inter-inbox-message", HttpMethod.POST, interInboxDto,
                MediaType.APPLICATION_JSON, date, authorization, dateSec, authorizationSec, InterMessageDto.class);
    }

    public FeignResponseEntity<List> doStripeRefunds(List<AppReturnFeeDto> appReturnFeeDtos,
                                                     String date, String authorization, String dateSec,
                                                     String authorizationSec) {
        return IaisEGPHelper.callEicGatewayWithBodyForList(gateWayUrl + "/v1/stripe-refund", HttpMethod.POST, appReturnFeeDtos,
                MediaType.APPLICATION_JSON, date, authorization, dateSec, authorizationSec, List.class);
    }
}

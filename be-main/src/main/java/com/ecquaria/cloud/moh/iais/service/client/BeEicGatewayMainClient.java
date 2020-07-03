package com.ecquaria.cloud.moh.iais.service.client;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.inbox.InterMessageDto;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * EicGatewayClient
 *
 * @author suocheng
 * @date 2019/12/14 17:33
 */
public class BeEicGatewayMainClient {
    @Value("${iais.intra.gateway.url}")
    private String gateWayUrl;

    public FeignResponseEntity<ApplicationDto> updateApplication(ApplicationDto applicationDto,
                                                          String date, String authorization, String dateSec,
                                                          String authorizationSec) {
         return IaisEGPHelper.callEicGateway(gateWayUrl + "/v1/iais-application/", HttpMethod.PUT, applicationDto,
                 MediaType.APPLICATION_JSON, date, authorization, dateSec, authorizationSec, ApplicationDto.class);
     }

    @RequestMapping(value = "/v1/iais-inter-inbox-message/",method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE)
    public FeignResponseEntity<InterMessageDto> saveInboxMessage(InterMessageDto interInboxDto,
                                                          String date, String authorization, String dateSec,
                                                          String authorizationSec) {
        return IaisEGPHelper.callEicGateway(gateWayUrl + "/v1/iais-inter-inbox-message/", HttpMethod.POST, interInboxDto,
                MediaType.APPLICATION_JSON, date, authorization, dateSec, authorizationSec, InterMessageDto.class);
    }

}

package com.ecquaria.cloud.moh.iais.service.client;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.inbox.InterMessageDto;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import org.springframework.http.HttpHeaders;

/**
 * FEEicGatewayClientFallback
 *
 * @author suocheng
 * @date 12/19/2019
 */

public class BeEicGatewayMainClientFallback implements BeEicGatewayMainClient {


    @Override
    public FeignResponseEntity<ApplicationDto> updateApplication(ApplicationDto applicationDto, String date, String authorization, String dateSec, String authorizationSec) {
        FeignResponseEntity entity = new FeignResponseEntity<>();
        HttpHeaders headers = new HttpHeaders();
        entity.setHeaders(headers);
        return entity;
    }

    @Override
    public FeignResponseEntity<InterMessageDto> saveInboxMessage(InterMessageDto interInboxDto, String date, String authorization, String dateSec, String authorizationSec) {
        FeignResponseEntity entity = new FeignResponseEntity<>();
        HttpHeaders headers = new HttpHeaders();
        entity.setHeaders(headers);
        return entity;
    }


}

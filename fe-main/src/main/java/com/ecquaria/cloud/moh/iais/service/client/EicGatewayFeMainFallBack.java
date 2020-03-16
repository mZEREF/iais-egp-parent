package com.ecquaria.cloud.moh.iais.service.client;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.recall.RecallApplicationDto;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import org.springframework.http.HttpHeaders;

/**
 * @Author: Hc
 * @Program: iais-egp
 * @Create: 2020-03-12 11:35
 **/
public class EicGatewayFeMainFallBack implements EicGatewayFeMainClient{
    @Override
    public FeignResponseEntity<Boolean> updateApplicationStatus(RecallApplicationDto recallApplicationDto, String date, String authorization, String dateSec, String authorizationSec) {
        FeignResponseEntity entity = new FeignResponseEntity<>();
        HttpHeaders headers = new HttpHeaders();
        entity.setHeaders(headers);
        return entity;
    }

    @Override
    public FeignResponseEntity<RecallApplicationDto> recallAppChangeTask(RecallApplicationDto recallApplicationDto, String date, String authorization, String dateSec, String authorizationSec) {
        FeignResponseEntity entity = new FeignResponseEntity<>();
        HttpHeaders headers = new HttpHeaders();
        entity.setHeaders(headers);
        return entity;
    }
}

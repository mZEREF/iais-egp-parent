package com.ecquaria.cloud.moh.iais.service.client;

import com.ecquaria.cloud.moh.iais.common.dto.appointment.ApptFeConfirmDateDto;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import org.springframework.http.HttpHeaders;

/**
 * @author Wenkang
 * @date 2019/12/4 15:13
 */
public class SendEmailClientFallback implements SendEmailClient {

    @Override
    public FeignResponseEntity<String> sendEmailByRefNo(ApptFeConfirmDateDto apptFeConfirmDateDto) {
        FeignResponseEntity entity = new FeignResponseEntity<>();
        HttpHeaders headers = new HttpHeaders();
        entity.setHeaders(headers);
        return entity;
    }
}

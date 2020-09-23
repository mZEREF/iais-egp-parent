package com.ecquaria.cloud.moh.iais.service.client;

import com.ecquaria.cloudfeign.FeignResponseEntity;
import org.springframework.http.HttpHeaders;

/**
 * EventBusClientClientFallBack
 *
 * @author Jinhua
 * @date 2020/9/23 14:01
 */
public class EventBusClientClientFallBack {
    public FeignResponseEntity<Void> trackCompersation() {
        FeignResponseEntity entity = new FeignResponseEntity<>();
        HttpHeaders headers = new HttpHeaders();
        entity.setHeaders(headers);
        return entity;
    }
}

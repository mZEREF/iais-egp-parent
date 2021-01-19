package com.ecquaria.cloud.moh.iais.service.client;

import com.ecquaria.cloudfeign.FeignResponseEntity;
import org.springframework.http.HttpHeaders;

/**
 * SystemParamClientFallback
 *
 * @author Jinhua
 * @date 2019/11/23 16:21
 */
public class SystemParamClientFallback {
    public FeignResponseEntity<String> refreshConfiguration() {
        FeignResponseEntity entity = new FeignResponseEntity<>();
        HttpHeaders headers = new HttpHeaders();
        entity.setHeaders(headers);
        return entity;
    }
}

package com.ecquaria.cloud.moh.iais.service.client;

import com.ecquaria.cloudfeign.FeignResponseEntity;
import org.springframework.http.HttpHeaders;

/**
 * SystemAdminClientFallback
 *
 * @author suocheng
 * @date 12/19/2019
 */

public class SystemAdminClientFallback {
    public FeignResponseEntity<String> getSeqId() {
        FeignResponseEntity entity = new FeignResponseEntity<>();
        HttpHeaders headers = new HttpHeaders();
        entity.setHeaders(headers);
        return entity;
    }
}

package com.ecquaria.cloud.moh.iais.service.client;

import com.ecquaria.cloudfeign.FeignResponseEntity;
import org.springframework.http.HttpHeaders;

/**
 * GenerateIdClientFallback
 *
 * @author suocheng
 * @date 12/28/2019
 */

public class GenerateIdClientFallback {
    public FeignResponseEntity<String> getSeqId(){
        FeignResponseEntity entity = new FeignResponseEntity<>();
        HttpHeaders headers = new HttpHeaders();
        entity.setHeaders(headers);
        return entity;
    }
}

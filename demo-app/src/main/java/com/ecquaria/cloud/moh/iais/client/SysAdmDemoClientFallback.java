package com.ecquaria.cloud.moh.iais.client;

import com.ecquaria.cloudfeign.FeignResponseEntity;
import org.springframework.http.HttpHeaders;

/**
 * SysAdmDemoClientFallback
 *
 * @author Jinhua
 * @date 2020/1/8 14:47
 */
public class SysAdmDemoClientFallback {
    public FeignResponseEntity<String> getSeqId() {
        FeignResponseEntity entity = new FeignResponseEntity<>();
        HttpHeaders headers = new HttpHeaders();
        entity.setHeaders(headers);
        return entity;
    }
}

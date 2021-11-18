package com.ecquaria.cloud.moh.iais.service.client;

import com.ecquaria.cloudfeign.FeignResponseEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;

import java.util.Arrays;

/**
 * @Description DpFeClientFallback
 * @Auther chenlei on 11/18/2021.
 */
@Slf4j
public class DpFeClientFallback {
    private FeignResponseEntity getFeignResponseEntity(Object... params) {
        log.warn("--------Params: " + Arrays.toString(params) + "-----------");
        FeignResponseEntity entity = new FeignResponseEntity<>();
        HttpHeaders headers = new HttpHeaders();
        entity.setHeaders(headers);
        return entity;
    }
}

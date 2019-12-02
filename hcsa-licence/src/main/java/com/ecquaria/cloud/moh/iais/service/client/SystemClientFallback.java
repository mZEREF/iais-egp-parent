package com.ecquaria.cloud.moh.iais.service.client;

import com.ecquaria.cloudfeign.FeignResponseEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

/**
 * @author Wenkang
 * @date 2019/11/28 10:28
 */
public class SystemClientFallback {
    FeignResponseEntity<Boolean> isFileExistence(@RequestBody Map<String,String> map){
        FeignResponseEntity entity = new FeignResponseEntity<>();
        HttpHeaders headers = new HttpHeaders();
        entity.setHeaders(headers);
        return entity;
    }
}

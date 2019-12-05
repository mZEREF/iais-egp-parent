package com.ecquaria.cloud.moh.iais.service.client;

import com.ecquaria.cloudfeign.FeignResponseEntity;
import java.util.Map;
import org.springframework.http.HttpHeaders;

/**
 * @author Wenkang
 * @date 2019/11/28 10:28
 */
public class SystemClientFallback {
    FeignResponseEntity<Boolean> isFileExistence( Map<String,String> map){
        FeignResponseEntity entity = new FeignResponseEntity<>();
        HttpHeaders headers = new HttpHeaders();
        entity.setHeaders(headers);
        return entity;
    }
    FeignResponseEntity<String> hclCodeByCode(String code){
        FeignResponseEntity entity = new FeignResponseEntity<>();
        HttpHeaders headers = new HttpHeaders();
        entity.setHeaders(headers);
        return entity;
    }
    FeignResponseEntity<String> licence( String hciCode,  String serviceCode,
                                       Integer yearLength,  Integer licenceSeq) {
        FeignResponseEntity entity = new FeignResponseEntity<>();
        HttpHeaders headers = new HttpHeaders();
        entity.setHeaders(headers);
        return entity;
    }
}

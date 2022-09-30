package com.ecquaria.cloud.moh.iais.client;

import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;

import java.util.Arrays;



@Slf4j
public class KangPingGameClientFallBack {
    private <T> FeignResponseEntity<T> getFeignResponseEntity(Object... objs){
        log.warn(StringUtil.changeForLog("Params:" + Arrays.toString(objs)));
        FeignResponseEntity entity = new FeignResponseEntity<>();
        HttpHeaders headers = new HttpHeaders();
        entity.setHeaders(headers);
        return entity;
    }
}

package com.ecquaria.cloud.moh.iais.service.client;

import com.ecquaria.cloud.moh.iais.common.dto.arcaUen.GenerateUENDto;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import org.springframework.http.HttpHeaders;

/**
 * AcraUenBeClientFallback
 *
 * @author junyu
 * @date 2020/9/1
 */
public class AcraUenBeClientFallback implements AcraUenBeClient{
    @Override
    public FeignResponseEntity<GenerateUENDto> getUen(String uen) {
        FeignResponseEntity entity = new FeignResponseEntity<>();
        HttpHeaders headers = new HttpHeaders();
        entity.setHeaders(headers);
        return entity;
    }
}

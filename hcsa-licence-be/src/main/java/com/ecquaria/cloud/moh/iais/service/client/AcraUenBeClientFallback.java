package com.ecquaria.cloud.moh.iais.service.client;

import com.ecquaria.cloud.moh.iais.common.dto.arca.uen.IaisUENDto;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import org.springframework.http.HttpHeaders;

import java.util.List;

/**
 * AcraUenBeClientFallback
 *
 * @author junyu
 * @date 2020/9/1
 */
public class AcraUenBeClientFallback implements AcraUenBeClient{
    @Override
    public FeignResponseEntity<String> generateUen(IaisUENDto iaisUENDto) {
        FeignResponseEntity entity = new FeignResponseEntity<>();
        HttpHeaders headers = new HttpHeaders();
        entity.setHeaders(headers);
        return entity;
    }

    @Override
    public FeignResponseEntity<Void> acraDeregister(List<String> licenseeIdList) {
        FeignResponseEntity entity = new FeignResponseEntity<>();
        HttpHeaders headers = new HttpHeaders();
        entity.setHeaders(headers);
        return entity;
    }


}

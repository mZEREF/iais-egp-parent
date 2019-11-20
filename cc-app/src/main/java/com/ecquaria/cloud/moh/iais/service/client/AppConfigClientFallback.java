package com.ecquaria.cloud.moh.iais.service.client;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceDto;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import java.util.List;
import org.springframework.http.HttpHeaders;

/**
 * AppConfigClientFallback
 *
 * @author Jinhua
 * @date 2019/11/19 9:47
 */
public class AppConfigClientFallback {
    public FeignResponseEntity<List<HcsaServiceDto>> getHcsaServiceDtosById(List<String> ids){
        FeignResponseEntity entity = new FeignResponseEntity<>();
        HttpHeaders headers = new HttpHeaders();
        entity.setHeaders(headers);
        return entity;
    }
}

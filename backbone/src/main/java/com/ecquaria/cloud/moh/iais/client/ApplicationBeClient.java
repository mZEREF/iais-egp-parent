package com.ecquaria.cloud.moh.iais.client;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppEicRequestTrackingDto;
import com.ecquaria.cloudfeign.FeignConfiguration;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.HttpHeaders;

/**
 * @author Wenkang
 * @date 2020/3/19 16:54
 */
@FeignClient(name = "hcsa-application", configuration = FeignConfiguration.class,
        fallback = ApplicationClientFallback.class)
public class ApplicationBeClient implements ApplicationClientFallback {



    @Override
    public FeignResponseEntity<AppEicRequestTrackingDto> getAppEicRequestTracking(String eventRefNo) {
        FeignResponseEntity entity = new FeignResponseEntity<>();
        HttpHeaders headers = new HttpHeaders();
        entity.setHeaders(headers);
        return entity;
    }

    @Override
    public FeignResponseEntity<AppEicRequestTrackingDto> updateAppEicRequestTracking(String eventRefNo) {
        FeignResponseEntity entity = new FeignResponseEntity<>();
        HttpHeaders headers = new HttpHeaders();
        entity.setHeaders(headers);
        return entity;
    }
}

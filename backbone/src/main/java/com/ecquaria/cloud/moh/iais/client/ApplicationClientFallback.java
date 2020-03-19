package com.ecquaria.cloud.moh.iais.client;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppEicRequestTrackingDto;
import com.ecquaria.cloudfeign.FeignConfiguration;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author Wenkang
 * @date 2020/3/19 16:55
 */
@FeignClient(name = "hcsa-application", configuration = FeignConfiguration.class,
        fallback = ApplicationClientFallback.class)
public interface ApplicationClientFallback {
    @GetMapping(value = "/app-eic-request-tracking/event-rfc-no",produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity <AppEicRequestTrackingDto> getAppEicRequestTracking(@RequestParam(name = "eventRefNo") String eventRefNo);
    @PutMapping(value = "/event-rfc-no-status",produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<AppEicRequestTrackingDto> updateAppEicRequestTracking(@RequestParam(name = "eventRefNo") String eventRefNo);
}

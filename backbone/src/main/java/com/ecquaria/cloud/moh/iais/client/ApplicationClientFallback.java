package com.ecquaria.cloud.moh.iais.client;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppEicRequestTrackingDto;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author Wenkang
 * @date 2020/3/19 16:55
 */
public interface ApplicationClientFallback {
    @GetMapping(value = "/app-eic-request-tracking/event-rfc-no",produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity <AppEicRequestTrackingDto> getAppEicRequestTracking(@RequestParam(name = "eventRefNo") String eventRefNo);
    @PutMapping(value = "/event-rfc-no-status",produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<AppEicRequestTrackingDto> updateAppEicRequestTracking(@RequestParam(name = "eventRefNo") String eventRefNo);
}

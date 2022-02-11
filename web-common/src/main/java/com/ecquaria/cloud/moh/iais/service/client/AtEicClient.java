package com.ecquaria.cloud.moh.iais.service.client;

import com.ecquaria.cloud.moh.iais.common.dto.EicRequestTrackingDto;
import com.ecquaria.cloudfeign.FeignConfiguration;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import java.util.List;
import java.util.Map;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * AtEicClient
 *
 * @author Jinhua
 * @date 2020/4/17 13:59
 */
@FeignClient(name = "audit-trail", configuration = FeignConfiguration.class,
        fallback = EicClientFallback.class)
public interface AtEicClient {
    @GetMapping(path = "/iais-audit-trail/pendingEicTracking/{moduleName}", produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<EicRequestTrackingDto>> getPendingRecords(@PathVariable("moduleName") String moduleName);
    @PutMapping(path = "/iais-audit-trail/eicTrackingStat", consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<Map<String, String>> updateStatus(@RequestBody List<EicRequestTrackingDto> dtoList);
    @DeleteMapping(value = "/eic-request-tracking", consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<Void> deleteOldEicRequestTracking();
}

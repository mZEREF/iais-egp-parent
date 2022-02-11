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
 * LicEicClient
 *
 * @author Jinhua
 * @date 2020/4/20 15:25
 */
@FeignClient(name = "hcsa-licence", configuration = FeignConfiguration.class,
        fallback = EicClientFallback.class)
public interface LicEicClient {
    @GetMapping(path = "/eicTracking/{moduleName}", produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<EicRequestTrackingDto>> getPendingRecords(@PathVariable("moduleName") String moduleName);

    @GetMapping(path = "/eicTracking/{referenceNumber}/eic-ref-num", produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<EicRequestTrackingDto> getPendingRecordByReferenceNumber(@PathVariable("referenceNumber") String referenceNumber);

    @PutMapping(path = "/eicTracking", consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<Map<String, String>> updateStatus(@RequestBody List<EicRequestTrackingDto> dtoList);

    @PutMapping(path = "/eicTracking/eic-request", consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<EicRequestTrackingDto> saveEicTrack(@RequestBody EicRequestTrackingDto eicRequestTrackingDto);

    @DeleteMapping(value = "/eic-request-tracking", consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<Void> deleteOldEicRequestTracking();
}

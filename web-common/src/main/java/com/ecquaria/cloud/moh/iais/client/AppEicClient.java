package com.ecquaria.cloud.moh.iais.client;

import com.ecquaria.cloud.moh.iais.common.dto.EicRequestTrackingDto;
import com.ecquaria.cloudfeign.FeignConfiguration;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Map;

/**
 * AppEicClient
 *
 * @author Jinhua
 * @date 2020/4/17 17:03
 */
@FeignClient(name = "hcsa-application", configuration = FeignConfiguration.class,
        fallback = AppEicClientFallback.class)
public interface AppEicClient {
    @GetMapping(path = "/eicTracking/{moduleName}", produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<EicRequestTrackingDto>> getPendingRecords(@PathVariable("moduleName") String moduleName);

    @PutMapping(path = "/eicTracking", consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<Map<String, String>> updateStatus(@RequestBody List<EicRequestTrackingDto> dtoList);

    @GetMapping(path = "/eicTracking/{referenceNumber}/eic-ref-num", produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<EicRequestTrackingDto> getPendingRecordByReferenceNumber(@PathVariable("referenceNumber") String referenceNumber);

    @PutMapping(path = "/eicTracking/eic-request", consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<Map<String, String>> saveEicTrack(@RequestBody EicRequestTrackingDto eicRequestTrackingDto);
}

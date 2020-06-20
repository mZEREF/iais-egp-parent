package com.ecquaria.cloud.moh.iais.service.client;

import com.ecquaria.cloud.moh.iais.common.dto.EicRequestTrackingDto;
import com.ecquaria.cloudfeign.FeignConfiguration;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;


@FeignClient(name = "system-admin", configuration = FeignConfiguration.class,
        fallback = SystemAdminMainFeClientFallback.class)
public interface SystemAdminMainFeClient {
    @RequestMapping(path = "/application-number")
    FeignResponseEntity<String> applicationNumber(@RequestParam(value = "type") String applicationType);

    @PutMapping(path = "/eicTracking",consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<Map<String, String>> saveEicTrack(@RequestBody List<EicRequestTrackingDto> dtoList);

    @GetMapping(path = "/eicTracking/{moduleName}", produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<EicRequestTrackingDto>> getPendingRecords(@PathVariable("moduleName") String moduleName);

    @GetMapping(path = "/eicTracking/{referenceNumber}/eic-ref-num", produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<EicRequestTrackingDto> getByRefNum(@PathVariable("referenceNumber") String refNum);
}

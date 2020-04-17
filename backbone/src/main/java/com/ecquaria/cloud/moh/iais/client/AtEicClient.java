package com.ecquaria.cloud.moh.iais.client;

import com.ecquaria.cloud.moh.iais.common.dto.EicRequestTrackingDto;
import com.ecquaria.cloudfeign.FeignConfiguration;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import java.util.List;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

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
}

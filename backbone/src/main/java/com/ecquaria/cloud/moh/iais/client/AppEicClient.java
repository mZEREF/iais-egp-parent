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
 * AppEicClient
 *
 * @author Jinhua
 * @date 2020/4/17 17:03
 */
@FeignClient(name = "hcsa-application", configuration = FeignConfiguration.class,
        fallback = EicClientFallback.class)
public interface AppEicClient {
    @GetMapping(path = "/eicTracking/{moduleName}", produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<EicRequestTrackingDto>> getPendingRecords(@PathVariable("moduleName") String moduleName);
}

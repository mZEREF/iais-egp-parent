package com.ecquaria.cloud.moh.iais.service.client;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesCorrelationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesRoutingHistoryDto;
import com.ecquaria.cloudfeign.FeignConfiguration;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import java.util.List;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * TaskApplicationClient
 *
 * @author suocheng
 * @date 12/4/2019
 */
@FeignClient(name = "hcsa-application", configuration = FeignConfiguration.class,
        fallback = TaskApplicationClientFallback.class)
public interface TaskApplicationClient {
    @GetMapping(path = "/iais-apppremisescorrelation-be/AppPremisesCorrelations/{appGropId}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<AppPremisesCorrelationDto>> getGroupAppsByNo(@PathVariable("appGropId") String appGropId);

    @GetMapping(value = "/iais-apppremisescorrelation-be/app-premises-correlations/{appId}")
    FeignResponseEntity<List<AppPremisesCorrelationDto>> getAppPremisesCorrelationsByAppId(@PathVariable("appId") String appId);

    @PostMapping(path = "/iais-application-history/historys",
            produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<AppPremisesRoutingHistoryDto>> createAppPremisesRoutingHistorys(@RequestBody List<AppPremisesRoutingHistoryDto> appPremisesRoutingHistoryDtos);
}

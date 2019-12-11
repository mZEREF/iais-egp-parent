package com.ecquaria.cloud.moh.iais.service.client;

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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * AppPremisesRoutingHistoryClient
 *
 * @author suocheng
 * @date 11/26/2019
 */
@FeignClient(name = "hcsa-application", configuration = FeignConfiguration.class,
        fallback = AppPremisesRoutingHistoryClientFallback.class)
public interface AppPremisesRoutingHistoryClient {
    @RequestMapping(path = "/iais-application-history",method = RequestMethod.POST, produces = { MediaType.APPLICATION_JSON_VALUE },
            consumes = {MediaType.APPLICATION_JSON_VALUE})
    FeignResponseEntity<AppPremisesRoutingHistoryDto> createAppPremisesRoutingHistory(AppPremisesRoutingHistoryDto appPremisesRoutingHistoryDto);

    @GetMapping(path = "/iais-application-history/appPremisesRoutingHistorys/{appId}")
    FeignResponseEntity<List<AppPremisesRoutingHistoryDto>> getAppPremisesRoutingHistorysById(@PathVariable("appId") String appId) ;

    @PostMapping(value = "/iais-application-history/historys",consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<AppPremisesRoutingHistoryDto>> createAppPremisesRoutingHistorys(@RequestBody List<AppPremisesRoutingHistoryDto> appPremisesRoutingHistoryDtos);
}

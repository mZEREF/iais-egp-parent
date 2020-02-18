package com.ecquaria.cloud.moh.iais.service.client;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesRoutingHistoryDto;
import com.ecquaria.cloudfeign.FeignConfiguration;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

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

    @RequestMapping(path = "/iais-application-history/appPremisesRoutingHistorys/{appNo}" ,method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<AppPremisesRoutingHistoryDto>> getAppPremisesRoutingHistorysByAppNo(@PathVariable("appNo") String appNo);

    @RequestMapping(path = "/iais-application-history/appPremisesRoutingHistory/{appNo}/{stageId}" ,method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<AppPremisesRoutingHistoryDto> getAppPremisesRoutingHistorysByAppNoAndStageId(@PathVariable("appNo") String appNo,
                                                                                                     @PathVariable("stageId") String stageId);

    @RequestMapping(value = "/iais-application-history/historys",method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<AppPremisesRoutingHistoryDto>> createAppPremisesRoutingHistorys(@RequestBody List<AppPremisesRoutingHistoryDto> appPremisesRoutingHistoryDtos);

    @GetMapping(value = "/iais-application-history/sub-stage-history/{appNo}/{stageId}")
    FeignResponseEntity<AppPremisesRoutingHistoryDto> getAppPremisesRoutingHistorySubStage(@PathVariable("appNo") String appCorrId, @PathVariable("stageId") String stageId);

    @GetMapping(value = "/iais-application-history/secondRouteBack-history/{appCorrId}")
    FeignResponseEntity<AppPremisesRoutingHistoryDto> getSecondRouteBackHistoryByCorrId(@PathVariable("appCorrId") String appCorrId);
}

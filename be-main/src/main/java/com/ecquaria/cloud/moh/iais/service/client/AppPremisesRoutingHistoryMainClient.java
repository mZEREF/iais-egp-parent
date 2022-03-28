package com.ecquaria.cloud.moh.iais.service.client;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesRoutingHistoryDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesRoutingHistoryExtDto;
import com.ecquaria.cloudfeign.FeignConfiguration;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import java.util.List;
import java.util.Map;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * AppPremisesRoutingHistoryClient
 *
 * @author suocheng
 * @date 11/26/2019
 */
@FeignClient(name = "hcsa-application", configuration = FeignConfiguration.class,
        fallback = AppPremisesRoutingHistoryMainClientFallback.class)
public interface AppPremisesRoutingHistoryMainClient {

    @RequestMapping(path = "/iais-application-history/appPremisesRoutingHistory/{appNo}/{stageId}" ,method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<AppPremisesRoutingHistoryDto> getAppPremisesRoutingHistorysByAppNoAndStageId(@PathVariable("appNo") String appNo,
                                                                                                     @PathVariable("stageId") String stageId);

    @RequestMapping(path = "/iais-application-history/appPremisesRoutingHistory/{appNo}/{stageId}/{roleId}" ,method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<AppPremisesRoutingHistoryDto> getAppPremisesRoutingHistorysByAppNoAndStageId(@PathVariable("appNo") String appNo,
                                                                                                     @PathVariable("stageId") String stageId,
                                                                                                     @PathVariable("roleId") String roleId);

    @RequestMapping(path = "/iais-application-history/appPremisesRoutingHistory/{appNo}/{stageId}/{roleId}/{appStatus}" ,method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<AppPremisesRoutingHistoryDto> getAppPremisesRoutingHistorysByAppNoAndStageId(@PathVariable("appNo") String appNo,
                                                                                                     @PathVariable("stageId") String stageId,
                                                                                                     @PathVariable("roleId") String roleId,
                                                                                                     @PathVariable("appStatus") String appStatus);

    @RequestMapping(path = "/iais-application-history",method = RequestMethod.POST, produces = { MediaType.APPLICATION_JSON_VALUE },
            consumes = {MediaType.APPLICATION_JSON_VALUE})
    FeignResponseEntity<AppPremisesRoutingHistoryDto> createAppPremisesRoutingHistory(AppPremisesRoutingHistoryDto appPremisesRoutingHistoryDto);

    @RequestMapping(path = "/iais-application-history/appPremisesRoutingHistorys" ,method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<AppPremisesRoutingHistoryDto>> getAppPremisesRoutingHistorysByAppNo(@RequestParam("appNo") String appNo);

    @RequestMapping(value = "/iais-application-history/historys",method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<AppPremisesRoutingHistoryDto>> createAppPremisesRoutingHistorys(@RequestBody List<AppPremisesRoutingHistoryDto> appPremisesRoutingHistoryDtos);

    @GetMapping(value = "/iais-application-history/sub-stage-history/{corrId}/{stageId}")
    FeignResponseEntity<AppPremisesRoutingHistoryDto> getAppPremisesRoutingHistorySubStage(@PathVariable("corrId") String corrId, @PathVariable("stageId") String stageId);

    @PostMapping(value = "/iais-application-history/sub-stage-histories")
    FeignResponseEntity<Map<String, AppPremisesRoutingHistoryDto>> getAppPremisesRoutingHistoriesSubStage(@RequestBody Map<String, String> paramMap);

    @GetMapping(value = "/iais-application-history/secondRouteBack-history/{appNo}/{status}")
    FeignResponseEntity<AppPremisesRoutingHistoryDto> getSecondRouteBackHistoryByAppNo(@PathVariable("appNo") String appNo,@PathVariable("status") String status);

    @GetMapping(value = "/iais-application-history/getAppPremisesRoutingHistoryExt/{appPremRhId}/{componentName}")
    FeignResponseEntity<AppPremisesRoutingHistoryExtDto> getAppPremisesRoutingHistoryExtByHistoryAndComponentName(@PathVariable("appPremRhId") String appPremRhId, @PathVariable("componentName") String componentName);

}

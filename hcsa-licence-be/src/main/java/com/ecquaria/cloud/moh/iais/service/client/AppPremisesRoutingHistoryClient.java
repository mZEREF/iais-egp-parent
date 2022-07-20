package com.ecquaria.cloud.moh.iais.service.client;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesRoutingHistoryDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesRoutingHistoryExtDto;
import com.ecquaria.cloudfeign.FeignConfiguration;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

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

    @RequestMapping(path = "/iais-application-history/appPremisesRoutingHistorys" ,method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<AppPremisesRoutingHistoryDto>> getAppPremisesRoutingHistorysByAppNo(@RequestParam("appNo") String appNo);

    @GetMapping(value = "/iais-application-history/activeAppPremisesRoutingHistorys",produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<AppPremisesRoutingHistoryDto>> getActiveAppPremisesRoutingHistorysByAppCorrId(@RequestParam("premCorrId") String premCorrId);

    @GetMapping(value = "/iais-application-history/appPremisesRoutingHistoryByAppNoAndRoleIds", produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<AppPremisesRoutingHistoryDto>> getAppPremisesRoutingHistoryDtosByAppNoAndRoleIds(@RequestParam(name ="appNo")  String appNo,
                                                                                                                @RequestParam(name ="roleIds")  List<String> roleIds);
    @RequestMapping(path = "/iais-application-history/appPremisesRoutingHistory/{appNo}/{stageId}" ,method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<AppPremisesRoutingHistoryDto> getAppPremisesRoutingHistorysByAppNoAndStageId(@PathVariable("appNo") String appNo,
                                                                                                     @PathVariable("stageId") String stageId);

    @RequestMapping(path = "/iais-application-history/activeAppPremisesRoutingHistory/{appNo}/{stageId}" ,method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<AppPremisesRoutingHistoryDto> getActiveAppPremisesRoutingHistorysByAppNoAndStageId(@PathVariable("appNo") String appNo,
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

    @RequestMapping(path = "/iais-application-history/appUserHistory/{appNo}/{actionBy}" ,method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<AppPremisesRoutingHistoryDto> getAppHistoryByAppNoAndActionBy(@PathVariable("appNo") String appNo,
                                                                                      @PathVariable("actionBy") String actionBy);

    @RequestMapping(value = "/iais-application-history/historys",method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<AppPremisesRoutingHistoryDto>> createAppPremisesRoutingHistorys(@RequestBody List<AppPremisesRoutingHistoryDto> appPremisesRoutingHistoryDtos);

    @GetMapping(value = "/iais-application-history/sub-stage-history/{corrId}/{stageId}")
    FeignResponseEntity<AppPremisesRoutingHistoryDto> getAppPremisesRoutingHistorySubStage(@PathVariable("corrId") String corrId, @PathVariable("stageId") String stageId);

    @GetMapping(value = "/iais-application-history/secondRouteBack-history/{appNo}/{status}")
    FeignResponseEntity<AppPremisesRoutingHistoryDto> getSecondRouteBackHistoryByAppNo(@PathVariable("appNo") String appNo,@PathVariable("status") String status);

    @RequestMapping(value = "/iais-application-history/save-history-ext",method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<AppPremisesRoutingHistoryExtDto> saveHistoryExt(@RequestBody AppPremisesRoutingHistoryExtDto appPremisesRoutingHistoryExtDto);

    @GetMapping(value = "/iais-application-history/getAppPremisesRoutingHistoryExt/{appPremRhId}/{componentName}")
    FeignResponseEntity<AppPremisesRoutingHistoryExtDto> getAppPremisesRoutingHistoryExtByHistoryAndComponentName(@PathVariable("appPremRhId") String appPremRhId, @PathVariable("componentName") String componentName);
}

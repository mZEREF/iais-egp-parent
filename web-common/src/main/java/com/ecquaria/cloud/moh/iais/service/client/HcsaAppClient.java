package com.ecquaria.cloud.moh.iais.service.client;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppGrpPremisesEntityDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesRoutingHistoryDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationGroupDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.AppInspectionStatusDto;
import com.ecquaria.cloudfeign.FeignConfiguration;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * HcsaAppClient
 *
 * @author Jinhua
 * @date 2020/7/13 11:11
 */
@FeignClient(name = "hcsa-application", configuration = FeignConfiguration.class,
        fallback = HcsaAppClientFallBack.class)
public interface HcsaAppClient {
    @GetMapping(value = "/hcsa-app-common/app-grp/{appGroupId}",produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<ApplicationGroupDto> getAppGrpById(@PathVariable("appGroupId") String appGroupId);

    @GetMapping(value = "/hcsa-app-common/app-grp-appNo/{appNo}",produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<ApplicationGroupDto> getAppGrpByAppNo(@PathVariable("appNo") String appNo);

    @GetMapping(value = "/iais-application-history/appPremisesRoutingHistorys",produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<AppPremisesRoutingHistoryDto>> getAppPremisesRoutingHistorysByAppNo(@RequestParam("appNo") String appNo);

    @GetMapping(value = "/hcsa-app-common/apps-by-grp/{appGrpId}",produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<ApplicationDto>> getAppsByGrpId(@PathVariable("appGrpId") String appGrpId);

    @RequestMapping(path = "/hcsa-app-common/application/{appNo}",method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<ApplicationDto> getAppByNo(@PathVariable("appNo") String appNo);

    @GetMapping(value = "/iais-application-be/get-prem-by-app-no",produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<AppGrpPremisesEntityDto> getPremisesByAppNo(@RequestParam("appNo") String appNo);

    @GetMapping(path = "/iais-inspecstatus/status-three/{appPremCorreId}", produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<AppInspectionStatusDto> getAppInspectionStatusByPremId(@PathVariable("appPremCorreId") String appPremCorreId);

    @PostMapping(value = "/hcsa-app-common/bundled-apps-by-app",produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<ApplicationDto> getBundledAppDtosByAppDto(@RequestBody ApplicationDto appDto);
}

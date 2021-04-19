package com.ecquaria.cloud.moh.iais.service.client;


import com.ecquaria.cloud.moh.iais.common.dto.application.AppReturnFeeDto;
import com.ecquaria.cloud.moh.iais.common.dto.application.ApplicationViewDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.appeal.AppPremiseMiscDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesCorrelationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationGroupDto;
import com.ecquaria.cloud.moh.iais.common.dto.system.ProcessFileTrackDto;
import com.ecquaria.cloudfeign.FeignConfiguration;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * ApplicationClient
 *
 * @author suocheng
 * @date 11/26/2019
 */
@FeignClient(name = "hcsa-application", configuration = FeignConfiguration.class,
        fallback = ApplicationMainClientFallback.class)
public interface ApplicationMainClient {
    @RequestMapping(path = "/iais-application-be/applicationview/{correlationId}",method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<ApplicationViewDto> getAppViewByCorrelationId(@PathVariable("correlationId") String correlationId);
    @RequestMapping(path = "/iais-application-be/application/{appNo}",method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<ApplicationDto> getAppByNo(@PathVariable("appNo") String appNo);
    @RequestMapping(path = "/iais-application-be",method = RequestMethod.PUT,consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<ApplicationDto> updateApplication(@RequestBody ApplicationDto applicationDto);
    @GetMapping(value = "/iais-apppremisescorrelation-be/apppremisescorrelationdto/{appCorreId}", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<AppPremisesCorrelationDto> getLastAppPremisesCorrelationDtoByCorreId(@PathVariable("appCorreId") String appCorreId);
    @RequestMapping(path = "/iais-application-be/applications/{appGropId}",method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<ApplicationDto>> getGroupAppsByNo(@PathVariable("appGropId") String appGropId);
    @RequestMapping(path = "/iais-application-group-be/{appGroupId}",method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<ApplicationGroupDto> getAppById(@PathVariable("appGroupId") String appGroupId);
    @PostMapping(value = "/iais-application-be/save-app-return-fee",produces = MediaType.APPLICATION_JSON_VALUE,consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<AppReturnFeeDto> saveAppReturnFee(@RequestBody AppReturnFeeDto appReturnFeeDto);

    @PostMapping(value = "/file-existence",consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<ProcessFileTrackDto> isFileExistence(@RequestBody Map<String,String> map);

    @GetMapping(value = "/iais-cessation/appId-misc-list-cessation",produces =MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<AppPremiseMiscDto>> getAppPremiseMiscDtoListByAppId(@RequestParam("appId") String appId);
    @GetMapping(value = "/iais-application-be/app-prem-corr/{appId}")
    FeignResponseEntity<AppPremisesCorrelationDto> getAppPremisesCorrelationDtosByAppId(@PathVariable(name = "appId") String appId);
    @GetMapping(value = "/iais-cessation/application-premises-misc",produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity <AppPremiseMiscDto> getAppPremisesMisc(@RequestParam("correId") String correId);

    @GetMapping(value = "/iais-inspection/appdto/{id}", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<ApplicationDto> getApplicationById(@PathVariable(name = "id") String id);
    @PostMapping(value = "/iais-application-be/clearHclcode-appIds",consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<String>> clearHclcodeByAppIds(@RequestBody List<ApplicationDto> applicationDtos);

    @GetMapping(value = "/iais-cessation/application-by-licId", produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<ApplicationDto>> getAppsByLicId(@RequestParam("licId") String licId);

    @PutMapping(value = "/iais-application-be/cessation-application",consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<ApplicationDto>> updateCessationApplications(@RequestBody List<ApplicationDto> applicationDtos);

    @GetMapping(value = "/iais-application-group-be/app-group-one/{appGroupNo}",produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<ApplicationGroupDto> getAppGrpByNo(@PathVariable("appGroupNo") String appGroupNo);

    @GetMapping(value = "/iais-application-be/prem-corr-list/{appGroupId}")
    FeignResponseEntity<List<AppPremisesCorrelationDto>> getPremCorrDtoByAppGroupId(@PathVariable(name = "appGroupId") String appGroupId);
}

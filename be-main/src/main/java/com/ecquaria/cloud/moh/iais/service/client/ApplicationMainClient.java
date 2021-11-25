package com.ecquaria.cloud.moh.iais.service.client;


import com.ecquaria.cloud.moh.iais.common.dto.application.AppPremPreInspectionNcDto;
import com.ecquaria.cloud.moh.iais.common.dto.application.AppReturnFeeDto;
import com.ecquaria.cloud.moh.iais.common.dto.application.ApplicationViewDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.appeal.AppPremiseMiscDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppEditSelectDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppGrpPremisesEntityDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesCorrelationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesRecommendationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationGroupDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.ReportResultDto;
import com.ecquaria.cloud.moh.iais.common.dto.intranetDashboard.HcsaTaskAssignDto;
import com.ecquaria.cloud.moh.iais.common.dto.system.ProcessFileTrackDto;
import com.ecquaria.cloudfeign.FeignConfiguration;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

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

    @GetMapping(value = "/iais-licence-view/appSubmissionDto/{appId}",consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<AppSubmissionDto> getAppSubmissionByAppId(@PathVariable("appId") String appId);

    @GetMapping(value = "/iais-application-be/get-prem-by-app-no",produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<AppGrpPremisesEntityDto> getPremisesByAppNo(@RequestParam("appNo") String appNo);

    @GetMapping(value = "/iais-licence-view/app-edit-select-by-type",produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<AppEditSelectDto>> getAppEditSelectDto(@RequestParam(name="appId")String appId, @RequestParam(name = "changeType")String changeType);

    @PostMapping(value = "/iais-inspection-report/saveReportResult" ,consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<String> saveReportResult(@RequestBody ReportResultDto reportResultDto );

    @GetMapping(path = "/application-be/RescomDto/{appPremId}/{recomType}",produces = { MediaType.APPLICATION_JSON_VALUE },
            consumes = {MediaType.APPLICATION_JSON_VALUE})
    FeignResponseEntity<AppPremisesRecommendationDto> getAppPremRecordByIdAndType(@PathVariable(value ="appPremId" ) String appPremId, @PathVariable(value ="recomType" ) String recomType);

    @GetMapping(path = "/iais-apppreinsnc-be/AppPremNcByAppCorrId{appCorrId}",produces = { MediaType.APPLICATION_JSON_VALUE },
            consumes = {MediaType.APPLICATION_JSON_VALUE})
    FeignResponseEntity<AppPremPreInspectionNcDto> getAppNcByAppCorrId(@PathVariable(value ="appCorrId" ) String appCorrId);

    @GetMapping(value = "/Iais-applicatio-be/app-grp/all-address-pool",produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<HcsaTaskAssignDto> getUnitNoAndAddressByAppGrpIds(@RequestBody List<String> appGroupIds);
}

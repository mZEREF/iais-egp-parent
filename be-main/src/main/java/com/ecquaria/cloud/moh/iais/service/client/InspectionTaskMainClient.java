package com.ecquaria.cloud.moh.iais.service.client;

import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.appeal.AppPremiseMiscDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.appeal.AppPremisesSpecialDocDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppGrpPremisesDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppInsRepDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppIntranetDocDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesCorrelationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesRecommendationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesRoutingHistoryDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppStageSlaTrackingDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcPrincipalOfficersDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationGroupDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.AppPremInsDraftDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspectionAppGroupQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspectionAppInGroupQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspectionCommonPoolQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspectionSubPoolQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.intranetDashboard.DashAssignMeAjaxQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.intranetDashboard.DashAssignMeQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.intranetDashboard.DashComPoolAjaxQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.intranetDashboard.DashComPoolQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.intranetDashboard.DashWorkTeamAjaxQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.intranetDashboard.DashWorkTeamQueryDto;
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
 * @author Shicheng
 * @date 2019/11/26 10:33
 **/
@FeignClient(name = "hcsa-application", configuration = FeignConfiguration.class,
        fallback = InspectionTaskMainClientFallback.class)
public interface InspectionTaskMainClient {
    @RequestMapping(path = "/iais-inspection/one-of-inspection/{applicationNo}",method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE },
            consumes = {MediaType.APPLICATION_JSON_VALUE})
    FeignResponseEntity<ApplicationDto> getApplicationDtoByAppNo(@PathVariable("applicationNo") String applicationNo);

    @RequestMapping(path = "/iais-inspection/appGroup-of-inspection/{appGroupId}",method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE },
            consumes = {MediaType.APPLICATION_JSON_VALUE})
    FeignResponseEntity<ApplicationGroupDto> getApplicationGroupDtoByAppGroId(@PathVariable("appGroupId") String appGroupId);

    @RequestMapping(path = "/iais-inspection/application-premises-by-app-id/{appCorrId}",method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE },
            consumes = {MediaType.APPLICATION_JSON_VALUE})
    FeignResponseEntity<AppGrpPremisesDto> getAppGrpPremisesDtoByAppGroId(@PathVariable("appCorrId") String applicationId);

    @RequestMapping(path = "/iais-inspection/inspection-searchParam",method = RequestMethod.POST, produces = { MediaType.APPLICATION_JSON_VALUE },
            consumes = {MediaType.APPLICATION_JSON_VALUE})
    FeignResponseEntity<SearchResult<InspectionCommonPoolQueryDto>> searchInspectionPool(SearchParam searchParam);

    @RequestMapping(path = "/iais-inspection/inspection-sub-searchParam",method = RequestMethod.POST, produces = { MediaType.APPLICATION_JSON_VALUE },
            consumes = {MediaType.APPLICATION_JSON_VALUE})
    FeignResponseEntity<SearchResult<InspectionSubPoolQueryDto>> searchInspectionSupPool(SearchParam searchParam);

    @RequestMapping(path = "/iais-inspection/inspection-be-appGroup",method = RequestMethod.POST, produces = { MediaType.APPLICATION_JSON_VALUE },
            consumes = {MediaType.APPLICATION_JSON_VALUE})
    FeignResponseEntity<SearchResult<InspectionAppGroupQueryDto>> searchInspectionBeAppGroup(SearchParam searchParam);

    @RequestMapping(path = "/iais-inspection/inspection-be-appGroup-ajax",method = RequestMethod.POST, produces = { MediaType.APPLICATION_JSON_VALUE },
        consumes = {MediaType.APPLICATION_JSON_VALUE})
    FeignResponseEntity<SearchResult<InspectionAppInGroupQueryDto>> searchInspectionBeAppGroupAjax(SearchParam searchParam);

    @PostMapping(value = "/iais-application-history/historys-kpi",consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<AppPremisesRoutingHistoryDto>> getHistoryForKpi(@RequestBody AppPremisesRoutingHistoryDto appPremisesRoutingHistoryDto );

    @GetMapping(value = "/iais-inspection/ins-draf-date/{appPreCorrId}")
    FeignResponseEntity<AppPremInsDraftDto> getAppPremInsDraftDtoByAppPreCorrId(@PathVariable(name = "appPreCorrId") String appPreCorrId);

    @GetMapping(path = "/iais-apppremisescorrelation-be/applicationcorreid/{appCorreId}", produces = { MediaType.APPLICATION_JSON_VALUE },
            consumes = {MediaType.APPLICATION_JSON_VALUE})
    FeignResponseEntity<ApplicationDto> getApplicationByCorreId(@PathVariable("appCorreId") String appCorreId);

    @GetMapping(value = "/application-be/app-stage-salday/{appNo}/{stageId}")
    FeignResponseEntity<AppStageSlaTrackingDto> getSlaTrackByAppNoStageId(@PathVariable("appNo") String appNo, @PathVariable("stageId") String stageId);

    @GetMapping(path = "/application-be/RescomDto/{appPremId}/{recomType}",produces = { MediaType.APPLICATION_JSON_VALUE },
            consumes = {MediaType.APPLICATION_JSON_VALUE})
    FeignResponseEntity<AppPremisesRecommendationDto> getAppPremRecordByIdAndType(@PathVariable(value ="appPremId" ) String appPremId, @PathVariable(value ="recomType" ) String recomType);

    @PostMapping(value = "/iais-inspection/dash-common-pool")
    FeignResponseEntity<SearchResult<DashComPoolQueryDto>> searchDashComPoolResult(@RequestBody SearchParam searchParam);

    @PostMapping(value = "/iais-inspection/dash-common-pool/drop")
    FeignResponseEntity<SearchResult<DashComPoolAjaxQueryDto>> searchDashComPoolDropResult(@RequestBody SearchParam searchParam);

    @GetMapping(value = "/iais-appintranetdoc/get-appintranetdocList-by-premid-status", produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<AppIntranetDocDto>> getAppIntranetDocListByPremIdAndStatus(@RequestParam(name = "premId") String premId, @RequestParam(name = "docStatus") String status );

    @GetMapping(value = "/iais-cessation/appId-misc-list-cessation",produces =MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<AppPremiseMiscDto>> getAppPremiseMiscDtoListByAppId(@RequestParam("appId") String appId);

    @GetMapping(value = "/iais-application-be/application-cgo-by-application-id",produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<AppSvcPrincipalOfficersDto> getApplicationCgoByAppId(@RequestParam(name = "applicationId") String applicationId, @RequestParam("psnType") String psnType);

    @GetMapping(value = "/iais-appPremisesdoc/get-appPremisesSpecialDoc-by-premid", produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<AppPremisesSpecialDocDto>> getAppPremisesSpecialDocByPremId(@RequestParam(name = "premId") String premId);

    @GetMapping(value = "/iais-apppremisescorrelation-be/app-premises-correlations/{appId}",produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<AppPremisesCorrelationDto>> getAppPremisesCorrelationsByAppId(@PathVariable("appId") String appId);

    @GetMapping(value = "/application-number-grp-premiese/{appPremcorrId}",produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<AppInsRepDto> appGrpPremises(@PathVariable("appPremcorrId") String appPremcorrId);

    @PostMapping(path = "/application-be/RescomDtoStorage",produces = { MediaType.APPLICATION_JSON_VALUE },
            consumes = {MediaType.APPLICATION_JSON_VALUE})
    FeignResponseEntity<AppPremisesRecommendationDto> saveAppRecom(@RequestBody AppPremisesRecommendationDto appPremisesRecommendationDto);

    @PostMapping(value = "/iais-inspection/dash-task-me")
    FeignResponseEntity<SearchResult<DashAssignMeQueryDto>> searchDashAssignMeResult(@RequestBody SearchParam searchParam);

    @PostMapping(value = "/iais-inspection/dash-task-me/drop")
    FeignResponseEntity<SearchResult<DashAssignMeAjaxQueryDto>> searchDashAssignMeAjaxResult(@RequestBody SearchParam searchParam);

    @PostMapping(value = "/iais-inspection/dash-work-team")
    FeignResponseEntity<SearchResult<DashWorkTeamQueryDto>> searchDashWorkTeamResult(@RequestBody SearchParam searchParam);

    @PostMapping(value = "/iais-inspection/dash-work-team/drop")
    FeignResponseEntity<SearchResult<DashWorkTeamAjaxQueryDto>> searchDashWorkTeamDropResult(@RequestBody SearchParam searchParam);

    @PostMapping(value = "/iais-apppremisescorrelation-be/AppPremisesCorrelations",produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<Map<String, AppGrpPremisesDto>> getGroupAppsByNos(@RequestBody List<String> appGropIds);

    @PostMapping(value = "/application-be/app-stage-saldays")
    FeignResponseEntity<List<AppStageSlaTrackingDto>> getSlaTrackByAppNoStageIds(@RequestBody Map<String, String> params);
}
package com.ecquaria.cloud.moh.iais.service.client;

import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.application.AppPremInspCorrelationDto;
import com.ecquaria.cloud.moh.iais.common.dto.application.AppPremPreInspectionNcDocDto;
import com.ecquaria.cloud.moh.iais.common.dto.appointment.AppointmentDto;
import com.ecquaria.cloud.moh.iais.common.dto.appointment.ReschedulingOfficerDto;
import com.ecquaria.cloud.moh.iais.common.dto.appointment.ReschedulingOfficerQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppGrpPremisesDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesInspecApptDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesRecommendationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesRoutingHistoryDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesRoutingHistoryExtDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppStageSlaTrackingDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationGroupDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.EventInspRecItemNcDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.AppPremInsDraftDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.ComPoolAjaxQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspectionCommonPoolQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspectionSubPoolQueryDto;
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

import java.util.List;

/**
 * @author Shicheng
 * @date 2019/11/26 10:33
 **/
@FeignClient(name = "hcsa-application", configuration = FeignConfiguration.class,
        fallback = InspectionTaskClientFallback.class)
public interface InspectionTaskClient {
    @RequestMapping(path = "/iais-inspection/one-of-inspection/{applicationNo}",method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE },
            consumes = {MediaType.APPLICATION_JSON_VALUE})
    FeignResponseEntity<ApplicationDto> getApplicationDtoByAppNo(@PathVariable("applicationNo") String applicationNo);

    @RequestMapping(path = "/iais-inspection/appGroup-of-inspection/{appGroupId}",method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE },
            consumes = {MediaType.APPLICATION_JSON_VALUE})
    FeignResponseEntity<ApplicationGroupDto> getApplicationGroupDtoByAppGroId(@PathVariable("appGroupId") String appGroupId);

    @RequestMapping(path = "/iais-inspection/application-premises-by-app-id/{appCorrId}",method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE },
            consumes = {MediaType.APPLICATION_JSON_VALUE})
    FeignResponseEntity<AppGrpPremisesDto> getAppGrpPremisesDtoByAppGroId(@PathVariable("appCorrId") String appCorrId);

    @RequestMapping(path = "/iais-inspection/inspection-searchParam",method = RequestMethod.POST, produces = { MediaType.APPLICATION_JSON_VALUE },
            consumes = {MediaType.APPLICATION_JSON_VALUE})
    FeignResponseEntity<SearchResult<InspectionCommonPoolQueryDto>> searchInspectionPool(SearchParam searchParam);

    @PostMapping(value = "/iais-inspection/common-ajax-param", produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<SearchResult<ComPoolAjaxQueryDto>> commonPoolResult(@RequestBody SearchParam searchParam);

    @RequestMapping(path = "/iais-inspection/inspection-sub-searchParam",method = RequestMethod.POST, produces = { MediaType.APPLICATION_JSON_VALUE },
            consumes = {MediaType.APPLICATION_JSON_VALUE})
    FeignResponseEntity<SearchResult<InspectionSubPoolQueryDto>> searchInspectionSupPool(SearchParam searchParam);

    @RequestMapping(path = "/application-be/rescomdtotype/{recomtype}",method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE },
            consumes = {MediaType.APPLICATION_JSON_VALUE})
    FeignResponseEntity<List<AppPremisesRecommendationDto>> getAppPremisesRecommendationDtoByType(@PathVariable("recomtype") String recomtype);

    @GetMapping(path = "/iais-apppremisescorrelation-be/applicationcorreid/{appCorreId}", produces = { MediaType.APPLICATION_JSON_VALUE },
            consumes = {MediaType.APPLICATION_JSON_VALUE})
    FeignResponseEntity<ApplicationDto> getApplicationByCorreId(@PathVariable("appCorreId") String appCorreId);

    @PostMapping(value = "/iais-application-be/self-decl/results", consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<String>> getSelfDeclChecklistByCorreId(@RequestBody List<String> correIdList);

    @PostMapping(value = "/iais-application-history/historys-kpi",consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<AppPremisesRoutingHistoryDto>> getHistoryForKpi(@RequestBody AppPremisesRoutingHistoryDto appPremisesRoutingHistoryDto );

    @GetMapping(value = "/iais-inspection/appt-date/{appPreCorrId}", produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<AppointmentDto> getApptStartEndDateByAppCorrId(@PathVariable(name = "appPreCorrId") String appPreCorrId);

    @GetMapping(value = "/iais-appt-inspec-be/appt-specific-dto/{appPremCorrId}", produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<AppPremisesInspecApptDto> getSpecificDtoByAppPremCorrId(@PathVariable(name = "appPremCorrId")String appPremCorrId);

    @PostMapping(value = "/iais-appt-inspec-be/appt-specific-dto/list", produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<AppPremisesInspecApptDto>> getSpecificDtosByAppPremCorrIds(@RequestBody List<String> appPremCorrIds);

    @GetMapping(value = "/iais-appt-inspec-be/appt-systemdate-dto/{appPremCorrId}", produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<AppPremisesInspecApptDto>> getSystemDtosByAppPremCorrId(@PathVariable(name = "appPremCorrId")String appPremCorrId);

    @GetMapping(value = "/iais-inspection/itemids/{appPremCorrId}", produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = {MediaType.APPLICATION_JSON_VALUE})
    FeignResponseEntity<List<String>> getItemIdsByAppNo(@PathVariable(name = "appPremCorrId") String appPremCorrId);

    @GetMapping(value = "/iais-apppreinsncitem-be/nc-file-ids/{id}",produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = {MediaType.APPLICATION_JSON_VALUE})
    FeignResponseEntity<List<AppPremPreInspectionNcDocDto>> getFilesByItemId(@PathVariable(name = "id") String id);

    @PostMapping(value = "/iais-inspection/ins-rec-event", produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<EventInspRecItemNcDto> getEventInspRecItemNcDtoByCorrIds(@RequestBody EventInspRecItemNcDto eventInspRecItemNcDto);

    @GetMapping(value = "/iais-inspection/ins-draf-date/{appPreCorrId}")
    FeignResponseEntity<AppPremInsDraftDto> getAppPremInsDraftDtoByAppPreCorrId(@PathVariable(name = "appPreCorrId") String appPreCorrId);

    @GetMapping(value = "/iais-inspection/insp-history-app/{licenceId}", consumes = {MediaType.APPLICATION_JSON_VALUE})
    FeignResponseEntity<List<ApplicationDto>> getInspHistoryAppByLicId(@PathVariable(name = "licenceId") String licenceId);

    /**
     * Kpi stageId sla days
     */
    @PostMapping(value = "/application-be/kpi-sla-dayc", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<AppStageSlaTrackingDto> createAppStageSlaTrackingDto(@RequestBody AppStageSlaTrackingDto appStageSlaTrackingDto);

    @PutMapping(value = "/application-be/kpi-sla-dayu", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<AppStageSlaTrackingDto> updateAppStageSlaTrackingDto(@RequestBody AppStageSlaTrackingDto appStageSlaTrackingDto);

    @GetMapping(value = "/application-be/every-stage-salday/{appNo}")
    FeignResponseEntity<List<AppStageSlaTrackingDto>> getSlaTrackByAppNo(@PathVariable("appNo") String appNo);

    @GetMapping(value = "/application-be/app-stage-salday/{appNo}/{stageId}")
    FeignResponseEntity<AppStageSlaTrackingDto> getSlaTrackByAppNoStageId(@PathVariable("appNo") String appNo, @PathVariable("stageId") String stageId);

    @GetMapping(value = "/application-be/one-stage-salday/{id}")
    FeignResponseEntity<AppStageSlaTrackingDto> searchSlaTrackById(@PathVariable("id") String id);

    @PostMapping(value = "/iais-inspection/history-ext", produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<AppPremisesRoutingHistoryExtDto>> createAppPremisesRoutingHistoryExtDtos(@RequestBody List<AppPremisesRoutingHistoryExtDto> appPremisesRoutingHistoryExtDtos);

    @PostMapping(value = "/iais-appt-inspec-be/app-insp-corrc", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<AppPremInspCorrelationDto>> createAppPremInspCorrelationDto(@RequestBody List<AppPremInspCorrelationDto> appPremInspCorrelationDtos);

    @PutMapping(value = "/iais-appt-inspec-be/app-insp-corru", consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<AppPremInspCorrelationDto> updateAppPremInspCorrelationDto(@RequestBody AppPremInspCorrelationDto appPremInspCorrelationDto);

    @GetMapping(value = "/iais-appt-inspec-be/app-insp-corrf/{appNo}/{userId}/{status}", consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<AppPremInspCorrelationDto>> getAppInspCorreByAppNoUserIdStatus(@PathVariable(name = "appNo")String appNo,
                                                                                            @PathVariable(name = "userId")String userId,
                                                                                            @PathVariable(name = "status")String status);

    @GetMapping(value = "/iais-appt-inspec-be/app-insp-corrs/{userId}/{status}", consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<AppPremInspCorrelationDto>> getAppInspCorreByUserIdStatus(@PathVariable(name = "userId")String userId,
                                                                                       @PathVariable(name = "status")String status);

    @GetMapping(value = "/iais-appt-inspec-be/app-insp-corrt/{appNo}/{status}", consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<AppPremInspCorrelationDto>> getAppInspCorreByAppNoStatus(@PathVariable(name = "appNo")String appNo,
                                                                                      @PathVariable(name = "status")String status);

    @PostMapping(value = "/iais-appt-inspec-be/officer-rescheduling-search")
    FeignResponseEntity<SearchResult<ReschedulingOfficerQueryDto>> officerReSchSearch(@RequestBody SearchParam searchParam);

    @PostMapping(value = "/iais-inspection/re-sch-datas", produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<ReschedulingOfficerDto> reScheduleSaveRouteData(@RequestBody ReschedulingOfficerDto reschedulingOfficerDto);
}

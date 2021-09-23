package com.ecquaria.cloud.moh.iais.service.client;

import com.ecquaria.cloud.moh.iais.common.dto.application.AdhocChecklistItemDto;
import com.ecquaria.cloud.moh.iais.common.dto.application.AppPremPreInspectionNcDocDto;
import com.ecquaria.cloud.moh.iais.common.dto.application.AppPremisesPreInspectionNcItemDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesInspecApptDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcVehicleDto;
import com.ecquaria.cloudfeign.FeignConfiguration;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * @author Shicheng
 * @date 2020/2/18 11:26
 **/
@FeignClient(name = "hcsa-application", configuration = FeignConfiguration.class,
        fallback = ApplicationFeClientFallback.class)
public interface InspectionFeClient {

    @PostMapping(value = "/iais-appt-inspec-fe", consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<AppPremisesInspecApptDto>> createAppPremisesInspecApptDto(@RequestBody List<AppPremisesInspecApptDto> appPremisesInspecApptDtos);

    @PutMapping(value = "/iais-appt-inspec-fe", consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<AppPremisesInspecApptDto> updateAppPremisesInspecApptDto(@RequestBody AppPremisesInspecApptDto appPremisesInspecApptDto);

    @PutMapping(value = "/iais-appt-inspec-fe/appt-others", consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<AppPremisesInspecApptDto>> updateAppPremisesInspecApptDtoList(@RequestBody List<AppPremisesInspecApptDto> appPremisesInspecApptDtos);

    @GetMapping(value = "/iais-appt-inspec-fe/appt-specific-dto/{appPremCorrId}", produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<AppPremisesInspecApptDto> getSpecificDtoByAppPremCorrId(@PathVariable(name = "appPremCorrId")String appPremCorrId);

    @GetMapping(value = "/iais-appt-inspec-fe/appt-systemdate-dto/{appPremCorrId}", produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<AppPremisesInspecApptDto>> getSystemDtosByAppPremCorrId(@PathVariable(name = "appPremCorrId")String appPremCorrId);

    @GetMapping(value = "/iais-appt-inspec-fe/rescheduling-max-version/{appPremCorrId}", produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<Integer> getMaxReschedulingVersion(@PathVariable(name = "appPremCorrId")String appPremCorrId);

    @DeleteMapping(value = "/iais-inspection-fe/file-report-did/{fileId}",produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<String> deleteByFileReportId(@PathVariable(name = "fileId") String fileId);

    @GetMapping(value = "/iais-inspection-fe/nc-item-list/{appNcId}", produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<AppPremisesPreInspectionNcItemDto>> getNcItemDtoListByAppPremNcId(@PathVariable(name = "appNcId") String appNcId);

    @PostMapping(value = "/iais-appt-inspec-fe/appt-systemdate-dtos", produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<AppPremisesInspecApptDto>> getSystemDtosByAppPremCorrIdList(@RequestBody List<String> appPremCorrIds);

    @DeleteMapping(value = "/iais-inspection-fe/rem-nc-doc", produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<AppPremPreInspectionNcDocDto>> deleteNcDocByIds(@RequestBody List<String> ids);

    @GetMapping(value = "/iais-inspection-fe/adhoc-item/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<AdhocChecklistItemDto> getAdhocChecklistItemById(@PathVariable(name = "id") String id);

    /**
     * Vehicle
     * @param appPremCorrId
     * @return AppSvcVehicleDto
     */
    @GetMapping(value = "/halp-fe-svc-vehicle/svc-vehicles/{appPremCorrId}",produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<AppSvcVehicleDto>> getAppSvcVehicleDtoListByCorrId(@PathVariable(name = "appPremCorrId") String appPremCorrId);

    @GetMapping(value = "/halp-fe-svc-vehicle/svc-vehicles-status/{appPremCorrId}/{status}",produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<AppSvcVehicleDto>> getSvcVehicleDtoListByCorrIdStatus(@PathVariable(name = "appPremCorrId") String appPremCorrId,
                                                                                     @PathVariable(name = "status") String status);

    @GetMapping(value = "/halp-fe-svc-vehicle/vehicles-three-asa/{appPremCorrId}/{status}/{actCode}",produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<AppSvcVehicleDto>> getVehiclesByCorrIdStatusAct(@PathVariable(name = "appPremCorrId") String appPremCorrId,
                                                                               @PathVariable(name = "status") String status,
                                                                               @PathVariable(name = "actCode") String actCode);

    @GetMapping(value = "/halp-fe-svc-vehicle/vehicles-status-actcode/{status}/{actCode}",produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<AppSvcVehicleDto>> getVehiclesByStatusAct(@PathVariable(name = "status") String status,
                                                                         @PathVariable(name = "actCode") String actCode);
}

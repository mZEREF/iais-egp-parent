package com.ecquaria.cloud.moh.iais.service.client;


import com.ecquaria.cloud.moh.iais.common.dto.application.AdhocCheckListConifgDto;
import com.ecquaria.cloud.moh.iais.common.dto.application.AdhocChecklistItemDto;
import com.ecquaria.cloud.moh.iais.common.dto.application.ApplicationViewDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesRoutingHistoryDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationGroupDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationLicenceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationListFileDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.BroadcastApplicationDto;
import com.ecquaria.cloudfeign.FeignConfiguration;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * ApplicationClient
 *
 * @author suocheng
 * @date 11/26/2019
 */
@FeignClient(name = "hcsa-application", configuration = FeignConfiguration.class,
        fallback = BelicationClientFallback.class)
public interface BelicationClient {
    @GetMapping(path = "/iais-application-be/applicationview/{correlationId}",produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<ApplicationViewDto> getAppViewByCorrelationId(@PathVariable("correlationId") String correlationId);
    @GetMapping(path = "/iais-application-be/application/{appNo}",produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<ApplicationDto> getAppByNo(@PathVariable("appNo") String appNo);
    @PostMapping(path = "/iais-application/files",produces = MediaType.TEXT_HTML_VALUE,consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<Void> getDownloadFile(@RequestBody ApplicationListFileDto applicationListDtos);
    @GetMapping(path = "/iais-application-group-be/{appGroupId}",produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<ApplicationGroupDto> getAppById(@PathVariable("appGroupId") String appGroupId);
    @PutMapping(path = "/iais-application-group-be",consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<ApplicationGroupDto> updateApplication(@RequestBody ApplicationGroupDto applicationGroupDto);
    @GetMapping(path = "/iais-application-be/applications/{appGropId}",produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<ApplicationDto>> getGroupAppsByNo(@PathVariable("appGropId") String appGropId);
    @PutMapping(path = "/iais-application-be",consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<ApplicationDto> updateApplication(@RequestBody ApplicationDto applicationDto);
    @PostMapping(path = "/iais-application-history",consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<AppPremisesRoutingHistoryDto> create(@RequestBody AppPremisesRoutingHistoryDto appPremisesRoutingHistoryDto);
    @GetMapping(path = "/iais-application-be/application-group",produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<ApplicationLicenceDto>> getGroup(@RequestParam(name = "day", required = false) Integer day);

    @PutMapping(path = "/iais-application-group-be/groups",consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<ApplicationGroupDto>> updateApplications(@RequestBody List<ApplicationGroupDto> applicationGroupDtos);

    @GetMapping(value = "/iais-application-group-be/app-group-by-licensee-id/{licenseeId}")
    FeignResponseEntity<List<ApplicationGroupDto>> getAppGrpsByLicenseeId(@PathVariable(name = "licenseeId") String licenseeId);

    @PostMapping(path = "/iais-broadcast",consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<BroadcastApplicationDto> createBroadcast(@RequestBody BroadcastApplicationDto broadcastApplicationDto);

    @PostMapping(value = "/iais-adhoc-checklist-conifg/adhoc-items", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<AdhocChecklistItemDto>> saveAdhocChecklist(@RequestBody AdhocCheckListConifgDto adhocConfigDto);

    @GetMapping(value = "/iais-adhoc-checklist-conifg/adhocchecklistbyappremid/{appremId}",consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<AdhocChecklistItemDto>> getAdhocByAppPremCorrId(@PathVariable(name = "appremId") String appremId);

    @PostMapping(path = "/iais-adhoc-checklist-conifg/singleconfig",produces = { MediaType.APPLICATION_JSON_VALUE },
            consumes = {MediaType.APPLICATION_JSON_VALUE})
    FeignResponseEntity<AdhocCheckListConifgDto> saveAppAdhocConfig(@RequestBody AdhocCheckListConifgDto adhocCheckListConifgDto);


    @GetMapping(value = "/iais-adhoc-checklist-conifg/adhoc-checklist/config/{premId}",consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<AdhocCheckListConifgDto> getAdhocConfigByAppPremCorrId(@PathVariable(name = "premId") String appremId);

    @PostMapping(path = "/iais-adhoc-checklist-conifg/itemstorage",produces = { MediaType.APPLICATION_JSON_VALUE },
            consumes = {MediaType.APPLICATION_JSON_VALUE})
    FeignResponseEntity<List<AdhocChecklistItemDto>> saveAdhocItems(@RequestBody List<AdhocChecklistItemDto> itemDtoList);

    @GetMapping(value = "/iais-application-be/doDelete",produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<Void> doDeleteBySql(@RequestParam("sql") String sql);
}

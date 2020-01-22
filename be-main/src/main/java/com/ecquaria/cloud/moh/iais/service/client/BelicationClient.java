package com.ecquaria.cloud.moh.iais.service.client;


import com.ecquaria.cloud.moh.iais.common.dto.application.AdhocCheckListConifgDto;
import com.ecquaria.cloud.moh.iais.common.dto.application.AdhocChecklistItemDto;
import com.ecquaria.cloud.moh.iais.common.dto.application.ApplicationViewDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesRoutingHistoryDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcPremisesScopeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationGroupDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationLicenceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationListFileDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.BroadcastApplicationDto;
import com.ecquaria.cloudfeign.FeignConfiguration;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import java.util.List;
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
 * ApplicationClient
 *
 * @author suocheng
 * @date 11/26/2019
 */
@FeignClient(name = "hcsa-application", configuration = FeignConfiguration.class,
        fallback = BelicationClientFallback.class)
public interface BelicationClient {
    @RequestMapping(path = "/iais-application-be/applicationview/{correlationId}",method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<ApplicationViewDto> getAppViewByCorrelationId(@PathVariable("correlationId") String correlationId);
    @RequestMapping(path = "/iais-application-be/application/{appNo}",method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<ApplicationDto> getAppByNo(@PathVariable("appNo") String appNo);
    @RequestMapping(path = "/iais-application/files",method = RequestMethod.POST,produces = MediaType.TEXT_HTML_VALUE,consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<Void> getDownloadFile(@RequestBody ApplicationListFileDto applicationListDtos);
    @RequestMapping(path = "/iais-application/list-application-dto",method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<ApplicationDto>> getApplicationDto();
    @RequestMapping(path = "/iais-application-group-be/{appGroupId}",method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<ApplicationGroupDto> getAppById(@PathVariable("appGroupId") String appGroupId);
    @RequestMapping(path = "/iais-application-group-be",method = RequestMethod.PUT,consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<ApplicationGroupDto> updateApplication(@RequestBody ApplicationGroupDto applicationGroupDto);
    @RequestMapping(path = "/iais-application-be/applications/{appGropId}",method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<ApplicationDto>> getGroupAppsByNo(@PathVariable("appGropId") String appGropId);
    @RequestMapping(path = "/iais-application-be",method = RequestMethod.PUT,consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<ApplicationDto> updateApplication(@RequestBody ApplicationDto applicationDto);
    @RequestMapping(path = "/iais-application-history",method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<AppPremisesRoutingHistoryDto> create(@RequestBody AppPremisesRoutingHistoryDto appPremisesRoutingHistoryDto);
    @RequestMapping(path = "/iais-application-be/application-group",method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<ApplicationLicenceDto>> getGroup(@RequestParam(name = "day", required = false) Integer day);

    @RequestMapping(path = "/iais-application-group-be/groups",method = RequestMethod.PUT,consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<ApplicationGroupDto>> updateApplications(@RequestBody List<ApplicationGroupDto> applicationGroupDtos);

    @GetMapping(value = "/iais-application-be/application/premises-scope/{correId}")
    FeignResponseEntity<List<AppSvcPremisesScopeDto>> getAppSvcPremisesScopeListByCorreId(@PathVariable(name = "correId") String correId);

    @RequestMapping(path = "/iais-broadcast",method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<BroadcastApplicationDto> createBroadcast(@RequestBody BroadcastApplicationDto broadcastApplicationDto);

    @PostMapping(value = "/iais-adhoc-checklist-conifg/adhoc-items", consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<String> saveAdhocChecklist(@RequestBody AdhocCheckListConifgDto adhocConfigDto);

    @GetMapping(value = "/iais-adhoc-checklist-conifg/adhocchecklistbyappremid/{appremId}",consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<AdhocChecklistItemDto>> getAdhocByAppPremCorrId(@PathVariable(name = "appremId") String appremId);

    @PostMapping(path = "/iais-adhoc-checklist-conifg/singleconfig",produces = { MediaType.APPLICATION_JSON_VALUE },
            consumes = {MediaType.APPLICATION_JSON_VALUE})
    FeignResponseEntity<AdhocCheckListConifgDto> saveAppAdhocConfig(@RequestBody AdhocCheckListConifgDto adhocCheckListConifgDto);


    @GetMapping(value = "/iais-adhoc-checklist-conifg/adhoccheckconfigbyappremid/{premId}",consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<AdhocCheckListConifgDto> getAdhocConfigByAppPremCorrId(@PathVariable(name = "premId") String appremId);

    @PostMapping(path = "/iais-adhoc-checklist-conifg/itemstorage",produces = { MediaType.APPLICATION_JSON_VALUE },
            consumes = {MediaType.APPLICATION_JSON_VALUE})
    FeignResponseEntity<List<AdhocChecklistItemDto>> saveAdhocItems(@RequestBody List<AdhocChecklistItemDto> itemDtoList);


}

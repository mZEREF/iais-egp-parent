package com.ecquaria.cloud.moh.iais.service.client;

/*
 *author: yichen
 *date time:11/28/2019 2:44 PM
 *description:
 */

import com.ecquaria.cloud.moh.iais.common.dto.IaisApiResult;
import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.application.ChecklistQuestionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.checklist.CheckItemQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.checklist.ChecklistConfigDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.checklist.ChecklistConfigQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.checklist.ChecklistItemDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.checklist.ChecklistSectionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.checklist.ChecklistSectionItemDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.checklist.HcsaChklSvcRegulationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.checklist.ChecklistItemExcel;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.checklist.RegulationQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceSubTypeDto;
import com.ecquaria.cloud.moh.iais.common.dto.message.ErrorMsgContent;
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

@FeignClient(name = "hcsa-config", configuration = {FeignConfiguration.class},
        fallback = HcsaChklFallback.class)
public interface HcsaChklClient {
    @PostMapping(path = "/iais-hcsa-checklist/config/status", consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<String> inActiveConfig(@RequestBody String confId);

    @PostMapping(path = "/iais-hcsa-checklist/item/status", consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<Boolean> inActiveItem(@RequestBody String itemId);

    @PostMapping(path = "/iais-hcsa-checklist/config/results", consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<SearchResult<ChecklistConfigQueryDto>> listChecklistConfig(SearchParam searchParam);

    @PostMapping(path = "/iais-hcsa-checklist/item/results", consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<SearchResult<CheckItemQueryDto>> listChklItem(SearchParam searchParam);

    @PostMapping(path = "/iais-hcsa-checklist/item/items-by-ids", consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<ChecklistItemDto>>  listChklItemByItemId(List<String> itemIds);

    @GetMapping(path = "/iais-hcsa-checklist/item/{id}")
    FeignResponseEntity<ChecklistItemDto> getChklItemById(@PathVariable(value = "id") String id);

    @GetMapping(path = "/iais-hcsa-checklist/procedures/gen/config-id")
    FeignResponseEntity<String> callProceduresGenUUID();

    @PostMapping(path = "/iais-hcsa-checklist/item", consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<IaisApiResult<ChecklistItemDto>> saveChklItem(ChecklistItemDto itemDto);

    @GetMapping(path = "/iais-hcsa-checklist/regulation/regulation-clauses-distinct")
    FeignResponseEntity<List<HcsaChklSvcRegulationDto>> listRegulationClauseNo();

    @PostMapping(value = "/iais-regulation/regulation", consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<IaisApiResult<HcsaChklSvcRegulationDto>> createRegulation(@RequestBody HcsaChklSvcRegulationDto regulationDto);

    @PutMapping(value = "/iais-regulation/regulation", consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<IaisApiResult<HcsaChklSvcRegulationDto>> updateRegulation(@RequestBody HcsaChklSvcRegulationDto regulationDto);

    @GetMapping(path = "/iais-hcsa-checklist/regulations")
    FeignResponseEntity<List<HcsaChklSvcRegulationDto>> getAllRegulation();

    @GetMapping(path = "/iais-hcsa-checklist/items")
    FeignResponseEntity<List<ChecklistItemDto>> getAllChecklistItem();

    @GetMapping(path = "/iais-hcsa-checklist/configs")
    FeignResponseEntity<List<ChecklistConfigDto>> getAllChecklistConfig();

    @GetMapping(path = "/iais-hcsa-checklist/sections")
    FeignResponseEntity<List<ChecklistSectionDto>> getAllSection();

    @GetMapping(path = "/iais-hcsa-checklist/section-items")
    FeignResponseEntity<List<ChecklistSectionItemDto>> getAllSectionItem();

    @PutMapping(value = "/iais-regulation/regulation/{id}")
    FeignResponseEntity<Boolean> deleteRegulation(@PathVariable("id") String regulationId);

    @PostMapping(path = "/iais-hcsa-checklist/item/items-clone", consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<Boolean> submitCloneItem(List<ChecklistItemDto> hcsaChklItemDtos);

    @PostMapping(path = "/iais-hcsa-checklist/item/items-upload", consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<IaisApiResult<List<ErrorMsgContent>>> saveUploadItems(List<ChecklistItemExcel> checklistItemList);

    @PostMapping(path = "/iais-hcsa-checklist/item/items-upload-update", consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<IaisApiResult<List<ErrorMsgContent>>> updateUploadItems(List<ChecklistItemExcel> checklistItemList);

    @PostMapping(path = "/iais-hcsa-checklist/config", consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<ChecklistConfigDto> submitConfig(ChecklistConfigDto checklistConfigDto);

    @GetMapping(path = "/iais-hcsa-service/subtype-name-results")
    FeignResponseEntity<List<String>> listSubTypeName();

    @GetMapping(path = "/iais-hcsa-service/subtype-phase1-results")
    FeignResponseEntity<List<HcsaServiceSubTypeDto>> listSubTypePhase1();

    @GetMapping(path = "/iais-hcsa-checklist/regulation/results")
    FeignResponseEntity<List<HcsaChklSvcRegulationDto>> getRegulationClauseListIsActive();

    @GetMapping(path = "/iais-hcsa-service/svc-name-results")
    FeignResponseEntity<List<String>> listServiceName(@RequestParam(value = "type", required = false) String type);

    @GetMapping(path = "/iais-hcsa-checklist/config/{id}")
    FeignResponseEntity<ChecklistConfigDto> getChecklistConfigById(@PathVariable(value = "id") String configId);

    @GetMapping(path = "/iais-hcsa-checklist/config/results/{svcCode}/{svcType}", produces = { MediaType.APPLICATION_JSON_VALUE})
    FeignResponseEntity<List<ChecklistQuestionDto>> getcheckListQuestionDtoList(@PathVariable(value = "svcCode") String svcCode, @PathVariable(value = "svcType") String svcType);

    @GetMapping(path = "/iais-hcsa-checklist/regulation/{id}", produces = { MediaType.APPLICATION_JSON_VALUE})
    FeignResponseEntity<HcsaChklSvcRegulationDto> getRegulationDtoById(@PathVariable(value = "id") String id);

    @PostMapping(path = "/iais-hcsa-service/hcsa-service-by-ids", consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<HcsaServiceDto>> getHcsaServiceByIds(@RequestBody List<String> serviceId);

    @GetMapping(value = "/iais-hcsa-checklist/config/results-max-version/{svcCode}/{type}/{module}")
    FeignResponseEntity<ChecklistConfigDto> getMaxVersionConfigByParams(@PathVariable("svcCode")String svcCode,
                                @PathVariable("type")String type,
                                @PathVariable("module") String module);

    @GetMapping(value = "/iais-hcsa-checklist/config/results-max-version/{svcCode}/{type}/{module}/{subTypeName}")
    FeignResponseEntity<ChecklistConfigDto> getMaxVersionConfigByParams(@PathVariable("svcCode")String svcCode,
                                @PathVariable("type")String type,
                                @PathVariable("module") String module,
                                @PathVariable(value = "subTypeName", required = false) String subTypeName);

    @GetMapping(value = "/iais-hcsa-checklist/config/results-max-version/{svcCode}/{type}/{module}/inspection-entity/{inspectionEntity}")
    FeignResponseEntity<ChecklistConfigDto> getMaxVersionInspectionEntityConfig(@PathVariable("svcCode")String svcCode,
                                @PathVariable("type")String type,
                                @PathVariable("module") String module,
                                @PathVariable(value = "inspectionEntity") String inspectionEntity);

    @GetMapping(value = "/iais-hcsa-checklist/config/results-max-version/{svcCode}/{type}/{module}/{subTypeName}/{hciCode}")
    FeignResponseEntity<ChecklistConfigDto> getMaxVersionConfigByParams(@PathVariable("svcCode")String svcCode,
                                                                        @PathVariable("type")String type,
                                                                        @PathVariable("module") String module,
                                                                        @PathVariable(value = "subTypeName", required = false) String subTypeName,
                                                                        @PathVariable(value = "hciCode", required = false) String hciCode);

    @GetMapping(value = "/iais-hcsa-checklist/service-config/results-max-version/")
    FeignResponseEntity<ChecklistConfigDto> getMaxVersionServiceConfigByParams(@RequestParam("svcCode")String svcCode,
                                                                        @RequestParam("type")String type,
                                                                        @RequestParam("module") String module,
                                                                        @RequestParam(value = "subTypeName", required = false) String subTypeName,
                                                                        @RequestParam(value = "hciCode", required = false) String hciCode);

    @GetMapping(path = "/iais-hcsa-checklist/common-config-max-version/results", produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<ChecklistConfigDto> getMaxVersionCommonConfig();

    @PostMapping(path = "/iais-hcsa-checklist/config/record/validation", consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<Boolean> isExistsRecord(@RequestBody ChecklistConfigDto configDto);

    @PostMapping(value = "/iais-hcsa-checklist/regulation/", consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<IaisApiResult<List<ErrorMsgContent>>> submitHcsaChklSvcRegulation(@RequestBody List<HcsaChklSvcRegulationDto> regulationList);

    @PostMapping(value = "/iais-regulation/results",consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<SearchResult<RegulationQueryDto>> searchRegulation(@RequestBody SearchParam searchParam);

    @PostMapping(value = "/iais-hcsa-checklist/config-excel-template", consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<ErrorMsgContent>> saveConfigByTemplate(@RequestBody ChecklistConfigDto excelTemplate);

    @PostMapping(value = "/iais-regulation/results/ids", consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<HcsaChklSvcRegulationDto>> listRegulationByIds(@RequestBody List<String> ids);
}
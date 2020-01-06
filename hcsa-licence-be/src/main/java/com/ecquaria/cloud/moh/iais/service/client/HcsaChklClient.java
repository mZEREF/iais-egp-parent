package com.ecquaria.cloud.moh.iais.service.client;

/*
 *author: yichen
 *date time:11/28/2019 2:44 PM
 *description:
 */

import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.application.ChecklistQuestionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.checklist.CheckItemQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.checklist.ChecklistConfigDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.checklist.ChecklistConfigQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.checklist.ChecklistItemDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.checklist.HcsaChklSvcRegulationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceDto;
import com.ecquaria.cloudfeign.FeignConfiguration;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

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

    @GetMapping(path = "/iais-hcsa-checklist/item/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<ChecklistItemDto> getChklItemById(@PathVariable(value = "id") String id);

    @PostMapping(path = "/iais-hcsa-checklist/item", consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<String> saveChklItem(ChecklistItemDto itemDto);

    @GetMapping(path = "/iais-hcsa-checklist/regulation/regulation-clauses-distinct")
    FeignResponseEntity<List<String>> listRegulationClauseNo();

    @PostMapping(path = "/iais-hcsa-checklist/item/items-clone", consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<String> submitCloneItem(List<ChecklistItemDto> hcsaChklItemDtos);

    @PostMapping(path = "/iais-hcsa-checklist/item/items-upload", consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<String> submitUploadItem(List<ChecklistItemDto> checklistItemList);

    @PostMapping(path = "/iais-hcsa-checklist/config", consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<ChecklistConfigDto> submitConfig(ChecklistConfigDto checklistConfigDto);

    @GetMapping(path = "/iais-hcsa-service/subtype-name-results")
    FeignResponseEntity<List<String>> listSubTypeName();

    @GetMapping(path = "/iais-hcsa-service/svc-name-results")
    FeignResponseEntity<List<String>> listServiceName();

    @GetMapping(path = "/iais-hcsa-checklist/config/{id}")
    FeignResponseEntity<ChecklistConfigDto> getChecklistConfigById(@PathVariable(value = "id") String configId);

    @GetMapping(path = "/iais-hcsa-checklist/config/results/{svcCode}/{svcType}", produces = { MediaType.APPLICATION_JSON_VALUE})
    FeignResponseEntity<List<ChecklistQuestionDto>> getcheckListQuestionDtoList(@PathVariable(value = "svcCode") String svcCode, @PathVariable(value = "svcType") String svcType);

    @GetMapping(path = "/iais-hcsa-checklist/self-desc/RegulationDtoById/{id}", produces = { MediaType.APPLICATION_JSON_VALUE})
    FeignResponseEntity<HcsaChklSvcRegulationDto> getRegulationDtoById(@PathVariable(value = "id") String svcCode);

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

    @GetMapping(path = "/iais-hcsa-checklist/common-config-max-version/results", produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<ChecklistConfigDto> getMaxVersionCommonConfig();

    @PostMapping(path = "/iais-hcsa-checklist/config/record/validation", consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<Boolean> isExistsRecord(@RequestBody ChecklistConfigDto configDto);

    @PostMapping(value = "/iais-hcsa-checklist/regulation/", consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<String> submitHcsaChklSvcRegulation(@RequestBody List<HcsaChklSvcRegulationDto> regulationList);
}
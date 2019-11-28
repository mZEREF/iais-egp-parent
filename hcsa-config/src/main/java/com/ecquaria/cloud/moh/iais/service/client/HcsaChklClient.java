package com.ecquaria.cloud.moh.iais.service.client;

/*
 *author: yichen
 *date time:11/28/2019 2:44 PM
 *description:
 */

import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.checklist.CheckItemQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.checklist.ChecklistConfigDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.checklist.ChecklistItemDto;
import com.ecquaria.cloudfeign.FeignConfiguration;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@FeignClient(name = "hcsa-config", configuration = {FeignConfiguration.class},
        fallback = HcsaChklFallback.class)
public interface HcsaChklClient {

    @DeleteMapping(path = "/iais-hcsa-checklist/config/{id}")
    FeignResponseEntity<Void> deleteRecord(String confId);

    @PostMapping(path = "/iais-hcsa-checklist/config/results", consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<SearchResult> listChecklistConfig(SearchParam searchParam);

    @PostMapping(path = "/iais-hcsa-checklist/item/results", consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<SearchResult<CheckItemQueryDto>> listChklItem(SearchParam searchParam);

    @PostMapping(path = "/iais-hcsa-checklist/item/items-by-ids", consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<ChecklistItemDto>>  listChklItemByItemId(List<String> itemIds);

    @GetMapping(path = "/iais-hcsa-checklist/item")
    FeignResponseEntity<ChecklistItemDto> getChklItemById(String id);

    @PostMapping(path = "/iais-hcsa-checklist/item", consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<Void> saveChklItem(ChecklistItemDto itemDto);

    @GetMapping(path = "/iais-hcsa-checklist/regulation/regulation-clauses-distinct")
    FeignResponseEntity<List<String>> listRegulationClauseNo();

    @PostMapping(path = "/iais-hcsa-checklist/item/items-clone", consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<Void> submitCloneItem(List<ChecklistItemDto> hcsaChklItemDtos);

    @PostMapping(path = "/iais-hcsa-checklist/config", consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<Void> submitConfig(ChecklistConfigDto checklistConfigDto);

    @GetMapping(path = "/iais-hcsa-service/subtype-name-results")
    FeignResponseEntity<List<String>> listSubTypeName();

    @GetMapping(path = "/iais-hcsa-service/subtype-name-results")
    FeignResponseEntity<List<String>> listServiceName();
}
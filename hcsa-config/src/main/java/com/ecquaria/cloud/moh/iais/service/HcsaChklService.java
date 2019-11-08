package com.ecquaria.cloud.moh.iais.service;

/*
 *author: yichen
 *date time:9/25/2019 2:26 PM
 *description:
 */

import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.checklist.CheckItemQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.checklist.ChecklistConfigQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.checklist.ChecklistItemDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface HcsaChklService {
    SearchResult<CheckItemQueryDto> listChklItem(SearchParam searchParam);
    SearchResult<ChecklistConfigQueryDto> listChecklistConfig(SearchParam searchParam);

    List<ChecklistItemDto> listChklItemByItemId(List<String> itemIds);

    ChecklistItemDto getChklItemById(String id);

    void saveChklItem(ChecklistItemDto itemDto);

    List<String> listRegulationClauseNo();

    void submitCloneItem(List<ChecklistItemDto> hcsaChklItemDtos);

}

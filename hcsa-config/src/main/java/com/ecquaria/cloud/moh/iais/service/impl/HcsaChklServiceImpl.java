package com.ecquaria.cloud.moh.iais.service.impl;

/*
 *author: yichen
 *date time:9/24/2019 5:10 PM
 *description:
 */

import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.checklist.CheckItemQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.checklist.ChecklistConfigQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.checklist.ChecklistItemDto;
import com.ecquaria.cloud.moh.iais.common.utils.RestApiUtil;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.service.HcsaChklService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class HcsaChklServiceImpl implements HcsaChklService {

    @Override
    public SearchResult<CheckItemQueryDto> listChklItem(SearchParam searchParam) {
        return RestApiUtil.query("hcsa-config:8878/iais-hcsa-chkl/chklitem/results", searchParam);
    }

    @Override
    public SearchResult<ChecklistConfigQueryDto> listChecklistConfig(SearchParam searchParam) {
        return null;
    }

    /**
     * Gets the value on the page
     * @param itemIds
     * @return
     */
    @Override
    public List<ChecklistItemDto> listChklItemByItemId(List<String> itemIds) {
        return  RestApiUtil.postGetList("hcsa-config:8878/iais-hcsa-chkl/chklitem/items-by-ids", itemIds, ChecklistItemDto.class);
    }

    @Override
    public ChecklistItemDto getChklItemById(String id) {
        return IaisEGPHelper.getRecordByPrimaryKey("hcsa-config:8878/iais-hcsa-chkl/chklitem", id, ChecklistItemDto.class);
    }

    @Override
    public void saveChklItem(ChecklistItemDto itemDto) {
        RestApiUtil.save("hcsa-config:8878/iais-hcsa-chkl/chklitem", itemDto);
    }

    @Override

    /**
    * @description:  Get the deduplicated clause
    * @param: []
    * @return: java.util.List<java.lang.String>
    * @author: yichen
    */
    public List<String> listRegulationClauseNo() {
        return RestApiUtil.getList("hcsa-config:8878/iais-hcsa-chkl/regulation/clauses-distinct", List.class);
    }

    @Override
    public void submitCloneItem(List<ChecklistItemDto> hcsaChklItemDtos) {
        RestApiUtil.save("hcsa-config:8878/iais-hcsa-chkl/chklitem/items-clone", hcsaChklItemDtos);
    }
}

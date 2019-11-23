package com.ecquaria.cloud.moh.iais.service.impl;

/*
 *author: yichen
 *date time:9/24/2019 5:10 PM
 *description:
 */

import com.ecquaria.cloud.moh.iais.common.constant.rest.RestApiUrlConsts;
import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.checklist.CheckItemQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.checklist.ChecklistConfigDto;
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
        return RestApiUtil.query(RestApiUrlConsts.HCSA_CONFIG +  RestApiUrlConsts.CHECKLIST_ITEM_RESULTS, searchParam);
    }

    @Override
    public SearchResult<ChecklistConfigQueryDto> listChecklistConfig(SearchParam searchParam) {
        return RestApiUtil.query(RestApiUrlConsts.HCSA_CONFIG + RestApiUrlConsts.CHECKLIST_CONFIG_RESULTS, searchParam);
    }


    @Override
    public List<ChecklistItemDto> listChklItemByItemId(List<String> itemIds) {
        return  RestApiUtil.postGetList(RestApiUrlConsts.HCSA_CONFIG +  RestApiUrlConsts.CHECKLIST_ITEM_BY_IDS, itemIds, ChecklistItemDto.class);
    }

    @Override
    public ChecklistItemDto getChklItemById(String id) {
        return IaisEGPHelper.getRecordByPrimaryKey(RestApiUrlConsts.HCSA_CONFIG + RestApiUrlConsts.HCSA_CONFIG_CHECKLIST_ITEM_SLD_URL, id, ChecklistItemDto.class);
    }


    @Override
    public void saveChklItem(ChecklistItemDto itemDto) {
        RestApiUtil.save(RestApiUrlConsts.HCSA_CONFIG +  RestApiUrlConsts.HCSA_CONFIG_CHECKLIST_ITEM_SLD_URL, itemDto);
    }

    @Override

    public List<String> listRegulationClauseNo() {
        return RestApiUtil.getList(RestApiUrlConsts.HCSA_CONFIG + RestApiUrlConsts.DISTINCT_REGULATION_CLAUSES, String.class);
    }


    @Override
    public void submitCloneItem(List<ChecklistItemDto> hcsaChklItemDtos) {
        RestApiUtil.save(RestApiUrlConsts.HCSA_CONFIG +  RestApiUrlConsts.CHECKLIST_ITEM_CLONE, hcsaChklItemDtos);
    }

    @Override
    public void submitConfig(ChecklistConfigDto checklistConfigDto) {
        RestApiUtil.save(RestApiUrlConsts.HCSA_CONFIG +  RestApiUrlConsts.HCSA_CONFIG_CHECKLIST_CONFIG_SLD_URL, checklistConfigDto);
    }

    @Override
    public List<String> listSubTypeName() {
        return RestApiUtil.getList(RestApiUrlConsts.GET_HCSA_SUBTYPE_NAME_RESULTS, String.class);
    }

    @Override
    public List<String> listServiceName() {
        return RestApiUtil.getList(RestApiUrlConsts.GET_HCSA_SVC_NAME_RESULTS, String.class);
    }
}

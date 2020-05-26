package com.ecquaria.cloud.moh.iais.service.impl;

/*
 *author: yichen
 *date time:9/24/2019 5:10 PM
 *description:
 */

import com.ecquaria.cloud.moh.iais.annotation.SearchTrack;
import com.ecquaria.cloud.moh.iais.common.dto.IaisApiResult;
import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.checklist.CheckItemQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.checklist.ChecklistConfigDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.checklist.ChecklistConfigQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.checklist.ChecklistItemDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.checklist.HcsaChklSvcRegulationDto;
import com.ecquaria.cloud.moh.iais.common.dto.message.ErrorMsgContent;
import com.ecquaria.cloud.moh.iais.service.HcsaChklService;
import com.ecquaria.cloud.moh.iais.service.client.FillUpCheckListGetAppClient;
import com.ecquaria.cloud.moh.iais.service.client.HcsaChklClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class HcsaChklServiceImpl implements HcsaChklService {

    @Autowired
    private HcsaChklClient chklClient;

    @Autowired
    private FillUpCheckListGetAppClient fillUpCheckListGetAppClient;

    @Override
    public Boolean deleteRecord(String configId) {
        chklClient.inActiveConfig(configId);
        return Boolean.TRUE;
    }

    @Override
    public Boolean inActiveItem(String itemId) {
        return chklClient.inActiveItem(itemId).getEntity();
    }

    @Override
    @SearchTrack(catalog = "hcsaconfig",key = "listChklItem")
    public SearchResult<CheckItemQueryDto> listChklItem(SearchParam searchParam) {
        return chklClient.listChklItem(searchParam).getEntity();
    }

    @Override
    public Map<String, Boolean> configUsageStatus(List<String> configId) {
        return fillUpCheckListGetAppClient.configUsageStatus(configId).getEntity();
    }

    @Override
    public List<HcsaChklSvcRegulationDto> getRegulationClauseListIsActive() {
        return chklClient.getRegulationClauseListIsActive().getEntity();
    }

    @Override
    @SearchTrack(catalog = "hcsaconfig",key = "listChecklistConfig")
    public SearchResult<ChecklistConfigQueryDto> listChecklistConfig(SearchParam searchParam) {
        return chklClient.listChecklistConfig(searchParam).getEntity();
    }


    @Override
    public List<ChecklistItemDto> listChklItemByItemId(List<String> itemIds) {
        return  chklClient.listChklItemByItemId(itemIds).getEntity();
    }

    @Override
    public ChecklistItemDto getChklItemById(String id) {
        return chklClient.getChklItemById(id).getEntity();
    }


    @Override
    public IaisApiResult<ChecklistItemDto> saveChklItem(ChecklistItemDto itemDto) {
       return chklClient.saveChklItem(itemDto).getEntity();
    }

    @Override

    public List<String> listRegulationClauseNo() {
        return chklClient.listRegulationClauseNo().getEntity();
    }


    @Override
    public Boolean submitCloneItem(List<ChecklistItemDto> hcsaChklItemDtos) {
        return chklClient.submitCloneItem(hcsaChklItemDtos).getEntity();
    }

    @Override
    public ChecklistConfigDto submitConfig(ChecklistConfigDto checklistConfigDto) {
        return chklClient.submitConfig(checklistConfigDto).getEntity();
    }

    @Override
    public List<String> listSubTypeName() {
        return chklClient.listSubTypeName().getEntity();
    }

    @Override
    public List<String> listServiceName() {
        return chklClient.listServiceName().getEntity();
    }

    @Override
    public ChecklistConfigDto getChecklistConfigById(String id) {
        return chklClient.getChecklistConfigById(id).getEntity();
    }

    @Override
    public Boolean isExistsRecord(ChecklistConfigDto configDto){
        return chklClient.isExistsRecord(configDto).getEntity();
    }

    @Override
    public List<ErrorMsgContent> submitUploadItems(List<ChecklistItemDto> uploadItems) {
        IaisApiResult<List<ErrorMsgContent>> iaisApiResult = chklClient.saveUploadItems(uploadItems).getEntity();
        return iaisApiResult.getEntity();
    }
}

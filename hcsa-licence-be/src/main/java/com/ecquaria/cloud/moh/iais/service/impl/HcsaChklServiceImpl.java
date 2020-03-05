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
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.checklist.*;
import com.ecquaria.cloud.moh.iais.common.exception.IaisRuntimeException;
import com.ecquaria.cloud.moh.iais.service.HcsaChklService;
import com.ecquaria.cloud.moh.iais.service.client.HcsaChklClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class HcsaChklServiceImpl implements HcsaChklService {

    @Autowired
    private HcsaChklClient chklClient;

    @Value("${iais.hmac.keyId}")
    private String keyId;
    @Value("${iais.hmac.second.keyId}")
    private String secKeyId;

    @Value("${iais.hmac.secretKey}")
    private String secretKey;
    @Value("${iais.hmac.second.secretKey}")
    private String secSecretKey;

    @Override
    public void deleteRecord(String configId) {
        chklClient.inActiveConfig(configId);
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
    public String submitCloneItem(List<ChecklistItemDto> hcsaChklItemDtos) {
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
    public String submitUploadRegulation(List<HcsaChklSvcRegulationDto> regulationExcelList) {
        String retJson = chklClient.submitHcsaChklSvcRegulation(regulationExcelList).getEntity();

        try {
            //sync to fe
            List<HcsaChklSvcRegulationDto> allRegulation = chklClient.getAllRegulation().getEntity();

        }catch (IaisRuntimeException e){
            log.error("encounter failure when sync regulation to fe" + e.getMessage());
        }

        return retJson;
    }

    @Override
    public String submitUploadItem(List<ChecklistItemDto> checklistItemExcelList) {
        return chklClient.submitUploadItem(checklistItemExcelList).getEntity();
    }


}

package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.mastercode.MasterCodeCategoryDto;
import com.ecquaria.cloud.moh.iais.common.dto.mastercode.MasterCodeDto;
import com.ecquaria.cloud.moh.iais.common.dto.mastercode.MasterCodeQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.mastercode.MasterCodeToExcelDto;
import com.ecquaria.cloud.moh.iais.service.MasterCodeService;
import com.ecquaria.cloud.moh.iais.service.client.MasterCodeClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * @author hc
 */
@Service
@Slf4j
public class MasterCodeServiceImpl implements MasterCodeService {

    @Autowired
    private MasterCodeClient masterCodeClient;

    @Override
    public SearchResult<MasterCodeQueryDto> doQuery(SearchParam param) {
        return masterCodeClient.doQuery(param).getEntity();
    }

    @Override
    public List<MasterCodeToExcelDto> findAllMasterCode() {
        return masterCodeClient.findAllMasterCode().getEntity();
    }

    @Override
    public MasterCodeDto saveMasterCode(MasterCodeDto masterCode) {
        return masterCodeClient.saveMasterCode(masterCode).getEntity();
    }

    @Override
    public MasterCodeDto updateMasterCode(MasterCodeDto masterCode) {
        return masterCodeClient.updateMasterCode(masterCode).getEntity();
    }

    @Override
    public void deleteMasterCodeById(String id) {
        masterCodeClient.delMasterCode(id).getEntity();
    }

    @Override
    public MasterCodeDto findMasterCodeByMcId(String id) {
        return masterCodeClient.getMasterCodeById(id).getEntity();
    }

    @Override
    public String findCodeCategoryByDescription(String description) {
        return masterCodeClient.getCodeCategoryByDescription(description).getEntity();
    }

    @Override
    public MasterCodeCategoryDto saveMasterCodeCategory(MasterCodeCategoryDto masterCodeCategoryDto) {
        return masterCodeClient.saveMasterCodeCategory(masterCodeCategoryDto).getEntity();
    }

    @Override
    public String findCodeKeyByDescription(String description) {
        return masterCodeClient.getCodeKeyByDescription(description).getEntity();
    }

}

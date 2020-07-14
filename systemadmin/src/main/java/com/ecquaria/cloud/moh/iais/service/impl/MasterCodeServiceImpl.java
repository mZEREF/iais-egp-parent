package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.mastercode.MasterCodeCategoryDto;
import com.ecquaria.cloud.moh.iais.common.dto.mastercode.MasterCodeDto;
import com.ecquaria.cloud.moh.iais.common.dto.mastercode.MasterCodeQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.mastercode.MasterCodeToExcelDto;
import com.ecquaria.cloud.moh.iais.service.MasterCodeService;
import com.ecquaria.cloud.moh.iais.service.client.SaMasterCodeClient;
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
    private SaMasterCodeClient saMasterCodeClient;

    @Override
    public SearchResult<MasterCodeQueryDto> doQuery(SearchParam param) {
        return saMasterCodeClient.doQuery(param).getEntity();
    }

    @Override
    public List<MasterCodeToExcelDto> findAllMasterCode() {
        return saMasterCodeClient.findAllMasterCode().getEntity();
    }

    @Override
    public MasterCodeDto saveMasterCode(MasterCodeDto masterCode) {
        return saMasterCodeClient.saveMasterCode(masterCode).getEntity();
    }

    @Override
    public MasterCodeDto updateMasterCode(MasterCodeDto masterCode) {
        return saMasterCodeClient.updateMasterCode(masterCode).getEntity();
    }

    @Override
    public void deleteMasterCodeById(String id) {
        saMasterCodeClient.delMasterCode(id).getEntity();
    }

    @Override
    public List<String> suggestCodeDescription(String codeDescription) {
        return saMasterCodeClient.suggestCodeDescription(codeDescription).getEntity();
    }

    @Override
    public MasterCodeDto findMasterCodeByMcId(String id) {
        return saMasterCodeClient.getMasterCodeById(id).getEntity();
    }

    @Override
    public String findCodeCategoryByDescription(String description) {
        return saMasterCodeClient.getCodeCategoryByDescription(description).getEntity();
    }

    @Override
    public List<MasterCodeCategoryDto> getAllCodeCategory() {
        return saMasterCodeClient.getAllMasterCodeCategory().getEntity();
    }

    @Override
    public boolean masterCodeKeyIsExist(String masterCodekey) {
        return saMasterCodeClient.masterCodeKeyIsExist(masterCodekey).getEntity();
    }

    @Override
    public Boolean saveMasterCodeList(List<MasterCodeToExcelDto> masterCodeToExcelDtoList) {
        return saMasterCodeClient.saveMasterCodeExcel(masterCodeToExcelDtoList).getEntity();
    }

    @Override
    public String findCodeKeyByDescription(String description) {
        return saMasterCodeClient.getCodeKeyByDescription(description).getEntity();
    }

}

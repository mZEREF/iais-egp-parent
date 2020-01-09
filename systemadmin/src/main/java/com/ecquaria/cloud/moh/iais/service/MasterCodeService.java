package com.ecquaria.cloud.moh.iais.service;

import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.mastercode.MasterCodeCategoryDto;
import com.ecquaria.cloud.moh.iais.common.dto.mastercode.MasterCodeDto;
import com.ecquaria.cloud.moh.iais.common.dto.mastercode.MasterCodeQueryDto;

public interface MasterCodeService {
    SearchResult<MasterCodeQueryDto> doQuery(SearchParam param);
    void deleteMasterCodeById(String id);
    String findCodeKeyByDescription(String description);
    MasterCodeDto saveMasterCode(MasterCodeDto masterCode);
    MasterCodeDto updateMasterCode(MasterCodeDto masterCode);
    MasterCodeDto findMasterCodeByMcId(String masterCodeId);
    String findCodeCategoryByDescription(String description);
    MasterCodeCategoryDto saveMasterCodeCategory(MasterCodeCategoryDto masterCodeCategoryDto);
}

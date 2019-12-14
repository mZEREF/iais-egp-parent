package com.ecquaria.cloud.moh.iais.service;

import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.mastercode.MasterCodeDto;
import com.ecquaria.cloud.moh.iais.common.dto.mastercode.MasterCodeQueryDto;

import java.util.List;

public interface MasterCodeService {
    SearchResult<MasterCodeQueryDto> doQuery(SearchParam param);
    void saveMasterCode(MasterCodeDto masterCode);
    void deleteMasterCodeById(String id);
    MasterCodeDto findMasterCodeByMcId(String masterCodeId);
    String findMasterCodeByDescription(String description);
}

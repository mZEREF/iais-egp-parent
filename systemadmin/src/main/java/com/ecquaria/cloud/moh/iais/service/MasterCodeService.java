package com.ecquaria.cloud.moh.iais.service;

import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.dto.MasterCodeDto;

public interface MasterCodeService {
    SearchResult<MasterCodeDto> doQuery(SearchParam param);
    void saveMasterCode(MasterCodeDto masterCodeDto);
    void deleteMasterCodeById(Long id);
    MasterCodeDto findMasterCodeByRowguid(String rowguid);
}

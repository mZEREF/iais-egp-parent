package com.ecquaria.cloud.moh.iais.service;

import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.dto.MasterCodeDto;
import com.ecquaria.cloud.moh.iais.dto.MasterCodeQueryDto;

public interface MasterCodeService {
    SearchResult<MasterCodeQueryDto> doQuery(SearchParam param);
    void saveMasterCode(MasterCodeDto masterCode);
    void deleteMasterCodeById(Long id);
    MasterCodeDto findMasterCodeByRowguid(String rowguid);
}

package com.ecquaria.cloud.moh.iais.service;

import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.dto.MasterCodeDto;
import com.ecquaria.cloud.moh.iais.entity.MasterCode;

public interface MasterCodeService {
    SearchResult<MasterCodeDto> doQuery(SearchParam param);
    void saveMasterCode(MasterCode masterCode);
    void deleteMasterCodeById(Long id);
    MasterCode findMasterCodeByRowguid(String rowguid);
}

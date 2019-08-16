package com.ecquaria.cloud.moh.iais.service;

import com.ecquaria.cloud.moh.iais.dto.MasterCodeQuery;
import com.ecquaria.cloud.moh.iais.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.entity.MasterCode;

public interface MasterCodeService {
    void saveMasterCode(MasterCode mc);
    void deleteMasterCodeById(Long id);
    MasterCode findMasterCodeByRowguid(String rowguid);
    SearchResult<MasterCodeQuery> doQuery(SearchParam param, String catalog, String key);
}

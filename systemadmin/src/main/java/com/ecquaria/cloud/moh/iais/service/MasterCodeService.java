package com.ecquaria.cloud.moh.iais.service;

import com.ecquaria.cloud.moh.iais.dto.MasterCodeQuery;
import sg.gov.moh.iais.common.dto.SearchParam;
import sg.gov.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.entity.MasterCode;

public interface MasterCodeService {
    void saveMasterCode(MasterCode mc);
    void deleteMasterCodeById(Long id);
    MasterCode findMasterCodeByRowguid(String rowguid);
    SearchResult<MasterCodeQuery> doQuery(SearchParam param, String catalog, String key);
}

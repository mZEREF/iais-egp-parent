package com.ecquaria.cloud.moh.iais.service;

import com.ecquaria.cloud.moh.iais.dto.MasterCodeQuery;
import com.ecquaria.cloud.moh.iais.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.entity.MasterCode;

import java.util.List;

public interface MasterCodeService {
    void saveMasterCode(MasterCode mc);
    void deleteMasterCodeById(Long id);
    List<MasterCode> getMasterCodeList();
    List<MasterCode> findMasterCodeById(int id);
    MasterCode findMasterCodeByRowguid(String rowguid);
    MasterCode updateMasterCode(MasterCode mc);
    SearchResult<MasterCodeQuery> doQuery(SearchParam param, String catalog, String key);
}

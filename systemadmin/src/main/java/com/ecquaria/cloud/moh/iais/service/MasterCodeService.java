package com.ecquaria.cloud.moh.iais.service;

import com.ecquaria.cloud.moh.iais.dto.MasterCodeQuery;
import sg.gov.moh.iais.common.dto.SearchParam;
import sg.gov.moh.iais.common.dto.SearchResult;
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

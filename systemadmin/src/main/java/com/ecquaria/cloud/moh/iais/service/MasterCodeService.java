package com.ecquaria.cloud.moh.iais.service;

import com.ecquaria.cloud.moh.iais.entity.MasterCode;

public interface MasterCodeService {
    void saveMasterCode(MasterCode mc);
    void deleteMasterCodeById(Long id);
    MasterCode findMasterCodeByRowguid(String rowguid);
}

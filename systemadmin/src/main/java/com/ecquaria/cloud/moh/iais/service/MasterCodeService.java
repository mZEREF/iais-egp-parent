package com.ecquaria.cloud.moh.iais.service;

import com.ecquaria.cloud.moh.iais.entity.MasterCode;

import java.util.List;

public interface MasterCodeService {
    List<MasterCode> getMasterCodeList();
    List<MasterCode> findMasterCodeById(int id);
    MasterCode updateMasterCode(MasterCode mc);
}

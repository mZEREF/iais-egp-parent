package sg.gov.moh.iais.service;

import sg.gov.moh.iais.entity.MasterCode;

import java.util.List;

public interface MasterCodeService {
    MasterCode addMasterCode(MasterCode mc);
    List<MasterCode> getMasterCodeList();
    List<MasterCode> findMasterCodeById(int id);
    MasterCode updateMasterCode(MasterCode mc);
}

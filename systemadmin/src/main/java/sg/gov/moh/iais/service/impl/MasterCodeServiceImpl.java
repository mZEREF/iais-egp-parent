package sg.gov.moh.iais.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sg.gov.moh.iais.dao.MasterCodeRepository;
import sg.gov.moh.iais.entity.MasterCode;
import sg.gov.moh.iais.service.MasterCodeService;

import java.util.ArrayList;
import java.util.List;

/**
 * @author hc
 */
@Service
public class MasterCodeServiceImpl implements MasterCodeService {

    @Autowired
    MasterCodeRepository masterCodeRepository;

    @Autowired
    MasterCode masterCode;

    @Override
    public List<MasterCode> getMasterCodeList() {
        List<MasterCode> masterCodeList = new ArrayList<MasterCode>();
        masterCodeList = masterCodeRepository.findAll();
        return masterCodeList;
    }

    @Override
    public MasterCode addMasterCode(MasterCode mc) {
        masterCodeRepository.save(mc);
        return mc;
    }

    @Override
    public List<MasterCode> findMasterCodeById(int id) {
        List<MasterCode> masterCodeList = new ArrayList<>();
        masterCodeList = masterCodeRepository.findMasterCodeByMasterCodeId(id);
        return masterCodeList;
    }

    @Override
    public MasterCode updateMasterCode(MasterCode mc) {
        masterCodeRepository.save(mc);
        return mc;
    }
}

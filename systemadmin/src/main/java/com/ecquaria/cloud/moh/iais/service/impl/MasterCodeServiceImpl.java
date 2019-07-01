package com.ecquaria.cloud.moh.iais.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ecquaria.cloud.moh.iais.dao.MasterCodeRepository;
import com.ecquaria.cloud.moh.iais.entity.MasterCode;
import com.ecquaria.cloud.moh.iais.service.MasterCodeService;

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
        List<MasterCode> masterCodeList ;
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
        List<MasterCode> masterCodeList;
        masterCodeList = masterCodeRepository.findMasterCodeByMasterCodeId(id);
        return masterCodeList;
    }

    @Override
    public MasterCode updateMasterCode(MasterCode mc) {
        masterCodeRepository.save(mc);
        return mc;
    }
}

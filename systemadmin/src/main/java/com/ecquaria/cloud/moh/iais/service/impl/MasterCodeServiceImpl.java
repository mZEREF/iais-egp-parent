package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.dao.MasterCodeRepository;
import com.ecquaria.cloud.moh.iais.entity.MasterCode;
import com.ecquaria.cloud.moh.iais.service.MasterCodeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author hc
 */
@Service
@Slf4j
public class MasterCodeServiceImpl implements MasterCodeService {

    @Autowired
    MasterCodeRepository masterCodeRepository;

    @Autowired
    MasterCode masterCode;

    @Override
    public void saveMasterCode(MasterCode mc) {
        masterCodeRepository.save(mc);
    }

    @Override
    public void deleteMasterCodeById(Long id) {
        masterCodeRepository.delete(id);
    }

    @Override
    public MasterCode findMasterCodeByRowguid(String rowguid) {
        return masterCodeRepository.findMasterCodeByRowguid(rowguid);
    }
    
}

package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.mastercode.MasterCodeDto;
import com.ecquaria.cloud.moh.iais.common.dto.mastercode.MasterCodeQueryDto;
import com.ecquaria.cloud.moh.iais.service.MasterCodeService;
import com.ecquaria.cloud.moh.iais.service.client.MasterCodeClient;
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
    private MasterCodeClient masterCodeClient;

    @Override
    public SearchResult<MasterCodeQueryDto> doQuery(SearchParam param) {
        return  masterCodeClient.doQuery(param).getEntity();
    }

    @Override
    public void saveMasterCode(MasterCodeDto masterCode) {
        masterCodeClient.saveMasterCode(masterCode).getEntity();
    }

    @Override
    public void deleteMasterCodeById(String id) {
        masterCodeClient.delMasterCode(id).getEntity();
    }

    @Override
    public MasterCodeDto findMasterCodeByRowguid(String id) {
        return masterCodeClient.getMasterCodeById(id).getEntity();
    }

}

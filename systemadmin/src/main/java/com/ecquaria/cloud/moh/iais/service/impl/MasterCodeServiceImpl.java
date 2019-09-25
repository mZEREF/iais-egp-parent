package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.utils.RestApiUtil;
import com.ecquaria.cloud.moh.iais.dto.MasterCodeDto;
import com.ecquaria.cloud.moh.iais.entity.MasterCode;
import com.ecquaria.cloud.moh.iais.service.MasterCodeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @author hc
 */
@Service
@Slf4j
public class MasterCodeServiceImpl implements MasterCodeService {


    @Autowired
    MasterCode masterCode;

    @Override
    public SearchResult<MasterCodeDto> doQuery(SearchParam param) {
        return  RestApiUtil.query("system-admin-service:8886/iaia-mastercode/results", param);
    }

    @Override
    public void saveMasterCode(MasterCode masterCode) {
        RestApiUtil.save("system-admin-service:8886/iais-mastercode", masterCode);
    }

    @Override
    public void deleteMasterCodeById(Long id) {
        RestApiUtil.delete("system-admin-service:8886/iais-mastercode",id);
    }

    @Override
    public MasterCode findMasterCodeByRowguid(String rowguid) {
        Map<String,Object> map = new HashMap<>();
        map.put("rowguid",rowguid);
        return RestApiUtil.getByReqParam("system-admin-service:8886/iais-mastercode/{rowguid}",map, MasterCode.class);
    }

}

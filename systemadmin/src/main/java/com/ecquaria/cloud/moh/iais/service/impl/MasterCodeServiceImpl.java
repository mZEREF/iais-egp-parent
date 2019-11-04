package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.mastercode.MasterCodeDto;
import com.ecquaria.cloud.moh.iais.common.dto.mastercode.MasterCodeQueryDto;
import com.ecquaria.cloud.moh.iais.common.utils.RestApiUtil;
import com.ecquaria.cloud.moh.iais.service.MasterCodeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @author hc
 */
@Service
@Slf4j
public class MasterCodeServiceImpl implements MasterCodeService {


    @Override
    public SearchResult<MasterCodeQueryDto> doQuery(SearchParam param) {
        return  RestApiUtil.query("system-admin-service:8886/iais-mastercode/results", param);
    }

    @Override
    public void saveMasterCode(MasterCodeDto masterCode) {
        RestApiUtil.save("system-admin-service:8886/iais-mastercode", masterCode);
    }

    @Override
    public void deleteMasterCodeById(Long id) {
        RestApiUtil.delete("system-admin-service:8886/iais-mastercode",id);
    }

    @Override
    public MasterCodeDto findMasterCodeByRowguid(String rowguid) {
        Map<String,Object> map = new HashMap<>();
        map.put("rowguid",rowguid);
        return RestApiUtil.getByReqParam("system-admin-service:8886/iais-mastercode/{rowguid}",map, MasterCodeDto.class);
    }

}

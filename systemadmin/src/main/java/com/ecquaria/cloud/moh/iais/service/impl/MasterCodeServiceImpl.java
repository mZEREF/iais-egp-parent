package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.utils.RestApiUtil;
import com.ecquaria.cloud.moh.iais.dao.MasterCodeRepository;
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
    MasterCodeRepository masterCodeRepository;

    @Autowired
    MasterCode masterCode;

    @Override
    public SearchResult<MasterCodeDto> doQuery(SearchParam param) {
        return  RestApiUtil.query("/iaia-mastercode/results", param);
    }

    @Override
    public void saveMasterCode(MasterCodeDto masterCodeDto) {
        RestApiUtil.save("/iais-mastercode", masterCodeDto);
    }

    @Override
    public void deleteMasterCodeById(Long id) {
        RestApiUtil.delete("/iais-mastercode",id);
    }

    @Override
    public MasterCodeDto findMasterCodeByRowguid(String rowguid) {
        Map<String,Object> map = new HashMap<>();
        map.put("rowguid",rowguid);
        return RestApiUtil.getByReqParam("/iais-mastercode/{rowguid}",map, MasterCodeDto.class);
    }
    
}

/*
 *   This file is generated by ECQ project skeleton automatically.
 *
 *   Copyright 2019-2049, Ecquaria Technologies Pte Ltd. All rights reserved.
 *
 *   No part of this material may be copied, reproduced, transmitted,
 *   stored in a retrieval system, reverse engineered, decompiled,
 *   disassembled, localised, ported, adapted, varied, modified, reused,
 *   customised or translated into any language in any form or by any means,
 *   electronic, mechanical, photocopying, recording or otherwise,
 *   without the prior written permission of Ecquaria Technologies Pte Ltd.
 */

package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.annotation.SearchTrack;
import com.ecquaria.cloud.moh.iais.common.constant.rest.RestApiUrlConsts;
import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.sample.DemoQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.sample.OrgUserAccountSampleDto;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.service.OrgUserAccountSampleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;


/**
 * OrgUserAccountServiceImpl
 *
 * @author suocheng
 * @date 7/12/2019
 */
@Service
@Slf4j
public class OrgUserAccountSampleServiceImpl implements OrgUserAccountSampleService {
    @Override
    public void deleteOrgUserAccountsById(String id) {
        OrgUserAccountSampleDto orgUserAccountDto = new OrgUserAccountSampleDto();
        orgUserAccountDto.setId(Integer.parseInt(id));
        orgUserAccountDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
//        RestApiUtil.delete(RestApiUrlConsts.SAMPLE_SERVICE,orgUserAccountDto);
    }

    @Override
    public void saveOrgUserAccounts(OrgUserAccountSampleDto orgUserAccountDto) {
        orgUserAccountDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
//        RestApiUtil.save(RestApiUrlConsts.SAMPLE_SERVICE,orgUserAccountDto);
    }

    @Override
    public OrgUserAccountSampleDto getOrgUserAccountByRowguId(String rowguId) {
        Map<String, Object> map = new HashMap<>();
        map.put("searchField", "rowguId");
        map.put("filterValue", rowguId);
        return null;
//        return RestApiUtil.getByReqParam(RestApiUrlConsts.SAMPLE_SERVICE, map, OrgUserAccountSampleDto.class);
    }

    @Override
    public OrgUserAccountSampleDto getOrgUserAccountByNircNo(String nircNo) {
        Map<String, Object> map = new HashMap<>();
        map.put("searchField", "nircNo");
        map.put("filterValue", nircNo);

        return null;
//        return RestApiUtil.getByReqParam(RestApiUrlConsts.SAMPLE_SERVICE, map, OrgUserAccountSampleDto.class);
    }

    @Override
    @SearchTrack(catalog = "demo",key = "searchDemo")
    public SearchResult<DemoQueryDto> doQuery(SearchParam param) {
        return null;
//        return RestApiUtil.query(RestApiUrlConsts.SAMPLE_SERVICE+"/results", param);
    }
}

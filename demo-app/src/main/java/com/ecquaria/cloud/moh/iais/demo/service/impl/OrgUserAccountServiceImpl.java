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

package com.ecquaria.cloud.moh.iais.demo.service.impl;

import com.ecquaria.cloud.moh.iais.demo.dto.OrgUserAccountDto;
import com.ecquaria.cloud.moh.iais.demo.entity.DemoQuery;
import com.ecquaria.cloud.moh.iais.demo.service.OrgUserAccountService;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import sg.gov.moh.iais.common.dto.SearchParam;
import sg.gov.moh.iais.common.dto.SearchResult;


/**
 * OrgUserAccountServiceImpl
 *
 * @author suocheng
 * @date 7/12/2019
 */
@Service
@Slf4j
public class OrgUserAccountServiceImpl implements OrgUserAccountService {


//    @Override
//    public void deleteOrgUserAccountsById(String id) {
//        orgUserAccountDao.delete(Integer.parseInt(id));
//
//    }
//
    @Override
    public void saveOrgUserAccounts(OrgUserAccountDto orgUserAccountDto) {
        IaisEGPHelper.doSave("/api/demo/save",orgUserAccountDto);
    }
//
    @Override
    public OrgUserAccountDto getOrgUserAccountByRowguId(String rowguId) {
        return (OrgUserAccountDto)IaisEGPHelper.doGetByRowguId("/api/demo/getByRowguId",rowguId,OrgUserAccountDto.class);
    }

    @Override
    public SearchResult<DemoQuery> doQuery(SearchParam param) {
        return IaisEGPHelper.doQuery("/api/demo/query",param);
    }
}

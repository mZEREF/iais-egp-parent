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

import com.ecquaria.cloud.moh.iais.annotation.SearchTrack;
import com.ecquaria.cloud.moh.iais.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.querydao.QueryDao;
import com.ecquaria.cloud.moh.iais.demo.dao.OrgUserAccountDao;
import com.ecquaria.cloud.moh.iais.demo.entity.DemoQuery;
import com.ecquaria.cloud.moh.iais.demo.entity.OrgUserAccount;
import com.ecquaria.cloud.moh.iais.demo.service.OrgUserAccountService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

/**
 * OrgUserAccountServiceImpl
 *
 * @author suocheng
 * @date 7/12/2019
 */
@Service
@Slf4j
public class OrgUserAccountServiceImpl implements OrgUserAccountService {

    @Autowired
    private OrgUserAccountDao orgUserAccountDao;
    @Autowired
    private QueryDao<DemoQuery> demoQueryDao;

    @Override
    public void deleteOrgUserAccountsById(String id) {
        orgUserAccountDao.delete(Integer.parseInt(id));

    }

    @Override
    public void saveOrgUserAccounts(OrgUserAccount orgUserAccount) {
        orgUserAccountDao.save(orgUserAccount);
    }

    @Override
    public OrgUserAccount getOrgUserAccountByRowguId(String rowguId) {
        OrgUserAccount orgUserAccount = new OrgUserAccount();
        orgUserAccount.setRowguid(rowguId);
        ExampleMatcher exampleMatcher =
                ExampleMatcher.matching().withMatcher("rowguid",ExampleMatcher.GenericPropertyMatchers.exact());

        Example<OrgUserAccount> example = Example.of(orgUserAccount,exampleMatcher);
        return orgUserAccountDao.findOne(example);
    }

    @Override
    @SearchTrack
    public SearchResult<DemoQuery> doQuery(SearchParam param, String catalog, String key) {
        return demoQueryDao.doQuery(param, catalog, key);
    }
}

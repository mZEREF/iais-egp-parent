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

package com.ecquaria.cloud.moh.iais.test.service.impl;

import com.ecquaria.cloud.moh.iais.test.dao.OrganizationDao;
import com.ecquaria.cloud.moh.iais.test.entity.Organization;
import com.ecquaria.cloud.moh.iais.test.service.OrganizationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

/**
 * OrganizationServiceImpl
 *
 * @author suocheng
 * @date 7/12/2019
 */
@Service
@Slf4j
public class OrganizationServiceImpl implements OrganizationService {

    @Autowired
    private OrganizationDao organizationDao;

    @Override
    public Organization getOrganizationByUenNo(String uenNo) {
        Organization organization = new Organization();
        organization.setUenNo(uenNo);
        Example<Organization> example = Example.of(organization);
        return organizationDao.findOne(example);
    }


}

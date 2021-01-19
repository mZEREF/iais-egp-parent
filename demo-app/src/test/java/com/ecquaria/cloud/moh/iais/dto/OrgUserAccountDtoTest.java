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

package com.ecquaria.cloud.moh.iais.dto;

import com.ecquaria.cloud.moh.iais.common.dto.sample.OrgUserAccountSampleDto;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Spy;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.junit.Assert.assertNotNull;

@RunWith(PowerMockRunner.class)
@PrepareForTest({OrgUserAccountSampleDto.class})
public class OrgUserAccountDtoTest {

    @Spy
    OrgUserAccountSampleDto entity = new OrgUserAccountSampleDto();

    @Test
    public void testGetterSetter() {
        entity.getId();
        entity.setId(1);
        entity.getRowguid();
        entity.setRowguid(null);
        entity.getName();
        entity.setName(null);
        entity.getOldNricNo();
        entity.setOldNricNo(null);
        entity.getNircNo();
        entity.setNircNo(null);
        entity.getCorpPassId();
        entity.setCorpPassId(null);
        entity.getStatus();
        entity.setStatus(null);
        entity.getOrgId();
        entity.setOrgId(null);
        entity.isEditFlag();
        entity.setEditFlag(false);
        entity.validateNricEdit("");
        entity.fakeValidateA("");
        entity.fakeValidateB("");
        entity.validateNricEdit("nric");
        entity.fakeValidateA("arg");
        entity.fakeValidateB("arg");
        entity.setAuditTrailDto(null);
        entity.getAuditTrailDto();
        assertNotNull(entity);
    }
}
package com.ecquaria.cloud.moh.iais.test.entity;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Spy;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.junit.Assert.assertNotNull;

@RunWith(PowerMockRunner.class)
@PrepareForTest({OrgUserAccount.class})
public class OrgUserAccountTest {

    @Spy
    OrgUserAccount entity = new OrgUserAccount();

    @Test
    public void testGetterSetter() {
        entity.getId();
        entity.setId(null);
        entity.getRowguid();
        entity.setRowguid(null);
        entity.getName();
        entity.setName(null);
        entity.getNircNo();
        entity.setNircNo(null);
        entity.getCorpPassId();
        entity.setCorpPassId(null);
        entity.getStatus();
        entity.setStatus(null);
        entity.getOrgId();
        entity.setOrgId(null);
        entity.getCreateBy();
        entity.setCreateBy(null);
        entity.getCreateDm();
        entity.setCreateDm(null);
        entity.getCreateDt();
        entity.setCreateDt(null);
        entity.getUpdateBy();
        entity.setUpdateBy(null);
        entity.getUpdateDm();
        entity.setUpdateDm(null);
        entity.getUpdateDt();
        entity.setUpdateDt(null);
        entity.getOrganization();
        entity.setOrganization(null);
        assertNotNull(entity);
    }
}
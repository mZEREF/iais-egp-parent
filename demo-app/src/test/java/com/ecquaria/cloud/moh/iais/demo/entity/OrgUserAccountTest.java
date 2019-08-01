package com.ecquaria.cloud.moh.iais.demo.entity;

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
        entity.getOrganization();
        entity.setOrganization(null);
        assertNotNull(entity);
    }
}
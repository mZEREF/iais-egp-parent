package com.ecquaria.cloud.moh.iais.demo.entity;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Spy;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.junit.Assert.assertNotNull;

@RunWith(PowerMockRunner.class)
@PrepareForTest({Organization.class})
public class OrganizationTest {

    @Spy
    Organization entity = new Organization();

    @Test
    public void testGetterSetter() {
        entity.getId();
        entity.setId(null);
        entity.getUenNo();
        entity.setUenNo(null);
        entity.getOrgType();
        entity.setOrgType(null);
        entity.getStatus();
        entity.setStatus(null);
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
        entity.getOrgUserAccountList();
        entity.setOrgUserAccountList(null);
        assertNotNull(entity);
    }
}
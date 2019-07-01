package com.ecquaria.cloud.moh.iais.entity;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Spy;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.junit.Assert.assertNotNull;

@RunWith(PowerMockRunner.class)
@PrepareForTest({MasterCode.class})
public class MasterCodeTest {

    @Spy
    MasterCode entity = new MasterCode();

    @Test
    public void testGetterSetter() {
        entity.getId();
        entity.setId(1);
        entity.getMasterCodeKey();
        entity.setMasterCodeKey(null);
        entity.getCodeCategory();
        entity.setCodeCategory(1);
        entity.getCodeValue();
        entity.setCodeValue(null);
        entity.getCodeDescription();
        entity.setCodeDescription(null);
        entity.getFilterValue();
        entity.setFilterValue(null);
        entity.getSequence();
        entity.setSequence(1);
        entity.getRemarks();
        entity.setRemarks(null);
        entity.getStatus();
        entity.setStatus(1);
        entity.getEffectiveFrom();
        entity.setEffectiveFrom(null);
        entity.getEffectiveTo();
        entity.setEffectiveTo(null);
        entity.getVersion();
        entity.setVersion(1);
        assertNotNull(entity);
    }
}
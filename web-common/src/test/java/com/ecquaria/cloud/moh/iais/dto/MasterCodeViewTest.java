package com.ecquaria.cloud.moh.iais.dto;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Spy;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.junit.Assert.assertNotNull;

@RunWith(PowerMockRunner.class)
@PrepareForTest({MasterCodeView.class})
public class MasterCodeViewTest {

    @Spy
    MasterCodeView entity = new MasterCodeView();

    @Test
    public void testGetterSetter() {
        entity.getMasterCodeId();
        entity.setMasterCodeId(1);
        entity.getCode();
        entity.setCode(null);
        entity.getCodeValue();
        entity.setCodeValue(null);
        entity.getCategory();
        entity.setCategory(1);
        entity.getDescription();
        entity.setDescription(null);
        entity.getFilterValue();
        entity.setFilterValue(null);
        entity.getSequence();
        entity.setSequence(1);
        entity.getStatus();
        entity.setStatus(1);
        entity.getVersion();
        entity.setVersion(1);
        entity.getEffectFrom();
        entity.setEffectFrom(null);
        entity.getEffectTo();
        entity.setEffectTo(null);
        assertNotNull(entity);
    }
}
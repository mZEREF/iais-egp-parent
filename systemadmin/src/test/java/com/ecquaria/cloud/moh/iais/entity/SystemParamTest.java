package com.ecquaria.cloud.moh.iais.entity;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Spy;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.junit.Assert.assertNotNull;

@RunWith(PowerMockRunner.class)
@PrepareForTest({SystemParam.class})
public class SystemParamTest {

    @Spy
    SystemParam entity = new SystemParam();

    @Test
    public void testGetterSetter() {
        entity.getId();
        entity.setId(null);
        entity.getRowguid();
        entity.setRowguid(null);
        entity.getDescription();
        entity.setDescription(null);
        entity.getValue();
        entity.setValue(null);
        entity.getParamType();
        entity.setParamType(null);
        entity.isManDatory();
        entity.setManDatory(false);
        entity.isCanUpdate();
        entity.setCanUpdate(false);
        entity.getMaxLength();
        entity.setMaxLength(null);
        entity.isEnable();
        entity.setEnable(false);
        entity.isEnableCheckBox();
        entity.setEnableCheckBox(false);
        entity.getCreatedBy();
        entity.setCreatedBy(null);
        entity.getCreatedAt();
        entity.setCreatedAt(null);
        entity.getModifiedBy();
        entity.setModifiedBy(null);
        entity.getModifiedAt();
        entity.setModifiedAt(null);
        assertNotNull(entity);
    }
}
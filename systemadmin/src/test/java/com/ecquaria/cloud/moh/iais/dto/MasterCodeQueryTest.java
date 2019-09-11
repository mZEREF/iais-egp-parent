package com.ecquaria.cloud.moh.iais.dto;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Spy;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.junit.Assert.assertNotNull;

@RunWith(PowerMockRunner.class)
@PrepareForTest({MasterCodeQuery.class})
public class MasterCodeQueryTest {

    @Spy
    MasterCodeQuery entity = new MasterCodeQuery();

    @Test
    public void testGetterSetter() {
        entity.getMasterCodeId();
        entity.setMasterCodeId(null);
        entity.getRowguid();
        entity.setRowguid(null);
        entity.getMasterCodeKey();
        entity.setMasterCodeKey(null);
        entity.getCodeCategory();
        entity.setCodeCategory(1);
        entity.getCodeValue();
        entity.setCodeValue(null);
        entity.getCodeDescription();
        entity.setCodeDescription(null);
        entity.getStatus();
        entity.setStatus(1);
        assertNotNull(entity);
    }
}

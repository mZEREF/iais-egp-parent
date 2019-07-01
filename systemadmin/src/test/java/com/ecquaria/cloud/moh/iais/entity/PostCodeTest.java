package com.ecquaria.cloud.moh.iais.entity;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Spy;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.junit.Assert.assertNotNull;

@RunWith(PowerMockRunner.class)
@PrepareForTest({PostCode.class})
public class PostCodeTest {

    @Spy
    PostCode entity = new PostCode();

    @Test
    public void testGetterSetter() {
        entity.getId();
        entity.setId(null);
        entity.getPostalCode();
        entity.setPostalCode(null);
        entity.getAddressType();
        entity.setAddressType(null);
        entity.getBlkHseNo();
        entity.setBlkHseNo(null);
        entity.getStreetName();
        entity.setStreetName(null);
        entity.getBuildingName();
        entity.setBuildingName(null);
        assertNotNull(entity);
    }
}
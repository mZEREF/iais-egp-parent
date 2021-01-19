package com.ecquaria.cloud.moh.iais.dto;

import com.ecquaria.cloud.moh.iais.common.dto.postcode.PostCodeDto;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Spy;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.junit.Assert.assertNotNull;

@RunWith(PowerMockRunner.class)
@PrepareForTest({PostCodeDto.class})
public class PostCodeDtoTest {

    @Spy
    PostCodeDto entity = new PostCodeDto();

    @Test
    public void testGetterSetter() {
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
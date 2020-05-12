package com.ecquaria.cloud.moh.iais.dto;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Spy;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.junit.Assert.assertNotNull;

@RunWith(PowerMockRunner.class)
@PrepareForTest({HcsaInspectionValidateDto.class})
public class HcsaInspectionValidateDtoTest {

    @Spy
    HcsaInspectionValidateDto entity = new HcsaInspectionValidateDto();

    @Test
    public void testGetterSetter() {
        assertNotNull(entity);
    }
}
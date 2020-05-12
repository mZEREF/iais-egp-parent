package com.ecquaria.cloud.moh.iais.dto;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Spy;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.junit.Assert.assertNotNull;

@RunWith(PowerMockRunner.class)
@PrepareForTest({HcsaWeightageRiskValidateDto.class})
public class HcsaWeightageRiskValidateDtoTest {

    @Spy
    HcsaWeightageRiskValidateDto entity = new HcsaWeightageRiskValidateDto();

    @Test
    public void testGetterSetter() {
        assertNotNull(entity);
    }
}
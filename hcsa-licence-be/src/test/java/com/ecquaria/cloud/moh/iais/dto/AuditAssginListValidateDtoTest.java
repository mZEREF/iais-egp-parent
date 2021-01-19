package com.ecquaria.cloud.moh.iais.dto;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Spy;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.junit.Assert.assertNotNull;

@RunWith(PowerMockRunner.class)
@PrepareForTest({AuditAssginListValidateDto.class})
public class AuditAssginListValidateDtoTest {

    @Spy
    AuditAssginListValidateDto entity = new AuditAssginListValidateDto();

    @Test
    public void testGetterSetter() {
        assertNotNull(entity);
    }
}
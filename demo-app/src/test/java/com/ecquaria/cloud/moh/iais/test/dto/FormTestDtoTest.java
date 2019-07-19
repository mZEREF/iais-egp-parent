package com.ecquaria.cloud.moh.iais.test.dto;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Spy;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.junit.Assert.assertNotNull;

@RunWith(PowerMockRunner.class)
@PrepareForTest({FormTestDto.class})
public class FormTestDtoTest {

    @Spy
    FormTestDto entity = new FormTestDto();

    @Test
    public void testGetterSetter() {
        entity.getId();
        entity.setId(null);
        entity.getRowguid();
        entity.setRowguid(null);
        entity.getName();
        entity.setName(null);
        entity.getAge();
        entity.setAge(null);
        assertNotNull(entity);
    }
}
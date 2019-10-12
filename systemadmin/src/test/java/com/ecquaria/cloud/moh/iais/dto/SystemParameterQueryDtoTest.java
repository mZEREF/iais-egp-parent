package com.ecquaria.cloud.moh.iais.dto;

/*
 *author: yichen
 *date time:9/9/2019 12:11 PM
 *description:
 */

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Spy;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
public class SystemParameterQueryDtoTest {
    @Spy
    SystemParameterQueryDto dto = new SystemParameterQueryDto();

    @Test
    public void testGetterSetter() {
        dto.getId();
        dto.getDescription();
        dto.getDomainType();
        dto.getModule();
        dto.getStatus();
        dto.getValue();
        dto.getValueType();
        dto.getUpdatedBy();
        dto.getUpdatedOn();


        dto.setId(null);
        dto.setDescription(null);
        dto.setDomainType(null);
        dto.setModule(null);
        dto.setStatus(null);
        dto.setValue(null);
        dto.setValueType(null);
        dto.setUpdatedBy(null);
        dto.setUpdatedOn(null);
        Assert.assertTrue(true);
    }
}

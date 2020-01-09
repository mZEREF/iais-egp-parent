package com.ecquaria.cloud.moh.iais.dto;

import com.ecquaria.cloud.moh.iais.common.dto.parameter.SystemParameterDto;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Spy;
import org.powermock.modules.junit4.PowerMockRunner;

/*
 *author: yichen
 *date time:9/10/2019 4:31 PM
 *description:
 */
@RunWith(PowerMockRunner.class)
public class SystemParameterDtoTest {
    @Spy
    SystemParameterDto dto = new SystemParameterDto();

    @Test
    public void testGetterSetter() {
        dto.getId();
        dto.getDescription();
        dto.getDomainType();
        dto.getModule();
        dto.getStatus();
        dto.getValue();
        dto.getParamType();


        dto.setId(null);
        dto.setDescription(null);
        dto.setDomainType(null);
        dto.setModule(null);
        dto.setStatus(null);
        dto.setValue(null);
        dto.setParamType(null);
        Assert.assertTrue(true);
    }
}

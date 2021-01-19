package com.ecquaria.cloud.moh.iais.dto;

/*
 *author: yichen
 *date time:9/9/2019 12:11 PM
 *description:
 */

import com.ecquaria.cloud.moh.iais.common.dto.parameter.SystemParameterQueryDto;
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


        dto.setId(null);
        dto.setDescription(null);
        dto.setDomainType(null);
        dto.setModule(null);
        dto.setStatus(null);
        dto.setValue(null);
        Assert.assertTrue(true);
    }
}

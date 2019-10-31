package com.ecquaria.cloud.moh.iais.dto;

import com.ecquaria.cloud.moh.iais.common.dto.sample.DemoQueryDto;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Spy;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.junit.Assert.assertNotNull;

@RunWith(PowerMockRunner.class)
@PrepareForTest({DemoQueryDto.class})
public class DemoQueryDtoTest {

    @Spy
    DemoQueryDto entity = new DemoQueryDto();

    @Test
    public void testGetterSetter() {
        entity.getUserId();
        entity.setUserId(1);
        entity.getNuicNum();
        entity.setNuicNum(null);
        entity.getUenNo();
        entity.setUenNo(null);
        entity.getRowguid();
        entity.setRowguid(null);
        entity.getOrgId();
        entity.setOrgId(null);
        entity.getStatus();
        entity.setStatus(null);
        assertNotNull(entity);
    }
}
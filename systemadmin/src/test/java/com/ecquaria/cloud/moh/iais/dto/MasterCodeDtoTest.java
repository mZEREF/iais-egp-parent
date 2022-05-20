package com.ecquaria.cloud.moh.iais.dto;


import com.ecquaria.cloud.moh.iais.common.dto.mastercode.MasterCodeDto;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Spy;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.junit.Assert.assertNotNull;

@RunWith(PowerMockRunner.class)
@PrepareForTest(MasterCodeDto.class)
public class MasterCodeDtoTest {

    @Spy
    MasterCodeDto masterCodeDto = new MasterCodeDto();

    @Test
    public void testMasterCodeDto(){
        masterCodeDto.getId();
        masterCodeDto.getCodeValue();
        masterCodeDto.getCodeCategory();
        masterCodeDto.getCodeDescription();
        masterCodeDto.getStatus();
        masterCodeDto.getMasterCodeKey();



        masterCodeDto.setMasterCodeKey(null);
        masterCodeDto.setCodeCategory("");
        masterCodeDto.setCodeValue(null);
        masterCodeDto.setId("1");
        masterCodeDto.setStatus("0");
        masterCodeDto.setCodeDescription(null);
        assertNotNull(masterCodeDto);
    }
}

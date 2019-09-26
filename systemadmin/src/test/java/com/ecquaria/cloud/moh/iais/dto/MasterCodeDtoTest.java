package com.ecquaria.cloud.moh.iais.dto;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Spy;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest(MasterCodeDto.class)
public class MasterCodeDtoTest {

    @Spy
    MasterCodeDto masterCodeDto = new MasterCodeDto();

    @Test
    public void testMasterCodeDto(){
        masterCodeDto.getMasterCodeId();
        masterCodeDto.getCodeValue();
        masterCodeDto.getCodeCategory();
        masterCodeDto.getCodeDescription();
        masterCodeDto.getStatus();
        masterCodeDto.getMasterCodeKey();
        masterCodeDto.getRowguid();


        masterCodeDto.setMasterCodeKey(null);
        masterCodeDto.setCodeCategory(0);
        masterCodeDto.setCodeValue(null);
        masterCodeDto.setMasterCodeId(0);
        masterCodeDto.setStatus(0);
        masterCodeDto.setCodeDescription(null);
        masterCodeDto.setRowguid(null);
    }
}

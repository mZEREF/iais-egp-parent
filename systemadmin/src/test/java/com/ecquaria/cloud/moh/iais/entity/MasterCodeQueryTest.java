package com.ecquaria.cloud.moh.iais.entity;


import com.ecquaria.cloud.moh.iais.dto.MasterCodeQuery;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Spy;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest({MasterCodeQuery.class})
public class MasterCodeQueryTest {


    @Spy
    MasterCodeQuery masterCodeQuery = new MasterCodeQuery();

    @Test
    public void testmastercodeQuery(){
        masterCodeQuery.getCodeCategory();
        masterCodeQuery.getCodeDescription();
        masterCodeQuery.getCodeValue();
        masterCodeQuery.getMasterCodeId();
        masterCodeQuery.getMasterCodeKey();
        masterCodeQuery.getRowguid();
        masterCodeQuery.getStatus();


        masterCodeQuery.setCodeCategory(2);
        masterCodeQuery.setCodeDescription(null);
        masterCodeQuery.setCodeValue(null);
        masterCodeQuery.setMasterCodeId(1L);
        masterCodeQuery.setMasterCodeKey(null);
        masterCodeQuery.setRowguid(null);
        masterCodeQuery.setStatus(1);

    }
}

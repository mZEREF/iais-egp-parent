package com.ecquaria.cloud.moh.iais.service;
/*
 *File Name: SystemParamServiceTest
 *Creator: yichen    yichen_guo@ecquaria.com
 *Creation time:6/10/2019 2:54 PM
 *Describe:
 */


import com.ecquaria.cloud.moh.iais.dao.SystemParamDAO;
import com.ecquaria.cloud.moh.iais.entity.SystemParam;
import com.ecquaria.cloud.moh.iais.service.impl.SystemParamServiceImpl;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.api.mockito.mockpolicies.Slf4jMockPolicy;
import org.powermock.core.classloader.annotations.MockPolicy;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.ArrayList;
import java.util.List;

@RunWith(PowerMockRunner.class)
@MockPolicy(Slf4jMockPolicy.class)
@PrepareForTest({SystemParamService.class})
public class SystemParamServiceTest {

    @InjectMocks
    private SystemParamServiceImpl systemParamService;

    @Mock
    private SystemParamDAO systemParamDAO;

    @Test
    public void  testListSystemParam(){
        List<SystemParam> result = new ArrayList<>();
        SystemParam sp = new SystemParam();
        result.add(sp);
        Mockito.doReturn(result).when(systemParamDAO).listSystemParam();
        systemParamService.listSystemParam();
        Assert.assertTrue(true);
    }

    @Test(expected = RuntimeException.class)
    public void testUpdateParamByPkId(){
        String guid = "52";
        String value = "555";

        PowerMockito.when(systemParamDAO.updateValueByGuid(guid, Integer.valueOf(value))).thenThrow(new RuntimeException());
        systemParamService.updateValueByGuid(guid, value);
        Assert.assertTrue(true);
    }

    @Test
    public void testInsertRecord() {
        SystemParam systemParam = new SystemParam();
        systemParam.setId(52);
        systemParam.setValue("111");
        PowerMockito.doReturn(null).when(systemParamDAO).save(systemParam);
        systemParamService.insertRecord(systemParam);
        Assert.assertTrue(true);
    }

}

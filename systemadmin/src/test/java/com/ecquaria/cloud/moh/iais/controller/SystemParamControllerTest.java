package com.ecquaria.cloud.moh.iais.controller;

/*
 *File Name: SystemParamControllerTest
 *Creator: yichen    yichen_guo@ecquaria.com
 *Creation time:6/10/2019 2:46 PM
 *Describe:
 */

import com.ecquaria.cloud.moh.iais.entity.SystemParam;
import com.ecquaria.cloud.moh.iais.service.impl.SystemParamServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.powermock.api.mockito.mockpolicies.Slf4jMockPolicy;
import org.powermock.core.classloader.annotations.MockPolicy;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.mock.web.MockHttpServletRequest;



@RunWith(PowerMockRunner.class)
@MockPolicy(Slf4jMockPolicy.class)
@PrepareForTest({SystemParamController.class})
public class SystemParamControllerTest {
    @Spy
    private MockHttpServletRequest request = new MockHttpServletRequest();

    @InjectMocks
    private SystemParamController systemParamController;

    @Mock
    private SystemParamServiceImpl service;

    @Test
    public void testView(){
        Mockito.doReturn(null).when(service).listSystemParam();
        systemParamController.view(request);
    }

    @Test
    public void testUpdateValueByGuid(){
        SystemParam systemParam = new SystemParam();
        systemParam.setRowguid("9A7149C4-06CB-4BAC-BC8B-9C5038DA0276");
        systemParam.setValue("444");
        request.setAttribute("systemParam", systemParam);

        Mockito.doNothing().when(service).updateValueByGuid(systemParam.getRowguid(), systemParam.getValue());
        systemParamController.updateValueByGuid(request);
    }

    @Test
    public void testInsertRecord(){
        SystemParam systemParam = new SystemParam();
        systemParam.setRowguid("9A7149C4-06CB-4BAC-BC8B-9C5038DA0276");
        systemParam.setValue("444");
        systemParam.setDescription("Number of records per page to display the search results");
        request.setAttribute("systemParam", systemParam);

        Mockito.doNothing().when(service).insertRecord(systemParam);
        systemParamController.insertRecord(request);
    }

}

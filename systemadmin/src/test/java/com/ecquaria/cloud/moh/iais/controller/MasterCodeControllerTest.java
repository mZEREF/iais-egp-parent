package com.ecquaria.cloud.moh.iais.controller;

import com.ecquaria.cloud.moh.iais.service.impl.MasterCodeServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.powermock.api.mockito.mockpolicies.Slf4jMockPolicy;
import org.powermock.core.classloader.annotations.MockPolicy;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import sop.webflow.rt.api.BaseProcessClass;

@RunWith(PowerMockRunner.class)
@MockPolicy(Slf4jMockPolicy.class)
@PrepareForTest({MasterCodeController.class})
public class MasterCodeControllerTest {
    @InjectMocks
    private MasterCodeController masterCodeController;

    @Mock
    private MasterCodeServiceImpl masterCodeServiceImpl;

    @Mock
    private BaseProcessClass baseProcessClass;

    @Test(expected = UnsupportedOperationException.class)
    public void testgetMasterCodeList(){
        masterCodeController.getMasterCodeList();
    }
}

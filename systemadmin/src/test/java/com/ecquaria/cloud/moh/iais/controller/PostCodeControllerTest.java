package com.ecquaria.cloud.moh.iais.controller;

import com.ecquaria.cloud.moh.iais.service.impl.PostCodeServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.powermock.api.mockito.mockpolicies.Slf4jMockPolicy;
import org.powermock.core.classloader.annotations.MockPolicy;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import sop.webflow.rt.api.BaseProcessClass;

import java.io.IOException;

@RunWith(PowerMockRunner.class)
@MockPolicy(Slf4jMockPolicy.class)
@PrepareForTest({PostCodeController.class})
public class PostCodeControllerTest {
    @InjectMocks
    private PostCodeController postCodeController;

    @Mock
    private BaseProcessClass baseProcessClass;

    @Mock
    private PostCodeServiceImpl postCodeService;

    @Test
    public void testimportPostCode(){
        try {
            postCodeController.importPostCode(baseProcessClass);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

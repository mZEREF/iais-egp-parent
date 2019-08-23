package com.ecquaria.cloud.moh.iais.controller;

import com.ecquaria.cloud.moh.iais.service.impl.PostCodeServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.powermock.api.mockito.mockpolicies.Slf4jMockPolicy;
import org.powermock.core.classloader.annotations.MockPolicy;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import sop.webflow.rt.api.BaseProcessClass;

import java.lang.reflect.Field;

@RunWith(PowerMockRunner.class)
@MockPolicy(Slf4jMockPolicy.class)
@PrepareForTest({PostCodeDelegator.class})
public class PostCodeControllerTest {
    @InjectMocks
    private PostCodeDelegator postCodeController;

    @Mock
    private BaseProcessClass baseProcessClass;

    @Mock
    private PostCodeServiceImpl postCodeService;


    @Before
    public void setup() throws IllegalAccessException, NoSuchFieldException {
     //Field field1 = PowerMockito.field(PostCodeController.class,"POSTCODE_PATH");
        Field field1 = postCodeController.getClass().getDeclaredField("streetsPath");
        field1.setAccessible(true);
        field1.set(postCodeController,Thread.currentThread().getContextClassLoader().getResource("").getPath() + "file/STREETS.TXT");
        Field field2 = postCodeController.getClass().getDeclaredField("postCodePath");
        field2.setAccessible(true);
        field2.set(postCodeController,Thread.currentThread().getContextClassLoader().getResource("").getPath() + "file/POSTCODE.TXT");

        Field field3 = postCodeController.getClass().getDeclaredField("buildingPath");
        field3.setAccessible(true);
        field3.set(postCodeController,Thread.currentThread().getContextClassLoader().getResource("").getPath() + "file/BUILDING.TXT");

    }

    @Test
    public void testimportPostCode() throws Exception {
//        try {
//            postCodeController.importPostCode();
//        } catch (IOException e) {
//            e.printStackTrace();
//            Assert.assertTrue(false);
//        }

    }
}

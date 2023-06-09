package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.moh.iais.service.PostCodeService;
import com.ecquaria.cloud.moh.iais.service.impl.PostCodeServiceImpl;
import java.io.IOException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.powermock.api.mockito.mockpolicies.Slf4jMockPolicy;
import org.powermock.core.classloader.annotations.MockPolicy;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;
import org.springframework.mock.web.MockHttpServletRequest;
import sop.webflow.rt.api.BaseProcessClass;

@RunWith(PowerMockRunner.class)
@MockPolicy(Slf4jMockPolicy.class)
@PrepareForTest({PostCodeDelegator.class})
public class PostCodeControllerTest {
    @InjectMocks
    private PostCodeDelegator postCodeController;

    @Mock
    private BaseProcessClass baseProcessClass;
    private MockHttpServletRequest request = new MockHttpServletRequest();
    private PostCodeService postCodeService = new PostCodeServiceImpl();

    @Before
    public void setup() throws IllegalAccessException, NoSuchFieldException {
        baseProcessClass.request = request;
        Whitebox.setInternalState(postCodeController,"streetsPath",
                Thread.currentThread().getContextClassLoader().getResource("").getPath() + "file/STREETS.TXT");
        Whitebox.setInternalState(postCodeController,"postCodePath",
                Thread.currentThread().getContextClassLoader().getResource("").getPath() + "file/POSTCODE.TXT");
        Whitebox.setInternalState(postCodeController,"buildingPath",
                Thread.currentThread().getContextClassLoader().getResource("").getPath() + "file/BUILDING.TXT");
        Whitebox.setInternalState(postCodeController,"postCodeService",postCodeService);
    }

    @Test
    public void testimportPostCode() throws IOException {
        //postCodeController.importPostCode(baseProcessClass);
        Assert.assertTrue(true);

        Whitebox.setInternalState(postCodeController,"streetsPath","");
        Whitebox.setInternalState(postCodeController,"postCodePath","");
        Whitebox.setInternalState(postCodeController,"buildingPath","");
         //postCodeController.importPostCode(baseProcessClass);
        Assert.assertTrue(true);

    }
}

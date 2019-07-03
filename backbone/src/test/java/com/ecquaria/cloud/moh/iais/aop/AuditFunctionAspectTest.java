package com.ecquaria.cloud.moh.iais.aop;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.powermock.api.mockito.mockpolicies.Slf4jMockPolicy;
import org.powermock.core.classloader.annotations.MockPolicy;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.client.RestTemplate;
import sg.gov.moh.iais.common.constant.AppConsts;
import sop.iwe.SessionManager;
import sop.rbac.user.User;

@RunWith(PowerMockRunner.class)
@MockPolicy(Slf4jMockPolicy.class)
@PrepareForTest({AuditFunctionAspect.class})
public class AuditFunctionAspectTest {
    @InjectMocks
    private AuditFunctionAspect aspect;
    @Mock
    private RestTemplate restTemplate;
    @Spy
    private MockHttpServletRequest request = new MockHttpServletRequest();


    @Before
    public void setup() {
        aspect.setRequest(request);
        aspect.setRestTemplate(restTemplate);
        User user = new User();
        user.setId("Test User");
        user.setUserDomain(AppConsts.USER_DOMAIN_INTRANET);
        request.getSession().setAttribute(SessionManager.SOP_LOGIN_INFO, user);
        request.addHeader("User-Agent", "firefox");

    }

    @Test
    public void testAspect() {

    }
}

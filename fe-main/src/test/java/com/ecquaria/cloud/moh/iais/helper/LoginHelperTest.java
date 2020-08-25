package com.ecquaria.cloud.moh.iais.helper;

import com.ecquaria.cloud.helper.SpringContextHelper;
import com.ecquaria.cloud.moh.iais.common.constant.AuditTrailConsts;
import com.ecquaria.cloud.moh.iais.web.logging.util.AuditLogUtil;
import com.ecquaria.cloud.submission.client.wrapper.SubmissionClient;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.api.mockito.mockpolicies.Slf4jMockPolicy;
import org.powermock.core.classloader.annotations.MockPolicy;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.mock.web.MockHttpServletRequest;
import sop.iwe.SessionManager;
import sop.rbac.user.User;

import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.anyObject;
import static org.powermock.api.mockito.PowerMockito.doNothing;
import static org.powermock.api.mockito.PowerMockito.when;

/**
 * LoginHelperTest
 *
 * @author Jinhua
 * @date 2020/5/12 14:07
 */
@RunWith(PowerMockRunner.class)
@MockPolicy(Slf4jMockPolicy.class)
@PowerMockIgnore("javax.management.*")
@PrepareForTest({LoginHelper.class, SessionManager.class, AccessUtil.class, IaisEGPHelper.class,
        SpringContextHelper.class, ApplicationContext.class, SubmissionClient.class, AuditLogUtil.class,
        HttpServletResponse.class})
public class LoginHelperTest {
    @Mock
    private SessionManager sessionManager;
    @Mock
    private ApplicationContext applicationContext;
    @Mock
    private SubmissionClient submissionClient;
    @Mock
    private HttpServletResponse response;

    private MockHttpServletRequest request = new MockHttpServletRequest();


    @Before
    public void setup() {
        PowerMockito.mockStatic(SessionManager.class);
        when(SessionManager.getInstance(request)).thenReturn(sessionManager);
        PowerMockito.mockStatic(SpringContextHelper.class);
        when(SpringContextHelper.getContext()).thenReturn(applicationContext);
    }

    @Test
    public void testInitUserInfo() throws Exception {
        User user = new User();
        when(sessionManager.initSopLoginInfo(request)).thenReturn(null);
        doNothing().when(sessionManager).imitateLogin(user, true, true);
        PowerMockito.mockStatic(AccessUtil.class);
        doNothing().when(AccessUtil.class, "initLoginUserInfo", request);
        PowerMockito.mockStatic(IaisEGPHelper.class);
        doNothing().when(IaisEGPHelper.class, "setAuditLoginUserInfo", anyObject());
        when(applicationContext.getBean(SubmissionClient.class)).thenReturn(submissionClient);
        PowerMockito.mockStatic(AuditLogUtil.class);
        doNothing().when(AuditLogUtil.class, "callWithEventDriven", new Object[] {anyObject(), anyObject()});
        LoginHelper.initUserInfo(request, response, user, AuditTrailConsts.LOGIN_TYPE_CORP_PASS);
        assertNotNull(user);
    }

    @Test(expected = IllegalStateException.class)
    public void testPrivate() throws NoSuchMethodException, IllegalAccessException,
            InvocationTargetException, InstantiationException {

        Class cls = LoginHelper.class;
        Constructor<LoginHelper> con = cls.getDeclaredConstructor(null);
        con.setAccessible(true);
        con.newInstance(null);
    }
}

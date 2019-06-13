package com.ecquaria.cloud.moh.iais.util;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.powermock.api.mockito.mockpolicies.Slf4jMockPolicy;
import org.powermock.core.classloader.annotations.MockPolicy;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.mock.web.MockHttpServletRequest;

@RunWith(PowerMockRunner.class)
@MockPolicy(Slf4jMockPolicy.class)
@PrepareForTest({MiscUtil.class})
public class MiscUtilTest {
    private static final String RESULTIP = "192.168.6.184";
    private static final String REMOTEIP = "127.0.0.1";
    private static final String SPLIT = ",";
    @Spy
    private MockHttpServletRequest request = new MockHttpServletRequest();
    // test the method of the getClientIp
    @Test
    public  void testgetClientIp(){
        Mockito.doReturn(RESULTIP+SPLIT+REMOTEIP).when(request).getHeader("x-forwarded-for");
        String ip =  MiscUtil.getClientIp(request);
        Assert.assertEquals(ip,RESULTIP);
    }
    @Test
    public void testgetClientIpRemoteAddr(){
        String ip =  MiscUtil.getClientIp(request);
        Assert.assertEquals(ip,REMOTEIP);
    }
}

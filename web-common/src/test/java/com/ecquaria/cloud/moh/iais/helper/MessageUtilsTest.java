package com.ecquaria.cloud.moh.iais.helper;


import com.ecquaria.cloud.helper.SpringContextHelper;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.api.mockito.mockpolicies.Slf4jMockPolicy;
import org.powermock.core.classloader.annotations.MockPolicy;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.context.ApplicationContext;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.powermock.api.mockito.PowerMockito.doNothing;
import static org.powermock.api.mockito.PowerMockito.when;

/**
 * @Author Hua_Chong
 * @Date 2019/7/26 9:30
 */
@RunWith(PowerMockRunner.class)
@MockPolicy(Slf4jMockPolicy.class)
@PrepareForTest({MessageUtil.class, RedisCacheHelper.class, SpringContextHelper.class})
public class MessageUtilsTest {
    @Mock
    private RedisCacheHelper rch;

    @Test(expected = IllegalStateException.class)
    public void testPrivate() throws NoSuchMethodException, IllegalAccessException,
            InvocationTargetException, InstantiationException {
        Class cls = MessageUtil.class;
        Constructor<MessageUtil> con = cls.getDeclaredConstructor(null);
        con.setAccessible(true);
        con.newInstance(null);
    }

    @Test
    public  void testPublics(){
        PowerMockito.mockStatic(RedisCacheHelper.class);
        PowerMockito.mockStatic(SpringContextHelper.class);
        ApplicationContext context = PowerMockito.mock(ApplicationContext.class);
        when(SpringContextHelper.getContext()).thenReturn(context);
        when(RedisCacheHelper.getInstance()).thenReturn(rch);
        doNothing().when(rch).set(anyString(), anyString(), anyObject());
        Map<String,String> message = IaisCommonUtils.genNewHashMap();
        message.put("messageKey","messageValue");
        MessageUtil.loadMessages(message);
        String messageKey =  MessageUtil.getMessageDesc("messageKey");
        String pattern = "The message {key} test";
        HashMap<String,String> arguments=IaisCommonUtils.genNewHashMap();
        arguments.put("key","value");
        String messageKey1 = MessageUtil.getMessageDesc(pattern,arguments);
        Assert.assertEquals("The message value test", messageKey1);
    }

    @Test
    public void testNullFlow(){
        MessageUtil.loadMessages(null);
        assertNotNull(MessageUtil.class);
    }

}

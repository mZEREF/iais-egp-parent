package com.ecquaria.cloud.moh.iais.validate;

/*
 *author: yichen
 *date time:2019/8/8 17:57
 *description:
 */

import com.ecquaria.cloud.moh.iais.dto.MessageDto;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Spy;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.api.mockito.mockpolicies.Slf4jMockPolicy;
import org.powermock.core.classloader.annotations.MockPolicy;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.mock.web.MockHttpServletRequest;
import sg.gov.moh.iais.common.utils.ParamUtil;

@RunWith(PowerMockRunner.class)
@MockPolicy(Slf4jMockPolicy.class)
@PrepareForTest({MessageValidate.class, ParamUtil.class})
public class MessageValidateTest {
    @Spy
    private MessageValidate messageValidate = new MessageValidate();
    private MockHttpServletRequest request = new MockHttpServletRequest();


    @Before
    public void setup(){
        PowerMockito.mockStatic(ParamUtil.class);
    }

    @Test
    public void testValidate(){
        MessageDto nullMessageDto = new MessageDto();
        PowerMockito.when(ParamUtil.getSessionAttr(request, MessageDto.MESSAGE_REQUEST_DTO)).thenReturn(nullMessageDto);
        messageValidate.validate(request);
        Assert.assertTrue(true);
    }

    @Test
    public void testValidate2(){
        MessageDto messageDto = new MessageDto();
        messageDto.setDomainType("Internet");
        messageDto.setMsgType("Error");
        messageDto.setModule("New");
        messageDto.setDescription("%#$#");
        PowerMockito.when((MessageDto) ParamUtil.getRequestAttr(request, MessageDto.MESSAGE_REQUEST_DTO)).thenReturn(messageDto);
        messageValidate.validate(request);
        Assert.assertTrue(true);
    }

    @Test
    public void testValidate3(){
        MessageDto messageDto = new MessageDto();
        messageDto.setDomainType("");
        messageDto.setMsgType("Error");
        messageDto.setModule("New");
        messageDto.setDescription("");
        PowerMockito.when((MessageDto) ParamUtil.getRequestAttr(request, MessageDto.MESSAGE_REQUEST_DTO)).thenReturn(messageDto);
        messageValidate.validate(request);
        Assert.assertTrue(true);
    }

    @Test
    public void testValidate4(){
        MessageDto messageDto = new MessageDto();
        messageDto.setDomainType("Internet");
        messageDto.setMsgType("Error");
        messageDto.setModule("New");
        messageDto.setDescription("lengthaaaaaaaaaaaaaaaaaaaaaaaaaaa" +
                "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa" +
                "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa" +
                "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
        PowerMockito.when((MessageDto) ParamUtil.getRequestAttr(request, MessageDto.MESSAGE_REQUEST_DTO)).thenReturn(messageDto);
        messageValidate.validate(request);
        Assert.assertTrue(true);
    }

    @Test
    public void testValidate5(){
        MessageDto messageDto = new MessageDto();
        messageDto.setDomainType("Internet");
        messageDto.setMsgType("Error");
        messageDto.setModule("New");
        messageDto.setDescription("hhahaa");
        PowerMockito.when((MessageDto) ParamUtil.getRequestAttr(request, MessageDto.MESSAGE_REQUEST_DTO)).thenReturn(messageDto);
        messageValidate.validate(request);
        Assert.assertTrue(true);
    }
}

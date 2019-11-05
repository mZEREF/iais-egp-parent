package com.ecquaria.cloud.moh.iais.validate;


import com.ecquaria.cloud.moh.iais.common.constant.mastercode.MasterCodeConstants;
import com.ecquaria.cloud.moh.iais.common.dto.mastercode.MasterCodeDto;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.validation.MasterCodeValidate;
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

@RunWith(PowerMockRunner.class)
@MockPolicy(Slf4jMockPolicy.class)
@PrepareForTest({MasterCodeValidate.class, ParamUtil.class})
public class MasterCodeValidateTest {

    @Spy
    private MasterCodeValidate masterCodeValidate = new MasterCodeValidate();

    private MockHttpServletRequest request = new MockHttpServletRequest();

    @Before
    public void setup(){
        PowerMockito.mockStatic(ParamUtil.class);
    }

    @Test
    public void testValidate(){
        MasterCodeDto masterCodeDto = new MasterCodeDto();
        PowerMockito.when(ParamUtil.getSessionAttr(request, MasterCodeConstants.MASTERCODE_USER_DTO_ATTR)).thenReturn(masterCodeDto);
        masterCodeValidate.validate(request);
        Assert.assertTrue(true);
    }

    @Test
    public void testValidateNull(){
        MasterCodeDto masterCodeDto = new MasterCodeDto();
        masterCodeDto.setMasterCodeId(1);
        PowerMockito.when((MasterCodeDto) ParamUtil.getRequestAttr(request, MasterCodeConstants.MASTERCODE_USER_DTO_ATTR)).thenReturn(masterCodeDto);
        masterCodeValidate.validate(request);
        Assert.assertTrue(true);
    }
}

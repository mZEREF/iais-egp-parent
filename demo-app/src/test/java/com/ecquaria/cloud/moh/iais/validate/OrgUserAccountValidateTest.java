/*
 *   This file is generated by ECQ project skeleton automatically.
 *
 *   Copyright 2019-2049, Ecquaria Technologies Pte Ltd. All rights reserved.
 *
 *   No part of this material may be copied, reproduced, transmitted,
 *   stored in a retrieval system, reverse engineered, decompiled,
 *   disassembled, localised, ported, adapted, varied, modified, reused,
 *   customised or translated into any language in any form or by any means,
 *   electronic, mechanical, photocopying, recording or otherwise,
 *   without the prior written permission of Ecquaria Technologies Pte Ltd.
 */

package com.ecquaria.cloud.moh.iais.validate;

import com.ecquaria.cloud.helper.SpringContextHelper;
import com.ecquaria.cloud.moh.iais.action.OrgUserAccountDelegator;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.RestApiUtil;
import com.ecquaria.cloud.moh.iais.dto.OrgUserAccountDto;
import com.ecquaria.cloud.moh.iais.service.impl.OrgUserAccountServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.api.mockito.mockpolicies.Slf4jMockPolicy;
import org.powermock.core.classloader.annotations.MockPolicy;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.mock.web.MockHttpServletRequest;
import sop.util.Assert;

import java.util.Map;

import static org.powermock.api.mockito.PowerMockito.doReturn;
import static org.powermock.api.mockito.PowerMockito.when;

/**
 * OrgUserAccountValidateTest
 *
 * @author suocheng
 * @date 8/7/2019
 */
@RunWith(PowerMockRunner.class)
@MockPolicy(Slf4jMockPolicy.class)
@PrepareForTest({OrgUserAccountValidate.class,SpringContextHelper.class,RestApiUtil.class,OrgUserAccountServiceImpl.class})
public class OrgUserAccountValidateTest {
    @InjectMocks
    private OrgUserAccountValidate orgUserAccountValidate ;
    private MockHttpServletRequest request = new MockHttpServletRequest();
    @Mock
    private OrgUserAccountServiceImpl orgUserAccountService ;

    @Before
    public void setup(){
        PowerMockito.mockStatic(SpringContextHelper.class);
        PowerMockito.mockStatic(RestApiUtil.class);
        ApplicationContext context = PowerMockito.mock(ApplicationContext.class);
        when(SpringContextHelper.getContext()).thenReturn(context);
        doReturn(orgUserAccountService).when(context).getBean(OrgUserAccountServiceImpl.class);
    }

    @Test
    public void testValidate(){
        OrgUserAccountDto orgUserAccountDto = new OrgUserAccountDto();
        orgUserAccountDto.setNircNo("NircNo");
        orgUserAccountDto.setId(0);
        ParamUtil.setSessionAttr(request, OrgUserAccountDelegator.ORG_USER_DTO_ATTR,orgUserAccountDto);
        OrgUserAccountDto orgUserAccountDto1 = new OrgUserAccountDto();
        orgUserAccountDto1.setId(1);
        PowerMockito.when(RestApiUtil.getByReqParam(Mockito.anyString(),Mockito.anyMap(),Mockito.any(Class.class))).thenReturn(orgUserAccountDto1);
        Map<String,String> errorMap = orgUserAccountValidate.validate(request);
        Assert.assertNotNull(errorMap);

        // test if (dto == null || StringUtil.isEmpty(dto.getNircNo())) return errMap;
        ParamUtil.setSessionAttr(request, OrgUserAccountDelegator.ORG_USER_DTO_ATTR,null);
        Map<String,String> errorMap1 = orgUserAccountValidate.validate(request);
        Assert.assertNotNull(errorMap1);


    }
}

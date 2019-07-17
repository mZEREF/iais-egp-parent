/*
 * This file is generated by ECQ project skeleton automatically.
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

package com.ecquaria.cloud.moh.iais.helper;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.api.mockito.mockpolicies.Slf4jMockPolicy;
import org.powermock.core.classloader.annotations.MockPolicy;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.mock.web.MockHttpServletRequest;
import sg.gov.moh.iais.common.constant.AppConsts;
import sg.gov.moh.iais.common.utils.MiscUtil;
import sg.gov.moh.iais.web.logging.dto.AuditTrailDto;
import sop.iwe.SessionManager;
import sop.rbac.user.User;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import static org.junit.Assert.assertEquals;

/**
 * IaisEGPHelperTest
 *
 * @author Jinhua
 * @date 2019/7/17 17:19
 */
@RunWith(PowerMockRunner.class)
@MockPolicy(Slf4jMockPolicy.class)
@PrepareForTest({IaisEGPHelper.class, MiscUtil.class})
@PowerMockIgnore("javax.management.*")
public class IaisEGPHelperTest {
    @Test(expected = IllegalStateException.class)
    public void testConstructor() throws NoSuchMethodException, IllegalAccessException,
            InvocationTargetException, InstantiationException {
        Class cls = IaisEGPHelper.class;
        Constructor<IaisEGPHelper> con = cls.getDeclaredConstructor(null);
        con.setAccessible(true);
        con.newInstance(null);
    }

    @Test
    public void testSetAuditLoginInfo() {
        IaisEGPHelper.setAuditLoginUserInfo(null);
        AuditTrailDto dto = new AuditTrailDto();
        PowerMockito.mockStatic(MiscUtil.class);
        PowerMockito.when(MiscUtil.getCurrentRequest()).thenReturn(null);
        IaisEGPHelper.setAuditLoginUserInfo(dto);
        MockHttpServletRequest request = new MockHttpServletRequest();
        User user = new User();
        user.setId("Test User");
        user.setUserDomain(AppConsts.USER_DOMAIN_INTRANET);
        SessionManager.LoginInformation lif = new SessionManager.LoginInformation();
        lif.setUser(user);
        request.getSession().setAttribute(SessionManager.SOP_LOGIN_INFO, lif);
        request.addHeader("User-Agent", "firefox");
        PowerMockito.when(MiscUtil.getCurrentRequest()).thenReturn(request);
        IaisEGPHelper.setAuditLoginUserInfo(dto);
        assertEquals("Test User", dto.getMohUserId());
    }
}

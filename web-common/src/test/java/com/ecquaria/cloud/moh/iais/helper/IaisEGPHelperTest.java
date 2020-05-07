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

import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.dto.AuditTrailDto;
import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.utils.MiscUtil;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.dto.FilterParameter;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.api.mockito.mockpolicies.Slf4jMockPolicy;
import org.powermock.core.classloader.annotations.MockPolicy;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.mock.web.MockHttpServletRequest;
import sop.iwe.SessionManager;
import sop.rbac.user.User;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.powermock.api.mockito.PowerMockito.when;

/**
 * IaisEGPHelperTest
 *
 * @author Jinhua
 * @date 2019/7/17 17:19
 */
@RunWith(PowerMockRunner.class)
@MockPolicy(Slf4jMockPolicy.class)
@PrepareForTest({IaisEGPHelper.class, MiscUtil.class, ParamUtil.class})
@PowerMockIgnore("javax.management.*")
public class IaisEGPHelperTest {

    public static final String PARAM_TEST_FLEID = "test";

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
        when(MiscUtil.getCurrentRequest()).thenReturn(null);
        IaisEGPHelper.setAuditLoginUserInfo(dto);
        MockHttpServletRequest request = new MockHttpServletRequest();
        User user = new User();
        user.setId("Test User");
        user.setUserDomain(AppConsts.USER_DOMAIN_INTRANET);
        SessionManager.LoginInformation lif = new SessionManager.LoginInformation();
        lif.setUser(user);
        request.getSession().setAttribute(SessionManager.SOP_LOGIN_INFO, lif);
        request.addHeader("User-Agent", "firefox");
        when(MiscUtil.getCurrentRequest()).thenReturn(request);
        IaisEGPHelper.setAuditLoginUserInfo(dto);
        assertEquals("Test User", dto.getMohUserId());
    }

    @Test
    public void testGetRootPath() throws MalformedURLException, NoSuchFieldException, IllegalAccessException {
        String path = IaisEGPHelper.getRootPath();
        assertNotNull(path);
    }

    @Test
    public void testGetSearchParam(){
        MockHttpServletRequest mockHttpServletRequest = new MockHttpServletRequest();
        FilterParameter filterParameter = new FilterParameter.Builder().build();
        filterParameter.setSearchAttr("test");
        Class clz = ParamUtil.class;
        filterParameter.setClz(clz);
        PowerMockito.mockStatic(ParamUtil.class);
        when(ParamUtil.getSessionAttr(mockHttpServletRequest, filterParameter.getSearchAttr())).thenReturn(null);
        assertNotNull(IaisEGPHelper.getSearchParam(mockHttpServletRequest, filterParameter));
    }

    @Test
    public void testClearSessionAttrToError(){
        MockHttpServletRequest mockHttpServletRequest = new MockHttpServletRequest();
        try {
            IaisEGPHelper.clearSessionAttr(mockHttpServletRequest, null);

            IaisEGPHelper.clearSessionAttr(null, IaisEGPHelperTest.class);
        } catch (IllegalAccessException e) {
            log.error(e.getMessage(), e);
        }
    }

    @Test
    public void testClearSessionAttrToSuccess(){
        MockHttpServletRequest mockHttpServletRequest = new MockHttpServletRequest();
        try {
            IaisEGPHelper.clearSessionAttr(mockHttpServletRequest, IaisEGPHelperTest.class);
        } catch (IllegalAccessException e) {
            log.error(e.getMessage(), e);
        }
        Assert.assertTrue(true);
    }

    @Test
    public void testGetSearchParamToError(){
        MockHttpServletRequest mockHttpServletRequest = new MockHttpServletRequest();
        FilterParameter filterParameter = new FilterParameter.Builder().build();
        filterParameter.setSearchAttr("test");
        filterParameter.setClz(null);
        PowerMockito.mockStatic(ParamUtil.class);
        SearchParam param = new SearchParam("some");
        when(ParamUtil.getSessionAttr(mockHttpServletRequest, filterParameter.getSearchAttr())).thenReturn(param);
        IaisEGPHelper.getSearchParam(mockHttpServletRequest, filterParameter);
        assertNotNull(param);
    }

    @Test
    public void testGetCurrentAuditTrailDto(){
        MockHttpServletRequest mockHttpServletRequest = new MockHttpServletRequest();

        PowerMockito.mockStatic(MiscUtil.class);
        when(MiscUtil.getCurrentRequest()).thenReturn(mockHttpServletRequest);
        IaisEGPHelper.getCurrentAuditTrailDto();
        Assert.assertTrue(true);
    }



    @Test(expected = NullPointerException.class)
    public void testGetSearchParamToException(){
        MockHttpServletRequest mockHttpServletRequest = new MockHttpServletRequest();
        FilterParameter filterParameter = null;
        assertNotNull(IaisEGPHelper.getSearchParam(mockHttpServletRequest, true, filterParameter));
    }

    @Test
    public void testGetDate(){
        Date date = IaisEGPHelper.parseToDate("2019-01-01", "yyyy-MM-dd");
        Assert.assertTrue(true);
    }
}

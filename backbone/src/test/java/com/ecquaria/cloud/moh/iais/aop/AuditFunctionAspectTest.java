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

package com.ecquaria.cloud.moh.iais.aop;

import com.ecquaria.cloud.moh.iais.dto.SearchParam;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.modules.junit4.PowerMockRunnerDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.*;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.util.AopTestUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import sg.gov.moh.iais.common.constant.AppConsts;
import sg.gov.moh.iais.web.logging.dto.AuditTrailDto;
import sop.iwe.SessionManager;
import sop.rbac.user.User;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertNotNull;
import static org.powermock.api.mockito.PowerMockito.when;

/**
 * AuditFunctionAspectTest.java
 *
 * @author Jinhua
 * @date 2019/7/4 13:46
 */
@RunWith(PowerMockRunner.class)
@PowerMockRunnerDelegate(SpringJUnit4ClassRunner.class)
@PowerMockIgnore("javax.management.*")
@PrepareForTest({AuditFunctionAspect.class, RequestContextHolder.class, SessionManager.class})
@ContextConfiguration("classpath*:**spring-config.xml")
public class AuditFunctionAspectTest {
    @Autowired
    @InjectMocks
    private TestFunctionTrack tft;
    @Autowired
    @InjectMocks
    private TestNonFunctionTrack tnf;

    @Autowired
    private ApplicationContext applicationContext;

    @Mock
    private RestTemplate restTemplate;

    @Autowired
    private AuditFunctionAspect aspect;

    private HttpEntity<Collection<AuditTrailDto>> jsonPart;
    private MockHttpServletRequest request = null;
    private SessionManager.LoginInformation lif;

    @Before
    public void setup() throws NoSuchFieldException, IllegalAccessException {
        request = new MockHttpServletRequest();
        MockitoAnnotations.initMocks(this);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        List<AuditTrailDto> adList = new ArrayList<>();
        jsonPart = new HttpEntity<>(adList, headers);
        User user = new User();
        user.setId("Test User");
        user.setUserDomain(AppConsts.USER_DOMAIN_INTRANET);
        lif = new SessionManager.LoginInformation();
        lif.setUser(user);
        request.getSession().setAttribute(SessionManager.SOP_LOGIN_INFO, lif);
        request.addHeader("User-Agent", "firefox");
        Field field = AuditFunctionAspect.class.getDeclaredField("restTemplate");
        field.setAccessible(true);
        field.set(aspect, restTemplate);
        when(restTemplate.exchange("http://localhost:8887/api/audittrail/cudTrail",
                HttpMethod.POST, jsonPart, String.class)).thenReturn(new ResponseEntity(HttpStatus.OK));
        PowerMockito.mockStatic(RequestContextHolder.class);
        ServletRequestAttributes sra = new ServletRequestAttributes(request);
        PowerMockito.when(RequestContextHolder.getRequestAttributes()).thenReturn(sra);
    }

    @Test
    public void testAround() {
        AopTestUtils.getTargetObject(tft);
        SearchParam param = new SearchParam();
        Map<String, Object> filters = param.getFilters();
        filters.put("aaaa", "bbbb");
        tft.searchForSomething(param);
        lif.setUser(null);
        assertNotNull(tft);
    }

    @Test
    public void testNon() {
        AopTestUtils.getTargetObject(tnf);
        tnf.test();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAuditFunction() {
        aspect.auditFunction();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAuditClass() {
        aspect.auditClass();
    }
}

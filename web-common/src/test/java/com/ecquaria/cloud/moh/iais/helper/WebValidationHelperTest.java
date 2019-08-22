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

import com.ecquaria.cloud.moh.iais.common.validation.ValidationUtils;
import com.ecquaria.cloud.moh.iais.common.validation.dto.ValidationResult;
import com.ecquaria.cloud.moh.iais.web.logging.dto.AuditTrailDto;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.api.mockito.mockpolicies.Slf4jMockPolicy;
import org.powermock.core.classloader.annotations.MockPolicy;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

/**
 * WebValidationHelperTest
 *
 * @author Jinhua
 * @date 2019/7/18 16:32
 */
@RunWith(PowerMockRunner.class)
@MockPolicy(Slf4jMockPolicy.class)
@PrepareForTest({WebValidationHelper.class, ValidationUtils.class, AuditTrailDto.class})
@PowerMockIgnore("javax.management.*")
public class WebValidationHelperTest {

    @Mock
    private ValidationResult validationResult;

    @Before
    public void setup() {
        PowerMockito.mockStatic(AuditTrailDto.class);
        PowerMockito.mockStatic(ValidationUtils.class);
    }

    @Test(expected = IllegalStateException.class)
    public void testConstructor() throws NoSuchMethodException, IllegalAccessException,
            InvocationTargetException, InstantiationException {
        Class cls = WebValidationHelper.class;
        Constructor<WebValidationHelper> con = cls.getDeclaredConstructor(null);
        con.setAccessible(true);
        con.newInstance(null);
    }

    @Test
    public void testValidateEntity() {
        AuditTrailDto.setThreadDto(new AuditTrailDto());
        Map<String, String> errorMap = new HashMap<>();
        errorMap.put("key","value");
        PowerMockito.when(ValidationUtils.validateEntity(Mockito.anyObject())).thenReturn(validationResult);
        PowerMockito.when(validationResult.isHasErrors()).thenReturn(true);
        PowerMockito.when(validationResult.retrieveAll()).thenReturn(errorMap);
        AuditTrailDto dto = new AuditTrailDto();
        PowerMockito.when(AuditTrailDto.getThreadDto()).thenReturn(dto);
        WebValidationHelper.validateEntity(new Object());
        Assert.assertTrue(true);
    }
    @Test
    public void testValidateProperty() {
        PowerMockito.when(ValidationUtils.validateProperty(Mockito.anyObject(),Mockito.anyString())).thenReturn(validationResult);
        PowerMockito.when(validationResult.isHasErrors()).thenReturn(false);
        WebValidationHelper.validateProperty(new Object(),"");
        Assert.assertTrue(true);
    }
    @Test
    public void testDoValidate() {
        PowerMockito.when(ValidationUtils.doValidate(Mockito.anyObject(),Mockito.anyObject())).thenReturn(validationResult);
        PowerMockito.when(validationResult.isHasErrors()).thenReturn(false);
        WebValidationHelper.doValidate(new Object[]{});
        Assert.assertTrue(true);
    }
}

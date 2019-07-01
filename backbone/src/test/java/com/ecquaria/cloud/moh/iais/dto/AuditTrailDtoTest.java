package com.ecquaria.cloud.moh.iais.dto;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Spy;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.junit.Assert.assertNotNull;

@RunWith(PowerMockRunner.class)
@PrepareForTest({AuditTrailDto.class})
public class AuditTrailDtoTest {

    @Spy
    AuditTrailDto entity = new AuditTrailDto();

    @Test
    public void testGetterSetter() {
        entity.getOperation();
        entity.setOperation(1);
        entity.getNricNumber();
        entity.setNricNumber(null);
        entity.getUenId();
        entity.setUenId(null);
        entity.getMohUserId();
        entity.setMohUserId(null);
        entity.getLoginType();
        entity.setLoginType(1);
        entity.getUserDomain();
        entity.setUserDomain(null);
        entity.getSessionId();
        entity.setSessionId(null);
        entity.getClientIp();
        entity.setClientIp(null);
        entity.getUserAgent();
        entity.setUserAgent(null);
        entity.getApplicationNum();
        entity.setApplicationNum(null);
        entity.getLicenseNum();
        entity.setLicenseNum(null);
        entity.getModule();
        entity.setModule(null);
        entity.getFunctionName();
        entity.setFunctionName(null);
        entity.getProgrameName();
        entity.setProgrameName(null);
        entity.getBeforeAction();
        entity.setBeforeAction(null);
        entity.getAfterAction();
        entity.setAfterAction(null);
        entity.getValidationFail();
        entity.setValidationFail(null);
        entity.getViewParams();
        entity.setViewParams(null);
        entity.getFailReason();
        entity.setFailReason(null);
        assertNotNull(entity);
    }
}
package com.ecquaria.cloud.moh.iais.dto;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Spy;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.junit.Assert.assertNotNull;

@RunWith(PowerMockRunner.class)
@PrepareForTest({LoginContext.class})
public class LoginContextTest {

    @Spy
    LoginContext entity = new LoginContext();

    @Test
    public void testGetterSetter() {
        entity.getLoginId();
        entity.setLoginId(null);
        entity.getUserId();
        entity.setUserId(null);
        entity.getUserDomain();
        entity.setUserDomain(null);
        entity.getUserName();
        entity.setUserName(null);
        entity.getNricNum();
        entity.setNricNum(null);
        entity.getRoleIds();
        entity.getCurRoleId();
        entity.setCurRoleId(null);
        entity.getWrkGrpIds();
        entity.getLicenseeId();
        entity.setLicenseeId(null);
        entity.getOrgId();
        entity.setOrgId(null);
        assertNotNull(entity);
    }
}
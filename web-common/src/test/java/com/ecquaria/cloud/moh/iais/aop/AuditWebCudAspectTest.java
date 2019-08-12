package com.ecquaria.cloud.moh.iais.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.api.mockito.mockpolicies.Slf4jMockPolicy;
import org.powermock.core.classloader.annotations.MockPolicy;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;
import sg.gov.moh.iais.web.logging.aop.AuditCudAspect;

import static org.junit.Assert.assertNotNull;
import static org.powermock.api.mockito.PowerMockito.when;

/**
 * AuditWebCudAspectTest
 *
 * @author Jinhua
 * @date 2019/8/12 10:58
 */
@RunWith(PowerMockRunner.class)
@MockPolicy(Slf4jMockPolicy.class)
@PowerMockIgnore("javax.management.*")
@PrepareForTest({AuditWebCudAspect.class, AuditCudAspect.class})
public class AuditWebCudAspectTest {
    @Test
    public void test() throws Throwable {
        AuditWebCudAspect aspect = new AuditWebCudAspect();
        AuditCudAspect aca = PowerMockito.mock(AuditCudAspect.class);
        ProceedingJoinPoint point = PowerMockito.mock(ProceedingJoinPoint.class);
        Whitebox.setInternalState(AuditWebCudAspect.class, "auditCudAspect", aca);
        when(aca.doAround(point)).thenReturn(null);
        aspect.daoTrail();
        aspect.doAround(point);
        assertNotNull(aspect);
    }
}

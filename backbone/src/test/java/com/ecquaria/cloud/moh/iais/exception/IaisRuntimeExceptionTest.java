package com.ecquaria.cloud.moh.iais.exception;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.mockpolicies.Slf4jMockPolicy;
import org.powermock.core.classloader.annotations.MockPolicy;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@MockPolicy(Slf4jMockPolicy.class)
@PrepareForTest({IaisRuntimeException.class})
public class IaisRuntimeExceptionTest {

    @Test
    public void test() {
        RuntimeException exp = new RuntimeException();
        new IaisRuntimeException(exp);
        new IaisRuntimeException("aaadddd", exp);
        new IaisRuntimeException("aaadddd");
    }
}

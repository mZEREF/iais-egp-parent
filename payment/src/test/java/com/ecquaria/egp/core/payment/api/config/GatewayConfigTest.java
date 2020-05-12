package com.ecquaria.egp.core.payment.api.config;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Spy;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.junit.Assert.assertNotNull;

@RunWith(PowerMockRunner.class)
@PrepareForTest({GatewayConfig.class})
public class GatewayConfigTest {

    @Spy
    GatewayConfig entity = new GatewayConfig();

    @Test
    public void testGetterSetter() {
        assertNotNull(entity);
    }
}
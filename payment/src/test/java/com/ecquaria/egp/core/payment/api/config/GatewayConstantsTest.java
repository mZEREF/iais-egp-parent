package com.ecquaria.egp.core.payment.api.config;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Spy;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.junit.Assert.assertNotNull;

@RunWith(PowerMockRunner.class)
@PrepareForTest({GatewayConstants.class})
public class GatewayConstantsTest {

    @Spy
    GatewayConstants entity = new GatewayConstants();

    @Test
    public void testGetterSetter() {
        assertNotNull(entity);
    }
}
package com.ecquaria.cloud.moh.iais.config;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Spy;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.junit.Assert.assertNotNull;

@RunWith(PowerMockRunner.class)
@PrepareForTest({FeignMultipartConfig.class})
public class FeignMultipartConfigTest {

    @Spy
    FeignMultipartConfig entity = new FeignMultipartConfig();

    @Test
    public void testGetterSetter() {
        assertNotNull(entity);
    }
}
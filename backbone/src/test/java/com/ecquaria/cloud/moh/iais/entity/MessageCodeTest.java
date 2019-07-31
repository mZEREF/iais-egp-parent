package com.ecquaria.cloud.moh.iais.entity;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Spy;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.junit.Assert.assertNotNull;

@RunWith(PowerMockRunner.class)
@PrepareForTest({MessageCode.class})
public class MessageCodeTest {

    @Spy
    MessageCode entity = new MessageCode();

    @Test
    public void testGetterSetter() {
        entity.getMsgId();
        entity.setMsgId(1);
        entity.getCodeKey();
        entity.setCodeKey(null);
        entity.getDescription();
        entity.setDescription(null);
        assertNotNull(entity);
    }
}
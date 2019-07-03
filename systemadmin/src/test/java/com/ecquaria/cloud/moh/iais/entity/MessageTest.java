package com.ecquaria.cloud.moh.iais.entity;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Spy;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.junit.Assert.assertNotNull;

@RunWith(PowerMockRunner.class)
@PrepareForTest({Message.class})
public class MessageTest {

    @Spy
    Message entity = new Message();

    @Test
    public void testGetterSetter() {
        entity.getCodeId();
        entity.setCodeId(null);
        entity.getCodeKey();
        entity.setCodeKey(null);
        entity.getType();
        entity.setType(null);
        entity.getMessageType();
        entity.setMessageType(null);
        entity.getModule();
        entity.setModule(null);
        entity.getDescription();
        entity.setDescription(null);
        entity.getStatus();
        entity.setStatus(null);
        assertNotNull(entity);
    }
}
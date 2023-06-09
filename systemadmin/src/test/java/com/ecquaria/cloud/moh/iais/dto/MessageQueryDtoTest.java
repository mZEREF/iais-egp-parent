package com.ecquaria.cloud.moh.iais.dto;

/*
 *author: yichen
 *date time:2019/8/26 14:42
 *description:
 */

import com.ecquaria.cloud.moh.iais.common.dto.message.MessageQueryDto;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Spy;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.junit.Assert.assertNotNull;

@RunWith(PowerMockRunner.class)
@PrepareForTest({MessageQueryDto.class})
public class MessageQueryDtoTest {

    @Spy
    MessageQueryDto entity = new MessageQueryDto();

    @Test
    public void testGetterSetter() {
        entity.getId();
        entity.getDomainType();
        entity.getCodeKey();
        entity.getModule();
        entity.getMsgType();
        entity.getDescription();
        entity.getStatus();
        entity.getMessage();

        entity.setId(null);
        entity.setDomainType(null);
        entity.setCodeKey(null);
        entity.setMsgType(null);
        entity.setDescription(null);
        entity.setStatus(null);
        entity.setMessage(null);
        assertNotNull(entity);
    }

}

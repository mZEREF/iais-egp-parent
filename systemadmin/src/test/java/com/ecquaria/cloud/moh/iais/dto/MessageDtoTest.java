package com.ecquaria.cloud.moh.iais.dto;

import com.ecquaria.cloud.moh.iais.common.dto.message.MessageDto;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Spy;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.junit.Assert.assertNotNull;

/*
 *author: yichen
 *date time:2019/8/8 16:56
 *description:
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(MessageDto.class)
public class MessageDtoTest {

    @Spy
    MessageDto dto = new MessageDto();

    @Test
    public void testMessageDto(){
        dto.getId();
        dto.getDomainType();
        dto.getCodeKey();
        dto.getModule();
        dto.getMsgType();
        dto.getDescription();
        dto.getStatus();
        dto.getAuditTrailDto();
        dto.getMessage();

        dto.setId(null);
        dto.setDomainType(null);
        dto.setCodeKey(null);
        dto.setMsgType(null);
        dto.setDescription(null);
        dto.setStatus(null);
        dto.setMessage(null);

        dto.validateDescriptionRegEx("");
        dto.validateDescriptionRegEx("asd");
        dto.validateDescriptionRegEx("&*()");

        dto.setAuditTrailDto(null);
        assertNotNull(dto);
    }
}

package com.ecquaria.cloud.moh.iais.dto;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Spy;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

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
        dto.getRowguid();
        dto.getDomainType();
        dto.getCodeKey();
        dto.getModule();
        dto.getMsgType();
        dto.getDescription();
        dto.getStatus();
        dto.getAuditTrailDto();

        dto.setId(null);
        dto.setRowguid(null);
        dto.setDomainType(null);
        dto.setCodeKey(null);
        dto.setMsgType(null);
        dto.setDescription(null);
        dto.setStatus(null);

        dto.validateDescriptionRegEx("");
        dto.validateDescriptionRegEx("asd");
        dto.validateDescriptionRegEx("&*()");

        dto.setAuditTrailDto(null);
    }
}

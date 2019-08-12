package com.ecquaria.cloud.moh.iais.entity;

/*
 *author: yichen
 *date time:2019/8/12 10:39
 *description:
 */

import com.ecquaria.cloud.moh.iais.dto.MessageDto;
import org.junit.Test;
import org.mockito.Spy;

public class MessageTest {
    @Spy
    Message msg = new Message();

    @Test
    public void testMessageDto(){
        msg.getId();
        msg.getRowguid();
        msg.getDomainType();
        msg.getCodeKey();
        msg.getModule();
        msg.getMsgType();
        msg.getDescription();
        msg.getStatus();

        msg.setId(null);
        msg.setRowguid(null);
        msg.setDomainType(null);
        msg.setCodeKey(null);
        msg.setMsgType(null);
        msg.setDescription(null);
        msg.setStatus(null);
    }
}

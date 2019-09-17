package com.ecquaria.cloud.moh.iais.service.impl;

/*
 *File Name: MessageServiceImpl
 *Creator: yichen
 *Creation time:2019/8/2 10:49
 *Describe:
 */

import com.ecquaria.cloud.moh.iais.annotation.SearchTrack;
import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.utils.RestApiUtil;
import com.ecquaria.cloud.moh.iais.dto.MessageDto;
import com.ecquaria.cloud.moh.iais.dto.MessageQueryDto;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.service.MessageService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class MessageServiceImpl implements MessageService {

    @SearchTrack(catalog = "message", key = "search")
    public SearchResult<MessageQueryDto> doQuery(SearchParam searchParam) {
        return  RestApiUtil.query("system-admin-service:8886/iais-message/results", searchParam);
    }

    public void saveMessage(MessageDto messageDto) {
        messageDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        RestApiUtil.save("system-admin-service:8886/iais-message", messageDto);
    }

    public MessageDto getMessageByRowguid(String rowguid) {
        return IaisEGPHelper.getRecordByRowguid("system-admin-service:8886/iais-message", rowguid, MessageDto.class);
    }

}

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
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.service.MessageService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class MessageServiceImpl implements MessageService {

    @SearchTrack(catalog = "message", key = "search")
    public SearchResult<MessageDto> doQuery(SearchParam param) {
        return  RestApiUtil.query("/iais-message/results", param);
    }

    public void saveMessage(MessageDto messageDto) {
        messageDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        RestApiUtil.save("/iais-message/messages", messageDto);
    }

    public MessageDto getMessageByRowguid(String rowguid) {
        Map<String, Object> map = new HashMap<>();
        map.put("rowguid", rowguid);
        return RestApiUtil.getByReqParam("/iais-message/rowguid", map, MessageDto.class);
    }

}

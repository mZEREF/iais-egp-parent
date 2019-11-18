package com.ecquaria.cloud.moh.iais.service.impl;

/*
 *File Name: MessageServiceImpl
 *Creator: yichen
 *Creation time:2019/8/2 10:49
 *Describe:
 */

import com.ecquaria.cloud.moh.iais.annotation.SearchTrack;
import com.ecquaria.cloud.moh.iais.common.constant.rest.RestApiUrlConsts;
import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.message.MessageDto;
import com.ecquaria.cloud.moh.iais.common.dto.message.MessageQueryDto;
import com.ecquaria.cloud.moh.iais.common.utils.RestApiUtil;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.service.MessageService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class MessageServiceImpl implements MessageService {

    @SearchTrack(catalog = "message", key = "search")
    @Override
    public SearchResult<MessageQueryDto> doQuery(SearchParam searchParam) {
        return  RestApiUtil.query(RestApiUrlConsts.MESSAGE_RESULTS, searchParam);
    }

    @Override
    public void saveMessage(MessageDto messageDto) {
        messageDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        RestApiUtil.save(RestApiUrlConsts.SYSTEM_ADMIN_SERVICE + RestApiUrlConsts.IAIS_MESSAGE, messageDto);
    }

    @Override
    public MessageDto getMessageById(String id) {
        Map<String, Object> paramMapper = new HashMap<>();
        paramMapper.put("id", id);
        return RestApiUtil.getByReqParam(RestApiUrlConsts.GET_MESSAGE_BY_ID, paramMapper, MessageDto.class);
    }

}

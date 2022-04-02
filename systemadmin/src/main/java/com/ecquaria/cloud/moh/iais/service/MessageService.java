package com.ecquaria.cloud.moh.iais.service;

/*
 *File Name: MessageService
 *Creator: yichen
 *Creation time:2019/8/2 10:49
 *Describe:
 */

import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.message.MessageDto;
import com.ecquaria.cloud.moh.iais.common.dto.message.MessageQueryDto;

import java.util.List;

public interface MessageService {
    SearchResult<MessageQueryDto> doQuery(SearchParam param);

    void saveMessage(MessageDto messageDto);

    MessageDto getMessageById(String id);

    List<String> listModuleTypes();
}

package com.ecquaria.cloud.moh.iais.service;

/*
 *File Name: MessageService
 *Creator: yichen
 *Creation time:2019/8/2 10:49
 *Describe:
 */

import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.dto.MessageDto;

public interface MessageService {
    SearchResult<MessageDto> doQuery(SearchParam param);

    void saveMessage(MessageDto msg);

    MessageDto getMessageByRowguid(String rowguid);
}

package com.ecquaria.cloud.moh.iais.service;

/*
 *File Name: MsgService
 *Creator: yichen
 *Creation time:2019/8/2 10:49
 *Describe:
 */

import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.entity.Message;

public interface MsgService {
    SearchResult<Message> doSearch(SearchParam param, String catalog, String key);

    void saveMessage(Message msg);

    Message getMessageByRowguid(String rowguid);
}

package com.ecquaria.cloud.moh.iais.service;

/*
 *File Name: MsgService
 *Creator: yichen
 *Creation time:2019/8/2 10:49
 *Describe:
 */

import com.ecquaria.cloud.moh.iais.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.entity.Message;

public interface MsgService {
    SearchResult<Message> doSearch(SearchParam param, String catalog, String key);

    void deleteMessageById(Integer id);

    void saveMessage(Message msg);

    Message getMessageByMsgId(Integer id);
}

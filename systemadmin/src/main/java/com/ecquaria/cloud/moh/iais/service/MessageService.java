package com.ecquaria.cloud.moh.iais.service;

import com.ecquaria.cloud.moh.iais.dto.SearchConditionDTO;
import com.ecquaria.cloud.moh.iais.entity.Message;

import java.util.List;

public interface MessageService {
    List<Message> listMessageBySearchCondition(SearchConditionDTO condition);
    void updateMessageByCodeId(String codeId, String desctipion);
}

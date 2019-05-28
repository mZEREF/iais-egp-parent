package sg.gov.moh.iais.service;

import sg.gov.moh.iais.dto.SearchConditionDTO;
import sg.gov.moh.iais.entity.Message;

import java.util.List;

public interface MessageService {
    List<Message> listMessageBySearchCondition(SearchConditionDTO condition);
    void updateMessageByCodeId(String codeId, String desctipion);
}

package com.ecquaria.cloud.moh.iais.service;

import com.ecquaria.cloud.moh.iais.common.dto.inbox.InterMessageDto;

/**
 * InboxMsgService
 *
 * @author suocheng
 * @date 12/17/2019
 */

public interface InboxMsgService {

    public InterMessageDto saveInterMessage(InterMessageDto interMessageDto);
    public String getMessageNo();

}

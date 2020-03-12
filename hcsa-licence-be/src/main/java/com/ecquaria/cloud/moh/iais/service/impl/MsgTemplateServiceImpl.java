package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.common.dto.templates.MsgTemplateDto;
import com.ecquaria.cloud.moh.iais.service.MsgTemplateService;
import com.ecquaria.cloud.moh.iais.service.client.MsgTemplateClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * MsgTemplateServiceImpl
 *
 * @author suocheng
 * @date 3/12/2020
 */
@Service
@Slf4j
public class MsgTemplateServiceImpl implements MsgTemplateService {

    @Autowired
    private MsgTemplateClient msgTemplateClient;

    @Override
    public MsgTemplateDto getMessageById(String id) {
        return msgTemplateClient.getMsgTemplate(id).getEntity();
    }
}

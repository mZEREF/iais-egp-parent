package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.templates.MsgTemplateDto;
import com.ecquaria.cloud.moh.iais.common.dto.templates.MsgTemplateQueryDto;
import com.ecquaria.cloud.moh.iais.service.TemplatesService;
import com.ecquaria.cloud.moh.iais.service.client.MsgTemplateClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author: Hc
 * @Program: iais-egp
 * @Create: 2019-12-23 15:02
 **/
@Service
public class TemplatesServiceImpl implements TemplatesService {

    @Autowired
    private MsgTemplateClient msgTemplateClient;

    @Override
    public SearchResult<MsgTemplateQueryDto> getTemplateResults(SearchParam param) {
        return msgTemplateClient.getMsgTemplateResult(param).getEntity();
    }

    @Override
    public MsgTemplateDto getMsgTemplate(String id) {
        return msgTemplateClient.getMsgTemplate(id).getEntity();
    }

    @Override
    public MsgTemplateDto updateMsgTemplate(MsgTemplateDto msgTemplateDto) {
        return msgTemplateClient.updateMasterCode(msgTemplateDto).getEntity();
    }
}

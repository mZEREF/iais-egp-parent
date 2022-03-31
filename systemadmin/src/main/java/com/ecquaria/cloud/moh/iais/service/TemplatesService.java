package com.ecquaria.cloud.moh.iais.service;

import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.templates.MsgTemplateDto;
import com.ecquaria.cloud.moh.iais.common.dto.templates.MsgTemplateQueryDto;

import java.util.List;

/**
 * @Author: Hc
 * @Program: iais-egp
 * @Create: 2019-12-23 15:01
 **/

public interface TemplatesService {
    SearchResult<MsgTemplateQueryDto> getTemplateResults(SearchParam param);
    MsgTemplateDto getMsgTemplate(String id);
    MsgTemplateDto updateMsgTemplate(MsgTemplateDto msgTemplateDto);
    List<String> suggestTemplateCodeDescription(String code);

    void syncTemplateFe(MsgTemplateDto msgTemplateDto);
}

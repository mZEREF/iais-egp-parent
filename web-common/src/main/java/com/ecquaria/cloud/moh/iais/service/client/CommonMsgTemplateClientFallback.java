package com.ecquaria.cloud.moh.iais.service.client;

import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.templates.MsgTemplateDto;
import com.ecquaria.cloud.moh.iais.common.dto.templates.MsgTemplateQueryDto;
import com.ecquaria.cloudfeign.FeignResponseEntity;

/**
 * @Author: Hc
 * @Program: iais-egp
 * @Create: 2019-12-24 10:53
 **/
public class CommonMsgTemplateClientFallback implements CommonMsgTemplateClient {

    @Override
    public FeignResponseEntity<SearchResult<MsgTemplateQueryDto>> getMsgTemplateResult(SearchParam param) {
        return null;
    }

    @Override
    public FeignResponseEntity<MsgTemplateDto> getMsgTemplate(String id) {
        return null;
    }

    @Override
    public FeignResponseEntity<MsgTemplateDto> updateMasterCode(MsgTemplateDto dto) {
        return null;
    }
}

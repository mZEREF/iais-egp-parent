package com.ecquaria.cloud.moh.iais.service.client;

import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.templates.MsgTemplateDto;
import com.ecquaria.cloud.moh.iais.common.dto.templates.MsgTemplateQueryDto;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloudfeign.FeignResponseEntity;

import java.util.List;

/**
 * @Author: Hc
 * @Program: iais-egp
 * @Create: 2019-12-24 10:53
 **/
public class MsgTemplateMainClientFallback implements MsgTemplateMainClient{
    @Override
    public FeignResponseEntity<SearchResult<MsgTemplateQueryDto>> getMsgTemplateResult(SearchParam param) {
        return IaisEGPHelper.getFeignResponseEntity("getMsgTemplateResult",param);
    }

    @Override
    public FeignResponseEntity<MsgTemplateDto> getMsgTemplate(String id) {
        return IaisEGPHelper.getFeignResponseEntity("getMsgTemplate",id);
    }

    @Override
    public FeignResponseEntity<MsgTemplateDto> updateMasterCode(MsgTemplateDto dto) {
        return IaisEGPHelper.getFeignResponseEntity("updateMasterCode",dto);
    }

    @Override
    public FeignResponseEntity<List<MsgTemplateDto>> getAlertMsgTemplate(String domain) {
        return IaisEGPHelper.getFeignResponseEntity("getAlertMsgTemplate",domain);
    }
}

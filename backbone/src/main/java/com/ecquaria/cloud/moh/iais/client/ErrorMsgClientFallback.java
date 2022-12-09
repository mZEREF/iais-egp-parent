package com.ecquaria.cloud.moh.iais.client;

import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.entity.MessageCode;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloudfeign.FeignResponseEntity;

/**
 * ErrorMsgClientFallback
 *
 * @author Jinhua
 * @date 2019/11/25 14:23
 */
public class ErrorMsgClientFallback implements ErrorMsgClient{
    @Override
    public FeignResponseEntity<SearchResult<MessageCode>> retrieveErrorMsgs(SearchParam param){
        return IaisEGPHelper.getFeignResponseEntity("retrieveErrorMsgs", param);
    }

    @Override
    public FeignResponseEntity<String> getValueByPropertiesKey(String propertiesKey) {
        return IaisEGPHelper.getFeignResponseEntity("retrieveErrorMsgs", propertiesKey);
    }

}

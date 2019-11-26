package com.ecquaria.cloud.moh.iais.client;

import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.entity.MessageCode;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import org.springframework.http.HttpHeaders;

/**
 * ErrorMsgClientFallback
 *
 * @author Jinhua
 * @date 2019/11/25 14:23
 */
public class ErrorMsgClientFallback {
    public FeignResponseEntity<SearchResult<MessageCode>> retrieveErrorMsgs(SearchParam param){
        FeignResponseEntity entity = new FeignResponseEntity<>();
        HttpHeaders headers = new HttpHeaders();
        entity.setHeaders(headers);
        return entity;
    }
}

package com.ecquaria.cloud.moh.iais.client;

import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.entity.MessageCode;
import com.ecquaria.cloudfeign.FeignConfiguration;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * ErrorMsgClient
 *
 * @author Jinhua
 * @date 2019/11/25 14:22
 */
@FeignClient(name = "system-admin", configuration = FeignConfiguration.class,
        fallback = ErrorMsgClientFallback.class)
public interface ErrorMsgClient {
    @RequestMapping(path = "/iais-message/allMsg",method = RequestMethod.POST, produces = { MediaType.APPLICATION_JSON_VALUE },
            consumes = {MediaType.APPLICATION_JSON_VALUE})
    FeignResponseEntity<SearchResult<MessageCode>> retrieveErrorMsgs(SearchParam param);
}

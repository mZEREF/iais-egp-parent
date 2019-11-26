package com.ecquaria.cloud.moh.iais.client;

import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.mastercode.MasterCodeView;
import com.ecquaria.cloudfeign.FeignConfiguration;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * MasterCodeClient
 *
 * @author Jinhua
 * @date 2019/11/25 21:41
 */
@FeignClient(name = "system-admin", configuration = FeignConfiguration.class,
        fallback = MasterCodeClientFallback.class)
public interface MasterCodeClient {
    @RequestMapping(path = "/iais-mastercode/caches",method = RequestMethod.POST, produces = { MediaType.APPLICATION_JSON_VALUE },
            consumes = {MediaType.APPLICATION_JSON_VALUE})
    FeignResponseEntity<SearchResult<MasterCodeView>> retrieveMasterCodes(SearchParam param);
}

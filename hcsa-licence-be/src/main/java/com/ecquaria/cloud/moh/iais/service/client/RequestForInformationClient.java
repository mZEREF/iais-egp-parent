package com.ecquaria.cloud.moh.iais.service.client;

import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.ReqForInfoSearchListDto;
import com.ecquaria.cloudfeign.FeignConfiguration;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * RequestForInformationClient
 *
 * @author junyu
 * @date 2019/12/18
 */
@FeignClient(name = "hcsa-application", configuration = FeignConfiguration.class,
        fallback = RequestForInformationClientFallback.class)
public interface RequestForInformationClient {
    @RequestMapping(path = "/iais-reqForInfo/reqForInfo-searchParam",method = RequestMethod.POST, produces = { MediaType.APPLICATION_JSON_VALUE },
            consumes = {MediaType.APPLICATION_JSON_VALUE})
    FeignResponseEntity<SearchResult<ReqForInfoSearchListDto>> searchRfiApp(SearchParam searchParam);
}

package com.ecquaria.cloud.moh.iais.service.client;

import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.inbox.InboxLicenceQueryDto;
import com.ecquaria.cloudfeign.FeignConfiguration;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


/**
 * @Author: Hc
 * @Program: iais-egp
 * @Create: 2019-11-29 13:17
 **/
@FeignClient(name = "hcsa-licence",configuration = FeignConfiguration.class,fallback = LicenceInboxFallback.class)
public interface LicenceInboxClient {
    @RequestMapping(path = "/hcsa-licence/licence-param",method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<SearchResult<InboxLicenceQueryDto>> searchResultFromLicence(@RequestBody SearchParam searchParam);
}
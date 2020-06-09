package com.ecquaria.cloud.moh.iais.service.client;

import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDraftDto;
import com.ecquaria.cloud.moh.iais.common.dto.inbox.InboxAppQueryDto;
import com.ecquaria.cloudfeign.FeignConfiguration;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @Author: Hc
 * @Program: iais-egp
 * @Create: 2019-11-29 09:57
 **/
@FeignClient(name = "hcsa-application",configuration = FeignConfiguration.class,fallback = AssessmentSchematicsClientFallback.class)
public interface AssessmentSchematicsClient {

    @RequestMapping(path = "/iais-application/app-param",method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<SearchResult<InboxAppQueryDto>> searchResultFromApp(SearchParam searchParam);

    @RequestMapping(path = "/iais-submission/draft-service-name",method = RequestMethod.GET)
    FeignResponseEntity<ApplicationDraftDto> getDraftInfo(@RequestParam(value = "draftId") String draftId);


}

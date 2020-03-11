package com.ecquaria.cloud.moh.iais.service.client;

import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDraftDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.inbox.InboxAppQueryDto;
import com.ecquaria.cloudfeign.FeignConfiguration;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @Author: Hc
 * @Program: iais-egp
 * @Create: 2019-11-29 09:57
 **/
@FeignClient(name = "hcsa-application",configuration = FeignConfiguration.class,fallback = AppInboxFallback.class)
public interface AppInboxClient {

    @RequestMapping(path = "/iais-application/app-param",method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<SearchResult<InboxAppQueryDto>> searchResultFromApp(SearchParam searchParam);

    @GetMapping(value = "/iais-application/app-grp-ids/{licenseeId}")
    FeignResponseEntity<List<String>> getAppGrpIdsByLicenseeIs(@PathVariable(value = "licenseeId")String licenseeId);

    @RequestMapping(path = "/iais-submission/draftNumber/{appNo}",method = RequestMethod.GET)
    FeignResponseEntity<String> getDraftNumber(@PathVariable("appNo")String appNo);

    @RequestMapping(path = "/iais-submission/drafts",method = RequestMethod.POST)
    FeignResponseEntity<List<ApplicationDraftDto>> getDraftList();

    @RequestMapping(path = "/iais-submission/application-rfc", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<AppSubmissionDto> saveAppsForRequestForChange(@RequestBody AppSubmissionDto appSubmissionDto);

    @GetMapping(value = "/iais-submission/submission-id")
    FeignResponseEntity<String> getSubmissionId();

    @GetMapping(value = "/iais-submission/application-status-draft",consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<Integer> getAppDraftNum(@RequestParam(value = "appGrpIds") List<String> appOrgIds);


    @RequestMapping(path = "/iais-application/application-licenceId",method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<ApplicationDto> getApplicaitonByLicenceId(@RequestParam(name = "licenceId")String licenceId);

    @PostMapping(value = "/iais-submission/draft/{draftNo}/{status}")
    FeignResponseEntity<String> updateDraftStatus(@PathVariable("draftNo")String draftNo, @PathVariable("status")String status);
}

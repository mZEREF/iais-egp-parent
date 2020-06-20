package com.ecquaria.cloud.moh.iais.service.client;

import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesCorrelationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDraftDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.inbox.InboxAppQueryDto;
import com.ecquaria.cloudfeign.FeignConfiguration;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

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

    @RequestMapping(path = "/iais-submission/draft-service-name",method = RequestMethod.GET)
    FeignResponseEntity<ApplicationDraftDto> getDraftInfo(@RequestParam(value = "draftId") String draftId);

    @RequestMapping(path = "/iais-submission/application-rfc", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<AppSubmissionDto> saveAppsForRequestForChange(@RequestBody AppSubmissionDto appSubmissionDto);

    @GetMapping(value = "/iais-submission/submission-id")
    FeignResponseEntity<String> getSubmissionId();

    @GetMapping(value = "/iais-submission/application-status-draft",consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<Integer> getAppDraftNum(@RequestParam(value = "licenseeId") String licenseeId);

    @RequestMapping(path = "/iais-application/application-licenceId",method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<ApplicationDto> getApplicaitonByLicenceId(@RequestParam(name = "licenceId")String licenceId);

    @PutMapping(value = "/iais-submission/draft/{draftNo}/{status}")
    FeignResponseEntity<String> updateDraftStatus(@PathVariable("draftNo")String draftNo, @PathVariable("status")String status);

    @GetMapping(path = "/iais-application/application/correlations/{appid}")
    FeignResponseEntity<List<AppPremisesCorrelationDto>> listAppPremisesCorrelation(@PathVariable(name = "appid") String appId);

    @GetMapping(path = "/iais-application/application-licenceId", produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<ApplicationDto>> getAppByLicIdAndExcludeNew(@RequestParam(name = "licenceId")String licenceId);

    @GetMapping(path = "/appeal/appeal-eligibility", produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<Boolean> isAppealEligibility(@RequestParam("id") String id);

    @PutMapping(value = "/app-fe-status")
    FeignResponseEntity<Void> updateFeAppStatus(@RequestParam(value = "appId") String appId,@RequestParam(value = "appStatus") String appStatus);

    @PostMapping(value = "/appeal/list-can-ceased",consumes = MediaType.APPLICATION_JSON_VALUE,produces =MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<Map<String,Boolean>> listCanCeased(@RequestBody List<String> licIds);

}

package com.ecquaria.cloud.moh.iais.service.client;

import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.application.AppPremisesDoQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.appeal.AppPremiseMiscDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppGrpPremisesEntityDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesCorrelationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDraftDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationGroupDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationSubDraftDto;
import com.ecquaria.cloud.moh.iais.common.dto.inbox.InboxAppQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.inbox.InterMessageSearchDto;
import com.ecquaria.cloudfeign.FeignConfiguration;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

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

    @PostMapping(value = "/iais-submission/application-status-draft",consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<Integer> getAppDraftNum(@RequestBody InterMessageSearchDto interMessageSearchDto);

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

    @PutMapping(value = "/iais-application/app-fe-status")
    FeignResponseEntity<Void> updateFeAppStatus(@RequestParam(value = "appId") String appId,@RequestParam(value = "appStatus") String appStatus);

    @PostMapping(value = "/appeal/list-can-ceased",consumes = MediaType.APPLICATION_JSON_VALUE,produces =MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<Map<String,Boolean>> listCanCeased(@RequestBody List<String> licIds);
    @GetMapping(value = "/iais-application/applicationdto-id/{appId}", produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<ApplicationDto> getApplicarionById(@PathVariable(name = "appId")String appId);

    @PostMapping(value = "/iais-submission/darft-service-codes",consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<String> selectDarft(@RequestBody Map<String,Object> serviceCodes);
    @GetMapping(value = "/appeal/licence-appeal-or-cessation-by-licence-id")
    FeignResponseEntity<Boolean> isLiscenceAppealOrCessation(@RequestParam("licenceId") String licenceId);
    @GetMapping(value = "/appeal/application-withdrawal-by-app-id")
    FeignResponseEntity<Boolean> isApplicationWithdrawal(@RequestParam("appId") String appId);
    @GetMapping(value = "/iais-submission/get-draft-by-lic-app-id",produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<ApplicationSubDraftDto>> getDraftByLicAppId(@RequestParam(value = "licAppId",required = false)String licAppId);
    @PutMapping(value = "/iais-submission/delete-draft-by-no")
    FeignResponseEntity  deleteDraftByNo(@RequestParam("draftNo") String draftNo);
    @GetMapping(value = "/iais-submission/draft-app-no")
    FeignResponseEntity<ApplicationDraftDto>  getDraftInfoByAppNo(@RequestParam("appNo") String appNo);
    @PostMapping(value = "/iais-application/pending-app-premises",consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<AppGrpPremisesEntityDto>> getPendAppPremises(@RequestBody AppPremisesDoQueryDto appPremisesDoQueryDto);
    @DeleteMapping(value = "/iais-submission/draft-by-numbers",consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<String> deleteDraftNUmber(@RequestBody List<String> draftNumbers);
    @GetMapping(value = "/appeal/app-premise-misc-dto-relate-id",produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<AppPremiseMiscDto>> getAppPremiseMiscDtoRelateId(@RequestParam("relateId") String relateId);

    @GetMapping(value = "/iais-application/app-group-by-licensee-id/{licenseeId}")
    FeignResponseEntity<List<ApplicationGroupDto>> getAppGrpsByLicenseeId(@PathVariable(name = "licenseeId") String licenseeId);

    @GetMapping(value = "/iais-application/application/grp/{groupId}")
    FeignResponseEntity<ApplicationGroupDto> getApplicationGroup(@PathVariable(name = "groupId")String groupId);

    @GetMapping(value = "/iais-application/applicationdto-id/{appId}", produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<ApplicationDto> getApplicationById(@PathVariable(name = "appId") String appId);

    @GetMapping(value = "/iais-submission/draft-by-lic-app-id",produces = MediaType.APPLICATION_JSON_VALUE,consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<ApplicationSubDraftDto>> getDraftByLicAppIdAndStatus(@RequestParam("licAppId") String licAppId,@RequestParam("status") String status);
    @PostMapping(value = "/iais-submission/draft-by-svc-codes",produces = MediaType.APPLICATION_JSON_VALUE,consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<ApplicationSubDraftDto>> getDraftListBySvcCodeAndStatus(@RequestBody List<String> svcCodeList, @RequestParam("licenseeId")String licenseeId, @RequestParam("status")String status,@RequestParam("appType")String appType);

    @GetMapping(value = "/iais-application/app-one/{appPremCorrId}")
    FeignResponseEntity<ApplicationDto> getApplicationByCorreId(@PathVariable(name = "appPremCorrId")String appPremCorrId);
}

package com.ecquaria.cloud.moh.iais.service.client;

import com.ecquaria.cloud.moh.iais.common.dto.application.AdhocCheckListConifgDto;
import com.ecquaria.cloud.moh.iais.common.dto.appointment.ApptFeConfirmDateDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.appeal.AppealApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.appeal.AppealLicenceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppEditSelectDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesInspecApptDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSubmissionForAuditDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.checklist.BeSyncCompareDataRequest;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.checklist.BeSyncCompareDataResponse;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.checklist.SyncDataBody;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.EventBusLicenceGroupDtos;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicPremisesReqForInfoDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.risksm.HcsaRiskFeSupportDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServicePrefInspPeriodDto;
import com.ecquaria.cloud.moh.iais.common.dto.inbox.InterMessageDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspRectificationSaveDto;
import com.ecquaria.cloudfeign.FeignConfiguration;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

/**
 * EicGatewayClient
 *
 * @author suocheng
 * @date 2019/12/14 17:33
 */
@FeignClient(value = "eicgate", url="${iais.intra.gateway.url}", configuration = {FeignConfiguration.class},
        fallback = BeEicGatewayClientFallback.class)
public interface BeEicGatewayClient {
    @RequestMapping(value = "/v1/hcsa-licence-transport-licence/",method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<EventBusLicenceGroupDtos> createLicence(@RequestBody EventBusLicenceGroupDtos eventBusLicenceGroupDtos,
                 @RequestHeader("date") String date,
                 @RequestHeader("authorization") String authorization,
                 @RequestHeader("date-Secondary") String dateSec,
                 @RequestHeader("authorization-Secondary") String authorizationSec);
    @RequestMapping(value = "/v1/iais-application/",method = RequestMethod.PUT,consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<ApplicationDto> updateApplication(@RequestBody ApplicationDto applicationDto,
              @RequestHeader("date") String date,
              @RequestHeader("authorization") String authorization,
              @RequestHeader("date-Secondary") String dateSec,
              @RequestHeader("authorization-Secondary") String authorizationSec);

    @RequestMapping(value = "/v1/iais-inter-inbox-message/",method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<InterMessageDto> saveInboxMessage(@RequestBody InterMessageDto interInboxDto,
              @RequestHeader("date") String date,
              @RequestHeader("authorization") String authorization,
              @RequestHeader("date-Secondary") String dateSec,
              @RequestHeader("authorization-Secondary") String authorizationSec);

    @PostMapping(value = "/v1/rfi-fe-bridge/", consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<LicPremisesReqForInfoDto> createLicPremisesReqForInfoFe(
            @RequestBody LicPremisesReqForInfoDto licPremisesReqForInfoDto,
            @RequestHeader("date") String date,
            @RequestHeader("authorization") String authorization,
            @RequestHeader("date-Secondary") String dateSec,
            @RequestHeader("authorization-Secondary") String authorizationSec);

    @RequestMapping(value = "/v1/app-req-for-info/",method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<AppEditSelectDto> createAppEditSelectDto(@RequestBody AppEditSelectDto  appEditSelectDto,
                                                        @RequestHeader("date") String date,
                                                        @RequestHeader("authorization") String authorization,
                                                        @RequestHeader("date-Secondary") String dateSec,
                                                        @RequestHeader("authorization-Secondary") String authorizationSec);

    @PutMapping(path = "/v1/iais-application/status")
    FeignResponseEntity<Void> updateStatus(@RequestBody Map<String,List<String>> map,
                                           @RequestHeader("date") String date,
                                           @RequestHeader("authorization") String authorization,
                                           @RequestHeader("date-Secondary") String dateSec,
                                           @RequestHeader("authorization-Secondary") String authorizationSec
    );

    @RequestMapping(value = "/v1/hcsa-licence-transport-appeal/",method = RequestMethod.PUT,consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<AppealLicenceDto> updateAppealLicence(@RequestBody AppealLicenceDto appealLicenceDto,
                                                              @RequestHeader("date") String date,
                                                              @RequestHeader("authorization") String authorization,
                                                              @RequestHeader("date-Secondary") String dateSec,
                                                              @RequestHeader("authorization-Secondary") String authorizationSec);

    @RequestMapping(value = "/v1/appeal-beApplication/",method = RequestMethod.PUT,consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<AppealApplicationDto> updateAppealApplication(@RequestBody AppealApplicationDto appealApplicationDto,
                                                                @RequestHeader("date") String date,
                                                                @RequestHeader("authorization") String authorization,
                                                                @RequestHeader("date-Secondary") String dateSec,
                                                                @RequestHeader("authorization-Secondary") String authorizationSec);

    @PostMapping(value = "/v1/inspec-sync-rectification/",consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<AppPremisesInspecApptDto>> createAppPremisesInspecApptDto(@RequestBody List<AppPremisesInspecApptDto> appPremisesInspecApptDtos,
                                                                 @RequestHeader("date") String date,
                                                                 @RequestHeader("authorization") String authorization,
                                                                 @RequestHeader("date-Secondary") String dateSec,
                                                                 @RequestHeader("authorization-Secondary") String authorizationSec);

    @PostMapping(value = "/v1/hcsa-app-insdate-down", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<ApptFeConfirmDateDto> reSchedulingSaveFeDate(@RequestBody ApptFeConfirmDateDto apptFeConfirmDateDto,
                                                                     @RequestHeader("date") String date,
                                                                     @RequestHeader("authorization") String authorization,
                                                                     @RequestHeader("date-Secondary") String dateSec,
                                                                     @RequestHeader("authorization-Secondary") String authorizationSec);

    @PostMapping(value = "/v1/hcsa-inspect-nc-down", produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<InspRectificationSaveDto> beCreateNcData(@RequestBody InspRectificationSaveDto inspRectificationSaveDto,
                                                                 @RequestHeader("date") String date,
                                                                 @RequestHeader("authorization") String authorization,
                                                                 @RequestHeader("date-Secondary") String dateSec,
                                                                 @RequestHeader("authorization-Secondary") String authorizationSec);

    @PostMapping(value = "/v1/hcsal/riskscore-configs", produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<HcsaRiskFeSupportDto> feCreateRiskData(@RequestBody HcsaRiskFeSupportDto hcsaRiskFeSupportDto,
                                                                 @RequestHeader("date") String date,
                                                                 @RequestHeader("authorization") String authorization,
                                                                 @RequestHeader("date-Secondary") String dateSec,
                                                                 @RequestHeader("authorization-Secondary") String authorizationSec);


    @PostMapping(value = "/v1/hcsa-chklst-sync-comp", consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<BeSyncCompareDataResponse>> compareFeData(@RequestBody BeSyncCompareDataRequest beSyncCompareDataRequest,
                                                                       @RequestHeader("date") String date,
                                                                       @RequestHeader("authorization") String authorization,
                                                                       @RequestHeader("date-Secondary") String dateSec,
                                                                       @RequestHeader("authorization-Secondary") String authorizationSec);

    @PostMapping(value = "/v1/hcsa-chklst-sync-save", consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<Void> saveSyncData(@RequestBody SyncDataBody syncDataBody,
                                                                       @RequestHeader("date") String date,
                                                                       @RequestHeader("authorization") String authorization,
                                                                       @RequestHeader("date-Secondary") String dateSec,
                                                                       @RequestHeader("authorization-Secondary") String authorizationSec);

    @GetMapping(value = "/v1/new-app-no", consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<String> getAppNo(@RequestParam(value = "type") String applicationType,
                                                                 @RequestHeader("date") String date,
                                                                 @RequestHeader("authorization") String authorization,
                                                                 @RequestHeader("date-Secondary") String dateSec,
                                                                 @RequestHeader("authorization-Secondary") String authorizationSec);


    @PutMapping(value = "/v1/hcsa-insp-period-sync", consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<Void> syncInspPeriodToFe(@RequestBody HcsaServicePrefInspPeriodDto period,
                                         @RequestHeader("date") String date,
                                         @RequestHeader("authorization") String authorization,
                                         @RequestHeader("date-Secondary") String dateSec,
                                         @RequestHeader("authorization-Secondary") String authorizationSec);

    @PostMapping(value = "/v1/hcsa-audit-insp-app-sync", produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<AppSubmissionForAuditDto> saveAppForAuditToFe(@RequestBody AppSubmissionForAuditDto appSubmissionForAuditDto,
                                                                  @RequestHeader("date") String date,
                                                                  @RequestHeader("authorization") String authorization,
                                                                  @RequestHeader("date-Secondary") String dateSec,
                                                                  @RequestHeader("authorization-Secondary") String authorizationSec);


    @PostMapping(value = "/v1/hcsa-adhoc-chkl-sync", produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<HttpStatus> syncAdhocItemData(@RequestBody AdhocCheckListConifgDto configDto,
                                                      @RequestHeader("date") String date,
                                                      @RequestHeader("authorization") String authorization,
                                                      @RequestHeader("date-Secondary") String dateSec,
                                                      @RequestHeader("authorization-Secondary") String authorizationSec);

    @PutMapping(value = "/v1/hcsa-licence-status", produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<HttpStatus> updateFeLicDto(@RequestBody List<LicenceDto> licenceDtos,
                                                   @RequestHeader("date") String date,
                                                   @RequestHeader("authorization") String authorization,
                                                   @RequestHeader("date-Secondary") String dateSec,
                                                   @RequestHeader("authorization-Secondary") String authorizationSec);
}

package com.ecquaria.cloud.moh.iais.service.client;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.appeal.AppealApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.appeal.AppealLicenceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppEditSelectDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesInspecApptDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.EventBusLicenceGroupDtos;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicPremisesReqForInfoDto;
import com.ecquaria.cloud.moh.iais.common.dto.inbox.InterMessageDto;
import com.ecquaria.cloudfeign.FeignConfiguration;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

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

    @RequestMapping(value = "/v1/hcsa-licence-transport-appeal/",method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE)
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
    FeignResponseEntity<AppEditSelectDto> createAppPremisesInspecApptDto(@RequestBody List<AppPremisesInspecApptDto> appPremisesInspecApptDtos,
                                                                 @RequestHeader("date") String date,
                                                                 @RequestHeader("authorization") String authorization,
                                                                 @RequestHeader("date-Secondary") String dateSec,
                                                                 @RequestHeader("authorization-Secondary") String authorizationSec);
}

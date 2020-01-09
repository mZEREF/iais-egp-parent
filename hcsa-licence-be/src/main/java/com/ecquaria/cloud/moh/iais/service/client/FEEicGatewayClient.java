package com.ecquaria.cloud.moh.iais.service.client;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicPremisesReqForInfoDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenceGroupDto;
import com.ecquaria.cloud.moh.iais.common.dto.inbox.InterMessageDto;
import com.ecquaria.cloud.moh.iais.config.FeignMultipartConfig;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import java.util.List;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * EicGatewayClient
 *
 * @author suocheng
 * @date 2019/12/14 17:33
 */
@FeignClient(value = "eicgate", url="${iais.inter.gateway.url}", configuration = {FeignMultipartConfig.class},
        fallback = FEEicGatewayClientFallback.class)
public interface FEEicGatewayClient {
    @RequestMapping(value = "/hcsa-licence-transport-licence/",method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<LicenceGroupDto>> createLicence(@RequestBody List<LicenceGroupDto> licenceGroupDtoList,
                 @RequestHeader("date") String date,
                 @RequestHeader("authorization") String authorization,
                 @RequestHeader("date_Secondary") String dateSec,
                 @RequestHeader("authorization_Secondary") String authorizationSec);
    @RequestMapping(value = "/iais-application/",method = RequestMethod.PUT,consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<ApplicationDto> updateApplication(@RequestBody ApplicationDto applicationDto,
              @RequestHeader("date") String date,
              @RequestHeader("authorization") String authorization,
              @RequestHeader("date_Secondary") String dateSec,
              @RequestHeader("authorization_Secondary") String authorizationSec);

    @RequestMapping(value = "/iais-inter-inbox-message/",method = RequestMethod.PUT,consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<InterMessageDto> saveInboxMessage(@RequestBody InterMessageDto interInboxDto,
              @RequestHeader("date") String date,
              @RequestHeader("authorization") String authorization,
              @RequestHeader("date_Secondary") String dateSec,
              @RequestHeader("authorization_Secondary") String authorizationSec);
    @PostMapping(value = "/rfi-fe-bridge/", consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<LicPremisesReqForInfoDto> createLicPremisesReqForInfoFe(
            @RequestBody LicPremisesReqForInfoDto licPremisesReqForInfoDto,
            @RequestHeader("date") String date,
            @RequestHeader("authorization") String authorization,
            @RequestHeader("date_Secondary") String dateSec,
            @RequestHeader("authorization_Secondary") String authorizationSec);

}

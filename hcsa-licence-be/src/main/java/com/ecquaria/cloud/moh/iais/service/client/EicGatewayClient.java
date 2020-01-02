package com.ecquaria.cloud.moh.iais.service.client;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicPremisesReqForInfoDto;
import com.ecquaria.cloud.moh.iais.config.FeignMultipartConfig;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

/**
 * @author Wenkang
 * @date 2019/12/28 10:33
 */
@FeignClient(value = "eicgate",  configuration = {FeignMultipartConfig.class},
        fallback = ComSystemAdminClientFallback.class)
public interface EicGatewayClient {

    @PostMapping(value = "/rfi-fe-bridge/", consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<LicPremisesReqForInfoDto> createLicPremisesReqForInfoFe(@RequestBody LicPremisesReqForInfoDto licPremisesReqForInfoDto,
                                                  @RequestHeader("date") String date,
                                                  @RequestHeader("authorization") String authorization);
}

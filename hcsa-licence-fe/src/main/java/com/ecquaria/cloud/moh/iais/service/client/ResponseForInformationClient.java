package com.ecquaria.cloud.moh.iais.service.client;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicPremisesReqForInfoDto;
import com.ecquaria.cloudfeign.FeignConfiguration;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

/**
 * RequestForInformationClient
 *
 * @author junyu
 * @date 2019/12/18
 */
@FeignClient(name = "hcsa-licence", configuration = FeignConfiguration.class,
        fallback = ResponseForInformationClientFallback.class)
public interface ResponseForInformationClient {

    @GetMapping(value = "/hcsa-reqForInfo/licence-search-rfi/{licenseeId}")
    FeignResponseEntity<List<LicPremisesReqForInfoDto>> searchLicPreRfiBylicenseeId(@PathVariable("licenseeId") String licenseeId);

    @GetMapping (value = "/hcsa-reqForInfo/licence-one-rfi/{reqForInfoId}")
    FeignResponseEntity<LicPremisesReqForInfoDto> getLicPreReqForInfo(@PathVariable("reqForInfoId") String id);


}

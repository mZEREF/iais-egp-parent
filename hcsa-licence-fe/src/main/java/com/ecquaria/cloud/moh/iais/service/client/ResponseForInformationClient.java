package com.ecquaria.cloud.moh.iais.service.client;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicPremisesReqForInfoDto;
import com.ecquaria.cloudfeign.FeignConfiguration;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

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
    @PostMapping(value = "/hcsa-resForInfo-fe/create-rfi-fe",consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<LicPremisesReqForInfoDto> createLicPremisesReqForInfoFe(@RequestBody LicPremisesReqForInfoDto licPremisesReqForInfoDto);

    @GetMapping(value = "/hcsa-reqForInfo-fe/licence-search-rfi/{licenseeId}")
    FeignResponseEntity<List<LicPremisesReqForInfoDto>> searchLicPreRfiBylicenseeId(@PathVariable("licenseeId") String licenseeId);

    @GetMapping (value = "/hcsa-reqForInfo-fe/licence-one-rfi/{reqForInfoId}")
    FeignResponseEntity<LicPremisesReqForInfoDto> getLicPreReqForInfo(@PathVariable("reqForInfoId") String id);

    @DeleteMapping(value = "/hcsa-reqForInfo-fe/licence-cancel-rfi",consumes = MediaType.APPLICATION_JSON_VALUE)
    void deleteLicPremisesReqForInfo(@RequestBody String id);


}

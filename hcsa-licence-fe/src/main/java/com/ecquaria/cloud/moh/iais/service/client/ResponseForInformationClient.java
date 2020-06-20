package com.ecquaria.cloud.moh.iais.service.client;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicPremisesReqForInfoDto;
import com.ecquaria.cloudfeign.FeignConfiguration;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

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

    @GetMapping(value = "/hcsa-resForInfo-fe/licence-search-rfi/{licenseeId}")
    FeignResponseEntity<List<LicPremisesReqForInfoDto>> searchLicPreRfiBylicenseeId(@PathVariable("licenseeId") String licenseeId);

    @GetMapping (value = "/hcsa-resForInfo-fe/licence-one-rfi/{reqForInfoId}")
    FeignResponseEntity<LicPremisesReqForInfoDto> getLicPreReqForInfo(@PathVariable("reqForInfoId") String id);

    @PostMapping(value = "/hcsa-resForInfo-fe/licence-accept-rfi",consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<LicPremisesReqForInfoDto> acceptLicPremisesReqForInfo(@RequestBody LicPremisesReqForInfoDto licPremisesReqForInfoDto);



}

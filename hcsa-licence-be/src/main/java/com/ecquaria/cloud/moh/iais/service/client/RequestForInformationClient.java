package com.ecquaria.cloud.moh.iais.service.client;

import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicPremisesReqForInfoDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.RfiLicenceQueryDto;
import com.ecquaria.cloudfeign.FeignConfiguration;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * RequestForInformationClient
 *
 * @author junyu
 * @date 2019/12/18
 */
@FeignClient(name = "hcsa-licence", configuration = FeignConfiguration.class,
        fallback = RequestForInformationClientFallback.class)
public interface RequestForInformationClient {
    @RequestMapping(path = "/hcsa-licence/rfi-licence-param",method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<SearchResult<RfiLicenceQueryDto>> searchRfiLicence(@RequestBody SearchParam searchParam);

    @PostMapping(value = "/hcsa-reqForInfo/create-rfi",consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<LicPremisesReqForInfoDto> createLicPremisesReqForInfo(@RequestBody LicPremisesReqForInfoDto licPremisesReqForInfoDto);


    @GetMapping(value = "/hcsa-reqForInfo/licence-search-rfi/{licPremId}")
    FeignResponseEntity<List<LicPremisesReqForInfoDto>> searchLicPremisesReqForInfo(@PathVariable("licPremId")String licPremId);

    @GetMapping (value = "/hcsa-reqForInfo/licence-one-rfi/{reqForInfoId}")
    FeignResponseEntity<LicPremisesReqForInfoDto> getLicPreReqForInfo(@PathVariable("reqForInfoId")String id);

    @PostMapping(value = "/hcsa-reqForInfo/update-rfi",consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<LicPremisesReqForInfoDto> updateLicPremisesReqForInfoFe(@RequestBody LicPremisesReqForInfoDto licPremisesReqForInfoDto);

    @GetMapping(value = "/hcsa-reqForInfo/licence-all-rfi")
    FeignResponseEntity<List<LicPremisesReqForInfoDto>> getAllReqForInfo();

    @PostMapping(value = "/hcsa-reqForInfo/update-reply-to-be",consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<LicPremisesReqForInfoDto> rfiFeUpdateToBe(@RequestBody LicPremisesReqForInfoDto licPremisesReqForInfoDto);
}

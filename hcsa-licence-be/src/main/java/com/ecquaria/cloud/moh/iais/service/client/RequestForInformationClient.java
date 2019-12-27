package com.ecquaria.cloud.moh.iais.service.client;

import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicPremisesReqForInfoDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.RfiLicenceQueryDto;
import com.ecquaria.cloudfeign.FeignConfiguration;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
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
    void createLicPremisesReqForInfo(@RequestBody LicPremisesReqForInfoDto licPremisesReqForInfoDto);

    @GetMapping(value = "/hcsa-reqForInfo/licence-search-rfi/{licPremId}")
    FeignResponseEntity<List<LicPremisesReqForInfoDto>> searchLicPremisesReqForInfo(@PathVariable("licPremId")String licPremId);

    @GetMapping (value = "/licence-one-rfi/{reqForInfoId}")
    FeignResponseEntity<LicPremisesReqForInfoDto> getLicPreReqForInfo(@PathVariable("reqForInfoId")String id);

    @DeleteMapping(value = "/hcsa-reqForInfo/licence-cancel-rfi",consumes = MediaType.APPLICATION_JSON_VALUE)
    void deleteLicPremisesReqForInfo(@RequestBody String id);

    @PostMapping(value = "/hcsa-reqForInfo/licence-accept-rfi",consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
    void acceptLicPremisesReqForInfo(@RequestBody String id);
}

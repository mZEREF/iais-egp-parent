package com.ecquaria.cloud.moh.iais.service.client;

import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.GiroAccountFormDocDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.GiroAccountInfoDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.GiroAccountInfoQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.OrganizationPremisesViewQueryDto;
import com.ecquaria.cloudfeign.FeignConfiguration;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * GiroAccountBeClient
 *
 * @author junyu
 * @date 2021/3/3
 */
@FeignClient(name = "hcsa-licence", configuration = FeignConfiguration.class,
        fallback = GiroAccountBeClientFallback.class)
public interface GiroAccountBeClient {
    @PostMapping(value = "/hcsa-giro/lic-giro-acct-param", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<SearchResult<GiroAccountInfoQueryDto>> searchGiroInfoByParam(@RequestBody SearchParam searchParam);

    @PostMapping(value = "/hcsa-giro/lic-org-prem-view-param", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<SearchResult<OrganizationPremisesViewQueryDto>> searchOrgPremByParam(@RequestBody SearchParam searchParam);

    @PostMapping(value = "/hcsa-giro/create-giro-acct")
    FeignResponseEntity<List<GiroAccountInfoDto>> createGiroAccountInfo(@RequestBody List<GiroAccountInfoDto> giroAccountInfoDtos);

    @PostMapping(value = "/hcsa-giro/update-giro-acct")
    FeignResponseEntity<Void> updateGiroAccountInfo(@RequestBody List<GiroAccountInfoDto> giroAccountInfoDtos);

    @GetMapping(value = "/hcsa-giro/giro-acct-doc-by-id/{acctId}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<GiroAccountFormDocDto>> findGiroAccountFormDocDtoListByAcctId(@PathVariable(name = "acctId") String acctId);

    @GetMapping(value = "/hcsa-giro/giro-acct-info-by-id/{acctId}", consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<GiroAccountInfoDto> findGiroAccountInfoDtoByAcctId(@PathVariable(name = "acctId")String acctId);
}
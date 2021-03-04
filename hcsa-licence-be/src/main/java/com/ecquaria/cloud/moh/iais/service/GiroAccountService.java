package com.ecquaria.cloud.moh.iais.service;

import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.GiroAccountFormDocDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.GiroAccountInfoDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.GiroAccountInfoQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.OrganizationPremisesViewQueryDto;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * GiroAccountService
 *
 * @author junyu
 * @date 2021/3/3
 */
public interface GiroAccountService {

    SearchResult<GiroAccountInfoQueryDto> searchGiroInfoByParam(SearchParam searchParam);

    SearchResult<OrganizationPremisesViewQueryDto> searchOrgPremByParam( SearchParam searchParam);

    GiroAccountInfoDto createGiroAccountInfo( GiroAccountInfoDto giroAccountInfoDto);

    Void updateGiroAccountInfo( GiroAccountInfoDto giroAccountInfoDto);

    List<GiroAccountFormDocDto> findGiroAccountFormDocDtoListByAcctId(String acctId);

}

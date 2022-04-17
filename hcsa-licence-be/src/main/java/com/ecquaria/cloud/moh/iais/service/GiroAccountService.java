package com.ecquaria.cloud.moh.iais.service;

import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.GiroAccountFormDocDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.GiroAccountInfoDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.GiroAccountInfoQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.OrganizationPremisesViewQueryDto;

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

    List<GiroAccountInfoDto> createGiroAccountInfo( List<GiroAccountInfoDto> giroAccountInfoDto);

    void updateGiroAccountInfo(List<GiroAccountInfoDto> giroAccountInfoDto);

    List<GiroAccountFormDocDto> findGiroAccountFormDocDtoListByAcctId(String acctId);

    GiroAccountInfoDto findGiroAccountInfoDtoByAcctId(String acctId);

    void syncFeGiroAcctDto(List<GiroAccountInfoDto> giroAccountInfoDto);

    void sendEmailForGiroAccountAndSMSAndMessage(GiroAccountInfoDto giroAccountInfoDto,int size);
}

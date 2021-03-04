package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.GiroAccountFormDocDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.GiroAccountInfoDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.GiroAccountInfoQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.OrganizationPremisesViewQueryDto;
import com.ecquaria.cloud.moh.iais.service.GiroAccountService;
import com.ecquaria.cloud.moh.iais.service.client.GiroAccountBeClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * GiroAccountServiceImpl
 *
 * @author junyu
 * @date 2021/3/3
 */
@Slf4j
@Service
public class GiroAccountServiceImpl implements GiroAccountService {

    @Autowired
    GiroAccountBeClient giroAccountBeClient;

    @Override
    public SearchResult<GiroAccountInfoQueryDto> searchGiroInfoByParam(SearchParam searchParam) {
        return giroAccountBeClient.searchGiroInfoByParam(searchParam).getEntity();
    }

    @Override
    public SearchResult<OrganizationPremisesViewQueryDto> searchOrgPremByParam(SearchParam searchParam) {
        return giroAccountBeClient.searchOrgPremByParam(searchParam).getEntity();
    }

    @Override
    public GiroAccountInfoDto createGiroAccountInfo(GiroAccountInfoDto giroAccountInfoDto) {
        return giroAccountBeClient.createGiroAccountInfo(giroAccountInfoDto).getEntity();
    }

    @Override
    public Void updateGiroAccountInfo(GiroAccountInfoDto giroAccountInfoDto) {
        giroAccountBeClient.updateGiroAccountInfo(giroAccountInfoDto);
        return null;
    }

    @Override
    public List<GiroAccountFormDocDto> findGiroAccountFormDocDtoListByAcctId(String acctId) {
        return giroAccountBeClient.findGiroAccountFormDocDtoListByAcctId(acctId).getEntity();
    }

    @Override
    public GiroAccountInfoDto findGiroAccountInfoDtoByAcctId(String acctId) {
        return giroAccountBeClient.findGiroAccountInfoDtoByAcctId(acctId).getEntity();
    }
}

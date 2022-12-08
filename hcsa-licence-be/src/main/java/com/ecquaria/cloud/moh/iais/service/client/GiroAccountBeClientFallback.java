package com.ecquaria.cloud.moh.iais.service.client;

import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.GiroAccountFormDocDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.GiroAccountInfoDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.GiroAccountInfoQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.OrganizationPremisesViewQueryDto;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloudfeign.FeignResponseEntity;

import java.util.List;

/**
 * GiroAccountBeClientFallback
 *
 * @author junyu
 * @date 2021/3/3
 */
public class GiroAccountBeClientFallback implements GiroAccountBeClient {

    @Override
    public FeignResponseEntity<SearchResult<GiroAccountInfoQueryDto>> searchGiroInfoByParam(SearchParam searchParam) {
        return IaisEGPHelper.getFeignResponseEntity("searchGiroInfoByParam", searchParam);
    }

    @Override
    public FeignResponseEntity<SearchResult<OrganizationPremisesViewQueryDto>> searchOrgPremByParam(SearchParam searchParam) {
        return IaisEGPHelper.getFeignResponseEntity("searchOrgPremByParam", searchParam);
    }

    @Override
    public FeignResponseEntity<List<GiroAccountInfoDto>> createGiroAccountInfo(List<GiroAccountInfoDto> giroAccountInfoDtos) {
        return IaisEGPHelper.getFeignResponseEntity("createGiroAccountInfo", giroAccountInfoDtos);
    }

    @Override
    public FeignResponseEntity<Void> updateGiroAccountInfo(List<GiroAccountInfoDto> giroAccountInfoDtos) {
        return IaisEGPHelper.getFeignResponseEntity("updateGiroAccountInfo", giroAccountInfoDtos);
    }

    @Override
    public FeignResponseEntity<List<GiroAccountFormDocDto>> findGiroAccountFormDocDtoListByAcctId(String acctId) {
        return IaisEGPHelper.getFeignResponseEntity("findGiroAccountFormDocDtoListByAcctId", acctId);
    }

    @Override
    public FeignResponseEntity<GiroAccountInfoDto> findGiroAccountInfoDtoByAcctId(String acctId) {
        return IaisEGPHelper.getFeignResponseEntity("findGiroAccountInfoDtoByAcctId", acctId);
    }

}

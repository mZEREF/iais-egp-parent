package com.ecquaria.cloud.moh.iais.service.client;

import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.GiroAccountFormDocDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.GiroAccountInfoDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.GiroAccountInfoQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.OrganizationPremisesViewQueryDto;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import org.springframework.http.HttpHeaders;

import java.util.List;

/**
 * GiroAccountBeClientFallback
 *
 * @author junyu
 * @date 2021/3/3
 */
public class GiroAccountBeClientFallback implements GiroAccountBeClient{
    @Override
    public FeignResponseEntity<SearchResult<GiroAccountInfoQueryDto>> searchGiroInfoByParam(SearchParam searchParam) {
        FeignResponseEntity entity = new FeignResponseEntity<>();
        HttpHeaders headers = new HttpHeaders();
        entity.setHeaders(headers);
        return entity;
    }

    @Override
    public FeignResponseEntity<SearchResult<OrganizationPremisesViewQueryDto>> searchOrgPremByParam(SearchParam searchParam) {
        FeignResponseEntity entity = new FeignResponseEntity<>();
        HttpHeaders headers = new HttpHeaders();
        entity.setHeaders(headers);
        return entity;
    }

    @Override
    public FeignResponseEntity<GiroAccountInfoDto> createGiroAccountInfo(GiroAccountInfoDto giroAccountInfoDto) {
        FeignResponseEntity entity = new FeignResponseEntity<>();
        HttpHeaders headers = new HttpHeaders();
        entity.setHeaders(headers);
        return entity;
    }

    @Override
    public FeignResponseEntity<Void> updateGiroAccountInfo(GiroAccountInfoDto giroAccountInfoDto) {
        FeignResponseEntity entity = new FeignResponseEntity<>();
        HttpHeaders headers = new HttpHeaders();
        entity.setHeaders(headers);
        return entity;
    }

    @Override
    public FeignResponseEntity<List<GiroAccountFormDocDto>> findGiroAccountFormDocDtoListByAcctId(String acctId) {
        FeignResponseEntity entity = new FeignResponseEntity<>();
        HttpHeaders headers = new HttpHeaders();
        entity.setHeaders(headers);
        return entity;
    }

    @Override
    public FeignResponseEntity<GiroAccountInfoDto> findGiroAccountInfoDtoByAcctId(String acctId) {
        FeignResponseEntity entity = new FeignResponseEntity<>();
        HttpHeaders headers = new HttpHeaders();
        entity.setHeaders(headers);
        return entity;
    }
}

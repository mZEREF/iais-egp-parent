package com.ecquaria.cloud.moh.iais.service.client;

import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicPremisesReqForInfoDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.RfiLicenceQueryDto;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * RequestForInformationClientFallback
 *
 * @author junyu
 * @date 2019/12/18
 */
@Component
public class RequestForInformationClientFallback implements RequestForInformationClient{

    @Override
    public FeignResponseEntity<SearchResult<RfiLicenceQueryDto>> searchRfiLicence(SearchParam searchParam) {
        FeignResponseEntity entity = new FeignResponseEntity<>();
        HttpHeaders headers = new HttpHeaders();
        entity.setHeaders(headers);
        return entity;
    }

    @Override
    public FeignResponseEntity<LicPremisesReqForInfoDto> createLicPremisesReqForInfo(LicPremisesReqForInfoDto licPremisesReqForInfoDto) {
        FeignResponseEntity entity = new FeignResponseEntity<>();
        HttpHeaders headers = new HttpHeaders();
        entity.setHeaders(headers);
        return entity;
    }



    @Override
    public FeignResponseEntity<List<LicPremisesReqForInfoDto>> searchLicPremisesReqForInfo(String licPremId) {
        FeignResponseEntity entity = new FeignResponseEntity<>();
        HttpHeaders headers = new HttpHeaders();
        entity.setHeaders(headers);
        return entity;    }

    @Override
    public FeignResponseEntity<LicPremisesReqForInfoDto> getLicPreReqForInfo(String id) {
        FeignResponseEntity entity = new FeignResponseEntity<>();
        HttpHeaders headers = new HttpHeaders();
        entity.setHeaders(headers);
        return entity;
    }

    @Override
    public void deleteLicPremisesReqForInfo(String id) {
        FeignResponseEntity entity = new FeignResponseEntity<>();
        HttpHeaders headers = new HttpHeaders();
        entity.setHeaders(headers);
    }

    @Override
    public void acceptLicPremisesReqForInfo(String id) {
        FeignResponseEntity entity = new FeignResponseEntity<>();
        HttpHeaders headers = new HttpHeaders();
        entity.setHeaders(headers);
    }
    @Override
    public FeignResponseEntity<LicPremisesReqForInfoDto> updateLicPremisesReqForInfoFe(LicPremisesReqForInfoDto licPremisesReqForInfoDto) {
        FeignResponseEntity entity = new FeignResponseEntity<>();
        HttpHeaders headers = new HttpHeaders();
        entity.setHeaders(headers);
        return entity;
    }
}

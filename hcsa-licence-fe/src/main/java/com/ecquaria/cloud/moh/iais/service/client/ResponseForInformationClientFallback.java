package com.ecquaria.cloud.moh.iais.service.client;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicPremisesReqForInfoDto;
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
public class ResponseForInformationClientFallback implements ResponseForInformationClient {



    @Override
    public FeignResponseEntity<List<LicPremisesReqForInfoDto>> searchLicPreRfiBylicenseeId(String licenseeId){
        FeignResponseEntity entity = new FeignResponseEntity<>();
        HttpHeaders headers = new HttpHeaders();
        entity.setHeaders(headers);
        return entity;
    }

    @Override
    public FeignResponseEntity<LicPremisesReqForInfoDto> getLicPreReqForInfo(String id) {
        FeignResponseEntity entity = new FeignResponseEntity<>();
        HttpHeaders headers = new HttpHeaders();
        entity.setHeaders(headers);
        return entity;
    }

    @Override
    public FeignResponseEntity<LicPremisesReqForInfoDto> acceptLicPremisesReqForInfo(LicPremisesReqForInfoDto licPremisesReqForInfoDto) {
        FeignResponseEntity entity = new FeignResponseEntity<>();
        HttpHeaders headers = new HttpHeaders();
        entity.setHeaders(headers);
        return entity;
    }



    @Override
    public FeignResponseEntity<LicPremisesReqForInfoDto> createLicPremisesReqForInfoFe(LicPremisesReqForInfoDto licPremisesReqForInfoDto) {
        FeignResponseEntity entity = new FeignResponseEntity<>();
        HttpHeaders headers = new HttpHeaders();
        entity.setHeaders(headers);
        return entity;
    }

}

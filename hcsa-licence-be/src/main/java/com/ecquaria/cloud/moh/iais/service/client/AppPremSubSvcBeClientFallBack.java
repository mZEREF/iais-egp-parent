package com.ecquaria.cloud.moh.iais.service.client;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremSubSvcRelDto;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import org.springframework.http.HttpHeaders;

import java.util.List;


public class AppPremSubSvcBeClientFallBack implements AppPremSubSvcBeClient {

    @Override
    public FeignResponseEntity<List<AppPremSubSvcRelDto>> getAppPremSubSvcRelDtoListByCorrId(String appPremCorrId) {
        FeignResponseEntity entity = new FeignResponseEntity<>();
        HttpHeaders headers = new HttpHeaders();
        entity.setHeaders(headers);
        return entity;
    }

    @Override
    public FeignResponseEntity<List<AppPremSubSvcRelDto>> getAppPremSubSvcRelDtoListByCorrIdAndType(String appPremCorrId, String type) {
        FeignResponseEntity entity = new FeignResponseEntity<>();
        HttpHeaders headers = new HttpHeaders();
        entity.setHeaders(headers);
        return entity;
    }
}

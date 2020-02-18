package com.ecquaria.cloud.moh.iais.service.client;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesInspecApptDto;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import org.springframework.http.HttpHeaders;

/**
 * @author Shicheng
 * @date 2020/2/18 11:27
 **/
public class InspectionFeClientFallback implements InspectionFeClient {
    @Override
    public FeignResponseEntity<AppPremisesInspecApptDto> getSpecificDtoByAppPremCorrId(String appPremCorrId) {
        FeignResponseEntity entity = new FeignResponseEntity<>();
        HttpHeaders headers = new HttpHeaders();
        entity.setHeaders(headers);
        return entity;
    }
}

package com.ecquaria.cloud.moh.iais.service.client;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import org.springframework.http.HttpHeaders;

/**
 * LicenceFEEicGatewayClientFallback
 *
 * @author suocheng
 * @date 12/14/2019
 */

public class ApplicationFEEicGatewayClientFallback {
    public FeignResponseEntity<ApplicationDto> updateApplication(ApplicationDto applicationDto,
                                                                  String date,
                                                                  String authorization){
        FeignResponseEntity entity = new FeignResponseEntity<>();
        HttpHeaders headers = new HttpHeaders();
        entity.setHeaders(headers);
        return entity;
    }
}

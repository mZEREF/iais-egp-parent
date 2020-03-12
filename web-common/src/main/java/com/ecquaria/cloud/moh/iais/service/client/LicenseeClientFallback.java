package com.ecquaria.cloud.moh.iais.service.client;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenseeDto;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import org.springframework.http.HttpHeaders;

/**
 * LicenseeClientFallback
 *
 * @author suocheng
 * @date 3/12/2020
 */

public class LicenseeClientFallback {
    public FeignResponseEntity<LicenseeDto> getLicenseeDtoById (String id){
        FeignResponseEntity entity = new FeignResponseEntity<>();
        HttpHeaders headers = new HttpHeaders();
        entity.setHeaders(headers);
        return entity;
    }
}

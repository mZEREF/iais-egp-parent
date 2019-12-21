package com.ecquaria.cloud.moh.iais.service.client;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenseeDto;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import org.springframework.http.HttpHeaders;

/**
 * OrgLicenseeClientFallback
 *
 * @author junyu
 * @date 2019/12/21
 */
public class OrgLicenseeClientFallback implements OrgLicenseeClient{

    @Override
    public FeignResponseEntity<LicenseeDto> getLicenseeDtoById(String id) {
        FeignResponseEntity entity = new FeignResponseEntity<>();
        HttpHeaders headers = new HttpHeaders();
        entity.setHeaders(headers);
        return entity;
    }
}

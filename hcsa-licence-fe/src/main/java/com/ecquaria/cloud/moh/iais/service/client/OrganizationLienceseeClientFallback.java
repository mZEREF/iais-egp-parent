package com.ecquaria.cloud.moh.iais.service.client;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenseeKeyApptPersonDto;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import org.springframework.http.HttpHeaders;

import java.util.List;

/**
 * OrganizationLienceseeClientFallback
 *
 * @author caijing
 * @date 2020/1/15
 */
public class OrganizationLienceseeClientFallback implements OrganizationLienceseeClient{
    @Override
    public FeignResponseEntity<List<LicenseeKeyApptPersonDto>> getLicenseeKeyApptPersonDtoListByUen(String uenNo) {
        FeignResponseEntity entity = new FeignResponseEntity<>();
        HttpHeaders headers = new HttpHeaders();
        entity.setHeaders(headers);
        return entity;
    }

    @Override
    public FeignResponseEntity<List<LicenseeKeyApptPersonDto>> getLicenseeKeyApptPersonDtoListByLicenseeId(String licenseeId) {
        FeignResponseEntity entity = new FeignResponseEntity<>();
        HttpHeaders headers = new HttpHeaders();
        entity.setHeaders(headers);
        return entity;
    }
}

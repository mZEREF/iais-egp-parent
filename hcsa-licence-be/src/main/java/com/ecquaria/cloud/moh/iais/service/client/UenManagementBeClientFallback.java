package com.ecquaria.cloud.moh.iais.service.client;

import com.ecquaria.cloud.moh.iais.common.dto.organization.MohUenDto;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import org.springframework.http.HttpHeaders;

/**
 * UenManagementClientFallback
 *
 * @author junyu
 * @date 2020/1/22
 */
public class UenManagementBeClientFallback implements UenManagementBeClient{
    @Override
    public FeignResponseEntity<MohUenDto> getMohUenById(String uenNo){
        FeignResponseEntity entity = new FeignResponseEntity<>();
        HttpHeaders headers = new HttpHeaders();
        entity.setHeaders(headers);
        return entity;
    }

    @Override
    public FeignResponseEntity<MohUenDto>  generatesMohIssuedUen(MohUenDto mohUenDto){
        FeignResponseEntity entity = new FeignResponseEntity<>();
        HttpHeaders headers = new HttpHeaders();
        entity.setHeaders(headers);
        return entity;
    }

}

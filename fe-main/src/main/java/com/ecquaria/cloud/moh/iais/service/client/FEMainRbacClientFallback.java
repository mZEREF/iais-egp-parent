package com.ecquaria.cloud.moh.iais.service.client;

import com.ecquaria.cloud.moh.iais.common.dto.organization.EgpUserRoleDto;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import org.springframework.http.HttpHeaders;

/**
 * @author yichen
 * @Date:2020/8/20
 */

public class FEMainRbacClientFallback implements FEMainRbacClient{
    @Override
    public FeignResponseEntity<String> createUerRoleIds(EgpUserRoleDto var1) {
        FeignResponseEntity entity = new FeignResponseEntity<>();
        HttpHeaders headers = new HttpHeaders();
        entity.setHeaders(headers);
        return entity;
    }
}

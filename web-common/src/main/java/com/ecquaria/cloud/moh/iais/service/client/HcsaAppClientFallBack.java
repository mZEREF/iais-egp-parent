package com.ecquaria.cloud.moh.iais.service.client;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationGroupDto;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import org.springframework.http.HttpHeaders;

/**
 * HcsaAppClientFallBack
 *
 * @author Jinhua
 * @date 2020/7/13 11:11
 */
public class HcsaAppClientFallBack {
    public FeignResponseEntity<ApplicationGroupDto> getAppById(String appGroupId) {
        FeignResponseEntity entity = new FeignResponseEntity<>();
        HttpHeaders headers = new HttpHeaders();
        entity.setHeaders(headers);

        return entity;
    }
}

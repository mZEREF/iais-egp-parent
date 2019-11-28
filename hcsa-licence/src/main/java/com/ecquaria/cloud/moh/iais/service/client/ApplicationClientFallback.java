package com.ecquaria.cloud.moh.iais.service.client;

import com.ecquaria.cloud.moh.iais.common.dto.application.ApplicationViewDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * ApplicationClientFallback
 *
 * @author suocheng
 * @date 11/26/2019
 */
public class ApplicationClientFallback {
    FeignResponseEntity<ApplicationViewDto> getAppViewByNo(@PathVariable("appNo") String appNo){
        FeignResponseEntity entity = new FeignResponseEntity<>();
        HttpHeaders headers = new HttpHeaders();
        entity.setHeaders(headers);
        return entity;
    }
    FeignResponseEntity<ApplicationDto> getAppByNo(@PathVariable("appNo") String appNo){
        FeignResponseEntity entity = new FeignResponseEntity<>();
        HttpHeaders headers = new HttpHeaders();
        entity.setHeaders(headers);
        return entity;
    }
}

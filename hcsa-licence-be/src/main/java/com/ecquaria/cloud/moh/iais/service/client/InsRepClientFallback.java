package com.ecquaria.cloud.moh.iais.service.client;

import com.ecquaria.cloud.moh.iais.common.dto.application.ApplicationViewDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppInsRepDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesRecommendationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationGroupDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcDocConfigDto;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author weilu
 * @date 2019/11/29 11:01
 */
public class InsRepClientFallback implements InsRepClient{

    @Override
    public FeignResponseEntity<String> saveRecommendationData(AppPremisesRecommendationDto appPremisesRecommendationDto) {
        FeignResponseEntity entity = new FeignResponseEntity<>();
        HttpHeaders headers = new HttpHeaders();
        entity.setHeaders(headers);
        return entity;
    }

    @Override
    public FeignResponseEntity<AppInsRepDto> getAppInsRepDto(String appPremCorreId) {
        FeignResponseEntity entity = new FeignResponseEntity<>();
        HttpHeaders headers = new HttpHeaders();
        entity.setHeaders(headers);
        return entity;
    }

    @Override
    public FeignResponseEntity<String> getAppType(String appId) {
        FeignResponseEntity entity = new FeignResponseEntity<>();
        HttpHeaders headers = new HttpHeaders();
        entity.setHeaders(headers);
        return entity;
    }

    @Override
    public FeignResponseEntity<ApplicationGroupDto> getApplicationGroupDto(String appId) {
        FeignResponseEntity entity = new FeignResponseEntity<>();
        HttpHeaders headers = new HttpHeaders();
        entity.setHeaders(headers);
        return entity;
    }

    @Override
    public FeignResponseEntity<List<Boolean>> isRectified(List<String> itemId) {
        FeignResponseEntity entity = new FeignResponseEntity<>();
        HttpHeaders headers = new HttpHeaders();
        entity.setHeaders(headers);
        return entity;
    }

    @Override
    public FeignResponseEntity<AppPremisesRecommendationDto> getRecommendationDto(String appPremCorreId, String type) {
        FeignResponseEntity entity = new FeignResponseEntity<>();
        HttpHeaders headers = new HttpHeaders();
        entity.setHeaders(headers);
        return entity;
    }

    @Override
    public FeignResponseEntity<List<Date>> getInspectionRecomInDateByCorreId(List<String> correId) {
        FeignResponseEntity entity = new FeignResponseEntity<>();
        HttpHeaders headers = new HttpHeaders();
        entity.setHeaders(headers);
        return entity;
    }
}

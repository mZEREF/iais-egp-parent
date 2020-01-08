package com.ecquaria.cloud.moh.iais.service.client;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.AppInspectionStatusDto;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import org.springframework.http.HttpHeaders;

import java.util.List;

/**
 * @author Shicheng
 * @date 2019/12/10 16:52
 **/
public class AppInspectionStatusClientFallback {
    FeignResponseEntity<List<AppInspectionStatusDto>> getAppInspectionStatusByStatus(String status){
        FeignResponseEntity entity = new FeignResponseEntity<>();
        HttpHeaders headers = new HttpHeaders();
        entity.setHeaders(headers);
        return entity;
    }

    FeignResponseEntity<List<AppInspectionStatusDto>> getAppInspecStatusByIds(String ids){
        FeignResponseEntity entity = new FeignResponseEntity<>();
        HttpHeaders headers = new HttpHeaders();
        entity.setHeaders(headers);
        return entity;
    }

    FeignResponseEntity<AppInspectionStatusDto> getAppInspectionStatusByPremId(String appPremCorreId){
        FeignResponseEntity entity = new FeignResponseEntity<>();
        HttpHeaders headers = new HttpHeaders();
        entity.setHeaders(headers);
        return entity;
    }

    FeignResponseEntity<List<AppInspectionStatusDto>> create(List<AppInspectionStatusDto> appInspecStatusDtos){
        FeignResponseEntity entity = new FeignResponseEntity<>();
        HttpHeaders headers = new HttpHeaders();
        entity.setHeaders(headers);
        return entity;
    }

    FeignResponseEntity<List<AppInspectionStatusDto>> update(AppInspectionStatusDto appInspecStatusDto){
        FeignResponseEntity entity = new FeignResponseEntity<>();
        HttpHeaders headers = new HttpHeaders();
        entity.setHeaders(headers);
        return entity;
    }

    FeignResponseEntity<AppInspectionStatusDto> createAppInspectionStatusByAppDto(ApplicationDto applicationDto){
        FeignResponseEntity entity = new FeignResponseEntity<>();
        HttpHeaders headers = new HttpHeaders();
        entity.setHeaders(headers);
        return entity;
    }
}

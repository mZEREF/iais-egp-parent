package com.ecquaria.cloud.moh.iais.service.client;

import com.ecquaria.cloud.moh.iais.common.dto.application.ApplicationViewDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppInsRepDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesRecommendationDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspectionReportDto;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @author weilu
 * @date 2019/11/29 11:01
 */
public class InsRepClientFallback {


//    public FeignResponseEntity<ChecklistItemDto> getChklItemById(String id){
//        FeignResponseEntity entity = new FeignResponseEntity<>();
//        HttpHeaders headers = new HttpHeaders();
//        entity.setHeaders(headers);
//        return entity;
//    }
    
    public FeignResponseEntity<String> saveData(@RequestBody AppPremisesRecommendationDto appPremisesRecommendationDto){
        FeignResponseEntity entity = new FeignResponseEntity<>();
        HttpHeaders headers = new HttpHeaders();
        entity.setHeaders(headers);
        return entity;
    }

    FeignResponseEntity<ApplicationViewDto> getAppViewByNo(@PathVariable("appNo") String appNo){
        FeignResponseEntity entity = new FeignResponseEntity<>();
        HttpHeaders headers = new HttpHeaders();
        entity.setHeaders(headers);
        return entity;
    }

    FeignResponseEntity<AppInsRepDto> getAppInsRepDto(@PathVariable("appNo") String appNo){
        FeignResponseEntity entity = new FeignResponseEntity<>();
        HttpHeaders headers = new HttpHeaders();
        entity.setHeaders(headers);
        return entity;
    }
}

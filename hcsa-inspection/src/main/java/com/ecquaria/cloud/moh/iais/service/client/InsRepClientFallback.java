package com.ecquaria.cloud.moh.iais.service.client;

import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspectionReportDto;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import org.springframework.http.HttpHeaders;

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

    public FeignResponseEntity<InspectionReportDto> getInspectionReportDtoByAppNo(String appNo){
        FeignResponseEntity entity = new FeignResponseEntity<>();
        HttpHeaders headers = new HttpHeaders();
        entity.setHeaders(headers);
        return entity;
    }
}

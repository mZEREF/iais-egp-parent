package com.ecquaria.cloud.moh.iais.service.client;

import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspectionEmailTemplateDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspectionReportDto;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * InsEmailClientFallback
 *
 * @author junyu
 * @date 2019/12/2
 */
public class InsEmailClientFallback {

    public FeignResponseEntity<String> insertEmailTemplate(InspectionEmailTemplateDto inspectionEmailTemplateDto){
        FeignResponseEntity entity = new FeignResponseEntity<>();
        HttpHeaders headers = new HttpHeaders();
        entity.setHeaders(headers);
        return entity;
    }
    public FeignResponseEntity<InspectionEmailTemplateDto> recallEmailTemplate(String id){
        FeignResponseEntity entity = new FeignResponseEntity<>();
        HttpHeaders headers = new HttpHeaders();
        entity.setHeaders(headers);
        return entity;
    }
    public FeignResponseEntity<InspectionEmailTemplateDto> getInsertEmail( String appPremCorrId){
        FeignResponseEntity entity = new FeignResponseEntity<>();
        HttpHeaders headers = new HttpHeaders();
        entity.setHeaders(headers);
        return entity;
    }

}

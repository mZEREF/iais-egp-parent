package com.ecquaria.cloud.moh.iais.service.client;

import com.ecquaria.cloud.moh.iais.common.dto.emailsms.EmailDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspectionEmailTemplateDto;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import org.springframework.http.HttpHeaders;

import java.util.Map;

/**
 * InsEmailClientFallback
 *
 * @author junyu
 * @date 2019/12/2
 */
public class InsEmailClientFallBack implements InsEmailClient{

    @Override
    public FeignResponseEntity<String> insertEmailDraft(InspectionEmailTemplateDto inspectionEmailTemplateDto) {
        FeignResponseEntity entity = new FeignResponseEntity<>();
        HttpHeaders headers = new HttpHeaders();
        entity.setHeaders(headers);
        return entity;
    }

    @Override
    public FeignResponseEntity<String> updateEmailDraft(InspectionEmailTemplateDto inspectionEmailTemplateDto) {
         FeignResponseEntity entity = new FeignResponseEntity<>();
        HttpHeaders headers = new HttpHeaders();
        entity.setHeaders(headers);
        return entity;
    }

    @Override
    public void recallEmailTemplate(String id) {

    }

    @Override
    public FeignResponseEntity<InspectionEmailTemplateDto> getInspectionEmail(String appPremCorrId) {
         FeignResponseEntity entity = new FeignResponseEntity<>();
        HttpHeaders headers = new HttpHeaders();
        entity.setHeaders(headers);
        return entity;
    }

    @Override
    public FeignResponseEntity<Map<String, String>> sendAndSaveEmail(EmailDto email) {
         FeignResponseEntity entity = new FeignResponseEntity<>();
        HttpHeaders headers = new HttpHeaders();
        entity.setHeaders(headers);
        return entity;
    }
}

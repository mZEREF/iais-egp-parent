package com.ecquaria.cloud.moh.iais.service.client;

import com.ecquaria.cloud.moh.iais.common.dto.emailsms.EmailDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspectionEmailTemplateDto;
import com.ecquaria.cloudfeign.FeignConfiguration;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

/**
 * InsEmailClient
 *
 * @author junyu
 * @date 2019/12/2
 */
@FeignClient(name = "hcsa-application", configuration = {FeignConfiguration.class},
        fallback = InsEmailClientFallBack.class)
public interface InsEmailClient {

    @PutMapping(value = "/iais-inspection/insert-email-draft", consumes =  MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<String> insertEmailDraft(@RequestBody InspectionEmailTemplateDto inspectionEmailTemplateDto);

    @PostMapping(value = "/iais-inspection/update-email-draft", consumes =  MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<String> updateEmailDraft(@RequestBody InspectionEmailTemplateDto inspectionEmailTemplateDto);


    @DeleteMapping(path = "/iais-inspection/recall-template",  consumes =  MediaType.APPLICATION_JSON_VALUE)
    void recallEmailTemplate(@RequestBody String id);

    @GetMapping(path = "/iais-inspection/inspection-email/{appPremCorrId}",  consumes =  MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<InspectionEmailTemplateDto> getInspectionEmail(@PathVariable(value = "appPremCorrId") String appPremCorrId);

    @PostMapping(path = "/emails",  consumes =  MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<Map<String, String >> sendAndSaveEmail(@RequestBody EmailDto email);

}

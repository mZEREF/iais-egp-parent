package com.ecquaria.cloud.moh.iais.service.client;

import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspectionEmailTemplateDto;
import com.ecquaria.cloudfeign.FeignConfiguration;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * InsEmailClient
 *
 * @author junyu
 * @date 2019/12/2
 */
@FeignClient(name = "iais-application", configuration = {FeignConfiguration.class},
        fallback = InsEmailClientFallback.class)
public interface InsEmailClient {

    @PostMapping(path = "/iais-inspection/insert-template",  consumes =  MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<String> insertEmailTemplate(@RequestBody InspectionEmailTemplateDto inspectionEmailTemplateDto);

    @DeleteMapping(path = "/iais-inspection/recall-template")
     void recallEmailTemplate(@RequestBody String id);

}

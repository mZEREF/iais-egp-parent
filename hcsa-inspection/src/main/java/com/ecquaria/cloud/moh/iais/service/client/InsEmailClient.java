package com.ecquaria.cloud.moh.iais.service.client;

import com.ecquaria.cloud.moh.iais.common.dto.application.ApplicationViewDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspectionEmailTemplateDto;
import com.ecquaria.cloudfeign.FeignConfiguration;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

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

    @DeleteMapping(path = "/iais-inspection/recall-template",  consumes =  MediaType.APPLICATION_JSON_VALUE)
     void recallEmailTemplate(@RequestBody String id);

    @GetMapping(path = "/iais-inspection/recall-template/{appPremCorrId}",  consumes =  MediaType.APPLICATION_JSON_VALUE)
    InspectionEmailTemplateDto getInsertEmail(@PathVariable(value = "appPremCorrId") String appPremCorrId);
    @RequestMapping(path = "/iais-application-be/applicationview/{appNo}",method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<ApplicationViewDto> getAppViewByNo(@PathVariable("appNo") String appNo);
}

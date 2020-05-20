package com.ecquaria.cloud.moh.iais.service.client;

import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspectionEmailTemplateDto;
import com.ecquaria.cloudfeign.FeignConfiguration;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @author Wenkang
 * @date 2019/11/28 10:28
 */
@FeignClient(name = "system-admin", configuration = FeignConfiguration.class,
        fallback = SystemClientBeLicFallback.class)
public interface SystemBeLicClient {

    @PostMapping(value = "/iais-messageTemplate" ,consumes =  MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<InspectionEmailTemplateDto> loadingEmailTemplate(@RequestBody String id);

    @RequestMapping(path = "/message-id",method = RequestMethod.GET)
    FeignResponseEntity<String> messageID();

}

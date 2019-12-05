package com.ecquaria.cloud.moh.iais.service.client;

import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspectionEmailTemplateDto;
import com.ecquaria.cloudfeign.FeignConfiguration;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * SysAdminMessageTemplateClient
 *
 * @author junyu
 * @date 2019/12/4
 */
@FeignClient(name = "system-admin", configuration = {FeignConfiguration.class},
        fallback = SysAdminMessageTemplateClientFallBack.class)
public interface SysAdminMessageTemplateClient {
    @PostMapping(value = "/iais-messageTemplate" ,consumes =  MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<InspectionEmailTemplateDto> loadingEmailTemplate(@RequestBody String id);
}

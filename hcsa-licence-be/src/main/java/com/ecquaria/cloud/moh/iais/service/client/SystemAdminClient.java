package com.ecquaria.cloud.moh.iais.service.client;

import com.ecquaria.cloud.moh.iais.common.dto.postcode.PostCodeDto;
import com.ecquaria.cloud.moh.iais.common.dto.templates.MsgTemplateDto;
import com.ecquaria.cloudfeign.FeignConfiguration;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

/**
 * @author Wenkang
 * @date 2019/11/27 17:11
 */
@FeignClient(name = "system-admin", configuration = FeignConfiguration.class,
        fallback = SystemAdminClientFallback.class)
public interface SystemAdminClient  {
    @RequestMapping(path = "/draft-number/{type}",method = RequestMethod.GET)
    FeignResponseEntity<String> draftNumber(@PathVariable(name = "type") String applicationType);

    @RequestMapping(path = "/application-number/{type}")
    FeignResponseEntity<String> applicationNumber(@PathVariable(name = "type") String applicationType);

    @RequestMapping(path="//api-postcodes/postal-code", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<PostCodeDto> getPostCodeByCode(@RequestParam(value = "postalCode") String postalCode);

    @GetMapping(path = "/iais-messageTemplate/template/{msgId}")
    FeignResponseEntity<MsgTemplateDto> getMsgTemplate(@PathVariable("msgId") String id);
}

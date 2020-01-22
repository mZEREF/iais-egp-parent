package com.ecquaria.cloud.moh.iais.service.client;

import com.ecquaria.cloud.moh.iais.common.dto.emailsms.EmailDto;
import com.ecquaria.cloudfeign.FeignConfiguration;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @author Wenkang
 * @date 2020/1/19 15:54
 */
@FeignClient(name = "email-sms",configuration = FeignConfiguration.class,fallback = EmailFClientFallback.class)
public interface EmailClient {
    @PostMapping(value = "/emails/mailNoAttach", consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity <String> sendNotification(@RequestBody EmailDto email);
}

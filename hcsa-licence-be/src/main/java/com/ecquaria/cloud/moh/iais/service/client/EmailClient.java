package com.ecquaria.cloud.moh.iais.service.client;

import com.ecquaria.cloud.moh.iais.common.dto.emailsms.EmailDto;
import com.ecquaria.cloud.moh.iais.common.dto.emailsms.SmsDto;
import com.ecquaria.cloudfeign.FeignConfiguration;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import net.sf.oval.constraint.exclusion.Nullable;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

/**
 * @author Wenkang
 * @date 2020/1/19 15:54
 */

@FeignClient(name = "email-sms",configuration = FeignConfiguration.class,fallback = EmailFClientFallback.class)
public interface EmailClient {

    @PostMapping(value = "/emails/mailNoAttach", consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<String> sendNotification(@RequestBody EmailDto email);

    @PostMapping(value = "/api/sms/notification", consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<Map<String, String>> sendSMS(@RequestParam(value = "recipts") List<String> recipts,
                                                     @Valid @RequestBody SmsDto sms,
                                                     @RequestParam(value = "reqRefNum") @Nullable String reqRefNum);
}

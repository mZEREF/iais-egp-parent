package com.ecquaria.cloud.moh.iais.service.client;

import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.emailsms.EmailAttachMentDto;
import com.ecquaria.cloud.moh.iais.common.dto.emailsms.SmsDto;
import com.ecquaria.cloud.moh.iais.common.dto.system.EmailAuditTrailDto;
import com.ecquaria.cloud.moh.iais.common.dto.system.ResendListDto;
import com.ecquaria.cloudfeign.FeignConfiguration;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import net.sf.oval.constraint.exclusion.Nullable;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

/**
 * @author guyin
 * @date 2019/12/28 10:45
 */
@FeignClient(name = "email-sms", configuration = FeignConfiguration.class, fallback = EmailHistoryClientFallback.class)
public interface EmailHistoryClient {
    @PostMapping(value = "/iais-emails-history/getAuditList",consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<SearchResult<EmailAuditTrailDto>> getAuditList(@RequestBody SearchParam searchParam);

    @PostMapping(value = "/iais-emails-history/getResendList",consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<SearchResult<ResendListDto>> getResendList(@RequestBody SearchParam searchParam);

    @PostMapping(value = "/api/sms/notification", consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<Map<String, String>> sendSMS(@RequestParam(value = "recipts") List<String> recipts,
                                                     @Valid @RequestBody SmsDto sms,
                                                     @RequestParam(value = "reqRefNum") @Nullable String reqRefNum);

    @PostMapping(value = "/iais-emails-history/getEmailById",produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<EmailAttachMentDto> getEmailById(@RequestParam(value = "id") String id);

    @PostMapping(value = "/iais-emails-history/setEmailResend",produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<Void> setEmailResend(@RequestParam(value = "id") String id);

}


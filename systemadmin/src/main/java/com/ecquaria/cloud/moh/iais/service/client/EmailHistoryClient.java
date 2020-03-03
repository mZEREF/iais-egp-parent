package com.ecquaria.cloud.moh.iais.service.client;

import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.system.EmailAuditTrailDto;
import com.ecquaria.cloud.moh.iais.common.dto.system.ResendListDto;
import com.ecquaria.cloudfeign.FeignConfiguration;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @author guyin
 * @date 2019/12/28 10:45
 */
@FeignClient(name = "email-sms", configuration = FeignConfiguration.class, fallback = IntranetUserClientFallback.class)
public interface EmailHistoryClient {
    @PostMapping(value = "/iais-emails-history/getAuditList",consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<SearchResult<EmailAuditTrailDto>> getAuditList(@RequestBody SearchParam searchParam);

    @PostMapping(value = "/iais-emails-history/getResendList",consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<SearchResult<ResendListDto>> getResendList(@RequestBody SearchParam searchParam);

}

package com.ecquaria.cloud.moh.iais.service.client;

import com.ecquaria.cloud.moh.iais.common.dto.AuditTrailDto;
import com.ecquaria.cloudfeign.FeignConfiguration;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * BbAuditTrailClient
 *
 * @author Jinhua
 * @date 2020/9/29 12:36
 */
@FeignClient(name = "audit-trail", configuration = FeignConfiguration.class,
        fallback = BbAuditTrailClientFallback.class)
public interface BbAuditTrailClient {
    @GetMapping(path = "/iais-audit-trail/login-inf/{sessionId}", produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<AuditTrailDto> getLoginInfoBySessionId(@PathVariable("sessionId") String sessionId);
    @PutMapping(path = "/iais-audit-trail/session-duration")
    FeignResponseEntity<Void> updateSessionDuration(@RequestParam("sessionId") String sessionId, @RequestParam("perioid") int period);
}

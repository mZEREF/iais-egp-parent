package com.ecquaria.cloud.moh.iais.service.client;

import com.ecquaria.cloud.moh.iais.common.dto.AuditTrailDto;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import org.springframework.http.HttpHeaders;

/**
 * BbAuditTrailClientFallback
 *
 * @author Jinhua
 * @date 2020/9/29 12:47
 */
public class BbAuditTrailClientFallback {
    public FeignResponseEntity<AuditTrailDto> getLoginInfoBySessionId(String sessionId) {
        FeignResponseEntity entity = new FeignResponseEntity<>();
        HttpHeaders headers = new HttpHeaders();
        entity.setHeaders(headers);
        return entity;
    }

    public FeignResponseEntity<Void> updateSessionDuration(String sessionId, int period) {
        FeignResponseEntity entity = new FeignResponseEntity<>();
        HttpHeaders headers = new HttpHeaders();
        entity.setHeaders(headers);
        return entity;
    }
}

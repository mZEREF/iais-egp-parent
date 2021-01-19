package com.ecquaria.cloud.moh.iais.service.client;

import com.ecquaria.cloud.moh.iais.common.dto.AuditTrailDto;
import com.ecquaria.cloudfeign.FeignConfiguration;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * @author yichen
 * @Date:2020/10/19
 */

@FeignClient(name = "audit-trail", configuration = FeignConfiguration.class,
        fallback = AuditTrailWbClientFallBack.class)
public interface AuditTrailWbClient {

    @PostMapping(path = "/iais-audit-trail", consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<Void> insertAuditTrail(@RequestBody List<AuditTrailDto> audits);
}

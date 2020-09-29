package com.ecquaria.cloud.moh.iais.service.client;

import com.ecquaria.cloud.moh.iais.common.dto.AuditTrailDto;
import com.ecquaria.cloud.moh.iais.common.dto.audit.AuditTrailEntityDto;
import com.ecquaria.cloudfeign.FeignConfiguration;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Map;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author: yichen
 * @date time:2/5/2020 3:57 PM
 * @description:
 */

@FeignClient(name = "audit-trail", configuration = FeignConfiguration.class,
		fallback = AuditTrailMainBeClientFallBack.class)
public interface AuditTrailMainBeClient {

	@PostMapping(value = "/iais-audit-trail/auditTrail-migrated-syuc-Update", consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
	FeignResponseEntity<Map<String, String>> syucUpdateAuditTrail(@RequestBody List<AuditTrailEntityDto> audits);

	@GetMapping(path = "/iais-audit-trail/login-inf/{sessionId}", produces = MediaType.APPLICATION_JSON_VALUE)
	FeignResponseEntity<AuditTrailDto> getLoginInfoBySessionId(@PathVariable("sessionId") String sessionId);

	@PutMapping(path = "/iais-audit-trail/session-duration")
	FeignResponseEntity<Void> updateSessionDuration(@RequestParam("sessionId") String sessionId, @RequestParam("perioid") int period);
}

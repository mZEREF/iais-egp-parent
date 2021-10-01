package com.ecquaria.cloud.moh.iais.service.client;

import com.ecquaria.cloud.moh.iais.common.dto.AuditTrailDto;
import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.audit.AuditTrailEntityDto;
import com.ecquaria.cloud.moh.iais.common.dto.audit.AuditTrailQueryDto;
import com.ecquaria.cloudfeign.FeignConfiguration;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import java.util.List;
import java.util.Map;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author: yichen
 * @date time:2/5/2020 3:57 PM
 * @description:
 */

@FeignClient(name = "audit-trail", configuration = FeignConfiguration.class,
		fallback = AuditTrailMainClientFallBack.class)
public interface AuditTrailMainClient {

	@PostMapping(path = "/iais-audit-trail/results", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	FeignResponseEntity<SearchResult<AuditTrailQueryDto>> listAuditTrailDto(@RequestBody SearchParam searchParam);

	@PostMapping(value = "/iais-audit-trail/auditTrail-migrated-change", consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
	FeignResponseEntity<List<AuditTrailEntityDto>> getAuditTrailsByMigrated1();

	@PostMapping(value = "/iais-audit-trail/auditTrail-migrated-syuc-Update", consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
	FeignResponseEntity<Map<String, String>> syucUpdateAuditTrail(@RequestBody List<AuditTrailEntityDto> audits);

	@GetMapping(path = "/iais-audit-trail/last-login/{loginUserId}/{sessionId}", produces = MediaType.APPLICATION_JSON_VALUE)
	FeignResponseEntity<AuditTrailDto> getLastLoginInfo(@PathVariable("loginUserId") String loginUserId, @PathVariable("sessionId") String sessionId);

	@GetMapping(path = "/iais-audit-trail/last-action/{sessionId}", produces = MediaType.APPLICATION_JSON_VALUE)
	FeignResponseEntity<AuditTrailDto> getLastAction(@PathVariable("sessionId") String sessionId);

	@GetMapping(path = "/iais-audit-trail/login-inf/{sessionId}", produces = MediaType.APPLICATION_JSON_VALUE)
	FeignResponseEntity<AuditTrailDto> getLoginInfoBySessionId(@PathVariable("sessionId") String sessionId);

	@PutMapping(path = "/iais-audit-trail/session-duration")
	FeignResponseEntity<Void> updateSessionDuration(@RequestParam("sessionId") String sessionId, @RequestParam("perioid") int period);

}

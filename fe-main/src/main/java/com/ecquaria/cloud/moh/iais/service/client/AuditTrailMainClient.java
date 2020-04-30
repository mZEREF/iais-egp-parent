package com.ecquaria.cloud.moh.iais.service.client;

import com.ecquaria.cloud.moh.iais.common.dto.AuditTrailDto;
import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.audit.AuditTrailEntityDto;
import com.ecquaria.cloud.moh.iais.common.dto.audit.AuditTrailQueryDto;
import com.ecquaria.cloudfeign.FeignConfiguration;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

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

	@GetMapping(path = "/iais-audit-trail/last-login/{loginUserId}", produces = MediaType.APPLICATION_JSON_VALUE)
	FeignResponseEntity<AuditTrailDto> getLastLoginInfo(@PathVariable("loginUserId") String loginUserId);

	@GetMapping(path = "/iais-audit-trail/last-action/{sessionId}", produces = MediaType.APPLICATION_JSON_VALUE)
	FeignResponseEntity<AuditTrailDto> getLastAction(@PathVariable("sessionId") String sessionId);
}

package com.ecquaria.cloud.moh.iais.service.client;

import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.audit.AuditTrailQueryDto;
import com.ecquaria.cloudfeign.FeignConfiguration;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @author: yichen
 * @date time:2/5/2020 3:57 PM
 * @description:
 */

@FeignClient(name = "audit-trail", configuration = FeignConfiguration.class,
		fallback = AuditTrailClientFallBack.class)
public interface AuditTrailClient {

	@PostMapping(path = "/iais-audit-trail/results", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	FeignResponseEntity<SearchResult<AuditTrailQueryDto>> listAuditTrailDto(@RequestBody SearchParam searchParam);
}

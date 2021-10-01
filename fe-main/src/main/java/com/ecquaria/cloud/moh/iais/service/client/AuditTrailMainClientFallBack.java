package com.ecquaria.cloud.moh.iais.service.client;

import com.ecquaria.cloud.moh.iais.common.dto.AuditTrailDto;
import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.audit.AuditTrailEntityDto;
import com.ecquaria.cloud.moh.iais.common.dto.audit.AuditTrailQueryDto;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import java.util.List;
import java.util.Map;
import org.springframework.http.HttpHeaders;

/**
 * @author: yichen
 * @date time:2/5/2020 3:58 PM
 * @description:
 */


public class AuditTrailMainClientFallBack implements AuditTrailMainClient {
	@Override
	public FeignResponseEntity<SearchResult<AuditTrailQueryDto>> listAuditTrailDto(SearchParam searchParam) {
		return null;
	}

	@Override
	public FeignResponseEntity<List<AuditTrailEntityDto>> getAuditTrailsByMigrated1() {
		FeignResponseEntity entity = new FeignResponseEntity<>();
		HttpHeaders headers = new HttpHeaders();
		entity.setHeaders(headers);
		return entity;
	}

	@Override
	public FeignResponseEntity<Map<String, String>> syucUpdateAuditTrail(List<AuditTrailEntityDto> audits) {
		FeignResponseEntity entity = new FeignResponseEntity<>();
		HttpHeaders headers = new HttpHeaders();
		entity.setHeaders(headers);
		return entity;
	}

	@Override
	public FeignResponseEntity<AuditTrailDto> getLastLoginInfo(String loginUserId, String sessionId) {
		FeignResponseEntity entity = new FeignResponseEntity<>();
		HttpHeaders headers = new HttpHeaders();
		entity.setHeaders(headers);
		return entity;
	}

	@Override
	public FeignResponseEntity<AuditTrailDto> getLastAction(String sessionId) {
		FeignResponseEntity entity = new FeignResponseEntity<>();
		HttpHeaders headers = new HttpHeaders();
		entity.setHeaders(headers);
		return entity;
	}

	@Override
	public FeignResponseEntity<AuditTrailDto> getLoginInfoBySessionId(String sessionId) {
		FeignResponseEntity entity = new FeignResponseEntity<>();
		HttpHeaders headers = new HttpHeaders();
		entity.setHeaders(headers);
		return entity;
	}

	@Override
	public FeignResponseEntity<Void> updateSessionDuration(String sessionId, int period) {
		FeignResponseEntity entity = new FeignResponseEntity<>();
		HttpHeaders headers = new HttpHeaders();
		entity.setHeaders(headers);
		return entity;
	}
}

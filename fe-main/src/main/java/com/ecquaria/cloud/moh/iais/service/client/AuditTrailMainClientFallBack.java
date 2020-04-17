package com.ecquaria.cloud.moh.iais.service.client;

import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.audit.AuditTrailEntityDto;
import com.ecquaria.cloud.moh.iais.common.dto.audit.AuditTrailQueryDto;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import org.springframework.http.HttpHeaders;

import java.util.List;

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
}

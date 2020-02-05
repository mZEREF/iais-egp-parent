package com.ecquaria.cloud.moh.iais.service.client;

import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.audit.AuditTrailQueryDto;
import com.ecquaria.cloudfeign.FeignResponseEntity;

/**
 * @author: yichen
 * @date time:2/5/2020 3:58 PM
 * @description:
 */


public class AuditTrailClientFallBack implements AuditTrailClient {
	@Override
	public FeignResponseEntity<SearchResult<AuditTrailQueryDto>> listAuditTrailDto(SearchParam searchParam) {
		return null;
	}
}

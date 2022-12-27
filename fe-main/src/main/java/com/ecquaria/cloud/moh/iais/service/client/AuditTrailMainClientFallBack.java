package com.ecquaria.cloud.moh.iais.service.client;

import com.ecquaria.cloud.moh.iais.common.dto.AuditTrailDto;
import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.audit.AuditTrailEntityDto;
import com.ecquaria.cloud.moh.iais.common.dto.audit.AuditTrailQueryDto;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import java.util.List;
import java.util.Map;


/**
 * @author: yichen
 * @date time:2/5/2020 3:58 PM
 * @description:
 */


public class AuditTrailMainClientFallBack implements AuditTrailMainClient {
	@Override
	public FeignResponseEntity<SearchResult<AuditTrailQueryDto>> listAuditTrailDto(SearchParam searchParam) {
		return IaisEGPHelper.getFeignResponseEntity("listAuditTrailDto", searchParam);
	}

	@Override
	public FeignResponseEntity<List<AuditTrailEntityDto>> getAuditTrailsByMigrated1() {
		return IaisEGPHelper.getFeignResponseEntity("getAuditTrailsByMigrated1");
	}

	@Override
	public FeignResponseEntity<Map<String, String>> syucUpdateAuditTrail(List<AuditTrailEntityDto> audits) {
		return IaisEGPHelper.getFeignResponseEntity("syucUpdateAuditTrail",audits);
	}

	@Override
	public FeignResponseEntity<AuditTrailDto> getLastLoginInfo(String loginUserId, String sessionId) {
		return IaisEGPHelper.getFeignResponseEntity("getLastLoginInfo",loginUserId);
	}

	@Override
	public FeignResponseEntity<AuditTrailDto> getLastAction(String sessionId) {
		return IaisEGPHelper.getFeignResponseEntity("getLastAction",sessionId);
	}

	@Override
	public FeignResponseEntity<AuditTrailDto> getLoginInfoBySessionId(String sessionId) {
		return IaisEGPHelper.getFeignResponseEntity("getLoginInfoBySessionId",sessionId);
	}

	@Override
	public FeignResponseEntity<Void> updateSessionDuration(String sessionId, int period) {
		return IaisEGPHelper.getFeignResponseEntity("updateSessionDuration",sessionId);
	}
}

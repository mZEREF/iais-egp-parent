package com.ecquaria.cloud.moh.iais.service.client;

import com.ecquaria.cloud.moh.iais.common.dto.AuditTrailDto;
import com.ecquaria.cloud.moh.iais.common.dto.audit.AuditTrailEntityDto;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloudfeign.FeignResponseEntity;

import java.util.List;
import java.util.Map;

/**
 * @author: yichen
 * @date time:2/5/2020 3:58 PM
 * @description:
 */


public class AuditTrailMainBeClientFallBack implements AuditTrailMainBeClient {

	@Override
	public FeignResponseEntity<Map<String, String>> syucUpdateAuditTrail(List<AuditTrailEntityDto> audits) {
		return IaisEGPHelper.getFeignResponseEntity("syucUpdateAuditTrail",audits);
	}

	@Override
	public FeignResponseEntity<AuditTrailDto> getLoginInfoBySessionId(String sessionId) {
		return IaisEGPHelper.getFeignResponseEntity("getLoginInfoBySessionId",sessionId);
	}

	@Override
	public FeignResponseEntity<Void> updateSessionDuration(String sessionId, int period) {
		return IaisEGPHelper.getFeignResponseEntity("updateSessionDuration",sessionId,period);
	}
}

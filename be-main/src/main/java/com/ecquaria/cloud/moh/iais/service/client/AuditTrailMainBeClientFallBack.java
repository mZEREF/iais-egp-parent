package com.ecquaria.cloud.moh.iais.service.client;

import com.ecquaria.cloud.moh.iais.common.dto.audit.AuditTrailEntityDto;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import org.springframework.http.HttpHeaders;

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
		FeignResponseEntity entity = new FeignResponseEntity<>();
		HttpHeaders headers = new HttpHeaders();
		entity.setHeaders(headers);
		return entity;	}
}

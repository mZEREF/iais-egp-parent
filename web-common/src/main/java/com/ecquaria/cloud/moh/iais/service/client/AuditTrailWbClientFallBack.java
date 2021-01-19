package com.ecquaria.cloud.moh.iais.service.client;

import com.ecquaria.cloud.moh.iais.common.dto.AuditTrailDto;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import org.springframework.http.HttpHeaders;

import java.util.List;

/**
 * @author yichen
 * @Date:2020/10/19
 */

public class AuditTrailWbClientFallBack implements AuditTrailWbClient{
    @Override
    public FeignResponseEntity<Void> insertAuditTrail(List<AuditTrailDto> audits) {
        FeignResponseEntity entity = new FeignResponseEntity<>();
        HttpHeaders headers = new HttpHeaders();
        entity.setHeaders(headers);
        return entity;
    }
}

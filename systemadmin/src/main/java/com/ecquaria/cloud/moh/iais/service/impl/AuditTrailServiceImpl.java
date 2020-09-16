package com.ecquaria.cloud.moh.iais.service.impl;

/*
 *author: yichen
 *date time:9/16/2019 3:46 PM
 *description:
 */

import com.ecquaria.cloud.moh.iais.annotation.SearchTrack;
import com.ecquaria.cloud.moh.iais.common.dto.AuditTrailDto;
import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.audit.AuditTrailQueryDto;
import com.ecquaria.cloud.moh.iais.service.AuditTrailService;
import com.ecquaria.cloud.moh.iais.service.client.AuditTrailClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuditTrailServiceImpl implements AuditTrailService {
    @Autowired
    private AuditTrailClient trailClient;


    @SearchTrack(catalog = "systemAdmin", key = "queryFullModeAuditTrail")
    @Override
    public SearchResult<AuditTrailQueryDto> listAuditTrailDto(SearchParam searchParam) {
        return trailClient.listAuditTrailDto(searchParam).getEntity();
    }

    @Override
    public AuditTrailDto getAuditTrailById(String auditId) {
        return trailClient.getAuditTrailById(auditId).getEntity();
    }
}

package com.ecquaria.cloud.moh.iais.service.impl;

/*
 *author: yichen
 *date time:9/16/2019 3:46 PM
 *description:
 */

import com.ecquaria.cloud.moh.iais.annotation.SearchTrack;
import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.audit.AuditTrailQueryDto;
import com.ecquaria.cloud.moh.iais.common.utils.RestApiUtil;
import com.ecquaria.cloud.moh.iais.service.AuditTrailService;
import org.springframework.stereotype.Service;

@Service
public class AuditTrailServiceImpl implements AuditTrailService {

    @SearchTrack(catalog = "AuditTrail", key = "search")
    public SearchResult<AuditTrailQueryDto> listAuditTrailDto(SearchParam searchParam) {
        return RestApiUtil.query("audit-trail-service:8887/iais-audit-trail/results", searchParam);
    }
}

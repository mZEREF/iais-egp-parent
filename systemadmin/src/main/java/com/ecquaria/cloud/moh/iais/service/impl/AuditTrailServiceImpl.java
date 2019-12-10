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
import com.ecquaria.cloud.moh.iais.service.AuditTrailService;
import com.ecquaria.cloud.moh.iais.service.client.SystemClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuditTrailServiceImpl implements AuditTrailService {
    @Autowired
    private SystemClient systemClient;


    @SearchTrack(catalog = "AuditTrail", key = "search")
    @Override
    public SearchResult<AuditTrailQueryDto> listAuditTrailDto(SearchParam searchParam) {
        return systemClient.listAuditTrailDto(searchParam).getEntity();
    }
}

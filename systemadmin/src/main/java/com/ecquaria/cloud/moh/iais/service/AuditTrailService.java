package com.ecquaria.cloud.moh.iais.service;

/*
 *author: yichen
 *date time:9/16/2019 3:46 PM
 *description:
 */

import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.dto.AuditTrailQueryDto;

public interface AuditTrailService {

    SearchResult<AuditTrailQueryDto> listAuditTrailDto(SearchParam searchParam);
}

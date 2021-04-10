package com.ecquaria.cloud.moh.iais.service;

/*
 *author: yichen
 *date time:9/16/2019 3:46 PM
 *description:
 */

import com.ecquaria.cloud.moh.iais.common.dto.AuditTrailDto;
import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.audit.AuditTrailExcelDto;
import com.ecquaria.cloud.moh.iais.common.dto.audit.AuditTrailQueryDto;
import com.ecquaria.cloud.moh.iais.dto.AuditLogRecView;

import java.util.ArrayList;
import java.util.Map;

public interface AuditTrailService {

    SearchResult<AuditTrailQueryDto> listAuditTrailDto(SearchParam searchParam);

    AuditTrailDto getAuditTrailById(String auditId);

    void addAuditLogRevToList(ArrayList<AuditLogRecView> list, Map<String, Object> map);

    ArrayList<AuditLogRecView> genAuditLogRecList(String detail);

    void doSetAuditTrailExcel(AuditTrailExcelDto trailExcel, AuditTrailQueryDto trailQuery);
}

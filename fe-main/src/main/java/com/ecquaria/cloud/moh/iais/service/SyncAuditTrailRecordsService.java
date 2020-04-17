package com.ecquaria.cloud.moh.iais.service;

import com.ecquaria.cloud.moh.iais.common.dto.audit.AuditTrailEntityDto;

import java.util.List;

/**
 * SyncAuditTrailRecordsService
 *
 * @author junyu
 * @date 2020/4/16
 */
public interface SyncAuditTrailRecordsService {
    List<AuditTrailEntityDto> getAuditTrailsByMigrated1();

    String getData(List<AuditTrailEntityDto> auditTrailDtos);

    void saveFile(String data) ;

    void compressFile();
}

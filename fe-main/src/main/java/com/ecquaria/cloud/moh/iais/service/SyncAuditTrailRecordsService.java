package com.ecquaria.cloud.moh.iais.service;

import com.ecquaria.cloud.moh.iais.common.dto.EicRequestTrackingDto;
import com.ecquaria.cloud.moh.iais.common.dto.audit.AuditTrailEntityDto;
import com.ecquaria.cloud.moh.iais.common.dto.system.ProcessFileTrackDto;

import java.io.IOException;
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

    void saveFile(String data) throws IOException;

    void compressFile();

    void   updateSysAdmEicRequestTrackingDto(EicRequestTrackingDto licEicRequestTrackingDto);

    String createBeAuditTrailProcessFileTrack(ProcessFileTrackDto processFileTrackDto);
}

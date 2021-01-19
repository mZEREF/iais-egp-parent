package com.ecquaria.cloud.moh.iais.service;

import com.ecquaria.cloud.moh.iais.common.dto.system.ProcessFileTrackDto;

/**
 * AuditTrailRecordsToBeService
 *
 * @author junyu
 * @date 2020/4/16
 */
public interface AuditTrailRecordsToBeService {
    void info();

    void compress();

    void download(ProcessFileTrackDto processFileTrackDto, String fileName, String refId, String submissionId);

    }

package com.ecquaria.cloud.moh.iais.service;

import com.ecquaria.cloud.moh.iais.common.dto.monitoringExcel.MonitoringSheetsDto;

/**
 * ExcelMonitoringService
 *
 * @author junyu
 * @date 2022/5/19
 */
public interface ExcelMonitoringService {
    MonitoringSheetsDto parse();

    String saveFile(MonitoringSheetsDto parse);

    String compressFile(String timeId);

    void renameAndSave(String compressFileName, String timeId);
}

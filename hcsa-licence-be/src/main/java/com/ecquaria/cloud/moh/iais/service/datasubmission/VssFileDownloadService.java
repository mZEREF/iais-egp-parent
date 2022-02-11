package com.ecquaria.cloud.moh.iais.service.datasubmission;

/**
 * @author fanghao
 * @date 2022/1/25
 */
public interface VssFileDownloadService {
    void initPath();

    boolean decompression() throws Exception;
}

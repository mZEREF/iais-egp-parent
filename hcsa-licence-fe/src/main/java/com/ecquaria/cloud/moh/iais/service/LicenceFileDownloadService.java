package com.ecquaria.cloud.moh.iais.service;

/**
 * LicenceFileDownloadService
 *
 * @author junyu
 * @date 2022/5/11
 */
public interface LicenceFileDownloadService {
    void initPath();

    boolean decompression() throws Exception;
}

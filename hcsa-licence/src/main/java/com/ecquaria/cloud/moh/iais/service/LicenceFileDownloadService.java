package com.ecquaria.cloud.moh.iais.service;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;

import java.util.List;

/**
 * @author Wenkang
 * @date 2019/11/9 16:08
 */
public interface LicenceFileDownloadService {

    String  download();
    void compress();
    List<ApplicationDto> listApplication();
}

package com.ecquaria.cloud.moh.iais.service;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSubmissionDto;

/**
 * LicenceViewService
 *
 * @author suocheng
 * @date 12/16/2019
 */

public interface LicenceViewService {
    public AppSubmissionDto getAppSubmissionByAppId(String appId);
}

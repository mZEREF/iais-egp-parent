package com.ecquaria.cloud.moh.iais.service;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppEditSelectDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSubmissionDto;

/**
 * LicenceViewService
 *
 * @author suocheng
 * @date 12/16/2019
 */

public interface LicenceViewService {
    AppSubmissionDto getAppSubmissionByAppId(String appId);

    AppEditSelectDto saveAppEditSelect(AppEditSelectDto appEditSelectDto);
    AppEditSelectDto saveAppEditSelectToFe(AppEditSelectDto appEditSelectDto);
}

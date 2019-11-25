package com.ecquaria.cloud.moh.iais.service;

import com.ecquaria.cloud.moh.iais.common.dto.application.ApplicationViewDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;

public interface ApplicationViewService {
    ApplicationViewDto searchByAppNo(String appNo);
    boolean isAllApplicationSubmit(String appNo);
    ApplicationDto getApplicaitonByAppNo(String appNo);
    ApplicationDto updateApplicaiton(ApplicationDto applicationDto);

    
}

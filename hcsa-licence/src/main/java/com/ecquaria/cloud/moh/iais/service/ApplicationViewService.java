package com.ecquaria.cloud.moh.iais.service;

import com.ecquaria.cloud.moh.iais.common.dto.application.ApplicationViewDto;

public interface ApplicationViewService {
    ApplicationViewDto searchByAppNo(String appNo);
    
}

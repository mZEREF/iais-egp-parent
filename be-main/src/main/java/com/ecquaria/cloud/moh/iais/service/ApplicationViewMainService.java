package com.ecquaria.cloud.moh.iais.service;

import com.ecquaria.cloud.moh.iais.common.dto.application.ApplicationViewDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesCorrelationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;

public interface ApplicationViewMainService {

    ApplicationViewDto searchByCorrelationIdo(String correlationId);

    ApplicationDto getApplicaitonByAppNo(String appNo);

    ApplicationDto updateApplicaiton(ApplicationDto applicationDto);

    public AppPremisesCorrelationDto getLastAppPremisesCorrelationDtoById(String id);



}

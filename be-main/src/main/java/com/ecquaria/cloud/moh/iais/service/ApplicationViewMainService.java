package com.ecquaria.cloud.moh.iais.service;

import com.ecquaria.cloud.moh.iais.common.dto.application.ApplicationViewDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesCorrelationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;

public interface ApplicationViewMainService {

    ApplicationViewDto searchByCorrelationIdo(String correlationId);

    public AppPremisesCorrelationDto getLastAppPremisesCorrelationDtoById(String id);

    public ApplicationDto updateFEApplicaiton(ApplicationDto applicationDto);

}

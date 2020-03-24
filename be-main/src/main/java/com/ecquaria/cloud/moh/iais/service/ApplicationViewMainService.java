package com.ecquaria.cloud.moh.iais.service;

import com.ecquaria.cloud.moh.iais.common.dto.application.ApplicationViewDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesCorrelationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationGroupDto;

import java.util.List;

public interface ApplicationViewMainService {
    public List<ApplicationDto> getApplicaitonsByAppGroupId(String appGroupId);
    public boolean isOtherApplicaitonSubmit(List<ApplicationDto> applicationDtoList,List<String> appNo,String status);

    ApplicationViewDto searchByCorrelationIdo(String correlationId);

    public AppPremisesCorrelationDto getLastAppPremisesCorrelationDtoById(String id);

    public ApplicationGroupDto getApplicationGroupDtoById(String appGroupId);

}

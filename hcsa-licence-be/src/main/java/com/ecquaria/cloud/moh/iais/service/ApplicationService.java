package com.ecquaria.cloud.moh.iais.service;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesCorrelationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import java.util.List;

/**
 * ApplicationService
 *
 * @author suocheng
 * @date 11/28/2019
 */
public interface ApplicationService {
    public List<ApplicationDto> getApplicaitonsByAppGroupId(String appGroupId);
    public boolean isOtherApplicaitonSubmit(List<ApplicationDto> applicationDtoList,String appId,String status);
    //
    public List<AppPremisesCorrelationDto> getAppPremisesCorrelationByAppGroupId(String appGroupId);

    public ApplicationDto updateFEApplicaiton(ApplicationDto applicationDto);

}

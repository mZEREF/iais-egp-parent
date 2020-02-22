package com.ecquaria.cloud.moh.iais.service;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppEditSelectDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesCorrelationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.RequestInformationSubmitDto;
import java.util.List;

/**
 * ApplicationService
 *
 * @author suocheng
 * @date 11/28/2019
 */
public interface ApplicationService {
    public List<ApplicationDto> getApplicaitonsByAppGroupId(String appGroupId);
    public boolean isOtherApplicaitonSubmit(List<ApplicationDto> applicationDtoList,String appNo,String status);
    //
    public List<AppPremisesCorrelationDto> getAppPremisesCorrelationByAppGroupId(String appGroupId);
    public Integer getAppBYGroupIdAndStatus(String appGroupId,String  status);

    public ApplicationDto updateFEApplicaiton(ApplicationDto applicationDto);

    public List<RequestInformationSubmitDto> getRequestInformationSubmitDtos(List<ApplicationDto> applicationDtos);

    public List<AppEditSelectDto> getAppEditSelectDtos(String appId, String changeType);

}

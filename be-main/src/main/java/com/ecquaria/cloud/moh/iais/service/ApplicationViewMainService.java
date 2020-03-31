package com.ecquaria.cloud.moh.iais.service;

import com.ecquaria.cloud.moh.iais.common.dto.application.ApplicationViewDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesCorrelationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationGroupDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcDocConfigDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.OrgUserDto;

import java.util.List;

public interface ApplicationViewMainService {
    public List<ApplicationDto> getApplicaitonsByAppGroupId(String appGroupId);
    public boolean isOtherApplicaitonSubmit(List<ApplicationDto> applicationDtoList,List<String> appNo,String status);

    ApplicationViewDto searchByCorrelationIdo(String correlationId);

    public AppPremisesCorrelationDto getLastAppPremisesCorrelationDtoById(String id);

    public ApplicationGroupDto getApplicationGroupDtoById(String appGroupId);

    public ApplicationDto updateFEApplicaiton(ApplicationDto applicationDto);

    ApplicationViewDto getApplicationViewDtoByCorrId(String appCorId);

    HcsaServiceDto getHcsaServiceDtoById(String id);

    List<HcsaSvcDocConfigDto> getTitleById(List<String> titleIdList);

    List<OrgUserDto> getUserNameById(List<String> userIdList);

    String getWrkGrpName(String id);
}

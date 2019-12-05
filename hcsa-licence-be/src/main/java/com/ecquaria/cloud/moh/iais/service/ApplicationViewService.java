package com.ecquaria.cloud.moh.iais.service;

import com.ecquaria.cloud.moh.iais.common.dto.application.ApplicationViewDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcDocConfigDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcRoutingStageDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.OrgUserDto;
import java.util.List;

public interface ApplicationViewService {

    ApplicationViewDto searchByAppNo(String appNo);


    ApplicationDto getApplicaitonByAppNo(String appNo);

    ApplicationDto updateApplicaiton(ApplicationDto applicationDto);

    List<OrgUserDto> getUserNameById(List<String> userIdList);

    List<HcsaSvcDocConfigDto> getTitleById(List<String> titleIdList);

    List<HcsaSvcRoutingStageDto> getStage(String serviceId,String stageId);
}

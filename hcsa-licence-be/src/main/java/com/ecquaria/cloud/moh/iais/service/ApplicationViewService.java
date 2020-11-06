package com.ecquaria.cloud.moh.iais.service;

import com.ecquaria.cloud.moh.iais.common.dto.application.ApplicationViewDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesCorrelationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcDocConfigDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcRoutingStageDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcSubtypeOrSubsumedDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.OrgUserDto;
import java.util.List;

public interface ApplicationViewService {

    ApplicationViewDto searchByCorrelationIdo(String correlationId);

    ApplicationDto getApplicaitonByAppNo(String appNo);

    ApplicationDto updateApplicaiton(ApplicationDto applicationDto);

    List<OrgUserDto> getUserNameById(List<String> userIdList);

    List<HcsaSvcDocConfigDto> getTitleById(List<String> titleIdList);

    List<HcsaSvcRoutingStageDto> getStage(String serviceId,String stageId);

    List<HcsaSvcRoutingStageDto> getStage(String serviceId,String stageId,String appType,Integer isPreIns);

    OrgUserDto getUserById(String userId);

    String getWrkGrpName(String id);

    HcsaServiceDto getHcsaServiceDtoById(String id);

    List<HcsaSvcSubtypeOrSubsumedDto> getHcsaSvcSubtypeOrSubsumedByServiceId(String serviceId);

    public AppPremisesCorrelationDto getLastAppPremisesCorrelationDtoById(String id);

    HcsaSvcRoutingStageDto getStageById(String id);

    ApplicationViewDto getApplicationViewDtoByCorrId(String appCorId);

    void clearApprovedHclCodeByExistRejectApp( List<ApplicationDto> saveApplicationDtoList,String appGroupType,ApplicationDto applicationDtoMain);
}

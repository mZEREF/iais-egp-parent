package com.ecquaria.cloud.moh.iais.service;

import com.ecquaria.cloud.moh.iais.common.dto.application.ApplicationViewDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppGrpPremisesDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspecTaskCreAndAssDto;
import com.ecquaria.cloud.moh.iais.common.dto.intranetDashboard.HcsaTaskAssignDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.GroupRoleFieldDto;
import com.ecquaria.cloud.moh.iais.dto.LoginContext;

/**
 * @author Shicheng
 * @date 2019/11/22 10:19
 **/
public interface InspectionMainAssignTaskService {

    HcsaServiceDto getHcsaServiceDtoByServiceId(String serviceId);

    String taskRead(String taskId);

    /**
     * @author: shicheng
     * @Date 2021/04/19
     * @Param: appGroupId
     * @return: AppGrpPremisesDto
     * @Descripation: get Application Group Premises By Application Id
     */
    AppGrpPremisesDto getAppGrpPremisesDtoByAppCorrId(String applicationId);
    AppGrpPremisesDto getAppGrpPremisesDtoByAppCorrId(AppGrpPremisesDto appGrpPremisesDto);

    /**
     * @author: shicheng
     * @Date 2020/3/19
     * @Param: appGrpPremisesDtom, hcsaTaskAssignDto
     * @return: String
     * @Descripation: getAddress
     */
    String getAddress(AppGrpPremisesDto appGrpPremisesDto, HcsaTaskAssignDto hcsaTaskAssignDto);

    /**
      * @author: shicheng
      * @Date 2021/4/20
      * @Param: inspecTaskCreAndAssDto, applicationDto
      * @return: InspecTaskCreAndAssDto
      * @Descripation: setFastTrackFlag
      */
    InspecTaskCreAndAssDto setFastTrackFlag(InspecTaskCreAndAssDto inspecTaskCreAndAssDto, ApplicationDto applicationDto);

    /**
      * @author: shicheng
      * @Date 2021/4/20
      * @Param: applicationDto, loginContext, inspecTaskCreAndAssDto, hcsaTaskAssignDto
      * @return: InspecTaskCreAndAssDto
      * @Descripation: getInspecTaskCreAndAssDto
      */
    InspecTaskCreAndAssDto getInspecTaskCreAndAssDto(ApplicationDto applicationDto, LoginContext loginContext, InspecTaskCreAndAssDto inspecTaskCreAndAssDto,
                                                     HcsaTaskAssignDto hcsaTaskAssignDto);

    /**
      * @author: shicheng
      * @Date 2021/4/20
      * @Param: inspecTaskCreAndAssDto, applicationDto
      * @return: InspecTaskCreAndAssDto
      * @Descripation: setEditHoursFlagByAppAndUser
      */
    InspecTaskCreAndAssDto setEditHoursFlagByAppAndUser(InspecTaskCreAndAssDto inspecTaskCreAndAssDto, ApplicationDto applicationDto);

    /**
      * @author: shicheng
      * @Date 2021/4/21
      * @Param: inspecTaskCreAndAssDto, internalRemarks, loginContext
      * @return: save flag
      * @Descripation: routingTaskByCommonPool
      */
    String routingTaskByCommonPool(ApplicationViewDto applicationViewDto, InspecTaskCreAndAssDto inspecTaskCreAndAssDto, String internalRemarks, LoginContext loginContext);

    /**
     * @author: shicheng
     * @Date 2020/3/20
     * @Param: loginContext
     * @return: GroupRoleFieldDto
     * @Descripation: get Group Role Field
     */
    GroupRoleFieldDto getGroupRoleField(LoginContext loginContext);
}

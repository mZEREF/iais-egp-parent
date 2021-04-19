package com.ecquaria.cloud.moh.iais.service;

import com.ecquaria.cloud.moh.iais.common.dto.application.ApplicationViewDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppGrpPremisesDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceDto;

/**
 * @author Shicheng
 * @date 2019/11/22 10:19
 **/
public interface InspectionMainAssignTaskService {

    /**
      * @author: shicheng
      * @Date 2019/12/5
      * @Param: applicationNo
      * @return: ApplicationViewDto
      * @Descripation: search ApplicationViewDto By Application No
      */
    ApplicationViewDto searchByAppNo(String applicationNo);

    HcsaServiceDto getHcsaServiceDtoByServiceId(String serviceId);

    String taskRead(String taskId);

    /**
     * @author: shicheng
     * @Date 2021/04/19
     * @Param: appGroupId
     * @return: AppGrpPremisesDto
     * @Descripation: get Application Group Premises By Application Id
     */
    AppGrpPremisesDto getAppGrpPremisesDtoByAppGroId(String applicationId);

    /**
     * @author: shicheng
     * @Date 2020/3/19
     * @Param: appGrpPremisesDto
     * @return: String
     * @Descripation: getAddress
     */
    String getAddress(AppGrpPremisesDto appGrpPremisesDto);
}

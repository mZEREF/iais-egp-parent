package com.ecquaria.cloud.moh.iais.service;

import com.ecquaria.cloud.moh.iais.common.dto.application.ApplicationViewDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.BroadcastApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.EventBeLicenseDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.BroadcastOrganizationDto;
import com.ecquaria.cloud.moh.iais.common.dto.task.TaskDto;
import sop.webflow.rt.api.Process;

/**
 * BroadcastService
 *
 * @author suocheng
 * @date 12/10/2019
 */

public interface BroadcastService {
    public BroadcastOrganizationDto svaeBroadcastOrganization(BroadcastOrganizationDto broadcastOrganizationDto,Process process,String submissionId);
    public BroadcastApplicationDto svaeBroadcastApplicationDto(BroadcastApplicationDto broadcastApplicationDto,Process process,String submissionId);
    public BroadcastOrganizationDto getBroadcastOrganizationDto(String groupName, String groupDomain);

    /**
      * @author: shicheng
      * @Date 2021/6/8
      * @Param: broadcastApplicationDto, applicationViewDto
      * @return: BroadcastApplicationDto
      * @Descripation: setAppSvcVehicleDtoByAppView
      */
    BroadcastApplicationDto setAppSvcVehicleDtoByAppView(BroadcastApplicationDto broadcastApplicationDto, ApplicationViewDto applicationViewDto,
                                                         String appStatus, String appType);

    BroadcastApplicationDto setAppPremSubSvcDtoByAppView(BroadcastApplicationDto broadcastApplicationDto, ApplicationViewDto applicationViewDto,
                                                         String appStatus, String appType);

    /**
      * @author: shicheng
      * @Date 2021/6/24
      * @Param: taskDto, applicationViewDto, broadcastApplicationDto
      * @return: BroadcastApplicationDto
      * @Descripation: replySetVehicleByRole
      */
    BroadcastApplicationDto replySetVehicleByRole(TaskDto taskDto, ApplicationViewDto applicationViewDto, BroadcastApplicationDto broadcastApplicationDto);


    BroadcastApplicationDto replySetSubSvcByRole(TaskDto taskDto, ApplicationViewDto applicationViewDto, BroadcastApplicationDto broadcastApplicationDto);

    /**
      * @author: shicheng
      * @Date 2021/7/28
      * @Param: appStatus(process decision), applicationDto, submissionId, evenRefNum
      * @return:
      * @Descripation:
      */
    EventBeLicenseDto saveEventBeLicenseDto(String appStatus, ApplicationDto applicationDto, String submissionId, String evenRefNum, Process process);
}

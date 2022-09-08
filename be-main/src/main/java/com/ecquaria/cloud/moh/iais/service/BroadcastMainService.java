package com.ecquaria.cloud.moh.iais.service;

import com.ecquaria.cloud.moh.iais.common.dto.application.ApplicationViewDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.BroadcastApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.BroadcastOrganizationDto;
import sop.webflow.rt.api.Process;

/**
 * BroadcastService
 *
 * @author suocheng
 * @date 12/10/2019
 */

public interface BroadcastMainService {
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
}

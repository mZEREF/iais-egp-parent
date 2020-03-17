package com.ecquaria.cloud.moh.iais.service;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.BroadcastApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.BroadcastOrganizationDto;
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

}

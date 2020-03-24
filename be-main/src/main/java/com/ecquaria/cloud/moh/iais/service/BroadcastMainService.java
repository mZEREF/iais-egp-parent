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

public interface BroadcastMainService {
    public BroadcastOrganizationDto svaeBroadcastOrganization(BroadcastOrganizationDto broadcastOrganizationDto, Process processm,boolean wait);
    public BroadcastApplicationDto svaeBroadcastApplicationDto(BroadcastApplicationDto broadcastApplicationDto, Process process,boolean wait);

}

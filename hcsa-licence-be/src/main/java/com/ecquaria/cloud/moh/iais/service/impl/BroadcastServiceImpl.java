package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.common.constant.EventBusConsts;
import com.ecquaria.cloud.moh.iais.common.dto.application.ApplicationViewDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcVehicleDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.BroadcastApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.BroadcastOrganizationDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.helper.EventBusHelper;
import com.ecquaria.cloud.moh.iais.service.BroadcastService;
import com.ecquaria.cloud.moh.iais.service.client.AppSvcVehicleBeClient;
import com.ecquaria.cloud.moh.iais.service.client.OrganizationClient;
import com.ecquaria.cloud.submission.client.model.SubmitResp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sop.webflow.rt.api.Process;

import java.util.List;

/**
 * BroadcastServiceImpl
 *
 * @author suocheng
 * @date 12/10/2019
 */
@Service
public class BroadcastServiceImpl implements BroadcastService {
    @Autowired
    private OrganizationClient organizationClient;
    @Autowired
    private EventBusHelper eventBusHelper;

    @Autowired
    private AppSvcVehicleBeClient appSvcVehicleBeClient;

    @Override
    public BroadcastOrganizationDto svaeBroadcastOrganization(BroadcastOrganizationDto broadcastOrganizationDto,Process process,String submissionId) {
        SubmitResp submitResp = eventBusHelper.submitAsyncRequest(broadcastOrganizationDto, submissionId,
                EventBusConsts.SERVICE_NAME_ROUNTINGTASK,
                EventBusConsts.OPERATION_ROUNTINGTASK_ROUNTING,
                broadcastOrganizationDto.getEventRefNo(), process);

        return broadcastOrganizationDto;
    }

    @Override
    public BroadcastApplicationDto svaeBroadcastApplicationDto(BroadcastApplicationDto broadcastApplicationDto,Process process,String submissionId) {
        SubmitResp submitResp = eventBusHelper.submitAsyncRequest(broadcastApplicationDto, submissionId,
                EventBusConsts.SERVICE_NAME_APPSUBMIT,
                EventBusConsts.OPERATION_ROUNTINGTASK_ROUNTING,
                broadcastApplicationDto.getEventRefNo(), process);

        return broadcastApplicationDto;
    }

    @Override
    public BroadcastOrganizationDto getBroadcastOrganizationDto(String groupName, String groupDomain) {
        return organizationClient.getBroadcastOrganizationDto(groupName,groupDomain).getEntity();
    }

    @Override
    public BroadcastApplicationDto setAppSvcVehicleDtoByAppView(BroadcastApplicationDto broadcastApplicationDto, ApplicationViewDto applicationViewDto) {
        if(applicationViewDto != null) {
            List<AppSvcVehicleDto> appSvcVehicleDtos = applicationViewDto.getAppSvcVehicleDtos();
            if(!IaisCommonUtils.isEmpty(appSvcVehicleDtos)) {
                broadcastApplicationDto.setAppSvcVehicleDtos(appSvcVehicleDtos);
                //get db data
                List<AppSvcVehicleDto> appSvcVehicleDtoList = appSvcVehicleBeClient.getAppSvcVehicleDtoListByCorrId(appSvcVehicleDtos.get(0).getAppPremCorreId()).getEntity();
                broadcastApplicationDto.setRollBackAppSvcVehicleDtos(appSvcVehicleDtoList);
            }
        }
        return broadcastApplicationDto;
    }
}

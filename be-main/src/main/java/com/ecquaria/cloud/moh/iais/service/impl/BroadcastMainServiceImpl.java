package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.constant.EventBusConsts;
import com.ecquaria.cloud.moh.iais.common.constant.inspection.InspectionConstants;
import com.ecquaria.cloud.moh.iais.common.dto.application.ApplicationViewDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcVehicleDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.BroadcastApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.BroadcastOrganizationDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.helper.EventBusHelper;
import com.ecquaria.cloud.moh.iais.service.BroadcastMainService;
import com.ecquaria.cloud.moh.iais.service.client.AppSvcVehicleBeMainClient;
import com.ecquaria.cloud.moh.iais.service.client.OrganizationMainClient;
import com.ecquaria.cloud.submission.client.model.SubmitResp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
public class BroadcastMainServiceImpl implements BroadcastMainService {

    @Autowired
    private OrganizationMainClient organizationClient;

    @Autowired
    private AppSvcVehicleBeMainClient appSvcVehicleBeMainClient;

    @Autowired
    private EventBusHelper eventBusHelper;

    @Value("${easmts.vehicle.sperate.flag}")
    private String vehicleOpenFlag;

    @Override
    public BroadcastOrganizationDto svaeBroadcastOrganization(BroadcastOrganizationDto broadcastOrganizationDto,Process process,String submissionId) {
        SubmitResp submitResp = eventBusHelper.submitAsyncRequestWithoutCallback(broadcastOrganizationDto, submissionId,
                EventBusConsts.SERVICE_NAME_ROUNTINGTASK,
                EventBusConsts.OPERATION_ROUNTINGTASK_ROUNTING,
                broadcastOrganizationDto.getEventRefNo(), process);

        return broadcastOrganizationDto;
    }

    @Override
    public BroadcastApplicationDto svaeBroadcastApplicationDto(BroadcastApplicationDto broadcastApplicationDto,Process process,String submissionId) {
        SubmitResp submitResp = eventBusHelper.submitAsyncRequestWithoutCallback(broadcastApplicationDto, submissionId,
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
    public BroadcastApplicationDto setAppSvcVehicleDtoByAppView(BroadcastApplicationDto broadcastApplicationDto, ApplicationViewDto applicationViewDto,
                                                                String appStatus, String appType) {
        if(applicationViewDto != null) {
            List<AppSvcVehicleDto> appSvcVehicleDtos;
            if (ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE.equals(appType) && InspectionConstants.SWITCH_ACTION_YES.equals(vehicleOpenFlag)) {
                appSvcVehicleDtos = applicationViewDto.getVehicleRfcShowDtos();
            } else {
                appSvcVehicleDtos = applicationViewDto.getAppSvcVehicleDtos();
            }
            if (!IaisCommonUtils.isEmpty(appSvcVehicleDtos)) {
                //get db data
                List<AppSvcVehicleDto> appSvcVehicleDtoList = appSvcVehicleBeMainClient.getAppSvcVehicleDtoListByCorrId(appSvcVehicleDtos.get(0).getAppPremCorreId()).getEntity();
                //vehicle details show
                if(InspectionConstants.SWITCH_ACTION_YES.equals(vehicleOpenFlag)) {

                    if(ApplicationConsts.APPLICATION_STATUS_REJECTED.equals(appStatus)) {
                        for(AppSvcVehicleDto appSvcVehicleDto : appSvcVehicleDtos) {
                            appSvcVehicleDto.setStatus(ApplicationConsts.VEHICLE_STATUS_REJECT);
                        }
                    }
                    broadcastApplicationDto.setAppSvcVehicleDtos(appSvcVehicleDtos);
                    //set db data for roll back
                    broadcastApplicationDto.setRollBackAppSvcVehicleDtos(appSvcVehicleDtoList);

                    //vehicle details don't show
                } else {
                    for(AppSvcVehicleDto appSvcVehicleDto : appSvcVehicleDtos) {
                        if(ApplicationConsts.APPLICATION_STATUS_APPROVED.equals(appStatus)) {
                            appSvcVehicleDto.setStatus(ApplicationConsts.VEHICLE_STATUS_APPROVE);
                        } else if(ApplicationConsts.APPLICATION_STATUS_REJECTED.equals(appStatus)) {
                            appSvcVehicleDto.setStatus(ApplicationConsts.VEHICLE_STATUS_REJECT);
                        }
                    }
                    broadcastApplicationDto.setAppSvcVehicleDtos(appSvcVehicleDtos);
                    //set db data for roll back
                    broadcastApplicationDto.setRollBackAppSvcVehicleDtos(appSvcVehicleDtoList);
                }
            }
        }
        return broadcastApplicationDto;
    }
}

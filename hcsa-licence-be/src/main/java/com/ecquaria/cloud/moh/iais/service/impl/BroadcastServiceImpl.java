package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.constant.EventBusConsts;
import com.ecquaria.cloud.moh.iais.common.constant.HcsaConsts;
import com.ecquaria.cloud.moh.iais.common.constant.inspection.InspectionConstants;
import com.ecquaria.cloud.moh.iais.common.dto.application.ApplicationViewDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcVehicleDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.BroadcastApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.EventBeLicenseDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicAppCorrelationDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.BroadcastOrganizationDto;
import com.ecquaria.cloud.moh.iais.common.dto.task.TaskDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.helper.EventBusHelper;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.service.BroadcastService;
import com.ecquaria.cloud.moh.iais.service.client.AppSvcVehicleBeClient;
import com.ecquaria.cloud.moh.iais.service.client.OrganizationClient;
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
public class BroadcastServiceImpl implements BroadcastService {
    @Autowired
    private OrganizationClient organizationClient;
    @Autowired
    private EventBusHelper eventBusHelper;

    @Autowired
    private AppSvcVehicleBeClient appSvcVehicleBeClient;

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
                List<AppSvcVehicleDto> appSvcVehicleDtoList = appSvcVehicleBeClient.getAppSvcVehicleDtoListByCorrId(appSvcVehicleDtos.get(0).getAppPremCorreId()).getEntity();
                //vehicle details show
                if(InspectionConstants.SWITCH_ACTION_YES.equals(vehicleOpenFlag)) {
                    if(ApplicationConsts.APPLICATION_STATUS_REJECTED.equals(appStatus)) {
                        for(AppSvcVehicleDto appSvcVehicleDto : appSvcVehicleDtos) {
                            appSvcVehicleDto.setStatus(ApplicationConsts.VEHICLE_STATUS_REJECT);
                            appSvcVehicleDto.setActCode(ApplicationConsts.VEHICLE_ACTION_CODE_ONCHANGE);
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
                            appSvcVehicleDto.setActCode(ApplicationConsts.VEHICLE_ACTION_CODE_ONCHANGE);
                        } else if(ApplicationConsts.APPLICATION_STATUS_REJECTED.equals(appStatus)) {
                            appSvcVehicleDto.setStatus(ApplicationConsts.VEHICLE_STATUS_REJECT);
                            appSvcVehicleDto.setActCode(ApplicationConsts.VEHICLE_ACTION_CODE_ONCHANGE);
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

    @Override
    public BroadcastApplicationDto replySetVehicleByRole(TaskDto taskDto, ApplicationViewDto applicationViewDto, BroadcastApplicationDto broadcastApplicationDto) {
        if(taskDto != null && applicationViewDto != null) {
            String stageId = taskDto.getTaskKey();
            List<AppSvcVehicleDto> appSvcVehicleDtos;
            if (ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE.equals(applicationViewDto.getApplicationDto().getApplicationType())) {
                appSvcVehicleDtos = applicationViewDto.getVehicleRfcShowDtos();
            } else {
                appSvcVehicleDtos = applicationViewDto.getAppSvcVehicleDtos();
            }
            if(HcsaConsts.ROUTING_STAGE_ASO.equals(stageId) || HcsaConsts.ROUTING_STAGE_PSO.equals(stageId)) {
                if (!IaisCommonUtils.isEmpty(appSvcVehicleDtos)) {
                    //get db data
                    List<AppSvcVehicleDto> appSvcVehicleDtoList = appSvcVehicleBeClient.getAppSvcVehicleDtoListByCorrId(appSvcVehicleDtos.get(0).getAppPremCorreId()).getEntity();
                    //vehicle details show
                    broadcastApplicationDto.setAppSvcVehicleDtos(appSvcVehicleDtos);
                    //set db data for roll back
                    broadcastApplicationDto.setRollBackAppSvcVehicleDtos(appSvcVehicleDtoList);
                }
            }
        }
        return broadcastApplicationDto;
    }

    @Override
    public EventBeLicenseDto saveEventBeLicenseDto(String appStatus, ApplicationDto applicationDto, String submissionId, String evenRefNum,
                                                   Process process) {
        if(applicationDto != null) {
            if(ApplicationConsts.APPLICATION_STATUS_REJECTED.equals(appStatus)) {
                String appType = applicationDto.getApplicationType();
                if(ApplicationConsts.APPLICATION_TYPE_RENEWAL.equals(appType) || ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE.equals(appType)) {
                    EventBeLicenseDto eventBeLicenseDto = new EventBeLicenseDto();
                    eventBeLicenseDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
                    eventBeLicenseDto.setEventRefNo(evenRefNum);
                    //set dto
                    LicAppCorrelationDto licAppCorrelationDto = new LicAppCorrelationDto();
                    licAppCorrelationDto.setLicenceId(applicationDto.getOriginLicenceId());
                    licAppCorrelationDto.setApplicationId(applicationDto.getId());
                    //set roll back
                    LicAppCorrelationDto rollBackLicAppCorrelationDto = new LicAppCorrelationDto();
                    rollBackLicAppCorrelationDto.setApplicationId(applicationDto.getId());
                    rollBackLicAppCorrelationDto.setLicenceId(applicationDto.getOriginLicenceId());

                    //save
                    SubmitResp submitResp = eventBusHelper.submitAsyncRequestWithoutCallback(eventBeLicenseDto, submissionId,
                            EventBusConsts.SERVICE_NAME_LICENCESAVE,
                            EventBusConsts.OPERATION_ROUNTINGTASK_ROUNTING,
                            evenRefNum, process);

                    return eventBeLicenseDto;
                }
            }
        }
        return null;
    }
}

package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.common.config.SystemParamConfig;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.EventBusConsts;
import com.ecquaria.cloud.moh.iais.common.constant.rest.RestApiUrlConsts;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.BroadcastApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.BroadcastOrganizationDto;
import com.ecquaria.cloud.moh.iais.helper.EventBusHelper;
import com.ecquaria.cloud.moh.iais.service.BroadcastService;
import com.ecquaria.cloud.moh.iais.service.client.GenerateIdClient;
import com.ecquaria.cloud.moh.iais.service.client.OrganizationClient;
import com.ecquaria.cloud.submission.client.model.SubmitReq;
import com.ecquaria.cloud.submission.client.model.SubmitResp;
import com.ecquaria.cloud.submission.client.wrapper.SubmissionClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sop.webflow.rt.api.Process;

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
    private GenerateIdClient generateIdClient;
    @Autowired
    private SubmissionClient client;
    @Autowired
    private SystemParamConfig systemParamConfig;
    @Override
    public BroadcastOrganizationDto svaeBroadcastOrganization(BroadcastOrganizationDto broadcastOrganizationDto,Process process) {
        String callBackUrl = systemParamConfig.getIntraServerName()+"/hcsa-licence-web/eservice/INTRANET/LicenceEventBusCallBack";
        String sopUrl = systemParamConfig.getIntraServerName()+"/hcsa-licence-web/eservice/INTRANET/ApplicationView";
        String project ="hcsaLicenceBe";
        String processName = "rountingASO_createTask";
        String step = "start";
        if(process!=null){
            project= process.getCurrentProject();
            processName = process.getCurrentProcessName();
            step = process.getCurrentComponentName();
            callBackUrl =  process.getHttpRequest().getServerName()
                    +process.getHttpRequest().getContextPath()
                    +"/eservice/INTRANET/LicenceEventBusCallBack";
        }
        SubmitReq req = EventBusHelper.getSubmitReq(broadcastOrganizationDto, generateIdClient.getSeqId().getEntity(),
                EventBusConsts.SERVICE_NAME_ROUNTINGTASK,
                EventBusConsts.OPERATION_ROUNTINGTASK_ROUNTING,
                sopUrl,
                callBackUrl, EventBusConsts.SOP_USER_ID,false,project,processName,step);
        //
        SubmitResp submitResp = client.submit(AppConsts.REST_PROTOCOL_TYPE + RestApiUrlConsts.EVENT_BUS, req);
        return null;
    }

    @Override
    public BroadcastApplicationDto svaeBroadcastApplicationDto(BroadcastApplicationDto broadcastApplicationDto,Process process) {
        String callBackUrl = systemParamConfig.getIntraServerName()+"/hcsa-licence-web/eservice/INTRANET/LicenceEventBusCallBack";
        String sopUrl = systemParamConfig.getIntraServerName()+"/hcsa-licence-web/eservice/INTRANET/ApplicationView";
        String project ="hcsaLicenceBe";
        String processName = "rountingASO_updateApplication";
        String step = "start";
        if(process!=null){
            project= process.getCurrentProject();
            processName = process.getCurrentProcessName();
            step = process.getCurrentComponentName();
            callBackUrl =  process.getHttpRequest().getServerName()
                    +process.getHttpRequest().getContextPath()
                    +"/eservice/INTRANET/LicenceEventBusCallBack";
        }
        SubmitReq req =EventBusHelper.getSubmitReq(broadcastApplicationDto, generateIdClient.getSeqId().getEntity(),
                EventBusConsts.SERVICE_NAME_APPSUBMIT,
                EventBusConsts.OPERATION_ROUNTINGTASK_ROUNTING,
                sopUrl,
                callBackUrl, EventBusConsts.SOP_USER_ID,false,project,processName,step);
        //
        SubmitResp submitResp = client.submit(AppConsts.REST_PROTOCOL_TYPE + RestApiUrlConsts.EVENT_BUS, req);
        return null;
    }

    @Override
    public BroadcastOrganizationDto getBroadcastOrganizationDto(String groupName, String groupDomain) {
        return organizationClient.getBroadcastOrganizationDto(groupName,groupDomain).getEntity();
    }
}

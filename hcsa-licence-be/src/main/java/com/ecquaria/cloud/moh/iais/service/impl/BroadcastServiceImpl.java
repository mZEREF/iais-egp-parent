package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.EventBusConsts;
import com.ecquaria.cloud.moh.iais.common.constant.rest.RestApiUrlConsts;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.BroadcastApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.BroadcastOrganizationDto;
import com.ecquaria.cloud.moh.iais.helper.EventBusHelper;
import com.ecquaria.cloud.moh.iais.service.BroadcastService;
import com.ecquaria.cloud.moh.iais.service.client.OrganizationClient;
import com.ecquaria.cloud.moh.iais.service.client.SystemAdminClient;
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
    private SystemAdminClient systemAdminClient;
    @Autowired
    private SubmissionClient client;
    @Override
    public BroadcastOrganizationDto svaeBroadcastOrganization(BroadcastOrganizationDto broadcastOrganizationDto,Process process) {
        String  callBackUrl = "egp.sit.intra.iais.com/hcsa-licence-web/eservice/INTRANET/LicenceEventBusCallBack";
        String project ="hcsaLicenceBe";
        String processName = "rountingASO";
        String step = "start";
        if(process!=null){
            project= process.getCurrentProject();
            processName = process.getCurrentProcessName();
            step = process.getCurrentComponentName();
            callBackUrl =  process.getHttpRequest().getServerName()
                    +process.getHttpRequest().getContextPath()
                    +"/eservice/INTRANET/LicenceEventBusCallBack";
        }
        SubmitReq req = EventBusHelper.getSubmitReq(broadcastOrganizationDto, systemAdminClient.getSeqId().getEntity(),
                EventBusConsts.SERVICE_NAME_ROUNTINGTASK,
                EventBusConsts.OPERATION_ROUNTINGTASK_ROUNTING,
                "https://egp.sit.intra.iais.com/hcsa-licence-web/eservice/INTRANET/ApplicationView",
                callBackUrl, "sop",false,project,processName,step);
        //
        SubmitResp submitResp = client.submit(AppConsts.REST_PROTOCOL_TYPE + RestApiUrlConsts.EVENT_BUS, req);
        return null;
    }

    @Override
    public BroadcastApplicationDto svaeBroadcastApplicationDto(BroadcastApplicationDto broadcastApplicationDto,Process process) {
        String  callBackUrl = "egp.sit.intra.iais.com/hcsa-licence-web/eservice/INTRANET/LicenceEventBusCallBack";
        String project ="hcsaLicenceBe";
        String processName = "rountingASO";
        String step = "start";
        if(process!=null){
            project= process.getCurrentProject();
            processName = process.getCurrentProcessName();
            step = process.getCurrentComponentName();
            callBackUrl =  process.getHttpRequest().getServerName()
                    +process.getHttpRequest().getContextPath()
                    +"/eservice/INTRANET/LicenceEventBusCallBack";
        }
        SubmitReq req =EventBusHelper.getSubmitReq(broadcastApplicationDto,systemAdminClient.getSeqId().getEntity(),
                EventBusConsts.SERVICE_NAME_APPSUBMIT,
                EventBusConsts.OPERATION_ROUNTINGTASK_ROUNTING,
                "https://egp.sit.intra.iais.com/hcsa-licence-web/eservice/INTRANET/ApplicationView",
                callBackUrl, "sop",false,project,processName,step);
        //
        SubmitResp submitResp = client.submit(AppConsts.REST_PROTOCOL_TYPE + RestApiUrlConsts.EVENT_BUS, req);
        return null;
    }

    @Override
    public BroadcastOrganizationDto getBroadcastOrganizationDto(String groupName, String groupDomain) {
        return organizationClient.getBroadcastOrganizationDto(groupName,groupDomain).getEntity();
    }
}

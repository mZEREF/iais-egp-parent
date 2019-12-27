package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.EventBusConsts;
import com.ecquaria.cloud.moh.iais.common.constant.rest.RestApiUrlConsts;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationGroupDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.EventApplicationGroupDto;
import com.ecquaria.cloud.moh.iais.helper.EventBusHelper;
import com.ecquaria.cloud.moh.iais.service.ApplicationGroupService;
import com.ecquaria.cloud.moh.iais.service.client.ApplicationClient;
import com.ecquaria.cloud.moh.iais.service.client.SystemAdminClient;
import com.ecquaria.cloud.submission.client.model.SubmitReq;
import com.ecquaria.cloud.submission.client.model.SubmitResp;
import com.ecquaria.cloud.submission.client.wrapper.SubmissionClient;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * ApplicationGroupServiceImpl
 *
 * @author suocheng
 * @date 11/28/2019
 */
@Service
@Slf4j
public class ApplicationGroupServiceImpl implements ApplicationGroupService {

    @Autowired
    private ApplicationClient applicationClient;

    @Autowired
    private SystemAdminClient systemAdminClient;
    @Autowired
    private SubmissionClient client;

    @Override
    public ApplicationGroupDto getApplicationGroupDtoById(String appGroupId) {
        return applicationClient.getAppById(appGroupId).getEntity();
    }

    @Override
    public ApplicationGroupDto updateApplicationGroup(ApplicationGroupDto applicationGroupDto) {
        return applicationClient.updateApplication(applicationGroupDto).getEntity();
    }

    @Override
    public List<ApplicationGroupDto> updateApplicationGroups(List<ApplicationGroupDto> applicationGroupDtos) {

        return applicationClient.updateApplications(applicationGroupDtos).getEntity();
    }

    @Override
    public EventApplicationGroupDto updateEventApplicationGroupDto(EventApplicationGroupDto eventApplicationGroupDto) {
        String  callBackUrl = "egp.sit.intra.iais.com/hcsa-licence-web/eservice/INTRANET/LicenceEventBusCallBack";
        String project ="hcsaLicenceBe";
        String processName = "generateLicence";
        String step = "start";
        SubmitReq req =EventBusHelper.getSubmitReq(eventApplicationGroupDto,systemAdminClient.getSeqId().getEntity(),
                EventBusConsts.SERVICE_NAME_APPSUBMIT,
                EventBusConsts.OPERATION_APPLICATION_UPDATE,
                "https://egp.sit.intra.iais.com/hcsa-licence-web/eservice/INTRANET/ApplicationView",
                callBackUrl, "sop",true,project,processName,step);
        //
        SubmitResp submitResp = client.submit(AppConsts.REST_PROTOCOL_TYPE + RestApiUrlConsts.EVENT_BUS, req);
        return null;
    }


}

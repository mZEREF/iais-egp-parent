package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.common.config.SystemParamConfig;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.EventBusConsts;
import com.ecquaria.cloud.moh.iais.common.constant.rest.RestApiUrlConsts;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationGroupDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.EventApplicationGroupDto;
import com.ecquaria.cloud.moh.iais.helper.EventBusHelper;
import com.ecquaria.cloud.moh.iais.service.ApplicationGroupService;
import com.ecquaria.cloud.moh.iais.service.client.ApplicationClient;
import com.ecquaria.cloud.moh.iais.service.client.GenerateIdClient;
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
    private GenerateIdClient generateIdClient;
    @Autowired
    private SubmissionClient client;
    @Autowired
    private SystemParamConfig systemParamConfig;

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
        String callBackUrl = systemParamConfig.getIntraServerName()+"/hcsa-licence-web/eservice/INTRANET/LicenceEventBusCallBack";
        String sopUrl = systemParamConfig.getIntraServerName()+"/hcsa-licence-web/eservice/INTRANET/ApplicationView";
        String project ="hcsaLicenceBe";
        String processName = "generateLicence";
        String step = "start";
        SubmitReq req =EventBusHelper.getSubmitReq(eventApplicationGroupDto, generateIdClient.getSeqId().getEntity(),
                EventBusConsts.SERVICE_NAME_APPSUBMIT,
                EventBusConsts.OPERATION_APPLICATION_UPDATE,
                sopUrl,
                callBackUrl, EventBusConsts.SOP_USER_ID,false,project,processName,step);
        //
        SubmitResp submitResp = client.submit(AppConsts.REST_PROTOCOL_TYPE + RestApiUrlConsts.EVENT_BUS, req);
        return null;
    }


}

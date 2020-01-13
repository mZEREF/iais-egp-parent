package com.ecquaria.cloud.moh.iais.service.impl;


import com.ecquaria.cloud.moh.iais.common.config.SystemParamConfig;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.PremisesListQueryDto;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.service.RequestForChangeService;
import com.ecquaria.cloud.moh.iais.service.client.ApplicationClient;
import com.ecquaria.cloud.moh.iais.service.client.LicenceClient;
import com.ecquaria.cloud.moh.iais.service.client.SystemAdminClient;
import com.ecquaria.cloud.submission.client.wrapper.SubmissionClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sop.webflow.rt.api.Process;

import java.util.List;

/****
 *
 *   @date 12/26/2019
 *   @author zixian
 */
@Service
@Slf4j
public class RequestForChangeServiceImpl implements RequestForChangeService {
    @Autowired
    private LicenceClient licenceClient;

    @Autowired
    private ApplicationClient applicationClient;

    @Autowired
    private SystemParamConfig systemParamConfig;

    @Autowired
    private SubmissionClient client;

    @Autowired
    private SystemAdminClient systemAdminClient;

    @Override
    public List<PremisesListQueryDto> getPremisesList(String licenseeId) {
        List<PremisesListQueryDto> premisesListQueryDtos = licenceClient.getPremises(licenseeId).getEntity();
        return premisesListQueryDtos;
    }

    @Override
    public AppSubmissionDto getAppSubmissionDtoByLicenceId(String licenceId) {
        return licenceClient.getAppSubmissionDto(licenceId).getEntity();
    }

    @Override
    public AppSubmissionDto submitChange(AppSubmissionDto appSubmissionDto, Process process) {
        appSubmissionDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        //save appGrp and app
        appSubmissionDto = applicationClient.saveAppsForRequestForChange(appSubmissionDto).getEntity();
//asynchronous save the other data.
        eventBus(appSubmissionDto, process);
        return appSubmissionDto;
    }

    private  void eventBus(AppSubmissionDto appSubmissionDto, Process process){
        /*//prepare request parameters
        appSubmissionDto.setEventRefNo(appSubmissionDto.getAppGrpNo());
        SubmitReq req = new SubmitReq();
        req.setSubmissionId(applicationClient.getSubmissionId().getEntity());
        req.setProject(process.getCurrentProject());
        req.setProcess(process.getCurrentProcessName());
        req.setStep(process.getCurrentComponentName());
        req.setService(EventBusConsts.SERVICE_NAME_APPSUBMIT);
        req.setOperation(EventBusConsts.OPERATION_NEW_APP_SUBMIT);
        req.setSopUrl("https://" + systemParamConfig.getInterServerName()
                +  "/hcsa-licence-web/eservice/INTERNET/MohNewApplication");
        req.setData(JsonUtil.parseToJson(appSubmissionDto));
        req.setCallbackUrl("https://"
                + systemParamConfig.getInterServerName()
                + "/hcsa-licence-web/eservice/INTERNET/HcsaApplicationEventBusCallBack");
        req.setUserId("SOP");
        req.setWait(false);
        req.addCallbackParam("token", IaisEGPHelper.genTokenForCallback(req.getSubmissionId(), req.getService()));
        //
        SubmitResp submitResp = client.submit(AppConsts.REST_PROTOCOL_TYPE + RestApiUrlConsts.EVENT_BUS, req);*/
    }

    @Override
    public ApplicationDto getApplicationByLicenceId(String licenceId) {
        return applicationClient.getApplicaitonByLicenceId(licenceId).getEntity();
    }
    

    @Override
    public String getApplicationGroupNumber(String appType) {
        return systemAdminClient.applicationNumber(appType).getEntity();
    }


}

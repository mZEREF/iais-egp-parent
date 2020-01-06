package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.common.config.SystemParamConfig;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.EventBusConsts;
import com.ecquaria.cloud.moh.iais.common.constant.rest.RestApiUrlConsts;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.PremisesListQueryDto;
import com.ecquaria.cloud.moh.iais.common.utils.JsonUtil;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.service.RequestForChangeService;
import com.ecquaria.cloud.moh.iais.service.client.AppInboxClient;
import com.ecquaria.cloud.moh.iais.service.client.LicenceInboxClient;
import com.ecquaria.cloud.submission.client.model.SubmitReq;
import com.ecquaria.cloud.submission.client.model.SubmitResp;
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
    private LicenceInboxClient licenceInboxClient;

    @Autowired
    private AppInboxClient appInboxClient;

    @Autowired
    private SystemParamConfig systemParamConfig;

    @Autowired
    private SubmissionClient client;

    @Override
    public List<PremisesListQueryDto> getPremisesList(String licenseeId) {
        List<PremisesListQueryDto> premisesListQueryDtos = licenceInboxClient.getPremises(licenseeId).getEntity();
        return premisesListQueryDtos;
    }

    @Override
    public AppSubmissionDto getAppSubmissionDtoByLicenceId(String licenceId) {
        return licenceInboxClient.getAppSubmissionDto(licenceId).getEntity();
    }

    @Override
    public AppSubmissionDto submitChange(AppSubmissionDto appSubmissionDto, Process process) {
        appSubmissionDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        //save appGrp and app
        appSubmissionDto = appInboxClient.saveAppsForRequestForChange(appSubmissionDto).getEntity();
//asynchronous save the other data.
        eventBus(appSubmissionDto, process);
        return appSubmissionDto;
    }

    private  void eventBus(AppSubmissionDto appSubmissionDto, Process process){
        //prepare request parameters
        appSubmissionDto.setEventRefNo(appSubmissionDto.getAppGrpNo());
        SubmitReq req = new SubmitReq();
        req.setSubmissionId(appInboxClient.getSubmissionId().getEntity());
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
        SubmitResp submitResp = client.submit(AppConsts.REST_PROTOCOL_TYPE + RestApiUrlConsts.EVENT_BUS, req);
    }

}

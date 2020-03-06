package com.ecquaria.cloud.moh.iais.helper;

import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.rest.RestApiUrlConsts;
import com.ecquaria.cloud.moh.iais.common.utils.JsonUtil;
import com.ecquaria.cloud.submission.client.model.SubmitReq;
import com.ecquaria.cloud.submission.client.model.SubmitResp;
import com.ecquaria.cloud.submission.client.wrapper.SubmissionClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import sop.webflow.rt.api.Process;

/**
 * EventBusHelper
 *
 * @author suocheng
 * @date 12/24/2019
 */
@Service
public class EventBusHelper {

    @Value("${iais.eventbus.callbackUrl}")
    private String callBackUrl;
    @Autowired
    private SubmissionClient submissionClient;

    public SubmitResp submitAsyncRequest(Object dto, String submissionId, String service, String operation,
                                         String eventRefNo, Process process) {
        return submitRequest(dto, submissionId, service, operation, eventRefNo, false, 0,
                process);
    }


    public SubmitResp submitSyncRequest(Object dto, String submissionId, String service, String operation,
                        String eventRefNo, int waitTime, Process process){
        return submitRequest(dto, submissionId, service, operation, eventRefNo, true
                , waitTime, process);
    }

    private SubmitResp submitRequest(Object dto, String submissionId, String service, String operation,
                                     String eventRefNo, boolean wait, int waitTime, Process process) {
        SubmitReq req = new SubmitReq();
        req.setSubmissionId(submissionId);
        req.setProject(process.getCurrentProject());
        req.setProcess(process.getCurrentProcessName());
        req.setStep("eventBusSubmit");
        req.setCallbackUrl(callBackUrl);
        req.setService(service);
        req.setOperation(operation);
        req.setSopUrl(process.continueURL());
        req.setData(JsonUtil.parseToJson(dto));
        req.setUserId("iaisSubmission");
        req.setWait(wait);
        if (wait) {
            req.setTotalWait(waitTime);
        }
        req.addCallbackParam("token", IaisEGPHelper.genTokenForCallback(req.getSubmissionId(), req.getService()));
        req.addCallbackParam("eventRefNo", eventRefNo);

        return  submissionClient.submit(AppConsts.REST_PROTOCOL_TYPE + RestApiUrlConsts.EVENT_BUS, req);
    }
}

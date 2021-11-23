package com.ecquaria.cloud.moh.iais.helper;

import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.rest.RestApiUrlConsts;
import com.ecquaria.cloud.moh.iais.common.utils.JsonUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.submission.client.model.SubmitReq;
import com.ecquaria.cloud.submission.client.model.SubmitResp;
import com.ecquaria.cloud.submission.client.wrapper.SubmissionClient;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class EventBusHelper {

    @Value("${iais.eventbus.callbackUrl}")
    private String callBackUrl;
    @Autowired
    private SubmissionClient submissionClient;

    public SubmitResp submitAsyncRequest(Object dto, String submissionId, String service, String operation,
                                         String eventRefNo, Process process) {
        return submitRequest(dto, submissionId, service, operation, eventRefNo, false, 0,
                process, true);
    }

    public SubmitResp submitAsyncRequestWithoutCallback(Object dto, String submissionId, String service, String operation,
                                         String eventRefNo, Process process) {
        return submitRequest(dto, submissionId, service, operation, eventRefNo, false, 0,
                process, false);
    }

    public SubmitResp submitSyncRequest(Object dto, String submissionId, String service, String operation,
                        String eventRefNo, int waitTime, Process process){
        return submitRequest(dto, submissionId, service, operation, eventRefNo, true
                , waitTime, process, true);
    }

    public SubmitResp submitAsyncRequest(Object dto, String submissionId, String service, String operation,
                                         String eventRefNo, String projectName, String processName) {
        return submitRequest(dto, submissionId, service, operation, eventRefNo, false, 0,
                projectName, processName, callBackUrl, true);
    }

    private SubmitResp submitRequest(Object dto, String submissionId, String service, String operation,
                                     String eventRefNo, boolean wait, int waitTime, Process process, boolean needCallback) {
        String projectName = "IAIS";
        String processName = "Batchjob";
        String sopUrl = callBackUrl;
        if (process != null) {
            projectName = process.getCurrentProject();
            processName = process.getCurrentProcessName();
            sopUrl = process.continueURL();
        }

        return submitRequest(dto, submissionId, service, operation, eventRefNo, wait, waitTime, projectName,
                processName, sopUrl, needCallback);
    }

    private SubmitResp submitRequest(Object dto, String submissionId, String service, String operation,
                                 String eventRefNo, boolean wait, int waitTime,
                                 String projectName, String processName, String sopUrl, boolean needCallback) {
        log.info("<=== Start to call Event bus ===>");
        log.info(StringUtil.changeForLog("Call Submission Id ==>" + submissionId));
        log.info(StringUtil.changeForLog("Call Service ==>" + service));
        log.info(StringUtil.changeForLog("Call Operation ==>" + operation));
        SubmitReq req = new SubmitReq();
        req.setSubmissionId(submissionId);
        req.setProject(projectName);
        req.setProcess(processName);
        req.setSopUrl(sopUrl);
        req.setStep("eventBusSubmit");
        if (needCallback) {
            req.setCallbackUrl(callBackUrl);
        }
        req.setService(service);
        req.setOperation(operation);
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

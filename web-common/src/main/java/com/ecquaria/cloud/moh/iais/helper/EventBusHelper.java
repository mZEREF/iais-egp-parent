package com.ecquaria.cloud.moh.iais.helper;

import com.ecquaria.cloud.moh.iais.common.utils.JsonUtil;
import com.ecquaria.cloud.submission.client.model.SubmitReq;
import sop.webflow.rt.api.Process;

/**
 * EventBusHelper
 *
 * @author suocheng
 * @date 12/24/2019
 */
public class EventBusHelper {



    public static String getEventRefNo(){
     return  String.valueOf(System.currentTimeMillis());
    }

    public static  SubmitReq getSubmitReq(Object dto,String submissionId,String service,String operation,String sopUrl,String callBackUrl,
                                   String userId,boolean wait,Process process){
        SubmitReq req = new SubmitReq();
        req.setSubmissionId(submissionId);
        req.setProject(process.getCurrentProject());
        req.setProcess(process.getCurrentProcessName());
        req.setStep(process.getCurrentComponentName());
        req.setService(service);
        req.setOperation(operation);
        req.setSopUrl(sopUrl);
        req.setData(JsonUtil.parseToJson(dto));
        req.setCallbackUrl("https://"
                +process.getHttpRequest().getServerName()
                +process.getHttpRequest().getContextPath()
                +callBackUrl);
        req.setUserId(userId);
        req.setWait(wait);
        req.addCallbackParam("token", IaisEGPHelper.genTokenForCallback(req.getSubmissionId(), req.getService()));
        return  req;
    }

    private EventBusHelper() {
        throw new IllegalStateException("Utility class");
    }
}

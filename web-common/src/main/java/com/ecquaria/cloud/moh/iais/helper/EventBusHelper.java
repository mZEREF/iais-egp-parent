package com.ecquaria.cloud.moh.iais.helper;

import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.utils.JsonUtil;
import com.ecquaria.cloud.submission.client.model.SubmitReq;

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

    public static SubmitReq getSubmitReq(Object dto,String submissionId,String service,String operation,String sopUrl,String callBackUrl,
                                   String userId,boolean wait,String project,String process,String step){
        SubmitReq req = new SubmitReq();
        req.setSubmissionId(submissionId);
        req.setProject(project);
        req.setProcess(process);
        req.setStep(step);
        req.setCallbackUrl(AppConsts.REQUEST_TYPE_HTTPS + callBackUrl);
        req.setService(service);
        req.setOperation(operation);
        req.setSopUrl(AppConsts.REQUEST_TYPE_HTTPS + sopUrl);
        req.setData(JsonUtil.parseToJson(dto));

        req.setUserId(userId);
        req.setWait(wait);
        if (wait) {
            req.setTotalWait(3000);
        }
        req.addCallbackParam("token", IaisEGPHelper.genTokenForCallback(req.getSubmissionId(), req.getService()));
        return  req;
    }

    private EventBusHelper() {
        throw new IllegalStateException("Utility class");
    }
}

package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.rest.RestApiUrlConsts;
import com.ecquaria.cloud.moh.iais.common.exception.IaisRuntimeException;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.kafka.GlobalConstants;
import com.ecquaria.submission.client.model.ServiceStatus;
import com.ecquaria.submission.client.wrapper.SubmissionClient;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import sop.webflow.rt.api.BaseProcessClass;
/**
 * EventBusCallBackDelegator
 *
 * @author suocheng
 * @date 12/3/2019
 */
@Delegator(value = "eventBusCallBackDelegator")
@Slf4j
public class EventBusCallBackDelegator {
    /**
     * StartStep: Start
     *
     * @param bpc
     * @throws
     */
    public void callBack(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        String submissionId = ParamUtil.getString(request,"submissionId");
        String token = ParamUtil.getString(request, "token");
        String serviceName = ParamUtil.getString(request, "service");
        boolean isLeagal = IaisEGPHelper.verifyCallBackToken(submissionId, serviceName, token);
        if (!isLeagal) {
            throw new IaisRuntimeException("Visit without Token!!");
        }
        SubmissionClient client = new SubmissionClient();
        String operation = ParamUtil.getString(request, "operation");
        Map<String, List<ServiceStatus>> map = client.getSubmissionStatus(AppConsts.REST_PROTOCOL_TYPE
                        + RestApiUrlConsts.EVENT_BUS, submissionId, operation);
        if (map.size() == 1) {
            boolean completed = true;
            boolean success = true;
            for (Map.Entry<String, List<ServiceStatus>> ent : map.entrySet()) {
                 for (ServiceStatus status : ent.getValue()) {
                     if (!status.getStatus().equals(GlobalConstants.STATE_COMPLETED)) {
                         completed = false;
                     }
                     if (!status.getStatus().equals(GlobalConstants.STATE_COMPLETED)
                            && !status.getStatus().equals(GlobalConstants.STATUS_SUCCESS)) {
                         success = false;
                     }
                 }
                 if (!completed && !success) {
                     break;
                 }
            }
            if (!success) {
                client.setCompensation(AppConsts.REST_PROTOCOL_TYPE + RestApiUrlConsts.EVENT_BUS,
                        submissionId, operation, "");
            }
        }

    }
}

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
//
//        Long submissionId = (Long) request.getAttribute("submissionId");
//        String operation = (String) request.getAttribute("operation");
//
//        Map submissionStatusMap = wrapper.getSubmissionStatus(submissionId.longValue(), operation);
//
//        if(submissionStatusMap.size()==5) {
//            Iterator it = submissionStatusMap.keySet().iterator();
//            String svc = null;
//            ServiceStatus serviceStatus = null;
//            String svcStatus;
//            String errorCode = null;
//            String errorMsg = null;
//            boolean completed = true;
//            boolean success = true;
//
//            while (it.hasNext()) {
//                svc  = (String) it.next();
//                serviceStatus = (ServiceStatus) submissionStatusMap.get(svc);
//                svcStatus = (serviceStatus.getStatus());
//                if (!svcStatus.startsWith("Comp.")) {
//                    completed = false;
//                    break;
//                }
//
//                if (!svcStatus.endsWith("Success")) {
//                    success = false;
//                    errorCode = serviceStatus.getErrorCode();
//                    errorMsg = serviceStatus.getErrorMsg();
//                    break;
//                }
//            }
//
//            if (!completed) {
//                request.setAttribute("ApplicationStatus", "Pending");
//                request.setAttribute("waitCompletionStatus","Pending");
//            } else if (!success) {
//                request.setAttribute("ApplicationStatus", "Error");
//                request.setAttribute("ErrorMsg",errorMsg);
//                wrapper.setCompensation(submissionId.longValue(),operation, "Error (" + svc + ")");
//                request.setAttribute("waitCompletionStatus","Error");
//            } else {
//                request.setAttribute("ApplicationStatus","Success");
//                request.setAttribute("waitCompletionStatus","Success");
//            }
//        } else {
//            request.setAttribute("ApplicationStatus","Incomplete");
//            request.setAttribute("waitCompletionStatus","Incomplete");
//        }
    }
}

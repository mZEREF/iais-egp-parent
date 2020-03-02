package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.rest.RestApiUrlConsts;
import com.ecquaria.cloud.moh.iais.common.exception.IaisRuntimeException;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.submission.client.model.ServiceStatus;
import com.ecquaria.cloud.submission.client.wrapper.SubmissionClient;
import com.ecquaria.kafka.GlobalConstants;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sop.webflow.rt.api.BaseProcessClass;

/**
 * @author Shicheng
 * @date 2019/12/31 17:19
 **/
@Delegator("inspecSaveRecRollBackDelegator")
@Slf4j
public class InspecSaveRecRollBackDelegator {
    @Autowired
    private SubmissionClient client;

    /**
     * StartStep: inspecSaveRecRollBackStart
     *
     * @param bpc
     * @throws
     */
    public void inspecSaveRecRollBackStart(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        String submissionId = ParamUtil.getString(request,"submissionId");
        log.info("Submission Id ==> " + submissionId);
        String token = ParamUtil.getString(request, "token");
        String serviceName = ParamUtil.getString(request, "service");
        boolean isLeagal = IaisEGPHelper.verifyCallBackToken(submissionId, serviceName, token);
        if (!isLeagal) {
            throw new IaisRuntimeException("Visit without Token!!");
        }
        String operation = ParamUtil.getString(request, "operation");
        Map<String, List<ServiceStatus>> map = client.getSubmissionStatus(AppConsts.REST_PROTOCOL_TYPE
                + RestApiUrlConsts.EVENT_BUS, submissionId, operation);
        if (map.size() >= 1) {
            log.info("Got records from DB");
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
                client.submitCompensation(AppConsts.REST_PROTOCOL_TYPE + RestApiUrlConsts.EVENT_BUS,
                        submissionId, serviceName ,operation);
            }
        }
        log.info("Complete");
    }

    /**
     * StartStep: inspecSaveRecRollBackAction
     *
     * @param bpc
     * @throws
     */
    public void inspecSaveRecRollBackAction(BaseProcessClass bpc){
        logAbout("inspecSaveRecRollBackAction");

    }

    private void logAbout(String methodName){
        log.debug(StringUtil.changeForLog("****The***** " + methodName +" ******Start ****"));
    }
}

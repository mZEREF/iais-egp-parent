package com.ecquaria.cloud.moh.iais.batchjob;

import com.ecquaria.cloud.job.executor.biz.model.ReturnT;
import com.ecquaria.cloud.job.executor.handler.annotation.JobHandler;
import com.ecquaria.cloud.job.executor.log.JobLogger;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.rest.RestApiUrlConsts;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.service.client.EventBusClient;
import com.ecquaria.cloud.submission.client.model.SubmitReq;
import com.ecquaria.cloud.submission.client.wrapper.SubmissionClient;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * FeEventbusTraceRecoveryJobHandler
 *
 * @author Jinhua
 * @date 2021/11/17 15:29
 */
@Component
@JobHandler(value="feEventbusTraceCompJobHandler")
@Slf4j
public class FeEventbusTraceRecoveryJobHandler extends MohJobHandler {
    @Autowired
    private EventBusClient eventBusClient;
    @Autowired
    private SubmissionClient submissionClient;

    @Override
    public ReturnT<String> doExecute(String str) throws Exception {
        try {
            AuditTrailHelper.setupBatchJobAuditTrail(this);
            List<SubmitReq> list = eventBusClient.getRequestToRecover().getEntity();
            if (list != null && !list.isEmpty()) {
                for (SubmitReq sr : list) {
                    try {
                        sr.setUserId("iaisSubmission");
                        sr.setWait(false);
                        sr.addCallbackParam("token", IaisEGPHelper.genTokenForCallback(sr.getSubmissionId(), sr.getService()));
                        sr.addCallbackParam("eventRefNo", String.valueOf(System.currentTimeMillis()));
                        submissionClient.submit(AppConsts.REST_PROTOCOL_TYPE + RestApiUrlConsts.EVENT_BUS, sr);
                    } catch (Throwable th) {
                        log.error("Error when recover eventbus", th);
                    }
                }
            }
        } catch (Throwable th) {
            log.error(th.getMessage(), th);
            JobLogger.log(th);
            return ReturnT.FAIL;
        }

        return ReturnT.SUCCESS;
    }
}

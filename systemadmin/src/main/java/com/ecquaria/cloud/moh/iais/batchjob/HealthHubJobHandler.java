package com.ecquaria.cloud.moh.iais.batchjob;

import com.ecquaria.cloud.job.executor.biz.model.ReturnT;
import com.ecquaria.cloud.job.executor.handler.IJobHandler;
import com.ecquaria.cloud.job.executor.handler.annotation.JobHandler;
import com.ecquaria.cloud.job.executor.log.JobLogger;
import com.ecquaria.cloud.moh.iais.service.client.HalpReportServerClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author yichen
 * @Date:2021/4/16
 */

@JobHandler(value = "healthHubJobHandler")
@Component
@Slf4j
public class HealthHubJobHandler extends IJobHandler {

    @Autowired
    private HalpReportServerClient reportServerClient;

    @Override
    public ReturnT<String> execute(String s) throws Exception {
        try {
            log.info("HealthHubJobHandler Start ........... ");
            log.info("HealthHubJobHandler .Create healthHub Data.START. ");
            reportServerClient.receive();
            log.info("HealthHubJobHandler .Create healthHub Data.END. ");
            return ReturnT.SUCCESS;
        } catch (Throwable e) {
            log.error(e.getMessage(), e);
            JobLogger.log(e);
            return ReturnT.FAIL;
        }
    }
}

package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.job.executor.biz.model.ReturnT;
import com.ecquaria.cloud.job.executor.handler.IJobHandler;
import com.ecquaria.cloud.job.executor.handler.annotation.JobHandler;
import com.ecquaria.cloud.job.executor.log.JobLogger;
import com.ecquaria.cloud.moh.iais.service.client.AppEicClient;
import com.ecquaria.cloud.moh.iais.service.client.AtEicClient;
import com.ecquaria.cloud.moh.iais.service.client.EicClient;
import com.ecquaria.cloud.moh.iais.service.client.LicEicClient;
import com.ecquaria.cloud.moh.iais.service.client.LicmEicClient;
import com.ecquaria.cloud.moh.iais.service.client.OrgEicClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * EicTrackingClearJobHandler
 *
 * @author Jinhua
 * @date 2022/1/27 17:19
 */
@JobHandler(value="eicTrackingClearJobHandler")
@Component
@Slf4j
public class EicTrackingClearJobHandler extends IJobHandler {
    @Autowired
    private AtEicClient atEicClient;
    @Autowired
    private AppEicClient appEicClient;
    @Autowired
    private LicEicClient licEicClient;
    @Autowired
    private LicmEicClient licmEicClient;
    @Autowired
    private EicClient eicClient;
    @Autowired
    private OrgEicClient orgEicClient;

    @Override
    public ReturnT<String> execute(String s) {
        try {
            log.info("<======== Start EIC Tracking Clear Job =========>");
            JobLogger.log("<======== Start EIC Tracking Clear Job =========>");
            atEicClient.deleteOldEicRequestTracking();
            appEicClient.deleteOldEicRequestTracking();
            licEicClient.deleteOldEicRequestTracking();
            licmEicClient.deleteOldEicRequestTracking();
            orgEicClient.deleteOldEicRequestTracking();
            eicClient.deleteOldEicRequestTracking();
            log.info("<======== End EIC Tracking Clear Job =========>");
            JobLogger.log("<======== End EIC Tracking Clear Job =========>");
            return ReturnT.SUCCESS;
        } catch (Exception e) {
            log.error(e.getMessage());
            JobLogger.log(e);
            return ReturnT.FAIL;
        }
    }
}

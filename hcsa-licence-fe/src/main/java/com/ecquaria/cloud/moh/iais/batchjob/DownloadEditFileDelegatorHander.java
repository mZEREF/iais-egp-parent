package com.ecquaria.cloud.moh.iais.batchjob;

import com.ecquaria.cloud.job.executor.biz.model.ReturnT;
import com.ecquaria.cloud.job.executor.handler.IJobHandler;
import com.ecquaria.cloud.job.executor.handler.annotation.JobHandler;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Wenkang
 * @date 2020/9/23 14:51
 */
@JobHandler(value="downloadEditFileDelegatorHander")
@Component
@Slf4j
public class DownloadEditFileDelegatorHander extends IJobHandler {
    @Autowired
    private DownloadEditFileDelegator licenceFileDownloadDelegator;


    @Override
    public ReturnT<String> execute(String s) throws Exception {
        log.debug(StringUtil.changeForLog("The licenceFileDownloadDelegatorHander is Start ..."));
        AuditTrailHelper.setupBatchJobAuditTrail(this);
        try {
            licenceFileDownloadDelegator.jobStart();
        }catch (Exception e){
            return ReturnT.FAIL;
        }
        return ReturnT.SUCCESS;
    }
}

package com.ecquaria.cloud.moh.iais.batchjob;

import com.ecquaria.cloud.job.executor.biz.model.ReturnT;
import com.ecquaria.cloud.job.executor.handler.annotation.JobHandler;
import com.ecquaria.cloud.job.executor.log.JobLogger;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author Shicheng
 * @date 2020/9/25 9:45
 **/
@JobHandler(value="syncServiceByEndJobHandler")
@Component
@Slf4j
public class SyncServiceByEndJobHandler extends MohJobHandler {
    @Override
    public ReturnT<String> doExecute(String var1) {
        try {
            logAbout("SyncServiceByEndJobHandler");

            return ReturnT.SUCCESS;
        } catch (Throwable e) {
            log.error(e.getMessage(), e);
            JobLogger.log(e);
            return ReturnT.FAIL;
        }
    }

    private void logAbout(String methodName){
        log.info(StringUtil.changeForLog("****The****" + methodName + "*****Start****"));
        JobLogger.log(StringUtil.changeForLog("****The****" + methodName + "*****Start****"));
    }
}

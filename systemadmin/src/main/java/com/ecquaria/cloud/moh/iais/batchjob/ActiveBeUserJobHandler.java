package com.ecquaria.cloud.moh.iais.batchjob;

import com.ecquaria.cloud.job.executor.biz.model.ReturnT;
import com.ecquaria.cloud.job.executor.handler.IJobHandler;
import com.ecquaria.cloud.job.executor.handler.annotation.JobHandler;
import com.ecquaria.cloud.job.executor.log.JobLogger;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Author weilu
 * @Date 2021/1/15 17:04
 */
@JobHandler(value = "activeBeUser")
@Component
@Slf4j
public class ActiveBeUserJobHandler extends IJobHandler {

    @Autowired
    private ActiveBeUserBatchJob activeBeUserBatchJob;

    @Override
    public ReturnT<String> execute(String s) {

        try {
            activeBeUserBatchJob.jobExecute();
            return ReturnT.SUCCESS;
        } catch (Throwable e) {
            log.error(e.getMessage(), e);
            JobLogger.log(e);
            return ReturnT.FAIL;
        }
    }
}

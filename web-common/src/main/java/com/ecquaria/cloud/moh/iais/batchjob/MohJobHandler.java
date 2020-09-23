package com.ecquaria.cloud.moh.iais.batchjob;

import com.ecquaria.cloud.job.executor.biz.model.ReturnT;
import com.ecquaria.cloud.job.executor.handler.IJobHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * MohJobHandler
 *
 * @author suocheng
 * @date 9/23/2020
 */
@Slf4j
public abstract class MohJobHandler extends IJobHandler {
    @Override
    public ReturnT<String> execute(String s) throws Exception {
//        try {
//            return  doExecute(s);
//        } catch (Throwable e) {
//            log.error(e.getMessage(), e);
//            JobLogger.log(e);
//            return ReturnT.FAIL;
//        }
        return  doExecute(s);
    }
    public abstract ReturnT<String> doExecute(String var1) throws Exception;
}

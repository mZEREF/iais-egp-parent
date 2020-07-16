package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.job.executor.biz.model.ReturnT;
import com.ecquaria.cloud.job.executor.handler.IJobHandler;
import com.ecquaria.cloud.job.executor.handler.annotation.JobHandler;
import com.ecquaria.cloud.job.executor.log.JobLogger;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.service.FeToBeRecFileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Batch Job
 * @Process MohInspecUserRecFeToBe
 * @author Shicheng
 * @date 2019/12/26 10:37
 **/
@JobHandler(value="inspecUserRecFeToBeJobHandler")
@Component
@Slf4j
public class InspecUserRecFeToBeJobHandler extends IJobHandler {

    @Autowired
    private FeToBeRecFileService feToBeRecFileService;

    @Override
    public ReturnT<String> execute(String s) throws Exception {
        try {
            logAbout("inspecUserRecFeToBePre");
            Map<String, String> appItemMap = feToBeRecFileService.getDocFile();
            feToBeRecFileService.compressFile(appItemMap);
            return ReturnT.SUCCESS;
        } catch (Throwable e) {
            log.error(e.getMessage(), e);
            JobLogger.log(e);
            return ReturnT.FAIL;
        }
    }

    private void logAbout(String methodName){
        log.info(StringUtil.changeForLog("****The****" + methodName +" *****Start****"));
        JobLogger.log(StringUtil.changeForLog("****The****" + methodName +" *****Start****"));
    }
}

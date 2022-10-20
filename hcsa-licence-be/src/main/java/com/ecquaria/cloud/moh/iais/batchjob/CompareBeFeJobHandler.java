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
 * ConsolRecToCompareBeJobHandler
 *
 * @author junyu
 * @date 2022/5/20
 */
@JobHandler(value="CompareFEBE")
@Component
@Slf4j
public class CompareBeFeJobHandler extends IJobHandler {
    @Autowired
    private CompareBeFeDelegator compareBeFeDelegator;


    @Override
    public ReturnT<String> execute(String s) throws Exception {
        log.debug(StringUtil.changeForLog("The compareBeFeJobHandler is Start ..."));
        AuditTrailHelper.setupBatchJobAuditTrail(this);
        try {
            compareBeFeDelegator.jobStart();
        }catch (Exception e){
            return ReturnT.FAIL;
        }
        return ReturnT.SUCCESS;
    }
}

package com.ecquaria.cloud.moh.iais.batchjob;

import com.ecquaria.cloud.job.executor.biz.model.ReturnT;
import com.ecquaria.cloud.job.executor.handler.IJobHandler;
import com.ecquaria.cloud.job.executor.handler.annotation.JobHandler;
import com.ecquaria.cloud.job.executor.log.JobLogger;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Author weilu
 * @Date 2020/9/30 13:24
 */
@JobHandler(value="PrepareCessation")
@Component
@Slf4j
public class PrepareCessationJobHandler extends IJobHandler {
    @Autowired
    PrepareCessation prepareCessation;
    @Override
    public ReturnT<String> execute(String s) throws Exception {
        try {
            log.debug(StringUtil.changeForLog("The prepareCessation is Start ..."));
            AuditTrailHelper.setupBatchJobAuditTrail(AppConsts.DOMAIN_INTRANET, this);
            prepareCessation.jobExecute();
        } catch (Throwable e) {
            log.error(e.getMessage(), e);
            JobLogger.log(e);
            return ReturnT.FAIL;
        }
        return ReturnT.SUCCESS;
    }
    }

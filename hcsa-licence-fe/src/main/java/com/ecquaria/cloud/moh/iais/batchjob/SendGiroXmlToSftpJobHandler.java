package com.ecquaria.cloud.moh.iais.batchjob;


import com.ecquaria.cloud.job.executor.biz.model.ReturnT;
import com.ecquaria.cloud.job.executor.handler.IJobHandler;
import com.ecquaria.cloud.job.executor.handler.annotation.JobHandler;
import com.ecquaria.cloud.job.executor.log.JobLogger;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;

import com.ecquaria.cloud.moh.iais.service.ServiceConfigService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@JobHandler(value = "SendGiroXmlToSftpJobHandler")
@Component
@Slf4j
public class SendGiroXmlToSftpJobHandler extends IJobHandler {
    @Autowired
    private   ServiceConfigService serviceConfigService;

    @Override
    public ReturnT<String> execute(String s){
        try {
            JobLogger.log(StringUtil.changeForLog("SendGiroXmlToSftpJobHandler start ..."));
            log.info("-----SendGiroXmlToSftpJobHandler start ------");
            AuditTrailHelper.setupBatchJobAuditTrail(this);
            serviceConfigService.sendGiroXmlToSftp();
            JobLogger.log(StringUtil.changeForLog("SendGiroXmlToSftpJobHandler end ..."));
            log.info("-----SendGiroXmlToSftpJobHandler end ------");
            return ReturnT.SUCCESS;
        }catch (Throwable e){
            log.error(e.getMessage(), e);
            JobLogger.log(e);
            return ReturnT.FAIL;
        }
    }
}

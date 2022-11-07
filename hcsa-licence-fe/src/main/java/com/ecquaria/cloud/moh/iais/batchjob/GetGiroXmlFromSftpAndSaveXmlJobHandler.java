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

@JobHandler(value = "GetGiroXmlFromSftpAndSaveXmlJobHandler")
@Component
@Slf4j
public class GetGiroXmlFromSftpAndSaveXmlJobHandler extends IJobHandler {
    @Autowired
    private   ServiceConfigService serviceConfigService;

    @Override
    public ReturnT<String> execute(String s){
        try {
            JobLogger.log(StringUtil.changeForLog("GetGiroXmlFromSftpAndSaveXmlJobHandler start ..."));
            log.info("------ GetGiroXmlFromSftpAndSaveXmlJobHandler start----");
            AuditTrailHelper.setupBatchJobAuditTrail(this);
            serviceConfigService.sendGiroXmlToOtherServer();
            serviceConfigService.getGiroXmlFromSftpAndSaveXml();
            JobLogger.log(StringUtil.changeForLog("GetGiroXmlFromSftpAndSaveXmlJobHandler end ..."));
            log.info("------ GetGiroXmlFromSftpAndSaveXmlJobHandler end----");
            return ReturnT.SUCCESS;
        }catch (Throwable e){
            log.error(e.getMessage(), e);
            JobLogger.log(e);
            return ReturnT.FAIL;
        }
    }
}

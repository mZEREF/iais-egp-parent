package com.ecquaria.cloud.moh.iais.batchjob;

import com.ecquaria.cloud.job.executor.biz.model.ReturnT;
import com.ecquaria.cloud.job.executor.handler.IJobHandler;
import com.ecquaria.cloud.job.executor.handler.annotation.JobHandler;
import com.ecquaria.cloud.job.executor.log.JobLogger;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.service.datasubmission.VssUploadFileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Process VssFileJobHandler
 *
 * @author fanghao
 * @date 2022/1/28
 **/
@JobHandler(value="VssFileJobHandler")
@Component
@Slf4j
public class VssFileJobHandler extends IJobHandler {

    @Autowired
    private VssUploadFileService vssUploadFileService;

   @Override
    public ReturnT<String> execute(String s) {
        logAbout("VssFileJobHandler");
        try{
            AuditTrailHelper.setupBatchJobAuditTrail(this);
            vssUploadFileService.vssFile();
        }catch (Exception e){
            log.error(e.getMessage(), e);
            JobLogger.log(e);
            return ReturnT.FAIL;
        }
        logAbout("VssFileJobHandler end ");
        return ReturnT.SUCCESS;
    }
    private void logAbout(String methodName){
        log.info(StringUtil.changeForLog("****The***** " + methodName +" ******Start ****"));
        JobLogger.log(StringUtil.changeForLog("****The***** " + methodName +" ******Start ****"));
    }
}

package com.ecquaria.cloud.moh.iais.batchjob;

import com.ecquaria.cloud.job.executor.biz.model.ReturnT;
import com.ecquaria.cloud.job.executor.handler.IJobHandler;
import com.ecquaria.cloud.job.executor.handler.annotation.JobHandler;
import com.ecquaria.cloud.job.executor.log.JobLogger;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.dto.AuditTrailDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationGroupDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.service.datasubmission.VssFileDownloadService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Process VssFileDownloadJobHandler
 *
 * @author fanghao
 * @date 2022/1/26
 **/
@JobHandler(value="vssFileDownloadJobHandler")
@Component
@Slf4j
public class VssFileDownloadJobHandler extends IJobHandler {

    @Autowired
    private VssFileDownloadService vssFileDownloadService;

    @Override
    public ReturnT<String> execute(String s) {
        logAbout("vssFileDownloadJobHandler");
        try{
            AuditTrailHelper.setupBatchJobAuditTrail(this);
            vssFileDownloadService.initPath();
            vssFileDownloadService.decompression();
        }catch (Exception e){
            log.error(e.getMessage(), e);
            JobLogger.log(e);
            return ReturnT.FAIL;
        }
        logAbout("vssFileDownloadJobHandler end ");
        return ReturnT.SUCCESS;
    }
    private void logAbout(String methodName){
        log.info(StringUtil.changeForLog("****The***** " + methodName +" ******Start ****"));
        JobLogger.log(StringUtil.changeForLog("****The***** " + methodName +" ******Start ****"));
    }
}

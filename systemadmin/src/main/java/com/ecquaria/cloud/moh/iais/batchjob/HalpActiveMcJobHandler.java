package com.ecquaria.cloud.moh.iais.batchjob;

import com.ecquaria.cloud.job.executor.biz.model.ReturnT;
import com.ecquaria.cloud.job.executor.handler.IJobHandler;
import com.ecquaria.cloud.job.executor.handler.annotation.JobHandler;
import com.ecquaria.cloud.job.executor.log.JobLogger;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.dto.AuditTrailDto;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.service.MasterCodeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@JobHandler(value="HalpActiveMcJobHandler")
@Component
@Slf4j
public class HalpActiveMcJobHandler extends IJobHandler {

    @Autowired
    MasterCodeService masterCodeService;

    @Override
    public ReturnT<String> execute(String s) throws Exception {
        JobLogger.log(StringUtil.changeForLog("The InactiveMasterCodeJobHandler start..." ));
        try {
            AuditTrailDto auditTrailDto = AuditTrailHelper.getBatchJobAuditTrail(AppConsts.DOMAIN_INTRANET);
            masterCodeService.activeMasterCode(auditTrailDto);
            JobLogger.log(StringUtil.changeForLog("The InactiveMasterCodeJobHandler end..." ));
            return ReturnT.SUCCESS;
        }catch (Throwable e){
            log.error(e.getMessage(), e);
            JobLogger.log(e);
            return ReturnT.FAIL;
        }
    }
}

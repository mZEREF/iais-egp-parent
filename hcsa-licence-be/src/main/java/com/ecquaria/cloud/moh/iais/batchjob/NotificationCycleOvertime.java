package com.ecquaria.cloud.moh.iais.batchjob;


import com.ecquaria.cloud.job.executor.biz.model.ReturnT;
import com.ecquaria.cloud.job.executor.handler.IJobHandler;
import com.ecquaria.cloud.job.executor.handler.annotation.JobHandler;
import com.ecquaria.cloud.job.executor.log.JobLogger;
import com.ecquaria.cloud.moh.iais.common.dto.application.AppReturnFeeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.CycleDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.CycleStageSelectionDto;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.service.datasubmission.impl.NotificationCycleOvertimeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@JobHandler(value="notificationCycleOvertime")
@Component
@Slf4j
public class NotificationCycleOvertime extends IJobHandler {
    @Autowired
    private NotificationCycleOvertimeService service;
    @Override
    public ReturnT<String> execute(String s) throws Exception {
        try {
            AuditTrailHelper.setupBatchJobAuditTrail(this);
            service.firstNotification();
            service.repeatNotification();
        }catch (Exception e){
            log.error(e.getMessage(), e);
            JobLogger.log(e);
            return ReturnT.FAIL;
        }
        return ReturnT.SUCCESS;
    }
}

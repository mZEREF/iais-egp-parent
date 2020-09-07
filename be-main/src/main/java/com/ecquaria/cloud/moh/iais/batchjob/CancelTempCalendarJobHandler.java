package com.ecquaria.cloud.moh.iais.batchjob;

import com.ecquaria.cloud.job.executor.biz.model.ReturnT;
import com.ecquaria.cloud.job.executor.handler.IJobHandler;
import com.ecquaria.cloud.job.executor.handler.annotation.JobHandler;
import com.ecquaria.cloud.job.executor.log.JobLogger;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.service.client.AppointmentBeMainClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * CancelExpiredApptBatchjob
 *
 * @author Jinhua
 * @date 2020/9/4 17:18
 */
@JobHandler(value="cancelTempCalendarJobHandler")
@Component
@Slf4j
public class CancelTempCalendarJobHandler extends IJobHandler {
    @Autowired
    private AppointmentBeMainClient appointmentBeMainClient;

    @Override
    public ReturnT<String> execute(String s) {
        try{
            logInfo("<====== Start to cancel temp calendar ======>");
            appointmentBeMainClient.cancelTemp(2);
            logInfo("<====== End to cancel temp calendar ======>");
        }catch (Exception e){
            log.error(e.getMessage(), e);
            JobLogger.log(e);
            return ReturnT.FAIL;
        }

        return ReturnT.SUCCESS;
    }

    private void logInfo(String info) {
        log.info(StringUtil.changeForLog(info));
        JobLogger.log(StringUtil.changeForLog(info));
    }
}

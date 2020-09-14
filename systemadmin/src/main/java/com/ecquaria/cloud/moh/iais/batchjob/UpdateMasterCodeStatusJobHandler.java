package com.ecquaria.cloud.moh.iais.batchjob;

import com.ecquaria.cloud.job.executor.biz.model.ReturnT;
import com.ecquaria.cloud.job.executor.handler.IJobHandler;
import com.ecquaria.cloud.job.executor.handler.annotation.JobHandler;
import com.ecquaria.cloud.job.executor.log.JobLogger;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.service.MasterCodeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@JobHandler(value="UpdateMasterCodeStatusJobHandler")
@Component
@Slf4j

public class UpdateMasterCodeStatusJobHandler extends IJobHandler {
    @Autowired
    MasterCodeService masterCodeService;


//    public void updateStatus(BaseProcessClass bpc){
//        log.info(StringUtil.changeForLog("inactive master code"));
//        masterCodeService.inactiveMasterCode();
//    }


    @Override
    public ReturnT<String> execute(String s) throws Exception {
        JobLogger.log(StringUtil.changeForLog("The InactiveMasterCodeJobHandler start..." ));
        try {
            masterCodeService.inactiveMasterCode();
            JobLogger.log(StringUtil.changeForLog("The InactiveMasterCodeJobHandler end..." ));
            return ReturnT.SUCCESS;
        }catch (Throwable e){
            log.error(e.getMessage(), e);
            JobLogger.log(e);
            return ReturnT.FAIL;
        }
    }
}

package com.ecquaria.cloud.moh.iais.job;


import com.ecquaria.cloud.job.executor.biz.model.ReturnT;
import com.ecquaria.cloud.job.executor.handler.IJobHandler;
import com.ecquaria.cloud.job.executor.handler.annotation.JobHandler;
import com.ecquaria.cloud.job.executor.log.JobLogger;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.helper.MasterCodeUtil;
import com.ecquaria.cloud.moh.iais.service.AppSubmissionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@JobHandler(value = "DeleteAppDraftJobHandler")
@Component
@Slf4j
public class DeleteAppDraftJobHandler extends IJobHandler {
    @Autowired
    AppSubmissionService appSubmissionService;

    @Override
    public ReturnT<String> execute(String s) throws Exception {
        try {
            JobLogger.log(StringUtil.changeForLog("delete app draft job start ..."));
            String draftValidity = MasterCodeUtil.getCodeDesc("MS006");
            JobLogger.log(StringUtil.changeForLog("draft validity:"+draftValidity));
            if(!StringUtil.isEmpty(draftValidity)){
                appSubmissionService.deleteOverdueDraft(draftValidity);
            }
            JobLogger.log(StringUtil.changeForLog("delete app draft job end ..."));
            return ReturnT.SUCCESS;
        }catch (Throwable e){
            log.error(e.getMessage(), e);
            JobLogger.log(e);
            return ReturnT.FAIL;
        }
    }
}

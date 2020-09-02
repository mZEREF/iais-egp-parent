package com.ecquaria.cloud.moh.iais.job;


import com.ecquaria.cloud.job.executor.biz.model.ReturnT;
import com.ecquaria.cloud.job.executor.handler.IJobHandler;
import com.ecquaria.cloud.job.executor.handler.annotation.JobHandler;
import com.ecquaria.cloud.job.executor.log.JobLogger;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.service.AppSubmissionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@JobHandler(value = "DeleteAppDraftJobHandler")
@Component
@Slf4j
public class DeleteAppDraftJobHandler extends IJobHandler {
    @Autowired
    AppSubmissionService appSubmissionService;

    @Value("${iais.system.draft.validity}")
    private int draftValidity;

    @Override
    public ReturnT<String> execute(String s) throws Exception {
        try {
            JobLogger.log(StringUtil.changeForLog("delete app draft job start ..."));
            String draftValidityStr = String.valueOf(draftValidity);
            JobLogger.log(StringUtil.changeForLog("draft validity str:"+draftValidityStr));
            if(!StringUtil.isEmpty(draftValidityStr)){
                appSubmissionService.deleteOverdueDraft(draftValidityStr);
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

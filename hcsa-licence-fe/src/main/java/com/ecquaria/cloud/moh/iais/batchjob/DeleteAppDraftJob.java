package com.ecquaria.cloud.moh.iais.batchjob;


import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.service.AppSubmissionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import sop.webflow.rt.api.BaseProcessClass;

@Delegator("DeleteAppDraftJob")
@Slf4j
public class DeleteAppDraftJob {
    @Autowired
    AppSubmissionService appSubmissionService;

    @Value("${iais.system.draft.validity}")
    private int draftValidity;

    public void doBatchJob(BaseProcessClass bpc){
        log.info(StringUtil.changeForLog("delete app draft job start ..."));
        AuditTrailHelper.setupBatchJobAuditTrail(this);
        String draftValidityStr = String.valueOf(draftValidity);

        log.info(StringUtil.changeForLog("draft validity:"+draftValidityStr));
        if(!StringUtil.isEmpty(draftValidity)){
            appSubmissionService.deleteOverdueDraft(draftValidityStr);
        }
        log.info(StringUtil.changeForLog("delete app draft job end ..."));
    }

}

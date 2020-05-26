package com.ecquaria.cloud.moh.iais.job;


import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.helper.MasterCodeUtil;
import com.ecquaria.cloud.moh.iais.service.AppSubmissionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sop.webflow.rt.api.BaseProcessClass;

@Delegator("DeleteAppDraftJob")
@Slf4j
public class DeleteAppDraftJob {
    @Autowired
    AppSubmissionService appSubmissionService;

    public void doBatchJob(BaseProcessClass bpc){
        log.info(StringUtil.changeForLog("delete app draft job start ..."));
        String draftValidity = MasterCodeUtil.getCodeDesc("MS006");
        log.info(StringUtil.changeForLog("draft validity:"+draftValidity));
        if(!StringUtil.isEmpty(draftValidity)){
            appSubmissionService.deleteOverdueDraft(draftValidity);
        }
        log.info(StringUtil.changeForLog("delete app draft job end ..."));
    }

}

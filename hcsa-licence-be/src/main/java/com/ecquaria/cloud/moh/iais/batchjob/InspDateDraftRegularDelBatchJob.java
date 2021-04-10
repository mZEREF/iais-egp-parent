package com.ecquaria.cloud.moh.iais.batchjob;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.config.SystemParamConfig;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sop.webflow.rt.api.BaseProcessClass;

/**
 * @author Shicheng
 * @date 2021/4/10 10:39
 **/
@Delegator("inspDateDraftRegularDelDelegator")
@Slf4j
public class InspDateDraftRegularDelBatchJob {

    @Autowired
    private InspDateDraftRegularDelHandler inspDateDraftRegularDelHandler;

    @Autowired
    private SystemParamConfig systemParamConfig;

    /**
     * StartStep: inspDateDraftRegularDelStart
     *
     * @param bpc
     * @throws
     */
    public void inspDateDraftRegularDelStart(BaseProcessClass bpc){
        AuditTrailHelper.setupBatchJobAuditTrail(this);
        log.info(StringUtil.changeForLog("the inspDateDraftRegularDelStart start ...."));
    }

    /**
     * StartStep: inspDateDraftRegularDelDo
     *
     * @param bpc
     * @throws
     */
    public void inspDateDraftRegularDelDo(BaseProcessClass bpc){
        AuditTrailHelper.setupBatchJobAuditTrail(this);
        log.info(StringUtil.changeForLog("the inspDateDraftRegularDelDo start ...."));
        int removeHours = systemParamConfig.getRemoveInspDateDraft();
        inspDateDraftRegularDelHandler.removeInspDateDraftByConfigValue(removeHours);
    }
}

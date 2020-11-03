package com.ecquaria.cloud.moh.iais.action;


import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.batchjob.HalpActiveMcJobHandler;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.dto.AuditTrailDto;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.service.MasterCodeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sop.webflow.rt.api.BaseProcessClass;

@Delegator(value = "halpActiveMcBatchJob")
@Slf4j
public class HalpActiveMcDelegator {

    @Autowired
    MasterCodeService masterCodeService;

    public void updateMsStep(BaseProcessClass bpc){
        AuditTrailDto auditTrailDto = AuditTrailHelper.getCurrentAuditTrailDto();
        masterCodeService.activeMasterCode(auditTrailDto);
    }
}

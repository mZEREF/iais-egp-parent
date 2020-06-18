package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.service.ApptConfirmReSchDateService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sop.webflow.rt.api.BaseProcessClass;

/**
 * @Process MohApptUserChooseDate
 *
 * @author Shicheng
 * @date 2020/6/18 13:43
 **/
@Delegator(value = "apptConfirmReSchDateDelegator")
@Slf4j
public class ApptConfirmReSchDateDelegator {
    @Autowired
    private ApptConfirmReSchDateService apptConfirmReSchDateService;

    @Autowired
    private ApptConfirmReSchDateDelegator(ApptConfirmReSchDateService apptConfirmReSchDateService){
        this.apptConfirmReSchDateService = apptConfirmReSchDateService;
    }

    /**
     * StartStep: userConfirmInspDateStart
     *
     * @param bpc
     * @throws
     */
    public void userConfirmInspDateStart(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the userConfirmInspDateStart start ...."));

        AuditTrailHelper.auditFunction("Appointment Re-Scheduling Confirm date", "Appointment Re-Scheduling Confirm date");
    }
}

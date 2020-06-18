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
     * StartStep: apptUserChooseDateStart
     *
     * @param bpc
     * @throws
     */
    public void apptUserChooseDateStart(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the apptUserChooseDateStart start ...."));

        AuditTrailHelper.auditFunction("Appointment Re-Scheduling Confirm date", "Appointment Re-Scheduling Confirm date");
    }

    /**
     * StartStep: apptUserChooseDateInit
     *
     * @param bpc
     * @throws
     */
    public void apptUserChooseDateInit(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the apptUserChooseDateInit start ...."));
    }

    /**
     * StartStep: apptUserChooseDatePre
     *
     * @param bpc
     * @throws
     */
    public void apptUserChooseDatePre(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the apptUserChooseDatePre start ...."));
    }

    /**
     * StartStep: apptUserChooseDateVali
     *
     * @param bpc
     * @throws
     */
    public void apptUserChooseDateVali(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the apptUserChooseDateVali start ...."));
    }

    /**
     * StartStep: apptUserChooseDateStep1
     *
     * @param bpc
     * @throws
     */
    public void apptUserChooseDateStep1(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the apptUserChooseDateStep1 start ...."));
    }

    /**
     * StartStep: apptUserChooseDateSuccess
     *
     * @param bpc
     * @throws
     */
    public void apptUserChooseDateSuccess(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the apptUserChooseDateSuccess start ...."));
    }

    /**
     * StartStep: apptUserChooseDateReject
     *
     * @param bpc
     * @throws
     */
    public void apptUserChooseDateReject(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the apptUserChooseDateReject start ...."));
    }
}

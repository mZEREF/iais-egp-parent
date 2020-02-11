package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.helper.AccessUtil;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import lombok.extern.slf4j.Slf4j;
import sop.webflow.rt.api.BaseProcessClass;

/**
 * @Process: MohApptInspectionDate
 *
 * @author Shicheng
 * @date 2020/2/10 16:29
 **/
@Delegator(value = "apptInspectionDateDelegator")
@Slf4j
public class ApptInspectionDateDelegator {

    /**
     * StartStep: apptInspectionDateStart
     *
     * @param bpc
     * @throws
     */
    public void apptInspectionDateStart(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the apptInspectionDateStart start ...."));
        AccessUtil.initLoginUserInfo(bpc.request);
        AuditTrailHelper.auditFunction("Appointment Inspection Date", "Appointment Inspection Date");
    }

    /**
     * StartStep: apptInspectionDateInit
     *
     * @param bpc
     * @throws
     */
    public void apptInspectionDateInit(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the apptInspectionDateInit start ...."));
    }

    /**
     * StartStep: apptInspectionDatePre
     *
     * @param bpc
     * @throws
     */
    public void apptInspectionDatePre(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the apptInspectionDatePre start ...."));
    }

    /**
     * StartStep: apptInspectionDateVali
     *
     * @param bpc
     * @throws
     */
    public void apptInspectionDateVali(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the apptInspectionDateVali start ...."));
    }

    /**
     * StartStep: apptInspectionDateSuccess
     *
     * @param bpc
     * @throws
     */
    public void apptInspectionDateSuccess(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the apptInspectionDateSuccess start ...."));
    }
}

package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.service.OfficersReSchedulingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sop.webflow.rt.api.BaseProcessClass;

/**
 * @Process: MohOfficerReScheduling
 *
 * @author Shicheng
 * @date 2020/6/24 17:48
 **/
@Delegator("mohOfficerReSchedulingDelegator")
@Slf4j
public class OfficersReSchedulingDelegator {

    @Autowired
    private OfficersReSchedulingService officersReSchedulingService;

    @Autowired
    private OfficersReSchedulingDelegator(OfficersReSchedulingService officersReSchedulingService){
        this.officersReSchedulingService = officersReSchedulingService;
    }

    /**
     * StartStep: mohOfficerReSchedulingStart
     *
     * @param bpc
     * @throws
     */
    public void mohOfficerReSchedulingStart(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the mohOfficerReSchedulingStart start ...."));
        AuditTrailHelper.auditFunction("Moh Officer Rescheduling", "Moh Officer Rescheduling");
    }

    /**
     * StartStep: mohOfficerReSchedulingInit
     *
     * @param bpc
     * @throws
     */
    public void mohOfficerReSchedulingInit(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the mohOfficerReSchedulingInit start ...."));
    }

    /**
     * StartStep: mohOfficerReSchedulingPer
     *
     * @param bpc
     * @throws
     */
    public void mohOfficerReSchedulingPer(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the mohOfficerReSchedulingPer start ...."));
    }

    /**
     * StartStep: mohOfficerReSchedulingStep
     *
     * @param bpc
     * @throws
     */
    public void mohOfficerReSchedulingStep(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the mohOfficerReSchedulingStep start ...."));
    }

    /**
     * StartStep: mohOfficerReSchedulingSearch
     *
     * @param bpc
     * @throws
     */
    public void mohOfficerReSchedulingSearch(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the mohOfficerReSchedulingSearch start ...."));
    }

    /**
     * StartStep: mohOfficerReSchedulingSort
     *
     * @param bpc
     * @throws
     */
    public void mohOfficerReSchedulingSort(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the mohOfficerReSchedulingSort start ...."));
    }

    /**
     * StartStep: mohOfficerReSchedulingPage
     *
     * @param bpc
     * @throws
     */
    public void mohOfficerReSchedulingPage(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the mohOfficerReSchedulingPage start ...."));
    }

    /**
     * StartStep: mohOfficerReSchedulingQuery
     *
     * @param bpc
     * @throws
     */
    public void mohOfficerReSchedulingQuery(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the mohOfficerReSchedulingQuery start ...."));
    }

    /**
     * StartStep: mohOfficerReSchedulingInsp
     *
     * @param bpc
     * @throws
     */
    public void mohOfficerReSchedulingInsp(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the mohOfficerReSchedulingInsp start ...."));
    }

    /**
     * StartStep: mohOfficerReSchedulingAudit
     *
     * @param bpc
     * @throws
     */
    public void mohOfficerReSchedulingAudit(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the mohOfficerReSchedulingAudit start ...."));
    }

    /**
     * StartStep: mohOfficerReSchedulingVali
     *
     * @param bpc
     * @throws
     */
    public void mohOfficerReSchedulingVali(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the mohOfficerReSchedulingVali start ...."));
    }

    /**
     * StartStep: mohOfficerReSchedulingSuccess
     *
     * @param bpc
     * @throws
     */
    public void mohOfficerReSchedulingSuccess(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the mohOfficerReSchedulingSuccess start ...."));
    }
}

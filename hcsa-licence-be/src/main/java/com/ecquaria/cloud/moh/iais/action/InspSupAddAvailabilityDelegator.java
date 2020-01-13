package com.ecquaria.cloud.moh.iais.action;

/**
 * @Process: MohInspSupAddAvailability
 *
 * @author Shicheng
 * @date 2019/11/14 18:01
 **/

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.helper.AccessUtil;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.service.InspSupAddAvailabilityService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sop.webflow.rt.api.BaseProcessClass;

@Delegator("inspSupAddAvailabilityDelegator")
@Slf4j
public class InspSupAddAvailabilityDelegator {

    @Autowired
    private InspSupAddAvailabilityService inspSupAddAvailabilityService;

    @Autowired
    private InspSupAddAvailabilityDelegator(InspSupAddAvailabilityService inspSupAddAvailabilityService){
        this.inspSupAddAvailabilityService = inspSupAddAvailabilityService;
    }

    /**
     * StartStep: inspSupAddAvailabilityStart
     *
     * @param bpc
     * @throws
     */
    public void inspSupAddAvailabilityStart(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the inspSupAddAvailabilityStart start ...."));
        AuditTrailHelper.auditFunction("Inspection Sup Add Availability", "Inspection Sup Add Availability");
    }

    /**
     * StartStep: inspSupAddAvailabilityInit
     *
     * @param bpc
     * @throws
     */
    public void inspSupAddAvailabilityInit(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the inspSupAddAvailabilityInit start ...."));
        AccessUtil.initLoginUserInfo(bpc.request);

    }

    /**
     * StartStep: inspSupAddAvailabilityPre
     *
     * @param bpc
     * @throws
     */
    public void inspSupAddAvailabilityPre(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the inspSupAddAvailabilityPre start ...."));
    }

    /**
     * StartStep: inspSupAddAvailabilityStep
     *
     * @param bpc
     * @throws
     */
    public void inspSupAddAvailabilityStep(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the inspSupAddAvailabilityStep start ...."));
    }

    /**
     * StartStep: inspSupAddAvailabilitySearch
     *
     * @param bpc
     * @throws
     */
    public void inspSupAddAvailabilitySearch(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the inspSupAddAvailabilitySearch start ...."));
    }

    /**
     * StartStep: inspSupAddAvailabilitySort
     *
     * @param bpc
     * @throws
     */
    public void inspSupAddAvailabilitySort(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the inspSupAddAvailabilitySort start ...."));
    }

    /**
     * StartStep: inspSupAddAvailabilityPage
     *
     * @param bpc
     * @throws
     */
    public void inspSupAddAvailabilityPage(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the inspSupAddAvailabilityPage start ...."));
    }

    /**
     * StartStep: inspSupAddAvailabilityQuery
     *
     * @param bpc
     * @throws
     */
    public void inspSupAddAvailabilityQuery(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the inspSupAddAvailabilityQuery start ...."));
    }

    /**
     * StartStep: inspSupAddAvailabilityAdd
     *
     * @param bpc
     * @throws
     */
    public void inspSupAddAvailabilityAdd(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the inspSupAddAvailabilityAdd start ...."));
    }

    /**
     * StartStep: inspSupAddAvailabilityVali
     *
     * @param bpc
     * @throws
     */
    public void inspSupAddAvailabilityVali(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the inspSupAddAvailabilityVali start ...."));
    }

    /**
     * StartStep: inspSupAddAvailabilityConfirm
     *
     * @param bpc
     * @throws
     */
    public void inspSupAddAvailabilityConfirm(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the inspSupAddAvailabilityConfirm start ...."));
    }

    /**
     * StartStep: inspSupAddAvailabilityStep2
     *
     * @param bpc
     * @throws
     */
    public void inspSupAddAvailabilityStep2(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the inspSupAddAvailabilityStep2 start ...."));
    }

    /**
     * StartStep: inspSupAddAvailabilityDelete
     *
     * @param bpc
     * @throws
     */
    public void inspSupAddAvailabilityDelete(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the inspSupAddAvailabilityDelete start ...."));
    }
}

package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import lombok.extern.slf4j.Slf4j;
import sop.webflow.rt.api.BaseProcessClass;

/**
 * @Process: MohBeGiroDeduction
 *
 * @author Shicheng
 * @date 2020/10/19 10:31
 **/
@Delegator(value = "giroDeductionBeDelegator")
@Slf4j
public class GiroDeductionBeDelegator {

    /**
     * StartStep: beGiroDeductionStart
     *
     * @param bpc
     * @throws
     */
    public void beGiroDeductionStart(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the beGiroDeductionStart start ...."));
    }

    /**
     * StartStep: beGiroDeductionInit
     *
     * @param bpc
     * @throws
     */
    public void beGiroDeductionInit(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the beGiroDeductionInit start ...."));
    }

    /**
     * StartStep: beGiroDeductionPre
     *
     * @param bpc
     * @throws
     */
    public void beGiroDeductionPre(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the beGiroDeductionPre start ...."));
    }

    /**
     * StartStep: beGiroDeductionStep
     *
     * @param bpc
     * @throws
     */
    public void beGiroDeductionStep(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the beGiroDeductionStep start ...."));
    }

    /**
     * StartStep: beGiroDeductionDoSearch
     *
     * @param bpc
     * @throws
     */
    public void beGiroDeductionDoSearch(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the beGiroDeductionDoSearch start ...."));
    }

    /**
     * StartStep: beGiroDeductionSort
     *
     * @param bpc
     * @throws
     */
    public void beGiroDeductionSort(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the beGiroDeductionSort start ...."));
    }

    /**
     * StartStep: beGiroDeductionPage
     *
     * @param bpc
     * @throws
     */
    public void beGiroDeductionPage(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the beGiroDeductionPage start ...."));
    }

    /**
     * StartStep: beGiroDeductionQuery
     *
     * @param bpc
     * @throws
     */
    public void beGiroDeductionQuery(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the beGiroDeductionQuery start ...."));
    }

    /**
     * StartStep: beGiroDeductionRetVali
     *
     * @param bpc
     * @throws
     */
    public void beGiroDeductionRetVali(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the beGiroDeductionRetVali start ...."));

    }

    /**
     * StartStep: beGiroDeductionRetrigger
     *
     * @param bpc
     * @throws
     */
    public void beGiroDeductionRetrigger(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the beGiroDeductionRetrigger start ...."));
    }
}

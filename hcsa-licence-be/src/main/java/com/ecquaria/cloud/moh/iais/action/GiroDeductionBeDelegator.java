package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant;
import com.ecquaria.cloud.moh.iais.helper.MessageUtil;
import com.ecquaria.cloud.moh.iais.helper.WebValidationHelper;
import com.ecquaria.cloud.moh.iais.service.GiroDeductionBeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sop.webflow.rt.api.BaseProcessClass;

import java.util.List;
import java.util.Map;

/**
 * @Process: MohBeGiroDeduction
 *
 * @author Shicheng
 * @date 2020/10/19 10:31
 **/
@Delegator(value = "giroDeductionBeDelegator")
@Slf4j
public class GiroDeductionBeDelegator {

    @Autowired
    private GiroDeductionBeService giroDeductionBeService;

    @Autowired
    private GiroDeductionBeDelegator(GiroDeductionBeService giroDeductionBeService){
        this.giroDeductionBeService = giroDeductionBeService;
    }

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
        String[] appGroupNos = ParamUtil.getStrings(bpc.request,"appGroupNo");
        Map<String, String> errMap = IaisCommonUtils.genNewHashMap();
        String retValiError = "giroDeductionError";
        List<String> appGroupList = IaisCommonUtils.genNewArrayList();
        if(appGroupNos != null && appGroupNos.length > 0){
            //todo appGroup payment status validate
            for(int i = 0; i < appGroupNos.length; i++){
                appGroupList.add(appGroupNos[i]);
            }
        } else {
            errMap.put(retValiError, MessageUtil.replaceMessage("GENERAL_ERR0006", "GIRO Deduction Checkbox","field"));
        }
        if(errMap != null && errMap.size() > 0){
            ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errMap));
            ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.ISVALID, IaisEGPConstant.NO);
            ParamUtil.setRequestAttr(bpc.request, "flag", AppConsts.FALSE);
        } else {
            ParamUtil.setRequestAttr(bpc.request,"appGroupList", appGroupList);
            ParamUtil.setRequestAttr(bpc.request,"flag", AppConsts.TRUE);
        }
    }

    /**
     * StartStep: beGiroDeductionRetrigger
     *
     * @param bpc
     * @throws
     */
    public void beGiroDeductionRetrigger(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the beGiroDeductionRetrigger start ...."));
        List<String> appGroupList = (List<String>)ParamUtil.getRequestAttr(bpc.request, "appGroupList");
        giroDeductionBeService.sendMessageEmail(appGroupList);
    }
}

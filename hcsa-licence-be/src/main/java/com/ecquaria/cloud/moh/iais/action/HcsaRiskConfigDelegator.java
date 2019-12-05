package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.service.HcsaRiskService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;

/**
 * @Author: jiahao
 * @Date: 2019/11/13 13:23
 */
@Delegator(value = "hcsaRiskConfigDelegator")
@Slf4j
public class HcsaRiskConfigDelegator {
    private HcsaRiskService hcsaRiskService;

    @Autowired
    public HcsaRiskConfigDelegator(HcsaRiskService hcsaRiskService){
        this.hcsaRiskService = hcsaRiskService;
    }
    public void start(BaseProcessClass bpc) {
        log.debug(StringUtil.changeForLog("the doStart start ...."));
        HttpServletRequest request = bpc.request;

    }

    public void init(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the doInit init ...."));
        HttpServletRequest request = bpc.request;
    }

    public void prepare(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the prepare ...."));
        HttpServletRequest request = bpc.request;
    }

    public void donext(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the donext ...."));
        HttpServletRequest request = bpc.request;
    }

    public void preGolbalRiskConig(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the preGolbalRiskConig ...."));
        HttpServletRequest request = bpc.request;
    }

    public void preIndividualRiskConig(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the preIndividualRiskConig...."));
        HttpServletRequest request = bpc.request;
    }

    public void preFinancialRiskConig(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the preFinancialRiskConig ...."));
        HttpServletRequest request = bpc.request;
    }

    public void preLeadershipRiskConig(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the preLeadershipRiskConig ...."));
        HttpServletRequest request = bpc.request;
    }

    public void preLegislativeRiskConig(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the preLegislativeRiskConig...."));
        HttpServletRequest request = bpc.request;
    }

    public void preWeightageRiskConig(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the preWeightageRiskConig ...."));
        HttpServletRequest request = bpc.request;
    }

    public void preTenureRiskConig(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the preTenureRiskConig...."));
        HttpServletRequest request = bpc.request;
    }

}

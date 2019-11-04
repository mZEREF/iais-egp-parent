package com.ecquaria.cloud.moh.iais.controller;

/*
 *author: yichen
 *date time:10/15/2019 3:39 PM
 *description:
 */

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.checklist.HcsaChklConstants;
import com.ecquaria.cloud.moh.iais.dto.FilterParameter;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.service.HcsaChklService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;

@Delegator(value = "hcsaChklConfigDelegator")
@Slf4j
public class HcsaChklConfigDelegator {

    private HcsaChklService hcsaChklService;
    private FilterParameter filterParameter;

    @Autowired
    public void HcsaChklConfigDelegator(HcsaChklService hcsaChklService, FilterParameter filterParameter){
        this.hcsaChklService = hcsaChklService;
        this.filterParameter = filterParameter;
    }

    /**
     * StartStep: startStep
     * @param bpc
     * @throws IllegalAccessException
     */
    public void startStep(BaseProcessClass bpc) throws IllegalAccessException {
        log.info("=======>>>>>startStep>>>>>>>>>>>>>>>>HcsaChklConfigDelegator");
        AuditTrailHelper.auditFunction("Checklist Management", "Checklist Config");
        HttpServletRequest request = bpc.request;
        IaisEGPHelper.clearSessionAttr(request, HcsaChklConstants.class);
    }

    /**
     * @AutoStep: saveChecklistConfig
     * @param:
     * @return:
     * @author: yichen
     */
    public void saveChecklistConfig(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
    }
}

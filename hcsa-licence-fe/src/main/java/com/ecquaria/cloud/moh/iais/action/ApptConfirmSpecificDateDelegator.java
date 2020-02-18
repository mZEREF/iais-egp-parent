package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.service.ApplicantConfirmInspDateService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sop.webflow.rt.api.BaseProcessClass;

/**
 * @Process MohUserConfirmSpecificDate
 *
 * @author Shicheng
 * @date 2020/2/18 10:16
 **/
@Delegator(value = "userConfirmSpecificDateDelegator")
@Slf4j
public class ApptConfirmSpecificDateDelegator {
    @Autowired
    private ApplicantConfirmInspDateService applicantConfirmInspDateService;

    @Autowired
    private ApptConfirmSpecificDateDelegator(ApplicantConfirmInspDateService applicantConfirmInspDateService){
        this.applicantConfirmInspDateService = applicantConfirmInspDateService;
    }
    /**
     * StartStep: userConfirmSpecificDateStart
     *
     * @param bpc
     * @throws
     */
    public void userConfirmSpecificDateStart(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the userConfirmSpecificDateStart start ...."));
    }

    /**
     * StartStep: userConfirmSpecificDateInit
     *
     * @param bpc
     * @throws
     */
    public void userConfirmSpecificDateInit(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the userConfirmSpecificDateInit start ...."));
    }

    /**
     * StartStep: userConfirmSpecificDatePre
     *
     * @param bpc
     * @throws
     */
    public void userConfirmSpecificDatePre(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the userConfirmSpecificDatePre start ...."));
    }

    /**
     * StartStep: userConfirmSpecificDateStep
     *
     * @param bpc
     * @throws
     */
    public void userConfirmSpecificDateStep(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the userConfirmSpecificDateStep start ...."));
    }

    /**
     * StartStep: userConfirmSpecificDateAcc
     *
     * @param bpc
     * @throws
     */
    public void userConfirmSpecificDateAcc(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the userConfirmSpecificDateAcc start ...."));
    }

    /**
     * StartStep: userConfirmSpecificDateReject
     *
     * @param bpc
     * @throws
     */
    public void userConfirmSpecificDateReject(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the userConfirmSpecificDateReject start ...."));
    }
}

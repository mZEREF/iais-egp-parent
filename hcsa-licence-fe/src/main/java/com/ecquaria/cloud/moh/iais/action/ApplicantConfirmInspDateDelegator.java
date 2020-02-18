package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.dto.appointment.ApptFeConfirmDateDto;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.service.ApplicantConfirmInspDateService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sop.webflow.rt.api.BaseProcessClass;

/**
 * @Process MohUserConfirmInspDate
 *
 * @author Shicheng
 * @date 2020/2/15 16:26
 **/
@Delegator(value = "applicantConfirmInspDateDelegator")
@Slf4j
public class ApplicantConfirmInspDateDelegator {

    @Autowired
    private ApplicantConfirmInspDateService applicantConfirmInspDateService;

    @Autowired
    private ApplicantConfirmInspDateDelegator(ApplicantConfirmInspDateService applicantConfirmInspDateService){
        this.applicantConfirmInspDateService = applicantConfirmInspDateService;
    }
    /**
     * StartStep: userConfirmInspDateStart
     *
     * @param bpc
     * @throws
     */
    public void userConfirmInspDateStart(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the userConfirmInspDateStart start ...."));
    }

    /**
     * StartStep: userConfirmInspDateInit
     *
     * @param bpc
     * @throws
     */
    public void userConfirmInspDateInit(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the userConfirmInspDateInit start ...."));
        ParamUtil.setSessionAttr(bpc.request, "apptFeConfirmDateDto", null);
    }

    /**
     * StartStep: userConfirmInspDatePre
     *
     * @param bpc
     * @throws
     */
    public void userConfirmInspDatePre(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the userConfirmInspDatePre start ...."));
        ApptFeConfirmDateDto apptFeConfirmDateDto = (ApptFeConfirmDateDto) ParamUtil.getSessionAttr(bpc.request, "apptFeConfirmDateDto");
        if(apptFeConfirmDateDto == null){
            String appPremCorrId = ParamUtil.getRequestString(bpc.request, "appPremCorrId");
            apptFeConfirmDateDto = applicantConfirmInspDateService.getApptSystemDate(appPremCorrId);
        }
        ParamUtil.setSessionAttr(bpc.request, "apptFeConfirmDateDto", apptFeConfirmDateDto);
    }

    /**
     * StartStep: userConfirmInspDateStep
     *
     * @param bpc
     * @throws
     */
    public void userConfirmInspDateStep(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the userConfirmInspDateStep start ...."));
    }

    /**
     * StartStep: userConfirmInspDateConfirm
     *
     * @param bpc
     * @throws
     */
    public void userConfirmInspDateConfirm(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the userConfirmInspDateConfirm start ...."));
    }

    /**
     * StartStep: userConfirmInspDateConDo
     *
     * @param bpc
     * @throws
     */
    public void userConfirmInspDateConDo(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the userConfirmInspDateConDo start ...."));
    }

    /**
     * StartStep: userConfirmInspDateRe
     *
     * @param bpc
     * @throws
     */
    public void userConfirmInspDateRe(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the userConfirmInspDateRe start ...."));
    }

    /**
     * StartStep: userConfirmInspDateReVali
     *
     * @param bpc
     * @throws
     */
    public void userConfirmInspDateReVali(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the userConfirmInspDateReVali start ...."));
    }

    /**
     * StartStep: userConfirmInspDateSuccess
     *
     * @param bpc
     * @throws
     */
    public void userConfirmInspDateSuccess(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the userConfirmInspDateSuccess start ...."));
    }

    /**
     * StartStep: userConfirmInspDateReject
     *
     * @param bpc
     * @throws
     */
    public void userConfirmInspDateReject(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the userConfirmInspDateReject start ...."));
    }

    /**
     * StartStep: userConfirmInspDateRejVali
     *
     * @param bpc
     * @throws
     */
    public void userConfirmInspDateRejVali(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the userConfirmInspDateRejVali start ...."));
    }

    /**
     * StartStep: userConfirmInspDateRejSucc
     *
     * @param bpc
     * @throws
     */
    public void userConfirmInspDateRejSucc(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the userConfirmInspDateRejSucc start ...."));
    }
}

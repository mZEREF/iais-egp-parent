package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.dto.appointment.ApptFeConfirmDateDto;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.helper.AccessUtil;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
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
        AccessUtil.initLoginUserInfo(bpc.request);
        AuditTrailHelper.auditFunction("Appointment Confirm Specific Date", "Appointment Confirm Specific Date");
    }

    /**
     * StartStep: userConfirmSpecificDateInit
     *
     * @param bpc
     * @throws
     */
    public void userConfirmSpecificDateInit(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the userConfirmSpecificDateInit start ...."));
        ParamUtil.setSessionAttr(bpc.request, "apptFeConfirmDateDto", null);
    }

    /**
     * StartStep: userConfirmSpecificDatePre
     *
     * @param bpc
     * @throws
     */
    public void userConfirmSpecificDatePre(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the userConfirmSpecificDatePre start ...."));
        ApptFeConfirmDateDto apptFeConfirmDateDto = (ApptFeConfirmDateDto) ParamUtil.getSessionAttr(bpc.request, "apptFeConfirmDateDto");
        if(apptFeConfirmDateDto == null){
            String appPremCorrId = ParamUtil.getRequestString(bpc.request, "appPremCorrId");
            apptFeConfirmDateDto = applicantConfirmInspDateService.getSpecificDateDto(appPremCorrId);
        }
        ParamUtil.setSessionAttr(bpc.request, "apptFeConfirmDateDto", apptFeConfirmDateDto);
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
        ApptFeConfirmDateDto apptFeConfirmDateDto = (ApptFeConfirmDateDto) ParamUtil.getSessionAttr(bpc.request, "apptFeConfirmDateDto");
        applicantConfirmInspDateService.saveAccSpecificDate(apptFeConfirmDateDto);
        ParamUtil.setSessionAttr(bpc.request, "apptFeConfirmDateDto", apptFeConfirmDateDto);
    }

    /**
     * StartStep: userConfirmSpecificDateReject
     *
     * @param bpc
     * @throws
     */
    public void userConfirmSpecificDateReject(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the userConfirmSpecificDateReject start ...."));
        ApptFeConfirmDateDto apptFeConfirmDateDto = (ApptFeConfirmDateDto) ParamUtil.getSessionAttr(bpc.request, "apptFeConfirmDateDto");
        applicantConfirmInspDateService.rejectSpecificDate(apptFeConfirmDateDto);
        ParamUtil.setSessionAttr(bpc.request, "apptFeConfirmDateDto", apptFeConfirmDateDto);
    }
}

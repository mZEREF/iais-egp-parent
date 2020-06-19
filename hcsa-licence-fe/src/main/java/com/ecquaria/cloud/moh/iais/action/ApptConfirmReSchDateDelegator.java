package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.dto.appointment.ProcessReSchedulingDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspSetMaskValueDto;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.constant.HcsaLicenceFeConstant;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.service.ApptConfirmReSchDateService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sop.webflow.rt.api.BaseProcessClass;

/**
 * @Process MohApptUserChooseDate
 *
 * @author Shicheng
 * @date 2020/6/18 13:43
 **/
@Delegator(value = "apptConfirmReSchDateDelegator")
@Slf4j
public class ApptConfirmReSchDateDelegator {
    @Autowired
    private ApptConfirmReSchDateService apptConfirmReSchDateService;

    @Autowired
    private ApptConfirmReSchDateDelegator(ApptConfirmReSchDateService apptConfirmReSchDateService){
        this.apptConfirmReSchDateService = apptConfirmReSchDateService;
    }

    /**
     * StartStep: apptUserChooseDateStart
     *
     * @param bpc
     * @throws
     */
    public void apptUserChooseDateStart(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the apptUserChooseDateStart start ...."));
        ParamUtil.setSessionAttr(bpc.request, "inspSetMaskValueDto", null);
        String applicationNo = ParamUtil.getMaskedString(bpc.request, "applicationNo");
        String messageId = (String) ParamUtil.getSessionAttr(bpc.request, AppConsts.SESSION_INTER_INBOX_MESSAGE_ID);
        InspSetMaskValueDto inspSetMaskValueDto = new InspSetMaskValueDto();
        inspSetMaskValueDto.setApplicationNo(applicationNo);
        AuditTrailHelper.auditFunction("Appointment Re-Scheduling Confirm date", "Appointment Re-Scheduling Confirm date");
        ParamUtil.setSessionAttr(bpc.request, "inspSetMaskValueDto", inspSetMaskValueDto);
        ParamUtil.setSessionAttr(bpc.request, AppConsts.SESSION_INTER_INBOX_MESSAGE_ID, messageId);
    }

    /**
     * StartStep: apptUserChooseDateInit
     *
     * @param bpc
     * @throws
     */
    public void apptUserChooseDateInit(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the apptUserChooseDateInit start ...."));
        ParamUtil.setSessionAttr(bpc.request, "processReSchedulingDto", null);
    }

    /**
     * StartStep: apptUserChooseDatePre
     *
     * @param bpc
     * @throws
     */
    public void apptUserChooseDatePre(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the apptUserChooseDatePre start ...."));
        InspSetMaskValueDto inspSetMaskValueDto = (InspSetMaskValueDto)ParamUtil.getSessionAttr(bpc.request, "inspSetMaskValueDto");
        ProcessReSchedulingDto processReSchedulingDto = (ProcessReSchedulingDto)ParamUtil.getSessionAttr(bpc.request, "processReSchedulingDto");
        String applicationNo = inspSetMaskValueDto.getApplicationNo();
        ApplicationDto applicationDto = apptConfirmReSchDateService.getApplicationDtoByAppNo(applicationNo);
        String appId = applicationDto.getId();
        String appPremCorrId = apptConfirmReSchDateService.getAppPremCorrIdByAppId(appId);
        String appStatus = applicationDto.getStatus();
        if(processReSchedulingDto == null) {
            if (ApplicationConsts.APPLICATION_STATUS_RE_SCHEDULING_APPLICANT.equals(appStatus)) {
                processReSchedulingDto = apptConfirmReSchDateService.getApptSystemDateByCorrId(appPremCorrId);
                ParamUtil.setSessionAttr(bpc.request, "apptInspFlag", AppConsts.FALSE);
            } else {
                ParamUtil.setSessionAttr(bpc.request, "apptInspFlag", AppConsts.SUCCESS);
            }
        }
        ParamUtil.setSessionAttr(bpc.request, HcsaLicenceFeConstant.DASHBOARDTITLE,"System Proposes Alternate Dates");
        ParamUtil.setSessionAttr(bpc.request, "processReSchedulingDto", processReSchedulingDto);
    }

    /**
     * StartStep: apptUserChooseDateVali
     *
     * @param bpc
     * @throws
     */
    public void apptUserChooseDateVali(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the apptUserChooseDateVali start ...."));
        ParamUtil.setRequestAttr(bpc.request,"flag", AppConsts.TRUE);
    }

    /**
     * StartStep: apptUserChooseDateStep1
     *
     * @param bpc
     * @throws
     */
    public void apptUserChooseDateStep1(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the apptUserChooseDateStep1 start ...."));
    }

    /**
     * StartStep: apptUserChooseDateSuccess
     *
     * @param bpc
     * @throws
     */
    public void apptUserChooseDateSuccess(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the apptUserChooseDateSuccess start ...."));
    }

    /**
     * StartStep: apptUserChooseDateReject
     *
     * @param bpc
     * @throws
     */
    public void apptUserChooseDateReject(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the apptUserChooseDateReject start ...."));
    }
}

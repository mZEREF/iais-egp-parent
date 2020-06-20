package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.constant.inspection.InspectionConstants;
import com.ecquaria.cloud.moh.iais.common.constant.message.MessageConstants;
import com.ecquaria.cloud.moh.iais.common.dto.appointment.ProcessReSchedulingDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspSetMaskValueDto;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.common.validation.dto.ValidationResult;
import com.ecquaria.cloud.moh.iais.constant.HcsaLicenceFeConstant;
import com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.WebValidationHelper;
import com.ecquaria.cloud.moh.iais.service.ApptConfirmReSchDateService;
import com.ecquaria.cloud.moh.iais.service.InspecUserRecUploadService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sop.webflow.rt.api.BaseProcessClass;

import java.util.Date;
import java.util.Map;

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
    private InspecUserRecUploadService inspecUserRecUploadService;

    @Autowired
    private ApptConfirmReSchDateDelegator(ApptConfirmReSchDateService apptConfirmReSchDateService, InspecUserRecUploadService inspecUserRecUploadService){
        this.apptConfirmReSchDateService = apptConfirmReSchDateService;
        this.inspecUserRecUploadService = inspecUserRecUploadService;
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
            if (ApplicationConsts.APPLICATION_STATUS_RE_SCHEDULING_APPLICANT.equals(appStatus) ||
                    ApplicationConsts.APPLICATION_STATUS_OFFICER_RESCHEDULING_APPLICANT.equals(appStatus)) {
                processReSchedulingDto = apptConfirmReSchDateService.getApptSystemDateByCorrId(appPremCorrId);
                processReSchedulingDto.setApplicationDto(applicationDto);
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
        ProcessReSchedulingDto processReSchedulingDto = (ProcessReSchedulingDto)ParamUtil.getSessionAttr(bpc.request, "processReSchedulingDto");
        String dateValue = ParamUtil.getRequestString(bpc.request, "apptCheckDate");
        String actionValue = ParamUtil.getRequestString(bpc.request, "actionValue");
        processReSchedulingDto.setCheckDate(dateValue);
        if(InspectionConstants.SWITCH_ACTION_SUCCESS.equals(actionValue)){
            ValidationResult validationResult = WebValidationHelper.validateProperty(processReSchedulingDto,"confirm");
            if (validationResult.isHasErrors()) {
                Map<String, String> errorMap = validationResult.retrieveAll();
                ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errorMap));
                ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.ISVALID, IaisEGPConstant.NO);
                ParamUtil.setRequestAttr(bpc.request, "flag", AppConsts.FALSE);
            } else {
                ParamUtil.setRequestAttr(bpc.request,"flag",AppConsts.TRUE);
            }
        } else {
            ParamUtil.setRequestAttr(bpc.request,"flag", AppConsts.TRUE);
        }
        ParamUtil.setSessionAttr(bpc.request, "processReSchedulingDto", processReSchedulingDto);
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
        ProcessReSchedulingDto processReSchedulingDto = (ProcessReSchedulingDto)ParamUtil.getSessionAttr(bpc.request, "processReSchedulingDto");
        String messageId = (String) ParamUtil.getSessionAttr(bpc.request, AppConsts.SESSION_INTER_INBOX_MESSAGE_ID);
        inspecUserRecUploadService.updateMessageStatus(messageId, MessageConstants.MESSAGE_STATUS_RESPONSE);
        apptConfirmReSchDateService.acceptReschedulingDate(processReSchedulingDto);
        ParamUtil.setSessionAttr(bpc.request, "processReSchedulingDto", processReSchedulingDto);
    }

    /**
     * StartStep: apptUserChooseDateReject
     *
     * @param bpc
     * @throws
     */
    public void apptUserChooseDateReject(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the apptUserChooseDateReject start ...."));
        ProcessReSchedulingDto processReSchedulingDto = (ProcessReSchedulingDto)ParamUtil.getSessionAttr(bpc.request, "processReSchedulingDto");
        String messageId = (String) ParamUtil.getSessionAttr(bpc.request, AppConsts.SESSION_INTER_INBOX_MESSAGE_ID);
        inspecUserRecUploadService.updateMessageStatus(messageId, MessageConstants.MESSAGE_STATUS_RESPONSE);
        String dateValue = processReSchedulingDto.getCheckDate();
        Map<String, Date> inspectionDateMap = processReSchedulingDto.getInspectionDateMap();
        Date checkDate = inspectionDateMap.get(dateValue);
        processReSchedulingDto.setSaveDate(checkDate);
        apptConfirmReSchDateService.rejectReschedulingDate(processReSchedulingDto);
        ParamUtil.setSessionAttr(bpc.request, "processReSchedulingDto", processReSchedulingDto);
    }
}

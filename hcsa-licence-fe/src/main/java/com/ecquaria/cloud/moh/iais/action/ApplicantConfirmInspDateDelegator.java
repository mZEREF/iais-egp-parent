package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.constant.inspection.InspectionConstants;
import com.ecquaria.cloud.moh.iais.common.constant.message.MessageConstants;
import com.ecquaria.cloud.moh.iais.common.constant.task.TaskConsts;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.appointment.ApptFeConfirmDateDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspSetMaskValueDto;
import com.ecquaria.cloud.moh.iais.common.utils.Formatter;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.common.validation.dto.ValidationResult;
import com.ecquaria.cloud.moh.iais.constant.HcsaLicenceFeConstant;
import com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.WebValidationHelper;
import com.ecquaria.cloud.moh.iais.service.ApplicantConfirmInspDateService;
import com.ecquaria.cloud.moh.iais.service.InspecUserRecUploadService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sop.webflow.rt.api.BaseProcessClass;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

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
    private InspecUserRecUploadService inspecUserRecUploadService;

    @Autowired
    private ApplicantConfirmInspDateDelegator(InspecUserRecUploadService inspecUserRecUploadService, ApplicantConfirmInspDateService applicantConfirmInspDateService){
        this.applicantConfirmInspDateService = applicantConfirmInspDateService;
        this.inspecUserRecUploadService = inspecUserRecUploadService;
    }
    /**
     * StartStep: userConfirmInspDateStart
     *
     * @param bpc
     * @throws
     */
    public void userConfirmInspDateStart(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the userConfirmInspDateStart start ...."));
        ParamUtil.setSessionAttr(bpc.request, "inspSetMaskValueDto", null);
        String appPremCorrId = ParamUtil.getMaskedString(bpc.request, "appPremCorrId");
        String messageId = (String) ParamUtil.getSessionAttr(bpc.request, AppConsts.SESSION_INTER_INBOX_MESSAGE_ID);
        InspSetMaskValueDto inspSetMaskValueDto = new InspSetMaskValueDto();
        inspSetMaskValueDto.setAppPremCorrId(appPremCorrId);
        ParamUtil.setSessionAttr(bpc.request, "inspSetMaskValueDto", inspSetMaskValueDto);
        AuditTrailHelper.auditFunction("Appointment Confirm System Date", "Appointment Confirm System Date");
        ParamUtil.setSessionAttr(bpc.request, AppConsts.SESSION_INTER_INBOX_MESSAGE_ID, messageId);
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
        ParamUtil.setSessionAttr(bpc.request, "hoursFeOption", null);
        ParamUtil.setSessionAttr(bpc.request, "amPmFeOption", null);
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
        InspSetMaskValueDto inspSetMaskValueDto = (InspSetMaskValueDto)ParamUtil.getSessionAttr(bpc.request, "inspSetMaskValueDto");
        if(apptFeConfirmDateDto == null){
            String appPremCorrId = inspSetMaskValueDto.getAppPremCorrId();
            if(!StringUtil.isEmpty(appPremCorrId)) {
                ApplicationDto applicationDto = inspecUserRecUploadService.getApplicationByCorrId(appPremCorrId);
                String appStatus = applicationDto.getStatus();
                if(ApplicationConsts.APPLICATION_STATUS_PENDING_FE_APPOINTMENT_SCHEDULING.equals(appStatus)){
                    apptFeConfirmDateDto = applicantConfirmInspDateService.getApptSystemDate(appPremCorrId);
                    apptFeConfirmDateDto.setCTaskUrl(TaskConsts.TASK_PROCESS_URL_APPT_INSPECTION_DATE);
                    ParamUtil.setSessionAttr(bpc.request, "apptInspFlag", AppConsts.FALSE);
                } else {
                    ParamUtil.setSessionAttr(bpc.request, "apptInspFlag", AppConsts.SUCCESS);
                }
            }
        }
        ParamUtil.setSessionAttr(bpc.request, "apptFeConfirmDateDto", apptFeConfirmDateDto);
        ParamUtil.setRequestAttr(bpc.request, HcsaLicenceFeConstant.DASHBOARDTITLE,"Appointment Scheduling");
    }

    /**
     * StartStep: userConfirmInspDateStep
     *
     * @param bpc
     * @throws
     */
    public void userConfirmInspDateStep(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the userConfirmInspDateStep start ...."));
        ApptFeConfirmDateDto apptFeConfirmDateDto = (ApptFeConfirmDateDto) ParamUtil.getSessionAttr(bpc.request, "apptFeConfirmDateDto");
        String actionValue = ParamUtil.getRequestString(bpc.request, "actionValue");
        if(!StringUtil.isEmpty(actionValue)){
            apptFeConfirmDateDto.setActionValue(actionValue);
        }
        ParamUtil.setSessionAttr(bpc.request, "apptFeConfirmDateDto", apptFeConfirmDateDto);
    }

    /**
     * StartStep: userConfirmInspDateConfirm
     *
     * @param bpc
     * @throws
     */
    public void userConfirmInspDateConfirm(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the userConfirmInspDateConfirm start ...."));
        ApptFeConfirmDateDto apptFeConfirmDateDto = (ApptFeConfirmDateDto) ParamUtil.getSessionAttr(bpc.request, "apptFeConfirmDateDto");
        String dateValue = ParamUtil.getRequestString(bpc.request, "apptCheckDate");
        if(!StringUtil.isEmpty(dateValue)){
            boolean dateFlag = checkDateFlag(dateValue, apptFeConfirmDateDto.getInspectionDateMap());
            if(dateFlag){
                apptFeConfirmDateDto.setCheckDate(dateValue);
            } else {
                apptFeConfirmDateDto.setCheckDate(null);
            }
        } else {
            apptFeConfirmDateDto.setCheckDate(null);
        }

        ValidationResult validationResult = WebValidationHelper.validateProperty(apptFeConfirmDateDto,"confirm");
        if (validationResult.isHasErrors()) {
            Map<String, String> errorMap = validationResult.retrieveAll();
            ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errorMap));
            ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.ISVALID, IaisEGPConstant.NO);
            ParamUtil.setRequestAttr(bpc.request, "flag", AppConsts.FALSE);
        } else {
            ParamUtil.setRequestAttr(bpc.request,"flag",AppConsts.TRUE);
        }
        ParamUtil.setSessionAttr(bpc.request, "apptFeConfirmDateDto", apptFeConfirmDateDto);
    }

    /**
     * StartStep: userConfirmInspDateConDo
     *
     * @param bpc
     * @throws
     */
    public void userConfirmInspDateConDo(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the userConfirmInspDateConDo start ...."));
        ApptFeConfirmDateDto apptFeConfirmDateDto = (ApptFeConfirmDateDto) ParamUtil.getSessionAttr(bpc.request, "apptFeConfirmDateDto");
        String dateValue = apptFeConfirmDateDto.getCheckDate();
        Map<String, Date> inspectionDateMap = apptFeConfirmDateDto.getInspectionDateMap();
        Date checkDate = inspectionDateMap.get(dateValue);
        apptFeConfirmDateDto.setSaveDate(checkDate);
        applicantConfirmInspDateService.confirmInspectionDate(apptFeConfirmDateDto);
        String messageId = (String) ParamUtil.getSessionAttr(bpc.request, AppConsts.SESSION_INTER_INBOX_MESSAGE_ID);
        inspecUserRecUploadService.updateMessageStatus(messageId, MessageConstants.MESSAGE_STATUS_RESPONSE);
        ParamUtil.setSessionAttr(bpc.request, "apptFeConfirmDateDto", apptFeConfirmDateDto);
        ParamUtil.setRequestAttr(bpc.request, HcsaLicenceFeConstant.DASHBOARDTITLE,"Appointment Scheduling");
    }

    /**
     * StartStep: userConfirmInspDateRe
     *
     * @param bpc
     * @throws
     */
    public void userConfirmInspDateRe(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the userConfirmInspDateRe start ...."));
        ApptFeConfirmDateDto apptFeConfirmDateDto = (ApptFeConfirmDateDto) ParamUtil.getSessionAttr(bpc.request, "apptFeConfirmDateDto");
        apptFeConfirmDateDto = applicantConfirmInspDateService.getApptNewSystemDate(apptFeConfirmDateDto);
        ParamUtil.setRequestAttr(bpc.request, HcsaLicenceFeConstant.DASHBOARDTITLE,"Recomputing of Dates");
        ParamUtil.setSessionAttr(bpc.request, "apptFeConfirmDateDto", apptFeConfirmDateDto);
    }

    /**
     * StartStep: userConfirmInspDateReVali
     *
     * @param bpc
     * @throws
     */
    public void userConfirmInspDateReVali(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the userConfirmInspDateReVali start ...."));
        ApptFeConfirmDateDto apptFeConfirmDateDto = (ApptFeConfirmDateDto) ParamUtil.getSessionAttr(bpc.request, "apptFeConfirmDateDto");
        String actionValue = ParamUtil.getRequestString(bpc.request, "actionValue");
        if(InspectionConstants.SWITCH_ACTION_BACK.equals(actionValue)){
            ParamUtil.setRequestAttr(bpc.request,"flag",AppConsts.TRUE);
        } else {
            String newDateValue = ParamUtil.getRequestString(bpc.request, "apptCheckNewDate");
            if(!StringUtil.isEmpty(newDateValue)) {
                boolean dateFlag = checkDateFlag(newDateValue, apptFeConfirmDateDto.getInspectionNewDateMap());
                if (dateFlag) {
                    apptFeConfirmDateDto.setCheckNewDate(newDateValue);
                } else {
                    apptFeConfirmDateDto.setCheckNewDate(null);
                }
            } else {
                apptFeConfirmDateDto.setCheckNewDate(null);
            }
            ValidationResult validationResult = WebValidationHelper.validateProperty(apptFeConfirmDateDto,"reconfirm");
            if (validationResult.isHasErrors()) {
                Map<String, String> errorMap = validationResult.retrieveAll();
                ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errorMap));
                ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.ISVALID, IaisEGPConstant.NO);
                ParamUtil.setRequestAttr(bpc.request, "flag", AppConsts.FALSE);
            } else {
                ParamUtil.setRequestAttr(bpc.request,"flag",AppConsts.TRUE);
            }
        }
        ParamUtil.setSessionAttr(bpc.request, "apptFeConfirmDateDto", apptFeConfirmDateDto);
    }

    private boolean checkDateFlag(String newDateValue, Map<String, Date> inspectionNewDateMap) {
        if(inspectionNewDateMap != null){
            for(Map.Entry<String, Date> map : inspectionNewDateMap.entrySet()){
                if(newDateValue.equals(map.getKey())){
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * StartStep: userConfirmInspDateSuccess
     *
     * @param bpc
     * @throws
     */
    public void userConfirmInspDateSuccess(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the userConfirmInspDateSuccess start ...."));
        ApptFeConfirmDateDto apptFeConfirmDateDto = (ApptFeConfirmDateDto) ParamUtil.getSessionAttr(bpc.request, "apptFeConfirmDateDto");
        String actionValue = apptFeConfirmDateDto.getActionValue();
        if(InspectionConstants.SWITCH_ACTION_RE_CONFIRM.equals(actionValue)){
            apptFeConfirmDateDto = applicantConfirmInspDateService.confirmNewDate(apptFeConfirmDateDto);
            String messageId = (String) ParamUtil.getSessionAttr(bpc.request, AppConsts.SESSION_INTER_INBOX_MESSAGE_ID);
            inspecUserRecUploadService.updateMessageStatus(messageId, MessageConstants.MESSAGE_STATUS_RESPONSE);
        }
        ParamUtil.setSessionAttr(bpc.request, "apptFeConfirmDateDto", apptFeConfirmDateDto);
        ParamUtil.setRequestAttr(bpc.request, HcsaLicenceFeConstant.DASHBOARDTITLE,"Recomputing of Dates");
    }

    /**
     * StartStep: userConfirmInspDateReject
     *
     * @param bpc
     * @throws
     */
    public void userConfirmInspDateReject(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the userConfirmInspDateReject start ...."));
        ApptFeConfirmDateDto apptFeConfirmDateDto = (ApptFeConfirmDateDto) ParamUtil.getSessionAttr(bpc.request, "apptFeConfirmDateDto");
        List<SelectOption> amPm = applicantConfirmInspDateService.getAmPmOption();
        ParamUtil.setSessionAttr(bpc.request, "amPmFeOption", (Serializable) amPm);
        ParamUtil.setSessionAttr(bpc.request, "apptFeConfirmDateDto", apptFeConfirmDateDto);
        ParamUtil.setRequestAttr(bpc.request, HcsaLicenceFeConstant.DASHBOARDTITLE,"Requests a Particular Date");
    }

    /**
     * StartStep: userConfirmInspDateRejVali
     *
     * @param bpc
     * @throws
     */
    public void userConfirmInspDateRejVali(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the userConfirmInspDateRejVali start ...."));
        ApptFeConfirmDateDto apptFeConfirmDateDto = (ApptFeConfirmDateDto) ParamUtil.getSessionAttr(bpc.request, "apptFeConfirmDateDto");
        String actionValue = ParamUtil.getRequestString(bpc.request, "actionValue");
        if(InspectionConstants.SWITCH_ACTION_BACK.equals(actionValue)){
            ParamUtil.setRequestAttr(bpc.request,"flag",AppConsts.TRUE);
        } else {
            List<SelectOption> hoursOption = (List<SelectOption>)ParamUtil.getSessionAttr(bpc.request, "hoursFeOption");
            List<SelectOption> amPmOption = (List<SelectOption>)ParamUtil.getSessionAttr(bpc.request, "amPmFeOption");
            String rejectDate = ParamUtil.getRequestString(bpc.request, "rejectDate");
            String apptAmPm = ParamUtil.getRequestString(bpc.request, "apptAmPm");
            String apptRejectReason = ParamUtil.getRequestString(bpc.request, "apptRejectReason");
            apptFeConfirmDateDto.setRejectDate(rejectDate);
            apptFeConfirmDateDto.setReason(apptRejectReason);

            apptFeConfirmDateDto = setHoursAndAmPm(apptFeConfirmDateDto, hoursOption, amPmOption, apptAmPm);

            ValidationResult validationResult = WebValidationHelper.validateProperty(apptFeConfirmDateDto,"reject");
            if (validationResult.isHasErrors()) {
                Map<String, String> errorMap = validationResult.retrieveAll();
                ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errorMap));
                ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.ISVALID, IaisEGPConstant.NO);
                ParamUtil.setRequestAttr(bpc.request, "flag", AppConsts.FALSE);
            } else {
                ParamUtil.setRequestAttr(bpc.request,"flag",AppConsts.TRUE);
            }
        }
        ParamUtil.setSessionAttr(bpc.request, "apptFeConfirmDateDto", apptFeConfirmDateDto);
    }

    private ApptFeConfirmDateDto setHoursAndAmPm(ApptFeConfirmDateDto apptFeConfirmDateDto, List<SelectOption> hoursOption, List<SelectOption> amPmOption, String apptAmPm) {
        boolean flagContain = containValueInList(apptAmPm, amPmOption);
        if(flagContain){
            apptFeConfirmDateDto.setAmPm(apptAmPm);
        } else {
            apptFeConfirmDateDto.setAmPm(null);
        }
        String hours = null;
        if(Formatter.DAY_PM.equals(apptAmPm)){
            hours = "09";
        } else if(Formatter.DAY_AM.equals(apptAmPm)) {
            hours = "14";
        }
        apptFeConfirmDateDto.setHours(hours);
        return apptFeConfirmDateDto;
    }

    private boolean containValueInList(String str, List<SelectOption> optionList) {
        if(!StringUtil.isEmpty(str) && !IaisCommonUtils.isEmpty(optionList)){
            for(SelectOption so : optionList){
                if(str.equals(so.getValue())){
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * StartStep: userConfirmInspDateRejSucc
     *
     * @param bpc
     * @throws
     */
    public void userConfirmInspDateRejSucc(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the userConfirmInspDateRejSucc start ...."));
        ApptFeConfirmDateDto apptFeConfirmDateDto = (ApptFeConfirmDateDto) ParamUtil.getSessionAttr(bpc.request, "apptFeConfirmDateDto");
        String reason = ParamUtil.getRequestString(bpc.request, "apptRejectReason");
        Date saveDate = getSaveDate(apptFeConfirmDateDto);
        apptFeConfirmDateDto.setSaveDate(saveDate);
        apptFeConfirmDateDto.setReason(reason);
        applicantConfirmInspDateService.rejectSystemDateAndCreateTask(apptFeConfirmDateDto);
        String messageId = (String) ParamUtil.getSessionAttr(bpc.request, AppConsts.SESSION_INTER_INBOX_MESSAGE_ID);
        inspecUserRecUploadService.updateMessageStatus(messageId, MessageConstants.MESSAGE_STATUS_RESPONSE);
        ParamUtil.setSessionAttr(bpc.request, "apptFeConfirmDateDto", apptFeConfirmDateDto);
        ParamUtil.setRequestAttr(bpc.request, HcsaLicenceFeConstant.DASHBOARDTITLE,"Requests a Particular Date");
    }

    private Date getSaveDate(ApptFeConfirmDateDto apptFeConfirmDateDto) {
        Date saveDate = null;
        SimpleDateFormat sdf = new SimpleDateFormat(AppConsts.DEFAULT_DATE_FORMAT);
        Date sub_date1 = null;
        try {
            sub_date1 = sdf.parse(apptFeConfirmDateDto.getRejectDate());
        } catch (ParseException e) {
            log.error(e.getMessage(), e);
        }
        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sdf3 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String sub_date = sdf2.format(sub_date1);
        sub_date = sub_date + " " + apptFeConfirmDateDto.getHours() + ":00:00";
        try {
            saveDate = sdf3.parse(sub_date);
        } catch (ParseException e) {
            log.error(e.getMessage(), e);
        }
        return saveDate;
    }
}

package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.inspection.InspectionConstants;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.application.ApplicationViewDto;
import com.ecquaria.cloud.moh.iais.common.dto.appointment.ApptInspectionDateDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.utils.Formatter;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.common.validation.dto.ValidationResult;
import com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant;
import com.ecquaria.cloud.moh.iais.helper.AccessUtil;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.WebValidationHelper;
import com.ecquaria.cloud.moh.iais.service.ApplicationViewService;
import com.ecquaria.cloud.moh.iais.service.ApptInspectionDateService;
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
 * @Process: MohApptInspectionDate
 *
 * @author Shicheng
 * @date 2020/2/10 16:29
 **/
@Delegator(value = "apptInspectionDateDelegator")
@Slf4j
public class ApptInspectionDateDelegator {

    @Autowired
    private ApptInspectionDateService apptInspectionDateService;

    @Autowired
    private ApplicationViewService applicationViewService;

    @Autowired
    private ApptInspectionDateDelegator(ApplicationViewService applicationViewService, ApptInspectionDateService apptInspectionDateService){
        this.apptInspectionDateService = apptInspectionDateService;
        this.applicationViewService = applicationViewService;
    }
    /**
     * StartStep: apptInspectionDateStart
     *
     * @param bpc
     * @throws
     */
    public void apptInspectionDateStart(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the apptInspectionDateStart start ...."));
        AccessUtil.initLoginUserInfo(bpc.request);
        AuditTrailHelper.auditFunction("Appointment Inspection Date", "Appointment Inspection Date");
    }

    /**
     * StartStep: apptInspectionDateInit
     *
     * @param bpc
     * @throws
     */
    public void apptInspectionDateInit(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the apptInspectionDateInit start ...."));
        ParamUtil.setSessionAttr(bpc.request, "apptInspectionDateDto", null);
        ParamUtil.setSessionAttr(bpc.request, "hours", null);
        ParamUtil.setSessionAttr(bpc.request, "amPm", null);
        ParamUtil.setSessionAttr(bpc.request, "applicationViewDto", null);
    }

    /**
     * StartStep: apptInspectionDatePre
     *
     * @param bpc
     * @throws
     */
    public void apptInspectionDatePre(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the apptInspectionDatePre start ...."));
        ApptInspectionDateDto apptInspectionDateDto = (ApptInspectionDateDto) ParamUtil.getSessionAttr(bpc.request, "apptInspectionDateDto");
        if(apptInspectionDateDto == null){
            String taskId = ParamUtil.getRequestString(bpc.request, "taskId");
            ApplicationViewDto applicationViewDto = applicationViewService.getApplicationViewDtoByCorrId(taskId);
            apptInspectionDateDto = new ApptInspectionDateDto();
            apptInspectionDateDto  = apptInspectionDateService.getInspectionDate(taskId, apptInspectionDateDto);
            ParamUtil.setSessionAttr(bpc.request, "applicationViewDto", applicationViewDto);
        } else {
            Map<ApplicationDto, List<String>> applicationInfoMap = apptInspectionDateService.getApplicationInfoToShow(apptInspectionDateDto.getRefNo(), apptInspectionDateDto.getTaskDtos());
            apptInspectionDateDto.setApplicationInfoShow(applicationInfoMap);
        }
        String actionButtonFlag = apptInspectionDateService.getActionButtonFlag(apptInspectionDateDto);
        apptInspectionDateDto.setActionButtonFlag(actionButtonFlag);

        ParamUtil.setSessionAttr(bpc.request, "apptInspectionDateDto", apptInspectionDateDto);
    }

    /**
     * StartStep: apptInspectionDateStep1
     *
     * @param bpc
     * @throws
     */
    public void apptInspectionDateStep1(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the apptInspectionDateStep1 start ...."));
        ApptInspectionDateDto apptInspectionDateDto = (ApptInspectionDateDto) ParamUtil.getSessionAttr(bpc.request, "apptInspectionDateDto");
        ParamUtil.setSessionAttr(bpc.request, "apptInspectionDateDto", apptInspectionDateDto);
    }

    /**
     * StartStep: apptInspectionDateSpec
     *
     * @param bpc
     * @throws
     */
    public void apptInspectionDateSpec(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the apptInspectionDateSpec start ...."));
        ApptInspectionDateDto apptInspectionDateDto = (ApptInspectionDateDto) ParamUtil.getSessionAttr(bpc.request, "apptInspectionDateDto");
        List<SelectOption> hours = apptInspectionDateService.getInspectionDateHours();
        List<SelectOption> amPm = apptInspectionDateService.getAmPmOption();
        ParamUtil.setSessionAttr(bpc.request, "hours", (Serializable) hours);
        ParamUtil.setSessionAttr(bpc.request, "amPm", (Serializable) amPm);
        ParamUtil.setSessionAttr(bpc.request, "apptInspectionDateDto", apptInspectionDateDto);
    }

    /**
     * StartStep: apptInspectionDateVali
     *
     * @param bpc
     * @throws
     */
    public void apptInspectionDateVali(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the apptInspectionDateVali start ...."));
        ApptInspectionDateDto apptInspectionDateDto = (ApptInspectionDateDto) ParamUtil.getSessionAttr(bpc.request, "apptInspectionDateDto");
        String processDec = ParamUtil.getRequestString(bpc.request, "processDec");
        apptInspectionDateDto.setProcessDec(processDec);
        apptInspectionDateDto = getValidateValue(apptInspectionDateDto, bpc);
        ValidationResult validationResult;
        if(processDec.equals(InspectionConstants.PROCESS_DECI_ASSIGN_SPECIFIC_DATE)) {
            validationResult = WebValidationHelper.validateProperty(apptInspectionDateDto,"specific");
        } else {
            validationResult = WebValidationHelper.validateProperty(apptInspectionDateDto,"lead");
        }
        if (validationResult.isHasErrors()) {
            Map<String, String> errorMap = validationResult.retrieveAll();
            ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errorMap));
            ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.ISVALID, IaisEGPConstant.NO);
            ParamUtil.setRequestAttr(bpc.request, "flag", AppConsts.FALSE);
        } else {
            ParamUtil.setRequestAttr(bpc.request,"flag",AppConsts.TRUE);
        }
        ParamUtil.setSessionAttr(bpc.request, "apptInspectionDateDto", apptInspectionDateDto);
    }

    private ApptInspectionDateDto getValidateValue(ApptInspectionDateDto apptInspectionDateDto, BaseProcessClass bpc) {
        String specificDate1 = ParamUtil.getDate(bpc.request, "specificDate");
        String strHours = ParamUtil.getRequestString(bpc.request, "hours");
        String amPm = ParamUtil.getRequestString(bpc.request, "amPm");
        List<SelectOption> hoursOption = (List<SelectOption>)ParamUtil.getSessionAttr(bpc.request, "hours");
        List<SelectOption> amPmOption = (List<SelectOption>)ParamUtil.getSessionAttr(bpc.request, "amPm");
        boolean flagContain = containValueInList(amPm, amPmOption);
        if(flagContain){
            apptInspectionDateDto.setAmPm(amPm);
        } else {
            apptInspectionDateDto.setAmPm(null);
        }
        if(containValueInList(strHours, hoursOption)){
            try {
                int hours = Integer.parseInt(strHours);
                if(Formatter.DAY_PM.equals(amPm)){
                    hours = hours + 12;
                }
                if(hours < 10) {
                    apptInspectionDateDto.setHours("0" + hours);
                } else {
                    apptInspectionDateDto.setHours(hours + "");
                }

            } catch (Exception e){
                e.printStackTrace();
                apptInspectionDateDto.setHours(null);
            }
        } else {
            apptInspectionDateDto.setHours(null);
        }
        Date specificDate = getSpecificDate(specificDate1, apptInspectionDateDto);
        if(specificDate != null){
            apptInspectionDateDto.setSpecificDate(specificDate);
        }
        return apptInspectionDateDto;
    }

    private Date getSpecificDate(String specificDate1, ApptInspectionDateDto apptInspectionDateDto) {
        Date specificDate = null;
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date sub_date1 = null;
        try {
            sub_date1 = sdf.parse(specificDate1);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sdf3 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String sub_date = sdf2.format(sub_date1);
        sub_date = sub_date + " " + apptInspectionDateDto.getHours() + ":00:00";
        try {
            specificDate = sdf3.parse(sub_date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return specificDate;
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
     * StartStep: apptInspectionDateSuccess
     *
     * @param bpc
     * @throws
     */
    public void apptInspectionDateSuccess(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the apptInspectionDateSuccess start ...."));
        ApptInspectionDateDto apptInspectionDateDto = (ApptInspectionDateDto) ParamUtil.getSessionAttr(bpc.request, "apptInspectionDateDto");
        ApplicationViewDto applicationViewDto = (ApplicationViewDto)ParamUtil.getSessionAttr(bpc.request, "applicationViewDto");
        if(InspectionConstants.PROCESS_DECI_ASSIGN_SPECIFIC_DATE.equals(apptInspectionDateDto.getProcessDec())){
            apptInspectionDateService.saveLeadSpecificDate(apptInspectionDateDto, applicationViewDto);
        } else if(InspectionConstants.PROCESS_DECI_ALLOW_SYSTEM_TO_PROPOSE_DATE.equals(apptInspectionDateDto.getProcessDec())) {
            apptInspectionDateService.saveSystemInspectionDate(apptInspectionDateDto, applicationViewDto);
        }
        ParamUtil.setSessionAttr(bpc.request, "apptInspectionDateDto", apptInspectionDateDto);
        ParamUtil.setSessionAttr(bpc.request, "applicationViewDto", applicationViewDto);
    }
}

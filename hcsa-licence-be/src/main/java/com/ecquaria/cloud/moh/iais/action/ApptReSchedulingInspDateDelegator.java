package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.inspection.InspectionConstants;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.application.ApplicationViewDto;
import com.ecquaria.cloud.moh.iais.common.dto.appointment.ApptInspectionDateDto;
import com.ecquaria.cloud.moh.iais.common.utils.Formatter;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.common.validation.dto.ValidationResult;
import com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant;
import com.ecquaria.cloud.moh.iais.dto.LoginContext;
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
 * @Process: MohApptReSchedulingInspDate
 *
 * @author Shicheng
 * @date 2020/2/19 10:52
 **/
@Delegator(value = "apptReSchedulingInspDateDelegator")
@Slf4j
public class ApptReSchedulingInspDateDelegator {

    @Autowired
    private ApptInspectionDateService apptInspectionDateService;

    @Autowired
    private ApplicationViewService applicationViewService;

    @Autowired
    private ApptReSchedulingInspDateDelegator(ApplicationViewService applicationViewService, ApptInspectionDateService apptInspectionDateService){
        this.apptInspectionDateService = apptInspectionDateService;
        this.applicationViewService = applicationViewService;
    }

    /**
     * StartStep: apptReSchInspDateStart
     *
     * @param bpc
     * @throws
     */
    public void apptReSchInspDateStart(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the apptReSchInspDateStart start ...."));
        AccessUtil.initLoginUserInfo(bpc.request);
        AuditTrailHelper.auditFunction("Re-Scheduling Appointment Inspection Date", "Re-Scheduling Appointment Inspection Date");
    }

    /**
     * StartStep: apptReSchInspDateInit
     *
     * @param bpc
     * @throws
     */
    public void apptReSchInspDateInit(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the apptReSchInspDateInit start ...."));
        ParamUtil.setSessionAttr(bpc.request, "apptInspectionDateDto", null);
        ParamUtil.setSessionAttr(bpc.request, "hours", null);
        ParamUtil.setSessionAttr(bpc.request, "amPm", null);
    }

    /**
     * StartStep: apptReSchInspDatePre
     *
     * @param bpc
     * @throws
     */
    public void apptReSchInspDatePre(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the apptReSchInspDatePre start ...."));
        ApptInspectionDateDto apptInspectionDateDto = (ApptInspectionDateDto) ParamUtil.getSessionAttr(bpc.request, "apptInspectionDateDto");
        if(apptInspectionDateDto == null){
            String taskId = ParamUtil.getRequestString(bpc.request, "taskId");
            ApplicationViewDto applicationViewDto = applicationViewService.getApplicationViewDtoByCorrId(taskId);
            apptInspectionDateDto = new ApptInspectionDateDto();
            apptInspectionDateDto  = apptInspectionDateService.getApptSpecificDate(taskId, apptInspectionDateDto);
            ParamUtil.setSessionAttr(bpc.request, "applicationViewDto", applicationViewDto);
        }
        List<SelectOption> processDecList = apptInspectionDateService.getReShProcessDecList(apptInspectionDateDto);
        List<SelectOption> hours = apptInspectionDateService.getInspectionDateHours();
        List<SelectOption> amPm = apptInspectionDateService.getAmPmOption();
        ParamUtil.setSessionAttr(bpc.request, "apptInspectionDateDto", apptInspectionDateDto);
        ParamUtil.setSessionAttr(bpc.request, "inspecProDec", (Serializable) processDecList);
        ParamUtil.setSessionAttr(bpc.request, "hours", (Serializable) hours);
        ParamUtil.setSessionAttr(bpc.request, "amPm", (Serializable) amPm);
    }

    /**
     * StartStep: apptReSchInspDateVali
     *
     * @param bpc
     * @throws
     */
    public void apptReSchInspDateVali(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the apptReSchInspDateVali start ...."));
        ApptInspectionDateDto apptInspectionDateDto = (ApptInspectionDateDto) ParamUtil.getSessionAttr(bpc.request, "apptInspectionDateDto");
        List<SelectOption> processDecList = (List<SelectOption>)ParamUtil.getSessionAttr(bpc.request, "apptInspectionDateDto");
        String processDec = ParamUtil.getRequestString(bpc.request, "processDec");
        boolean processFlag = containValueInList(processDec, processDecList);
        if(StringUtil.isEmpty(processDec) || !processFlag){
            apptInspectionDateDto.setProcessDec(null);
        } else {
            apptInspectionDateDto.setProcessDec(processDec);
        }
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
     * StartStep: apptReSchInspDateSuccess
     *
     * @param bpc
     * @throws
     */
    public void apptReSchInspDateSuccess(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the apptInspectionDateSuccess start ...."));
        ApptInspectionDateDto apptInspectionDateDto = (ApptInspectionDateDto) ParamUtil.getSessionAttr(bpc.request, "apptInspectionDateDto");
        LoginContext loginContext = (LoginContext)ParamUtil.getSessionAttr(bpc.request, AppConsts.SESSION_ATTR_LOGIN_USER);
        apptInspectionDateService.saveSpecificDateLast(apptInspectionDateDto, loginContext);
        ParamUtil.setSessionAttr(bpc.request, "apptInspectionDateDto", apptInspectionDateDto);
    }
}

package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.inspection.InspectionConstants;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.application.ApplicationViewDto;
import com.ecquaria.cloud.moh.iais.common.dto.appointment.ApptInspectionDateDto;
import com.ecquaria.cloud.moh.iais.common.dto.task.TaskDto;
import com.ecquaria.cloud.moh.iais.common.utils.Formatter;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.common.validation.dto.ValidationResult;
import com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant;
import com.ecquaria.cloud.moh.iais.dto.LoginContext;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.WebValidationHelper;
import com.ecquaria.cloud.moh.iais.service.ApplicationViewService;
import com.ecquaria.cloud.moh.iais.service.ApptInspectionDateService;
import com.ecquaria.cloud.moh.iais.service.TaskService;
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
    private TaskService taskService;

    @Autowired
    private ApptReSchedulingInspDateDelegator(TaskService taskService, ApplicationViewService applicationViewService, ApptInspectionDateService apptInspectionDateService){
        this.apptInspectionDateService = apptInspectionDateService;
        this.applicationViewService = applicationViewService;
        this.taskService = taskService;
    }

    /**
     * StartStep: apptReSchInspDateStart
     *
     * @param bpc
     * @throws
     */
    public void apptReSchInspDateStart(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the apptReSchInspDateStart start ...."));
    }

    /**
     * StartStep: apptReSchInspDateInit
     *
     * @param bpc
     * @throws
     */
    public void apptReSchInspDateInit(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the apptReSchInspDateInit start ...."));
        String taskId = ParamUtil.getMaskedString(bpc.request, "taskId");
        TaskDto taskDto = taskService.getTaskById(taskId);
        ParamUtil.setSessionAttr(bpc.request, "taskDto", taskDto);
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
        TaskDto taskDto = (TaskDto) ParamUtil.getSessionAttr(bpc.request, "taskDto");
        if(apptInspectionDateDto == null){
            ApplicationViewDto applicationViewDto = applicationViewService.getApplicationViewDtoByCorrId(taskDto.getRefNo());
            apptInspectionDateDto = new ApptInspectionDateDto();
            apptInspectionDateDto = apptInspectionDateService.getApptSpecificDate(taskDto.getId(), apptInspectionDateDto);
            AuditTrailHelper.auditFunctionWithAppNo("Re-Scheduling Appointment Inspection Date", "Re-Scheduling Appointment Inspection Date",
                    applicationViewDto.getApplicationDto().getApplicationNo());
            ParamUtil.setSessionAttr(bpc.request, "applicationViewDto", applicationViewDto);
            ParamUtil.setSessionAttr(bpc.request, "taskDto", taskDto);
        }
        List<SelectOption> processDecList = apptInspectionDateService.getReShProcessDecList(apptInspectionDateDto);
        List<SelectOption> hours = apptInspectionDateService.getInspectionDateHours();
        ParamUtil.setSessionAttr(bpc.request, "apptInspectionDateDto", apptInspectionDateDto);
        ParamUtil.setSessionAttr(bpc.request, "inspecProDec", (Serializable) processDecList);
        ParamUtil.setSessionAttr(bpc.request, "hoursOption", (Serializable) hours);
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
        String processDec = InspectionConstants.PROCESS_DECI_ASSIGN_SPECIFIC_DATE;
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
        String specificStartDate = ParamUtil.getDate(bpc.request, "specificStartDate");
        String specificEndDate = ParamUtil.getDate(bpc.request, "specificEndDate");
        String startHours = ParamUtil.getRequestString(bpc.request, "startHours");
        String endHours = ParamUtil.getRequestString(bpc.request, "endHours");
        List<SelectOption> hoursOption = (List<SelectOption>)ParamUtil.getSessionAttr(bpc.request, "hoursOption");
        if(containValueInList(startHours, hoursOption)){
            apptInspectionDateDto.setStartHours(startHours);
        } else {
            apptInspectionDateDto.setStartHours(null);
        }
        if(containValueInList(endHours, hoursOption)){
            apptInspectionDateDto.setEndHours(endHours);
        } else {
            apptInspectionDateDto.setEndHours(null);
        }
        Date startDate = getSpecificDate(specificStartDate, hoursOption, startHours);
        Date endDate = getSpecificDate(specificEndDate, hoursOption, endHours);
        if(startDate != null){
            apptInspectionDateDto.setSpecificStartDate(startDate);
        }
        if(endDate != null){
            apptInspectionDateDto.setSpecificEndDate(endDate);
        }
        return apptInspectionDateDto;
    }

    private Date getSpecificDate(String specificDate1, List<SelectOption> hoursOption, String hours) {
        if(specificDate1 != null) {
            Date specificDate = null;
            Date sub_date1 = null;
            try {
                sub_date1 = Formatter.parseDate(specificDate1);
            } catch (ParseException e) {
                log.error(e.getMessage(), e);
            }
            SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat sdf3 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            StringBuilder subDate = new StringBuilder();
            subDate.append(sdf2.format(sub_date1)) ;
            if(!StringUtil.isEmpty(hours)) {
                for(SelectOption so : hoursOption){
                    if(hours.equals(so.getValue())){
                        subDate.append(" ").append(so.getText()).append(":00");
                    }
                }
            } else {
                subDate.append( " " + ":00:00");
            }
            try {
                specificDate = sdf3.parse(subDate.toString());
            } catch (ParseException e) {
                log.error(e.getMessage(), e);
            }
            return specificDate;
        }
        return null;
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

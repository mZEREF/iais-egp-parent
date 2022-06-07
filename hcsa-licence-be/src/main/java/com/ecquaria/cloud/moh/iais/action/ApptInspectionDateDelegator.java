package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.constant.AuditTrailConsts;
import com.ecquaria.cloud.moh.iais.common.constant.inspection.InspectionConstants;
import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.application.ApplicationViewDto;
import com.ecquaria.cloud.moh.iais.common.dto.appointment.AppPremInspApptDraftDto;
import com.ecquaria.cloud.moh.iais.common.dto.appointment.ApptAppInfoShowDto;
import com.ecquaria.cloud.moh.iais.common.dto.appointment.ApptInspectionDateDto;
import com.ecquaria.cloud.moh.iais.common.dto.task.TaskDto;
import com.ecquaria.cloud.moh.iais.common.mask.MaskAttackException;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.common.validation.dto.ValidationResult;
import com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.helper.MasterCodeUtil;
import com.ecquaria.cloud.moh.iais.helper.WebValidationHelper;
import com.ecquaria.cloud.moh.iais.service.ApplicationViewService;
import com.ecquaria.cloud.moh.iais.service.ApptInspectionDateService;
import com.ecquaria.cloud.moh.iais.service.InspectionService;
import com.ecquaria.cloud.moh.iais.service.TaskService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sop.webflow.rt.api.BaseProcessClass;

import java.io.IOException;
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
    private TaskService taskService;

    @Autowired
    private InspectionService inspectionService;

    /**
     * StartStep: apptInspectionDateStart
     *
     * @param bpc
     * @throws
     */
    public void apptInspectionDateStart(BaseProcessClass bpc) {
        log.debug(StringUtil.changeForLog("the apptInspectionDateStart start ...."));
        String taskId = "";
        try {
            taskId = ParamUtil.getMaskedString(bpc.request, "taskId");
        } catch (MaskAttackException e) {
            log.error(e.getMessage(), e);
            try {
                IaisEGPHelper.redirectUrl(bpc.response, "https://" + bpc.request.getServerName() + "/hcsa-licence-web/CsrfErrorPage.jsp");
            } catch (IOException ioe) {
                log.error(ioe.getMessage(), ioe);
                return;
            }
        }
        TaskDto taskDto = taskService.getTaskById(taskId);
        ParamUtil.setSessionAttr(bpc.request, "taskDto", taskDto);
        SearchParam searchParamGroup = (SearchParam)ParamUtil.getSessionAttr(bpc.request, "backendinboxSearchParam");
        ParamUtil.setSessionAttr(bpc.request,"backSearchParamFromHcsaApplication",searchParamGroup);
        AuditTrailHelper.auditFunction(AuditTrailConsts.MODULE_INSPECTION,  AuditTrailConsts.FUNCTION_ONLINE_APPOINTMENT);
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
        ParamUtil.setSessionAttr(bpc.request, "hoursOption", null);
        ParamUtil.setSessionAttr(bpc.request, "endHoursOption", null);
        ParamUtil.setSessionAttr(bpc.request, "amPmOption", null);
        ParamUtil.setSessionAttr(bpc.request, "applicationViewDto", null);
        ParamUtil.setSessionAttr(bpc.request, "scheduledInspApptDraftDtos", null);
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
        TaskDto taskDto = (TaskDto) ParamUtil.getSessionAttr(bpc.request, "taskDto");
        ApplicationViewDto applicationViewDto;
        if(apptInspectionDateDto == null){
            applicationViewDto = applicationViewService.getApplicationViewDtoByCorrId(taskDto.getRefNo(), taskDto.getRoleId());
            apptInspectionDateDto = new ApptInspectionDateDto();
            //set application info show
            apptInspectionDateDto = apptInspectionDateService.getInspectionDate(taskDto, apptInspectionDateDto, applicationViewDto);
            //get inspection appt draft
            List<AppPremInspApptDraftDto> appPremInspApptDraftDtos = apptInspectionDateService.getInspApptDraftBySamePremises(apptInspectionDateDto);
            ParamUtil.setSessionAttr(bpc.request, "applicationViewDto", applicationViewDto);
            ParamUtil.setSessionAttr(bpc.request, "scheduledInspApptDraftDtos", (Serializable) appPremInspApptDraftDtos);
        } else {
            applicationViewDto = (ApplicationViewDto) ParamUtil.getSessionAttr(bpc.request, "applicationViewDto");
            List<ApptAppInfoShowDto> apptAppInfoShowDtos = apptInspectionDateService.getApplicationInfoToShow(apptInspectionDateDto.getRefNo(), apptInspectionDateDto.getTaskDtos(), null);
            apptInspectionDateDto.setApplicationInfoShow(apptAppInfoShowDtos);
        }
        String actionButtonFlag = apptInspectionDateService.getActionButtonFlag(apptInspectionDateDto, applicationViewDto.getApplicationDto());
        apptInspectionDateDto.setActionButtonFlag(actionButtonFlag);

        ParamUtil.setSessionAttr(bpc.request, "apptInspectionDateDto", apptInspectionDateDto);
        ParamUtil.setSessionAttr(bpc.request, "taskDto", taskDto);

        // spec date
        List<SelectOption> hours = apptInspectionDateService.getInspectionDateHours();
        List<SelectOption> endHours = apptInspectionDateService.getInspectionDateEndHours();
        ParamUtil.setSessionAttr(bpc.request, "hoursOption", (Serializable) hours);
        ParamUtil.setSessionAttr(bpc.request, "endHoursOption", (Serializable) endHours);

        List<String> processDecValues = IaisCommonUtils.genNewArrayList();
        if (AppConsts.TRUE.equals(apptInspectionDateDto.getSysInspDateFlag())) {
            processDecValues.add(InspectionConstants.PROCESS_DECI_ALLOW_SYSTEM_TO_PROPOSE_DATE);
        }
        if (AppConsts.TRUE.equals(apptInspectionDateDto.getSysSpecDateFlag())) {
            processDecValues.add(InspectionConstants.PROCESS_DECI_ASSIGN_SPECIFIC_DATE);
        }
        processDecValues.add(InspectionConstants.PROCESS_DECI_ROLL_BACK);
        ParamUtil.setRequestAttr(bpc.request, "nextStages", MasterCodeUtil.retrieveOptionsByCodes(processDecValues.toArray(new String[0])));

        //set rollback options
        List<SelectOption> rollBackStage = inspectionService.getRollBackSelectOptions(applicationViewDto.getRollBackHistroyList());
        ParamUtil.setSessionAttr(bpc.request, "rollBackOptions", (Serializable) rollBackStage);
    }

    /**
     * StartStep: apptInspectionDateStep1
     *
     * @param bpc
     * @throws
     */
    public void apptInspectionDateStep1(BaseProcessClass bpc) {
        log.debug(StringUtil.changeForLog("the apptInspectionDateStep1 start ...."));
        ApptInspectionDateDto apptInspectionDateDto = (ApptInspectionDateDto) ParamUtil.getSessionAttr(bpc.request, "apptInspectionDateDto");
        String processDec = ParamUtil.getRequestString(bpc.request, "processDec");
        apptInspectionDateDto.setProcessDec(processDec);
        ParamUtil.setSessionAttr(bpc.request, "apptInspectionDateDto", apptInspectionDateDto);

        Map<String, String> errorMap = IaisCommonUtils.genNewHashMap();
        if (InspectionConstants.PROCESS_DECI_ASSIGN_SPECIFIC_DATE.equals(processDec)) {
            validateSpecificDate(bpc, errorMap, apptInspectionDateDto);
        } else if (InspectionConstants.PROCESS_DECI_ROLL_BACK.equals(processDec)) {
            validateRollBack(bpc, errorMap);
        }
        if (!errorMap.isEmpty()) {
            ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errorMap));
            WebValidationHelper.saveAuditTrailForNoUseResult(errorMap);
            ParamUtil.setRequestAttr(bpc.request, "apptInspectionDateType", "back");
            return;
        }
        ParamUtil.setRequestAttr(bpc.request, "apptInspectionDateType", "success");
    }

    private void validateRollBack(BaseProcessClass bpc, Map<String, String> errorMap) {
        String rollBackTo = ParamUtil.getRequestString(bpc.request, "rollBackTo");
        if (StringUtil.isEmpty(rollBackTo)) {
            errorMap.put("rollBackTo", "GENERAL_ERR0006");
        }
    }

    private void validateSpecificDate(BaseProcessClass bpc, Map<String, String> errorMap, ApptInspectionDateDto apptInspectionDateDto) {
        apptInspectionDateDto = getValidateValue(apptInspectionDateDto, bpc);
        ValidationResult validationResult = WebValidationHelper.validateProperty(apptInspectionDateDto, "specific");
        if (validationResult.isHasErrors()) {
            errorMap.putAll(validationResult.retrieveAll());
        }
    }

    private ApptInspectionDateDto getValidateValue(ApptInspectionDateDto apptInspectionDateDto, BaseProcessClass bpc) {
        String specificStartDate = ParamUtil.getDate(bpc.request, "specificStartDate");
        String specificEndDate = ParamUtil.getDate(bpc.request, "specificEndDate");
        String startHours = ParamUtil.getRequestString(bpc.request, "startHours");
        String endHours = ParamUtil.getRequestString(bpc.request, "endHours");
        List<SelectOption> hoursOption = (List<SelectOption>)ParamUtil.getSessionAttr(bpc.request, "hoursOption");
        List<SelectOption> endHoursOption = (List<SelectOption>)ParamUtil.getSessionAttr(bpc.request, "endHoursOption");
        if(containValueInList(startHours, hoursOption)){
            apptInspectionDateDto.setStartHours(startHours);
        } else {
            apptInspectionDateDto.setStartHours(null);
        }
        if(containValueInList(endHours, endHoursOption)){
            apptInspectionDateDto.setEndHours(endHours);
        } else {
            apptInspectionDateDto.setEndHours(null);
        }
        Date startDate = getSpecificDate(specificStartDate, hoursOption, startHours);
        Date endDate = getSpecificDate(specificEndDate, endHoursOption, endHours);
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
            SimpleDateFormat sdf = new SimpleDateFormat(AppConsts.DEFAULT_DATE_FORMAT);
            Date sub_date1 = null;
            try {
                sub_date1 = sdf.parse(specificDate1);
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
                        subDate.append(' ').append(so.getText()).append(":00");
                    }
                }
            } else {
                subDate .append(' ').append(":00:00");
            }
            try {
                specificDate = sdf3.parse(subDate.toString());
            } catch (ParseException e) {
                log.info("SpecificDate Conversion failure");
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
     * StartStep: apptInspectionDateSuccess
     *
     * @param bpc
     * @throws
     */
    public void apptInspectionDateSuccess(BaseProcessClass bpc) throws CloneNotSupportedException {
        log.debug(StringUtil.changeForLog("the apptInspectionDateSuccess start ...."));
        ApptInspectionDateDto apptInspectionDateDto = (ApptInspectionDateDto) ParamUtil.getSessionAttr(bpc.request, "apptInspectionDateDto");
        ApplicationViewDto applicationViewDto = (ApplicationViewDto) ParamUtil.getSessionAttr(bpc.request, "applicationViewDto");
        String appType = applicationViewDto.getApplicationDto().getApplicationType();
        if (ApplicationConsts.APPLICATION_TYPE_CREATE_AUDIT_TASK.equals(appType)) {
            apptInspectionDateService.saveAuditInspectionDate(apptInspectionDateDto, applicationViewDto);
        } else {
            if (InspectionConstants.PROCESS_DECI_ASSIGN_SPECIFIC_DATE.equals(apptInspectionDateDto.getProcessDec())) {
                apptInspectionDateService.saveLeadSpecificDate(apptInspectionDateDto, applicationViewDto);
            } else if (InspectionConstants.PROCESS_DECI_ALLOW_SYSTEM_TO_PROPOSE_DATE.equals(apptInspectionDateDto.getProcessDec())) {
                apptInspectionDateService.saveSystemInspectionDate(apptInspectionDateDto, applicationViewDto);
                ParamUtil.setSessionAttr(bpc.request, "scheduledInspApptDraftDtos", null);
            } else if (InspectionConstants.PROCESS_DECI_ROLL_BACK.equals(apptInspectionDateDto.getProcessDec())) {
                int rollBackToIndex = ParamUtil.getInt(bpc.request, "rollBackTo");
                TaskDto taskDto = (TaskDto) ParamUtil.getSessionAttr(bpc.request, "taskDto");
                inspectionService.rollBack(bpc,taskDto,applicationViewDto,applicationViewDto.getRollBackHistroyList().get(rollBackToIndex));
                ParamUtil.setRequestAttr(bpc.request, "isRollBack", AppConsts.TRUE);
            }
        }
        apptInspectionDateService.saveAppUserCorrelation(apptInspectionDateDto);
        ParamUtil.setSessionAttr(bpc.request, "apptInspectionDateDto", apptInspectionDateDto);
        ParamUtil.setSessionAttr(bpc.request, "applicationViewDto", applicationViewDto);
    }
}

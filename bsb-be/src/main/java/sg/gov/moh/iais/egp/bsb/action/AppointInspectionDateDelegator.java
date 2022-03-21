package sg.gov.moh.iais.egp.bsb.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.appointment.AppointmentDto;
import com.ecquaria.cloud.moh.iais.common.dto.appointment.ApptInspectionDateDto;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import sg.gov.moh.iais.egp.bsb.client.BsbAppointmentClient;
import sg.gov.moh.iais.egp.bsb.constant.ValidationConstants;
import sg.gov.moh.iais.egp.bsb.constant.module.AppViewConstants;
import sg.gov.moh.iais.egp.bsb.dto.ResponseDto;
import sg.gov.moh.iais.egp.bsb.dto.appointment.AppointmentReviewDataDto;
import sg.gov.moh.iais.egp.bsb.dto.entity.InspectionAppointmentDraftDto;
import sg.gov.moh.iais.egp.bsb.dto.file.DocDisplayDto;
import sg.gov.moh.iais.egp.bsb.dto.validation.ValidationResultDto;
import sg.gov.moh.iais.egp.bsb.service.ApptInspectionDateService;
import sg.gov.moh.iais.egp.bsb.service.ProcessHistoryService;
import sg.gov.moh.iais.egp.bsb.util.MaskHelper;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static sg.gov.moh.iais.egp.bsb.constant.AppointmentConstants.*;
import static sg.gov.moh.iais.egp.bsb.constant.module.AppViewConstants.MODULE_VIEW_APPOINTMENT;
import static sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants.KEY_ACTION_TYPE;
import static sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants.KEY_TAB_DOCUMENT_SUPPORT_DOC_LIST;
import static sg.gov.moh.iais.egp.bsb.constant.module.TaskModuleConstants.PARAM_NAME_APP_ID;
import static sg.gov.moh.iais.egp.bsb.constant.module.TaskModuleConstants.PARAM_NAME_TASK_ID;

/**
 * @author tangtang
 * @date 2022/3/3 13:52
 */
@Slf4j
@Delegator(value = "appointInspectionDateDelegator")
public class AppointInspectionDateDelegator {

    private final BsbAppointmentClient bsbAppointmentClient;
    private final ProcessHistoryService processHistoryService;
    private final ApptInspectionDateService apptInspectionDateService;

    public AppointInspectionDateDelegator(BsbAppointmentClient bsbAppointmentClient, ProcessHistoryService processHistoryService, ApptInspectionDateService apptInspectionDateService) {
        this.bsbAppointmentClient = bsbAppointmentClient;
        this.processHistoryService = processHistoryService;
        this.apptInspectionDateService = apptInspectionDateService;
    }

    public void start(BaseProcessClass bpc) {
        AuditTrailHelper.auditFunction("Inspection", "Appoint Inspection Date");
        HttpServletRequest request = bpc.request;
        MaskHelper.taskProcessUnmask(request, PARAM_NAME_APP_ID, PARAM_NAME_TASK_ID);
        ParamUtil.setSessionAttr(request, APPOINTMENT_INSPECTION_DATE_DTO, null);
        ParamUtil.setSessionAttr(request, APPOINTMENT_REVIEW_DATA, null);
        ParamUtil.setSessionAttr(request, APPOINTMENT_INSPECTION_DATE_DRAFT_DTO, null);
        ParamUtil.setSessionAttr(request, KEY_TAB_DOCUMENT_SUPPORT_DOC_LIST, null);
        ParamUtil.setSessionAttr(request, KEY_START_HOURS_OPTION, null);
        ParamUtil.setSessionAttr(request, KEY_END_HOURS_OPTION, null);
    }

    public void prepareData(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        ParamUtil.setRequestAttr(request, "canNotUploadInternalDoc", true);
        AppointmentReviewDataDto dto = getReviewDataDto(request);
        if (StringUtils.isEmpty(dto.getApplicationId())) {
            String appId = (String) ParamUtil.getSessionAttr(request, PARAM_NAME_APP_ID);
            String taskId = (String) ParamUtil.getSessionAttr(request, PARAM_NAME_TASK_ID);
            ResponseDto<AppointmentReviewDataDto> result = bsbAppointmentClient.getOfficerReviewData(appId);
            if (result.ok()) {
                dto = result.getEntity();
                dto.setTaskId(taskId);
                ParamUtil.setSessionAttr(request, APPOINTMENT_REVIEW_DATA, dto);
                //show routingHistory list
                processHistoryService.getAndSetHistoryInSession(dto.getSubmissionDetailsDto().getApplicationNo(), request);
                // view application need appId and moduleType
                ParamUtil.setRequestAttr(request, AppViewConstants.MASK_PARAM_APP_ID, appId);
                ParamUtil.setRequestAttr(request, AppViewConstants.MASK_PARAM_APP_VIEW_MODULE_TYPE, MODULE_VIEW_APPOINTMENT);
                //
                List<DocDisplayDto> docDisplayDtoList = dto.getDocDisplayDtoList();
                ParamUtil.setSessionAttr(request, KEY_TAB_DOCUMENT_SUPPORT_DOC_LIST, (Serializable) docDisplayDtoList);
            } else {
                log.warn("get appointment API doesn't return ok, the response is {}", result);
                ParamUtil.setSessionAttr(request, APPOINTMENT_REVIEW_DATA, new AppointmentReviewDataDto());
            }
            setApptInspectionDateDto(taskId, request);
            setInspApptDraftDto(dto.getApplicationNo(), request);
        }
    }

    public void setApptInspectionDateDto(String taskId, HttpServletRequest request) {
        ApptInspectionDateDto apptInspectionDateDto = (ApptInspectionDateDto) ParamUtil.getSessionAttr(request, APPOINTMENT_INSPECTION_DATE_DTO);
        if (apptInspectionDateDto == null) {
            ResponseDto<ApptInspectionDateDto> result = bsbAppointmentClient.getInspectionDateDto(taskId);
            if (result.ok()) {
                apptInspectionDateDto = result.getEntity();
                ParamUtil.setSessionAttr(request, APPOINTMENT_INSPECTION_DATE_DTO, apptInspectionDateDto);
            } else {
                log.warn("get appointment API doesn't return ok, the response is {}", result);
                ParamUtil.setSessionAttr(request, APPOINTMENT_INSPECTION_DATE_DTO, new ApptInspectionDateDto());
            }
        }
    }

    public void setInspApptDraftDto(String appNo, HttpServletRequest request) {
        List<InspectionAppointmentDraftDto> draftDtoList = bsbAppointmentClient.getActiveAppointmentDraftData(appNo).getEntity();
        ParamUtil.setSessionAttr(request, APPOINTMENT_INSPECTION_DATE_DRAFT_DTO, (Serializable) draftDtoList);
    }

    public void preSwitch(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        ApptInspectionDateDto apptInspectionDateDto = (ApptInspectionDateDto) ParamUtil.getSessionAttr(request, APPOINTMENT_INSPECTION_DATE_DTO);
        String processDec = ParamUtil.getRequestString(request, "processDec");
        apptInspectionDateDto.setProcessDec(processDec);
        ParamUtil.setSessionAttr(request, APPOINTMENT_INSPECTION_DATE_DTO, apptInspectionDateDto);
    }

    public void apptInspectionDateSpec(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        log.debug(StringUtil.changeForLog("the apptInspectionDateSpec start ...."));
        AppointmentReviewDataDto reviewDataDto = getReviewDataDto(request);
        List<SelectOption> startHours = apptInspectionDateService.getInspectionDateStartHours();
        List<SelectOption> endHours = apptInspectionDateService.getInspectionDateEndHours();
        ParamUtil.setSessionAttr(request, KEY_START_HOURS_OPTION, (Serializable) startHours);
        ParamUtil.setSessionAttr(request, KEY_END_HOURS_OPTION, (Serializable) endHours);
        ParamUtil.setSessionAttr(request, APPOINTMENT_REVIEW_DATA, reviewDataDto);
    }

    public void validateSpecificDate(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        log.debug(StringUtil.changeForLog("the apptInspectionDateVali start ...."));
        AppointmentReviewDataDto dto = getReviewDataDto(request);
        String actionValue = ParamUtil.getRequestString(bpc.request, "actionValue");
        AppointmentDto specificApptDto = apptInspectionDateService.getValidateValue(dto, request);
        //validation
        String actionType;
        dto.setModule("specifyAppointmentDate");
        ValidationResultDto validationResultDto = bsbAppointmentClient.validateAppointmentData(dto);
        if ("back".equals(actionValue)) {
            actionType = "back";
        } else {
            if (!validationResultDto.isPass()) {
                ParamUtil.setRequestAttr(request, ValidationConstants.KEY_VALIDATION_ERRORS, validationResultDto.toErrorMsg());
                actionType = "prepare";
            } else {
                actionType = "next";
                Map<String, String> errMap = apptInspectionDateService.validateDateFromUserCalendar(specificApptDto);
                if (!CollectionUtils.isEmpty(errMap)){
                    validationResultDto.setPass(false);
                    validationResultDto.setErrorMap((HashMap<String, String>) errMap);
                    ParamUtil.setRequestAttr(request, ValidationConstants.KEY_VALIDATION_ERRORS, validationResultDto.toErrorMsg());
                    actionType = "prepare";
                }
            }
        }
        ParamUtil.setRequestAttr(request, KEY_ACTION_TYPE, actionType);
    }

    public void saveAppointmentDate(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        ApptInspectionDateDto apptInspectionDateDto = (ApptInspectionDateDto) ParamUtil.getSessionAttr(request, APPOINTMENT_INSPECTION_DATE_DTO);
        AppointmentReviewDataDto dto = (AppointmentReviewDataDto) ParamUtil.getSessionAttr(request, APPOINTMENT_REVIEW_DATA);
        if (PROCESS_DEC_SPECIFY_NEW_DATE.equals(apptInspectionDateDto.getProcessDec())) {
            log.info("user specify date");
            apptInspectionDateService.saveUserSpecificDate(apptInspectionDateDto, dto.getApplicationId());
        } else if (PROCESS_DEC_CONFIRM_DATE.equals(apptInspectionDateDto.getProcessDec())) {
            apptInspectionDateService.saveSystemInspectionDate(apptInspectionDateDto, dto.getApplicationId());
            ParamUtil.setSessionAttr(bpc.request, APPOINTMENT_INSPECTION_DATE_DRAFT_DTO, null);
        }
    }

    private AppointmentReviewDataDto getReviewDataDto(HttpServletRequest request) {
        AppointmentReviewDataDto appointmentReviewDataDto = (AppointmentReviewDataDto) ParamUtil.getSessionAttr(request, APPOINTMENT_REVIEW_DATA);
        return appointmentReviewDataDto == null ? getDefaultProcessDto() : appointmentReviewDataDto;
    }

    private AppointmentReviewDataDto getDefaultProcessDto() {
        return new AppointmentReviewDataDto();
    }
}

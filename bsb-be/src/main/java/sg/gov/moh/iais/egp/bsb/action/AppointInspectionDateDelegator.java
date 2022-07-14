package sg.gov.moh.iais.egp.bsb.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import sg.gov.moh.iais.egp.bsb.client.BsbAppointmentClient;
import sg.gov.moh.iais.egp.bsb.client.InspectionClient;
import sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants;
import sg.gov.moh.iais.egp.bsb.dto.ResponseDto;
import sg.gov.moh.iais.egp.bsb.dto.appointment.AppointmentReviewDataDto;
import sg.gov.moh.iais.egp.bsb.dto.appointment.BsbAppointmentDto;
import sg.gov.moh.iais.egp.bsb.dto.appointment.BsbApptInspectionDateDto;
import sg.gov.moh.iais.egp.bsb.dto.entity.InspectionInfoDto;
import sg.gov.moh.iais.egp.bsb.dto.file.DocDisplayDto;
import sg.gov.moh.iais.egp.bsb.dto.inspection.InsProcessDto;
import sg.gov.moh.iais.egp.bsb.dto.validation.ValidationResultDto;
import sg.gov.moh.iais.egp.bsb.service.AppViewService;
import sg.gov.moh.iais.egp.bsb.service.ApptInspectionDateService;
import sg.gov.moh.iais.egp.bsb.service.ProcessHistoryService;
import sg.gov.moh.iais.egp.bsb.util.MaskHelper;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import java.io.Serializable;
import java.util.List;

import static com.ecquaria.cloud.moh.iais.common.constant.BsbAuditTrailConstants.MODULE_INSPECTION;
import static sg.gov.moh.iais.egp.bsb.constant.AppointmentConstants.APPOINTMENT_INSPECTION_DATE_DTO;
import static sg.gov.moh.iais.egp.bsb.constant.AppointmentConstants.APPOINTMENT_REVIEW_DATA;
import static sg.gov.moh.iais.egp.bsb.constant.AppointmentConstants.BACK_URL;
import static sg.gov.moh.iais.egp.bsb.constant.AppointmentConstants.BACK_URL_TASK_LIST;
import static sg.gov.moh.iais.egp.bsb.constant.AppointmentConstants.INSPECTION_INFO_DTO;
import static sg.gov.moh.iais.egp.bsb.constant.AppointmentConstants.KEY_END_HOURS_OPTION;
import static sg.gov.moh.iais.egp.bsb.constant.AppointmentConstants.KEY_START_HOURS_OPTION;
import static sg.gov.moh.iais.egp.bsb.constant.AppointmentConstants.PROCESS_DEC_CONFIRM_DATE;
import static sg.gov.moh.iais.egp.bsb.constant.AppointmentConstants.PROCESS_DEC_SPECIFY_NEW_DATE;
import static com.ecquaria.cloud.moh.iais.common.constant.BsbAuditTrailConstants.FUNCTION_APPOINT_INSPECTION_DATE;
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
    private final InspectionClient inspectionClient;
    private final ProcessHistoryService processHistoryService;
    private final ApptInspectionDateService apptInspectionDateService;

    public AppointInspectionDateDelegator(BsbAppointmentClient bsbAppointmentClient, InspectionClient inspectionClient, ProcessHistoryService processHistoryService, ApptInspectionDateService apptInspectionDateService) {
        this.bsbAppointmentClient = bsbAppointmentClient;
        this.inspectionClient = inspectionClient;
        this.processHistoryService = processHistoryService;
        this.apptInspectionDateService = apptInspectionDateService;
    }

    public void start(BaseProcessClass bpc) {
        AuditTrailHelper.auditFunction(MODULE_INSPECTION, FUNCTION_APPOINT_INSPECTION_DATE);
        HttpServletRequest request = bpc.request;
        MaskHelper.taskProcessUnmask(request, PARAM_NAME_APP_ID, PARAM_NAME_TASK_ID);
        HttpSession session = request.getSession();
        session.removeAttribute(APPOINTMENT_INSPECTION_DATE_DTO);
        session.removeAttribute(APPOINTMENT_REVIEW_DATA);
        session.removeAttribute(KEY_TAB_DOCUMENT_SUPPORT_DOC_LIST);
        session.removeAttribute(KEY_START_HOURS_OPTION);
        session.removeAttribute(KEY_END_HOURS_OPTION);
        session.removeAttribute(INSPECTION_INFO_DTO);
    }

    public void prepareData(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        ParamUtil.setRequestAttr(request, "canNotUploadInternalDoc", Boolean.TRUE);
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
                //
                List<DocDisplayDto> docDisplayDtoList = dto.getDocDisplayDtoList();
                ParamUtil.setSessionAttr(request, KEY_TAB_DOCUMENT_SUPPORT_DOC_LIST, (Serializable) docDisplayDtoList);
            } else {
                log.warn("get appointment API doesn't return ok, the response is {}", result);
                ParamUtil.setSessionAttr(request, APPOINTMENT_REVIEW_DATA, new AppointmentReviewDataDto());
            }
            setApptInspectionDateDto(taskId, request);
            apptInspectionDateService.setInspectionInfoDto(appId,request);
        }
        // view application
        AppViewService.facilityRegistrationViewApp(request, dto.getApplicationId());
        ParamUtil.setRequestAttr(request, BACK_URL, BACK_URL_TASK_LIST);
    }

    public void skip(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        String appId = (String) ParamUtil.getSessionAttr(request, PARAM_NAME_APP_ID);
        String taskId = (String) ParamUtil.getSessionAttr(request, PARAM_NAME_TASK_ID);
        inspectionClient.skipInspection(appId,taskId,new InsProcessDto(MasterCodeConstants.MOH_PROCESSING_DECISION_SKIP_INSPECTION));
    }

    public void setApptInspectionDateDto(String taskId, HttpServletRequest request) {
        BsbApptInspectionDateDto bsbApptInspectionDateDto = (BsbApptInspectionDateDto) ParamUtil.getSessionAttr(request, APPOINTMENT_INSPECTION_DATE_DTO);
        if (bsbApptInspectionDateDto == null) {
            ResponseDto<BsbApptInspectionDateDto> result = bsbAppointmentClient.getInspectionDateDto(taskId);
            if (result.ok()) {
                bsbApptInspectionDateDto = result.getEntity();
                ParamUtil.setSessionAttr(request, APPOINTMENT_INSPECTION_DATE_DTO, bsbApptInspectionDateDto);
            } else {
                log.warn("get appointment API doesn't return ok, the response is {}", result);
                ParamUtil.setSessionAttr(request, APPOINTMENT_INSPECTION_DATE_DTO, new BsbApptInspectionDateDto());
            }
        }
    }

    public void preSwitch(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        BsbApptInspectionDateDto bsbApptInspectionDateDto = (BsbApptInspectionDateDto) ParamUtil.getSessionAttr(request, APPOINTMENT_INSPECTION_DATE_DTO);
        String processDec = ParamUtil.getRequestString(request, "processDec");
        bsbApptInspectionDateDto.setProcessDec(processDec);
        ParamUtil.setSessionAttr(request, APPOINTMENT_INSPECTION_DATE_DTO, bsbApptInspectionDateDto);
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
        BsbAppointmentDto specificApptDto = apptInspectionDateService.getValidateValue(dto, request);
        //validation
        dto.setModule("specifyAppointmentDate");
        ValidationResultDto validationResultDto = bsbAppointmentClient.validateAppointmentData(dto);
        String actionType = apptInspectionDateService.ensureValResult(actionValue,validationResultDto,request,specificApptDto);
        ParamUtil.setRequestAttr(request, KEY_ACTION_TYPE, actionType);
        ParamUtil.setSessionAttr(request, APPOINTMENT_REVIEW_DATA, dto);
    }

    public void saveAppointmentDate(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        InspectionInfoDto inspectionInfoDto = (InspectionInfoDto) ParamUtil.getSessionAttr(request, INSPECTION_INFO_DTO);
        BsbApptInspectionDateDto bsbApptInspectionDateDto = (BsbApptInspectionDateDto) ParamUtil.getSessionAttr(request, APPOINTMENT_INSPECTION_DATE_DTO);
        if (PROCESS_DEC_SPECIFY_NEW_DATE.equals(bsbApptInspectionDateDto.getProcessDec())) {
            log.info("user specify date");
            apptInspectionDateService.saveUserSpecificDate(bsbApptInspectionDateDto,inspectionInfoDto);
        } else if (PROCESS_DEC_CONFIRM_DATE.equals(bsbApptInspectionDateDto.getProcessDec())) {
            apptInspectionDateService.saveSystemInspectionDate(bsbApptInspectionDateDto,inspectionInfoDto);
        }
        ParamUtil.setRequestAttr(request, BACK_URL, BACK_URL_TASK_LIST);
    }

    private AppointmentReviewDataDto getReviewDataDto(HttpServletRequest request) {
        AppointmentReviewDataDto appointmentReviewDataDto = (AppointmentReviewDataDto) ParamUtil.getSessionAttr(request, APPOINTMENT_REVIEW_DATA);
        return appointmentReviewDataDto == null ? getDefaultProcessDto() : appointmentReviewDataDto;
    }

    private AppointmentReviewDataDto getDefaultProcessDto() {
        return new AppointmentReviewDataDto();
    }
}

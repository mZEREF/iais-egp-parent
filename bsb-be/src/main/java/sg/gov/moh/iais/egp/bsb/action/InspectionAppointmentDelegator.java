package sg.gov.moh.iais.egp.bsb.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.exception.IaisRuntimeException;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import sg.gov.moh.iais.egp.bsb.client.InspectionClient;
import sg.gov.moh.iais.egp.bsb.client.RfiClient;
import sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants;
import sg.gov.moh.iais.egp.bsb.constant.module.ProcessContants;
import sg.gov.moh.iais.egp.bsb.dto.inspection.AppointmentReviewDataDto;
import sg.gov.moh.iais.egp.bsb.dto.process.DualDocSortingDto;
import sg.gov.moh.iais.egp.bsb.dto.rfi.PageAppEditSelectDto;
import sg.gov.moh.iais.egp.bsb.dto.rfi.RfiAppSelectDto;
import sg.gov.moh.iais.egp.bsb.dto.validation.ValidationResultDto;
import sg.gov.moh.iais.egp.bsb.service.ApptInspectionDateService;
import sg.gov.moh.iais.egp.bsb.service.DocSettingService;
import sg.gov.moh.iais.egp.bsb.service.DualDocSortingService;
import sg.gov.moh.iais.egp.bsb.service.RfiService;
import sg.gov.moh.iais.egp.bsb.util.MaskHelper;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.Serializable;
import java.util.List;

import static com.ecquaria.cloud.moh.iais.common.constant.BsbAuditTrailConstants.FUNCTION_APPOINT_INSPECTION_DATE;
import static com.ecquaria.cloud.moh.iais.common.constant.BsbAuditTrailConstants.MODULE_INSPECTION;
import static sg.gov.moh.iais.egp.bsb.constant.AppointmentConstants.APPOINTMENT_INSPECTION_DATE_DTO;
import static sg.gov.moh.iais.egp.bsb.constant.AppointmentConstants.APPOINTMENT_REVIEW_DATA;
import static sg.gov.moh.iais.egp.bsb.constant.AppointmentConstants.CRUD_ACTION_TYPE_NEXT;
import static sg.gov.moh.iais.egp.bsb.constant.AppointmentConstants.CRUD_ACTION_TYPE_PREPARE;
import static sg.gov.moh.iais.egp.bsb.constant.AppointmentConstants.KEY_END_HOURS_OPTION;
import static sg.gov.moh.iais.egp.bsb.constant.AppointmentConstants.KEY_START_HOURS_OPTION;
import static sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants.APP_STATUS_PEND_APPLICANT_CLARIFICATION;
import static sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants.APP_STATUS_PEND_APPOINTMENT_CONFIRMATION;
import static sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants.APP_STATUS_PEND_APPOINTMENT_SCHEDULING;
import static sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants.MOH_PROCESS_DECISION_CONFIRM_PROPOSED_DATE;
import static sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants.MOH_PROCESS_DECISION_REQUEST_FOR_INFORMATION;
import static sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants.MOH_PROCESS_DECISION_SELECT_ANOTHER_DATE;
import static sg.gov.moh.iais.egp.bsb.constant.module.InspectionConstants.KEY_SELF_ASSESSMENT_AVAILABLE;
import static sg.gov.moh.iais.egp.bsb.constant.module.InspectionConstants.TAB_ACTIVE;
import static sg.gov.moh.iais.egp.bsb.constant.module.InspectionConstants.TAB_PROCESSING;
import static sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants.INTERNAL_DOCUMENT_TYPE_LIST;
import static sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants.KEY_ACTION_TYPE;
import static sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants.KEY_CRUD_ACTION_TYPE;
import static sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants.KEY_DUAL_DOC_SORTING_INFO;
import static sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants.KEY_FACILITY_DETAILS_INFO;
import static sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants.KEY_ROUTING_HISTORY_LIST;
import static sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants.KEY_SUBMISSION_DETAILS_INFO;
import static sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants.KEY_TAB_DOCUMENT_SUPPORT_DOC_LIST;
import static sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants.KEY_VALIDATION_ERRORS;
import static sg.gov.moh.iais.egp.bsb.constant.module.RfiConstants.KEY_PAGE_APP_EDIT_SELECT_DTO;
import static sg.gov.moh.iais.egp.bsb.constant.module.RfiConstants.KEY_RFI_APP_SELECT_DTO;
import static sg.gov.moh.iais.egp.bsb.constant.module.TaskModuleConstants.PARAM_NAME_APP_ID;
import static sg.gov.moh.iais.egp.bsb.constant.module.TaskModuleConstants.PARAM_NAME_TASK_ID;


@Slf4j
@RequiredArgsConstructor
@Delegator(value = "inspectionAppointmentDelegator")
public class InspectionAppointmentDelegator {
    private final InspectionClient inspectionClient;
    private final RfiService rfiService;
    private final DocSettingService docSettingService;
    private final ApptInspectionDateService apptInspectionDateService;
    private final RfiClient rfiClient;
    private final DualDocSortingService dualDocSortingService;

    public void start(BaseProcessClass bpc) {
        AuditTrailHelper.auditFunction(MODULE_INSPECTION, FUNCTION_APPOINT_INSPECTION_DATE);
        HttpServletRequest request = bpc.request;
        MaskHelper.taskProcessUnmask(request, PARAM_NAME_APP_ID, PARAM_NAME_TASK_ID);
        HttpSession session = request.getSession();
        DualDocSortingService.clearDualDocSortingInfo(session);
        session.removeAttribute(APPOINTMENT_INSPECTION_DATE_DTO);
        session.removeAttribute(KEY_START_HOURS_OPTION);
        session.removeAttribute(KEY_END_HOURS_OPTION);

        session.removeAttribute(APPOINTMENT_REVIEW_DATA);
        session.removeAttribute(KEY_ROUTING_HISTORY_LIST);
        session.removeAttribute(KEY_TAB_DOCUMENT_SUPPORT_DOC_LIST);
        session.removeAttribute(KEY_SUBMISSION_DETAILS_INFO);
        session.removeAttribute(KEY_FACILITY_DETAILS_INFO);

        // if can select RFI section, need clear this, set in 'BeViewApplicationDelegator'
        session.removeAttribute(KEY_PAGE_APP_EDIT_SELECT_DTO);
        // set in 'rfiService'
        session.removeAttribute(KEY_RFI_APP_SELECT_DTO);
    }

    public void prepareData(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        String appId = (String) ParamUtil.getSessionAttr(request, PARAM_NAME_APP_ID);
        String taskId = (String) ParamUtil.getSessionAttr(request, PARAM_NAME_TASK_ID);
        AppointmentReviewDataDto dto = apptInspectionDateService.getReviewDataDto(request, appId, taskId);
        ParamUtil.setSessionAttr(request, APPOINTMENT_REVIEW_DATA, dto);
        ParamUtil.setSessionAttr(request, KEY_ROUTING_HISTORY_LIST, (Serializable) dto.getProcessHistoryDtoList());
        ParamUtil.setSessionAttr(request, KEY_SUBMISSION_DETAILS_INFO, dto.getSubmissionDetailsInfo());
        ParamUtil.setSessionAttr(request, KEY_FACILITY_DETAILS_INFO, dto.getFacilityDetailsInfo());
        //set internal doc types
        ParamUtil.setRequestAttr(request, INTERNAL_DOCUMENT_TYPE_LIST, docSettingService.getInternalDocTypeList());

        dualDocSortingService.retrieveDualDocDisplayDtoIntoRequest(request, appId);

        String appStatus = dto.getApplicationStatus();
        if (APP_STATUS_PEND_APPOINTMENT_SCHEDULING.equals(appStatus) || APP_STATUS_PEND_APPLICANT_CLARIFICATION.equals(appStatus)) {
            ParamUtil.setRequestAttr(request, KEY_SELF_ASSESSMENT_AVAILABLE, Boolean.TRUE);
        } else if (APP_STATUS_PEND_APPOINTMENT_CONFIRMATION.equals(appStatus)) {
            ParamUtil.setRequestAttr(request, KEY_SELF_ASSESSMENT_AVAILABLE, Boolean.TRUE);

            List<SelectOption> startHours = apptInspectionDateService.getInspectionDateStartHours();
            List<SelectOption> endHours = apptInspectionDateService.getInspectionDateEndHours();
            ParamUtil.setRequestAttr(request, KEY_START_HOURS_OPTION, startHours);
            ParamUtil.setRequestAttr(request, KEY_END_HOURS_OPTION, endHours);
        } else {
            // no pre-inspection checklisk commit by applicant
            ParamUtil.setRequestAttr(request, KEY_SELF_ASSESSMENT_AVAILABLE, Boolean.FALSE);
        }
    }

    public void prepareSwitch(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        String crudActionType;
        String actionType = ParamUtil.getString(request, KEY_ACTION_TYPE);
        if (ModuleCommonConstants.KEY_ACTION_TYPE_SORT.equals(actionType)) {
            DualDocSortingDto dualDocSortingDto = DualDocSortingService.retrieveDualDocSortingInfo(request);
            DualDocSortingService.readDualDocSortingInfo(request, dualDocSortingDto);
            ParamUtil.setSessionAttr(request, KEY_DUAL_DOC_SORTING_INFO, dualDocSortingDto);
            crudActionType = ProcessContants.CRUD_ACTION_TYPE_PREPARE;
            ParamUtil.setRequestAttr(request, ModuleCommonConstants.TAB_ACTIVE, ModuleCommonConstants.TAB_DOC);
        } else if (ModuleCommonConstants.KEY_SUBMIT.equals(actionType)) {
            AppointmentReviewDataDto dto = (AppointmentReviewDataDto) ParamUtil.getSessionAttr(request, APPOINTMENT_REVIEW_DATA);
            apptInspectionDateService.reqObjMappingAppointmentReviewDataDto(request, dto);
            ParamUtil.setSessionAttr(request, APPOINTMENT_REVIEW_DATA, dto);
            //validation
            PageAppEditSelectDto pageAppEditSelectDto  = (PageAppEditSelectDto) ParamUtil.getSessionAttr(request, KEY_PAGE_APP_EDIT_SELECT_DTO);
            RfiAppSelectDto rfiAppSelectDto = (RfiAppSelectDto) ParamUtil.getSessionAttr(request, KEY_RFI_APP_SELECT_DTO);
            ValidationResultDto validationResultDto = inspectionClient.validateAppointmentReviewDataDto(dto);
            rfiService.validateRfiSelection(dto.getProcessingDecision(), validationResultDto, pageAppEditSelectDto, rfiAppSelectDto);
            if (!validationResultDto.isPass()) {
                ParamUtil.setRequestAttr(request, KEY_VALIDATION_ERRORS, validationResultDto.toErrorMsg());
                crudActionType = CRUD_ACTION_TYPE_PREPARE;
                ParamUtil.setRequestAttr(request, TAB_ACTIVE, TAB_PROCESSING);
            } else {
                crudActionType = CRUD_ACTION_TYPE_NEXT;
            }
        } else {
            throw new IaisRuntimeException("Invalid action type");
        }
        ParamUtil.setRequestAttr(request, KEY_CRUD_ACTION_TYPE, crudActionType);
    }

    public void processData(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        String taskId = (String) ParamUtil.getSessionAttr(request, PARAM_NAME_TASK_ID);
        String appId = (String) ParamUtil.getSessionAttr(request, PARAM_NAME_APP_ID);
        AppointmentReviewDataDto dto = (AppointmentReviewDataDto) ParamUtil.getSessionAttr(request, APPOINTMENT_REVIEW_DATA);
        String processingDecision = dto.getProcessingDecision();
        switch (processingDecision) {
            case MOH_PROCESS_DECISION_CONFIRM_PROPOSED_DATE:
                inspectionClient.saveAppointmentConfirmProposedDate(appId, taskId, dto);
                break;
            case MOH_PROCESS_DECISION_SELECT_ANOTHER_DATE:
                inspectionClient.saveAppointmentSelectAnotherDate(appId, taskId, dto);
                break;
            case MOH_PROCESS_DECISION_REQUEST_FOR_INFORMATION:
                dto.setPageAppEditSelectDto((PageAppEditSelectDto) ParamUtil.getSessionAttr(request, KEY_PAGE_APP_EDIT_SELECT_DTO));
                dto.setRfiAppSelectDto((RfiAppSelectDto) ParamUtil.getSessionAttr(request, KEY_RFI_APP_SELECT_DTO));
                rfiClient.saveInspectionAppointmentRfi(dto, appId);
                break;
            default:
                log.info("don't have such processingDecision {}", org.apache.commons.lang.StringUtils.normalizeSpace(processingDecision));
                break;
        }
    }
}

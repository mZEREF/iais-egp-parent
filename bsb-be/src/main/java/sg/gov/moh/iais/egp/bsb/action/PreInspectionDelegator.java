package sg.gov.moh.iais.egp.bsb.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.utils.JsonUtil;
import com.ecquaria.cloud.moh.iais.common.utils.LogUtil;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import sg.gov.moh.iais.egp.bsb.client.InspectionClient;
import sg.gov.moh.iais.egp.bsb.client.RfiClient;
import sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants;
import sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants;
import sg.gov.moh.iais.egp.bsb.dto.entity.AdhocChecklistConfigDto;
import sg.gov.moh.iais.egp.bsb.dto.inspection.PreInspectionDataDto;
import sg.gov.moh.iais.egp.bsb.dto.mohprocessingdisplay.SubmissionDetailsInfo;
import sg.gov.moh.iais.egp.bsb.dto.process.DualDocSortingDto;
import sg.gov.moh.iais.egp.bsb.dto.rfi.PageAppEditSelectDto;
import sg.gov.moh.iais.egp.bsb.dto.rfi.RfiAppSelectDto;
import sg.gov.moh.iais.egp.bsb.dto.rfi.RfiInspectionSaveDto;
import sg.gov.moh.iais.egp.bsb.dto.validation.ValidationResultDto;
import sg.gov.moh.iais.egp.bsb.service.DocSettingService;
import sg.gov.moh.iais.egp.bsb.service.DualDocSortingService;
import sg.gov.moh.iais.egp.bsb.service.RfiService;
import sg.gov.moh.iais.egp.bsb.util.MaskHelper;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;

import static com.ecquaria.cloud.moh.iais.common.constant.BsbAuditTrailConstants.FUNCTION_PRE_INSPECTION;
import static com.ecquaria.cloud.moh.iais.common.constant.BsbAuditTrailConstants.MODULE_INSPECTION;
import static sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants.MOH_PROCESS_DECISION_REQUEST_FOR_INFORMATION;
import static sg.gov.moh.iais.egp.bsb.constant.module.InspectionConstants.KEY_ADHOC_CHECKLIST_LIST_ATTR;
import static sg.gov.moh.iais.egp.bsb.constant.module.InspectionConstants.KEY_APP_ID;
import static sg.gov.moh.iais.egp.bsb.constant.module.InspectionConstants.KEY_INSPECTION_CONFIG;
import static sg.gov.moh.iais.egp.bsb.constant.module.InspectionConstants.KEY_INS_DECISION;
import static sg.gov.moh.iais.egp.bsb.constant.module.InspectionConstants.KEY_RESULT_MSG;
import static sg.gov.moh.iais.egp.bsb.constant.module.InspectionConstants.KEY_SELF_ASSESSMENT_AVAILABLE;
import static sg.gov.moh.iais.egp.bsb.constant.module.InspectionConstants.KEY_TASK_ID;
import static sg.gov.moh.iais.egp.bsb.constant.module.InspectionConstants.TAB_ACTIVE;
import static sg.gov.moh.iais.egp.bsb.constant.module.InspectionConstants.TAB_PROCESSING;
import static sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants.INTERNAL_DOCUMENT_TYPE_LIST;
import static sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants.KEY_ACTION_TYPE;
import static sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants.KEY_FACILITY_DETAILS_INFO;
import static sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants.KEY_ROUTING_HISTORY_LIST;
import static sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants.KEY_SUBMISSION_DETAILS_INFO;
import static sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants.KEY_VALID;
import static sg.gov.moh.iais.egp.bsb.constant.module.RfiConstants.KEY_PAGE_APP_EDIT_SELECT_DTO;
import static sg.gov.moh.iais.egp.bsb.constant.module.RfiConstants.KEY_RFI_APP_SELECT_DTO;


@Slf4j
@RequiredArgsConstructor
@Delegator("bsbPreInspection")
public class PreInspectionDelegator {
    private final InspectionClient inspectionClient;
    private final RfiService rfiService;
    private final RfiClient rfiClient;
    private final DocSettingService docSettingService;
    private final DualDocSortingService dualDocSortingService;

    public void start(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        MaskHelper.taskProcessUnmask(request, KEY_APP_ID, KEY_TASK_ID);
        AuditTrailHelper.auditFunction(MODULE_INSPECTION, FUNCTION_PRE_INSPECTION);
    }

    public void init(BaseProcessClass bpc) {
        HttpSession session = bpc.request.getSession();
        DualDocSortingService.clearDualDocSortingInfo(session);
        session.removeAttribute(KEY_INS_DECISION);
        session.removeAttribute(KEY_ADHOC_CHECKLIST_LIST_ATTR);

        // if can select RFI section, need clear this, set in 'BeViewApplicationDelegator'
        session.removeAttribute(KEY_PAGE_APP_EDIT_SELECT_DTO);
        // set in 'rfiService'
        session.removeAttribute(KEY_RFI_APP_SELECT_DTO);
    }

    public void prepareData(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        String appId = (String) ParamUtil.getSessionAttr(request, KEY_APP_ID);
        PreInspectionDataDto preInspectionDataDto = inspectionClient.getPreInspectionDataDto(appId);
        SubmissionDetailsInfo submissionDetailsInfo = preInspectionDataDto.getSubmissionDetailsInfo();
        ParamUtil.setRequestAttr(request, KEY_SUBMISSION_DETAILS_INFO, submissionDetailsInfo);
        dualDocSortingService.retrieveDualDocDisplayDtoIntoRequest(request, appId);
        ParamUtil.setRequestAttr(request, KEY_FACILITY_DETAILS_INFO, preInspectionDataDto.getFacilityDetailsInfo());
        ParamUtil.setRequestAttr(request, KEY_ROUTING_HISTORY_LIST, preInspectionDataDto.getProcessHistoryDtoList());
        ParamUtil.setRequestAttr(request, KEY_INSPECTION_CONFIG, preInspectionDataDto.getBsbChecklistConfigDto());
        ParamUtil.setRequestAttr(request, KEY_SELF_ASSESSMENT_AVAILABLE, Boolean.TRUE);
        //set internal doc types
        ParamUtil.setRequestAttr(request, INTERNAL_DOCUMENT_TYPE_LIST, docSettingService.getInternalDocTypeList());

        RfiInspectionSaveDto processDto = (RfiInspectionSaveDto) ParamUtil.getSessionAttr(request, KEY_INS_DECISION);
        if (processDto == null) {
            processDto = new RfiInspectionSaveDto();
            ParamUtil.setSessionAttr(request, KEY_INS_DECISION, processDto);
        }

        AdhocChecklistConfigDto adhocChecklistConfigDto = (AdhocChecklistConfigDto) ParamUtil.getSessionAttr(request, KEY_ADHOC_CHECKLIST_LIST_ATTR);
        if (adhocChecklistConfigDto == null) {
            if (StringUtil.isNotEmpty(appId)) {
                adhocChecklistConfigDto = inspectionClient.getAdhocChecklistConfigDaoByAppid(appId).getBody();
                ParamUtil.setSessionAttr(request, KEY_ADHOC_CHECKLIST_LIST_ATTR, adhocChecklistConfigDto);
            } else {
                log.info("-----------application id is null-------");
            }
        }
    }

    public void bindAction(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        DualDocSortingService.readDualDocSortingInfo(request);
    }

    public void validateSubmission(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        RfiInspectionSaveDto processDto = (RfiInspectionSaveDto) ParamUtil.getSessionAttr(request, KEY_INS_DECISION);
        processDto.reqObjMapping(request);
        rfiService.reqObjMappingRfiAppSelectDto(request);

        ParamUtil.setSessionAttr(request, KEY_INS_DECISION, processDto);
        ValidationResultDto validationResultDto = inspectionClient.validatePreInsSubmission(processDto);
        PageAppEditSelectDto pageAppEditSelectDto  = (PageAppEditSelectDto) ParamUtil.getSessionAttr(request, KEY_PAGE_APP_EDIT_SELECT_DTO);
        RfiAppSelectDto rfiAppSelectDto = (RfiAppSelectDto) ParamUtil.getSessionAttr(request, KEY_RFI_APP_SELECT_DTO);
        rfiService.validateRfiSelection(processDto.getDecision(), validationResultDto, pageAppEditSelectDto, rfiAppSelectDto);
        String validateResult;
        if (validationResultDto.isPass()) {
            if (MasterCodeConstants.MOH_PROCESS_DECISION_MARK_INSPECTION_TASK_AS_READY.equals(processDto.getDecision())) {
                validateResult = "ready";
            } else if (MOH_PROCESS_DECISION_REQUEST_FOR_INFORMATION.equals(processDto.getDecision())) {
                validateResult = "rfi";
            } else {
                validateResult = "unknown";
            }
        } else {
            validateResult = "invalid";
            HashMap<String, String> errorMap = validationResultDto.getErrorMap();
            ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.ERRORMSG, JsonUtil.parseToJson(errorMap));
            ParamUtil.setRequestAttr(request, TAB_ACTIVE, TAB_PROCESSING);
        }
        if (log.isInfoEnabled()) {
            log.info("Officer submit decision [{}] for pre-inspection, route result [{}]", LogUtil.escapeCrlf(processDto.getDecision()), validateResult);
        }
        ParamUtil.setRequestAttr(request, KEY_VALID, validateResult);
    }


    public void ready(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        String appId = (String) ParamUtil.getSessionAttr(request, KEY_APP_ID);
        String taskId = (String) ParamUtil.getSessionAttr(request, KEY_TASK_ID);
        RfiInspectionSaveDto processDto = (RfiInspectionSaveDto) ParamUtil.getSessionAttr(request, KEY_INS_DECISION);
        log.info("AppId {} TaskId {} Inspection mark as ready", appId, taskId);
        inspectionClient.changeInspectionStatusToReady(appId, taskId, processDto);
        ParamUtil.setRequestAttr(request, ModuleCommonConstants.RESULT_MSG, "BISACKINS002");
    }

    public void rfi(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        String appId = (String) ParamUtil.getSessionAttr(request, KEY_APP_ID);
        String taskId = (String) ParamUtil.getSessionAttr(request, KEY_TASK_ID);

        RfiInspectionSaveDto processDto = (RfiInspectionSaveDto) ParamUtil.getSessionAttr(request, KEY_INS_DECISION);
        processDto.setPageAppEditSelectDto((PageAppEditSelectDto) ParamUtil.getSessionAttr(request, KEY_PAGE_APP_EDIT_SELECT_DTO));
        processDto.setRfiAppSelectDto((RfiAppSelectDto) ParamUtil.getSessionAttr(request, KEY_RFI_APP_SELECT_DTO));
        log.info("AppId {} TaskId {} Inspection mark as rfi", appId, taskId);
        rfiClient.saveInspectionRfi(processDto, appId, taskId);
        ParamUtil.setRequestAttr(request, ModuleCommonConstants.RESULT_MSG, "BISACKINS003");
    }
}
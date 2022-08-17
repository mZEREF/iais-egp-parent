package sg.gov.moh.iais.egp.bsb.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.checklist.ChecklistConfigDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.LogUtil;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.MasterCodeUtil;
import com.ecquaria.cloud.moh.iais.helper.WebValidationHelper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sg.gov.moh.iais.egp.bsb.client.InspectionClient;
import sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants;
import sg.gov.moh.iais.egp.bsb.constant.TaskType;
import sg.gov.moh.iais.egp.bsb.constant.module.TaskModuleConstants;
import sg.gov.moh.iais.egp.bsb.dto.chklst.ChklstItemAnswerDto;
import sg.gov.moh.iais.egp.bsb.dto.entity.AdhocChecklistConfigDto;
import sg.gov.moh.iais.egp.bsb.dto.entity.SelfAssessmtChklDto;
import sg.gov.moh.iais.egp.bsb.dto.inspection.InsProcessDto;
import sg.gov.moh.iais.egp.bsb.dto.inspection.PreInspectionDataDto;
import sg.gov.moh.iais.egp.bsb.dto.inspection.RfiPreInspectionDto;
import sg.gov.moh.iais.egp.bsb.dto.mohprocessingdisplay.SubmissionDetailsInfo;
import sg.gov.moh.iais.egp.bsb.dto.rfi.RfiProcessDto;
import sg.gov.moh.iais.egp.bsb.dto.validation.ValidationResultDto;
import sg.gov.moh.iais.egp.bsb.service.AppViewService;
import sg.gov.moh.iais.egp.bsb.service.RfiService;
import sg.gov.moh.iais.egp.bsb.util.MaskHelper;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

import static com.ecquaria.cloud.moh.iais.common.constant.BsbAuditTrailConstants.FUNCTION_PRE_INSPECTION;
import static com.ecquaria.cloud.moh.iais.common.constant.BsbAuditTrailConstants.MODULE_INSPECTION;
import static sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants.APP_STATUS_PEND_APPLICANT_CLARIFICATION;
import static sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants.MOH_PROCESS_DECISION_REQUEST_FOR_INFORMATION;
import static sg.gov.moh.iais.egp.bsb.constant.module.InspectionConstants.KEY_ADHOC_CHECKLIST_LIST_ATTR;
import static sg.gov.moh.iais.egp.bsb.constant.module.InspectionConstants.KEY_ANSWER_MAP;
import static sg.gov.moh.iais.egp.bsb.constant.module.InspectionConstants.KEY_APP_ID;
import static sg.gov.moh.iais.egp.bsb.constant.module.InspectionConstants.KEY_CAN_RFI;
import static sg.gov.moh.iais.egp.bsb.constant.module.InspectionConstants.KEY_CHKL_CONFIG;
import static sg.gov.moh.iais.egp.bsb.constant.module.InspectionConstants.KEY_EDITABLE;
import static sg.gov.moh.iais.egp.bsb.constant.module.InspectionConstants.KEY_INSPECTION_CONFIG;
import static sg.gov.moh.iais.egp.bsb.constant.module.InspectionConstants.KEY_INS_DECISION;
import static sg.gov.moh.iais.egp.bsb.constant.module.InspectionConstants.KEY_RESULT_MSG;
import static sg.gov.moh.iais.egp.bsb.constant.module.InspectionConstants.KEY_SELF_ASSESSMENT_AVAILABLE;
import static sg.gov.moh.iais.egp.bsb.constant.module.InspectionConstants.KEY_SEPARATOR;
import static sg.gov.moh.iais.egp.bsb.constant.module.InspectionConstants.KEY_TASK_ID;
import static sg.gov.moh.iais.egp.bsb.constant.module.InspectionConstants.VALUE_RFI_FLAG_APPLICATION;
import static sg.gov.moh.iais.egp.bsb.constant.module.InspectionConstants.VALUE_RFI_FLAG_SELF;
import static sg.gov.moh.iais.egp.bsb.constant.module.InspectionConstants.VALUE_RFI_FLAG_SELF_APPLICATION;
import static sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants.KEY_APPLICANT;
import static sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants.KEY_DO;
import static sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants.KEY_FACILITY_DETAILS_INFO;
import static sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants.KEY_ROUTING_HISTORY_LIST;
import static sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants.KEY_SUBMISSION_DETAILS_INFO;
import static sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants.KEY_TAB_DOCUMENT_INTERNAL_DOC_LIST;
import static sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants.KEY_TAB_DOCUMENT_SUPPORT_DOC_LIST;
import static sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants.KEY_VALID;
import static sg.gov.moh.iais.egp.bsb.constant.module.RfiConstants.KEY_RFI_PROCESS_DTO;


@Slf4j
@Delegator("bsbPreInspection")
public class PreInspectionDelegator {
    private final InspectionClient inspectionClient;
    private final AppViewService appViewService;
    private final RfiService rfiService;

    private static final String RFI_APPLICATION = "AppPreInspRfiCheck";
    private static final String RFI_SELF = "SelfPreInspRfiCheck";

    @Autowired
    public PreInspectionDelegator(InspectionClient inspectionClient, AppViewService appViewService, RfiService rfiService) {
        this.inspectionClient = inspectionClient;
        this.appViewService = appViewService;
        this.rfiService = rfiService;
    }


    public void start(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        MaskHelper.taskProcessUnmask(request, KEY_APP_ID, KEY_TASK_ID);
        AuditTrailHelper.auditFunction(MODULE_INSPECTION, FUNCTION_PRE_INSPECTION);
    }

    public void init(BaseProcessClass bpc) {
        HttpSession session = bpc.request.getSession();
        session.removeAttribute(KEY_INS_DECISION);
        session.removeAttribute(RFI_APPLICATION);
        session.removeAttribute(RFI_SELF);
        session.removeAttribute(KEY_ADHOC_CHECKLIST_LIST_ATTR);
        session.removeAttribute(KEY_RFI_PROCESS_DTO);
    }

    public void prepareData(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        String appId = (String) ParamUtil.getSessionAttr(request, KEY_APP_ID);
        PreInspectionDataDto preInspectionDataDto = inspectionClient.getPreInspectionDataDto(appId);
        SubmissionDetailsInfo submissionDetailsInfo = preInspectionDataDto.getSubmissionDetailsInfo();
        ParamUtil.setRequestAttr(request, KEY_SUBMISSION_DETAILS_INFO, submissionDetailsInfo);
        ParamUtil.setRequestAttr(request, KEY_TAB_DOCUMENT_INTERNAL_DOC_LIST, preInspectionDataDto.getInternalDocDisplayDtoList());
        ParamUtil.setRequestAttr(request, KEY_TAB_DOCUMENT_SUPPORT_DOC_LIST, preInspectionDataDto.getSupportDocDisplayDtoList());
        ParamUtil.setRequestAttr(request, KEY_FACILITY_DETAILS_INFO, preInspectionDataDto.getFacilityDetailsInfo());
        ParamUtil.setRequestAttr(request, KEY_ROUTING_HISTORY_LIST, preInspectionDataDto.getProcessHistoryDtoList());
        ParamUtil.setRequestAttr(request, KEY_INSPECTION_CONFIG, preInspectionDataDto.getBsbChecklistConfigDto());

        // TODO: check this app status
        if (MasterCodeConstants.APP_STATUS_PEND_CHECKLIST_SUBMISSION.equals(submissionDetailsInfo.getApplicationStatus())) {
            ParamUtil.setRequestAttr(request, KEY_SELF_ASSESSMENT_AVAILABLE, Boolean.FALSE);
        } else {
            ParamUtil.setRequestAttr(request, KEY_SELF_ASSESSMENT_AVAILABLE, Boolean.TRUE);
        }
        if (MasterCodeConstants.APP_STATUS_PEND_INSPECTION_READINESS.equals(submissionDetailsInfo.getApplicationStatus())) {
            ParamUtil.setRequestAttr(request, KEY_CAN_RFI, Boolean.TRUE);
        } else {
            ParamUtil.setRequestAttr(request, KEY_CAN_RFI, Boolean.FALSE);
        }

        InsProcessDto processDto = (InsProcessDto) ParamUtil.getSessionAttr(request, KEY_INS_DECISION);
        if (processDto == null) {
            processDto = new InsProcessDto();
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

        // view application
        AppViewService.facilityRegistrationViewApp(request, appId, TaskType.CHECK_INSPECTION_READINESS);
    }

    public void bindAction(BaseProcessClass bpc) {
        // do nothing now
    }


    public void preSelfAssessment(BaseProcessClass bpc) throws JsonProcessingException {
        HttpServletRequest request = bpc.request;
        String appId = (String) ParamUtil.getSessionAttr(request, KEY_APP_ID);
        SelfAssessmtChklDto answerRecordDto = inspectionClient.getSavedSelfAssessment(appId);
        ChecklistConfigDto configDto = inspectionClient.getChecklistConfigById(answerRecordDto.getChkLstConfigId());

        String answerJson = answerRecordDto.getAnswer();
        ObjectMapper mapper = new ObjectMapper();
        List<ChklstItemAnswerDto> answerDtoList = mapper.readValue(answerJson, new TypeReference<List<ChklstItemAnswerDto>>() {
        });
        Map<String, ChklstItemAnswerDto> answerMap = Maps.newHashMapWithExpectedSize(answerDtoList.size());
        for (ChklstItemAnswerDto answerDto : answerDtoList) {
            answerMap.put(answerDto.getConfigId() + KEY_SEPARATOR + answerDto.getSectionId() + KEY_SEPARATOR + answerDto.getItemId(), answerDto);
        }

        ParamUtil.setRequestAttr(request, KEY_CHKL_CONFIG, configDto);
        ParamUtil.setRequestAttr(request, KEY_ANSWER_MAP, answerMap);
        ParamUtil.setRequestAttr(request, KEY_EDITABLE, Boolean.FALSE);
    }


    public void validateSubmission(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        InsProcessDto processDto = (InsProcessDto) ParamUtil.getSessionAttr(request, KEY_INS_DECISION);
        processDto.reqObjMapping(request);
        setRfiFromPage(request, processDto);
        Map<String, String> errorMap = validateRfi(request, processDto);
        ParamUtil.setSessionAttr(request, KEY_INS_DECISION, processDto);
        ValidationResultDto validationResultDto = inspectionClient.validatePreInsSubmission(processDto);
        String validateResult;
        // TODO: check these decision
        if (validationResultDto.isPass() && errorMap.isEmpty()) {
            if (MasterCodeConstants.MOH_PROCESS_DECISION_MARK_INSPECTION_TASK_AS_READY.equals(processDto.getDecision())) {
                validateResult = "ready";
            } else if (MOH_PROCESS_DECISION_REQUEST_FOR_INFORMATION.equals(processDto.getDecision())) {
                validateResult = "rfi";
            } else if (MasterCodeConstants.MOH_PROCESS_DECISION_SKIP_INSPECTION.equals(processDto.getDecision())) {
                validateResult = "skip";
            } else {
                validateResult = "unknown";
            }
        } else {
            validateResult = "invalid";
            ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errorMap));
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
        InsProcessDto processDto = (InsProcessDto) ParamUtil.getSessionAttr(request, KEY_INS_DECISION);
        log.info("AppId {} TaskId {} Inspection mark as ready", appId, taskId);
        inspectionClient.changeInspectionStatusToReady(appId, taskId, processDto);
        ParamUtil.setRequestAttr(request, TaskModuleConstants.KEY_CURRENT_TASK, MasterCodeUtil.getCodeDesc(MasterCodeConstants.APP_STATUS_PEND_INSPECTION_READINESS));
        ParamUtil.setRequestAttr(request,TaskModuleConstants.KEY_NEXT_TASK, MasterCodeUtil.getCodeDesc(MasterCodeConstants.APP_STATUS_PEND_INSPECTION));
        ParamUtil.setRequestAttr(request,TaskModuleConstants.KEY_NEXT_ROLE, KEY_DO);
    }

    public void rfi(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        String appId = (String) ParamUtil.getSessionAttr(request, KEY_APP_ID);
        String taskId = (String) ParamUtil.getSessionAttr(request, KEY_TASK_ID);
        int rfiFlag = getRfiFlag(request);
        InsProcessDto processDto = (InsProcessDto) ParamUtil.getSessionAttr(request, KEY_INS_DECISION);

        RfiProcessDto rfiProcessDto = (RfiProcessDto) ParamUtil.getSessionAttr(request, KEY_RFI_PROCESS_DTO);

        RfiPreInspectionDto rfiPreInspectionDto = new RfiPreInspectionDto();
        rfiPreInspectionDto.setInsProcessDto(processDto);
        rfiPreInspectionDto.setRfiFlag(rfiFlag);
        rfiPreInspectionDto.setRfiProcessDto(rfiProcessDto);
        log.info("AppId {} TaskId {} RfiFlag {} Inspection mark as rfi", appId, taskId, rfiFlag);
        inspectionClient.changeInspectionStatusToRfi(appId, taskId, rfiPreInspectionDto);
        ParamUtil.setRequestAttr(request, TaskModuleConstants.KEY_CURRENT_TASK, MasterCodeUtil.getCodeDesc(MasterCodeConstants.APP_STATUS_PEND_INSPECTION_READINESS));
        ParamUtil.setRequestAttr(request,TaskModuleConstants.KEY_NEXT_TASK, MasterCodeUtil.getCodeDesc(APP_STATUS_PEND_APPLICANT_CLARIFICATION));
        ParamUtil.setRequestAttr(request,TaskModuleConstants.KEY_NEXT_ROLE, KEY_APPLICANT);
    }

    public void skip(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        InsProcessDto processDto = (InsProcessDto) ParamUtil.getSessionAttr(request, KEY_INS_DECISION);
        String appId = (String) ParamUtil.getSessionAttr(request, KEY_APP_ID);
        String taskId = (String) ParamUtil.getSessionAttr(request, KEY_TASK_ID);
        inspectionClient.skipInspection(appId, taskId, processDto);
        ParamUtil.setRequestAttr(request, KEY_RESULT_MSG, "You have successfully completed your task");
    }

    public void prepareApplicationRfi(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        String appId = (String) ParamUtil.getSessionAttr(request, KEY_APP_ID);
        appViewService.retrieveFacReg(request, appId);
    }

    public void doApplicationRfi(BaseProcessClass bpc) {
        rfiService.doNewFacilityRfi(bpc.request);
    }

    private Map<String, String> validateRfi(HttpServletRequest request, InsProcessDto processDto) {
        Map<String, String> errorMap = IaisCommonUtils.genNewHashMap(1);
        if (MOH_PROCESS_DECISION_REQUEST_FOR_INFORMATION.equals(processDto.getDecision())) {
            boolean rfiApp = (boolean) ParamUtil.getSessionAttr(request, RFI_APPLICATION);
            boolean rfiSelf = (boolean) ParamUtil.getSessionAttr(request, RFI_SELF);
            if (!(rfiApp || rfiSelf)) {
                errorMap.put("preInspRfiCheck", "GENERAL_ERR0006");
            }
        }
        return errorMap;
    }

    private void setRfiFromPage(HttpServletRequest request, InsProcessDto processDto) {
        if (MOH_PROCESS_DECISION_REQUEST_FOR_INFORMATION.equals(processDto.getDecision())) {
            boolean rfiApp = "true".equals(ParamUtil.getString(request, RFI_APPLICATION));
            boolean rfiSelf = "true".equals(ParamUtil.getString(request, RFI_SELF));
            ParamUtil.setSessionAttr(request, RFI_APPLICATION, rfiApp);
            ParamUtil.setSessionAttr(request, RFI_SELF, rfiSelf);
        }
    }

    private int getRfiFlag(HttpServletRequest request) {
        boolean rfiApp = (boolean) ParamUtil.getSessionAttr(request, RFI_APPLICATION);
        boolean rfiSelf = (boolean) ParamUtil.getSessionAttr(request, RFI_SELF);
        if (rfiSelf && rfiApp) {
            return VALUE_RFI_FLAG_SELF_APPLICATION;
        } else if (rfiApp) {
            return VALUE_RFI_FLAG_APPLICATION;
        } else {
            return VALUE_RFI_FLAG_SELF;
        }
    }
}
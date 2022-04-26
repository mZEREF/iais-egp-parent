package sg.gov.moh.iais.egp.bsb.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.AuditTrailConsts;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.checklist.ChecklistConfigDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.LogUtil;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.WebValidationHelper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sg.gov.moh.iais.egp.bsb.client.InspectionClient;
import sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants;
import sg.gov.moh.iais.egp.bsb.dto.chklst.ChklstItemAnswerDto;
import sg.gov.moh.iais.egp.bsb.dto.entity.SelfAssessmtChklDto;
import sg.gov.moh.iais.egp.bsb.dto.inspection.InsProcessDto;
import sg.gov.moh.iais.egp.bsb.dto.inspection.PreInspectionDataDto;
import sg.gov.moh.iais.egp.bsb.dto.mohprocessingdisplay.SubmissionDetailsInfo;
import sg.gov.moh.iais.egp.bsb.dto.validation.ValidationResultDto;
import sg.gov.moh.iais.egp.bsb.util.MaskHelper;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

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
import static sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants.KEY_FACILITY_DETAILS_INFO;
import static sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants.KEY_ROUTING_HISTORY_LIST;
import static sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants.KEY_SUBMISSION_DETAILS_INFO;
import static sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants.KEY_TAB_DOCUMENT_INTERNAL_DOC_LIST;
import static sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants.KEY_TAB_DOCUMENT_SUPPORT_DOC_LIST;
import static sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants.KEY_VALID;


@Slf4j
@Delegator("bsbPreInspection")
public class PreInspectionDelegator {
    private final InspectionClient inspectionClient;

    private final static String RFI_APPLICATION = "AppPreInspRfiCheck";
    private final static String RFI_SELF = "SelfPreInspRfiCheck";

    @Autowired
    public PreInspectionDelegator(InspectionClient inspectionClient) {
        this.inspectionClient = inspectionClient;
    }


    public void start(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        MaskHelper.taskProcessUnmask(request, KEY_APP_ID, KEY_TASK_ID);

        AuditTrailHelper.auditFunction(AuditTrailConsts.MODULE_INSPECTION, AuditTrailConsts.FUNCTION_PRE_INSPECTION);
    }

    public void init(BaseProcessClass bpc) {
        HttpSession session = bpc.request.getSession();
        session.removeAttribute(KEY_INS_DECISION);
        session.removeAttribute(RFI_APPLICATION);
        session.removeAttribute(RFI_SELF);
    }

    public void prepareData(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        String appId = (String) ParamUtil.getSessionAttr(bpc.request, KEY_APP_ID);
        PreInspectionDataDto preInspectionDataDto = inspectionClient.getPreInspectionDataDto(appId);
        SubmissionDetailsInfo submissionDetailsInfo = preInspectionDataDto.getSubmissionDetailsInfo();
        ParamUtil.setRequestAttr(request, KEY_SUBMISSION_DETAILS_INFO, submissionDetailsInfo);
        ParamUtil.setRequestAttr(request, KEY_TAB_DOCUMENT_INTERNAL_DOC_LIST, preInspectionDataDto.getInternalDocDisplayDtoList());
        ParamUtil.setRequestAttr(request, KEY_TAB_DOCUMENT_SUPPORT_DOC_LIST, preInspectionDataDto.getSupportDocDisplayDtoList());
        ParamUtil.setRequestAttr(request, KEY_FACILITY_DETAILS_INFO, preInspectionDataDto.getFacilityDetailsInfo());
        ParamUtil.setRequestAttr(request, KEY_ROUTING_HISTORY_LIST, preInspectionDataDto.getProcessHistoryDtoList());
        ParamUtil.setRequestAttr(request, KEY_INSPECTION_CONFIG, preInspectionDataDto.getBsbChecklistConfigDto());

        if (MasterCodeConstants.APP_STATUS_PEND_SUBMIT_SELF_ASSESSMENT.equals(submissionDetailsInfo.getApplicationStatus())) {
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
    }

    public void bindAction(BaseProcessClass bpc) {
        // do nothing now
    }


    public void preSelfAssessment(BaseProcessClass bpc) throws JsonProcessingException {
        HttpServletRequest request = bpc.request;
        String appId = (String) ParamUtil.getSessionAttr(request, KEY_APP_ID);
        SelfAssessmtChklDto answerRecordDto = inspectionClient.getSavedSelfAssessment(appId);
        ChecklistConfigDto configDto = inspectionClient.getChecklistConfigById(answerRecordDto.getChkLstConfigId());
        ChecklistConfigDto commonConfigDto = inspectionClient.getChecklistConfigById(answerRecordDto.getCommonChkLstConfigId());

        String answerJson = answerRecordDto.getAnswer();
        ObjectMapper mapper = new ObjectMapper();
        List<ChklstItemAnswerDto> answerDtoList = mapper.readValue(answerJson, new TypeReference<List<ChklstItemAnswerDto>>() {
        });
        Map<String, ChklstItemAnswerDto> answerMap = Maps.newHashMapWithExpectedSize(answerDtoList.size());
        for (ChklstItemAnswerDto answerDto : answerDtoList) {
            answerMap.put(answerDto.getConfigId() + KEY_SEPARATOR + answerDto.getSectionId() + KEY_SEPARATOR + answerDto.getItemId(), answerDto);
        }

        ParamUtil.setRequestAttr(request, KEY_CHKL_CONFIG, Arrays.asList(commonConfigDto, configDto));
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
        if (validationResultDto.isPass() && errorMap.isEmpty()) {
            if (MasterCodeConstants.MOH_PROCESSING_DECISION_MARK_AS_READY.equals(processDto.getDecision())) {
                validateResult = "ready";
            } else if (MasterCodeConstants.MOH_PROCESSING_DECISION_REQUEST_FOR_INFO.equals(processDto.getDecision())) {
                validateResult = "rfi";
            } else if (MasterCodeConstants.MOH_PROCESSING_DECISION_SKIP_INSPECTION.equals(processDto.getDecision())) {
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
        ParamUtil.setRequestAttr(request, KEY_RESULT_MSG, "You have successfully completed your task");
    }


    public void rfi(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        String appId = (String) ParamUtil.getSessionAttr(request, KEY_APP_ID);
        String taskId = (String) ParamUtil.getSessionAttr(request, KEY_TASK_ID);
        int rfiFlag = getRfiFlag(request);
        InsProcessDto processDto = (InsProcessDto) ParamUtil.getSessionAttr(request, KEY_INS_DECISION);
        log.info("AppId {} TaskId {} RfiFlag {} Inspection mark as rfi", appId, taskId, rfiFlag);
        inspectionClient.changeInspectionStatusToRfi(appId, taskId, rfiFlag, processDto);
        ParamUtil.setRequestAttr(request, KEY_RESULT_MSG, "You have successfully completed your task");
    }

    public void skip(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        InsProcessDto processDto = (InsProcessDto) ParamUtil.getSessionAttr(request, KEY_INS_DECISION);
        String appId = (String) ParamUtil.getSessionAttr(request, KEY_APP_ID);
        String taskId = (String) ParamUtil.getSessionAttr(request, KEY_TASK_ID);
        inspectionClient.skipInspection(appId, taskId, processDto);
    }

    private Map<String, String> validateRfi(HttpServletRequest request, InsProcessDto processDto) {
        Map<String, String> errorMap = IaisCommonUtils.genNewHashMap(1);
        if ("MOHPRO002".equals(processDto.getDecision())) {
            Boolean rfiApp = (Boolean) ParamUtil.getSessionAttr(request, RFI_APPLICATION);
            Boolean rfiSelf = (Boolean) ParamUtil.getSessionAttr(request, RFI_SELF);
            if (!(rfiApp || rfiSelf)) {
                errorMap.put("preInspRfiCheck", "GENERAL_ERR0006");
            }
        }
        return errorMap;
    }

    private void setRfiFromPage(HttpServletRequest request, InsProcessDto processDto) {
        if ("MOHPRO002".equals(processDto.getDecision())) {
            Boolean rfiApp = "true".equals(ParamUtil.getString(request, RFI_APPLICATION));
            Boolean rfiSelf = "true".equals(ParamUtil.getString(request, RFI_SELF));
            ParamUtil.setSessionAttr(request, RFI_APPLICATION, rfiApp);
            ParamUtil.setSessionAttr(request, RFI_SELF, rfiSelf);
        }
    }

    private int getRfiFlag(HttpServletRequest request) {
        Boolean rfiApp = (Boolean) ParamUtil.getSessionAttr(request, RFI_APPLICATION);
        Boolean rfiSelf = (Boolean) ParamUtil.getSessionAttr(request, RFI_SELF);
        if (rfiSelf && rfiApp) {
            return VALUE_RFI_FLAG_SELF_APPLICATION;
        } else if (rfiApp) {
            return VALUE_RFI_FLAG_APPLICATION;
        } else {
            return VALUE_RFI_FLAG_SELF;
        }
    }
}

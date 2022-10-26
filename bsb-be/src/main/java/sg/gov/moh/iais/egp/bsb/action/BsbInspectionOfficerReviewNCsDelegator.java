package sg.gov.moh.iais.egp.bsb.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.utils.LogUtil;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import sg.gov.moh.iais.egp.bsb.client.InspectionClient;
import sg.gov.moh.iais.egp.bsb.client.RfiClient;
import sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants;
import sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants;
import sg.gov.moh.iais.egp.bsb.dto.ProcessHistoryDto;
import sg.gov.moh.iais.egp.bsb.dto.file.DocRecordInfo;
import sg.gov.moh.iais.egp.bsb.dto.inspection.InsNCRectificationDataDto;
import sg.gov.moh.iais.egp.bsb.dto.inspection.InsRectificationDisplayDto;
import sg.gov.moh.iais.egp.bsb.dto.inspection.followup.InsNCProcessDto;
import sg.gov.moh.iais.egp.bsb.dto.mohprocessingdisplay.FacilityDetailsInfo;
import sg.gov.moh.iais.egp.bsb.dto.mohprocessingdisplay.SubmissionDetailsInfo;
import sg.gov.moh.iais.egp.bsb.dto.validation.ValidationResultDto;
import sg.gov.moh.iais.egp.bsb.service.DocSettingService;
import sg.gov.moh.iais.egp.bsb.service.DualDocSortingService;
import sg.gov.moh.iais.egp.bsb.service.InspectionService;
import sg.gov.moh.iais.egp.bsb.util.CollectionUtils;
import sg.gov.moh.iais.egp.bsb.util.MaskHelper;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import static com.ecquaria.cloud.moh.iais.common.constant.BsbAuditTrailConstants.FUNCTION_AO_REVIEW_NC_RECTIFICATION_EVIDENCE_DOCUMENTS;
import static com.ecquaria.cloud.moh.iais.common.constant.BsbAuditTrailConstants.FUNCTION_DO_REVIEW_NC_RECTIFICATION_EVIDENCE_DOCUMENTS;
import static com.ecquaria.cloud.moh.iais.common.constant.BsbAuditTrailConstants.MODULE_INSPECTION;
import static sg.gov.moh.iais.egp.bsb.constant.module.InspectionConstants.KEY_APP_ID;
import static sg.gov.moh.iais.egp.bsb.constant.module.InspectionConstants.KEY_INS_DECISION;
import static sg.gov.moh.iais.egp.bsb.constant.module.InspectionConstants.KEY_INS_DOC_RECORD_INFO_SUB_TYPE_MAP;
import static sg.gov.moh.iais.egp.bsb.constant.module.InspectionConstants.KEY_INS_INFO;
import static sg.gov.moh.iais.egp.bsb.constant.module.InspectionConstants.KEY_INS_NON_COMPLIANCE;
import static sg.gov.moh.iais.egp.bsb.constant.module.InspectionConstants.KEY_RESULT_MSG;
import static sg.gov.moh.iais.egp.bsb.constant.module.InspectionConstants.KEY_ROUTE;
import static sg.gov.moh.iais.egp.bsb.constant.module.InspectionConstants.KEY_TASK_ID;
import static sg.gov.moh.iais.egp.bsb.constant.module.InspectionConstants.TAB_ACTIVE;
import static sg.gov.moh.iais.egp.bsb.constant.module.InspectionConstants.TAB_PROCESSING;
import static sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants.INTERNAL_DOCUMENT_TYPE_LIST;
import static sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants.KEY_FACILITY_DETAILS_INFO;
import static sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants.KEY_ROUTING_HISTORY_LIST;
import static sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants.KEY_SELECT_ROUTE_TO_MOH;
import static sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants.KEY_SUBMISSION_DETAILS_INFO;
import static sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants.KEY_TAB_DOCUMENT_SUPPORT_DOC_LIST;
import static sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants.KEY_VALIDATION_ERRORS;


@Slf4j
@Delegator("bsbInspectionOfficerReviewNCs")
@RequiredArgsConstructor
public class BsbInspectionOfficerReviewNCsDelegator {
    private static final String BIS_ACK_INS003 = "BISACKINS003";

    private final DocSettingService docSettingService;
    private final InspectionService inspectionService;
    private final DualDocSortingService dualDocSortingService;

    private final InspectionClient inspectionClient;
    private final RfiClient rfiClient;


    public void startDO(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        MaskHelper.taskProcessUnmask(request, KEY_APP_ID, KEY_TASK_ID);

        AuditTrailHelper.auditFunction(MODULE_INSPECTION, FUNCTION_DO_REVIEW_NC_RECTIFICATION_EVIDENCE_DOCUMENTS);
    }

    public void startAO(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        MaskHelper.taskProcessUnmask(request, KEY_APP_ID, KEY_TASK_ID);

        AuditTrailHelper.auditFunction(MODULE_INSPECTION, FUNCTION_AO_REVIEW_NC_RECTIFICATION_EVIDENCE_DOCUMENTS);
    }

    public void init(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        HttpSession session = request.getSession();
        DualDocSortingService.clearDualDocSortingInfo(session);
        session.removeAttribute(KEY_SUBMISSION_DETAILS_INFO);
        session.removeAttribute(KEY_FACILITY_DETAILS_INFO);
        session.removeAttribute(KEY_ROUTING_HISTORY_LIST);
        session.removeAttribute(KEY_SELECT_ROUTE_TO_MOH);
        session.removeAttribute(KEY_INS_INFO);
        session.removeAttribute(KEY_INS_NON_COMPLIANCE);
        session.removeAttribute(KEY_INS_DOC_RECORD_INFO_SUB_TYPE_MAP);
        session.removeAttribute(KEY_INS_DECISION);
        session.removeAttribute(KEY_TAB_DOCUMENT_SUPPORT_DOC_LIST);
        String appId = (String) ParamUtil.getSessionAttr(request, KEY_APP_ID);
        InsNCRectificationDataDto initDataDto = inspectionClient.getInitInsNCRectificationData(appId);


        // submission info
        SubmissionDetailsInfo submissionDetailsInfo = initDataDto.getSubmissionDetailsInfo();
        ParamUtil.setSessionAttr(request, KEY_SUBMISSION_DETAILS_INFO, submissionDetailsInfo);

        // facility details
        FacilityDetailsInfo facilityDetailsInfo = initDataDto.getFacilityDetailsInfo();
        ParamUtil.setSessionAttr(request, KEY_FACILITY_DETAILS_INFO, facilityDetailsInfo);

        // inspection NCs
        ArrayList<InsRectificationDisplayDto> findingDisplayDtoList = new ArrayList<>(initDataDto.getRectificationDisplayDtoList());
        ParamUtil.setSessionAttr(request, KEY_INS_NON_COMPLIANCE, findingDisplayDtoList);

        ArrayList<ProcessHistoryDto> processHistoryDtoList = new ArrayList<>(initDataDto.getProcessHistoryDtoList());
        ParamUtil.setSessionAttr(request, KEY_ROUTING_HISTORY_LIST, processHistoryDtoList);

        ArrayList<SelectOption> selectRouteToMoh = new ArrayList<>(initDataDto.getSelectRouteToMoh());
        ParamUtil.setSessionAttr(request,KEY_SELECT_ROUTE_TO_MOH,selectRouteToMoh);

        //inspection NCs Document
        List<DocRecordInfo> rectificationDocs = initDataDto.getRectificationDoc();
        HashMap<String,List<DocRecordInfo>> docSubTypeDocRecordInfoMap;
        if (rectificationDocs.isEmpty()) {
            docSubTypeDocRecordInfoMap = new HashMap<>();
        } else {
            docSubTypeDocRecordInfoMap = (HashMap<String, List<DocRecordInfo>>) CollectionUtils.groupCollectionToMap(DocRecordInfo::getDocSubType, rectificationDocs);
        }
        ParamUtil.setSessionAttr(request,KEY_INS_DOC_RECORD_INFO_SUB_TYPE_MAP, docSubTypeDocRecordInfoMap);

        // inspection processing
        InsNCProcessDto processDto = new InsNCProcessDto();
        ParamUtil.setSessionAttr(request, KEY_INS_DECISION, processDto);
    }

    public void pre(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        String appId = (String) ParamUtil.getSessionAttr(request, KEY_APP_ID);
        dualDocSortingService.retrieveDualDocDisplayDtoIntoRequest(request, appId);
        //set internal doc types
        ParamUtil.setRequestAttr(request, INTERNAL_DOCUMENT_TYPE_LIST, docSettingService.getInternalDocTypeList());

        SubmissionDetailsInfo submissionDetailsInfo = (SubmissionDetailsInfo) ParamUtil.getSessionAttr(request, KEY_SUBMISSION_DETAILS_INFO);
        String appStatus = submissionDetailsInfo.getApplicationStatus();
        if(MasterCodeConstants.APP_STATUS_PEND_DO_RECTIFICATION_REVIEW.equals(appStatus)){
            ParamUtil.setRequestAttr(request, "needRemarks", true);
        }
    }

    public void bindAction(BaseProcessClass bpc){
        DualDocSortingService.readDualDocSortingInfo(bpc.request);
    }

    public void handleSubmit(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        InsNCProcessDto processDto = (InsNCProcessDto) ParamUtil.getSessionAttr(request, KEY_INS_DECISION);
        List<InsRectificationDisplayDto> findingDisplayDtoList = (List<InsRectificationDisplayDto>) ParamUtil.getSessionAttr(request, KEY_INS_NON_COMPLIANCE);
        List<String> itemValues = findingDisplayDtoList.stream().map(InsRectificationDisplayDto::getItemValue).collect(Collectors.toList());
        processDto.reqObjMapping(request, itemValues);
        ParamUtil.setSessionAttr(request, KEY_INS_DECISION, processDto);

        SubmissionDetailsInfo submissionDetailsInfo = (SubmissionDetailsInfo) ParamUtil.getSessionAttr(request, KEY_SUBMISSION_DETAILS_INFO);
        String appStatus = submissionDetailsInfo.getApplicationStatus();
        ValidationResultDto validationResultDto;
        if (MasterCodeConstants.APP_STATUS_PEND_DO_RECTIFICATION_REVIEW.equals(appStatus) ||
            MasterCodeConstants.APP_STATUS_PEND_DO_CLARIFICATION_OF_NC_REVIEW.equals(appStatus)) {
            validationResultDto = inspectionClient.validateDONCRectificationReviewDecision(processDto);
        } else if (MasterCodeConstants.APP_STATUS_PEND_AO_RECTIFICATION_REVIEW.equals(appStatus)) {
            validationResultDto = inspectionClient.validateAONCRectificationReviewDecision(processDto);
        } else {
            // change this to decision-appStatus match logic in the future
            throw new IllegalStateException();
        }
        String validateResult = "invalid";
        if (validationResultDto.isPass()) {
            if (MasterCodeConstants.APP_STATUS_PEND_DO_RECTIFICATION_REVIEW.equals(appStatus) ||
                MasterCodeConstants.APP_STATUS_PEND_DO_CLARIFICATION_OF_NC_REVIEW.equals(appStatus)) {
                if (MasterCodeConstants.MOH_PROCESS_DECISION_REQUEST_FOR_INFORMATION.equals(processDto.getDecision())) {
                    validateResult = "rfi";
                } else if (MasterCodeConstants.MOH_PROCESS_DECISION_APPROVE.equals(processDto.getDecision())) {
                    validateResult = "ao";
                } else if (MasterCodeConstants.MOH_PROCESS_DECISION_REJECT.equals(processDto.getDecision())) {
                    validateResult = "ao";
                }
            } else {
                if(MasterCodeConstants.MOH_PROCESS_DECISION_ROUTE_BACK_TO_DO.equals(processDto.getDecision())){
                    validateResult = "reject";
                } else if(MasterCodeConstants.MOH_PROCESS_DECISION_APPROVE.equals(processDto.getDecision())){
                    validateResult = "accept";
                }
            }
        } else {
            log.error("Validation failure info: {}", validationResultDto.toErrorMsg());
            ParamUtil.setRequestAttr(request, KEY_VALIDATION_ERRORS, validationResultDto.toErrorMsg());
            ParamUtil.setRequestAttr(request, TAB_ACTIVE, TAB_PROCESSING);
            validateResult = "back";
        }
        log.info("Officer submit decision [{}] for review inspection NCs Rectification report, route result [{}]", LogUtil.escapeCrlf(processDto.getDecision()), validateResult);
        ParamUtil.setRequestAttr(request, KEY_ROUTE, validateResult);
    }

    public void routeToAO(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        String appId = (String) ParamUtil.getSessionAttr(request, KEY_APP_ID);
        String taskId = (String) ParamUtil.getSessionAttr(request, KEY_TASK_ID);
        InsNCProcessDto processDto = (InsNCProcessDto) ParamUtil.getSessionAttr(request, KEY_INS_DECISION);
        inspectionClient.reviewInspectionNCToAO(appId,taskId,processDto);
        ParamUtil.setRequestAttr(request, ModuleCommonConstants.RESULT_MSG, BIS_ACK_INS003);
    }

    public void requestForInformationToApplicant(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        String appId = (String) ParamUtil.getSessionAttr(request, KEY_APP_ID);
        String taskId = (String) ParamUtil.getSessionAttr(request, KEY_TASK_ID);
        InsNCProcessDto processDto = (InsNCProcessDto) ParamUtil.getSessionAttr(request, KEY_INS_DECISION);
        rfiClient.reviewInspectionNCDORequestForInformation(appId,taskId,processDto);
        ParamUtil.setRequestAttr(request, ModuleCommonConstants.RESULT_MSG, BIS_ACK_INS003);
    }

    public void rejectAndRouteToDO(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        String appId = (String) ParamUtil.getSessionAttr(request, KEY_APP_ID);
        String taskId = (String) ParamUtil.getSessionAttr(request, KEY_TASK_ID);
        InsNCProcessDto processDto = (InsNCProcessDto) ParamUtil.getSessionAttr(request, KEY_INS_DECISION);
        inspectionClient.reviewInspectionNCAORouteBackToDo(appId,taskId,processDto);
        ParamUtil.setRequestAttr(request, ModuleCommonConstants.RESULT_MSG, BIS_ACK_INS003);
    }

    public void finalizeProcess(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        String appId = (String) ParamUtil.getSessionAttr(request, KEY_APP_ID);
        String taskId = (String) ParamUtil.getSessionAttr(request, KEY_TASK_ID);
        InsNCProcessDto processDto = (InsNCProcessDto) ParamUtil.getSessionAttr(request, KEY_INS_DECISION);
        inspectionClient.finalizeReviewInspectionNC(appId,taskId,processDto);
        ParamUtil.setRequestAttr(request, ModuleCommonConstants.RESULT_MSG, BIS_ACK_INS003);
    }

}

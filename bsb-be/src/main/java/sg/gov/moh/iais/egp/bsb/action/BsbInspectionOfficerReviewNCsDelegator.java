package sg.gov.moh.iais.egp.bsb.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.AuditTrailConsts;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.utils.LogUtil;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;
import sg.gov.moh.iais.egp.bsb.client.InspectionClient;
import sg.gov.moh.iais.egp.bsb.client.InternalDocClient;
import sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants;
import sg.gov.moh.iais.egp.bsb.dto.ProcessHistoryDto;
import sg.gov.moh.iais.egp.bsb.dto.mohprocessingdisplay.FacilityDetailsInfo;
import sg.gov.moh.iais.egp.bsb.dto.mohprocessingdisplay.SubmissionDetailsInfo;
import sg.gov.moh.iais.egp.bsb.dto.validation.ValidationResultDto;
import sg.gov.moh.iais.egp.bsb.dto.file.DocDisplayDto;
import sg.gov.moh.iais.egp.bsb.dto.file.DocRecordInfo;
import sg.gov.moh.iais.egp.bsb.dto.inspection.*;
import sg.gov.moh.iais.egp.bsb.util.CollectionUtils;
import sg.gov.moh.iais.egp.bsb.util.MaskHelper;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static sg.gov.moh.iais.egp.bsb.constant.module.InspectionConstants.*;
import static sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants.*;


@Slf4j
@Delegator("bsbInspectionOfficerReviewNCs")
public class BsbInspectionOfficerReviewNCsDelegator {
    private final InspectionClient inspectionClient;
    private final InternalDocClient internalDocClient;

    public BsbInspectionOfficerReviewNCsDelegator(InspectionClient inspectionClient, InternalDocClient internalDocClient) {
        this.inspectionClient = inspectionClient;
        this.internalDocClient = internalDocClient;
    }

    public void startDO(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        MaskHelper.taskProcessUnmask(request, KEY_APP_ID, KEY_TASK_ID);

        AuditTrailHelper.auditFunction(AuditTrailConsts.FUNCTION_INSPECTION_RECTIFICATION, "DO Review NC Rectification Evidence Documents");
    }

    public void startAO(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        MaskHelper.taskProcessUnmask(request, KEY_APP_ID, KEY_TASK_ID);

        AuditTrailHelper.auditFunction(AuditTrailConsts.FUNCTION_INSPECTION_RECTIFICATION, "AO Review NC Rectification Evidence Documents ");
    }

    public void init(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        HttpSession session = request.getSession();
        session.removeAttribute(KEY_INS_INFO);
        session.removeAttribute(KEY_INS_NON_COMPLIANCE);
        session.removeAttribute(KEY_INS_DOC_RECORD_INFO_SUB_TYPE_MAP);
        session.removeAttribute(KEY_INS_DECISION);
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
        // TODO temporary allow empty map
        List<DocRecordInfo> rectificationDocs = initDataDto.getRectificationDoc();
        HashMap<String,List<DocRecordInfo>> docSubTypeDocRecordInfoMap;
        if (rectificationDocs.isEmpty()) {
            docSubTypeDocRecordInfoMap = new HashMap<>();
        } else {
            docSubTypeDocRecordInfoMap = (HashMap<String, List<DocRecordInfo>>) CollectionUtils.groupCollectionToMap(rectificationDocs,DocRecordInfo::getDocSubType);
        }
        ParamUtil.setSessionAttr(request,KEY_INS_DOC_RECORD_INFO_SUB_TYPE_MAP, docSubTypeDocRecordInfoMap);

        // inspection processing
        InsProcessDto processDto = new InsProcessDto();
        ParamUtil.setSessionAttr(request, KEY_INS_DECISION, processDto);
    }

    public void pre(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        String appId = (String) ParamUtil.getSessionAttr(request, KEY_APP_ID);
        List<DocDisplayDto> internalDocDisplayDto = internalDocClient.getInternalDocForDisplay(appId);
        ParamUtil.setRequestAttr(request, KEY_TAB_DOCUMENT_INTERNAL_DOC_LIST, internalDocDisplayDto);
    }

    public void bindAction(BaseProcessClass bpc){
        // do nothing now
    }

    public void handleSubmit(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        InsProcessDto processDto = (InsProcessDto) ParamUtil.getSessionAttr(request, KEY_INS_DECISION);
        processDto.reqObjMapping(request);
        ParamUtil.setSessionAttr(request, KEY_INS_DECISION, processDto);

        ValidationResultDto validationResultDto = inspectionClient.validateActualOfficerReviewNCDecision(processDto);
        String validateResult;
        if (validationResultDto.isPass()) {
            if (MasterCodeConstants.MOH_PROCESSING_DECISION_ACCEPTS_RECTIFICATIONS_AND_ROUTE_TO_AO.equals(processDto.getDecision())) {
                validateResult = "ao";
            }else if(MasterCodeConstants.MOH_PROCESSING_DECISION_REQUEST_FOR_INFO.equals(processDto.getDecision())){
                validateResult = "rfi";
            }else if(MasterCodeConstants.MOH_PROCESSING_DECISION_REJECT_AND_ROUTE_TO_DO_FOR_REVISION.equals(processDto.getDecision())){
                validateResult = "reject";
            } else if(MasterCodeConstants.MOH_PROCESSING_DECISION_ACCEPT.equals(processDto.getDecision())){
                validateResult = "accept";
            }else if(MasterCodeConstants.MOH_PROCESSING_DECISION_SKIP_INSPECTION.equals(processDto.getDecision())){
                validateResult = "skip";
            }else {
                validateResult = "invalid";
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
        InsProcessDto processDto = (InsProcessDto) ParamUtil.getSessionAttr(request, KEY_INS_DECISION);
        inspectionClient.reviewInspectionNCToAO(appId,taskId,processDto);
        ParamUtil.setRequestAttr(request, KEY_RESULT_MSG, "You have successfully route to AO review.");
    }

    public void requestForInformationToApplicant(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        String appId = (String) ParamUtil.getSessionAttr(request, KEY_APP_ID);
        String taskId = (String) ParamUtil.getSessionAttr(request, KEY_TASK_ID);
        InsProcessDto processDto = (InsProcessDto) ParamUtil.getSessionAttr(request, KEY_INS_DECISION);
        inspectionClient.reviewInspectionNCDORequestForInformation(appId,taskId,processDto);
        ParamUtil.setRequestAttr(request, KEY_RESULT_MSG, "You have successfully route to applicant for information.");
    }

    public void rejectAndRouteToDO(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        String appId = (String) ParamUtil.getSessionAttr(request, KEY_APP_ID);
        String taskId = (String) ParamUtil.getSessionAttr(request, KEY_TASK_ID);
        InsProcessDto processDto = (InsProcessDto) ParamUtil.getSessionAttr(request, KEY_INS_DECISION);
        inspectionClient.reviewInspectionNCAORequestForInformation(appId,taskId,processDto);
        ParamUtil.setRequestAttr(request, KEY_RESULT_MSG, "You have successfully route to DO report.");
    }

    public void finalizeProcess(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        String appId = (String) ParamUtil.getSessionAttr(request, KEY_APP_ID);
        String taskId = (String) ParamUtil.getSessionAttr(request, KEY_TASK_ID);
        InsProcessDto processDto = (InsProcessDto) ParamUtil.getSessionAttr(request, KEY_INS_DECISION);
        inspectionClient.finalizeReviewInspectionNC(appId,taskId,processDto);
        ParamUtil.setRequestAttr(request, KEY_RESULT_MSG, "You have successfully finalize report.");
    }

    public void skip(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        String appId = (String) ParamUtil.getSessionAttr(request, KEY_APP_ID);
        String taskId = (String) ParamUtil.getSessionAttr(request, KEY_TASK_ID);
        InsProcessDto processDto = (InsProcessDto) ParamUtil.getSessionAttr(request, KEY_INS_DECISION);
        inspectionClient.skipInspection(appId,taskId,processDto);
    }

}

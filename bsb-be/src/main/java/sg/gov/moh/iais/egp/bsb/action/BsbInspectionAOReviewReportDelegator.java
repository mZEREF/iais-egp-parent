package sg.gov.moh.iais.egp.bsb.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.AuditTrailConsts;
import com.ecquaria.cloud.moh.iais.common.utils.LogUtil;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sg.gov.moh.iais.egp.bsb.client.InspectionClient;
import sg.gov.moh.iais.egp.bsb.client.InternalDocClient;
import sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants;
import sg.gov.moh.iais.egp.bsb.constant.module.AppViewConstants;
import sg.gov.moh.iais.egp.bsb.dto.validation.ValidationResultDto;
import sg.gov.moh.iais.egp.bsb.dto.entity.InspectionOutcomeDto;
import sg.gov.moh.iais.egp.bsb.dto.file.DocDisplayDto;
import sg.gov.moh.iais.egp.bsb.dto.inspection.InsFacInfoDto;
import sg.gov.moh.iais.egp.bsb.dto.inspection.InsFindingDisplayDto;
import sg.gov.moh.iais.egp.bsb.dto.inspection.InsProcessDto;
import sg.gov.moh.iais.egp.bsb.dto.inspection.InsSubmitReportDataDto;
import sg.gov.moh.iais.egp.bsb.util.MaskHelper;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import java.util.ArrayList;
import java.util.List;

import static sg.gov.moh.iais.egp.bsb.constant.module.InspectionConstants.*;
import static sg.gov.moh.iais.egp.bsb.constant.module.InspectionConstants.KEY_INS_DECISION;
import static sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants.*;

@Slf4j
@Delegator("bsbAOReviewInspectionReport")
public class BsbInspectionAOReviewReportDelegator {
    private final InspectionClient inspectionClient;
    private final InternalDocClient internalDocClient;

    @Autowired
    public BsbInspectionAOReviewReportDelegator(InspectionClient inspectionClient, InternalDocClient internalDocClient) {
        this.inspectionClient = inspectionClient;
        this.internalDocClient = internalDocClient;
    }

    public void start(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        //TODO: update
//        MaskHelper.taskProcessUnmask(request, KEY_APP_ID, KEY_TASK_ID);

        AuditTrailHelper.auditFunction(AuditTrailConsts.MODULE_INSPECTION, "AO Review Inspection Report");
    }

    public void init(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        HttpSession session = request.getSession();
        session.removeAttribute(KEY_SUBMISSION_DETAILS_INFO);
        session.removeAttribute(KEY_FACILITY_DETAILS_INFO);
//        session.removeAttribute(KEY_INS_FINDING);
//        session.removeAttribute(KEY_INS_OUTCOME);
        session.removeAttribute(KEY_INS_DECISION);

        // TODO: will update
//        String appId = (String) ParamUtil.getSessionAttr(request, KEY_APP_ID);
        String appId = "48BD553A-07BB-EC11-BE76-000C298D317C";

        // judge whether we need to send email
        InsSubmitReportDataDto initDataDto = inspectionClient.getInitInsSubmitReportData(appId);

        // submission details info
        ParamUtil.setSessionAttr(request, KEY_SUBMISSION_DETAILS_INFO, initDataDto.getSubmissionDetailsInfo());
        // facility details
        ParamUtil.setSessionAttr(request, KEY_FACILITY_DETAILS_INFO, initDataDto.getFacilityDetailsInfo());
        // inspection report
        ParamUtil.setSessionAttr(request, KEY_INS_INFO, initDataDto.getInsFacInfoDto());

//        // inspection findings
//        ArrayList<InsFindingDisplayDto> findingDisplayDtoList = new ArrayList<>(initDataDto.getFindingDtoList());
//        ParamUtil.setSessionAttr(request, KEY_INS_FINDING, findingDisplayDtoList);
//
//        // inspection outcome
//        InspectionOutcomeDto outcomeDto = initDataDto.getOutcomeDto();
//        ParamUtil.setSessionAttr(request, KEY_INS_OUTCOME, outcomeDto);

        // inspection processing
        InsProcessDto processDto = new InsProcessDto();
        ParamUtil.setSessionAttr(request, KEY_INS_DECISION, processDto);
    }

    public void pre(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        //TODO: update
//        String appId = (String) ParamUtil.getSessionAttr(request, KEY_APP_ID);
        String appId = "48BD553A-07BB-EC11-BE76-000C298D317C";
        List<DocDisplayDto> internalDocDisplayDto = internalDocClient.getInternalDocForDisplay(appId);
        ParamUtil.setRequestAttr(request, KEY_TAB_DOCUMENT_INTERNAL_DOC_LIST, internalDocDisplayDto);

        // view application need appId and moduleType
        ParamUtil.setRequestAttr(request, AppViewConstants.MASK_PARAM_APP_ID, appId);
        ParamUtil.setRequestAttr(request, AppViewConstants.MASK_PARAM_APP_VIEW_MODULE_TYPE, AppViewConstants.MODULE_VIEW_NEW_FACILITY);
    }

    public void bindAction(BaseProcessClass bpc) {
        // do nothing now
    }

    public void handleSubmit(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        InsProcessDto processDto = (InsProcessDto) ParamUtil.getSessionAttr(request, KEY_INS_DECISION);
        processDto.reqObjMapping(request);
        ParamUtil.setSessionAttr(request, KEY_INS_DECISION, processDto);

        ValidationResultDto validationResultDto = inspectionClient.validateActualInspectionAOReviewDecision(processDto);
        String validateResult;
        if (validationResultDto.isPass()) {
            if (MasterCodeConstants.MOH_PROCESSING_DECISION_APPROVE.equals(processDto.getDecision())) {
                validateResult = "approve";
            } else if (MasterCodeConstants.MOH_PROCESSING_DECISION_ROUTE_BACK_TO_DO.equals(processDto.getDecision())) {
                validateResult = "routeDO";
            } else if(MasterCodeConstants.MOH_PROCESSING_DECISION_SKIP_INSPECTION.equals(processDto.getDecision())){
                validateResult = "skip";
            } else {
                validateResult = "invalid";
            }
        } else {
            log.error("Validation failure info: {}", validationResultDto.toErrorMsg());
            ParamUtil.setRequestAttr(request, KEY_VALIDATION_ERRORS, validationResultDto.toErrorMsg());
            ParamUtil.setRequestAttr(request, TAB_ACTIVE, TAB_PROCESSING);
            validateResult = "back";
        }
        log.info("Officer submit decision [{}] for review inspection report, route result [{}]", LogUtil.escapeCrlf(processDto.getDecision()), validateResult);
        ParamUtil.setRequestAttr(request, KEY_ROUTE, validateResult);
    }

    public void revision(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        String appId = (String) ParamUtil.getSessionAttr(request, KEY_APP_ID);
        String taskId = (String) ParamUtil.getSessionAttr(request, KEY_TASK_ID);
        InsProcessDto processDto = (InsProcessDto) ParamUtil.getSessionAttr(request, KEY_INS_DECISION);
        inspectionClient.reviewInspectionReportRouteBackToDO(appId, taskId, processDto);
        ParamUtil.setRequestAttr(request, KEY_RESULT_MSG, "You have successfully routed back draft report.");
    }

    public void approve(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        String appId = (String) ParamUtil.getSessionAttr(request, KEY_APP_ID);
        String taskId = (String) ParamUtil.getSessionAttr(request, KEY_TASK_ID);
        InsProcessDto processDto = (InsProcessDto) ParamUtil.getSessionAttr(request, KEY_INS_DECISION);
        inspectionClient.reviewInspectionReportApprove(appId, taskId, processDto);
        ParamUtil.setRequestAttr(request, KEY_RESULT_MSG, "You have successfully approved the report.");
    }

    public void skip(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        String appId = (String) ParamUtil.getSessionAttr(request, KEY_APP_ID);
        String taskId = (String) ParamUtil.getSessionAttr(request, KEY_TASK_ID);
        InsProcessDto processDto = (InsProcessDto) ParamUtil.getSessionAttr(request, KEY_INS_DECISION);
        inspectionClient.skipInspection(appId,taskId,processDto);
    }
}

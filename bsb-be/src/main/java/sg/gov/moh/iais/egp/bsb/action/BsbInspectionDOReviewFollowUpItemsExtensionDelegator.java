package sg.gov.moh.iais.egp.bsb.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.AuditTrailConsts;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sg.gov.moh.iais.egp.bsb.client.InspectionClient;
import sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants;
import sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants;
import sg.gov.moh.iais.egp.bsb.dto.inspection.InsProcessDto;
import sg.gov.moh.iais.egp.bsb.dto.inspection.followup.ReviewInsFollowUpDto;
import sg.gov.moh.iais.egp.bsb.dto.validation.ValidationResultDto;
import sg.gov.moh.iais.egp.bsb.service.InspectionService;
import sg.gov.moh.iais.egp.bsb.util.MaskHelper;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;

import static sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants.*;
import static sg.gov.moh.iais.egp.bsb.constant.module.InspectionConstants.*;
import static sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants.*;

@Slf4j
@Delegator("mohDOReviewFollowUpItemsExtension")
public class BsbInspectionDOReviewFollowUpItemsExtensionDelegator {
    private final InspectionClient inspectionClient;
    private final InspectionService inspectionService;

    @Autowired
    public BsbInspectionDOReviewFollowUpItemsExtensionDelegator(InspectionClient inspectionClient, InspectionService inspectionService) {
        this.inspectionClient = inspectionClient;
        this.inspectionService = inspectionService;
    }

    public void start(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        MaskHelper.taskProcessUnmask(request, KEY_APP_ID, KEY_TASK_ID);
        AuditTrailHelper.auditFunction(AuditTrailConsts.MODULE_INSPECTION, "DO Review Follow-up Items");
        request.getSession().removeAttribute(KEY_REVIEW_FOLLOW_UP_DTO);
        request.getSession().removeAttribute(KEY_INS_DECISION);
        request.getSession().removeAttribute(KEY_DOC_DISPLAY_DTO_REPO_ID_NAME_MAP);
    }

    public void prepare(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        ReviewInsFollowUpDto dto = getDisplayDto(request);
        inspectionService.getInitFollowUpData(request,dto);
        //decide what kind of page you want
        ParamUtil.setRequestAttr(request, KEY_REQUEST_EXTENSION, YES);
        ParamUtil.setRequestAttr(request, KEY_CAN_NOT_UPLOAD_DOC, true);
    }

    public void bindAction(BaseProcessClass bpc) {
        // do nothing now
    }

    public void handleSubmit(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        InsProcessDto insProcessDto = getInsProcessDto(request);
        //get param from page
        bindParam(request, insProcessDto);
        ParamUtil.setSessionAttr(request, KEY_INS_DECISION, insProcessDto);
        ValidationResultDto validationResultDto = inspectionClient.validatePostInspectionDOReviewFollowUpItems(insProcessDto);
        String validateResult;
        if (validationResultDto.isPass()) {
            String processingDecision = insProcessDto.getDecision();
            if (MasterCodeConstants.MOH_PROCESSING_DECISION_APPROVE.equals(processingDecision)) {
                validateResult = "approve";
            } else if (MasterCodeConstants.MOH_PROCESSING_DECISION_SKIP_INSPECTION.equals(processingDecision)) {
                validateResult = "skip";
            } else if (MasterCodeConstants.MOH_PROCESSING_DECISION_REJECT.equals(processingDecision)) {
                validateResult = "reject";
            } else {
                validateResult = "invalid";
            }
        } else {
            log.error("Validation failure info: {}", validationResultDto.toErrorMsg());
            ParamUtil.setRequestAttr(request, ModuleCommonConstants.KEY_VALIDATION_ERRORS, validationResultDto.toErrorMsg());
            ParamUtil.setRequestAttr(request, TAB_ACTIVE, TAB_PROCESSING);
            validateResult = "invalid";
        }
        ParamUtil.setRequestAttr(request, KEY_ROUTE, validateResult);
    }

    public void acceptResponse(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        String appId = (String) ParamUtil.getSessionAttr(request, KEY_APP_ID);
        String taskId = (String) ParamUtil.getSessionAttr(request, KEY_TASK_ID);
        InsProcessDto insProcessDto = getInsProcessDto(request);
        //todo specify new due date
        inspectionClient.doReviewInspectionFollowUpItemsRouteBackToApplicant(appId, taskId, APP_STATUS_PEND_SUBMIT_FOLLOW_UP_ITEMS, insProcessDto);
        ParamUtil.setRequestAttr(request, KEY_RESULT_MSG, "You have successfully follow-up item Verified.");
    }

    /**
     * if the task is request extension,update app data and complete task
     * if the task is submit follow up data,update app data and complete task and create new task for Approval Officer
     */
    public void rejectAction(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        String appId = (String) ParamUtil.getSessionAttr(request, KEY_APP_ID);
        String taskId = (String) ParamUtil.getSessionAttr(request, KEY_TASK_ID);
        InsProcessDto insProcessDto = getInsProcessDto(request);
        inspectionClient.doReviewInspectionFollowUpItemsRouteBackToApplicant(appId, taskId, APP_STATUS_PEND_SUBMIT_FOLLOW_UP_ITEMS, insProcessDto);
        ParamUtil.setRequestAttr(request, KEY_RESULT_MSG, "You have successfully follow-up item Verified.");
    }

    public void skip(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        String appId = (String) ParamUtil.getSessionAttr(request, KEY_APP_ID);
        String taskId = (String) ParamUtil.getSessionAttr(request, KEY_TASK_ID);
        InsProcessDto insProcessDto = getInsProcessDto(request);
        inspectionClient.skipInspection(appId,taskId,insProcessDto);
    }

    public ReviewInsFollowUpDto getDisplayDto(HttpServletRequest request) {
        ReviewInsFollowUpDto dto = (ReviewInsFollowUpDto) ParamUtil.getSessionAttr(request, KEY_REVIEW_FOLLOW_UP_DTO);
        if (dto == null) {
            dto = new ReviewInsFollowUpDto();
        }
        return dto;
    }

    public InsProcessDto getInsProcessDto(HttpServletRequest request) {
        InsProcessDto dto = (InsProcessDto) ParamUtil.getSessionAttr(request, KEY_INS_DECISION);
        if (dto == null) {
            dto = new InsProcessDto();
        }
        return dto;
    }

    public void bindParam(HttpServletRequest request, InsProcessDto insProcessDto) {
        insProcessDto.setRemark(ParamUtil.getString(request, "remarks"));
        insProcessDto.setDecision(ParamUtil.getString(request, "processingExtensionDecision"));
    }
}

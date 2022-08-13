package sg.gov.moh.iais.egp.bsb.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.MasterCodeUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sg.gov.moh.iais.egp.bsb.client.InspectionClient;
import sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants;
import sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants;
import sg.gov.moh.iais.egp.bsb.constant.module.TaskModuleConstants;
import sg.gov.moh.iais.egp.bsb.dto.inspection.InsProcessDto;
import sg.gov.moh.iais.egp.bsb.dto.inspection.followup.ReviewInsFollowUpDto;
import sg.gov.moh.iais.egp.bsb.dto.validation.ValidationResultDto;
import sg.gov.moh.iais.egp.bsb.service.InspectionService;
import sg.gov.moh.iais.egp.bsb.util.MaskHelper;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;

import static com.ecquaria.cloud.moh.iais.common.constant.BsbAuditTrailConstants.MODULE_INSPECTION;
import static com.ecquaria.cloud.moh.iais.common.constant.BsbAuditTrailConstants.FUNCTION_AO_REVIEW_FOLLOW_UP_ITEMS;
import static sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants.APP_STATUS_PEND_AO_REPORT_APPROVAL;
import static sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants.APP_STATUS_PEND_DO_REPORT_APPROVAL;
import static sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants.NO;
import static sg.gov.moh.iais.egp.bsb.constant.module.InspectionConstants.KEY_APP_ID;
import static sg.gov.moh.iais.egp.bsb.constant.module.InspectionConstants.KEY_INS_DECISION;
import static sg.gov.moh.iais.egp.bsb.constant.module.InspectionConstants.KEY_REQUEST_EXTENSION;
import static sg.gov.moh.iais.egp.bsb.constant.module.InspectionConstants.KEY_RESULT_MSG;
import static sg.gov.moh.iais.egp.bsb.constant.module.InspectionConstants.KEY_REVIEW_FOLLOW_UP_DTO;
import static sg.gov.moh.iais.egp.bsb.constant.module.InspectionConstants.KEY_ROUTE;
import static sg.gov.moh.iais.egp.bsb.constant.module.InspectionConstants.KEY_TASK_ID;
import static sg.gov.moh.iais.egp.bsb.constant.module.InspectionConstants.TAB_ACTIVE;
import static sg.gov.moh.iais.egp.bsb.constant.module.InspectionConstants.TAB_PROCESSING;
import static sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants.KEY_CAN_NOT_UPLOAD_DOC;
import static sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants.KEY_DOC_DISPLAY_DTO_REPO_ID_NAME_MAP;

@Slf4j
@Delegator("mohAOReviewFollowUpItems")
public class BsbInspectionAOReviewFollowUpItemsDelegator {
    private final InspectionClient inspectionClient;
    private final InspectionService inspectionService;

    @Autowired
    public BsbInspectionAOReviewFollowUpItemsDelegator(InspectionClient inspectionClient, InspectionService inspectionService) {
        this.inspectionClient = inspectionClient;
        this.inspectionService = inspectionService;
    }

    public void start(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        MaskHelper.taskProcessUnmask(request, KEY_APP_ID, KEY_TASK_ID);
        AuditTrailHelper.auditFunction(MODULE_INSPECTION, FUNCTION_AO_REVIEW_FOLLOW_UP_ITEMS);
        request.getSession().removeAttribute(KEY_REVIEW_FOLLOW_UP_DTO);
        request.getSession().removeAttribute(KEY_INS_DECISION);
        request.getSession().removeAttribute(KEY_DOC_DISPLAY_DTO_REPO_ID_NAME_MAP);
    }

    public void prepare(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        ReviewInsFollowUpDto dto = getDisplayDto(request);
        inspectionService.getInitFollowUpData(request,dto);
        //decide what kind of page you want
        ParamUtil.setRequestAttr(request, KEY_REQUEST_EXTENSION, NO);
        ParamUtil.setRequestAttr(request, KEY_CAN_NOT_UPLOAD_DOC, true);
    }

    public void bindParam(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        InsProcessDto insProcessDto = getInsProcessDto(request);
        insProcessDto.reqObjMapping(request);
        ParamUtil.setSessionAttr(request, KEY_INS_DECISION, insProcessDto);
    }

    public void handleSubmit(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        InsProcessDto insProcessDto = getInsProcessDto(request);
        ValidationResultDto validationResultDto = inspectionClient.validatePostInspectionAOReviewFollowUpItems(insProcessDto);
        String validateResult;
        if (validationResultDto.isPass()) {
            // TODO: check these decision
            String processingDecision = insProcessDto.getDecision();
            if (MasterCodeConstants.MOH_PROCESS_DECISION_ROUTE_BACK_TO_DO.equals(processingDecision)) {
                validateResult = "routeBack";
            } else if (MasterCodeConstants.MOH_PROCESS_DECISION_APPROVE.equals(processingDecision)) {
                validateResult = "approve";
            } else if (MasterCodeConstants.MOH_PROCESS_DECISION_SKIP_INSPECTION.equals(processingDecision)) {
                validateResult = "skip";
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

    public void routeBackToDO(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        String appId = (String) ParamUtil.getSessionAttr(request, KEY_APP_ID);
        String taskId = (String) ParamUtil.getSessionAttr(request, KEY_TASK_ID);
        InsProcessDto insProcessDto = getInsProcessDto(request);
        inspectionClient.aoReviewInspectionFollowUpItemsRouteBackToDO(appId, taskId, insProcessDto);
        // TODO: check these app status
        ParamUtil.setRequestAttr(request, TaskModuleConstants.KEY_CURRENT_TASK, MasterCodeUtil.getCodeDesc(APP_STATUS_PEND_AO_REPORT_APPROVAL));
        ParamUtil.setRequestAttr(request,TaskModuleConstants.KEY_NEXT_TASK, MasterCodeUtil.getCodeDesc(APP_STATUS_PEND_DO_REPORT_APPROVAL));
        ParamUtil.setRequestAttr(request,TaskModuleConstants.KEY_NEXT_ROLE, ModuleCommonConstants.KEY_DO);
    }

    /**
     * if the task is request extension,update app data and complete task (and save the new due date)
     * if the task is submit follow up data,update app data and complete task and create new task for Approval Officer
     */
    public void approve(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        String appId = (String) ParamUtil.getSessionAttr(request, KEY_APP_ID);
        String taskId = (String) ParamUtil.getSessionAttr(request, KEY_TASK_ID);
        InsProcessDto insProcessDto = getInsProcessDto(request);
        inspectionClient.aoReviewInspectionFollowUpItemsAcceptResponse(appId, taskId, insProcessDto);
        ParamUtil.setRequestAttr(request, KEY_RESULT_MSG, "You have successfully follow-up item Verified.");
        // TODO: check this app status
        ParamUtil.setRequestAttr(request, TaskModuleConstants.KEY_CURRENT_TASK, MasterCodeUtil.getCodeDesc(APP_STATUS_PEND_AO_REPORT_APPROVAL));
    }

    public void skip(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        String appId = (String) ParamUtil.getSessionAttr(request, KEY_APP_ID);
        String taskId = (String) ParamUtil.getSessionAttr(request, KEY_TASK_ID);
        InsProcessDto insProcessDto = getInsProcessDto(request);
        inspectionClient.skipInspection(appId,taskId,insProcessDto);
        ParamUtil.setRequestAttr(request, KEY_RESULT_MSG, "You have successfully completed your task");
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
}

package sg.gov.moh.iais.egp.bsb.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.role.RoleConsts;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import sg.gov.moh.iais.egp.bsb.client.InspectionClient;
import sg.gov.moh.iais.egp.bsb.client.RfiClient;
import sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants;
import sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants;
import sg.gov.moh.iais.egp.bsb.dto.inspection.followup.InsFollowUpProcessDto;
import sg.gov.moh.iais.egp.bsb.dto.inspection.followup.ReviewInsFollowUpDto;
import sg.gov.moh.iais.egp.bsb.dto.validation.ValidationResultDto;
import sg.gov.moh.iais.egp.bsb.service.DualDocSortingService;
import sg.gov.moh.iais.egp.bsb.service.InspectionService;
import sg.gov.moh.iais.egp.bsb.util.MaskHelper;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;


import static com.ecquaria.cloud.moh.iais.common.constant.BsbAuditTrailConstants.MODULE_INSPECTION;
import static com.ecquaria.cloud.moh.iais.common.constant.BsbAuditTrailConstants.FUNCTION_DO_REVIEW_FOLLOW_UP_ITEMS;
import static sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants.NO;
import static sg.gov.moh.iais.egp.bsb.constant.module.InspectionConstants.KEY_APP_ID;
import static sg.gov.moh.iais.egp.bsb.constant.module.InspectionConstants.KEY_INS_DECISION;
import static sg.gov.moh.iais.egp.bsb.constant.module.InspectionConstants.KEY_REQUEST_EXTENSION;
import static sg.gov.moh.iais.egp.bsb.constant.module.InspectionConstants.KEY_REVIEW_FOLLOW_UP_DTO;
import static sg.gov.moh.iais.egp.bsb.constant.module.InspectionConstants.KEY_ROUTE;
import static sg.gov.moh.iais.egp.bsb.constant.module.InspectionConstants.KEY_TASK_ID;
import static sg.gov.moh.iais.egp.bsb.constant.module.InspectionConstants.TAB_ACTIVE;
import static sg.gov.moh.iais.egp.bsb.constant.module.InspectionConstants.TAB_PROCESSING;
import static sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants.KEY_CAN_NOT_UPLOAD_DOC;
import static sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants.KEY_DOC_DISPLAY_DTO_REPO_ID_NAME_MAP;

@Slf4j
@RequiredArgsConstructor
@Delegator("mohDOReviewFollowUpItems")
public class BsbInspectionDOReviewFollowUpItemsDelegator {
    private static final String BIS_ACK_INS003 = "BISACKINS003";
    private final InspectionClient inspectionClient;
    private final InspectionService inspectionService;
    private final RfiClient rfiClient;
    private final DualDocSortingService dualDocSortingService;


    public void start(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        MaskHelper.taskProcessUnmask(request, KEY_APP_ID, KEY_TASK_ID);
        AuditTrailHelper.auditFunction(MODULE_INSPECTION, FUNCTION_DO_REVIEW_FOLLOW_UP_ITEMS);
        DualDocSortingService.clearDualDocSortingInfo(request);
        request.getSession().removeAttribute(KEY_REVIEW_FOLLOW_UP_DTO);
        request.getSession().removeAttribute(KEY_INS_DECISION);
        request.getSession().removeAttribute(KEY_DOC_DISPLAY_DTO_REPO_ID_NAME_MAP);
    }

    public void prepare(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        String appId = (String) ParamUtil.getSessionAttr(request, KEY_APP_ID);
        inspectionService.getInitFollowUpData(request, appId, RoleConsts.USER_ROLE_BSB_AO);
        dualDocSortingService.retrieveDualDocDisplayDtoIntoRequest(request, appId);

        //decide what kind of page you want
        ParamUtil.setRequestAttr(request, KEY_REQUEST_EXTENSION, NO);
        ParamUtil.setRequestAttr(request, KEY_CAN_NOT_UPLOAD_DOC, true);
    }

    public void bindAction(BaseProcessClass bpc) {
        DualDocSortingService.readDualDocSortingInfo(bpc.request);
    }

    public void handleSubmit(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        InsFollowUpProcessDto insProcessDto = getInsProcessDto(request);
        insProcessDto.reqObjMapping(request);
        ParamUtil.setSessionAttr(request, KEY_INS_DECISION, insProcessDto);

        ValidationResultDto validationResultDto = inspectionClient.validatePostInspectionDOReviewFollowUpItems(insProcessDto);
        String validateResult;
        if (validationResultDto.isPass()) {
            String processingDecision = insProcessDto.getDecision();
            if (MasterCodeConstants.MOH_PROCESS_DECISION_ROUTE_BACK_TO_APPLICANT.equals(processingDecision)) {
                validateResult = "routeBack";
            } else if (MasterCodeConstants.MOH_PROCESS_DECISION_APPROVE.equals(processingDecision)) {
                validateResult = "acceptResponse";
            } else if (MasterCodeConstants.MOH_PROCESS_DECISION_REJECT.equals(processingDecision)) {
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

    public void routeBackToApplicant(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        String appId = (String) ParamUtil.getSessionAttr(request, KEY_APP_ID);
        String taskId = (String) ParamUtil.getSessionAttr(request, KEY_TASK_ID);
        InsFollowUpProcessDto insProcessDto = getInsProcessDto(request);
        rfiClient.doReviewInspectionFollowUpItemsRFI(appId, taskId, insProcessDto);
        ParamUtil.setRequestAttr(request, ModuleCommonConstants.RESULT_MSG, BIS_ACK_INS003);
    }

    /**
     * if the task is request extension,update app data and complete task (and save the new due date)
     * if the task is submit follow up data,update app data and complete task and create new task for Approval Officer
     */
    public void acceptResponse(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        String appId = (String) ParamUtil.getSessionAttr(request, KEY_APP_ID);
        String taskId = (String) ParamUtil.getSessionAttr(request, KEY_TASK_ID);
        InsFollowUpProcessDto insProcessDto = getInsProcessDto(request);
        inspectionClient.doReviewInspectionFollowUpItemsAcceptResponse(appId, taskId, insProcessDto);
        ParamUtil.setRequestAttr(request, ModuleCommonConstants.RESULT_MSG, BIS_ACK_INS003);
    }

    /**
     * if the task is request extension,update app data and complete task
     * if the task is submit follow up data,update app data and complete task and create new task for Approval Officer
     */
    public void rejectAction(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        String appId = (String) ParamUtil.getSessionAttr(request, KEY_APP_ID);
        String taskId = (String) ParamUtil.getSessionAttr(request, KEY_TASK_ID);
        InsFollowUpProcessDto insProcessDto = getInsProcessDto(request);
        inspectionClient.doReviewInspectionFollowUpItemsAcceptResponse(appId, taskId, insProcessDto);
        ParamUtil.setRequestAttr(request, ModuleCommonConstants.RESULT_MSG, BIS_ACK_INS003);
    }

    public void skip(BaseProcessClass bpc) {
        throw new UnsupportedOperationException("Can not skip inspection in follow up module");
    }

    public ReviewInsFollowUpDto getDisplayDto(HttpServletRequest request) {
        ReviewInsFollowUpDto dto = (ReviewInsFollowUpDto) ParamUtil.getSessionAttr(request, KEY_REVIEW_FOLLOW_UP_DTO);
        if (dto == null) {
            dto = new ReviewInsFollowUpDto();
        }
        return dto;
    }

    public InsFollowUpProcessDto getInsProcessDto(HttpServletRequest request) {
        InsFollowUpProcessDto dto = (InsFollowUpProcessDto) ParamUtil.getSessionAttr(request, KEY_INS_DECISION);
        if (dto == null) {
            dto = new InsFollowUpProcessDto();
        }
        return dto;
    }
}

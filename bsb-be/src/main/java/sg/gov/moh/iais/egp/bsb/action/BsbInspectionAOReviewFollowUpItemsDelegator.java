package sg.gov.moh.iais.egp.bsb.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import sg.gov.moh.iais.egp.bsb.client.InspectionClient;
import sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants;
import sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants;
import sg.gov.moh.iais.egp.bsb.dto.inspection.InsProcessDto;
import sg.gov.moh.iais.egp.bsb.dto.inspection.followup.ReviewInsFollowUpDto;
import sg.gov.moh.iais.egp.bsb.dto.validation.ValidationResultDto;
import sg.gov.moh.iais.egp.bsb.service.DualDocSortingService;
import sg.gov.moh.iais.egp.bsb.service.InspectionService;
import sg.gov.moh.iais.egp.bsb.util.MaskHelper;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;

import static com.ecquaria.cloud.moh.iais.common.constant.BsbAuditTrailConstants.FUNCTION_AO_REVIEW_FOLLOW_UP_ITEMS;
import static com.ecquaria.cloud.moh.iais.common.constant.BsbAuditTrailConstants.MODULE_INSPECTION;
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
@RequiredArgsConstructor
public class BsbInspectionAOReviewFollowUpItemsDelegator {
    private final InspectionClient inspectionClient;
    private final InspectionService inspectionService;
    private final DualDocSortingService dualDocSortingService;


    public void start(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        MaskHelper.taskProcessUnmask(request, KEY_APP_ID, KEY_TASK_ID);
        AuditTrailHelper.auditFunction(MODULE_INSPECTION, FUNCTION_AO_REVIEW_FOLLOW_UP_ITEMS);
        DualDocSortingService.clearDualDocSortingInfo(request);
        request.getSession().removeAttribute(KEY_REVIEW_FOLLOW_UP_DTO);
        request.getSession().removeAttribute(KEY_INS_DECISION);
        request.getSession().removeAttribute(KEY_DOC_DISPLAY_DTO_REPO_ID_NAME_MAP);
    }

    public void prepare(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        String appId = (String) ParamUtil.getSessionAttr(request, KEY_APP_ID);
        inspectionService.getInitFollowUpData(request, appId,null);
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
        InsProcessDto insProcessDto = getInsProcessDto(request);
        insProcessDto.reqObjMapping(request);
        ParamUtil.setSessionAttr(request, KEY_INS_DECISION, insProcessDto);

        ValidationResultDto validationResultDto = inspectionClient.validatePostInspectionAOReviewFollowUpItems(insProcessDto);
        String validateResult;
        if (validationResultDto.isPass()) {
            String processingDecision = insProcessDto.getDecision();
            if (MasterCodeConstants.MOH_PROCESS_DECISION_ROUTE_BACK_TO_DO.equals(processingDecision)) {
                validateResult = "routeBack";
            } else if (MasterCodeConstants.MOH_PROCESS_DECISION_APPROVE.equals(processingDecision)) {
                validateResult = "approve";
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
        ParamUtil.setRequestAttr(request, ModuleCommonConstants.RESULT_MSG, "BISACKINS003");
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
        ParamUtil.setRequestAttr(request, ModuleCommonConstants.RESULT_MSG, "BISACKINS003");
    }

    public InsProcessDto getInsProcessDto(HttpServletRequest request) {
        InsProcessDto dto = (InsProcessDto) ParamUtil.getSessionAttr(request, KEY_INS_DECISION);
        if (dto == null) {
            dto = new InsProcessDto();
        }
        return dto;
    }
}

package sg.gov.moh.iais.egp.bsb.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.role.RoleConsts;
import com.ecquaria.cloud.moh.iais.common.exception.IaisRuntimeException;
import com.ecquaria.cloud.moh.iais.common.utils.LogUtil;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import sg.gov.moh.iais.egp.bsb.client.InspectionClient;
import sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants;
import sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants;
import sg.gov.moh.iais.egp.bsb.dto.inspection.InsReportAOApprovalProcessDto;
import sg.gov.moh.iais.egp.bsb.dto.inspection.InsSubmitReportDataDto;
import sg.gov.moh.iais.egp.bsb.dto.process.DualDocSortingDto;
import sg.gov.moh.iais.egp.bsb.dto.validation.ValidationResultDto;
import sg.gov.moh.iais.egp.bsb.service.DocSettingService;
import sg.gov.moh.iais.egp.bsb.service.DualDocSortingService;
import sg.gov.moh.iais.egp.bsb.service.InspectionService;
import sg.gov.moh.iais.egp.bsb.util.MaskHelper;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;

import static com.ecquaria.cloud.moh.iais.common.constant.BsbAuditTrailConstants.FUNCTION_AO_INSPECTION_REPORT_APPROVAL;
import static com.ecquaria.cloud.moh.iais.common.constant.BsbAuditTrailConstants.MODULE_INSPECTION;
import static sg.gov.moh.iais.egp.bsb.constant.module.InspectionConstants.KEY_APP_ID;
import static sg.gov.moh.iais.egp.bsb.constant.module.InspectionConstants.KEY_INS_DECISION;
import static sg.gov.moh.iais.egp.bsb.constant.module.InspectionConstants.KEY_INS_REPORT;
import static sg.gov.moh.iais.egp.bsb.constant.module.InspectionConstants.KEY_INS_SUBMIT_REPORT_DATE_DTO;
import static sg.gov.moh.iais.egp.bsb.constant.module.InspectionConstants.KEY_TASK_ID;
import static sg.gov.moh.iais.egp.bsb.constant.module.InspectionConstants.TAB_ACTIVE;
import static sg.gov.moh.iais.egp.bsb.constant.module.InspectionConstants.TAB_PROCESSING;
import static sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants.INTERNAL_DOCUMENT_TYPE_LIST;
import static sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants.KEY_ACTION_TYPE;
import static sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants.KEY_ACTION_TYPE_SORT;
import static sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants.KEY_BACK;
import static sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants.KEY_CRUD_ACTION_TYPE;
import static sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants.KEY_FACILITY_DETAILS_INFO;
import static sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants.KEY_ROUTING_HISTORY_LIST;
import static sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants.KEY_SELECT_ROUTE_TO_MOH;
import static sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants.KEY_SUBMISSION_DETAILS_INFO;
import static sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants.KEY_SUBMIT;
import static sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants.KEY_VALIDATION_ERRORS;


/**
 * AO inspection report approval
 */
@Slf4j
@RequiredArgsConstructor
@Delegator("bsbInspectionReportAOApprovalDelegator")
public class BsbInspectionReportAOApprovalDelegator {
    private static final String BIS_ACK_INS003 = "BISACKINS003";

    private final DocSettingService docSettingService;
    private final InspectionService inspectionService;
    private final DualDocSortingService dualDocSortingService;
    private final InspectionClient inspectionClient;


    public void start(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        MaskHelper.taskProcessUnmask(request, KEY_APP_ID, KEY_TASK_ID);

        AuditTrailHelper.auditFunction(MODULE_INSPECTION, FUNCTION_AO_INSPECTION_REPORT_APPROVAL);

        HttpSession session = request.getSession();
        DualDocSortingService.clearDualDocSortingInfo(session);
        session.removeAttribute(KEY_INS_SUBMIT_REPORT_DATE_DTO);
        session.removeAttribute(KEY_INS_DECISION);

        String appId = (String) ParamUtil.getSessionAttr(request, KEY_APP_ID);

        InsSubmitReportDataDto initDataDto = inspectionService.getInsSubmitReportDataDto(request, appId, RoleConsts.USER_ROLE_BSB_HM);
        ParamUtil.setSessionAttr(request, KEY_INS_SUBMIT_REPORT_DATE_DTO, initDataDto);

        // inspection processing
        InsReportAOApprovalProcessDto processDto = new InsReportAOApprovalProcessDto();
        ParamUtil.setSessionAttr(request, KEY_INS_DECISION, processDto);
    }


    public void pre(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        String appId = (String) ParamUtil.getSessionAttr(request, KEY_APP_ID);
        dualDocSortingService.retrieveDualDocDisplayDtoIntoRequest(request, appId);

        InsSubmitReportDataDto initDataDto = (InsSubmitReportDataDto) ParamUtil.getSessionAttr(request, KEY_INS_SUBMIT_REPORT_DATE_DTO);
        // submission details info
        ParamUtil.setRequestAttr(request, KEY_SUBMISSION_DETAILS_INFO, initDataDto.getSubmissionDetailsInfo());
        // facility details
        ParamUtil.setRequestAttr(request, KEY_FACILITY_DETAILS_INFO, initDataDto.getFacilityDetailsInfo());

        // show route to moh selection list
        ParamUtil.setRequestAttr(request, KEY_SELECT_ROUTE_TO_MOH, new ArrayList<>(initDataDto.getSelectRouteToMoh()));

        // inspection report
        ParamUtil.setRequestAttr(request, KEY_INS_REPORT, initDataDto.getReportDto());
        // show routingHistory list
        ParamUtil.setRequestAttr(request, KEY_ROUTING_HISTORY_LIST, new ArrayList<>(initDataDto.getProcessHistoryDtoList()));

        //set internal doc types
        ParamUtil.setRequestAttr(request, INTERNAL_DOCUMENT_TYPE_LIST, docSettingService.getInternalDocTypeList());
    }


    public void bindAction(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        InsReportAOApprovalProcessDto processDto = (InsReportAOApprovalProcessDto) ParamUtil.getSessionAttr(request, KEY_INS_DECISION);
        processDto.reqObjMapping(request);
        ParamUtil.setSessionAttr(request, KEY_INS_DECISION, processDto);

        String actionType = ParamUtil.getString(request, KEY_ACTION_TYPE);
        if (KEY_ACTION_TYPE_SORT.equals(actionType)) {
            DualDocSortingDto dualDocSortingDto = DualDocSortingService.retrieveDualDocSortingInfo(request);
            DualDocSortingService.readDualDocSortingInfo(request, dualDocSortingDto);
            ParamUtil.setRequestAttr(request,KEY_CRUD_ACTION_TYPE, KEY_ACTION_TYPE_SORT);
        } else if (KEY_SUBMIT.equals(actionType)) {
            ValidationResultDto validationResultDto = inspectionClient.validateActualInspectionReportAOApprovalDecision(processDto);
            String validateResult;
            if (validationResultDto.isPass()) {
                validateResult = KEY_SUBMIT;
            } else {
                log.error("Validation failure info: {}", validationResultDto.toErrorMsg());
                ParamUtil.setRequestAttr(request, KEY_VALIDATION_ERRORS, validationResultDto.toErrorMsg());
                ParamUtil.setRequestAttr(request, TAB_ACTIVE, TAB_PROCESSING);
                validateResult = KEY_BACK;
            }
            ParamUtil.setRequestAttr(request,KEY_CRUD_ACTION_TYPE,validateResult);
            log.info("Officer submit decision [{}] for review inspection report, route result [{}]", LogUtil.escapeCrlf(processDto.getDecision()), validateResult);
        } else {
            throw new IaisRuntimeException("Invalid action type");
        }
    }


    public void handleSubmit(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        InsReportAOApprovalProcessDto processDto = (InsReportAOApprovalProcessDto) ParamUtil.getSessionAttr(request, KEY_INS_DECISION);

        if (MasterCodeConstants.MOH_PROCESS_DECISION_APPROVE.equals(processDto.getDecision())) {
            approve(bpc);
        } else if (MasterCodeConstants.MOH_PROCESS_DECISION_ROUTE_BACK_TO_DO.equals(processDto.getDecision())) {
            routeBack(bpc);
        } else if (MasterCodeConstants.MOH_PROCESS_DECISION_ROUTE_TO_HM_FOR_REVIEW.equals(processDto.getDecision())) {
            routeToHM(bpc);
        }
    }


    public void approve(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        String appId = (String) ParamUtil.getSessionAttr(request, KEY_APP_ID);
        String taskId = (String) ParamUtil.getSessionAttr(request, KEY_TASK_ID);
        InsReportAOApprovalProcessDto processDto = (InsReportAOApprovalProcessDto) ParamUtil.getSessionAttr(request, KEY_INS_DECISION);
        inspectionClient.aoFinalizeInspectionReport(appId, taskId, processDto);
        ParamUtil.setRequestAttr(request, ModuleCommonConstants.RESULT_MSG, BIS_ACK_INS003);
    }

    public void routeBack(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        String appId = (String) ParamUtil.getSessionAttr(request, KEY_APP_ID);
        String taskId = (String) ParamUtil.getSessionAttr(request, KEY_TASK_ID);
        InsReportAOApprovalProcessDto processDto = (InsReportAOApprovalProcessDto) ParamUtil.getSessionAttr(request, KEY_INS_DECISION);
        inspectionClient.aoRouteBackFinalInspectionReport(appId, taskId, processDto);
        ParamUtil.setRequestAttr(request, ModuleCommonConstants.RESULT_MSG, BIS_ACK_INS003);
    }

    public void routeToHM(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        String appId = (String) ParamUtil.getSessionAttr(request, KEY_APP_ID);
        String taskId = (String) ParamUtil.getSessionAttr(request, KEY_TASK_ID);
        InsReportAOApprovalProcessDto processDto = (InsReportAOApprovalProcessDto) ParamUtil.getSessionAttr(request, KEY_INS_DECISION);
        inspectionClient.aoRouteFinalInspectionReportToHM(appId, taskId, processDto);
        ParamUtil.setRequestAttr(request, ModuleCommonConstants.RESULT_MSG, BIS_ACK_INS003);
    }
}

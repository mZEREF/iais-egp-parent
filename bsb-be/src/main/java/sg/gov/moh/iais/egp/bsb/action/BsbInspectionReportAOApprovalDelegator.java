package sg.gov.moh.iais.egp.bsb.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.role.RoleConsts;
import com.ecquaria.cloud.moh.iais.common.utils.LogUtil;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.MasterCodeUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sg.gov.moh.iais.egp.bsb.client.InspectionClient;
import sg.gov.moh.iais.egp.bsb.client.InternalDocClient;
import sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants;
import sg.gov.moh.iais.egp.bsb.constant.StageConstants;
import sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants;
import sg.gov.moh.iais.egp.bsb.constant.module.TaskModuleConstants;
import sg.gov.moh.iais.egp.bsb.dto.file.DocDisplayDto;
import sg.gov.moh.iais.egp.bsb.dto.inspection.InsProcessDto;
import sg.gov.moh.iais.egp.bsb.dto.inspection.InsSubmitReportDataDto;
import sg.gov.moh.iais.egp.bsb.dto.validation.ValidationResultDto;
import sg.gov.moh.iais.egp.bsb.service.AppViewService;
import sg.gov.moh.iais.egp.bsb.service.InspectionService;
import sg.gov.moh.iais.egp.bsb.util.MaskHelper;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

import static com.ecquaria.cloud.moh.iais.common.constant.BsbAuditTrailConstants.FUNCTION_AO_INSPECTION_REPORT_APPROVAL;
import static com.ecquaria.cloud.moh.iais.common.constant.BsbAuditTrailConstants.MODULE_INSPECTION;
import static sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants.APP_STATUS_PEND_AO_REPORT_APPROVAL;
import static sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants.APP_STATUS_PEND_DO_REPORT_APPROVAL;
import static sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants.APP_STATUS_PEND_HM_REPORT_APPROVAL;
import static sg.gov.moh.iais.egp.bsb.constant.module.InspectionConstants.KEY_APP_ID;
import static sg.gov.moh.iais.egp.bsb.constant.module.InspectionConstants.KEY_INS_DECISION;
import static sg.gov.moh.iais.egp.bsb.constant.module.InspectionConstants.KEY_INS_REPORT;
import static sg.gov.moh.iais.egp.bsb.constant.module.InspectionConstants.KEY_TASK_ID;
import static sg.gov.moh.iais.egp.bsb.constant.module.InspectionConstants.TAB_ACTIVE;
import static sg.gov.moh.iais.egp.bsb.constant.module.InspectionConstants.TAB_PROCESSING;
import static sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants.KEY_FACILITY_DETAILS_INFO;
import static sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants.KEY_PREVIOUS_OFFICER_NOTE;
import static sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants.KEY_ROUTING_HISTORY_LIST;
import static sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants.KEY_SELECT_ROUTE_TO_MOH;
import static sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants.KEY_SUBMISSION_DETAILS_INFO;
import static sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants.KEY_TAB_DOCUMENT_INTERNAL_DOC_LIST;
import static sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants.KEY_VALIDATION_ERRORS;


/**
 * AO inspection report approval
 */
@Slf4j
@Delegator("bsbInspectionReportAOApprovalDelegator")
public class BsbInspectionReportAOApprovalDelegator {
    private final InspectionService inspectionService;
    private final InspectionClient inspectionClient;
    private final InternalDocClient internalDocClient;

    @Autowired
    public BsbInspectionReportAOApprovalDelegator(InspectionService inspectionService, InspectionClient inspectionClient, InternalDocClient internalDocClient) {
        this.inspectionService = inspectionService;
        this.inspectionClient = inspectionClient;
        this.internalDocClient = internalDocClient;
    }


    public void start(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        MaskHelper.taskProcessUnmask(request, KEY_APP_ID, KEY_TASK_ID);

        AuditTrailHelper.auditFunction(MODULE_INSPECTION, FUNCTION_AO_INSPECTION_REPORT_APPROVAL);

        HttpSession session = request.getSession();
        session.removeAttribute(KEY_SUBMISSION_DETAILS_INFO);
        session.removeAttribute(KEY_FACILITY_DETAILS_INFO);
        session.removeAttribute(KEY_INS_REPORT);
        session.removeAttribute(KEY_ROUTING_HISTORY_LIST);
        session.removeAttribute(KEY_INS_DECISION);

        String appId = (String) ParamUtil.getSessionAttr(request, KEY_APP_ID);

        // judge whether we need to send email
        InsSubmitReportDataDto initDataDto = inspectionClient.getInitInsSubmitReportData(appId, RoleConsts.USER_ROLE_BSB_HM);

        // submission details info
        ParamUtil.setSessionAttr(request, KEY_SUBMISSION_DETAILS_INFO, initDataDto.getSubmissionDetailsInfo());
        // facility details
        ParamUtil.setSessionAttr(request, KEY_FACILITY_DETAILS_INFO, initDataDto.getFacilityDetailsInfo());

        // show route to moh selection list
        ParamUtil.setSessionAttr(request, KEY_SELECT_ROUTE_TO_MOH, new ArrayList<>(initDataDto.getSelectRouteToMoh()));

        // inspection report
        ParamUtil.setSessionAttr(request, KEY_INS_REPORT, initDataDto.getReportDto());
        // show routingHistory list
        ParamUtil.setSessionAttr(request, KEY_ROUTING_HISTORY_LIST, new ArrayList<>(initDataDto.getProcessHistoryDtoList()));

        ParamUtil.setSessionAttr(request, KEY_PREVIOUS_OFFICER_NOTE, initDataDto.getPrevOfficerNoteDto());

        // inspection processing
        InsProcessDto processDto = new InsProcessDto();
        ParamUtil.setSessionAttr(request, KEY_INS_DECISION, processDto);
    }


    public void pre(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        String appId = (String) ParamUtil.getSessionAttr(request, KEY_APP_ID);
        List<DocDisplayDto> internalDocDisplayDto = internalDocClient.getInternalDocForDisplay(appId);
        ParamUtil.setRequestAttr(request, KEY_TAB_DOCUMENT_INTERNAL_DOC_LIST, internalDocDisplayDto);

        // view application
        AppViewService.facilityRegistrationViewApp(request, appId);
    }


    public void bindAction(BaseProcessClass bpc) {
        // do nothing now
    }


    public void handleSubmit(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        InsProcessDto processDto = (InsProcessDto) ParamUtil.getSessionAttr(request, KEY_INS_DECISION);
        processDto.reqObjMapping(request);
        ParamUtil.setSessionAttr(request, KEY_INS_DECISION, processDto);

        ValidationResultDto validationResultDto = inspectionClient.validateActualInspectionReportAOApprovalDecision(processDto);
        String validateResult;
        if (validationResultDto.isPass()) {
            if (MasterCodeConstants.MOH_PROCESS_DECISION_APPROVE.equals(processDto.getDecision())) {
                approve(bpc);
                validateResult = "approve";
            } else if (MasterCodeConstants.MOH_PROCESS_DECISION_ROUTE_BACK_TO_DO.equals(processDto.getDecision())) {
                routeBack(bpc);
                validateResult = "routeBack";
            } else if (MasterCodeConstants.MOH_PROCESS_DECISION_ROUTE_TO_HM_FOR_REVIEW.equals(processDto.getDecision())) {
                routeToHM(bpc);
                validateResult = "toHM";
            } else if (MasterCodeConstants.MOH_PROCESS_DECISION_SKIP_INSPECTION.equals(processDto.getDecision())) {
                skip(bpc);
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
    }


    public void approve(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        String appId = (String) ParamUtil.getSessionAttr(request, KEY_APP_ID);
        String taskId = (String) ParamUtil.getSessionAttr(request, KEY_TASK_ID);
        InsProcessDto processDto = (InsProcessDto) ParamUtil.getSessionAttr(request, KEY_INS_DECISION);
        String nextAppStatus = inspectionClient.aoFinalizeInspectionReport(appId, taskId, processDto);

        ParamUtil.setRequestAttr(request, TaskModuleConstants.KEY_CURRENT_TASK, MasterCodeUtil.getCodeDesc(APP_STATUS_PEND_AO_REPORT_APPROVAL));
        ParamUtil.setRequestAttr(request,TaskModuleConstants.KEY_NEXT_TASK, MasterCodeUtil.getCodeDesc(nextAppStatus));
        String nextRole = MasterCodeConstants.APP_STATUS_PEND_NC_RECTIFICATION.equals(nextAppStatus) ? StageConstants.ROLE_APPLICANT : StageConstants.ROLE_DO;
        ParamUtil.setRequestAttr(request,TaskModuleConstants.KEY_NEXT_ROLE, nextRole);
    }

    public void routeBack(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        String appId = (String) ParamUtil.getSessionAttr(request, KEY_APP_ID);
        String taskId = (String) ParamUtil.getSessionAttr(request, KEY_TASK_ID);
        InsProcessDto processDto = (InsProcessDto) ParamUtil.getSessionAttr(request, KEY_INS_DECISION);
        inspectionClient.aoRouteBackFinalInspectionReport(appId, taskId, processDto);

        ParamUtil.setRequestAttr(request, TaskModuleConstants.KEY_CURRENT_TASK, MasterCodeUtil.getCodeDesc(APP_STATUS_PEND_AO_REPORT_APPROVAL));
        ParamUtil.setRequestAttr(request,TaskModuleConstants.KEY_NEXT_TASK, MasterCodeUtil.getCodeDesc(APP_STATUS_PEND_DO_REPORT_APPROVAL));
        ParamUtil.setRequestAttr(request,TaskModuleConstants.KEY_NEXT_ROLE, ModuleCommonConstants.KEY_DO);
    }

    public void routeToHM(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        String appId = (String) ParamUtil.getSessionAttr(request, KEY_APP_ID);
        String taskId = (String) ParamUtil.getSessionAttr(request, KEY_TASK_ID);
        InsProcessDto processDto = (InsProcessDto) ParamUtil.getSessionAttr(request, KEY_INS_DECISION);
        inspectionClient.aoRouteFinalInspectionReportToHM(appId, taskId, processDto);

        ParamUtil.setRequestAttr(request, TaskModuleConstants.KEY_CURRENT_TASK, MasterCodeUtil.getCodeDesc(APP_STATUS_PEND_AO_REPORT_APPROVAL));
        ParamUtil.setRequestAttr(request,TaskModuleConstants.KEY_NEXT_TASK, MasterCodeUtil.getCodeDesc(APP_STATUS_PEND_HM_REPORT_APPROVAL));
        ParamUtil.setRequestAttr(request,TaskModuleConstants.KEY_NEXT_ROLE, ModuleCommonConstants.KEY_HM);
    }

    public void skip(BaseProcessClass bpc) {
        inspectionService.skipInspection(bpc.request);
    }
}

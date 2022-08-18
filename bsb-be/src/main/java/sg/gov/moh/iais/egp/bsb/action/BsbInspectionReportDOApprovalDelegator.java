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
import sg.gov.moh.iais.egp.bsb.dto.inspection.InsReportDoApprovalProcessDto;
import sg.gov.moh.iais.egp.bsb.dto.inspection.InsSubmitReportDataDto;
import sg.gov.moh.iais.egp.bsb.dto.inspection.ReportDto;
import sg.gov.moh.iais.egp.bsb.dto.validation.ValidationResultDto;
import sg.gov.moh.iais.egp.bsb.service.AppViewService;
import sg.gov.moh.iais.egp.bsb.service.InspectionService;
import sg.gov.moh.iais.egp.bsb.util.MaskHelper;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import java.util.ArrayList;
import java.util.List;

import static com.ecquaria.cloud.moh.iais.common.constant.BsbAuditTrailConstants.FUNCTION_DO_INSPECTION_REPORT_APPROVAL;
import static com.ecquaria.cloud.moh.iais.common.constant.BsbAuditTrailConstants.MODULE_INSPECTION;
import static sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants.APP_STATUS_PEND_AO_REPORT_APPROVAL;
import static sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants.APP_STATUS_PEND_APPLICANT_INPUT;
import static sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants.APP_STATUS_PEND_DO_REPORT_APPROVAL;
import static sg.gov.moh.iais.egp.bsb.constant.module.InspectionConstants.KEY_AFTER_SAVE_REPORT;
import static sg.gov.moh.iais.egp.bsb.constant.module.InspectionConstants.KEY_APP_ID;
import static sg.gov.moh.iais.egp.bsb.constant.module.InspectionConstants.KEY_INS_DECISION;
import static sg.gov.moh.iais.egp.bsb.constant.module.InspectionConstants.KEY_INS_REPORT;
import static sg.gov.moh.iais.egp.bsb.constant.module.InspectionConstants.KEY_ROUTE;
import static sg.gov.moh.iais.egp.bsb.constant.module.InspectionConstants.KEY_TASK_ID;
import static sg.gov.moh.iais.egp.bsb.constant.module.InspectionConstants.TAB_ACTIVE;
import static sg.gov.moh.iais.egp.bsb.constant.module.InspectionConstants.TAB_INS_REPORT;
import static sg.gov.moh.iais.egp.bsb.constant.module.InspectionConstants.TAB_PROCESSING;
import static sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants.KEY_FACILITY_DETAILS_INFO;
import static sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants.KEY_PREVIOUS_OFFICER_NOTE;
import static sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants.KEY_ROUTING_HISTORY_LIST;
import static sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants.KEY_SELECT_ROUTE_TO_MOH;
import static sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants.KEY_SUBMISSION_DETAILS_INFO;
import static sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants.KEY_TAB_DOCUMENT_INTERNAL_DOC_LIST;
import static sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants.KEY_VALIDATION_ERRORS;


/**
 * DO inspection report approval
 */
@Slf4j
@Delegator("bsbInspectionReportDOApprovalDelegator")
public class BsbInspectionReportDOApprovalDelegator {
    private final InspectionService inspectionService;
    private final InspectionClient inspectionClient;
    private final InternalDocClient internalDocClient;

    @Autowired
    public BsbInspectionReportDOApprovalDelegator(InspectionService inspectionService, InspectionClient inspectionClient, InternalDocClient internalDocClient) {
        this.inspectionService = inspectionService;
        this.inspectionClient = inspectionClient;
        this.internalDocClient = internalDocClient;
    }


    public void start(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        MaskHelper.taskProcessUnmask(request, KEY_APP_ID, KEY_TASK_ID);

        AuditTrailHelper.auditFunction(MODULE_INSPECTION, FUNCTION_DO_INSPECTION_REPORT_APPROVAL);

        HttpSession session = request.getSession();
        session.removeAttribute(KEY_SUBMISSION_DETAILS_INFO);
        session.removeAttribute(KEY_FACILITY_DETAILS_INFO);
        session.removeAttribute(KEY_INS_REPORT);
        session.removeAttribute(KEY_ROUTING_HISTORY_LIST);
        session.removeAttribute(KEY_INS_DECISION);

        String appId = (String) ParamUtil.getSessionAttr(request, KEY_APP_ID);

        // judge whether we need to send email
        InsSubmitReportDataDto initDataDto = inspectionClient.getInitInsSubmitReportData(appId, RoleConsts.USER_ROLE_BSB_AO);

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

        ParamUtil.setSessionAttr(request, KEY_PREVIOUS_OFFICER_NOTE,initDataDto.getPrevOfficerNoteDto());


        // inspection processing
        InsReportDoApprovalProcessDto processDto = new InsReportDoApprovalProcessDto();
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

    public void handleSaveReport(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        String appId = (String) ParamUtil.getSessionAttr(request, KEY_APP_ID);

        InsReportDoApprovalProcessDto processDto = (InsReportDoApprovalProcessDto) ParamUtil.getSessionAttr(request, KEY_INS_DECISION);
        processDto.reqObjMapping(request);
        ParamUtil.setSessionAttr(request, KEY_INS_DECISION, processDto);

        ReportDto reportDto = (ReportDto) ParamUtil.getSessionAttr(request, KEY_INS_REPORT);
        reportDto.reqObjMapping(request);
        ParamUtil.setSessionAttr(request, KEY_INS_REPORT, reportDto);
        ValidationResultDto validationResultDto = inspectionClient.validateActualInspectionReport(reportDto);
        if (validationResultDto.isPass()) {
            inspectionClient.saveInspectionReportDto(appId, StageConstants.ROLE_DO, reportDto);
            ParamUtil.setRequestAttr(request, KEY_AFTER_SAVE_REPORT, Boolean.TRUE);
        } else {
            log.error("Validation inspection report failure info: {}", validationResultDto.toErrorMsg());
            ParamUtil.setRequestAttr(request, KEY_VALIDATION_ERRORS, validationResultDto.toErrorMsg());
        }
        ParamUtil.setRequestAttr(request, TAB_ACTIVE, TAB_INS_REPORT);
    }


    public void handleSubmit(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        InsReportDoApprovalProcessDto processDto = (InsReportDoApprovalProcessDto) ParamUtil.getSessionAttr(request, KEY_INS_DECISION);
        processDto.reqObjMapping(request);
        ParamUtil.setSessionAttr(request, KEY_INS_DECISION, processDto);

        ValidationResultDto validationResultDto = inspectionClient.validateInspectionReportDOApprovalDecision(processDto);
        String validateResult;
        if (validationResultDto.isPass()) {
            if (MasterCodeConstants.MOH_PROCESS_DECISION_MARK_AS_FINAL_AND_ROUTE_TO_AO.equals(processDto.getDecision())) {
                validateResult = "markAsFinal";
            } else if (MasterCodeConstants.MOH_PROCESS_DECISION_ROUTE_BACK_TO_APPLICANT.equals(processDto.getDecision())) {
                validateResult = "rfi";
            } else if (MasterCodeConstants.MOH_PROCESS_DECISION_SKIP_INSPECTION.equals(processDto.getDecision())){
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


    public void markAsFinal(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        String appId = (String) ParamUtil.getSessionAttr(request, KEY_APP_ID);
        String taskId = (String) ParamUtil.getSessionAttr(request, KEY_TASK_ID);
        InsReportDoApprovalProcessDto processDto = (InsReportDoApprovalProcessDto) ParamUtil.getSessionAttr(request, KEY_INS_DECISION);
        inspectionClient.doFinalizeInspectionReport(appId, taskId, processDto);

        ParamUtil.setRequestAttr(request, TaskModuleConstants.KEY_CURRENT_TASK, MasterCodeUtil.getCodeDesc(APP_STATUS_PEND_DO_REPORT_APPROVAL));
        ParamUtil.setRequestAttr(request,TaskModuleConstants.KEY_NEXT_TASK, MasterCodeUtil.getCodeDesc(APP_STATUS_PEND_AO_REPORT_APPROVAL));
        ParamUtil.setRequestAttr(request,TaskModuleConstants.KEY_NEXT_ROLE, ModuleCommonConstants.KEY_AO);
    }


    public void rfi(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        String appId = (String) ParamUtil.getSessionAttr(request, KEY_APP_ID);
        String taskId = (String) ParamUtil.getSessionAttr(request, KEY_TASK_ID);
        InsReportDoApprovalProcessDto processDto = (InsReportDoApprovalProcessDto) ParamUtil.getSessionAttr(request, KEY_INS_DECISION);
        inspectionClient.routeInspectionReportToApplicant(appId, taskId, processDto);
        ParamUtil.setRequestAttr(request, TaskModuleConstants.KEY_CURRENT_TASK, MasterCodeUtil.getCodeDesc(APP_STATUS_PEND_DO_REPORT_APPROVAL));
        ParamUtil.setRequestAttr(request, TaskModuleConstants.KEY_NEXT_TASK, MasterCodeUtil.getCodeDesc(APP_STATUS_PEND_APPLICANT_INPUT));
        ParamUtil.setRequestAttr(request, TaskModuleConstants.KEY_NEXT_ROLE, ModuleCommonConstants.KEY_APPLICANT);
    }

    public void skip(BaseProcessClass bpc){
        inspectionService.skipInspection(bpc.request);
    }
}

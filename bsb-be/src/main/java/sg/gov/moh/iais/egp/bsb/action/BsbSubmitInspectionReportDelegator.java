package sg.gov.moh.iais.egp.bsb.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.AuditTrailConsts;
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
import sg.gov.moh.iais.egp.bsb.constant.module.InspectionConstants;
import sg.gov.moh.iais.egp.bsb.constant.module.TaskModuleConstants;
import sg.gov.moh.iais.egp.bsb.dto.inspection.InsReportProcessDto;
import sg.gov.moh.iais.egp.bsb.dto.inspection.ReportDto;
import sg.gov.moh.iais.egp.bsb.dto.validation.ValidationResultDto;
import sg.gov.moh.iais.egp.bsb.dto.file.DocDisplayDto;
import sg.gov.moh.iais.egp.bsb.dto.inspection.InsProcessDto;
import sg.gov.moh.iais.egp.bsb.dto.inspection.InsSubmitReportDataDto;
import sg.gov.moh.iais.egp.bsb.service.AppViewService;
import sg.gov.moh.iais.egp.bsb.util.MaskHelper;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.Serializable;
import java.util.List;

import static sg.gov.moh.iais.egp.bsb.constant.module.InspectionConstants.KEY_AFTER_SAVE_REPORT;
import static sg.gov.moh.iais.egp.bsb.constant.module.InspectionConstants.KEY_APP_ID;
import static sg.gov.moh.iais.egp.bsb.constant.module.InspectionConstants.KEY_APP_STATUS;
import static sg.gov.moh.iais.egp.bsb.constant.module.InspectionConstants.KEY_INS_DECISION;
import static sg.gov.moh.iais.egp.bsb.constant.module.InspectionConstants.KEY_INS_REPORT;
import static sg.gov.moh.iais.egp.bsb.constant.module.InspectionConstants.KEY_ROUTE;
import static sg.gov.moh.iais.egp.bsb.constant.module.InspectionConstants.KEY_TASK_ID;
import static sg.gov.moh.iais.egp.bsb.constant.module.InspectionConstants.TAB_ACTIVE;
import static sg.gov.moh.iais.egp.bsb.constant.module.InspectionConstants.TAB_INS_REPORT;
import static sg.gov.moh.iais.egp.bsb.constant.module.InspectionConstants.TAB_PROCESSING;
import static sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants.KEY_AO;
import static sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants.KEY_APPLICANT;
import static sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants.KEY_DO;
import static sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants.KEY_FACILITY_DETAILS_INFO;
import static sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants.KEY_ROUTING_HISTORY_LIST;
import static sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants.KEY_SELECT_ROUTE_TO_MOH;
import static sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants.KEY_SUBMISSION_DETAILS_INFO;
import static sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants.KEY_TAB_DOCUMENT_INTERNAL_DOC_LIST;
import static sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants.KEY_VALIDATION_ERRORS;

/**
 * DO inspection report
 */
@Slf4j
@Delegator("bsbSubmitInspectionReport")
public class BsbSubmitInspectionReportDelegator {
    private final InspectionClient inspectionClient;
    private final InternalDocClient internalDocClient;

    @Autowired
    public BsbSubmitInspectionReportDelegator(InspectionClient inspectionClient, InternalDocClient internalDocClient) {
        this.inspectionClient = inspectionClient;
        this.internalDocClient = internalDocClient;
    }


    public void start(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        MaskHelper.taskProcessUnmask(request, KEY_APP_ID, KEY_TASK_ID);

        AuditTrailHelper.auditFunction(AuditTrailConsts.MODULE_INSPECTION, AuditTrailConsts.FUNCTION_INSPECTION_REPORT);
    }

    public void init(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        HttpSession session = request.getSession();
        session.removeAttribute(KEY_SUBMISSION_DETAILS_INFO);
        session.removeAttribute(KEY_FACILITY_DETAILS_INFO);
        session.removeAttribute(KEY_INS_REPORT);
        session.removeAttribute(KEY_SELECT_ROUTE_TO_MOH);
        session.removeAttribute(KEY_ROUTING_HISTORY_LIST);
        session.removeAttribute(KEY_INS_DECISION);
        session.removeAttribute(KEY_APP_STATUS);
        session.removeAttribute(InspectionConstants.KEY_HAS_NON_COMPLIANCE);

        String appId = (String) ParamUtil.getSessionAttr(request, KEY_APP_ID);

        // judge whether we need to send email
        InsSubmitReportDataDto initDataDto = inspectionClient.getInitInsSubmitReportData(appId);

        // submission details info
        ParamUtil.setSessionAttr(request, KEY_SUBMISSION_DETAILS_INFO, initDataDto.getSubmissionDetailsInfo());
        // facility details
        ParamUtil.setSessionAttr(request, KEY_FACILITY_DETAILS_INFO, initDataDto.getFacilityDetailsInfo());
        // inspection report
        ParamUtil.setSessionAttr(request, KEY_INS_REPORT, initDataDto.getReportDto());
        // show route to moh selection list
        ParamUtil.setSessionAttr(request, KEY_SELECT_ROUTE_TO_MOH, (Serializable) initDataDto.getSelectRouteToMoh());
        // show routingHistory list
        ParamUtil.setSessionAttr(request, KEY_ROUTING_HISTORY_LIST, (Serializable) initDataDto.getProcessHistoryDtoList());

        // inspection processing
        InsReportProcessDto processDto = new InsReportProcessDto();
        ParamUtil.setSessionAttr(request, KEY_INS_DECISION, processDto);

        ParamUtil.setSessionAttr(request, KEY_APP_STATUS, initDataDto.getSubmissionDetailsInfo().getApplicationStatus());

        ParamUtil.setSessionAttr(request, InspectionConstants.KEY_HAS_NON_COMPLIANCE,initDataDto.getHasNonCompliance());
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
        String appId = (String) ParamUtil.getSessionAttr(request, KEY_APP_ID);
        InsReportProcessDto processDto = (InsReportProcessDto) ParamUtil.getSessionAttr(request, KEY_INS_DECISION);
        processDto.reqObjMapping(request);
        ParamUtil.setSessionAttr(request, KEY_INS_DECISION, processDto);
        ValidationResultDto validationResultDto = inspectionClient.validateActualInspectionDOSubmitReportDecision(processDto, appId);
        String validateResult;
        if (validationResultDto.isPass()) {
            if (MasterCodeConstants.MOH_PROCESSING_DECISION_SUBMIT_REPORT_TO_AO_FOR_REVIEW.equals(processDto.getDecision())) {
                validateResult = "ao";
            } else if (MasterCodeConstants.MOH_PROCESSING_DECISION_ROUTE_REPORT_TO_APPLICANT.equals(processDto.getDecision())) {
                validateResult = "applicant";
            } else if (MasterCodeConstants.MOH_PROCESSING_DECISION_MARK_AS_FINAL.equals(processDto.getDecision())) {
                validateResult = "final";
            } else if(MasterCodeConstants.MOH_PROCESSING_DECISION_SKIP_INSPECTION.equals(processDto.getDecision())){
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
        log.info("Officer submit decision [{}] for submit inspection report, route result [{}]", LogUtil.escapeCrlf(processDto.getDecision()), validateResult);
        ParamUtil.setRequestAttr(request, KEY_ROUTE, validateResult);
    }

    public void skip(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        String appId = (String) ParamUtil.getSessionAttr(request, KEY_APP_ID);
        String taskId = (String) ParamUtil.getSessionAttr(request, KEY_TASK_ID);
        InsReportProcessDto processDto = (InsReportProcessDto) ParamUtil.getSessionAttr(request, KEY_INS_DECISION);
        inspectionClient.skipInspection(appId,taskId,processDto);
    }

    public void handleSaveReport(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        String appId = (String) ParamUtil.getSessionAttr(request, KEY_APP_ID);

        InsReportProcessDto processDto = (InsReportProcessDto) ParamUtil.getSessionAttr(request, KEY_INS_DECISION);
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

    public void submitToAo(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        String appId = (String) ParamUtil.getSessionAttr(request, KEY_APP_ID);
        String taskId = (String) ParamUtil.getSessionAttr(request, KEY_TASK_ID);
        InsReportProcessDto processDto = (InsReportProcessDto) ParamUtil.getSessionAttr(request, KEY_INS_DECISION);
        inspectionClient.submitInspectionReportToAO(appId, taskId, processDto);
        String appStatus = (String) ParamUtil.getSessionAttr(request, KEY_APP_STATUS);
        ParamUtil.setRequestAttr(request, TaskModuleConstants.KEY_CURRENT_TASK, MasterCodeUtil.getCodeDesc(appStatus));
        ParamUtil.setRequestAttr(request,TaskModuleConstants.KEY_NEXT_TASK, MasterCodeUtil.getCodeDesc(MasterCodeConstants.APP_STATUS_PEND_AO_REVIEW));
        ParamUtil.setRequestAttr(request,TaskModuleConstants.KEY_NEXT_ROLE, KEY_AO);
    }

    public void routeToApplicant(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        String appId = (String) ParamUtil.getSessionAttr(request, KEY_APP_ID);
        String taskId = (String) ParamUtil.getSessionAttr(request, KEY_TASK_ID);
        InsReportProcessDto processDto = (InsReportProcessDto) ParamUtil.getSessionAttr(request, KEY_INS_DECISION);
        inspectionClient.routeInspectionReportToApplicant(appId, taskId, processDto);
        String appStatus = (String) ParamUtil.getSessionAttr(request, KEY_APP_STATUS);
        ParamUtil.setRequestAttr(request, TaskModuleConstants.KEY_CURRENT_TASK, MasterCodeUtil.getCodeDesc(appStatus));
        ParamUtil.setRequestAttr(request,TaskModuleConstants.KEY_NEXT_TASK, MasterCodeUtil.getCodeDesc(MasterCodeConstants.APP_STATUS_PEND_INPUT));
        ParamUtil.setRequestAttr(request,TaskModuleConstants.KEY_NEXT_ROLE, KEY_APPLICANT);
    }

    public void markFinal(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        String appId = (String) ParamUtil.getSessionAttr(request, KEY_APP_ID);
        String taskId = (String) ParamUtil.getSessionAttr(request, KEY_TASK_ID);
        InsReportProcessDto processDto = (InsReportProcessDto) ParamUtil.getSessionAttr(request, KEY_INS_DECISION);
        log.info("AppId {} taskId {} finalize inspection report", appId, taskId);
        String nextStatus = inspectionClient.finalizeInspectionReport(appId, taskId, processDto);
        String appStatus = (String) ParamUtil.getSessionAttr(request, KEY_APP_STATUS);
        ParamUtil.setRequestAttr(request, TaskModuleConstants.KEY_CURRENT_TASK, MasterCodeUtil.getCodeDesc(appStatus));
        ParamUtil.setRequestAttr(request,TaskModuleConstants.KEY_NEXT_TASK, MasterCodeUtil.getCodeDesc(nextStatus));
        if (nextStatus.equals(MasterCodeConstants.APP_STATUS_PEND_NC_RECTIFICATION)) {
            ParamUtil.setRequestAttr(request, TaskModuleConstants.KEY_NEXT_ROLE, KEY_APPLICANT);
        } else if (nextStatus.equals(MasterCodeConstants.APP_STATUS_PEND_DO) || nextStatus.equals(MasterCodeConstants.APP_STATUS_PEND_DO_NC_EMAIL_DRAFT)) {
            ParamUtil.setRequestAttr(request, TaskModuleConstants.KEY_NEXT_ROLE, KEY_DO);
        }
    }
}

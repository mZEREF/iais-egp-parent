package sg.gov.moh.iais.egp.bsb.action;


import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.utils.LogUtil;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import lombok.extern.slf4j.Slf4j;
import sg.gov.moh.iais.egp.bsb.client.InspectionClient;
import sg.gov.moh.iais.egp.bsb.client.InternalDocClient;
import sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants;
import sg.gov.moh.iais.egp.bsb.dto.ProcessHistoryDto;
import sg.gov.moh.iais.egp.bsb.dto.file.DocDisplayDto;
import sg.gov.moh.iais.egp.bsb.dto.inspection.InsApprovalLetterDto;
import sg.gov.moh.iais.egp.bsb.dto.inspection.InsApprovalLetterInitDataDto;
import sg.gov.moh.iais.egp.bsb.dto.inspection.InsProcessDto;
import sg.gov.moh.iais.egp.bsb.dto.mohprocessingdisplay.FacilityDetailsInfo;
import sg.gov.moh.iais.egp.bsb.dto.mohprocessingdisplay.SubmissionDetailsInfo;
import sg.gov.moh.iais.egp.bsb.dto.validation.ValidationResultDto;
import sg.gov.moh.iais.egp.bsb.util.MaskHelper;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import java.util.ArrayList;
import java.util.List;

import static sg.gov.moh.iais.egp.bsb.constant.module.InspectionConstants.*;
import static sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants.*;


@Slf4j
@Delegator("mohOfficerInsApprovalLetterDelegator")
public class MohOfficerInsApprovalLetterDelegator {
    private final InspectionClient inspectionClient;
    private final InternalDocClient internalDocClient;

    public MohOfficerInsApprovalLetterDelegator(InspectionClient inspectionClient, InternalDocClient internalDocClient) {
        this.inspectionClient = inspectionClient;
        this.internalDocClient = internalDocClient;
    }

    public void startDO(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        MaskHelper.taskProcessUnmask(request, KEY_APP_ID, KEY_TASK_ID);
        AuditTrailHelper.auditFunction("Pending DO Approval Letter Draft", "Pending DO Approval Letter Draft");
    }

    public void startAO(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        MaskHelper.taskProcessUnmask(request, KEY_APP_ID, KEY_TASK_ID);
        AuditTrailHelper.auditFunction("Pending AO Approval Letter Review", "Pending AO Approval Letter Review");
    }

    public void init(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        HttpSession session = request.getSession();
        session.removeAttribute(KEY_SUBMISSION_DETAILS_INFO);
        session.removeAttribute(KEY_FACILITY_DETAILS_INFO);
        session.removeAttribute(KEY_ROUTING_HISTORY_LIST);
        session.removeAttribute(KEY_SELECT_ROUTE_TO_MOH);
        session.removeAttribute(KEY_INS_DTO_INS_LETTER);
        String appId = (String) ParamUtil.getSessionAttr(request,KEY_APP_ID);
        InsApprovalLetterInitDataDto initDataDto = inspectionClient.getInitInsApprovalLetterData(appId);

        // submission info
        SubmissionDetailsInfo submissionDetailsInfo = initDataDto.getSubmissionDetailsInfo();
        ParamUtil.setSessionAttr(request, KEY_SUBMISSION_DETAILS_INFO, submissionDetailsInfo);

        // facility details
        FacilityDetailsInfo facilityDetailsInfo = initDataDto.getFacilityDetailsInfo();
        ParamUtil.setSessionAttr(request, KEY_FACILITY_DETAILS_INFO, facilityDetailsInfo);

        //support doc list
        ArrayList<DocDisplayDto> supportDocList = new ArrayList<>(initDataDto.getSupportDocDisplayDtoList());
        ParamUtil.setSessionAttr(request,KEY_TAB_DOCUMENT_SUPPORT_DOC_LIST,supportDocList);

        //routing history
        ArrayList<ProcessHistoryDto> processHistoryDtoList = new ArrayList<>(initDataDto.getProcessHistoryDtoList());
        ParamUtil.setSessionAttr(request, KEY_ROUTING_HISTORY_LIST, processHistoryDtoList);

        //moh officer selection
        ArrayList<SelectOption> selectRouteToMoh = new ArrayList<>(initDataDto.getSelectRouteToMoh());
        ParamUtil.setSessionAttr(request,KEY_SELECT_ROUTE_TO_MOH,selectRouteToMoh);


        InsApprovalLetterDto letterDto = initDataDto.getInsApprovalLetterDto();
        ParamUtil.setSessionAttr(request,KEY_INS_DTO_INS_LETTER,letterDto);
    }

    public void pre(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        String appId = (String) ParamUtil.getSessionAttr(request, KEY_APP_ID);
        List<DocDisplayDto> internalDocDisplayDto = internalDocClient.getInternalDocForDisplay(appId);
        ParamUtil.setRequestAttr(request, KEY_TAB_DOCUMENT_INTERNAL_DOC_LIST, internalDocDisplayDto);
    }

    public void prePreviewApprovalLetter(BaseProcessClass bpc){
        //do noting now
        HttpServletRequest request = bpc.request;
        InsApprovalLetterDto letterDto = (InsApprovalLetterDto) ParamUtil.getSessionAttr(request,KEY_INS_DTO_INS_LETTER);
        letterDto.reqObjMapping(request);
        ParamUtil.setSessionAttr(request,KEY_INS_DTO_INS_LETTER,letterDto);
    }

    public void bindAction(BaseProcessClass bpc) {
        // do nothing now
    }

    public void handleSubmit(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        InsApprovalLetterDto letterDto = (InsApprovalLetterDto) ParamUtil.getSessionAttr(request,KEY_INS_DTO_INS_LETTER);
        letterDto.reqObjMapping(request);
        ParamUtil.setSessionAttr(request,KEY_INS_DTO_INS_LETTER,letterDto);

        ValidationResultDto validationResultDto = inspectionClient.validateInsApprovalLetter(letterDto);
        String validateResult;
        if (validationResultDto.isPass()) {
            if (MasterCodeConstants.MOH_PROCESSING_DECISION_ROUTE_TO_AO.equals(letterDto.getDecision())) {
                validateResult = "ao";
            }else if(MasterCodeConstants.MOH_PROCESSING_DECISION_APPROVE.equals(letterDto.getDecision())){
                validateResult = "approve";
            }else if(MasterCodeConstants.MOH_PROCESSING_DECISION_ROUTE_BACK_TO_DO.equals(letterDto.getDecision())){
                validateResult = "do";
            }else if(MasterCodeConstants.MOH_PROCESSING_DECISION_SKIP_INSPECTION.equals(letterDto.getDecision())){
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
        log.info("Officer submit decision [{}] for review inspection NCs Rectification report, route result [{}]", LogUtil.escapeCrlf(letterDto.getDecision()), validateResult);
        ParamUtil.setRequestAttr(request, KEY_ROUTE, validateResult);
    }

    public void skip(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        String appId = (String) ParamUtil.getSessionAttr(request, KEY_APP_ID);
        String taskId = (String) ParamUtil.getSessionAttr(request, KEY_TASK_ID);
        InsProcessDto processDto = (InsProcessDto) ParamUtil.getSessionAttr(request, KEY_INS_DECISION);
        inspectionClient.skipInspection(appId,taskId,processDto);
    }

    public void routeToAO(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        String appId = (String) ParamUtil.getSessionAttr(request, KEY_APP_ID);
        String taskId = (String) ParamUtil.getSessionAttr(request, KEY_TASK_ID);
        InsApprovalLetterDto letterDto = (InsApprovalLetterDto) ParamUtil.getSessionAttr(request,KEY_INS_DTO_INS_LETTER);
        inspectionClient.inspectionApprovalLetterDOSubmitToAO(appId,taskId,letterDto);
        ParamUtil.setRequestAttr(request, KEY_RESULT_MSG, "You have successfully submitted task to approval Officer");
    }

    public void approve(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        String appId = (String) ParamUtil.getSessionAttr(request, KEY_APP_ID);
        String taskId = (String) ParamUtil.getSessionAttr(request, KEY_TASK_ID);
        InsApprovalLetterDto letterDto = (InsApprovalLetterDto) ParamUtil.getSessionAttr(request,KEY_INS_DTO_INS_LETTER);
        inspectionClient.inspectionApprovalLetterAOApprove(appId,taskId,letterDto);
        ParamUtil.setRequestAttr(request, KEY_RESULT_MSG, "You have successfully finalize report");

    }

    public void routeToDO(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        String appId = (String) ParamUtil.getSessionAttr(request, KEY_APP_ID);
        String taskId = (String) ParamUtil.getSessionAttr(request, KEY_TASK_ID);
        InsApprovalLetterDto letterDto = (InsApprovalLetterDto) ParamUtil.getSessionAttr(request,KEY_INS_DTO_INS_LETTER);
        inspectionClient.inspectionApprovalLetterAORouteBackToDO(appId,taskId,letterDto);
        ParamUtil.setRequestAttr(request, KEY_RESULT_MSG, "You have successfully route report back to Duty Officer");
    }

}

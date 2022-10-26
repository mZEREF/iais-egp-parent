package sg.gov.moh.iais.egp.bsb.action;


import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.utils.LogUtil;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import sg.gov.moh.iais.egp.bsb.client.InspectionClient;
import sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants;
import sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants;
import sg.gov.moh.iais.egp.bsb.dto.ProcessHistoryDto;
import sg.gov.moh.iais.egp.bsb.dto.process.ApprovalLetterDto;
import sg.gov.moh.iais.egp.bsb.dto.process.ApprovalLetterInitDataDto;
import sg.gov.moh.iais.egp.bsb.dto.mohprocessingdisplay.FacilityDetailsInfo;
import sg.gov.moh.iais.egp.bsb.dto.mohprocessingdisplay.SubmissionDetailsInfo;
import sg.gov.moh.iais.egp.bsb.dto.validation.ValidationResultDto;
import sg.gov.moh.iais.egp.bsb.service.DocSettingService;
import sg.gov.moh.iais.egp.bsb.service.DualDocSortingService;
import sg.gov.moh.iais.egp.bsb.util.MaskHelper;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;

import static com.ecquaria.cloud.moh.iais.common.constant.BsbAuditTrailConstants.FUNCTION_AO_APPROVAL_LETTER_REVIEW;
import static com.ecquaria.cloud.moh.iais.common.constant.BsbAuditTrailConstants.FUNCTION_DO_APPROVAL_LETTER_DRAFT;
import static com.ecquaria.cloud.moh.iais.common.constant.BsbAuditTrailConstants.MODULE_APPROVAL_LETTER;
import static sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants.APP_STATUS_PEND_AO_APPROVAL_LETTER_REVIEW;
import static sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants.APP_STATUS_PEND_DO_APPROVAL_LETTER_DRAFT;
import static sg.gov.moh.iais.egp.bsb.constant.module.InspectionConstants.KEY_APP_ID;
import static sg.gov.moh.iais.egp.bsb.constant.module.InspectionConstants.KEY_INS_DTO_INS_LETTER;
import static sg.gov.moh.iais.egp.bsb.constant.module.InspectionConstants.KEY_ROUTE;
import static sg.gov.moh.iais.egp.bsb.constant.module.InspectionConstants.KEY_TASK_ID;
import static sg.gov.moh.iais.egp.bsb.constant.module.InspectionConstants.TAB_ACTIVE;
import static sg.gov.moh.iais.egp.bsb.constant.module.InspectionConstants.TAB_PROCESSING;
import static sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants.INTERNAL_DOCUMENT_TYPE_LIST;
import static sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants.KEY_FACILITY_DETAILS_INFO;
import static sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants.KEY_ROUTING_HISTORY_LIST;
import static sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants.KEY_SELECT_ROUTE_TO_MOH;
import static sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants.KEY_SUBMISSION_DETAILS_INFO;
import static sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants.KEY_VALIDATION_ERRORS;


@Slf4j
@RequiredArgsConstructor
@Delegator("mohOfficerInsApprovalLetterDelegator")
public class MohOfficerInsApprovalLetterDelegator {
    private final InspectionClient inspectionClient;
    private final DocSettingService docSettingService;
    private final DualDocSortingService dualDocSortingService;


    public void startDO(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        MaskHelper.taskProcessUnmask(request, KEY_APP_ID, KEY_TASK_ID);
        AuditTrailHelper.auditFunction(MODULE_APPROVAL_LETTER, FUNCTION_DO_APPROVAL_LETTER_DRAFT);
    }

    public void startAO(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        MaskHelper.taskProcessUnmask(request, KEY_APP_ID, KEY_TASK_ID);
        AuditTrailHelper.auditFunction(MODULE_APPROVAL_LETTER, FUNCTION_AO_APPROVAL_LETTER_REVIEW);
    }

    public void init(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        HttpSession session = request.getSession();
        DualDocSortingService.clearDualDocSortingInfo(session);
        session.removeAttribute(KEY_SUBMISSION_DETAILS_INFO);
        session.removeAttribute(KEY_FACILITY_DETAILS_INFO);
        session.removeAttribute(KEY_ROUTING_HISTORY_LIST);
        session.removeAttribute(KEY_SELECT_ROUTE_TO_MOH);
        session.removeAttribute(KEY_INS_DTO_INS_LETTER);
        String appId = (String) ParamUtil.getSessionAttr(request,KEY_APP_ID);
        ApprovalLetterInitDataDto initDataDto = inspectionClient.getInitInsApprovalLetterData(appId);

        // submission info
        SubmissionDetailsInfo submissionDetailsInfo = initDataDto.getSubmissionDetailsInfo();
        ParamUtil.setSessionAttr(request, KEY_SUBMISSION_DETAILS_INFO, submissionDetailsInfo);

        // facility details
        FacilityDetailsInfo facilityDetailsInfo = initDataDto.getFacilityDetailsInfo();
        ParamUtil.setSessionAttr(request, KEY_FACILITY_DETAILS_INFO, facilityDetailsInfo);

        //routing history
        ArrayList<ProcessHistoryDto> processHistoryDtoList = new ArrayList<>(initDataDto.getProcessHistoryDtoList());
        ParamUtil.setSessionAttr(request, KEY_ROUTING_HISTORY_LIST, processHistoryDtoList);

        //moh officer selection
        ArrayList<SelectOption> selectRouteToMoh = new ArrayList<>(initDataDto.getSelectRouteToMoh());
        ParamUtil.setSessionAttr(request,KEY_SELECT_ROUTE_TO_MOH,selectRouteToMoh);

        ApprovalLetterDto letterDto = initDataDto.getInsApprovalLetterDto();
        ParamUtil.setSessionAttr(request,KEY_INS_DTO_INS_LETTER,letterDto);
    }

    public void pre(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        String appId = (String) ParamUtil.getSessionAttr(request, KEY_APP_ID);
        dualDocSortingService.retrieveDualDocDisplayDtoIntoRequest(request, appId);
        //set internal doc types
        ParamUtil.setRequestAttr(request, INTERNAL_DOCUMENT_TYPE_LIST, docSettingService.getInternalDocTypeList());
    }

    public void prePreviewApprovalLetter(BaseProcessClass bpc){
        //do noting now
        HttpServletRequest request = bpc.request;
        ApprovalLetterDto letterDto = (ApprovalLetterDto) ParamUtil.getSessionAttr(request,KEY_INS_DTO_INS_LETTER);
        letterDto.reqObjMapping(request);
        ParamUtil.setSessionAttr(request,KEY_INS_DTO_INS_LETTER,letterDto);
    }

    public void bindAction(BaseProcessClass bpc) {
        DualDocSortingService.readDualDocSortingInfo(bpc.request);
    }

    public void handleSubmit(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        ApprovalLetterDto letterDto = (ApprovalLetterDto) ParamUtil.getSessionAttr(request,KEY_INS_DTO_INS_LETTER);
        letterDto.reqObjMapping(request);
        ParamUtil.setSessionAttr(request,KEY_INS_DTO_INS_LETTER,letterDto);

        SubmissionDetailsInfo submissionDetailsInfo = (SubmissionDetailsInfo) ParamUtil.getSessionAttr(request, KEY_SUBMISSION_DETAILS_INFO);
        String appStatus = submissionDetailsInfo.getApplicationStatus();
        ValidationResultDto validationResultDto ;
        if (APP_STATUS_PEND_DO_APPROVAL_LETTER_DRAFT.equals(appStatus)) {
            validationResultDto = inspectionClient.validateDODraftApprovalLetter(letterDto);
        } else if (APP_STATUS_PEND_AO_APPROVAL_LETTER_REVIEW.equals(appStatus)) {
            validationResultDto = inspectionClient.validateAOReviewApprovalLetter(letterDto);
        } else {
            throw new IllegalStateException();
        }
        String validateResult;
        if (validationResultDto.isPass()) {
            if (MasterCodeConstants.MOH_PROCESS_DECISION_ROUTE_TO_AO_FOR_APPROVAL.equals(letterDto.getDecision())) {
                validateResult = "ao";
            }else if(MasterCodeConstants.MOH_PROCESS_DECISION_APPROVE.equals(letterDto.getDecision())){
                validateResult = "approve";
            }else if(MasterCodeConstants.MOH_PROCESS_DECISION_ROUTE_BACK_TO_DO.equals(letterDto.getDecision())){
                validateResult = "do";
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

    public void routeToAO(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        String appId = (String) ParamUtil.getSessionAttr(request, KEY_APP_ID);
        String taskId = (String) ParamUtil.getSessionAttr(request, KEY_TASK_ID);
        ApprovalLetterDto letterDto = (ApprovalLetterDto) ParamUtil.getSessionAttr(request,KEY_INS_DTO_INS_LETTER);
        inspectionClient.inspectionApprovalLetterDOSubmitToAO(appId,taskId,letterDto);
        ParamUtil.setRequestAttr(request, ModuleCommonConstants.RESULT_MSG, "BISACKNEW009");
    }

    public void approve(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        String appId = (String) ParamUtil.getSessionAttr(request, KEY_APP_ID);
        String taskId = (String) ParamUtil.getSessionAttr(request, KEY_TASK_ID);
        ApprovalLetterDto letterDto = (ApprovalLetterDto) ParamUtil.getSessionAttr(request,KEY_INS_DTO_INS_LETTER);
        inspectionClient.inspectionApprovalLetterAOApprove(appId,taskId,letterDto);
        ParamUtil.setRequestAttr(request, ModuleCommonConstants.RESULT_MSG, "BISACKNEW006");
    }

    public void routeToDO(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        String appId = (String) ParamUtil.getSessionAttr(request, KEY_APP_ID);
        String taskId = (String) ParamUtil.getSessionAttr(request, KEY_TASK_ID);
        ApprovalLetterDto letterDto = (ApprovalLetterDto) ParamUtil.getSessionAttr(request,KEY_INS_DTO_INS_LETTER);
        inspectionClient.inspectionApprovalLetterAORouteBackToDO(appId,taskId,letterDto);
        ParamUtil.setRequestAttr(request, ModuleCommonConstants.RESULT_MSG, "BISACKNEW007");
    }

}

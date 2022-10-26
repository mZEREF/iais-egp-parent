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
import sg.gov.moh.iais.egp.bsb.constant.StageConstants;
import sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants;
import sg.gov.moh.iais.egp.bsb.dto.ProcessHistoryDto;
import sg.gov.moh.iais.egp.bsb.dto.inspection.InsNCEmailDto;
import sg.gov.moh.iais.egp.bsb.dto.inspection.InsNCEmailInitDataDto;
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

import static com.ecquaria.cloud.moh.iais.common.constant.BsbAuditTrailConstants.FUNCTION_AO_NC_EMAIL;
import static com.ecquaria.cloud.moh.iais.common.constant.BsbAuditTrailConstants.FUNCTION_DO_NC_EMAIL_DRAFT;
import static com.ecquaria.cloud.moh.iais.common.constant.BsbAuditTrailConstants.MODULE_NC_EMAIL;
import static sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants.APP_STATUS_PEND_NC_NOTIFICATION_EMAIL;
import static sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants.APP_STATUS_PEND_NC_NOTIFICATION_EMAIL_REVIEW;
import static sg.gov.moh.iais.egp.bsb.constant.module.InspectionConstants.KEY_APP_ID;
import static sg.gov.moh.iais.egp.bsb.constant.module.InspectionConstants.KEY_INS_NC_EMAIL_DTO;
import static sg.gov.moh.iais.egp.bsb.constant.module.InspectionConstants.KEY_ROUTE;
import static sg.gov.moh.iais.egp.bsb.constant.module.InspectionConstants.KEY_TASK_ID;
import static sg.gov.moh.iais.egp.bsb.constant.module.InspectionConstants.TAB_ACTIVE;
import static sg.gov.moh.iais.egp.bsb.constant.module.InspectionConstants.TAB_PROCESSING;
import static sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants.INTERNAL_DOCUMENT_TYPE_LIST;
import static sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants.KEY_FACILITY_DETAILS_INFO;
import static sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants.KEY_ROUTING_HISTORY_LIST;
import static sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants.KEY_SELECT_ROUTE_TO_MOH;
import static sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants.KEY_SUBMISSION_DETAILS_INFO;
import static sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants.KEY_TAB_DOCUMENT_SUPPORT_DOC_LIST;
import static sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants.KEY_VALIDATION_ERRORS;


@Slf4j
@RequiredArgsConstructor
@Delegator("mohOfficerNCEmailDelegator")
public class MohOfficerNCEmailDelegator {
    private static final String BIS_ACK_INS003 = "BISACKINS003";

    private final InspectionClient inspectionClient;
    private final DocSettingService docSettingService;
    private final DualDocSortingService dualDocSortingService;

    public void startDO(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        MaskHelper.taskProcessUnmask(request, KEY_APP_ID, KEY_TASK_ID);
        AuditTrailHelper.auditFunction(MODULE_NC_EMAIL, FUNCTION_DO_NC_EMAIL_DRAFT);
    }

    public void startAO(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        MaskHelper.taskProcessUnmask(request, KEY_APP_ID, KEY_TASK_ID);
        AuditTrailHelper.auditFunction(MODULE_NC_EMAIL, FUNCTION_AO_NC_EMAIL);
    }

    public void init(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        HttpSession session = request.getSession();
        DualDocSortingService.clearDualDocSortingInfo(session);
        session.removeAttribute(KEY_SUBMISSION_DETAILS_INFO);
        session.removeAttribute(KEY_FACILITY_DETAILS_INFO);
        session.removeAttribute(KEY_ROUTING_HISTORY_LIST);
        session.removeAttribute(KEY_SELECT_ROUTE_TO_MOH);
        session.removeAttribute(KEY_INS_NC_EMAIL_DTO);
        session.removeAttribute(KEY_TAB_DOCUMENT_SUPPORT_DOC_LIST);
        String appId = (String) ParamUtil.getSessionAttr(request,KEY_APP_ID);
        InsNCEmailInitDataDto initDataDto = inspectionClient.getInitInsNCEmailData(appId);

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


        InsNCEmailDto ncEmailDto = initDataDto.getInsNCEmailDto();
        ParamUtil.setSessionAttr(request, KEY_INS_NC_EMAIL_DTO,ncEmailDto);
    }

    public void pre(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        String appId = (String) ParamUtil.getSessionAttr(request, KEY_APP_ID);
        dualDocSortingService.retrieveDualDocDisplayDtoIntoRequest(request, appId);
        //set internal doc types
        ParamUtil.setRequestAttr(request, INTERNAL_DOCUMENT_TYPE_LIST, docSettingService.getInternalDocTypeList());
    }

    public void prePreviewNCEmail(BaseProcessClass bpc){
        //do noting now
        HttpServletRequest request = bpc.request;
        InsNCEmailDto ncEmailDto = (InsNCEmailDto) ParamUtil.getSessionAttr(request,KEY_INS_NC_EMAIL_DTO);
        ncEmailDto.reqObjMapping(request);
        ParamUtil.setSessionAttr(request,KEY_INS_NC_EMAIL_DTO,ncEmailDto);
    }

    public void bindAction(BaseProcessClass bpc) {
        DualDocSortingService.readDualDocSortingInfo(bpc.request);
    }

    public void handleSubmit(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        InsNCEmailDto ncEmailDto = (InsNCEmailDto) ParamUtil.getSessionAttr(request,KEY_INS_NC_EMAIL_DTO);
        ncEmailDto.reqObjMapping(request);
        ParamUtil.setSessionAttr(request,KEY_INS_NC_EMAIL_DTO,ncEmailDto);

        SubmissionDetailsInfo submissionDetailsInfo = (SubmissionDetailsInfo) ParamUtil.getSessionAttr(request, KEY_SUBMISSION_DETAILS_INFO);
        String appStatus = submissionDetailsInfo.getApplicationStatus();
        ValidationResultDto validationResultDto;
        if (APP_STATUS_PEND_NC_NOTIFICATION_EMAIL.equals(appStatus)) {
            validationResultDto = inspectionClient.validateInsDONCEmailDto(ncEmailDto);
        } else if (APP_STATUS_PEND_NC_NOTIFICATION_EMAIL_REVIEW.equals(appStatus)) {
            validationResultDto = inspectionClient.validateInsAONCEmailDto(ncEmailDto);
        } else {
            throw new IllegalStateException();
        }
        String validateResult;
        if (validationResultDto.isPass()) {
            if (MasterCodeConstants.MOH_PROCESS_DECISION_ROUTE_TO_AO_FOR_REVIEW.equals(ncEmailDto.getDecision())) {
                validateResult = "ao";
            }else if(MasterCodeConstants.MOH_PROCESS_DECISION_ROUTE_TO_APPLICANT.equals(ncEmailDto.getDecision()) ||
                    MasterCodeConstants.MOH_PROCESS_DECISION_APPROVE_AND_ROUTE_TO_APPLICANT.equals(ncEmailDto.getDecision())){
                validateResult = "applicant";
            }else if(MasterCodeConstants.MOH_PROCESS_DECISION_ROUTE_BACK_TO_DO.equals(ncEmailDto.getDecision())){
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
        log.info("Officer submit decision [{}] for review inspection NCs Rectification report, route result [{}]", LogUtil.escapeCrlf(ncEmailDto.getDecision()), validateResult);
        ParamUtil.setRequestAttr(request, KEY_ROUTE, validateResult);
    }

    public void routeToAO(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        String appId = (String) ParamUtil.getSessionAttr(request, KEY_APP_ID);
        String taskId = (String) ParamUtil.getSessionAttr(request, KEY_TASK_ID);
        InsNCEmailDto ncEmailDto = (InsNCEmailDto) ParamUtil.getSessionAttr(request,KEY_INS_NC_EMAIL_DTO);
        inspectionClient.inspectionNCEmailDORouteToAO(appId,taskId,ncEmailDto);
        ParamUtil.setRequestAttr(request, ModuleCommonConstants.RESULT_MSG, BIS_ACK_INS003);
    }

    public void doRouteToApplicant(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        String appId = (String) ParamUtil.getSessionAttr(request, KEY_APP_ID);
        String taskId = (String) ParamUtil.getSessionAttr(request, KEY_TASK_ID);
        InsNCEmailDto ncEmailDto = (InsNCEmailDto) ParamUtil.getSessionAttr(request,KEY_INS_NC_EMAIL_DTO);
        inspectionClient.inspectionNCEmailRouteToApplicant(appId,taskId, StageConstants.ROLE_DO,ncEmailDto);
        ParamUtil.setRequestAttr(request, ModuleCommonConstants.RESULT_MSG, BIS_ACK_INS003);
    }

    public void aoRouteToApplicant(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        String appId = (String) ParamUtil.getSessionAttr(request, KEY_APP_ID);
        String taskId = (String) ParamUtil.getSessionAttr(request, KEY_TASK_ID);
        InsNCEmailDto ncEmailDto = (InsNCEmailDto) ParamUtil.getSessionAttr(request,KEY_INS_NC_EMAIL_DTO);
        inspectionClient.inspectionNCEmailRouteToApplicant(appId,taskId, StageConstants.ROLE_AO,ncEmailDto);
        ParamUtil.setRequestAttr(request, ModuleCommonConstants.RESULT_MSG, BIS_ACK_INS003);
    }

    public void routeToDO(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        String appId = (String) ParamUtil.getSessionAttr(request, KEY_APP_ID);
        String taskId = (String) ParamUtil.getSessionAttr(request, KEY_TASK_ID);
        InsNCEmailDto ncEmailDto = (InsNCEmailDto) ParamUtil.getSessionAttr(request,KEY_INS_NC_EMAIL_DTO);
        inspectionClient.inspectionNCEmailAORouteBackToDO(appId,taskId,ncEmailDto);
        ParamUtil.setRequestAttr(request, ModuleCommonConstants.RESULT_MSG, BIS_ACK_INS003);
    }

}

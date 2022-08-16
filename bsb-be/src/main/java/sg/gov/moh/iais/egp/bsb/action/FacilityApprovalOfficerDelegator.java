package sg.gov.moh.iais.egp.bsb.action;

import com.ecquaria.cloud.annotation.Delegator;

import com.ecquaria.cloud.moh.iais.common.utils.LogUtil;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import sg.gov.moh.iais.egp.bsb.client.FacilityApprovalClient;
import sg.gov.moh.iais.egp.bsb.client.InternalDocClient;
import sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants;
import sg.gov.moh.iais.egp.bsb.constant.StageConstants;
import sg.gov.moh.iais.egp.bsb.constant.TaskType;
import sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants;
import sg.gov.moh.iais.egp.bsb.constant.module.TaskModuleConstants;
import sg.gov.moh.iais.egp.bsb.dto.ProcessHistoryDto;
import sg.gov.moh.iais.egp.bsb.dto.facilityapproval.FacApprovalInitDataDto;
import sg.gov.moh.iais.egp.bsb.dto.facilityapproval.FacApprovalProcessDto;
import sg.gov.moh.iais.egp.bsb.dto.file.DocDisplayDto;
import sg.gov.moh.iais.egp.bsb.dto.mohprocessingdisplay.FacilityBiologicalAgentInfo;
import sg.gov.moh.iais.egp.bsb.dto.mohprocessingdisplay.FacilityDetailsInfo;
import sg.gov.moh.iais.egp.bsb.dto.mohprocessingdisplay.SubmissionDetailsInfo;
import sg.gov.moh.iais.egp.bsb.dto.rfi.RfiProcessDto;
import sg.gov.moh.iais.egp.bsb.dto.validation.ValidationResultDto;
import sg.gov.moh.iais.egp.bsb.service.AppViewService;
import sg.gov.moh.iais.egp.bsb.service.RfiService;
import sg.gov.moh.iais.egp.bsb.util.CollectionUtils;
import sg.gov.moh.iais.egp.bsb.util.DocDisplayDtoUtil;
import sg.gov.moh.iais.egp.bsb.util.MaskHelper;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.ecquaria.cloud.moh.iais.common.constant.BsbAuditTrailConstants.FUNCTION_AO_HANDLE_PROCESSING;
import static com.ecquaria.cloud.moh.iais.common.constant.BsbAuditTrailConstants.FUNCTION_DO_RECOMMEND_PROCESSING;
import static com.ecquaria.cloud.moh.iais.common.constant.BsbAuditTrailConstants.FUNCTION_HM_HANDLE_PROCESSING;
import static com.ecquaria.cloud.moh.iais.common.constant.BsbAuditTrailConstants.MODULE_APPROVAL_APPLICATION;
import static sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants.KEY_DOC_DISPLAY_DTO_REPO_ID_NAME_MAP;
import static sg.gov.moh.iais.egp.bsb.constant.module.RfiConstants.KEY_RFI_PROCESS_DTO;


@Slf4j
@Delegator("facilityApprovalOfficerDelegator")
public class FacilityApprovalOfficerDelegator {
    private static final String KEY_FACILITY_APPROVAL_PROCESS_DTO = "processDto";
    private static final String TAB_ACTIVE = "activeTab";
    private static final String TAB_PROCESSING = "tabProcessing";
    private static final String KEY_ROUTE = "route";
    public static final String KEY_RESULT_MSG = "resultMsg";
    public static final String MSG_COMPLETE_TASK = "You have successfully completed your task";

    private final FacilityApprovalClient facApprovalClient;
    private final InternalDocClient internalDocClient;
    private final AppViewService appViewService;
    private final RfiService rfiService;

    @Autowired
    public FacilityApprovalOfficerDelegator(FacilityApprovalClient facApprovalClient, InternalDocClient internalDocClient, AppViewService appViewService, RfiService rfiService) {
        this.facApprovalClient = facApprovalClient;
        this.internalDocClient = internalDocClient;
        this.appViewService = appViewService;
        this.rfiService = rfiService;
    }

    public void startDO(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        MaskHelper.taskProcessUnmask(request, TaskModuleConstants.PARAM_NAME_APP_ID, TaskModuleConstants.PARAM_NAME_TASK_ID);
        AuditTrailHelper.auditFunction(MODULE_APPROVAL_APPLICATION, FUNCTION_DO_RECOMMEND_PROCESSING);
    }

    public void startAO(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        MaskHelper.taskProcessUnmask(request, TaskModuleConstants.PARAM_NAME_APP_ID, TaskModuleConstants.PARAM_NAME_TASK_ID);
        AuditTrailHelper.auditFunction(MODULE_APPROVAL_APPLICATION, FUNCTION_AO_HANDLE_PROCESSING);
    }

    public void startHM(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        MaskHelper.taskProcessUnmask(request, TaskModuleConstants.PARAM_NAME_APP_ID, TaskModuleConstants.PARAM_NAME_TASK_ID);
        AuditTrailHelper.auditFunction(MODULE_APPROVAL_APPLICATION, FUNCTION_HM_HANDLE_PROCESSING);
    }

    public void init(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        HttpSession session = request.getSession();
        session.removeAttribute(ModuleCommonConstants.KEY_SUBMISSION_DETAILS_INFO);
        session.removeAttribute(ModuleCommonConstants.KEY_FACILITY_DETAILS_INFO);
        session.removeAttribute(ModuleCommonConstants.KEY_ROUTING_HISTORY_LIST);
        session.removeAttribute(ModuleCommonConstants.KEY_TAB_DOCUMENT_SUPPORT_DOC_LIST);
        session.removeAttribute(KEY_FACILITY_APPROVAL_PROCESS_DTO);
        session.removeAttribute(ModuleCommonConstants.KEY_BAT_INFO_MAP);
        session.removeAttribute(KEY_DOC_DISPLAY_DTO_REPO_ID_NAME_MAP);
        session.removeAttribute("aoSelectionOps");
        session.removeAttribute("hmSelectionOps");
        session.removeAttribute(KEY_RFI_PROCESS_DTO);

        String appId = (String) ParamUtil.getSessionAttr(request, TaskModuleConstants.PARAM_NAME_APP_ID);
        FacApprovalInitDataDto initDataDto = facApprovalClient.getInitFacilityApprovalData(appId).getEntity();

        // submission info
        SubmissionDetailsInfo submissionDetailsInfo = initDataDto.getSubmissionDetailsInfo();
        ParamUtil.setSessionAttr(request, ModuleCommonConstants.KEY_SUBMISSION_DETAILS_INFO, submissionDetailsInfo);

        // facility details
        FacilityDetailsInfo facilityDetailsInfo = initDataDto.getFacilityDetailsInfo();
        ParamUtil.setSessionAttr(request, ModuleCommonConstants.KEY_FACILITY_DETAILS_INFO, facilityDetailsInfo);

        // show BAT info
        List<FacilityBiologicalAgentInfo> batInfoList = Optional.of(initDataDto)
                .map(FacApprovalInitDataDto::getFacilityDetailsInfo)
                .map(FacilityDetailsInfo::getFacilityBiologicalAgentInfoList)
                .orElse(null);
        if (batInfoList != null && !batInfoList.isEmpty()) {
            Map<String, List<FacilityBiologicalAgentInfo>> batMap = CollectionUtils.groupCollectionToMap(batInfoList, FacilityBiologicalAgentInfo::getApproveType);
            ParamUtil.setRequestAttr(request, ModuleCommonConstants.KEY_BAT_INFO_MAP, batMap);
        }

        List<DocDisplayDto> docDisplayDtoList = initDataDto.getSupportDocDisplayDtoList();
        ParamUtil.setSessionAttr(request, ModuleCommonConstants.KEY_TAB_DOCUMENT_SUPPORT_DOC_LIST, new ArrayList<>(docDisplayDtoList));

        Map<String, String> repoIdDocNameMap = DocDisplayDtoUtil.getRepoIdDocNameMap(docDisplayDtoList);
        repoIdDocNameMap.putAll(DocDisplayDtoUtil.getRepoIdDocNameMap(docDisplayDtoList));
        ParamUtil.setSessionAttr(request, KEY_DOC_DISPLAY_DTO_REPO_ID_NAME_MAP, (Serializable) repoIdDocNameMap);

        ArrayList<ProcessHistoryDto> processHistoryDtoList = new ArrayList<>(initDataDto.getProcessHistoryDtoList());
        ParamUtil.setSessionAttr(request, ModuleCommonConstants.KEY_ROUTING_HISTORY_LIST, processHistoryDtoList);

        ParamUtil.setSessionAttr(request, "aoSelectionOps", new ArrayList<>(initDataDto.getSelectRouteToAO()));
        ParamUtil.setSessionAttr(request, "hmSelectionOps", new ArrayList<>(initDataDto.getSelectRouteToHM()));

        // inspection processing
        FacApprovalProcessDto processDto = initDataDto.getProcessDto();
        if (processDto == null) {
            processDto = new FacApprovalProcessDto();
        }
        String taskId = (String) ParamUtil.getSessionAttr(request, TaskModuleConstants.PARAM_NAME_TASK_ID);
        processDto.setTaskId(taskId);
        processDto.setAppId(appId);
        processDto.setAppStatus(submissionDetailsInfo.getApplicationStatus());
        processDto.setBatAgentIdApprovedMap(new HashMap<>());
        ParamUtil.setSessionAttr(request, KEY_FACILITY_APPROVAL_PROCESS_DTO, processDto);
    }

    public void pre(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        String appId = (String) ParamUtil.getSessionAttr(request, TaskModuleConstants.PARAM_NAME_APP_ID);
        List<DocDisplayDto> internalDocDisplayDto = internalDocClient.getInternalDocForDisplay(appId);
        ParamUtil.setRequestAttr(request, ModuleCommonConstants.KEY_TAB_DOCUMENT_INTERNAL_DOC_LIST, internalDocDisplayDto);

        // view application
        SubmissionDetailsInfo submissionDetailsInfo = (SubmissionDetailsInfo) ParamUtil.getSessionAttr(request, ModuleCommonConstants.KEY_SUBMISSION_DETAILS_INFO);
        String appStatus = submissionDetailsInfo.getApplicationStatus();
        // TODO: check these app status
        switch(appStatus) {
            case MasterCodeConstants.APP_STATUS_PEND_DO_RECOMMENDATION:
                AppViewService.approvalAppViewApp(request, appId, TaskType.DO_PROCESSING_FACILITY_APPROVAL);
                break;
            case MasterCodeConstants.APP_STATUS_PEND_AO_APPROVAL:
                AppViewService.approvalAppViewApp(request, appId, TaskType.AO_PROCESSING_FACILITY_APPROVAL);
                break;
            case MasterCodeConstants.APP_STATUS_PEND_HM_APPROVAL:
                AppViewService.approvalAppViewApp(request, appId, TaskType.HM_PROCESSING_FACILITY_APPROVAL);
                break;
            default:
                log.info("no such appStatus {}", StringUtils.normalizeSpace(appStatus));
                break;
        }
    }

    public void bindAction(BaseProcessClass bpc) {
        // do nothing now
    }

    public void handleSubmit(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        FacApprovalProcessDto processDto = (FacApprovalProcessDto) ParamUtil.getSessionAttr(request, KEY_FACILITY_APPROVAL_PROCESS_DTO);
        processDto.reqObjMapping(request);
        ParamUtil.setSessionAttr(request, KEY_FACILITY_APPROVAL_PROCESS_DTO, processDto);

        ValidationResultDto validationResultDto = facApprovalClient.validateFacApprovalProcessDto(processDto);
        String decision = processDto.getProcessingDecision();
        String validateResult;
        // TODO: check these decision
        if (validationResultDto.isPass()) {
            if (MasterCodeConstants.MOH_PROCESS_DECISION_REQUEST_FOR_INFORMATION.equals(decision)) {
                validateResult = "rfi";
            } else if (MasterCodeConstants.MOH_PROCESS_DECISION_REJECT.equals(decision)) {
                validateResult = "reject";
            } else if (MasterCodeConstants.MOH_PROCESS_DECISION_APPROVE.equals(decision)) {
                validateResult = "approve";
            } else if (MasterCodeConstants.MOH_PROCESS_DECISION_ROUTE_BACK_TO_DO.equals(decision)) {
                validateResult = "routeDO";
            } else if (MasterCodeConstants.MOH_PROCESS_DECISION_ROUTE_TO_HM.equals(decision)) {
                validateResult = "routeHM";
            } else {
                validateResult = "invalid";
            }
        } else {
            log.error("Validation failure info: {}", validationResultDto.toErrorMsg());
            ParamUtil.setRequestAttr(request, ModuleCommonConstants.KEY_VALIDATION_ERRORS, validationResultDto.toErrorMsg());
            ParamUtil.setRequestAttr(request, TAB_ACTIVE, TAB_PROCESSING);
            validateResult = "back";
        }
        log.info("Officer submit decision [{}] for review inspection NCs Rectification report, route result [{}]", LogUtil.escapeCrlf(processDto.getProcessingDecision()), validateResult);
        ParamUtil.setRequestAttr(request, KEY_ROUTE, validateResult);
    }

    public void approveByDO(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        FacApprovalProcessDto processDto = (FacApprovalProcessDto) ParamUtil.getSessionAttr(request, KEY_FACILITY_APPROVAL_PROCESS_DTO);
        facApprovalClient.facApprovalApprove(processDto, StageConstants.ROLE_DO);
        ParamUtil.setRequestAttr(request, KEY_RESULT_MSG, MSG_COMPLETE_TASK);
    }

    public void rejectByDO(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        FacApprovalProcessDto processDto = (FacApprovalProcessDto) ParamUtil.getSessionAttr(request, KEY_FACILITY_APPROVAL_PROCESS_DTO);
        facApprovalClient.facApprovalReject(processDto, StageConstants.ROLE_DO);
        ParamUtil.setRequestAttr(request, KEY_RESULT_MSG, MSG_COMPLETE_TASK);
    }

    public void rfiByDO(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        FacApprovalProcessDto processDto = (FacApprovalProcessDto) ParamUtil.getSessionAttr(request, KEY_FACILITY_APPROVAL_PROCESS_DTO);
        RfiProcessDto rfiProcessDto = (RfiProcessDto) ParamUtil.getSessionAttr(request, KEY_RFI_PROCESS_DTO);
        processDto.setRfiProcessDto(rfiProcessDto);
        facApprovalClient.facApprovalDORfi(processDto);
        ParamUtil.setRequestAttr(request, KEY_RESULT_MSG, MSG_COMPLETE_TASK);
    }

    public void rfiByAO(BaseProcessClass bpc) {
        //DO FRI
    }

    public void approveByAO(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        FacApprovalProcessDto processDto = (FacApprovalProcessDto) ParamUtil.getSessionAttr(request, KEY_FACILITY_APPROVAL_PROCESS_DTO);
        facApprovalClient.facApprovalApprove(processDto, StageConstants.ROLE_AO);
        ParamUtil.setRequestAttr(request, KEY_RESULT_MSG, MSG_COMPLETE_TASK);
    }

    public void rejectByAO(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        FacApprovalProcessDto processDto = (FacApprovalProcessDto) ParamUtil.getSessionAttr(request, KEY_FACILITY_APPROVAL_PROCESS_DTO);
        facApprovalClient.facApprovalReject(processDto, StageConstants.ROLE_AO);
        ParamUtil.setRequestAttr(request, KEY_RESULT_MSG, MSG_COMPLETE_TASK);
    }

    public void routeDO(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        FacApprovalProcessDto processDto = (FacApprovalProcessDto) ParamUtil.getSessionAttr(request, KEY_FACILITY_APPROVAL_PROCESS_DTO);
        facApprovalClient.facApprovalRouteToDO(processDto);
        ParamUtil.setRequestAttr(request, KEY_RESULT_MSG, MSG_COMPLETE_TASK);
    }

    public void routeHM(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        FacApprovalProcessDto processDto = (FacApprovalProcessDto) ParamUtil.getSessionAttr(request, KEY_FACILITY_APPROVAL_PROCESS_DTO);
        facApprovalClient.facApprovalRouteToHM(processDto);
        ParamUtil.setRequestAttr(request, KEY_RESULT_MSG, MSG_COMPLETE_TASK);
    }

    public void approveByHM(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        FacApprovalProcessDto processDto = (FacApprovalProcessDto) ParamUtil.getSessionAttr(request, KEY_FACILITY_APPROVAL_PROCESS_DTO);
        facApprovalClient.facApprovalApprove(processDto, StageConstants.ROLE_HM);
        ParamUtil.setRequestAttr(request, KEY_RESULT_MSG, MSG_COMPLETE_TASK);
    }

    public void rejectByHM(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        FacApprovalProcessDto processDto = (FacApprovalProcessDto) ParamUtil.getSessionAttr(request, KEY_FACILITY_APPROVAL_PROCESS_DTO);
        facApprovalClient.facApprovalReject(processDto, StageConstants.ROLE_HM);
        ParamUtil.setRequestAttr(request, KEY_RESULT_MSG, MSG_COMPLETE_TASK);
    }

    public void prepareRfi(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        String appId = (String) ParamUtil.getSessionAttr(request, TaskModuleConstants.PARAM_NAME_APP_ID);
        appViewService.retrieveApprovalBatAndActivity(request, appId);
    }

    public void doRfi(BaseProcessClass bpc) {
        rfiService.doNewApprovalAppRfi(bpc.request);
    }
}

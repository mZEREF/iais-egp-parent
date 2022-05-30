package sg.gov.moh.iais.egp.bsb.action;


import com.ecquaria.cloud.annotation.Delegator;

import com.ecquaria.cloud.moh.iais.common.utils.LogUtil;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import lombok.extern.slf4j.Slf4j;
import sg.gov.moh.iais.egp.bsb.client.FacilityApprovalClient;
import sg.gov.moh.iais.egp.bsb.client.InternalDocClient;
import sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants;
import sg.gov.moh.iais.egp.bsb.constant.StageConstants;
import sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants;
import sg.gov.moh.iais.egp.bsb.constant.module.TaskModuleConstants;
import sg.gov.moh.iais.egp.bsb.dto.ProcessHistoryDto;
import sg.gov.moh.iais.egp.bsb.dto.facilityapproval.FacApprovalInitDataDto;
import sg.gov.moh.iais.egp.bsb.dto.facilityapproval.FacApprovalProcessDto;
import sg.gov.moh.iais.egp.bsb.dto.file.DocDisplayDto;
import sg.gov.moh.iais.egp.bsb.dto.mohprocessingdisplay.FacilityBiologicalAgentInfo;
import sg.gov.moh.iais.egp.bsb.dto.mohprocessingdisplay.FacilityDetailsInfo;
import sg.gov.moh.iais.egp.bsb.dto.mohprocessingdisplay.SubmissionDetailsInfo;
import sg.gov.moh.iais.egp.bsb.dto.validation.ValidationResultDto;
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

import static sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants.KEY_DOC_DISPLAY_DTO_REPO_ID_NAME_MAP;


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

    public FacilityApprovalOfficerDelegator(FacilityApprovalClient facApprovalClient, InternalDocClient internalDocClient) {
        this.facApprovalClient = facApprovalClient;
        this.internalDocClient = internalDocClient;
    }

    public void startDO(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        MaskHelper.taskProcessUnmask(request, TaskModuleConstants.PARAM_NAME_APP_ID, TaskModuleConstants.PARAM_NAME_TASK_ID);
        AuditTrailHelper.auditFunction("Pending DO Recommendation", "DO recommend processing decision");
    }

    public void startAO(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        MaskHelper.taskProcessUnmask(request, TaskModuleConstants.PARAM_NAME_APP_ID, TaskModuleConstants.PARAM_NAME_TASK_ID);
        AuditTrailHelper.auditFunction("Pending AO Approval", "AO handle processing decision");
    }

    public void startHM(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        MaskHelper.taskProcessUnmask(request, TaskModuleConstants.PARAM_NAME_APP_ID, TaskModuleConstants.PARAM_NAME_TASK_ID);
        AuditTrailHelper.auditFunction("Pending HM Approval", "HM handle processing decision");
    }

    public void init(BaseProcessClass bpc){
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

        ParamUtil.setSessionAttr(request,"aoSelectionOps",new ArrayList<>(initDataDto.getSelectRouteToAO()));
        ParamUtil.setSessionAttr(request,"hmSelectionOps",new ArrayList<>(initDataDto.getSelectRouteToHM()));

        // inspection processing
        FacApprovalProcessDto processDto = initDataDto.getProcessDto();
        if(processDto == null){
            processDto = new FacApprovalProcessDto();
        }
        String taskId = (String) ParamUtil.getSessionAttr(request, TaskModuleConstants.PARAM_NAME_TASK_ID);
        processDto.setTaskId(taskId);
        processDto.setAppId(appId);
        processDto.setAppStatus(submissionDetailsInfo.getApplicationStatus());
        processDto.setBatAgentIdApprovedMap(new HashMap<>());
        ParamUtil.setSessionAttr(request, KEY_FACILITY_APPROVAL_PROCESS_DTO, processDto);
    }

    public void pre(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        String appId = (String) ParamUtil.getSessionAttr(request, TaskModuleConstants.PARAM_NAME_APP_ID);
        List<DocDisplayDto> internalDocDisplayDto = internalDocClient.getInternalDocForDisplay(appId);
        ParamUtil.setRequestAttr(request, ModuleCommonConstants.KEY_TAB_DOCUMENT_INTERNAL_DOC_LIST, internalDocDisplayDto);

    }

    public void bindAction(BaseProcessClass bpc){
        // do nothing now
    }

    public void handleSubmit(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        FacApprovalProcessDto processDto = (FacApprovalProcessDto) ParamUtil.getSessionAttr(request, KEY_FACILITY_APPROVAL_PROCESS_DTO);
        processDto.reqObjMapping(request);
        ParamUtil.setSessionAttr(request, KEY_FACILITY_APPROVAL_PROCESS_DTO, processDto);

        ValidationResultDto validationResultDto = facApprovalClient.validateFacApprovalProcessDto(processDto);
        String decision = processDto.getProcessingDecision();
        String validateResult;
        if (validationResultDto.isPass()) {
            if(MasterCodeConstants.MOH_PROCESSING_DECISION_REQUEST_FOR_INFO.equals(decision)){
                validateResult = "rfi";
            }else if(MasterCodeConstants.MOH_PROCESSING_DECISION_REJECT.equals(decision)){
                validateResult = "reject";
            }else if(MasterCodeConstants.MOH_PROCESSING_DECISION_APPROVE.equals(decision)){
                validateResult = "approve";
            }else if(MasterCodeConstants.MOH_PROCESSING_DECISION_ROUTE_BACK_TO_DO.equals(decision)){
                validateResult = "routeDO";
            }else if(MasterCodeConstants.MOH_PROCESSING_DECISION_ROUTE_BACK_TO_HM.equals(decision)){
                validateResult = "routeHM";
            }else{
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

    public void approveByDO(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        FacApprovalProcessDto processDto = (FacApprovalProcessDto) ParamUtil.getSessionAttr(request, KEY_FACILITY_APPROVAL_PROCESS_DTO);
        facApprovalClient.facApprovalApprove(processDto, StageConstants.ROLE_DO);
        ParamUtil.setRequestAttr(request, KEY_RESULT_MSG, MSG_COMPLETE_TASK);
    }

    public void rejectByDO(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        FacApprovalProcessDto processDto = (FacApprovalProcessDto) ParamUtil.getSessionAttr(request, KEY_FACILITY_APPROVAL_PROCESS_DTO);
        facApprovalClient.facApprovalReject(processDto, StageConstants.ROLE_DO);
        ParamUtil.setRequestAttr(request, KEY_RESULT_MSG, MSG_COMPLETE_TASK);
    }

    public void rfiByDO(BaseProcessClass bpc){
        //DO FRI
    }

    public void rfiByAO(BaseProcessClass bpc){
        //DO FRI
    }

    public void approveByAO(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        FacApprovalProcessDto processDto = (FacApprovalProcessDto) ParamUtil.getSessionAttr(request, KEY_FACILITY_APPROVAL_PROCESS_DTO);
        facApprovalClient.facApprovalApprove(processDto, StageConstants.ROLE_AO);
        ParamUtil.setRequestAttr(request, KEY_RESULT_MSG, MSG_COMPLETE_TASK);
    }

    public void rejectByAO(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        FacApprovalProcessDto processDto = (FacApprovalProcessDto) ParamUtil.getSessionAttr(request, KEY_FACILITY_APPROVAL_PROCESS_DTO);
        facApprovalClient.facApprovalReject(processDto, StageConstants.ROLE_AO);
        ParamUtil.setRequestAttr(request, KEY_RESULT_MSG, MSG_COMPLETE_TASK);
    }

    public void routeDO(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        FacApprovalProcessDto processDto = (FacApprovalProcessDto) ParamUtil.getSessionAttr(request, KEY_FACILITY_APPROVAL_PROCESS_DTO);
        facApprovalClient.facApprovalRouteToDO(processDto);
        ParamUtil.setRequestAttr(request, KEY_RESULT_MSG, MSG_COMPLETE_TASK);
    }

    public void routeHM(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        FacApprovalProcessDto processDto = (FacApprovalProcessDto) ParamUtil.getSessionAttr(request, KEY_FACILITY_APPROVAL_PROCESS_DTO);
        facApprovalClient.facApprovalRouteToHM(processDto);
        ParamUtil.setRequestAttr(request, KEY_RESULT_MSG, MSG_COMPLETE_TASK);
    }

    public void approveByHM(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        FacApprovalProcessDto processDto = (FacApprovalProcessDto) ParamUtil.getSessionAttr(request, KEY_FACILITY_APPROVAL_PROCESS_DTO);
        facApprovalClient.facApprovalApprove(processDto, StageConstants.ROLE_HM);
        ParamUtil.setRequestAttr(request, KEY_RESULT_MSG, MSG_COMPLETE_TASK);
    }

    public void rejectByHM(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        FacApprovalProcessDto processDto = (FacApprovalProcessDto) ParamUtil.getSessionAttr(request, KEY_FACILITY_APPROVAL_PROCESS_DTO);
        facApprovalClient.facApprovalReject(processDto, StageConstants.ROLE_HM);
        ParamUtil.setRequestAttr(request, KEY_RESULT_MSG, MSG_COMPLETE_TASK);
    }
}

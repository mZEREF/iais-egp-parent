package sg.gov.moh.iais.egp.bsb.service;

import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import sg.gov.moh.iais.egp.bsb.client.InternalDocClient;
import sg.gov.moh.iais.egp.bsb.client.ProcessClient;
import sg.gov.moh.iais.egp.bsb.constant.TaskType;
import sg.gov.moh.iais.egp.bsb.dto.ResponseDto;
import sg.gov.moh.iais.egp.bsb.dto.inspection.ReportDto;
import sg.gov.moh.iais.egp.bsb.dto.inspection.afc.ReviewAFCReportDto;
import sg.gov.moh.iais.egp.bsb.dto.mohprocessingdisplay.FacilityBiologicalAgentInfo;
import sg.gov.moh.iais.egp.bsb.dto.mohprocessingdisplay.FacilityDetailsInfo;
import sg.gov.moh.iais.egp.bsb.dto.mohprocessingdisplay.RFFacilityDetailsInfo;
import sg.gov.moh.iais.egp.bsb.dto.mohprocessingdisplay.RFFacilityInfo;
import sg.gov.moh.iais.egp.bsb.dto.process.DOVerificationDto;
import sg.gov.moh.iais.egp.bsb.dto.process.MohProcessDto;
import sg.gov.moh.iais.egp.bsb.dto.validation.ValidationResultDto;
import sg.gov.moh.iais.egp.bsb.util.CollectionUtils;
import sg.gov.moh.iais.egp.bsb.util.DocDisplayDtoUtil;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.ecquaria.cloud.moh.iais.common.constant.BsbAuditTrailConstants.FUNCTION_AO_APPROVAL;
import static com.ecquaria.cloud.moh.iais.common.constant.BsbAuditTrailConstants.FUNCTION_DO_RECOMMENDATION;
import static com.ecquaria.cloud.moh.iais.common.constant.BsbAuditTrailConstants.FUNCTION_DO_SCREENING;
import static com.ecquaria.cloud.moh.iais.common.constant.BsbAuditTrailConstants.FUNCTION_HM_APPROVAL;
import static sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants.ACTIVITY_SP_HANDLE_PV_POTENTIAL;
import static sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants.NO;
import static sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants.YES;
import static sg.gov.moh.iais.egp.bsb.constant.module.InspectionConstants.KEY_INS_REPORT;
import static sg.gov.moh.iais.egp.bsb.constant.module.InspectionConstants.KEY_REVIEW_AFC_REPORT_DTO;
import static sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants.KEY_BAT_INFO_MAP;
import static sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants.KEY_CRUD_ACTION_TYPE;
import static sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants.KEY_DOC_DISPLAY_DTO_REPO_ID_NAME_MAP;
import static sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants.KEY_FACILITY_DETAILS_INFO;
import static sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants.KEY_ROUTING_HISTORY_LIST;
import static sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants.KEY_SELECT_ROUTE_TO_MOH;
import static sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants.KEY_SUBMISSION_DETAILS_INFO;
import static sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants.KEY_TAB_DOCUMENT_INTERNAL_DOC_LIST;
import static sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants.KEY_TAB_DOCUMENT_SUPPORT_DOC_LIST;
import static sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants.KEY_VALIDATION_ERRORS;
import static sg.gov.moh.iais.egp.bsb.constant.module.ProcessContants.CRUD_ACTION_TYPE_PREPARE;
import static sg.gov.moh.iais.egp.bsb.constant.module.ProcessContants.CRUD_ACTION_TYPE_PROCESS;
import static sg.gov.moh.iais.egp.bsb.constant.module.ProcessContants.KEY_DO_VERIFICATION_DTO;
import static sg.gov.moh.iais.egp.bsb.constant.module.ProcessContants.KEY_MOH_PROCESS_DTO;
import static sg.gov.moh.iais.egp.bsb.constant.module.ProcessContants.KEY_PROCESSING_DECISION;
import static sg.gov.moh.iais.egp.bsb.constant.module.ProcessContants.KEY_REMARKS;
import static sg.gov.moh.iais.egp.bsb.constant.module.ProcessContants.MOH_PROCESS_PAGE_VALIDATION;
import static sg.gov.moh.iais.egp.bsb.constant.module.RfiConstants.KEY_COMMENTS_TO_APPLICANT;
import static sg.gov.moh.iais.egp.bsb.constant.module.TaskModuleConstants.PARAM_NAME_APP_ID;


@Service
@Slf4j
public class MohProcessService {
    private final ProcessClient processClient;
    private final InternalDocClient internalDocClient;
    private final InsAFCReportService insAFCReportService;

    public MohProcessService(ProcessClient processClient, InternalDocClient internalDocClient, InsAFCReportService insAFCReportService) {
        this.processClient = processClient;
        this.internalDocClient = internalDocClient;
        this.insAFCReportService = insAFCReportService;
    }

    public MohProcessDto getMohProcessDto(HttpServletRequest request, String applicationId) {
        MohProcessDto dto = (MohProcessDto) ParamUtil.getSessionAttr(request, KEY_MOH_PROCESS_DTO);
        if (dto == null) {
            dto = processClient.getMohProcessDtoByAppId(applicationId).getEntity();
        }
        return dto;
    }

    public DOVerificationDto getDOVerificationDto(HttpServletRequest request, String applicationId) {
        DOVerificationDto dto = (DOVerificationDto) ParamUtil.getSessionAttr(request, KEY_DO_VERIFICATION_DTO);
        if (dto == null) {
            dto = processClient.getDOVerificationByAppId(applicationId).getEntity();
        }
        return dto;
    }

//    ---------------------------- Moh process delegator public part ----------------------------------------------

    public void prepareData(BaseProcessClass bpc, String functionName) {
        HttpServletRequest request = bpc.request;
        String appId = (String) ParamUtil.getSessionAttr(request, PARAM_NAME_APP_ID);
        MohProcessDto mohProcessDto = getMohProcessDto(request, appId);
        ParamUtil.setSessionAttr(request, KEY_MOH_PROCESS_DTO, mohProcessDto);

        // show data
        ParamUtil.setRequestAttr(request, KEY_SUBMISSION_DETAILS_INFO, mohProcessDto.getSubmissionDetailsInfo());
        ParamUtil.setRequestAttr(request, KEY_FACILITY_DETAILS_INFO, mohProcessDto.getFacilityDetailsInfo());

        // show BAT info
        List<FacilityBiologicalAgentInfo> batInfoList = Optional.of(mohProcessDto)
                .map(MohProcessDto::getFacilityDetailsInfo)
                .map(FacilityDetailsInfo::getFacilityBiologicalAgentInfoList)
                .orElse(null);
        if (batInfoList != null && !batInfoList.isEmpty()) {
            Map<String, List<FacilityBiologicalAgentInfo>> batMap = CollectionUtils.groupCollectionToMap(batInfoList, FacilityBiologicalAgentInfo::getApproveType);
            ParamUtil.setRequestAttr(request, KEY_BAT_INFO_MAP, batMap);
        }


        // show applicant support doc
        ParamUtil.setRequestAttr(request, KEY_TAB_DOCUMENT_SUPPORT_DOC_LIST, mohProcessDto.getSupportDocDisplayDtoList());
        // provide for download support doc
        Map<String, String> repoIdDocNameMap = DocDisplayDtoUtil.getRepoIdDocNameMap(mohProcessDto.getSupportDocDisplayDtoList());
        ParamUtil.setSessionAttr(request, KEY_DOC_DISPLAY_DTO_REPO_ID_NAME_MAP, (Serializable) repoIdDocNameMap);
        // show internal doc
        ParamUtil.setRequestAttr(request, KEY_TAB_DOCUMENT_INTERNAL_DOC_LIST, internalDocClient.getInternalDocForDisplay(appId));
        // show route to moh selection list
        ParamUtil.setRequestAttr(request, KEY_SELECT_ROUTE_TO_MOH, mohProcessDto.getSelectRouteToMoh());
        // show routingHistory list
        ParamUtil.setRequestAttr(request, KEY_ROUTING_HISTORY_LIST, mohProcessDto.getProcessHistoryDtoList());

        // view application
        if (functionName.equals(FUNCTION_DO_SCREENING)) {
            // has rfi decision
            AppViewService.facilityRegistrationViewApp(request, appId, TaskType.DO_SCREENING);
        } else if (functionName.equals(FUNCTION_DO_RECOMMENDATION)) {
            // has rfi decision
            AppViewService.facilityRegistrationViewApp(request, appId, TaskType.DO_PROCESSING);
        } else {
            // hasn't rfi decision
            AppViewService.facilityRegistrationViewApp(request, appId);
        }

        //AFC Certification Report and Inspection Report
        if (functionName.equals(FUNCTION_DO_RECOMMENDATION) || functionName.equals(FUNCTION_AO_APPROVAL) || functionName.equals(FUNCTION_HM_APPROVAL)) {
            setAFCAndInspectionReportDataRequest(request, appId);
        }
    }

    public void prepareSwitch(BaseProcessClass bpc, String functionName) {
        HttpServletRequest request = bpc.request;
        String appId = (String) ParamUtil.getSessionAttr(request, PARAM_NAME_APP_ID);
        MohProcessDto mohProcessDto = (MohProcessDto) ParamUtil.getSessionAttr(request, KEY_MOH_PROCESS_DTO);

        mohProcessDto.reqObjMapping(request, functionName);
        ParamUtil.setSessionAttr(request, KEY_MOH_PROCESS_DTO, mohProcessDto);
        //validation
        ValidationResultDto validationResultDto = processClient.validateMohProcessDto(mohProcessDto);
        String crudActionType;
        if (!validationResultDto.isPass()) {
            ParamUtil.setRequestAttr(request, MOH_PROCESS_PAGE_VALIDATION, YES);
            ParamUtil.setRequestAttr(request, KEY_VALIDATION_ERRORS, validationResultDto.toErrorMsg());
            crudActionType = CRUD_ACTION_TYPE_PREPARE;
        } else {
            crudActionType = CRUD_ACTION_TYPE_PROCESS;
        }
        if (functionName.equals(FUNCTION_DO_RECOMMENDATION)) {
            String canSubmit = processClient.judgeCanSubmitDOProcessingTask(appId);
            if (canSubmit.equals(NO)) {
                crudActionType = CRUD_ACTION_TYPE_PREPARE;
            }
            ParamUtil.setRequestAttr(request, "canSubmit", canSubmit);
        }
        ParamUtil.setRequestAttr(request, KEY_CRUD_ACTION_TYPE, crudActionType);
    }

    public void setAFCAndInspectionReportDataRequest(HttpServletRequest request, String appId) {
        ResponseDto<ReviewAFCReportDto> responseDto = processClient.getLatestCertificationReportByInsAppId(appId);
        if (responseDto.ok()) {
            ReviewAFCReportDto reviewAFCReportDto = responseDto.getEntity();
            ParamUtil.setRequestAttr(request, KEY_REVIEW_AFC_REPORT_DTO, reviewAFCReportDto);
            insAFCReportService.setSavedDocMap(reviewAFCReportDto, request);
        } else {
            ParamUtil.setRequestAttr(request, KEY_REVIEW_AFC_REPORT_DTO, new ReviewAFCReportDto());
        }
        ResponseDto<ReportDto> response = processClient.getInsReportData(appId);
        if (response.ok()) {
            ParamUtil.setRequestAttr(request, KEY_INS_REPORT, response.getEntity());
        } else {
            ParamUtil.setRequestAttr(request, KEY_INS_REPORT, null);
        }
    }

    public void reqObjMappingDOVerification(HttpServletRequest request, DOVerificationDto doVerificationDto){
        doVerificationDto.setRemarks(ParamUtil.getString(request, KEY_REMARKS));
        doVerificationDto.setProcessingDecision(ParamUtil.getString(request, KEY_PROCESSING_DECISION));
        doVerificationDto.setCommentsToApplicant(ParamUtil.getString(request, KEY_COMMENTS_TO_APPLICANT));

        RFFacilityDetailsInfo rfFacilityDetailsInfo = doVerificationDto.getRfFacilityDetailsInfo();
        String facilityActivityType = rfFacilityDetailsInfo.getFacilityActivityType();
        if (ACTIVITY_SP_HANDLE_PV_POTENTIAL.equals(facilityActivityType)) {
            // this activity type can apply multi facility
            List<RFFacilityInfo> rfFacilityInfoList = rfFacilityDetailsInfo.getRfFacilityInfoList();
            for (RFFacilityInfo rfFacilityInfo : rfFacilityInfoList) {
                String checked = ParamUtil.getString(request, rfFacilityInfo.getId());
                rfFacilityInfo.setStatus(checked);
            }
        }
    }
}

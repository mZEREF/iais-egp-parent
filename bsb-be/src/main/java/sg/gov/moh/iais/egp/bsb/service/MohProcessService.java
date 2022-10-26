package sg.gov.moh.iais.egp.bsb.service;

import com.ecquaria.cloud.moh.iais.common.exception.IaisRuntimeException;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import sg.gov.moh.iais.egp.bsb.client.AppSupportingDocClient;
import sg.gov.moh.iais.egp.bsb.client.InternalDocClient;
import sg.gov.moh.iais.egp.bsb.client.ProcessClient;
import sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants;
import sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants;
import sg.gov.moh.iais.egp.bsb.dto.ResponseDto;
import sg.gov.moh.iais.egp.bsb.dto.file.DocDisplayDto;
import sg.gov.moh.iais.egp.bsb.dto.file.DocRecordInfo;
import sg.gov.moh.iais.egp.bsb.dto.inspection.ReportDto;
import sg.gov.moh.iais.egp.bsb.dto.inspection.afc.ReviewAFCReportDto;
import sg.gov.moh.iais.egp.bsb.dto.mohprocessingdisplay.AdeBatInfo;
import sg.gov.moh.iais.egp.bsb.dto.mohprocessingdisplay.AdeFacilityActivityInfo;
import sg.gov.moh.iais.egp.bsb.dto.mohprocessingdisplay.AdeFacilityInfo;
import sg.gov.moh.iais.egp.bsb.dto.mohprocessingdisplay.FacilityDetailsInfo;
import sg.gov.moh.iais.egp.bsb.dto.process.DOVerificationDto;
import sg.gov.moh.iais.egp.bsb.dto.process.DualDocSortingDto;
import sg.gov.moh.iais.egp.bsb.dto.process.MohProcessDto;
import sg.gov.moh.iais.egp.bsb.dto.rfi.PageAppEditSelectDto;
import sg.gov.moh.iais.egp.bsb.dto.validation.ValidationResultDto;
import sg.gov.moh.iais.egp.bsb.util.DocDisplayDtoUtil;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.ecquaria.cloud.moh.iais.common.constant.BsbAuditTrailConstants.FUNCTION_AO_APPROVAL;
import static com.ecquaria.cloud.moh.iais.common.constant.BsbAuditTrailConstants.FUNCTION_AO_SCREENING;
import static com.ecquaria.cloud.moh.iais.common.constant.BsbAuditTrailConstants.FUNCTION_DO_RECOMMENDATION;
import static com.ecquaria.cloud.moh.iais.common.constant.BsbAuditTrailConstants.FUNCTION_DO_SCREENING;
import static com.ecquaria.cloud.moh.iais.common.constant.BsbAuditTrailConstants.FUNCTION_HM_APPROVAL;
import static com.ecquaria.cloud.moh.iais.common.constant.BsbAuditTrailConstants.FUNCTION_HM_DECISION;
import static sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants.ACTIVITY_SP_HANDLE_PV_POTENTIAL;
import static sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants.MOH_PROCESS_DECISION_APPROVE_APPLICATION;
import static sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants.MOH_PROCESS_DECISION_REJECT_APPLICATION;
import static sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants.MOH_PROCESS_FACILITY_PROCESS_TYPE;
import static sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants.NO;
import static sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants.PROCESS_TYPE_APPROVAL_FOR_FACILITY_ACTIVITY_TYPE;
import static sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants.PROCESS_TYPE_FAC_REG;
import static sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants.YES;
import static sg.gov.moh.iais.egp.bsb.constant.module.InspectionConstants.KEY_INS_REPORT;
import static sg.gov.moh.iais.egp.bsb.constant.module.InspectionConstants.KEY_REVIEW_AFC_REPORT_DTO;
import static sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants.INTERNAL_DOCUMENT_TYPE_LIST;
import static sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants.KEY_ACTION_ADDITIONAL;
import static sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants.KEY_ACTION_TYPE;
import static sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants.KEY_ACTION_VALUE;
import static sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants.KEY_CRUD_ACTION_TYPE;
import static sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants.KEY_DOC_DISPLAY_DTO_REPO_ID_NAME_MAP;
import static sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants.KEY_DUAL_DOC_SORTING_INFO;
import static sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants.KEY_FACILITY_DETAILS_INFO;
import static sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants.KEY_INTERNAL_DOC_BSB_ASSESSMENT_AND_RECOMMENDATION;
import static sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants.KEY_ROUTING_HISTORY_LIST;
import static sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants.KEY_SELECT_ROUTE_TO_MOH;
import static sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants.KEY_SUBMISSION_DETAILS_INFO;
import static sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants.KEY_TAB_DOCUMENT_INTERNAL_DOC_LIST;
import static sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants.KEY_TAB_DOCUMENT_SUPPORT_DOC_LIST;
import static sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants.KEY_VALIDATION_ERRORS;
import static sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants.TAB_ACTIVE;
import static sg.gov.moh.iais.egp.bsb.constant.module.ProcessContants.CRUD_ACTION_TYPE_PREPARE;
import static sg.gov.moh.iais.egp.bsb.constant.module.ProcessContants.CRUD_ACTION_TYPE_PROCESS;
import static sg.gov.moh.iais.egp.bsb.constant.module.ProcessContants.KEY_AO_RECOMMENDATION;
import static sg.gov.moh.iais.egp.bsb.constant.module.ProcessContants.KEY_CERTIFICATION_REQUIRED;
import static sg.gov.moh.iais.egp.bsb.constant.module.ProcessContants.KEY_DO_VERIFICATION_DTO;
import static sg.gov.moh.iais.egp.bsb.constant.module.ProcessContants.KEY_FAC_VALIDITY_END_DATE;
import static sg.gov.moh.iais.egp.bsb.constant.module.ProcessContants.KEY_INSPECTION_REQUIRED;
import static sg.gov.moh.iais.egp.bsb.constant.module.ProcessContants.KEY_MOH_PROCESS_DTO;
import static sg.gov.moh.iais.egp.bsb.constant.module.ProcessContants.KEY_PROCESSING_DECISION;
import static sg.gov.moh.iais.egp.bsb.constant.module.ProcessContants.KEY_REASON_FOR_REJECTION;
import static sg.gov.moh.iais.egp.bsb.constant.module.ProcessContants.KEY_REMARKS;
import static sg.gov.moh.iais.egp.bsb.constant.module.ProcessContants.KEY_SELECT_MOH_USER;
import static sg.gov.moh.iais.egp.bsb.constant.module.RfiConstants.KEY_COMMENTS_TO_APPLICANT;
import static sg.gov.moh.iais.egp.bsb.constant.module.RfiConstants.KEY_PAGE_APP_EDIT_SELECT_DTO;
import static sg.gov.moh.iais.egp.bsb.constant.module.TaskModuleConstants.PARAM_NAME_APP_ID;


@Service
@Slf4j
@RequiredArgsConstructor
public class MohProcessService {
    private static final String SEPARATOR = "--v--";

    private final ProcessClient processClient;
    private final AppSupportingDocClient appSupportingDocClient;
    private final InternalDocClient internalDocClient;
    private final InsAFCReportService insAFCReportService;
    private final DocSettingService docSettingService;
    private final RfiService rfiService;

    public MohProcessDto getMohProcessDto(HttpServletRequest request, String applicationId, String functionName) {
        MohProcessDto dto = (MohProcessDto) ParamUtil.getSessionAttr(request, KEY_MOH_PROCESS_DTO);
        if (dto == null) {
            dto = processClient.getMohProcessDtoByAppId(applicationId, functionName).getEntity();
        }
        return dto;
    }

//    ---------------------------- Moh process delegator public part ----------------------------------------------

    public void prepareData(BaseProcessClass bpc, String functionName) {
        HttpServletRequest request = bpc.request;
        String appId = (String) ParamUtil.getSessionAttr(request, PARAM_NAME_APP_ID);
        MohProcessDto mohProcessDto = getMohProcessDto(request, appId, functionName);
        ParamUtil.setSessionAttr(request, KEY_MOH_PROCESS_DTO, mohProcessDto);

        // show data
        ParamUtil.setRequestAttr(request, KEY_SUBMISSION_DETAILS_INFO, mohProcessDto.getSubmissionDetailsInfo());
        ParamUtil.setRequestAttr(request, KEY_FACILITY_DETAILS_INFO, mohProcessDto.getFacilityDetailsInfo());

        // show applicant support doc
        DualDocSortingDto dualDocSortingDto = DualDocSortingService.retrieveDualDocSortingInfo(request);
        List<DocDisplayDto> supportingDocDisplayDtoList = appSupportingDocClient.getAppSupportingDocForProcessByAppId(appId, dualDocSortingDto.getSupportingDocSort());
        ParamUtil.setRequestAttr(request, KEY_TAB_DOCUMENT_SUPPORT_DOC_LIST, supportingDocDisplayDtoList);
        // provide for download support doc
        Map<String, String> repoIdDocNameMap = DocDisplayDtoUtil.getRepoIdDocNameMap(supportingDocDisplayDtoList);
        ParamUtil.setSessionAttr(request, KEY_DOC_DISPLAY_DTO_REPO_ID_NAME_MAP, (Serializable) repoIdDocNameMap);
        //set internal doc types
        ParamUtil.setRequestAttr(request, INTERNAL_DOCUMENT_TYPE_LIST, docSettingService.getInternalDocTypeList());
        // show internal doc
        List<DocDisplayDto> dtoList = internalDocClient.getSortedInternalDocForDisplay(appId, dualDocSortingDto.getInternalDocSort());
        ParamUtil.setRequestAttr(request, KEY_TAB_DOCUMENT_INTERNAL_DOC_LIST, dtoList);
        // show route to moh selection list
        ParamUtil.setRequestAttr(request, KEY_SELECT_ROUTE_TO_MOH, mohProcessDto.getSelectRouteToMoh());
        // show routingHistory list
        ParamUtil.setRequestAttr(request, KEY_ROUTING_HISTORY_LIST, mohProcessDto.getProcessHistoryDtoList());

        //AFC Certification Report and Inspection Report
        if (FUNCTION_DO_RECOMMENDATION.equals(functionName) || FUNCTION_AO_APPROVAL.equals(functionName) || FUNCTION_HM_APPROVAL.equals(functionName)) {
            setAFCAndInspectionReportDataRequest(request, appId);
        }

        //prepare internal doc which type is assessment and recommendation

        if(FUNCTION_AO_SCREENING.equals(functionName) || FUNCTION_HM_DECISION.equals(functionName)
                || FUNCTION_AO_APPROVAL.equals(functionName) || FUNCTION_HM_APPROVAL.equals(functionName)){

            List<DocRecordInfo> mohFile = internalDocClient.getActiveAssessmentInternalDocList(appId);
            ParamUtil.setSessionAttr(request, KEY_INTERNAL_DOC_BSB_ASSESSMENT_AND_RECOMMENDATION , new ArrayList<>(mohFile));
        }


    }

    /**
     * If functionName is FUNCTION_DO_RECOMMENDATION, it is necessary to judge whether AFC Certification is submitted or not.
     * If AFC Certification is submitted, then verify the form
     *
     * If functionName is FUNCTION_AO_APPROVAL, then the form needs to be verified to determine whether
     * the decision is one of the following: MOH_PROCESS_DECISION_APPROVE_APPLICATION, MOH_PROCESS_DECISION_REJECT_APPLICATION;
     * if the FORM verification passes and it is one of these decisions, then it needs to determine whether HM has processed it or not.
     */
    public void prepareSwitch(BaseProcessClass bpc, String functionName) {
        HttpServletRequest request = bpc.request;
        String appId = (String) ParamUtil.getSessionAttr(request, PARAM_NAME_APP_ID);
        MohProcessDto mohProcessDto = (MohProcessDto) ParamUtil.getSessionAttr(request, KEY_MOH_PROCESS_DTO);
        reqObjMappingMohProcess(request, functionName, mohProcessDto);

        String crudActionType;
        String actionType = ParamUtil.getString(request, KEY_ACTION_TYPE);
        if (ModuleCommonConstants.KEY_ACTION_TYPE_SORT.equals(actionType)) {
            // click sorting button in the document tab
            DualDocSortingDto dualDocSortingDto = DualDocSortingService.retrieveDualDocSortingInfo(request);
            DualDocSortingService.readDualDocSortingInfo(request, dualDocSortingDto);
            ParamUtil.setSessionAttr(request, KEY_DUAL_DOC_SORTING_INFO, dualDocSortingDto);
            ParamUtil.setRequestAttr(request, TAB_ACTIVE, ModuleCommonConstants.TAB_DOC);
            crudActionType = CRUD_ACTION_TYPE_PREPARE;
        } else if (ModuleCommonConstants.KEY_SUBMIT.equals(actionType)) {
            // submit decision
            String processType = mohProcessDto.getSubmissionDetailsInfo().getApplicationSubType();
            if (FUNCTION_DO_RECOMMENDATION.equals(functionName) && MOH_PROCESS_FACILITY_PROCESS_TYPE.contains(processType)) {
                String canSubmit = processClient.judgeCanSubmitDOProcessingTask(appId);
                ParamUtil.setRequestAttr(request, "canSubmit", canSubmit);
                if (canSubmit.equals(NO)) {
                    crudActionType = CRUD_ACTION_TYPE_PREPARE;
                }else {
                    crudActionType = commonValidate(request, mohProcessDto, functionName);
                }
            } else {
                crudActionType = commonValidate(request, mohProcessDto, functionName);
            }
            if (FUNCTION_AO_APPROVAL.equals(functionName) && CRUD_ACTION_TYPE_PROCESS.equals(crudActionType) &&
                    (MOH_PROCESS_DECISION_APPROVE_APPLICATION.equals(mohProcessDto.getProcessingDecision()) || MOH_PROCESS_DECISION_REJECT_APPLICATION.equals(mohProcessDto.getProcessingDecision()))
                    && MOH_PROCESS_FACILITY_PROCESS_TYPE.contains(processType)) {
                String isSubmitHM = processClient.judgeCanSubmitAOProcessingTask(appId);
                if(isSubmitHM.equals(NO)){
                    crudActionType = CRUD_ACTION_TYPE_PREPARE;
                }
                ParamUtil.setRequestAttr(bpc.request, "isSubmitHM", isSubmitHM);
            }
        } else {
            throw new IaisRuntimeException("Invalid action type");
        }
        ParamUtil.setSessionAttr(request, KEY_MOH_PROCESS_DTO, mohProcessDto);
        ParamUtil.setRequestAttr(request, KEY_CRUD_ACTION_TYPE, crudActionType);
    }


    private String commonValidate(HttpServletRequest request, MohProcessDto mohProcessDto, String functionName) {
        String crudActionType;//validation
        PageAppEditSelectDto pageAppEditSelectDto = (PageAppEditSelectDto) ParamUtil.getSessionAttr(request, KEY_PAGE_APP_EDIT_SELECT_DTO);
        ValidationResultDto validationResultDto = processClient.validateMohProcessDto(mohProcessDto, functionName);
        rfiService.validateRfiSelection(mohProcessDto.getProcessingDecision(), validationResultDto, pageAppEditSelectDto, null);
        if (!validationResultDto.isPass()) {
            ParamUtil.setRequestAttr(request, TAB_ACTIVE, ModuleCommonConstants.TAB_PROCESSING);
            ParamUtil.setRequestAttr(request, KEY_VALIDATION_ERRORS, validationResultDto.toErrorMsg());
            crudActionType = CRUD_ACTION_TYPE_PREPARE;
        } else {
            crudActionType = CRUD_ACTION_TYPE_PROCESS;
        }
        return crudActionType;
    }

    public void setAFCAndInspectionReportDataRequest(HttpServletRequest request, String appId) {
        //todo judge appType and process type whether renew defer
        ResponseDto<ReviewAFCReportDto> responseDto = processClient.getCertificationReportByMainAppId(appId);
        if (responseDto.ok()) {
            ReviewAFCReportDto reviewAFCReportDto = responseDto.getEntity();
            ParamUtil.setRequestAttr(request, KEY_REVIEW_AFC_REPORT_DTO, reviewAFCReportDto);
            insAFCReportService.setSavedDocMap(reviewAFCReportDto, request);
        } else {
            ParamUtil.setRequestAttr(request, KEY_REVIEW_AFC_REPORT_DTO, new ReviewAFCReportDto());
        }
        ResponseDto<ReportDto> response = processClient.getFinalInsReportData(appId);
        if (response.ok()) {
            ParamUtil.setRequestAttr(request, KEY_INS_REPORT, response.getEntity());
        } else {
            ParamUtil.setRequestAttr(request, KEY_INS_REPORT, null);
        }
    }

    public void reqObjMappingMohProcess(HttpServletRequest request, String functionName, MohProcessDto mohProcessDto) {
        mohProcessDto.setRemarks(ParamUtil.getString(request, KEY_REMARKS));
        mohProcessDto.setProcessingDecision(ParamUtil.getString(request, KEY_PROCESSING_DECISION));

        FacilityDetailsInfo facilityDetailsInfo = mohProcessDto.getFacilityDetailsInfo();
        String applicationType = mohProcessDto.getSubmissionDetailsInfo().getApplicationType();
        String processType = mohProcessDto.getSubmissionDetailsInfo().getApplicationSubType();

        switch (functionName) {
            case FUNCTION_DO_SCREENING: {
                mohProcessDto.setSelectMohUser(ParamUtil.getString(request, KEY_SELECT_MOH_USER));
                mohProcessDto.setCertificationRequired(ParamUtil.getString(request, KEY_CERTIFICATION_REQUIRED));
                mohProcessDto.setInspectionRequired(ParamUtil.getString(request, KEY_INSPECTION_REQUIRED));
                mohProcessDto.setReasonForRejection(ParamUtil.getString(request, KEY_REASON_FOR_REJECTION));
                mohProcessDto.setCommentsToApplicant(ParamUtil.getString(request, KEY_COMMENTS_TO_APPLICANT));
                break;
            }
            case FUNCTION_AO_SCREENING: {
                mohProcessDto.setSelectMohUser(ParamUtil.getString(request, KEY_SELECT_MOH_USER));
                mohProcessDto.setReasonForRejection(ParamUtil.getString(request, KEY_REASON_FOR_REJECTION));
                mohProcessDto.setAoRecommendation(ParamUtil.getString(request, KEY_AO_RECOMMENDATION));
                break;
            }
            case FUNCTION_DO_RECOMMENDATION:
                boolean recommendApprove = applicationType.equals(MasterCodeConstants.APP_TYPE_RFC) || applicationType.equals(MasterCodeConstants.APP_TYPE_RENEW);
                List<AdeFacilityActivityInfo> adeFacilityActivityInfoList = facilityDetailsInfo.getActivities();
                if (!CollectionUtils.isEmpty(adeFacilityActivityInfoList)) {
                    for (AdeFacilityActivityInfo adeFacilityActivityInfo : adeFacilityActivityInfoList) {
                        if (recommendApprove) {
                            adeFacilityActivityInfo.setRecommendApprove(Boolean.TRUE);
                        } else {
                            String checked = ParamUtil.getString(request, adeFacilityActivityInfo.getActivityType());
                            adeFacilityActivityInfo.setRecommendApprove(YES.equals(checked));
                        }
                    }
                }
                List<AdeBatInfo> atpBats = facilityDetailsInfo.getAtpBats();
                if (!CollectionUtils.isEmpty(atpBats)) {
                    for (AdeBatInfo adeBatInfo : atpBats) {
                        String checked = ParamUtil.getString(request, adeBatInfo.getActivityType() + SEPARATOR + adeBatInfo.getBatCode());
                        if (recommendApprove) {
                            adeBatInfo.setRecommendApprove(Boolean.TRUE);
                        } else {
                            adeBatInfo.setRecommendApprove(YES.equals(checked));
                        }
                    }
                }
                List<AdeBatInfo> lspBats = facilityDetailsInfo.getLspBats();
                if (!CollectionUtils.isEmpty(lspBats)) {
                    for (AdeBatInfo adeBatInfo : lspBats) {
                        String checked = ParamUtil.getString(request, adeBatInfo.getActivityType() + SEPARATOR + adeBatInfo.getBatCode());
                        if (recommendApprove) {
                            adeBatInfo.setRecommendApprove(Boolean.TRUE);
                        } else {
                            adeBatInfo.setRecommendApprove(YES.equals(checked));
                        }
                    }
                }
                List<AdeBatInfo> sathBats = facilityDetailsInfo.getSathBats();
                if (!CollectionUtils.isEmpty(sathBats)) {
                    for (AdeBatInfo adeBatInfo : sathBats) {
                        String checked = ParamUtil.getString(request, adeBatInfo.getActivityType() + SEPARATOR + adeBatInfo.getBatCode());
                        if (recommendApprove) {
                            adeBatInfo.setRecommendApprove(Boolean.TRUE);
                        } else {
                            adeBatInfo.setRecommendApprove(YES.equals(checked));
                        }
                    }
                }
                if (PROCESS_TYPE_FAC_REG.equals(processType) || PROCESS_TYPE_APPROVAL_FOR_FACILITY_ACTIVITY_TYPE.equals(processType)) {
                    mohProcessDto.setFacValidityEndDate(ParamUtil.getString(request, KEY_FAC_VALIDITY_END_DATE));
                }
                mohProcessDto.setSelectMohUser(ParamUtil.getString(request, KEY_SELECT_MOH_USER));
                mohProcessDto.setReasonForRejection(ParamUtil.getString(request, KEY_REASON_FOR_REJECTION));
                mohProcessDto.setCommentsToApplicant(ParamUtil.getString(request, KEY_COMMENTS_TO_APPLICANT));
                break;
            case FUNCTION_AO_APPROVAL:
                mohProcessDto.setSelectMohUser(ParamUtil.getString(request, KEY_SELECT_MOH_USER));
                mohProcessDto.setReasonForRejection(ParamUtil.getString(request, KEY_REASON_FOR_REJECTION));
                mohProcessDto.setAoRecommendation(ParamUtil.getString(request, KEY_AO_RECOMMENDATION));
                break;
            default:
                log.info("don't have such functionName {}", StringUtils.normalizeSpace(functionName));
                break;
        }
    }

    /***************************************** DO Verification ****************************************************************/
    public DOVerificationDto getDOVerificationDto(HttpServletRequest request, String applicationId) {
        DOVerificationDto dto = (DOVerificationDto) ParamUtil.getSessionAttr(request, KEY_DO_VERIFICATION_DTO);
        if (dto == null) {
            dto = processClient.getDOVerificationByAppId(applicationId).getEntity();
        }
        return dto;
    }

    public void reqObjMappingDOVerification(HttpServletRequest request, DOVerificationDto doVerificationDto){
        doVerificationDto.setRemarks(ParamUtil.getString(request, KEY_REMARKS));
        doVerificationDto.setProcessingDecision(ParamUtil.getString(request, KEY_PROCESSING_DECISION));
        doVerificationDto.setCommentsToApplicant(ParamUtil.getString(request, KEY_COMMENTS_TO_APPLICANT));

        FacilityDetailsInfo facilityDetailsInfo = doVerificationDto.getFacilityDetailsInfo();
        String facilityActivityType = facilityDetailsInfo.getRfFacilityActivityType();
        if (ACTIVITY_SP_HANDLE_PV_POTENTIAL.equals(facilityActivityType)) {
            // this activity type can apply multi facility
            List<AdeFacilityInfo> adeFacilityInfoList = facilityDetailsInfo.getRfFacilities();
            for (AdeFacilityInfo adeFacilityInfo : adeFacilityInfoList) {
                String checked = ParamUtil.getString(request, adeFacilityInfo.getFacilityCode());
                adeFacilityInfo.setRecommendApprove(YES.equals(checked));
            }
        }
    }
}

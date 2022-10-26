package sg.gov.moh.iais.egp.bsb.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.role.RoleConsts;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.utils.LogUtil;
import com.ecquaria.cloud.moh.iais.common.utils.MaskUtil;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.google.common.collect.Maps;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import sg.gov.moh.iais.egp.bsb.client.InspectionClient;
import sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants;
import sg.gov.moh.iais.egp.bsb.constant.ValidationConstants;
import sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants;
import sg.gov.moh.iais.egp.bsb.dto.ProcessHistoryDto;
import sg.gov.moh.iais.egp.bsb.dto.inspection.InsCertificationInitDataDto;
import sg.gov.moh.iais.egp.bsb.dto.inspection.InsProcessDto;
import sg.gov.moh.iais.egp.bsb.dto.inspection.afc.AFCCommonDocDto;
import sg.gov.moh.iais.egp.bsb.dto.inspection.afc.CertificationDocDisPlayDto;
import sg.gov.moh.iais.egp.bsb.dto.inspection.afc.ReviewAFCReportDto;
import sg.gov.moh.iais.egp.bsb.dto.validation.ValidationResultDto;
import sg.gov.moh.iais.egp.bsb.service.DocSettingService;
import sg.gov.moh.iais.egp.bsb.service.DualDocSortingService;
import sg.gov.moh.iais.egp.bsb.service.InsAFCReportService;
import sg.gov.moh.iais.egp.bsb.util.MaskHelper;
import sg.gov.moh.iais.egp.bsb.util.mastercode.MasterCodeHolder;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.ecquaria.cloud.moh.iais.common.constant.BsbAuditTrailConstants.FUNCTION_DO_REVIEW_INSPECTION_REPORT_CERTIFICATION;
import static com.ecquaria.cloud.moh.iais.common.constant.BsbAuditTrailConstants.MODULE_INSPECTION;
import static sg.gov.moh.iais.egp.bsb.constant.DocConstants.KEY_COMMON_DOC_DTO;
import static sg.gov.moh.iais.egp.bsb.constant.DocConstants.PARAM_REPO_ID_DOC_MAP;
import static sg.gov.moh.iais.egp.bsb.constant.module.InspectionConstants.KEY_AFC_DASHBOARD_MSG;
import static sg.gov.moh.iais.egp.bsb.constant.module.InspectionConstants.KEY_APP_ID;
import static sg.gov.moh.iais.egp.bsb.constant.module.InspectionConstants.KEY_DASHBOARD_MSG;
import static sg.gov.moh.iais.egp.bsb.constant.module.InspectionConstants.KEY_INS_DECISION;
import static sg.gov.moh.iais.egp.bsb.constant.module.InspectionConstants.KEY_REVIEW_AFC_REPORT_DTO;
import static sg.gov.moh.iais.egp.bsb.constant.module.InspectionConstants.KEY_ROUTE;
import static sg.gov.moh.iais.egp.bsb.constant.module.InspectionConstants.KEY_TASK_ID;
import static sg.gov.moh.iais.egp.bsb.constant.module.InspectionConstants.PARAM_CAN_ACTION_ROLE;
import static sg.gov.moh.iais.egp.bsb.constant.module.InspectionConstants.TAB_ACTIVE;
import static sg.gov.moh.iais.egp.bsb.constant.module.InspectionConstants.TAB_CERTIFICATION;
import static sg.gov.moh.iais.egp.bsb.constant.module.InspectionConstants.TAB_PROCESSING;
import static sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants.INTERNAL_DOCUMENT_TYPE_LIST;
import static sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants.KEY_ACTION_TYPE;
import static sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants.KEY_FACILITY_DETAILS_INFO;
import static sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants.KEY_ROUTING_HISTORY_LIST;
import static sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants.KEY_SELECT_ROUTE_TO_MOH;
import static sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants.KEY_SUBMISSION_DETAILS_INFO;
import static sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants.KEY_VALIDATION_ERRORS;


@Slf4j
@Delegator("insDoCertificationDelegator")
@RequiredArgsConstructor
public class InspectionDoCertificationDelegator {

    private final InspectionClient inspectionClient;
    private final DocSettingService docSettingService;
    private final InsAFCReportService insAFCReportService;
    private final DualDocSortingService dualDocSortingService;

    public void start(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        HttpSession session = request.getSession();
        MaskHelper.taskProcessUnmask(request, KEY_APP_ID, KEY_TASK_ID);
        DualDocSortingService.clearDualDocSortingInfo(session);
        session.removeAttribute(KEY_REVIEW_AFC_REPORT_DTO);
        session.removeAttribute(KEY_COMMON_DOC_DTO);
        session.removeAttribute(PARAM_REPO_ID_DOC_MAP);
        session.removeAttribute("ValidSave");
        AuditTrailHelper.auditFunction(MODULE_INSPECTION, FUNCTION_DO_REVIEW_INSPECTION_REPORT_CERTIFICATION);
    }

    public void init(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        HttpSession session = request.getSession();
        session.removeAttribute(KEY_SUBMISSION_DETAILS_INFO);
        session.removeAttribute(KEY_FACILITY_DETAILS_INFO);
        session.removeAttribute(KEY_ROUTING_HISTORY_LIST);
        session.removeAttribute(KEY_INS_DECISION);
        session.removeAttribute(KEY_SELECT_ROUTE_TO_MOH);
        String appId = (String) ParamUtil.getSessionAttr(request, KEY_APP_ID);
        //
        InsCertificationInitDataDto initDataDto = inspectionClient.getInsCertificationInitDataDto(appId);
        // submission details info
        ParamUtil.setSessionAttr(request, KEY_SUBMISSION_DETAILS_INFO, initDataDto.getSubmissionDetailsInfo());
        // facility details
        ParamUtil.setSessionAttr(request, KEY_FACILITY_DETAILS_INFO, initDataDto.getFacilityDetailsInfo());
        // show routingHistory list
        ArrayList<ProcessHistoryDto> processHistoryDtoList = new ArrayList<>(initDataDto.getProcessHistoryDtoList());
        ParamUtil.setSessionAttr(request, KEY_ROUTING_HISTORY_LIST, processHistoryDtoList);

        ArrayList<SelectOption> selectRouteToMoh = new ArrayList<>(initDataDto.getSelectRouteToMoh());
        ParamUtil.setSessionAttr(request,KEY_SELECT_ROUTE_TO_MOH,selectRouteToMoh);

        // inspection processing
        InsProcessDto processDto = new InsProcessDto();
        ParamUtil.setSessionAttr(request, KEY_INS_DECISION, processDto);
    }

    @SuppressWarnings("unchecked")
    public void prepareDate(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        String appId = (String) ParamUtil.getSessionAttr(request, KEY_APP_ID);

        dualDocSortingService.retrieveDualDocDisplayDtoIntoRequest(request, appId);

        //set internal doc types
        ParamUtil.setRequestAttr(request, INTERNAL_DOCUMENT_TYPE_LIST, docSettingService.getInternalDocTypeList());
        //
        ReviewAFCReportDto dto = insAFCReportService.getDisplayDto(request);
        AFCCommonDocDto commonDocDto = insAFCReportService.getAFCCommonDocDto(request);
        //
        List<CertificationDocDisPlayDto> certificationDocDisPlayDtos;
        Map<String, CertificationDocDisPlayDto> docDtoMap = (Map<String, CertificationDocDisPlayDto>) ParamUtil.getSessionAttr(request,PARAM_REPO_ID_DOC_MAP);
        if(CollectionUtils.isEmpty(docDtoMap)){
            certificationDocDisPlayDtos = dto.getCertificationDocDisPlayDtos();
            if(certificationDocDisPlayDtos==null){
                certificationDocDisPlayDtos = new ArrayList<>(0);
            }
            for (CertificationDocDisPlayDto certificationDocDisPlayDto : certificationDocDisPlayDtos) {
                certificationDocDisPlayDto.setMaskedRepoId(MaskUtil.maskValue("file", certificationDocDisPlayDto.getRepoId()));
            }
            dto.setCertificationDocDisPlayDtos(certificationDocDisPlayDtos);
            insAFCReportService.setSavedDocMap(dto, request);
        }else {
            certificationDocDisPlayDtos = new ArrayList<>(docDtoMap.values());
            dto.setCertificationDocDisPlayDtos(certificationDocDisPlayDtos);
        }
        ParamUtil.setSessionAttr(request, KEY_COMMON_DOC_DTO, commonDocDto);
        ParamUtil.setRequestAttr(request, KEY_DASHBOARD_MSG, KEY_AFC_DASHBOARD_MSG);
        ParamUtil.setRequestAttr(request, PARAM_CAN_ACTION_ROLE, RoleConsts.USER_ROLE_BSB_DO);
        ParamUtil.setSessionAttr(request, KEY_REVIEW_AFC_REPORT_DTO, dto);

        List<SelectOption> docTypeOps = MasterCodeHolder.DOCUMENT_TYPE.customOptions(docSettingService.getAfcCertificationUploadDocTypeList().toArray(new String[0]));
        ParamUtil.setRequestAttr(request, "docTypeOps", docTypeOps);
    }

    public void bindAction(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        DualDocSortingService.readDualDocSortingInfo(request);

        String actionType = ParamUtil.getString(request, KEY_ACTION_TYPE);
        ParamUtil.setRequestAttr(request, KEY_ACTION_TYPE, actionType);
    }

    public void handleSaveReport(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        InsProcessDto processDto = (InsProcessDto) ParamUtil.getSessionAttr(request, KEY_INS_DECISION);
        processDto.reqObjMapping(request);
        ParamUtil.setSessionAttr(request, KEY_INS_DECISION, processDto);

        AFCCommonDocDto commonDocDto = insAFCReportService.getAFCCommonDocDto(request);
        commonDocDto.reqObjMapping(request);
        ParamUtil.setSessionAttr(request, KEY_COMMON_DOC_DTO, commonDocDto);

        insAFCReportService.validateAndSaveReport(request, RoleConsts.USER_ROLE_BSB_DO, true);
    }

    /**
     * When it is the 5th round of review, system will display a message information for the Duty Officer,
     * Approving Officer and Applicant that this is the final round of review.
     * Pop up message will be triggered upon clicking on “Submit” button, users will need to “Confirm” before
     * clicking on “Submit” again
     */
    public void handleSubmit(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        InsProcessDto processDto = (InsProcessDto) ParamUtil.getSessionAttr(request, KEY_INS_DECISION);
        processDto.reqObjMapping(request);
        ParamUtil.setSessionAttr(request, KEY_INS_DECISION, processDto);

        ValidationResultDto validationProcessDto = inspectionClient.validateDoCertification(processDto);
        String validateResult;
        if (validationProcessDto.isPass()) {
            if (MasterCodeConstants.MOH_PROCESS_DECISION_ROUTE_TO_AO.equals(processDto.getDecision())) {
                validateResult = "routeToAO";
                validateResult = insAFCReportService.popupMsgDisplayJudgement(request,validateResult);
            } else {
                validateResult = "invalid";
            }
        } else {
            log.error("Validation failure info: {}", validationProcessDto.toErrorMsg());
            ParamUtil.setRequestAttr(request, KEY_VALIDATION_ERRORS, validationProcessDto.toErrorMsg());
            ParamUtil.setRequestAttr(request, TAB_ACTIVE, TAB_PROCESSING);
            validateResult = "back";
        }
        String valid = (String) ParamUtil.getSessionAttr(request,"ValidSave");
        // 'valid' is null mean user do not click on save report button
        if(StringUtils.isEmpty(valid)){
            // Verify that the user whether submitted a file before
            // If the user has submitted a file, but has not submitted the form,
            // and the second entry requires verification of the previously submitted file
            insAFCReportService.validateAndSaveReport(request, RoleConsts.USER_ROLE_BSB_DO, false);
            String errorMsg = (String) ParamUtil.getRequestAttr(request, ValidationConstants.KEY_VALIDATION_ERRORS);
            ReviewAFCReportDto dto = insAFCReportService.getDisplayDto(request);
            if (StringUtils.hasLength(errorMsg)) {
                validateResult = "back";
                ParamUtil.setRequestAttr(request, TAB_ACTIVE, TAB_CERTIFICATION);
            } else if (dto.isActionOnOld()) {
                HashMap<String, String> errorMap = Maps.newHashMapWithExpectedSize(1);
                errorMap.put("chooseOneAction","Must execute one of the following: Upload a new file or mark the previous file as final.");
                ParamUtil.setRequestAttr(request, ValidationConstants.KEY_VALIDATION_ERRORS, ValidationResultDto.toErrorMsg(errorMap));
                validateResult = "back";
                ParamUtil.setRequestAttr(request, TAB_ACTIVE, TAB_CERTIFICATION);
            }
        }
        log.info("Officer submit decision [{}] for review inspection report, route result [{}]", LogUtil.escapeCrlf(processDto.getDecision()), validateResult);
        ParamUtil.setRequestAttr(request, KEY_ROUTE, validateResult);
    }

    public void routeToAO(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        String appId = (String) ParamUtil.getSessionAttr(request, KEY_APP_ID);
        String taskId = (String) ParamUtil.getSessionAttr(request, KEY_TASK_ID);
        InsProcessDto processDto = (InsProcessDto) ParamUtil.getSessionAttr(request, KEY_INS_DECISION);
        inspectionClient.inspectionDoCertificationToAO(appId, taskId, processDto);
        ParamUtil.setRequestAttr(request, ModuleCommonConstants.RESULT_MSG, "BISACKINS003");
    }

    public void skip(BaseProcessClass bpc) {
        throw new UnsupportedOperationException("Skip of certification is not implemented");
    }
}

package sg.gov.moh.iais.egp.bsb.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.AuditTrailConsts;
import com.ecquaria.cloud.moh.iais.common.utils.LogUtil;
import com.ecquaria.cloud.moh.iais.common.utils.MaskUtil;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import sg.gov.moh.iais.egp.bsb.client.InspectionAFCClient;
import sg.gov.moh.iais.egp.bsb.client.InspectionClient;
import sg.gov.moh.iais.egp.bsb.client.InternalDocClient;
import sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants;
import sg.gov.moh.iais.egp.bsb.constant.RoleConstants;
import sg.gov.moh.iais.egp.bsb.constant.module.AppViewConstants;
import sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants;
import sg.gov.moh.iais.egp.bsb.dto.ProcessHistoryDto;
import sg.gov.moh.iais.egp.bsb.dto.ResponseDto;
import sg.gov.moh.iais.egp.bsb.dto.file.DocDisplayDto;
import sg.gov.moh.iais.egp.bsb.dto.inspection.InsCertificationInitDataDto;
import sg.gov.moh.iais.egp.bsb.dto.inspection.InsProcessDto;
import sg.gov.moh.iais.egp.bsb.dto.inspection.afc.AFCCommonDocDto;
import sg.gov.moh.iais.egp.bsb.dto.inspection.afc.CertificationDocDisPlayDto;
import sg.gov.moh.iais.egp.bsb.dto.inspection.afc.ReviewAFCReportDto;
import sg.gov.moh.iais.egp.bsb.dto.validation.ValidationResultDto;
import sg.gov.moh.iais.egp.bsb.service.InsAFCReportService;
import sg.gov.moh.iais.egp.bsb.util.DateUtil;
import sg.gov.moh.iais.egp.bsb.util.MaskHelper;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static sg.gov.moh.iais.egp.bsb.constant.DocConstants.KEY_COMMON_DOC_DTO;
import static sg.gov.moh.iais.egp.bsb.constant.DocConstants.PARAM_REPO_ID_DOC_MAP;
import static sg.gov.moh.iais.egp.bsb.constant.module.InspectionConstants.*;
import static sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants.*;
import static sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants.KEY_VALIDATION_ERRORS;
@Slf4j
@Delegator("insAoCertificationDelegator")
public class InspectionAoCertificationDelegator {
    private final InspectionClient inspectionClient;
    private final InternalDocClient internalDocClient;
    private final InspectionAFCClient inspectionAFCClient;
    private final InsAFCReportService insAFCReportService;

    @Autowired
    public InspectionAoCertificationDelegator(InspectionClient inspectionClient,InsAFCReportService insAFCReportService, InspectionAFCClient inspectionAFCClient, InternalDocClient internalDocClient) {
        this.inspectionClient = inspectionClient;
        this.internalDocClient = internalDocClient;
        this.inspectionAFCClient = inspectionAFCClient;
        this.insAFCReportService = insAFCReportService;
    }

    public void start(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        HttpSession session = request.getSession();
        MaskHelper.taskProcessUnmask(request, KEY_APP_ID, KEY_TASK_ID);
        session.removeAttribute(KEY_REVIEW_AFC_REPORT_DTO);
        session.removeAttribute(KEY_COMMON_DOC_DTO);
        session.removeAttribute(PARAM_REPO_ID_DOC_MAP);
        ParamUtil.setSessionAttr(request, "ValidSave", null);
        AuditTrailHelper.auditFunction(AuditTrailConsts.MODULE_INSPECTION, "Ao Review Inspection Report(Certification)");
    }

    public void init(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        HttpSession session = request.getSession();
        session.removeAttribute(KEY_SUBMISSION_DETAILS_INFO);
        session.removeAttribute(KEY_FACILITY_DETAILS_INFO);
        session.removeAttribute(KEY_ROUTING_HISTORY_LIST);
        session.removeAttribute(KEY_INS_DECISION);

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
        // show supportDocDisplayDto List
        ArrayList<DocDisplayDto> supportDocList = new ArrayList<>(initDataDto.getSupportDocDisplayDtoList());
        ParamUtil.setSessionAttr(request,KEY_TAB_DOCUMENT_SUPPORT_DOC_LIST,supportDocList);

        // inspection processing
        InsProcessDto processDto = new InsProcessDto();
        ParamUtil.setSessionAttr(request, KEY_INS_DECISION, processDto);
    }

    public void prepareDate(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        String appId = (String) ParamUtil.getSessionAttr(request, KEY_APP_ID);
        List<DocDisplayDto> internalDocDisplayDto = internalDocClient.getInternalDocForDisplay(appId);
        ParamUtil.setRequestAttr(request, KEY_TAB_DOCUMENT_INTERNAL_DOC_LIST, internalDocDisplayDto);

        ReviewAFCReportDto dto = insAFCReportService.getDisplayDto(request);
        AFCCommonDocDto commonDocDto = insAFCReportService.getAFCCommonDocDto(request);
        if (!StringUtils.hasLength(dto.getAppId())) {

            ResponseDto<ReviewAFCReportDto> responseDto = inspectionAFCClient.getReviewAFCReportDto(appId);
            if (responseDto.ok()) {
                dto = responseDto.getEntity();
            } else {
                log.warn("get AFC API doesn't return ok, the response is {}", responseDto);
                ParamUtil.setSessionAttr(request, KEY_REVIEW_AFC_REPORT_DTO, new ReviewAFCReportDto());
            }
        }
        List<CertificationDocDisPlayDto> certificationDocDisPlayDtos;
        Map<String, CertificationDocDisPlayDto> docDtoMap = (Map<String, CertificationDocDisPlayDto>) ParamUtil.getSessionAttr(request,PARAM_REPO_ID_DOC_MAP);
        if(StringUtils.isEmpty(docDtoMap)){
            certificationDocDisPlayDtos = dto.getCertificationDocDisPlayDtos();
            if(certificationDocDisPlayDtos==null){
                certificationDocDisPlayDtos = new ArrayList<>(0);
            }
            for (int i=0; i<certificationDocDisPlayDtos.size();i++){
                certificationDocDisPlayDtos.get(i).setMaskedRepoId(MaskUtil.maskValue("file",certificationDocDisPlayDtos.get(i).getRepoId()));
            }
            dto.setCertificationDocDisPlayDtos(certificationDocDisPlayDtos);
            insAFCReportService.setSavedDocMap(dto, request);
        }else {
            certificationDocDisPlayDtos = new ArrayList<>(docDtoMap.values());
            dto.setCertificationDocDisPlayDtos(certificationDocDisPlayDtos);
        }
        ParamUtil.setSessionAttr(request, KEY_COMMON_DOC_DTO, commonDocDto);
        ParamUtil.setRequestAttr(request, KEY_DASHBOARD_MSG, KEY_AFC_DASHBOARD_MSG);
        ParamUtil.setRequestAttr(request, PARAM_CAN_ACTION_ROLE, RoleConstants.ROLE_BSB_AO);
        ParamUtil.setSessionAttr(request, KEY_REVIEW_AFC_REPORT_DTO, dto);

        // view application need appId and moduleType
        ParamUtil.setRequestAttr(request, AppViewConstants.MASK_PARAM_APP_ID, appId);
        ParamUtil.setRequestAttr(request, AppViewConstants.MASK_PARAM_APP_VIEW_MODULE_TYPE, AppViewConstants.MODULE_VIEW_NEW_FACILITY);
    }

    public void bingAction(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        ReviewAFCReportDto dto = insAFCReportService.getDisplayDto(request);
        AFCCommonDocDto commonDocDto = insAFCReportService.getAFCCommonDocDto(request);
        commonDocDto.reqObjMapping(request);
        String actionValue = ParamUtil.getString(request, ModuleCommonConstants.KEY_ACTION_VALUE);
        if (actionValue.equals("savaDoc")) {
            ParamUtil.setRequestAttr(request, ModuleCommonConstants.KEY_ACTION_TYPE, "saveReport");
        } else if (actionValue.equals("submitDoc")) {
            ParamUtil.setRequestAttr(request, ModuleCommonConstants.KEY_ACTION_TYPE, "submit");
        }else if(actionValue.equals("uploadDoc")){
            ParamUtil.setRequestAttr(request, ModuleCommonConstants.KEY_ACTION_TYPE, "upload");
        }
        ParamUtil.setSessionAttr(request, KEY_COMMON_DOC_DTO, commonDocDto);
        ParamUtil.setSessionAttr(request, KEY_REVIEW_AFC_REPORT_DTO, dto);
    }

    public void handleSaveReport(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        InsProcessDto processDto = (InsProcessDto) ParamUtil.getSessionAttr(request, KEY_INS_DECISION);
        processDto.reqObjMapping(request);
        ParamUtil.setSessionAttr(request, KEY_INS_DECISION, processDto);

        AFCCommonDocDto commonDocDto = insAFCReportService.getAFCCommonDocDto(request);
        commonDocDto.reqObjMapping(request);
        ParamUtil.setSessionAttr(request, KEY_COMMON_DOC_DTO, commonDocDto);
        String actionValue = ParamUtil.getString(request, ModuleCommonConstants.KEY_ACTION_VALUE);
        if (actionValue.equals("savaDoc")) {
            insAFCReportService.validateAo(request);
        }
    }

    public void handleSubmit(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        InsProcessDto processDto = (InsProcessDto) ParamUtil.getSessionAttr(request, KEY_INS_DECISION);
        processDto.reqObjMapping(request);
        ParamUtil.setSessionAttr(request, KEY_INS_DECISION, processDto);

        ValidationResultDto validationProcessDto = inspectionClient.validateDoCertification(processDto);
        String validateResult;
        if (validationProcessDto.isPass()) {
            if (MasterCodeConstants.MOH_PROCESSING_DECISION_ROUTE_BACK_TO_DO.equals(processDto.getDecision())) {
                validateResult = "routeBack";
            } else if (MasterCodeConstants.MOH_PROCESSING_DECISION_APPROVE.equals(processDto.getDecision())) {
                validateResult = "approve";
            } else if (MasterCodeConstants.MOH_PROCESSING_DECISION_SKIP_INSPECTION.equals(processDto.getDecision())) {
                validateResult = "skip";
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
        if(StringUtils.isEmpty(valid)){
            validateResult = "back";
            Map<String, String> errMap = validationProcessDto.getErrorMap();
            if(CollectionUtils.isEmpty(errMap)){
                errMap = Maps.newHashMapWithExpectedSize(1);
            }
            errMap.put("chooseOne","Must execute one of the following: Upload a new file or mark the previous file as final");
            validationProcessDto.setErrorMap((HashMap<String, String>) errMap);
            ParamUtil.setRequestAttr(request, KEY_VALIDATION_ERRORS, validationProcessDto.toErrorMsg());
        }
        log.info("Officer submit decision [{}] for review inspection report, route result [{}]", LogUtil.escapeCrlf(processDto.getDecision()), validateResult);
        ParamUtil.setRequestAttr(request, KEY_ROUTE, validateResult);
    }

    public void routeBackToDO(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        String appId = (String) ParamUtil.getSessionAttr(request, KEY_APP_ID);
        String taskId = (String) ParamUtil.getSessionAttr(request, KEY_TASK_ID);
        InsProcessDto insProcessDto = (InsProcessDto) ParamUtil.getSessionAttr(request, KEY_INS_DECISION);
        inspectionClient.inspectionAoCertificationToDO(appId, taskId, insProcessDto);
        ParamUtil.setRequestAttr(request, KEY_RESULT_MSG, "Your Certification has been sent to the Duty Officer on" + DateUtil.convertToString(LocalDate.now()) + ".");
    }

    public void skip(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        InsProcessDto processDto = (InsProcessDto) ParamUtil.getSessionAttr(request, KEY_INS_DECISION);
        String appId = (String) ParamUtil.getSessionAttr(request, KEY_APP_ID);
        String taskId = (String) ParamUtil.getSessionAttr(request, KEY_TASK_ID);
        inspectionClient.skipInspection(appId, taskId, processDto);
    }

    public void approve(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        String appId = (String) ParamUtil.getSessionAttr(request, KEY_APP_ID);
        String taskId = (String) ParamUtil.getSessionAttr(request, KEY_TASK_ID);
        InsProcessDto processDto = (InsProcessDto) ParamUtil.getSessionAttr(request, KEY_INS_DECISION);
        inspectionClient.inspectionCertificationApprove(appId, taskId, processDto);
        ParamUtil.setRequestAttr(request, KEY_RESULT_MSG, "You have successfully Certification Verified.");
    }
}

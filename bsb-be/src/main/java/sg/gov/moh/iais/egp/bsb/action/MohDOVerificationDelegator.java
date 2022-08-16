package sg.gov.moh.iais.egp.bsb.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import sg.gov.moh.iais.egp.bsb.client.InternalDocClient;
import sg.gov.moh.iais.egp.bsb.client.ProcessClient;
import sg.gov.moh.iais.egp.bsb.constant.TaskType;
import sg.gov.moh.iais.egp.bsb.constant.module.TaskModuleConstants;
import sg.gov.moh.iais.egp.bsb.dto.process.DOVerificationDto;
import sg.gov.moh.iais.egp.bsb.dto.rfi.RfiProcessDto;
import sg.gov.moh.iais.egp.bsb.dto.validation.ValidationResultDto;
import sg.gov.moh.iais.egp.bsb.service.AppViewService;
import sg.gov.moh.iais.egp.bsb.service.MohProcessService;
import sg.gov.moh.iais.egp.bsb.service.RfiService;
import sg.gov.moh.iais.egp.bsb.util.DocDisplayDtoUtil;
import sg.gov.moh.iais.egp.bsb.util.MaskHelper;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import java.io.Serializable;
import java.util.Map;

import static com.ecquaria.cloud.moh.iais.common.constant.BsbAuditTrailConstants.FUNCTION_DO_VERIFICATION;
import static com.ecquaria.cloud.moh.iais.common.constant.BsbAuditTrailConstants.FUNCTION_HM_APPROVAL;
import static com.ecquaria.cloud.moh.iais.common.constant.BsbAuditTrailConstants.MODULE_FACILITY_REGISTRATION;
import static sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants.MOH_PROCESS_DECISION_ACCEPT;
import static sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants.MOH_PROCESS_DECISION_REJECT;
import static sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants.MOH_PROCESS_DECISION_REQUEST_FOR_INFORMATION;
import static sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants.YES;
import static sg.gov.moh.iais.egp.bsb.constant.module.InspectionConstants.KEY_APP_ID;
import static sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants.KEY_CRUD_ACTION_TYPE;
import static sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants.KEY_DOC_DISPLAY_DTO_REPO_ID_NAME_MAP;
import static sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants.KEY_RF_FACILITY_DETAILS_INFO;
import static sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants.KEY_ROUTING_HISTORY_LIST;
import static sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants.KEY_SUBMISSION_DETAILS_INFO;
import static sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants.KEY_TAB_DOCUMENT_INTERNAL_DOC_LIST;
import static sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants.KEY_TAB_DOCUMENT_SUPPORT_DOC_LIST;
import static sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants.KEY_VALIDATION_ERRORS;
import static sg.gov.moh.iais.egp.bsb.constant.module.ProcessContants.CRUD_ACTION_TYPE_ACCEPT;
import static sg.gov.moh.iais.egp.bsb.constant.module.ProcessContants.CRUD_ACTION_TYPE_PREPARE_DATA;
import static sg.gov.moh.iais.egp.bsb.constant.module.ProcessContants.CRUD_ACTION_TYPE_REJECT;
import static sg.gov.moh.iais.egp.bsb.constant.module.ProcessContants.CRUD_ACTION_TYPE_REQUEST_FOR_INFORMATION;
import static sg.gov.moh.iais.egp.bsb.constant.module.ProcessContants.KEY_DO_VERIFICATION_DTO;
import static sg.gov.moh.iais.egp.bsb.constant.module.ProcessContants.MOH_PROCESS_PAGE_VALIDATION;
import static sg.gov.moh.iais.egp.bsb.constant.module.RfiConstants.KEY_RFI_PROCESS_DTO;
import static sg.gov.moh.iais.egp.bsb.constant.module.TaskModuleConstants.PARAM_NAME_APP_ID;
import static sg.gov.moh.iais.egp.bsb.constant.module.TaskModuleConstants.PARAM_NAME_TASK_ID;


@Slf4j
@Delegator("doVerificationDelegator")
public class MohDOVerificationDelegator {
    private final ProcessClient processClient;
    private final InternalDocClient internalDocClient;
    private final MohProcessService mohProcessService;
    private final AppViewService appViewService;
    private final RfiService rfiService;

    @Autowired
    public MohDOVerificationDelegator(ProcessClient processClient, InternalDocClient internalDocClient,
                                      MohProcessService mohProcessService, AppViewService appViewService, RfiService rfiService) {
        this.processClient = processClient;
        this.internalDocClient = internalDocClient;
        this.mohProcessService = mohProcessService;
        this.appViewService = appViewService;
        this.rfiService = rfiService;
    }

    public void start(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        HttpSession session = request.getSession();
        session.removeAttribute(KEY_DO_VERIFICATION_DTO);
        session.removeAttribute(KEY_RFI_PROCESS_DTO);
        MaskHelper.taskProcessUnmask(request, PARAM_NAME_APP_ID, PARAM_NAME_TASK_ID);
        AuditTrailHelper.auditFunction(MODULE_FACILITY_REGISTRATION, FUNCTION_DO_VERIFICATION);
    }

    public void prepareData(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        String appId = (String) ParamUtil.getSessionAttr(request, PARAM_NAME_APP_ID);
        DOVerificationDto doVerificationDto = mohProcessService.getDOVerificationDto(request, appId);
        ParamUtil.setSessionAttr(request, KEY_DO_VERIFICATION_DTO, doVerificationDto);
        // show data
        ParamUtil.setRequestAttr(request, KEY_SUBMISSION_DETAILS_INFO, doVerificationDto.getSubmissionDetailsInfo());
        ParamUtil.setRequestAttr(request, KEY_RF_FACILITY_DETAILS_INFO, doVerificationDto.getRfFacilityDetailsInfo());
        // show applicant support doc
        ParamUtil.setRequestAttr(request, KEY_TAB_DOCUMENT_SUPPORT_DOC_LIST, doVerificationDto.getSupportDocDisplayDtoList());
        // provide for download support doc
        Map<String, String> repoIdDocNameMap = DocDisplayDtoUtil.getRepoIdDocNameMap(doVerificationDto.getSupportDocDisplayDtoList());
        ParamUtil.setSessionAttr(request, KEY_DOC_DISPLAY_DTO_REPO_ID_NAME_MAP, (Serializable) repoIdDocNameMap);
        // show internal doc
        ParamUtil.setRequestAttr(request, KEY_TAB_DOCUMENT_INTERNAL_DOC_LIST, internalDocClient.getInternalDocForDisplay(appId));
        // show routingHistory list
        ParamUtil.setRequestAttr(request, KEY_ROUTING_HISTORY_LIST, doVerificationDto.getProcessHistoryDtoList());
        // view application
        AppViewService.facilityRegistrationViewApp(request, appId, TaskType.DO_VERIFICATION);
    }

    public void prepareSwitch(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        DOVerificationDto doVerificationDto = (DOVerificationDto) ParamUtil.getSessionAttr(request, KEY_DO_VERIFICATION_DTO);
        mohProcessService.reqObjMappingDOVerification(request, doVerificationDto);
        ParamUtil.setSessionAttr(request, KEY_DO_VERIFICATION_DTO, doVerificationDto);
        // validation
        ValidationResultDto validationResultDto = processClient.validateDOVerificationDto(doVerificationDto);
        String crudActionType = "";
        if (!validationResultDto.isPass()) {
            ParamUtil.setRequestAttr(request, MOH_PROCESS_PAGE_VALIDATION, YES);
            ParamUtil.setRequestAttr(request, KEY_VALIDATION_ERRORS, validationResultDto.toErrorMsg());
            crudActionType = CRUD_ACTION_TYPE_PREPARE_DATA;
        } else {
            String processingDecision = doVerificationDto.getProcessingDecision();
            switch (processingDecision) {
                case MOH_PROCESS_DECISION_ACCEPT:
                    crudActionType = CRUD_ACTION_TYPE_ACCEPT;
                    break;
                case MOH_PROCESS_DECISION_REJECT:
                    crudActionType = CRUD_ACTION_TYPE_REJECT;
                    break;
                case MOH_PROCESS_DECISION_REQUEST_FOR_INFORMATION:
                    crudActionType = CRUD_ACTION_TYPE_REQUEST_FOR_INFORMATION;
                    break;
                default:
                    log.info("no such processingDecision {}", StringUtils.normalizeSpace(processingDecision));
                    break;
            }
        }
        ParamUtil.setRequestAttr(request, KEY_CRUD_ACTION_TYPE, crudActionType);
    }

    public void accept(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        String taskId = (String) ParamUtil.getSessionAttr(request, PARAM_NAME_TASK_ID);
        String appId = (String) ParamUtil.getSessionAttr(request, PARAM_NAME_APP_ID);
        DOVerificationDto doVerificationDto = (DOVerificationDto) ParamUtil.getSessionAttr(request, KEY_DO_VERIFICATION_DTO);
        processClient.saveDOVerificationAccept(appId, taskId, doVerificationDto);
        ParamUtil.setRequestAttr(request, TaskModuleConstants.KEY_CURRENT_TASK, FUNCTION_DO_VERIFICATION);
    }

    public void reject(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        String taskId = (String) ParamUtil.getSessionAttr(request, PARAM_NAME_TASK_ID);
        String appId = (String) ParamUtil.getSessionAttr(request, PARAM_NAME_APP_ID);
        DOVerificationDto doVerificationDto = (DOVerificationDto) ParamUtil.getSessionAttr(request, KEY_DO_VERIFICATION_DTO);
        processClient.saveDOVerificationReject(appId, taskId, doVerificationDto);
        ParamUtil.setRequestAttr(request, TaskModuleConstants.KEY_CURRENT_TASK, FUNCTION_DO_VERIFICATION);
    }

    public void requestForInformation(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        String taskId = (String) ParamUtil.getSessionAttr(request, PARAM_NAME_TASK_ID);
        String appId = (String) ParamUtil.getSessionAttr(request, PARAM_NAME_APP_ID);
        DOVerificationDto doVerificationDto = (DOVerificationDto) ParamUtil.getSessionAttr(request, KEY_DO_VERIFICATION_DTO);
        RfiProcessDto rfiProcessDto = (RfiProcessDto) ParamUtil.getSessionAttr(request, KEY_RFI_PROCESS_DTO);
        doVerificationDto.setRfiProcessDto(rfiProcessDto);
        processClient.saveDOVerificationRFI(appId, taskId, doVerificationDto);
        // TODO
    }

    public void prepareRfi(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        String appId = (String) ParamUtil.getSessionAttr(request, KEY_APP_ID);
        appViewService.retrieveFacReg(request, appId);
    }

    public void doRfi(BaseProcessClass bpc) {
        rfiService.doNewFacilityRfi(bpc.request);
    }
}

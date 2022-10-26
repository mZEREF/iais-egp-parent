package sg.gov.moh.iais.egp.bsb.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.exception.IaisRuntimeException;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import sg.gov.moh.iais.egp.bsb.client.AppSupportingDocClient;
import sg.gov.moh.iais.egp.bsb.client.InternalDocClient;
import sg.gov.moh.iais.egp.bsb.client.ProcessClient;
import sg.gov.moh.iais.egp.bsb.client.RfiClient;
import sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants;
import sg.gov.moh.iais.egp.bsb.dto.file.DocDisplayDto;
import sg.gov.moh.iais.egp.bsb.dto.process.DOVerificationDto;
import sg.gov.moh.iais.egp.bsb.dto.process.DualDocSortingDto;
import sg.gov.moh.iais.egp.bsb.dto.rfi.PageAppEditSelectDto;
import sg.gov.moh.iais.egp.bsb.dto.validation.ValidationResultDto;
import sg.gov.moh.iais.egp.bsb.service.DocSettingService;
import sg.gov.moh.iais.egp.bsb.service.DualDocSortingService;
import sg.gov.moh.iais.egp.bsb.service.MohProcessService;
import sg.gov.moh.iais.egp.bsb.service.RfiService;
import sg.gov.moh.iais.egp.bsb.util.DocDisplayDtoUtil;
import sg.gov.moh.iais.egp.bsb.util.MaskHelper;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import static com.ecquaria.cloud.moh.iais.common.constant.BsbAuditTrailConstants.FUNCTION_DO_VERIFICATION;
import static com.ecquaria.cloud.moh.iais.common.constant.BsbAuditTrailConstants.MODULE_FACILITY_REGISTRATION;
import static sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants.MOH_PROCESS_DECISION_ACCEPT;
import static sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants.MOH_PROCESS_DECISION_REJECT;
import static sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants.MOH_PROCESS_DECISION_REQUEST_FOR_INFORMATION;
import static sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants.INTERNAL_DOCUMENT_TYPE_LIST;
import static sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants.KEY_ACTION_TYPE;
import static sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants.KEY_CRUD_ACTION_TYPE;
import static sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants.KEY_DOC_DISPLAY_DTO_REPO_ID_NAME_MAP;
import static sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants.KEY_DUAL_DOC_SORTING_INFO;
import static sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants.KEY_FACILITY_DETAILS_INFO;
import static sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants.KEY_ROUTING_HISTORY_LIST;
import static sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants.KEY_SUBMISSION_DETAILS_INFO;
import static sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants.KEY_TAB_DOCUMENT_INTERNAL_DOC_LIST;
import static sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants.KEY_TAB_DOCUMENT_SUPPORT_DOC_LIST;
import static sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants.KEY_VALIDATION_ERRORS;
import static sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants.TAB_ACTIVE;
import static sg.gov.moh.iais.egp.bsb.constant.module.ProcessContants.CRUD_ACTION_TYPE_PREPARE;
import static sg.gov.moh.iais.egp.bsb.constant.module.ProcessContants.CRUD_ACTION_TYPE_PROCESS;
import static sg.gov.moh.iais.egp.bsb.constant.module.ProcessContants.KEY_DO_VERIFICATION_DTO;
import static sg.gov.moh.iais.egp.bsb.constant.module.RfiConstants.KEY_PAGE_APP_EDIT_SELECT_DTO;
import static sg.gov.moh.iais.egp.bsb.constant.module.TaskModuleConstants.PARAM_NAME_APP_ID;
import static sg.gov.moh.iais.egp.bsb.constant.module.TaskModuleConstants.PARAM_NAME_TASK_ID;


@Slf4j
@RequiredArgsConstructor
@Delegator("doVerificationDelegator")
public class MohDOVerificationDelegator {
    private final ProcessClient processClient;
    private final AppSupportingDocClient appSupportingDocClient;
    private final InternalDocClient internalDocClient;
    private final RfiService rfiService;
    private final MohProcessService mohProcessService;
    private final DocSettingService docSettingService;
    private final RfiClient rfiClient;

    public void start(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        HttpSession session = request.getSession();
        DualDocSortingService.clearDualDocSortingInfo(session);
        session.removeAttribute(KEY_DO_VERIFICATION_DTO);
        // if can select RFI section, need clear this, set in 'BeViewApplicationDelegator'
        session.removeAttribute(KEY_PAGE_APP_EDIT_SELECT_DTO);
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
        ParamUtil.setRequestAttr(request, KEY_FACILITY_DETAILS_INFO, doVerificationDto.getFacilityDetailsInfo());
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
        ParamUtil.setRequestAttr(request, KEY_TAB_DOCUMENT_INTERNAL_DOC_LIST, internalDocClient.getSortedInternalDocForDisplay(appId, dualDocSortingDto.getInternalDocSort()));
        // show routingHistory list
        ParamUtil.setRequestAttr(request, KEY_ROUTING_HISTORY_LIST, doVerificationDto.getProcessHistoryDtoList());
    }

    public void prepareSwitch(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        DOVerificationDto doVerificationDto = (DOVerificationDto) ParamUtil.getSessionAttr(request, KEY_DO_VERIFICATION_DTO);
        mohProcessService.reqObjMappingDOVerification(request, doVerificationDto);

        String crudActionType;
        String actionType = ParamUtil.getString(request, KEY_ACTION_TYPE);
        if (ModuleCommonConstants.KEY_ACTION_TYPE_SORT.equals(actionType)) {
            DualDocSortingDto dualDocSortingDto = DualDocSortingService.retrieveDualDocSortingInfo(request);
            DualDocSortingService.readDualDocSortingInfo(request, dualDocSortingDto);
            ParamUtil.setSessionAttr(request, KEY_DUAL_DOC_SORTING_INFO, dualDocSortingDto);
            crudActionType = CRUD_ACTION_TYPE_PREPARE;
            ParamUtil.setRequestAttr(request, TAB_ACTIVE, ModuleCommonConstants.TAB_DOC);
        } else if (ModuleCommonConstants.KEY_SUBMIT.equals(actionType)) {
            // validation
            PageAppEditSelectDto pageAppEditSelectDto  = (PageAppEditSelectDto) ParamUtil.getSessionAttr(request, KEY_PAGE_APP_EDIT_SELECT_DTO);
            ValidationResultDto validationResultDto = processClient.validateDOVerificationDto(doVerificationDto);
            rfiService.validateRfiSelection(doVerificationDto.getProcessingDecision(), validationResultDto, pageAppEditSelectDto, null);
            if (!validationResultDto.isPass()) {
                ParamUtil.setRequestAttr(request, TAB_ACTIVE, ModuleCommonConstants.TAB_PROCESSING);
                ParamUtil.setRequestAttr(request, KEY_VALIDATION_ERRORS, validationResultDto.toErrorMsg());
                crudActionType = CRUD_ACTION_TYPE_PREPARE;
            } else {
                crudActionType = CRUD_ACTION_TYPE_PROCESS;
            }
        } else {
            throw new IaisRuntimeException("Invalid action type");
        }
        ParamUtil.setSessionAttr(request, KEY_DO_VERIFICATION_DTO, doVerificationDto);
        ParamUtil.setRequestAttr(request, KEY_CRUD_ACTION_TYPE, crudActionType);
    }

    public void process(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        String taskId = (String) ParamUtil.getSessionAttr(request, PARAM_NAME_TASK_ID);
        String appId = (String) ParamUtil.getSessionAttr(request, PARAM_NAME_APP_ID);
        DOVerificationDto doVerificationDto = (DOVerificationDto) ParamUtil.getSessionAttr(request, KEY_DO_VERIFICATION_DTO);
        String processingDecision = doVerificationDto.getProcessingDecision();
        switch (processingDecision) {
            case MOH_PROCESS_DECISION_ACCEPT:
                processClient.saveDOVerificationAccept(appId, taskId, doVerificationDto);
                ParamUtil.setRequestAttr(request, ModuleCommonConstants.RESULT_MSG, "BISACKNEW001");
                break;
            case MOH_PROCESS_DECISION_REJECT:
                processClient.saveDOVerificationReject(appId, taskId, doVerificationDto);
                ParamUtil.setRequestAttr(request, ModuleCommonConstants.RESULT_MSG, "BISACKNEW005");
                break;
            case MOH_PROCESS_DECISION_REQUEST_FOR_INFORMATION:
                doVerificationDto.setPageAppEditSelectDto((PageAppEditSelectDto) ParamUtil.getSessionAttr(request, KEY_PAGE_APP_EDIT_SELECT_DTO));
                rfiClient.saveDOVerificationRfi(doVerificationDto, appId, taskId);
                ParamUtil.setRequestAttr(request, ModuleCommonConstants.RESULT_MSG, "BISACKNEW004");
                break;
            default:
                log.info("don't have such processingDecision {}", StringUtils.normalizeSpace(processingDecision));
                break;
        }
    }
}

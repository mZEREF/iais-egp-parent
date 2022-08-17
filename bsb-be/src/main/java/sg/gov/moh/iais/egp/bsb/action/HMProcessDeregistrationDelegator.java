package sg.gov.moh.iais.egp.bsb.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sg.gov.moh.iais.egp.bsb.client.ApplicationDocClient;
import sg.gov.moh.iais.egp.bsb.client.InternalDocClient;
import sg.gov.moh.iais.egp.bsb.client.ProcessDeregistrationClient;
import sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants;
import sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants;
import sg.gov.moh.iais.egp.bsb.dto.validation.ValidationResultDto;
import sg.gov.moh.iais.egp.bsb.dto.file.DocDisplayDto;
import sg.gov.moh.iais.egp.bsb.dto.processderegistration.HMProcessDto;
import sg.gov.moh.iais.egp.bsb.service.ProcessDeregistrationService;
import sg.gov.moh.iais.egp.bsb.service.ProcessHistoryService;
import sg.gov.moh.iais.egp.bsb.util.DocDisplayDtoUtil;
import sg.gov.moh.iais.egp.bsb.util.MaskHelper;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

import static com.ecquaria.cloud.moh.iais.common.constant.BsbAuditTrailConstants.MODULE_DEREGISTRATION;
import static sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants.KEY_TAB_DOCUMENT_INTERNAL_DOC_LIST;
import static sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants.KEY_TAB_DOCUMENT_SUPPORT_DOC_LIST;
import static sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants.KEY_VALIDATION_ERRORS;
import static sg.gov.moh.iais.egp.bsb.constant.module.ProcessDeregistrationConstants.INDEED_ACTION_TYPE_DO_PROCESS;
import static sg.gov.moh.iais.egp.bsb.constant.module.ProcessDeregistrationConstants.INDEED_ACTION_TYPE_PREPARE_DATA;
import static sg.gov.moh.iais.egp.bsb.constant.module.ProcessDeregistrationConstants.KEY_HM_PROCESS_DTO;
import static sg.gov.moh.iais.egp.bsb.constant.module.ProcessDeregistrationConstants.KEY_SUBMISSION_DETAILS_DTO;
import static sg.gov.moh.iais.egp.bsb.constant.module.ProcessDeregistrationConstants.PROCESS_PAGE_VALIDATION;
import static sg.gov.moh.iais.egp.bsb.constant.module.TaskModuleConstants.PARAM_NAME_APP_ID;
import static sg.gov.moh.iais.egp.bsb.constant.module.TaskModuleConstants.PARAM_NAME_TASK_ID;


@Slf4j
@Delegator(value = "hmProcessDeregistrationDelegator")
public class HMProcessDeregistrationDelegator {
    private final ProcessDeregistrationClient processDeregistrationClient;
    private final InternalDocClient internalDocClient;
    private final ApplicationDocClient applicationDocClient;

    private final ProcessHistoryService processHistoryService;
    private final ProcessDeregistrationService processDeregistrationService;

    @Autowired
    public HMProcessDeregistrationDelegator(ProcessDeregistrationClient processDeregistrationClient, InternalDocClient internalDocClient,
                                            ApplicationDocClient applicationDocClient, ProcessHistoryService processHistoryService,
                                            ProcessDeregistrationService processDeregistrationService) {
        this.processDeregistrationClient = processDeregistrationClient;
        this.internalDocClient = internalDocClient;
        this.applicationDocClient = applicationDocClient;
        this.processHistoryService = processHistoryService;
        this.processDeregistrationService = processDeregistrationService;
    }

    public void start(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        request.getSession().removeAttribute(KEY_HM_PROCESS_DTO);
        request.getSession().removeAttribute("applicationDocRepoIdNameMap");
        MaskHelper.taskProcessUnmask(request, PARAM_NAME_APP_ID, PARAM_NAME_TASK_ID);
        AuditTrailHelper.auditFunction(MODULE_DEREGISTRATION, "");
    }

    public void prepareData(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        String appId = (String) ParamUtil.getSessionAttr(request, PARAM_NAME_APP_ID);
        HMProcessDto hmProcessDto = processDeregistrationService.getHMProcessDto(request, appId);
        ParamUtil.setSessionAttr(request, KEY_HM_PROCESS_DTO, hmProcessDto);
        ParamUtil.setRequestAttr(request, KEY_SUBMISSION_DETAILS_DTO, hmProcessDto.getSubmissionDetailsDto());
        //show routingHistory list
        processHistoryService.getAndSetHistoryInRequest(hmProcessDto.getSubmissionDetailsDto().getApplicationNo(), request);
        //show internal doc
        List<DocDisplayDto> internalDocDisplayDto = internalDocClient.getInternalDocForDisplay(appId);
        ParamUtil.setRequestAttr(request, KEY_TAB_DOCUMENT_INTERNAL_DOC_LIST, internalDocDisplayDto);
        //show applicant submit doc
        List<DocDisplayDto> supportDocDisplayDto = applicationDocClient.getApplicationDocForDisplay(appId);
        ParamUtil.setRequestAttr(request, KEY_TAB_DOCUMENT_SUPPORT_DOC_LIST, supportDocDisplayDto);
        //provide for download support doc
        Map<String, String> repoIdDocNameMap = DocDisplayDtoUtil.getRepoIdDocNameMap(supportDocDisplayDto);
        ParamUtil.setSessionAttr(request, "docDisplayDtoRepoIdNameMap", (Serializable) repoIdDocNameMap);
    }

    public void prepareSwitch(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        HMProcessDto hmProcessDto = (HMProcessDto) ParamUtil.getSessionAttr(request, KEY_HM_PROCESS_DTO);
        processDeregistrationService.reqHMProcessDto(request, hmProcessDto);
        ParamUtil.setSessionAttr(request, KEY_HM_PROCESS_DTO, hmProcessDto);
        //validation
        ValidationResultDto validationResultDto = processDeregistrationClient.validateHMProcessDto(hmProcessDto);
        String indeedActionType;
        if (!validationResultDto.isPass()){
            ParamUtil.setRequestAttr(request, PROCESS_PAGE_VALIDATION, MasterCodeConstants.YES);
            ParamUtil.setRequestAttr(request, KEY_VALIDATION_ERRORS, validationResultDto.toErrorMsg());
            indeedActionType = INDEED_ACTION_TYPE_PREPARE_DATA;
        }else {
            indeedActionType = INDEED_ACTION_TYPE_DO_PROCESS;
        }
        ParamUtil.setRequestAttr(request, ModuleCommonConstants.KEY_INDEED_ACTION_TYPE, indeedActionType);
    }

    public void doProcess(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        String taskId = (String) ParamUtil.getSessionAttr(request, PARAM_NAME_TASK_ID);
        String appId = (String) ParamUtil.getSessionAttr(request, PARAM_NAME_APP_ID);
        HMProcessDto hmProcessDto = (HMProcessDto) ParamUtil.getSessionAttr(request, KEY_HM_PROCESS_DTO);
        hmProcessDto.setTaskId(taskId);
        hmProcessDto.setApplicationId(appId);
        processDeregistrationClient.saveHMProcessDto(hmProcessDto);
    }
}

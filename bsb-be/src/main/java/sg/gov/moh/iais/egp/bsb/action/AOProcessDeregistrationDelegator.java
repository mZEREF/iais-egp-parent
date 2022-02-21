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
import sg.gov.moh.iais.egp.bsb.constant.module.AppViewConstants;
import sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants;
import sg.gov.moh.iais.egp.bsb.dto.ValidationResultDto;
import sg.gov.moh.iais.egp.bsb.dto.file.DocDisplayDto;
import sg.gov.moh.iais.egp.bsb.dto.processderegistration.AOProcessDto;
import sg.gov.moh.iais.egp.bsb.service.AppViewService;
import sg.gov.moh.iais.egp.bsb.service.ProcessDeregistrationService;
import sg.gov.moh.iais.egp.bsb.service.ProcessHistoryService;
import sg.gov.moh.iais.egp.bsb.util.DocDisplayDtoUtil;
import sg.gov.moh.iais.egp.bsb.util.MaskHelper;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

import static sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants.*;
import static sg.gov.moh.iais.egp.bsb.constant.module.ProcessDeregistrationConstants.*;
import static sg.gov.moh.iais.egp.bsb.constant.module.TaskModuleConstants.PARAM_NAME_APP_ID;
import static sg.gov.moh.iais.egp.bsb.constant.module.TaskModuleConstants.PARAM_NAME_TASK_ID;

/**
 * @author : LiRan
 * @date : 2022/1/20
 */
@Slf4j
@Delegator(value = "aoProcessDeregistrationDelegator")
public class AOProcessDeregistrationDelegator {
    private final ProcessDeregistrationClient processDeregistrationClient;
    private final InternalDocClient internalDocClient;
    private final ApplicationDocClient applicationDocClient;

    private final AppViewService appViewService;
    private final ProcessHistoryService processHistoryService;
    private final ProcessDeregistrationService processDeregistrationService;

    @Autowired
    public AOProcessDeregistrationDelegator(ProcessDeregistrationClient processDeregistrationClient, InternalDocClient internalDocClient,
                                            ApplicationDocClient applicationDocClient, AppViewService appViewService, ProcessHistoryService processHistoryService,
                                            ProcessDeregistrationService processDeregistrationService) {
        this.processDeregistrationClient = processDeregistrationClient;
        this.internalDocClient = internalDocClient;
        this.applicationDocClient = applicationDocClient;
        this.appViewService = appViewService;
        this.processHistoryService = processHistoryService;
        this.processDeregistrationService = processDeregistrationService;
    }

    public void start(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        request.getSession().removeAttribute(KEY_AO_PROCESS_DTO);
        request.getSession().removeAttribute(AppViewConstants.KEY_APP_VIEW_DTO);
        request.getSession().removeAttribute("applicationDocRepoIdNameMap");
        MaskHelper.taskProcessUnmask(request, PARAM_NAME_APP_ID, PARAM_NAME_TASK_ID);
        AuditTrailHelper.auditFunction(MODULE_NAME, FUNCTION_NAME_DO_PROCESS);
    }

    public void prepareData(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        String appId = (String) ParamUtil.getSessionAttr(request, PARAM_NAME_APP_ID);
        AOProcessDto aoProcessDto = processDeregistrationService.getAOProcessDto(request, appId);
        ParamUtil.setSessionAttr(request, KEY_AO_PROCESS_DTO, aoProcessDto);
        ParamUtil.setRequestAttr(request, KEY_SUBMISSION_DETAILS_DTO, aoProcessDto.getSubmissionDetailsDto());
        //view application process need set appViewDto
        String moduleType = appViewService.judgeProcessAppModuleType(aoProcessDto.getSubmissionDetailsDto().getProcessType(), aoProcessDto.getSubmissionDetailsDto().getApplicationType());
        appViewService.createAndSetAppViewDtoInSession(appId, moduleType, request);
        //show routingHistory list
        processHistoryService.getAndSetHistoryInRequest(aoProcessDto.getSubmissionDetailsDto().getApplicationNo(), request);
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
        AOProcessDto aoProcessDto = (AOProcessDto) ParamUtil.getSessionAttr(request, KEY_AO_PROCESS_DTO);
        processDeregistrationService.reqAOProcessDto(request, aoProcessDto);
        ParamUtil.setSessionAttr(request, KEY_AO_PROCESS_DTO, aoProcessDto);
        //validation
        ValidationResultDto validationResultDto = processDeregistrationClient.validateAOProcessDto(aoProcessDto);
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
        AOProcessDto aoProcessDto = (AOProcessDto) ParamUtil.getSessionAttr(request, KEY_AO_PROCESS_DTO);
        aoProcessDto.setTaskId(taskId);
        aoProcessDto.setApplicationId(appId);
        processDeregistrationClient.saveAOProcessDto(aoProcessDto);
    }
}

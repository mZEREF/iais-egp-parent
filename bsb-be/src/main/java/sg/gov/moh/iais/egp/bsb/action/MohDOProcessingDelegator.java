package sg.gov.moh.iais.egp.bsb.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sg.gov.moh.iais.egp.bsb.client.InternalDocClient;
import sg.gov.moh.iais.egp.bsb.client.ProcessClient;
import sg.gov.moh.iais.egp.bsb.constant.module.AppViewConstants;
import sg.gov.moh.iais.egp.bsb.dto.validation.ValidationResultDto;
import sg.gov.moh.iais.egp.bsb.dto.file.DocDisplayDto;
import sg.gov.moh.iais.egp.bsb.dto.process.DOProcessingDto;
import sg.gov.moh.iais.egp.bsb.service.AppViewService;
import sg.gov.moh.iais.egp.bsb.service.MohProcessService;
import sg.gov.moh.iais.egp.bsb.service.ProcessHistoryService;
import sg.gov.moh.iais.egp.bsb.util.DocDisplayDtoUtil;
import sg.gov.moh.iais.egp.bsb.util.MaskHelper;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import static sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants.*;
import static sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants.*;
import static sg.gov.moh.iais.egp.bsb.constant.module.ProcessContants.*;
import static sg.gov.moh.iais.egp.bsb.constant.module.TaskModuleConstants.*;

/**
 * @author : LiRan
 * @date : 2021/11/22
 */
@Delegator(value = "doProcessingDelegator")
@Slf4j
public class MohDOProcessingDelegator {
    private final ProcessClient processClient;
    private final InternalDocClient internalDocClient;

    private final ProcessHistoryService processHistoryService;
    private final MohProcessService mohProcessService;

    @Autowired
    public MohDOProcessingDelegator(ProcessClient processClient, InternalDocClient internalDocClient,
                                    ProcessHistoryService processHistoryService, MohProcessService mohProcessService) {
        this.processClient = processClient;
        this.internalDocClient = internalDocClient;
        this.processHistoryService = processHistoryService;
        this.mohProcessService = mohProcessService;
    }

    public void start(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        request.getSession().removeAttribute(KEY_DO_PROCESSING_DTO);
        MaskHelper.taskProcessUnmask(request, PARAM_NAME_APP_ID, PARAM_NAME_TASK_ID);
        AuditTrailHelper.auditFunction(MODULE_NAME, FUNCTION_NAME_DO_PROCESSING);
    }

    public void prepareData(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        String appId = (String) ParamUtil.getSessionAttr(request, PARAM_NAME_APP_ID);
        DOProcessingDto doProcessingDto = mohProcessService.getDOProcessingDto(request, appId);
        ParamUtil.setSessionAttr(request, KEY_DO_PROCESSING_DTO, doProcessingDto);
        ParamUtil.setRequestAttr(request, KEY_SUBMIT_DETAILS_DTO, doProcessingDto.getSubmitDetailsDto());
        // view application need appId and moduleType
        String moduleType = AppViewService.judgeProcessAppModuleType(doProcessingDto.getSubmitDetailsDto().getProcessType(), doProcessingDto.getSubmitDetailsDto().getAppType());
        ParamUtil.setRequestAttr(request, AppViewConstants.MASK_PARAM_APP_ID, appId);
        ParamUtil.setRequestAttr(request, AppViewConstants.MASK_PARAM_APP_VIEW_MODULE_TYPE, moduleType);
        //show routingHistory list
        processHistoryService.getAndSetHistoryInRequest(doProcessingDto.getSubmitDetailsDto().getApplicationNo(), request);
        //show internal doc
        List<DocDisplayDto> internalDocDisplayDto = internalDocClient.getInternalDocForDisplay(appId);
        ParamUtil.setRequestAttr(request, KEY_TAB_DOCUMENT_INTERNAL_DOC_LIST, internalDocDisplayDto);
        //show applicant submit doc
        List<DocDisplayDto> supportDocDisplayDto = processClient.getDifferentModuleDoc(appId);
        ParamUtil.setRequestAttr(request, KEY_TAB_DOCUMENT_SUPPORT_DOC_LIST, supportDocDisplayDto);
        //provide for download support doc
        Map<String, String> repoIdDocNameMap = DocDisplayDtoUtil.getRepoIdDocNameMap(supportDocDisplayDto);
        ParamUtil.setSessionAttr(request, "docDisplayDtoRepoIdNameMap", (Serializable) repoIdDocNameMap);
    }

    public void prepareSwitch(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        DOProcessingDto doProcessingDto = (DOProcessingDto) ParamUtil.getSessionAttr(request, KEY_DO_PROCESSING_DTO);
        mohProcessService.getAndSetDOProcessingDto(request, doProcessingDto);
        ParamUtil.setSessionAttr(request, KEY_DO_PROCESSING_DTO, doProcessingDto);
        //validation
        ValidationResultDto validationResultDto = processClient.validateDOProcessingDto(doProcessingDto);
        String crudActionType;
        if (!validationResultDto.isPass()){
            ParamUtil.setRequestAttr(request, MOH_PROCESS_PAGE_VALIDATION, YES);
            ParamUtil.setRequestAttr(request, KEY_VALIDATION_ERRORS, validationResultDto.toErrorMsg());
            crudActionType = CRUD_ACTION_TYPE_PREPARE;
        }else {
            crudActionType = CRUD_ACTION_TYPE_PROCESS;
        }
        ParamUtil.setRequestAttr(request, KEY_CRUD_ACTION_TYPE, crudActionType);
    }

    public void process(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        String taskId = (String) ParamUtil.getSessionAttr(request, PARAM_NAME_TASK_ID);
        String appId = (String) ParamUtil.getSessionAttr(request, PARAM_NAME_APP_ID);
        DOProcessingDto doProcessingDto = (DOProcessingDto) ParamUtil.getSessionAttr(request, KEY_DO_PROCESSING_DTO);
        doProcessingDto.setTaskId(taskId);
        doProcessingDto.setApplicationId(appId);
        //decision: Recommend Approval, Request for Information, Recommend Rejection
        processClient.saveDOProcessing(doProcessingDto);
    }
}
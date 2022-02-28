package sg.gov.moh.iais.egp.bsb.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sg.gov.moh.iais.egp.bsb.client.InternalDocClient;
import sg.gov.moh.iais.egp.bsb.client.ProcessClient;
import sg.gov.moh.iais.egp.bsb.constant.module.AppViewConstants;
import sg.gov.moh.iais.egp.bsb.dto.ValidationResultDto;
import sg.gov.moh.iais.egp.bsb.dto.file.DocDisplayDto;
import sg.gov.moh.iais.egp.bsb.dto.process.HMScreeningDto;
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

import static sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants.YES;
import static sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants.*;
import static sg.gov.moh.iais.egp.bsb.constant.module.ProcessContants.*;
import static sg.gov.moh.iais.egp.bsb.constant.module.TaskModuleConstants.*;

/**
 * @author : LiRan
 * @date : 2021/11/22
 */
@Delegator(value = "hmProcessingDelegator")
@Slf4j
public class MohHMProcessingDelegator {
    private final ProcessClient processClient;
    private final InternalDocClient internalDocClient;

    private final ProcessHistoryService processHistoryService;
    private final MohProcessService mohProcessService;
    private final AppViewService appViewService;

    @Autowired
    public MohHMProcessingDelegator(ProcessClient processClient, InternalDocClient internalDocClient,
                                    ProcessHistoryService processHistoryService, MohProcessService mohProcessService,
                                    AppViewService appViewService) {
        this.processClient = processClient;
        this.internalDocClient = internalDocClient;
        this.processHistoryService = processHistoryService;
        this.mohProcessService = mohProcessService;
        this.appViewService = appViewService;
    }

    public void start(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        request.getSession().removeAttribute(KEY_HM_SCREENING_DTO);
        request.getSession().removeAttribute(AppViewConstants.KEY_APP_VIEW_DTO);
        request.getSession().removeAttribute(KEY_ROUTING_HISTORY_LIST);
        MaskHelper.taskProcessUnmask(request, PARAM_NAME_APP_ID, PARAM_NAME_TASK_ID);
        AuditTrailHelper.auditFunction(MODULE_NAME, FUNCTION_NAME_HM_PROCESSING);
    }

    public void prepareData(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        String appId = (String) ParamUtil.getSessionAttr(request, PARAM_NAME_APP_ID);
        HMScreeningDto hmScreeningDto = mohProcessService.getHMScreeningDto(request, appId);
        ParamUtil.setSessionAttr(request, KEY_HM_SCREENING_DTO, hmScreeningDto);
        ParamUtil.setRequestAttr(request, KEY_SUBMIT_DETAILS_DTO, hmScreeningDto.getSubmitDetailsDto());
        //view application process need an applicationDto
        String moduleType = appViewService.judgeProcessAppModuleType(hmScreeningDto.getSubmitDetailsDto().getProcessType(), hmScreeningDto.getSubmitDetailsDto().getAppType());
        AppViewService.createAndSetAppViewDtoInSession(appId, moduleType, request);
        //show routingHistory list
        processHistoryService.getAndSetHistoryInSession(hmScreeningDto.getSubmitDetailsDto().getApplicationNo(), request);
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
        HMScreeningDto hmScreeningDto = (HMScreeningDto) ParamUtil.getSessionAttr(request, KEY_HM_SCREENING_DTO);
        mohProcessService.getAndSetHMScreeningDto(request, hmScreeningDto);
        ParamUtil.setSessionAttr(request, KEY_HM_SCREENING_DTO, hmScreeningDto);
        //validation
        ValidationResultDto validationResultDto = processClient.validateHMScreeningDto(hmScreeningDto);
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
        HMScreeningDto hmScreeningDto = (HMScreeningDto) ParamUtil.getSessionAttr(request, KEY_HM_SCREENING_DTO);
        hmScreeningDto.setTaskId(taskId);
        hmScreeningDto.setApplicationId(appId);
        //decision: approval, reject
        processClient.saveHMScreening(hmScreeningDto);
    }
}
package sg.gov.moh.iais.egp.bsb.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import sg.gov.moh.iais.egp.bsb.client.ProcessClient;
import sg.gov.moh.iais.egp.bsb.constant.RoleConstants;
import sg.gov.moh.iais.egp.bsb.constant.module.AppViewConstants;
import sg.gov.moh.iais.egp.bsb.dto.process.MohProcessDto;
import sg.gov.moh.iais.egp.bsb.dto.validation.ValidationResultDto;
import sg.gov.moh.iais.egp.bsb.service.AppViewService;
import sg.gov.moh.iais.egp.bsb.service.MohProcessService;
import sg.gov.moh.iais.egp.bsb.util.DocDisplayDtoUtil;
import sg.gov.moh.iais.egp.bsb.util.MaskHelper;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;

import java.io.Serializable;
import java.util.Map;

import static sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants.*;
import static sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants.*;
import static sg.gov.moh.iais.egp.bsb.constant.module.ProcessContants.*;
import static sg.gov.moh.iais.egp.bsb.constant.module.TaskModuleConstants.*;

/**
 * @author : LiRan
 * @date : 2021/11/22
 */
@Delegator(value = "aoScreeningDelegator")
@Slf4j
public class MohAOScreeningDelegator {
    private final ProcessClient processClient;
    private final MohProcessService mohProcessService;

    @Autowired
    public MohAOScreeningDelegator(ProcessClient processClient, MohProcessService mohProcessService) {
        this.processClient = processClient;
        this.mohProcessService = mohProcessService;
    }


    public void start(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        request.getSession().removeAttribute(KEY_MOH_PROCESS_DTO);
        MaskHelper.taskProcessUnmask(request, PARAM_NAME_APP_ID, PARAM_NAME_TASK_ID);
        AuditTrailHelper.auditFunction(MODULE_NAME, FUNCTION_NAME_AO_SCREENING);
    }

    public void prepareData(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        String appId = (String) ParamUtil.getSessionAttr(request, PARAM_NAME_APP_ID);
        MohProcessDto mohProcessDto = mohProcessService.getMohProcessDto(request, appId, MODULE_NAME_AO_SCREENING);
        ParamUtil.setSessionAttr(request, KEY_MOH_PROCESS_DTO, mohProcessDto);

        // show data
        ParamUtil.setRequestAttr(request, KEY_SUBMISSION_DETAILS_INFO, mohProcessDto.getSubmissionDetailsInfo());
        ParamUtil.setRequestAttr(request, KEY_FACILITY_DETAILS_INFO, mohProcessDto.getFacilityDetailsInfo());
        // show applicant support doc
        ParamUtil.setRequestAttr(request, KEY_TAB_DOCUMENT_SUPPORT_DOC_LIST, mohProcessDto.getSupportDocDisplayDtoList());
        // provide for download support doc
        Map<String, String> repoIdDocNameMap = DocDisplayDtoUtil.getRepoIdDocNameMap(mohProcessDto.getSupportDocDisplayDtoList());
        ParamUtil.setSessionAttr(request, KEY_DOC_DISPLAY_DTO_REPO_ID_NAME_MAP, (Serializable) repoIdDocNameMap);
        // show internal doc
        ParamUtil.setRequestAttr(request, KEY_TAB_DOCUMENT_INTERNAL_DOC_LIST, mohProcessDto.getInternalDocDisplayDtoList());
        // show route to moh selection list
        ParamUtil.setRequestAttr(request, KEY_SELECT_ROUTE_TO_MOH, mohProcessDto.getSelectRouteToMoh());
        // show routingHistory list
        ParamUtil.setRequestAttr(request, KEY_ROUTING_HISTORY_LIST, mohProcessDto.getProcessHistoryDtoList());

        // view application need appId and moduleType
        String moduleType = AppViewService.judgeProcessAppModuleType(mohProcessDto.getSubmissionDetailsInfo().getApplicationSubType(), mohProcessDto.getSubmissionDetailsInfo().getApplicationType());
        ParamUtil.setRequestAttr(request, AppViewConstants.MASK_PARAM_APP_ID, appId);
        ParamUtil.setRequestAttr(request, AppViewConstants.MASK_PARAM_APP_VIEW_MODULE_TYPE, moduleType);
    }

    public void prepareSwitch(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        MohProcessDto mohProcessDto = (MohProcessDto) ParamUtil.getSessionAttr(request, KEY_MOH_PROCESS_DTO);
        mohProcessService.getAndSetMohProcessDto(request, mohProcessDto);
        ParamUtil.setSessionAttr(request, KEY_DO_SCREENING_DTO, mohProcessDto);
        //validation
        ValidationResultDto validationResultDto = processClient.validateMohProcessDto(mohProcessDto, MODULE_NAME_AO_SCREENING);
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
        MohProcessDto mohProcessDto = (MohProcessDto) ParamUtil.getSessionAttr(request, KEY_MOH_PROCESS_DTO);
        String processingDecision = mohProcessDto.getProcessingDecision();
        switch (processingDecision) {
            case MOH_PROCESSING_DECISION_APPROVE:
                processClient.saveMohProcessDtoAoApprove(appId, taskId, mohProcessDto);
                break;
            case MOH_PROCESSING_DECISION_REJECT:
                processClient.saveMohProcessDtoAoReject(appId, taskId, mohProcessDto);
                break;
            case MOH_PROCESSING_DECISION_ROUTE_BACK_TO_DO:
                processClient.saveMohProcessDtoRouteBackToDo(appId, taskId, mohProcessDto);
                break;
            case MOH_PROCESSING_DECISION_ROUTE_BACK_TO_HM:
                processClient.saveMohProcessDtoRouteToHm(appId, taskId, mohProcessDto);
                break;
            default:
                log.info("don't have such processingDecision {}", StringUtils.normalizeSpace(processingDecision));
                break;
        }
    }
}
package sg.gov.moh.iais.egp.bsb.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.dto.organization.OrgUserDto;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sg.gov.moh.iais.egp.bsb.client.InternalDocClient;
import sg.gov.moh.iais.egp.bsb.client.ProcessClient;
import sg.gov.moh.iais.egp.bsb.constant.module.AppViewConstants;
import sg.gov.moh.iais.egp.bsb.dto.info.common.AppBasicInfo;
import sg.gov.moh.iais.egp.bsb.dto.process.MohProcessDto;
import sg.gov.moh.iais.egp.bsb.dto.validation.ValidationResultDto;
import sg.gov.moh.iais.egp.bsb.dto.file.DocDisplayDto;
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

import static sg.gov.moh.iais.egp.bsb.constant.module.ProcessContants.*;
import static sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants.*;
import static sg.gov.moh.iais.egp.bsb.constant.module.TaskModuleConstants.*;
import static sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants.*;

/**
 * @author : LiRan
 * @date : 2021/11/22
 */
@Delegator(value = "doScreeningDelegator")
@Slf4j
public class MohDOScreeningDelegator {
    private final ProcessClient processClient;
    private final InternalDocClient internalDocClient;

    private final ProcessHistoryService processHistoryService;
    private final MohProcessService mohProcessService;

    @Autowired
    public MohDOScreeningDelegator(ProcessClient processClient, InternalDocClient internalDocClient,
                                   ProcessHistoryService processHistoryService, MohProcessService mohProcessService) {
        this.processClient = processClient;
        this.internalDocClient = internalDocClient;
        this.processHistoryService = processHistoryService;
        this.mohProcessService = mohProcessService;
    }

    public void start(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        request.getSession().removeAttribute(KEY_MOH_PROCESS_DTO);
        MaskHelper.taskProcessUnmask(request, PARAM_NAME_APP_ID, PARAM_NAME_TASK_ID);
        AuditTrailHelper.auditFunction(MODULE_NAME, FUNCTION_NAME_DO_SCREENING);
    }

    public void prepareData(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        String appId = (String) ParamUtil.getSessionAttr(request, PARAM_NAME_APP_ID);
        // TODO: data will get from dataBase
        MohProcessDto mohProcessDto = mohProcessService.getMohProcessDto(request, appId, "BSB_AO");
        mohProcessDto.setModuleName("doScreening");

        AppBasicInfo appBasicInfo = mohProcessDto.getAppBasicInfo();


        ParamUtil.setSessionAttr(request, KEY_MOH_PROCESS_DTO, mohProcessDto);

        ParamUtil.setRequestAttr(request, "appBasicInfo", mohProcessDto.getAppBasicInfo());
        ParamUtil.setRequestAttr(request, "facilityDetailsInfo", mohProcessDto.getFacilityDetailsInfo());
        // show applicant submit doc
        ParamUtil.setRequestAttr(request, KEY_TAB_DOCUMENT_SUPPORT_DOC_LIST, mohProcessDto.getDocDisplayDtoList());
        //provide for download support doc
        Map<String, String> repoIdDocNameMap = DocDisplayDtoUtil.getRepoIdDocNameMap(mohProcessDto.getDocDisplayDtoList());
        ParamUtil.setSessionAttr(request, "docDisplayDtoRepoIdNameMap", (Serializable) repoIdDocNameMap);

        // view application need appId and moduleType
        String moduleType = AppViewService.judgeProcessAppModuleType(appBasicInfo.getProcessType(), appBasicInfo.getAppType());
        ParamUtil.setRequestAttr(request, AppViewConstants.MASK_PARAM_APP_ID, appId);
        ParamUtil.setRequestAttr(request, AppViewConstants.MASK_PARAM_APP_VIEW_MODULE_TYPE, moduleType);

        // show internal doc
        List<DocDisplayDto> internalDocDisplayDto = internalDocClient.getInternalDocForDisplay(appId);
        ParamUtil.setRequestAttr(request, KEY_TAB_DOCUMENT_INTERNAL_DOC_LIST, internalDocDisplayDto);
        // show routingHistory list
        processHistoryService.getAndSetHistoryInRequest(appBasicInfo.getAppNo(), request);
    }

    public void prepareSwitch(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        MohProcessDto mohProcessDto = (MohProcessDto) ParamUtil.getSessionAttr(request, KEY_MOH_PROCESS_DTO);
        mohProcessService.getAndSetMohProcessDto(request, mohProcessDto);
        ParamUtil.setSessionAttr(request, KEY_DO_SCREENING_DTO, mohProcessDto);
        //validation
        ValidationResultDto validationResultDto = processClient.validateMohProcessDto(mohProcessDto);
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
//        String taskId = (String) ParamUtil.getSessionAttr(request, PARAM_NAME_TASK_ID);
//        String appId = (String) ParamUtil.getSessionAttr(request, PARAM_NAME_APP_ID);
//        MohProcessDto mohProcessDto = (MohProcessDto) ParamUtil.getSessionAttr(request, KEY_MOH_PROCESS_DTO);
//        String processingDecision = mohProcessDto.getProcessingDecision();
//        // decision: SCREENED_BY_DO, REQUEST_FOR_INFORMATION, REJECT
//        switch (processingDecision) {
//            case MOH_PROCESSING_DECISION_SCREENED_BY_DO:
//                processClient.saveMohProcessDtoScreenedByDO(appId, taskId, mohProcessDto);
//                break;
//            case MOH_PROCESSING_DECISION_REQUEST_FOR_INFO:
//                processClient.saveMohProcessDtoRequestForInformation(appId, taskId, mohProcessDto);
//                break;
//            case MOH_PROCESSING_DECISION_REJECT:
//                processClient.saveMohProcessDtoDoReject(appId, taskId, mohProcessDto);
//                break;
//            default:
//                log.info("don't have such processingDecision {}", StringUtils.normalizeSpace(processingDecision));
//                break;
//        }
    }
}
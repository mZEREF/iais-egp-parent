package sg.gov.moh.iais.egp.bsb.service;

import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import sg.gov.moh.iais.egp.bsb.client.ProcessClient;
import sg.gov.moh.iais.egp.bsb.constant.module.AppViewConstants;
import sg.gov.moh.iais.egp.bsb.dto.process.*;
import sg.gov.moh.iais.egp.bsb.dto.validation.ValidationResultDto;
import sg.gov.moh.iais.egp.bsb.util.DocDisplayDtoUtil;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;

import java.io.Serializable;
import java.util.Map;

import static sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants.NO;
import static sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants.YES;
import static sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants.*;
import static sg.gov.moh.iais.egp.bsb.constant.module.ProcessContants.*;
import static sg.gov.moh.iais.egp.bsb.constant.module.TaskModuleConstants.PARAM_NAME_APP_ID;

/**
 * @author : LiRan
 * @date : 2022/1/26
 */
@Service
@Slf4j
public class MohProcessService {
    private final ProcessClient processClient;

    public MohProcessService(ProcessClient processClient) {
        this.processClient = processClient;
    }

    public MohProcessDto getMohProcessDto(HttpServletRequest request, String applicationId, String moduleName){
        MohProcessDto dto = (MohProcessDto) ParamUtil.getSessionAttr(request, KEY_MOH_PROCESS_DTO);
        if (dto == null){
            dto = processClient.getMohProcessDtoByAppId(applicationId, moduleName).getEntity();
        }
        return dto;
    }

//    ---------------------------- Moh process delegator public part ----------------------------------------------

    public void prepareData(BaseProcessClass bpc, String moduleName) {
        HttpServletRequest request = bpc.request;
        String appId = (String) ParamUtil.getSessionAttr(request, PARAM_NAME_APP_ID);
        MohProcessDto mohProcessDto = getMohProcessDto(request, appId, moduleName);
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
        ParamUtil.setRequestAttr(request, "canNotSaveNew", true);
    }

    public void prepareSwitch(BaseProcessClass bpc, String moduleName){
        HttpServletRequest request = bpc.request;
        String appId = (String) ParamUtil.getSessionAttr(request, PARAM_NAME_APP_ID);
        MohProcessDto mohProcessDto = (MohProcessDto) ParamUtil.getSessionAttr(request, KEY_MOH_PROCESS_DTO);
        mohProcessDto.reqObjMapping(request, moduleName);
        ParamUtil.setSessionAttr(request, KEY_MOH_PROCESS_DTO, mohProcessDto);
        //validation
        ValidationResultDto validationResultDto = processClient.validateMohProcessDto(mohProcessDto, moduleName);
        String crudActionType;
        if (!validationResultDto.isPass()) {
            ParamUtil.setRequestAttr(request, MOH_PROCESS_PAGE_VALIDATION, YES);
            ParamUtil.setRequestAttr(request, KEY_VALIDATION_ERRORS, validationResultDto.toErrorMsg());
            crudActionType = CRUD_ACTION_TYPE_PREPARE;
        } else {
            crudActionType = CRUD_ACTION_TYPE_PROCESS;
        }
        if (moduleName.equals(MODULE_NAME_DO_PROCESSING)) {
            String canSubmit = processClient.judgeCanSubmitDOProcessingTask(appId);
            if (canSubmit.equals(NO)) {
                crudActionType = CRUD_ACTION_TYPE_PREPARE;
            }
            ParamUtil.setRequestAttr(request, "canSubmit", canSubmit);
        }
        ParamUtil.setRequestAttr(request, KEY_CRUD_ACTION_TYPE, crudActionType);
    }
}

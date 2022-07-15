package sg.gov.moh.iais.egp.bsb.service;

import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import sg.gov.moh.iais.egp.bsb.client.ApplicationDocClient;
import sg.gov.moh.iais.egp.bsb.client.InternalDocClient;
import sg.gov.moh.iais.egp.bsb.client.WithdrawnClient;
import sg.gov.moh.iais.egp.bsb.constant.ValidationConstants;
import sg.gov.moh.iais.egp.bsb.dto.file.DocDisplayDto;
import sg.gov.moh.iais.egp.bsb.dto.validation.ValidationResultDto;
import sg.gov.moh.iais.egp.bsb.dto.withdrawn.AppSubmitWithdrawnDto;
import sg.gov.moh.iais.egp.bsb.util.DocDisplayDtoUtil;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import static sg.gov.moh.iais.egp.bsb.constant.module.AppViewConstants.KEY_APP_VIEW_URL;
import static sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants.KEY_DOC_DISPLAY_DTO_REPO_ID_NAME_MAP;
import static sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants.KEY_SUBMISSION_DETAILS_INFO;
import static sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants.KEY_TAB_DOCUMENT_INTERNAL_DOC_LIST;
import static sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants.KEY_TAB_DOCUMENT_SUPPORT_DOC_LIST;
import static sg.gov.moh.iais.egp.bsb.constant.module.TaskModuleConstants.PARAM_NAME_APP_ID;
import static sg.gov.moh.iais.egp.bsb.constant.module.TaskModuleConstants.PARAM_NAME_TASK_ID;

@Service
@Slf4j
public class ProcessWithdrawalService {
    public static final String ACTION_TYPE_SAVE = "doSave";
    public static final String ACTION_TYPE_PREPARE = "prepare";
    public static final String ACTION_TYPE = "action_type";
    public static final String WITHDRAWN_APP_DTO = "withdrawnDto";

    private final WithdrawnClient withdrawnClient;
    private final InternalDocClient internalDocClient;
    private final ApplicationDocClient applicationDocClient;
    private final ProcessHistoryService processHistoryService;

    public ProcessWithdrawalService(WithdrawnClient withdrawnClient, InternalDocClient internalDocClient, ApplicationDocClient applicationDocClient, ProcessHistoryService processHistoryService) {
        this.withdrawnClient = withdrawnClient;
        this.internalDocClient = internalDocClient;
        this.applicationDocClient = applicationDocClient;
        this.processHistoryService = processHistoryService;
    }

    public void prepareData(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        String appId = (String) ParamUtil.getSessionAttr(request, PARAM_NAME_APP_ID);
        AppSubmitWithdrawnDto dto = getWithdrawnDto(request, appId);
        ParamUtil.setSessionAttr(request, WITHDRAWN_APP_DTO, dto);
        // submission details info
        ParamUtil.setRequestAttr(request, KEY_SUBMISSION_DETAILS_INFO, dto.getSubmissionDetailsInfo());
        //
        processHistoryService.getAndSetHistoryInRequest(dto.getAppNo(), request);
        //show internal doc
        List<DocDisplayDto> internalDocDisplayDto = internalDocClient.getInternalDocForDisplay(dto.getAppId());
        ParamUtil.setRequestAttr(request, KEY_TAB_DOCUMENT_INTERNAL_DOC_LIST, internalDocDisplayDto);
        //show applicant submit doc
        List<DocDisplayDto> supportDocDisplayDto = applicationDocClient.getApplicationDocForDisplay(dto.getAppId());
        ParamUtil.setRequestAttr(request, KEY_TAB_DOCUMENT_SUPPORT_DOC_LIST, supportDocDisplayDto);
        //provide for download support doc
        Map<String, String> repoIdDocNameMap = DocDisplayDtoUtil.getRepoIdDocNameMap(supportDocDisplayDto);
        ParamUtil.setSessionAttr(request, KEY_DOC_DISPLAY_DTO_REPO_ID_NAME_MAP, (Serializable) repoIdDocNameMap);

        //view application
        ParamUtil.setRequestAttr(request, PARAM_NAME_APP_ID, appId);
        ParamUtil.setRequestAttr(request, KEY_APP_VIEW_URL, "/bsb-web/eservice/INTRANET/ViewWithdrawn");
    }

    public AppSubmitWithdrawnDto getWithdrawnDto(HttpServletRequest request, String applicationId) {
        AppSubmitWithdrawnDto dto = (AppSubmitWithdrawnDto) ParamUtil.getSessionAttr(request, WITHDRAWN_APP_DTO);
        if (dto == null) {
            dto = withdrawnClient.getWithdrawnDataByApplicationId(applicationId).getEntity();
        }
        return dto;
    }

    public void validateData(AppSubmitWithdrawnDto dto, HttpServletRequest request) {
        //validation
        String actionType;
        ValidationResultDto validationResultDto = withdrawnClient.validateWithdrawnDto(dto);
        if (!validationResultDto.isPass()) {
            ParamUtil.setRequestAttr(request, ValidationConstants.KEY_VALIDATION_ERRORS, validationResultDto.toErrorMsg());
            actionType = ACTION_TYPE_PREPARE;
        } else {
            actionType = ACTION_TYPE_SAVE;
        }
        ParamUtil.setRequestAttr(request, ACTION_TYPE, actionType);
    }

    public void setProcessDto(HttpServletRequest request) {
        String taskId = (String) ParamUtil.getSessionAttr(request, PARAM_NAME_TASK_ID);
        String appId = (String) ParamUtil.getSessionAttr(request, PARAM_NAME_APP_ID);
        AppSubmitWithdrawnDto dto = (AppSubmitWithdrawnDto) ParamUtil.getSessionAttr(request, WITHDRAWN_APP_DTO);
        dto.setTaskId(taskId);
        dto.setAppId(appId);
    }
}

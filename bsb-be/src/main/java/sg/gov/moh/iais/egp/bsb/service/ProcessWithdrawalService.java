package sg.gov.moh.iais.egp.bsb.service;

import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import sg.gov.moh.iais.egp.bsb.client.WithdrawnClient;
import sg.gov.moh.iais.egp.bsb.constant.ValidationConstants;
import sg.gov.moh.iais.egp.bsb.dto.validation.ValidationResultDto;
import sg.gov.moh.iais.egp.bsb.dto.withdrawn.AppSubmitWithdrawnDto;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;

import static sg.gov.moh.iais.egp.bsb.constant.module.AppViewConstants.KEY_APP_VIEW_URL;
import static sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants.KEY_SUBMISSION_DETAILS_INFO;
import static sg.gov.moh.iais.egp.bsb.constant.module.TaskModuleConstants.PARAM_NAME_APP_ID;
import static sg.gov.moh.iais.egp.bsb.constant.module.TaskModuleConstants.PARAM_NAME_TASK_ID;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProcessWithdrawalService {
    public static final String ACTION_TYPE_SAVE = "doSave";
    public static final String ACTION_TYPE_PREPARE = "prepare";
    public static final String WITHDRAWN_APP_DTO = "withdrawnDto";
    private static final String KEY_ROUTE = "route";

    private final WithdrawnClient withdrawnClient;
    private final ProcessHistoryService processHistoryService;
    private final DualDocSortingService dualDocSortingService;


    public void prepareData(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        String appId = (String) ParamUtil.getSessionAttr(request, PARAM_NAME_APP_ID);
        AppSubmitWithdrawnDto dto = getWithdrawnDto(request, appId);
        ParamUtil.setSessionAttr(request, WITHDRAWN_APP_DTO, dto);
        // submission details info
        ParamUtil.setRequestAttr(request, KEY_SUBMISSION_DETAILS_INFO, dto.getSubmissionDetailsInfo());
        //
        processHistoryService.getAndSetHistoryInRequest(appId, request);
        //show internal doc and applicant submit doc
        dualDocSortingService.retrieveDualDocDisplayDtoIntoRequest(request, appId);

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
        ParamUtil.setRequestAttr(request, KEY_ROUTE, actionType);
    }

    public void setProcessDto(HttpServletRequest request) {
        String taskId = (String) ParamUtil.getSessionAttr(request, PARAM_NAME_TASK_ID);
        String appId = (String) ParamUtil.getSessionAttr(request, PARAM_NAME_APP_ID);
        AppSubmitWithdrawnDto dto = (AppSubmitWithdrawnDto) ParamUtil.getSessionAttr(request, WITHDRAWN_APP_DTO);
        dto.setTaskId(taskId);
        dto.setAppId(appId);
    }
}

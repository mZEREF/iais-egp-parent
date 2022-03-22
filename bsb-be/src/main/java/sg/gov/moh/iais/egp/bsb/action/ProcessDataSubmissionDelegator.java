package sg.gov.moh.iais.egp.bsb.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import sg.gov.moh.iais.egp.bsb.client.DataSubmissionClient;
import sg.gov.moh.iais.egp.bsb.constant.ValidationConstants;
import sg.gov.moh.iais.egp.bsb.constant.module.AppViewConstants;
import sg.gov.moh.iais.egp.bsb.dto.ResponseDto;
import sg.gov.moh.iais.egp.bsb.dto.validation.ValidationResultDto;
import sg.gov.moh.iais.egp.bsb.dto.datasubmission.MohProcessDto;
import sg.gov.moh.iais.egp.bsb.dto.datasubmission.MohReviewDataSubmissionDto;
import sg.gov.moh.iais.egp.bsb.dto.withdrawn.AppSubmitWithdrawnDto;
import sg.gov.moh.iais.egp.bsb.service.ProcessHistoryService;
import sg.gov.moh.iais.egp.bsb.util.MaskHelper;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;

import static sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants.KEY_TAB_DOCUMENT_SUPPORT_DOC_LIST;
import static sg.gov.moh.iais.egp.bsb.constant.module.TaskModuleConstants.*;

/**
 * @author : tangtang
 */
@Slf4j
@Delegator(value = "processDataSubmissionDelegator")
public class ProcessDataSubmissionDelegator {
    private static final String DATA_SUBMISSION_PROCESS_DTO = "processDto";
    private static final String ACTION_TYPE_NEXT = "doNext";
    private static final String ACTION_TYPE_PREPARE = "prepare";
    private static final String ACTION_TYPE = "action_type";

    private final DataSubmissionClient dataSubmissionClient;
    private final ProcessHistoryService processHistoryService;

    public ProcessDataSubmissionDelegator(DataSubmissionClient dataSubmissionClient, ProcessHistoryService processHistoryService) {
        this.dataSubmissionClient = dataSubmissionClient;
        this.processHistoryService = processHistoryService;
    }

    public void start(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        request.getSession().removeAttribute(DATA_SUBMISSION_PROCESS_DTO);
        MaskHelper.taskProcessUnmask(request, PARAM_NAME_APP_ID, PARAM_NAME_TASK_ID);
        AuditTrailHelper.auditFunction("Data Submission", "Data Submission");
    }

    /**
     * todo query BA/T info
     */
    public void prepareData(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        MohReviewDataSubmissionDto dto = getProcessDto(request);
        ParamUtil.setRequestAttr(request, "canNotUploadInternalDoc", true);
        ParamUtil.setSessionAttr(request, DATA_SUBMISSION_PROCESS_DTO, dto);
        if (StringUtils.isEmpty(dto.getApplicationId())) {
            String appId = (String) ParamUtil.getSessionAttr(request, PARAM_NAME_APP_ID);
            String taskId = (String) ParamUtil.getSessionAttr(request, PARAM_NAME_TASK_ID);
            if (appId != null && taskId != null) {
                ResponseDto<MohReviewDataSubmissionDto> responseDto = dataSubmissionClient.getOfficerReviewDataByAppId(appId);
                if (responseDto.ok()) {
                    dto = responseDto.getEntity();
                    dto.setTaskId(taskId);
                    ParamUtil.setSessionAttr(request, DATA_SUBMISSION_PROCESS_DTO, dto);
                    //show routingHistory list
                    processHistoryService.getAndSetHistoryInSession(dto.getSubmissionDetailsDto().getApplicationNo(), request);
                    //
                    ParamUtil.setRequestAttr(request, KEY_TAB_DOCUMENT_SUPPORT_DOC_LIST, dto.getDocDisplayDtoList());
                } else {
                    log.warn("get withdrawn API doesn't return ok, the response is {}", responseDto);
                    ParamUtil.setSessionAttr(request, DATA_SUBMISSION_PROCESS_DTO, new AppSubmitWithdrawnDto());
                }
            }
        }
        // view application need appId and moduleType
        ParamUtil.setRequestAttr(request, AppViewConstants.MASK_PARAM_APP_ID, dto.getApplicationId());
        ParamUtil.setRequestAttr(request, AppViewConstants.MASK_PARAM_APP_VIEW_MODULE_TYPE, dto.getNotificationType());
    }

    public void doValidate(BaseProcessClass bpc) {
        //validate duty officer submitted data
        HttpServletRequest request = bpc.request;
        String remarks = ParamUtil.getRequestString(request, "doRemarks");
        String decision = ParamUtil.getRequestString(request, "doDecision");
        //
        MohReviewDataSubmissionDto reviewDto = getProcessDto(request);
        reviewDto.setDoRemarks(remarks);
        reviewDto.setDoDecision(decision);
        reviewDto.setModule("doSubmission");
        validateData(reviewDto, request);
        ParamUtil.setSessionAttr(request, DATA_SUBMISSION_PROCESS_DTO, reviewDto);
    }

    public void doProcessDataSubmission(BaseProcessClass bpc) {
        log.info("enter do process dataSubmission method");
        HttpServletRequest request = bpc.request;
        MohReviewDataSubmissionDto reviewDto = getProcessDto(request);
        MohProcessDto processDto = MohProcessDto.setDoProcessData(reviewDto.getApplicationId(), reviewDto.getTaskId(), reviewDto.getDoRemarks(), reviewDto.getDoDecision());
        dataSubmissionClient.doProcessDataSubmission(processDto);
    }

    public void aoValidate(BaseProcessClass bpc) {
        //validate approval officer submitted data
        HttpServletRequest request = bpc.request;
        String remarks = ParamUtil.getRequestString(request, "aoRemarks");
        String decision = ParamUtil.getRequestString(request, "aoDecision");
        //
        MohReviewDataSubmissionDto reviewDto = getProcessDto(request);
        reviewDto.setAoRemarks(remarks);
        reviewDto.setAoDecision(decision);
        reviewDto.setModule("aoSubmission");
        validateData(reviewDto, request);
        ParamUtil.setSessionAttr(request, DATA_SUBMISSION_PROCESS_DTO, reviewDto);
    }

    public void aoProcessDataSubmission(BaseProcessClass bpc) {
        log.info("enter do process dataSubmission method");
        HttpServletRequest request = bpc.request;
        MohReviewDataSubmissionDto reviewDto = getProcessDto(request);
        MohProcessDto processDto = MohProcessDto.setAoProcessData(reviewDto.getApplicationId(), reviewDto.getTaskId(), reviewDto.getAoRemarks(), reviewDto.getAoDecision());
        dataSubmissionClient.aoProcessDataSubmission(processDto);
    }

    private MohReviewDataSubmissionDto getProcessDto(HttpServletRequest request) {
        MohReviewDataSubmissionDto dto = (MohReviewDataSubmissionDto) ParamUtil.getSessionAttr(request, DATA_SUBMISSION_PROCESS_DTO);
        return dto == null ? getDefaultProcessDto() : dto;
    }

    private MohReviewDataSubmissionDto getDefaultProcessDto() {
        return new MohReviewDataSubmissionDto();
    }

    private void validateData(MohReviewDataSubmissionDto dto, HttpServletRequest request) {
        //validation
        String actionType;
        ValidationResultDto validationResultDto = dataSubmissionClient.validateMohProcessDto(dto);
        if (!validationResultDto.isPass()) {
            ParamUtil.setRequestAttr(request, ValidationConstants.KEY_VALIDATION_ERRORS, validationResultDto.toErrorMsg());
            actionType = ACTION_TYPE_PREPARE;
        } else {
            actionType = ACTION_TYPE_NEXT;
        }
        ParamUtil.setRequestAttr(request, ACTION_TYPE, actionType);
    }

}

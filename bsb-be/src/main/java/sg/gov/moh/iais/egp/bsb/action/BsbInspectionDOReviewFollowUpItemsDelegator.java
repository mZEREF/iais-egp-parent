package sg.gov.moh.iais.egp.bsb.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.AuditTrailConsts;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import sg.gov.moh.iais.egp.bsb.client.InspectionClient;
import sg.gov.moh.iais.egp.bsb.client.InternalDocClient;
import sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants;
import sg.gov.moh.iais.egp.bsb.constant.module.AppViewConstants;
import sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants;
import sg.gov.moh.iais.egp.bsb.dto.inspection.followup.ReviewInsFollowUpDto;
import sg.gov.moh.iais.egp.bsb.dto.validation.ValidationResultDto;
import sg.gov.moh.iais.egp.bsb.dto.file.DocDisplayDto;
import sg.gov.moh.iais.egp.bsb.dto.inspection.InsProcessDto;
import sg.gov.moh.iais.egp.bsb.util.DateUtil;
import sg.gov.moh.iais.egp.bsb.util.DocDisplayDtoUtil;
import sg.gov.moh.iais.egp.bsb.util.MaskHelper;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants.NO;
import static sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants.YES;
import static sg.gov.moh.iais.egp.bsb.constant.module.AppViewConstants.MODULE_VIEW_NEW_FACILITY;
import static sg.gov.moh.iais.egp.bsb.constant.module.InspectionConstants.*;
import static sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants.*;
import static sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants.KEY_ROUTING_HISTORY_LIST;

@Slf4j
@Delegator("mohDOReviewFollowUpItems")
public class BsbInspectionDOReviewFollowUpItemsDelegator {
    public static final String KEY_REVIEW_FOLLOW_UP_DTO = "reviewFollowUpDto";
    public static final String KEY_REQUEST_EXTENSION = "requestExtension";

    private final InspectionClient inspectionClient;
    private final InternalDocClient internalDocClient;

    @Autowired
    public BsbInspectionDOReviewFollowUpItemsDelegator(InspectionClient inspectionClient, InternalDocClient internalDocClient) {
        this.inspectionClient = inspectionClient;
        this.internalDocClient = internalDocClient;
    }

    public void start(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        MaskHelper.taskProcessUnmask(request, KEY_APP_ID, KEY_TASK_ID);
        AuditTrailHelper.auditFunction(AuditTrailConsts.MODULE_INSPECTION, "DO Review Follow-up Items");
        request.getSession().removeAttribute(KEY_REVIEW_FOLLOW_UP_DTO);
        request.getSession().removeAttribute(KEY_INS_DECISION);
        request.getSession().removeAttribute(KEY_DOC_DISPLAY_DTO_REPO_ID_NAME_MAP);
    }

    public void prepare(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        ReviewInsFollowUpDto dto = getDisplayDto(request);
        if (!StringUtils.hasLength(dto.getAppId())) {
            String appId = (String) ParamUtil.getSessionAttr(request, KEY_APP_ID);
            // facility info
            dto = inspectionClient.getInitInsFollowUpData(appId);
            dto.setAppId(appId);
            ParamUtil.setSessionAttr(request, KEY_REVIEW_FOLLOW_UP_DTO, dto);

        }
        // submission details info
        ParamUtil.setRequestAttr(request, KEY_SUBMISSION_DETAILS_INFO, dto.getSubmissionDetailsInfo());
        //support doc
        ParamUtil.setRequestAttr(request, KEY_TAB_DOCUMENT_SUPPORT_DOC_LIST, dto.getSupportDocDisplayDtoList());
        // facility details
        ParamUtil.setRequestAttr(request, KEY_FACILITY_DETAILS_INFO, dto.getFacilityDetailsInfo());
        // show routingHistory list
        ParamUtil.setRequestAttr(request, KEY_ROUTING_HISTORY_LIST, dto.getProcessHistoryDtoList());
        // view application need appId and moduleType
        ParamUtil.setRequestAttr(request, AppViewConstants.MASK_PARAM_APP_ID, dto.getInsAppId());
        ParamUtil.setRequestAttr(request, AppViewConstants.MASK_PARAM_APP_VIEW_MODULE_TYPE, MODULE_VIEW_NEW_FACILITY);
        // provide for download support doc
        Map<String, String> repoIdDocNameMap = DocDisplayDtoUtil.getRepoIdDocNameMap(dto.getSupportDocDisplayDtoList());
        repoIdDocNameMap.putAll(DocDisplayDtoUtil.getRepoIdDocNameMap(dto.getFollowUpDocDisplayDtoList()));
        ParamUtil.setSessionAttr(request, KEY_DOC_DISPLAY_DTO_REPO_ID_NAME_MAP, (Serializable) repoIdDocNameMap);
        // display internal doc
        List<DocDisplayDto> internalDocDisplayDto = internalDocClient.getInternalDocForDisplay(dto.getAppId());
        ParamUtil.setRequestAttr(request, KEY_TAB_DOCUMENT_INTERNAL_DOC_LIST, internalDocDisplayDto);
        //decide what kind of page you want
        ParamUtil.setRequestAttr(request, KEY_REQUEST_EXTENSION, dto.getRequestExtension());
    }

    public void bindAction(BaseProcessClass bpc) {
        // do nothing now
    }

    public void handleSubmit(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        InsProcessDto insProcessDto = getInsProcessDto(request);
        ReviewInsFollowUpDto displayDto = getDisplayDto(request);
        //judge the task is request extension or submit follow up data
        //get param from page
        if (displayDto.getRequestExtension().equals(YES)) {
            bindParam(request, insProcessDto);
        } else if (displayDto.getRequestExtension().equals(NO)) {
            insProcessDto.reqObjMapping(request);
        }
        ParamUtil.setSessionAttr(request, KEY_INS_DECISION, insProcessDto);

        ValidationResultDto validationResultDto = inspectionClient.validatePostInspectionDOReviewFollowUpItems(insProcessDto);
        String validateResult;
        if (validationResultDto.isPass()) {
            String processingDecision = insProcessDto.getDecision();
            if (MasterCodeConstants.MOH_PROCESSING_DECISION_ROUTE_REPORT_TO_APPLICANT.equals(processingDecision)) {
                validateResult = "routeBack";
            } else if (MasterCodeConstants.MOH_PROCESSING_DECISION_APPROVE.equals(processingDecision)) {
                validateResult = "acceptResponse";
            } else if (MasterCodeConstants.MOH_PROCESSING_DECISION_SKIP_INSPECTION.equals(processingDecision)) {
                validateResult = "skip";
            } else if (MasterCodeConstants.MOH_PROCESSING_DECISION_REJECT.equals(processingDecision)) {
                validateResult = "reject";
            } else {
                validateResult = "invalid";
            }
        } else {
            log.error("Validation failure info: {}", validationResultDto.toErrorMsg());
            ParamUtil.setRequestAttr(request, ModuleCommonConstants.KEY_VALIDATION_ERRORS, validationResultDto.toErrorMsg());
            ParamUtil.setRequestAttr(request, TAB_ACTIVE, TAB_PROCESSING);
            validateResult = "back";
        }
        ParamUtil.setRequestAttr(request, KEY_ROUTE, validateResult);
    }

    public void routeBackToApplicant(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        String appId = (String) ParamUtil.getSessionAttr(request, KEY_APP_ID);
        String taskId = (String) ParamUtil.getSessionAttr(request, KEY_TASK_ID);
        InsProcessDto insProcessDto = getInsProcessDto(request);
        inspectionClient.doReviewInspectionFollowUpItemsRouteBackToApplicant(appId, taskId, insProcessDto);
        ParamUtil.setRequestAttr(request, KEY_RESULT_MSG, "Your Request for Information has been sent to the Applicant on" + DateUtil.convertToString(LocalDate.now()) + ".");
    }

    /**
     * if the task is request extension,update app data and complete task (and save the new due date)
     * if the task is submit follow up data,update app data and complete task and create new task for Approval Officer
     */
    public void acceptResponse(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        ReviewInsFollowUpDto dto = getDisplayDto(request);
        String appId = (String) ParamUtil.getSessionAttr(request, KEY_APP_ID);
        String taskId = (String) ParamUtil.getSessionAttr(request, KEY_TASK_ID);
        InsProcessDto insProcessDto = getInsProcessDto(request);
        if (dto.getRequestExtension().equals(YES)) {
            //todo specify new due date
            inspectionClient.doReviewInspectionFollowUpItemsRouteBackToApplicant(appId, taskId, insProcessDto);
        } else if (dto.getRequestExtension().equals(NO)) {
            inspectionClient.doReviewInspectionFollowUpItemsAcceptResponse(appId, taskId, insProcessDto);
        }
        ParamUtil.setRequestAttr(request, KEY_RESULT_MSG, "You have successfully follow-up item Verified.");
    }

    /**
     * if the task is request extension,update app data and complete task
     * if the task is submit follow up data,update app data and complete task and create new task for Approval Officer
     */
    public void rejectAction(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        ReviewInsFollowUpDto dto = getDisplayDto(request);
        String appId = (String) ParamUtil.getSessionAttr(request, KEY_APP_ID);
        String taskId = (String) ParamUtil.getSessionAttr(request, KEY_TASK_ID);
        InsProcessDto insProcessDto = getInsProcessDto(request);
        if (dto.getRequestExtension().equals(YES)) {
            inspectionClient.doReviewInspectionFollowUpItemsRouteBackToApplicant(appId, taskId, insProcessDto);
        } else if (dto.getRequestExtension().equals(NO)) {
            // todo
//            inspectionClient.doReviewInspectionFollowUpItemsAcceptResponse(appId, taskId, insProcessDto);
        }
        ParamUtil.setRequestAttr(request, KEY_RESULT_MSG, "You have successfully follow-up item Verified.");
    }

    public void skip(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        String appId = (String) ParamUtil.getSessionAttr(request, KEY_APP_ID);
        String taskId = (String) ParamUtil.getSessionAttr(request, KEY_TASK_ID);
        InsProcessDto insProcessDto = getInsProcessDto(request);
        inspectionClient.skipInspection(appId,taskId,insProcessDto);
    }

    public ReviewInsFollowUpDto getDisplayDto(HttpServletRequest request) {
        ReviewInsFollowUpDto dto = (ReviewInsFollowUpDto) ParamUtil.getSessionAttr(request, KEY_REVIEW_FOLLOW_UP_DTO);
        if (dto == null) {
            dto = new ReviewInsFollowUpDto();
        }
        return dto;
    }

    public InsProcessDto getInsProcessDto(HttpServletRequest request) {
        InsProcessDto dto = (InsProcessDto) ParamUtil.getSessionAttr(request, KEY_INS_DECISION);
        if (dto == null) {
            dto = new InsProcessDto();
        }
        return dto;
    }

    public void bindParam(HttpServletRequest request, InsProcessDto insProcessDto) {
        insProcessDto.setRemark(ParamUtil.getString(request, "remarks"));
        insProcessDto.setDecision(ParamUtil.getString(request, "processingExtensionDecision"));
    }
}

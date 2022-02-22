package sg.gov.moh.iais.egp.bsb.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.AuditTrailConsts;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sg.gov.moh.iais.egp.bsb.client.InspectionClient;
import sg.gov.moh.iais.egp.bsb.client.InternalDocClient;
import sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants;
import sg.gov.moh.iais.egp.bsb.dto.ValidationResultDto;
import sg.gov.moh.iais.egp.bsb.dto.file.DocDisplayDto;
import sg.gov.moh.iais.egp.bsb.dto.inspection.DOReviewFollowUpItemsDto;
import sg.gov.moh.iais.egp.bsb.util.DateUtil;
import sg.gov.moh.iais.egp.bsb.util.MaskHelper;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import java.time.LocalDate;
import java.util.List;

import static sg.gov.moh.iais.egp.bsb.constant.module.InspectionConstants.*;
import static sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants.KEY_TAB_DOCUMENT_INTERNAL_DOC_LIST;
import static sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants.KEY_VALIDATION_ERRORS;

/**
 * @author : LiRan
 * @date : 2022/2/22
 */
@Slf4j
@Delegator("mohDOReviewFollowUpItems")
public class BsbInspectionDOReviewFollowUpItemsDelegator {
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
    }

    public void init(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        HttpSession session = request.getSession();
        session.removeAttribute(KEY_DO_REVIEW_FOLLOW_UP_ITEMS_DTO);

        String appId = (String) ParamUtil.getSessionAttr(request, KEY_APP_ID);

        // DO review inspection follow-up items
        DOReviewFollowUpItemsDto doReviewFollowUpItemsDto = new DOReviewFollowUpItemsDto();
        // TODO need set currentStatus, mohRemarks, applicantRemarks, newDueDate
        ParamUtil.setSessionAttr(request, KEY_DO_REVIEW_FOLLOW_UP_ITEMS_DTO, doReviewFollowUpItemsDto);
    }

    public void prepare(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        String appId = (String) ParamUtil.getSessionAttr(request, KEY_APP_ID);
        List<DocDisplayDto> internalDocDisplayDto = internalDocClient.getInternalDocForDisplay(appId);
        ParamUtil.setRequestAttr(request, KEY_TAB_DOCUMENT_INTERNAL_DOC_LIST, internalDocDisplayDto);
    }

    public void bindAction(BaseProcessClass bpc) {
        // do nothing now
    }

    public void handleSubmit(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        DOReviewFollowUpItemsDto doReviewFollowUpItemsDto = (DOReviewFollowUpItemsDto) ParamUtil.getSessionAttr(request, KEY_DO_REVIEW_FOLLOW_UP_ITEMS_DTO);
        doReviewFollowUpItemsDto.reqObjMapping(request);
        ParamUtil.setSessionAttr(request, KEY_DO_REVIEW_FOLLOW_UP_ITEMS_DTO, doReviewFollowUpItemsDto);

        ValidationResultDto validationResultDto = inspectionClient.validatePostInspectionDOReviewFollowUpItems(doReviewFollowUpItemsDto);
        String validateResult;
        if (validationResultDto.isPass()){
            String processingDecision = doReviewFollowUpItemsDto.getProcessingDecision();
            if (MasterCodeConstants.MOH_PROCESSING_DECISION_ROUTE_REPORT_TO_APPLICANT.equals(processingDecision)){
                validateResult = "routeBack";
            } else if (MasterCodeConstants.MOH_PROCESSING_DECISION_VERIFIED.equals(processingDecision)){
                validateResult = "acceptResponse";
            } else {
                validateResult = "invalid";
            }
        } else {
            log.error("Validation failure info: {}", validationResultDto.toErrorMsg());
            ParamUtil.setRequestAttr(request, KEY_VALIDATION_ERRORS, validationResultDto.toErrorMsg());
            ParamUtil.setRequestAttr(request, TAB_ACTIVE, TAB_PROCESSING);
            validateResult = "back";
        }
        ParamUtil.setRequestAttr(request, KEY_ROUTE, validateResult);
    }

    public void routeBackToApplicant(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        String appId = (String) ParamUtil.getSessionAttr(request, KEY_APP_ID);
        String taskId = (String) ParamUtil.getSessionAttr(request, KEY_TASK_ID);
        DOReviewFollowUpItemsDto doReviewFollowUpItemsDto = (DOReviewFollowUpItemsDto) ParamUtil.getSessionAttr(request, KEY_DO_REVIEW_FOLLOW_UP_ITEMS_DTO);
        //TODO save data
        ParamUtil.setRequestAttr(request, KEY_RESULT_MSG, "Your Request for Information has been sent to the Applicant on" + DateUtil.convertToString(LocalDate.now()) + ".");
    }

    public void acceptResponse(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        String appId = (String) ParamUtil.getSessionAttr(request, KEY_APP_ID);
        String taskId = (String) ParamUtil.getSessionAttr(request, KEY_TASK_ID);
        DOReviewFollowUpItemsDto doReviewFollowUpItemsDto = (DOReviewFollowUpItemsDto) ParamUtil.getSessionAttr(request, KEY_DO_REVIEW_FOLLOW_UP_ITEMS_DTO);
        //TODO save data
        ParamUtil.setRequestAttr(request, KEY_RESULT_MSG, "You have successfully follow-up item Verified.");
    }
}

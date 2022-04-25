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
import sg.gov.moh.iais.egp.bsb.dto.ProcessHistoryDto;
import sg.gov.moh.iais.egp.bsb.dto.file.DocDisplayDto;
import sg.gov.moh.iais.egp.bsb.dto.inspection.InsProcessDto;
import sg.gov.moh.iais.egp.bsb.dto.inspection.InsSubmitFindingDataDto;
import sg.gov.moh.iais.egp.bsb.dto.mohprocessingdisplay.FacilityDetailsInfo;
import sg.gov.moh.iais.egp.bsb.dto.mohprocessingdisplay.SubmissionDetailsInfo;
import sg.gov.moh.iais.egp.bsb.dto.validation.ValidationResultDto;
import sg.gov.moh.iais.egp.bsb.util.MaskHelper;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

import static sg.gov.moh.iais.egp.bsb.constant.module.InspectionConstants.KEY_APP_ID;
import static sg.gov.moh.iais.egp.bsb.constant.module.InspectionConstants.KEY_RESULT_MSG;
import static sg.gov.moh.iais.egp.bsb.constant.module.InspectionConstants.KEY_ROUTE;
import static sg.gov.moh.iais.egp.bsb.constant.module.InspectionConstants.KEY_TASK_ID;
import static sg.gov.moh.iais.egp.bsb.constant.module.InspectionConstants.TAB_ACTIVE;
import static sg.gov.moh.iais.egp.bsb.constant.module.InspectionConstants.TAB_PROCESSING;
import static sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants.KEY_ACTION_VALUE;
import static sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants.KEY_FACILITY_DETAILS_INFO;
import static sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants.KEY_ROUTING_HISTORY_LIST;
import static sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants.KEY_SUBMISSION_DETAILS_INFO;
import static sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants.KEY_TAB_DOCUMENT_INTERNAL_DOC_LIST;
import static sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants.KEY_TAB_DOCUMENT_SUPPORT_DOC_LIST;
import static sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants.KEY_VALIDATION_ERRORS;


@Slf4j
@Delegator("bsbInspectionDO")
public class InspectionDODelegator {
    private final InspectionClient inspectionClient;
    private final InternalDocClient internalDocClient;

    @Autowired
    public InspectionDODelegator(InspectionClient inspectionClient, InternalDocClient internalDocClient) {
        this.inspectionClient = inspectionClient;
        this.internalDocClient = internalDocClient;
    }

    public void start(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        MaskHelper.taskProcessUnmask(request, KEY_APP_ID, KEY_TASK_ID);

        AuditTrailHelper.auditFunction(AuditTrailConsts.MODULE_INSPECTION, AuditTrailConsts.FUNCTION_INSPECTION_CHECKLIST);
    }

    public void init(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        HttpSession session = request.getSession();
        session.removeAttribute(KEY_SUBMISSION_DETAILS_INFO);
        session.removeAttribute(KEY_FACILITY_DETAILS_INFO);
        session.removeAttribute(KEY_TAB_DOCUMENT_SUPPORT_DOC_LIST);
        session.removeAttribute(KEY_TAB_DOCUMENT_INTERNAL_DOC_LIST);
        session.removeAttribute(KEY_ROUTING_HISTORY_LIST);


        String appId = (String) ParamUtil.getSessionAttr(request, KEY_APP_ID);

        InsSubmitFindingDataDto initDataDto = inspectionClient.getInitInsFindingData(appId);

        // facility info and inspection info
        SubmissionDetailsInfo infoDto = initDataDto.getSubmissionDetailsInfo();
        ParamUtil.setSessionAttr(request, KEY_SUBMISSION_DETAILS_INFO, infoDto);


        FacilityDetailsInfo detailsInfo = initDataDto.getFacilityDetailsInfo();
        ParamUtil.setSessionAttr(request, KEY_FACILITY_DETAILS_INFO, detailsInfo);

        List<DocDisplayDto> supportDocList = initDataDto.getSupportDocDisplayDtoList();
        ParamUtil.setSessionAttr(request,KEY_TAB_DOCUMENT_SUPPORT_DOC_LIST,new ArrayList<>(supportDocList));

        List<ProcessHistoryDto> processHistoryDtoList = initDataDto.getProcessHistoryDtoList();
        ParamUtil.setSessionAttr(request,KEY_ROUTING_HISTORY_LIST,new ArrayList<>(processHistoryDtoList));
    }

    public void pre(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        String appId = (String) ParamUtil.getSessionAttr(request, KEY_APP_ID);
        List<DocDisplayDto> internalDocDisplayDto = internalDocClient.getInternalDocForDisplay(appId);
        ParamUtil.setRequestAttr(request, KEY_TAB_DOCUMENT_INTERNAL_DOC_LIST, internalDocDisplayDto);
    }

    public void bindAction(BaseProcessClass bpc) {
        // do nothing now
    }

    public void preChecklist(BaseProcessClass bpc) {
        // do nothing now
    }

    public void changeTab(BaseProcessClass bpc) {
        // do nothing now
    }

    public void saveDraft(BaseProcessClass bpc) {
        // do nothing now
    }

    public void validateAndSaveChecklist(BaseProcessClass bpc) {
        // do nothing now
    }

    public void saveInsFinding(BaseProcessClass bpc) {
        // do nothing now
    }

    public void saveInsOutcome(BaseProcessClass bpc) {
        // do nothing now
    }

    public void handleSubmit(BaseProcessClass bpc) {
        log.info("Officer submit for inspection findings step");
        HttpServletRequest request = bpc.request;
        String actionValue = ParamUtil.getString(request, KEY_ACTION_VALUE);
        String appId = (String) ParamUtil.getSessionAttr(request, KEY_APP_ID);
        ValidationResultDto validationResultDto = inspectionClient.validateActualInspectionFindings(appId);
        String route;
        if (validationResultDto.isPass() || (StringUtils.hasLength(actionValue) && "noValidate".equals(actionValue))) {
            String taskId = (String) ParamUtil.getSessionAttr(request, KEY_TASK_ID);
            inspectionClient.submitInspectionFindingChangeStatusToReport(appId, taskId);
            ParamUtil.setRequestAttr(request, KEY_RESULT_MSG, "You have successfully completed your task");
            route = "submit";
        } else {
            log.info("But not all necessary data has been submitted");
            ParamUtil.setRequestAttr(request, KEY_VALIDATION_ERRORS, validationResultDto.toErrorMsg());
            ParamUtil.setRequestAttr(request, TAB_ACTIVE, TAB_PROCESSING);
            route = "back";
        }
        ParamUtil.setRequestAttr(request, KEY_ROUTE, route);
    }

    public void skip(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        String appId = (String) ParamUtil.getSessionAttr(request, KEY_APP_ID);
        String taskId = (String) ParamUtil.getSessionAttr(request, KEY_TASK_ID);
        inspectionClient.skipInspection(appId,taskId,new InsProcessDto(MasterCodeConstants.MOH_PROCESSING_DECISION_SKIP_INSPECTION));
    }
}

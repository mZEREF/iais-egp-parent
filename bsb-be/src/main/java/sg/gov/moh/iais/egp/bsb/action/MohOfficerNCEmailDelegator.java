package sg.gov.moh.iais.egp.bsb.action;


import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.utils.LogUtil;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.MasterCodeUtil;
import lombok.extern.slf4j.Slf4j;
import sg.gov.moh.iais.egp.bsb.client.InspectionClient;
import sg.gov.moh.iais.egp.bsb.client.InternalDocClient;
import sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants;
import sg.gov.moh.iais.egp.bsb.constant.StageConstants;
import sg.gov.moh.iais.egp.bsb.constant.module.TaskModuleConstants;
import sg.gov.moh.iais.egp.bsb.dto.ProcessHistoryDto;
import sg.gov.moh.iais.egp.bsb.dto.file.DocDisplayDto;
import sg.gov.moh.iais.egp.bsb.dto.inspection.InsNCEmailDto;
import sg.gov.moh.iais.egp.bsb.dto.inspection.InsNCEmailInitDataDto;
import sg.gov.moh.iais.egp.bsb.dto.mohprocessingdisplay.FacilityDetailsInfo;
import sg.gov.moh.iais.egp.bsb.dto.mohprocessingdisplay.SubmissionDetailsInfo;
import sg.gov.moh.iais.egp.bsb.dto.validation.ValidationResultDto;
import sg.gov.moh.iais.egp.bsb.service.AppViewService;
import sg.gov.moh.iais.egp.bsb.util.MaskHelper;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

import static sg.gov.moh.iais.egp.bsb.constant.module.InspectionConstants.KEY_APP_ID;
import static sg.gov.moh.iais.egp.bsb.constant.module.InspectionConstants.KEY_INS_NC_EMAIL_DTO;
import static sg.gov.moh.iais.egp.bsb.constant.module.InspectionConstants.KEY_ROUTE;
import static sg.gov.moh.iais.egp.bsb.constant.module.InspectionConstants.KEY_TASK_ID;
import static sg.gov.moh.iais.egp.bsb.constant.module.InspectionConstants.TAB_ACTIVE;
import static sg.gov.moh.iais.egp.bsb.constant.module.InspectionConstants.TAB_PROCESSING;
import static sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants.KEY_AO;
import static sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants.KEY_APPLICANT;
import static sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants.KEY_DO;
import static sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants.KEY_FACILITY_DETAILS_INFO;
import static sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants.KEY_ROUTING_HISTORY_LIST;
import static sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants.KEY_SELECT_ROUTE_TO_MOH;
import static sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants.KEY_SUBMISSION_DETAILS_INFO;
import static sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants.KEY_TAB_DOCUMENT_INTERNAL_DOC_LIST;
import static sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants.KEY_TAB_DOCUMENT_SUPPORT_DOC_LIST;
import static sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants.KEY_VALIDATION_ERRORS;


@Slf4j
@Delegator("mohOfficerNCEmailDelegator")
public class MohOfficerNCEmailDelegator {
    private final InspectionClient inspectionClient;
    private final InternalDocClient internalDocClient;

    public MohOfficerNCEmailDelegator(InspectionClient inspectionClient, InternalDocClient internalDocClient) {
        this.inspectionClient = inspectionClient;
        this.internalDocClient = internalDocClient;
    }

    public void startDO(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        MaskHelper.taskProcessUnmask(request, KEY_APP_ID, KEY_TASK_ID);
        AuditTrailHelper.auditFunction("Pending DO NC Email Draft", "Pending DO NC Email Draft");
    }

    public void startAO(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        MaskHelper.taskProcessUnmask(request, KEY_APP_ID, KEY_TASK_ID);
        AuditTrailHelper.auditFunction("Pending AO NC Email", "Pending AO NC Email");
    }

    public void init(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        HttpSession session = request.getSession();
        session.removeAttribute(KEY_SUBMISSION_DETAILS_INFO);
        session.removeAttribute(KEY_FACILITY_DETAILS_INFO);
        session.removeAttribute(KEY_ROUTING_HISTORY_LIST);
        session.removeAttribute(KEY_SELECT_ROUTE_TO_MOH);
        session.removeAttribute(KEY_INS_NC_EMAIL_DTO);
        String appId = (String) ParamUtil.getSessionAttr(request,KEY_APP_ID);
        InsNCEmailInitDataDto initDataDto = inspectionClient.getInitInsNCEmailData(appId);

        // submission info
        SubmissionDetailsInfo submissionDetailsInfo = initDataDto.getSubmissionDetailsInfo();
        ParamUtil.setSessionAttr(request, KEY_SUBMISSION_DETAILS_INFO, submissionDetailsInfo);

        // facility details
        FacilityDetailsInfo facilityDetailsInfo = initDataDto.getFacilityDetailsInfo();
        ParamUtil.setSessionAttr(request, KEY_FACILITY_DETAILS_INFO, facilityDetailsInfo);

        //support doc list
        ArrayList<DocDisplayDto> supportDocList = new ArrayList<>(initDataDto.getSupportDocDisplayDtoList());
        ParamUtil.setSessionAttr(request,KEY_TAB_DOCUMENT_SUPPORT_DOC_LIST,supportDocList);

        //routing history
        ArrayList<ProcessHistoryDto> processHistoryDtoList = new ArrayList<>(initDataDto.getProcessHistoryDtoList());
        ParamUtil.setSessionAttr(request, KEY_ROUTING_HISTORY_LIST, processHistoryDtoList);

        //moh officer selection
        ArrayList<SelectOption> selectRouteToMoh = new ArrayList<>(initDataDto.getSelectRouteToMoh());
        ParamUtil.setSessionAttr(request,KEY_SELECT_ROUTE_TO_MOH,selectRouteToMoh);


        InsNCEmailDto ncEmailDto = initDataDto.getInsNCEmailDto();
        ParamUtil.setSessionAttr(request, KEY_INS_NC_EMAIL_DTO,ncEmailDto);
    }

    public void pre(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        String appId = (String) ParamUtil.getSessionAttr(request, KEY_APP_ID);
        List<DocDisplayDto> internalDocDisplayDto = internalDocClient.getInternalDocForDisplay(appId);
        ParamUtil.setRequestAttr(request, KEY_TAB_DOCUMENT_INTERNAL_DOC_LIST, internalDocDisplayDto);
        
        AppViewService.facilityRegistrationViewApp(request, appId);

    }

    public void prePreviewNCEmail(BaseProcessClass bpc){
        //do noting now
        HttpServletRequest request = bpc.request;
        InsNCEmailDto ncEmailDto = (InsNCEmailDto) ParamUtil.getSessionAttr(request,KEY_INS_NC_EMAIL_DTO);
        ncEmailDto.reqObjMapping(request);
        ParamUtil.setSessionAttr(request,KEY_INS_NC_EMAIL_DTO,ncEmailDto);
    }

    public void bindAction(BaseProcessClass bpc) {
        // do nothing now
    }

    public void handleSubmit(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        InsNCEmailDto ncEmailDto = (InsNCEmailDto) ParamUtil.getSessionAttr(request,KEY_INS_NC_EMAIL_DTO);
        ncEmailDto.reqObjMapping(request);
        ParamUtil.setSessionAttr(request,KEY_INS_NC_EMAIL_DTO,ncEmailDto);

        ValidationResultDto validationResultDto = inspectionClient.validateInsNCEmailDto(ncEmailDto);
        String validateResult;
        if (validationResultDto.isPass()) {
            if (MasterCodeConstants.MOH_PROCESSING_DECISION_ROUTE_TO_AO.equals(ncEmailDto.getDecision())) {
                validateResult = "ao";
            }else if(MasterCodeConstants.MOH_PROCESSING_DECISION_APPROVE.equals(ncEmailDto.getDecision())){
                validateResult = "applicant";
            }else if(MasterCodeConstants.MOH_PROCESSING_DECISION_ROUTE_BACK_TO_DO.equals(ncEmailDto.getDecision())){
                validateResult = "do";
            }else {
                validateResult = "invalid";
            }
        } else {
            log.error("Validation failure info: {}", validationResultDto.toErrorMsg());
            ParamUtil.setRequestAttr(request, KEY_VALIDATION_ERRORS, validationResultDto.toErrorMsg());
            ParamUtil.setRequestAttr(request, TAB_ACTIVE, TAB_PROCESSING);
            validateResult = "back";
        }
        log.info("Officer submit decision [{}] for review inspection NCs Rectification report, route result [{}]", LogUtil.escapeCrlf(ncEmailDto.getDecision()), validateResult);
        ParamUtil.setRequestAttr(request, KEY_ROUTE, validateResult);
    }

    public void routeToAO(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        String appId = (String) ParamUtil.getSessionAttr(request, KEY_APP_ID);
        String taskId = (String) ParamUtil.getSessionAttr(request, KEY_TASK_ID);
        InsNCEmailDto ncEmailDto = (InsNCEmailDto) ParamUtil.getSessionAttr(request,KEY_INS_NC_EMAIL_DTO);
        inspectionClient.inspectionNCEmailDORouteToAO(appId,taskId,ncEmailDto);
        ParamUtil.setRequestAttr(request, TaskModuleConstants.KEY_CURRENT_TASK, MasterCodeUtil.getCodeDesc(MasterCodeConstants.APP_STATUS_PEND_DO_NC_EMAIL_DRAFT));
        ParamUtil.setRequestAttr(request,TaskModuleConstants.KEY_NEXT_TASK, MasterCodeUtil.getCodeDesc(MasterCodeConstants.APP_STATUS_PEND_AO_NC_EMAIL_REVIEW));
        ParamUtil.setRequestAttr(request, TaskModuleConstants.KEY_NEXT_ROLE, KEY_AO);
    }

    public void doRouteToApplicant(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        String appId = (String) ParamUtil.getSessionAttr(request, KEY_APP_ID);
        String taskId = (String) ParamUtil.getSessionAttr(request, KEY_TASK_ID);
        InsNCEmailDto ncEmailDto = (InsNCEmailDto) ParamUtil.getSessionAttr(request,KEY_INS_NC_EMAIL_DTO);
        inspectionClient.inspectionNCEmailRouteToApplicant(appId,taskId, StageConstants.ROLE_DO,ncEmailDto);
        ParamUtil.setRequestAttr(request, TaskModuleConstants.KEY_CURRENT_TASK, MasterCodeUtil.getCodeDesc(MasterCodeConstants.APP_STATUS_PEND_DO_NC_EMAIL_DRAFT));
        ParamUtil.setRequestAttr(request,TaskModuleConstants.KEY_NEXT_TASK, MasterCodeUtil.getCodeDesc(MasterCodeConstants.APP_STATUS_PEND_NC_RECTIFICATION));
        ParamUtil.setRequestAttr(request, TaskModuleConstants.KEY_NEXT_ROLE, KEY_APPLICANT);
    }

    public void aoRouteToApplicant(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        String appId = (String) ParamUtil.getSessionAttr(request, KEY_APP_ID);
        String taskId = (String) ParamUtil.getSessionAttr(request, KEY_TASK_ID);
        InsNCEmailDto ncEmailDto = (InsNCEmailDto) ParamUtil.getSessionAttr(request,KEY_INS_NC_EMAIL_DTO);
        inspectionClient.inspectionNCEmailRouteToApplicant(appId,taskId, StageConstants.ROLE_AO,ncEmailDto);
        ParamUtil.setRequestAttr(request, TaskModuleConstants.KEY_CURRENT_TASK, MasterCodeUtil.getCodeDesc(MasterCodeConstants.APP_STATUS_PEND_AO_NC_EMAIL_REVIEW));
        ParamUtil.setRequestAttr(request,TaskModuleConstants.KEY_NEXT_TASK, MasterCodeUtil.getCodeDesc(MasterCodeConstants.APP_STATUS_PEND_NC_RECTIFICATION));
        ParamUtil.setRequestAttr(request, TaskModuleConstants.KEY_NEXT_ROLE, KEY_APPLICANT);
    }

    public void routeToDO(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        String appId = (String) ParamUtil.getSessionAttr(request, KEY_APP_ID);
        String taskId = (String) ParamUtil.getSessionAttr(request, KEY_TASK_ID);
        InsNCEmailDto ncEmailDto = (InsNCEmailDto) ParamUtil.getSessionAttr(request,KEY_INS_NC_EMAIL_DTO);
        inspectionClient.inspectionNCEmailAORouteBackToDO(appId,taskId,ncEmailDto);
        ParamUtil.setRequestAttr(request, TaskModuleConstants.KEY_CURRENT_TASK, MasterCodeUtil.getCodeDesc(MasterCodeConstants.APP_STATUS_PEND_AO_NC_EMAIL_REVIEW));
        ParamUtil.setRequestAttr(request,TaskModuleConstants.KEY_NEXT_TASK, MasterCodeUtil.getCodeDesc(MasterCodeConstants.APP_STATUS_PEND_DO_NC_EMAIL_DRAFT));
        ParamUtil.setRequestAttr(request, TaskModuleConstants.KEY_NEXT_ROLE, KEY_DO);
    }

}

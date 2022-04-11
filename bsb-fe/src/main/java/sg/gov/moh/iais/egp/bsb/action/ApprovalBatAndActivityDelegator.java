package sg.gov.moh.iais.egp.bsb.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.exception.IaisRuntimeException;
import com.ecquaria.cloud.moh.iais.common.utils.LogUtil;
import com.ecquaria.cloud.moh.iais.common.utils.MaskUtil;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import sg.gov.moh.iais.egp.bsb.client.ApprovalBatAndActivityClient;
import sg.gov.moh.iais.egp.bsb.common.node.Node;
import sg.gov.moh.iais.egp.bsb.common.node.NodeGroup;
import sg.gov.moh.iais.egp.bsb.common.node.Nodes;
import sg.gov.moh.iais.egp.bsb.common.node.simple.SimpleNode;
import sg.gov.moh.iais.egp.bsb.dto.file.DocRecordInfo;
import sg.gov.moh.iais.egp.bsb.dto.file.NewDocInfo;
import sg.gov.moh.iais.egp.bsb.dto.file.NewFileSyncDto;
import sg.gov.moh.iais.egp.bsb.dto.info.facility.FacilityBasicInfo;
import sg.gov.moh.iais.egp.bsb.dto.register.approval.*;
import sg.gov.moh.iais.egp.bsb.dto.validation.ValidationResultDto;
import sg.gov.moh.iais.egp.bsb.service.ApprovalBatAndActivityService;
import sg.gov.moh.iais.egp.bsb.service.DocSettingService;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static sg.gov.moh.iais.egp.bsb.constant.module.ApprovalBatAndActivityConstants.*;

/**
 * @author : LiRan
 * @date : 2022/3/17
 */
@Slf4j
@Delegator("approvalBatAndActivityDelegator")
public class ApprovalBatAndActivityDelegator {

    private final ApprovalBatAndActivityService approvalBatAndActivityService;
    private final ApprovalBatAndActivityClient approvalBatAndActivityClient;
    private final DocSettingService docSettingService;

    @Autowired
    public ApprovalBatAndActivityDelegator(ApprovalBatAndActivityService approvalBatAndActivityService, ApprovalBatAndActivityClient approvalBatAndActivityClient, DocSettingService docSettingService) {
        this.approvalBatAndActivityService = approvalBatAndActivityService;
        this.approvalBatAndActivityClient = approvalBatAndActivityClient;
        this.docSettingService = docSettingService;
    }

    public void start(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        HttpSession session = request.getSession();
        session.removeAttribute(KEY_APPROVAL_BAT_AND_ACTIVITY_DTO);
        session.removeAttribute(KEY_ROOT_NODE_GROUP);
        session.removeAttribute(KEY_PROCESS_TYPE);
        AuditTrailHelper.auditFunction("Application for Approval", "Application for Approval");
    }

    public void init(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        boolean newApprovalApp = true;
        // check if we are doing editing
        String maskedAppId = request.getParameter(KEY_EDIT_APP_ID);
        if (StringUtils.hasLength(maskedAppId)) {
            if (log.isInfoEnabled()) {
                log.info("masked app ID: {}", LogUtil.escapeCrlf(maskedAppId));
            }
            newApprovalApp = false;
            String appId = MaskUtil.unMaskValue(KEY_EDIT_APP_ID, maskedAppId);
            if (appId != null && !maskedAppId.equals(appId)) {
                ApprovalBatAndActivityDto editDto = approvalBatAndActivityService.getEditDtoData(appId);
                NodeGroup approvalAppRoot = editDto.toApprovalAppRootGroup(KEY_ROOT_NODE_GROUP);
                ParamUtil.setSessionAttr(request, KEY_ROOT_NODE_GROUP, approvalAppRoot);
                ParamUtil.setSessionAttr(request, KEY_APPROVAL_BAT_AND_ACTIVITY_DTO, editDto);
                // dash bord display
                ParamUtil.setSessionAttr(request, KEY_PROCESS_TYPE, editDto.getApprovalSelectionDto().getProcessType());
            }
        }
        if (newApprovalApp) {
            ParamUtil.setSessionAttr(request, KEY_APPROVAL_BAT_AND_ACTIVITY_DTO, new ApprovalBatAndActivityDto());
        }
    }

    public void preBegin(BaseProcessClass bpc){
        // do nothing now
    }

    public void handleBegin(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        ParamUtil.setSessionAttr(request, KEY_JUMP_DEST_NODE, NODE_NAME_APPROVAL_SELECTION);
    }

    public void preApprovalSelection(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        //TODO: The facility value will be obtained from another method, and the method will be deleted
        List<FacilityBasicInfo> facilityBasicInfoList = approvalBatAndActivityClient.getApprovedFacility();
        List<SelectOption> facilityIdList = new ArrayList<>(facilityBasicInfoList.size());
        if (!CollectionUtils.isEmpty(facilityBasicInfoList)) {
            for (FacilityBasicInfo fac : facilityBasicInfoList) {
                facilityIdList.add(new SelectOption(fac.getId(), fac.getName()));
            }
        }
        ParamUtil.setRequestAttr(request, SELECTION_FACILITY_ID, facilityIdList);

        // data display
        ApprovalBatAndActivityDto approvalBatAndActivityDto = approvalBatAndActivityService.getApprovalBatAndActivityDto(request);
        ApprovalSelectionDto approvalSelectionDto = approvalBatAndActivityDto.getApprovalSelectionDto();
        ParamUtil.setRequestAttr(request, KEY_APPROVAL_SELECTION_DTO, approvalSelectionDto);
    }

    public void handleApprovalSelection(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        // get approvalBatAndActivityDto
        ApprovalBatAndActivityDto approvalBatAndActivityDto = approvalBatAndActivityService.getApprovalBatAndActivityDto(request);
        ApprovalSelectionDto approvalSelectionDto = approvalBatAndActivityDto.getApprovalSelectionDto();
        String oldProcessType = "";
        if (approvalSelectionDto != null) {
            // edit app
            oldProcessType = approvalSelectionDto.getProcessType();
        } else {
            // new app
            approvalSelectionDto = new ApprovalSelectionDto();
            approvalBatAndActivityDto.setApprovalSelectionDto(approvalSelectionDto);
        }
        approvalSelectionDto.reqObjMapping(request);
        // judge jump logic
        String actionType = ParamUtil.getString(request, KEY_ACTION_TYPE);
        String actionValue = ParamUtil.getString(request, KEY_ACTION_VALUE);
        if (KEY_ACTION_JUMP.equals(actionType)) {
            if (KEY_NAV_NEXT.equals(actionValue)) {
                // do next: validate approvalSelectionDto
                ValidationResultDto validationResultDto = approvalBatAndActivityClient.validateApprovalSelectionDto(approvalSelectionDto);
                if (validationResultDto.isPass()) {
                    // get nodeGroup
                    NodeGroup approvalAppRoot;
                    // judge whether the selected processType is the same
                    String newProcessType = approvalSelectionDto.getProcessType();
                    if (StringUtils.hasLength(oldProcessType) && oldProcessType.equals(newProcessType)) {
                        // selected processType is the same
                        approvalAppRoot = (NodeGroup) ParamUtil.getSessionAttr(request, KEY_ROOT_NODE_GROUP);
                    } else {
                        // selected processType is the different
                        approvalAppRoot = approvalBatAndActivityService.newApprovalAppRoot(KEY_ROOT_NODE_GROUP, newProcessType);
                    }
                    ParamUtil.setSessionAttr(request, KEY_ROOT_NODE_GROUP, approvalAppRoot);
                    ParamUtil.setSessionAttr(request, KEY_JUMP_DEST_NODE, NODE_NAME_COMPANY_INFO);
                    ParamUtil.setSessionAttr(request, KEY_APPROVAL_BAT_AND_ACTIVITY_DTO, approvalBatAndActivityDto);
                    // dash bord display
                    ParamUtil.setSessionAttr(request, KEY_PROCESS_TYPE, approvalSelectionDto.getProcessType());
                } else {
                    ParamUtil.setRequestAttr(request, KEY_VALIDATION_ERRORS, validationResultDto.toErrorMsg());
                    ParamUtil.setSessionAttr(request, KEY_JUMP_DEST_NODE, NODE_NAME_APPROVAL_SELECTION);
                }
            } else if (KEY_NAV_BACK.equals(actionValue)) {
                // do back
                ParamUtil.setSessionAttr(request, KEY_JUMP_DEST_NODE, NODE_NAME_BEGIN);
            }
        } else {
            throw new IaisRuntimeException(ERR_MSG_INVALID_ACTION);
        }
    }

    public void preCompanyInfo(BaseProcessClass bpc){
        // do nothing now
    }

    public void handleCompanyInfo(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        String actionType = ParamUtil.getString(request, KEY_ACTION_TYPE);
        String actionValue = ParamUtil.getString(request, KEY_ACTION_VALUE);
        if (KEY_ACTION_JUMP.equals(actionType)) {
            if (KEY_NAV_NEXT.equals(actionValue)) {
                ParamUtil.setSessionAttr(request, KEY_JUMP_DEST_NODE, "appInfo_facProfile");
            } else if (KEY_NAV_BACK.equals(actionValue)) {
                ParamUtil.setSessionAttr(request, KEY_JUMP_DEST_NODE, NODE_NAME_APPROVAL_SELECTION);
            }
        } else {
            throw new IaisRuntimeException(ERR_MSG_INVALID_ACTION);
        }
    }

    public void preFacProfile(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        // data display
        ApprovalBatAndActivityDto approvalBatAndActivityDto = approvalBatAndActivityService.getApprovalBatAndActivityDto(request);
        String facilityId = approvalBatAndActivityDto.getApprovalSelectionDto().getFacilityId();
        FacProfileDto facProfileDto = approvalBatAndActivityService.getFacProfileDtoByFacilityId(facilityId);
        ParamUtil.setRequestAttr(request, KEY_FAC_PROFILE_DTO, facProfileDto);
    }

    public void handleFacProfile(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        NodeGroup approvalAppRoot = approvalBatAndActivityService.getApprovalActivityRoot(request, null);
        String currentNodePath = NODE_NAME_APP_INFO + approvalAppRoot.getPathSeparator() + NODE_NAME_FAC_PROFILE;
        Node facProfileNode = approvalAppRoot.at(currentNodePath);

        String actionType = ParamUtil.getString(request, KEY_ACTION_TYPE);
        if (KEY_ACTION_JUMP.equals(actionType)) {
            String actionValue = ParamUtil.getString(request, KEY_ACTION_VALUE);
            if (actionValue.equals(KEY_NAV_BACK)){
                ParamUtil.setSessionAttr(request, KEY_JUMP_DEST_NODE, NODE_NAME_COMPANY_INFO);
            } else {
                approvalBatAndActivityService.jumpHandler(request, approvalAppRoot, currentNodePath, facProfileNode);
            }
        } else {
            throw new IaisRuntimeException(ERR_MSG_INVALID_ACTION);
        }
        ParamUtil.setSessionAttr(request, KEY_ROOT_NODE_GROUP, approvalAppRoot);
    }

    public void prePossessBatDetails(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        NodeGroup approvalAppRoot = approvalBatAndActivityService.getApprovalActivityRoot(request, null);
        String currentNodePath = NODE_NAME_APP_INFO + approvalAppRoot.getPathSeparator() + NODE_NAME_POSSESS_BAT;
        SimpleNode possessBatNode = (SimpleNode) approvalAppRoot.at(currentNodePath);
        ApprovalToPossessDto approvalToPossessDto = (ApprovalToPossessDto) possessBatNode.getValue();
        Boolean needShowError = (Boolean) ParamUtil.getRequestAttr(request, KEY_SHOW_ERROR_SWITCH);
        if (needShowError == Boolean.TRUE) {
            ParamUtil.setRequestAttr(request, KEY_VALIDATION_ERRORS, approvalToPossessDto.retrieveValidationResult());
        }
        Nodes.needValidation(approvalAppRoot, currentNodePath);
        ParamUtil.setRequestAttr(request, KEY_BAT_INFO, approvalToPossessDto);
    }

    public void handlePossessBatDetails(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        NodeGroup approvalAppRoot = approvalBatAndActivityService.getApprovalActivityRoot(request, null);
        String currentNodePath = NODE_NAME_APP_INFO + approvalAppRoot.getPathSeparator() + NODE_NAME_POSSESS_BAT;
        SimpleNode possessBatNode = (SimpleNode) approvalAppRoot.at(currentNodePath);
        ApprovalToPossessDto approvalToPossessDto = (ApprovalToPossessDto) possessBatNode.getValue();
        approvalToPossessDto.reqObjMapping(request);

        String actionType = ParamUtil.getString(request, KEY_ACTION_TYPE);
        if (KEY_ACTION_JUMP.equals(actionType)) {
            approvalBatAndActivityService.jumpHandler(request, approvalAppRoot, currentNodePath, possessBatNode);
        } else if (KEY_ACTION_SAVE_AS_DRAFT.equals(actionType)) {
            ParamUtil.setRequestAttr(request, KEY_ACTION_TYPE, KEY_ACTION_SAVE_AS_DRAFT);
            ParamUtil.setSessionAttr(request, KEY_JUMP_DEST_NODE, currentNodePath);
        } else {
            throw new IaisRuntimeException(ERR_MSG_INVALID_ACTION);
        }
        ParamUtil.setSessionAttr(request, KEY_ROOT_NODE_GROUP, approvalAppRoot);
    }

    public void preLargeBatDetails(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        NodeGroup approvalAppRoot = approvalBatAndActivityService.getApprovalActivityRoot(request, null);
        String currentNodePath = NODE_NAME_APP_INFO + approvalAppRoot.getPathSeparator() + NODE_NAME_LARGE_BAT;
        SimpleNode largeBatNode = (SimpleNode) approvalAppRoot.at(currentNodePath);
        ApprovalToLargeDto approvalToLargeDto = (ApprovalToLargeDto) largeBatNode.getValue();
        Boolean needShowError = (Boolean) ParamUtil.getRequestAttr(request, KEY_SHOW_ERROR_SWITCH);
        if (needShowError == Boolean.TRUE) {
            ParamUtil.setRequestAttr(request, KEY_VALIDATION_ERRORS, approvalToLargeDto.retrieveValidationResult());
        }
        Nodes.needValidation(approvalAppRoot, currentNodePath);
        ParamUtil.setRequestAttr(request, KEY_BAT_INFO, approvalToLargeDto);
    }

    public void handleLargeBatDetails(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        NodeGroup approvalAppRoot = approvalBatAndActivityService.getApprovalActivityRoot(request, null);
        String currentNodePath = NODE_NAME_APP_INFO + approvalAppRoot.getPathSeparator() + NODE_NAME_LARGE_BAT;
        SimpleNode largeBatNode = (SimpleNode) approvalAppRoot.at(currentNodePath);
        ApprovalToLargeDto approvalToLargeDto = (ApprovalToLargeDto) largeBatNode.getValue();
        approvalToLargeDto.reqObjMapping(request);

        String actionType = ParamUtil.getString(request, KEY_ACTION_TYPE);
        if (KEY_ACTION_JUMP.equals(actionType)) {
            approvalBatAndActivityService.jumpHandler(request, approvalAppRoot, currentNodePath, largeBatNode);
        } else if (KEY_ACTION_SAVE_AS_DRAFT.equals(actionType)) {
            ParamUtil.setRequestAttr(request, KEY_ACTION_TYPE, KEY_ACTION_SAVE_AS_DRAFT);
            ParamUtil.setSessionAttr(request, KEY_JUMP_DEST_NODE, currentNodePath);
        } else {
            throw new IaisRuntimeException(ERR_MSG_INVALID_ACTION);
        }
        ParamUtil.setSessionAttr(request, KEY_ROOT_NODE_GROUP, approvalAppRoot);
    }

    public void preSpecialBatDetails(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        NodeGroup approvalAppRoot = approvalBatAndActivityService.getApprovalActivityRoot(request, null);
        String currentNodePath = NODE_NAME_APP_INFO + approvalAppRoot.getPathSeparator() + NODE_NAME_SPECIAL_BAT;
        SimpleNode specialBatNode = (SimpleNode) approvalAppRoot.at(currentNodePath);
        ApprovalToSpecialDto approvalToSpecialDto = (ApprovalToSpecialDto) specialBatNode.getValue();
        Boolean needShowError = (Boolean) ParamUtil.getRequestAttr(request, KEY_SHOW_ERROR_SWITCH);
        if (needShowError == Boolean.TRUE) {
            ParamUtil.setRequestAttr(request, KEY_VALIDATION_ERRORS, approvalToSpecialDto.retrieveValidationResult());
        }
        Nodes.needValidation(approvalAppRoot, currentNodePath);
        ParamUtil.setRequestAttr(request, KEY_BAT_INFO, approvalToSpecialDto);
    }

    public void handleSpecialBatDetails(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        NodeGroup approvalAppRoot = approvalBatAndActivityService.getApprovalActivityRoot(request, null);
        String currentNodePath = NODE_NAME_APP_INFO + approvalAppRoot.getPathSeparator() + NODE_NAME_SPECIAL_BAT;
        SimpleNode specialBatNode = (SimpleNode) approvalAppRoot.at(currentNodePath);
        ApprovalToSpecialDto approvalToSpecialDto = (ApprovalToSpecialDto) specialBatNode.getValue();
        approvalToSpecialDto.reqObjMapping(request);

        String actionType = ParamUtil.getString(request, KEY_ACTION_TYPE);
        if (KEY_ACTION_JUMP.equals(actionType)) {
            approvalBatAndActivityService.jumpHandler(request, approvalAppRoot, currentNodePath, specialBatNode);
        } else if (KEY_ACTION_SAVE_AS_DRAFT.equals(actionType)) {
            ParamUtil.setRequestAttr(request, KEY_ACTION_TYPE, KEY_ACTION_SAVE_AS_DRAFT);
            ParamUtil.setSessionAttr(request, KEY_JUMP_DEST_NODE, currentNodePath);
        } else {
            throw new IaisRuntimeException(ERR_MSG_INVALID_ACTION);
        }
        ParamUtil.setSessionAttr(request, KEY_ROOT_NODE_GROUP, approvalAppRoot);
    }

    public void preFacAuthorised(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        NodeGroup approvalAppRoot = approvalBatAndActivityService.getApprovalActivityRoot(request, null);
        String currentNodePath = NODE_NAME_APP_INFO + approvalAppRoot.getPathSeparator() + NODE_NAME_FAC_AUTHORISED;
        SimpleNode facAuthorisedNode = (SimpleNode) approvalAppRoot.at(currentNodePath);
        FacAuthorisedDto facAuthorisedDto = (FacAuthorisedDto) facAuthorisedNode.getValue();
        Boolean needShowError = (Boolean) ParamUtil.getRequestAttr(request, KEY_SHOW_ERROR_SWITCH);
        if (needShowError == Boolean.TRUE) {
            ParamUtil.setRequestAttr(request, KEY_VALIDATION_ERRORS, facAuthorisedDto.retrieveValidationResult());
        }
        Nodes.needValidation(approvalAppRoot, currentNodePath);
        ParamUtil.setRequestAttr(request, KEY_FAC_AUTHORISED_DTO, facAuthorisedDto);
    }

    public void handleFacAuthorised(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        NodeGroup approvalAppRoot = approvalBatAndActivityService.getApprovalActivityRoot(request, null);
        String currentNodePath = NODE_NAME_APP_INFO + approvalAppRoot.getPathSeparator() + NODE_NAME_FAC_AUTHORISED;
        SimpleNode facAuthorisedNode = (SimpleNode) approvalAppRoot.at(currentNodePath);
        FacAuthorisedDto facAuthorisedDto = (FacAuthorisedDto) facAuthorisedNode.getValue();
        facAuthorisedDto.reqObjMapping(request);

        String actionType = ParamUtil.getString(request, KEY_ACTION_TYPE);
        if (KEY_ACTION_JUMP.equals(actionType)) {
            approvalBatAndActivityService.jumpHandler(request, approvalAppRoot, currentNodePath, facAuthorisedNode);
        } else if (KEY_ACTION_SAVE_AS_DRAFT.equals(actionType)) {
            ParamUtil.setRequestAttr(request, KEY_ACTION_TYPE, KEY_ACTION_SAVE_AS_DRAFT);
            ParamUtil.setSessionAttr(request, KEY_JUMP_DEST_NODE, currentNodePath);
        } else {
            throw new IaisRuntimeException(ERR_MSG_INVALID_ACTION);
        }
        ParamUtil.setSessionAttr(request, KEY_ROOT_NODE_GROUP, approvalAppRoot);
    }

    public void preActivityDetails(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        NodeGroup approvalAppRoot = approvalBatAndActivityService.getApprovalActivityRoot(request, null);
        String currentNodePath = NODE_NAME_APP_INFO + approvalAppRoot.getPathSeparator() + NODE_NAME_FAC_ACTIVITY;
        SimpleNode facActivityNode = (SimpleNode) approvalAppRoot.at(currentNodePath);
        ApprovalToActivityDto approvalToActivityDto = (ApprovalToActivityDto) facActivityNode.getValue();
        Boolean needShowError = (Boolean) ParamUtil.getRequestAttr(request, KEY_SHOW_ERROR_SWITCH);
        if (needShowError == Boolean.TRUE) {
            ParamUtil.setRequestAttr(request, KEY_VALIDATION_ERRORS, approvalToActivityDto.retrieveValidationResult());
        }
        Nodes.needValidation(approvalAppRoot, currentNodePath);
        ParamUtil.setRequestAttr(request, KEY_APPROVAL_TO_ACTIVITY_DTO, approvalToActivityDto);
        // get facilityId
        ApprovalBatAndActivityDto approvalBatAndActivityDto = approvalBatAndActivityService.getApprovalBatAndActivityDto(request);
        ApprovalSelectionDto approvalSelectionDto = approvalBatAndActivityDto.getApprovalSelectionDto();
        String facilityId = approvalSelectionDto.getFacilityId();
        // show that belongs to this classification without approval facActivityType
        List<String> notExistFacActivityTypeApprovalList = approvalBatAndActivityClient.getNotApprovalActivities(facilityId);
        ParamUtil.setRequestAttr(request, KEY_NOT_EXIST_FAC_ACTIVITY_TYPE_APPROVAL_LIST, notExistFacActivityTypeApprovalList);
    }

    public void handleActivityDetails(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        NodeGroup approvalAppRoot = approvalBatAndActivityService.getApprovalActivityRoot(request, null);
        String currentNodePath = NODE_NAME_APP_INFO + approvalAppRoot.getPathSeparator() + NODE_NAME_FAC_ACTIVITY;
        SimpleNode facActivityNode = (SimpleNode) approvalAppRoot.at(currentNodePath);
        ApprovalToActivityDto approvalToActivityDto = (ApprovalToActivityDto) facActivityNode.getValue();
        approvalToActivityDto.reqObjMapping(request);

        String actionType = ParamUtil.getString(request, KEY_ACTION_TYPE);
        if (KEY_ACTION_JUMP.equals(actionType)) {
            approvalBatAndActivityService.jumpHandler(request, approvalAppRoot, currentNodePath, facActivityNode);
        } else if (KEY_ACTION_SAVE_AS_DRAFT.equals(actionType)) {
            ParamUtil.setRequestAttr(request, KEY_ACTION_TYPE, KEY_ACTION_SAVE_AS_DRAFT);
            ParamUtil.setSessionAttr(request, KEY_JUMP_DEST_NODE, currentNodePath);
        } else {
            throw new IaisRuntimeException(ERR_MSG_INVALID_ACTION);
        }
        ParamUtil.setSessionAttr(request, KEY_ROOT_NODE_GROUP, approvalAppRoot);
    }

    public void prePrimaryDoc(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        String processType = (String) ParamUtil.getSessionAttr(request, KEY_PROCESS_TYPE);
        NodeGroup approvalAppRoot = approvalBatAndActivityService.getApprovalActivityRoot(request, null);
        SimpleNode primaryDocNode = (SimpleNode) approvalAppRoot.at(NODE_NAME_PRIMARY_DOC);
        PrimaryDocDto primaryDocDto = (PrimaryDocDto) primaryDocNode.getValue();
        Boolean needShowError = (Boolean) ParamUtil.getRequestAttr(request, KEY_SHOW_ERROR_SWITCH);
        if (needShowError == Boolean.TRUE) {
            ParamUtil.setRequestAttr(request, KEY_VALIDATION_ERRORS, primaryDocDto.retrieveValidationResult());
        }
        Nodes.needValidation(approvalAppRoot, NODE_NAME_PRIMARY_DOC);

        ParamUtil.setRequestAttr(request, "docSettings", docSettingService.getApprovalAppDocSettings(processType));

        Map<String, List<DocRecordInfo>> savedFiles = primaryDocDto.getExistDocTypeMap();
        Map<String, List<NewDocInfo>> newFiles = primaryDocDto.getNewDocTypeMap();
        ParamUtil.setRequestAttr(request, "savedFiles", savedFiles);
        ParamUtil.setRequestAttr(request, "newFiles", newFiles);
    }

    public void handlePrimaryDoc(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        NodeGroup approvalAppRoot = approvalBatAndActivityService.getApprovalActivityRoot(request, null);
        SimpleNode primaryDocNode = (SimpleNode) approvalAppRoot.at(NODE_NAME_PRIMARY_DOC);
        PrimaryDocDto primaryDocDto = (PrimaryDocDto) primaryDocNode.getValue();
        primaryDocDto.reqObjMapping(request);

        String actionType = ParamUtil.getString(request, KEY_ACTION_TYPE);
        if (KEY_ACTION_JUMP.equals(actionType)) {
            approvalBatAndActivityService.jumpHandler(request, approvalAppRoot, NODE_NAME_PRIMARY_DOC, primaryDocNode);
        } else if (KEY_ACTION_SAVE_AS_DRAFT.equals(actionType)) {
            ParamUtil.setRequestAttr(request, KEY_ACTION_TYPE, KEY_ACTION_SAVE_AS_DRAFT);
            ParamUtil.setSessionAttr(request, KEY_JUMP_DEST_NODE, NODE_NAME_PRIMARY_DOC);
        } else {
            throw new IaisRuntimeException(ERR_MSG_INVALID_ACTION);
        }
        ParamUtil.setSessionAttr(request, KEY_ROOT_NODE_GROUP, approvalAppRoot);
    }

    public void prePreview(BaseProcessClass bpc){
        approvalBatAndActivityService.preparePreviewData(bpc);
    }

    public void handlePreview(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        NodeGroup approvalAppRoot = approvalBatAndActivityService.getApprovalActivityRoot(request, null);
        SimpleNode previewNode = (SimpleNode) approvalAppRoot.at(NODE_NAME_PREVIEW);
        PreviewDto previewDto = (PreviewDto) previewNode.getValue();
        previewDto.reqObjMapping(request);

        String actionType = ParamUtil.getString(request, KEY_ACTION_TYPE);
        String actionValue = ParamUtil.getString(request, KEY_ACTION_VALUE);
        if (KEY_ACTION_JUMP.equals(actionType)) {
            if (KEY_NAV_NEXT.equals(actionValue)) {
                if (previewNode.doValidation()) {
                    previewNode.passValidation();

                    // save docs
                    log.info("Save documents into file-repo");
                    PrimaryDocDto primaryDocDto = (PrimaryDocDto) ((SimpleNode) approvalAppRoot.at(NODE_NAME_PREVIEW)).getValue();
                    List<NewFileSyncDto> newFilesToSync = approvalBatAndActivityService.saveNewUploadedDoc(primaryDocDto);

                    //TODO: save data
                    log.info("Save approval application data");

                    try {
                        // delete docs
                        log.info("Delete already saved documents in file-repo");
                        List<String> toBeDeletedRepoIds = approvalBatAndActivityService.deleteUnwantedDoc(primaryDocDto);
                        // sync docs
                        approvalBatAndActivityService.syncNewDocsAndDeleteFiles(newFilesToSync, toBeDeletedRepoIds);
                    } catch (Exception e) {
                        log.error("Fail to synchronize documents", e);
                    }

                    ParamUtil.setRequestAttr(request, KEY_ACTION_TYPE, KEY_ACTION_SUBMIT);
                } else {
                    ParamUtil.setRequestAttr(request, KEY_SHOW_ERROR_SWITCH, Boolean.TRUE);
                    ParamUtil.setSessionAttr(request, KEY_JUMP_DEST_NODE, NODE_NAME_PREVIEW);
                }
            } else {
                approvalBatAndActivityService.jumpHandler(request, approvalAppRoot, NODE_NAME_PREVIEW, previewNode);
            }
        } else if (KEY_ACTION_SAVE_AS_DRAFT.equals(actionType)) {
            ParamUtil.setRequestAttr(request, KEY_ACTION_TYPE, KEY_ACTION_SAVE_AS_DRAFT);
            ParamUtil.setSessionAttr(request, KEY_JUMP_DEST_NODE, NODE_NAME_PREVIEW);
        } else {
            throw new IaisRuntimeException(ERR_MSG_INVALID_ACTION);
        }
        ParamUtil.setSessionAttr(request, KEY_ROOT_NODE_GROUP, approvalAppRoot);
    }

    public void actionFilter(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        String actionType = (String) ParamUtil.getRequestAttr(request, KEY_ACTION_TYPE);
        if (!StringUtils.hasLength(actionType)) {
            actionType = ParamUtil.getString(request, KEY_ACTION_TYPE);
        } else {
            if (KEY_ACTION_SAVE_AS_DRAFT.equals(actionType)) {
                actionType = KEY_ACTION_JUMP;
                approvalBatAndActivityService.saveDraft(request);
            }
        }
        ParamUtil.setRequestAttr(request, KEY_INDEED_ACTION_TYPE, actionType);
    }

    public void jumpFilter(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        String destNode = (String) ParamUtil.getSessionAttr(request, KEY_JUMP_DEST_NODE);
        ParamUtil.setRequestAttr(request, KEY_DEST_NODE_ROUTE, destNode);
    }

    public void preAcknowledge(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
    }

    public void print(BaseProcessClass bpc){
        approvalBatAndActivityService.preparePreviewData(bpc);
    }

}

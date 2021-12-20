package sg.gov.moh.iais.egp.bsb.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.AuditTrailConsts;
import com.ecquaria.cloud.moh.iais.common.dto.AuditTrailDto;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.filerepo.FileRepoDto;
import com.ecquaria.cloud.moh.iais.common.exception.IaisRuntimeException;
import com.ecquaria.cloud.moh.iais.common.utils.MaskUtil;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import sg.gov.moh.iais.egp.bsb.client.BsbFileClient;
import sg.gov.moh.iais.egp.bsb.client.FacilityRegisterClient;
import sg.gov.moh.iais.egp.bsb.client.FileRepoClient;
import sg.gov.moh.iais.egp.bsb.common.node.Node;
import sg.gov.moh.iais.egp.bsb.common.node.NodeGroup;
import sg.gov.moh.iais.egp.bsb.common.node.Nodes;
import sg.gov.moh.iais.egp.bsb.common.node.simple.SimpleNode;
import sg.gov.moh.iais.egp.bsb.dto.ResponseDto;
import sg.gov.moh.iais.egp.bsb.dto.file.DocRecordInfo;
import sg.gov.moh.iais.egp.bsb.dto.file.FileRepoSyncDto;
import sg.gov.moh.iais.egp.bsb.dto.file.NewDocInfo;
import sg.gov.moh.iais.egp.bsb.dto.file.NewFileSyncDto;
import sg.gov.moh.iais.egp.bsb.dto.register.facility.*;
import sg.gov.moh.iais.egp.bsb.dto.renewal.FacilityRegistrationReviewDto;
import sg.gov.moh.iais.egp.bsb.service.FacilityRegistrationService;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static sg.gov.moh.iais.egp.bsb.constant.FacRegisterConstants.*;

/**
 * @author : LiRan
 * @date : 2021/12/11
 */
@Slf4j
@Delegator("renewalFacilityRegisterDelegator")
public class RenewalFacilityRegistrationDelegator {
    private static final String MODULE_NAME = "Renewal Facility Registration";

    private final FacilityRegisterClient facRegClient;
    private final FileRepoClient fileRepoClient;
    private final BsbFileClient bsbFileClient;
    private final FacilityRegistrationService facilityRegistrationService;

    public RenewalFacilityRegistrationDelegator(FacilityRegisterClient facRegClient, FileRepoClient fileRepoClient,
                                                BsbFileClient bsbFileClient, FacilityRegistrationService facilityRegistrationService) {
        this.facRegClient = facRegClient;
        this.fileRepoClient = fileRepoClient;
        this.bsbFileClient = bsbFileClient;
        this.facilityRegistrationService = facilityRegistrationService;
    }

    public void start(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        request.getSession().removeAttribute(KEY_ROOT_NODE_GROUP);
        request.getSession().removeAttribute(KEY_INSTRUCTION_INFO);
        request.getSession().removeAttribute(KEY_RENEWAL_VIEW_APPROVAL_ROOT_NODE_GROUP);
        AuditTrailHelper.auditFunction(MODULE_NAME, MODULE_NAME);
    }

    public void init(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        String maskedAppId = request.getParameter(KEY_EDIT_APP_ID);
        if (StringUtils.hasLength(maskedAppId)) {
            if (log.isInfoEnabled()) {
                log.info("masked app ID: {}", org.apache.commons.lang.StringUtils.normalizeSpace(maskedAppId));
            }
            boolean failRetrieveEditData = true;
            String appId = MaskUtil.unMaskValue(KEY_EDIT_APP_ID, maskedAppId);
            if (appId != null && !maskedAppId.equals(appId)) {
                ResponseDto<FacilityRegisterDto> resultDto = facRegClient.getRenewalFacRegAppDataByApprovalId(appId);
                if (resultDto.ok()) {
                    failRetrieveEditData = false;
                    FacilityRegisterDto facilityRegisterDto = resultDto.getEntity();
                    NodeGroup viewApprovalRoot = facilityRegisterDto.toRenewalReviewRootGroup(KEY_RENEWAL_VIEW_APPROVAL_ROOT_NODE_GROUP);
                    NodeGroup facRegRoot = facilityRegisterDto.toRenewalFacRegRootGroup(KEY_ROOT_NODE_GROUP);
                    ParamUtil.setSessionAttr(request, KEY_RENEWAL_VIEW_APPROVAL_ROOT_NODE_GROUP, viewApprovalRoot);
                    ParamUtil.setSessionAttr(request, KEY_ROOT_NODE_GROUP, facRegRoot);
                    ParamUtil.setSessionAttr(request, KEY_INSTRUCTION_INFO, facilityRegisterDto.getInstructionDto());
                }
            }
            if (failRetrieveEditData) {
                throw new IaisRuntimeException("Fail to retrieve app data");
            }
        }
    }

    public void preInstruction(BaseProcessClass bpc) {
        // do nothing now
    }

    public void preReview(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        NodeGroup viewApprovalRoot = (NodeGroup) ParamUtil.getSessionAttr(request, KEY_RENEWAL_VIEW_APPROVAL_ROOT_NODE_GROUP);
        SimpleNode reviewNode = (SimpleNode) viewApprovalRoot.at(NODE_NAME_REVIEW);
        FacilityRegistrationReviewDto facilityRegistrationReviewDto = (FacilityRegistrationReviewDto) reviewNode.getValue();
        Boolean needShowError = (Boolean) ParamUtil.getRequestAttr(request, KEY_SHOW_ERROR_SWITCH);
        if (needShowError == Boolean.TRUE) {
            ParamUtil.setRequestAttr(request, KEY_VALIDATION_ERRORS, facilityRegistrationReviewDto.retrieveValidationResult());
        }
        Nodes.needValidation(viewApprovalRoot, NODE_NAME_REVIEW);
        ParamUtil.setRequestAttr(request, NODE_NAME_REVIEW, facilityRegistrationReviewDto);

        NodeGroup facRegRoot = (NodeGroup) ParamUtil.getSessionAttr(request, KEY_ROOT_NODE_GROUP);
        ParamUtil.setRequestAttr(request, NODE_NAME_FAC_PROFILE, ((SimpleNode)facRegRoot.at(NODE_NAME_FAC_INFO + viewApprovalRoot.getPathSeparator() + NODE_NAME_FAC_PROFILE)).getValue());
        ParamUtil.setRequestAttr(request, NODE_NAME_FAC_OPERATOR, ((SimpleNode)facRegRoot.at(NODE_NAME_FAC_INFO + viewApprovalRoot.getPathSeparator() + NODE_NAME_FAC_OPERATOR)).getValue());
        ParamUtil.setRequestAttr(request, NODE_NAME_FAC_AUTH, ((SimpleNode)facRegRoot.at(NODE_NAME_FAC_INFO + viewApprovalRoot.getPathSeparator() + NODE_NAME_FAC_AUTH)).getValue());
        ParamUtil.setRequestAttr(request, NODE_NAME_FAC_ADMIN, ((SimpleNode)facRegRoot.at(NODE_NAME_FAC_INFO + viewApprovalRoot.getPathSeparator() + NODE_NAME_FAC_ADMIN)).getValue());
        ParamUtil.setRequestAttr(request, NODE_NAME_FAC_OFFICER, ((SimpleNode)facRegRoot.at(NODE_NAME_FAC_INFO + viewApprovalRoot.getPathSeparator() + NODE_NAME_FAC_OFFICER)).getValue());
        ParamUtil.setRequestAttr(request, NODE_NAME_FAC_COMMITTEE, ((SimpleNode)facRegRoot.at(NODE_NAME_FAC_INFO + viewApprovalRoot.getPathSeparator() + NODE_NAME_FAC_COMMITTEE)).getValue());

        NodeGroup batNodeGroup = (NodeGroup) facRegRoot.at(NODE_NAME_FAC_BAT_INFO);
        List<BiologicalAgentToxinDto> batList = FacilityRegistrationService.getBatInfoList(batNodeGroup);
        ParamUtil.setRequestAttr(request, "batList", batList);

        ParamUtil.setRequestAttr(request, "docSettings", facilityRegistrationService.getFacRegDocSettings());
        PrimaryDocDto primaryDocDto = (PrimaryDocDto) ((SimpleNode)facRegRoot.at(NODE_NAME_PRIMARY_DOC)).getValue();
        Map<String, List<DocRecordInfo>> savedFiles = primaryDocDto.getExistDocTypeMap();
        Map<String, List<NewDocInfo>> newFiles = primaryDocDto.getNewDocTypeMap();
        ParamUtil.setRequestAttr(request, "savedFiles", savedFiles);
        ParamUtil.setRequestAttr(request, "newFiles", newFiles);

        List<SelectOption> approvedFacCertifierOps = new ArrayList<>(0);
        ParamUtil.setRequestAttr(request, "approvedFacCertifierOps", approvedFacCertifierOps);
    }

    public void preCompInfo(BaseProcessClass bpc) {
        facilityRegistrationService.preCompInfo(bpc);
    }

    public void preServiceSelection(BaseProcessClass bpc) {
        facilityRegistrationService.preServiceSelection(bpc);
    }

    public void preFacProfile(BaseProcessClass bpc) {
        facilityRegistrationService.preFacProfile(bpc);
    }

    public void preFacOperator(BaseProcessClass bpc) {
        facilityRegistrationService.preFacOperator(bpc);
    }

    public void preFacInfoAuthoriser(BaseProcessClass bpc) {
        facilityRegistrationService.preFacInfoAuthoriser(bpc);
    }

    public void preFacAdmin(BaseProcessClass bpc) {
        facilityRegistrationService.preFacAdmin(bpc);
    }

    public void preFacOfficer(BaseProcessClass bpc) {
        facilityRegistrationService.preFacOfficer(bpc);
    }

    public void preFacInfoCommittee(BaseProcessClass bpc) {
        facilityRegistrationService.preFacInfoCommittee(bpc);
    }

    public void preBAToxin(BaseProcessClass bpc) {
        facilityRegistrationService.preBAToxin(bpc);
    }

    public void preOtherAppInfo(BaseProcessClass bpc) {
        facilityRegistrationService.preOtherAppInfo(bpc);
    }

    public void prePrimaryDoc(BaseProcessClass bpc) {
        facilityRegistrationService.prePrimaryDoc(bpc);
    }

    public void handleInstruction(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        NodeGroup viewApprovalRoot = (NodeGroup) ParamUtil.getSessionAttr(request, KEY_RENEWAL_VIEW_APPROVAL_ROOT_NODE_GROUP);
        Node instructionNode = viewApprovalRoot.getNode(NODE_NAME_INSTRUCTION);
        String actionType = ParamUtil.getString(request, KEY_ACTION_TYPE);
        if (KEY_ACTION_JUMP.equals(actionType)) {
            facilityRegistrationService.jumpHandler(request, viewApprovalRoot, NODE_NAME_INSTRUCTION, instructionNode);
        } else {
            throw new IaisRuntimeException(ERR_MSG_INVALID_ACTION);
        }
        ParamUtil.setSessionAttr(request, KEY_RENEWAL_VIEW_APPROVAL_ROOT_NODE_GROUP, viewApprovalRoot);
    }

    public void handleReview(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        NodeGroup viewApprovalRoot = (NodeGroup) ParamUtil.getSessionAttr(request, KEY_RENEWAL_VIEW_APPROVAL_ROOT_NODE_GROUP);
        NodeGroup facRegRoot = (NodeGroup) ParamUtil.getSessionAttr(request, KEY_ROOT_NODE_GROUP);
        SimpleNode reviewNode = (SimpleNode) viewApprovalRoot.at(NODE_NAME_REVIEW);
        FacilityRegistrationReviewDto facilityRegistrationReviewDto = (FacilityRegistrationReviewDto) reviewNode.getValue();
        facilityRegistrationReviewDto.reqObjMapping(request);

        String actionType = ParamUtil.getString(request, KEY_ACTION_TYPE);
        String actionValue = ParamUtil.getString(request, KEY_ACTION_VALUE);

        //there has three action button, 'nav tab jump', 'submit', 'back', 'reviewEdit'
        if (KEY_ACTION_REVIEW_EDIT.equals(actionType)){// 'reviewEdit'
            String destNode = facilityRegistrationService.computeDestNodePath(facRegRoot, actionValue);
            String checkedDestNode = Nodes.jump(facRegRoot, destNode);
            if (!checkedDestNode.equals(destNode)) {
                ParamUtil.setRequestAttr(request, KEY_SHOW_ERROR_SWITCH, Boolean.TRUE);
            }
            ParamUtil.setSessionAttr(request, KEY_JUMP_DEST_NODE, checkedDestNode);
            ParamUtil.setRequestAttr(request, KEY_ACTION_TYPE, KEY_ACTION_JUMP);
        } else if (KEY_ACTION_JUMP.equals(actionType)) {
            if (KEY_NAV_NEXT.equals(actionValue)) {// 'submit'
                if (reviewNode.doValidation()) {
                    reviewNode.passValidation();

                    // save docs
                    SimpleNode primaryDocNode = (SimpleNode) facRegRoot.at(NODE_NAME_PRIMARY_DOC);
                    PrimaryDocDto primaryDocDto = (PrimaryDocDto) primaryDocNode.getValue();
                    List<NewFileSyncDto> newFilesToSync = null;
                    if (!primaryDocDto.getNewDocMap().isEmpty()) {
                        MultipartFile[] files = primaryDocDto.getNewDocMap().values().stream().map(NewDocInfo::getMultipartFile).toArray(MultipartFile[]::new);
                        List<String> repoIds = fileRepoClient.saveFiles(files).getEntity();
                        newFilesToSync = primaryDocDto.newFileSaved(repoIds);
                    }

                    // save data
                    FacilityRegisterDto finalAllDataDto = FacilityRegisterDto.fromRenewal(viewApprovalRoot, facRegRoot);
                    AuditTrailDto auditTrailDto = (AuditTrailDto) ParamUtil.getSessionAttr(request, AuditTrailConsts.SESSION_ATTR_PARAM_NAME);
                    finalAllDataDto.setAuditTrailDto(auditTrailDto);
                    ResponseDto<String> responseDto = facRegClient.saveRenewalRegisteredFacility(finalAllDataDto);
                    log.info("save renewal facility response: {}", org.apache.commons.lang.StringUtils.normalizeSpace(responseDto.toString()));

                    try {
                        // sync files to BE file-repo (save new added files, delete useless files)
                        if ((newFilesToSync != null && !newFilesToSync.isEmpty()) || !primaryDocDto.getToBeDeletedRepoIds().isEmpty()) {
                            /* Ignore the failure of sync files currently.
                             * We should add a mechanism to retry synchronization of files in the future */
                            FileRepoSyncDto syncDto = new FileRepoSyncDto();
                            syncDto.setNewFiles(newFilesToSync);
                            syncDto.setToDeleteIds(new ArrayList<>(primaryDocDto.getToBeDeletedRepoIds()));
                            bsbFileClient.saveFiles(syncDto);
                        }

                        // delete docs in FE file-repo
                        /* Ignore the failure when try to delete FE files because this is not a big issue.
                         * The not deleted file won't be retrieved, so it's just a waste of disk space */
                        for (String id: primaryDocDto.getToBeDeletedRepoIds()) {
                            FileRepoDto fileRepoDto = new FileRepoDto();
                            fileRepoDto.setId(id);
                            fileRepoClient.removeFileById(fileRepoDto);
                        }
                    } catch (Exception e) {
                        log.error("Fail to sync files to BE", e);
                    }

                    ParamUtil.setRequestAttr(request, KEY_ACTION_TYPE, KEY_ACTION_SUBMIT);
                } else {
                    ParamUtil.setRequestAttr(request, KEY_SHOW_ERROR_SWITCH, Boolean.TRUE);
                    ParamUtil.setSessionAttr(request, KEY_JUMP_DEST_NODE, NODE_NAME_REVIEW);
                }
            }else {
                facilityRegistrationService.jumpHandler(request, viewApprovalRoot, NODE_NAME_PREVIEW_SUBMIT, reviewNode);
            }

        } else {
            throw new IaisRuntimeException(ERR_MSG_INVALID_ACTION);
        }
        ParamUtil.setSessionAttr(request, KEY_ROOT_NODE_GROUP, facRegRoot);
        ParamUtil.setSessionAttr(request, KEY_RENEWAL_VIEW_APPROVAL_ROOT_NODE_GROUP, viewApprovalRoot);
    }

    public void handleCompInfo(BaseProcessClass bpc) {
        facilityRegistrationService.handleCompInfo(bpc);
    }

    public void handleServiceSelection(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        NodeGroup facRegRoot = facilityRegistrationService.getFacilityRegisterRoot(request);
        SimpleNode facSelectionNode = (SimpleNode) facRegRoot.getNode(NODE_NAME_FAC_SELECTION);
        FacilitySelectionDto selectionDto = (FacilitySelectionDto) facSelectionNode.getValue();
        selectionDto.reqObjMapping(request);

        String actionType = ParamUtil.getString(request, KEY_ACTION_TYPE);
        if (NODE_NAME_REVIEW.equals(actionType)){
            facilityRegistrationService.renewalJumpHandle(request, facRegRoot, NODE_NAME_FAC_SELECTION, facSelectionNode);
        } else if (KEY_ACTION_JUMP.equals(actionType)) {
            facilityRegistrationService.jumpHandler(request, facRegRoot, NODE_NAME_FAC_SELECTION, facSelectionNode);
        } else {
            throw new IaisRuntimeException(ERR_MSG_INVALID_ACTION);
        }

        if (facSelectionNode.isValidated()) {
            NodeGroup batGroup = (NodeGroup) facRegRoot.getNode(NODE_NAME_FAC_BAT_INFO);
            FacilityRegistrationService.changeBatNodeGroup(batGroup, selectionDto);
        }
        ParamUtil.setSessionAttr(request, KEY_ROOT_NODE_GROUP, facRegRoot);
    }

    public void handleFacProfile(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        NodeGroup facRegRoot = facilityRegistrationService.getFacilityRegisterRoot(request);
        String currentNodePath = NODE_NAME_FAC_INFO + facRegRoot.getPathSeparator() + NODE_NAME_FAC_PROFILE;
        SimpleNode facProfileNode = (SimpleNode) facRegRoot.at(currentNodePath);
        FacilityProfileDto facProfileDto = (FacilityProfileDto) facProfileNode.getValue();
        SimpleNode facAuthNode = (SimpleNode) facRegRoot.at(NODE_NAME_FAC_INFO + facRegRoot.getPathSeparator() + NODE_NAME_FAC_AUTH);
        FacilityAuthoriserDto facAuthDto = (FacilityAuthoriserDto) facAuthNode.getValue();
        facAuthDto.setIsProtectedPlace(facProfileDto.getIsFacilityProtected());

        String actionType = ParamUtil.getString(request, KEY_ACTION_TYPE);
        if (NODE_NAME_REVIEW.equals(actionType)){
            facilityRegistrationService.renewalJumpHandle(request, facRegRoot, currentNodePath, facProfileNode);
        } else if (KEY_ACTION_JUMP.equals(actionType)) {
            facilityRegistrationService.jumpHandler(request, facRegRoot, currentNodePath, facProfileNode);
        } else {
            throw new IaisRuntimeException(ERR_MSG_INVALID_ACTION);
        }
        ParamUtil.setSessionAttr(request, KEY_ROOT_NODE_GROUP, facRegRoot);
    }

    public void handleFacOperator(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        NodeGroup facRegRoot = facilityRegistrationService.getFacilityRegisterRoot(request);
        String currentNodePath = NODE_NAME_FAC_INFO + facRegRoot.getPathSeparator() + NODE_NAME_FAC_OPERATOR;
        SimpleNode facOpNode = (SimpleNode) facRegRoot.at(currentNodePath);

        String actionType = ParamUtil.getString(request, KEY_ACTION_TYPE);
        if (NODE_NAME_REVIEW.equals(actionType)){
            facilityRegistrationService.renewalJumpHandle(request, facRegRoot, currentNodePath, facOpNode);
        } else if (KEY_ACTION_JUMP.equals(actionType)) {
            facilityRegistrationService.jumpHandler(request, facRegRoot, currentNodePath, facOpNode);
        } else {
            throw new IaisRuntimeException(ERR_MSG_INVALID_ACTION);
        }
        ParamUtil.setSessionAttr(request, KEY_ROOT_NODE_GROUP, facRegRoot);
    }

    public void handleFacInfoAuthoriser(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        NodeGroup facRegRoot = facilityRegistrationService.getFacilityRegisterRoot(request);
        String currentNodePath = NODE_NAME_FAC_INFO + facRegRoot.getPathSeparator() + NODE_NAME_FAC_AUTH;
        SimpleNode facAuthNode = (SimpleNode) facRegRoot.at(currentNodePath);
        FacilityAuthoriserDto facAuthDto = (FacilityAuthoriserDto) facAuthNode.getValue();
        facAuthDto.reqObjMapping(request);

        String actionType = ParamUtil.getString(request, KEY_ACTION_TYPE);
        if (NODE_NAME_REVIEW.equals(actionType)){
            facilityRegistrationService.renewalJumpHandle(request, facRegRoot, currentNodePath, facAuthNode);
        } else if (KEY_ACTION_JUMP.equals(actionType)) {
            facilityRegistrationService.jumpHandler(request, facRegRoot, currentNodePath, facAuthNode);
        } else {
            throw new IaisRuntimeException(ERR_MSG_INVALID_ACTION);
        }
        ParamUtil.setSessionAttr(request, KEY_ROOT_NODE_GROUP, facRegRoot);
    }

    public void handleFacAdmin(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        NodeGroup facRegRoot = facilityRegistrationService.getFacilityRegisterRoot(request);
        String currentNodePath = NODE_NAME_FAC_INFO + facRegRoot.getPathSeparator() + NODE_NAME_FAC_ADMIN;
        SimpleNode facAdminNode = (SimpleNode) facRegRoot.at(currentNodePath);
        FacilityAdministratorDto facAdminDto = (FacilityAdministratorDto) facAdminNode.getValue();
        facAdminDto.reqObjMapping(request);

        String actionType = ParamUtil.getString(request, KEY_ACTION_TYPE);
        if (NODE_NAME_REVIEW.equals(actionType)){
            facilityRegistrationService.renewalJumpHandle(request, facRegRoot, currentNodePath, facAdminNode);
        } else if (KEY_ACTION_JUMP.equals(actionType)) {
            facilityRegistrationService.jumpHandler(request, facRegRoot, currentNodePath, facAdminNode);
        } else {
            throw new IaisRuntimeException(ERR_MSG_INVALID_ACTION);
        }
        ParamUtil.setSessionAttr(request, KEY_ROOT_NODE_GROUP, facRegRoot);
    }

    public void handleFacOfficer(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        NodeGroup facRegRoot = facilityRegistrationService.getFacilityRegisterRoot(request);
        String currentNodePath = NODE_NAME_FAC_INFO + facRegRoot.getPathSeparator() + NODE_NAME_FAC_OFFICER;
        SimpleNode facOfficerNode = (SimpleNode) facRegRoot.at(currentNodePath);
        FacilityOfficerDto facOfficerDto = (FacilityOfficerDto) facOfficerNode.getValue();
        facOfficerDto.reqObjMapping(request);

        String actionType = ParamUtil.getString(request, KEY_ACTION_TYPE);
        if (NODE_NAME_REVIEW.equals(actionType)){
            facilityRegistrationService.renewalJumpHandle(request, facRegRoot, currentNodePath, facOfficerNode);
        } else if (KEY_ACTION_JUMP.equals(actionType)) {
            facilityRegistrationService.jumpHandler(request, facRegRoot, currentNodePath, facOfficerNode);
        } else {
            throw new IaisRuntimeException(ERR_MSG_INVALID_ACTION);
        }
        ParamUtil.setSessionAttr(request, KEY_ROOT_NODE_GROUP, facRegRoot);
    }

    public void handleFacInfoCommittee(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        NodeGroup facRegRoot = facilityRegistrationService.getFacilityRegisterRoot(request);
        String currentNodePath = NODE_NAME_FAC_INFO + facRegRoot.getPathSeparator() + NODE_NAME_FAC_COMMITTEE;
        SimpleNode facCommitteeNode = (SimpleNode) facRegRoot.at(currentNodePath);
        FacilityCommitteeDto facCommitteeDto = (FacilityCommitteeDto) facCommitteeNode.getValue();
        facCommitteeDto.reqObjMapping(request);

        String actionType = ParamUtil.getString(request, KEY_ACTION_TYPE);
        if (NODE_NAME_REVIEW.equals(actionType)){
            facilityRegistrationService.renewalJumpHandle(request, facRegRoot, currentNodePath, facCommitteeNode);
        } else if (KEY_ACTION_JUMP.equals(actionType)) {
            facilityRegistrationService.jumpHandler(request, facRegRoot, currentNodePath, facCommitteeNode);
        } else {
            throw new IaisRuntimeException(ERR_MSG_INVALID_ACTION);
        }
        ParamUtil.setSessionAttr(request, KEY_ROOT_NODE_GROUP, facRegRoot);
    }

    public void handleBAToxin(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        NodeGroup facRegRoot = facilityRegistrationService.getFacilityRegisterRoot(request);
        String currentNodePath = (String) ParamUtil.getSessionAttr(request, KEY_JUMP_DEST_NODE);
        SimpleNode batNode = (SimpleNode) facRegRoot.at(currentNodePath);
        BiologicalAgentToxinDto batDto = (BiologicalAgentToxinDto) batNode.getValue();
        batDto.reqObjMapping(request);

        String actionType = ParamUtil.getString(request, KEY_ACTION_TYPE);
        if (NODE_NAME_REVIEW.equals(actionType)){
            facilityRegistrationService.renewalJumpHandle(request, facRegRoot, currentNodePath, batNode);
        } else if (KEY_ACTION_JUMP.equals(actionType)) {
            facilityRegistrationService.jumpHandler(request, facRegRoot, currentNodePath, batNode);
        } else {
            throw new IaisRuntimeException(ERR_MSG_INVALID_ACTION);
        }
        ParamUtil.setSessionAttr(request, KEY_ROOT_NODE_GROUP, facRegRoot);
    }

    public void handleOtherAppInfo(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        NodeGroup facRegRoot = facilityRegistrationService.getFacilityRegisterRoot(request);
        SimpleNode otherAppInfoNode = (SimpleNode) facRegRoot.at(NODE_NAME_OTHER_INFO);
        OtherApplicationInfoDto otherAppInfoDto = (OtherApplicationInfoDto) otherAppInfoNode.getValue();
        otherAppInfoDto.reqObjMapping(request);

        String actionType = ParamUtil.getString(request, KEY_ACTION_TYPE);
        if (NODE_NAME_REVIEW.equals(actionType)){
            facilityRegistrationService.renewalJumpHandle(request, facRegRoot, NODE_NAME_OTHER_INFO, otherAppInfoNode);
        } else if (KEY_ACTION_JUMP.equals(actionType)) {
            facilityRegistrationService.jumpHandler(request, facRegRoot, NODE_NAME_OTHER_INFO, otherAppInfoNode);
        } else {
            throw new IaisRuntimeException(ERR_MSG_INVALID_ACTION);
        }
        ParamUtil.setSessionAttr(request, KEY_ROOT_NODE_GROUP, facRegRoot);
    }

    public void handlePrimaryDoc(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        NodeGroup facRegRoot = facilityRegistrationService.getFacilityRegisterRoot(request);
        SimpleNode primaryDocNode = (SimpleNode) facRegRoot.at(NODE_NAME_PRIMARY_DOC);
        PrimaryDocDto primaryDocDto = (PrimaryDocDto) primaryDocNode.getValue();
        primaryDocDto.reqObjMapping(request);

        String actionType = ParamUtil.getString(request, KEY_ACTION_TYPE);
        if (NODE_NAME_REVIEW.equals(actionType)){
            facilityRegistrationService.renewalJumpHandle(request, facRegRoot, NODE_NAME_PRIMARY_DOC, primaryDocNode);
        } else if (KEY_ACTION_JUMP.equals(actionType)) {
            facilityRegistrationService.jumpHandler(request, facRegRoot, NODE_NAME_PRIMARY_DOC, primaryDocNode);
        } else {
            throw new IaisRuntimeException(ERR_MSG_INVALID_ACTION);
        }
        ParamUtil.setSessionAttr(request, KEY_ROOT_NODE_GROUP, facRegRoot);
    }

    public void actionFilter(BaseProcessClass bpc) {
        facilityRegistrationService.actionFilter(bpc);
    }

    public void jumpFilter(BaseProcessClass bpc) {
        facilityRegistrationService.jumpFilter(bpc);
    }

    public void preAcknowledgement(BaseProcessClass bpc) {
        //do nothing now
    }
}

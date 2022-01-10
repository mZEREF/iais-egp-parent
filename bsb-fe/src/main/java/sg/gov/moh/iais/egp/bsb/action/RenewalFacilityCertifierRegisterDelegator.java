package sg.gov.moh.iais.egp.bsb.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.AuditTrailConsts;
import com.ecquaria.cloud.moh.iais.common.dto.AuditTrailDto;
import com.ecquaria.cloud.moh.iais.common.dto.filerepo.FileRepoDto;
import com.ecquaria.cloud.moh.iais.common.exception.IaisRuntimeException;
import com.ecquaria.cloud.moh.iais.common.utils.MaskUtil;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import sg.gov.moh.iais.egp.bsb.client.BsbFileClient;
import sg.gov.moh.iais.egp.bsb.client.FileRepoClient;
import sg.gov.moh.iais.egp.bsb.common.node.Node;
import sg.gov.moh.iais.egp.bsb.common.node.NodeGroup;
import sg.gov.moh.iais.egp.bsb.common.node.Nodes;
import sg.gov.moh.iais.egp.bsb.common.node.simple.SimpleNode;
import sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants;
import sg.gov.moh.iais.egp.bsb.dto.file.DocRecordInfo;
import sg.gov.moh.iais.egp.bsb.dto.file.FileRepoSyncDto;
import sg.gov.moh.iais.egp.bsb.dto.file.NewDocInfo;
import sg.gov.moh.iais.egp.bsb.dto.file.NewFileSyncDto;
import sg.gov.moh.iais.egp.bsb.dto.register.afc.*;
import sg.gov.moh.iais.egp.bsb.dto.renewal.FacilityCertifierRegistrationReviewDto;
import sg.gov.moh.iais.egp.bsb.service.FacilityCertifierRegistrationService;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static sg.gov.moh.iais.egp.bsb.constant.FacCertifierRegisterConstants.*;
import static sg.gov.moh.iais.egp.bsb.constant.FacRegisterConstants.NODE_NAME_REVIEW;

/**
 * @author : LiRan
 * @date : 2021/12/18
 */
@Slf4j
@Delegator("renewalFacilityCertifierRegisterDelegator")
public class RenewalFacilityCertifierRegisterDelegator {
    private static final String MODULE_NAME = "Renewal Facility Certifier Registration";

    private final FileRepoClient fileRepoClient;
    private final BsbFileClient bsbFileClient;
    private final FacilityCertifierRegistrationService facilityCertifierRegistrationService;

    public RenewalFacilityCertifierRegisterDelegator(FileRepoClient fileRepoClient, BsbFileClient bsbFileClient, FacilityCertifierRegistrationService facilityCertifierRegistrationService) {
        this.fileRepoClient = fileRepoClient;
        this.bsbFileClient = bsbFileClient;
        this.facilityCertifierRegistrationService = facilityCertifierRegistrationService;
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
        //charge if maskedAppId is null
        String maskedAppId = request.getParameter(KEY_EDIT_APP_ID);
        if(StringUtils.hasLength(maskedAppId)){
            if (log.isInfoEnabled()) {
                log.info("masked app ID: {}", org.apache.commons.lang.StringUtils.normalizeSpace(maskedAppId));
            }
            boolean failRetrieveEditData = true;
            String appId = MaskUtil.unMaskValue(KEY_EDIT_APP_ID,maskedAppId);
            if(appId != null && !maskedAppId.equals(appId)){
                FacilityCertifierRegisterDto dto = facilityCertifierRegistrationService.getRenewalFacCertifierRegisterAppByApprovalId(appId);
                NodeGroup viewApprovalRoot = dto.toRenewalReviewRootGroup(KEY_RENEWAL_VIEW_APPROVAL_ROOT_NODE_GROUP);
                NodeGroup facRegRoot = dto.toRenewalFacCerRegRootGroup(KEY_ROOT_NODE_GROUP);
                ParamUtil.setSessionAttr(request, KEY_RENEWAL_VIEW_APPROVAL_ROOT_NODE_GROUP, viewApprovalRoot);
                ParamUtil.setSessionAttr(request, KEY_ROOT_NODE_GROUP, facRegRoot);
                ParamUtil.setSessionAttr(request, KEY_INSTRUCTION_INFO, dto.getInstructionDto());
                failRetrieveEditData = false;
            }
            if(failRetrieveEditData){
                throw new IaisRuntimeException("Fail to retrieve app data");
            }
        }
    }

    public void preInstruction(BaseProcessClass bpc) {
        // do nothing now
    }

    public void doInstruction(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        NodeGroup viewApprovalRoot = (NodeGroup) ParamUtil.getSessionAttr(request, KEY_RENEWAL_VIEW_APPROVAL_ROOT_NODE_GROUP);
        Node instructionNode = viewApprovalRoot.getNode(NODE_NAME_INSTRUCTION);
        String actionType = ParamUtil.getString(request, KEY_ACTION_TYPE);
        if (KEY_ACTION_JUMP.equals(actionType)) {
            facilityCertifierRegistrationService.jumpHandler(request, viewApprovalRoot, NODE_NAME_INSTRUCTION, instructionNode);
        } else {
            throw new IaisRuntimeException(ERR_MSG_INVALID_ACTION);
        }
        ParamUtil.setSessionAttr(request, KEY_RENEWAL_VIEW_APPROVAL_ROOT_NODE_GROUP, viewApprovalRoot);
    }

    public void preReview(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        NodeGroup viewApprovalRoot = (NodeGroup) ParamUtil.getSessionAttr(request, KEY_RENEWAL_VIEW_APPROVAL_ROOT_NODE_GROUP);
        SimpleNode reviewNode = (SimpleNode) viewApprovalRoot.at(NODE_NAME_REVIEW);
        FacilityCertifierRegistrationReviewDto reviewDto = (FacilityCertifierRegistrationReviewDto) reviewNode.getValue();
        Boolean needShowError = (Boolean) ParamUtil.getRequestAttr(request, KEY_SHOW_ERROR_SWITCH);
        if (needShowError == Boolean.TRUE) {
            ParamUtil.setRequestAttr(request, KEY_VALIDATION_ERRORS, reviewDto.retrieveValidationResult());
        }
        Nodes.needValidation(viewApprovalRoot, NODE_NAME_REVIEW);
        ParamUtil.setRequestAttr(request, NODE_NAME_REVIEW, reviewDto);

        NodeGroup facRegRoot = (NodeGroup) ParamUtil.getSessionAttr(request, KEY_ROOT_NODE_GROUP);
        ParamUtil.setRequestAttr(request, NODE_NAME_ORG_PROFILE, ((SimpleNode) facRegRoot.at(NODE_NAME_ORGANISATION_INFO + facRegRoot.getPathSeparator() + NODE_NAME_ORG_PROFILE)).getValue());
        ParamUtil.setRequestAttr(request, NODE_NAME_ORG_CERTIFYING_TEAM, ((SimpleNode) facRegRoot.at(NODE_NAME_ORGANISATION_INFO + facRegRoot.getPathSeparator() + NODE_NAME_ORG_CERTIFYING_TEAM)).getValue());
        ParamUtil.setRequestAttr(request, NODE_NAME_ORG_FAC_ADMINISTRATOR, ((SimpleNode) facRegRoot.at(NODE_NAME_ORGANISATION_INFO + facRegRoot.getPathSeparator() + NODE_NAME_ORG_FAC_ADMINISTRATOR)).getValue());
        ParamUtil.setRequestAttr(request, "docSettings", facilityCertifierRegistrationService.getFacRegDocSettings());
        PrimaryDocDto primaryDocDto = (PrimaryDocDto) ((SimpleNode)facRegRoot.at(NODE_NAME_FAC_PRIMARY_DOCUMENT)).getValue();
        Map<String, List<DocRecordInfo>> savedFiles = primaryDocDto.getExistDocTypeMap();
        Map<String, List<NewDocInfo>> newFiles = primaryDocDto.getNewDocTypeMap();
        ParamUtil.setRequestAttr(request, "savedFiles", savedFiles);
        ParamUtil.setRequestAttr(request, "newFiles", newFiles);
    }

    public void doReview(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        NodeGroup viewApprovalRoot = (NodeGroup) ParamUtil.getSessionAttr(request, KEY_RENEWAL_VIEW_APPROVAL_ROOT_NODE_GROUP);
        NodeGroup facRegRoot = (NodeGroup) ParamUtil.getSessionAttr(request, KEY_ROOT_NODE_GROUP);
        SimpleNode reviewNode = (SimpleNode) viewApprovalRoot.at(NODE_NAME_REVIEW);
        FacilityCertifierRegistrationReviewDto reviewDto = (FacilityCertifierRegistrationReviewDto) reviewNode.getValue();
        reviewDto.reqObjMapping(request);

        String actionType = ParamUtil.getString(request, KEY_ACTION_TYPE);
        String actionValue = ParamUtil.getString(request, KEY_ACTION_VALUE);

        //there has three action button, 'nav tab jump', 'submit', 'back', 'reviewEdit'
        if (KEY_ACTION_REVIEW_EDIT.equals(actionType)){// 'reviewEdit'
            String destNode = facilityCertifierRegistrationService.computeDestNodePath(facRegRoot, actionValue);
            String checkedDestNode = Nodes.jump(facRegRoot, destNode);
            if (!checkedDestNode.equals(destNode)){
                ParamUtil.setRequestAttr(request, KEY_SHOW_ERROR_SWITCH, Boolean.TRUE);
            }
            ParamUtil.setSessionAttr(request, KEY_JUMP_DEST_NODE, checkedDestNode);
            ParamUtil.setRequestAttr(request, KEY_ACTION_TYPE, KEY_ACTION_JUMP);
        }else if (KEY_ACTION_JUMP.equals(actionType)) {
            if (KEY_NAV_NEXT.equals(actionValue)) {
                if (reviewNode.doValidation()) {
                    reviewNode.passValidation();

                    //upload document
                    SimpleNode primaryDocNode = (SimpleNode) facRegRoot.at(NODE_NAME_FAC_PRIMARY_DOCUMENT);
                    PrimaryDocDto primaryDocDto = (PrimaryDocDto) primaryDocNode.getValue();
                    List<NewFileSyncDto> newFilesToSync = null;
                    if (!primaryDocDto.getNewDocMap().isEmpty()) {
                        MultipartFile[] files = primaryDocDto.getNewDocMap().values().stream().map(NewDocInfo::getMultipartFile).toArray(MultipartFile[]::new);
                        List<String> repoIds = fileRepoClient.saveFiles(files).getEntity();
                        newFilesToSync = primaryDocDto.newFileSaved(repoIds);
                    }

                    // save data
                    FacilityCertifierRegisterDto finalAllDataDto = FacilityCertifierRegisterDto.fromRenewal(viewApprovalRoot, facRegRoot);
                    AuditTrailDto auditTrailDto = (AuditTrailDto) ParamUtil.getSessionAttr(request, AuditTrailConsts.SESSION_ATTR_PARAM_NAME);
                    finalAllDataDto.setAuditTrailDto(auditTrailDto);
                    finalAllDataDto.setAppStatus(MasterCodeConstants.APP_STATUS_PEND_DO);
                    String response = facilityCertifierRegistrationService.saveRenewalRegisteredFacCertifier(finalAllDataDto);
                    log.info("save renewal facilityCertifierRegister response: {}", org.apache.commons.lang.StringUtils.normalizeSpace(response));

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
            } else {
                facilityCertifierRegistrationService.jumpHandler(request, viewApprovalRoot, NODE_NAME_REVIEW, reviewNode);
            }
        } else {
            throw new IaisRuntimeException(ERR_MSG_INVALID_ACTION);
        }
        ParamUtil.setSessionAttr(request, KEY_ROOT_NODE_GROUP, facRegRoot);
        ParamUtil.setSessionAttr(request, KEY_RENEWAL_VIEW_APPROVAL_ROOT_NODE_GROUP, viewApprovalRoot);
    }

    public void preCompInfo(BaseProcessClass bpc) {
        facilityCertifierRegistrationService.preCompInfo(bpc);
    }

    public void doCompInfo(BaseProcessClass bpc) {
        facilityCertifierRegistrationService.doCompInfo(bpc);
    }

    public void preAdministrator(BaseProcessClass bpc) {
        facilityCertifierRegistrationService.preAdministrator(bpc);
    }

    public void doAdministrator(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        NodeGroup facCertifierRegRoot = facilityCertifierRegistrationService.getFacCertifierRegisterRoot(request);
        String currentNodePath = NODE_NAME_ORGANISATION_INFO + facCertifierRegRoot.getPathSeparator() + NODE_NAME_ORG_FAC_ADMINISTRATOR;
        SimpleNode administratorNode = (SimpleNode) facCertifierRegRoot.at(currentNodePath);
        AdministratorDto administratorDto = (AdministratorDto) administratorNode.getValue();
        administratorDto.reqObjMapping(request);
        String actionType = ParamUtil.getString(request, KEY_ACTION_TYPE);
        if (NODE_NAME_REVIEW.equals(actionType)){
            facilityCertifierRegistrationService.renewalJumpHandle(request, facCertifierRegRoot, currentNodePath, administratorNode);
        }else if (KEY_ACTION_JUMP.equals(actionType)) {
            facilityCertifierRegistrationService.jumpHandler(request, facCertifierRegRoot, currentNodePath, administratorNode);
        } else {
            throw new IaisRuntimeException(ERR_MSG_INVALID_ACTION);
        }
        ParamUtil.setSessionAttr(request, KEY_ROOT_NODE_GROUP, facCertifierRegRoot);
    }

    public void preCertifyingTeam(BaseProcessClass bpc) {
        facilityCertifierRegistrationService.preCertifyingTeam(bpc);
    }

    public void doCertifyingTeam(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        NodeGroup facRegRoot = facilityCertifierRegistrationService.getFacCertifierRegisterRoot(request);
        String currentNodePath = NODE_NAME_ORGANISATION_INFO + facRegRoot.getPathSeparator() + NODE_NAME_ORG_CERTIFYING_TEAM;
        SimpleNode certifyingTeamNode = (SimpleNode) facRegRoot.at(currentNodePath);
        CertifyingTeamDto certifyingTeamDto = (CertifyingTeamDto) certifyingTeamNode.getValue();
        certifyingTeamDto.reqObjMapping(request);
        String actionType = ParamUtil.getString(request, KEY_ACTION_TYPE);
        if (NODE_NAME_REVIEW.equals(actionType)){
            facilityCertifierRegistrationService.renewalJumpHandle(request, facRegRoot, currentNodePath, certifyingTeamNode);
        }else if (KEY_ACTION_JUMP.equals(actionType)) {
            facilityCertifierRegistrationService.jumpHandler(request, facRegRoot, currentNodePath, certifyingTeamNode);
        } else {
            throw new IaisRuntimeException(ERR_MSG_INVALID_ACTION);
        }
        ParamUtil.setSessionAttr(request, KEY_ROOT_NODE_GROUP, facRegRoot);
    }

    public void preOrganisationInfo(BaseProcessClass bpc) {
        facilityCertifierRegistrationService.preOrganisationInfo(bpc);

    }

    public void doOrganisationProfile(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        NodeGroup facRegRoot = facilityCertifierRegistrationService.getFacCertifierRegisterRoot(request);
        String currentNodePath = NODE_NAME_ORGANISATION_INFO + facRegRoot.getPathSeparator() + NODE_NAME_ORG_PROFILE;
        SimpleNode orgProfileNode = (SimpleNode) facRegRoot.at(currentNodePath);
        OrganisationProfileDto orgProfile = (OrganisationProfileDto) orgProfileNode.getValue();
        orgProfile.reqObjMapping(request);
        String actionType = ParamUtil.getString(request, KEY_ACTION_TYPE);
        if (NODE_NAME_REVIEW.equals(actionType)){
            facilityCertifierRegistrationService.renewalJumpHandle(request, facRegRoot, currentNodePath, orgProfileNode);
        }else if (KEY_ACTION_JUMP.equals(actionType)) {
            facilityCertifierRegistrationService.jumpHandler(request, facRegRoot, currentNodePath, orgProfileNode);
        }else {
            throw new IaisRuntimeException(ERR_MSG_INVALID_ACTION);
        }
        ParamUtil.setSessionAttr(request, KEY_ROOT_NODE_GROUP, facRegRoot);
    }

    public void prepareDocuments(BaseProcessClass bpc) {
        facilityCertifierRegistrationService.prepareDocuments(bpc);
    }

    public void doDocument(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        NodeGroup facRegRoot = facilityCertifierRegistrationService.getFacCertifierRegisterRoot(request);
        String currentNodePath = NODE_NAME_FAC_PRIMARY_DOCUMENT;
        SimpleNode primaryDocNode = (SimpleNode) facRegRoot.at(currentNodePath);
        PrimaryDocDto primaryDocDto = (PrimaryDocDto) primaryDocNode.getValue();
        primaryDocDto.reqObjMapping(request);

        String actionType = ParamUtil.getString(request, KEY_ACTION_TYPE);
        if (NODE_NAME_REVIEW.equals(actionType)){
            facilityCertifierRegistrationService.renewalJumpHandle(request, facRegRoot, currentNodePath, primaryDocNode);
        }else if (KEY_ACTION_JUMP.equals(actionType)) {
            facilityCertifierRegistrationService.jumpHandler(request, facRegRoot, currentNodePath, primaryDocNode);
        } else {
            throw new IaisRuntimeException(ERR_MSG_INVALID_ACTION);
        }
        ParamUtil.setSessionAttr(request, KEY_ROOT_NODE_GROUP, facRegRoot);
    }

    public void preAcknowledge(BaseProcessClass bpc) {
        //do nothing now
    }

    public void prepare(BaseProcessClass bpc) {
        facilityCertifierRegistrationService.prepare(bpc);
    }

    public void controlSwitch(BaseProcessClass bpc) {
        facilityCertifierRegistrationService.controlSwitch(bpc);
    }
}

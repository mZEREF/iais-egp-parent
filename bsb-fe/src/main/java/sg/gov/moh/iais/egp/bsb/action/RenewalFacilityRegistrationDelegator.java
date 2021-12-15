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
import sg.gov.moh.iais.egp.bsb.dto.renewal.ReviewDto;
import sg.gov.moh.iais.egp.bsb.service.FacilityRegistrationService;
import sg.gov.moh.iais.egp.bsb.util.LogUtil;
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
                log.info("masked app ID: {}", LogUtil.escapeCrlf(maskedAppId));
            }
            boolean failRetrieveEditData = true;
            String appId = MaskUtil.unMaskValue(KEY_EDIT_APP_ID, maskedAppId);
            if (appId != null && !maskedAppId.equals(appId)) {
                ResponseDto<FacilityRegisterDto> resultDto = facRegClient.getRenewalFacRegAppDataByApprovalId(appId);
                if (resultDto.ok()) {
                    failRetrieveEditData = false;
                    NodeGroup viewApprovalRoot = resultDto.getEntity().toRenewalReviewRootGroup(KEY_RENEWAL_VIEW_APPROVAL_ROOT_NODE_GROUP);
                    NodeGroup facRegRoot = resultDto.getEntity().toRenewalFacRegRootGroup(KEY_ROOT_NODE_GROUP);
                    ParamUtil.setSessionAttr(request, KEY_RENEWAL_VIEW_APPROVAL_ROOT_NODE_GROUP, viewApprovalRoot);
                    ParamUtil.setSessionAttr(request, KEY_ROOT_NODE_GROUP, facRegRoot);
                    ParamUtil.setSessionAttr(request, KEY_INSTRUCTION_INFO, resultDto.getEntity().getInstructionDto());
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
        ReviewDto reviewDto = (ReviewDto) reviewNode.getValue();
        Boolean needShowError = (Boolean) ParamUtil.getRequestAttr(request, KEY_SHOW_ERROR_SWITCH);
        if (needShowError == Boolean.TRUE) {
            ParamUtil.setRequestAttr(request, KEY_VALIDATION_ERRORS, reviewDto.retrieveValidationResult());
        }
        Nodes.needValidation(viewApprovalRoot, NODE_NAME_REVIEW);
        ParamUtil.setRequestAttr(request, NODE_NAME_REVIEW, reviewDto);

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
        ReviewDto reviewDto = (ReviewDto) reviewNode.getValue();
        reviewDto.reqObjMapping(request);

        String actionType = ParamUtil.getString(request, KEY_ACTION_TYPE);
        String actionValue = ParamUtil.getString(request, KEY_ACTION_VALUE);

        //there has three action button, 'nav tab jump', 'submit', 'back', 'reviewEdit'
        //todo
        if (KEY_ACTION_REVIEW_EDIT.equals(actionType)){// 'reviewEdit'
            ParamUtil.setRequestAttr(request, KEY_ACTION_TYPE, KEY_ACTION_JUMP);
            ParamUtil.setSessionAttr(request, KEY_JUMP_DEST_NODE, actionValue);
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
                    FacilityRegisterDto finalAllDataDto = FacilityRegisterDto.from(facRegRoot);
                    AuditTrailDto auditTrailDto = (AuditTrailDto) ParamUtil.getSessionAttr(request, AuditTrailConsts.SESSION_ATTR_PARAM_NAME);
                    finalAllDataDto.setAuditTrailDto(auditTrailDto);
                    ResponseDto<String> responseDto = facRegClient.saveNewRegisteredFacility(finalAllDataDto);
                    log.info("save new facility response: {}", responseDto);

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
        ParamUtil.setSessionAttr(request, KEY_RENEWAL_VIEW_APPROVAL_ROOT_NODE_GROUP, viewApprovalRoot);
    }

    public void handleCompInfo(BaseProcessClass bpc) {
        facilityRegistrationService.handleCompInfo(bpc);
    }

    public void handleServiceSelection(BaseProcessClass bpc) {
        facilityRegistrationService.handleServiceSelection(bpc);
    }

    public void handleFacProfile(BaseProcessClass bpc) {
        facilityRegistrationService.handleFacProfile(bpc);
    }

    public void handleFacOperator(BaseProcessClass bpc) {
        facilityRegistrationService.handleFacOperator(bpc);
    }

    public void handleFacInfoAuthoriser(BaseProcessClass bpc) {
        facilityRegistrationService.handleFacInfoAuthoriser(bpc);
    }

    public void handleFacAdmin(BaseProcessClass bpc) {
        facilityRegistrationService.handleFacAdmin(bpc);
    }

    public void handleFacOfficer(BaseProcessClass bpc) {
        facilityRegistrationService.handleFacOfficer(bpc);
    }

    public void handleFacInfoCommittee(BaseProcessClass bpc) {
        facilityRegistrationService.handleFacInfoCommittee(bpc);
    }

    public void handleBAToxin(BaseProcessClass bpc) {
        facilityRegistrationService.handleBAToxin(bpc);
    }

    public void handleOtherAppInfo(BaseProcessClass bpc) {
        facilityRegistrationService.handleOtherAppInfo(bpc);
    }

    public void handlePrimaryDoc(BaseProcessClass bpc) {
        facilityRegistrationService.handlePrimaryDoc(bpc);
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

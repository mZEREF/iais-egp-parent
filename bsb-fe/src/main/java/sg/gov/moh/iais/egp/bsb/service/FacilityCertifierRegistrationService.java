package sg.gov.moh.iais.egp.bsb.service;

import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.filerepo.FileRepoDto;
import com.ecquaria.cloud.moh.iais.common.exception.IaisRuntimeException;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import sg.gov.moh.iais.egp.bsb.client.BsbFileClient;
import sg.gov.moh.iais.egp.bsb.client.FacCertifierRegisterClient;
import sg.gov.moh.iais.egp.bsb.client.FileRepoClient;
import sg.gov.moh.iais.egp.bsb.common.node.Node;
import sg.gov.moh.iais.egp.bsb.common.node.NodeGroup;
import sg.gov.moh.iais.egp.bsb.common.node.Nodes;
import sg.gov.moh.iais.egp.bsb.common.node.simple.SimpleNode;
import sg.gov.moh.iais.egp.bsb.common.rfc.CompareTwoObject;
import sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants;
import sg.gov.moh.iais.egp.bsb.dto.file.DocRecordInfo;
import sg.gov.moh.iais.egp.bsb.dto.file.FileRepoSyncDto;
import sg.gov.moh.iais.egp.bsb.dto.file.NewDocInfo;
import sg.gov.moh.iais.egp.bsb.dto.file.NewFileSyncDto;
import sg.gov.moh.iais.egp.bsb.dto.info.common.EmployeeInfo;
import sg.gov.moh.iais.egp.bsb.dto.register.afc.*;
import sg.gov.moh.iais.egp.bsb.dto.rfc.DiffContent;
import sg.gov.moh.iais.egp.bsb.dto.validation.ValidationListResultUnit;
import sg.gov.moh.iais.egp.bsb.util.mastercode.MasterCodeHolder;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static sg.gov.moh.iais.egp.bsb.constant.FacCertifierRegisterConstants.*;
import static sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants.KEY_ACTION_ADDITIONAL;
import static sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants.KEY_IND_AFTER_SAVE_AS_DRAFT;

/**
 * Facility Certifier Registration(new application, rfc, renewal) delegator common method.
 * @author : LiRan
 * @date : 2021/12/18
 */
@Service
@Slf4j
public class FacilityCertifierRegistrationService {
    private final FileRepoClient fileRepoClient;
    private final BsbFileClient bsbFileClient;
    private final FacCertifierRegisterClient facCertifierRegisterClient;
    private final DocSettingService docSettingService;

    public FacilityCertifierRegistrationService(FileRepoClient fileRepoClient, BsbFileClient bsbFileClient, FacCertifierRegisterClient facCertifierRegisterClient, DocSettingService docSettingService) {
        this.fileRepoClient = fileRepoClient;
        this.bsbFileClient = bsbFileClient;
        this.facCertifierRegisterClient = facCertifierRegisterClient;
        this.docSettingService = docSettingService;
    }

    public void preBeginFacilityCertifier(BaseProcessClass bpc){
        // do nothing now, need to prepare company info in the future
    }

    public void handleBeginFacilityCertifier(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        NodeGroup facRegRoot = getFacCertifierRegisterRoot(request);
        Node beginFacCertNode = facRegRoot.getNode(NODE_NAME_BEGIN_FACILITY_CERTIFIER);
        String actionType = ParamUtil.getString(request, KEY_ACTION_TYPE);
        if (KEY_ACTION_JUMP.equals(actionType)) {
            jumpHandler(request, facRegRoot, NODE_NAME_BEGIN_FACILITY_CERTIFIER, beginFacCertNode);
        }else if (KEY_NAV_SAVE_DRAFT.equals(actionType)) {
            ParamUtil.setRequestAttr(request, KEY_ACTION_TYPE, KEY_NAV_SAVE_DRAFT);
            ParamUtil.setSessionAttr(request, KEY_JUMP_DEST_NODE, NODE_NAME_BEGIN_FACILITY_CERTIFIER);
        } else {
            throw new IaisRuntimeException(ERR_MSG_INVALID_ACTION);
        }
        ParamUtil.setSessionAttr(request, KEY_ROOT_NODE_GROUP, facRegRoot);
    }


    public void preCompanyProfile(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        NodeGroup facRegRoot = getFacCertifierRegisterRoot(request);
        String currentNodePath = NODE_NAME_APPLICATION_INFO + facRegRoot.getPathSeparator() + NODE_NAME_COMPANY_PROFILE;
        SimpleNode companyProfileNode = (SimpleNode) facRegRoot.at(currentNodePath);
        CompanyProfileDto companyProfileDto = (CompanyProfileDto) companyProfileNode.getValue();
        Boolean needShowError = (Boolean) ParamUtil.getRequestAttr(request, KEY_SHOW_ERROR_SWITCH);
        if (needShowError == Boolean.TRUE) {
            ParamUtil.setRequestAttr(request, KEY_VALIDATION_ERRORS, companyProfileDto.retrieveValidationResult());
        }
        Nodes.needValidation(facRegRoot,currentNodePath);
        ParamUtil.setRequestAttr(request, KEY_COUNTRY_OPTIONS, tmpCountryOps());
        ParamUtil.setRequestAttr(request, NODE_NAME_COMPANY_PROFILE, companyProfileDto);
    }

    public void handleCompanyProfile(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        NodeGroup facRegRoot = getFacCertifierRegisterRoot(request);
        String currentNodePath = NODE_NAME_APPLICATION_INFO + facRegRoot.getPathSeparator() + NODE_NAME_COMPANY_PROFILE;
        SimpleNode companyProfileNode = (SimpleNode) facRegRoot.at(currentNodePath);
        CompanyProfileDto companyProfileDto = (CompanyProfileDto) companyProfileNode.getValue();
        companyProfileDto.reqObjMapping(request);
        String actionType = ParamUtil.getString(request, KEY_ACTION_TYPE);
        if (KEY_ACTION_JUMP.equals(actionType)) {
            jumpHandler(request, facRegRoot, currentNodePath, companyProfileNode);
        } else if (KEY_NAV_SAVE_DRAFT.equals(actionType)) {
            ParamUtil.setRequestAttr(request, KEY_ACTION_TYPE, KEY_NAV_SAVE_DRAFT);
            ParamUtil.setSessionAttr(request, KEY_JUMP_DEST_NODE, currentNodePath);
        } else {
            throw new IaisRuntimeException(ERR_MSG_INVALID_ACTION);
        }
        ParamUtil.setSessionAttr(request, KEY_ROOT_NODE_GROUP, facRegRoot);
    }

    public void preAdministrator(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        NodeGroup facRegRoot = getFacCertifierRegisterRoot(request);
        String currentNodePath = NODE_NAME_APPLICATION_INFO + facRegRoot.getPathSeparator() + NODE_NAME_ADMINISTRATOR;
        SimpleNode administratorNode = (SimpleNode) facRegRoot.at(currentNodePath);
        AdministratorDto administratorDto = (AdministratorDto) administratorNode.getValue();
        Boolean needShowError = (Boolean) ParamUtil.getRequestAttr(request, KEY_SHOW_ERROR_SWITCH);
        if (needShowError == Boolean.TRUE) {
            ParamUtil.setRequestAttr(request, KEY_VALIDATION_ERRORS, administratorDto.retrieveValidationResult());
        }
        Nodes.needValidation(facRegRoot,currentNodePath);
        ParamUtil.setRequestAttr(request, KEY_NATIONALITY_OPTIONS, MasterCodeHolder.NATIONALITY.allOptions());
        ParamUtil.setRequestAttr(request,KEY_SALUTATION_OPTIONS,MasterCodeHolder.SALUTATION.allOptions());
        ParamUtil.setRequestAttr(request, NODE_NAME_ADMINISTRATOR, administratorDto);
    }

    public void handleAdministrator(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        NodeGroup facCertifierRegRoot = getFacCertifierRegisterRoot(request);
        String currentNodePath = NODE_NAME_APPLICATION_INFO + facCertifierRegRoot.getPathSeparator() + NODE_NAME_ADMINISTRATOR;
        SimpleNode administratorNode = (SimpleNode) facCertifierRegRoot.at(currentNodePath);
        AdministratorDto administratorDto = (AdministratorDto) administratorNode.getValue();
        administratorDto.reqObjMapping(request);
        String actionType = ParamUtil.getString(request, KEY_ACTION_TYPE);
        if (KEY_ACTION_JUMP.equals(actionType)) {
            jumpHandler(request, facCertifierRegRoot, currentNodePath, administratorNode);
        } else if (KEY_NAV_SAVE_DRAFT.equals(actionType)) {
            ParamUtil.setRequestAttr(request, KEY_ACTION_TYPE, KEY_NAV_SAVE_DRAFT);
            ParamUtil.setSessionAttr(request, KEY_JUMP_DEST_NODE, currentNodePath);
        } else {
            throw new IaisRuntimeException(ERR_MSG_INVALID_ACTION);
        }
        ParamUtil.setSessionAttr(request, KEY_ROOT_NODE_GROUP, facCertifierRegRoot);
    }

    public void preCertifyingTeam(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        NodeGroup facRegRoot = getFacCertifierRegisterRoot(request);
        String currentNodePath = NODE_NAME_APPLICATION_INFO + facRegRoot.getPathSeparator() + NODE_NAME_CERTIFYING_TEAM_DETAIL;
        SimpleNode certifyingTeamNode = (SimpleNode) facRegRoot.at(currentNodePath);
        CertifyingTeamDto certifyingTeamDto = (CertifyingTeamDto) certifyingTeamNode.getValue();
        if (certifyingTeamNode.isValidated()) {
            ParamUtil.setRequestAttr(request, KEY_VALID_DATA_FILE, Boolean.TRUE);
        }
        /* If DTO contains data error, we don't convert error msg to JSON. Or rather, we retrieve the error map,
         * and then render a table. */
        if (certifyingTeamDto.isDataErrorExists()) {
            List<ValidationListResultUnit> resultUnitList = ValidationListResultUnit.fromDateErrorMap(certifyingTeamDto.getValidationResultDto());
            ParamUtil.setRequestAttr(request, KEY_DATA_ERRORS, resultUnitList);
            ParamUtil.setRequestAttr(request, KEY_ERROR_IN_DATA_FILE, Boolean.TRUE);
        } else {
            Boolean needShowError = (Boolean) ParamUtil.getRequestAttr(request, KEY_SHOW_ERROR_SWITCH);
            if (needShowError == Boolean.TRUE) {
                ParamUtil.setRequestAttr(request, KEY_VALIDATION_ERRORS, certifyingTeamDto.retrieveValidationResult());
            }
        }
        ParamUtil.setRequestAttr(request, NODE_NAME_CERTIFYING_TEAM_DETAIL, certifyingTeamDto);
        ParamUtil.setRequestAttr(request, KEY_POSITION_OPTIONS, tmpPositionOps());
        ParamUtil.setRequestAttr(request, KEY_NATIONALITY_OPTIONS, MasterCodeHolder.NATIONALITY.allOptions());
    }

    public void handleCertifyingTeam(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        NodeGroup facRegRoot = getFacCertifierRegisterRoot(request);
        String currentNodePath = NODE_NAME_APPLICATION_INFO + facRegRoot.getPathSeparator() + NODE_NAME_CERTIFYING_TEAM_DETAIL;
        SimpleNode certifyingTeamNode = (SimpleNode) facRegRoot.at(currentNodePath);
        CertifyingTeamDto certifyingTeamDto = (CertifyingTeamDto) certifyingTeamNode.getValue();

        String actionType = ParamUtil.getString(request, KEY_ACTION_TYPE);
        if (KEY_ACTION_LOAD_DATA_FILE.equals(actionType)) {
            /* Do not change validated to false when jump to this node, because user can not change data in current
             * form except for upload a new file.
             * When user upload a file, we immediately clear data in the DTO, and make the node need validation. */
            Nodes.needValidation(facRegRoot, currentNodePath);
            /* We do many things in the 'if' condition expression:
             * 1, we call validation of the file type, size etc. metadata info.
             * 2, if pass, we convert dat in the file to DTO list.
             * 3, if success, we call validation of the DTOs.
             * If any step fails, it will set the ValidationResultDto properly, so in the 'pre' step, this error msg
             * will be supplied to applicant.
             */
            certifyingTeamDto.reqObjMapping(request);
            if (!certifyingTeamDto.validateDataFile() || !certifyingTeamDto.loadFileData() || !certifyingTeamDto.doValidation()) {
                ParamUtil.setRequestAttr(request, KEY_SHOW_ERROR_SWITCH, Boolean.TRUE);
            } else {
                // data are valid
                Nodes.passValidation(facRegRoot, currentNodePath);
            }
            ParamUtil.setRequestAttr(request, KEY_ACTION_TYPE, KEY_ACTION_JUMP);
            ParamUtil.setSessionAttr(request, KEY_JUMP_DEST_NODE, currentNodePath);
        }else if (KEY_ACTION_DELETE_DATA_FILE.equals(actionType)) {
            certifyingTeamDto.deleteDataFile();
            Nodes.needValidation(facRegRoot, currentNodePath);
            ParamUtil.setRequestAttr(request, KEY_ACTION_TYPE, KEY_ACTION_JUMP);
            ParamUtil.setSessionAttr(request, KEY_JUMP_DEST_NODE, currentNodePath);
        } else if (KEY_ACTION_EXPAND_FILE.equals(actionType)) {
            if (!certifyingTeamNode.isValidated()) {
                throw new IllegalStateException("Invalid, can not expand");
            }
            List<CertifyingTeamFileDto> dataList = certifyingTeamDto.getDataListForDisplay();
            ParamUtil.setRequestAttr(request, KEY_DATA_LIST, dataList);
            ParamUtil.setRequestAttr(request, KEY_ACTION_TYPE, KEY_ACTION_JUMP);
            ParamUtil.setSessionAttr(request, KEY_JUMP_DEST_NODE, STEP_NAME_FACILITY_CERTIFIER_PREVIEW);
        }else if (KEY_ACTION_JUMP.equals(actionType)) {
            jumpHandler(request, facRegRoot, currentNodePath, certifyingTeamNode);
        } else if (KEY_NAV_SAVE_DRAFT.equals(actionType)) {
            ParamUtil.setRequestAttr(request, KEY_ACTION_TYPE, KEY_NAV_SAVE_DRAFT);
            ParamUtil.setSessionAttr(request, KEY_JUMP_DEST_NODE, currentNodePath);
        } else {
            throw new IaisRuntimeException(ERR_MSG_INVALID_ACTION);
        }
        ParamUtil.setSessionAttr(request, KEY_ROOT_NODE_GROUP, facRegRoot);
    }

    public void preCertifyingTeamPreview(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        String srcPath = ParamUtil.getString(request, KEY_ACTION_ADDITIONAL);
        ParamUtil.setRequestAttr(request, KEY_SOURCE_NODE_PATH, srcPath);
    }

    public void handleCertifyingTeamPreview(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        NodeGroup facRegRoot = getFacCertifierRegisterRoot(request);
        String actionType = ParamUtil.getString(request, KEY_ACTION_TYPE);
        if (KEY_ACTION_JUMP.equals(actionType)) {
            jumpHandler(request, facRegRoot, null, null);
        } else {
            throw new IaisRuntimeException(ERR_MSG_INVALID_ACTION);
        }
    }


    public void preSupportingDoc(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        NodeGroup facRegRoot = getFacCertifierRegisterRoot(request);
        SimpleNode primaryDocNode = (SimpleNode) facRegRoot.at(NODE_NAME_SUPPORTING_DOCUMENT);
        PrimaryDocDto primaryDocDto = (PrimaryDocDto) primaryDocNode.getValue();
        SimpleNode certifyingTeamNode = (SimpleNode) facRegRoot.at(NODE_NAME_APPLICATION_INFO+facRegRoot.getPathSeparator()+NODE_NAME_CERTIFYING_TEAM_DETAIL);
        CertifyingTeamDto certifyingTeamDto = (CertifyingTeamDto) certifyingTeamNode.getValue();
        ParamUtil.setRequestAttr(request,KEY_CERTIFIER_TEAM_LIST,certifyingTeamDto.getCertifierTeamMemberList());
        Boolean needShowError = (Boolean) ParamUtil.getRequestAttr(request, KEY_SHOW_ERROR_SWITCH);
        if (needShowError == Boolean.TRUE) {
            ParamUtil.setRequestAttr(request, KEY_VALIDATION_ERRORS, primaryDocDto.retrieveValidationResult());
        }
        Nodes.needValidation(facRegRoot,NODE_NAME_SUPPORTING_DOCUMENT);

        ParamUtil.setRequestAttr(request, "docSettings", docSettingService.getFacCerRegDocSettings());

        Map<String, List<DocRecordInfo>> savedFiles = primaryDocDto.getExistDocTypeMap();
        Map<String, CertTeamSavedDoc> certTeamSavedFiles = primaryDocDto.getExistCertTeamKeyMap();
        Map<String, List<NewDocInfo>> newFiles = primaryDocDto.getNewDocTypeMap();
        Map<String, CertTeamNewDoc> certTeamNewFiles = primaryDocDto.getNewCertTeamKeyMap();
        ParamUtil.setRequestAttr(request, "savedFiles", savedFiles);
        ParamUtil.setRequestAttr(request, "newFiles", newFiles);
        ParamUtil.setRequestAttr(request,"certTeamSavedFiles",certTeamSavedFiles);
        ParamUtil.setRequestAttr(request,"certTeamNewFiles",certTeamNewFiles);
    }

    public void handleSupportingDoc(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        NodeGroup facRegRoot = getFacCertifierRegisterRoot(request);
        String currentNodePath = NODE_NAME_SUPPORTING_DOCUMENT;
        SimpleNode supportingDocNode = (SimpleNode) facRegRoot.at(currentNodePath);
        PrimaryDocDto primaryDocDto = (PrimaryDocDto) supportingDocNode.getValue();
        primaryDocDto.reqObjMapping(request);

        String actionType = ParamUtil.getString(request, KEY_ACTION_TYPE);
        if (KEY_ACTION_JUMP.equals(actionType)) {
            jumpHandler(request, facRegRoot, currentNodePath, supportingDocNode);
        } else if(KEY_NAV_LOAD_CERTIFY_TEAM_FILE.equals(actionType)){
            ParamUtil.setRequestAttr(request, KEY_ACTION_TYPE, KEY_ACTION_JUMP);
            ParamUtil.setSessionAttr(request, KEY_JUMP_DEST_NODE, currentNodePath);
            ParamUtil.setSessionAttr(request,KEY_IS_CERTIFY_TEAM_FILE,"Y");
        } else if (KEY_NAV_SAVE_DRAFT.equals(actionType)) {
            ParamUtil.setRequestAttr(request, KEY_ACTION_TYPE, KEY_NAV_SAVE_DRAFT);
            ParamUtil.setSessionAttr(request, KEY_JUMP_DEST_NODE, currentNodePath);
        } else {
            throw new IaisRuntimeException(ERR_MSG_INVALID_ACTION);
        }
        ParamUtil.setSessionAttr(request, KEY_ROOT_NODE_GROUP, facRegRoot);
    }

    public void preparePreviewSubmit(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        NodeGroup facRegRoot = getFacCertifierRegisterRoot(request);
        SimpleNode previewSubmitNode = (SimpleNode) facRegRoot.at(NODE_NAME_FACILITY_CERTIFIER_PREVIEW_SUBMIT);
        PreviewSubmitDto previewSubmitDto = (PreviewSubmitDto) previewSubmitNode.getValue();
        Boolean needShowError = (Boolean) ParamUtil.getRequestAttr(request, KEY_SHOW_ERROR_SWITCH);
        if (needShowError == Boolean.TRUE) {
            ParamUtil.setRequestAttr(request, KEY_VALIDATION_ERRORS, previewSubmitDto.retrieveValidationResult());
        }
        Nodes.needValidation(facRegRoot,NODE_NAME_FACILITY_CERTIFIER_PREVIEW_SUBMIT);
        ParamUtil.setRequestAttr(request, NODE_NAME_FACILITY_CERTIFIER_PREVIEW_SUBMIT, previewSubmitDto);

        ParamUtil.setRequestAttr(request, NODE_NAME_COMPANY_PROFILE, ((SimpleNode) facRegRoot.at(NODE_NAME_APPLICATION_INFO + facRegRoot.getPathSeparator() + NODE_NAME_COMPANY_PROFILE)).getValue());
        ParamUtil.setRequestAttr(request, NODE_NAME_CERTIFYING_TEAM_DETAIL, ((SimpleNode) facRegRoot.at(NODE_NAME_APPLICATION_INFO + facRegRoot.getPathSeparator() + NODE_NAME_CERTIFYING_TEAM_DETAIL)).getValue());
        ParamUtil.setRequestAttr(request, NODE_NAME_ADMINISTRATOR, ((SimpleNode) facRegRoot.at(NODE_NAME_APPLICATION_INFO + facRegRoot.getPathSeparator() + NODE_NAME_ADMINISTRATOR)).getValue());
        ParamUtil.setRequestAttr(request, "docSettings", docSettingService.getFacCerRegDocSettings());
        PrimaryDocDto primaryDocDto = (PrimaryDocDto) ((SimpleNode)facRegRoot.at(NODE_NAME_SUPPORTING_DOCUMENT)).getValue();
        Map<String, List<DocRecordInfo>> savedFiles = primaryDocDto.getExistDocTypeMap();
        Map<String, List<NewDocInfo>> newFiles = primaryDocDto.getNewDocTypeMap();
        ParamUtil.setRequestAttr(request, "savedFiles", savedFiles);
        ParamUtil.setRequestAttr(request, "newFiles", newFiles);

        //new certify team file
        ParamUtil.setRequestAttr(request,"certTeamNewFiles",primaryDocDto.getNewCertTeamKeyMap());
        ParamUtil.setRequestAttr(request,"certTeamSavedFiles",primaryDocDto.getExistCertTeamKeyMap());
    }

    public void jumpFilter(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        String destNode = (String) ParamUtil.getSessionAttr(request, KEY_JUMP_DEST_NODE);
        ParamUtil.setRequestAttr(request, KEY_DEST_NODE_ROUTE, destNode);
    }

    public void actionFilter(BaseProcessClass bpc, String appType){
        HttpServletRequest request = bpc.request;
        String actionType = (String) ParamUtil.getRequestAttr(request, KEY_ACTION_TYPE);
        if (!StringUtils.hasLength(actionType)) {
            actionType = ParamUtil.getString(request, KEY_ACTION_TYPE);
        }else {
            // set, if the action is 'save draft', we save it and route back to that page
            if (KEY_NAV_SAVE_DRAFT.equals(actionType)) {
                actionType = KEY_ACTION_JUMP;
                saveDraft(request, appType);
            }
        }
        ParamUtil.setRequestAttr(request, KEY_INDEED_ACTION_TYPE, actionType);
    }

    /**
     * common actions when we do 'jump'
     * decide the routing logic
     * will set a dest node in the request attribute;
     * will set a floag if we need to show the error messages.
     * @param facRegRoot root data structure of this flow
     */
    public void jumpHandler(HttpServletRequest request, NodeGroup facRegRoot, String currentPath, Node currentNode) {
        String actionValue = ParamUtil.getString(request, KEY_ACTION_VALUE);
        Assert.hasText(actionValue, "Invalid action value");
        boolean currentLetGo = true;  // if false, we have to stay current node
        if (KEY_NAV_NEXT.equals(actionValue)) {  // if click next, we need to validate current node anyway
            currentLetGo = currentNode.doValidation();
            if (currentLetGo) {
                Nodes.passValidation(facRegRoot, currentPath);
            }
        }
        if (currentLetGo) {
            String destNode = computeDestNodePath(facRegRoot, actionValue);
            String checkedDestNode = Nodes.jump(facRegRoot, destNode);
            if (!checkedDestNode.equals(destNode)) {
                ParamUtil.setRequestAttr(request, KEY_SHOW_ERROR_SWITCH, Boolean.TRUE);
            }
            ParamUtil.setSessionAttr(request, KEY_JUMP_DEST_NODE, checkedDestNode);
        } else {
            ParamUtil.setRequestAttr(request, KEY_SHOW_ERROR_SWITCH, Boolean.TRUE);
            ParamUtil.setSessionAttr(request, KEY_JUMP_DEST_NODE, currentPath);
        }
    }

    /**
     * Renewal Module, when actionType is review, special jump handle
     */
    public void renewalJumpHandle(HttpServletRequest request, NodeGroup facCerRegRoot, String currentPath, Node currentNode){
        String actionValue = ParamUtil.getString(request, KEY_ACTION_VALUE);
        Assert.hasText(actionValue, "Invalid action value");
        boolean currentLetGo = true;  // if false, we have to stay current node
        if (KEY_NAV_NEXT.equals(actionValue)) {  // if click next, we need to validate current node anyway
            currentLetGo = currentNode.doValidation();
            if (currentLetGo) {
                Nodes.passValidation(facCerRegRoot, currentPath);
            }
        }
        if (currentLetGo) {
            ParamUtil.setSessionAttr(request, KEY_JUMP_DEST_NODE, NODE_NAME_REVIEW);
            ParamUtil.setRequestAttr(request, KEY_ACTION_TYPE, KEY_ACTION_JUMP);
        } else {
            ParamUtil.setRequestAttr(request, KEY_ACTION_TYPE, KEY_ACTION_JUMP);
            ParamUtil.setRequestAttr(request, KEY_SHOW_ERROR_SWITCH, Boolean.TRUE);
            ParamUtil.setSessionAttr(request, KEY_JUMP_DEST_NODE, currentPath);
        }
    }

    public String computeDestNodePath(NodeGroup facRegRoot, String actionValue) {
        String destNode;
        switch (actionValue) {
            case KEY_NAV_NEXT:
                destNode = Nodes.getNextNodePath(facRegRoot);
                break;
            case KEY_NAV_BACK:
                destNode = Nodes.getPreviousNodePath(facRegRoot);
                break;
            default:
                Assert.hasText(actionValue, "Action value should be a node path");
                destNode = Nodes.expandNode(facRegRoot, actionValue);
                break;
        }
        return destNode;
    }


    public NodeGroup getFacCertifierRegisterRoot (HttpServletRequest request){
        NodeGroup root = (NodeGroup) ParamUtil.getSessionAttr(request, KEY_ROOT_NODE_GROUP);
        if (root == null) {
            root = newFacCertifierRegisterRoot(KEY_ROOT_NODE_GROUP);
        }
        return root;
    }
    public static NodeGroup newFacCertifierRegisterRoot (String name){
        Node beginFacCertNode = new Node(NODE_NAME_BEGIN_FACILITY_CERTIFIER, new Node[0]);
        NodeGroup appInfoNodeGroup = newAppInfoNodeGroup(new Node[0]);
        SimpleNode supportingDocNode = new SimpleNode(new PrimaryDocDto(),NODE_NAME_SUPPORTING_DOCUMENT,new Node[]{appInfoNodeGroup});
        SimpleNode previewSubmitNode = new SimpleNode(new PreviewSubmitDto(),NODE_NAME_FACILITY_CERTIFIER_PREVIEW_SUBMIT,new Node[]{appInfoNodeGroup,supportingDocNode});
        return new NodeGroup.Builder().name(name)
                .addNode(beginFacCertNode)
                .addNode(appInfoNodeGroup)
                .addNode(supportingDocNode)
                .addNode(previewSubmitNode)
                .build();
    }

    public static NodeGroup newAppInfoNodeGroup(Node[] dependNodes) {
        SimpleNode companyProfileNode = new SimpleNode(new CompanyProfileDto(),NODE_NAME_COMPANY_PROFILE,new Node[0]);
        SimpleNode administratorNode = new SimpleNode(new AdministratorDto(),NODE_NAME_ADMINISTRATOR,new Node[]{companyProfileNode});
        SimpleNode certTeamNode = new SimpleNode(new CertifyingTeamDto(),NODE_NAME_CERTIFYING_TEAM_DETAIL,new Node[]{companyProfileNode,administratorNode});


        return new NodeGroup.Builder().name(NODE_NAME_APPLICATION_INFO)
                .dependNodes(dependNodes)
                .addNode(companyProfileNode)
                .addNode(administratorNode)
                .addNode(certTeamNode)
                .build();
    }

    /**
     * Same logic as {@link #computeTabClassname(NodeGroup, String)}, but will not throw exception.
     * And give a default classname when exception occur.
     * This behaviour is intended for the usage in the JSP, we don't want the JSP throw an exception.
     */
    public static String computeTabClassnameForJsp(NodeGroup group, String name) {
        String classname = "incomplete";
        try {
            classname = computeTabClassname(group, name);
        } catch (Exception e) {
            log.error("Fail to compute the class name", e);
        }
        return classname;
    }

    /**
     * Compute the class name for the nav tab.
     * The result will be active, complete or incomplete
     * @param group the node group of the facRegRoot
     * @return the class name
     */
    public static String computeTabClassname(NodeGroup group, String name) {
        Assert.notNull(group, ERR_MSG_NULL_GROUP);
        Assert.notNull(name, ERR_MSG_NULL_NAME);
        String className;
        if (name.equals(group.getActiveNodeKey())) {
            className = "active";
        } else {
            Node node = group.getNode(name);
            Assert.notNull(node, name + " node does not exist!");
            className = node.isValidated() ? "complete" : "incomplete";
        }
        return className;
    }


    /*
     * @param group the node group of the facRegRoot
     */
    public static boolean ifNodeSelected(NodeGroup group, String name) {
        Assert.notNull(group, ERR_MSG_NULL_GROUP);
        Assert.notNull(name, ERR_MSG_NULL_NAME);
        return name.equals(group.getActiveNodeKey());
    }

    public static boolean ifNodeSelectedForJsp(NodeGroup group, String name) {
        boolean selected = false;
        try {
            selected = ifNodeSelected(group, name);
        } catch (Exception e) {
            log.error("Fail to judge if the node selected", e);
        }
        return selected;
    }

    /**
     *
     * @param group the node group contains the sub steps
     */
    public static String computeStepClassname(NodeGroup group, String name) {
        Assert.notNull(group, ERR_MSG_NULL_GROUP);
        Assert.notNull(name, ERR_MSG_NULL_NAME);
        return group.getNode(name).isValidated() || name.equals(group.getActiveNodeKey()) ? "active" : "disabled";
    }

    public static String computeStepClassnameForJsp(NodeGroup group, String name) {
        String classname = "disabled";
        try {
            classname = computeStepClassname(group, name);
        } catch (Exception e) {
            log.error("Fail to compute the class name", e);
        }
        return classname;
    }

    private static List<SelectOption> tmpCountryOps() {
        return Arrays.asList(new SelectOption(null, TEXT_VALUE_PLEASE_SELECT),new SelectOption(TEXT_VALUE_SINGAPORE, TEXT_VALUE_SINGAPORE), new SelectOption(TEXT_VALUE_CHINA, TEXT_VALUE_CHINA),new SelectOption(TEXT_VALUE_MALAYSIA,TEXT_VALUE_MALAYSIA),new SelectOption("USA","USA"),new SelectOption("UK","UK"));
    }

    private static List<SelectOption> tmpPositionOps() {
        return Arrays.asList(new SelectOption(null, TEXT_VALUE_PLEASE_SELECT),new SelectOption("Biosafety Certifier", "Biosafety Certifier"), new SelectOption("Engineering Certifier", "Engineering Certifier"),new SelectOption("Assistant Biosafety Certifier","Assistant Biosafety Certifier"),new SelectOption("Assistant Engineering Certifier","Assistant Engineering Certifier"));
    }

    /**
     * edit application, get facilityCertifierRegisterDto by applicationId
     * @param appId applicationId
     */
    public FacilityCertifierRegisterDto getCertifierRegistrationAppData(String appId){
        return facCertifierRegisterClient.getCertifierRegistrationAppData(appId).getEntity();
    }

    /**
     * new application and edit application, save facilityCertifierRegisterDto
     */
    public String saveNewRegisteredFacCertifier(FacilityCertifierRegisterDto dto){
        return facCertifierRegisterClient.saveNewRegisteredFacCertifier(dto).getEntity();
    }

    /**
     * only use to 'rfc' module
     * rfc compare
     */
    public List<DiffContent> compareTwoDto(FacilityCertifierRegisterDto oldFacilityCertifierRegisterDto, FacilityCertifierRegisterDto newFacilityCertifierRegisterDto){
        List<DiffContent> diffContentList = new ArrayList<>();
        CompareTwoObject.diff(oldFacilityCertifierRegisterDto.getProfileDto(), newFacilityCertifierRegisterDto.getProfileDto(), diffContentList);
        CompareTwoObject.diff(oldFacilityCertifierRegisterDto.getCertifyingTeamDto(), newFacilityCertifierRegisterDto.getCertifyingTeamDto(), diffContentList, CertifyingTeamDto.CertifierTeamMember.class);
        CompareTwoObject.diff(oldFacilityCertifierRegisterDto.getAdministratorDto(), newFacilityCertifierRegisterDto.getAdministratorDto(), diffContentList, EmployeeInfo.class);
        //docRecordInfos don't process
        return diffContentList;
    }

    /**
     * Rfc, get facilityCertifierRegisterDto by approvalId
     */
    public FacilityCertifierRegisterDto getCertifierRegistrationAppDataByApprovalId(String approvalId){
        return facCertifierRegisterClient.getCertifierRegistrationAppDataByApprovalId(approvalId).getEntity();
    }

    /**
     * Rfc, save amendment facilityCertifierRegisterDto
     */
    public String saveAmendmentFacCertifier(FacilityCertifierRegisterDto dto){
        return facCertifierRegisterClient.saveAmendmentFacCertifier(dto).getEntity();
    }

    /**
     * Renewal, get facilityCertifierRegisterDto by approvalId
     */
    public FacilityCertifierRegisterDto getRenewalFacCertifierRegisterAppByApprovalId(String approvalId){
        return facCertifierRegisterClient.getRenewalFacCertifierRegisterAppByApprovalId(approvalId).getEntity();
    }

    /**
     * renewal application, save facilityCertifierRegisterDto
     */
    public String saveRenewalRegisteredFacCertifier(FacilityCertifierRegisterDto dto){
        return facCertifierRegisterClient.saveRenewalRegisteredFacCertifier(dto).getEntity();
    }

    /** Save new uploaded documents into FE file repo.
     * @param primaryDocDto document DTO have the specific structure
     * @return a list of DTOs can be used to sync to BE
     */
    public List<NewFileSyncDto> saveNewUploadedDoc(PrimaryDocDto primaryDocDto) {
        List<NewFileSyncDto> newFilesToSync = new ArrayList<>();
        if (!primaryDocDto.getNewDocMap().isEmpty()) {
            MultipartFile[] files = primaryDocDto.getNewDocMap().values().stream().map(NewDocInfo::getMultipartFile).toArray(MultipartFile[]::new);
            List<String> repoIds = fileRepoClient.saveFiles(files).getEntity();
            newFilesToSync.addAll(primaryDocDto.newFileSaved(repoIds));
        }
        if(!primaryDocDto.getCertTeamNewDocMap().isEmpty()){
            MultipartFile[] files = primaryDocDto.getCertTeamNewDocMap().values().stream().map(CertTeamNewDoc::getMultipartFile).toArray(MultipartFile[]::new);
            List<String> repoIds = fileRepoClient.saveFiles(files).getEntity();
            newFilesToSync.addAll(primaryDocDto.newCertTeamFileSaved(repoIds));
        }
        return newFilesToSync;
    }

    public NewFileSyncDto saveCertifyTeamNewDataFile(CertifyingTeamDto certifyingTeamDto) {
        if (certifyingTeamDto.getNewFile() != null) {
            MultipartFile file = certifyingTeamDto.getNewFile().getMultipartFile();
            String repoId = fileRepoClient.saveFiles(new MultipartFile[]{file}).getEntity().get(0);
            return certifyingTeamDto.newFileSaved(repoId);
        } else {
            return null;
        }
    }

    /** Delete unwanted documents in FE file repo.
     * This method will remove the repoId from the toBeDeletedRepoIds set after a call of removal.
     * @param primaryDocDto document DTO have the specific structure
     * @return a list of repo IDs deleted in FE file repo
     */
    public List<String> deleteUnwantedDoc(PrimaryDocDto primaryDocDto) {
        /* Ignore the failure when try to delete FE files because this is not a big issue.
         * The not deleted file won't be retrieved, so it's just a waste of disk space */
        List<String> toBeDeletedRepoIds = new ArrayList<>(primaryDocDto.getToBeDeletedRepoIds().size()+primaryDocDto.getCertTeamToBeDeletedRepoIds().size());
        toBeDeletedRepoIds.addAll(primaryDocDto.getToBeDeletedRepoIds());
        toBeDeletedRepoIds.addAll(primaryDocDto.getCertTeamToBeDeletedRepoIds());
        for (String id: toBeDeletedRepoIds) {
            FileRepoDto fileRepoDto = new FileRepoDto();
            fileRepoDto.setId(id);
            fileRepoClient.removeFileById(fileRepoDto);
            primaryDocDto.getToBeDeletedRepoIds().remove(id);
        }
        return toBeDeletedRepoIds;
    }

    /** Sync new uploaded documents to BE; delete unwanted documents in BE too.
     * @param newFilesToSync a list of DTOs contains ID and data
     * @param toBeDeletedRepoIds a list of repo IDs to be deleted in BE
     */
    public void syncNewDocsAndDeleteFiles(List<NewFileSyncDto> newFilesToSync, List<String> toBeDeletedRepoIds) {
        // sync files to BE file-repo (save new added files, delete useless files)
        if (!CollectionUtils.isEmpty(newFilesToSync) || !CollectionUtils.isEmpty(toBeDeletedRepoIds)) {
            /* Ignore the failure of sync files currently.
             * We should add a mechanism to retry synchronization of files in the future */
            FileRepoSyncDto syncDto = new FileRepoSyncDto();
            syncDto.setNewFiles(newFilesToSync);
            syncDto.setToDeleteIds(toBeDeletedRepoIds);
            bsbFileClient.saveFiles(syncDto);
        }
    }

    public void saveDraft(HttpServletRequest request, String appType) {
        NodeGroup facRegRoot = getFacCertifierRegisterRoot(request);

        // save docs
        SimpleNode primaryDocNode = (SimpleNode) facRegRoot.at(NODE_NAME_SUPPORTING_DOCUMENT);
        CertifyingTeamDto certifyingTeamDto = (CertifyingTeamDto) ((SimpleNode) facRegRoot.at(NODE_NAME_APPLICATION_INFO + facRegRoot.getPathSeparator() + NODE_NAME_CERTIFYING_TEAM_DETAIL)).getValue();
        PrimaryDocDto primaryDocDto = (PrimaryDocDto) primaryDocNode.getValue();
        List<NewFileSyncDto> newFilesToSync = new ArrayList<>(primaryDocDto.getCertTeamNewDocMap().size() + primaryDocDto.getNewDocMap().size() + 2);
        newFilesToSync.addAll(saveNewUploadedDoc(primaryDocDto));
        newFilesToSync.add(saveCertifyTeamNewDataFile(certifyingTeamDto));

        // save data
        FacilityCertifierRegisterDto finalAllDataDto = null;
        if (appType.equals(MasterCodeConstants.APP_TYPE_NEW) || appType.equals(MasterCodeConstants.APP_TYPE_RFC)){
            finalAllDataDto = FacilityCertifierRegisterDto.from(facRegRoot);
            finalAllDataDto.setAppType(appType);
        }else if (appType.equals(MasterCodeConstants.APP_TYPE_RENEW)){
            NodeGroup viewApprovalRoot = (NodeGroup) ParamUtil.getSessionAttr(request, KEY_RENEWAL_VIEW_APPROVAL_ROOT_NODE_GROUP);
            finalAllDataDto = FacilityCertifierRegisterDto.fromRenewal(viewApprovalRoot, facRegRoot);
            finalAllDataDto.setAppType(appType);
        }
        String draftAppNo = facCertifierRegisterClient.saveFacCertifierDraft(finalAllDataDto);
        // set draft app No. into the NodeGroup
        CompanyProfileDto profileDto = (CompanyProfileDto) ((SimpleNode) facRegRoot.at(NODE_NAME_APPLICATION_INFO + facRegRoot.getPathSeparator() + NODE_NAME_COMPANY_PROFILE)).getValue();
        profileDto.setDraftAppNo(draftAppNo);
        ParamUtil.setSessionAttr(request, KEY_ROOT_NODE_GROUP, facRegRoot);

        try {
            // delete docs
            List<String> toBeDeletedRepoIds = new ArrayList<>(primaryDocDto.getToBeDeletedRepoIds().size() + primaryDocDto.getCertTeamToBeDeletedRepoIds().size() + 2);
            toBeDeletedRepoIds.addAll(deleteUnwantedDoc(primaryDocDto));
            if(certifyingTeamDto.getToBeDeletedRepoId() != null){
                FileRepoDto certTeamDeleteDto = new FileRepoDto();
                certTeamDeleteDto.setId(certifyingTeamDto.getToBeDeletedRepoId());
                fileRepoClient.removeFileById(certTeamDeleteDto);
                toBeDeletedRepoIds.add(certifyingTeamDto.getToBeDeletedRepoId());
                certifyingTeamDto.setToBeDeletedRepoId(null);
            }
            // sync docs
            syncNewDocsAndDeleteFiles(newFilesToSync, toBeDeletedRepoIds);
        } catch (Exception e) {
            log.error("Fail to sync files to BE", e);
        }

        ParamUtil.setRequestAttr(request, KEY_IND_AFTER_SAVE_AS_DRAFT, Boolean.TRUE);
    }
}

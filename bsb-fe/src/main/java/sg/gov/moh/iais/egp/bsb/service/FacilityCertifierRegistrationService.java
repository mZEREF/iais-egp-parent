package sg.gov.moh.iais.egp.bsb.service;

import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.mastercode.MasterCodeView;
import com.ecquaria.cloud.moh.iais.common.exception.IaisRuntimeException;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.helper.MasterCodeUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import sg.gov.moh.iais.egp.bsb.client.FacCertifierRegisterClient;
import sg.gov.moh.iais.egp.bsb.common.node.Node;
import sg.gov.moh.iais.egp.bsb.common.node.NodeGroup;
import sg.gov.moh.iais.egp.bsb.common.node.Nodes;
import sg.gov.moh.iais.egp.bsb.common.node.simple.SimpleNode;
import sg.gov.moh.iais.egp.bsb.common.rfc.CompareTwoObject;
import sg.gov.moh.iais.egp.bsb.constant.DocConstants;
import sg.gov.moh.iais.egp.bsb.dto.ResponseDto;
import sg.gov.moh.iais.egp.bsb.dto.file.DocRecordInfo;
import sg.gov.moh.iais.egp.bsb.dto.file.NewDocInfo;
import sg.gov.moh.iais.egp.bsb.dto.register.afc.*;
import sg.gov.moh.iais.egp.bsb.dto.rfc.DiffContent;
import sg.gov.moh.iais.egp.bsb.entity.DocSetting;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static sg.gov.moh.iais.egp.bsb.constant.FacCertifierRegisterConstants.*;

/**
 * Facility Certifier Registration(new application, rfc, renewal) delegator common method.
 * @author : LiRan
 * @date : 2021/12/18
 */
@Service
@Slf4j
public class FacilityCertifierRegistrationService {
    private final FacCertifierRegisterClient facCertifierRegisterClient;

    public FacilityCertifierRegistrationService(FacCertifierRegisterClient facCertifierRegisterClient) {
        this.facCertifierRegisterClient = facCertifierRegisterClient;
    }

    public void preCompInfo(BaseProcessClass bpc){
        // do nothing now, need to prepare company info in the future
    }

    public void doCompInfo(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        NodeGroup facRegRoot = getFacCertifierRegisterRoot(request);
        Node compInfoNode = facRegRoot.getNode(NODE_NAME_COMPANY_INFO);
        String actionType = ParamUtil.getString(request, KEY_ACTION_TYPE);
        if (KEY_ACTION_JUMP.equals(actionType)) {
            jumpHandler(request, facRegRoot, NODE_NAME_COMPANY_INFO, compInfoNode);
        } else {
            throw new IaisRuntimeException(ERR_MSG_INVALID_ACTION);
        }
        ParamUtil.setSessionAttr(request, KEY_ROOT_NODE_GROUP, facRegRoot);
    }

    public void preAdministrator(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        NodeGroup facRegRoot = getFacCertifierRegisterRoot(request);
        String currentNodePath = NODE_NAME_ORGANISATION_INFO + facRegRoot.getPathSeparator() + NODE_NAME_ORG_FAC_ADMINISTRATOR;
        SimpleNode administratorNode = (SimpleNode) facRegRoot.at(currentNodePath);
        AdministratorDto administratorDto = (AdministratorDto) administratorNode.getValue();
        Boolean needShowError = (Boolean) ParamUtil.getRequestAttr(request, KEY_SHOW_ERROR_SWITCH);
        if (needShowError == Boolean.TRUE) {
            ParamUtil.setRequestAttr(request, KEY_VALIDATION_ERRORS, administratorDto.retrieveValidationResult());
        }
        Nodes.needValidation(facRegRoot,currentNodePath);
        ParamUtil.setRequestAttr(request, KEY_NATIONALITY_OPTIONS, tmpNationalityOps());
        ParamUtil.setRequestAttr(request, NODE_NAME_ORG_FAC_ADMINISTRATOR, administratorDto);
    }

    public void doAdministrator(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        NodeGroup facCertifierRegRoot = getFacCertifierRegisterRoot(request);
        String currentNodePath = NODE_NAME_ORGANISATION_INFO + facCertifierRegRoot.getPathSeparator() + NODE_NAME_ORG_FAC_ADMINISTRATOR;
        SimpleNode administratorNode = (SimpleNode) facCertifierRegRoot.at(currentNodePath);
        AdministratorDto administratorDto = (AdministratorDto) administratorNode.getValue();
        administratorDto.reqObjMapping(request);
        String actionType = ParamUtil.getString(request, KEY_ACTION_TYPE);
        if (KEY_ACTION_JUMP.equals(actionType)) {
            jumpHandler(request, facCertifierRegRoot, currentNodePath, administratorNode);
        } else {
            throw new IaisRuntimeException(ERR_MSG_INVALID_ACTION);
        }
        ParamUtil.setSessionAttr(request, KEY_ROOT_NODE_GROUP, facCertifierRegRoot);
    }

    public void preCertifyingTeam(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        NodeGroup facRegRoot = getFacCertifierRegisterRoot(request);
        String currentNodePath = NODE_NAME_ORGANISATION_INFO + facRegRoot.getPathSeparator() + NODE_NAME_ORG_CERTIFYING_TEAM;
        SimpleNode certifyingTeamNode = (SimpleNode) facRegRoot.at(currentNodePath);
        CertifyingTeamDto certifyingTeamDto = (CertifyingTeamDto) certifyingTeamNode.getValue();
        Boolean needShowError = (Boolean) ParamUtil.getRequestAttr(request, KEY_SHOW_ERROR_SWITCH);
        if (needShowError == Boolean.TRUE) {
            ParamUtil.setRequestAttr(request, KEY_VALIDATION_ERRORS, certifyingTeamDto.retrieveValidationResult());
        }
        ParamUtil.setRequestAttr(request, KEY_POSITION_OPTIONS, tmpPositionOps());
        ParamUtil.setRequestAttr(request, KEY_NATIONALITY_OPTIONS, tmpNationalityOps());
        Nodes.needValidation(facRegRoot,currentNodePath);
        ParamUtil.setRequestAttr(request, NODE_NAME_ORG_CERTIFYING_TEAM, certifyingTeamDto);
    }

    public void doCertifyingTeam(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        NodeGroup facRegRoot = getFacCertifierRegisterRoot(request);
        String currentNodePath = NODE_NAME_ORGANISATION_INFO + facRegRoot.getPathSeparator() + NODE_NAME_ORG_CERTIFYING_TEAM;
        SimpleNode certifyingTeamNode = (SimpleNode) facRegRoot.at(currentNodePath);
        CertifyingTeamDto certifyingTeamDto = (CertifyingTeamDto) certifyingTeamNode.getValue();
        certifyingTeamDto.reqObjMapping(request);
        String actionType = ParamUtil.getString(request, KEY_ACTION_TYPE);
        if (KEY_ACTION_JUMP.equals(actionType)) {
            jumpHandler(request, facRegRoot, currentNodePath, certifyingTeamNode);
        } else {
            throw new IaisRuntimeException(ERR_MSG_INVALID_ACTION);
        }
        ParamUtil.setSessionAttr(request, KEY_ROOT_NODE_GROUP, facRegRoot);
    }

    public void preOrganisationInfo(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        NodeGroup facRegRoot = getFacCertifierRegisterRoot(request);
        String currentNodePath = NODE_NAME_ORGANISATION_INFO + facRegRoot.getPathSeparator() + NODE_NAME_ORG_PROFILE;
        SimpleNode orgProfileNode = (SimpleNode) facRegRoot.at(currentNodePath);
        OrganisationProfileDto orgProfileDto = (OrganisationProfileDto) orgProfileNode.getValue();
        Boolean needShowError = (Boolean) ParamUtil.getRequestAttr(request, KEY_SHOW_ERROR_SWITCH);
        if (needShowError == Boolean.TRUE) {
            ParamUtil.setRequestAttr(request, KEY_VALIDATION_ERRORS, orgProfileDto.retrieveValidationResult());
        }
        Nodes.needValidation(facRegRoot,currentNodePath);
        ParamUtil.setRequestAttr(request, KEY_COUNTRY_OPTIONS, tmpCountryOps());
        ParamUtil.setRequestAttr(request, NODE_NAME_ORG_PROFILE, orgProfileDto);
    }

    public void doOrganisationProfile(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        NodeGroup facRegRoot = getFacCertifierRegisterRoot(request);
        String currentNodePath = NODE_NAME_ORGANISATION_INFO + facRegRoot.getPathSeparator() + NODE_NAME_ORG_PROFILE;
        SimpleNode orgProfileNode = (SimpleNode) facRegRoot.at(currentNodePath);
        OrganisationProfileDto orgProfile = (OrganisationProfileDto) orgProfileNode.getValue();
        orgProfile.reqObjMapping(request);
        String actionType = ParamUtil.getString(request, KEY_ACTION_TYPE);
        if (KEY_ACTION_JUMP.equals(actionType)) {
            jumpHandler(request, facRegRoot, currentNodePath, orgProfileNode);
        }else {
            throw new IaisRuntimeException(ERR_MSG_INVALID_ACTION);
        }
        ParamUtil.setSessionAttr(request, KEY_ROOT_NODE_GROUP, facRegRoot);
    }

    public void prepareDocuments(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        NodeGroup facRegRoot = getFacCertifierRegisterRoot(request);
        SimpleNode primaryDocNode = (SimpleNode) facRegRoot.at(NODE_NAME_FAC_PRIMARY_DOCUMENT);
        PrimaryDocDto primaryDocDto = (PrimaryDocDto) primaryDocNode.getValue();
        Boolean needShowError = (Boolean) ParamUtil.getRequestAttr(request, KEY_SHOW_ERROR_SWITCH);
        if (needShowError == Boolean.TRUE) {
            ParamUtil.setRequestAttr(request, KEY_VALIDATION_ERRORS, primaryDocDto.retrieveValidationResult());
        }
        Nodes.needValidation(facRegRoot,NODE_NAME_FAC_PRIMARY_DOCUMENT);

        ParamUtil.setRequestAttr(request, "docSettings", getFacRegDocSettings());

        Map<String, List<DocRecordInfo>> savedFiles = primaryDocDto.getExistDocTypeMap();
        Map<String, List<NewDocInfo>> newFiles = primaryDocDto.getNewDocTypeMap();
        ParamUtil.setRequestAttr(request, "savedFiles", savedFiles);
        ParamUtil.setRequestAttr(request, "newFiles", newFiles);
    }

    public void doDocument(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        NodeGroup facRegRoot = getFacCertifierRegisterRoot(request);
        String currentNodePath = NODE_NAME_FAC_PRIMARY_DOCUMENT;
        SimpleNode primaryDocNode = (SimpleNode) facRegRoot.at(currentNodePath);
        PrimaryDocDto primaryDocDto = (PrimaryDocDto) primaryDocNode.getValue();
        primaryDocDto.reqObjMapping(request);

        String actionType = ParamUtil.getString(request, KEY_ACTION_TYPE);
        if (KEY_ACTION_JUMP.equals(actionType)) {
            jumpHandler(request, facRegRoot, currentNodePath, primaryDocNode);
        } else {
            throw new IaisRuntimeException(ERR_MSG_INVALID_ACTION);
        }
        ParamUtil.setSessionAttr(request, KEY_ROOT_NODE_GROUP, facRegRoot);
    }

    public void preparePreview(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        NodeGroup facRegRoot = getFacCertifierRegisterRoot(request);
        SimpleNode previewSubmitNode = (SimpleNode) facRegRoot.at(NODE_NAME_CER_PREVIEW_SUBMIT);
        PreviewSubmitDto previewSubmitDto = (PreviewSubmitDto) previewSubmitNode.getValue();
        Boolean needShowError = (Boolean) ParamUtil.getRequestAttr(request, KEY_SHOW_ERROR_SWITCH);
        if (needShowError == Boolean.TRUE) {
            ParamUtil.setRequestAttr(request, KEY_VALIDATION_ERRORS, previewSubmitDto.retrieveValidationResult());
        }
        Nodes.needValidation(facRegRoot,NODE_NAME_CER_PREVIEW_SUBMIT);
        ParamUtil.setRequestAttr(request, NODE_NAME_CER_PREVIEW_SUBMIT, previewSubmitDto);

        ParamUtil.setRequestAttr(request, NODE_NAME_ORG_PROFILE, ((SimpleNode) facRegRoot.at(NODE_NAME_ORGANISATION_INFO + facRegRoot.getPathSeparator() + NODE_NAME_ORG_PROFILE)).getValue());
        ParamUtil.setRequestAttr(request, NODE_NAME_ORG_CERTIFYING_TEAM, ((SimpleNode) facRegRoot.at(NODE_NAME_ORGANISATION_INFO + facRegRoot.getPathSeparator() + NODE_NAME_ORG_CERTIFYING_TEAM)).getValue());
        ParamUtil.setRequestAttr(request, NODE_NAME_ORG_FAC_ADMINISTRATOR, ((SimpleNode) facRegRoot.at(NODE_NAME_ORGANISATION_INFO + facRegRoot.getPathSeparator() + NODE_NAME_ORG_FAC_ADMINISTRATOR)).getValue());
        ParamUtil.setRequestAttr(request, "docSettings", getFacRegDocSettings());
        PrimaryDocDto primaryDocDto = (PrimaryDocDto) ((SimpleNode)facRegRoot.at(NODE_NAME_FAC_PRIMARY_DOCUMENT)).getValue();
        Map<String, List<DocRecordInfo>> savedFiles = primaryDocDto.getExistDocTypeMap();
        Map<String, List<NewDocInfo>> newFiles = primaryDocDto.getNewDocTypeMap();
        ParamUtil.setRequestAttr(request, "savedFiles", savedFiles);
        ParamUtil.setRequestAttr(request, "newFiles", newFiles);
    }

    public void prepare(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        String destNode = (String) ParamUtil.getSessionAttr(request, KEY_JUMP_DEST_NODE);
        ParamUtil.setRequestAttr(request, KEY_DEST_NODE_ROUTE, destNode);
    }

    public void controlSwitch(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        String actionType = (String) ParamUtil.getRequestAttr(request, KEY_ACTION_TYPE);
        if (!StringUtils.hasLength(actionType)) {
            actionType = ParamUtil.getString(request, KEY_ACTION_TYPE);
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
        if (KEY_NAV_NEXT.equals(actionValue) || KEY_NAV_SAVE_DRAFT.equals(actionValue)) {  // if click next, we need to validate current node anyway
            currentLetGo = currentNode.doValidation();
            if (currentLetGo) {
                Nodes.passValidation(facRegRoot, currentPath);
            }
        }
        if (currentLetGo) {
            if(!KEY_NAV_SAVE_DRAFT.equals(actionValue)){
                String destNode = computeDestNodePath(facRegRoot, actionValue);
                String checkedDestNode = Nodes.jump(facRegRoot, destNode);
                if (!checkedDestNode.equals(destNode)) {
                    ParamUtil.setRequestAttr(request, KEY_SHOW_ERROR_SWITCH, Boolean.TRUE);
                }
                ParamUtil.setSessionAttr(request, KEY_JUMP_DEST_NODE, checkedDestNode);
            }else{
                FacilityCertifierRegisterDto finalAllDataDto = FacilityCertifierRegisterDto.from(facRegRoot);
                finalAllDataDto.setAppStatus("BSBAPST011");
                ResponseDto<String> responseDto = facCertifierRegisterClient.saveNewRegisteredFacCertifier(finalAllDataDto);
                log.info("save as draft response: {}", org.apache.commons.lang.StringUtils.normalizeSpace(responseDto.toString()));
                ParamUtil.setSessionAttr(request, KEY_JUMP_DEST_NODE, currentPath);
            }
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

    /**
     * common actions when we do 'saveDraft'
     * decide the routing logic
     * will set a dest node in the request attribute;
     * will set a floag if we need to show the error messages.
     * @param facRegRoot root data structure of this flow
     */
    public void saveDraftHandler(HttpServletRequest request, NodeGroup facRegRoot, String currentPath, Node currentNode) {
        boolean currentLetGo = true;  // if false, we have to stay current node// if click next, we need to validate current node anyway
        currentLetGo = currentNode.doValidation();
        if (currentLetGo) {
            Nodes.passValidation(facRegRoot, currentPath);
        }
        if (currentLetGo) {
            FacilityCertifierRegisterDto finalAllDataDto = FacilityCertifierRegisterDto.from(facRegRoot);
            ResponseDto<String> responseDto = facCertifierRegisterClient.saveNewRegisteredFacCertifier(finalAllDataDto);
            log.info("save as draft response: {}", org.apache.commons.lang.StringUtils.normalizeSpace(responseDto.toString()));
        }else{
            ParamUtil.setRequestAttr(request, KEY_SHOW_ERROR_SWITCH, Boolean.TRUE);
        }
        ParamUtil.setSessionAttr(request, KEY_JUMP_DEST_NODE, currentPath);
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
        Node companyInfoDto = new Node(NODE_NAME_COMPANY_INFO, new Node[0]);
        NodeGroup orgInfoNodeGroup = newOrgInfoNodeGroup(new Node[0]);
        SimpleNode primaryDocNode = new SimpleNode(new PrimaryDocDto(),NODE_NAME_FAC_PRIMARY_DOCUMENT,new Node[]{orgInfoNodeGroup});
        SimpleNode previewSubmitNode = new SimpleNode(new PreviewSubmitDto(),NODE_NAME_CER_PREVIEW_SUBMIT,new Node[]{orgInfoNodeGroup,primaryDocNode});
        return new NodeGroup.Builder().name(name)
                .addNode(companyInfoDto)
                .addNode(orgInfoNodeGroup)
                .addNode(primaryDocNode)
                .addNode(previewSubmitNode)
                .build();
    }

    public static NodeGroup newOrgInfoNodeGroup(Node[] dependNodes) {
        SimpleNode orgProfileNode = new SimpleNode(new OrganisationProfileDto(),NODE_NAME_ORG_PROFILE,new Node[0]);
        SimpleNode certTeamNode = new SimpleNode(new CertifyingTeamDto(),NODE_NAME_ORG_CERTIFYING_TEAM,new Node[]{orgProfileNode});
        SimpleNode administratorNode = new SimpleNode(new AdministratorDto(),NODE_NAME_ORG_FAC_ADMINISTRATOR,new Node[]{orgProfileNode,certTeamNode});

        return new NodeGroup.Builder().name(NODE_NAME_ORGANISATION_INFO)
                .dependNodes(dependNodes)
                .addNode(orgProfileNode)
                .addNode(certTeamNode)
                .addNode(administratorNode)
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

    /* Will be removed in future, will get this from master code */
    private static List<SelectOption> tmpNationalityOps() {
        List<MasterCodeView> views = MasterCodeUtil.retrieveByCategory(MasterCodeUtil.CATE_ID_NATIONALITY);
        List<SelectOption> ops = new ArrayList<>(views.size());
        if(!CollectionUtils.isEmpty(views)){
            for (MasterCodeView view : views) {
                ops.add(new SelectOption(view.getCode(), view.getCodeValue()));
            }
        }
        return ops;
    }

    private static List<SelectOption> tmpCountryOps() {
        return Arrays.asList(new SelectOption(null, TEXT_VALUE_PLEASE_SELECT),new SelectOption(TEXT_VALUE_SINGAPORE, TEXT_VALUE_SINGAPORE), new SelectOption(TEXT_VALUE_CHINA, TEXT_VALUE_CHINA),new SelectOption(TEXT_VALUE_MALAYSIA,TEXT_VALUE_MALAYSIA),new SelectOption("USA","USA"),new SelectOption("UK","UK"));
    }

    private static List<SelectOption> tmpPositionOps() {
        return Arrays.asList(new SelectOption(null, TEXT_VALUE_PLEASE_SELECT),new SelectOption("Biosafety Certifier", "Biosafety Certifier"), new SelectOption("Engineering Certifier", "Engineering Certifier"),new SelectOption("Assistant Biosafety Certifier","Assistant Biosafety Certifier"),new SelectOption("Assistant Engineering Certifier","Assistant Engineering Certifier"));
    }


    /* Will be removed in future, will get this from config mechanism */
    public List<DocSetting> getFacRegDocSettings () {
        List<DocSetting> docSettings = new ArrayList<>();
        docSettings.add(new DocSetting(DocConstants.DOC_TYPE_COMPANY_INFORMATION, "Company Information", true));
        docSettings.add(new DocSetting(DocConstants.DOC_TYPE_SOP_FOR_CERTIFICATION, "SOP for Certification", true));
        docSettings.add(new DocSetting(DocConstants.DOC_TYPE_OTHERS, "Others", false));
        docSettings.add(new DocSetting(DocConstants.DOC_TYPE_TESTIMONIALS, "Testimonials", true));
        docSettings.add(new DocSetting(DocConstants.DOC_TYPE_CURRICULUM_VITAE, "Curriculum Vitae", true));
        return docSettings;
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
        CompareTwoObject.diff(oldFacilityCertifierRegisterDto.getAdministratorDto(), newFacilityCertifierRegisterDto.getAdministratorDto(), diffContentList, AdministratorDto.FacilityAdministratorInfo.class);
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
}

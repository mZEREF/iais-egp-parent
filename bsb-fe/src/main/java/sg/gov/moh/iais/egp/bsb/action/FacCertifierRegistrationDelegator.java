package sg.gov.moh.iais.egp.bsb.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.exception.IaisRuntimeException;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import sg.gov.moh.iais.egp.bsb.client.FacCertifierRegisterClient;
import sg.gov.moh.iais.egp.bsb.common.node.Node;
import sg.gov.moh.iais.egp.bsb.common.node.NodeGroup;
import sg.gov.moh.iais.egp.bsb.common.node.Nodes;
import sg.gov.moh.iais.egp.bsb.dto.ResponseDto;
import sg.gov.moh.iais.egp.bsb.dto.register.facility.FacilityRegisterDto;
import sg.gov.moh.iais.egp.bsb.dto.register.facilityCertifier.*;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;

import static sg.gov.moh.iais.egp.bsb.constant.FacCertifierRegisterConstants.*;


/**
 * AUTHOR: YiMing
 * DATE:2021/9/26 15:04
 * DESCRIPTION: TODO
 **/

@Slf4j
@Delegator("bsbFacCertifierRegisterDelegator")
public class FacCertifierRegistrationDelegator {
    private static final String KEY_ROOT_NODE_GROUP = "facCertifierRegRoot";
    private static final String KEY_ACTION_TYPE = "action_type";
    private static final String KEY_INDEED_ACTION_TYPE = "indeed_action_type";
    private static final String KEY_ACTION_VALUE = "action_value";
    private static final String KEY_VALIDATION_ERRORS = "errorMsg";

    private static final String KEY_NAV_NEXT = "next";
    private static final String KEY_NAV_BACK = "back";

    private static final String KEY_ACTION_SUBMIT = "submit";
    private static final String KEY_ACTION_JUMP = "jump";
    private static final String KEY_JUMP_DEST_NODE = "destNode";
    private static final String KEY_DEST_NODE_ROUTE = "nodeRoute";

    private static final String KEY_SHOW_ERROR_SWITCH = "needShowValidationError";

    private static final String ERR_MSG_NULL_GROUP = "Node group must not be null!";
    private static final String ERR_MSG_NULL_NAME = "Name must not be null!";
    private static final String ERR_MSG_INVALID_ACTION = "Invalid action";

    @Autowired
    private FacCertifierRegisterClient facCertifierRegisterClient;


    public void init(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        request.getSession().removeAttribute(KEY_ROOT_NODE_GROUP);
        ParamUtil.setSessionAttr(request, KEY_ROOT_NODE_GROUP, getFacCertifierRegisterRoot(request));
    }

    public void start(BaseProcessClass bpc){

    }

    public void preCompInfo(BaseProcessClass bpc){
        // do nothing now, need to prepare company info in the future
    }

    public void prepare(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        String destNode = (String) ParamUtil.getSessionAttr(request, KEY_JUMP_DEST_NODE);
        ParamUtil.setRequestAttr(request, KEY_DEST_NODE_ROUTE, destNode);
    }

    public void preOrganisationInfo(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        NodeGroup facRegRoot = getFacCertifierRegisterRoot(request);
        String currentNodePath = NODE_NAME_ORGANISATION_INFO + facRegRoot.getPathSeparator() + NODE_NAME_ORG_PROFILE;
        OrganisationProfileDto orgProfileDto = (OrganisationProfileDto) facRegRoot.at(currentNodePath);
        Boolean needShowError = (Boolean) ParamUtil.getRequestAttr(request, KEY_SHOW_ERROR_SWITCH);
        if (needShowError == Boolean.TRUE) {
            ParamUtil.setRequestAttr(request, KEY_VALIDATION_ERRORS, orgProfileDto.retrieveValidationResult());
        }
        orgProfileDto.needValidation();
        ParamUtil.setRequestAttr(request, NODE_NAME_ORG_PROFILE, orgProfileDto);
    }

    public void preAdministrator(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        NodeGroup facRegRoot = getFacCertifierRegisterRoot(request);
        String currentNodePath = NODE_NAME_ORGANISATION_INFO + facRegRoot.getPathSeparator() + NODE_NAME_ORG_FAC_ADMINISTRATOR;
        AdministratorDto administratorDto = (AdministratorDto) facRegRoot.at(currentNodePath);
        Boolean needShowError = (Boolean) ParamUtil.getRequestAttr(request, KEY_SHOW_ERROR_SWITCH);
        if (needShowError == Boolean.TRUE) {
            ParamUtil.setRequestAttr(request, KEY_VALIDATION_ERRORS, administratorDto.retrieveValidationResult());
        }
        administratorDto.needValidation();
        ParamUtil.setRequestAttr(request, NODE_NAME_ORG_PROFILE, administratorDto);
    }

    public void preCertifyingTeam(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        NodeGroup facRegRoot = getFacCertifierRegisterRoot(request);
        String currentNodePath = NODE_NAME_ORGANISATION_INFO + facRegRoot.getPathSeparator() + NODE_NAME_ORG_CERTIFYING_TEAM;
        CertifyingTeamDto certifyingTeamDto = (CertifyingTeamDto) facRegRoot.at(currentNodePath);Boolean needShowError = (Boolean) ParamUtil.getRequestAttr(request, KEY_SHOW_ERROR_SWITCH);
        if (needShowError == Boolean.TRUE) {
            ParamUtil.setRequestAttr(request, KEY_VALIDATION_ERRORS, certifyingTeamDto.retrieveValidationResult());
        }
        certifyingTeamDto.needValidation();
        ParamUtil.setRequestAttr(request, NODE_NAME_ORG_CERTIFYING_TEAM, certifyingTeamDto);
    }

    public void prepareDocuments(BaseProcessClass bpc){

    }

    public void preparePreview(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        NodeGroup facRegRoot = getFacCertifierRegisterRoot(request);
        PreviewSubmitDto previewSubmitDto = (PreviewSubmitDto) facRegRoot.at(NODE_NAME_CER_PREVIEW_SUBMIT);
        Boolean needShowError = (Boolean) ParamUtil.getRequestAttr(request, KEY_SHOW_ERROR_SWITCH);
        if (needShowError == Boolean.TRUE) {
            ParamUtil.setRequestAttr(request, KEY_VALIDATION_ERRORS, previewSubmitDto.retrieveValidationResult());
        }
        previewSubmitDto.needValidation();
        ParamUtil.setRequestAttr(request, NODE_NAME_CER_PREVIEW_SUBMIT, previewSubmitDto);

        ParamUtil.setRequestAttr(request, NODE_NAME_ORG_PROFILE, facRegRoot.at(NODE_NAME_ORGANISATION_INFO + facRegRoot.getPathSeparator() + NODE_NAME_ORG_PROFILE));
        ParamUtil.setRequestAttr(request, NODE_NAME_ORG_CERTIFYING_TEAM, facRegRoot.at(NODE_NAME_ORGANISATION_INFO + facRegRoot.getPathSeparator() + NODE_NAME_ORG_CERTIFYING_TEAM));
        ParamUtil.setRequestAttr(request, NODE_NAME_ORG_FAC_ADMINISTRATOR, facRegRoot.at(NODE_NAME_ORGANISATION_INFO + facRegRoot.getPathSeparator() + NODE_NAME_ORG_FAC_ADMINISTRATOR));
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

    public void doAdministrator(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        NodeGroup facCertifierRegRoot = getFacCertifierRegisterRoot(request);
        String currentNodePath = NODE_NAME_ORGANISATION_INFO + facCertifierRegRoot.getPathSeparator() + NODE_NAME_ORG_FAC_ADMINISTRATOR;
        AdministratorDto administratorDto = (AdministratorDto) facCertifierRegRoot.at(currentNodePath);
        administratorDto.reqObjMapping(request);
        String actionType = ParamUtil.getString(request, KEY_ACTION_TYPE);
        if (KEY_ACTION_JUMP.equals(actionType)) {
            jumpHandler(request, facCertifierRegRoot, currentNodePath, administratorDto);
        } else {
            throw new IaisRuntimeException(ERR_MSG_INVALID_ACTION);
        }
        ParamUtil.setSessionAttr(request, KEY_ROOT_NODE_GROUP, facCertifierRegRoot);
    }

    public void doCertifyingTeam(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        NodeGroup facRegRoot = getFacCertifierRegisterRoot(request);
        String currentNodePath = NODE_NAME_ORGANISATION_INFO + facRegRoot.getPathSeparator() + NODE_NAME_ORG_CERTIFYING_TEAM;
        CertifyingTeamDto certifyingTeamDto = (CertifyingTeamDto) facRegRoot.at(currentNodePath);
        certifyingTeamDto.reqObjMapping(request);
        String actionType = ParamUtil.getString(request, KEY_ACTION_TYPE);
        if (KEY_ACTION_JUMP.equals(actionType)) {
            jumpHandler(request, facRegRoot, currentNodePath, certifyingTeamDto);
        } else {
            throw new IaisRuntimeException(ERR_MSG_INVALID_ACTION);
        }
        ParamUtil.setSessionAttr(request, KEY_ROOT_NODE_GROUP, facRegRoot);
    }

    public void doOrganisationProfile(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        NodeGroup facRegRoot = getFacCertifierRegisterRoot(request);
        String currentNodePath = NODE_NAME_ORGANISATION_INFO + facRegRoot.getPathSeparator() + NODE_NAME_ORG_PROFILE;
        OrganisationProfileDto orgProfile = (OrganisationProfileDto) facRegRoot.at(currentNodePath);
        orgProfile.reqObjMapping(request);
        String actionType = ParamUtil.getString(request, KEY_ACTION_TYPE);
        if (KEY_ACTION_JUMP.equals(actionType)) {
            jumpHandler(request, facRegRoot, currentNodePath, orgProfile);
        } else {
            throw new IaisRuntimeException(ERR_MSG_INVALID_ACTION);
        }
        ParamUtil.setSessionAttr(request, KEY_ROOT_NODE_GROUP, facRegRoot);
    }

    public void doDocument(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        NodeGroup facRegRoot = getFacCertifierRegisterRoot(request);
        String currentNodePath = NODE_NAME_FAC_PRIMARY_DOCUMENT;
        PrimaryDocDto primaryDocDto = (PrimaryDocDto) facRegRoot.at(currentNodePath);
        primaryDocDto.reqObjMapping(request);

        String actionType = ParamUtil.getString(request, KEY_ACTION_TYPE);
        if (KEY_ACTION_JUMP.equals(actionType)) {
            jumpHandler(request, facRegRoot, currentNodePath, primaryDocDto);
        } else {
            throw new IaisRuntimeException(ERR_MSG_INVALID_ACTION);
        }
        ParamUtil.setSessionAttr(request, KEY_ROOT_NODE_GROUP, facRegRoot);
    }

    public void doPreview(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        NodeGroup facRegRoot = getFacCertifierRegisterRoot(request);
        String currentNodePath = NODE_NAME_CER_PREVIEW_SUBMIT;
        PreviewSubmitDto previewSubmitDto = (PreviewSubmitDto) facRegRoot.at(currentNodePath);
        previewSubmitDto.reqObjMapping(request);

        String actionType = ParamUtil.getString(request, KEY_ACTION_TYPE);
        String actionValue = ParamUtil.getString(request, KEY_ACTION_VALUE);
        if (KEY_ACTION_JUMP.equals(actionType)) {
            if (KEY_NAV_NEXT.equals(actionValue)) {
                if (previewSubmitDto.doValidation()) {
                    previewSubmitDto.passValidation();

                    // save data
                    FacilityCertifierRegisterDto finalAllDataDto = FacilityCertifierRegisterDto.from(facRegRoot);
                    ResponseDto<String> responseDto = facCertifierRegisterClient.saveNewRegisteredFacCertifier(finalAllDataDto);
                    log.info("save new facility response: {}", responseDto);

                    ParamUtil.setRequestAttr(request, KEY_ACTION_TYPE, KEY_ACTION_SUBMIT);
                } else {
                    ParamUtil.setRequestAttr(request, KEY_SHOW_ERROR_SWITCH, Boolean.TRUE);
                    ParamUtil.setSessionAttr(request, KEY_JUMP_DEST_NODE, NODE_NAME_CER_PREVIEW_SUBMIT);
                }
            } else {
                jumpHandler(request, facRegRoot, currentNodePath, previewSubmitDto);
            }
        } else {
            throw new IaisRuntimeException(ERR_MSG_INVALID_ACTION);
        }
        ParamUtil.setSessionAttr(request, KEY_ROOT_NODE_GROUP, facRegRoot);
    }

    public void controlSwitch(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        String actionType = (String) ParamUtil.getRequestAttr(request, KEY_ACTION_TYPE);
        if (!StringUtils.hasLength(actionType)) {
            actionType = ParamUtil.getString(request, KEY_ACTION_TYPE);
        }
        ParamUtil.setRequestAttr(request, KEY_INDEED_ACTION_TYPE, actionType);
    }

    public void doSubmit(BaseProcessClass bpc){

    }
    public void doSaveDraft(BaseProcessClass bpc){

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
        PrimaryDocDto primaryDocDto = PrimaryDocDto.getInstance(new Node[]{orgInfoNodeGroup});
        PreviewSubmitDto previewSubmitDto = PreviewSubmitDto.getInstance(new Node[]{orgInfoNodeGroup,primaryDocDto});
        return new NodeGroup.Builder().name(name)
                .addNode(companyInfoDto)
                .addNode(orgInfoNodeGroup)
                .addNode(primaryDocDto)
                .addNode(previewSubmitDto)
                .build();
    }

    public static NodeGroup newOrgInfoNodeGroup(Node[] dependNodes) {
        OrganisationProfileDto orgProfileDto = OrganisationProfileDto.getInstance(new Node[0]);
        CertifyingTeamDto certTeamDto = CertifyingTeamDto.getInstance(new Node[]{orgProfileDto});
        AdministratorDto administratorDto = AdministratorDto.getInstance(new Node[]{orgProfileDto,certTeamDto});

        return new NodeGroup.Builder().name(NODE_NAME_ORGANISATION_INFO)
                .dependNodes(dependNodes)
                .addNode(orgProfileDto)
                .addNode(certTeamDto)
                .addNode(administratorDto)
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

}

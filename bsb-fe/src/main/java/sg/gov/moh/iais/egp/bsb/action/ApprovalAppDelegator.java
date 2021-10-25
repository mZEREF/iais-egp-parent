package sg.gov.moh.iais.egp.bsb.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.exception.IaisRuntimeException;
import com.ecquaria.cloud.moh.iais.common.utils.MaskUtil;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import sg.gov.moh.iais.egp.bsb.client.ApprovalAppClient;
import sg.gov.moh.iais.egp.bsb.common.node.Node;
import sg.gov.moh.iais.egp.bsb.common.node.NodeGroup;
import sg.gov.moh.iais.egp.bsb.common.node.Nodes;
import sg.gov.moh.iais.egp.bsb.common.node.simple.SimpleNode;
import sg.gov.moh.iais.egp.bsb.constant.ApprovalApplicationConstants;
import sg.gov.moh.iais.egp.bsb.dto.ResponseDto;
import sg.gov.moh.iais.egp.bsb.dto.approvalApp.*;
import sg.gov.moh.iais.egp.bsb.dto.register.facility.BiologicalAgentToxinDto;
import sg.gov.moh.iais.egp.bsb.dto.register.facility.FacilitySelectionDto;
import sg.gov.moh.iais.egp.bsb.entity.Facility;
import sg.gov.moh.iais.egp.bsb.util.LogUtil;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;

import java.util.ArrayList;
import java.util.List;

import static sg.gov.moh.iais.egp.bsb.constant.ApprovalAppConstants.*;

/**
 * @author : LiRan
 * @date : 2021/10/8
 */
@Slf4j
@Delegator("bsbApprovalAppDelegator")
public class ApprovalAppDelegator {
    private static final String KEY_ROOT_NODE_GROUP = "approvalAppRoot";

    private static final String KEY_EDIT_APP_ID = "editId";
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

    private static final String KEY_NATIONALITY_OPTIONS = "nationalityOps";

    private static final String ERR_MSG_BAT_NOT_NULL = "Biological Agent/Toxin node group must not be null!";
    private static final String ERR_MSG_NULL_GROUP = "Node group must not be null!";
    private static final String ERR_MSG_NULL_NAME = "Name must not be null!";
    private static final String ERR_MSG_INVALID_ACTION = "Invalid action";

    private final ApprovalAppClient approvalAppClient;

    @Autowired
    public ApprovalAppDelegator(ApprovalAppClient approvalAppClient) {
        this.approvalAppClient = approvalAppClient;
    }

    public void init(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        request.getSession().removeAttribute(KEY_ROOT_NODE_GROUP);
        boolean newApprovalApp = true;
        // check if we are doing editing
        String maskedAppId = request.getParameter(KEY_EDIT_APP_ID);
        if (StringUtils.hasLength(maskedAppId)) {
            if (log.isInfoEnabled()) {
                log.info("masked app ID: {}", LogUtil.escapeCrlf(maskedAppId));
            }
            newApprovalApp = false;
            boolean failRetrieveEditData = true;
            String appId = MaskUtil.unMaskValue(KEY_EDIT_APP_ID, maskedAppId);
            if (appId != null && !maskedAppId.equals(appId)) {
                ResponseDto<ApprovalAppDto> resultDto = approvalAppClient.getApprovalAppAppData(appId);
                if (resultDto.ok()) {
                    failRetrieveEditData = false;
                    //TODO
                    NodeGroup approvalAppNodeGroup = resultDto.getEntity().toApprovalAppRootGroup(KEY_ROOT_NODE_GROUP);
                    ParamUtil.setSessionAttr(request, KEY_ROOT_NODE_GROUP, approvalAppNodeGroup);
                }
            }
            if (failRetrieveEditData) {
                throw new IaisRuntimeException("Fail to retrieve app data");
            }
        }
        if (newApprovalApp) {
            ParamUtil.setSessionAttr(request, KEY_ROOT_NODE_GROUP, getApprovalAppRoot(request));
        }
    }

    public void start(BaseProcessClass bpc){

    }

    public void prepareCompanyInfo(BaseProcessClass bpc){
        // do nothing now, need to prepare company info in the future
    }

    public void handleCompanyInfo(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        NodeGroup approvalAppRoot = getApprovalAppRoot(request);
        Node compInfoNode = approvalAppRoot.getNode(NODE_NAME_COMPANY_INFO);
        String actionType = ParamUtil.getString(request, KEY_ACTION_TYPE);
        if (KEY_ACTION_JUMP.equals(actionType)) {
            jumpHandler(request, approvalAppRoot, NODE_NAME_COMPANY_INFO, compInfoNode);
        } else {
            throw new IaisRuntimeException(ERR_MSG_INVALID_ACTION);
        }
        ParamUtil.setSessionAttr(request, KEY_ROOT_NODE_GROUP, approvalAppRoot);
    }

    public void prepareActivity(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        List<Facility> facilityList = approvalAppClient.getAllMainActApprovalFac().getEntity();
        List<SelectOption> facilityNameList =  new ArrayList<>(facilityList.size());
        if (facilityList != null && !facilityList.isEmpty()){
            for (Facility fac : facilityList) {
                facilityNameList.add(new SelectOption(fac.getId(),fac.getFacilityName()));
            }
        }
        ParamUtil.setRequestAttr(request, ApprovalApplicationConstants.FACILITY_NAME_SELECT, facilityNameList);
        // TODO test my code how to add to chenwei
        NodeGroup approvalAppRoot = getApprovalAppRoot(request);
        SimpleNode activityNode = (SimpleNode) approvalAppRoot.getNode(NODE_NAME_ACTIVITY);
        ActivityDto activityDto = (ActivityDto)activityNode.getValue();
        Boolean needShowError = (Boolean) ParamUtil.getRequestAttr(request, KEY_SHOW_ERROR_SWITCH);
        if (needShowError == Boolean.TRUE) {
            ParamUtil.setRequestAttr(request, KEY_VALIDATION_ERRORS, activityDto.retrieveValidationResult());
        }
        activityNode.needValidation();
        ParamUtil.setRequestAttr(request, NODE_NAME_ACTIVITY, activityDto);
    }

    public void handleActivity(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        NodeGroup approvalAppRoot = getApprovalAppRoot(request);
        //TODO ??? one floor code what is currentNodePath
        SimpleNode activityNode = (SimpleNode) approvalAppRoot.getNode(NODE_NAME_ACTIVITY);
        ActivityDto activityDto = (ActivityDto) activityNode.getValue();
        activityDto.reqObjMapping(request);

        String actionType = ParamUtil.getString(request, KEY_ACTION_TYPE);
        if (KEY_ACTION_JUMP.equals(actionType)) {
            jumpHandler(request, approvalAppRoot, NODE_NAME_ACTIVITY, activityNode);
        } else {
            throw new IaisRuntimeException(ERR_MSG_INVALID_ACTION);
        }

        if (activityNode.isValidated()) {
            NodeGroup approvalProfileGroup = (NodeGroup) approvalAppRoot.getNode(NODE_NAME_APPROVAL_PROFILE);
            //todo jixu
            /*changeBatNodeGroup(approvalProfileGroup, activityDto);*/
        }
        ParamUtil.setSessionAttr(request, KEY_ROOT_NODE_GROUP, approvalAppRoot);
    }

    public void prepareApprovalProfile(BaseProcessClass bpc){
    }

    public void handleApprovalProfile(BaseProcessClass bpc){
    }

    public void prepareDocuments(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        NodeGroup approvalAppRoot = getApprovalAppRoot(request);
        SimpleNode primaryDocNode = (SimpleNode) approvalAppRoot.at(NODE_NAME_PRIMARY_DOC);
        PrimaryDocDto primaryDocDto = (PrimaryDocDto)primaryDocNode.getValue();
        Boolean needShowError = (Boolean) ParamUtil.getRequestAttr(request, KEY_SHOW_ERROR_SWITCH);
        if (needShowError == Boolean.TRUE) {
            ParamUtil.setRequestAttr(request, KEY_VALIDATION_ERRORS, primaryDocDto.retrieveValidationResult());
        }
        primaryDocNode.needValidation();
        ParamUtil.setRequestAttr(request, NODE_NAME_PRIMARY_DOC, primaryDocDto);
    }

    public void handleDocuments(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        NodeGroup approvalAppRoot = getApprovalAppRoot(request);
        String currentNodePath = NODE_NAME_PRIMARY_DOC;
        SimpleNode primaryDocNode = (SimpleNode) approvalAppRoot.at(NODE_NAME_PRIMARY_DOC);
        PrimaryDocDto primaryDocDto = (PrimaryDocDto) primaryDocNode.getValue();
        primaryDocDto.reqObjMapping(request);

        String actionType = ParamUtil.getString(request, KEY_ACTION_TYPE);
        if (KEY_ACTION_JUMP.equals(actionType)) {
            jumpHandler(request, approvalAppRoot, currentNodePath, primaryDocNode);
        } else {
            throw new IaisRuntimeException(ERR_MSG_INVALID_ACTION);
        }
        ParamUtil.setSessionAttr(request, KEY_ROOT_NODE_GROUP, approvalAppRoot);
    }

    public void preparePreview(BaseProcessClass bpc){
    }

    public void handlePreview(BaseProcessClass bpc){
    }

    public void actionFilter(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        String actionType = (String) ParamUtil.getRequestAttr(request, KEY_ACTION_TYPE);
        if (!StringUtils.hasLength(actionType)) {
            actionType = ParamUtil.getString(request, KEY_ACTION_TYPE);
        }
        ParamUtil.setRequestAttr(request, KEY_INDEED_ACTION_TYPE, actionType);
    }

    /**
     * Do special route changes.
     * This method is used when we re-use some pages for different nodes,
     * then we need to resolve the nodes to the same destination.
     */
    public void jumpFilter(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        String destNode = (String) ParamUtil.getSessionAttr(request, KEY_JUMP_DEST_NODE);
        destNode = approvalProfileNodeSpecialHandle(destNode);
        ParamUtil.setRequestAttr(request, KEY_DEST_NODE_ROUTE, destNode);
    }

    public void doSaveDraft(BaseProcessClass bpc){
    }

    public void doSubmit(BaseProcessClass bpc){
    }

    /**
     * Get the root data structure of this flow
     */
    public NodeGroup getApprovalAppRoot(HttpServletRequest request) {
        NodeGroup root = (NodeGroup) ParamUtil.getSessionAttr(request, KEY_ROOT_NODE_GROUP);
        if (root == null) {
            root = newApprovalAppRoot(KEY_ROOT_NODE_GROUP);
        }
        return root;
    }

    public static NodeGroup newApprovalAppRoot(String name) {
        Node companyInfoNode = new Node(NODE_NAME_COMPANY_INFO, new Node[0]);
        //TODO primaryDocDto previewSubmitDto is not mine
        SimpleNode activityNode = new SimpleNode(new ActivityDto(),NODE_NAME_ACTIVITY,new Node[0]);
        NodeGroup approvalProfileNodeGroup = initApprovalProfileNodeGroup(new Node[]{activityNode});
        SimpleNode primaryDocNode = new SimpleNode(new PrimaryDocDto(),NODE_NAME_PRIMARY_DOC,new Node[]{approvalProfileNodeGroup,approvalProfileNodeGroup});
        SimpleNode previewSubmitNode = new SimpleNode(new PreviewSubmitDto(),NODE_NAME_PREVIEW_SUBMIT,new Node[]{approvalProfileNodeGroup,approvalProfileNodeGroup,primaryDocNode});
        NodeGroup build = new NodeGroup.Builder().name(name)
                .addNode(companyInfoNode)
                .addNode(activityNode)
                .addNode(approvalProfileNodeGroup)
                .addNode(primaryDocNode)
                .addNode(previewSubmitNode)
                .build();
        return build;
    }

    public static NodeGroup initApprovalProfileNodeGroup(Node[] dependNodes) {
        return new NodeGroup.Builder().name(NODE_NAME_APPROVAL_PROFILE)
                .dependNodes(dependNodes)
                .addNode(new Node("error", new Node[0]))
                .build();
    }

    public static void changeApprovalProfileNodeGroup(NodeGroup approvalProfileNodeGroup, ActivityDto activityDto) {
        // TODO errorMessage condition update
        /*Assert.notNull(approvalProfileNodeGroup, ERR_MSG_BAT_NOT_NULL);
        Node[] subNodes = new Node[activityDto.getSchedules().size()];
        List<String> activityTypes = activityDto.getSchedules();
        for (int i = 0; i < activityTypes.size(); i++) {
            subNodes[i] = new SimpleNode(new ApprovalProfileDto(activityTypes.get(i)), activityTypes.get(i), new Node[0]);
        }
        approvalProfileNodeGroup.replaceNodes(subNodes);*/
    }

    /**
     * Compute the destiny node path we will go to.
     * @param approvalAppRoot the root NodeGroup
     * @param actionValue the value we received, it can be next, back, or a value specified by tab
     * @return the destiny node path, return null if we can't go the next or previous node
     */
    public String computeDestNodePath(NodeGroup approvalAppRoot, String actionValue) {
        String destNode;
        switch (actionValue) {
            case KEY_NAV_NEXT:
                destNode = Nodes.getNextNodePath(approvalAppRoot);
                break;
            case KEY_NAV_BACK:
                destNode = Nodes.getPreviousNodePath(approvalAppRoot);
                break;
            default:
                Assert.hasText(actionValue, "Action value should be a node path");
                destNode = Nodes.expandNode(approvalAppRoot, actionValue);
                break;
        }
        return destNode;
    }

    /**
     * common actions when we do 'jump'
     * decide the routing logic
     * will set a dest node in the request attribute;
     * will set a flag if we need to show the error messages.
     * @param approvalAppRoot root data structure of this flow
     */
    public void jumpHandler(HttpServletRequest request, NodeGroup approvalAppRoot, String currentPath, Node currentNode) {
        String actionValue = ParamUtil.getString(request, KEY_ACTION_VALUE);
        Assert.hasText(actionValue, "Invalid action value");
        boolean currentLetGo = true;  // if false, we have to stay current node
        if (KEY_NAV_NEXT.equals(actionValue)) {  // if click next, we need to validate current node anyway
            currentLetGo = currentNode.doValidation();
            if (currentLetGo) {
                Nodes.passValidation(approvalAppRoot, currentPath);
            }
        }
        if (currentLetGo) {
            String destNode = computeDestNodePath(approvalAppRoot, actionValue);
            String checkedDestNode = Nodes.jump(approvalAppRoot, destNode);
            if (!checkedDestNode.equals(destNode)) {
                ParamUtil.setRequestAttr(request, KEY_SHOW_ERROR_SWITCH, Boolean.TRUE);
            }
            ParamUtil.setSessionAttr(request, KEY_JUMP_DEST_NODE, checkedDestNode);
        } else {
            ParamUtil.setRequestAttr(request, KEY_SHOW_ERROR_SWITCH, Boolean.TRUE);
            ParamUtil.setSessionAttr(request, KEY_JUMP_DEST_NODE, currentPath);
        }
    }

    public String approvalProfileNodeSpecialHandle(String destNode) {
        return destNode.startsWith(NODE_NAME_APPROVAL_PROFILE) ? NODE_NAME_APPROVAL_PROFILE : destNode;
    }
}

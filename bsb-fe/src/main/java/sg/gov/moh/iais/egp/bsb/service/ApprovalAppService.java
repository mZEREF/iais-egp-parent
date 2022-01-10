package sg.gov.moh.iais.egp.bsb.service;

import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.mastercode.MasterCodeView;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.helper.MasterCodeUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import sg.gov.moh.iais.egp.bsb.common.node.Node;
import sg.gov.moh.iais.egp.bsb.common.node.NodeGroup;
import sg.gov.moh.iais.egp.bsb.common.node.Nodes;
import sg.gov.moh.iais.egp.bsb.common.node.simple.SimpleNode;
import sg.gov.moh.iais.egp.bsb.common.rfc.CompareTwoObject;
import sg.gov.moh.iais.egp.bsb.constant.DocConstants;
import sg.gov.moh.iais.egp.bsb.dto.approval.ActivityDto;
import sg.gov.moh.iais.egp.bsb.dto.approval.ApprovalAppDto;
import sg.gov.moh.iais.egp.bsb.dto.approval.ApprovalProfileDto;
import sg.gov.moh.iais.egp.bsb.dto.approval.PrimaryDocDto;
import sg.gov.moh.iais.egp.bsb.dto.rfc.DiffContent;
import sg.gov.moh.iais.egp.bsb.entity.DocSetting;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

import static sg.gov.moh.iais.egp.bsb.constant.ApprovalAppConstants.*;
import static sg.gov.moh.iais.egp.bsb.constant.ApprovalAppConstants.NODE_NAME_APPROVAL_PROFILE;

/**
 * @author : LiRan
 * @date : 2021/12/9
 */
@Service
@Slf4j
public class ApprovalAppService {
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
        SimpleNode activityNode = new SimpleNode(new ActivityDto(),NODE_NAME_ACTIVITY,new Node[0]);
        NodeGroup approvalProfileNodeGroup = initApprovalProfileNodeGroup(new Node[]{activityNode});
        SimpleNode primaryDocNode = new SimpleNode(new PrimaryDocDto(),NODE_NAME_PRIMARY_DOC,new Node[]{activityNode,approvalProfileNodeGroup});
        Node previewSubmitNode = new Node(NODE_NAME_PREVIEW_SUBMIT,new Node[]{activityNode,approvalProfileNodeGroup,primaryDocNode});

        return new NodeGroup.Builder().name(name)
                .addNode(companyInfoNode)
                .addNode(activityNode)
                .addNode(approvalProfileNodeGroup)
                .addNode(primaryDocNode)
                .addNode(previewSubmitNode)
                .build();
    }

    public static NodeGroup initApprovalProfileNodeGroup(Node[] dependNodes) {
        return new NodeGroup.Builder().name(NODE_NAME_APPROVAL_PROFILE)
                .dependNodes(dependNodes)
                .addNode(new Node("error", new Node[0]))
                .build();
    }

    /**
     * change new approvalProfileGroup to approvalAppRoot by scheduleList
     */
    public static void changeApprovalProfileNodeGroup(NodeGroup approvalProfileNodeGroup, ActivityDto activityDto) {
        Assert.notNull(approvalProfileNodeGroup, ERR_MSG_BAT_NOT_NULL);
        Node[] subNodes = new Node[activityDto.getSchedules().size()];
        List<String> schedules = activityDto.getSchedules();
        for (int i = 0; i < schedules.size(); i++) {
            subNodes[i] = new SimpleNode(new ApprovalProfileDto(schedules.get(i)), schedules.get(i), new Node[0]);
        }
        approvalProfileNodeGroup.reorganizeNodes(subNodes);
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

    /**
     * Compute the class name for the nav tab.
     * The result will be active, complete or incomplete
     * @param group the node group of the approvalAppRoot
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
     *
     * @param group the node group of the approvalAppRoot
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

    /* Will be removed in future, will get this from config mechanism */
    public List<DocSetting> getApprovalAppDocSettings () {
        List<DocSetting> docSettings = new ArrayList<>(5);
        docSettings.add(new DocSetting(DocConstants.DOC_TYPE_BIO_SAFETY_COM, "Approval/Endorsement: Biosafety Committee", true));
        docSettings.add(new DocSetting(DocConstants.DOC_TYPE_RISK_ASSESSMENT, "Risk Assessment", false));
        docSettings.add(new DocSetting(DocConstants.DOC_TYPE_STANDARD_OPERATING_PROCEDURE, "Standard Operating Procedure (SOP)", false));
        docSettings.add(new DocSetting(DocConstants.DOC_TYPE_GMAC_ENDORSEMENT, "GMAC Endorsement", false));
        docSettings.add(new DocSetting(DocConstants.DOC_TYPE_OTHERS, "Others", false));
        return docSettings;
    }

    public static List<ApprovalProfileDto> getApprovalProfileList(NodeGroup approvalProfileNodeGroup) {
        Assert.notNull(approvalProfileNodeGroup, ERR_MSG_BAT_NOT_NULL);
        List<ApprovalProfileDto> approvalProfileList = new ArrayList<>(approvalProfileNodeGroup.count());
        for (Node node : approvalProfileNodeGroup.getAllNodes()) {
            assert node instanceof SimpleNode;
            approvalProfileList.add((ApprovalProfileDto) ((SimpleNode) node).getValue());
        }
        return approvalProfileList;
    }

    /* Will be removed in future, will get this from master code */
    public static List<SelectOption> tmpCountryOps() {
        List<MasterCodeView> views = MasterCodeUtil.retrieveByCategory(MasterCodeUtil.CATE_ID_NATIONALITY);
        List<SelectOption> ops = new ArrayList<>(views.size());
        if(!org.springframework.util.CollectionUtils.isEmpty(views)){
            for (MasterCodeView view : views) {
                ops.add(new SelectOption(view.getCode(), view.getCodeValue()));
            }
        }
        return ops;
    }

    /**
     * rfc compare
     */
    public List<DiffContent> compareTwoDto(ApprovalAppDto oldApprovalAppDto, ApprovalAppDto newApprovalAppDto){
        List<DiffContent> diffContentList = new ArrayList<>();
        CompareTwoObject.diff(oldApprovalAppDto.getActivityDto(), newApprovalAppDto.getActivityDto(), diffContentList);
        CompareTwoObject.diffMap(oldApprovalAppDto.getApprovalProfileMap(), newApprovalAppDto.getApprovalProfileMap(), diffContentList, ApprovalProfileDto.BATInfo.class);
        //docRecordInfos don't process
        return diffContentList;
    }
}

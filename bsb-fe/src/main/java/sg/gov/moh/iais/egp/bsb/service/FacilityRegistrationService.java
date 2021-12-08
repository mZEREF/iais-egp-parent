package sg.gov.moh.iais.egp.bsb.service;

import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.mastercode.MasterCodeView;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.helper.MasterCodeUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import sg.gov.moh.iais.egp.bsb.common.node.Node;
import sg.gov.moh.iais.egp.bsb.common.node.NodeGroup;
import sg.gov.moh.iais.egp.bsb.common.node.Nodes;
import sg.gov.moh.iais.egp.bsb.common.node.simple.SimpleNode;
import sg.gov.moh.iais.egp.bsb.constant.DocConstants;
import sg.gov.moh.iais.egp.bsb.dto.register.facility.*;
import sg.gov.moh.iais.egp.bsb.entity.DocSetting;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

import static sg.gov.moh.iais.egp.bsb.constant.FacRegisterConstants.*;
import static sg.gov.moh.iais.egp.bsb.constant.FacRegisterConstants.NODE_NAME_FAC_BAT_INFO;


/**
 * @author : LiRan
 * @date : 2021/12/8
 */
@Service
@Slf4j
public class FacilityRegistrationService {
    /**
     * Get the root data structure of this flow
     */
    public NodeGroup getFacilityRegisterRoot(HttpServletRequest request) {
        NodeGroup root = (NodeGroup) ParamUtil.getSessionAttr(request, KEY_ROOT_NODE_GROUP);
        if (root == null) {
            root = newFacRegisterRoot(KEY_ROOT_NODE_GROUP);
        }
        return root;
    }

    /**
     * Compute the destiny node path we will go to.
     * @param facRegRoot the root NodeGroup
     * @param actionValue the value we received, it can be next, back, or a value specified by tab
     * @return the destiny node path, return null if we can't go the next or previous node
     */
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

    public String batNodeSpecialHandle(String destNode) {
        return destNode.startsWith(NODE_NAME_FAC_BAT_INFO) ? NODE_NAME_FAC_BAT_INFO : destNode;
    }

    public static NodeGroup newFacRegisterRoot(String name) {
        Node companyInfoNode = new Node(NODE_NAME_COMPANY_INFO, new Node[0]);
        SimpleNode facSelectionNode = new SimpleNode(new FacilitySelectionDto(), NODE_NAME_FAC_SELECTION, new Node[0]);
        NodeGroup facInfoNodeGroup = newFacInfoNodeGroup(new Node[]{facSelectionNode});
        NodeGroup batNodeGroup = initBatNodeGroup(new Node[]{facSelectionNode, facInfoNodeGroup});
        SimpleNode otherAppInfoNode = new SimpleNode(new OtherApplicationInfoDto(), NODE_NAME_OTHER_INFO, new Node[]{facSelectionNode, facInfoNodeGroup, batNodeGroup});
        SimpleNode primaryDocNode = new SimpleNode(new PrimaryDocDto(), NODE_NAME_PRIMARY_DOC, new Node[]{facSelectionNode, facInfoNodeGroup, batNodeGroup, otherAppInfoNode});
        SimpleNode previewSubmitNode = new SimpleNode(new PreviewSubmitDto(), NODE_NAME_PREVIEW_SUBMIT, new Node[]{facSelectionNode, facInfoNodeGroup, batNodeGroup, otherAppInfoNode, primaryDocNode});

        return new NodeGroup.Builder().name(name)
                .addNode(companyInfoNode)
                .addNode(facSelectionNode)
                .addNode(facInfoNodeGroup)
                .addNode(batNodeGroup)
                .addNode(otherAppInfoNode)
                .addNode(primaryDocNode)
                .addNode(previewSubmitNode)
                .build();
    }

    public static NodeGroup newFacInfoNodeGroup(Node[] dependNodes) {
        SimpleNode facProfileNode = new SimpleNode(new FacilityProfileDto(), NODE_NAME_FAC_PROFILE, new Node[0]);
        SimpleNode facOperatorNode = new SimpleNode(new FacilityOperatorDto(), NODE_NAME_FAC_OPERATOR, new Node[]{facProfileNode});
        SimpleNode facAuthNode = new SimpleNode(new FacilityAuthoriserDto(), NODE_NAME_FAC_AUTH, new Node[]{facProfileNode, facOperatorNode});
        SimpleNode facAdminNode = new SimpleNode(new FacilityAdministratorDto(), NODE_NAME_FAC_ADMIN, new Node[]{facProfileNode, facOperatorNode, facAuthNode});
        SimpleNode facOfficerNode = new SimpleNode(new FacilityOfficerDto(), NODE_NAME_FAC_OFFICER, new Node[]{facProfileNode, facOperatorNode, facAuthNode, facAdminNode});
        SimpleNode facCommitteeNode = new SimpleNode(new FacilityCommitteeDto(), NODE_NAME_FAC_COMMITTEE, new Node[]{facProfileNode, facOperatorNode, facAuthNode, facAdminNode, facOfficerNode});

        return new NodeGroup.Builder().name(NODE_NAME_FAC_INFO)
                .dependNodes(dependNodes)
                .addNode(facProfileNode)
                .addNode(facOperatorNode)
                .addNode(facAuthNode)
                .addNode(facAdminNode)
                .addNode(facOfficerNode)
                .addNode(facCommitteeNode)
                .build();
    }

    public static NodeGroup initBatNodeGroup(Node[] dependNodes) {
        return new NodeGroup.Builder().name(NODE_NAME_FAC_BAT_INFO)
                .dependNodes(dependNodes)
                .addNode(new Node("error", new Node[0]))
                .build();
    }

    public static void changeBatNodeGroup(NodeGroup batNodeGroup, FacilitySelectionDto selectionDto) {
        Assert.notNull(batNodeGroup, ERR_MSG_BAT_NOT_NULL);
        Node[] subNodes = new Node[selectionDto.getActivityTypes().size()];
        List<String> activityTypes = selectionDto.getActivityTypes();
        for (int i = 0; i < activityTypes.size(); i++) {
            subNodes[i] = new SimpleNode(new BiologicalAgentToxinDto(activityTypes.get(i)), activityTypes.get(i), new Node[0]);
        }
        batNodeGroup.reorganizeNodes(subNodes);
    }

    public static List<BiologicalAgentToxinDto> getBatInfoList(NodeGroup batNodeGroup) {
        Assert.notNull(batNodeGroup, ERR_MSG_BAT_NOT_NULL);
        List<BiologicalAgentToxinDto> batList = new ArrayList<>(batNodeGroup.count());
        for (Node node : batNodeGroup.getAllNodes()) {
            assert node instanceof SimpleNode;
            batList.add((BiologicalAgentToxinDto) ((SimpleNode) node).getValue());
        }
        return batList;
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
    public static List<SelectOption> tmpNationalityOps() {
        List<MasterCodeView> views = MasterCodeUtil.retrieveByCategory(MasterCodeUtil.CATE_ID_NATIONALITY);
        List<SelectOption> ops = new ArrayList<>(views.size());
        if(!CollectionUtils.isEmpty(views)){
            for (MasterCodeView view : views) {
                ops.add(new SelectOption(view.getCode(), view.getCodeValue()));
            }
        }
        return ops;
    }

    /* Will be removed in future, will get this from master code */
    public static List<SelectOption> tmpPersonnelRoleOps() {
        List<SelectOption> personnelRoleOps = new ArrayList<>(4);
        personnelRoleOps.add(new SelectOption("COMTPRO001", "Senior Management Representative"));
        personnelRoleOps.add(new SelectOption("COMTPRO002", "Biosafety Coordinator"));
        personnelRoleOps.add(new SelectOption("COMTPRO003", "Person in charge of safe and proper functioning of facility and equipment"));
        personnelRoleOps.add(new SelectOption("COMTPRO004", "Other qualified personnel"));
        return personnelRoleOps;
    }

    /* Will be removed in future, will get this from config mechanism */
    public List<DocSetting> getFacRegDocSettings () {
        List<DocSetting> docSettings = new ArrayList<>(9);
        docSettings.add(new DocSetting(DocConstants.DOC_TYPE_BIO_SAFETY_COORDINATOR_CERTIFICATES, "BioSafety Coordinator Certificates", true));
        docSettings.add(new DocSetting(DocConstants.DOC_TYPE_INVENTORY_FILE, "Inventory File", false));
        docSettings.add(new DocSetting(DocConstants.DOC_TYPE_GMAC_ENDORSEMENT, "GMAC Endorsement", false));
        docSettings.add(new DocSetting(DocConstants.DOC_TYPE_RISK_ASSESS_PLAN, "Risk Assessment Plan", false));
        docSettings.add(new DocSetting(DocConstants.DOC_TYPE_STANDARD_OPERATING_PROCEDURE, "Standard Operating Procedure", false));
        docSettings.add(new DocSetting(DocConstants.DOC_TYPE_EMERGENCY_RESPONSE_PLAN, "Emergency Response Plan", false));
        docSettings.add(new DocSetting(DocConstants.DOC_TYPE_BIO_SAFETY_COM, "Approval/Endorsement : Biosafety Com", false));
        docSettings.add(new DocSetting(DocConstants.DOC_TYPE_FACILITY_PLAN_LAYOUT, "Facility Plan/Layout", false));
        docSettings.add(new DocSetting(DocConstants.DOC_TYPE_OTHERS, "Others", false));
        return docSettings;
    }

}

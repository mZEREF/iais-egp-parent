package sg.gov.moh.iais.egp.bsb.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.exception.IaisRuntimeException;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import sg.gov.moh.iais.egp.bsb.common.node.Node;
import sg.gov.moh.iais.egp.bsb.common.node.NodeGroup;
import sg.gov.moh.iais.egp.bsb.common.node.Nodes;
import sg.gov.moh.iais.egp.bsb.common.node.simple.SimpleNode;
import sg.gov.moh.iais.egp.bsb.dto.report.PrimaryDocDto;
import sg.gov.moh.iais.egp.bsb.dto.report.investigation.IncidentInfoDto;
import sg.gov.moh.iais.egp.bsb.dto.report.investigation.IncidentInvestDto;
import sg.gov.moh.iais.egp.bsb.dto.report.investigation.MedicalInvestDto;
import sg.gov.moh.iais.egp.bsb.dto.report.notification.PersonReportingDto;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;

import static sg.gov.moh.iais.egp.bsb.constant.ReportableEventConstants.*;

/**
 * @author YiMing
 * @version 2021/12/15 9:22
 **/

@Slf4j
@Delegator("investReportDelegator")
public class InvestReportDelegator {

    public void start(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        request.getSession().removeAttribute(KEY_ROOT_NODE_GROUP_INVEST_REPORT);
        AuditTrailHelper.auditFunction(MODULE_NAME_INVESTIGATION_REPORT, MODULE_NAME_INVESTIGATION_REPORT);
    }

    public void init(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        ParamUtil.setSessionAttr(request,KEY_ROOT_NODE_GROUP_INVEST_REPORT,getInvestReportRoot(request));
    }


    public void jumpFilter(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        String destNode = (String) ParamUtil.getSessionAttr(request, KEY_JUMP_DEST_NODE);
        ParamUtil.setRequestAttr(request, KEY_NODE_ROUTE, destNode);
    }


    public void actionFilter(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        String actionType = (String) ParamUtil.getRequestAttr(request, KEY_ACTION_TYPE);
        if (!StringUtils.hasLength(actionType)) {
            actionType = ParamUtil.getString(request, KEY_ACTION_TYPE);
        }
        ParamUtil.setRequestAttr(request, KEY_INDEED_ACTION_TYPE, actionType);
    }

    public void preIncidentInfo(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        NodeGroup investRepoRoot = getInvestReportRoot(request);
        SimpleNode incidentNode = (SimpleNode) investRepoRoot.getNode(NODE_NAME_INCIDENT_INFO);
        IncidentInfoDto incidentInfoDto = (IncidentInfoDto) incidentNode.getValue();
        Boolean needShowError = (Boolean) ParamUtil.getRequestAttr(request, KEY_SHOW_ERROR_SWITCH);
        if (needShowError == Boolean.TRUE) {
            ParamUtil.setRequestAttr(request, KEY_VALIDATION_ERRORS, incidentInfoDto.retrieveValidationResult());
        }
        Nodes.needValidation(investRepoRoot, NODE_NAME_INCIDENT_INFO);
        ParamUtil.setRequestAttr(request, NODE_NAME_INCIDENT_INFO, incidentInfoDto);
    }

    public void handleIncidentInfo(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        NodeGroup investRepoRoot = getInvestReportRoot(request);
        SimpleNode incidentNode = (SimpleNode) investRepoRoot.getNode(NODE_NAME_INCIDENT_INFO);
        IncidentInfoDto incidentInfoDto = (IncidentInfoDto) incidentNode.getValue();
        incidentInfoDto.reqObjMapping(request);
        String actionType = ParamUtil.getString(request, KEY_ACTION_TYPE);
        if (KEY_ACTION_JUMP.equals(actionType)) {
            jumpHandler(request, investRepoRoot);
        } else {
            throw new IaisRuntimeException(ERR_MSG_INVALID_ACTION);
        }
        ParamUtil.setSessionAttr(request, KEY_ROOT_NODE_GROUP_INCIDENT_NOT, investRepoRoot);
    }


    public void preIncidentInvestigation(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        NodeGroup investRepoRoot = getInvestReportRoot(request);
        SimpleNode incidentInvestNode = (SimpleNode) investRepoRoot.getNode(NODE_NAME_INCIDENT_INVESTIGATION);
        IncidentInvestDto incidentInvestDto = (IncidentInvestDto) incidentInvestNode.getValue();
        Boolean needShowError = (Boolean) ParamUtil.getRequestAttr(request, KEY_SHOW_ERROR_SWITCH);
        if (needShowError == Boolean.TRUE) {
            ParamUtil.setRequestAttr(request, KEY_VALIDATION_ERRORS, incidentInvestDto.retrieveValidationResult());
        }
        Nodes.needValidation(investRepoRoot, NODE_NAME_INCIDENT_INVESTIGATION);
        ParamUtil.setRequestAttr(request, NODE_NAME_INCIDENT_INVESTIGATION, incidentInvestDto);
    }

    public void handleIncidentInvestigation(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        NodeGroup investRepoRoot = getInvestReportRoot(request);
        SimpleNode incidentInvestNode = (SimpleNode) investRepoRoot.getNode(NODE_NAME_INCIDENT_INVESTIGATION);
        IncidentInvestDto incidentInvestDto = (IncidentInvestDto) incidentInvestNode.getValue();
//        personReportingDto.reqObjMapping(request);
        String actionType = ParamUtil.getString(request, KEY_ACTION_TYPE);
        if (KEY_ACTION_JUMP.equals(actionType)) {
            jumpHandler(request, investRepoRoot);
        } else {
            throw new IaisRuntimeException(ERR_MSG_INVALID_ACTION);
        }
        ParamUtil.setSessionAttr(request, KEY_ROOT_NODE_GROUP_INVEST_REPORT, investRepoRoot);
    }


    /**
     * common actions when we do 'jump'
     * decide the routing logic
     * will set a dest node in the request attribute;
     * will set a floag if we need to show the error messages.
     * @param facRegRoot root data structure of this flow
     */
    public void jumpHandler(HttpServletRequest request, NodeGroup facRegRoot) {
        String actionValue = ParamUtil.getString(request, KEY_ACTION_VALUE);
        Assert.hasText(actionValue, "Invalid action value");
        String destNode = computeDestNodePath(facRegRoot, actionValue);
        String checkedDestNode = Nodes.jump(facRegRoot, destNode);
        if (!checkedDestNode.equals(destNode)) {
            ParamUtil.setRequestAttr(request, KEY_SHOW_ERROR_SWITCH, Boolean.TRUE);
        }
        ParamUtil.setSessionAttr(request, KEY_JUMP_DEST_NODE, checkedDestNode);
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

    public NodeGroup getInvestReportRoot (HttpServletRequest request){
        NodeGroup root = (NodeGroup) ParamUtil.getSessionAttr(request, KEY_ROOT_NODE_GROUP_INVEST_REPORT);
        if (root == null) {
            root = newInvestReportRoot(KEY_ROOT_NODE_GROUP_INVEST_REPORT);
        }
        return root;
    }

    public static NodeGroup newInvestReportRoot(String name) {
        SimpleNode incidentNode = new SimpleNode(new IncidentInfoDto(), NODE_NAME_INCIDENT_INFO, new Node[0]);
        SimpleNode incidentInvestNode = new SimpleNode(new IncidentInvestDto(), NODE_NAME_INCIDENT_INVESTIGATION,new Node[0]);
        SimpleNode medicalInvestNode = new SimpleNode(new MedicalInvestDto(), NODE_NAME_MEDICAL_INVESTIGATION, new Node[0]);
        SimpleNode documentNode = new SimpleNode(new PrimaryDocDto(), NODE_NAME_DOCUMENTS, new Node[0]);
        Node previewSubmitNode = new Node(NODE_NAME_PREVIEW_SUBMIT, new Node[]{incidentNode,incidentInvestNode,medicalInvestNode,documentNode});

        return new NodeGroup.Builder().name(name)
                .addNode(incidentNode)
                .addNode(incidentInvestNode)
                .addNode(medicalInvestNode)
                .addNode(documentNode)
                .addNode(previewSubmitNode)
                .build();
    }
}

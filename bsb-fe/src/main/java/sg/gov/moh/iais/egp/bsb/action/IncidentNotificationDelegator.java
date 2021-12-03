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
import sg.gov.moh.iais.egp.bsb.constant.DocConstants;
import sg.gov.moh.iais.egp.bsb.dto.file.DocRecordInfo;
import sg.gov.moh.iais.egp.bsb.dto.file.NewDocInfo;
import sg.gov.moh.iais.egp.bsb.dto.report.notification.IncidentInfoDto;
import sg.gov.moh.iais.egp.bsb.dto.report.notification.PersonInvolvedInfoDto;
import sg.gov.moh.iais.egp.bsb.dto.report.notification.PersonReportingDto;
import sg.gov.moh.iais.egp.bsb.dto.report.notification.PrimaryDocDto;
import sg.gov.moh.iais.egp.bsb.entity.DocSetting;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static sg.gov.moh.iais.egp.bsb.constant.IncidentNotificationConstants.*;

/**
 * @author YiMing
 * @version 2021/12/2 9:12
 **/
@Slf4j
@Delegator("incidentNotificationDelegator")
public class IncidentNotificationDelegator {
    private static final String ERR_MSG_BAT_NOT_NULL = "Biological Agent/Toxin node group must not be null!";
    private static final String ERR_MSG_NULL_GROUP = "Node group must not be null!";
    private static final String ERR_MSG_NULL_NAME = "Name must not be null!";
    private static final String ERR_MSG_INVALID_ACTION = "Invalid action";
    private static final String KEY_SHOW_ERROR_SWITCH = "needShowValidationError";
    private static final String KEY_VALIDATION_ERRORS = "errorMsg";

    public void start(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        request.getSession().removeAttribute(KEY_ROOT_NODE_GROUP);

        AuditTrailHelper.auditFunction(MODULE_NAME, MODULE_NAME);
    }

    public void init(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        ParamUtil.setSessionAttr(request,KEY_ROOT_NODE_GROUP,getIncidentNotRoot(request));
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
        NodeGroup incidentNotRoot = getIncidentNotRoot(request);
        SimpleNode incidentNode = (SimpleNode) incidentNotRoot.getNode(NODE_NAME_INCIDENT_INFO);
        IncidentInfoDto incidentInfoDto = (IncidentInfoDto) incidentNode.getValue();
        Boolean needShowError = (Boolean) ParamUtil.getRequestAttr(request, KEY_SHOW_ERROR_SWITCH);
        if (needShowError == Boolean.TRUE) {
            ParamUtil.setRequestAttr(request, KEY_VALIDATION_ERRORS, incidentInfoDto.retrieveValidationResult());
        }
        Nodes.needValidation(incidentNotRoot, NODE_NAME_INCIDENT_INFO);
        ParamUtil.setRequestAttr(request, NODE_NAME_INCIDENT_INFO, incidentInfoDto);

    }

    public void handleIncidentInfo(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        NodeGroup incidentNotRoot = getIncidentNotRoot(request);
        SimpleNode incidentNode = (SimpleNode) incidentNotRoot.getNode(NODE_NAME_INCIDENT_INFO);
        IncidentInfoDto incidentInfoDto = (IncidentInfoDto) incidentNode.getValue();
        incidentInfoDto.reqObjMapping(request);
        String actionType = ParamUtil.getString(request, KEY_ACTION_TYPE);
        if (KEY_ACTION_JUMP.equals(actionType)) {
            jumpHandler(request, incidentNotRoot, NODE_NAME_INCIDENT_INFO, incidentNode);
        } else {
            throw new IaisRuntimeException(ERR_MSG_INVALID_ACTION);
        }
        ParamUtil.setSessionAttr(request, KEY_ROOT_NODE_GROUP, incidentNotRoot);
    }

    public void prePersonReportingInfo(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        NodeGroup incidentNotRoot = getIncidentNotRoot(request);
        SimpleNode reportingPersonNode = (SimpleNode) incidentNotRoot.getNode(NODE_NAME_PERSON_REPORTING_INFO);
        PersonReportingDto personReportingDto = (PersonReportingDto) reportingPersonNode.getValue();
        Boolean needShowError = (Boolean) ParamUtil.getRequestAttr(request, KEY_SHOW_ERROR_SWITCH);
        if (needShowError == Boolean.TRUE) {
            ParamUtil.setRequestAttr(request, KEY_VALIDATION_ERRORS, personReportingDto.retrieveValidationResult());
        }
        Nodes.needValidation(incidentNotRoot, NODE_NAME_PERSON_REPORTING_INFO);
        ParamUtil.setRequestAttr(request, NODE_NAME_PERSON_REPORTING_INFO, personReportingDto);

    }

    public void handlePersonReportingInfo(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        NodeGroup incidentNotRoot = getIncidentNotRoot(request);
        SimpleNode reportingPersonNode = (SimpleNode) incidentNotRoot.getNode(NODE_NAME_PERSON_REPORTING_INFO);
        PersonReportingDto personReportingDto = (PersonReportingDto) reportingPersonNode.getValue();
        personReportingDto.reqObjMapping(request);
        String actionType = ParamUtil.getString(request, KEY_ACTION_TYPE);
        if (KEY_ACTION_JUMP.equals(actionType)) {
            jumpHandler(request, incidentNotRoot, NODE_NAME_PERSON_REPORTING_INFO, reportingPersonNode);
        } else {
            throw new IaisRuntimeException(ERR_MSG_INVALID_ACTION);
        }
        ParamUtil.setSessionAttr(request, KEY_ROOT_NODE_GROUP, incidentNotRoot);
    }

    public void preInvolvedPersonInfo(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        NodeGroup incidentNotRoot = getIncidentNotRoot(request);
        SimpleNode involvedPersonNode = (SimpleNode) incidentNotRoot.getNode(NODE_NAME_PERSON_INVOLVED_INFO);
        PersonInvolvedInfoDto personInvolvedInfoDto = (PersonInvolvedInfoDto) involvedPersonNode.getValue();
        Boolean needShowError = (Boolean) ParamUtil.getRequestAttr(request, KEY_SHOW_ERROR_SWITCH);
        if (needShowError == Boolean.TRUE) {
            ParamUtil.setRequestAttr(request, KEY_VALIDATION_ERRORS, personInvolvedInfoDto.retrieveValidationResult());
        }
        Nodes.needValidation(incidentNotRoot, NODE_NAME_PERSON_INVOLVED_INFO);
        ParamUtil.setRequestAttr(request, NODE_NAME_PERSON_INVOLVED_INFO, personInvolvedInfoDto);

    }

    public void handleInvolvedPersonInfo(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        NodeGroup incidentNotRoot = getIncidentNotRoot(request);
        SimpleNode involvedPersonNode = (SimpleNode) incidentNotRoot.getNode(NODE_NAME_PERSON_INVOLVED_INFO);
        PersonInvolvedInfoDto personInvolvedInfoDto = (PersonInvolvedInfoDto) involvedPersonNode.getValue();
        personInvolvedInfoDto.reqObjMapping(request);
        String actionType = ParamUtil.getString(request, KEY_ACTION_TYPE);
        if (KEY_ACTION_JUMP.equals(actionType)) {
            jumpHandler(request, incidentNotRoot, NODE_NAME_PERSON_INVOLVED_INFO, involvedPersonNode);
        } else {
            throw new IaisRuntimeException(ERR_MSG_INVALID_ACTION);
        }
        ParamUtil.setSessionAttr(request, KEY_ROOT_NODE_GROUP, incidentNotRoot);
    }

    public void preDocuments(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        NodeGroup incidentNotRoot = getIncidentNotRoot(request);
        SimpleNode documentNode = (SimpleNode) incidentNotRoot.at(NODE_NAME_DOCUMENTS);
        PrimaryDocDto primaryDocDto = (PrimaryDocDto) documentNode.getValue();
        Boolean needShowError = (Boolean) ParamUtil.getRequestAttr(request, KEY_SHOW_ERROR_SWITCH);
        if (needShowError == Boolean.TRUE) {
            ParamUtil.setRequestAttr(request, KEY_VALIDATION_ERRORS, primaryDocDto.retrieveValidationResult());
        }
        Nodes.needValidation(incidentNotRoot, NODE_NAME_DOCUMENTS);

        ParamUtil.setRequestAttr(request, "docSettings", getIncidentNotDocSettings());

        Map<String, List<DocRecordInfo>> savedFiles = primaryDocDto.getExistDocTypeMap();
        Map<String, List<NewDocInfo>> newFiles = primaryDocDto.getNewDocTypeMap();
        ParamUtil.setRequestAttr(request, "savedFiles", savedFiles);
        ParamUtil.setRequestAttr(request, "newFiles", newFiles);
    }

    public void handleDocuments(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        NodeGroup incidentNotRoot = getIncidentNotRoot(request);
        SimpleNode documentNode = (SimpleNode) incidentNotRoot.at(NODE_NAME_DOCUMENTS);
        PrimaryDocDto primaryDocDto = (PrimaryDocDto) documentNode.getValue();
        primaryDocDto.reqObjMapping(request);

        String actionType = ParamUtil.getString(request, KEY_ACTION_TYPE);
        if (KEY_ACTION_JUMP.equals(actionType)) {
            jumpHandler(request, incidentNotRoot, NODE_NAME_DOCUMENTS, documentNode);
        } else {
            throw new IaisRuntimeException(ERR_MSG_INVALID_ACTION);
        }
        ParamUtil.setSessionAttr(request, KEY_ROOT_NODE_GROUP, incidentNotRoot);
    }

    public void preSubmit(BaseProcessClass bpc){
    }

    public void handleSubmit(BaseProcessClass bpc){
    }

    public void preAcknowledge(BaseProcessClass bpc){
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

    public NodeGroup getIncidentNotRoot (HttpServletRequest request){
        NodeGroup root = (NodeGroup) ParamUtil.getSessionAttr(request, KEY_ROOT_NODE_GROUP);
        if (root == null) {
            root = newIncidentNotRoot(KEY_ROOT_NODE_GROUP);
        }
        return root;
    }

    public static NodeGroup newIncidentNotRoot(String name) {
        SimpleNode incidentNode = new SimpleNode(new IncidentInfoDto(), NODE_NAME_INCIDENT_INFO, new Node[0]);
        SimpleNode reportingPersonNode = new SimpleNode(new PersonReportingDto(), NODE_NAME_PERSON_REPORTING_INFO, new Node[0]);
        SimpleNode involvedPersonNode = new SimpleNode(new PersonInvolvedInfoDto(), NODE_NAME_PERSON_INVOLVED_INFO, new Node[0]);
        SimpleNode documentNode = new SimpleNode(new PrimaryDocDto(), NODE_NAME_DOCUMENTS, new Node[0]);
        Node companyInfoNode = new Node(NODE_NAME_PREVIEW_SUBMIT, new Node[]{incidentNode,reportingPersonNode,involvedPersonNode,documentNode});

        return new NodeGroup.Builder().name(name)
                .addNode(incidentNode)
                .addNode(reportingPersonNode)
                .addNode(involvedPersonNode)
                .addNode(documentNode)
                .addNode(companyInfoNode)
                .build();
    }

    /* Will be removed in future, will get this from config mechanism */
    private List<DocSetting> getIncidentNotDocSettings () {
        List<DocSetting> docSettings = new ArrayList<>(3);
        docSettings.add(new DocSetting(DocConstants.DOC_INCIDENT_REPORT, "Incident Report", false));
        docSettings.add(new DocSetting(DocConstants.DOC_INCIDENT_ACTION_REPORT, "Incident Action Report", false));
        docSettings.add(new DocSetting(DocConstants.DOC_TYPE_OTHERS, "Others", false));
        return docSettings;
    }
}

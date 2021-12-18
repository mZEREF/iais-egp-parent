package sg.gov.moh.iais.egp.bsb.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.exception.IaisRuntimeException;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import sg.gov.moh.iais.egp.bsb.client.BsbFileClient;
import sg.gov.moh.iais.egp.bsb.client.FileRepoClient;
import sg.gov.moh.iais.egp.bsb.common.node.Node;
import sg.gov.moh.iais.egp.bsb.common.node.NodeGroup;
import sg.gov.moh.iais.egp.bsb.common.node.Nodes;
import sg.gov.moh.iais.egp.bsb.common.node.simple.SimpleNode;
import sg.gov.moh.iais.egp.bsb.constant.DocConstants;
import sg.gov.moh.iais.egp.bsb.dto.file.DocRecordInfo;
import sg.gov.moh.iais.egp.bsb.dto.file.NewDocInfo;
import sg.gov.moh.iais.egp.bsb.dto.report.PrimaryDocDto;
import sg.gov.moh.iais.egp.bsb.dto.report.investigation.IncidentInfoDto;
import sg.gov.moh.iais.egp.bsb.dto.report.investigation.IncidentInvestDto;
import sg.gov.moh.iais.egp.bsb.dto.report.investigation.MedicalInvestDto;
import sg.gov.moh.iais.egp.bsb.entity.DocSetting;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static sg.gov.moh.iais.egp.bsb.constant.ReportableEventConstants.*;

/**
 * @author YiMing
 * @version 2021/12/15 9:22
 **/

@Slf4j
@Delegator("investReportDelegator")
public class InvestReportDelegator {
    private final FileRepoClient fileRepoClient;
    private final BsbFileClient bsbFileClient;

    public InvestReportDelegator(FileRepoClient fileRepoClient, BsbFileClient bsbFileClient) {
        this.fileRepoClient = fileRepoClient;
        this.bsbFileClient = bsbFileClient;
    }

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
        incidentInvestDto.reqObjMapping(request);
        String actionType = ParamUtil.getString(request, KEY_ACTION_TYPE);
        if (KEY_ACTION_JUMP.equals(actionType)) {
            jumpHandler(request, investRepoRoot);
        } else {
            throw new IaisRuntimeException(ERR_MSG_INVALID_ACTION);
        }
        ParamUtil.setSessionAttr(request, KEY_ROOT_NODE_GROUP_INVEST_REPORT, investRepoRoot);
    }

    public void preMedicalInvestigation(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        NodeGroup investRepoRoot = getInvestReportRoot(request);
        SimpleNode medicalInvestNode = (SimpleNode) investRepoRoot.getNode(NODE_NAME_MEDICAL_INVESTIGATION);
        MedicalInvestDto medicalInvestDto = (MedicalInvestDto) medicalInvestNode.getValue();
        Boolean needShowError = (Boolean) ParamUtil.getRequestAttr(request, KEY_SHOW_ERROR_SWITCH);
        if (needShowError == Boolean.TRUE) {
            ParamUtil.setRequestAttr(request, KEY_VALIDATION_ERRORS, medicalInvestDto.retrieveValidationResult());
        }
        Nodes.needValidation(investRepoRoot, NODE_NAME_MEDICAL_INVESTIGATION);
        ParamUtil.setRequestAttr(request, NODE_NAME_MEDICAL_INVESTIGATION, medicalInvestDto);
    }

    public void handleMedicalInvestigation(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        NodeGroup investRepoRoot = getInvestReportRoot(request);
        SimpleNode medicalInvestNode = (SimpleNode) investRepoRoot.getNode(NODE_NAME_MEDICAL_INVESTIGATION);
        MedicalInvestDto medicalInvestDto = (MedicalInvestDto) medicalInvestNode.getValue();
        medicalInvestDto.reqObjMapping(request);
        String actionType = ParamUtil.getString(request, KEY_ACTION_TYPE);
        if (KEY_ACTION_JUMP.equals(actionType)) {
            jumpHandler(request, investRepoRoot);
        } else {
            throw new IaisRuntimeException(ERR_MSG_INVALID_ACTION);
        }
        ParamUtil.setSessionAttr(request, KEY_ROOT_NODE_GROUP_INVEST_REPORT, investRepoRoot);
    }

    public void preDocuments(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        NodeGroup investRepoRoot = getInvestReportRoot(request);
        SimpleNode documentNode = (SimpleNode) investRepoRoot.at(NODE_NAME_DOCUMENTS);
        PrimaryDocDto primaryDocDto = (PrimaryDocDto) documentNode.getValue();
        Boolean needShowError = (Boolean) ParamUtil.getRequestAttr(request, KEY_SHOW_ERROR_SWITCH);
        if (needShowError == Boolean.TRUE) {
            ParamUtil.setRequestAttr(request, KEY_VALIDATION_ERRORS, primaryDocDto.retrieveValidationResult());
        }
        Nodes.needValidation(investRepoRoot, NODE_NAME_DOCUMENTS);
        ParamUtil.setRequestAttr(request, "docSettings", getInvestReportDocSettings());


        Map<String, List<DocRecordInfo>> savedFiles = primaryDocDto.getExistDocTypeMap();
        Map<String, List<NewDocInfo>> newFiles = primaryDocDto.getNewDocTypeMap();
        ParamUtil.setRequestAttr(request, "savedFiles", savedFiles);
        ParamUtil.setRequestAttr(request, "newFiles", newFiles);
    }

    public void handleDocuments(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        NodeGroup investRepoRoot = getInvestReportRoot(request);
        SimpleNode documentNode = (SimpleNode) investRepoRoot.at(NODE_NAME_DOCUMENTS);
        PrimaryDocDto primaryDocDto = (PrimaryDocDto) documentNode.getValue();
        primaryDocDto.reqObjMapping(request);

        String actionType = ParamUtil.getString(request, KEY_ACTION_TYPE);
        if (KEY_ACTION_JUMP.equals(actionType)) {
            jumpHandler(request, investRepoRoot);
        } else {
            throw new IaisRuntimeException(ERR_MSG_INVALID_ACTION);
        }
        ParamUtil.setSessionAttr(request, KEY_ROOT_NODE_GROUP_INVEST_REPORT, investRepoRoot);
    }

    public void prePreviewSubmit(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        NodeGroup investRepoRoot = getInvestReportRoot(request);

        ParamUtil.setRequestAttr(request, NODE_NAME_INCIDENT_INFO, ((SimpleNode)investRepoRoot.at(NODE_NAME_INCIDENT_INFO)).getValue());
        ParamUtil.setRequestAttr(request, NODE_NAME_INCIDENT_INVESTIGATION, ((SimpleNode)investRepoRoot.at(NODE_NAME_INCIDENT_INVESTIGATION)).getValue());
        ParamUtil.setRequestAttr(request, NODE_NAME_MEDICAL_INVESTIGATION, ((SimpleNode)investRepoRoot.at(NODE_NAME_MEDICAL_INVESTIGATION)).getValue());


        ParamUtil.setRequestAttr(request, "docSettings", getInvestReportDocSettings());
        PrimaryDocDto primaryDocDto = (PrimaryDocDto) ((SimpleNode)investRepoRoot.at(NODE_NAME_DOCUMENTS)).getValue();
        Map<String, List<DocRecordInfo>> savedFiles = primaryDocDto.getExistDocTypeMap();
        Map<String, List<NewDocInfo>> newFiles = primaryDocDto.getNewDocTypeMap();
        ParamUtil.setRequestAttr(request, "savedFiles", savedFiles);
        ParamUtil.setRequestAttr(request, "newFiles", newFiles);
    }

    public void handlePreviewSubmit(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
//        NodeGroup investRepoRoot = getInvestReportRoot(request);
//        String actionType = ParamUtil.getString(request, KEY_ACTION_TYPE);
//        String actionValue = ParamUtil.getString(request, KEY_ACTION_VALUE);
//        if (KEY_ACTION_JUMP.equals(actionType)) {
//            if (KEY_NAV_NEXT.equals(actionValue)) {
//                // save docs
//                SimpleNode primaryDocNode = (SimpleNode) investRepoRoot.at(NODE_NAME_DOCUMENTS);
//                PrimaryDocDto primaryDocDto = (PrimaryDocDto) primaryDocNode.getValue();
//                List<NewFileSyncDto> newFilesToSync = null;
//                if (!primaryDocDto.getNewDocMap().isEmpty()) {
//                    MultipartFile[] files = primaryDocDto.getNewDocMap().values().stream().map(NewDocInfo::getMultipartFile).toArray(MultipartFile[]::new);
//                    List<String> repoIds = fileRepoClient.saveFiles(files).getEntity();
//                    newFilesToSync = primaryDocDto.newFileSaved(repoIds);
//                }
//
//                // save data
//                IncidentNotificationDto incidentNotificationDto = IncidentNotificationDto.from(incidentNotRoot);
//                AuditTrailDto auditTrailDto = (AuditTrailDto) ParamUtil.getSessionAttr(request, AuditTrailConsts.SESSION_ATTR_PARAM_NAME);
//                incidentNotificationDto.setAuditTrailDto(auditTrailDto);
//                ResponseDto<String> responseDto = incidentClient.saveNewIncidentNotification(incidentNotificationDto);
//                if(log.isInfoEnabled()){
//                    log.info("save new facility response: {}", LogUtil.escapeCrlf(responseDto.toString()));
//                }
//                try {
//                    // sync files to BE file-repo (save new added files, delete useless files)
//                    if ((newFilesToSync != null && !newFilesToSync.isEmpty()) || !primaryDocDto.getToBeDeletedRepoIds().isEmpty()) {
//                        /* Ignore the failure of sync files currently.
//                         * We should add a mechanism to retry synchronization of files in the future */
//                        FileRepoSyncDto syncDto = new FileRepoSyncDto();
//                        syncDto.setNewFiles(newFilesToSync);
//                        syncDto.setToDeleteIds(new ArrayList<>(primaryDocDto.getToBeDeletedRepoIds()));
//                        bsbFileClient.saveFiles(syncDto);
//                    }
//
//                    // delete docs in FE file-repo
//                    /* Ignore the failure when try to delete FE files because this is not a big issue.
//                     * The not deleted file won't be retrieved, so it's just a waste of disk space */
//                    for (String id: primaryDocDto.getToBeDeletedRepoIds()) {
//                        FileRepoDto fileRepoDto = new FileRepoDto();
//                        fileRepoDto.setId(id);
//                        fileRepoClient.removeFileById(fileRepoDto);
//                    }
//                } catch (Exception e) {
//                    log.error("Fail to sync files to BE", e);
//                }
//
//                ParamUtil.setRequestAttr(request, KEY_ACTION_TYPE, KEY_ACTION_SUBMIT);
//            } else {
//                jumpHandler(request, incidentNotRoot);
//            }
//        } else {
//            throw new IaisRuntimeException(ERR_MSG_INVALID_ACTION);
//        }
//        ParamUtil.setSessionAttr(request, KEY_ROOT_NODE_GROUP_INCIDENT_NOT, incidentNotRoot);
    }

    public void preAcknowledge(BaseProcessClass bpc){

    }

    /* Will be removed in future, will get this from config mechanism */
    private List<DocSetting> getInvestReportDocSettings () {
        List<DocSetting> docSettings = new ArrayList<>(3);
        docSettings.add(new DocSetting(DocConstants.DOC_INCIDENT_REPORT, "Incident Report", false));
        docSettings.add(new DocSetting(DocConstants.DOC_INCIDENT_ACTION_REPORT, "Incident Action Report", false));
        docSettings.add(new DocSetting(DocConstants.DOC_TYPE_OTHERS, "Others", false));
        return docSettings;
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

package sg.gov.moh.iais.egp.bsb.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.AuditTrailConsts;
import com.ecquaria.cloud.moh.iais.common.dto.AuditTrailDto;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.filerepo.FileRepoDto;
import com.ecquaria.cloud.moh.iais.common.exception.IaisRuntimeException;
import com.ecquaria.cloud.moh.iais.common.utils.LogUtil;
import com.ecquaria.cloud.moh.iais.common.utils.MaskUtil;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import sg.gov.moh.iais.egp.bsb.client.BsbFileClient;
import sg.gov.moh.iais.egp.bsb.client.FileRepoClient;
import sg.gov.moh.iais.egp.bsb.client.InvestReportClient;
import sg.gov.moh.iais.egp.bsb.common.node.Node;
import sg.gov.moh.iais.egp.bsb.common.node.NodeGroup;
import sg.gov.moh.iais.egp.bsb.common.node.Nodes;
import sg.gov.moh.iais.egp.bsb.common.node.simple.SimpleNode;
import sg.gov.moh.iais.egp.bsb.constant.DocConstants;
import sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants;
import sg.gov.moh.iais.egp.bsb.dto.ResponseDto;
import sg.gov.moh.iais.egp.bsb.dto.file.DocRecordInfo;
import sg.gov.moh.iais.egp.bsb.dto.file.FileRepoSyncDto;
import sg.gov.moh.iais.egp.bsb.dto.file.NewDocInfo;
import sg.gov.moh.iais.egp.bsb.dto.file.NewFileSyncDto;
import sg.gov.moh.iais.egp.bsb.dto.report.PrimaryDocDto;
import sg.gov.moh.iais.egp.bsb.dto.report.investigation.*;
import sg.gov.moh.iais.egp.bsb.dto.report.notification.IncidentNotificationDto;
import sg.gov.moh.iais.egp.bsb.entity.DocSetting;
import sg.gov.moh.iais.egp.bsb.entity.Draft;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static sg.gov.moh.iais.egp.bsb.constant.FacRegisterConstants.KEY_ACTION_JUMP;
import static sg.gov.moh.iais.egp.bsb.constant.FacRegisterConstants.KEY_ACTION_SAVE_AS_DRAFT;
import static sg.gov.moh.iais.egp.bsb.constant.FacRegisterConstants.KEY_ACTION_TYPE;
import static sg.gov.moh.iais.egp.bsb.constant.FacRegisterConstants.KEY_INDEED_ACTION_TYPE;
import static sg.gov.moh.iais.egp.bsb.constant.ReportableEventConstants.*;
import static sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants.KEY_IND_AFTER_SAVE_AS_DRAFT;

/**
 * @author YiMing
 * @version 2021/12/15 9:22
 **/

@Slf4j
@Delegator("investReportDelegator")
public class InvestReportDelegator {
    private final FileRepoClient fileRepoClient;
    private final BsbFileClient bsbFileClient;
    private final InvestReportClient investReportClient;
    private static final String PARAM_REFERENCE_NO = "refNo";
    private static final String PARAM_INCIDENT_DTO = "incidentDto";
    private static final String KEY_EDIT_REF_ID = "editId";
    private static final String KEY_DRAFT = "draft";

    public InvestReportDelegator(FileRepoClient fileRepoClient, BsbFileClient bsbFileClient, InvestReportClient investReportClient) {
        this.fileRepoClient = fileRepoClient;
        this.bsbFileClient = bsbFileClient;
        this.investReportClient = investReportClient;
    }

    public void start(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        request.getSession().removeAttribute(KEY_ROOT_NODE_GROUP_INVEST_REPORT);
        request.getSession().removeAttribute(PARAM_INCIDENT_DTO);
        AuditTrailHelper.auditFunction(MODULE_NAME_INVESTIGATION_REPORT, MODULE_NAME_INVESTIGATION_REPORT);
    }

    public void init(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        boolean newIncidentNot = true;
        //enter from draft continue button
        //processType is make sure enter from your own draft
        String processType = (String) ParamUtil.getSessionAttr(request,ModuleCommonConstants.KEY_ACTION_TYPE);
        Draft draft = (Draft) ParamUtil.getSessionAttr(request,KEY_DRAFT);
        if(draft != null && "investigation".equals(processType)){
            newIncidentNot = false;
            ObjectMapper mapper = new ObjectMapper();
            try {
                InvestReportDto investReportDto = mapper.readValue(draft.getDraftData(),InvestReportDto.class);
                NodeGroup investRepoRoot = investReportDto.toInvestReportDto(KEY_ROOT_NODE_GROUP_INVEST_REPORT);
                ParamUtil.setSessionAttr(request, KEY_ROOT_NODE_GROUP_INVEST_REPORT, investRepoRoot);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }
        if (newIncidentNot) {
            ParamUtil.setSessionAttr(request, KEY_ROOT_NODE_GROUP_INVEST_REPORT, getInvestReportRoot(request));
        }
    }


    public void jumpFilter(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        String destNode = (String) ParamUtil.getSessionAttr(request, KEY_JUMP_DEST_NODE);
        ParamUtil.setRequestAttr(request, KEY_NODE_ROUTE, destNode);
    }


    public void actionFilter(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        // check if there is action set to override the action from request
        String actionType = (String) ParamUtil.getRequestAttr(request, KEY_ACTION_TYPE);
        if (!StringUtils.hasLength(actionType)) {
            // not set, use action from user's client
            actionType = ParamUtil.getString(request, KEY_ACTION_TYPE);
        } else {
            // set, if the action is 'save draft', we save it and route back to that page
            if (KEY_ACTION_SAVE_AS_DRAFT.equals(actionType)) {
                actionType = KEY_ACTION_JUMP;
                saveDraft(request);
            }
        }
        ParamUtil.setRequestAttr(request, KEY_INDEED_ACTION_TYPE, actionType);
    }

    public void preReferNoSelection(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        ParamUtil.setRequestAttr(request,"referNoOps",tempReferenceNoOps());
        NodeGroup investRepoRoot = getInvestReportRoot(request);
        SimpleNode incidentNode = (SimpleNode) investRepoRoot.getNode(NODE_NAME_REFERENCE_NO_SELECTION);
        ReferNoSelectionDto referNoSelectionDto = (ReferNoSelectionDto) incidentNode.getValue();
        Boolean needShowError = (Boolean) ParamUtil.getRequestAttr(request, KEY_SHOW_ERROR_SWITCH);
        if (needShowError == Boolean.TRUE) {
            ParamUtil.setRequestAttr(request, KEY_VALIDATION_ERRORS, referNoSelectionDto.retrieveValidationResult());
        }
        Nodes.needValidation(investRepoRoot,NODE_NAME_REFERENCE_NO_SELECTION);
        ParamUtil.setRequestAttr(request,NODE_NAME_REFERENCE_NO_SELECTION,referNoSelectionDto);
    }

    public void handleReferNoSelection(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        NodeGroup investRepoRoot = getInvestReportRoot(request);
        String actionType = ParamUtil.getString(request, KEY_ACTION_TYPE);
        String maskReferenceNo = ParamUtil.getString(request,PARAM_REFERENCE_NO);
        String referenceNo = MaskUtil.unMaskValue(PARAM_REFERENCE_NO,maskReferenceNo);
        SimpleNode referNoSelectNode = (SimpleNode) investRepoRoot.getNode(NODE_NAME_REFERENCE_NO_SELECTION);
        ReferNoSelectionDto referNoSelectionDto = (ReferNoSelectionDto) referNoSelectNode.getValue();
        referNoSelectionDto.setRefNo(referenceNo);
        if (KEY_ACTION_JUMP.equals(actionType)) {
            jumpHandler(request, investRepoRoot);
        } else if (ModuleCommonConstants.KEY_NAV_DRAFT.equals(actionType)) {
            ParamUtil.setRequestAttr(request, KEY_ACTION_TYPE, ModuleCommonConstants.KEY_NAV_DRAFT);
            ParamUtil.setSessionAttr(request, KEY_JUMP_DEST_NODE, NODE_NAME_REFERENCE_NO_SELECTION);
        } else {
            throw new IaisRuntimeException(ERR_MSG_INVALID_ACTION);
        }
        if(StringUtils.hasLength(maskReferenceNo)){
            IncidentDto incidentDto = investReportClient.queryIncidentByRefNo(referenceNo);
            ParamUtil.setSessionAttr(request,PARAM_INCIDENT_DTO,incidentDto);
        }
        ParamUtil.setSessionAttr(request, KEY_ROOT_NODE_GROUP_INVEST_REPORT, investRepoRoot);
    }

    public void preIncidentInfo(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        NodeGroup investRepoRoot = getInvestReportRoot(request);
        SimpleNode incidentNode = (SimpleNode) investRepoRoot.getNode(NODE_NAME_INCIDENT_INFO);
        IncidentInfoDto incidentInfoDto = (IncidentInfoDto) incidentNode.getValue();
        IncidentDto incidentDto = (IncidentDto) ParamUtil.getSessionAttr(request,PARAM_INCIDENT_DTO);
        //get incident id and set dto
        incidentInfoDto.setIncidentId(incidentDto.getId());
        String test = MaskUtil.maskValue(KEY_EDIT_REF_ID,incidentDto.getId());
        log.info("test value is {}", LogUtil.escapeCrlf(test));
        ParamUtil.setRequestAttr(request,"maskedEditId",MaskUtil.maskValue(KEY_EDIT_REF_ID,incidentDto.getId()));
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
        } else if (ModuleCommonConstants.KEY_NAV_DRAFT.equals(actionType)) {
            ParamUtil.setRequestAttr(request, KEY_ACTION_TYPE, ModuleCommonConstants.KEY_NAV_DRAFT);
            ParamUtil.setSessionAttr(request, KEY_JUMP_DEST_NODE, NODE_NAME_INCIDENT_INFO);
        } else {
            throw new IaisRuntimeException(ERR_MSG_INVALID_ACTION);
        }
        ParamUtil.setSessionAttr(request,KEY_ROOT_NODE_GROUP_INVEST_REPORT, investRepoRoot);
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
        ParamUtil.setRequestAttr(request,"causeSet",incidentInvestDto.getCauseSet());
        ParamUtil.setRequestAttr(request,"causeMap",incidentInvestDto.getIncidentCauseMap());
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
        } else if (ModuleCommonConstants.KEY_NAV_DRAFT.equals(actionType)) {
            ParamUtil.setRequestAttr(request, KEY_ACTION_TYPE, ModuleCommonConstants.KEY_NAV_DRAFT);
            ParamUtil.setSessionAttr(request, KEY_JUMP_DEST_NODE, NODE_NAME_INCIDENT_INVESTIGATION);
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
        } else if (ModuleCommonConstants.KEY_NAV_DRAFT.equals(actionType)) {
            ParamUtil.setRequestAttr(request, KEY_ACTION_TYPE, ModuleCommonConstants.KEY_NAV_DRAFT);
            ParamUtil.setSessionAttr(request, KEY_JUMP_DEST_NODE, NODE_NAME_MEDICAL_INVESTIGATION);
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
        } else if (ModuleCommonConstants.KEY_NAV_DRAFT.equals(actionType)) {
            ParamUtil.setRequestAttr(request, KEY_ACTION_TYPE, ModuleCommonConstants.KEY_NAV_DRAFT);
            ParamUtil.setSessionAttr(request, KEY_JUMP_DEST_NODE, NODE_NAME_DOCUMENTS);
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

        Nodes.needValidation(investRepoRoot, NODE_NAME_PREVIEW_SUBMIT);
        ParamUtil.setRequestAttr(request, "docSettings", getInvestReportDocSettings());
        PrimaryDocDto primaryDocDto = (PrimaryDocDto) ((SimpleNode)investRepoRoot.at(NODE_NAME_DOCUMENTS)).getValue();
        Map<String, List<DocRecordInfo>> savedFiles = primaryDocDto.getExistDocTypeMap();
        Map<String, List<NewDocInfo>> newFiles = primaryDocDto.getNewDocTypeMap();
        ParamUtil.setRequestAttr(request, "savedFiles", savedFiles);
        ParamUtil.setRequestAttr(request, "newFiles", newFiles);
    }

    public void handlePreviewSubmit(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        NodeGroup investRepoRoot = getInvestReportRoot(request);
        String actionType = ParamUtil.getString(request, KEY_ACTION_TYPE);
        String actionValue = ParamUtil.getString(request, KEY_ACTION_VALUE);
        if (KEY_ACTION_JUMP.equals(actionType)) {
            if (KEY_NAV_NEXT.equals(actionValue)) {
                // save docs
                SimpleNode primaryDocNode = (SimpleNode) investRepoRoot.at(NODE_NAME_DOCUMENTS);
                PrimaryDocDto primaryDocDto = (PrimaryDocDto) primaryDocNode.getValue();
                List<NewFileSyncDto> newFilesToSync = null;
                if (!primaryDocDto.getNewDocMap().isEmpty()) {
                    newFilesToSync = saveNewUploadedDoc(primaryDocDto);
                }

                // save data
                InvestReportDto investReportDto = InvestReportDto.from(investRepoRoot);
                AuditTrailDto auditTrailDto = (AuditTrailDto) ParamUtil.getSessionAttr(request, AuditTrailConsts.SESSION_ATTR_PARAM_NAME);
                investReportDto.setAuditTrailDto(auditTrailDto);
                ResponseDto<String> responseDto = investReportClient.saveNewIncidentInvest(investReportDto);
                if(log.isInfoEnabled()){
                    log.info("save new facility response: {}", LogUtil.escapeCrlf(responseDto.toString()));
                }
                try {
                    // sync files to BE file-repo (save new added files, delete useless files)
                    // delete docs
                    List<String> toBeDeletedRepoIds = deleteUnwantedDoc(primaryDocDto);
                    // sync docs
                    syncNewDocsAndDeleteFiles(newFilesToSync,toBeDeletedRepoIds);
                } catch (Exception e) {
                    log.error("Fail to sync files to BE", e);
                }

                ParamUtil.setRequestAttr(request, KEY_ACTION_TYPE, KEY_ACTION_SUBMIT);
            } else {
                jumpHandler(request, investRepoRoot);
            }
        } else if (ModuleCommonConstants.KEY_NAV_DRAFT.equals(actionType)) {
            ParamUtil.setRequestAttr(request, KEY_ACTION_TYPE, ModuleCommonConstants.KEY_NAV_DRAFT);
            ParamUtil.setSessionAttr(request, KEY_JUMP_DEST_NODE, NODE_NAME_PREVIEW_SUBMIT);
        } else {
            throw new IaisRuntimeException(ERR_MSG_INVALID_ACTION);
        }
        ParamUtil.setSessionAttr(request, KEY_ROOT_NODE_GROUP_INVEST_REPORT, investRepoRoot);
    }

    public void preAcknowledge(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        ParamUtil.setRequestAttr(request,KEY_INCIDENT_TITLE,KEY_TITLE_INVESTIGATION_REPORT);
    }

    /* Will be removed in future, will get this from config mechanism */
    private List<DocSetting> getInvestReportDocSettings () {
        List<DocSetting> docSettings = new ArrayList<>(1);
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
        SimpleNode referNoSelectionNode = new SimpleNode(new ReferNoSelectionDto(),NODE_NAME_REFERENCE_NO_SELECTION,new Node[0]);
        SimpleNode incidentNode = new SimpleNode(new IncidentInfoDto(), NODE_NAME_INCIDENT_INFO, new Node[]{referNoSelectionNode});
        SimpleNode incidentInvestNode = new SimpleNode(new IncidentInvestDto(), NODE_NAME_INCIDENT_INVESTIGATION,new Node[0]);
        SimpleNode medicalInvestNode = new SimpleNode(new MedicalInvestDto(), NODE_NAME_MEDICAL_INVESTIGATION, new Node[0]);
        SimpleNode documentNode = new SimpleNode(new PrimaryDocDto(), NODE_NAME_DOCUMENTS, new Node[0]);
        Node previewSubmitNode = new Node(NODE_NAME_PREVIEW_SUBMIT, new Node[]{incidentNode,incidentInvestNode,medicalInvestNode,documentNode});

        return new NodeGroup.Builder().name(name)
                .addNode(referNoSelectionNode)
                .addNode(incidentNode)
                .addNode(incidentInvestNode)
                .addNode(medicalInvestNode)
                .addNode(documentNode)
                .addNode(previewSubmitNode)
                .build();
    }

    public List<SelectOption> tempReferenceNoOps(){
        List<String> referNoList  = investReportClient.queryAllRefNo();
        if(CollectionUtils.isEmpty(referNoList)){
            return originalOps();
        }
        List<SelectOption> referNoOps = new ArrayList<>(referNoList.size()+1);
        referNoOps.add(new SelectOption("","Please Select"));
        for (String refNo : referNoList) {
            referNoOps.add(new SelectOption(refNo,refNo));
        }
        return referNoOps;
    }

    public static List<SelectOption> originalOps(){
        List<SelectOption> originalOps = new ArrayList<>(1);
        originalOps.add(new SelectOption("","Please Select"));
        return originalOps;
    }

    /** Save new uploaded documents into FE file repo.
     * @param primaryDocDto document DTO have the specific structure
     * @return a list of DTOs can be used to sync to BE
     */
    public List<NewFileSyncDto> saveNewUploadedDoc(PrimaryDocDto primaryDocDto) {
        List<NewFileSyncDto> newFilesToSync = null;
        if (!primaryDocDto.getNewDocMap().isEmpty()) {
            MultipartFile[] files = primaryDocDto.getNewDocMap().values().stream().map(NewDocInfo::getMultipartFile).toArray(MultipartFile[]::new);
            List<String> repoIds = fileRepoClient.saveFiles(files).getEntity();
            newFilesToSync = primaryDocDto.newFileSaved(repoIds);
        }
        return newFilesToSync;
    }

    /** Delete unwanted documents in FE file repo.
     * This method will remove the repoId from the toBeDeletedRepoIds set after a call of removal.
     * @param primaryDocDto document DTO have the specific structure
     * @return a list of repo IDs deleted in FE file repo
     */
    public List<String> deleteUnwantedDoc(PrimaryDocDto primaryDocDto) {
        /* Ignore the failure when try to delete FE files because this is not a big issue.
         * The not deleted file won't be retrieved, so it's just a waste of disk space */
        List<String> toBeDeletedRepoIds = new ArrayList<>(primaryDocDto.getToBeDeletedRepoIds());
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

    public void saveDraft(HttpServletRequest request) {
        NodeGroup investRepoRoot = getInvestReportRoot(request);

        // save docs
        SimpleNode primaryDocNode = (SimpleNode) investRepoRoot.at(NODE_NAME_DOCUMENTS);
        PrimaryDocDto primaryDocDto = (PrimaryDocDto) primaryDocNode.getValue();
        List<NewFileSyncDto> newFilesToSync = saveNewUploadedDoc(primaryDocDto);

        // save data
        InvestReportDto finalAllDataDto = InvestReportDto.from(investRepoRoot);
        String draftAppNo = investReportClient.saveDraftInvestigationReport(finalAllDataDto);
        // set draft app No. into the NodeGroup
        ReferNoSelectionDto referNoSelectionDto = (ReferNoSelectionDto) ((SimpleNode) investRepoRoot.at(NODE_NAME_REFERENCE_NO_SELECTION)).getValue();
        referNoSelectionDto.setDraftAppNo(draftAppNo);
        ParamUtil.setSessionAttr(request, KEY_ROOT_NODE_GROUP_INVEST_REPORT, investRepoRoot);

        try {
            // delete docs
            List<String> toBeDeletedRepoIds = deleteUnwantedDoc(primaryDocDto);
            // sync docs
            syncNewDocsAndDeleteFiles(newFilesToSync, toBeDeletedRepoIds);
        } catch (Exception e) {
            log.error("Fail to sync files to BE", e);
        }

        ParamUtil.setRequestAttr(request, KEY_IND_AFTER_SAVE_AS_DRAFT, Boolean.TRUE);
    }
}

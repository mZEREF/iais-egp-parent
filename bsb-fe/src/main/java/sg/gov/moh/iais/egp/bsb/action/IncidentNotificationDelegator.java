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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import sg.gov.moh.iais.egp.bsb.client.BsbFileClient;
import sg.gov.moh.iais.egp.bsb.client.FileRepoClient;
import sg.gov.moh.iais.egp.bsb.client.IncidentNotificationClient;
import sg.gov.moh.iais.egp.bsb.common.node.Node;
import sg.gov.moh.iais.egp.bsb.common.node.NodeGroup;
import sg.gov.moh.iais.egp.bsb.common.node.Nodes;
import sg.gov.moh.iais.egp.bsb.common.node.simple.SimpleNode;
import sg.gov.moh.iais.egp.bsb.constant.DocConstants;
import sg.gov.moh.iais.egp.bsb.constant.ReportableEventConstants;
import sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants;
import sg.gov.moh.iais.egp.bsb.dto.ResponseDto;
import sg.gov.moh.iais.egp.bsb.dto.file.DocRecordInfo;
import sg.gov.moh.iais.egp.bsb.dto.file.FileRepoSyncDto;
import sg.gov.moh.iais.egp.bsb.dto.file.NewDocInfo;
import sg.gov.moh.iais.egp.bsb.dto.file.NewFileSyncDto;
import sg.gov.moh.iais.egp.bsb.dto.report.FacilityInfo;
import sg.gov.moh.iais.egp.bsb.dto.report.PrimaryDocDto;
import sg.gov.moh.iais.egp.bsb.dto.report.notification.*;
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
 * @version 2021/12/2 9:12
 **/
@Slf4j
@Delegator("incidentNotificationDelegator")
public class IncidentNotificationDelegator {
    private static final String PARAM_SELECT_OCCURRENCE_HH_OPTIONS = "occurHHOps";
    private static final String PARAM_SELECT_OCCURRENCE_MM_OPTIONS = "occurMMOps";
    private static final String PARAM_SELECT_FACILITY_NAME_OPTIONS = "facNameOps";
    private static final String KEY_DRAFT = "draft";
    private static final String PARAM_PLEASE_SELECT = "Please Select";
    private static final String EDIT_REFERENCE_ID = "editRefId";
    private static final String KEY_EDIT_REF_ID = "editId";
    private final FileRepoClient fileRepoClient;
    private final IncidentNotificationClient incidentClient;
    private final BsbFileClient bsbFileClient;

    @Autowired
    public IncidentNotificationDelegator(FileRepoClient fileRepoClient, IncidentNotificationClient incidentClient, BsbFileClient bsbFileClient) {
        this.fileRepoClient = fileRepoClient;
        this.incidentClient = incidentClient;
        this.bsbFileClient = bsbFileClient;
    }

    public void start(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        request.getSession().removeAttribute(KEY_ROOT_NODE_GROUP_INCIDENT_NOT);

        AuditTrailHelper.auditFunction(MODULE_NAME_NOTIFICATION_OF_INCIDENT, MODULE_NAME_NOTIFICATION_OF_INCIDENT);
    }

    public void init(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        boolean newIncidentNot = true;

        // check if we are doing editing,reference Id
        String maskedRefId = request.getParameter(EDIT_REFERENCE_ID);
        if (StringUtils.hasLength(maskedRefId)) {
            if (log.isInfoEnabled()) {
                log.info("masked reference no: {}", LogUtil.escapeCrlf(maskedRefId));
            }
            newIncidentNot = false;
            boolean failRetrieveEditData = true;
            String refId = MaskUtil.unMaskValue(KEY_EDIT_REF_ID, maskedRefId);
            if (refId != null && !maskedRefId.equals(refId)) {
                ResponseDto<IncidentNotificationDto> resultDto = incidentClient.retrieveIncidentNotByReferenceId(refId);
                if (resultDto.ok()) {
                    failRetrieveEditData = false;
                    NodeGroup incidentNotRoot = resultDto.getEntity().toIncidentNotificationDto(KEY_ROOT_NODE_GROUP_INCIDENT_NOT);
                    ParamUtil.setSessionAttr(request, KEY_ROOT_NODE_GROUP_INCIDENT_NOT, incidentNotRoot);
                }
            }
            if (failRetrieveEditData) {
                throw new IaisRuntimeException("Fail to retrieve app data");
            }
        }

        //-------------------------------------------------------------
        //enter from draft continue button
        //processType is make sure enter from your own draft
        String processType = (String) ParamUtil.getSessionAttr(request,ModuleCommonConstants.KEY_ACTION_TYPE);
        Draft draft = (Draft) ParamUtil.getSessionAttr(request,KEY_DRAFT);
        if(draft != null && "notification".equals(processType)){
            newIncidentNot = false;
            ObjectMapper mapper = new ObjectMapper();
            try {
                IncidentNotificationDto notificationDto = mapper.readValue(draft.getDraftData(),IncidentNotificationDto.class);
                NodeGroup incidentNotRoot = notificationDto.toIncidentNotificationDto(KEY_ROOT_NODE_GROUP_INCIDENT_NOT);
                ParamUtil.setSessionAttr(request, KEY_ROOT_NODE_GROUP_INCIDENT_NOT, incidentNotRoot);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }

        if (newIncidentNot) {
            ParamUtil.setSessionAttr(request, KEY_ROOT_NODE_GROUP_INCIDENT_NOT,getIncidentNotRoot(request));
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
        ParamUtil.setRequestAttr(request,"incidentReportOps",tempReportingOfIncidentOps());
    }

    public void handleIncidentInfo(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        NodeGroup incidentNotRoot = getIncidentNotRoot(request);
        SimpleNode incidentNode = (SimpleNode) incidentNotRoot.getNode(NODE_NAME_INCIDENT_INFO);
        IncidentInfoDto incidentInfoDto = (IncidentInfoDto) incidentNode.getValue();
        incidentInfoDto.reqObjMapping(request);
        String actionType = ParamUtil.getString(request, KEY_ACTION_TYPE);
        if (KEY_ACTION_JUMP.equals(actionType)) {
            jumpHandler(request, incidentNotRoot);
        }  else if (ModuleCommonConstants.KEY_NAV_DRAFT.equals(actionType)) {
            ParamUtil.setRequestAttr(request, KEY_ACTION_TYPE, ModuleCommonConstants.KEY_NAV_DRAFT);
            ParamUtil.setSessionAttr(request, KEY_JUMP_DEST_NODE, NODE_NAME_INCIDENT_INFO);
        }else {
            throw new IaisRuntimeException(ERR_MSG_INVALID_ACTION);
        }
        ParamUtil.setSessionAttr(request, KEY_ROOT_NODE_GROUP_INCIDENT_NOT, incidentNotRoot);
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
        ParamUtil.setRequestAttr(request,PARAM_SELECT_OCCURRENCE_HH_OPTIONS,tempOccurrenceHHOps());
        ParamUtil.setRequestAttr(request,PARAM_SELECT_OCCURRENCE_MM_OPTIONS,tempOccurrenceMMOps());
        ParamUtil.setRequestAttr(request,PARAM_SELECT_FACILITY_NAME_OPTIONS,tempFacNameOps());
    }

    public void handlePersonReportingInfo(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        NodeGroup incidentNotRoot = getIncidentNotRoot(request);
        SimpleNode reportingPersonNode = (SimpleNode) incidentNotRoot.getNode(NODE_NAME_PERSON_REPORTING_INFO);
        PersonReportingDto personReportingDto = (PersonReportingDto) reportingPersonNode.getValue();
        personReportingDto.reqObjMapping(request);
        String actionType = ParamUtil.getString(request, KEY_ACTION_TYPE);
        if (KEY_ACTION_JUMP.equals(actionType)) {
            jumpHandler(request, incidentNotRoot);
        } else if (ModuleCommonConstants.KEY_NAV_DRAFT.equals(actionType)) {
            ParamUtil.setRequestAttr(request, KEY_ACTION_TYPE, ModuleCommonConstants.KEY_NAV_DRAFT);
            ParamUtil.setSessionAttr(request, KEY_JUMP_DEST_NODE, NODE_NAME_PERSON_REPORTING_INFO);
        } else {
            throw new IaisRuntimeException(ERR_MSG_INVALID_ACTION);
        }
        ParamUtil.setSessionAttr(request, KEY_ROOT_NODE_GROUP_INCIDENT_NOT, incidentNotRoot);
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
        //set person involved info to field involved person count
        SimpleNode reportingPersonNode = (SimpleNode) incidentNotRoot.getNode(NODE_NAME_PERSON_REPORTING_INFO);
        PersonReportingDto personReportingDto = (PersonReportingDto) reportingPersonNode.getValue();
        personReportingDto.setIncidentPersonInvolvedCount(String.valueOf(personInvolvedInfoDto.getIncidentPersons().size()));

        String actionType = ParamUtil.getString(request, KEY_ACTION_TYPE);
        if (KEY_ACTION_JUMP.equals(actionType)) {
            jumpHandler(request, incidentNotRoot);
        } else if (ModuleCommonConstants.KEY_NAV_DRAFT.equals(actionType)) {
            ParamUtil.setRequestAttr(request, KEY_ACTION_TYPE, ModuleCommonConstants.KEY_NAV_DRAFT);
            ParamUtil.setSessionAttr(request, KEY_JUMP_DEST_NODE, NODE_NAME_PERSON_INVOLVED_INFO);
        } else {
            throw new IaisRuntimeException(ERR_MSG_INVALID_ACTION);
        }
        ParamUtil.setSessionAttr(request, KEY_ROOT_NODE_GROUP_INCIDENT_NOT, incidentNotRoot);
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
            jumpHandler(request, incidentNotRoot);
        }else if (ModuleCommonConstants.KEY_NAV_DRAFT.equals(actionType)) {
            ParamUtil.setRequestAttr(request, KEY_ACTION_TYPE, ModuleCommonConstants.KEY_NAV_DRAFT);
            ParamUtil.setSessionAttr(request, KEY_JUMP_DEST_NODE, NODE_NAME_DOCUMENTS);
        } else {
            throw new IaisRuntimeException(ERR_MSG_INVALID_ACTION);
        }
        ParamUtil.setSessionAttr(request, KEY_ROOT_NODE_GROUP_INCIDENT_NOT, incidentNotRoot);
    }

    public void preSubmit(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        NodeGroup incidentNotRoot = getIncidentNotRoot(request);

        ParamUtil.setRequestAttr(request, NODE_NAME_INCIDENT_INFO, ((SimpleNode)incidentNotRoot.at(NODE_NAME_INCIDENT_INFO)).getValue());
        ParamUtil.setRequestAttr(request, NODE_NAME_PERSON_REPORTING_INFO, ((SimpleNode)incidentNotRoot.at(NODE_NAME_PERSON_REPORTING_INFO)).getValue());
        ParamUtil.setRequestAttr(request, NODE_NAME_PERSON_INVOLVED_INFO, ((SimpleNode)incidentNotRoot.at(NODE_NAME_PERSON_INVOLVED_INFO)).getValue());


        ParamUtil.setRequestAttr(request, "docSettings", getIncidentNotDocSettings());
        PrimaryDocDto primaryDocDto = (PrimaryDocDto) ((SimpleNode)incidentNotRoot.at(NODE_NAME_DOCUMENTS)).getValue();
        Map<String, List<DocRecordInfo>> savedFiles = primaryDocDto.getExistDocTypeMap();
        Map<String, List<NewDocInfo>> newFiles = primaryDocDto.getNewDocTypeMap();
        ParamUtil.setRequestAttr(request, "savedFiles", savedFiles);
        ParamUtil.setRequestAttr(request, "newFiles", newFiles);
    }

    public void handleSubmit(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        NodeGroup incidentNotRoot = getIncidentNotRoot(request);
        String actionType = ParamUtil.getString(request, KEY_ACTION_TYPE);
        String actionValue = ParamUtil.getString(request, KEY_ACTION_VALUE);
        if (KEY_ACTION_JUMP.equals(actionType)) {
            if (KEY_NAV_NEXT.equals(actionValue)) {
                // save docs
                SimpleNode primaryDocNode = (SimpleNode) incidentNotRoot.at(NODE_NAME_DOCUMENTS);
                PrimaryDocDto primaryDocDto = (PrimaryDocDto) primaryDocNode.getValue();
                List<NewFileSyncDto> newFilesToSync = null;
                if (!primaryDocDto.getNewDocMap().isEmpty()) {
                    newFilesToSync = saveNewUploadedDoc(primaryDocDto);
                }

                // save data
                IncidentNotificationDto incidentNotificationDto = IncidentNotificationDto.from(incidentNotRoot);
                AuditTrailDto auditTrailDto = (AuditTrailDto) ParamUtil.getSessionAttr(request, AuditTrailConsts.SESSION_ATTR_PARAM_NAME);
                incidentNotificationDto.setAuditTrailDto(auditTrailDto);
                ResponseDto<String> responseDto = incidentClient.saveNewIncidentNotification(incidentNotificationDto);
                if(log.isInfoEnabled()){
                    log.info("save new facility response: {}", LogUtil.escapeCrlf(responseDto.toString()));
                }
                try {
                    // delete docs
                    List<String> toBeDeletedRepoIds = deleteUnwantedDoc(primaryDocDto);
                    // sync docs
                    syncNewDocsAndDeleteFiles(newFilesToSync,toBeDeletedRepoIds);
                } catch (Exception e) {
                    log.error("Fail to sync files to BE", e);
                }

                ParamUtil.setRequestAttr(request, KEY_ACTION_TYPE, KEY_ACTION_SUBMIT);
            } else {
                jumpHandler(request, incidentNotRoot);
            }
        } else if (ModuleCommonConstants.KEY_NAV_DRAFT.equals(actionType)) {
            ParamUtil.setRequestAttr(request, KEY_ACTION_TYPE, ModuleCommonConstants.KEY_NAV_DRAFT);
            ParamUtil.setSessionAttr(request, KEY_JUMP_DEST_NODE, NODE_NAME_PREVIEW_SUBMIT);
        } else {
            throw new IaisRuntimeException(ERR_MSG_INVALID_ACTION);
        }
        ParamUtil.setSessionAttr(request, KEY_ROOT_NODE_GROUP_INCIDENT_NOT, incidentNotRoot);
    }

    public void preAcknowledge(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        ParamUtil.setRequestAttr(request,KEY_INCIDENT_TITLE,KEY_TITLE_INCIDENT_NOTIFICATION);
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

    public NodeGroup getIncidentNotRoot (HttpServletRequest request){
        NodeGroup root = (NodeGroup) ParamUtil.getSessionAttr(request, KEY_ROOT_NODE_GROUP_INCIDENT_NOT);
        if (root == null) {
            root = newIncidentNotRoot(KEY_ROOT_NODE_GROUP_INCIDENT_NOT);
        }
        return root;
    }

    public static NodeGroup newIncidentNotRoot(String name) {
        SimpleNode incidentNode = new SimpleNode(new IncidentInfoDto(), NODE_NAME_INCIDENT_INFO, new Node[0]);
        SimpleNode reportingPersonNode = new SimpleNode(new PersonReportingDto(), NODE_NAME_PERSON_REPORTING_INFO, new Node[0]);
        SimpleNode involvedPersonNode = new SimpleNode(new PersonInvolvedInfoDto(), NODE_NAME_PERSON_INVOLVED_INFO, new Node[0]);
        SimpleNode documentNode = new SimpleNode(new PrimaryDocDto(), NODE_NAME_DOCUMENTS, new Node[0]);
        Node previewSubmitNode = new Node(NODE_NAME_PREVIEW_SUBMIT, new Node[]{incidentNode,reportingPersonNode,involvedPersonNode,documentNode});

        return new NodeGroup.Builder().name(name)
                .addNode(incidentNode)
                .addNode(reportingPersonNode)
                .addNode(involvedPersonNode)
                .addNode(documentNode)
                .addNode(previewSubmitNode)
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

    public static List<SelectOption> tempOccurrenceHHOps(){
        List<SelectOption> occurrenceHHOps = new ArrayList<>(24);
        for (int i = 1; i <= 24; i++) {
            String val = i/10 == 0 ?"0"+i:String.valueOf(i);
            occurrenceHHOps.add(new SelectOption(val,val));
        }
        return occurrenceHHOps;
    }

    public static List<SelectOption> tempOccurrenceMMOps(){
        List<SelectOption> occurrenceMMops = new ArrayList<>(60);
        for (int i = 1; i <= 60 ; i++) {
            String val = i/10 == 0 ?"0"+i:String.valueOf(i);
            occurrenceMMops.add(new SelectOption(val,val));
        }
        return occurrenceMMops;
    }

    /**
     * temp deal option for dropdown list reportOfIncident
     * this method would be delete in future
     * */
    public static List<SelectOption> tempReportingOfIncidentOps(){
        List<SelectOption> reportIncidentOps = new ArrayList<>(3);
        reportIncidentOps.add(new SelectOption("",PARAM_PLEASE_SELECT));
        reportIncidentOps.add(new SelectOption("REPORT001","Adverse incident or accident"));
        reportIncidentOps.add(new SelectOption("REPORT002","Near miss"));
        return reportIncidentOps;
    }

    public List<SelectOption> tempFacNameOps(){
        List<FacilityInfo> facilityInfos = incidentClient.queryDistinctFacilityName();
        if(CollectionUtils.isEmpty(facilityInfos)){
           return originalOps();
        }
        List<SelectOption> facNameOps = new ArrayList<>(facilityInfos.size());
        facNameOps.add(new SelectOption("",PARAM_PLEASE_SELECT));
        for (FacilityInfo info : facilityInfos) {
            facNameOps.add(new SelectOption(MaskUtil.maskValue("id",info.getFacId()),info.getFacName()));
        }
        return facNameOps;
    }


    public static List<SelectOption> originalOps(){
        List<SelectOption> originalOps = new ArrayList<>(1);
        originalOps.add(new SelectOption("",PARAM_PLEASE_SELECT));
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
        NodeGroup incidentNotRoot = getIncidentNotRoot(request);

        // save docs
        SimpleNode primaryDocNode = (SimpleNode) incidentNotRoot.at(NODE_NAME_DOCUMENTS);
        PrimaryDocDto primaryDocDto = (PrimaryDocDto) primaryDocNode.getValue();
        List<NewFileSyncDto> newFilesToSync = saveNewUploadedDoc(primaryDocDto);

        // save data
        IncidentNotificationDto finalAllDataDto = IncidentNotificationDto.from(incidentNotRoot);
        String draftAppNo = incidentClient.saveDraftIncidentNotification(finalAllDataDto);
        // set draft app No. into the NodeGroup
        IncidentInfoDto incidentInfoDto = (IncidentInfoDto) ((SimpleNode) incidentNotRoot.at(NODE_NAME_INCIDENT_INFO)).getValue();
        incidentInfoDto.setDraftAppNo(draftAppNo);
        ParamUtil.setSessionAttr(request, KEY_ROOT_NODE_GROUP_INCIDENT_NOT, incidentNotRoot);

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

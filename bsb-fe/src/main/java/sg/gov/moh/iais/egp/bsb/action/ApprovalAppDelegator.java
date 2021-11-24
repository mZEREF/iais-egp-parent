package sg.gov.moh.iais.egp.bsb.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.AuditTrailConsts;
import com.ecquaria.cloud.moh.iais.common.dto.AuditTrailDto;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.filerepo.FileRepoDto;
import com.ecquaria.cloud.moh.iais.common.dto.mastercode.MasterCodeView;
import com.ecquaria.cloud.moh.iais.common.exception.IaisRuntimeException;
import com.ecquaria.cloud.moh.iais.common.utils.MaskUtil;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.MasterCodeUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import sg.gov.moh.iais.egp.bsb.client.ApprovalAppClient;
import sg.gov.moh.iais.egp.bsb.client.BsbFileClient;
import sg.gov.moh.iais.egp.bsb.client.FileRepoClient;
import sg.gov.moh.iais.egp.bsb.common.node.Node;
import sg.gov.moh.iais.egp.bsb.common.node.NodeGroup;
import sg.gov.moh.iais.egp.bsb.common.node.Nodes;
import sg.gov.moh.iais.egp.bsb.common.node.simple.SimpleNode;
import sg.gov.moh.iais.egp.bsb.constant.DocConstants;
import sg.gov.moh.iais.egp.bsb.dto.ResponseDto;
import sg.gov.moh.iais.egp.bsb.dto.approval.*;
import sg.gov.moh.iais.egp.bsb.dto.file.DocRecordInfo;
import sg.gov.moh.iais.egp.bsb.dto.file.FileRepoSyncDto;
import sg.gov.moh.iais.egp.bsb.dto.file.NewDocInfo;
import sg.gov.moh.iais.egp.bsb.dto.file.NewFileSyncDto;
import sg.gov.moh.iais.egp.bsb.entity.Biological;
import sg.gov.moh.iais.egp.bsb.entity.DocSetting;
import sg.gov.moh.iais.egp.bsb.entity.Facility;
import sg.gov.moh.iais.egp.bsb.entity.FacilityActivity;
import sg.gov.moh.iais.egp.bsb.util.CollectionUtils;
import sg.gov.moh.iais.egp.bsb.util.LogUtil;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import static sg.gov.moh.iais.egp.bsb.constant.ApprovalAppConstants.*;

/**
 * @author : LiRan
 * @date : 2021/10/8
 */
@Slf4j
@Delegator("bsbApprovalAppDelegator")
public class ApprovalAppDelegator {
    public static final String MODULE_NAME = "New Approval Application";
    private static final String KEY_ROOT_NODE_GROUP = "approvalAppRoot";

    private static final String KEY_EDIT_APP_ID = "editId";
    private static final String KEY_PROCESS_TYPE = "processType";
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

    private static final String ERR_MSG_BAT_NOT_NULL = "Biological Agent/Toxin node group must not be null!";
    private static final String ERR_MSG_NULL_GROUP = "Node group must not be null!";
    private static final String ERR_MSG_NULL_NAME = "Name must not be null!";
    private static final String ERR_MSG_INVALID_ACTION = "Invalid action";

    private static final String FACILITY_ID_SELECT = "facilityIdSelect";
    private static final String ACTIVITY_ID_SELECT_DTO = "activityIdSelectDto";

    private static final String KEY_COUNTRY_OPTIONS = "countryOps";

    private final ApprovalAppClient approvalAppClient;
    private final FileRepoClient fileRepoClient;
    private final BsbFileClient bsbFileClient;

    @Autowired
    public ApprovalAppDelegator(ApprovalAppClient approvalAppClient, FileRepoClient fileRepoClient, BsbFileClient bsbFileClient) {
        this.approvalAppClient = approvalAppClient;
        this.fileRepoClient = fileRepoClient;
        this.bsbFileClient = bsbFileClient;
    }

    public void init(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
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
                ResponseDto<ApprovalAppDto> resultDto = approvalAppClient.getApprovalAppAppDataByApplicationId(appId);
                if (resultDto.ok()) {
                    failRetrieveEditData = false;
                    NodeGroup approvalAppRoot = resultDto.getEntity().toApprovalAppRootGroup(KEY_ROOT_NODE_GROUP);
                    ParamUtil.setSessionAttr(request, KEY_ROOT_NODE_GROUP, approvalAppRoot);
                }
            }
            if (failRetrieveEditData) {
                throw new IaisRuntimeException("Fail to retrieve app data");
            }
        }
        String processType = request.getParameter(KEY_PROCESS_TYPE);
        ParamUtil.setSessionAttr(request, KEY_PROCESS_TYPE, processType);
        if (newApprovalApp) {
            ParamUtil.setSessionAttr(request, KEY_ROOT_NODE_GROUP, getApprovalAppRoot(request));
        }
    }

    public void start(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        request.getSession().removeAttribute(KEY_ROOT_NODE_GROUP);
        request.getSession().removeAttribute(KEY_PROCESS_TYPE);
        AuditTrailHelper.auditFunction(MODULE_NAME, MODULE_NAME);
    }

    public void preCompInfo(BaseProcessClass bpc){
        // do nothing now, need to prepare company info in the future
    }

    public void handleCompInfo(BaseProcessClass bpc){
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
        List<SelectOption> facilityIdList = new ArrayList<>(facilityList.size());
        facilityIdList.add(new SelectOption("Please Select","Please Select"));
        List<FacilityActivitySelectDto> facilityActivitySelectDtoList = new ArrayList<>();
        if (!facilityList.isEmpty()){
            for (Facility fac : facilityList) {
                //initialize facility selectOption
                facilityIdList.add(new SelectOption(fac.getId(),fac.getFacilityName()));
                //initialize facilityActivity selectOption
                List<FacilityActivity> facilityActivityList = approvalAppClient.getApprovalFAByFacId(fac.getId()).getEntity();
                List<SelectOption> activityIdList = new ArrayList<>(facilityActivityList.size());
                for (FacilityActivity facilityActivity : facilityActivityList) {
                    activityIdList.add(new SelectOption(facilityActivity.getId(),facilityActivity.getActivityType()));
                }
                facilityActivitySelectDtoList.add(new FacilityActivitySelectDto(fac.getId(),activityIdList));
            }
        }
        ParamUtil.setRequestAttr(request, ACTIVITY_ID_SELECT_DTO, facilityActivitySelectDtoList);
        ParamUtil.setRequestAttr(request, FACILITY_ID_SELECT, facilityIdList);

        NodeGroup approvalAppRoot = getApprovalAppRoot(request);
        SimpleNode activityNode = (SimpleNode) approvalAppRoot.getNode(NODE_NAME_ACTIVITY);
        ActivityDto activityDto = (ActivityDto)activityNode.getValue();
        Boolean needShowError = (Boolean) ParamUtil.getRequestAttr(request, KEY_SHOW_ERROR_SWITCH);
        if (needShowError == Boolean.TRUE) {
            ParamUtil.setRequestAttr(request, KEY_VALIDATION_ERRORS, activityDto.retrieveValidationResult());
        }
        Nodes.needValidation(approvalAppRoot, NODE_NAME_ACTIVITY);
        ParamUtil.setRequestAttr(request, NODE_NAME_ACTIVITY, activityDto);
    }

    public void handleActivity(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        NodeGroup approvalAppRoot = getApprovalAppRoot(request);
        SimpleNode activityNode = (SimpleNode) approvalAppRoot.getNode(NODE_NAME_ACTIVITY);
        ActivityDto activityDto = (ActivityDto) activityNode.getValue();
        activityDto.reqObjMapping(request);

        String actionType = ParamUtil.getString(request, KEY_ACTION_TYPE);
        String actionValue = ParamUtil.getString(request, KEY_ACTION_VALUE);
        Assert.hasText(actionValue, "Invalid action value");
        boolean currentLetGo = true;
        if (KEY_NAV_NEXT.equals(actionValue)) {  // if click next, we need to validate current node anyway
            currentLetGo = activityNode.doValidation();
            if (currentLetGo) {
                Nodes.passValidation(approvalAppRoot, NODE_NAME_ACTIVITY);
            }
        }

        if (activityNode.isValidated()) {
            //replace new approvalProfileGroup to approvalAppRoot by scheduleList
            NodeGroup approvalProfileGroup = (NodeGroup) approvalAppRoot.getNode(NODE_NAME_APPROVAL_PROFILE);
            changeApprovalProfileNodeGroup(approvalProfileGroup, activityDto);
            //get primaryDocDto(facility registration upload doc) by current facilityId
            Collection<DocRecordInfo> docRecordInfos = approvalAppClient.getFacDocByFacId(activityDto.getFacilityId()).getEntity();
            PrimaryDocDto registrationPrimaryDocDto = new PrimaryDocDto();
            registrationPrimaryDocDto.setSavedDocMap(CollectionUtils.uniqueIndexMap(docRecordInfos, DocRecordInfo::getRepoId));
            //get new primaryDocNode
            NodeGroup approvalProfileNodeGroup = (NodeGroup) approvalAppRoot.at(NODE_NAME_APPROVAL_PROFILE);
            SimpleNode primaryDocNode = new SimpleNode(registrationPrimaryDocDto, NODE_NAME_PRIMARY_DOC, new Node[]{activityNode,approvalProfileNodeGroup});
            //replace new primaryDocNode to approvalAppRoot
            approvalAppRoot.replaceNode(primaryDocNode);
            //replace new previewSubmitNode to approvalAppRoot(reason:this node is depend on old primaryDocNode)
            Node previewSubmitNode = new Node(NODE_NAME_PREVIEW_SUBMIT,new Node[]{activityNode,approvalProfileNodeGroup,primaryDocNode});
            approvalAppRoot.replaceNode(previewSubmitNode);
        }
        if (KEY_ACTION_JUMP.equals(actionType)) {
            jumpHandler(request, approvalAppRoot, NODE_NAME_ACTIVITY, activityNode);
        } else {
            throw new IaisRuntimeException(ERR_MSG_INVALID_ACTION);
        }
        ParamUtil.setSessionAttr(request, KEY_ROOT_NODE_GROUP, approvalAppRoot);
    }

    public void prepareApprovalProfile(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        NodeGroup approvalAppRoot = getApprovalAppRoot(request);
        String currentNodePath = (String) ParamUtil.getSessionAttr(request, KEY_JUMP_DEST_NODE);
        SimpleNode approvalProfileNode = (SimpleNode) approvalAppRoot.at(currentNodePath);
        ApprovalProfileDto approvalProfileDto = (ApprovalProfileDto) approvalProfileNode.getValue();
        Boolean needShowError = (Boolean) ParamUtil.getRequestAttr(request, KEY_SHOW_ERROR_SWITCH);
        if (needShowError == Boolean.TRUE) {
            ParamUtil.setRequestAttr(request, KEY_VALIDATION_ERRORS, approvalProfileDto.retrieveValidationResult());
        }
        Nodes.needValidation(approvalAppRoot,currentNodePath);
        ParamUtil.setRequestAttr(request, NODE_NAME_APPROVAL_PROFILE, approvalProfileDto);

        NodeGroup approvalProfileGroup = (NodeGroup) approvalAppRoot.at(NODE_NAME_APPROVAL_PROFILE);
        ParamUtil.setRequestAttr(request, "activeNodeKey", approvalProfileGroup.getActiveNodeKey());
        SimpleNode activityNode = (SimpleNode) approvalAppRoot.getNode(NODE_NAME_ACTIVITY);
        ActivityDto activityDto = (ActivityDto) activityNode.getValue();
        ParamUtil.setRequestAttr(request, "schedules", activityDto.getSchedules());
        String currentSchedule = approvalProfileNode.getName();
        List<Biological> biologicalList = this.approvalAppClient.getBiologicalBySchedule(currentSchedule).getEntity();
        List<SelectOption> batIdOps = new ArrayList<>(biologicalList.size());
        if (!biologicalList.isEmpty()){
            for (Biological biological : biologicalList) {
                batIdOps.add(new SelectOption(biological.getId(),biological.getName()));
            }
        }
        ParamUtil.setRequestAttr(request, "batIdOps", batIdOps);
        ParamUtil.setRequestAttr(request, KEY_COUNTRY_OPTIONS, tmpCountryOps());
    }

    public void handleApprovalProfile(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        NodeGroup approvalAppRoot = getApprovalAppRoot(request);
        String currentNodePath = (String) ParamUtil.getSessionAttr(request, KEY_JUMP_DEST_NODE);
        SimpleNode approvalProfileNode = (SimpleNode) approvalAppRoot.at(currentNodePath);
        ApprovalProfileDto approvalProfileDto = (ApprovalProfileDto) approvalProfileNode.getValue();
        approvalProfileDto.reqObjMapping(request);

        String actionType = ParamUtil.getString(request, KEY_ACTION_TYPE);
        if (KEY_ACTION_JUMP.equals(actionType)) {
            jumpHandler(request, approvalAppRoot, currentNodePath, approvalProfileNode);
        } else {
            throw new IaisRuntimeException(ERR_MSG_INVALID_ACTION);
        }
        ParamUtil.setSessionAttr(request, KEY_ROOT_NODE_GROUP, approvalAppRoot);
    }

    public void prePrimaryDoc(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        NodeGroup approvalAppRoot = getApprovalAppRoot(request);
        SimpleNode primaryDocNode = (SimpleNode) approvalAppRoot.at(NODE_NAME_PRIMARY_DOC);
        PrimaryDocDto primaryDocDto = (PrimaryDocDto) primaryDocNode.getValue();
        Boolean needShowError = (Boolean) ParamUtil.getRequestAttr(request, KEY_SHOW_ERROR_SWITCH);
        if (needShowError == Boolean.TRUE) {
            ParamUtil.setRequestAttr(request, KEY_VALIDATION_ERRORS, primaryDocDto.retrieveValidationResult());
        }
        Nodes.needValidation(approvalAppRoot, NODE_NAME_PRIMARY_DOC);

        ParamUtil.setRequestAttr(request, "docSettings", getApprovalAppDocSettings());

        Map<String, List<DocRecordInfo>> savedFiles = primaryDocDto.getExistDocTypeMap();
        Map<String, List<NewDocInfo>> newFiles = primaryDocDto.getNewDocTypeMap();
        ParamUtil.setRequestAttr(request, "savedFiles", savedFiles);
        ParamUtil.setRequestAttr(request, "newFiles", newFiles);
    }

    public void handlePrimaryDoc(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        NodeGroup approvalAppRoot = getApprovalAppRoot(request);
        SimpleNode primaryDocNode = (SimpleNode) approvalAppRoot.at(NODE_NAME_PRIMARY_DOC);
        PrimaryDocDto primaryDocDto = (PrimaryDocDto) primaryDocNode.getValue();
        primaryDocDto.reqObjMapping(request);

        String actionType = ParamUtil.getString(request, KEY_ACTION_TYPE);
        if (KEY_ACTION_JUMP.equals(actionType)) {
            jumpHandler(request, approvalAppRoot, NODE_NAME_PRIMARY_DOC, primaryDocNode);
        } else {
            throw new IaisRuntimeException(ERR_MSG_INVALID_ACTION);
        }
        ParamUtil.setSessionAttr(request, KEY_ROOT_NODE_GROUP, approvalAppRoot);
    }

    public void prePreviewSubmit(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        NodeGroup approvalAppRoot = getApprovalAppRoot(request);

        NodeGroup approvalProfileGroup = (NodeGroup) approvalAppRoot.at(NODE_NAME_APPROVAL_PROFILE);
        List<ApprovalProfileDto> batList = getApprovalProfileList(approvalProfileGroup);
        ParamUtil.setRequestAttr(request, "approvalProfileList", batList);

        ParamUtil.setRequestAttr(request, "docSettings", getApprovalAppDocSettings());
        ParamUtil.setRequestAttr(request, NODE_NAME_PRIMARY_DOC, ((PrimaryDocDto)((SimpleNode)approvalAppRoot.at(NODE_NAME_PRIMARY_DOC)).getValue()).getAllDocTypeMap());
    }

    public void handlePreviewSubmit(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        NodeGroup approvalAppRoot = getApprovalAppRoot(request);
        Node previewSubmitNode = approvalAppRoot.at(NODE_NAME_PREVIEW_SUBMIT);

        String actionType = ParamUtil.getString(request, KEY_ACTION_TYPE);
        String actionValue = ParamUtil.getString(request, KEY_ACTION_VALUE);
        if (KEY_ACTION_JUMP.equals(actionType)) {
            if (KEY_NAV_NEXT.equals(actionValue)) {
                // save docs
                SimpleNode primaryDocNode = (SimpleNode) approvalAppRoot.at(NODE_NAME_PRIMARY_DOC);
                PrimaryDocDto primaryDocDto = (PrimaryDocDto) primaryDocNode.getValue();
                List<NewFileSyncDto> newFilesToSync = null;
                if (!primaryDocDto.getNewDocMap().isEmpty()) {
                    MultipartFile[] files = primaryDocDto.getNewDocMap().values().stream().map(NewDocInfo::getMultipartFile).toArray(MultipartFile[]::new);
                    List<String> repoIds = fileRepoClient.saveFiles(files).getEntity();
                    newFilesToSync = primaryDocDto.newFileSaved(repoIds);
                }

                // save data
                ApprovalAppDto finalAllDataDto = ApprovalAppDto.from(approvalAppRoot);
                AuditTrailDto auditTrailDto = (AuditTrailDto) ParamUtil.getSessionAttr(request, AuditTrailConsts.SESSION_ATTR_PARAM_NAME);
                finalAllDataDto.setAuditTrailDto(auditTrailDto);
                ResponseDto<String> responseDto = approvalAppClient.saveNewApprovalApp(finalAllDataDto);
                log.info("save new approval application response: {}", responseDto);

                try {
                    // sync files to BE file-repo (save new added files, delete useless files)
                    if ((newFilesToSync != null && !newFilesToSync.isEmpty()) || !primaryDocDto.getToBeDeletedRepoIds().isEmpty()) {
                        /* Ignore the failure of sync files currently.
                         * We should add a mechanism to retry synchronization of files in the future */
                        FileRepoSyncDto syncDto = new FileRepoSyncDto();
                        syncDto.setNewFiles(newFilesToSync);
                        syncDto.setToDeleteIds(new ArrayList<>(primaryDocDto.getToBeDeletedRepoIds()));
                        bsbFileClient.saveFiles(syncDto);
                    }

                    // delete docs in FE file-repo
                    /* Ignore the failure when try to delete FE files because this is not a big issue.
                     * The not deleted file won't be retrieved, so it's just a waste of disk space */
                    for (String id: primaryDocDto.getToBeDeletedRepoIds()) {
                        FileRepoDto fileRepoDto = new FileRepoDto();
                        fileRepoDto.setId(id);
                        fileRepoClient.removeFileById(fileRepoDto);
                    }
                } catch (Exception e) {
                    log.error("Fail to sync files to BE", e);
                }

                ParamUtil.setRequestAttr(request, KEY_ACTION_TYPE, KEY_ACTION_SUBMIT);
            } else {
                jumpHandler(request, approvalAppRoot, NODE_NAME_PREVIEW_SUBMIT, previewSubmitNode);
            }
        } else {
            throw new IaisRuntimeException(ERR_MSG_INVALID_ACTION);
        }
        ParamUtil.setSessionAttr(request, KEY_ROOT_NODE_GROUP, approvalAppRoot);
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
        // do nothing now
    }

    public void doSubmit(BaseProcessClass bpc){
        // do nothing now
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
    private List<DocSetting> getApprovalAppDocSettings () {
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
    private static List<SelectOption> tmpCountryOps() {
        List<MasterCodeView> views = MasterCodeUtil.retrieveByCategory(MasterCodeUtil.CATE_ID_NATIONALITY);
        List<SelectOption> ops = new ArrayList<>(views.size());
        if(!org.springframework.util.CollectionUtils.isEmpty(views)){
            for (MasterCodeView view : views) {
                ops.add(new SelectOption(view.getCode(), view.getCodeValue()));
            }
        }
        return ops;
    }
}

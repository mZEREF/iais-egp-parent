package sg.gov.moh.iais.egp.bsb.service;

import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.filerepo.FileRepoDto;
import com.ecquaria.cloud.moh.iais.common.exception.IaisRuntimeException;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Maps;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;
import sg.gov.moh.iais.egp.bsb.client.ApprovalBatAndActivityClient;
import sg.gov.moh.iais.egp.bsb.client.BsbFileClient;
import sg.gov.moh.iais.egp.bsb.client.FileRepoClient;
import sg.gov.moh.iais.egp.bsb.common.node.Node;
import sg.gov.moh.iais.egp.bsb.common.node.NodeGroup;
import sg.gov.moh.iais.egp.bsb.common.node.Nodes;
import sg.gov.moh.iais.egp.bsb.common.node.simple.SimpleNode;
import sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants;
import sg.gov.moh.iais.egp.bsb.dto.ResponseDto;
import sg.gov.moh.iais.egp.bsb.dto.file.DocRecordInfo;
import sg.gov.moh.iais.egp.bsb.dto.file.FileRepoSyncDto;
import sg.gov.moh.iais.egp.bsb.dto.file.NewDocInfo;
import sg.gov.moh.iais.egp.bsb.dto.file.NewFileSyncDto;
import sg.gov.moh.iais.egp.bsb.dto.info.bat.BatBasicInfo;
import sg.gov.moh.iais.egp.bsb.dto.register.approval.*;
import sg.gov.moh.iais.egp.bsb.util.mastercode.MasterCodeHolder;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static sg.gov.moh.iais.egp.bsb.constant.module.ApprovalBatAndActivityConstants.*;

/**
 * @author : LiRan
 * @date : 2022/3/17
 */
@Service
@Slf4j
public class ApprovalBatAndActivityService {
    private final ApprovalBatAndActivityClient approvalBatAndActivityClient;
    private final DocSettingService docSettingService;
    private final FileRepoClient fileRepoClient;
    private final BsbFileClient bsbFileClient;

    @Autowired
    public ApprovalBatAndActivityService(ApprovalBatAndActivityClient approvalBatAndActivityClient, DocSettingService docSettingService,
                                         FileRepoClient fileRepoClient, BsbFileClient bsbFileClient) {
        this.approvalBatAndActivityClient = approvalBatAndActivityClient;
        this.docSettingService = docSettingService;
        this.fileRepoClient = fileRepoClient;
        this.bsbFileClient = bsbFileClient;
    }

    public ApprovalBatAndActivityDto getEditDtoData(String appId) {
        ResponseDto<ApprovalBatAndActivityDto> responseDto = approvalBatAndActivityClient.getApprovalAppAppDataByApplicationId(appId);
        if (responseDto.ok()) {
            return responseDto.getEntity();
        } else {
            throw new IaisRuntimeException("Fail to retrieve edit approval app data");
        }
    }

    public ApprovalBatAndActivityDto getApprovalBatAndActivityDto(HttpServletRequest request) {
        ApprovalBatAndActivityDto approvalBatAndActivityDto = (ApprovalBatAndActivityDto) ParamUtil.getSessionAttr(request, KEY_APPROVAL_BAT_AND_ACTIVITY_DTO);
        if (approvalBatAndActivityDto == null) {
            approvalBatAndActivityDto = new ApprovalBatAndActivityDto();
        }
        return approvalBatAndActivityDto;
    }

    public FacProfileDto getFacProfileDtoByFacilityId(String facId) {
        ResponseDto<FacProfileDto> responseDto = approvalBatAndActivityClient.getFacProfileDtoByFacilityId(facId);
        if (responseDto.ok()) {
            return responseDto.getEntity();
        } else {
            throw new IaisRuntimeException("Fail to query facProfileDto data by facilityId");
        }
    }

    public NodeGroup getApprovalActivityRoot(HttpServletRequest request, String processType) {
        NodeGroup root = (NodeGroup) ParamUtil.getSessionAttr(request, KEY_ROOT_NODE_GROUP);
        if (root == null && org.springframework.util.StringUtils.hasLength(processType)) {
            root = newApprovalAppRoot(KEY_ROOT_NODE_GROUP, processType);
        }
        return root;
    }

    public NodeGroup newApprovalAppRoot(String name, String processType) {
        NodeGroup appInfoNodeGroup = null;
        switch (processType) {
            case MasterCodeConstants.PROCESS_TYPE_APPROVE_POSSESS:
                appInfoNodeGroup = newApprovalPossessAppInfoNodeGroup(new Node[0]);
                break;
            case MasterCodeConstants.PROCESS_TYPE_APPROVE_LSP:
                appInfoNodeGroup = newApprovalLargeAppInfoNodeGroup(new Node[0]);
                break;
            case MasterCodeConstants.PROCESS_TYPE_SP_APPROVE_HANDLE:
                appInfoNodeGroup = newApprovalSpecialAppInfoNodeGroup(new Node[0]);
                break;
            case MasterCodeConstants.PROCESS_TYPE_APPROVAL_FOR_FACILITY_ACTIVITY_TYPE:
                appInfoNodeGroup = newApprovalActivityAppInfoNodeGroup(new Node[0]);
                break;
            default:
                log.info(StringUtil.changeForLog("no such processType " + StringUtils.normalizeSpace(processType)));
                break;
        }
        if (appInfoNodeGroup == null) {
            return null;
        }
        SimpleNode primaryDocNode = new SimpleNode(new PrimaryDocDto(), NODE_NAME_PRIMARY_DOC, new Node[]{appInfoNodeGroup});
        SimpleNode previewNode = new SimpleNode(new PreviewDto(), NODE_NAME_PREVIEW, new Node[]{appInfoNodeGroup, primaryDocNode});

        return new NodeGroup.Builder().name(name)
                .addNode(appInfoNodeGroup)
                .addNode(primaryDocNode)
                .addNode(previewNode)
                .build();
    }

    private NodeGroup newApprovalPossessAppInfoNodeGroup(Node[] dependNodes) {
        Node facProfileNode = new Node(NODE_NAME_FAC_PROFILE, new Node[0]);
        SimpleNode possessBatNode = new SimpleNode(new ApprovalToPossessDto(), NODE_NAME_POSSESS_BAT, new Node[]{facProfileNode});

        return new NodeGroup.Builder().name(NODE_NAME_APP_INFO)
                .dependNodes(dependNodes)
                .addNode(facProfileNode)
                .addNode(possessBatNode)
                .build();
    }

    private NodeGroup newApprovalLargeAppInfoNodeGroup(Node[] dependNodes) {
        Node facProfileNode = new Node(NODE_NAME_FAC_PROFILE, new Node[0]);
        SimpleNode largeBatNode = new SimpleNode(new ApprovalToLargeDto(), NODE_NAME_LARGE_BAT, new Node[]{facProfileNode});

        return new NodeGroup.Builder().name(NODE_NAME_APP_INFO)
                .dependNodes(dependNodes)
                .addNode(facProfileNode)
                .addNode(largeBatNode)
                .build();
    }

    private NodeGroup newApprovalSpecialAppInfoNodeGroup(Node[] dependNodes) {
        Node facProfileNode = new Node(NODE_NAME_FAC_PROFILE, new Node[0]);
        SimpleNode specialBatNode = new SimpleNode(new ApprovalToSpecialDto(), NODE_NAME_SPECIAL_BAT, new Node[]{facProfileNode});
        SimpleNode facAuthorisedNode = new SimpleNode(new FacAuthorisedDto(), NODE_NAME_FAC_AUTHORISED, new Node[]{facProfileNode, specialBatNode});

        return new NodeGroup.Builder().name(NODE_NAME_APP_INFO)
                .dependNodes(dependNodes)
                .addNode(facProfileNode)
                .addNode(specialBatNode)
                .addNode(facAuthorisedNode)
                .build();
    }

    private NodeGroup newApprovalActivityAppInfoNodeGroup(Node[] dependNodes) {
        Node facProfileNode = new Node(NODE_NAME_FAC_PROFILE, new Node[0]);
        SimpleNode facActivityNode = new SimpleNode(new ApprovalToActivityDto(), NODE_NAME_FAC_ACTIVITY, new Node[]{facProfileNode});

        return new NodeGroup.Builder().name(NODE_NAME_APP_INFO)
                .dependNodes(dependNodes)
                .addNode(facProfileNode)
                .addNode(facActivityNode)
                .build();
    }

    public void saveDraft(HttpServletRequest request){

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
     * @param group the node group of the approvalAppRoot
     */
    public static boolean ifNodeSelected(NodeGroup group, String name) {
        Assert.notNull(group, ERR_MSG_NULL_GROUP);
        Assert.notNull(name, ERR_MSG_NULL_NAME);
        return name.equals(group.getActiveNodeKey());
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

    /**
     * @param group the node group contains the sub steps
     */
    public static String computeStepClassname(NodeGroup group, String name) {
        Assert.notNull(group, ERR_MSG_NULL_GROUP);
        Assert.notNull(name, ERR_MSG_NULL_NAME);
        return group.getNode(name).isValidated() || name.equals(group.getActiveNodeKey()) ? "active" : "disabled";
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

    /** Save new uploaded documents into FE file repo.
     * @param primaryDocDto document DTO have the specific structure
     * @return a list of DTOs can be used to sync to BE
     */
    public List<NewFileSyncDto> saveNewUploadedDoc(PrimaryDocDto primaryDocDto) {
        List<NewFileSyncDto> newFilesToSync;
        if (!primaryDocDto.getNewDocMap().isEmpty()) {
            MultipartFile[] files = primaryDocDto.getNewDocMap().values().stream().map(NewDocInfo::getMultipartFile).toArray(MultipartFile[]::new);
            List<String> repoIds = fileRepoClient.saveFiles(files).getEntity();
            newFilesToSync = primaryDocDto.newFileSaved(repoIds);
        } else {
            newFilesToSync = new ArrayList<>(0);
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

    public void preparePreviewData(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        NodeGroup approvalAppRoot = getApprovalActivityRoot(request, null);
        SimpleNode previewNode = (SimpleNode) approvalAppRoot.at(NODE_NAME_PREVIEW);
        PreviewDto previewDto = (PreviewDto) previewNode.getValue();
        Boolean needShowError = (Boolean) ParamUtil.getRequestAttr(request, KEY_SHOW_ERROR_SWITCH);
        if (needShowError == Boolean.TRUE) {
            ParamUtil.setRequestAttr(request, KEY_VALIDATION_ERRORS, previewDto.retrieveValidationResult());
        }
        Nodes.needValidation(approvalAppRoot, NODE_NAME_PREVIEW);
        ParamUtil.setRequestAttr(request, NODE_NAME_PREVIEW, previewDto);

        FacProfileDto facProfileDto = getApprovalBatAndActivityDto(request).getFacProfileDto();
        ParamUtil.setRequestAttr(request, KEY_FAC_PROFILE_DTO, facProfileDto);

        String processType = (String) ParamUtil.getSessionAttr(request, KEY_PROCESS_TYPE);
        switch (processType) {
            case MasterCodeConstants.PROCESS_TYPE_APPROVE_POSSESS:
                ParamUtil.setRequestAttr(request, KEY_BAT_INFO, ((SimpleNode)approvalAppRoot.at(NODE_NAME_APP_INFO + approvalAppRoot.getPathSeparator() + NODE_NAME_POSSESS_BAT)).getValue());
                break;
            case MasterCodeConstants.PROCESS_TYPE_APPROVE_LSP:
                ParamUtil.setRequestAttr(request, KEY_BAT_INFO, ((SimpleNode)approvalAppRoot.at(NODE_NAME_APP_INFO + approvalAppRoot.getPathSeparator() + NODE_NAME_LARGE_BAT)).getValue());
                break;
            case MasterCodeConstants.PROCESS_TYPE_SP_APPROVE_HANDLE:
                ParamUtil.setRequestAttr(request, KEY_BAT_INFO, ((SimpleNode)approvalAppRoot.at(NODE_NAME_APP_INFO + approvalAppRoot.getPathSeparator() + NODE_NAME_SPECIAL_BAT)).getValue());
                ParamUtil.setRequestAttr(request, KEY_FAC_AUTHORISED_DTO, ((SimpleNode)approvalAppRoot.at(NODE_NAME_APP_INFO + approvalAppRoot.getPathSeparator() + NODE_NAME_FAC_AUTHORISED)).getValue());
                break;
            case MasterCodeConstants.PROCESS_TYPE_APPROVAL_FOR_FACILITY_ACTIVITY_TYPE:
                ParamUtil.setRequestAttr(request, KEY_APPROVAL_TO_ACTIVITY_DTO, ((SimpleNode)approvalAppRoot.at(NODE_NAME_APP_INFO + approvalAppRoot.getPathSeparator() + NODE_NAME_FAC_ACTIVITY)).getValue());
                break;
            default:
                log.info("no such processType {}", StringUtils.normalizeSpace(processType));
                break;
        }

        ParamUtil.setRequestAttr(request, "docSettings", docSettingService.getApprovalAppDocSettings(processType));
        PrimaryDocDto primaryDocDto = (PrimaryDocDto) ((SimpleNode)approvalAppRoot.at(NODE_NAME_PRIMARY_DOC)).getValue();
        Map<String, List<DocRecordInfo>> savedFiles = primaryDocDto.getExistDocTypeMap();
        Map<String, List<NewDocInfo>> newFiles = primaryDocDto.getNewDocTypeMap();
        ParamUtil.setRequestAttr(request, "savedFiles", savedFiles);
        ParamUtil.setRequestAttr(request, "newFiles", newFiles);
    }


    @SneakyThrows(JsonProcessingException.class)
    public void loadAllowedScheduleAndBatOptions(HttpServletRequest request, String activityType) {
        Map<String, List<BatBasicInfo>> scheduleBatMap = approvalBatAndActivityClient.queryScheduleBasedBatBasicInfo(activityType);
        List<SelectOption> scheduleTypeOps = MasterCodeHolder.SCHEDULE.customOptions(scheduleBatMap.keySet().toArray(new String[0]));
        ParamUtil.setRequestAttr(request, KEY_OPTIONS_SCHEDULE, scheduleTypeOps);
        ParamUtil.setRequestAttr(request, KEY_SCHEDULE_FIRST_OPTION, scheduleTypeOps.get(0).getValue());

        // convert BatBasicInfo to SelectOption object
        SelectOption pleaseSelect = new SelectOption("", "Please Select");
        Map<String, List<SelectOption>> scheduleBatOptionMap = Maps.newHashMapWithExpectedSize(scheduleBatMap.size());
        for (Map.Entry<String, List<BatBasicInfo>> entry : scheduleBatMap.entrySet()) {
            List<SelectOption> optionList = new ArrayList<>(entry.getValue().size());
            optionList.add(pleaseSelect);
            for (BatBasicInfo info : entry.getValue()) {
                SelectOption option = new SelectOption();
                option.setText(info.getName());
                option.setValue(info.getId());
                optionList.add(option);
            }
            scheduleBatOptionMap.put(entry.getKey(), optionList);
        }
        ParamUtil.setRequestAttr(request, KEY_SCHEDULE_BAT_MAP, scheduleBatOptionMap);
        ObjectMapper mapper = new ObjectMapper();
        String scheduleBatMapJson = mapper.writeValueAsString(scheduleBatOptionMap);
        ParamUtil.setRequestAttr(request, KEY_SCHEDULE_BAT_MAP_JSON, scheduleBatMapJson);
    }
}

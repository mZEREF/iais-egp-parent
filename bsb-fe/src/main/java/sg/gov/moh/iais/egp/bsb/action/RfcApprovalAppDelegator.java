package sg.gov.moh.iais.egp.bsb.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.filerepo.FileRepoDto;
import com.ecquaria.cloud.moh.iais.common.exception.IaisRuntimeException;
import com.ecquaria.cloud.moh.iais.common.utils.LogUtil;
import com.ecquaria.cloud.moh.iais.common.utils.MaskUtil;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
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
import sg.gov.moh.iais.egp.bsb.common.rfc.DecisionFlowType;
import sg.gov.moh.iais.egp.bsb.common.rfc.DecisionFlowTypeImpl;
import sg.gov.moh.iais.egp.bsb.constant.RfcFlowType;
import sg.gov.moh.iais.egp.bsb.dto.ResponseDto;
import sg.gov.moh.iais.egp.bsb.dto.approval.*;
import sg.gov.moh.iais.egp.bsb.dto.file.DocRecordInfo;
import sg.gov.moh.iais.egp.bsb.dto.file.FileRepoSyncDto;
import sg.gov.moh.iais.egp.bsb.dto.file.NewDocInfo;
import sg.gov.moh.iais.egp.bsb.dto.file.NewFileSyncDto;
import sg.gov.moh.iais.egp.bsb.entity.Biological;
import sg.gov.moh.iais.egp.bsb.entity.Facility;
import sg.gov.moh.iais.egp.bsb.entity.FacilityActivity;
import sg.gov.moh.iais.egp.bsb.service.ApprovalAppService;
import sg.gov.moh.iais.egp.bsb.util.CollectionUtils;
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
@Delegator("rfcApprovalAppDelegator")
public class RfcApprovalAppDelegator {
    public static final String MODULE_NAME = "Rfc Approval Application";

    private final ApprovalAppClient approvalAppClient;
    private final FileRepoClient fileRepoClient;
    private final BsbFileClient bsbFileClient;
    private final ApprovalAppService approvalAppService;

    @Autowired
    public RfcApprovalAppDelegator(ApprovalAppClient approvalAppClient, FileRepoClient fileRepoClient,
                                   BsbFileClient bsbFileClient, ApprovalAppService approvalAppService) {
        this.approvalAppClient = approvalAppClient;
        this.fileRepoClient = fileRepoClient;
        this.bsbFileClient = bsbFileClient;
        this.approvalAppService = approvalAppService;
    }

    public void start(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        request.getSession().removeAttribute(KEY_PROCESS_TYPE);
        request.getSession().removeAttribute(KEY_ROOT_NODE_GROUP);
        request.getSession().removeAttribute(KEY_OLD_APPROVAL_APP_DTO);
        AuditTrailHelper.auditFunction(MODULE_NAME, MODULE_NAME);
    }

    public void init(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        // check if we are doing editing
        String maskedAppId = request.getParameter(KEY_EDIT_APP_ID);
        if (StringUtils.hasLength(maskedAppId)) {
            if (log.isInfoEnabled()) {
                log.info("masked app ID: {}", LogUtil.escapeCrlf(maskedAppId));
            }
            boolean failRetrieveEditData = true;
            String appId = MaskUtil.unMaskValue(KEY_EDIT_APP_ID, maskedAppId);
            if (appId != null && !maskedAppId.equals(appId)) {
                ResponseDto<ApprovalAppDto> resultDto = approvalAppClient.getApprovalAppAppDataByApprovalId(appId);
                if (resultDto.ok()) {
                    failRetrieveEditData = false;
                    //This oldApprovalAppDto is the original data, which is for comparison with each DTO modification before Submit(unchangeable)
                    ApprovalAppDto oldApprovalAppDto = resultDto.getEntity();
                    ParamUtil.setSessionAttr(request, KEY_OLD_APPROVAL_APP_DTO, oldApprovalAppDto);

                    NodeGroup approvalAppRoot = oldApprovalAppDto.toApprovalAppRootGroup(KEY_ROOT_NODE_GROUP);
                    ParamUtil.setSessionAttr(request, KEY_ROOT_NODE_GROUP, approvalAppRoot);
                }
            }
            if (failRetrieveEditData) {
                throw new IaisRuntimeException("Fail to retrieve app data");
            }
        }
        String maskedProcessType = request.getParameter(KEY_PROCESS_TYPE);
        String processType = MaskUtil.unMaskValue(KEY_PROCESS_TYPE,maskedProcessType);
        ParamUtil.setSessionAttr(request, KEY_PROCESS_TYPE, processType);
    }

    public void preCompInfo(BaseProcessClass bpc){
        // do nothing now, need to prepare company info in the future
    }

    public void handleCompInfo(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        NodeGroup approvalAppRoot = (NodeGroup) ParamUtil.getSessionAttr(request, KEY_ROOT_NODE_GROUP);
        Node compInfoNode = approvalAppRoot.getNode(NODE_NAME_COMPANY_INFO);
        String actionType = ParamUtil.getString(request, KEY_ACTION_TYPE);
        if (KEY_ACTION_JUMP.equals(actionType)) {
            approvalAppService.jumpHandler(request, approvalAppRoot, NODE_NAME_COMPANY_INFO, compInfoNode);
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

        NodeGroup approvalAppRoot = (NodeGroup) ParamUtil.getSessionAttr(request, KEY_ROOT_NODE_GROUP);
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
        NodeGroup approvalAppRoot = (NodeGroup) ParamUtil.getSessionAttr(request, KEY_ROOT_NODE_GROUP);
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
            ApprovalAppService.changeApprovalProfileNodeGroup(approvalProfileGroup, activityDto);
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
            approvalAppService.jumpHandler(request, approvalAppRoot, NODE_NAME_ACTIVITY, activityNode);
        } else {
            throw new IaisRuntimeException(ERR_MSG_INVALID_ACTION);
        }
        ParamUtil.setSessionAttr(request, KEY_ROOT_NODE_GROUP, approvalAppRoot);
    }

    public void prepareApprovalProfile(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        NodeGroup approvalAppRoot = (NodeGroup) ParamUtil.getSessionAttr(request, KEY_ROOT_NODE_GROUP);
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
        ParamUtil.setRequestAttr(request, KEY_COUNTRY_OPTIONS, ApprovalAppService.tmpCountryOps());
    }

    public void handleApprovalProfile(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        NodeGroup approvalAppRoot = (NodeGroup) ParamUtil.getSessionAttr(request, KEY_ROOT_NODE_GROUP);
        String currentNodePath = (String) ParamUtil.getSessionAttr(request, KEY_JUMP_DEST_NODE);
        SimpleNode approvalProfileNode = (SimpleNode) approvalAppRoot.at(currentNodePath);
        ApprovalProfileDto approvalProfileDto = (ApprovalProfileDto) approvalProfileNode.getValue();
        approvalProfileDto.reqObjMapping(request);

        String actionType = ParamUtil.getString(request, KEY_ACTION_TYPE);
        if (KEY_ACTION_JUMP.equals(actionType)) {
            approvalAppService.jumpHandler(request, approvalAppRoot, currentNodePath, approvalProfileNode);
        } else {
            throw new IaisRuntimeException(ERR_MSG_INVALID_ACTION);
        }
        ParamUtil.setSessionAttr(request, KEY_ROOT_NODE_GROUP, approvalAppRoot);
    }

    public void prePrimaryDoc(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        NodeGroup approvalAppRoot = (NodeGroup) ParamUtil.getSessionAttr(request, KEY_ROOT_NODE_GROUP);
        SimpleNode primaryDocNode = (SimpleNode) approvalAppRoot.at(NODE_NAME_PRIMARY_DOC);
        PrimaryDocDto primaryDocDto = (PrimaryDocDto) primaryDocNode.getValue();
        Boolean needShowError = (Boolean) ParamUtil.getRequestAttr(request, KEY_SHOW_ERROR_SWITCH);
        if (needShowError == Boolean.TRUE) {
            ParamUtil.setRequestAttr(request, KEY_VALIDATION_ERRORS, primaryDocDto.retrieveValidationResult());
        }
        Nodes.needValidation(approvalAppRoot, NODE_NAME_PRIMARY_DOC);

        ParamUtil.setRequestAttr(request, "docSettings", approvalAppService.getApprovalAppDocSettings());

        Map<String, List<DocRecordInfo>> savedFiles = primaryDocDto.getExistDocTypeMap();
        Map<String, List<NewDocInfo>> newFiles = primaryDocDto.getNewDocTypeMap();
        ParamUtil.setRequestAttr(request, "savedFiles", savedFiles);
        ParamUtil.setRequestAttr(request, "newFiles", newFiles);
    }

    public void handlePrimaryDoc(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        NodeGroup approvalAppRoot = (NodeGroup) ParamUtil.getSessionAttr(request, KEY_ROOT_NODE_GROUP);
        SimpleNode primaryDocNode = (SimpleNode) approvalAppRoot.at(NODE_NAME_PRIMARY_DOC);
        PrimaryDocDto primaryDocDto = (PrimaryDocDto) primaryDocNode.getValue();
        primaryDocDto.reqObjMapping(request);

        String actionType = ParamUtil.getString(request, KEY_ACTION_TYPE);
        if (KEY_ACTION_JUMP.equals(actionType)) {
            approvalAppService.jumpHandler(request, approvalAppRoot, NODE_NAME_PRIMARY_DOC, primaryDocNode);
        } else {
            throw new IaisRuntimeException(ERR_MSG_INVALID_ACTION);
        }
        ParamUtil.setSessionAttr(request, KEY_ROOT_NODE_GROUP, approvalAppRoot);
    }

    public void prePreviewSubmit(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        NodeGroup approvalAppRoot = (NodeGroup) ParamUtil.getSessionAttr(request, KEY_ROOT_NODE_GROUP);

        NodeGroup approvalProfileGroup = (NodeGroup) approvalAppRoot.at(NODE_NAME_APPROVAL_PROFILE);
        List<ApprovalProfileDto> batList = ApprovalAppService.getApprovalProfileList(approvalProfileGroup);
        ParamUtil.setRequestAttr(request, "approvalProfileList", batList);

        ParamUtil.setRequestAttr(request, "docSettings", approvalAppService.getApprovalAppDocSettings());
        PrimaryDocDto primaryDocDto = (PrimaryDocDto) ((SimpleNode)approvalAppRoot.at(NODE_NAME_PRIMARY_DOC)).getValue();
        Map<String, List<DocRecordInfo>> savedFiles = primaryDocDto.getExistDocTypeMap();
        Map<String, List<NewDocInfo>> newFiles = primaryDocDto.getNewDocTypeMap();
        ParamUtil.setRequestAttr(request, "savedFiles", savedFiles);
        ParamUtil.setRequestAttr(request, "newFiles", newFiles);
    }

    public void handlePreviewSubmit(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        NodeGroup approvalAppRoot = (NodeGroup) ParamUtil.getSessionAttr(request, KEY_ROOT_NODE_GROUP);
        Node previewSubmitNode = approvalAppRoot.at(NODE_NAME_PREVIEW_SUBMIT);

        String actionType = ParamUtil.getString(request, KEY_ACTION_TYPE);
        String actionValue = ParamUtil.getString(request, KEY_ACTION_VALUE);
        if (KEY_ACTION_JUMP.equals(actionType)) {
            if (KEY_NAV_NEXT.equals(actionValue)) {
                ApprovalAppDto finalAllDataDto = ApprovalAppDto.from(approvalAppRoot);
                //rfc compare to decision flowType
                ApprovalAppDto oldApprovalAppDto = (ApprovalAppDto)ParamUtil.getSessionAttr(request,KEY_OLD_APPROVAL_APP_DTO);
                DecisionFlowType flowType = new DecisionFlowTypeImpl();
                RfcFlowType rfcFlowType = flowType.approvalAppFlowType(approvalAppService.compareTwoDto(oldApprovalAppDto,finalAllDataDto));
                ParamUtil.setRequestAttr(request, "rfcFlowType", rfcFlowType);
                if (rfcFlowType == RfcFlowType.AMENDMENT){
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
                    ResponseDto<String> responseDto = approvalAppClient.saveAmendmentApprovalApp(finalAllDataDto);
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
                }
                ParamUtil.setRequestAttr(request, KEY_ACTION_TYPE, KEY_ACTION_SUBMIT);
            } else {
                approvalAppService.jumpHandler(request, approvalAppRoot, NODE_NAME_PREVIEW_SUBMIT, previewSubmitNode);
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
        destNode = approvalAppService.approvalProfileNodeSpecialHandle(destNode);
        ParamUtil.setRequestAttr(request, KEY_DEST_NODE_ROUTE, destNode);
    }

    public void doSaveDraft(BaseProcessClass bpc){
        // do nothing now
    }

    public void doSubmit(BaseProcessClass bpc){
        // do nothing now
    }
}

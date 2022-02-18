package sg.gov.moh.iais.egp.bsb.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.dto.filerepo.FileRepoDto;
import com.ecquaria.cloud.moh.iais.common.exception.IaisRuntimeException;
import com.ecquaria.cloud.moh.iais.common.utils.LogUtil;
import com.ecquaria.cloud.moh.iais.common.utils.MaskUtil;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import sg.gov.moh.iais.egp.bsb.client.ApprovalAppClient;
import sg.gov.moh.iais.egp.bsb.client.BsbFileClient;
import sg.gov.moh.iais.egp.bsb.client.FileRepoClient;
import sg.gov.moh.iais.egp.bsb.common.node.Node;
import sg.gov.moh.iais.egp.bsb.common.node.NodeGroup;
import sg.gov.moh.iais.egp.bsb.common.node.simple.SimpleNode;
import sg.gov.moh.iais.egp.bsb.common.rfc.DecisionFlowType;
import sg.gov.moh.iais.egp.bsb.common.rfc.DecisionFlowTypeImpl;
import sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants;
import sg.gov.moh.iais.egp.bsb.constant.RfcFlowType;
import sg.gov.moh.iais.egp.bsb.dto.ResponseDto;
import sg.gov.moh.iais.egp.bsb.dto.approval.*;
import sg.gov.moh.iais.egp.bsb.dto.file.FileRepoSyncDto;
import sg.gov.moh.iais.egp.bsb.dto.file.NewDocInfo;
import sg.gov.moh.iais.egp.bsb.dto.file.NewFileSyncDto;
import sg.gov.moh.iais.egp.bsb.service.ApprovalAppService;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

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
        approvalAppService.preCompInfo(bpc);
    }

    public void handleCompInfo(BaseProcessClass bpc){
        approvalAppService.handleCompInfo(bpc);
    }

    public void prepareActivity(BaseProcessClass bpc){
        approvalAppService.prepareActivity(bpc);
    }

    public void handleActivity(BaseProcessClass bpc){
        approvalAppService.handleActivity(bpc);
    }

    public void prepareApprovalProfile(BaseProcessClass bpc){
        approvalAppService.prepareApprovalProfile(bpc);
    }

    public void handleApprovalProfile(BaseProcessClass bpc){
        approvalAppService.handleApprovalProfile(bpc);
    }

    public void prePrimaryDoc(BaseProcessClass bpc){
        approvalAppService.prePrimaryDoc(bpc);
    }

    public void handlePrimaryDoc(BaseProcessClass bpc){
        approvalAppService.handlePrimaryDoc(bpc);
    }

    public void prePreviewSubmit(BaseProcessClass bpc) {
        approvalAppService.prePreviewSubmit(bpc);
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
        approvalAppService.actionFilter(bpc, MasterCodeConstants.APP_TYPE_RFC);
    }

    public void jumpFilter(BaseProcessClass bpc){
        approvalAppService.jumpFilter(bpc);
    }

    public void preAcknowledge(BaseProcessClass bpc){
        approvalAppService.preAcknowledge(bpc);
    }
}

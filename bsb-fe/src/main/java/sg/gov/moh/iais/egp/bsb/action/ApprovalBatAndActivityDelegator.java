package sg.gov.moh.iais.egp.bsb.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.exception.IaisRuntimeException;
import com.ecquaria.cloud.moh.iais.common.utils.LogUtil;
import com.ecquaria.cloud.moh.iais.common.utils.MaskUtil;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import sg.gov.moh.iais.egp.bsb.client.ApprovalBatAndActivityClient;
import sg.gov.moh.iais.egp.bsb.common.node.NodeGroup;
import sg.gov.moh.iais.egp.bsb.common.node.Nodes;
import sg.gov.moh.iais.egp.bsb.common.node.simple.SimpleNode;
import sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants;
import sg.gov.moh.iais.egp.bsb.dto.ResponseDto;
import sg.gov.moh.iais.egp.bsb.dto.file.NewFileSyncDto;
import sg.gov.moh.iais.egp.bsb.dto.info.common.AppMainInfo;
import sg.gov.moh.iais.egp.bsb.dto.register.approval.*;
import sg.gov.moh.iais.egp.bsb.service.ApprovalBatAndActivityService;

import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import java.util.List;

import static sg.gov.moh.iais.egp.bsb.constant.module.ApprovalBatAndActivityConstants.*;


@Slf4j
@Delegator("approvalBatAndActivityDelegator")
public class ApprovalBatAndActivityDelegator {

    private final ApprovalBatAndActivityClient approvalBatAndActivityClient;
    private final ApprovalBatAndActivityService approvalBatAndActivityService;

    @Autowired
    public ApprovalBatAndActivityDelegator(ApprovalBatAndActivityClient approvalBatAndActivityClient, ApprovalBatAndActivityService approvalBatAndActivityService) {
        this.approvalBatAndActivityClient = approvalBatAndActivityClient;
        this.approvalBatAndActivityService = approvalBatAndActivityService;
    }

    public void start(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        HttpSession session = request.getSession();
        session.removeAttribute(KEY_APPROVAL_SELECTION_DTO);
        session.removeAttribute(KEY_FAC_PROFILE_DTO);
        session.removeAttribute(KEY_ROOT_NODE_GROUP);
        session.removeAttribute(KEY_PROCESS_TYPE);
        session.removeAttribute(KEY_AUTH_PERSONNEL_DETAIL_MAP_JSON);
        session.removeAttribute(KEY_OPTIONS_AUTH_PERSONNEL);
        session.removeAttribute(KEY_USER_ID_FACILITY_AUTH_MAP);
        AuditTrailHelper.auditFunction("Application for Approval", "Application for Approval");
    }

    public void init(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        boolean newApprovalApp = true;
        // check if we are doing editing
        String maskedAppId = request.getParameter(KEY_EDIT_APP_ID);
        if (StringUtils.hasLength(maskedAppId)) {
            if (log.isInfoEnabled()) {
                log.info("masked app ID: {}", LogUtil.escapeCrlf(maskedAppId));
            }
            newApprovalApp = false;
            String appId = MaskUtil.unMaskValue(KEY_EDIT_APP_ID, maskedAppId);
            if (appId != null && !maskedAppId.equals(appId)) {
                ApprovalBatAndActivityDto editDto = approvalBatAndActivityService.getEditDtoData(appId);
                NodeGroup approvalAppRoot = editDto.toApprovalAppRootGroup(KEY_ROOT_NODE_GROUP);
                ParamUtil.setSessionAttr(request, KEY_ROOT_NODE_GROUP, approvalAppRoot);
                ParamUtil.setSessionAttr(request, KEY_APPROVAL_BAT_AND_ACTIVITY_DTO, editDto);
                // dash bord display
                ParamUtil.setSessionAttr(request, KEY_PROCESS_TYPE, editDto.getApprovalSelectionDto().getProcessType());
            }
        }
        if (newApprovalApp) {
            ParamUtil.setSessionAttr(request, KEY_APPROVAL_BAT_AND_ACTIVITY_DTO, new ApprovalBatAndActivityDto());
        }

        approvalBatAndActivityService.retrieveOrgAddressInfo(request);
    }

    public void preBegin(BaseProcessClass bpc){
        // do nothing now
    }

    public void handleBegin(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        ParamUtil.setSessionAttr(request, KEY_JUMP_DEST_NODE, NODE_NAME_APPROVAL_SELECTION);
    }

    public void preApprovalSelection(BaseProcessClass bpc){
        approvalBatAndActivityService.preApprovalSelection(bpc);
    }

    public void handleApprovalSelection(BaseProcessClass bpc){
        approvalBatAndActivityService.handleApprovalSelection(bpc);
    }

    public void preCompanyInfo(BaseProcessClass bpc){
        // do nothing now
    }

    public void handleCompanyInfo(BaseProcessClass bpc){
        approvalBatAndActivityService.handleCompanyInfo(bpc);
    }

    public void preFacProfile(BaseProcessClass bpc){
        approvalBatAndActivityService.preFacProfile(bpc);
    }

    public void handleFacProfile(BaseProcessClass bpc){
        approvalBatAndActivityService.handleFacProfile(bpc);
    }

    public void prePossessBatDetails(BaseProcessClass bpc){
        approvalBatAndActivityService.prePossessBatDetails(bpc);
    }

    public void handlePossessBatDetails(BaseProcessClass bpc){
        approvalBatAndActivityService.handlePossessBatDetails(bpc);
    }

    public void preLargeBatDetails(BaseProcessClass bpc){
        approvalBatAndActivityService.preLargeBatDetails(bpc);
    }

    public void handleLargeBatDetails(BaseProcessClass bpc){
        approvalBatAndActivityService.handleLargeBatDetails(bpc);
    }

    public void preSpecialBatDetails(BaseProcessClass bpc){
        approvalBatAndActivityService.preSpecialBatDetails(bpc);
    }

    public void handleSpecialBatDetails(BaseProcessClass bpc){
        approvalBatAndActivityService.handleSpecialBatDetails(bpc);
    }

    public void preFacAuthorised(BaseProcessClass bpc){
        approvalBatAndActivityService.preFacAuthorised(bpc);
    }

    public void handleFacAuthorised(BaseProcessClass bpc){
        approvalBatAndActivityService.handleFacAuthorised(bpc);
    }

    public void preActivityDetails(BaseProcessClass bpc){
        approvalBatAndActivityService.preActivityDetails(bpc);
    }

    public void handleActivityDetails(BaseProcessClass bpc){
        approvalBatAndActivityService.handleActivityDetails(bpc);
    }


    public void prePrimaryDoc(BaseProcessClass bpc){
        approvalBatAndActivityService.prePrimaryDoc(bpc);
    }

    public void handlePrimaryDoc(BaseProcessClass bpc){
        approvalBatAndActivityService.handlePrimaryDoc(bpc);
    }

    public void prePreview(BaseProcessClass bpc){
        approvalBatAndActivityService.preparePreviewData(bpc);
    }

    public void handlePreview(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        NodeGroup approvalAppRoot = approvalBatAndActivityService.getApprovalActivityRoot(request, null);
        SimpleNode previewNode = (SimpleNode) approvalAppRoot.at(NODE_NAME_PREVIEW);
        PreviewDto previewDto = (PreviewDto) previewNode.getValue();
        previewDto.reqObjMapping(request);

        String actionType = ParamUtil.getString(request, KEY_ACTION_TYPE);
        String actionValue = ParamUtil.getString(request, KEY_ACTION_VALUE);
        if (KEY_ACTION_JUMP.equals(actionType)) {
            if (KEY_NAV_NEXT.equals(actionValue)) {
                if (previewNode.doValidation()) {
                    previewNode.passValidation();

                    // save docs
                    log.info("Save documents into file-repo");
                    PrimaryDocDto primaryDocDto = (PrimaryDocDto) ((SimpleNode) approvalAppRoot.at(NODE_NAME_PRIMARY_DOC)).getValue();
                    List<NewFileSyncDto> newFilesToSync = approvalBatAndActivityService.saveNewUploadedDoc(primaryDocDto);

                    log.info("Save approval application data");
                    ApprovalSelectionDto approvalSelectionDto = approvalBatAndActivityService.getApprovalSelectionDto(request);
                    ApprovalBatAndActivityDto approvalBatAndActivityDto = ApprovalBatAndActivityDto.from(approvalSelectionDto,approvalAppRoot);
                    ResponseDto<AppMainInfo> responseDto = approvalBatAndActivityClient.saveNewApplicationToApproval(approvalBatAndActivityDto);
                    log.info("save new application to approval response: {}", org.apache.commons.lang.StringUtils.normalizeSpace(responseDto.toString()));
                    AppMainInfo appMainInfo = responseDto.getEntity();
                    ParamUtil.setRequestAttr(request, KEY_APP_NO, appMainInfo.getAppNo());
                    ParamUtil.setRequestAttr(request, KEY_APP_DT, appMainInfo.getDate());

                    try {
                        // delete docs
                        log.info("Delete already saved documents in file-repo");
                        List<String> toBeDeletedRepoIds = approvalBatAndActivityService.deleteUnwantedDoc(primaryDocDto);
                        // sync docs
                        approvalBatAndActivityService.syncNewDocsAndDeleteFiles(newFilesToSync, toBeDeletedRepoIds);
                    } catch (Exception e) {
                        log.error("Fail to synchronize documents", e);
                    }

                    ParamUtil.setRequestAttr(request, KEY_ACTION_TYPE, KEY_ACTION_SUBMIT);
                } else {
                    ParamUtil.setRequestAttr(request, KEY_SHOW_ERROR_SWITCH, Boolean.TRUE);
                    ParamUtil.setSessionAttr(request, KEY_JUMP_DEST_NODE, NODE_NAME_PREVIEW);
                }
            } else {
                approvalBatAndActivityService.jumpHandler(request, approvalAppRoot, NODE_NAME_PREVIEW, previewNode);
            }
        } else if (KEY_ACTION_SAVE_AS_DRAFT.equals(actionType)) {
            ParamUtil.setRequestAttr(request, KEY_ACTION_TYPE, KEY_ACTION_SAVE_AS_DRAFT);
            ParamUtil.setSessionAttr(request, KEY_JUMP_DEST_NODE, NODE_NAME_PREVIEW);
        } else {
            throw new IaisRuntimeException(ERR_MSG_INVALID_ACTION);
        }
        ParamUtil.setSessionAttr(request, KEY_ROOT_NODE_GROUP, approvalAppRoot);
    }

    public void actionFilter(BaseProcessClass bpc){
        approvalBatAndActivityService.actionFilter(bpc,MasterCodeConstants.APP_TYPE_NEW);
    }

    public void jumpFilter(BaseProcessClass bpc){
        approvalBatAndActivityService.jumpFilter(bpc);
    }

    public void preAcknowledge(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
    }

    public void print(BaseProcessClass bpc){
        approvalBatAndActivityService.preparePreviewData(bpc);
    }

}

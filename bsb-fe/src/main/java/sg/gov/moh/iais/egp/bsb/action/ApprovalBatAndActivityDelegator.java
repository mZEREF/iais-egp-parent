package sg.gov.moh.iais.egp.bsb.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.exception.IaisRuntimeException;
import com.ecquaria.cloud.moh.iais.common.utils.LogUtil;
import com.ecquaria.cloud.moh.iais.common.utils.MaskUtil;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import sg.gov.moh.iais.egp.bsb.client.ApprovalBatAndActivityClient;
import sg.gov.moh.iais.egp.bsb.common.edit.FieldAllEditableJudger;
import sg.gov.moh.iais.egp.bsb.common.node.NodeGroup;
import sg.gov.moh.iais.egp.bsb.common.node.simple.SimpleNode;
import sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants;
import sg.gov.moh.iais.egp.bsb.constant.ValidationConstants;
import sg.gov.moh.iais.egp.bsb.constant.module.ApprovalBatAndActivityConstants;
import sg.gov.moh.iais.egp.bsb.constant.module.SubmissionBlockConstants;
import sg.gov.moh.iais.egp.bsb.dto.ResponseDto;
import sg.gov.moh.iais.egp.bsb.dto.file.NewFileSyncDto;
import sg.gov.moh.iais.egp.bsb.dto.info.common.AppMainInfo;
import sg.gov.moh.iais.egp.bsb.dto.register.approval.ApprovalBatAndActivityDto;
import sg.gov.moh.iais.egp.bsb.dto.register.approval.ApprovalSelectionDto;
import sg.gov.moh.iais.egp.bsb.dto.register.approval.PreviewDto;
import sg.gov.moh.iais.egp.bsb.dto.register.approval.PrimaryDocDto;
import sg.gov.moh.iais.egp.bsb.dto.register.bat.BATInfo;
import sg.gov.moh.iais.egp.bsb.service.ApprovalBatAndActivityService;

import sg.gov.moh.iais.egp.bsb.service.OrganizationInfoService;
import sg.gov.moh.iais.egp.bsb.service.SubmissionBlockService;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.ecquaria.cloud.moh.iais.common.constant.BsbAuditTrailConstants.FUNCTION_APPROVAL_APPLICATION;
import static com.ecquaria.cloud.moh.iais.common.constant.BsbAuditTrailConstants.MODULE_NEW_APPLICATION;
import static sg.gov.moh.iais.egp.bsb.constant.module.ApprovalBatAndActivityConstants.DRAFT_APPROVAL_BAT_AND_ACTIVITY_DTO;
import static sg.gov.moh.iais.egp.bsb.constant.module.ApprovalBatAndActivityConstants.ERR_MSG_INVALID_ACTION;
import static sg.gov.moh.iais.egp.bsb.constant.module.ApprovalBatAndActivityConstants.HAVE_SUITABLE_DRAFT_DATA;
import static sg.gov.moh.iais.egp.bsb.constant.module.ApprovalBatAndActivityConstants.KEY_ACTION_JUMP;
import static sg.gov.moh.iais.egp.bsb.constant.module.ApprovalBatAndActivityConstants.KEY_ACTION_SAVE_AS_DRAFT;
import static sg.gov.moh.iais.egp.bsb.constant.module.ApprovalBatAndActivityConstants.KEY_ACTION_SUBMIT;
import static sg.gov.moh.iais.egp.bsb.constant.module.ApprovalBatAndActivityConstants.KEY_ACTION_TYPE;
import static sg.gov.moh.iais.egp.bsb.constant.module.ApprovalBatAndActivityConstants.KEY_ACTION_VALUE;
import static sg.gov.moh.iais.egp.bsb.constant.module.ApprovalBatAndActivityConstants.KEY_ALLOW_SAVE_DRAFT;
import static sg.gov.moh.iais.egp.bsb.constant.module.ApprovalBatAndActivityConstants.KEY_APPROVAL_BAT_AND_ACTIVITY_DTO;
import static sg.gov.moh.iais.egp.bsb.constant.module.ApprovalBatAndActivityConstants.KEY_APPROVAL_SELECTION_DTO;
import static sg.gov.moh.iais.egp.bsb.constant.module.ApprovalBatAndActivityConstants.KEY_APP_DT;
import static sg.gov.moh.iais.egp.bsb.constant.module.ApprovalBatAndActivityConstants.KEY_APP_NO;
import static sg.gov.moh.iais.egp.bsb.constant.module.ApprovalBatAndActivityConstants.KEY_AUTH_PERSON_LIST;
import static sg.gov.moh.iais.egp.bsb.constant.module.ApprovalBatAndActivityConstants.KEY_EDIT_APP_ID;
import static sg.gov.moh.iais.egp.bsb.constant.module.ApprovalBatAndActivityConstants.KEY_FACILITY_ID;
import static sg.gov.moh.iais.egp.bsb.constant.module.ApprovalBatAndActivityConstants.KEY_FAC_PROFILE_DTO;
import static sg.gov.moh.iais.egp.bsb.constant.module.ApprovalBatAndActivityConstants.KEY_JUMP_DEST_NODE;
import static sg.gov.moh.iais.egp.bsb.constant.module.ApprovalBatAndActivityConstants.KEY_NAV_NEXT;
import static sg.gov.moh.iais.egp.bsb.constant.module.ApprovalBatAndActivityConstants.KEY_PROCESS_TYPE;
import static sg.gov.moh.iais.egp.bsb.constant.module.ApprovalBatAndActivityConstants.KEY_ROOT_NODE_GROUP;
import static sg.gov.moh.iais.egp.bsb.constant.module.ApprovalBatAndActivityConstants.KEY_SHOW_ERROR_SWITCH;
import static sg.gov.moh.iais.egp.bsb.constant.module.ApprovalBatAndActivityConstants.NODE_NAME_PREVIEW;
import static sg.gov.moh.iais.egp.bsb.constant.module.ApprovalBatAndActivityConstants.NODE_NAME_PRIMARY_DOC;


@Slf4j
@Delegator("approvalBatAndActivityDelegator")
@RequiredArgsConstructor
public class ApprovalBatAndActivityDelegator {

    private final ApprovalBatAndActivityClient approvalBatAndActivityClient;
    private final ApprovalBatAndActivityService approvalBatAndActivityService;
    private final OrganizationInfoService organizationInfoService;
    private final SubmissionBlockService submissionBlockService;


    public void start(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        HttpSession session = request.getSession();
        session.removeAttribute(SubmissionBlockConstants.BLOCK_APPLY_APPROVAL);
        session.removeAttribute(KEY_APPROVAL_SELECTION_DTO);
        session.removeAttribute(KEY_FAC_PROFILE_DTO);
        session.removeAttribute(KEY_ROOT_NODE_GROUP);
        session.removeAttribute(KEY_PROCESS_TYPE);
        session.removeAttribute(KEY_AUTH_PERSON_LIST);

        session.removeAttribute(KEY_FACILITY_ID);
        session.removeAttribute(KEY_APPROVAL_BAT_AND_ACTIVITY_DTO);
        session.removeAttribute(DRAFT_APPROVAL_BAT_AND_ACTIVITY_DTO);
        session.removeAttribute(HAVE_SUITABLE_DRAFT_DATA);
        AuditTrailHelper.auditFunction(MODULE_NEW_APPLICATION, FUNCTION_APPROVAL_APPLICATION);
    }

    public void init(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        boolean editDraft = false;

        // check if we are doing editing
        String maskedAppId = request.getParameter(KEY_EDIT_APP_ID);
        if (StringUtils.hasLength(maskedAppId)) {
            if (log.isInfoEnabled()) {
                log.info("masked app ID: {}", LogUtil.escapeCrlf(maskedAppId));
            }
            editDraft = true;
            String appId = MaskUtil.unMaskValue(KEY_EDIT_APP_ID, maskedAppId);
            if (appId != null && !maskedAppId.equals(appId)) {
                ApprovalBatAndActivityDto editDto = approvalBatAndActivityService.getEditDtoData(appId);
                NodeGroup approvalAppRoot = editDto.toApprovalAppRootGroup(KEY_ROOT_NODE_GROUP, ValidationConstants.VALIDATION_PROFILE_NEW);
                ParamUtil.setSessionAttr(request, KEY_ROOT_NODE_GROUP, approvalAppRoot);
                ParamUtil.setSessionAttr(request, KEY_APPROVAL_BAT_AND_ACTIVITY_DTO, editDto);
                ParamUtil.setSessionAttr(request, KEY_APPROVAL_SELECTION_DTO, editDto.getApprovalSelectionDto());
                // dash bord display
                ParamUtil.setSessionAttr(request, KEY_PROCESS_TYPE, editDto.getApprovalSelectionDto().getProcessType());
            }
        }

        // allow saving draft for apply new, continue draft
        ParamUtil.setSessionAttr(request, KEY_ALLOW_SAVE_DRAFT, Boolean.TRUE);

        if (!editDraft) {
            ParamUtil.setSessionAttr(request, KEY_APPROVAL_BAT_AND_ACTIVITY_DTO, new ApprovalBatAndActivityDto());
        }

        organizationInfoService.retrieveOrgAddressInfo(request);
    }

    public void preApprovalSelection(BaseProcessClass bpc) {
        approvalBatAndActivityService.preApprovalSelection(bpc);
    }

    public void handleApprovalSelection(BaseProcessClass bpc) {
        approvalBatAndActivityService.handleApprovalSelection(bpc, ValidationConstants.VALIDATION_PROFILE_NEW);
    }

    public void preCompanyInfo(BaseProcessClass bpc) {
        // do nothing now
    }

    public void handleCompanyInfo(BaseProcessClass bpc) {
        approvalBatAndActivityService.handleCompanyInfo(bpc);
    }

    public void preFacProfile(BaseProcessClass bpc) {
        approvalBatAndActivityService.preFacProfile(bpc);
    }

    public void handleFacProfile(BaseProcessClass bpc) {
        approvalBatAndActivityService.handleFacProfile(bpc, MasterCodeConstants.APP_TYPE_NEW);
    }

    public void prePossessBatDetails(BaseProcessClass bpc) {
        approvalBatAndActivityService.prePossessBatDetails(bpc, null);
    }

    public void handlePossessBatDetails(BaseProcessClass bpc) {
        approvalBatAndActivityService.handlePossessBatDetails(bpc, new FieldAllEditableJudger(), MasterCodeConstants.APP_TYPE_NEW);
    }

    public void preLargeBatDetails(BaseProcessClass bpc) {
        approvalBatAndActivityService.preLargeBatDetails(bpc, null);
    }

    public void handleLargeBatDetails(BaseProcessClass bpc) {
        approvalBatAndActivityService.handleLargeBatDetails(bpc, new FieldAllEditableJudger(), MasterCodeConstants.APP_TYPE_NEW);
    }

    public void preSpecialBatDetails(BaseProcessClass bpc) {
        approvalBatAndActivityService.preSpecialBatDetails(bpc, null);
    }

    public void handleSpecialBatDetails(BaseProcessClass bpc) {
        approvalBatAndActivityService.handleSpecialBatDetails(bpc, new FieldAllEditableJudger(), MasterCodeConstants.APP_TYPE_NEW);
    }

    public void preFacAuthorised(BaseProcessClass bpc) {
        approvalBatAndActivityService.preFacAuthorised(bpc, null);
    }

    public void handleFacAuthorised(BaseProcessClass bpc) {
        approvalBatAndActivityService.handleFacAuthorised(bpc, new FieldAllEditableJudger(), MasterCodeConstants.APP_TYPE_NEW);
    }

    public void preActivityDetails(BaseProcessClass bpc) {
        approvalBatAndActivityService.preActivityDetails(bpc);
    }

    public void handleActivityDetails(BaseProcessClass bpc) {
        approvalBatAndActivityService.handleActivityDetails(bpc);
    }

    public void prePrimaryDoc(BaseProcessClass bpc) {
        approvalBatAndActivityService.prePrimaryDoc(bpc);
    }

    public void handlePrimaryDoc(BaseProcessClass bpc) {
        approvalBatAndActivityService.handlePrimaryDoc(bpc, MasterCodeConstants.APP_TYPE_NEW);
    }

    public void prePreview(BaseProcessClass bpc) {
        approvalBatAndActivityService.prePreview(bpc);
    }

    public void handlePreview(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        NodeGroup approvalAppRoot = approvalBatAndActivityService.getApprovalActivityRoot(request, null);
        String processType = (String) ParamUtil.getSessionAttr(request, ApprovalBatAndActivityConstants.KEY_PROCESS_TYPE);
        SimpleNode previewNode = (SimpleNode) approvalAppRoot.at(NODE_NAME_PREVIEW);
        PreviewDto previewDto = (PreviewDto) previewNode.getValue();
        previewDto.reqObjMapping(request);
        boolean isProcModeImport = (boolean) ParamUtil.getSessionAttr(request, "isProcModeImport");
        if (isProcModeImport) {
            previewDto.setHasProcModeImport(ValidationConstants.YES);
        } else {
            previewDto.setHasProcModeImport(ValidationConstants.NO);
        }
        request.getSession().removeAttribute("isProcModeImport");
        String actionType = ParamUtil.getString(request, KEY_ACTION_TYPE);
        String actionValue = ParamUtil.getString(request, KEY_ACTION_VALUE);
        if (KEY_ACTION_JUMP.equals(actionType)) {
            if (KEY_NAV_NEXT.equals(actionValue)) {
                if (previewNode.doValidation()) {
                    previewNode.passValidation();

                    // prevent double submission
                    submissionBlockService.blockSubmission(request, SubmissionBlockConstants.BLOCK_APPLY_APPROVAL);

                    // save docs
                    log.info("Save documents into file-repo");
                    PrimaryDocDto primaryDocDto = (PrimaryDocDto) ((SimpleNode) approvalAppRoot.at(NODE_NAME_PRIMARY_DOC)).getValue();
                    List<NewFileSyncDto> newFilesToSync = approvalBatAndActivityService.saveNewUploadedDoc(primaryDocDto);

                    log.info("Save approval application data");
                    ApprovalSelectionDto approvalSelectionDto = approvalBatAndActivityService.getApprovalSelectionDto(request);
                    ApprovalBatAndActivityDto approvalBatAndActivityDto = ApprovalBatAndActivityDto.from(approvalSelectionDto, approvalAppRoot, request);
                    ResponseDto<AppMainInfo> responseDto = approvalBatAndActivityClient.saveNewApplicationToApproval(approvalBatAndActivityDto);
                    log.info("save new application to approval response: {}", org.apache.commons.lang.StringUtils.normalizeSpace(responseDto.toString()));
                    AppMainInfo appMainInfo = responseDto.getEntity();
                    ParamUtil.setRequestAttr(request, KEY_APP_NO, appMainInfo.getAppNo());
                    ParamUtil.setRequestAttr(request, KEY_APP_DT, appMainInfo.getDate());
                    List<String> displayList;
                    switch (processType) {
                        case MasterCodeConstants.PROCESS_TYPE_APPROVE_POSSESS:
                            displayList = approvalBatAndActivityDto.getApprovalToPossessDto().getBatInfos().stream().map(BATInfo::getBatName).collect(Collectors.toList());
                            break;
                        case MasterCodeConstants.PROCESS_TYPE_APPROVE_LSP:
                            displayList = approvalBatAndActivityDto.getApprovalToLargeDto().getBatInfos().stream().map(BATInfo::getBatName).collect(Collectors.toList());
                            break;
                        case MasterCodeConstants.PROCESS_TYPE_SP_APPROVE_HANDLE:
                            displayList = new ArrayList<>();
                            displayList.add(approvalBatAndActivityDto.getApprovalToSpecialDto().getBatName());
                            break;
                        case MasterCodeConstants.PROCESS_TYPE_APPROVAL_FOR_FACILITY_ACTIVITY_TYPE:
                            displayList = approvalBatAndActivityDto.getApprovalToActivityDto().getFacActivityTypes();
                            break;
                        default:
                            displayList = new ArrayList<>();
                            break;
                    }
                    ParamUtil.setRequestAttr(request, "displayList", displayList);
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

    public void actionFilter(BaseProcessClass bpc) {
        approvalBatAndActivityService.actionFilter(bpc, MasterCodeConstants.APP_TYPE_NEW);
    }

    public void jumpFilter(BaseProcessClass bpc) {
        approvalBatAndActivityService.jumpFilter(bpc);
    }

    public void preAcknowledge(BaseProcessClass bpc) {
        approvalBatAndActivityService.preAcknowledge(bpc);
    }

    public void print(BaseProcessClass bpc) {
        approvalBatAndActivityService.print(bpc);
    }

}

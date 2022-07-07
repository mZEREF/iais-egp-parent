package sg.gov.moh.iais.egp.bsb.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.exception.IaisRuntimeException;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import lombok.extern.slf4j.Slf4j;
import sg.gov.moh.iais.egp.bsb.client.ApprovalBatAndActivityClient;
import sg.gov.moh.iais.egp.bsb.common.node.NodeGroup;
import sg.gov.moh.iais.egp.bsb.common.node.simple.SimpleNode;
import sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants;
import sg.gov.moh.iais.egp.bsb.constant.ValidationConstants;
import sg.gov.moh.iais.egp.bsb.constant.module.ApprovalBatAndActivityConstants;
import sg.gov.moh.iais.egp.bsb.dto.ResponseDto;
import sg.gov.moh.iais.egp.bsb.dto.file.NewFileSyncDto;
import sg.gov.moh.iais.egp.bsb.dto.info.common.AppMainInfo;
import sg.gov.moh.iais.egp.bsb.dto.register.approval.ApprovalBatAndActivityDto;
import sg.gov.moh.iais.egp.bsb.dto.register.approval.ApprovalSelectionDto;
import sg.gov.moh.iais.egp.bsb.dto.register.approval.PreviewDto;
import sg.gov.moh.iais.egp.bsb.dto.register.approval.PrimaryDocDto;
import sg.gov.moh.iais.egp.bsb.dto.register.bat.BATInfo;
import sg.gov.moh.iais.egp.bsb.service.ApprovalBatAndActivityService;
import sg.gov.moh.iais.egp.bsb.service.RfiService;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static sg.gov.moh.iais.egp.bsb.constant.module.ApprovalBatAndActivityConstants.DRAFT_APPROVAL_BAT_AND_ACTIVITY_DTO;
import static sg.gov.moh.iais.egp.bsb.constant.module.ApprovalBatAndActivityConstants.ERR_MSG_INVALID_ACTION;
import static sg.gov.moh.iais.egp.bsb.constant.module.ApprovalBatAndActivityConstants.HAVE_SUITABLE_DRAFT_DATA;
import static sg.gov.moh.iais.egp.bsb.constant.module.ApprovalBatAndActivityConstants.KEY_ACTION_JUMP;
import static sg.gov.moh.iais.egp.bsb.constant.module.ApprovalBatAndActivityConstants.KEY_ACTION_SAVE_AS_DRAFT;
import static sg.gov.moh.iais.egp.bsb.constant.module.ApprovalBatAndActivityConstants.KEY_ACTION_SUBMIT;
import static sg.gov.moh.iais.egp.bsb.constant.module.ApprovalBatAndActivityConstants.KEY_ACTION_TYPE;
import static sg.gov.moh.iais.egp.bsb.constant.module.ApprovalBatAndActivityConstants.KEY_ACTION_VALUE;
import static sg.gov.moh.iais.egp.bsb.constant.module.ApprovalBatAndActivityConstants.KEY_APPROVAL_BAT_AND_ACTIVITY_DTO;
import static sg.gov.moh.iais.egp.bsb.constant.module.ApprovalBatAndActivityConstants.KEY_APPROVAL_SELECTION_DTO;
import static sg.gov.moh.iais.egp.bsb.constant.module.ApprovalBatAndActivityConstants.KEY_APP_DT;
import static sg.gov.moh.iais.egp.bsb.constant.module.ApprovalBatAndActivityConstants.KEY_APP_NO;
import static sg.gov.moh.iais.egp.bsb.constant.module.ApprovalBatAndActivityConstants.KEY_AUTH_PERSONNEL_DETAIL_MAP_JSON;
import static sg.gov.moh.iais.egp.bsb.constant.module.ApprovalBatAndActivityConstants.KEY_FACILITY_ID;
import static sg.gov.moh.iais.egp.bsb.constant.module.ApprovalBatAndActivityConstants.KEY_FAC_PROFILE_DTO;
import static sg.gov.moh.iais.egp.bsb.constant.module.ApprovalBatAndActivityConstants.KEY_JUMP_DEST_NODE;
import static sg.gov.moh.iais.egp.bsb.constant.module.ApprovalBatAndActivityConstants.KEY_NAV_NEXT;
import static sg.gov.moh.iais.egp.bsb.constant.module.ApprovalBatAndActivityConstants.KEY_OPTIONS_AUTH_PERSONNEL;
import static sg.gov.moh.iais.egp.bsb.constant.module.ApprovalBatAndActivityConstants.KEY_PROCESS_TYPE;
import static sg.gov.moh.iais.egp.bsb.constant.module.ApprovalBatAndActivityConstants.KEY_ROOT_NODE_GROUP;
import static sg.gov.moh.iais.egp.bsb.constant.module.ApprovalBatAndActivityConstants.KEY_SHOW_ERROR_SWITCH;
import static sg.gov.moh.iais.egp.bsb.constant.module.ApprovalBatAndActivityConstants.NODE_NAME_PREVIEW;
import static sg.gov.moh.iais.egp.bsb.constant.module.ApprovalBatAndActivityConstants.NODE_NAME_PRIMARY_DOC;
import static sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants.KEY_APP_ID;
import static sg.gov.moh.iais.egp.bsb.dto.register.approval.FacAuthorisedDto.KEY_USER_ID_FACILITY_AUTH_MAP;

@Slf4j
@Delegator("bsbRfiApprovalBatAndActivityDelegator")
public class RfiApprovalBatAndActivityDelegator {

    private final ApprovalBatAndActivityClient approvalBatAndActivityClient;
    private final ApprovalBatAndActivityService approvalBatAndActivityService;
    private final RfiService rfiService;

    public RfiApprovalBatAndActivityDelegator(ApprovalBatAndActivityClient approvalBatAndActivityClient, ApprovalBatAndActivityService approvalBatAndActivityService, RfiService rfiService) {
        this.approvalBatAndActivityClient = approvalBatAndActivityClient;
        this.approvalBatAndActivityService = approvalBatAndActivityService;
        this.rfiService = rfiService;
    }

    public void start(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        // clear sessions
        HttpSession session = request.getSession();
        session.removeAttribute(KEY_APP_ID);
        rfiService.clearAndSetAppIdInSession(request);
        session.removeAttribute(KEY_APPROVAL_SELECTION_DTO);
        session.removeAttribute(KEY_FAC_PROFILE_DTO);
        session.removeAttribute(KEY_ROOT_NODE_GROUP);
        session.removeAttribute(KEY_PROCESS_TYPE);
        session.removeAttribute(KEY_AUTH_PERSONNEL_DETAIL_MAP_JSON);
        session.removeAttribute(KEY_OPTIONS_AUTH_PERSONNEL);
        session.removeAttribute(KEY_USER_ID_FACILITY_AUTH_MAP);
        session.removeAttribute(KEY_FACILITY_ID);
        session.removeAttribute(KEY_APPROVAL_BAT_AND_ACTIVITY_DTO);
        session.removeAttribute(DRAFT_APPROVAL_BAT_AND_ACTIVITY_DTO);
        session.removeAttribute(HAVE_SUITABLE_DRAFT_DATA);
        AuditTrailHelper.auditFunction("Application for Approval", "Application for Approval");
    }

    public void init(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        boolean failRetrieveRfiData = true;
        String appId = (String) ParamUtil.getSessionAttr(request, KEY_APP_ID);
        if (appId != null) {
            ResponseDto<ApprovalBatAndActivityDto> resultDto = approvalBatAndActivityClient.getApprovalAppAppDataByApplicationId(appId);
            if (resultDto.ok()) {
                failRetrieveRfiData = false;
                ApprovalBatAndActivityDto editDto = approvalBatAndActivityService.getEditDtoData(appId);
                NodeGroup approvalAppRoot = editDto.toApprovalAppRootGroup(KEY_ROOT_NODE_GROUP);
                ParamUtil.setSessionAttr(request, KEY_ROOT_NODE_GROUP, approvalAppRoot);
                ParamUtil.setSessionAttr(request, KEY_APPROVAL_BAT_AND_ACTIVITY_DTO, editDto);
                ParamUtil.setSessionAttr(request, KEY_APPROVAL_SELECTION_DTO, editDto.getApprovalSelectionDto());
                // dash bord display
                ParamUtil.setSessionAttr(request, KEY_PROCESS_TYPE, editDto.getApprovalSelectionDto().getProcessType());
            }
        }
        if (failRetrieveRfiData) {
            throw new IaisRuntimeException("Fail to retrieve rfi data");
        }

        approvalBatAndActivityService.retrieveOrgAddressInfo(request);
    }

    public void preApprovalSelection(BaseProcessClass bpc) {
        approvalBatAndActivityService.preApprovalSelection(bpc);
    }

    public void handleApprovalSelection(BaseProcessClass bpc) {
        approvalBatAndActivityService.handleApprovalSelection(bpc);
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
        approvalBatAndActivityService.handleFacProfile(bpc);
    }

    public void prePossessBatDetails(BaseProcessClass bpc) {
        approvalBatAndActivityService.prePossessBatDetails(bpc);
    }

    public void handlePossessBatDetails(BaseProcessClass bpc) {
        approvalBatAndActivityService.handlePossessBatDetails(bpc);
    }

    public void preLargeBatDetails(BaseProcessClass bpc) {
        approvalBatAndActivityService.preLargeBatDetails(bpc);
    }

    public void handleLargeBatDetails(BaseProcessClass bpc) {
        approvalBatAndActivityService.handleLargeBatDetails(bpc);
    }

    public void preSpecialBatDetails(BaseProcessClass bpc) {
        approvalBatAndActivityService.preSpecialBatDetails(bpc);
    }

    public void handleSpecialBatDetails(BaseProcessClass bpc) {
        approvalBatAndActivityService.handleSpecialBatDetails(bpc);
    }

    public void preFacAuthorised(BaseProcessClass bpc) {
        approvalBatAndActivityService.preFacAuthorised(bpc);
    }

    public void handleFacAuthorised(BaseProcessClass bpc) {
        approvalBatAndActivityService.handleFacAuthorised(bpc);
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
        approvalBatAndActivityService.handlePrimaryDoc(bpc);
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

                    // save docs
                    log.info("Save documents into file-repo");
                    PrimaryDocDto primaryDocDto = (PrimaryDocDto) ((SimpleNode) approvalAppRoot.at(NODE_NAME_PRIMARY_DOC)).getValue();
                    List<NewFileSyncDto> newFilesToSync = approvalBatAndActivityService.saveNewUploadedDoc(primaryDocDto);

                    log.info("Save approval application data");
                    ApprovalSelectionDto approvalSelectionDto = approvalBatAndActivityService.getApprovalSelectionDto(request);
                    ApprovalBatAndActivityDto approvalBatAndActivityDto = ApprovalBatAndActivityDto.from(approvalSelectionDto, approvalAppRoot, request);
                    ResponseDto<AppMainInfo> responseDto = rfiService.saveApprovalBatAndActivity(request, approvalBatAndActivityDto);
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
                        // sync docs
                        approvalBatAndActivityService.syncNewDocsAndDeleteFiles(newFilesToSync, null);
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

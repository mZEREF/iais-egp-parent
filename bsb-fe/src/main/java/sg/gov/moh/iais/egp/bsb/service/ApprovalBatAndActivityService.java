package sg.gov.moh.iais.egp.bsb.service;

import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.filerepo.FileRepoDto;
import com.ecquaria.cloud.moh.iais.common.exception.IaisRuntimeException;
import com.ecquaria.cloud.moh.iais.common.utils.JsonUtil;
import com.ecquaria.cloud.moh.iais.common.utils.MaskUtil;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Maps;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;
import sg.gov.moh.iais.egp.bsb.client.ApprovalBatAndActivityClient;
import sg.gov.moh.iais.egp.bsb.client.BsbFileClient;
import sg.gov.moh.iais.egp.bsb.client.DraftClient;
import sg.gov.moh.iais.egp.bsb.client.FileRepoClient;
import sg.gov.moh.iais.egp.bsb.common.edit.FieldEditableJudger;
import sg.gov.moh.iais.egp.bsb.common.node.Node;
import sg.gov.moh.iais.egp.bsb.common.node.NodeGroup;
import sg.gov.moh.iais.egp.bsb.common.node.Nodes;
import sg.gov.moh.iais.egp.bsb.common.node.simple.SimpleNode;
import sg.gov.moh.iais.egp.bsb.constant.FacRegisterConstants;
import sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants;
import sg.gov.moh.iais.egp.bsb.constant.RfcFlowType;
import sg.gov.moh.iais.egp.bsb.constant.ValidationConstants;
import sg.gov.moh.iais.egp.bsb.constant.module.ApprovalBatAndActivityConstants;
import sg.gov.moh.iais.egp.bsb.dto.ResponseDto;
import sg.gov.moh.iais.egp.bsb.dto.entity.FacilityAuthoriserDto;
import sg.gov.moh.iais.egp.bsb.dto.file.DocRecordInfo;
import sg.gov.moh.iais.egp.bsb.dto.file.FileRepoSyncDto;
import sg.gov.moh.iais.egp.bsb.dto.file.NewDocInfo;
import sg.gov.moh.iais.egp.bsb.dto.file.NewFileSyncDto;
import sg.gov.moh.iais.egp.bsb.dto.info.bat.RichBatCodeInfo;
import sg.gov.moh.iais.egp.bsb.dto.info.bat.RichSchedule;
import sg.gov.moh.iais.egp.bsb.dto.info.bat.RichScheduleBatInfo;
import sg.gov.moh.iais.egp.bsb.dto.info.facility.FacilityBasicInfo;
import sg.gov.moh.iais.egp.bsb.dto.register.approval.ApprovalBatAndActivityDto;
import sg.gov.moh.iais.egp.bsb.dto.register.approval.ApprovalSelectionDto;
import sg.gov.moh.iais.egp.bsb.dto.register.approval.ApprovalToActivityDto;
import sg.gov.moh.iais.egp.bsb.dto.register.approval.ApprovalToLargeDto;
import sg.gov.moh.iais.egp.bsb.dto.register.approval.ApprovalToPossessDto;
import sg.gov.moh.iais.egp.bsb.dto.register.approval.ApprovalToSpecialDto;
import sg.gov.moh.iais.egp.bsb.dto.register.approval.AuthorisedSelection;
import sg.gov.moh.iais.egp.bsb.dto.register.approval.FacAuthorisedDto;
import sg.gov.moh.iais.egp.bsb.dto.register.approval.FacProfileDto;
import sg.gov.moh.iais.egp.bsb.dto.register.approval.PreviewDto;
import sg.gov.moh.iais.egp.bsb.dto.register.approval.PrimaryDocDto;
import sg.gov.moh.iais.egp.bsb.dto.register.bat.BATInfo;
import sg.gov.moh.iais.egp.bsb.dto.validation.ValidationResultDto;
import sg.gov.moh.iais.egp.bsb.entity.DocSetting;
import sg.gov.moh.iais.egp.bsb.service.rfc.RfcFlowDecider;
import sg.gov.moh.iais.egp.bsb.util.JaversUtil;
import sg.gov.moh.iais.egp.bsb.util.mastercode.MasterCodeHolder;
import sg.gov.moh.iais.egp.common.modal.view.RichSelectOption;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static sg.gov.moh.iais.egp.bsb.constant.FacRegisterConstants.ELIGIBLE_DRAFT_REGISTER_DTO;
import static sg.gov.moh.iais.egp.bsb.constant.module.ApprovalBatAndActivityConstants.ACTION_LOAD_DRAFT;
import static sg.gov.moh.iais.egp.bsb.constant.module.ApprovalBatAndActivityConstants.DRAFT_APPROVAL_BAT_AND_ACTIVITY_DTO;
import static sg.gov.moh.iais.egp.bsb.constant.module.ApprovalBatAndActivityConstants.ERR_MSG_INVALID_ACTION;
import static sg.gov.moh.iais.egp.bsb.constant.module.ApprovalBatAndActivityConstants.HAVE_SUITABLE_DRAFT_DATA;
import static sg.gov.moh.iais.egp.bsb.constant.module.ApprovalBatAndActivityConstants.KEY_ACTION_JUMP;
import static sg.gov.moh.iais.egp.bsb.constant.module.ApprovalBatAndActivityConstants.KEY_ACTION_SAVE_AS_DRAFT;
import static sg.gov.moh.iais.egp.bsb.constant.module.ApprovalBatAndActivityConstants.KEY_ACTION_TYPE;
import static sg.gov.moh.iais.egp.bsb.constant.module.ApprovalBatAndActivityConstants.KEY_ACTION_VALUE;
import static sg.gov.moh.iais.egp.bsb.constant.module.ApprovalBatAndActivityConstants.KEY_APPROVAL_BAT_AND_ACTIVITY_DTO;
import static sg.gov.moh.iais.egp.bsb.constant.module.ApprovalBatAndActivityConstants.KEY_APPROVAL_SELECTION_DTO;
import static sg.gov.moh.iais.egp.bsb.constant.module.ApprovalBatAndActivityConstants.KEY_APPROVAL_TO_ACTIVITY_DTO;
import static sg.gov.moh.iais.egp.bsb.constant.module.ApprovalBatAndActivityConstants.KEY_AUTH_ID_SELECTION_MAP;
import static sg.gov.moh.iais.egp.bsb.constant.module.ApprovalBatAndActivityConstants.KEY_AUTH_PERSON_LIST;
import static sg.gov.moh.iais.egp.bsb.constant.module.ApprovalBatAndActivityConstants.KEY_BAT_INFO;
import static sg.gov.moh.iais.egp.bsb.constant.module.ApprovalBatAndActivityConstants.KEY_DEST_NODE_ROUTE;
import static sg.gov.moh.iais.egp.bsb.constant.module.ApprovalBatAndActivityConstants.KEY_DOC_SETTINGS;
import static sg.gov.moh.iais.egp.bsb.constant.module.ApprovalBatAndActivityConstants.KEY_DOC_TYPES_JSON;
import static sg.gov.moh.iais.egp.bsb.constant.module.ApprovalBatAndActivityConstants.KEY_EDITABLE_FIELD_SET;
import static sg.gov.moh.iais.egp.bsb.constant.module.ApprovalBatAndActivityConstants.KEY_FACILITY_ID;
import static sg.gov.moh.iais.egp.bsb.constant.module.ApprovalBatAndActivityConstants.KEY_FAC_ACTIVITY_TYPE_APPROVAL_WITH_APPROVED_LIST;
import static sg.gov.moh.iais.egp.bsb.constant.module.ApprovalBatAndActivityConstants.KEY_FAC_AUTHORISED_PERSON_LIST_WANTED;
import static sg.gov.moh.iais.egp.bsb.constant.module.ApprovalBatAndActivityConstants.KEY_FAC_PROFILE_DTO;
import static sg.gov.moh.iais.egp.bsb.constant.module.ApprovalBatAndActivityConstants.KEY_INDEED_ACTION_TYPE;
import static sg.gov.moh.iais.egp.bsb.constant.module.ApprovalBatAndActivityConstants.KEY_JUMP_DEST_NODE;
import static sg.gov.moh.iais.egp.bsb.constant.module.ApprovalBatAndActivityConstants.KEY_NAV_NEXT;
import static sg.gov.moh.iais.egp.bsb.constant.module.ApprovalBatAndActivityConstants.KEY_NAV_PREVIOUS;
import static sg.gov.moh.iais.egp.bsb.constant.module.ApprovalBatAndActivityConstants.KEY_OLD_APPROVAL_BAT_AND_ACTIVITY_DTO;
import static sg.gov.moh.iais.egp.bsb.constant.module.ApprovalBatAndActivityConstants.KEY_OPTIONS_ADDRESS_TYPE;
import static sg.gov.moh.iais.egp.bsb.constant.module.ApprovalBatAndActivityConstants.KEY_OPTIONS_COUNTRY;
import static sg.gov.moh.iais.egp.bsb.constant.module.ApprovalBatAndActivityConstants.KEY_OPTIONS_DOC_TYPES;
import static sg.gov.moh.iais.egp.bsb.constant.module.ApprovalBatAndActivityConstants.KEY_OTHER_DOC_TYPES;
import static sg.gov.moh.iais.egp.bsb.constant.module.ApprovalBatAndActivityConstants.KEY_PRINT_MASKED_ID;
import static sg.gov.moh.iais.egp.bsb.constant.module.ApprovalBatAndActivityConstants.KEY_PRINT_MASK_PARAM;
import static sg.gov.moh.iais.egp.bsb.constant.module.ApprovalBatAndActivityConstants.KEY_PROCESS_TYPE;
import static sg.gov.moh.iais.egp.bsb.constant.module.ApprovalBatAndActivityConstants.KEY_RFC_FLOW_TYPE;
import static sg.gov.moh.iais.egp.bsb.constant.module.ApprovalBatAndActivityConstants.KEY_ROOT_NODE_GROUP;
import static sg.gov.moh.iais.egp.bsb.constant.module.ApprovalBatAndActivityConstants.KEY_SHOW_ERROR_SWITCH;
import static sg.gov.moh.iais.egp.bsb.constant.module.ApprovalBatAndActivityConstants.KEY_VALIDATION_ERRORS;
import static sg.gov.moh.iais.egp.bsb.constant.module.ApprovalBatAndActivityConstants.NODE_NAME_APPROVAL_SELECTION;
import static sg.gov.moh.iais.egp.bsb.constant.module.ApprovalBatAndActivityConstants.NODE_NAME_APP_INFO;
import static sg.gov.moh.iais.egp.bsb.constant.module.ApprovalBatAndActivityConstants.NODE_NAME_BEGIN;
import static sg.gov.moh.iais.egp.bsb.constant.module.ApprovalBatAndActivityConstants.NODE_NAME_COMPANY_INFO;
import static sg.gov.moh.iais.egp.bsb.constant.module.ApprovalBatAndActivityConstants.NODE_NAME_FAC_ACTIVITY;
import static sg.gov.moh.iais.egp.bsb.constant.module.ApprovalBatAndActivityConstants.NODE_NAME_FAC_AUTHORISED;
import static sg.gov.moh.iais.egp.bsb.constant.module.ApprovalBatAndActivityConstants.NODE_NAME_FAC_PROFILE;
import static sg.gov.moh.iais.egp.bsb.constant.module.ApprovalBatAndActivityConstants.NODE_NAME_LARGE_BAT;
import static sg.gov.moh.iais.egp.bsb.constant.module.ApprovalBatAndActivityConstants.NODE_NAME_POSSESS_BAT;
import static sg.gov.moh.iais.egp.bsb.constant.module.ApprovalBatAndActivityConstants.NODE_NAME_PREVIEW;
import static sg.gov.moh.iais.egp.bsb.constant.module.ApprovalBatAndActivityConstants.NODE_NAME_PRIMARY_DOC;
import static sg.gov.moh.iais.egp.bsb.constant.module.ApprovalBatAndActivityConstants.NODE_NAME_SPECIAL_BAT;
import static sg.gov.moh.iais.egp.bsb.constant.module.ApprovalBatAndActivityConstants.SELECTION_FACILITY_ID;
import static sg.gov.moh.iais.egp.bsb.constant.module.ApprovalBatAndActivityConstants.SOURCE_FACILITY_DETAILS;
import static sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants.KEY_IND_AFTER_SAVE_AS_DRAFT;


@Slf4j
@Service
@RequiredArgsConstructor
public class ApprovalBatAndActivityService {
    private final ApprovalBatAndActivityClient approvalBatAndActivityClient;
    private final DocSettingService docSettingService;
    private final FileRepoClient fileRepoClient;
    private final BsbFileClient bsbFileClient;
    private final DraftClient draftClient;
    private final RfcFlowDecider rfcFlowDecider;

    public void preApprovalSelection(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        ApprovalSelectionDto approvalSelectionDto = getApprovalSelectionDto(request);
        //come from inbox facility tab
        String maskedFacId = request.getParameter(ApprovalBatAndActivityConstants.KEY_FACILITY_ID);
        if (org.springframework.util.StringUtils.hasLength(maskedFacId)) {
            String facId = MaskUtil.unMaskValue("applyApprovalFacId", maskedFacId);
            if (org.springframework.util.StringUtils.hasLength(facId) && !maskedFacId.equals(facId)) {
                approvalSelectionDto.setFacilityId(facId);
            } else {
                throw new IllegalArgumentException("invalid masked facility id");
            }
        }
        ParamUtil.setSessionAttr(request, KEY_APPROVAL_SELECTION_DTO, approvalSelectionDto);
    }

    public void getSameFacilityAndProcessTypeDraftData(HttpServletRequest request, ApprovalSelectionDto selectionDto, String appType) {
        ApprovalBatAndActivityDto suitableDraftDto = (ApprovalBatAndActivityDto) ParamUtil.getSessionAttr(request, DRAFT_APPROVAL_BAT_AND_ACTIVITY_DTO);
        // judge the action is click on Apply for Approval menu or click on Draft Application
        // if is click on draft application,do nothing
        Object requestAttr = ParamUtil.getRequestAttr(request, HAVE_SUITABLE_DRAFT_DATA);
        boolean haveSuitableDraftData = requestAttr != null && (boolean) requestAttr;
        if (suitableDraftDto == null && !org.springframework.util.StringUtils.hasLength(selectionDto.getDraftAppNo())) {
            suitableDraftDto = approvalBatAndActivityClient.getSameFacilityAndProcessTypeDraftData(selectionDto.getFacilityId(), selectionDto.getProcessType(), appType).getEntity();
            haveSuitableDraftData = suitableDraftDto != null;
        }
        if (suitableDraftDto != null && org.springframework.util.StringUtils.hasLength(selectionDto.getDraftAppNo())) {
            // the enteredInbox is used to judge whether display facility select option
            suitableDraftDto.getApprovalSelectionDto().setEnteredInbox(selectionDto.isEnteredInbox());
            haveSuitableDraftData = true;
        }
        ParamUtil.setSessionAttr(request, DRAFT_APPROVAL_BAT_AND_ACTIVITY_DTO, suitableDraftDto);
        ParamUtil.setRequestAttr(request, HAVE_SUITABLE_DRAFT_DATA, haveSuitableDraftData);
        if (suitableDraftDto != null) {
            // the enteredInbox is used to judge whether display facility select option
            suitableDraftDto.getApprovalSelectionDto().setEnteredInbox(selectionDto.isEnteredInbox());
            //judge whether need query the draft data again
            ApprovalSelectionDto approvalSelectionDto = suitableDraftDto.getApprovalSelectionDto();
            if (!approvalSelectionDto.getFacilityId().equals(selectionDto.getFacilityId()) || !approvalSelectionDto.getProcessType().equals(selectionDto.getProcessType())) {
                selectionDto.setDraftAppNo(null);
                ParamUtil.setSessionAttr(request, DRAFT_APPROVAL_BAT_AND_ACTIVITY_DTO, null);
                getSameFacilityAndProcessTypeDraftData(request, selectionDto, appType);
            }
        }
    }

    public void handleApprovalSelection(BaseProcessClass bpc, String profile) {
        HttpServletRequest request = bpc.request;
        // get approvalBatAndActivityDto
        ApprovalSelectionDto approvalSelectionDto = getApprovalSelectionDto(request);
        String oldProcessType = approvalSelectionDto.getProcessType();
        approvalSelectionDto.reqObjMapping(request);
        String appType = MasterCodeConstants.APP_TYPE_NEW;
        if (ValidationConstants.VALIDATION_PROFILE_RFC.equals(profile)) {
            appType = MasterCodeConstants.APP_TYPE_RFC;
        }

        // judge jump logic
        String actionType = ParamUtil.getString(request, KEY_ACTION_TYPE);
        String actionValue = ParamUtil.getString(request, KEY_ACTION_VALUE);
        String actionLoadDraft = ParamUtil.getString(request, ACTION_LOAD_DRAFT);
        if (KEY_ACTION_JUMP.equals(actionType)) {
            if (KEY_NAV_NEXT.equals(actionValue)) {
                // do next: validate approvalSelectionDto
                ValidationResultDto validationResultDto = approvalBatAndActivityClient.validateApprovalSelectionDto(approvalSelectionDto);
                if (validationResultDto.isPass()) {
                    ApprovalBatAndActivityDto suitableDraftDto = (ApprovalBatAndActivityDto) ParamUtil.getSessionAttr(request, DRAFT_APPROVAL_BAT_AND_ACTIVITY_DTO);
                    if (MasterCodeConstants.YES.equals(actionLoadDraft)) {
                        // if select to resume draft data,load draft data
                        NodeGroup approvalAppRoot = suitableDraftDto.toApprovalAppRootGroup(KEY_ROOT_NODE_GROUP, profile);
                        ParamUtil.setSessionAttr(request, KEY_ROOT_NODE_GROUP, approvalAppRoot);
                        ParamUtil.setSessionAttr(request, KEY_JUMP_DEST_NODE, NODE_NAME_COMPANY_INFO);
                        ParamUtil.setSessionAttr(request, KEY_APPROVAL_BAT_AND_ACTIVITY_DTO, suitableDraftDto);
                        ParamUtil.setSessionAttr(request, KEY_APPROVAL_SELECTION_DTO, suitableDraftDto.getApprovalSelectionDto());
                        // dash bord display
                        ParamUtil.setSessionAttr(request, KEY_PROCESS_TYPE, suitableDraftDto.getApprovalSelectionDto().getProcessType());
                    } else if (MasterCodeConstants.NO.equals(actionLoadDraft)) {
                        // if select to continue create new application,delete draft
                        if (suitableDraftDto != null) {
                            //delete draft from database
                            draftClient.doRemoveDraftByDraftAppNo(suitableDraftDto.getApprovalSelectionDto().getDraftAppNo());
                            //remove draft from session
                            ParamUtil.setSessionAttr(request, ELIGIBLE_DRAFT_REGISTER_DTO, null);
                        }
                        ParamUtil.setSessionAttr(request, KEY_APPROVAL_BAT_AND_ACTIVITY_DTO, null);
                        ParamUtil.setSessionAttr(request, KEY_APPROVAL_SELECTION_DTO, approvalSelectionDto);
                        commonNewApprovalHandle(request, approvalSelectionDto, oldProcessType);
                    } else {
                        ParamUtil.setSessionAttr(request, KEY_APPROVAL_SELECTION_DTO, approvalSelectionDto);
                        //judge whether have draft that have same facility and process type
                        getSameFacilityAndProcessTypeDraftData(request, approvalSelectionDto, appType);
                        boolean haveSuitableDraftData = (boolean) ParamUtil.getRequestAttr(request, HAVE_SUITABLE_DRAFT_DATA);
                        //if have draft data that have same facility and process type,display modal box
                        if (haveSuitableDraftData) {
                            ApprovalBatAndActivityDto approvalBatAndActivityDto = (ApprovalBatAndActivityDto) ParamUtil.getSessionAttr(request, DRAFT_APPROVAL_BAT_AND_ACTIVITY_DTO);
                            ParamUtil.setSessionAttr(request, KEY_FAC_PROFILE_DTO, approvalBatAndActivityDto.getFacProfileDto());
                            ParamUtil.setSessionAttr(request, KEY_JUMP_DEST_NODE, NODE_NAME_APPROVAL_SELECTION);
                        } else {
                            commonNewApprovalHandle(request, approvalSelectionDto, oldProcessType);
                        }
                    }
                } else {
                    ParamUtil.setRequestAttr(request, KEY_VALIDATION_ERRORS, validationResultDto.toErrorMsg());
                    ParamUtil.setSessionAttr(request, KEY_JUMP_DEST_NODE, NODE_NAME_APPROVAL_SELECTION);
                }
            } else if (KEY_NAV_PREVIOUS.equals(actionValue)) {
                // do back
                ParamUtil.setSessionAttr(request, KEY_JUMP_DEST_NODE, NODE_NAME_BEGIN);
            }
        } else {
            throw new IaisRuntimeException(ERR_MSG_INVALID_ACTION);
        }
    }

    private void commonNewApprovalHandle(HttpServletRequest request, ApprovalSelectionDto approvalSelectionDto, String oldProcessType) {
        // get nodeGroup
        NodeGroup approvalAppRoot;
        // judge whether the selected processType is the same
        String newProcessType = approvalSelectionDto.getProcessType();
        if (org.springframework.util.StringUtils.hasLength(oldProcessType) && oldProcessType.equals(newProcessType)) {
            // selected processType is the same
            approvalAppRoot = (NodeGroup) ParamUtil.getSessionAttr(request, KEY_ROOT_NODE_GROUP);
            if (approvalAppRoot == null) {
                //handle context oldProcessType is same as newProcessType,but validation is no pass,without init of NodeGroup
                approvalAppRoot = newApprovalAppRoot(KEY_ROOT_NODE_GROUP, oldProcessType);
            }
        } else {
            // selected processType is the different
            approvalAppRoot = newApprovalAppRoot(KEY_ROOT_NODE_GROUP, newProcessType);
        }
        ParamUtil.setSessionAttr(request, KEY_JUMP_DEST_NODE, NODE_NAME_COMPANY_INFO);
        // dash bord display
        ParamUtil.setSessionAttr(request, KEY_PROCESS_TYPE, approvalSelectionDto.getProcessType());

        /* Retrieves facility data here and set into BAT DTO.
         * Because: BAT page needs facilityId (already has) and protectedPlace gazette to get the schedule and
         * BAT dropdown options. */
        String facilityId = approvalSelectionDto.getFacilityId();
        FacProfileDto facProfileDto = getFacProfileDtoByFacilityId(facilityId);
        ParamUtil.setSessionAttr(request, KEY_FAC_PROFILE_DTO, facProfileDto);
        switch (approvalSelectionDto.getProcessType()) {
            case MasterCodeConstants.PROCESS_TYPE_APPROVE_POSSESS:
                ApprovalToPossessDto atpDto = (ApprovalToPossessDto) ((SimpleNode) approvalAppRoot.at(NODE_NAME_APP_INFO + approvalAppRoot.getPathSeparator() + NODE_NAME_POSSESS_BAT)).getValue();
                atpDto.setFacilityId(facilityId);
                atpDto.setProtectedPlace(MasterCodeConstants.YES.equals(facProfileDto.getProtectedPlace()));
                break;
            case MasterCodeConstants.PROCESS_TYPE_APPROVE_LSP:
                ApprovalToLargeDto lspDto = (ApprovalToLargeDto) ((SimpleNode) approvalAppRoot.at(NODE_NAME_APP_INFO + approvalAppRoot.getPathSeparator() + NODE_NAME_LARGE_BAT)).getValue();
                lspDto.setFacilityId(facilityId);
                lspDto.setProtectedPlace(MasterCodeConstants.YES.equals(facProfileDto.getProtectedPlace()));
                break;
            case MasterCodeConstants.PROCESS_TYPE_SP_APPROVE_HANDLE:
                ApprovalToSpecialDto spDto = (ApprovalToSpecialDto) ((SimpleNode) approvalAppRoot.at(NODE_NAME_APP_INFO + approvalAppRoot.getPathSeparator() + NODE_NAME_SPECIAL_BAT)).getValue();
                spDto.setFacilityId(facilityId);
                spDto.setProtectedPlace(MasterCodeConstants.YES.equals(facProfileDto.getProtectedPlace()));
                break;
            default:
                break;
        }

        ParamUtil.setSessionAttr(request, KEY_ROOT_NODE_GROUP, approvalAppRoot);
    }

    public void handleCompanyInfo(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        String actionType = ParamUtil.getString(request, KEY_ACTION_TYPE);
        String actionValue = ParamUtil.getString(request, KEY_ACTION_VALUE);
        if (KEY_ACTION_JUMP.equals(actionType)) {
            if (KEY_NAV_NEXT.equals(actionValue)) {
                ParamUtil.setSessionAttr(request, KEY_JUMP_DEST_NODE, "appInfo_facProfile");
            } else if (KEY_NAV_PREVIOUS.equals(actionValue)) {
                ParamUtil.setSessionAttr(request, KEY_JUMP_DEST_NODE, NODE_NAME_APPROVAL_SELECTION);
            }
        } else {
            throw new IaisRuntimeException(ERR_MSG_INVALID_ACTION);
        }
    }

    public void preFacProfile(BaseProcessClass bpc) {
        // do nothing now, the needed data is retrieved at the handleApprovalSelection step
    }

    public void handleFacProfile(BaseProcessClass bpc, String appType) {
        HttpServletRequest request = bpc.request;
        NodeGroup approvalAppRoot = getApprovalActivityRoot(request, null);
        String currentNodePath = NODE_NAME_APP_INFO + approvalAppRoot.getPathSeparator() + NODE_NAME_FAC_PROFILE;
        Node facProfileNode = approvalAppRoot.at(currentNodePath);

        String actionType = ParamUtil.getString(request, KEY_ACTION_TYPE);
        if (KEY_ACTION_JUMP.equals(actionType)) {
            String actionValue = ParamUtil.getString(request, KEY_ACTION_VALUE);
            if (actionValue.equals(KEY_NAV_PREVIOUS)) {
                if (MasterCodeConstants.APP_TYPE_RFC.equals(appType)) {
                    ParamUtil.setSessionAttr(request, KEY_JUMP_DEST_NODE, NODE_NAME_APPROVAL_SELECTION);
                } else {
                    ParamUtil.setSessionAttr(request, KEY_JUMP_DEST_NODE, NODE_NAME_COMPANY_INFO);
                }
            } else {
                jumpHandler(request, approvalAppRoot, currentNodePath, facProfileNode);
            }
        } else if (KEY_ACTION_SAVE_AS_DRAFT.equals(actionType)) {
            ParamUtil.setRequestAttr(request, KEY_ACTION_TYPE, KEY_ACTION_SAVE_AS_DRAFT);
            ParamUtil.setSessionAttr(request, KEY_JUMP_DEST_NODE, currentNodePath);
        } else {
            throw new IaisRuntimeException(ERR_MSG_INVALID_ACTION);
        }
        ParamUtil.setSessionAttr(request, KEY_ROOT_NODE_GROUP, approvalAppRoot);
    }

    public void prePossessBatDetails(BaseProcessClass bpc, Set<String> editableFieldSet) {
        HttpServletRequest request = bpc.request;
        NodeGroup approvalAppRoot = getApprovalActivityRoot(request, null);
        String currentNodePath = NODE_NAME_APP_INFO + approvalAppRoot.getPathSeparator() + NODE_NAME_POSSESS_BAT;
        SimpleNode possessBatNode = (SimpleNode) approvalAppRoot.at(currentNodePath);
        ApprovalToPossessDto approvalToPossessDto = (ApprovalToPossessDto) possessBatNode.getValue();
        Boolean needShowError = (Boolean) ParamUtil.getRequestAttr(request, KEY_SHOW_ERROR_SWITCH);
        if (needShowError == Boolean.TRUE) {
            ParamUtil.setRequestAttr(request, KEY_VALIDATION_ERRORS, approvalToPossessDto.retrieveValidationResult());
        }
        Nodes.needValidation(approvalAppRoot, currentNodePath);
        ParamUtil.setRequestAttr(request, KEY_BAT_INFO, approvalToPossessDto);
        ParamUtil.setRequestAttr(request, KEY_OPTIONS_ADDRESS_TYPE, MasterCodeHolder.ADDRESS_TYPE.allOptions());
        ParamUtil.setRequestAttr(request, KEY_OPTIONS_COUNTRY, MasterCodeHolder.COUNTRY.allOptions());
        loadAllowedScheduleAndBatOptions(request);
        FacProfileDto facProfileDto = (FacProfileDto) ParamUtil.getSessionAttr(request, KEY_FAC_PROFILE_DTO);
        ParamUtil.setRequestAttr(request, SOURCE_FACILITY_DETAILS, facProfileDto.getSourceFacDetails());
        if (!CollectionUtils.isEmpty(editableFieldSet)) {
            ParamUtil.setRequestAttr(request, KEY_EDITABLE_FIELD_SET, String.join(",", editableFieldSet));
        }
    }

    public void handlePossessBatDetails(BaseProcessClass bpc, FieldEditableJudger editableJudger, String appType) {
        HttpServletRequest request = bpc.request;
        ApprovalBatAndActivityDto oldApprovalBatAndActivityDto = (ApprovalBatAndActivityDto) ParamUtil.getSessionAttr(request, KEY_OLD_APPROVAL_BAT_AND_ACTIVITY_DTO);
        NodeGroup approvalAppRoot = getApprovalActivityRoot(request, null);
        String currentNodePath = NODE_NAME_APP_INFO + approvalAppRoot.getPathSeparator() + NODE_NAME_POSSESS_BAT;
        SimpleNode possessBatNode = (SimpleNode) approvalAppRoot.at(currentNodePath);
        ApprovalToPossessDto approvalToPossessDto = (ApprovalToPossessDto) possessBatNode.getValue();
        approvalToPossessDto.reqObjMapping(request, editableJudger);
        if (MasterCodeConstants.APP_TYPE_RFC.equals(appType)) {
            // compare old and new dto, to judge whether need to create task
            ApprovalToPossessDto oldApprovalToPossessDto = oldApprovalBatAndActivityDto.getApprovalToPossessDto();
            Set<String> changedKeys = JaversUtil.compareCollections(oldApprovalToPossessDto.getBatInfos(), approvalToPossessDto.getBatInfos(), BATInfo.class);
            commonJudgeRfcFlowType(request, changedKeys);
        }

        String actionType = ParamUtil.getString(request, KEY_ACTION_TYPE);
        if (KEY_ACTION_JUMP.equals(actionType)) {
            jumpHandler(request, approvalAppRoot, currentNodePath, possessBatNode);
        } else if (KEY_ACTION_SAVE_AS_DRAFT.equals(actionType)) {
            ParamUtil.setRequestAttr(request, KEY_ACTION_TYPE, KEY_ACTION_SAVE_AS_DRAFT);
            ParamUtil.setSessionAttr(request, KEY_JUMP_DEST_NODE, currentNodePath);
        } else {
            throw new IaisRuntimeException(ERR_MSG_INVALID_ACTION);
        }
        ParamUtil.setSessionAttr(request, KEY_ROOT_NODE_GROUP, approvalAppRoot);
    }

    public void preLargeBatDetails(BaseProcessClass bpc, Set<String> editableFieldSet) {
        HttpServletRequest request = bpc.request;
        NodeGroup approvalAppRoot = getApprovalActivityRoot(request, null);
        String currentNodePath = NODE_NAME_APP_INFO + approvalAppRoot.getPathSeparator() + NODE_NAME_LARGE_BAT;
        SimpleNode largeBatNode = (SimpleNode) approvalAppRoot.at(currentNodePath);
        ApprovalToLargeDto approvalToLargeDto = (ApprovalToLargeDto) largeBatNode.getValue();
        Boolean needShowError = (Boolean) ParamUtil.getRequestAttr(request, KEY_SHOW_ERROR_SWITCH);
        if (needShowError == Boolean.TRUE) {
            ParamUtil.setRequestAttr(request, KEY_VALIDATION_ERRORS, approvalToLargeDto.retrieveValidationResult());
        }
        Nodes.needValidation(approvalAppRoot, currentNodePath);
        ParamUtil.setRequestAttr(request, KEY_BAT_INFO, approvalToLargeDto);
        ParamUtil.setRequestAttr(request, KEY_OPTIONS_ADDRESS_TYPE, MasterCodeHolder.ADDRESS_TYPE.allOptions());
        ParamUtil.setRequestAttr(request, KEY_OPTIONS_COUNTRY, MasterCodeHolder.COUNTRY.allOptions());
        loadAllowedScheduleAndBatOptions(request);
        FacProfileDto facProfileDto = (FacProfileDto) ParamUtil.getSessionAttr(request, KEY_FAC_PROFILE_DTO);
        ParamUtil.setRequestAttr(request, SOURCE_FACILITY_DETAILS, facProfileDto.getSourceFacDetails());
        if (!CollectionUtils.isEmpty(editableFieldSet)) {
            ParamUtil.setRequestAttr(request, KEY_EDITABLE_FIELD_SET, String.join(",", editableFieldSet));
        }
    }

    public void handleLargeBatDetails(BaseProcessClass bpc, FieldEditableJudger editableJudger, String appType) {
        HttpServletRequest request = bpc.request;
        ApprovalBatAndActivityDto oldApprovalBatAndActivityDto = (ApprovalBatAndActivityDto) ParamUtil.getSessionAttr(request, KEY_OLD_APPROVAL_BAT_AND_ACTIVITY_DTO);
        NodeGroup approvalAppRoot = getApprovalActivityRoot(request, null);
        String currentNodePath = NODE_NAME_APP_INFO + approvalAppRoot.getPathSeparator() + NODE_NAME_LARGE_BAT;
        SimpleNode largeBatNode = (SimpleNode) approvalAppRoot.at(currentNodePath);
        ApprovalToLargeDto approvalToLargeDto = (ApprovalToLargeDto) largeBatNode.getValue();
        approvalToLargeDto.reqObjMapping(request, editableJudger);
        if (MasterCodeConstants.APP_TYPE_RFC.equals(appType)) {
            // compare old and new dto, to judge whether need to create task
            ApprovalToLargeDto oldApprovalToLargeDto = oldApprovalBatAndActivityDto.getApprovalToLargeDto();
            Set<String> changedKeys = JaversUtil.compareCollections(oldApprovalToLargeDto.getBatInfos(), approvalToLargeDto.getBatInfos(), BATInfo.class);
            //todo need to compare sampleType/workType in BATInfo
            commonJudgeRfcFlowType(request, changedKeys);
        }

        String actionType = ParamUtil.getString(request, KEY_ACTION_TYPE);
        if (KEY_ACTION_JUMP.equals(actionType)) {
            jumpHandler(request, approvalAppRoot, currentNodePath, largeBatNode);
        } else if (KEY_ACTION_SAVE_AS_DRAFT.equals(actionType)) {
            ParamUtil.setRequestAttr(request, KEY_ACTION_TYPE, KEY_ACTION_SAVE_AS_DRAFT);
            ParamUtil.setSessionAttr(request, KEY_JUMP_DEST_NODE, currentNodePath);
        } else {
            throw new IaisRuntimeException(ERR_MSG_INVALID_ACTION);
        }
        ParamUtil.setSessionAttr(request, KEY_ROOT_NODE_GROUP, approvalAppRoot);
    }

    public void preSpecialBatDetails(BaseProcessClass bpc, Set<String> editableFieldSet) {
        HttpServletRequest request = bpc.request;
        NodeGroup approvalAppRoot = getApprovalActivityRoot(request, null);
        String currentNodePath = NODE_NAME_APP_INFO + approvalAppRoot.getPathSeparator() + NODE_NAME_SPECIAL_BAT;
        SimpleNode specialBatNode = (SimpleNode) approvalAppRoot.at(currentNodePath);
        ApprovalToSpecialDto approvalToSpecialDto = (ApprovalToSpecialDto) specialBatNode.getValue();
        Boolean needShowError = (Boolean) ParamUtil.getRequestAttr(request, KEY_SHOW_ERROR_SWITCH);
        if (needShowError == Boolean.TRUE) {
            ParamUtil.setRequestAttr(request, KEY_VALIDATION_ERRORS, approvalToSpecialDto.retrieveValidationResult());
        }
        Nodes.needValidation(approvalAppRoot, currentNodePath);
        ParamUtil.setRequestAttr(request, KEY_BAT_INFO, approvalToSpecialDto);
        ParamUtil.setRequestAttr(request, KEY_OPTIONS_ADDRESS_TYPE, MasterCodeHolder.ADDRESS_TYPE.allOptions());
        ParamUtil.setRequestAttr(request, KEY_OPTIONS_COUNTRY, MasterCodeHolder.COUNTRY.allOptions());
        loadAllowedScheduleAndBatOptions(request);
        if (!CollectionUtils.isEmpty(editableFieldSet)) {
            ParamUtil.setRequestAttr(request, KEY_EDITABLE_FIELD_SET, String.join(",", editableFieldSet));
        }
    }

    public void handleSpecialBatDetails(BaseProcessClass bpc, FieldEditableJudger editableJudger, String appType) {
        HttpServletRequest request = bpc.request;
        ApprovalBatAndActivityDto oldApprovalBatAndActivityDto = (ApprovalBatAndActivityDto) ParamUtil.getSessionAttr(request, KEY_OLD_APPROVAL_BAT_AND_ACTIVITY_DTO);
        NodeGroup approvalAppRoot = getApprovalActivityRoot(request, null);
        String currentNodePath = NODE_NAME_APP_INFO + approvalAppRoot.getPathSeparator() + NODE_NAME_SPECIAL_BAT;
        SimpleNode specialBatNode = (SimpleNode) approvalAppRoot.at(currentNodePath);
        ApprovalToSpecialDto approvalToSpecialDto = (ApprovalToSpecialDto) specialBatNode.getValue();
        approvalToSpecialDto.reqObjMapping(request, editableJudger);
        if (MasterCodeConstants.APP_TYPE_RFC.equals(appType)) {
            // compare old and new dto, to judge whether need to create task
            ApprovalToSpecialDto oldApprovalToSpecialDto = oldApprovalBatAndActivityDto.getApprovalToSpecialDto();
            Set<String> changedKeys = JaversUtil.compare(oldApprovalToSpecialDto, approvalToSpecialDto);
            commonJudgeRfcFlowType(request, changedKeys);
        }

        String actionType = ParamUtil.getString(request, KEY_ACTION_TYPE);
        if (KEY_ACTION_JUMP.equals(actionType)) {
            jumpHandler(request, approvalAppRoot, currentNodePath, specialBatNode);
        } else if (KEY_ACTION_SAVE_AS_DRAFT.equals(actionType)) {
            ParamUtil.setRequestAttr(request, KEY_ACTION_TYPE, KEY_ACTION_SAVE_AS_DRAFT);
            ParamUtil.setSessionAttr(request, KEY_JUMP_DEST_NODE, currentNodePath);
        } else {
            throw new IaisRuntimeException(ERR_MSG_INVALID_ACTION);
        }
        ParamUtil.setSessionAttr(request, KEY_ROOT_NODE_GROUP, approvalAppRoot);
    }

    public void preFacAuthorised(BaseProcessClass bpc, Set<String> editableFieldSet) {
        HttpServletRequest request = bpc.request;
        NodeGroup approvalAppRoot = getApprovalActivityRoot(request, null);
        String currentNodePath = NODE_NAME_APP_INFO + approvalAppRoot.getPathSeparator() + NODE_NAME_FAC_AUTHORISED;
        SimpleNode facAuthorisedNode = (SimpleNode) approvalAppRoot.at(currentNodePath);
        FacAuthorisedDto facAuthorisedDto = (FacAuthorisedDto) facAuthorisedNode.getValue();
        Boolean needShowError = (Boolean) ParamUtil.getRequestAttr(request, KEY_SHOW_ERROR_SWITCH);
        if (needShowError == Boolean.TRUE) {
            ParamUtil.setRequestAttr(request, KEY_VALIDATION_ERRORS, facAuthorisedDto.retrieveValidationResult());
        }
        Nodes.needValidation(approvalAppRoot, currentNodePath);

        ApprovalSelectionDto approvalSelectionDto = getApprovalSelectionDto(request);
        List<FacilityAuthoriserDto> facilityAuthDtoList = (List<FacilityAuthoriserDto>) ParamUtil.getSessionAttr(request,KEY_AUTH_PERSON_LIST);
        if(CollectionUtils.isEmpty(facilityAuthDtoList)){
            facilityAuthDtoList = approvalBatAndActivityClient.getApprovalSelectAuthorisedPersonnelByFacId(approvalSelectionDto.getFacilityId());
            ParamUtil.setSessionAttr(request,KEY_AUTH_PERSON_LIST,new ArrayList<>(facilityAuthDtoList));
        }
        ParamUtil.setRequestAttr(request,KEY_AUTH_ID_SELECTION_MAP, facAuthorisedDto.getAuthorisedSelectionMap());
        if (!CollectionUtils.isEmpty(editableFieldSet)) {
            ParamUtil.setRequestAttr(request, KEY_EDITABLE_FIELD_SET, String.join(",", editableFieldSet));
        }
    }

    public void handleFacAuthorised(BaseProcessClass bpc, FieldEditableJudger editableJudger, String appType) {
        HttpServletRequest request = bpc.request;
        ApprovalBatAndActivityDto oldApprovalBatAndActivityDto = (ApprovalBatAndActivityDto) ParamUtil.getSessionAttr(request, KEY_OLD_APPROVAL_BAT_AND_ACTIVITY_DTO);
        NodeGroup approvalAppRoot = getApprovalActivityRoot(request, null);
        String currentNodePath = NODE_NAME_APP_INFO + approvalAppRoot.getPathSeparator() + NODE_NAME_FAC_AUTHORISED;
        SimpleNode facAuthorisedNode = (SimpleNode) approvalAppRoot.at(currentNodePath);
        FacAuthorisedDto facAuthorisedDto = (FacAuthorisedDto) facAuthorisedNode.getValue();
        facAuthorisedDto.reqObjMapping(request, editableJudger);
        if (MasterCodeConstants.APP_TYPE_RFC.equals(appType)) {
            // compare old and new dto, to judge whether need to create task
            FacAuthorisedDto oldFacAuthorisedDto = oldApprovalBatAndActivityDto.getFacAuthorisedDto();
            Set<String> changedKeys = JaversUtil.compareCollections(oldFacAuthorisedDto.getFacAuthorisedSelections(), facAuthorisedDto.getFacAuthorisedSelections(), AuthorisedSelection.class);
            if (!CollectionUtils.isEmpty(changedKeys)) {
                RfcFlowType rfcFlowType = (RfcFlowType) ParamUtil.getSessionAttr(request, KEY_RFC_FLOW_TYPE);
                Set<String> facAuthoriserChangedKeys = new HashSet<>(changedKeys.size());
                changedKeys.forEach(key -> facAuthoriserChangedKeys.add("authorisedPersonnel."+key));
                rfcFlowType = getRfcFlowType(rfcFlowType, facAuthoriserChangedKeys);
                ParamUtil.setSessionAttr(request, KEY_RFC_FLOW_TYPE, rfcFlowType);
            }
        }

        String actionType = ParamUtil.getString(request, KEY_ACTION_TYPE);
        if (KEY_ACTION_JUMP.equals(actionType)) {
            jumpHandler(request,approvalAppRoot,currentNodePath,facAuthorisedNode);
        } else if (KEY_ACTION_SAVE_AS_DRAFT.equals(actionType)) {
            ParamUtil.setRequestAttr(request, KEY_ACTION_TYPE, KEY_ACTION_SAVE_AS_DRAFT);
            ParamUtil.setSessionAttr(request, KEY_JUMP_DEST_NODE, currentNodePath);
        } else {
            throw new IaisRuntimeException(ERR_MSG_INVALID_ACTION);
        }
        ParamUtil.setSessionAttr(request, KEY_ROOT_NODE_GROUP, approvalAppRoot);
    }

    public void preActivityDetails(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        NodeGroup approvalAppRoot = getApprovalActivityRoot(request, null);
        String currentNodePath = NODE_NAME_APP_INFO + approvalAppRoot.getPathSeparator() + NODE_NAME_FAC_ACTIVITY;
        SimpleNode facActivityNode = (SimpleNode) approvalAppRoot.at(currentNodePath);
        ApprovalToActivityDto approvalToActivityDto = (ApprovalToActivityDto) facActivityNode.getValue();
        Boolean needShowError = (Boolean) ParamUtil.getRequestAttr(request, KEY_SHOW_ERROR_SWITCH);
        if (needShowError == Boolean.TRUE) {
            ParamUtil.setRequestAttr(request, KEY_VALIDATION_ERRORS, approvalToActivityDto.retrieveValidationResult());
        }
        Nodes.needValidation(approvalAppRoot, currentNodePath);
        ParamUtil.setRequestAttr(request, KEY_APPROVAL_TO_ACTIVITY_DTO, approvalToActivityDto);
        // get facilityId
        ApprovalSelectionDto approvalSelectionDto = getApprovalSelectionDto(request);
        String facilityId = approvalSelectionDto.getFacilityId();
        // show that belongs to this classification without approval facActivityType
        Map<String,String> allActivityMapWithApproved = approvalBatAndActivityClient.getNotApprovalActivities(facilityId);
        ParamUtil.setRequestAttr(request, KEY_FAC_ACTIVITY_TYPE_APPROVAL_WITH_APPROVED_LIST, allActivityMapWithApproved);
    }

    public void handleActivityDetails(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        NodeGroup approvalAppRoot = getApprovalActivityRoot(request, null);
        String currentNodePath = NODE_NAME_APP_INFO + approvalAppRoot.getPathSeparator() + NODE_NAME_FAC_ACTIVITY;
        SimpleNode facActivityNode = (SimpleNode) approvalAppRoot.at(currentNodePath);
        ApprovalToActivityDto approvalToActivityDto = (ApprovalToActivityDto) facActivityNode.getValue();
        // get facilityId
        ApprovalSelectionDto approvalSelectionDto = getApprovalSelectionDto(request);
        String facilityId = approvalSelectionDto.getFacilityId();
        approvalToActivityDto.setFacilityId(facilityId);
        approvalToActivityDto.reqObjMapping(request);

        String actionType = ParamUtil.getString(request, KEY_ACTION_TYPE);
        if (KEY_ACTION_JUMP.equals(actionType)) {
            jumpHandler(request, approvalAppRoot, currentNodePath, facActivityNode);
        } else if (KEY_ACTION_SAVE_AS_DRAFT.equals(actionType)) {
            ParamUtil.setRequestAttr(request, KEY_ACTION_TYPE, KEY_ACTION_SAVE_AS_DRAFT);
            ParamUtil.setSessionAttr(request, KEY_JUMP_DEST_NODE, currentNodePath);
        } else {
            throw new IaisRuntimeException(ERR_MSG_INVALID_ACTION);
        }
        ParamUtil.setSessionAttr(request, KEY_ROOT_NODE_GROUP, approvalAppRoot);
    }

    @SneakyThrows(JsonProcessingException.class)
    public void prePrimaryDoc(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        String processType = (String) ParamUtil.getSessionAttr(request, KEY_PROCESS_TYPE);
        NodeGroup approvalAppRoot = getApprovalActivityRoot(request, null);
        SimpleNode primaryDocNode = (SimpleNode) approvalAppRoot.at(NODE_NAME_PRIMARY_DOC);
        PrimaryDocDto primaryDocDto = (PrimaryDocDto) primaryDocNode.getValue();
        Boolean needShowError = (Boolean) ParamUtil.getRequestAttr(request, KEY_SHOW_ERROR_SWITCH);
        if (needShowError == Boolean.TRUE) {
            ParamUtil.setRequestAttr(request, KEY_VALIDATION_ERRORS, primaryDocDto.retrieveValidationResult());
        }
        Nodes.needValidation(approvalAppRoot, NODE_NAME_PRIMARY_DOC);

        List<DocSetting> approvalAppDocSettings = docSettingService.getApprovalAppDocSettings(processType);
        ParamUtil.setRequestAttr(request, KEY_DOC_SETTINGS, approvalAppDocSettings);

        Map<String, List<DocRecordInfo>> savedFiles = primaryDocDto.getExistDocTypeMap();
        Map<String, List<NewDocInfo>> newFiles = primaryDocDto.getNewDocTypeMap();
        ParamUtil.setRequestAttr(request, "savedFiles", savedFiles);
        ParamUtil.setRequestAttr(request, "newFiles", newFiles);

        Set<String> otherDocTypes = DocSettingService.computeOtherDocTypes(approvalAppDocSettings, savedFiles.keySet(), newFiles.keySet());
        ParamUtil.setRequestAttr(request, KEY_OTHER_DOC_TYPES, otherDocTypes);

        List<SelectOption> docTypeOps = MasterCodeHolder.APPROVAL_FOR_FACILITY_DOCUMENT_TYPE.allOptions();
        ParamUtil.setRequestAttr(request, KEY_OPTIONS_DOC_TYPES, docTypeOps);
        ObjectMapper mapper = new ObjectMapper();
        String docTypeOpsJson = mapper.writeValueAsString(docTypeOps);
        ParamUtil.setRequestAttr(request, KEY_DOC_TYPES_JSON, docTypeOpsJson);
    }

    public void handlePrimaryDoc(BaseProcessClass bpc, String appType) {
        HttpServletRequest request = bpc.request;
        NodeGroup approvalAppRoot = getApprovalActivityRoot(request, null);
        SimpleNode primaryDocNode = (SimpleNode) approvalAppRoot.at(NODE_NAME_PRIMARY_DOC);
        PrimaryDocDto primaryDocDto = (PrimaryDocDto) primaryDocNode.getValue();
        primaryDocDto.reqObjMapping(request);
        // compare old and new dto, to judge whether need to create task
        if (MasterCodeConstants.APP_TYPE_RFC.equals(appType)) {
            if (!CollectionUtils.isEmpty(primaryDocDto.getNewDocMap()) || !CollectionUtils.isEmpty(primaryDocDto.getToBeDeletedRepoIds())) {
                RfcFlowType rfcFlowType = (RfcFlowType) ParamUtil.getSessionAttr(request, KEY_RFC_FLOW_TYPE);
                Set<String> changedKeys = new HashSet<>(1);
                changedKeys.add("approvalSupportingDoc");
                rfcFlowType = getRfcFlowType(rfcFlowType, changedKeys);
                ParamUtil.setSessionAttr(request, KEY_RFC_FLOW_TYPE, rfcFlowType);
            }
        }

        String actionType = ParamUtil.getString(request, KEY_ACTION_TYPE);
        if (KEY_ACTION_JUMP.equals(actionType)) {
            jumpHandler(request, approvalAppRoot, NODE_NAME_PRIMARY_DOC, primaryDocNode);
        } else if (KEY_ACTION_SAVE_AS_DRAFT.equals(actionType)) {
            ParamUtil.setRequestAttr(request, KEY_ACTION_TYPE, KEY_ACTION_SAVE_AS_DRAFT);
            ParamUtil.setSessionAttr(request, KEY_JUMP_DEST_NODE, NODE_NAME_PRIMARY_DOC);
        } else {
            throw new IaisRuntimeException(ERR_MSG_INVALID_ACTION);
        }
        ParamUtil.setSessionAttr(request, KEY_ROOT_NODE_GROUP, approvalAppRoot);
    }

    public void prePreview(BaseProcessClass bpc) {
        preparePreviewData(bpc);
        preparePrintMaskId(bpc.request);
    }

    public void preAcknowledge(BaseProcessClass bpc) {
        preparePrintMaskId(bpc.request);
    }

    public void print(BaseProcessClass bpc) {
        validatePrintMaskId(bpc.request);
        preparePreviewData(bpc);
    }

    /**
     * Sets print if into request, used by js function in JSP
     */
    public void preparePrintMaskId(HttpServletRequest request) {
        NodeGroup approvalAppRoot = getApprovalActivityRoot(request, null);
        String maskForPrint = MaskUtil.maskValue(KEY_PRINT_MASK_PARAM, String.valueOf(approvalAppRoot.hashCode()));
        ParamUtil.setRequestAttr(request, KEY_PRINT_MASKED_ID, maskForPrint);
    }

    /**
     * Validates if it is allowed to print the approval application details.
     * If it is not allowed, an exception will be thrown
     */
    public void validatePrintMaskId(HttpServletRequest request) {
        String maskedId = ParamUtil.getString(request, "printId");
        boolean allowToPrint = false;
        if (org.springframework.util.StringUtils.hasLength(maskedId)) {
            MaskUtil.unMaskValue(KEY_PRINT_MASK_PARAM, maskedId);
            // if it can not be unmasked, an exception is thrown
            allowToPrint = true;
        }
        if (!allowToPrint) {
            throw new IaisRuntimeException("Invalid mask key, don't allow to print");
        }
    }

    public void actionFilter(BaseProcessClass bpc, String appType) {
        HttpServletRequest request = bpc.request;
        // check if there is action set to override the action from request
        String actionType = (String) ParamUtil.getRequestAttr(request, KEY_ACTION_TYPE);
        if (!org.springframework.util.StringUtils.hasLength(actionType)) {
            // not set, use action from user's client
            actionType = ParamUtil.getString(request, KEY_ACTION_TYPE);
        } else {
            // set, if the action is 'save draft', we save it and route back to that page
            if (KEY_ACTION_SAVE_AS_DRAFT.equals(actionType)) {
                actionType = KEY_ACTION_JUMP;
                saveDraft(request, appType);
            }
        }
        ParamUtil.setRequestAttr(request, KEY_INDEED_ACTION_TYPE, actionType);
    }

    public void jumpFilter(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        String destNode = (String) ParamUtil.getSessionAttr(request, KEY_JUMP_DEST_NODE);
        ParamUtil.setRequestAttr(request, KEY_DEST_NODE_ROUTE, destNode);
    }

    //----------------------------------------------------------------------------------------------
    public ApprovalBatAndActivityDto getRfcBatDataByApprovalId(String approvalId) {
        ResponseDto<ApprovalBatAndActivityDto> responseDto = approvalBatAndActivityClient.retrieveApprovalAppAppDataByApprovalId(approvalId);
        if (responseDto.ok()) {
            return responseDto.getEntity();
        } else {
            throw new IaisRuntimeException("Fail to retrieve edit approval app data");
        }
    }

    public ApprovalBatAndActivityDto getEditDtoData(String appId) {
        ResponseDto<ApprovalBatAndActivityDto> responseDto = approvalBatAndActivityClient.getApprovalAppAppDataByApplicationId(appId);
        if (responseDto.ok()) {
            return responseDto.getEntity();
        } else {
            throw new IaisRuntimeException("Fail to retrieve edit approval app data");
        }
    }

    public ApprovalSelectionDto getApprovalSelectionDto(HttpServletRequest request) {
        ApprovalSelectionDto approvalSelectionDto = (ApprovalSelectionDto) ParamUtil.getSessionAttr(request, KEY_APPROVAL_SELECTION_DTO);
        if (approvalSelectionDto == null) {
            approvalSelectionDto = new ApprovalSelectionDto();
        }
        return approvalSelectionDto;
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
        SimpleNode possessBatNode = new SimpleNode(new ApprovalToPossessDto(ValidationConstants.VALIDATION_PROFILE_NEW), NODE_NAME_POSSESS_BAT, new Node[]{facProfileNode});

        return new NodeGroup.Builder().name(NODE_NAME_APP_INFO)
                .dependNodes(dependNodes)
                .addNode(facProfileNode)
                .addNode(possessBatNode)
                .build();
    }

    private NodeGroup newApprovalLargeAppInfoNodeGroup(Node[] dependNodes) {
        Node facProfileNode = new Node(NODE_NAME_FAC_PROFILE, new Node[0]);
        SimpleNode largeBatNode = new SimpleNode(new ApprovalToLargeDto(ValidationConstants.VALIDATION_PROFILE_NEW), NODE_NAME_LARGE_BAT, new Node[]{facProfileNode});

        return new NodeGroup.Builder().name(NODE_NAME_APP_INFO)
                .dependNodes(dependNodes)
                .addNode(facProfileNode)
                .addNode(largeBatNode)
                .build();
    }

    private NodeGroup newApprovalSpecialAppInfoNodeGroup(Node[] dependNodes) {
        Node facProfileNode = new Node(NODE_NAME_FAC_PROFILE, new Node[0]);
        SimpleNode specialBatNode = new SimpleNode(new ApprovalToSpecialDto(ValidationConstants.VALIDATION_PROFILE_NEW), NODE_NAME_SPECIAL_BAT, new Node[]{facProfileNode});
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

    public void saveDraft(HttpServletRequest request, String appType) {
        NodeGroup approvalAppRoot = getApprovalActivityRoot(request, "");

        // save docs
        PrimaryDocDto primaryDocDto = (PrimaryDocDto) ((SimpleNode) approvalAppRoot.at(NODE_NAME_PRIMARY_DOC)).getValue();
        List<NewFileSyncDto> newFilesToSync = saveNewUploadedDoc(primaryDocDto);


        // save data
        ApprovalBatAndActivityDto finalAllDataDto = null;
        ApprovalSelectionDto approvalSelectionDto = getApprovalSelectionDto(request);
        if (appType.equals(MasterCodeConstants.APP_TYPE_NEW) || appType.equals(MasterCodeConstants.APP_TYPE_RFC)) {
            finalAllDataDto = ApprovalBatAndActivityDto.from(approvalSelectionDto, approvalAppRoot, request);
            FacProfileDto facProfileDto = (FacProfileDto) ParamUtil.getSessionAttr(request, KEY_FAC_PROFILE_DTO);
            finalAllDataDto.setFacProfileDto(facProfileDto);
            finalAllDataDto.setAppType(appType);
        }
        String draftAppNo = approvalBatAndActivityClient.saveNewFacilityDraft(finalAllDataDto);
        // set draft app No. into the NodeGroup
        approvalSelectionDto.setDraftAppNo(draftAppNo);
        ParamUtil.setSessionAttr(request, KEY_APPROVAL_SELECTION_DTO, approvalSelectionDto);
        ParamUtil.setSessionAttr(request, KEY_ROOT_NODE_GROUP, approvalAppRoot);

        try {
            // delete docs
            log.info("Delete already saved documents in file-repo");
            List<String> toBeDeletedRepoIds = deleteUnwantedDoc(primaryDocDto);
            // sync docs
            syncNewDocsAndDeleteFiles(newFilesToSync, toBeDeletedRepoIds);
        } catch (Exception e) {
            log.error("Fail to sync files to BE", e);
        }

        ParamUtil.setRequestAttr(request, KEY_IND_AFTER_SAVE_AS_DRAFT, Boolean.TRUE);
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
        Assert.notNull(group, ApprovalBatAndActivityConstants.ERR_MSG_NULL_GROUP);
        Assert.notNull(name, ApprovalBatAndActivityConstants.ERR_MSG_NULL_NAME);
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
        Assert.notNull(group, ApprovalBatAndActivityConstants.ERR_MSG_NULL_GROUP);
        Assert.notNull(name, ApprovalBatAndActivityConstants.ERR_MSG_NULL_NAME);
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
        Assert.notNull(group, ApprovalBatAndActivityConstants.ERR_MSG_NULL_GROUP);
        Assert.notNull(name, ApprovalBatAndActivityConstants.ERR_MSG_NULL_NAME);
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

    private void commonJudgeRfcFlowType(HttpServletRequest request, Set<String> changedKeys) {
        RfcFlowType rfcFlowType = (RfcFlowType) ParamUtil.getSessionAttr(request, KEY_RFC_FLOW_TYPE);
        if(!CollectionUtils.isEmpty(changedKeys)) {
            Set<String> commonKeySet = new HashSet<>(1);
            commonKeySet.add("bat.others");
            rfcFlowType = getRfcFlowType(rfcFlowType, commonKeySet);
        }
        ParamUtil.setSessionAttr(request, KEY_RFC_FLOW_TYPE, rfcFlowType);
    }

    private RfcFlowType getRfcFlowType(RfcFlowType rfcFlowType, Set<String> commonKeySet) {
        RfcFlowType newRfcFlowType = rfcFlowDecider.decide4Approval(commonKeySet);
        if (rfcFlowType == null || newRfcFlowType.ordinal() - rfcFlowType.ordinal() > 0) {
            rfcFlowType = newRfcFlowType;
        }
        return rfcFlowType;
    }

    /**
     * Compute the destiny node path we will go to.
     * @param approvalAppRoot the root NodeGroup
     * @param actionValue     the value we received, it can be next, back, or a value specified by tab
     * @return the destiny node path, return null if we can't go the next or previous node
     */
    public String computeDestNodePath(NodeGroup approvalAppRoot, String actionValue) {
        String destNode;
        switch (actionValue) {
            case KEY_NAV_NEXT:
                destNode = Nodes.getNextNodePath(approvalAppRoot);
                break;
            case KEY_NAV_PREVIOUS:
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
     * Save new uploaded documents into FE file repo.
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

    /**
     * Delete unwanted documents in FE file repo.
     * This method will remove the repoId from the toBeDeletedRepoIds set after a call of removal.
     * @param primaryDocDto document DTO have the specific structure
     * @return a list of repo IDs deleted in FE file repo
     */
    public List<String> deleteUnwantedDoc(PrimaryDocDto primaryDocDto) {
        /* Ignore the failure when try to delete FE files because this is not a big issue.
         * The not deleted file won't be retrieved, so it's just a waste of disk space */
        List<String> toBeDeletedRepoIds = new ArrayList<>(primaryDocDto.getToBeDeletedRepoIds());
        for (String id : toBeDeletedRepoIds) {
            FileRepoDto fileRepoDto = new FileRepoDto();
            fileRepoDto.setId(id);
            fileRepoClient.removeFileById(fileRepoDto);
            primaryDocDto.getToBeDeletedRepoIds().remove(id);
        }
        return toBeDeletedRepoIds;
    }

    /**
     * Sync new uploaded documents to BE; delete unwanted documents in BE too.
     * @param newFilesToSync     a list of DTOs contains ID and data
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

        boolean isProcModeImport = false;
        String processType = (String) ParamUtil.getSessionAttr(request, KEY_PROCESS_TYPE);
        switch (processType) {
            case MasterCodeConstants.PROCESS_TYPE_APPROVE_POSSESS:
                ApprovalToPossessDto dto = (ApprovalToPossessDto) ((SimpleNode) approvalAppRoot.at(NODE_NAME_APP_INFO + approvalAppRoot.getPathSeparator() + NODE_NAME_POSSESS_BAT)).getValue();
                ParamUtil.setRequestAttr(request, KEY_BAT_INFO, dto);
                isProcModeImport = dto.getBatInfos().stream().map(i -> i.getDetails().getProcurementMode()).collect(Collectors.toSet()).contains(MasterCodeConstants.PROCUREMENT_MODE_IMPORT);
                break;
            case MasterCodeConstants.PROCESS_TYPE_APPROVE_LSP:
                ApprovalToLargeDto lspDto = (ApprovalToLargeDto) ((SimpleNode) approvalAppRoot.at(NODE_NAME_APP_INFO + approvalAppRoot.getPathSeparator() + NODE_NAME_LARGE_BAT)).getValue();
                ParamUtil.setRequestAttr(request, KEY_BAT_INFO, lspDto);
                isProcModeImport = lspDto.getBatInfos().stream().map(i -> i.getDetails().getProcurementMode()).collect(Collectors.toSet()).contains(MasterCodeConstants.PROCUREMENT_MODE_IMPORT);
                break;
            case MasterCodeConstants.PROCESS_TYPE_SP_APPROVE_HANDLE:
                ParamUtil.setRequestAttr(request, KEY_BAT_INFO, ((SimpleNode) approvalAppRoot.at(NODE_NAME_APP_INFO + approvalAppRoot.getPathSeparator() + NODE_NAME_SPECIAL_BAT)).getValue());
                FacAuthorisedDto facAuthorisedDto = (FacAuthorisedDto) ((SimpleNode) approvalAppRoot.at(NODE_NAME_APP_INFO + approvalAppRoot.getPathSeparator() + NODE_NAME_FAC_AUTHORISED)).getValue();

                //facility authorised person id which checkbox is selected before
                List<String> facAuthPersonIds = facAuthorisedDto.getAuthorisedPersonIdList();
                List<FacilityAuthoriserDto> facilityAuthListSelected = approvalBatAndActivityClient.getAuthorisedPersonnelByAuthIds(facAuthPersonIds);
                ParamUtil.setRequestAttr(request, KEY_FAC_AUTHORISED_PERSON_LIST_WANTED, facilityAuthListSelected);
                break;
            case MasterCodeConstants.PROCESS_TYPE_APPROVAL_FOR_FACILITY_ACTIVITY_TYPE:
                ParamUtil.setRequestAttr(request, KEY_BAT_INFO, ((SimpleNode) approvalAppRoot.at(NODE_NAME_APP_INFO + approvalAppRoot.getPathSeparator() + NODE_NAME_FAC_ACTIVITY)).getValue());
                break;
            default:
                log.info("no such processType {}", StringUtils.normalizeSpace(processType));
                break;
        }
        List<DocSetting> approvalAppDocSettings = docSettingService.getApprovalAppDocSettings(processType);
        ParamUtil.setRequestAttr(request, "docSettings", approvalAppDocSettings);
        PrimaryDocDto primaryDocDto = (PrimaryDocDto) ((SimpleNode) approvalAppRoot.at(NODE_NAME_PRIMARY_DOC)).getValue();
        Map<String, List<DocRecordInfo>> savedFiles = primaryDocDto.getExistDocTypeMap();
        Map<String, List<NewDocInfo>> newFiles = primaryDocDto.getNewDocTypeMap();
        ParamUtil.setRequestAttr(request, "savedFiles", savedFiles);
        ParamUtil.setRequestAttr(request, "newFiles", newFiles);
        ParamUtil.setSessionAttr(request, "isProcModeImport", isProcModeImport);

        Set<String> otherDocTypes = DocSettingService.computeOtherDocTypes(approvalAppDocSettings, savedFiles.keySet(), newFiles.keySet());
        ParamUtil.setRequestAttr(request, KEY_OTHER_DOC_TYPES, otherDocTypes);
    }

    public void loadAllowedScheduleAndBatOptions(HttpServletRequest request) {
        ApprovalSelectionDto approvalSelectionDto = getApprovalSelectionDto(request);
        FacProfileDto facProfileDto = (FacProfileDto) ParamUtil.getSessionAttr(request, KEY_FAC_PROFILE_DTO);
        boolean protectedPlace = MasterCodeConstants.YES.equals(facProfileDto.getProtectedPlace());

        RichScheduleBatInfo richScheduleBatInfo = approvalBatAndActivityClient.queryScheduleBasedBatBasicInfo(approvalSelectionDto.getFacilityId(), protectedPlace, getApprovalTypeByProcessType(approvalSelectionDto.getProcessType()));
        Map<RichSchedule, List<RichBatCodeInfo>> scheduleBatMap = richScheduleBatInfo.getScheduleBatMap();
        List<RichSelectOption> richScheduleTypeOps = RichSchedule.customRichOption(scheduleBatMap.keySet());
        ParamUtil.setRequestAttr(request, FacRegisterConstants.KEY_OPTIONS_SCHEDULE, richScheduleTypeOps);
        ParamUtil.setRequestAttr(request, FacRegisterConstants.KEY_SCHEDULE_FIRST_OPTION, Optional.of(richScheduleTypeOps).filter(l -> !l.isEmpty()).map(l -> l.get(0)).map(SelectOption::getValue).orElse(null));

        // convert BatBasicInfo to RichSelectOption object
        Map<String, List<RichSelectOption>> scheduleRichBatOptionMap = Maps.newHashMapWithExpectedSize(scheduleBatMap.size());
        for (Map.Entry<RichSchedule, List<RichBatCodeInfo>> entry : scheduleBatMap.entrySet()) {
            List<RichSelectOption> optionList = new ArrayList<>(entry.getValue().size() + 1);
            optionList.add(new RichSelectOption("", "Please Select", false));
            for (RichBatCodeInfo info : entry.getValue()) {
                RichSelectOption option = new RichSelectOption(info.getCode(), info.getName(), info.isDisable());
                optionList.add(option);
            }
            scheduleRichBatOptionMap.put(entry.getKey().getSchedule(), optionList);
        }
        ParamUtil.setRequestAttr(request, FacRegisterConstants.KEY_SCHEDULE_BAT_MAP, scheduleRichBatOptionMap);
        String scheduleBatMapJson = JsonUtil.parseToJson(scheduleRichBatOptionMap);
        ParamUtil.setRequestAttr(request, FacRegisterConstants.KEY_SCHEDULE_BAT_MAP_JSON, scheduleBatMapJson);
    }

    public String getApprovalTypeByProcessType(String processType) {
        String approvalType = "";
        switch (processType) {
            case MasterCodeConstants.PROCESS_TYPE_APPROVE_POSSESS:
                approvalType = MasterCodeConstants.APPROVAL_TYPE_POSSESS;
                break;
            case MasterCodeConstants.PROCESS_TYPE_APPROVE_LSP:
                approvalType = MasterCodeConstants.APPROVAL_TYPE_LSP;
                break;
            case MasterCodeConstants.PROCESS_TYPE_SP_APPROVE_HANDLE:
                approvalType = MasterCodeConstants.APPROVAL_TYPE_SP_HANDLE;
                break;
            default:
                log.info("No such processType matches {}", StringUtils.normalizeSpace(processType));
                break;
        }
        return approvalType;
    }

    public void prepareRfcPreviewData(HttpServletRequest request) {
        ApprovalBatAndActivityDto approvalBatAndActivityDto = (ApprovalBatAndActivityDto) ParamUtil.getSessionAttr(request, KEY_APPROVAL_BAT_AND_ACTIVITY_DTO);
        FacProfileDto facProfileDto = approvalBatAndActivityDto.getFacProfileDto();
        ParamUtil.setRequestAttr(request, KEY_FAC_PROFILE_DTO, facProfileDto);

        String processType = approvalBatAndActivityDto.getApprovalSelectionDto().getProcessType();
        ParamUtil.setRequestAttr(request, KEY_PROCESS_TYPE, processType);

        switch (processType) {
            case MasterCodeConstants.PROCESS_TYPE_APPROVE_POSSESS:
                ApprovalToPossessDto dto = approvalBatAndActivityDto.getApprovalToPossessDto();
                ParamUtil.setRequestAttr(request, KEY_BAT_INFO, dto);
                break;
            case MasterCodeConstants.PROCESS_TYPE_APPROVE_LSP:
                ApprovalToLargeDto lspDto = approvalBatAndActivityDto.getApprovalToLargeDto();
                ParamUtil.setRequestAttr(request, KEY_BAT_INFO, lspDto);
                break;
            case MasterCodeConstants.PROCESS_TYPE_SP_APPROVE_HANDLE:
                ParamUtil.setRequestAttr(request, KEY_BAT_INFO, approvalBatAndActivityDto.getApprovalToSpecialDto());
                FacAuthorisedDto facAuthorisedDto = approvalBatAndActivityDto.getFacAuthorisedDto();
                List<String> authIdList = facAuthorisedDto.getAuthorisedPersonIdList();
                List<FacilityAuthoriserDto> authDtoList = approvalBatAndActivityClient.getAuthorisedPersonnelByAuthIds(authIdList);
                ParamUtil.setRequestAttr(request, KEY_FAC_AUTHORISED_PERSON_LIST_WANTED, authDtoList);
                break;
            case MasterCodeConstants.PROCESS_TYPE_APPROVAL_FOR_FACILITY_ACTIVITY_TYPE:
                ParamUtil.setRequestAttr(request, KEY_BAT_INFO, approvalBatAndActivityDto.getApprovalToActivityDto());
                break;
            default:
                log.info("no such processType {}", org.apache.commons.lang.StringUtils.normalizeSpace(processType));
                break;
        }
        List<DocSetting> approvalAppDocSettings = docSettingService.getApprovalAppDocSettings(processType);
        ParamUtil.setRequestAttr(request, "docSettings", approvalAppDocSettings);
        PrimaryDocDto primaryDocDto = new PrimaryDocDto();
        primaryDocDto.setSavedDocMap(sg.gov.moh.iais.egp.bsb.util.CollectionUtils.uniqueIndexMap(DocRecordInfo::getRepoId, approvalBatAndActivityDto.getDocRecordInfos()));
        Map<String, List<DocRecordInfo>> savedFiles = primaryDocDto.getExistDocTypeMap();
        ParamUtil.setRequestAttr(request, "savedFiles", savedFiles);

        Set<String> otherDocTypes = DocSettingService.computeOtherDocTypes(approvalAppDocSettings, savedFiles.keySet());
        ParamUtil.setRequestAttr(request, KEY_OTHER_DOC_TYPES, otherDocTypes);

        String maskForPrint = MaskUtil.maskValue(KEY_PRINT_MASK_PARAM, String.valueOf(approvalBatAndActivityDto.hashCode()));
        ParamUtil.setRequestAttr(request, KEY_PRINT_MASKED_ID, maskForPrint);
    }


    /**
     * In RFC view page when user click 'Edit' call this method to jump to target Node
     */
    public void handleRfcViewData(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        String actionValue = ParamUtil.getString(request, KEY_ACTION_VALUE);
        String actionType = ParamUtil.getString(request, FacRegisterConstants.KEY_ACTION_TYPE);

        // Designed for the RFC, to jump to any node
        NodeGroup approvalAppRoot = (NodeGroup) ParamUtil.getSessionAttr(request, KEY_ROOT_NODE_GROUP);
        String destNode = computeDestNodePath(approvalAppRoot, actionValue);
        Node targetNode = approvalAppRoot.at(destNode);
        if (FacRegisterConstants.KEY_ACTION_JUMP.equals(actionType)) {
            jumpHandler(request, approvalAppRoot, destNode, targetNode);
        } else {
            throw new IaisRuntimeException(FacRegisterConstants.ERR_MSG_INVALID_ACTION);
        }

        ApprovalBatAndActivityDto approvalBatAndActivityDto = (ApprovalBatAndActivityDto) ParamUtil.getSessionAttr(request, KEY_APPROVAL_BAT_AND_ACTIVITY_DTO);
        ParamUtil.setSessionAttr(request, KEY_FAC_PROFILE_DTO, approvalBatAndActivityDto.getFacProfileDto());
        ParamUtil.setSessionAttr(request, KEY_ROOT_NODE_GROUP, approvalAppRoot);
    }
}

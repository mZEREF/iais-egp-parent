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
import sg.gov.moh.iais.egp.bsb.client.FacilityRegisterClient;
import sg.gov.moh.iais.egp.bsb.common.edit.FieldEditableSetJudger;
import sg.gov.moh.iais.egp.bsb.common.node.Node;
import sg.gov.moh.iais.egp.bsb.common.node.NodeGroup;
import sg.gov.moh.iais.egp.bsb.common.node.Nodes;
import sg.gov.moh.iais.egp.bsb.common.node.simple.SimpleNode;
import sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants;
import sg.gov.moh.iais.egp.bsb.constant.ValidationConstants;
import sg.gov.moh.iais.egp.bsb.constant.module.SubmissionBlockConstants;
import sg.gov.moh.iais.egp.bsb.dto.ResponseDto;
import sg.gov.moh.iais.egp.bsb.dto.info.common.AppMainInfo;
import sg.gov.moh.iais.egp.bsb.dto.register.bat.BiologicalAgentToxinDto;
import sg.gov.moh.iais.egp.bsb.dto.register.facility.FacilityAuthoriserDto;
import sg.gov.moh.iais.egp.bsb.dto.register.facility.FacilityAuthoriserFileDto;
import sg.gov.moh.iais.egp.bsb.dto.register.facility.FacilityCommitteeDto;
import sg.gov.moh.iais.egp.bsb.dto.register.facility.FacilityCommitteeFileDto;
import sg.gov.moh.iais.egp.bsb.dto.register.facility.FacilityProfileDto;
import sg.gov.moh.iais.egp.bsb.dto.register.facility.FacilityRegisterDto;
import sg.gov.moh.iais.egp.bsb.dto.register.facility.FacilitySelectionDto;
import sg.gov.moh.iais.egp.bsb.dto.register.facility.PreviewSubmitDto;
import sg.gov.moh.iais.egp.bsb.dto.register.facility.PrimaryDocDto;
import sg.gov.moh.iais.egp.bsb.service.DocDtoService;
import sg.gov.moh.iais.egp.bsb.service.DocSettingService;
import sg.gov.moh.iais.egp.bsb.service.FacilityRegistrationService;
import sg.gov.moh.iais.egp.bsb.service.OrganizationInfoService;
import sg.gov.moh.iais.egp.bsb.service.SubmissionBlockService;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.ecquaria.cloud.moh.iais.common.constant.BsbAuditTrailConstants.FUNCTION_FACILITY_REGISTRATION;
import static com.ecquaria.cloud.moh.iais.common.constant.BsbAuditTrailConstants.MODULE_RENEW;
import static sg.gov.moh.iais.egp.bsb.constant.FacRegisterConstants.ELIGIBLE_DRAFT_REGISTER_DTO;
import static sg.gov.moh.iais.egp.bsb.constant.FacRegisterConstants.ERR_MSG_INVALID_ACTION;
import static sg.gov.moh.iais.egp.bsb.constant.FacRegisterConstants.KEY_ACTION_EXPAND_FILE;
import static sg.gov.moh.iais.egp.bsb.constant.FacRegisterConstants.KEY_ACTION_JUMP;
import static sg.gov.moh.iais.egp.bsb.constant.FacRegisterConstants.KEY_ACTION_SAVE_AS_DRAFT;
import static sg.gov.moh.iais.egp.bsb.constant.FacRegisterConstants.KEY_ACTION_SUBMIT;
import static sg.gov.moh.iais.egp.bsb.constant.FacRegisterConstants.KEY_ACTION_TYPE;
import static sg.gov.moh.iais.egp.bsb.constant.FacRegisterConstants.KEY_ACTION_VALUE;
import static sg.gov.moh.iais.egp.bsb.constant.FacRegisterConstants.KEY_ALLOW_SAVE_DRAFT;
import static sg.gov.moh.iais.egp.bsb.constant.FacRegisterConstants.KEY_ALL_FIELD;
import static sg.gov.moh.iais.egp.bsb.constant.FacRegisterConstants.KEY_APP_DT;
import static sg.gov.moh.iais.egp.bsb.constant.FacRegisterConstants.KEY_APP_NO;
import static sg.gov.moh.iais.egp.bsb.constant.FacRegisterConstants.KEY_CURRENT_FAC_ID;
import static sg.gov.moh.iais.egp.bsb.constant.FacRegisterConstants.KEY_DATA_LIST;
import static sg.gov.moh.iais.egp.bsb.constant.FacRegisterConstants.KEY_FAC_ID;
import static sg.gov.moh.iais.egp.bsb.constant.FacRegisterConstants.KEY_INSTRUCTION_INFO;
import static sg.gov.moh.iais.egp.bsb.constant.FacRegisterConstants.KEY_IS_CF;
import static sg.gov.moh.iais.egp.bsb.constant.FacRegisterConstants.KEY_IS_FIFTH_RF;
import static sg.gov.moh.iais.egp.bsb.constant.FacRegisterConstants.KEY_IS_PV_RF;
import static sg.gov.moh.iais.egp.bsb.constant.FacRegisterConstants.KEY_IS_RF;
import static sg.gov.moh.iais.egp.bsb.constant.FacRegisterConstants.KEY_IS_UCF;
import static sg.gov.moh.iais.egp.bsb.constant.FacRegisterConstants.KEY_JUMP_DEST_NODE;
import static sg.gov.moh.iais.egp.bsb.constant.FacRegisterConstants.KEY_NAV_NEXT;
import static sg.gov.moh.iais.egp.bsb.constant.FacRegisterConstants.KEY_ROOT_NODE_GROUP;
import static sg.gov.moh.iais.egp.bsb.constant.FacRegisterConstants.KEY_SAMPLE_AUTHORISER;
import static sg.gov.moh.iais.egp.bsb.constant.FacRegisterConstants.KEY_SAMPLE_COMMITTEE;
import static sg.gov.moh.iais.egp.bsb.constant.FacRegisterConstants.KEY_SELECTED_ACTIVITIES;
import static sg.gov.moh.iais.egp.bsb.constant.FacRegisterConstants.KEY_SELECTED_CLASSIFICATION;
import static sg.gov.moh.iais.egp.bsb.constant.FacRegisterConstants.KEY_SHOW_ERROR_SWITCH;
import static sg.gov.moh.iais.egp.bsb.constant.FacRegisterConstants.NODE_NAME_COMPANY_INFO;
import static sg.gov.moh.iais.egp.bsb.constant.FacRegisterConstants.NODE_NAME_FAC_AUTH;
import static sg.gov.moh.iais.egp.bsb.constant.FacRegisterConstants.NODE_NAME_FAC_BAT_INFO;
import static sg.gov.moh.iais.egp.bsb.constant.FacRegisterConstants.NODE_NAME_FAC_COMMITTEE;
import static sg.gov.moh.iais.egp.bsb.constant.FacRegisterConstants.NODE_NAME_FAC_INFO;
import static sg.gov.moh.iais.egp.bsb.constant.FacRegisterConstants.NODE_NAME_FAC_PROFILE;
import static sg.gov.moh.iais.egp.bsb.constant.FacRegisterConstants.NODE_NAME_FAC_SELECTION;
import static sg.gov.moh.iais.egp.bsb.constant.FacRegisterConstants.NODE_NAME_INSTRUCTION;
import static sg.gov.moh.iais.egp.bsb.constant.FacRegisterConstants.NODE_NAME_PREVIEW_SUBMIT;
import static sg.gov.moh.iais.egp.bsb.constant.FacRegisterConstants.NODE_NAME_PRIMARY_DOC;
import static sg.gov.moh.iais.egp.bsb.constant.FacRegisterConstants.STEP_NAME_AUTHORISER_PREVIEW;
import static sg.gov.moh.iais.egp.bsb.constant.FacRegisterConstants.STEP_NAME_COMMITTEE_PREVIEW;
import static sg.gov.moh.iais.egp.bsb.service.OrganizationInfoService.KEY_ORG_ADDRESS;


@Slf4j
@RequiredArgsConstructor
@Delegator("renewalFacilityRegisterDelegator")
public class RenewalFacilityRegistrationDelegator {
    private final FacilityRegisterClient facRegClient;
    private final FacilityRegistrationService facilityRegistrationService;
    private final DocSettingService docSettingService;
    private final OrganizationInfoService organizationInfoService;
    private final DocDtoService docDtoService;
    private final SubmissionBlockService submissionBlockService;

    private final Set<String> batEditFields = new HashSet<>(Arrays.asList(
            "exportingRetrieveAddressBtn",
            "localTransferRetrieveAddressBtn",
            "sampleType",
            "workType",
            "sampleWorkDetail",
            "estimatedMaximumVolume",
            "methodOrSystem",
            "procurementMode",
            "facNameT",
            "postalCodeT",
            "addressTypeT",
            "blockNoT",
            "floorNoT",
            "unitNoT",
            "streetNameT",
            "buildingNameT",
            "contactPersonNameT",
            "emailAddressT",
            "contactNoT",
            "expectedDateT",
            "courierServiceProviderNameT",
            "remarksT",
            "facNameE",
            "postalCodeE",
            "addressTypeE",
            "blockNoE",
            "floorNoE",
            "unitNoE",
            "streetNameE",
            "buildingNameE",
            "countryE",
            "cityE",
            "stateE",
            "contactPersonNameE",
            "emailAddressE",
            "contactNoE",
            "expectedDateE",
            "courierServiceProviderNameE",
            "remarksE"
    ));

    public void start(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        HttpSession session = request.getSession();
        session.removeAttribute(KEY_ROOT_NODE_GROUP);
        session.removeAttribute(KEY_ALLOW_SAVE_DRAFT);
        session.removeAttribute(KEY_JUMP_DEST_NODE);
        session.removeAttribute(KEY_SAMPLE_COMMITTEE);
        session.removeAttribute(KEY_SAMPLE_AUTHORISER);
        session.removeAttribute(KEY_ORG_ADDRESS);
        session.removeAttribute(KEY_IS_CF);
        session.removeAttribute(KEY_IS_UCF);
        session.removeAttribute(KEY_IS_RF);
        session.removeAttribute(KEY_IS_FIFTH_RF);
        session.removeAttribute(KEY_IS_PV_RF);
        session.removeAttribute(KEY_SELECTED_CLASSIFICATION);
        session.removeAttribute(KEY_SELECTED_ACTIVITIES);
        session.removeAttribute(ELIGIBLE_DRAFT_REGISTER_DTO);

        session.removeAttribute(SubmissionBlockConstants.BLOCK_RENEW_FACILITY);
        session.removeAttribute(KEY_INSTRUCTION_INFO);
        AuditTrailHelper.auditFunction(MODULE_RENEW, FUNCTION_FACILITY_REGISTRATION);
    }

    public void init(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        String maskedFacId = request.getParameter(KEY_FAC_ID);
        if (StringUtils.hasLength(maskedFacId)) {
            if (log.isInfoEnabled()) {
                log.info("masked fac ID: {}", LogUtil.escapeCrlf(maskedFacId));
            }
            boolean failRetrieveEditData = true;
            String facId = MaskUtil.unMaskValue(KEY_FAC_ID, maskedFacId);
            ParamUtil.setSessionAttr(request, KEY_CURRENT_FAC_ID, facId);
            if (facId != null && !maskedFacId.equals(facId)) {
                ResponseDto<FacilityRegisterDto> resultDto = facRegClient.getFacilityRegistrationAppDataByFacilityId(facId);
                if (resultDto.ok()) {
                    failRetrieveEditData = false;
                    FacilityRegisterDto facilityRegisterDto = resultDto.getEntity();
                    facilityRegistrationService.retrieveFacRegRoot(request, facilityRegisterDto);
                    organizationInfoService.retrieveOrgAddressInfo(request);
                    ParamUtil.setSessionAttr(request, KEY_INSTRUCTION_INFO, facilityRegisterDto.getFacilityInstructionDto());
                }
            }
            if (failRetrieveEditData) {
                throw new IaisRuntimeException("Fail to retrieve app data");
            }
            NodeGroup facRegRoot = facilityRegistrationService.getFacilityRegisterRoot(request);
            FacilitySelectionDto selectionDto = initRootNode(facRegRoot);
            facilityRegistrationService.handleServiceSelectionNextValidated(request, facRegRoot, selectionDto, KEY_NAV_NEXT);
            ParamUtil.setSessionAttr(request, KEY_ROOT_NODE_GROUP, facRegRoot);
        } else {
            throw new IllegalArgumentException("facility id is empty");
        }
    }

    private FacilitySelectionDto initRootNode(NodeGroup facRegRoot) {
        SimpleNode facSelectionNode = (SimpleNode) facRegRoot.getNode(NODE_NAME_FAC_SELECTION);
        FacilitySelectionDto selectionDto = (FacilitySelectionDto) facSelectionNode.getValue();
        List<Node> nodes = facRegRoot.getAllNodes();
        nodes.set(0, new Node(NODE_NAME_INSTRUCTION, new Node[0]));
        facRegRoot.reorganizeNodes(nodes.toArray(new Node[]{}), NODE_NAME_INSTRUCTION);
        facRegRoot.getNode(NODE_NAME_FAC_SELECTION).disappear();
        NodeGroup batNodeGroup = (NodeGroup) facRegRoot.getNode(NODE_NAME_FAC_BAT_INFO);
        Nodes.passValidation(facRegRoot, NODE_NAME_COMPANY_INFO);
        if (batNodeGroup.isAvailable()) {
            for (Node subNode : batNodeGroup.getAllNodes()) {
                BiologicalAgentToxinDto bat = (BiologicalAgentToxinDto) ((SimpleNode) subNode).getValue();
                bat.setValidationProfile(ValidationConstants.VALIDATION_PROFILE_RENEW);
            }
        }
        return selectionDto;
    }

    public void preInstruction(BaseProcessClass bpc) {
        // do nothing now
    }

    public void preAfc(BaseProcessClass bpc) {
        facilityRegistrationService.preApprovedFacilityCertifier(bpc, new HashSet<>(Collections.singletonList(KEY_ALL_FIELD)));
    }

    public void preCompInfo(BaseProcessClass bpc) {
        facilityRegistrationService.preCompInfo(bpc);
    }

    public void preServiceSelection(BaseProcessClass bpc) {
        facilityRegistrationService.preServiceSelection(bpc);
    }

    public void preFacProfile(BaseProcessClass bpc) {
        facilityRegistrationService.preFacProfile(bpc, new HashSet<>(Arrays.asList(
                KEY_ALL_FIELD, "retrieveAddressBtn"
        )));
    }

    public void preFacOperator(BaseProcessClass bpc) {
        facilityRegistrationService.preFacOperator(bpc, new HashSet<>(Collections.singletonList(KEY_ALL_FIELD)));
    }

    public void preFacInfoAuthoriser(BaseProcessClass bpc) {
        facilityRegistrationService.preFacInfoAuthoriser(bpc, new HashSet<>(Collections.singletonList("authoriserData")));
    }

    public void preFacAdmin(BaseProcessClass bpc) {
        facilityRegistrationService.preFacAdminOfficer(bpc, new HashSet<>(Arrays.asList(
                KEY_ALL_FIELD, "addNewOfficerSection", "removeBtn"
        )));
    }

    public void preFacInfoCommittee(BaseProcessClass bpc) {
        facilityRegistrationService.preFacInfoCommittee(bpc, new HashSet<>(Collections.singletonList("committeeData")));
    }
    public void preBAToxin(BaseProcessClass bpc) {
        facilityRegistrationService.preBAToxin(bpc, batEditFields);
    }

    public void preOtherAppInfo(BaseProcessClass bpc) {
        facilityRegistrationService.preOtherAppInfo(bpc);
    }

    public void prePrimaryDoc(BaseProcessClass bpc) {
        facilityRegistrationService.prePrimaryDoc(bpc, new HashSet<>(Collections.singletonList(KEY_ALL_FIELD)));
    }

    public void prePreviewSubmit(BaseProcessClass bpc) {
        facilityRegistrationService.prePreviewSubmit(bpc);
    }

    public void handleInstruction(BaseProcessClass bpc) {
        facilityRegistrationService.handleInstruction(bpc);
    }

    public void handleAfc(BaseProcessClass bpc) {
        facilityRegistrationService.handleApprovedFacilityCertifier(bpc);
    }

    public void handleCompInfo(BaseProcessClass bpc) {
        facilityRegistrationService.handleCompInfo(bpc);
    }

    public void handleServiceSelection(BaseProcessClass bpc) {
        facilityRegistrationService.handleServiceSelection(bpc);
    }

    public void handleFacProfile(BaseProcessClass bpc) {
        facilityRegistrationService.handleFacProfile(bpc);
    }

    public void handleFacOperator(BaseProcessClass bpc) {
        facilityRegistrationService.handleFacOperator(bpc);
    }

    public void handleFacInfoAuthoriser(BaseProcessClass bpc) {
        facilityRegistrationService.handleFacInfoAuthoriser(bpc);
    }

    public void handleFacAdmin(BaseProcessClass bpc) {
        facilityRegistrationService.handleFacAdminOfficer(bpc);
    }

    public void handleFacInfoCommittee(BaseProcessClass bpc) {
        facilityRegistrationService.handleFacInfoCommittee(bpc);
    }

    public void handleBAToxin(BaseProcessClass bpc) {
        facilityRegistrationService.handleBAToxin(bpc, new FieldEditableSetJudger(batEditFields));
    }

    public void handleOtherAppInfo(BaseProcessClass bpc) {
        facilityRegistrationService.handleOtherAppInfo(bpc);
    }

    public void handlePrimaryDoc(BaseProcessClass bpc) {
        facilityRegistrationService.handlePrimaryDoc(bpc);
    }

    public void preFacCommitteePreview(BaseProcessClass bpc) {
        facilityRegistrationService.prePreview(bpc);
    }

    public void handleFacCommitteePreview(BaseProcessClass bpc) {
        facilityRegistrationService.handlePreview(bpc);
    }

    public void preFacAuthoriserPreview(BaseProcessClass bpc) {
        facilityRegistrationService.prePreview(bpc);
    }

    public void handleFacAuthoriserPreview(BaseProcessClass bpc) {
        facilityRegistrationService.handlePreview(bpc);
    }

    public void handlePreviewSubmit(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        NodeGroup facRegRoot = facilityRegistrationService.getFacilityRegisterRoot(request);
        SimpleNode previewSubmitNode = (SimpleNode) facRegRoot.at(NODE_NAME_PREVIEW_SUBMIT);
        PreviewSubmitDto previewSubmitDto = (PreviewSubmitDto) previewSubmitNode.getValue();
        previewSubmitDto.reqObjMapping(request);

        String actionType = ParamUtil.getString(request, KEY_ACTION_TYPE);
        String actionValue = ParamUtil.getString(request, KEY_ACTION_VALUE);
        if (KEY_ACTION_JUMP.equals(actionType)) {
            if (KEY_NAV_NEXT.equals(actionValue)) {
                processSubmit(request, facRegRoot, previewSubmitNode);
            } else {
                facilityRegistrationService.jumpHandler(request, facRegRoot, NODE_NAME_PREVIEW_SUBMIT, previewSubmitNode);
            }
        } else if (KEY_ACTION_SAVE_AS_DRAFT.equals(actionType)) {
            ParamUtil.setRequestAttr(request, KEY_ACTION_TYPE, KEY_ACTION_SAVE_AS_DRAFT);
            ParamUtil.setSessionAttr(request, KEY_JUMP_DEST_NODE, NODE_NAME_PREVIEW_SUBMIT);
        } else if (KEY_ACTION_EXPAND_FILE.equals(actionType)) {
            processExpandFile(request, facRegRoot, actionValue);
        } else {
            throw new IaisRuntimeException(ERR_MSG_INVALID_ACTION);
        }
        ParamUtil.setSessionAttr(request, KEY_ROOT_NODE_GROUP, facRegRoot);
    }

    private void processSubmit(HttpServletRequest request, NodeGroup facRegRoot, SimpleNode previewSubmitNode) {
        if (previewSubmitNode.doValidation()) {
            previewSubmitNode.passValidation();

            // prevent double submission
            submissionBlockService.blockSubmission(request, SubmissionBlockConstants.BLOCK_RENEW_FACILITY);

            boolean isRf = (boolean) ParamUtil.getSessionAttr(request, KEY_IS_RF);

            // save docs
            log.info("Save documents into file-repo");
            PrimaryDocDto primaryDocDto = (PrimaryDocDto) ((SimpleNode) facRegRoot.at(NODE_NAME_PRIMARY_DOC)).getValue();
            List<String> createdFileRepoIds = new ArrayList<>(docDtoService.saveDocDtoAndRefresh(primaryDocDto));

            FacilityProfileDto profileDto = (FacilityProfileDto) ((SimpleNode) facRegRoot.at(NODE_NAME_FAC_INFO + facRegRoot.getPathSeparator() + NODE_NAME_FAC_PROFILE)).getValue();
            createdFileRepoIds.addAll(docDtoService.saveDocDtoAndRefresh(profileDto));

            if (!isRf) {
                FacilityCommitteeDto committeeDto = (FacilityCommitteeDto) ((SimpleNode) facRegRoot.at(NODE_NAME_FAC_INFO + facRegRoot.getPathSeparator() + NODE_NAME_FAC_COMMITTEE)).getValue();
                createdFileRepoIds.addAll(docDtoService.saveDocDtoAndRefresh(committeeDto));

                FacilityAuthoriserDto authDto = (FacilityAuthoriserDto) ((SimpleNode) facRegRoot.at(NODE_NAME_FAC_INFO + facRegRoot.getPathSeparator() + NODE_NAME_FAC_AUTH)).getValue();
                createdFileRepoIds.addAll(docDtoService.saveDocDtoAndRefresh(authDto));
            }


            // save data
            log.info("Save facility registration data");
            String currentFacId = (String) ParamUtil.getSessionAttr(request, KEY_CURRENT_FAC_ID);
            FacilityRegisterDto finalAllDataDto = FacilityRegistrationService.getRegisterDtoFromFacRegRoot(facRegRoot, createdFileRepoIds, currentFacId);
            ResponseDto<AppMainInfo> responseDto = facRegClient.saveRenewalRegisteredFacility(finalAllDataDto);
            facRegClient.updateFacilityRenewable(currentFacId, Boolean.FALSE);
            log.info("save new facility response: {}", org.apache.commons.lang.StringUtils.normalizeSpace(responseDto.toString()));
            AppMainInfo appMainInfo = responseDto.getEntity();
            ParamUtil.setRequestAttr(request, KEY_APP_NO, appMainInfo.getAppNo());
            ParamUtil.setRequestAttr(request, KEY_APP_DT, appMainInfo.getDate());

            ParamUtil.setRequestAttr(request, KEY_ACTION_TYPE, KEY_ACTION_SUBMIT);
        } else {
            ParamUtil.setRequestAttr(request, KEY_SHOW_ERROR_SWITCH, Boolean.TRUE);
            ParamUtil.setSessionAttr(request, KEY_JUMP_DEST_NODE, NODE_NAME_PREVIEW_SUBMIT);
        }
    }

    private void processExpandFile(HttpServletRequest request, NodeGroup facRegRoot, String actionValue) {
        if ("bsbCommittee".equals(actionValue)) {
            FacilityCommitteeDto facCommitteeDto = (FacilityCommitteeDto) ((SimpleNode) facRegRoot.at(NODE_NAME_FAC_INFO + facRegRoot.getPathSeparator() + NODE_NAME_FAC_COMMITTEE)).getValue();
            List<FacilityCommitteeFileDto> dataList = facCommitteeDto.getDataListForDisplay();
            ParamUtil.setRequestAttr(request, KEY_DATA_LIST, dataList);
            ParamUtil.setSessionAttr(request, KEY_JUMP_DEST_NODE, STEP_NAME_COMMITTEE_PREVIEW);
        } else if ("facAuth".equals(actionValue)) {
            FacilityAuthoriserDto facAuthDto = (FacilityAuthoriserDto) ((SimpleNode) facRegRoot.at(NODE_NAME_FAC_INFO + facRegRoot.getPathSeparator() + NODE_NAME_FAC_AUTH)).getValue();
            List<FacilityAuthoriserFileDto> dataList = facAuthDto.getDataListForDisplay();
            ParamUtil.setRequestAttr(request, KEY_DATA_LIST, dataList);
            ParamUtil.setSessionAttr(request, KEY_JUMP_DEST_NODE, STEP_NAME_AUTHORISER_PREVIEW);
        } else {
            throw new IaisRuntimeException(ERR_MSG_INVALID_ACTION);
        }
        ParamUtil.setRequestAttr(request, KEY_ACTION_TYPE, KEY_ACTION_JUMP);
    }

    public void actionFilter(BaseProcessClass bpc) {
        facilityRegistrationService.actionFilter(bpc, MasterCodeConstants.APP_TYPE_RENEW);
    }

    public void jumpFilter(BaseProcessClass bpc) {
        facilityRegistrationService.jumpFilter(bpc);
    }

    public void preAcknowledgement(BaseProcessClass bpc) {
        facilityRegistrationService.preAcknowledge(bpc);
    }
}

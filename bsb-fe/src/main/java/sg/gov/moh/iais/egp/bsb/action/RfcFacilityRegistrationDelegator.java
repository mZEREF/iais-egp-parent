package sg.gov.moh.iais.egp.bsb.action;


import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.exception.IaisRuntimeException;
import com.ecquaria.cloud.moh.iais.common.utils.LogUtil;
import com.ecquaria.cloud.moh.iais.common.utils.MaskUtil;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.helper.MasterCodeUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import sg.gov.moh.iais.egp.bsb.client.BsbInboxClient;
import sg.gov.moh.iais.egp.bsb.client.FacilityRegisterClient;
import sg.gov.moh.iais.egp.bsb.common.node.Node;
import sg.gov.moh.iais.egp.bsb.common.node.NodeGroup;
import sg.gov.moh.iais.egp.bsb.common.node.Nodes;
import sg.gov.moh.iais.egp.bsb.common.node.simple.SimpleNode;
import sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants;
import sg.gov.moh.iais.egp.bsb.constant.RfcFlowType;
import sg.gov.moh.iais.egp.bsb.constant.module.ApprovalBatAndActivityConstants;
import sg.gov.moh.iais.egp.bsb.constant.module.SubmissionBlockConstants;
import sg.gov.moh.iais.egp.bsb.dto.ResponseDto;
import sg.gov.moh.iais.egp.bsb.dto.info.common.AppMainInfo;
import sg.gov.moh.iais.egp.bsb.dto.info.common.EmployeeInfo;
import sg.gov.moh.iais.egp.bsb.dto.register.facility.FacilityAdminAndOfficerDto;
import sg.gov.moh.iais.egp.bsb.dto.register.facility.FacilityAuthoriserDto;
import sg.gov.moh.iais.egp.bsb.dto.register.facility.FacilityAuthoriserFileDto;
import sg.gov.moh.iais.egp.bsb.dto.register.facility.FacilityCommitteeDto;
import sg.gov.moh.iais.egp.bsb.dto.register.facility.FacilityCommitteeFileDto;
import sg.gov.moh.iais.egp.bsb.dto.register.facility.FacilityProfileDto;
import sg.gov.moh.iais.egp.bsb.dto.register.facility.FacilityProfileInfo;
import sg.gov.moh.iais.egp.bsb.dto.register.facility.FacilityRegisterDto;
import sg.gov.moh.iais.egp.bsb.dto.register.facility.FacilitySelectionDto;
import sg.gov.moh.iais.egp.bsb.dto.register.facility.PreviewSubmitDto;
import sg.gov.moh.iais.egp.bsb.dto.register.facility.PrimaryDocDto;
import sg.gov.moh.iais.egp.bsb.service.DocDtoService;
import sg.gov.moh.iais.egp.bsb.service.FacilityRegistrationService;
import sg.gov.moh.iais.egp.bsb.service.OrganizationInfoService;
import sg.gov.moh.iais.egp.bsb.service.SubmissionBlockService;
import sg.gov.moh.iais.egp.bsb.service.rfc.RfcFlowDecider;
import sg.gov.moh.iais.egp.bsb.service.rfc.RfcFlowHardCodeDecider;
import sg.gov.moh.iais.egp.bsb.util.JaversUtil;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.ecquaria.cloud.moh.iais.common.constant.BsbAuditTrailConstants.FUNCTION_FACILITY_REGISTRATION;
import static com.ecquaria.cloud.moh.iais.common.constant.BsbAuditTrailConstants.MODULE_REQUEST_FOR_CHANGE;
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
import static sg.gov.moh.iais.egp.bsb.constant.FacRegisterConstants.KEY_EDIT_APP_ID;
import static sg.gov.moh.iais.egp.bsb.constant.FacRegisterConstants.KEY_FAC_ID;
import static sg.gov.moh.iais.egp.bsb.constant.FacRegisterConstants.KEY_IS_CF;
import static sg.gov.moh.iais.egp.bsb.constant.FacRegisterConstants.KEY_IS_FIFTH_RF;
import static sg.gov.moh.iais.egp.bsb.constant.FacRegisterConstants.KEY_IS_PV_RF;
import static sg.gov.moh.iais.egp.bsb.constant.FacRegisterConstants.KEY_IS_RF;
import static sg.gov.moh.iais.egp.bsb.constant.FacRegisterConstants.KEY_IS_UCF;
import static sg.gov.moh.iais.egp.bsb.constant.FacRegisterConstants.KEY_JUMP_DEST_NODE;
import static sg.gov.moh.iais.egp.bsb.constant.FacRegisterConstants.KEY_NAV_NEXT;
import static sg.gov.moh.iais.egp.bsb.constant.FacRegisterConstants.KEY_OLD_FACILITY_REGISTER_DTO;
import static sg.gov.moh.iais.egp.bsb.constant.FacRegisterConstants.KEY_PROCESS_TYPE;
import static sg.gov.moh.iais.egp.bsb.constant.FacRegisterConstants.KEY_ROOT_NODE_GROUP;
import static sg.gov.moh.iais.egp.bsb.constant.FacRegisterConstants.KEY_SAMPLE_AUTHORISER;
import static sg.gov.moh.iais.egp.bsb.constant.FacRegisterConstants.KEY_SAMPLE_COMMITTEE;
import static sg.gov.moh.iais.egp.bsb.constant.FacRegisterConstants.KEY_SELECTED_ACTIVITIES;
import static sg.gov.moh.iais.egp.bsb.constant.FacRegisterConstants.KEY_SELECTED_CLASSIFICATION;
import static sg.gov.moh.iais.egp.bsb.constant.FacRegisterConstants.KEY_SHOW_ERROR_SWITCH;
import static sg.gov.moh.iais.egp.bsb.constant.FacRegisterConstants.NODE_NAME_BEFORE_BEGIN;
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
import static sg.gov.moh.iais.egp.bsb.constant.module.ApprovalBatAndActivityConstants.KEY_RFC_FLOW_TYPE;
import static sg.gov.moh.iais.egp.bsb.constant.module.ApprovalBatAndActivityConstants.NO_CHANGE;
import static sg.gov.moh.iais.egp.bsb.service.OrganizationInfoService.KEY_ORG_ADDRESS;


@Slf4j
@RequiredArgsConstructor
@Delegator("rfcFacilityRegisterDelegator")
public class RfcFacilityRegistrationDelegator {
    private final FacilityRegisterClient facRegClient;
    private final FacilityRegistrationService facilityRegistrationService;
    private final OrganizationInfoService organizationInfoService;
    private final DocDtoService docDtoService;
    private final RfcFlowDecider rfcFlowDecider;
    private final BsbInboxClient inboxClient;
    private final SubmissionBlockService submissionBlockService;

    private static final String ACTIVITY_TYPES_STRING = "activityTypesString";
    private static final String FAC_CLASSIFICATION = "facClassification";
    private static final String MISS_COMPANY_INFO = "missCompanyInfo";

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

        session.removeAttribute(SubmissionBlockConstants.BLOCK_RFC_FACILITY);
        session.removeAttribute(KEY_CURRENT_FAC_ID);
        session.removeAttribute(KEY_RFC_FLOW_TYPE);
        session.removeAttribute(KEY_PROCESS_TYPE);
        session.removeAttribute(KEY_OLD_FACILITY_REGISTER_DTO);
        session.removeAttribute(ACTIVITY_TYPES_STRING);
        session.removeAttribute(FAC_CLASSIFICATION);
        AuditTrailHelper.auditFunction(MODULE_REQUEST_FOR_CHANGE, FUNCTION_FACILITY_REGISTRATION);
    }

    public void init(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        FacilityRegisterDto facilityRegisterDto = getOldFacilityRegisterDto(bpc);
        FacilitySelectionDto selectionDto = facilityRegisterDto.getFacilitySelectionDto();
        ParamUtil.setSessionAttr(request, FAC_CLASSIFICATION, selectionDto.getFacClassification());
        ParamUtil.setSessionAttr(request, ACTIVITY_TYPES_STRING, selectionDto.getActivityTypes().stream().map(MasterCodeUtil::getCodeDesc).collect(Collectors.joining(" | ")));
        facilityRegistrationService.retrieveFacRegRoot(request, facilityRegisterDto);
        organizationInfoService.retrieveOrgAddressInfo(request);

        NodeGroup facRegRoot = facilityRegistrationService.getFacilityRegisterRoot(request);
        Nodes.disappear(facRegRoot, NODE_NAME_BEFORE_BEGIN);
        Nodes.disappear(facRegRoot, NODE_NAME_COMPANY_INFO);
        facilityRegistrationService.handleServiceSelectionNextValidated(request, facRegRoot, selectionDto, KEY_NAV_NEXT);
        Nodes.disappear(facRegRoot, NODE_NAME_FAC_BAT_INFO);
        facRegRoot.setActiveNodeKey(NODE_NAME_FAC_SELECTION);
        ParamUtil.setSessionAttr(request, KEY_ROOT_NODE_GROUP, facRegRoot);
        // allow saving draft for apply new, continue draft
        ParamUtil.setSessionAttr(request, ApprovalBatAndActivityConstants.KEY_ALLOW_SAVE_DRAFT, Boolean.TRUE);
    }

    private FacilityRegisterDto getOldFacilityRegisterDto(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        FacilityRegisterDto facilityRegisterDto = null;
        // check if we are doing editing
        String maskedAppId = request.getParameter(KEY_EDIT_APP_ID);
        String facId = null;
        if (StringUtils.hasLength(maskedAppId)) {
            if (log.isInfoEnabled()) {
                log.info("masked app ID: {}", LogUtil.escapeCrlf(maskedAppId));
            }
            boolean failRetrieveEditData = true;
            String appId = MaskUtil.unMaskValue(KEY_EDIT_APP_ID, maskedAppId);
            if (appId != null && !maskedAppId.equals(appId)) {
                ResponseDto<FacilityRegisterDto> resultDto = facRegClient.getFacilityRegistrationAppDataByApplicationId(appId);
                if (resultDto.ok()) {
                    failRetrieveEditData = false;
                    facilityRegisterDto = resultDto.getEntity();
                    facId = facilityRegisterDto.getAmendFacilityId();
                }
            }
            if (failRetrieveEditData) {
                throw new IaisRuntimeException("Fail to retrieve app data");
            }
        }
        if (facId == null) {
            String maskedFacId = request.getParameter(KEY_FAC_ID);
            facId = MaskUtil.unMaskValue(KEY_FAC_ID, maskedFacId);
            // handle exist draft
            FacilityRegisterDto draftFacilityRegDto = facRegClient.getRfcDraftData(facId).getEntity();
            String draftAction = ParamUtil.getString(request, "draftAction");
            if (draftFacilityRegDto != null) {
                if ("resume".equals(draftAction)) {
                    facilityRegisterDto = draftFacilityRegDto;
                } else if ("delete".equals(draftAction)) {
                    inboxClient.deleteDraftApplication(draftFacilityRegDto.getAppId());
                } else {
                    StringBuilder url = new StringBuilder();
                    url.append("https://").append(request.getServerName())
                            .append("/bsb-web/eservice/INTERNET/MohBSBInboxFac")
                            .append("?lastUrl=").append("/bsb-web/eservice/INTERNET/MohRfcFacilityRegistration").append("?facId=").append(MaskUtil.maskValue(KEY_FAC_ID, facId));
                    IaisEGPHelper.sendRedirect(request, bpc.response, url.toString());
                }
            }
        }
        if (facId != null) {
            ResponseDto<FacilityRegisterDto> resultDto = facRegClient.getRfcFacilityRegistrationAppDataByFacilityId(facId);
            if (resultDto.ok()) {
                //This oldFacilityRegisterDto is the original data, which is for comparison with each DTO modification before Submit(unchangeable)
                FacilityRegisterDto oldFacilityRegisterDto = resultDto.getEntity();
                ParamUtil.setSessionAttr(request, KEY_OLD_FACILITY_REGISTER_DTO, oldFacilityRegisterDto);
                if (facilityRegisterDto == null) {
                    facilityRegisterDto = oldFacilityRegisterDto;
                }
            }
        }
        ParamUtil.setSessionAttr(request, KEY_CURRENT_FAC_ID, facId);
        return facilityRegisterDto;
    }

    public void preAfc(BaseProcessClass bpc) {
        facilityRegistrationService.preApprovedFacilityCertifier(bpc, new HashSet<>(Collections.singletonList(KEY_ALL_FIELD)));
    }

    public void handleAfc(BaseProcessClass bpc) {
        facilityRegistrationService.handleApprovedFacilityCertifier(bpc);
    }

    public void preServiceSelection(BaseProcessClass bpc) {
        facilityRegistrationService.prePreviewSubmit(bpc);
    }

    public void handleServiceSelection(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        NodeGroup facRegRoot = facilityRegistrationService.getFacilityRegisterRoot(request);
        Node facilitySection = facRegRoot.getNode(NODE_NAME_FAC_SELECTION);
        String actionType = ParamUtil.getString(request, KEY_ACTION_TYPE);
        if (KEY_ACTION_JUMP.equals(actionType)) {
            facilityRegistrationService.jumpHandler(request, facRegRoot, NODE_NAME_INSTRUCTION, facilitySection);
        } else if (KEY_ACTION_EXPAND_FILE.equals(actionType)) {
            String actionValue = ParamUtil.getString(request, KEY_ACTION_VALUE);
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
        } else {
            throw new IaisRuntimeException(ERR_MSG_INVALID_ACTION);
        }
        ParamUtil.setSessionAttr(request, KEY_ROOT_NODE_GROUP, facRegRoot);
    }

    public void preFacProfile(BaseProcessClass bpc) {
        facilityRegistrationService.preFacProfile(bpc, new HashSet<>(Arrays.asList(
                KEY_ALL_FIELD, "retrieveAddressBtn"
        )));
    }

    public void handleFacProfile(BaseProcessClass bpc) {
        facilityRegistrationService.handleFacProfile(bpc);
    }

    public void preFacOperator(BaseProcessClass bpc) {
        facilityRegistrationService.preFacOperator(bpc, new HashSet<>(Collections.singletonList(KEY_ALL_FIELD)));
    }

    public void handleFacOperator(BaseProcessClass bpc) {
        facilityRegistrationService.handleFacOperator(bpc);
    }

    public void preFacInfoAuthoriser(BaseProcessClass bpc) {
        facilityRegistrationService.preFacInfoAuthoriser(bpc, new HashSet<>(Collections.singletonList("authoriserData")));
    }

    public void handleFacInfoAuthoriser(BaseProcessClass bpc) {
        facilityRegistrationService.handleFacInfoAuthoriser(bpc);
    }

    public void preFacAdmin(BaseProcessClass bpc) {
        facilityRegistrationService.preFacAdminOfficer(bpc, new HashSet<>(Arrays.asList(
                KEY_ALL_FIELD, "addNewOfficerSection", "removeBtn"
        )));
    }

    public void handleFacAdmin(BaseProcessClass bpc) {
        facilityRegistrationService.handleFacAdminOfficer(bpc);
    }

    public void preFacInfoCommittee(BaseProcessClass bpc) {
        facilityRegistrationService.preFacInfoCommittee(bpc, new HashSet<>(Collections.singletonList("committeeData")));
    }

    public void handleFacInfoCommittee(BaseProcessClass bpc) {
        facilityRegistrationService.handleFacInfoCommittee(bpc);
    }

    //no bat in rfc
    public void preBAToxin(BaseProcessClass bpc) {
        facilityRegistrationService.preBAToxin(bpc);
    }

    public void handleBAToxin(BaseProcessClass bpc) {
        facilityRegistrationService.handleBAToxin(bpc);
    }

    public void preOtherAppInfo(BaseProcessClass bpc) {
        facilityRegistrationService.preOtherAppInfo(bpc);
    }

    public void handleOtherAppInfo(BaseProcessClass bpc) {
        facilityRegistrationService.handleOtherAppInfo(bpc);
    }

    public void prePrimaryDoc(BaseProcessClass bpc) {
        facilityRegistrationService.prePrimaryDoc(bpc, new HashSet<>(Collections.singletonList(KEY_ALL_FIELD)));
    }

    public void handlePrimaryDoc(BaseProcessClass bpc) {
        facilityRegistrationService.handlePrimaryDoc(bpc);
    }

    public void prePreviewSubmit(BaseProcessClass bpc) {
        facilityRegistrationService.prePreviewSubmit(bpc);
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
                if (previewSubmitNode.doValidation()) {
                    previewSubmitNode.passValidation();

                    boolean isRf = (boolean) ParamUtil.getSessionAttr(request, KEY_IS_RF);

                    // needTask
                    PrimaryDocDto primaryDocDto = (PrimaryDocDto) ((SimpleNode) facRegRoot.at(NODE_NAME_PRIMARY_DOC)).getValue();
                    FacilityRegisterDto oldFacilityRegisterDto = (FacilityRegisterDto) ParamUtil.getSessionAttr(request, KEY_OLD_FACILITY_REGISTER_DTO);
                    FacilityRegisterDto facilityRegisterDto = FacilityRegistrationService.getRegisterDtoFromFacRegRoot(facRegRoot, Collections.emptySet(), null);

                    Set<String> facRegisterEditSet = getFacEditSet(primaryDocDto, oldFacilityRegisterDto, facilityRegisterDto);
                    RfcFlowType rfcFlowType = rfcFlowDecider.decide4Facility(facRegisterEditSet);
                    if (!facRegisterEditSet.isEmpty() && rfcFlowType.ordinal() < RfcFlowType.NOTIFICATION.ordinal()){
                        rfcFlowType = RfcFlowType.NOTIFICATION;
                    }
                    Boolean needTask;
                    Boolean needScreen = Boolean.FALSE;
                    if (RfcFlowType.DO_NOTHING.equals(rfcFlowType)) {
                        // popup tip to notify user to make change
                        log.info("No change are maked");
                        ParamUtil.setRequestAttr(request, NO_CHANGE, Boolean.TRUE);
                        return;
                    } else if (RfcFlowType.NOTIFICATION.equals(rfcFlowType)) {
                        // save and send notification to notify officer
                        needTask = Boolean.FALSE;
                        log.info("save RFC data and send notification to notify MOH Officer");
                    } else if (RfcFlowType.AMENDMENT.equals(rfcFlowType)) {
                        // save and create task for officer to process
                        needTask = Boolean.TRUE;
                        for (String screenField : RfcFlowHardCodeDecider.FACILITY_RFC_SCREEN_FIELD_SET) {
                            if (facRegisterEditSet.contains(screenField)) {
                                needScreen = Boolean.TRUE;
                                break;
                            }
                        }
                        log.info("save RFC data and create task for officer to process");
                    } else {
                        log.warn("Illegal operation when RFC");
                        request.getSession().removeAttribute(SubmissionBlockConstants.BLOCK_RFC_FACILITY);
                        return;
                    }

                    // prevent double submission
                    submissionBlockService.blockSubmission(request, SubmissionBlockConstants.BLOCK_RFC_FACILITY);

                    // save docs
                    log.info("Save documents into file-repo");
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
                    ResponseDto<AppMainInfo> responseDto = facRegClient.saveAmendmentFacility(finalAllDataDto, needTask, needScreen);
                    log.info("save new facility response: {}", org.apache.commons.lang.StringUtils.normalizeSpace(responseDto.toString()));
                    AppMainInfo appMainInfo = responseDto.getEntity();
                    ParamUtil.setRequestAttr(request, KEY_APP_NO, appMainInfo.getAppNo());
                    ParamUtil.setRequestAttr(request, KEY_APP_DT, appMainInfo.getDate());

                    ParamUtil.setRequestAttr(request, KEY_ACTION_TYPE, KEY_ACTION_SUBMIT);
                } else {
                    ParamUtil.setRequestAttr(request, KEY_SHOW_ERROR_SWITCH, Boolean.TRUE);
                    ParamUtil.setSessionAttr(request, KEY_JUMP_DEST_NODE, NODE_NAME_PREVIEW_SUBMIT);
                }
            } else {
                facilityRegistrationService.jumpHandler(request, facRegRoot, NODE_NAME_PREVIEW_SUBMIT, previewSubmitNode);
            }
        } else if (KEY_ACTION_SAVE_AS_DRAFT.equals(actionType)) {
            ParamUtil.setRequestAttr(request, KEY_ACTION_TYPE, KEY_ACTION_SAVE_AS_DRAFT);
            ParamUtil.setSessionAttr(request, KEY_JUMP_DEST_NODE, NODE_NAME_PREVIEW_SUBMIT);
        } else if (KEY_ACTION_EXPAND_FILE.equals(actionType)) {
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
        } else {
            throw new IaisRuntimeException(ERR_MSG_INVALID_ACTION);
        }
        ParamUtil.setSessionAttr(request, KEY_ROOT_NODE_GROUP, facRegRoot);
    }

    private Set<String> getFacEditSet(PrimaryDocDto primaryDocDto, FacilityRegisterDto oldFacilityRegisterDto, FacilityRegisterDto finalAllDataDto) {
        Set<String> facRegisterEditSet = new HashSet<>();
        facRegisterEditSet.addAll(JaversUtil.compareCollections(oldFacilityRegisterDto.getFacilityProfileDto().getInfoList(), finalAllDataDto.getFacilityProfileDto().getInfoList(), FacilityProfileInfo.class)
                .stream().map(it -> "facProfile." + it).collect(Collectors.toSet()));
        facRegisterEditSet.addAll(JaversUtil.compare(oldFacilityRegisterDto.getFacilityOperatorDto(), finalAllDataDto.getFacilityOperatorDto())
                .stream().filter(it->!"pass".equals(it))
                .map(it -> "facOperator." + it).collect(Collectors.toSet()));
        FacilityAdminAndOfficerDto oldFacilityAdminAndOfficerDto = oldFacilityRegisterDto.getFacilityAdminAndOfficerDto();
        List<EmployeeInfo> oldFacilityAdmins = Arrays.asList(oldFacilityAdminAndOfficerDto.getMainAdmin(), oldFacilityAdminAndOfficerDto.getAlternativeAdmin());
        FacilityAdminAndOfficerDto facilityAdminAndOfficerDto = finalAllDataDto.getFacilityAdminAndOfficerDto();
        List<EmployeeInfo> facilityAdmins = Arrays.asList(facilityAdminAndOfficerDto.getMainAdmin(), facilityAdminAndOfficerDto.getAlternativeAdmin());
        facRegisterEditSet.addAll(JaversUtil.compareCollections(oldFacilityAdmins, facilityAdmins, EmployeeInfo.class)
                .stream().map(it -> "facAdmin." + it).collect(Collectors.toSet()));
        facRegisterEditSet.addAll(JaversUtil.compareCollections(oldFacilityAdminAndOfficerDto.getOfficerList(), facilityAdminAndOfficerDto.getOfficerList(), EmployeeInfo.class)
                .stream().map(it -> "facOfficer." + it).collect(Collectors.toSet()));
        Set<String> authEditSet = JaversUtil.compareCollections(oldFacilityRegisterDto.getFacilityAuthoriserDto().getFacAuthPersonnelList(), finalAllDataDto.getFacilityAuthoriserDto().getFacAuthPersonnelList(), FacilityAuthoriserDto.FacilityAuthorisedPersonnel.class);
        if (!authEditSet.isEmpty()){
            facRegisterEditSet.add("facAuthoriser");
        }
        Set<String> commiteeEditSet = JaversUtil.compareCollections(oldFacilityRegisterDto.getFacilityCommitteeDto().getFacCommitteePersonnelList(), finalAllDataDto.getFacilityCommitteeDto().getFacCommitteePersonnelList(), FacilityCommitteeDto.BioSafetyCommitteePersonnel.class);
        if (!commiteeEditSet.isEmpty()){
            facRegisterEditSet.add("facilityCommittee");
        }
        if (!primaryDocDto.getNewDocMap().isEmpty() || !primaryDocDto.getToBeDeletedRepoIds().isEmpty()){
            facRegisterEditSet.add("facSupportingDoc");
        }
        return facRegisterEditSet;
    }

    /**
     * Do special route changes.
     * This method is used when we re-use some pages for different nodes,
     * then we need to resolve the nodes to the same destination.
     */
    public void jumpFilter(BaseProcessClass bpc) {
        facilityRegistrationService.jumpFilter(bpc);
    }

    public void actionFilter(BaseProcessClass bpc) {
        facilityRegistrationService.actionFilter(bpc, MasterCodeConstants.APP_TYPE_RFC);
    }

    public void preAcknowledge(BaseProcessClass bpc) {
        facilityRegistrationService.preAcknowledge(bpc);
    }
}

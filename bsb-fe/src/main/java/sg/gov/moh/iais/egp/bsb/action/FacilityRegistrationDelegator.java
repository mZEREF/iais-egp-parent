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
import sg.gov.moh.iais.egp.bsb.client.FacilityRegisterClient;
import sg.gov.moh.iais.egp.bsb.common.node.NodeGroup;
import sg.gov.moh.iais.egp.bsb.common.node.simple.SimpleNode;
import sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants;
import sg.gov.moh.iais.egp.bsb.dto.ResponseDto;
import sg.gov.moh.iais.egp.bsb.dto.info.common.AppMainInfo;
import sg.gov.moh.iais.egp.bsb.dto.register.facility.FacilityAuthoriserDto;
import sg.gov.moh.iais.egp.bsb.dto.register.facility.FacilityAuthoriserFileDto;
import sg.gov.moh.iais.egp.bsb.dto.register.facility.FacilityCommitteeDto;
import sg.gov.moh.iais.egp.bsb.dto.register.facility.FacilityCommitteeFileDto;
import sg.gov.moh.iais.egp.bsb.dto.register.facility.FacilityProfileDto;
import sg.gov.moh.iais.egp.bsb.dto.register.facility.FacilityRegisterDto;
import sg.gov.moh.iais.egp.bsb.dto.register.facility.PreviewSubmitDto;
import sg.gov.moh.iais.egp.bsb.dto.register.facility.PrimaryDocDto;
import sg.gov.moh.iais.egp.bsb.service.DocDtoService;
import sg.gov.moh.iais.egp.bsb.service.FacilityRegistrationService;
import sg.gov.moh.iais.egp.bsb.service.OrganizationInfoService;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

import static com.ecquaria.cloud.moh.iais.common.constant.BsbAuditTrailConstants.FUNCTION_FACILITY_REGISTRATION;
import static com.ecquaria.cloud.moh.iais.common.constant.BsbAuditTrailConstants.MODULE_NEW_APPLICATION;
import static sg.gov.moh.iais.egp.bsb.constant.FacRegisterConstants.ELIGIBLE_DRAFT_REGISTER_DTO;
import static sg.gov.moh.iais.egp.bsb.constant.FacRegisterConstants.ERR_MSG_INVALID_ACTION;
import static sg.gov.moh.iais.egp.bsb.constant.FacRegisterConstants.KEY_ACTION_EXPAND_FILE;
import static sg.gov.moh.iais.egp.bsb.constant.FacRegisterConstants.KEY_ACTION_JUMP;
import static sg.gov.moh.iais.egp.bsb.constant.FacRegisterConstants.KEY_ACTION_SAVE_AS_DRAFT;
import static sg.gov.moh.iais.egp.bsb.constant.FacRegisterConstants.KEY_ACTION_SUBMIT;
import static sg.gov.moh.iais.egp.bsb.constant.FacRegisterConstants.KEY_ACTION_TYPE;
import static sg.gov.moh.iais.egp.bsb.constant.FacRegisterConstants.KEY_ACTION_VALUE;
import static sg.gov.moh.iais.egp.bsb.constant.FacRegisterConstants.KEY_ALLOW_SAVE_DRAFT;
import static sg.gov.moh.iais.egp.bsb.constant.FacRegisterConstants.KEY_APP_DT;
import static sg.gov.moh.iais.egp.bsb.constant.FacRegisterConstants.KEY_APP_NO;
import static sg.gov.moh.iais.egp.bsb.constant.FacRegisterConstants.KEY_DATA_LIST;
import static sg.gov.moh.iais.egp.bsb.constant.FacRegisterConstants.KEY_EDIT_APP_ID;
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
import static sg.gov.moh.iais.egp.bsb.constant.FacRegisterConstants.NODE_NAME_FAC_AUTH;
import static sg.gov.moh.iais.egp.bsb.constant.FacRegisterConstants.NODE_NAME_FAC_COMMITTEE;
import static sg.gov.moh.iais.egp.bsb.constant.FacRegisterConstants.NODE_NAME_FAC_INFO;
import static sg.gov.moh.iais.egp.bsb.constant.FacRegisterConstants.NODE_NAME_FAC_PROFILE;
import static sg.gov.moh.iais.egp.bsb.constant.FacRegisterConstants.NODE_NAME_PREVIEW_SUBMIT;
import static sg.gov.moh.iais.egp.bsb.constant.FacRegisterConstants.NODE_NAME_PRIMARY_DOC;
import static sg.gov.moh.iais.egp.bsb.constant.FacRegisterConstants.STEP_NAME_AUTHORISER_PREVIEW;
import static sg.gov.moh.iais.egp.bsb.constant.FacRegisterConstants.STEP_NAME_COMMITTEE_PREVIEW;
import static sg.gov.moh.iais.egp.bsb.service.OrganizationInfoService.KEY_ORG_ADDRESS;


@Slf4j
@Delegator("bsbFacilityRegisterDelegator")
public class FacilityRegistrationDelegator {
    private final FacilityRegisterClient facRegClient;
    private final FacilityRegistrationService facilityRegistrationService;
    private final OrganizationInfoService organizationInfoService;
    private final DocDtoService docDtoService;

    @Autowired
    public FacilityRegistrationDelegator(FacilityRegisterClient facRegClient, FacilityRegistrationService facilityRegistrationService,
                                         OrganizationInfoService organizationInfoService, DocDtoService docDtoService) {
        this.facRegClient = facRegClient;
        this.facilityRegistrationService = facilityRegistrationService;
        this.organizationInfoService = organizationInfoService;
        this.docDtoService = docDtoService;
    }

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
        AuditTrailHelper.auditFunction(MODULE_NEW_APPLICATION, FUNCTION_FACILITY_REGISTRATION);
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
            boolean failRetrieveEditData = true;
            String appId = MaskUtil.unMaskValue(KEY_EDIT_APP_ID, maskedAppId);
            if (appId != null && !maskedAppId.equals(appId)) {
                ResponseDto<FacilityRegisterDto> resultDto = facRegClient.getFacilityRegistrationAppDataByApplicationId(appId);
                if (resultDto.ok()) {
                    failRetrieveEditData = false;
                    editDraft = true;
                    FacilityRegisterDto facilityRegisterDto = resultDto.getEntity();
                    facilityRegistrationService.retrieveFacRegRoot(request, facilityRegisterDto);
                }
            }
            if (failRetrieveEditData) {
                throw new IaisRuntimeException("Fail to retrieve app data");
            }
        }

        // allow saving draft for apply new, continue draft
        ParamUtil.setSessionAttr(request, KEY_ALLOW_SAVE_DRAFT, Boolean.TRUE);

        if (!editDraft) {
            ParamUtil.setSessionAttr(request, KEY_ROOT_NODE_GROUP, facilityRegistrationService.getFacilityRegisterRoot(request));
        }

        organizationInfoService.retrieveOrgAddressInfo(request);
    }

    public void handleBeforeBegin(BaseProcessClass bpc) {
        facilityRegistrationService.handleBeforeBegin(bpc);
    }

    public void preCompInfo(BaseProcessClass bpc) {
        facilityRegistrationService.preCompInfo(bpc);
    }

    public void handleCompInfo(BaseProcessClass bpc) {
        facilityRegistrationService.handleCompInfo(bpc);
    }

    public void preServiceSelection(BaseProcessClass bpc) {
        facilityRegistrationService.preServiceSelection(bpc);
    }

    public void handleServiceSelection(BaseProcessClass bpc) {
        facilityRegistrationService.handleNewFacilityServiceSelection(bpc);
    }

    public void preFacProfile(BaseProcessClass bpc) {
        facilityRegistrationService.preFacProfile(bpc);
    }

    public void handleFacProfile(BaseProcessClass bpc) {
        facilityRegistrationService.handleFacProfile(bpc);
    }

    public void preFacOperator(BaseProcessClass bpc) {
        facilityRegistrationService.preFacOperator(bpc);
    }

    public void handleFacOperator(BaseProcessClass bpc) {
        facilityRegistrationService.handleFacOperator(bpc);
    }

    public void preFacAdminOfficer(BaseProcessClass bpc) {
        facilityRegistrationService.preFacAdminOfficer(bpc);
    }

    public void handleFacAdminOfficer(BaseProcessClass bpc) {
        facilityRegistrationService.handleFacAdminOfficer(bpc);
    }

    public void preFacInfoCommittee(BaseProcessClass bpc) {
        facilityRegistrationService.preFacInfoCommittee(bpc);
    }

    public void handleFacInfoCommittee(BaseProcessClass bpc) {
        facilityRegistrationService.handleFacInfoCommittee(bpc);
    }

    public void preCommitteePreview(BaseProcessClass bpc) {
        facilityRegistrationService.prePreview(bpc);
    }

    public void handleCommitteePreview(BaseProcessClass bpc) {
        facilityRegistrationService.handlePreview(bpc);
    }

    public void preFacInfoAuthoriser(BaseProcessClass bpc) {
        facilityRegistrationService.preFacInfoAuthoriser(bpc);
    }

    public void handleFacInfoAuthoriser(BaseProcessClass bpc) {
        facilityRegistrationService.handleFacInfoAuthoriser(bpc);
    }

    public void preAuthoriserPreview(BaseProcessClass bpc) {
        facilityRegistrationService.prePreview(bpc);
    }

    public void handleAuthoriserPreview(BaseProcessClass bpc) {
        facilityRegistrationService.handlePreview(bpc);
    }

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
        facilityRegistrationService.prePrimaryDoc(bpc);
    }

    public void handlePrimaryDoc(BaseProcessClass bpc) {
        facilityRegistrationService.handlePrimaryDoc(bpc);
    }

    public void preApprovedFacilityCertifier(BaseProcessClass bpc){facilityRegistrationService.preApprovedFacilityCertifier(bpc);}

    public void handleApprovedFacilityCertifier(BaseProcessClass bpc){facilityRegistrationService.handleApprovedFacilityCertifier(bpc);}

    public void prePreviewSubmit(BaseProcessClass bpc) {
        facilityRegistrationService.prePreviewSubmit(bpc);
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
                    FacilityRegisterDto finalAllDataDto = FacilityRegistrationService.getRegisterDtoFromFacRegRoot(facRegRoot, createdFileRepoIds);
                    ResponseDto<AppMainInfo> responseDto = facRegClient.saveNewRegisteredFacility(finalAllDataDto);
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

    /**
     * Do special route changes.
     * This method is used when we re-use some pages for different nodes,
     * then we need to resolve the nodes to the same destination.
     */
    public void jumpFilter(BaseProcessClass bpc) {
        facilityRegistrationService.jumpFilter(bpc);
    }

    public void actionFilter(BaseProcessClass bpc) {
        facilityRegistrationService.actionFilter(bpc, MasterCodeConstants.APP_TYPE_NEW);
    }

    public void preAcknowledge(BaseProcessClass bpc) {
        facilityRegistrationService.preAcknowledge(bpc);
    }

    public void print(BaseProcessClass bpc) {
        facilityRegistrationService.print(bpc);
    }
}

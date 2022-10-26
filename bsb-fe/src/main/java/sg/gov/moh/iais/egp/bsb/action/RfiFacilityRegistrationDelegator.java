package sg.gov.moh.iais.egp.bsb.action;


import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.exception.IaisRuntimeException;
import com.ecquaria.cloud.moh.iais.common.utils.JsonUtil;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import sg.gov.moh.iais.egp.bsb.client.FacilityRegisterClient;
import sg.gov.moh.iais.egp.bsb.client.RfiClient;
import sg.gov.moh.iais.egp.bsb.common.edit.FieldEditableSetJudger;
import sg.gov.moh.iais.egp.bsb.common.node.NodeGroup;
import sg.gov.moh.iais.egp.bsb.common.node.Nodes;
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
import sg.gov.moh.iais.egp.bsb.dto.register.facility.FacilitySelectionDto;
import sg.gov.moh.iais.egp.bsb.dto.register.facility.PreviewSubmitDto;
import sg.gov.moh.iais.egp.bsb.dto.register.facility.PrimaryDocDto;
import sg.gov.moh.iais.egp.bsb.dto.rfi.PageAppEditSelectDto;
import sg.gov.moh.iais.egp.bsb.service.DocDtoService;
import sg.gov.moh.iais.egp.bsb.service.FacilityRegistrationService;
import sg.gov.moh.iais.egp.bsb.service.OrganizationInfoService;
import sg.gov.moh.iais.egp.bsb.service.RfiService;
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
import static com.ecquaria.cloud.moh.iais.common.constant.BsbAuditTrailConstants.MODULE_REQUEST_FOR_INFORMATION;
import static sg.gov.moh.iais.egp.bsb.constant.FacRegisterConstants.ERR_MSG_INVALID_ACTION;
import static sg.gov.moh.iais.egp.bsb.constant.FacRegisterConstants.KEY_ACTION_EXPAND_FILE;
import static sg.gov.moh.iais.egp.bsb.constant.FacRegisterConstants.KEY_ACTION_JUMP;
import static sg.gov.moh.iais.egp.bsb.constant.FacRegisterConstants.KEY_ACTION_SAVE_AS_DRAFT;
import static sg.gov.moh.iais.egp.bsb.constant.FacRegisterConstants.KEY_ACTION_SUBMIT;
import static sg.gov.moh.iais.egp.bsb.constant.FacRegisterConstants.KEY_ACTION_TYPE;
import static sg.gov.moh.iais.egp.bsb.constant.FacRegisterConstants.KEY_ACTION_VALUE;
import static sg.gov.moh.iais.egp.bsb.constant.FacRegisterConstants.KEY_ALL_FIELD;
import static sg.gov.moh.iais.egp.bsb.constant.FacRegisterConstants.KEY_APP_DT;
import static sg.gov.moh.iais.egp.bsb.constant.FacRegisterConstants.KEY_APP_NO;
import static sg.gov.moh.iais.egp.bsb.constant.FacRegisterConstants.KEY_CURRENT_FAC_ID;
import static sg.gov.moh.iais.egp.bsb.constant.FacRegisterConstants.KEY_DATA_LIST;
import static sg.gov.moh.iais.egp.bsb.constant.FacRegisterConstants.KEY_IS_RENEW;
import static sg.gov.moh.iais.egp.bsb.constant.FacRegisterConstants.KEY_IS_RF;
import static sg.gov.moh.iais.egp.bsb.constant.FacRegisterConstants.KEY_IS_RFC;
import static sg.gov.moh.iais.egp.bsb.constant.FacRegisterConstants.KEY_JUMP_DEST_NODE;
import static sg.gov.moh.iais.egp.bsb.constant.FacRegisterConstants.KEY_NAV_NEXT;
import static sg.gov.moh.iais.egp.bsb.constant.FacRegisterConstants.KEY_ROOT_NODE_GROUP;
import static sg.gov.moh.iais.egp.bsb.constant.FacRegisterConstants.KEY_SHOW_ERROR_SWITCH;
import static sg.gov.moh.iais.egp.bsb.constant.FacRegisterConstants.NODE_NAME_COMPANY_INFO;
import static sg.gov.moh.iais.egp.bsb.constant.FacRegisterConstants.NODE_NAME_FAC_AUTH;
import static sg.gov.moh.iais.egp.bsb.constant.FacRegisterConstants.NODE_NAME_FAC_BAT_INFO;
import static sg.gov.moh.iais.egp.bsb.constant.FacRegisterConstants.NODE_NAME_FAC_COMMITTEE;
import static sg.gov.moh.iais.egp.bsb.constant.FacRegisterConstants.NODE_NAME_FAC_INFO;
import static sg.gov.moh.iais.egp.bsb.constant.FacRegisterConstants.NODE_NAME_FAC_PROFILE;
import static sg.gov.moh.iais.egp.bsb.constant.FacRegisterConstants.NODE_NAME_FAC_SELECTION;
import static sg.gov.moh.iais.egp.bsb.constant.FacRegisterConstants.NODE_NAME_PREVIEW_SUBMIT;
import static sg.gov.moh.iais.egp.bsb.constant.FacRegisterConstants.NODE_NAME_PRIMARY_DOC;
import static sg.gov.moh.iais.egp.bsb.constant.FacRegisterConstants.STEP_NAME_AUTHORISER_PREVIEW;
import static sg.gov.moh.iais.egp.bsb.constant.FacRegisterConstants.STEP_NAME_COMMITTEE_PREVIEW;
import static sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants.KEY_APP_ID;
import static sg.gov.moh.iais.egp.bsb.constant.module.RfiConstants.KEY_PAGE_APP_EDIT_SELECT_DTO;
import static sg.gov.moh.iais.egp.bsb.constant.module.RfiConstants.KEY_RFT_DATA_ID;


@Slf4j
@RequiredArgsConstructor
@Delegator("bsbRfiFacilityRegisterDelegator")
public class RfiFacilityRegistrationDelegator {
    private final FacilityRegisterClient facRegClient;
    private final FacilityRegistrationService facilityRegistrationService;
    private final RfiService rfiService;
    private final RfiClient rfiClient;
    private final OrganizationInfoService organizationInfoService;
    private final DocDtoService docDtoService;
    private final Set<String> renewBatEditFields = new HashSet<>(Arrays.asList(
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
        // clear sessions
        HttpSession session = request.getSession();
        session.removeAttribute(KEY_ROOT_NODE_GROUP);
        session.removeAttribute(KEY_IS_RFC);
        session.removeAttribute(KEY_IS_RENEW);
        session.removeAttribute(KEY_CURRENT_FAC_ID);
        rfiService.clearAndSetAppIdInSession(request);
        AuditTrailHelper.auditFunction(MODULE_REQUEST_FOR_INFORMATION, FUNCTION_FACILITY_REGISTRATION);
    }

    public void init(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        boolean failRetrieveRfiData = true;
        boolean isRfc = false;
        boolean isRenew = false;
        String appId = (String) ParamUtil.getSessionAttr(request, KEY_APP_ID);
        if (appId != null) {
            ResponseDto<FacilityRegisterDto> resultDto = facRegClient.getFacilityRegistrationAppDataByApplicationId(appId);
            if (resultDto.ok()) {
                failRetrieveRfiData = false;
                FacilityRegisterDto facilityRegisterDto = resultDto.getEntity();
                isRfc = MasterCodeConstants.APP_TYPE_RFC.equals(facilityRegisterDto.getAppType());
                isRenew = MasterCodeConstants.APP_TYPE_RENEW.equals(facilityRegisterDto.getAppType());
                ParamUtil.setSessionAttr(request, KEY_IS_RFC, isRfc ? Boolean.TRUE : Boolean.FALSE);
                ParamUtil.setSessionAttr(request, KEY_IS_RENEW, isRenew ? Boolean.TRUE : Boolean.FALSE);
                ParamUtil.setSessionAttr(request, KEY_CURRENT_FAC_ID, facilityRegisterDto.getAmendFacilityId());
                facilityRegistrationService.retrieveFacRegRoot(request, facilityRegisterDto);
                organizationInfoService.retrieveOrgAddressInfo(request);
            }
        }
        if (failRetrieveRfiData) {
            throw new IaisRuntimeException("Fail to retrieve rfi data");
        }

        NodeGroup facRegRoot = facilityRegistrationService.getFacilityRegisterRoot(request);
        SimpleNode facSelectionNode = (SimpleNode) facRegRoot.getNode(NODE_NAME_FAC_SELECTION);
        FacilitySelectionDto selectionDto = (FacilitySelectionDto) facSelectionNode.getValue();
        facilityRegistrationService.handleServiceSelectionNextValidated(request, facRegRoot, selectionDto, KEY_NAV_NEXT);
        if (isRfc) {
            Nodes.disappear(facRegRoot, NODE_NAME_FAC_BAT_INFO);
        }
        Nodes.jump(facRegRoot, NODE_NAME_COMPANY_INFO);
        ParamUtil.setSessionAttr(request, KEY_ROOT_NODE_GROUP, facRegRoot);

        String rfiDataId = (String) ParamUtil.getSessionAttr(request, KEY_RFT_DATA_ID);
        String specialRFIIndicator = rfiClient.getSpecialRFIIndicatorById(rfiDataId);
        if (StringUtils.hasLength(specialRFIIndicator)) {
            PageAppEditSelectDto pageAppEditSelectDto = JsonUtil.parseToObject(specialRFIIndicator, PageAppEditSelectDto.class);
            ParamUtil.setSessionAttr(request, KEY_PAGE_APP_EDIT_SELECT_DTO, pageAppEditSelectDto);
        }
    }

    public void preCompInfo(BaseProcessClass bpc) {
        facilityRegistrationService.preCompInfo(bpc);
    }

    public void handleCompInfo(BaseProcessClass bpc) {
        facilityRegistrationService.handleCompInfo(bpc);
    }

    public void preFacProfile(BaseProcessClass bpc) {
        Boolean isRfc = (Boolean) ParamUtil.getSessionAttr(bpc.request, KEY_IS_RFC);
        Boolean isRenew = (Boolean) ParamUtil.getSessionAttr(bpc.request, KEY_IS_RENEW);
        HashSet<String> editFields = new HashSet<>(Arrays.asList(
                KEY_ALL_FIELD, "retrieveAddressBtn"
        ));
        if (Boolean.FALSE.equals(isRenew) && Boolean.FALSE.equals(isRfc)) {
            editFields.add("addNewProfileSection");
        }
        facilityRegistrationService.preFacProfile(bpc, editFields);
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

    public void preFacAdminOfficer(BaseProcessClass bpc) {
        facilityRegistrationService.preFacAdminOfficer(bpc, new HashSet<>(Arrays.asList(
                KEY_ALL_FIELD, "addNewOfficerSection", "removeBtn"
        )));
    }

    public void handleFacAdminOfficer(BaseProcessClass bpc) {
        facilityRegistrationService.handleFacAdminOfficer(bpc);
    }

    public void preFacInfoCommittee(BaseProcessClass bpc) {
        facilityRegistrationService.preFacInfoCommittee(bpc, new HashSet<>(Collections.singletonList("committeeData")));
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
        facilityRegistrationService.preFacInfoAuthoriser(bpc, new HashSet<>(Collections.singletonList("authoriserData")));
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
        Boolean isRenew = (Boolean) ParamUtil.getSessionAttr(bpc.request, KEY_IS_RENEW);
        if (Boolean.TRUE.equals(isRenew)) {
            facilityRegistrationService.preBAToxin(bpc, renewBatEditFields);
        } else {
            facilityRegistrationService.preBAToxin(bpc, new HashSet<>(Arrays.asList(
                    KEY_ALL_FIELD, "exportingRetrieveAddressBtn", "localTransferRetrieveAddressBtn"
            )));
        }
    }

    public void handleBAToxin(BaseProcessClass bpc) {
        facilityRegistrationService.handleBAToxin(bpc);
        Boolean isRenew = (Boolean) ParamUtil.getSessionAttr(bpc.request, KEY_IS_RENEW);
        if (Boolean.TRUE.equals(isRenew)) {
            facilityRegistrationService.handleBAToxin(bpc, new FieldEditableSetJudger(renewBatEditFields));
        } else {
            facilityRegistrationService.handleBAToxin(bpc);
        }
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

    public void preApprovedFacilityCertifier(BaseProcessClass bpc){facilityRegistrationService.preApprovedFacilityCertifier(bpc, new HashSet<>(Collections.singletonList(KEY_ALL_FIELD)));}

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
                    String currentFacId = (String) ParamUtil.getSessionAttr(request, KEY_CURRENT_FAC_ID);
                    FacilityRegisterDto finalAllDataDto = FacilityRegistrationService.getRegisterDtoFromFacRegRoot(facRegRoot, createdFileRepoIds, currentFacId);
                    finalAllDataDto.setAppId((String) ParamUtil.getSessionAttr(request, KEY_APP_ID));

                    // save rfi data
                    String rfiDataId = (String) ParamUtil.getSessionAttr(request, KEY_RFT_DATA_ID);
                    ResponseDto<AppMainInfo> responseDto = rfiClient.saveFacilityRegistration(finalAllDataDto, rfiDataId);
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
        // do nothing now, all data are set by previous page (select & save)
        facilityRegistrationService.preAcknowledge(bpc);
    }

    public void print(BaseProcessClass bpc) {
        facilityRegistrationService.print(bpc);
    }
}

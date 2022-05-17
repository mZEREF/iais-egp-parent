package sg.gov.moh.iais.egp.bsb.action;


import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.dto.filerepo.FileRepoDto;
import com.ecquaria.cloud.moh.iais.common.exception.IaisRuntimeException;
import com.ecquaria.cloud.moh.iais.common.utils.MaskUtil;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import sg.gov.moh.iais.egp.bsb.client.FacilityRegisterClient;
import sg.gov.moh.iais.egp.bsb.client.FileRepoClient;
import sg.gov.moh.iais.egp.bsb.common.node.NodeGroup;
import sg.gov.moh.iais.egp.bsb.common.node.Nodes;
import sg.gov.moh.iais.egp.bsb.common.node.simple.SimpleNode;
import sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants;
import sg.gov.moh.iais.egp.bsb.constant.module.RfiConstants;
import sg.gov.moh.iais.egp.bsb.dto.ResponseDto;
import sg.gov.moh.iais.egp.bsb.dto.file.NewFileSyncDto;
import sg.gov.moh.iais.egp.bsb.dto.info.common.AppMainInfo;
import sg.gov.moh.iais.egp.bsb.dto.register.facility.*;
import sg.gov.moh.iais.egp.bsb.service.FacilityRegistrationService;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

import static sg.gov.moh.iais.egp.bsb.constant.FacRegisterConstants.*;


@Slf4j
@Delegator("bsbRfiFacilityRegisterDelegator")
public class RfiFacilityRegistrationDelegator {
    private final FileRepoClient fileRepoClient;
    private final FacilityRegisterClient facRegClient;
    private final FacilityRegistrationService facilityRegistrationService;

    @Autowired
    public RfiFacilityRegistrationDelegator(FileRepoClient fileRepoClient,
                                            FacilityRegisterClient facRegClient, FacilityRegistrationService facilityRegistrationService) {
        this.fileRepoClient = fileRepoClient;
        this.facRegClient = facRegClient;
        this.facilityRegistrationService = facilityRegistrationService;
    }

    public void start(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        request.getSession().removeAttribute(KEY_ROOT_NODE_GROUP);
        AuditTrailHelper.auditFunction(MODULE_NAME_NEW, MODULE_NAME_NEW);
    }

    public void init(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        String maskedAppId = request.getParameter(RfiConstants.KEY_APP_ID);
        boolean failRetrieveRfiData = true;
        if (StringUtils.hasLength(maskedAppId)) {
            if (log.isInfoEnabled()) {
                log.info("masked application ID: {}", org.apache.commons.lang.StringUtils.normalizeSpace(maskedAppId));
            }
            String appId = MaskUtil.unMaskValue(RfiConstants.KEY_RFI_APP_ID, maskedAppId);
            if (appId != null) {
                ResponseDto<FacilityRegisterDto> resultDto = facRegClient.getFacilityRegistrationAppDataByApplicationId(appId);
                if (resultDto.ok()) {
                    failRetrieveRfiData = false;
                    facilityRegistrationService.retrieveFacRegRoot(request, resultDto);
                    facilityRegistrationService.retrieveOrgAddressInfo(request);
                }
                ParamUtil.setRequestAttr(request, RfiConstants.KEY_APP_ID, appId);
            }
        }
        if (failRetrieveRfiData) {
            throw new IaisRuntimeException("Fail to retrieve rfi data");
        }

        NodeGroup facRegRoot = (NodeGroup) ParamUtil.getSessionAttr(request, KEY_ROOT_NODE_GROUP);

        SimpleNode facSelectionNode = (SimpleNode) facRegRoot.getNode(NODE_NAME_FAC_SELECTION);
        FacilitySelectionDto selectionDto = (FacilitySelectionDto) facSelectionNode.getValue();

        boolean isCf = MasterCodeConstants.CERTIFIED_CLASSIFICATION.contains(selectionDto.getFacClassification());
        ParamUtil.setSessionAttr(request, KEY_IS_CF, isCf ? Boolean.TRUE : Boolean.FALSE);
        boolean isUcf = MasterCodeConstants.UNCERTIFIED_CLASSIFICATION.contains(selectionDto.getFacClassification());
        ParamUtil.setSessionAttr(request, KEY_IS_UCF, isUcf ? Boolean.TRUE : Boolean.FALSE);

        ParamUtil.setSessionAttr(request, KEY_SELECTED_CLASSIFICATION, selectionDto.getFacClassification());
        ParamUtil.setSessionAttr(request, KEY_SELECTED_ACTIVITIES, new ArrayList<>(selectionDto.getActivityTypes()));

        Nodes.passValidation(facRegRoot, NODE_NAME_FAC_SELECTION);
        facilityRegistrationService.jump(request, facRegRoot, KEY_NAV_NEXT);
        ParamUtil.setSessionAttr(request, KEY_ROOT_NODE_GROUP, facRegRoot);
    }

    public void preCompInfo(BaseProcessClass bpc) {
        facilityRegistrationService.preCompInfo(bpc);
    }

    public void handleCompInfo(BaseProcessClass bpc) {
        facilityRegistrationService.handleCompInfo(bpc);
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

                    // save docs
                    log.info("Save documents into file-repo");
                    PrimaryDocDto primaryDocDto = (PrimaryDocDto) ((SimpleNode) facRegRoot.at(NODE_NAME_PRIMARY_DOC)).getValue();
                    List<NewFileSyncDto> primaryDocNewFiles = facilityRegistrationService.saveNewUploadedDoc(primaryDocDto);
                    FacilityProfileDto profileDto = (FacilityProfileDto) ((SimpleNode) facRegRoot.at(NODE_NAME_FAC_INFO + facRegRoot.getPathSeparator() + NODE_NAME_FAC_PROFILE)).getValue();
                    List<NewFileSyncDto> profileNewFiles = facilityRegistrationService.saveProfileNewUploadedDoc(profileDto);
                    FacilityCommitteeDto committeeDto = (FacilityCommitteeDto) ((SimpleNode) facRegRoot.at(NODE_NAME_FAC_INFO + facRegRoot.getPathSeparator() + NODE_NAME_FAC_COMMITTEE)).getValue();
                    NewFileSyncDto committeeNewFile = facilityRegistrationService.saveCommitteeNewDataFile(committeeDto);
                    FacilityAuthoriserDto authDto = (FacilityAuthoriserDto) ((SimpleNode) facRegRoot.at(NODE_NAME_FAC_INFO + facRegRoot.getPathSeparator() + NODE_NAME_FAC_AUTH)).getValue();
                    NewFileSyncDto authoriserNewFile = facilityRegistrationService.saveAuthoriserNewDataFile(authDto);
                    List<NewFileSyncDto> newFilesToSync = new ArrayList<>(primaryDocNewFiles.size() + profileNewFiles.size() + 2);
                    newFilesToSync.addAll(primaryDocNewFiles);
                    newFilesToSync.addAll(profileNewFiles);
                    if (committeeNewFile != null) {
                        newFilesToSync.add(committeeNewFile);
                    }
                    if (authoriserNewFile != null) {
                        newFilesToSync.add(authoriserNewFile);
                    }


                    // save data
                    log.info("Save facility registration data");
                    FacilityRegisterDto finalAllDataDto = FacilityRegistrationService.getRegisterDtoFromFacRegRoot(facRegRoot);
                    ResponseDto<AppMainInfo> responseDto = facRegClient.saveNewRegisteredFacility(finalAllDataDto);
                    log.info("save new facility response: {}", org.apache.commons.lang.StringUtils.normalizeSpace(responseDto.toString()));
                    AppMainInfo appMainInfo = responseDto.getEntity();
                    ParamUtil.setRequestAttr(request, KEY_APP_NO, appMainInfo.getAppNo());
                    ParamUtil.setRequestAttr(request, KEY_APP_DT, appMainInfo.getDate());

                    try {
                        // delete docs
                        log.info("Delete already saved documents in file-repo");
                        List<String> primaryToBeDeletedRepoIds = facilityRegistrationService.deleteUnwantedDoc(primaryDocDto.getToBeDeletedRepoIds());
                        List<String> profileToBeDeletedRepoIds = facilityRegistrationService.deleteUnwantedDoc(profileDto.getToBeDeletedRepoIds());
                        List<String> toBeDeletedRepoIds = new ArrayList<>(primaryToBeDeletedRepoIds.size() + profileToBeDeletedRepoIds.size() + 2);
                        if (committeeDto.getToBeDeletedRepoId() != null) {
                            FileRepoDto committeeDeleteDto = new FileRepoDto();
                            committeeDeleteDto.setId(committeeDto.getToBeDeletedRepoId());
                            fileRepoClient.removeFileById(committeeDeleteDto);
                            toBeDeletedRepoIds.add(committeeDto.getToBeDeletedRepoId());
                            committeeDto.setToBeDeletedRepoId(null);
                        }
                        if (authDto.getToBeDeletedRepoId() != null) {
                            FileRepoDto authoriserDeleteDto = new FileRepoDto();
                            authoriserDeleteDto.setId(authDto.getToBeDeletedRepoId());
                            fileRepoClient.removeFileById(authoriserDeleteDto);
                            toBeDeletedRepoIds.add(authDto.getToBeDeletedRepoId());
                            authDto.setToBeDeletedRepoId(null);
                        }
                        toBeDeletedRepoIds.addAll(primaryToBeDeletedRepoIds);
                        toBeDeletedRepoIds.addAll(profileToBeDeletedRepoIds);
                        // sync docs
                        log.info("Sync new uploaded documents to BE");
                        facilityRegistrationService.syncNewDocsAndDeleteFiles(newFilesToSync, toBeDeletedRepoIds);
                    } catch (Exception e) {
                        log.error("Fail to synchronize documents", e);
                    }

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
    }

    public void print(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        facilityRegistrationService.preparePreviewData(request);
    }
}

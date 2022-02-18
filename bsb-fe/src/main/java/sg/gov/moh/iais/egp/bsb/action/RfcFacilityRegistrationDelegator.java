package sg.gov.moh.iais.egp.bsb.action;


import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.exception.IaisRuntimeException;
import com.ecquaria.cloud.moh.iais.common.utils.MaskUtil;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import sg.gov.moh.iais.egp.bsb.client.FacilityRegisterClient;
import sg.gov.moh.iais.egp.bsb.common.node.NodeGroup;
import sg.gov.moh.iais.egp.bsb.common.node.simple.SimpleNode;
import sg.gov.moh.iais.egp.bsb.common.rfc.DecisionFlowType;
import sg.gov.moh.iais.egp.bsb.common.rfc.DecisionFlowTypeImpl;
import sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants;
import sg.gov.moh.iais.egp.bsb.dto.ResponseDto;
import sg.gov.moh.iais.egp.bsb.dto.file.NewFileSyncDto;
import sg.gov.moh.iais.egp.bsb.dto.register.facility.*;
import sg.gov.moh.iais.egp.bsb.constant.RfcFlowType;
import sg.gov.moh.iais.egp.bsb.service.FacilityRegistrationService;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

import static sg.gov.moh.iais.egp.bsb.constant.FacRegisterConstants.*;


@Slf4j
@Delegator("rfcFacilityRegisterDelegator")
public class RfcFacilityRegistrationDelegator {
    private final FacilityRegisterClient facRegClient;
    private final FacilityRegistrationService facilityRegistrationService;

    @Autowired
    public RfcFacilityRegistrationDelegator(FacilityRegisterClient facRegClient, FacilityRegistrationService facilityRegistrationService) {
        this.facRegClient = facRegClient;
        this.facilityRegistrationService = facilityRegistrationService;
    }

    public void start(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        request.getSession().removeAttribute(KEY_PROCESS_TYPE);
        request.getSession().removeAttribute(KEY_ROOT_NODE_GROUP);
        request.getSession().removeAttribute(KEY_OLD_FACILITY_REGISTER_DTO);
        AuditTrailHelper.auditFunction(MODULE_NAME_RFC, MODULE_NAME_RFC);
    }

    public void init(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        // check if we are doing editing
        String maskedAppId = request.getParameter(KEY_EDIT_APP_ID);
        if (StringUtils.hasLength(maskedAppId)) {
            if (log.isInfoEnabled()) {
                log.info("masked app ID: {}", org.apache.commons.lang.StringUtils.normalizeSpace(maskedAppId));
            }
            boolean failRetrieveEditData = true;
            String appId = MaskUtil.unMaskValue(KEY_EDIT_APP_ID, maskedAppId);
            if (appId != null && !maskedAppId.equals(appId)) {
                ResponseDto<FacilityRegisterDto> resultDto = facRegClient.getFacilityRegistrationAppDataByApprovalId(appId);
                if (resultDto.ok()) {
                    failRetrieveEditData = false;
                    //This oldFacilityRegisterDto is the original data, which is for comparison with each DTO modification before Submit(unchangeable)
                    FacilityRegisterDto oldFacilityRegisterDto = resultDto.getEntity();
                    ParamUtil.setSessionAttr(request, KEY_OLD_FACILITY_REGISTER_DTO, oldFacilityRegisterDto);

                    NodeGroup facRegRoot = oldFacilityRegisterDto.toFacRegRootGroup(KEY_ROOT_NODE_GROUP);
                    ParamUtil.setSessionAttr(request, KEY_ROOT_NODE_GROUP, facRegRoot);
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
        facilityRegistrationService.handleServiceSelection(bpc);
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

    public void preFacInfoAuthoriser(BaseProcessClass bpc) {
        facilityRegistrationService.preFacInfoAuthoriser(bpc);
    }

    public void handleFacInfoAuthoriser(BaseProcessClass bpc) {
        facilityRegistrationService.handleFacInfoAuthoriser(bpc);
    }

    public void preFacAdmin(BaseProcessClass bpc) {
        facilityRegistrationService.preFacAdmin(bpc);
    }

    public void handleFacAdmin(BaseProcessClass bpc) {
        facilityRegistrationService.handleFacAdmin(bpc);
    }

    public void preFacOfficer(BaseProcessClass bpc) {
        facilityRegistrationService.preFacOfficer(bpc);
    }

    public void handleFacOfficer(BaseProcessClass bpc) {
        facilityRegistrationService.handleFacOfficer(bpc);
    }

    public void preFacInfoCommittee(BaseProcessClass bpc) {
        facilityRegistrationService.preFacInfoCommittee(bpc);
    }

    public void handleFacInfoCommittee(BaseProcessClass bpc) {
        facilityRegistrationService.handleFacInfoCommittee(bpc);
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

    public void prePreviewSubmit(BaseProcessClass bpc) {
        facilityRegistrationService.prePreviewSubmit(bpc);
    }

    public void handlePreviewSubmit(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        NodeGroup facRegRoot = (NodeGroup) ParamUtil.getSessionAttr(request, KEY_ROOT_NODE_GROUP);
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
                    List<NewFileSyncDto> newFilesToSync = new ArrayList<>(primaryDocNewFiles.size() + profileNewFiles.size());
                    newFilesToSync.addAll(primaryDocNewFiles);
                    newFilesToSync.addAll(profileNewFiles);

                    FacilityRegisterDto finalAllDataDto = FacilityRegisterDto.from(facRegRoot);
                    //rfc compare to decision flowType
                    FacilityRegisterDto oldFacilityRegisterDto = (FacilityRegisterDto)ParamUtil.getSessionAttr(request, KEY_OLD_FACILITY_REGISTER_DTO);
                    DecisionFlowType flowType = new DecisionFlowTypeImpl();
                    RfcFlowType rfcFlowType = flowType.facRegFlowType(facilityRegistrationService.compareTwoDto(oldFacilityRegisterDto,finalAllDataDto));
                    ParamUtil.setRequestAttr(request, "rfcFlowType", rfcFlowType);
                    if (rfcFlowType == RfcFlowType.AMENDMENT || rfcFlowType == RfcFlowType.NOTIFICATION){
                        // save data
                        log.info("Save rfc facility registration data");
                        ResponseDto<String> responseDto = facRegClient.saveAmendmentFacility(finalAllDataDto);
                        log.info("save rfc facility response: {}", org.apache.commons.lang.StringUtils.normalizeSpace(responseDto.toString()));

                        try {
                            // delete docs
                            log.info("Delete already saved documents in file-repo");
                            List<String> primaryToBeDeletedRepoIds = facilityRegistrationService.deleteUnwantedDoc(primaryDocDto.getToBeDeletedRepoIds());
                            List<String> profileToBeDeletedRepoIds = facilityRegistrationService.deleteUnwantedDoc(profileDto.getToBeDeletedRepoIds());
                            List<String> toBeDeletedRepoIds = new ArrayList<>(primaryToBeDeletedRepoIds.size() + profileToBeDeletedRepoIds.size());
                            toBeDeletedRepoIds.addAll(primaryToBeDeletedRepoIds);
                            toBeDeletedRepoIds.addAll(profileToBeDeletedRepoIds);
                            // sync docs
                            log.info("Sync new uploaded documents to BE");
                            facilityRegistrationService.syncNewDocsAndDeleteFiles(newFilesToSync, toBeDeletedRepoIds);
                        } catch (Exception e) {
                            log.error("Fail to synchronize documents", e);
                        }
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
        facilityRegistrationService.actionFilter(bpc, MasterCodeConstants.APP_TYPE_RFC);
    }

    public void preAcknowledge(BaseProcessClass bpc) {
        facilityRegistrationService.preAcknowledge(bpc);
    }
}

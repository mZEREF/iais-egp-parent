package sg.gov.moh.iais.egp.bsb.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.exception.IaisRuntimeException;
import com.ecquaria.cloud.moh.iais.common.utils.MaskUtil;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import sg.gov.moh.iais.egp.bsb.client.BsbFileClient;
import sg.gov.moh.iais.egp.bsb.client.FileRepoClient;
import sg.gov.moh.iais.egp.bsb.common.node.NodeGroup;
import sg.gov.moh.iais.egp.bsb.common.node.simple.SimpleNode;
import sg.gov.moh.iais.egp.bsb.common.rfc.DecisionFlowType;
import sg.gov.moh.iais.egp.bsb.common.rfc.DecisionFlowTypeImpl;
import sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants;
import sg.gov.moh.iais.egp.bsb.constant.RfcFlowType;
import sg.gov.moh.iais.egp.bsb.dto.file.NewFileSyncDto;
import sg.gov.moh.iais.egp.bsb.dto.register.afc.*;
import sg.gov.moh.iais.egp.bsb.service.FacilityCertifierRegistrationService;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

import static sg.gov.moh.iais.egp.bsb.constant.FacCertifierRegisterConstants.*;

@Slf4j
@Delegator("rfcFacCertifierRegisterDelegator")
public class RfcFacCertifierRegistrationDelegator {
    private static final String MODULE_NAME = "RFC Facility Certifier Registration";

    private final FileRepoClient fileRepoClient;
    private final BsbFileClient bsbFileClient;
    private final FacilityCertifierRegistrationService facilityCertifierRegistrationService;

    @Autowired
    public RfcFacCertifierRegistrationDelegator(FileRepoClient fileRepoClient, BsbFileClient bsbFileClient, FacilityCertifierRegistrationService facilityCertifierRegistrationService) {
        this.fileRepoClient = fileRepoClient;
        this.bsbFileClient = bsbFileClient;
        this.facilityCertifierRegistrationService = facilityCertifierRegistrationService;
    }

    public void start(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        request.getSession().removeAttribute(KEY_PROCESS_TYPE);
        request.getSession().removeAttribute(KEY_ROOT_NODE_GROUP);
        request.getSession().removeAttribute(KEY_OLD_FAC_CER_REG_DTO);
        AuditTrailHelper.auditFunction(MODULE_NAME, MODULE_NAME);
    }

    public void init(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        //charge if maskedAppId is null
        String maskedAppId = request.getParameter(KEY_EDIT_APP_ID);
        if(StringUtils.hasLength(maskedAppId)){
            if (log.isInfoEnabled()) {
                log.info("masked app ID: {}", org.apache.commons.lang.StringUtils.normalizeSpace(maskedAppId));
            }
            boolean failRetrieveEditData = true;
            String appId = MaskUtil.unMaskValue(KEY_EDIT_APP_ID,maskedAppId);
            if(appId != null && !maskedAppId.equals(appId)){
                //This oldFacilityCertifierRegisterDto is the original data, which is for comparison with each DTO modification before Submit(unchangeable)
                FacilityCertifierRegisterDto oldFacilityCertifierRegisterDto = facilityCertifierRegistrationService.getCertifierRegistrationAppDataByApprovalId(appId);
                ParamUtil.setSessionAttr(request, KEY_OLD_FAC_CER_REG_DTO, oldFacilityCertifierRegisterDto);
                NodeGroup facRegRoot = oldFacilityCertifierRegisterDto.toFacilityCertRegister(KEY_ROOT_NODE_GROUP);
                ParamUtil.setSessionAttr(request, KEY_ROOT_NODE_GROUP, facRegRoot);
                failRetrieveEditData = false;
            }
            if(failRetrieveEditData){
                throw new IaisRuntimeException("Fail to retrieve app data");
            }
        }
        String maskedProcessType = request.getParameter(KEY_PROCESS_TYPE);
        String processType = MaskUtil.unMaskValue(KEY_PROCESS_TYPE,maskedProcessType);
        ParamUtil.setSessionAttr(request, KEY_PROCESS_TYPE, processType);
    }


    public void preCompInfo(BaseProcessClass bpc){
        facilityCertifierRegistrationService.preCompInfo(bpc);
    }

    public void doCompInfo(BaseProcessClass bpc){
        facilityCertifierRegistrationService.doCompInfo(bpc);
    }

    public void preOrganisationInfo(BaseProcessClass bpc){
        facilityCertifierRegistrationService.preOrganisationInfo(bpc);
    }

    public void doOrganisationProfile(BaseProcessClass bpc){
        facilityCertifierRegistrationService.doOrganisationProfile(bpc);
    }

    public void preAdministrator(BaseProcessClass bpc){
        facilityCertifierRegistrationService.preAdministrator(bpc);
    }

    public void doAdministrator(BaseProcessClass bpc){
        facilityCertifierRegistrationService.doAdministrator(bpc);
    }

    public void preCertifyingTeam(BaseProcessClass bpc){
        facilityCertifierRegistrationService.preCertifyingTeam(bpc);
    }

    public void doCertifyingTeam(BaseProcessClass bpc){
        facilityCertifierRegistrationService.doCertifyingTeam(bpc);
    }

    public void prepareDocuments(BaseProcessClass bpc){
        facilityCertifierRegistrationService.prepareDocuments(bpc);
    }

    public void doDocument(BaseProcessClass bpc){
        facilityCertifierRegistrationService.doDocument(bpc);
    }

    public void preparePreview(BaseProcessClass bpc){
        facilityCertifierRegistrationService.preparePreview(bpc);
    }

    public void doPreview(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        NodeGroup facRegRoot = (NodeGroup) ParamUtil.getSessionAttr(request, KEY_ROOT_NODE_GROUP);
        String currentNodePath = NODE_NAME_CER_PREVIEW_SUBMIT;
        SimpleNode previewSubmitNode = (SimpleNode) facRegRoot.at(currentNodePath);
        PreviewSubmitDto previewSubmitDto = (PreviewSubmitDto) previewSubmitNode.getValue();
        previewSubmitDto.reqObjMapping(request);

        String actionType = ParamUtil.getString(request, KEY_ACTION_TYPE);
        String actionValue = ParamUtil.getString(request, KEY_ACTION_VALUE);
        if (KEY_ACTION_JUMP.equals(actionType)) {
            if (KEY_NAV_NEXT.equals(actionValue)) {
                if (previewSubmitDto.doValidation()) {
                    previewSubmitNode.passValidation();

                    //upload document
                    SimpleNode primaryDocNode = (SimpleNode) facRegRoot.at(NODE_NAME_FAC_PRIMARY_DOCUMENT);
                    PrimaryDocDto primaryDocDto = (PrimaryDocDto) primaryDocNode.getValue();
                    List<NewFileSyncDto> newFilesToSync = facilityCertifierRegistrationService.saveNewUploadedDoc(primaryDocDto);

                    FacilityCertifierRegisterDto finalAllDataDto = FacilityCertifierRegisterDto.from(facRegRoot);
                    //rfc compare to decision flowType
                    FacilityCertifierRegisterDto oldFacilityCertifierRegisterDto = (FacilityCertifierRegisterDto)ParamUtil.getSessionAttr(request,KEY_OLD_FAC_CER_REG_DTO);
                    DecisionFlowType flowType = new DecisionFlowTypeImpl();
                    RfcFlowType rfcFlowType = flowType.facCerRegFlowType(facilityCertifierRegistrationService.compareTwoDto(oldFacilityCertifierRegisterDto,finalAllDataDto));
                    ParamUtil.setRequestAttr(request, "rfcFlowType", rfcFlowType);
                    if (rfcFlowType == RfcFlowType.AMENDMENT || rfcFlowType == RfcFlowType.NOTIFICATION){
                        // save data
                        finalAllDataDto.setAppStatus(MasterCodeConstants.APP_STATUS_PEND_DO);
                        String response = facilityCertifierRegistrationService.saveAmendmentFacCertifier(finalAllDataDto);
                        log.info("save rfc facility certifier registration response: {}", org.apache.commons.lang.StringUtils.normalizeSpace(response));

                        try {
                            // delete docs
                            List<String> toBeDeletedRepoIds = facilityCertifierRegistrationService.deleteUnwantedDoc(primaryDocDto);
                            // sync docs
                            facilityCertifierRegistrationService.syncNewDocsAndDeleteFiles(newFilesToSync, toBeDeletedRepoIds);
                        } catch (Exception e) {
                            log.error("Fail to sync files to BE", e);
                        }
                    }
                    ParamUtil.setRequestAttr(request, KEY_ACTION_TYPE, KEY_ACTION_SUBMIT);
                } else {
                    ParamUtil.setRequestAttr(request, KEY_SHOW_ERROR_SWITCH, Boolean.TRUE);
                    ParamUtil.setSessionAttr(request, KEY_JUMP_DEST_NODE, NODE_NAME_CER_PREVIEW_SUBMIT);
                }
            } else {
                facilityCertifierRegistrationService.jumpHandler(request, facRegRoot, currentNodePath, previewSubmitNode);
            }
        } else if (KEY_NAV_SAVE_DRAFT.equals(actionType)){
            ParamUtil.setRequestAttr(request, KEY_ACTION_TYPE, KEY_NAV_SAVE_DRAFT);
            ParamUtil.setSessionAttr(request, KEY_JUMP_DEST_NODE, NODE_NAME_FAC_PRIMARY_DOCUMENT);
        } else {
            throw new IaisRuntimeException(ERR_MSG_INVALID_ACTION);
        }
        ParamUtil.setSessionAttr(request, KEY_ROOT_NODE_GROUP, facRegRoot);
    }

    public void prepare(BaseProcessClass bpc){
        facilityCertifierRegistrationService.prepare(bpc);
    }

    public void controlSwitch(BaseProcessClass bpc){
        facilityCertifierRegistrationService.controlSwitch(bpc, MasterCodeConstants.APP_TYPE_RFC);
    }
}

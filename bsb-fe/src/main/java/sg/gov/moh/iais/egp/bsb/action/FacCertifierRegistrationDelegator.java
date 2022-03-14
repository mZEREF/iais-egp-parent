package sg.gov.moh.iais.egp.bsb.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.exception.IaisRuntimeException;
import com.ecquaria.cloud.moh.iais.common.utils.MaskUtil;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import sg.gov.moh.iais.egp.bsb.common.node.NodeGroup;
import sg.gov.moh.iais.egp.bsb.common.node.simple.SimpleNode;
import sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants;
import sg.gov.moh.iais.egp.bsb.dto.file.NewFileSyncDto;
import sg.gov.moh.iais.egp.bsb.dto.register.afc.*;
import sg.gov.moh.iais.egp.bsb.service.FacilityCertifierRegistrationService;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;

import java.util.List;

import static sg.gov.moh.iais.egp.bsb.constant.FacCertifierRegisterConstants.*;

/**
 *@author YiMing
 * @version 2021/10/15 14:16
 **/

@Slf4j
@Delegator("bsbFacCertifierRegisterDelegator")
public class FacCertifierRegistrationDelegator {
    private final FacilityCertifierRegistrationService facilityCertifierRegistrationService;

    public FacCertifierRegistrationDelegator(FacilityCertifierRegistrationService facilityCertifierRegistrationService) {
        this.facilityCertifierRegistrationService = facilityCertifierRegistrationService;
    }


    public void init(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        request.getSession().removeAttribute(KEY_ROOT_NODE_GROUP);
        //charge if new node
        boolean newCertRegNode = true;
        //charge if maskedAppId is null
        String maskedAppId = request.getParameter(KEY_EDIT_APP_ID);
        if(StringUtils.hasLength(maskedAppId)){
            if (log.isInfoEnabled()) {
                log.info("masked app ID: {}", org.apache.commons.lang.StringUtils.normalizeSpace(maskedAppId));
            }
            newCertRegNode = false;
            boolean failRetrieveEditData = true;
            String appId = MaskUtil.unMaskValue(KEY_EDIT_APP_ID,maskedAppId);
            if(appId != null && !maskedAppId.equals(appId)){
                FacilityCertifierRegisterDto dto = facilityCertifierRegistrationService.getCertifierRegistrationAppData(appId);
                NodeGroup facRegRoot = dto.toFacilityCertRegister(KEY_ROOT_NODE_GROUP);
                ParamUtil.setSessionAttr(request, KEY_ROOT_NODE_GROUP, facRegRoot);
                failRetrieveEditData = false;
            }
            if(failRetrieveEditData){
                throw new IaisRuntimeException("Fail to retrieve app data");
            }
        }
        if(newCertRegNode){
            ParamUtil.setSessionAttr(request, KEY_ROOT_NODE_GROUP, facilityCertifierRegistrationService.getFacCertifierRegisterRoot(request));
        }
    }


    public void preCompInfo(BaseProcessClass bpc){
        facilityCertifierRegistrationService.preCompInfo(bpc);
    }

    public void doCompInfo(BaseProcessClass bpc){
        facilityCertifierRegistrationService.doCompInfo(bpc);
    }

    public void preCompanyProfile(BaseProcessClass bpc){
        facilityCertifierRegistrationService.preCompanyProfile(bpc);
    }

    public void doCompanyProfile(BaseProcessClass bpc){
        facilityCertifierRegistrationService.doCompanyProfile(bpc);
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
        NodeGroup facRegRoot = facilityCertifierRegistrationService.getFacCertifierRegisterRoot(request);
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

                    // save data
                    FacilityCertifierRegisterDto finalAllDataDto = FacilityCertifierRegisterDto.from(facRegRoot);
                    finalAllDataDto.setAppStatus(MasterCodeConstants.APP_STATUS_PEND_DO);
                    String response = facilityCertifierRegistrationService.saveNewRegisteredFacCertifier(finalAllDataDto);
                    log.info("save new facilityCertifierRegister response: {}", org.apache.commons.lang.StringUtils.normalizeSpace(response));

                    try {
                        // delete docs
                        List<String> toBeDeletedRepoIds = facilityCertifierRegistrationService.deleteUnwantedDoc(primaryDocDto);
                        // sync docs
                        facilityCertifierRegistrationService.syncNewDocsAndDeleteFiles(newFilesToSync, toBeDeletedRepoIds);
                    } catch (Exception e) {
                        log.error("Fail to sync files to BE", e);
                    }

                    ParamUtil.setRequestAttr(request, KEY_ACTION_TYPE, KEY_ACTION_SUBMIT);
                } else {
                    ParamUtil.setRequestAttr(request, KEY_SHOW_ERROR_SWITCH, Boolean.TRUE);
                    ParamUtil.setSessionAttr(request, KEY_JUMP_DEST_NODE, NODE_NAME_CER_PREVIEW_SUBMIT);
                }
            } else {
                facilityCertifierRegistrationService.jumpHandler(request, facRegRoot, currentNodePath, previewSubmitNode);
            }
        } else if (KEY_NAV_SAVE_DRAFT.equals(actionType)) {
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
        facilityCertifierRegistrationService.controlSwitch(bpc, MasterCodeConstants.APP_TYPE_NEW);
    }
}

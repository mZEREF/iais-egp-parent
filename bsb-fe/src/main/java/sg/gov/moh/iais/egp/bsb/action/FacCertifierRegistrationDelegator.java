package sg.gov.moh.iais.egp.bsb.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.dto.filerepo.FileRepoDto;
import com.ecquaria.cloud.moh.iais.common.exception.IaisRuntimeException;
import com.ecquaria.cloud.moh.iais.common.utils.MaskUtil;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import sg.gov.moh.iais.egp.bsb.client.FacCertifierRegisterClient;
import sg.gov.moh.iais.egp.bsb.client.FileRepoClient;
import sg.gov.moh.iais.egp.bsb.common.node.NodeGroup;
import sg.gov.moh.iais.egp.bsb.common.node.Nodes;
import sg.gov.moh.iais.egp.bsb.common.node.simple.SimpleNode;
import sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants;
import sg.gov.moh.iais.egp.bsb.dto.ResponseDto;
import sg.gov.moh.iais.egp.bsb.dto.file.NewFileSyncDto;
import sg.gov.moh.iais.egp.bsb.dto.info.common.OrgAddressInfo;
import sg.gov.moh.iais.egp.bsb.dto.register.afc.*;
import sg.gov.moh.iais.egp.bsb.service.FacilityCertifierRegistrationService;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;

import java.util.ArrayList;
import java.util.List;

import static sg.gov.moh.iais.egp.bsb.constant.FacCertifierRegisterConstants.*;
import static sg.gov.moh.iais.egp.bsb.constant.FacRegisterConstants.KEY_ORG_ADDRESS;

/**
 *@author YiMing
 * @version 2021/10/15 14:16
 **/

@Slf4j
@Delegator("bsbFacCertifierRegisterDelegator")
public class FacCertifierRegistrationDelegator {
    private final FacCertifierRegisterClient facCertifierRegisterClient;
    private final FileRepoClient fileRepoClient;
    private final FacilityCertifierRegistrationService facilityCertifierRegistrationService;

    public FacCertifierRegistrationDelegator(FacCertifierRegisterClient facCertifierRegisterClient, FileRepoClient fileRepoClient, FacilityCertifierRegistrationService facilityCertifierRegistrationService) {
        this.facCertifierRegisterClient = facCertifierRegisterClient;
        this.fileRepoClient = fileRepoClient;
        this.facilityCertifierRegistrationService = facilityCertifierRegistrationService;
    }

    public void start(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        request.getSession().removeAttribute(KEY_ROOT_NODE_GROUP);
        AuditTrailHelper.auditFunction(MODULE_NAME_NEW, MODULE_NAME_NEW);
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
                ResponseDto<FacilityCertifierRegisterDto> resultDto = facCertifierRegisterClient.getCertifierRegistrationAppData(appId);
                if(resultDto.ok()){
                    failRetrieveEditData = false;
                    NodeGroup facRegRoot = resultDto.getEntity().toFacilityCertRegisterGroup(KEY_ROOT_NODE_GROUP);

                    // check data uploaded by file
                    String certifyTeamNodePath = NODE_NAME_APPLICATION_INFO + facRegRoot.getPathSeparator() + NODE_NAME_CERTIFYING_TEAM_DETAIL;
                    SimpleNode certifyTeamNode = (SimpleNode) facRegRoot.at(certifyTeamNodePath);
                    CertifyingTeamDto certifyingTeamDto = (CertifyingTeamDto) certifyTeamNode.getValue();
                    /* If there is no committee data, we don't need to show error message.
                     * We call validation, if any error exists. The 'doValidation' method will set the errorVisible flag,
                     * so the error table should be displayed. This situation means user click save as draft when user
                     * upload a file contains error fields.
                     * If pass validation, we set the node status to avoid not necessary validation again. */
                    if (certifyingTeamDto.getAmount() > 0 && certifyingTeamDto.doValidation()) {
                        Nodes.passValidation(facRegRoot, certifyTeamNodePath);
                    }
                    ParamUtil.setSessionAttr(request, KEY_ROOT_NODE_GROUP, facRegRoot);
                }


            }
            if(failRetrieveEditData){
                throw new IaisRuntimeException("Fail to retrieve app data");
            }
        }
        if(newCertRegNode){
            ParamUtil.setSessionAttr(request, KEY_ROOT_NODE_GROUP, facilityCertifierRegistrationService.getFacCertifierRegisterRoot(request));
        }

        // TODO retrieve company address, and set in session
        OrgAddressInfo orgAddressInfo = new OrgAddressInfo();
        orgAddressInfo.setUen("185412420D");
        orgAddressInfo.setCompName("DBO Laboratories");
        orgAddressInfo.setPostalCode("980335");
        orgAddressInfo.setAddressType("ADDTY001");
        orgAddressInfo.setBlockNo("10");
        orgAddressInfo.setFloor("03");
        orgAddressInfo.setUnitNo("01");
        orgAddressInfo.setStreet("Toa Payoh Lorong 2");
        orgAddressInfo.setBuilding("-");
        ParamUtil.setSessionAttr(request, KEY_ORG_ADDRESS, orgAddressInfo);
    }


    public void preBeginFacilityCertifier(BaseProcessClass bpc){
        facilityCertifierRegistrationService.preBeginFacilityCertifier(bpc);
    }

    public void handleBeginFacilityCertifier(BaseProcessClass bpc){
        facilityCertifierRegistrationService.handleBeginFacilityCertifier(bpc);
    }

    public void preCompanyProfile(BaseProcessClass bpc){
        facilityCertifierRegistrationService.preCompanyProfile(bpc);
    }

    public void handleCompanyProfile(BaseProcessClass bpc){
        facilityCertifierRegistrationService.handleCompanyProfile(bpc);
    }

    public void preAdministrator(BaseProcessClass bpc){
        facilityCertifierRegistrationService.preAdministrator(bpc);
    }

    public void handleAdministrator(BaseProcessClass bpc){
        facilityCertifierRegistrationService.handleAdministrator(bpc);
    }

    public void preCertifyingTeam(BaseProcessClass bpc){
        facilityCertifierRegistrationService.preCertifyingTeam(bpc);
    }

    public void handleCertifyingTeam(BaseProcessClass bpc){
        facilityCertifierRegistrationService.handleCertifyingTeam(bpc);
    }

    public void preCertifyingTeamPreview(BaseProcessClass bpc){
        facilityCertifierRegistrationService.preCertifyingTeamPreview(bpc);
    }

    public void handleCertifyingTeamPreview(BaseProcessClass bpc){
        facilityCertifierRegistrationService.handleCertifyingTeamPreview(bpc);
    }

    public void preSupportingDoc(BaseProcessClass bpc){
        facilityCertifierRegistrationService.preSupportingDoc(bpc);
    }

    public void handleSupportingDoc(BaseProcessClass bpc){
        facilityCertifierRegistrationService.handleSupportingDoc(bpc);
    }

    public void preparePreviewSubmit(BaseProcessClass bpc){
        facilityCertifierRegistrationService.preparePreviewSubmit(bpc);
    }

    public void handlePreviewSubmit(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        NodeGroup facRegRoot = facilityCertifierRegistrationService.getFacCertifierRegisterRoot(request);
        String currentNodePath = NODE_NAME_FACILITY_CERTIFIER_PREVIEW_SUBMIT;
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
                    SimpleNode primaryDocNode = (SimpleNode) facRegRoot.at(NODE_NAME_SUPPORTING_DOCUMENT);
                    PrimaryDocDto primaryDocDto = (PrimaryDocDto) primaryDocNode.getValue();
                    List<NewFileSyncDto> newFilesToSync = facilityCertifierRegistrationService.saveNewUploadedDoc(primaryDocDto);
                    CertifyingTeamDto certifyingTeamDto = (CertifyingTeamDto) ((SimpleNode) facRegRoot.at(NODE_NAME_APPLICATION_INFO + facRegRoot.getPathSeparator() + NODE_NAME_CERTIFYING_TEAM_DETAIL)).getValue();
                    NewFileSyncDto certifyTeamNewFile = facilityCertifierRegistrationService.saveCertifyTeamNewDataFile(certifyingTeamDto);
                    if(certifyTeamNewFile != null){
                        newFilesToSync.add(certifyTeamNewFile);
                    }

                    // save data
                    FacilityCertifierRegisterDto finalAllDataDto = FacilityCertifierRegisterDto.from(facRegRoot);
                    finalAllDataDto.setAppStatus(MasterCodeConstants.APP_STATUS_PEND_DO);
                    String response = facilityCertifierRegistrationService.saveNewRegisteredFacCertifier(finalAllDataDto);
                    log.info("save new facilityCertifierRegister response: {}", org.apache.commons.lang.StringUtils.normalizeSpace(response));

                    try {
                        // delete docs
                        List<String> primaryToBeDeletedRepoIds = facilityCertifierRegistrationService.deleteUnwantedDoc(primaryDocDto);
                        List<String> toBeDeletedRepoIds = new ArrayList<>(primaryToBeDeletedRepoIds.size() + 1);
                        if(certifyingTeamDto.getToBeDeletedRepoId() != null){
                            FileRepoDto certifyTeamDeleteDto = new FileRepoDto();
                            certifyTeamDeleteDto.setId(certifyingTeamDto.getToBeDeletedRepoId());
                            fileRepoClient.removeFileById(certifyTeamDeleteDto);
                            toBeDeletedRepoIds.add(certifyingTeamDto.getToBeDeletedRepoId());
                            certifyingTeamDto.setToBeDeletedRepoId(null);
                        }
                        // sync docs
                        facilityCertifierRegistrationService.syncNewDocsAndDeleteFiles(newFilesToSync, toBeDeletedRepoIds);
                    } catch (Exception e) {
                        log.error("Fail to sync files to BE", e);
                    }

                    ParamUtil.setRequestAttr(request, KEY_ACTION_TYPE, KEY_ACTION_SUBMIT);
                } else {
                    ParamUtil.setRequestAttr(request, KEY_SHOW_ERROR_SWITCH, Boolean.TRUE);
                    ParamUtil.setSessionAttr(request, KEY_JUMP_DEST_NODE, NODE_NAME_FACILITY_CERTIFIER_PREVIEW_SUBMIT);
                }
            } else {
                facilityCertifierRegistrationService.jumpHandler(request, facRegRoot, currentNodePath, previewSubmitNode);
            }
        }else if (KEY_ACTION_EXPAND_FILE.equals(actionType)){
            CertifyingTeamDto certifyingTeamDto = (CertifyingTeamDto) ((SimpleNode) facRegRoot.at(NODE_NAME_APPLICATION_INFO + facRegRoot.getPathSeparator() + NODE_NAME_CERTIFYING_TEAM_DETAIL)).getValue();
            List<CertifyingTeamFileDto> dataList = certifyingTeamDto.getDataListForDisplay();
            ParamUtil.setRequestAttr(request, KEY_DATA_LIST, dataList);
            ParamUtil.setRequestAttr(request,KEY_ACTION_TYPE,KEY_ACTION_JUMP);
            ParamUtil.setSessionAttr(request,KEY_JUMP_DEST_NODE,STEP_NAME_FACILITY_CERTIFIER_PREVIEW);
        }else if (KEY_NAV_SAVE_DRAFT.equals(actionType)) {
            ParamUtil.setRequestAttr(request, KEY_ACTION_TYPE, KEY_NAV_SAVE_DRAFT);
            ParamUtil.setSessionAttr(request, KEY_JUMP_DEST_NODE, NODE_NAME_FACILITY_CERTIFIER_PREVIEW_SUBMIT);
    } else {
            throw new IaisRuntimeException(ERR_MSG_INVALID_ACTION);
        }
        ParamUtil.setSessionAttr(request, KEY_ROOT_NODE_GROUP, facRegRoot);
    }

    public void jumpFilter(BaseProcessClass bpc){
        facilityCertifierRegistrationService.jumpFilter(bpc);
    }

    public void actionFilter(BaseProcessClass bpc){
        facilityCertifierRegistrationService.actionFilter(bpc, MasterCodeConstants.APP_TYPE_NEW);
    }

    public void preAcknowledge(BaseProcessClass bpc) {
        // do someThings after
    }

    public void print(BaseProcessClass bpc){
        //do print logic
    }
}

package sg.gov.moh.iais.egp.bsb.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.AuditTrailConsts;
import com.ecquaria.cloud.moh.iais.common.dto.AuditTrailDto;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.filerepo.FileRepoDto;
import com.ecquaria.cloud.moh.iais.common.dto.mastercode.MasterCodeView;
import com.ecquaria.cloud.moh.iais.common.exception.IaisRuntimeException;
import com.ecquaria.cloud.moh.iais.common.utils.MaskUtil;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.MasterCodeUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import sg.gov.moh.iais.egp.bsb.client.BsbFileClient;
import sg.gov.moh.iais.egp.bsb.client.FacCertifierRegisterClient;
import sg.gov.moh.iais.egp.bsb.client.FileRepoClient;
import sg.gov.moh.iais.egp.bsb.common.node.Node;
import sg.gov.moh.iais.egp.bsb.common.node.NodeGroup;
import sg.gov.moh.iais.egp.bsb.common.node.Nodes;
import sg.gov.moh.iais.egp.bsb.common.node.simple.SimpleNode;
import sg.gov.moh.iais.egp.bsb.common.rfc.CompareTwoObject;
import sg.gov.moh.iais.egp.bsb.common.rfc.DecisionFlowType;
import sg.gov.moh.iais.egp.bsb.common.rfc.DecisionFlowTypeImpl;
import sg.gov.moh.iais.egp.bsb.constant.DocConstants;
import sg.gov.moh.iais.egp.bsb.constant.RfcFlowType;
import sg.gov.moh.iais.egp.bsb.dto.ResponseDto;
import sg.gov.moh.iais.egp.bsb.dto.file.DocRecordInfo;
import sg.gov.moh.iais.egp.bsb.dto.file.FileRepoSyncDto;
import sg.gov.moh.iais.egp.bsb.dto.file.NewDocInfo;
import sg.gov.moh.iais.egp.bsb.dto.file.NewFileSyncDto;
import sg.gov.moh.iais.egp.bsb.dto.register.afc.*;
import sg.gov.moh.iais.egp.bsb.dto.rfc.DiffContent;
import sg.gov.moh.iais.egp.bsb.entity.DocSetting;
import sg.gov.moh.iais.egp.bsb.util.LogUtil;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static sg.gov.moh.iais.egp.bsb.constant.FacCertifierRegisterConstants.*;

@Slf4j
@Delegator("rfcFacCertifierRegisterDelegator")
public class RfcFacCertifierRegistrationDelegator {
    public static final String MODULE_NAME = "Facility Certifier Registration";
    public static final String KEY_ROOT_NODE_GROUP = "facCertifierRegRoot";

    private static final String KEY_EDIT_APP_ID = "editId";
    private static final String KEY_ACTION_TYPE = "action_type";
    private static final String KEY_INDEED_ACTION_TYPE = "indeed_action_type";
    private static final String KEY_ACTION_VALUE = "action_value";
    private static final String KEY_VALIDATION_ERRORS = "errorMsg";
    private static final String TEXT_VALUE_PLEASE_SELECT = "Please Select";
    private static final String TEXT_VALUE_SINGAPORE = "Singapore";
    private static final String TEXT_VALUE_CHINA = "China";
    private static final String TEXT_VALUE_MALAYSIA = "Malaysia";

    private static final String KEY_NAV_NEXT = "next";
    private static final String KEY_NAV_BACK = "back";
    private static final String KEY_NAV_SAVE_DRAFT = "draft";

    private static final String KEY_NATIONALITY_OPTIONS = "nationalityOps";
    private static final String KEY_COUNTRY_OPTIONS = "countryOps";
    private static final String KEY_POSITION_OPTIONS = "positionOps";

    private static final String KEY_ACTION_SUBMIT = "submit";
    private static final String KEY_ACTION_JUMP = "jump";
    private static final String KEY_JUMP_DEST_NODE = "destNode";
    private static final String KEY_DEST_NODE_ROUTE = "nodeRoute";

    private static final String KEY_SHOW_ERROR_SWITCH = "needShowValidationError";

    private static final String KEY_PROCESS_TYPE = "processType";
    //This oldFacilityCertifierRegisterDto is the original data, which is for comparison with each DTO modification before Submit(unchangeable)
    private static final String KEY_OLD_FAC_CER_REG_DTO = "oldFacilityCertifierRegisterDto";

    private static final String ERR_MSG_NULL_GROUP = "Node group must not be null!";
    private static final String ERR_MSG_NULL_NAME = "Name must not be null!";
    private static final String ERR_MSG_INVALID_ACTION = "Invalid action";

    private final FacCertifierRegisterClient facCertifierRegisterClient;
    private final FileRepoClient fileRepoClient;
    private final BsbFileClient bsbFileClient;

    @Autowired
    public RfcFacCertifierRegistrationDelegator(FacCertifierRegisterClient facCertifierRegisterClient, FileRepoClient fileRepoClient, BsbFileClient bsbFileClient) {
        this.facCertifierRegisterClient = facCertifierRegisterClient;
        this.fileRepoClient = fileRepoClient;
        this.bsbFileClient = bsbFileClient;
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
        request.getSession().removeAttribute(KEY_ROOT_NODE_GROUP);
        //charge if maskedAppId is null
        String maskedAppId = request.getParameter(KEY_EDIT_APP_ID);
        if(StringUtils.hasLength(maskedAppId)){
            if (log.isInfoEnabled()) {
                log.info("masked app ID: {}", LogUtil.escapeCrlf(maskedAppId));
            }
            boolean failRetrieveEditData = true;
            String appId = MaskUtil.unMaskValue(KEY_EDIT_APP_ID,maskedAppId);
            if(appId != null && !maskedAppId.equals(appId)){
                ResponseDto<FacilityCertifierRegisterDto> resultDto = facCertifierRegisterClient.getCertifierRegistrationAppDataByApprovalId(appId);
                if (resultDto.ok()) {
                    failRetrieveEditData = false;
                    //This oldFacilityCertifierRegisterDto is the original data, which is for comparison with each DTO modification before Submit(unchangeable)
                    FacilityCertifierRegisterDto oldFacilityCertifierRegisterDto = resultDto.getEntity();
                    ParamUtil.setSessionAttr(request, KEY_OLD_FAC_CER_REG_DTO, oldFacilityCertifierRegisterDto);

                    NodeGroup facRegRoot = oldFacilityCertifierRegisterDto.toFacilityCertRegister(KEY_ROOT_NODE_GROUP);
                    ParamUtil.setSessionAttr(request, KEY_ROOT_NODE_GROUP, facRegRoot);
                }
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
        // do nothing now, need to prepare company info in the future
    }

    public void prepare(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        String destNode = (String) ParamUtil.getSessionAttr(request, KEY_JUMP_DEST_NODE);
        ParamUtil.setRequestAttr(request, KEY_DEST_NODE_ROUTE, destNode);
    }

    public void preOrganisationInfo(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        NodeGroup facRegRoot = (NodeGroup) ParamUtil.getSessionAttr(request, KEY_ROOT_NODE_GROUP);
        String currentNodePath = NODE_NAME_ORGANISATION_INFO + facRegRoot.getPathSeparator() + NODE_NAME_ORG_PROFILE;
        SimpleNode orgProfileNode = (SimpleNode) facRegRoot.at(currentNodePath);
        OrganisationProfileDto orgProfileDto = (OrganisationProfileDto) orgProfileNode.getValue();
        Boolean needShowError = (Boolean) ParamUtil.getRequestAttr(request, KEY_SHOW_ERROR_SWITCH);
        if (needShowError == Boolean.TRUE) {
            ParamUtil.setRequestAttr(request, KEY_VALIDATION_ERRORS, orgProfileDto.retrieveValidationResult());
        }
        Nodes.needValidation(facRegRoot,NODE_NAME_ORG_PROFILE);
        ParamUtil.setRequestAttr(request, KEY_COUNTRY_OPTIONS, tmpCountryOps());
        ParamUtil.setRequestAttr(request, NODE_NAME_ORG_PROFILE, orgProfileDto);
    }

    public void preAdministrator(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        NodeGroup facRegRoot = (NodeGroup) ParamUtil.getSessionAttr(request, KEY_ROOT_NODE_GROUP);
        String currentNodePath = NODE_NAME_ORGANISATION_INFO + facRegRoot.getPathSeparator() + NODE_NAME_ORG_FAC_ADMINISTRATOR;
        SimpleNode administratorNode = (SimpleNode) facRegRoot.at(currentNodePath);
        AdministratorDto administratorDto = (AdministratorDto) administratorNode.getValue();
        Boolean needShowError = (Boolean) ParamUtil.getRequestAttr(request, KEY_SHOW_ERROR_SWITCH);
        if (needShowError == Boolean.TRUE) {
            ParamUtil.setRequestAttr(request, KEY_VALIDATION_ERRORS, administratorDto.retrieveValidationResult());
        }
        Nodes.needValidation(facRegRoot,NODE_NAME_ORG_FAC_ADMINISTRATOR);
        ParamUtil.setRequestAttr(request, KEY_NATIONALITY_OPTIONS, tmpNationalityOps());
        ParamUtil.setRequestAttr(request, NODE_NAME_ORG_FAC_ADMINISTRATOR, administratorDto);
    }

    public void preCertifyingTeam(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        NodeGroup facRegRoot = (NodeGroup) ParamUtil.getSessionAttr(request, KEY_ROOT_NODE_GROUP);
        String currentNodePath = NODE_NAME_ORGANISATION_INFO + facRegRoot.getPathSeparator() + NODE_NAME_ORG_CERTIFYING_TEAM;
        SimpleNode certifyingTeamNode = (SimpleNode) facRegRoot.at(currentNodePath);
        CertifyingTeamDto certifyingTeamDto = (CertifyingTeamDto) certifyingTeamNode.getValue();
        Boolean needShowError = (Boolean) ParamUtil.getRequestAttr(request, KEY_SHOW_ERROR_SWITCH);
        if (needShowError == Boolean.TRUE) {
            ParamUtil.setRequestAttr(request, KEY_VALIDATION_ERRORS, certifyingTeamDto.retrieveValidationResult());
        }
        Nodes.needValidation(facRegRoot,NODE_NAME_ORG_CERTIFYING_TEAM);
        ParamUtil.setRequestAttr(request, KEY_POSITION_OPTIONS, tmpPositionOps());
        ParamUtil.setRequestAttr(request, KEY_NATIONALITY_OPTIONS, tmpNationalityOps());
        ParamUtil.setRequestAttr(request, NODE_NAME_ORG_CERTIFYING_TEAM, certifyingTeamDto);
    }

    public void prepareDocuments(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        NodeGroup facRegRoot = (NodeGroup) ParamUtil.getSessionAttr(request, KEY_ROOT_NODE_GROUP);
        SimpleNode primaryDocNode = (SimpleNode) facRegRoot.at(NODE_NAME_FAC_PRIMARY_DOCUMENT);
       PrimaryDocDto primaryDocDto = (PrimaryDocDto) primaryDocNode.getValue();
        Boolean needShowError = (Boolean) ParamUtil.getRequestAttr(request, KEY_SHOW_ERROR_SWITCH);
        if (needShowError == Boolean.TRUE) {
            ParamUtil.setRequestAttr(request, KEY_VALIDATION_ERRORS, primaryDocDto.retrieveValidationResult());
        }
        Nodes.needValidation(facRegRoot,NODE_NAME_FAC_PRIMARY_DOCUMENT);

        ParamUtil.setRequestAttr(request, "docSettings", getFacRegDocSettings());

        Map<String, List<DocRecordInfo>> savedFiles = primaryDocDto.getExistDocTypeMap();
        Map<String, List<NewDocInfo>> newFiles = primaryDocDto.getNewDocTypeMap();
        ParamUtil.setRequestAttr(request, "savedFiles", savedFiles);
        ParamUtil.setRequestAttr(request, "newFiles", newFiles);
    }

    public void preparePreview(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        NodeGroup facRegRoot = (NodeGroup) ParamUtil.getSessionAttr(request, KEY_ROOT_NODE_GROUP);
        SimpleNode previewSubmitNode = (SimpleNode) facRegRoot.at(NODE_NAME_CER_PREVIEW_SUBMIT);
        PreviewSubmitDto previewSubmitDto = (PreviewSubmitDto) previewSubmitNode.getValue();
        Boolean needShowError = (Boolean) ParamUtil.getRequestAttr(request, KEY_SHOW_ERROR_SWITCH);
        if (needShowError == Boolean.TRUE) {
            ParamUtil.setRequestAttr(request, KEY_VALIDATION_ERRORS, previewSubmitDto.retrieveValidationResult());
        }
        Nodes.needValidation(facRegRoot,NODE_NAME_CER_PREVIEW_SUBMIT);
        ParamUtil.setRequestAttr(request, NODE_NAME_CER_PREVIEW_SUBMIT, previewSubmitDto);

        ParamUtil.setRequestAttr(request, NODE_NAME_ORG_PROFILE, ((SimpleNode) facRegRoot.at(NODE_NAME_ORGANISATION_INFO + facRegRoot.getPathSeparator() + NODE_NAME_ORG_PROFILE)).getValue());
        ParamUtil.setRequestAttr(request, NODE_NAME_ORG_CERTIFYING_TEAM, ((SimpleNode) facRegRoot.at(NODE_NAME_ORGANISATION_INFO + facRegRoot.getPathSeparator() + NODE_NAME_ORG_CERTIFYING_TEAM)).getValue());
        ParamUtil.setRequestAttr(request, NODE_NAME_ORG_FAC_ADMINISTRATOR, ((SimpleNode) facRegRoot.at(NODE_NAME_ORGANISATION_INFO + facRegRoot.getPathSeparator() + NODE_NAME_ORG_FAC_ADMINISTRATOR)).getValue());
        ParamUtil.setRequestAttr(request, "docSettings", getFacRegDocSettings());
        PrimaryDocDto primaryDocDto = (PrimaryDocDto) ((SimpleNode)facRegRoot.at(NODE_NAME_FAC_PRIMARY_DOCUMENT)).getValue();
        Map<String, List<DocRecordInfo>> savedFiles = primaryDocDto.getExistDocTypeMap();
        Map<String, List<NewDocInfo>> newFiles = primaryDocDto.getNewDocTypeMap();
        ParamUtil.setRequestAttr(request, "savedFiles", savedFiles);
        ParamUtil.setRequestAttr(request, "newFiles", newFiles);
    }

    public void doCompInfo(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        NodeGroup facRegRoot = (NodeGroup) ParamUtil.getSessionAttr(request, KEY_ROOT_NODE_GROUP);
        Node compInfoNode = facRegRoot.getNode(NODE_NAME_COMPANY_INFO);
        String actionType = ParamUtil.getString(request, KEY_ACTION_TYPE);
        if (KEY_ACTION_JUMP.equals(actionType)) {
            jumpHandler(request, facRegRoot, NODE_NAME_COMPANY_INFO, compInfoNode);
        } else {
            throw new IaisRuntimeException(ERR_MSG_INVALID_ACTION);
        }
        ParamUtil.setSessionAttr(request, KEY_ROOT_NODE_GROUP, facRegRoot);
    }

    public void doAdministrator(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        NodeGroup facCertifierRegRoot = (NodeGroup) ParamUtil.getSessionAttr(request, KEY_ROOT_NODE_GROUP);
        String currentNodePath = NODE_NAME_ORGANISATION_INFO + facCertifierRegRoot.getPathSeparator() + NODE_NAME_ORG_FAC_ADMINISTRATOR;
        SimpleNode administratorNode = (SimpleNode) facCertifierRegRoot.at(currentNodePath);
        AdministratorDto administratorDto = (AdministratorDto) administratorNode.getValue();
        administratorDto.reqObjMapping(request);
        String actionType = ParamUtil.getString(request, KEY_ACTION_TYPE);
        if (KEY_ACTION_JUMP.equals(actionType)) {
            jumpHandler(request, facCertifierRegRoot, currentNodePath, administratorNode);
        } else {
            throw new IaisRuntimeException(ERR_MSG_INVALID_ACTION);
        }
        ParamUtil.setSessionAttr(request, KEY_ROOT_NODE_GROUP, facCertifierRegRoot);
    }

    public void doCertifyingTeam(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        NodeGroup facRegRoot = (NodeGroup) ParamUtil.getSessionAttr(request, KEY_ROOT_NODE_GROUP);
        String currentNodePath = NODE_NAME_ORGANISATION_INFO + facRegRoot.getPathSeparator() + NODE_NAME_ORG_CERTIFYING_TEAM;
        SimpleNode certifyingTeamNode = (SimpleNode) facRegRoot.at(currentNodePath);
        CertifyingTeamDto certifyingTeamDto = (CertifyingTeamDto) certifyingTeamNode.getValue();
        certifyingTeamDto.reqObjMapping(request);
        String actionType = ParamUtil.getString(request, KEY_ACTION_TYPE);
        if (KEY_ACTION_JUMP.equals(actionType)) {
            jumpHandler(request, facRegRoot, currentNodePath, certifyingTeamNode);
        } else {
            throw new IaisRuntimeException(ERR_MSG_INVALID_ACTION);
        }
        ParamUtil.setSessionAttr(request, KEY_ROOT_NODE_GROUP, facRegRoot);
    }

    public void doOrganisationProfile(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        NodeGroup facRegRoot = (NodeGroup) ParamUtil.getSessionAttr(request, KEY_ROOT_NODE_GROUP);
        String currentNodePath = NODE_NAME_ORGANISATION_INFO + facRegRoot.getPathSeparator() + NODE_NAME_ORG_PROFILE;
        SimpleNode orgProfileNode = (SimpleNode) facRegRoot.at(currentNodePath);
        OrganisationProfileDto orgProfile = (OrganisationProfileDto) orgProfileNode.getValue();
        orgProfile.reqObjMapping(request);
        String actionType = ParamUtil.getString(request, KEY_ACTION_TYPE);
        if (KEY_ACTION_JUMP.equals(actionType)) {
            jumpHandler(request, facRegRoot, currentNodePath, orgProfileNode);
        }else {
            throw new IaisRuntimeException(ERR_MSG_INVALID_ACTION);
        }
        ParamUtil.setSessionAttr(request, KEY_ROOT_NODE_GROUP, facRegRoot);
    }

    public void doDocument(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        NodeGroup facRegRoot = (NodeGroup) ParamUtil.getSessionAttr(request, KEY_ROOT_NODE_GROUP);
        String currentNodePath = NODE_NAME_FAC_PRIMARY_DOCUMENT;
        SimpleNode primaryDocNode = (SimpleNode) facRegRoot.at(currentNodePath);
        PrimaryDocDto primaryDocDto = (PrimaryDocDto) primaryDocNode.getValue();
        primaryDocDto.reqObjMapping(request);

        String actionType = ParamUtil.getString(request, KEY_ACTION_TYPE);
        if (KEY_ACTION_JUMP.equals(actionType)) {
            jumpHandler(request, facRegRoot, currentNodePath, primaryDocNode);
        } else {
            throw new IaisRuntimeException(ERR_MSG_INVALID_ACTION);
        }
        ParamUtil.setSessionAttr(request, KEY_ROOT_NODE_GROUP, facRegRoot);
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

                    FacilityCertifierRegisterDto finalAllDataDto = FacilityCertifierRegisterDto.from(facRegRoot);
                    //rfc compare to decision flowType
                    FacilityCertifierRegisterDto oldFacilityCertifierRegisterDto = (FacilityCertifierRegisterDto)ParamUtil.getSessionAttr(request,KEY_OLD_FAC_CER_REG_DTO);
                    DecisionFlowType flowType = new DecisionFlowTypeImpl();
                    RfcFlowType rfcFlowType = flowType.facCerRegFlowType(compareTwoDto(oldFacilityCertifierRegisterDto,finalAllDataDto));
                    ParamUtil.setRequestAttr(request, "rfcFlowType", rfcFlowType);
                    if (rfcFlowType.equals(RfcFlowType.AMENDMENT) || rfcFlowType.equals(RfcFlowType.NOTIFICATION)){
                        //upload document
                        SimpleNode primaryDocNode = (SimpleNode) facRegRoot.at(NODE_NAME_FAC_PRIMARY_DOCUMENT);
                        PrimaryDocDto primaryDocDto = (PrimaryDocDto) primaryDocNode.getValue();
                        List<NewFileSyncDto> newFilesToSync = null;
                        if (!primaryDocDto.getNewDocMap().isEmpty()) {
                            MultipartFile[] files = primaryDocDto.getNewDocMap().values().stream().map(NewDocInfo::getMultipartFile).toArray(MultipartFile[]::new);
                            List<String> repoIds = fileRepoClient.saveFiles(files).getEntity();
                            newFilesToSync = primaryDocDto.newFileSaved(repoIds);
                        }

                        // save data
                        AuditTrailDto auditTrailDto = (AuditTrailDto) ParamUtil.getSessionAttr(request, AuditTrailConsts.SESSION_ATTR_PARAM_NAME);
                        finalAllDataDto.setAuditTrailDto(auditTrailDto);
                        finalAllDataDto.setAppStatus("BSBAPST001");
                        ResponseDto<String> responseDto = facCertifierRegisterClient.saveAmendmentFacCertifier(finalAllDataDto);
                        log.info("save new facility response: {}", responseDto);

                        try {
                            // sync files to BE file-repo (save new added files, delete useless files)
                            if ((newFilesToSync != null && !newFilesToSync.isEmpty()) || !primaryDocDto.getToBeDeletedRepoIds().isEmpty()) {
                                /* Ignore the failure of sync files currently.
                                 * We should add a mechanism to retry synchronization of files in the future */
                                FileRepoSyncDto syncDto = new FileRepoSyncDto();
                                syncDto.setNewFiles(newFilesToSync);
                                syncDto.setToDeleteIds(new ArrayList<>(primaryDocDto.getToBeDeletedRepoIds()));
                                bsbFileClient.saveFiles(syncDto);
                            }

                            // delete docs in FE file-repo
                            /* Ignore the failure when try to delete FE files because this is not a big issue.
                             * The not deleted file won't be retrieved, so it's just a waste of disk space */
                            for (String id: primaryDocDto.getToBeDeletedRepoIds()) {
                                FileRepoDto fileRepoDto = new FileRepoDto();
                                fileRepoDto.setId(id);
                                fileRepoClient.removeFileById(fileRepoDto);
                            }
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
                jumpHandler(request, facRegRoot, currentNodePath, previewSubmitNode);
            }
        } else {
            throw new IaisRuntimeException(ERR_MSG_INVALID_ACTION);
        }
        ParamUtil.setSessionAttr(request, KEY_ROOT_NODE_GROUP, facRegRoot);
    }

    public void controlSwitch(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        String actionType = (String) ParamUtil.getRequestAttr(request, KEY_ACTION_TYPE);
        if (!StringUtils.hasLength(actionType)) {
            actionType = ParamUtil.getString(request, KEY_ACTION_TYPE);
        }
        ParamUtil.setRequestAttr(request, KEY_INDEED_ACTION_TYPE, actionType);
    }



    /**
     * common actions when we do 'jump'
     * decide the routing logic
     * will set a dest node in the request attribute;
     * will set a floag if we need to show the error messages.
     * @param facRegRoot root data structure of this flow
     */
    public void jumpHandler(HttpServletRequest request, NodeGroup facRegRoot, String currentPath, Node currentNode) {
        String actionValue = ParamUtil.getString(request, KEY_ACTION_VALUE);
        Assert.hasText(actionValue, "Invalid action value");
        boolean currentLetGo = true;  // if false, we have to stay current node
        if (KEY_NAV_NEXT.equals(actionValue) || KEY_NAV_SAVE_DRAFT.equals(actionValue)) {  // if click next, we need to validate current node anyway
            currentLetGo = currentNode.doValidation();
            if (currentLetGo) {
                Nodes.passValidation(facRegRoot, currentPath);
            }
        }
        if (currentLetGo) {
            if(!KEY_NAV_SAVE_DRAFT.equals(actionValue)){
                String destNode = computeDestNodePath(facRegRoot, actionValue);
                String checkedDestNode = Nodes.jump(facRegRoot, destNode);
                if (!checkedDestNode.equals(destNode)) {
                    ParamUtil.setRequestAttr(request, KEY_SHOW_ERROR_SWITCH, Boolean.TRUE);
                }
                ParamUtil.setSessionAttr(request, KEY_JUMP_DEST_NODE, checkedDestNode);
            }else{
                FacilityCertifierRegisterDto finalAllDataDto = FacilityCertifierRegisterDto.from(facRegRoot);
                finalAllDataDto.setAppStatus("BSBAPST011");
                ResponseDto<String> responseDto = facCertifierRegisterClient.saveNewRegisteredFacCertifier(finalAllDataDto);
                log.info("save as draft response: {}", responseDto);
                ParamUtil.setSessionAttr(request, KEY_JUMP_DEST_NODE, currentPath);
            }
        } else {
            ParamUtil.setRequestAttr(request, KEY_SHOW_ERROR_SWITCH, Boolean.TRUE);
            ParamUtil.setSessionAttr(request, KEY_JUMP_DEST_NODE, currentPath);
        }
    }

    /**
     * common actions when we do 'saveDraft'
     * decide the routing logic
     * will set a dest node in the request attribute;
     * will set a floag if we need to show the error messages.
     * @param facRegRoot root data structure of this flow
     */
    public void saveDraftHandler(HttpServletRequest request, NodeGroup facRegRoot, String currentPath, Node currentNode) {
        boolean currentLetGo = true;  // if false, we have to stay current node// if click next, we need to validate current node anyway
            currentLetGo = currentNode.doValidation();
            if (currentLetGo) {
                Nodes.passValidation(facRegRoot, currentPath);
            }
           if (currentLetGo) {
            FacilityCertifierRegisterDto finalAllDataDto = FacilityCertifierRegisterDto.from(facRegRoot);
            ResponseDto<String> responseDto = facCertifierRegisterClient.saveNewRegisteredFacCertifier(finalAllDataDto);
            log.info("save as draft response: {}", responseDto);
           }else{
            ParamUtil.setRequestAttr(request, KEY_SHOW_ERROR_SWITCH, Boolean.TRUE);
           }
        ParamUtil.setSessionAttr(request, KEY_JUMP_DEST_NODE, currentPath);
    }

    public String computeDestNodePath(NodeGroup facRegRoot, String actionValue) {
        String destNode;
        switch (actionValue) {
            case KEY_NAV_NEXT:
                destNode = Nodes.getNextNodePath(facRegRoot);
                break;
            case KEY_NAV_BACK:
                destNode = Nodes.getPreviousNodePath(facRegRoot);
                break;
            default:
                Assert.hasText(actionValue, "Action value should be a node path");
                destNode = Nodes.expandNode(facRegRoot, actionValue);
                break;
        }
        return destNode;
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
     * @param group the node group of the facRegRoot
     * @return the class name
     */
    public static String computeTabClassname(NodeGroup group, String name) {
        Assert.notNull(group, ERR_MSG_NULL_GROUP);
        Assert.notNull(name, ERR_MSG_NULL_NAME);
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


    /*
         * @param group the node group of the facRegRoot
     */
    public static boolean ifNodeSelected(NodeGroup group, String name) {
        Assert.notNull(group, ERR_MSG_NULL_GROUP);
        Assert.notNull(name, ERR_MSG_NULL_NAME);
        return name.equals(group.getActiveNodeKey());
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
     *
     * @param group the node group contains the sub steps
     */
    public static String computeStepClassname(NodeGroup group, String name) {
        Assert.notNull(group, ERR_MSG_NULL_GROUP);
        Assert.notNull(name, ERR_MSG_NULL_NAME);
        return group.getNode(name).isValidated() || name.equals(group.getActiveNodeKey()) ? "active" : "disabled";
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

    /* Will be removed in future, will get this from master code */
    private static List<SelectOption> tmpNationalityOps() {
        List<MasterCodeView> views = MasterCodeUtil.retrieveByCategory(MasterCodeUtil.CATE_ID_NATIONALITY);
        List<SelectOption> ops = new ArrayList<>(views.size());
        if(!CollectionUtils.isEmpty(views)){
            for (MasterCodeView view : views) {
                ops.add(new SelectOption(view.getCode(), view.getCodeValue()));
            }
        }
        return ops;
    }

    private static List<SelectOption> tmpCountryOps() {
        return Arrays.asList(new SelectOption(null, TEXT_VALUE_PLEASE_SELECT),new SelectOption(TEXT_VALUE_SINGAPORE, TEXT_VALUE_SINGAPORE), new SelectOption(TEXT_VALUE_CHINA, TEXT_VALUE_CHINA),new SelectOption(TEXT_VALUE_MALAYSIA,TEXT_VALUE_MALAYSIA),new SelectOption("USA","USA"),new SelectOption("UK","UK"));
    }

    private static List<SelectOption> tmpPositionOps() {
        return Arrays.asList(new SelectOption(null, TEXT_VALUE_PLEASE_SELECT),new SelectOption("Biosafety Certifier", "Biosafety Certifier"), new SelectOption("Engineering Certifier", "Engineering Certifier"),new SelectOption("Assistant Biosafety Certifier","Assistant Biosafety Certifier"),new SelectOption("Assistant Engineering Certifier","Assistant Engineering Certifier"));
    }


    /* Will be removed in future, will get this from config mechanism */
    private List<DocSetting> getFacRegDocSettings () {
        List<DocSetting> docSettings = new ArrayList<>();
        docSettings.add(new DocSetting(DocConstants.DOC_TYPE_COMPANY_INFORMATION, "Company Information", true));
        docSettings.add(new DocSetting(DocConstants.DOC_TYPE_SOP_FOR_CERTIFICATION, "SOP for Certification", true));
        docSettings.add(new DocSetting(DocConstants.DOC_TYPE_OTHERS, "Others", false));
        docSettings.add(new DocSetting(DocConstants.DOC_TYPE_TESTIMONIALS, "Testimonials", true));
        docSettings.add(new DocSetting(DocConstants.DOC_TYPE_CURRICULUM_VITAE, "Curriculum Vitae", true));
        return docSettings;
    }

    //rfc compare
    public List<DiffContent> compareTwoDto(FacilityCertifierRegisterDto oldFacilityCertifierRegisterDto, FacilityCertifierRegisterDto newFacilityCertifierRegisterDto){
        List<DiffContent> diffContentList = new ArrayList<>();
        CompareTwoObject.diff(oldFacilityCertifierRegisterDto.getProfileDto(), newFacilityCertifierRegisterDto.getProfileDto(), diffContentList);
        CompareTwoObject.diff(oldFacilityCertifierRegisterDto.getCertifyingTeamDto(), newFacilityCertifierRegisterDto.getCertifyingTeamDto(), diffContentList, CertifyingTeamDto.CertifierTeamMember.class);
        CompareTwoObject.diff(oldFacilityCertifierRegisterDto.getAdministratorDto(), newFacilityCertifierRegisterDto.getAdministratorDto(), diffContentList, AdministratorDto.FacilityAdministratorInfo.class);
        //docRecordInfos don't process
        return diffContentList;
    }
}

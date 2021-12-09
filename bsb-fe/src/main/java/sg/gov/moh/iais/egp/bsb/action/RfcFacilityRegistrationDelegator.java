package sg.gov.moh.iais.egp.bsb.action;


import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.AuditTrailConsts;
import com.ecquaria.cloud.moh.iais.common.dto.AuditTrailDto;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.filerepo.FileRepoDto;
import com.ecquaria.cloud.moh.iais.common.exception.IaisRuntimeException;
import com.ecquaria.cloud.moh.iais.common.utils.MaskUtil;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.MasterCodeUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import sg.gov.moh.iais.egp.bsb.client.BsbFileClient;
import sg.gov.moh.iais.egp.bsb.client.FacilityRegisterClient;
import sg.gov.moh.iais.egp.bsb.client.FileRepoClient;
import sg.gov.moh.iais.egp.bsb.common.node.Node;
import sg.gov.moh.iais.egp.bsb.common.node.NodeGroup;
import sg.gov.moh.iais.egp.bsb.common.node.Nodes;
import sg.gov.moh.iais.egp.bsb.common.node.simple.SimpleNode;
import sg.gov.moh.iais.egp.bsb.common.rfc.DecisionFlowType;
import sg.gov.moh.iais.egp.bsb.common.rfc.DecisionFlowTypeImpl;
import sg.gov.moh.iais.egp.bsb.dto.ResponseDto;
import sg.gov.moh.iais.egp.bsb.dto.file.DocRecordInfo;
import sg.gov.moh.iais.egp.bsb.dto.file.FileRepoSyncDto;
import sg.gov.moh.iais.egp.bsb.dto.file.NewDocInfo;
import sg.gov.moh.iais.egp.bsb.dto.file.NewFileSyncDto;
import sg.gov.moh.iais.egp.bsb.dto.register.facility.*;
import sg.gov.moh.iais.egp.bsb.constant.RfcFlowType;
import sg.gov.moh.iais.egp.bsb.service.FacilityRegistrationService;
import sg.gov.moh.iais.egp.bsb.util.LogUtil;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static sg.gov.moh.iais.egp.bsb.constant.FacRegisterConstants.*;


@Slf4j
@Delegator("rfcFacilityRegisterDelegator")
public class RfcFacilityRegistrationDelegator {
    private static final String MODULE_NAME = "Rfc Facility Registration";

    private final FacilityRegisterClient facRegClient;
    private final FileRepoClient fileRepoClient;
    private final BsbFileClient bsbFileClient;
    private final FacilityRegistrationService facilityRegistrationService;

    @Autowired
    public RfcFacilityRegistrationDelegator(FacilityRegisterClient facRegClient, FileRepoClient fileRepoClient,
                                            BsbFileClient bsbFileClient, FacilityRegistrationService facilityRegistrationService) {
        this.facRegClient = facRegClient;
        this.fileRepoClient = fileRepoClient;
        this.bsbFileClient = bsbFileClient;
        this.facilityRegistrationService = facilityRegistrationService;
    }

    public void start(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        request.getSession().removeAttribute(KEY_PROCESS_TYPE);
        request.getSession().removeAttribute(KEY_ROOT_NODE_GROUP);
        request.getSession().removeAttribute(KEY_OLD_FACILITY_REGISTER_DTO);
        AuditTrailHelper.auditFunction(MODULE_NAME, MODULE_NAME);
    }

    public void init(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        // check if we are doing editing
        String maskedAppId = request.getParameter(KEY_EDIT_APP_ID);
        if (StringUtils.hasLength(maskedAppId)) {
            if (log.isInfoEnabled()) {
                log.info("masked approval ID: {}", LogUtil.escapeCrlf(maskedAppId));
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
        // do nothing now, need to prepare company info in the future
    }

    public void handleCompInfo(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        NodeGroup facRegRoot = (NodeGroup) ParamUtil.getSessionAttr(request, KEY_ROOT_NODE_GROUP);
        Node compInfoNode = facRegRoot.getNode(NODE_NAME_COMPANY_INFO);
        String actionType = ParamUtil.getString(request, KEY_ACTION_TYPE);
        if (KEY_ACTION_JUMP.equals(actionType)) {
            facilityRegistrationService.jumpHandler(request, facRegRoot, NODE_NAME_COMPANY_INFO, compInfoNode);
        } else {
            throw new IaisRuntimeException(ERR_MSG_INVALID_ACTION);
        }
        ParamUtil.setSessionAttr(request, KEY_ROOT_NODE_GROUP, facRegRoot);
    }

    public void preServiceSelection(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        NodeGroup facRegRoot = (NodeGroup) ParamUtil.getSessionAttr(request, KEY_ROOT_NODE_GROUP);
        SimpleNode facSelectionNode = (SimpleNode) facRegRoot.getNode(NODE_NAME_FAC_SELECTION);
        FacilitySelectionDto selectionDto = (FacilitySelectionDto) facSelectionNode.getValue();
        Boolean needShowError = (Boolean) ParamUtil.getRequestAttr(request, KEY_SHOW_ERROR_SWITCH);
        if (needShowError == Boolean.TRUE) {
            ParamUtil.setRequestAttr(request, KEY_VALIDATION_ERRORS, selectionDto.retrieveValidationResult());
        }
        Nodes.needValidation(facRegRoot, NODE_NAME_FAC_SELECTION);
        ParamUtil.setRequestAttr(request, NODE_NAME_FAC_SELECTION, selectionDto);
    }

    public void handleServiceSelection(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        NodeGroup facRegRoot = (NodeGroup) ParamUtil.getSessionAttr(request, KEY_ROOT_NODE_GROUP);
        SimpleNode facSelectionNode = (SimpleNode) facRegRoot.getNode(NODE_NAME_FAC_SELECTION);
        FacilitySelectionDto selectionDto = (FacilitySelectionDto) facSelectionNode.getValue();
        selectionDto.reqObjMapping(request);

        String actionType = ParamUtil.getString(request, KEY_ACTION_TYPE);
        if (KEY_ACTION_JUMP.equals(actionType)) {
            facilityRegistrationService.jumpHandler(request, facRegRoot, NODE_NAME_FAC_SELECTION, facSelectionNode);
        } else {
            throw new IaisRuntimeException(ERR_MSG_INVALID_ACTION);
        }

        if (facSelectionNode.isValidated()) {
            NodeGroup batGroup = (NodeGroup) facRegRoot.getNode(NODE_NAME_FAC_BAT_INFO);
            FacilityRegistrationService.changeBatNodeGroup(batGroup, selectionDto);
        }
        ParamUtil.setSessionAttr(request, KEY_ROOT_NODE_GROUP, facRegRoot);
    }

    public void preFacProfile(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        NodeGroup facRegRoot = (NodeGroup) ParamUtil.getSessionAttr(request, KEY_ROOT_NODE_GROUP);
        String currentNodePath = NODE_NAME_FAC_INFO + facRegRoot.getPathSeparator() + NODE_NAME_FAC_PROFILE;
        SimpleNode facProfileNode = (SimpleNode) facRegRoot.at(currentNodePath);
        FacilityProfileDto facProfileDto = (FacilityProfileDto) facProfileNode.getValue();
        Boolean needShowError = (Boolean) ParamUtil.getRequestAttr(request, KEY_SHOW_ERROR_SWITCH);
        if (needShowError == Boolean.TRUE) {
            ParamUtil.setRequestAttr(request, KEY_VALIDATION_ERRORS, facProfileDto.retrieveValidationResult());
        }
        Nodes.needValidation(facRegRoot, currentNodePath);
        ParamUtil.setRequestAttr(request, NODE_NAME_FAC_PROFILE, facProfileDto);
    }

    public void handleFacProfile(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        NodeGroup facRegRoot = (NodeGroup) ParamUtil.getSessionAttr(request, KEY_ROOT_NODE_GROUP);
        String currentNodePath = NODE_NAME_FAC_INFO + facRegRoot.getPathSeparator() + NODE_NAME_FAC_PROFILE;
        SimpleNode facProfileNode = (SimpleNode) facRegRoot.at(currentNodePath);
        FacilityProfileDto facProfileDto = (FacilityProfileDto) facProfileNode.getValue();
        facProfileDto.reqObjMapping(request);
        SimpleNode facAuthNode = (SimpleNode) facRegRoot.at(NODE_NAME_FAC_INFO + facRegRoot.getPathSeparator() + NODE_NAME_FAC_AUTH);
        FacilityAuthoriserDto facAuthDto = (FacilityAuthoriserDto) facAuthNode.getValue();
        facAuthDto.setIsProtectedPlace(facProfileDto.getIsFacilityProtected());

        String actionType = ParamUtil.getString(request, KEY_ACTION_TYPE);
        if (KEY_ACTION_JUMP.equals(actionType)) {
            facilityRegistrationService.jumpHandler(request, facRegRoot, currentNodePath, facProfileNode);
        } else {
            throw new IaisRuntimeException(ERR_MSG_INVALID_ACTION);
        }
        ParamUtil.setSessionAttr(request, KEY_ROOT_NODE_GROUP, facRegRoot);
    }

    public void preFacOperator(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        NodeGroup facRegRoot = (NodeGroup) ParamUtil.getSessionAttr(request, KEY_ROOT_NODE_GROUP);
        String currentNodePath = NODE_NAME_FAC_INFO + facRegRoot.getPathSeparator() + NODE_NAME_FAC_OPERATOR;
        SimpleNode facOpNode = (SimpleNode) facRegRoot.at(currentNodePath);
        FacilityOperatorDto facOpDto = (FacilityOperatorDto) facOpNode.getValue();
        Boolean needShowError = (Boolean) ParamUtil.getRequestAttr(request, KEY_SHOW_ERROR_SWITCH);
        if (needShowError == Boolean.TRUE) {
            ParamUtil.setRequestAttr(request, KEY_VALIDATION_ERRORS, facOpDto.retrieveValidationResult());
        }
        Nodes.needValidation(facRegRoot, currentNodePath);
        ParamUtil.setRequestAttr(request, NODE_NAME_FAC_OPERATOR, facOpDto);

        ParamUtil.setRequestAttr(request, KEY_NATIONALITY_OPTIONS, FacilityRegistrationService.tmpNationalityOps());
    }

    public void handleFacOperator(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        NodeGroup facRegRoot = (NodeGroup) ParamUtil.getSessionAttr(request, KEY_ROOT_NODE_GROUP);
        String currentNodePath = NODE_NAME_FAC_INFO + facRegRoot.getPathSeparator() + NODE_NAME_FAC_OPERATOR;
        SimpleNode facOpNode = (SimpleNode) facRegRoot.at(currentNodePath);
        FacilityOperatorDto facOpDto = (FacilityOperatorDto) facOpNode.getValue();
        facOpDto.reqObjMapping(request);

        String actionType = ParamUtil.getString(request, KEY_ACTION_TYPE);
        if (KEY_ACTION_JUMP.equals(actionType)) {
            facilityRegistrationService.jumpHandler(request, facRegRoot, currentNodePath, facOpNode);
        } else {
            throw new IaisRuntimeException(ERR_MSG_INVALID_ACTION);
        }
        ParamUtil.setSessionAttr(request, KEY_ROOT_NODE_GROUP, facRegRoot);
    }

    public void preFacInfoAuthoriser(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        NodeGroup facRegRoot = (NodeGroup) ParamUtil.getSessionAttr(request, KEY_ROOT_NODE_GROUP);
        String currentNodePath = NODE_NAME_FAC_INFO + facRegRoot.getPathSeparator() + NODE_NAME_FAC_AUTH;
        SimpleNode facAuthNode = (SimpleNode) facRegRoot.at(currentNodePath);
        FacilityAuthoriserDto facAuthDto = (FacilityAuthoriserDto) facAuthNode.getValue();
        Boolean needShowError = (Boolean) ParamUtil.getRequestAttr(request, KEY_SHOW_ERROR_SWITCH);
        if (needShowError == Boolean.TRUE) {
            ParamUtil.setRequestAttr(request, KEY_VALIDATION_ERRORS, facAuthDto.retrieveValidationResult());
        }
        Nodes.needValidation(facRegRoot, currentNodePath);
        ParamUtil.setRequestAttr(request, NODE_NAME_FAC_AUTH, facAuthDto);

        ParamUtil.setRequestAttr(request, KEY_NATIONALITY_OPTIONS, FacilityRegistrationService.tmpNationalityOps());
    }

    public void handleFacInfoAuthoriser(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        NodeGroup facRegRoot = (NodeGroup) ParamUtil.getSessionAttr(request, KEY_ROOT_NODE_GROUP);
        String currentNodePath = NODE_NAME_FAC_INFO + facRegRoot.getPathSeparator() + NODE_NAME_FAC_AUTH;
        SimpleNode facAuthNode = (SimpleNode) facRegRoot.at(currentNodePath);
        FacilityAuthoriserDto facAuthDto = (FacilityAuthoriserDto) facAuthNode.getValue();
        facAuthDto.reqObjMapping(request);

        String actionType = ParamUtil.getString(request, KEY_ACTION_TYPE);
        if (KEY_ACTION_JUMP.equals(actionType)) {
            facilityRegistrationService.jumpHandler(request, facRegRoot, currentNodePath, facAuthNode);
        } else {
            throw new IaisRuntimeException(ERR_MSG_INVALID_ACTION);
        }
        ParamUtil.setSessionAttr(request, KEY_ROOT_NODE_GROUP, facRegRoot);
    }

    public void preFacAdmin(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        NodeGroup facRegRoot = (NodeGroup) ParamUtil.getSessionAttr(request, KEY_ROOT_NODE_GROUP);
        String currentNodePath = NODE_NAME_FAC_INFO + facRegRoot.getPathSeparator() + NODE_NAME_FAC_ADMIN;
        SimpleNode facAdminNode = (SimpleNode) facRegRoot.at(currentNodePath);
        FacilityAdministratorDto facAdminDto = (FacilityAdministratorDto) facAdminNode.getValue();
        Boolean needShowError = (Boolean) ParamUtil.getRequestAttr(request, KEY_SHOW_ERROR_SWITCH);
        if (needShowError == Boolean.TRUE) {
            ParamUtil.setRequestAttr(request, KEY_VALIDATION_ERRORS, facAdminDto.retrieveValidationResult());
        }
        Nodes.needValidation(facRegRoot, currentNodePath);
        ParamUtil.setRequestAttr(request, NODE_NAME_FAC_ADMIN, facAdminDto);

        ParamUtil.setRequestAttr(request, KEY_NATIONALITY_OPTIONS, FacilityRegistrationService.tmpNationalityOps());
    }

    public void handleFacAdmin(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        NodeGroup facRegRoot = (NodeGroup) ParamUtil.getSessionAttr(request, KEY_ROOT_NODE_GROUP);
        String currentNodePath = NODE_NAME_FAC_INFO + facRegRoot.getPathSeparator() + NODE_NAME_FAC_ADMIN;
        SimpleNode facAdminNode = (SimpleNode) facRegRoot.at(currentNodePath);
        FacilityAdministratorDto facAdminDto = (FacilityAdministratorDto) facAdminNode.getValue();
        facAdminDto.reqObjMapping(request);

        String actionType = ParamUtil.getString(request, KEY_ACTION_TYPE);
        if (KEY_ACTION_JUMP.equals(actionType)) {
            facilityRegistrationService.jumpHandler(request, facRegRoot, currentNodePath, facAdminNode);
        } else {
            throw new IaisRuntimeException(ERR_MSG_INVALID_ACTION);
        }
        ParamUtil.setSessionAttr(request, KEY_ROOT_NODE_GROUP, facRegRoot);
    }

    public void preFacOfficer(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        NodeGroup facRegRoot = (NodeGroup) ParamUtil.getSessionAttr(request, KEY_ROOT_NODE_GROUP);
        String currentNodePath = NODE_NAME_FAC_INFO + facRegRoot.getPathSeparator() + NODE_NAME_FAC_OFFICER;
        SimpleNode facOfficerNode = (SimpleNode) facRegRoot.at(currentNodePath);
        FacilityOfficerDto facOfficerDto = (FacilityOfficerDto) facOfficerNode.getValue();
        Boolean needShowError = (Boolean) ParamUtil.getRequestAttr(request, KEY_SHOW_ERROR_SWITCH);
        if (needShowError == Boolean.TRUE) {
            ParamUtil.setRequestAttr(request, KEY_VALIDATION_ERRORS, facOfficerDto.retrieveValidationResult());
        }
        Nodes.needValidation(facRegRoot, currentNodePath);
        ParamUtil.setRequestAttr(request, NODE_NAME_FAC_OFFICER, facOfficerDto);

        ParamUtil.setRequestAttr(request, KEY_NATIONALITY_OPTIONS, FacilityRegistrationService.tmpNationalityOps());
    }

    public void handleFacOfficer(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        NodeGroup facRegRoot = (NodeGroup) ParamUtil.getSessionAttr(request, KEY_ROOT_NODE_GROUP);
        String currentNodePath = NODE_NAME_FAC_INFO + facRegRoot.getPathSeparator() + NODE_NAME_FAC_OFFICER;
        SimpleNode facOfficerNode = (SimpleNode) facRegRoot.at(currentNodePath);
        FacilityOfficerDto facOfficerDto = (FacilityOfficerDto) facOfficerNode.getValue();
        facOfficerDto.reqObjMapping(request);

        String actionType = ParamUtil.getString(request, KEY_ACTION_TYPE);
        if (KEY_ACTION_JUMP.equals(actionType)) {
            facilityRegistrationService.jumpHandler(request, facRegRoot, currentNodePath, facOfficerNode);
        } else {
            throw new IaisRuntimeException(ERR_MSG_INVALID_ACTION);
        }
        ParamUtil.setSessionAttr(request, KEY_ROOT_NODE_GROUP, facRegRoot);
    }

    public void preFacInfoCommittee(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        NodeGroup facRegRoot = (NodeGroup) ParamUtil.getSessionAttr(request, KEY_ROOT_NODE_GROUP);
        String currentNodePath = NODE_NAME_FAC_INFO + facRegRoot.getPathSeparator() + NODE_NAME_FAC_COMMITTEE;
        SimpleNode facCommitteeNode = (SimpleNode) facRegRoot.at(currentNodePath);
        FacilityCommitteeDto facCommitteeDto = (FacilityCommitteeDto) facCommitteeNode.getValue();
        Boolean needShowError = (Boolean) ParamUtil.getRequestAttr(request, KEY_SHOW_ERROR_SWITCH);
        if (needShowError == Boolean.TRUE) {
            ParamUtil.setRequestAttr(request, KEY_VALIDATION_ERRORS, facCommitteeDto.retrieveValidationResult());
        }
        Nodes.needValidation(facRegRoot, currentNodePath);
        ParamUtil.setRequestAttr(request, NODE_NAME_FAC_COMMITTEE, facCommitteeDto);

        ParamUtil.setRequestAttr(request, KEY_NATIONALITY_OPTIONS, FacilityRegistrationService.tmpNationalityOps());
        ParamUtil.setRequestAttr(request, "personnelRoleOps", FacilityRegistrationService.tmpPersonnelRoleOps());
    }

    public void handleFacInfoCommittee(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        NodeGroup facRegRoot = (NodeGroup) ParamUtil.getSessionAttr(request, KEY_ROOT_NODE_GROUP);
        String currentNodePath = NODE_NAME_FAC_INFO + facRegRoot.getPathSeparator() + NODE_NAME_FAC_COMMITTEE;
        SimpleNode facCommitteeNode = (SimpleNode) facRegRoot.at(currentNodePath);
        FacilityCommitteeDto facCommitteeDto = (FacilityCommitteeDto) facCommitteeNode.getValue();
        facCommitteeDto.reqObjMapping(request);

        String actionType = ParamUtil.getString(request, KEY_ACTION_TYPE);
        if (KEY_ACTION_JUMP.equals(actionType)) {
            facilityRegistrationService.jumpHandler(request, facRegRoot, currentNodePath, facCommitteeNode);
        } else {
            throw new IaisRuntimeException(ERR_MSG_INVALID_ACTION);
        }
        ParamUtil.setSessionAttr(request, KEY_ROOT_NODE_GROUP, facRegRoot);
    }

    public void preBAToxin(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        NodeGroup facRegRoot = (NodeGroup) ParamUtil.getSessionAttr(request, KEY_ROOT_NODE_GROUP);
        String currentNodePath = (String) ParamUtil.getSessionAttr(request, KEY_JUMP_DEST_NODE);
        SimpleNode batNode = (SimpleNode) facRegRoot.at(currentNodePath);
        BiologicalAgentToxinDto batDto = (BiologicalAgentToxinDto) batNode.getValue();
        Boolean needShowError = (Boolean) ParamUtil.getRequestAttr(request, KEY_SHOW_ERROR_SWITCH);
        if (needShowError == Boolean.TRUE) {
            ParamUtil.setRequestAttr(request, KEY_VALIDATION_ERRORS, batDto.retrieveValidationResult());
        }
        Nodes.needValidation(facRegRoot, currentNodePath);
        ParamUtil.setRequestAttr(request, NODE_NAME_FAC_BAT_INFO, batDto);

        NodeGroup batGroup = (NodeGroup) facRegRoot.at(NODE_NAME_FAC_BAT_INFO);
        ParamUtil.setRequestAttr(request, "activeNodeKey", batGroup.getActiveNodeKey());
        SimpleNode facSelectionNode = (SimpleNode) facRegRoot.getNode(NODE_NAME_FAC_SELECTION);
        FacilitySelectionDto selectionDto = (FacilitySelectionDto) facSelectionNode.getValue();
        ParamUtil.setRequestAttr(request, "activityTypes", selectionDto.getActivityTypes());

        List<SelectOption> scheduleOps = MasterCodeUtil.retrieveOptionsByCate(MasterCodeUtil.CATE_ID_BSB_SCHEDULE_TYPE);
        ParamUtil.setRequestAttr(request, "ScheduleOps", scheduleOps);

        List<SelectOption> batNameOps = new ArrayList<>(2);
        batNameOps.add(new SelectOption("AEE1CC32-46F0-EB11-8B7D-000C293F0C99", "BRUCELLA CANIS"));
        batNameOps.add(new SelectOption("A4A0E7C9-46F0-EB11-8B7D-000C293F0C99", "CHLAMUDIA PSTTACI"));
        ParamUtil.setRequestAttr(request, "batNameOps", batNameOps);
    }

    public void handleBAToxin(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        NodeGroup facRegRoot = (NodeGroup) ParamUtil.getSessionAttr(request, KEY_ROOT_NODE_GROUP);
        String currentNodePath = (String) ParamUtil.getSessionAttr(request, KEY_JUMP_DEST_NODE);
        SimpleNode batNode = (SimpleNode) facRegRoot.at(currentNodePath);
        BiologicalAgentToxinDto batDto = (BiologicalAgentToxinDto) batNode.getValue();
        batDto.reqObjMapping(request);

        String actionType = ParamUtil.getString(request, KEY_ACTION_TYPE);
        if (KEY_ACTION_JUMP.equals(actionType)) {
            facilityRegistrationService.jumpHandler(request, facRegRoot, currentNodePath, batNode);
        } else {
            throw new IaisRuntimeException(ERR_MSG_INVALID_ACTION);
        }
        ParamUtil.setSessionAttr(request, KEY_ROOT_NODE_GROUP, facRegRoot);
    }

    public void preOtherAppInfo(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        NodeGroup facRegRoot = (NodeGroup) ParamUtil.getSessionAttr(request, KEY_ROOT_NODE_GROUP);
        SimpleNode otherAppInfoNode = (SimpleNode) facRegRoot.at(NODE_NAME_OTHER_INFO);
        OtherApplicationInfoDto otherAppInfoDto = (OtherApplicationInfoDto) otherAppInfoNode.getValue();
        Boolean needShowError = (Boolean) ParamUtil.getRequestAttr(request, KEY_SHOW_ERROR_SWITCH);
        if (needShowError == Boolean.TRUE) {
            ParamUtil.setRequestAttr(request, KEY_VALIDATION_ERRORS, otherAppInfoDto.retrieveValidationResult());
        }
        Nodes.needValidation(facRegRoot, NODE_NAME_OTHER_INFO);
        ParamUtil.setRequestAttr(request, NODE_NAME_OTHER_INFO, otherAppInfoDto);
    }

    public void handleOtherAppInfo(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        NodeGroup facRegRoot = (NodeGroup) ParamUtil.getSessionAttr(request, KEY_ROOT_NODE_GROUP);
        SimpleNode otherAppInfoNode = (SimpleNode) facRegRoot.at(NODE_NAME_OTHER_INFO);
        OtherApplicationInfoDto otherAppInfoDto = (OtherApplicationInfoDto) otherAppInfoNode.getValue();
        otherAppInfoDto.reqObjMapping(request);

        String actionType = ParamUtil.getString(request, KEY_ACTION_TYPE);
        if (KEY_ACTION_JUMP.equals(actionType)) {
            facilityRegistrationService.jumpHandler(request, facRegRoot, NODE_NAME_OTHER_INFO, otherAppInfoNode);
        } else {
            throw new IaisRuntimeException(ERR_MSG_INVALID_ACTION);
        }
        ParamUtil.setSessionAttr(request, KEY_ROOT_NODE_GROUP, facRegRoot);
    }

    public void prePrimaryDoc(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        NodeGroup facRegRoot = (NodeGroup) ParamUtil.getSessionAttr(request, KEY_ROOT_NODE_GROUP);
        SimpleNode primaryDocNode = (SimpleNode) facRegRoot.at(NODE_NAME_PRIMARY_DOC);
        PrimaryDocDto primaryDocDto = (PrimaryDocDto) primaryDocNode.getValue();
        Boolean needShowError = (Boolean) ParamUtil.getRequestAttr(request, KEY_SHOW_ERROR_SWITCH);
        if (needShowError == Boolean.TRUE) {
            ParamUtil.setRequestAttr(request, KEY_VALIDATION_ERRORS, primaryDocDto.retrieveValidationResult());
        }
        Nodes.needValidation(facRegRoot, NODE_NAME_PRIMARY_DOC);

        ParamUtil.setRequestAttr(request, "docSettings", facilityRegistrationService.getFacRegDocSettings());

        Map<String, List<DocRecordInfo>> savedFiles = primaryDocDto.getExistDocTypeMap();
        Map<String, List<NewDocInfo>> newFiles = primaryDocDto.getNewDocTypeMap();
        ParamUtil.setRequestAttr(request, "savedFiles", savedFiles);
        ParamUtil.setRequestAttr(request, "newFiles", newFiles);
    }

    public void handlePrimaryDoc(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        NodeGroup facRegRoot = (NodeGroup) ParamUtil.getSessionAttr(request, KEY_ROOT_NODE_GROUP);
        SimpleNode primaryDocNode = (SimpleNode) facRegRoot.at(NODE_NAME_PRIMARY_DOC);
        PrimaryDocDto primaryDocDto = (PrimaryDocDto) primaryDocNode.getValue();
        primaryDocDto.reqObjMapping(request);

        String actionType = ParamUtil.getString(request, KEY_ACTION_TYPE);
        if (KEY_ACTION_JUMP.equals(actionType)) {
            facilityRegistrationService.jumpHandler(request, facRegRoot, NODE_NAME_PRIMARY_DOC, primaryDocNode);
        } else {
            throw new IaisRuntimeException(ERR_MSG_INVALID_ACTION);
        }
        ParamUtil.setSessionAttr(request, KEY_ROOT_NODE_GROUP, facRegRoot);
    }

    public void prePreviewSubmit(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        NodeGroup facRegRoot = (NodeGroup) ParamUtil.getSessionAttr(request, KEY_ROOT_NODE_GROUP);
        SimpleNode previewSubmitNode = (SimpleNode) facRegRoot.at(NODE_NAME_PREVIEW_SUBMIT);
        PreviewSubmitDto previewSubmitDto = (PreviewSubmitDto) previewSubmitNode.getValue();
        Boolean needShowError = (Boolean) ParamUtil.getRequestAttr(request, KEY_SHOW_ERROR_SWITCH);
        if (needShowError == Boolean.TRUE) {
            ParamUtil.setRequestAttr(request, KEY_VALIDATION_ERRORS, previewSubmitDto.retrieveValidationResult());
        }
        Nodes.needValidation(facRegRoot, NODE_NAME_PREVIEW_SUBMIT);
        ParamUtil.setRequestAttr(request, NODE_NAME_PREVIEW_SUBMIT, previewSubmitDto);

        ParamUtil.setRequestAttr(request, NODE_NAME_FAC_PROFILE, ((SimpleNode)facRegRoot.at(NODE_NAME_FAC_INFO + facRegRoot.getPathSeparator() + NODE_NAME_FAC_PROFILE)).getValue());
        ParamUtil.setRequestAttr(request, NODE_NAME_FAC_OPERATOR, ((SimpleNode)facRegRoot.at(NODE_NAME_FAC_INFO + facRegRoot.getPathSeparator() + NODE_NAME_FAC_OPERATOR)).getValue());
        ParamUtil.setRequestAttr(request, NODE_NAME_FAC_AUTH, ((SimpleNode)facRegRoot.at(NODE_NAME_FAC_INFO + facRegRoot.getPathSeparator() + NODE_NAME_FAC_AUTH)).getValue());
        ParamUtil.setRequestAttr(request, NODE_NAME_FAC_ADMIN, ((SimpleNode)facRegRoot.at(NODE_NAME_FAC_INFO + facRegRoot.getPathSeparator() + NODE_NAME_FAC_ADMIN)).getValue());
        ParamUtil.setRequestAttr(request, NODE_NAME_FAC_OFFICER, ((SimpleNode)facRegRoot.at(NODE_NAME_FAC_INFO + facRegRoot.getPathSeparator() + NODE_NAME_FAC_OFFICER)).getValue());
        ParamUtil.setRequestAttr(request, NODE_NAME_FAC_COMMITTEE, ((SimpleNode)facRegRoot.at(NODE_NAME_FAC_INFO + facRegRoot.getPathSeparator() + NODE_NAME_FAC_COMMITTEE)).getValue());

        NodeGroup batNodeGroup = (NodeGroup) facRegRoot.at(NODE_NAME_FAC_BAT_INFO);
        List<BiologicalAgentToxinDto> batList = FacilityRegistrationService.getBatInfoList(batNodeGroup);
        ParamUtil.setRequestAttr(request, "batList", batList);

        ParamUtil.setRequestAttr(request, "docSettings", facilityRegistrationService.getFacRegDocSettings());
        PrimaryDocDto primaryDocDto = (PrimaryDocDto) ((SimpleNode)facRegRoot.at(NODE_NAME_PRIMARY_DOC)).getValue();
        Map<String, List<DocRecordInfo>> savedFiles = primaryDocDto.getExistDocTypeMap();
        Map<String, List<NewDocInfo>> newFiles = primaryDocDto.getNewDocTypeMap();
        ParamUtil.setRequestAttr(request, "savedFiles", savedFiles);
        ParamUtil.setRequestAttr(request, "newFiles", newFiles);

        List<SelectOption> approvedFacCertifierOps = new ArrayList<>(0);
        ParamUtil.setRequestAttr(request, "approvedFacCertifierOps", approvedFacCertifierOps);
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

                    FacilityRegisterDto finalAllDataDto = FacilityRegisterDto.from(facRegRoot);
                    //rfc compare to decision flowType
                    FacilityRegisterDto oldFacilityRegisterDto = (FacilityRegisterDto)ParamUtil.getSessionAttr(request, KEY_OLD_FACILITY_REGISTER_DTO);
                    DecisionFlowType flowType = new DecisionFlowTypeImpl();
                    RfcFlowType rfcFlowType = flowType.facRegFlowType(facilityRegistrationService.compareTwoDto(oldFacilityRegisterDto,finalAllDataDto));
                    ParamUtil.setRequestAttr(request, "rfcFlowType", rfcFlowType);
                    if (rfcFlowType == RfcFlowType.AMENDMENT || rfcFlowType == RfcFlowType.NOTIFICATION){
                        // save docs
                        SimpleNode primaryDocNode = (SimpleNode) facRegRoot.at(NODE_NAME_PRIMARY_DOC);
                        PrimaryDocDto primaryDocDto = (PrimaryDocDto) primaryDocNode.getValue();
                        List<NewFileSyncDto> newFilesToSync = null;
                        if (!primaryDocDto.getNewDocMap().isEmpty()) {
                            MultipartFile[] files = primaryDocDto.getNewDocMap().values().stream().map(NewDocInfo::getMultipartFile).toArray(MultipartFile[]::new);
                            List<String> repoIds = fileRepoClient.saveFiles(files).getEntity();
                            newFilesToSync = primaryDocDto.newFileSaved(repoIds);
                        }

                        AuditTrailDto auditTrailDto = (AuditTrailDto) ParamUtil.getSessionAttr(request, AuditTrailConsts.SESSION_ATTR_PARAM_NAME);
                        finalAllDataDto.setAuditTrailDto(auditTrailDto);
                        ResponseDto<String> responseDto = facRegClient.saveAmendmentFacility(finalAllDataDto);
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
                    ParamUtil.setSessionAttr(request, KEY_JUMP_DEST_NODE, NODE_NAME_PREVIEW_SUBMIT);
                }
            } else {
                facilityRegistrationService.jumpHandler(request, facRegRoot, NODE_NAME_PREVIEW_SUBMIT, previewSubmitNode);
            }
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
        HttpServletRequest request = bpc.request;
        String destNode = (String) ParamUtil.getSessionAttr(request, KEY_JUMP_DEST_NODE);
        destNode = facilityRegistrationService.batNodeSpecialHandle(destNode);
        ParamUtil.setRequestAttr(request, KEY_DEST_NODE_ROUTE, destNode);
    }

    public void actionFilter(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        String actionType = (String) ParamUtil.getRequestAttr(request, KEY_ACTION_TYPE);
        if (!StringUtils.hasLength(actionType)) {
            actionType = ParamUtil.getString(request, KEY_ACTION_TYPE);
        }
        ParamUtil.setRequestAttr(request, KEY_INDEED_ACTION_TYPE, actionType);
    }

    public void preAcknowledge(BaseProcessClass bpc) {
        //do nothing now
    }
}

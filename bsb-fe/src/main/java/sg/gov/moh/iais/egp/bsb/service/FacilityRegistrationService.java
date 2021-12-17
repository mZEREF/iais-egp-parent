package sg.gov.moh.iais.egp.bsb.service;

import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.mastercode.MasterCodeView;
import com.ecquaria.cloud.moh.iais.common.exception.IaisRuntimeException;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.helper.MasterCodeUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import sg.gov.moh.iais.egp.bsb.common.node.Node;
import sg.gov.moh.iais.egp.bsb.common.node.NodeGroup;
import sg.gov.moh.iais.egp.bsb.common.node.Nodes;
import sg.gov.moh.iais.egp.bsb.common.node.simple.SimpleNode;
import sg.gov.moh.iais.egp.bsb.common.rfc.CompareTwoObject;
import sg.gov.moh.iais.egp.bsb.constant.DocConstants;
import sg.gov.moh.iais.egp.bsb.dto.file.DocRecordInfo;
import sg.gov.moh.iais.egp.bsb.dto.file.NewDocInfo;
import sg.gov.moh.iais.egp.bsb.dto.register.facility.*;
import sg.gov.moh.iais.egp.bsb.dto.rfc.DiffContent;
import sg.gov.moh.iais.egp.bsb.entity.DocSetting;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static sg.gov.moh.iais.egp.bsb.constant.FacRegisterConstants.*;
import static sg.gov.moh.iais.egp.bsb.constant.FacRegisterConstants.NODE_NAME_FAC_BAT_INFO;


/**
 * Facility Registration(new application, rfc, renewal) delegator common method.
 * @author : LiRan
 * @date : 2021/12/8
 */
@Service
@Slf4j
public class FacilityRegistrationService {

    public void preCompInfo(BaseProcessClass bpc) {
        // do nothing now, need to prepare company info in the future
    }

    public void handleCompInfo(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        NodeGroup facRegRoot = getFacilityRegisterRoot(request);
        Node compInfoNode = facRegRoot.getNode(NODE_NAME_COMPANY_INFO);
        String actionType = ParamUtil.getString(request, KEY_ACTION_TYPE);
        if (KEY_ACTION_JUMP.equals(actionType)) {
            jumpHandler(request, facRegRoot, NODE_NAME_COMPANY_INFO, compInfoNode);
        } else {
            throw new IaisRuntimeException(ERR_MSG_INVALID_ACTION);
        }
        ParamUtil.setSessionAttr(request, KEY_ROOT_NODE_GROUP, facRegRoot);
    }

    public void preServiceSelection(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        NodeGroup facRegRoot = getFacilityRegisterRoot(request);
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
        NodeGroup facRegRoot = getFacilityRegisterRoot(request);
        SimpleNode facSelectionNode = (SimpleNode) facRegRoot.getNode(NODE_NAME_FAC_SELECTION);
        FacilitySelectionDto selectionDto = (FacilitySelectionDto) facSelectionNode.getValue();
        selectionDto.reqObjMapping(request);

        String actionType = ParamUtil.getString(request, KEY_ACTION_TYPE);
        if (KEY_ACTION_JUMP.equals(actionType)) {
            jumpHandler(request, facRegRoot, NODE_NAME_FAC_SELECTION, facSelectionNode);
        } else {
            throw new IaisRuntimeException(ERR_MSG_INVALID_ACTION);
        }

        if (facSelectionNode.isValidated()) {
            NodeGroup batGroup = (NodeGroup) facRegRoot.getNode(NODE_NAME_FAC_BAT_INFO);
            changeBatNodeGroup(batGroup, selectionDto);
        }
        ParamUtil.setSessionAttr(request, KEY_ROOT_NODE_GROUP, facRegRoot);
    }

    public void preFacProfile(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        NodeGroup facRegRoot = getFacilityRegisterRoot(request);
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
        NodeGroup facRegRoot = getFacilityRegisterRoot(request);
        String currentNodePath = NODE_NAME_FAC_INFO + facRegRoot.getPathSeparator() + NODE_NAME_FAC_PROFILE;
        SimpleNode facProfileNode = (SimpleNode) facRegRoot.at(currentNodePath);
        FacilityProfileDto facProfileDto = (FacilityProfileDto) facProfileNode.getValue();
        facProfileDto.reqObjMapping(request);
        SimpleNode facAuthNode = (SimpleNode) facRegRoot.at(NODE_NAME_FAC_INFO + facRegRoot.getPathSeparator() + NODE_NAME_FAC_AUTH);
        FacilityAuthoriserDto facAuthDto = (FacilityAuthoriserDto) facAuthNode.getValue();
        facAuthDto.setIsProtectedPlace(facProfileDto.getIsFacilityProtected());

        String actionType = ParamUtil.getString(request, KEY_ACTION_TYPE);
        if (KEY_ACTION_JUMP.equals(actionType)) {
            jumpHandler(request, facRegRoot, currentNodePath, facProfileNode);
        } else {
            throw new IaisRuntimeException(ERR_MSG_INVALID_ACTION);
        }
        ParamUtil.setSessionAttr(request, KEY_ROOT_NODE_GROUP, facRegRoot);
    }

    public void preFacOperator(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        NodeGroup facRegRoot = getFacilityRegisterRoot(request);
        String currentNodePath = NODE_NAME_FAC_INFO + facRegRoot.getPathSeparator() + NODE_NAME_FAC_OPERATOR;
        SimpleNode facOpNode = (SimpleNode) facRegRoot.at(currentNodePath);
        FacilityOperatorDto facOpDto = (FacilityOperatorDto) facOpNode.getValue();
        Boolean needShowError = (Boolean) ParamUtil.getRequestAttr(request, KEY_SHOW_ERROR_SWITCH);
        if (needShowError == Boolean.TRUE) {
            ParamUtil.setRequestAttr(request, KEY_VALIDATION_ERRORS, facOpDto.retrieveValidationResult());
        }
        Nodes.needValidation(facRegRoot, currentNodePath);
        ParamUtil.setRequestAttr(request, NODE_NAME_FAC_OPERATOR, facOpDto);

        ParamUtil.setRequestAttr(request, KEY_NATIONALITY_OPTIONS, tmpNationalityOps());
    }

    public void handleFacOperator(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        NodeGroup facRegRoot = getFacilityRegisterRoot(request);
        String currentNodePath = NODE_NAME_FAC_INFO + facRegRoot.getPathSeparator() + NODE_NAME_FAC_OPERATOR;
        SimpleNode facOpNode = (SimpleNode) facRegRoot.at(currentNodePath);
        FacilityOperatorDto facOpDto = (FacilityOperatorDto) facOpNode.getValue();
        facOpDto.reqObjMapping(request);

        String actionType = ParamUtil.getString(request, KEY_ACTION_TYPE);
        if (KEY_ACTION_JUMP.equals(actionType)) {
            jumpHandler(request, facRegRoot, currentNodePath, facOpNode);
        } else {
            throw new IaisRuntimeException(ERR_MSG_INVALID_ACTION);
        }
        ParamUtil.setSessionAttr(request, KEY_ROOT_NODE_GROUP, facRegRoot);
    }

    public void preFacInfoAuthoriser(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        NodeGroup facRegRoot = getFacilityRegisterRoot(request);
        String currentNodePath = NODE_NAME_FAC_INFO + facRegRoot.getPathSeparator() + NODE_NAME_FAC_AUTH;
        SimpleNode facAuthNode = (SimpleNode) facRegRoot.at(currentNodePath);
        FacilityAuthoriserDto facAuthDto = (FacilityAuthoriserDto) facAuthNode.getValue();
        Boolean needShowError = (Boolean) ParamUtil.getRequestAttr(request, KEY_SHOW_ERROR_SWITCH);
        if (needShowError == Boolean.TRUE) {
            ParamUtil.setRequestAttr(request, KEY_VALIDATION_ERRORS, facAuthDto.retrieveValidationResult());
        }
        Nodes.needValidation(facRegRoot, currentNodePath);
        ParamUtil.setRequestAttr(request, NODE_NAME_FAC_AUTH, facAuthDto);

        ParamUtil.setRequestAttr(request, KEY_NATIONALITY_OPTIONS, tmpNationalityOps());
    }

    public void handleFacInfoAuthoriser(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        NodeGroup facRegRoot = getFacilityRegisterRoot(request);
        String currentNodePath = NODE_NAME_FAC_INFO + facRegRoot.getPathSeparator() + NODE_NAME_FAC_AUTH;
        SimpleNode facAuthNode = (SimpleNode) facRegRoot.at(currentNodePath);
        FacilityAuthoriserDto facAuthDto = (FacilityAuthoriserDto) facAuthNode.getValue();
        facAuthDto.reqObjMapping(request);

        String actionType = ParamUtil.getString(request, KEY_ACTION_TYPE);
        if (KEY_ACTION_JUMP.equals(actionType)) {
            jumpHandler(request, facRegRoot, currentNodePath, facAuthNode);
        } else {
            throw new IaisRuntimeException(ERR_MSG_INVALID_ACTION);
        }
        ParamUtil.setSessionAttr(request, KEY_ROOT_NODE_GROUP, facRegRoot);
    }

    public void preFacAdmin(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        NodeGroup facRegRoot = getFacilityRegisterRoot(request);
        String currentNodePath = NODE_NAME_FAC_INFO + facRegRoot.getPathSeparator() + NODE_NAME_FAC_ADMIN;
        SimpleNode facAdminNode = (SimpleNode) facRegRoot.at(currentNodePath);
        FacilityAdministratorDto facAdminDto = (FacilityAdministratorDto) facAdminNode.getValue();
        Boolean needShowError = (Boolean) ParamUtil.getRequestAttr(request, KEY_SHOW_ERROR_SWITCH);
        if (needShowError == Boolean.TRUE) {
            ParamUtil.setRequestAttr(request, KEY_VALIDATION_ERRORS, facAdminDto.retrieveValidationResult());
        }
        Nodes.needValidation(facRegRoot, currentNodePath);
        ParamUtil.setRequestAttr(request, NODE_NAME_FAC_ADMIN, facAdminDto);

        ParamUtil.setRequestAttr(request, KEY_NATIONALITY_OPTIONS, tmpNationalityOps());
    }

    public void handleFacAdmin(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        NodeGroup facRegRoot = getFacilityRegisterRoot(request);
        String currentNodePath = NODE_NAME_FAC_INFO + facRegRoot.getPathSeparator() + NODE_NAME_FAC_ADMIN;
        SimpleNode facAdminNode = (SimpleNode) facRegRoot.at(currentNodePath);
        FacilityAdministratorDto facAdminDto = (FacilityAdministratorDto) facAdminNode.getValue();
        facAdminDto.reqObjMapping(request);

        String actionType = ParamUtil.getString(request, KEY_ACTION_TYPE);
        if (KEY_ACTION_JUMP.equals(actionType)) {
            jumpHandler(request, facRegRoot, currentNodePath, facAdminNode);
        } else {
            throw new IaisRuntimeException(ERR_MSG_INVALID_ACTION);
        }
        ParamUtil.setSessionAttr(request, KEY_ROOT_NODE_GROUP, facRegRoot);
    }

    public void preFacOfficer(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        NodeGroup facRegRoot = getFacilityRegisterRoot(request);
        String currentNodePath = NODE_NAME_FAC_INFO + facRegRoot.getPathSeparator() + NODE_NAME_FAC_OFFICER;
        SimpleNode facOfficerNode = (SimpleNode) facRegRoot.at(currentNodePath);
        FacilityOfficerDto facOfficerDto = (FacilityOfficerDto) facOfficerNode.getValue();
        Boolean needShowError = (Boolean) ParamUtil.getRequestAttr(request, KEY_SHOW_ERROR_SWITCH);
        if (needShowError == Boolean.TRUE) {
            ParamUtil.setRequestAttr(request, KEY_VALIDATION_ERRORS, facOfficerDto.retrieveValidationResult());
        }
        Nodes.needValidation(facRegRoot, currentNodePath);
        ParamUtil.setRequestAttr(request, NODE_NAME_FAC_OFFICER, facOfficerDto);

        ParamUtil.setRequestAttr(request, KEY_NATIONALITY_OPTIONS, tmpNationalityOps());
    }

    public void handleFacOfficer(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        NodeGroup facRegRoot = getFacilityRegisterRoot(request);
        String currentNodePath = NODE_NAME_FAC_INFO + facRegRoot.getPathSeparator() + NODE_NAME_FAC_OFFICER;
        SimpleNode facOfficerNode = (SimpleNode) facRegRoot.at(currentNodePath);
        FacilityOfficerDto facOfficerDto = (FacilityOfficerDto) facOfficerNode.getValue();
        facOfficerDto.reqObjMapping(request);

        String actionType = ParamUtil.getString(request, KEY_ACTION_TYPE);
        if (KEY_ACTION_JUMP.equals(actionType)) {
            jumpHandler(request, facRegRoot, currentNodePath, facOfficerNode);
        } else {
            throw new IaisRuntimeException(ERR_MSG_INVALID_ACTION);
        }
        ParamUtil.setSessionAttr(request, KEY_ROOT_NODE_GROUP, facRegRoot);
    }

    public void preFacInfoCommittee(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        NodeGroup facRegRoot = getFacilityRegisterRoot(request);
        String currentNodePath = NODE_NAME_FAC_INFO + facRegRoot.getPathSeparator() + NODE_NAME_FAC_COMMITTEE;
        SimpleNode facCommitteeNode = (SimpleNode) facRegRoot.at(currentNodePath);
        FacilityCommitteeDto facCommitteeDto = (FacilityCommitteeDto) facCommitteeNode.getValue();
        Boolean needShowError = (Boolean) ParamUtil.getRequestAttr(request, KEY_SHOW_ERROR_SWITCH);
        if (needShowError == Boolean.TRUE) {
            ParamUtil.setRequestAttr(request, KEY_VALIDATION_ERRORS, facCommitteeDto.retrieveValidationResult());
        }
        Nodes.needValidation(facRegRoot, currentNodePath);
        ParamUtil.setRequestAttr(request, NODE_NAME_FAC_COMMITTEE, facCommitteeDto);

        ParamUtil.setRequestAttr(request, KEY_NATIONALITY_OPTIONS, tmpNationalityOps());
        ParamUtil.setRequestAttr(request, "personnelRoleOps", tmpPersonnelRoleOps());
    }

    public void handleFacInfoCommittee(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        NodeGroup facRegRoot = getFacilityRegisterRoot(request);
        String currentNodePath = NODE_NAME_FAC_INFO + facRegRoot.getPathSeparator() + NODE_NAME_FAC_COMMITTEE;
        SimpleNode facCommitteeNode = (SimpleNode) facRegRoot.at(currentNodePath);
        FacilityCommitteeDto facCommitteeDto = (FacilityCommitteeDto) facCommitteeNode.getValue();
        facCommitteeDto.reqObjMapping(request);

        String actionType = ParamUtil.getString(request, KEY_ACTION_TYPE);
        if (KEY_ACTION_JUMP.equals(actionType)) {
            jumpHandler(request, facRegRoot, currentNodePath, facCommitteeNode);
        } else {
            throw new IaisRuntimeException(ERR_MSG_INVALID_ACTION);
        }
        ParamUtil.setSessionAttr(request, KEY_ROOT_NODE_GROUP, facRegRoot);
    }

    public void preBAToxin(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        NodeGroup facRegRoot = getFacilityRegisterRoot(request);
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
        NodeGroup facRegRoot = getFacilityRegisterRoot(request);
        String currentNodePath = (String) ParamUtil.getSessionAttr(request, KEY_JUMP_DEST_NODE);
        SimpleNode batNode = (SimpleNode) facRegRoot.at(currentNodePath);
        BiologicalAgentToxinDto batDto = (BiologicalAgentToxinDto) batNode.getValue();
        batDto.reqObjMapping(request);

        String actionType = ParamUtil.getString(request, KEY_ACTION_TYPE);
        if (KEY_ACTION_JUMP.equals(actionType)) {
            jumpHandler(request, facRegRoot, currentNodePath, batNode);
        } else {
            throw new IaisRuntimeException(ERR_MSG_INVALID_ACTION);
        }
        ParamUtil.setSessionAttr(request, KEY_ROOT_NODE_GROUP, facRegRoot);
    }

    public void preOtherAppInfo(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        NodeGroup facRegRoot = getFacilityRegisterRoot(request);
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
        NodeGroup facRegRoot = getFacilityRegisterRoot(request);
        SimpleNode otherAppInfoNode = (SimpleNode) facRegRoot.at(NODE_NAME_OTHER_INFO);
        OtherApplicationInfoDto otherAppInfoDto = (OtherApplicationInfoDto) otherAppInfoNode.getValue();
        otherAppInfoDto.reqObjMapping(request);

        String actionType = ParamUtil.getString(request, KEY_ACTION_TYPE);
        if (KEY_ACTION_JUMP.equals(actionType)) {
            jumpHandler(request, facRegRoot, NODE_NAME_OTHER_INFO, otherAppInfoNode);
        } else {
            throw new IaisRuntimeException(ERR_MSG_INVALID_ACTION);
        }
        ParamUtil.setSessionAttr(request, KEY_ROOT_NODE_GROUP, facRegRoot);
    }

    public void prePrimaryDoc(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        NodeGroup facRegRoot = getFacilityRegisterRoot(request);
        SimpleNode primaryDocNode = (SimpleNode) facRegRoot.at(NODE_NAME_PRIMARY_DOC);
        PrimaryDocDto primaryDocDto = (PrimaryDocDto) primaryDocNode.getValue();
        Boolean needShowError = (Boolean) ParamUtil.getRequestAttr(request, KEY_SHOW_ERROR_SWITCH);
        if (needShowError == Boolean.TRUE) {
            ParamUtil.setRequestAttr(request, KEY_VALIDATION_ERRORS, primaryDocDto.retrieveValidationResult());
        }
        Nodes.needValidation(facRegRoot, NODE_NAME_PRIMARY_DOC);

        ParamUtil.setRequestAttr(request, "docSettings", getFacRegDocSettings());

        Map<String, List<DocRecordInfo>> savedFiles = primaryDocDto.getExistDocTypeMap();
        Map<String, List<NewDocInfo>> newFiles = primaryDocDto.getNewDocTypeMap();
        ParamUtil.setRequestAttr(request, "savedFiles", savedFiles);
        ParamUtil.setRequestAttr(request, "newFiles", newFiles);
    }

    public void handlePrimaryDoc(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        NodeGroup facRegRoot = getFacilityRegisterRoot(request);
        SimpleNode primaryDocNode = (SimpleNode) facRegRoot.at(NODE_NAME_PRIMARY_DOC);
        PrimaryDocDto primaryDocDto = (PrimaryDocDto) primaryDocNode.getValue();
        primaryDocDto.reqObjMapping(request);

        String actionType = ParamUtil.getString(request, KEY_ACTION_TYPE);
        if (KEY_ACTION_JUMP.equals(actionType)) {
            jumpHandler(request, facRegRoot, NODE_NAME_PRIMARY_DOC, primaryDocNode);
        } else {
            throw new IaisRuntimeException(ERR_MSG_INVALID_ACTION);
        }
        ParamUtil.setSessionAttr(request, KEY_ROOT_NODE_GROUP, facRegRoot);
    }

    public void prePreviewSubmit(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        NodeGroup facRegRoot = getFacilityRegisterRoot(request);
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

        ParamUtil.setRequestAttr(request, "docSettings", getFacRegDocSettings());
        PrimaryDocDto primaryDocDto = (PrimaryDocDto) ((SimpleNode)facRegRoot.at(NODE_NAME_PRIMARY_DOC)).getValue();
        Map<String, List<DocRecordInfo>> savedFiles = primaryDocDto.getExistDocTypeMap();
        Map<String, List<NewDocInfo>> newFiles = primaryDocDto.getNewDocTypeMap();
        ParamUtil.setRequestAttr(request, "savedFiles", savedFiles);
        ParamUtil.setRequestAttr(request, "newFiles", newFiles);

        List<SelectOption> approvedFacCertifierOps = new ArrayList<>(0);
        ParamUtil.setRequestAttr(request, "approvedFacCertifierOps", approvedFacCertifierOps);
    }

    public void actionFilter(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        String actionType = (String) ParamUtil.getRequestAttr(request, KEY_ACTION_TYPE);
        if (!StringUtils.hasLength(actionType)) {
            actionType = ParamUtil.getString(request, KEY_ACTION_TYPE);
        }
        ParamUtil.setRequestAttr(request, KEY_INDEED_ACTION_TYPE, actionType);
    }

    public void jumpFilter(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        String destNode = (String) ParamUtil.getSessionAttr(request, KEY_JUMP_DEST_NODE);
        destNode = batNodeSpecialHandle(destNode);
        ParamUtil.setRequestAttr(request, KEY_DEST_NODE_ROUTE, destNode);
    }

    /**
     * Get the root data structure of this flow
     */
    public NodeGroup getFacilityRegisterRoot(HttpServletRequest request) {
        NodeGroup root = (NodeGroup) ParamUtil.getSessionAttr(request, KEY_ROOT_NODE_GROUP);
        if (root == null) {
            root = newFacRegisterRoot(KEY_ROOT_NODE_GROUP);
        }
        return root;
    }

    /**
     * Compute the destiny node path we will go to.
     * @param facRegRoot the root NodeGroup
     * @param actionValue the value we received, it can be next, back, or a value specified by tab
     * @return the destiny node path, return null if we can't go the next or previous node
     */
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
        if (KEY_NAV_NEXT.equals(actionValue)) {  // if click next, we need to validate current node anyway
            currentLetGo = currentNode.doValidation();
            if (currentLetGo) {
                Nodes.passValidation(facRegRoot, currentPath);
            }
        }
        if (currentLetGo) {
            String destNode = computeDestNodePath(facRegRoot, actionValue);
            String checkedDestNode = Nodes.jump(facRegRoot, destNode);
            if (!checkedDestNode.equals(destNode)) {
                ParamUtil.setRequestAttr(request, KEY_SHOW_ERROR_SWITCH, Boolean.TRUE);
            }
            ParamUtil.setSessionAttr(request, KEY_JUMP_DEST_NODE, checkedDestNode);
        } else {
            ParamUtil.setRequestAttr(request, KEY_SHOW_ERROR_SWITCH, Boolean.TRUE);
            ParamUtil.setSessionAttr(request, KEY_JUMP_DEST_NODE, currentPath);
        }
    }

    /**
     * Renewal Module, when actionType is review, special jump handle
     */
    public void renewalJumpHandle(HttpServletRequest request, NodeGroup facRegRoot, String currentPath,Node currentNode){
        String actionValue = ParamUtil.getString(request, KEY_ACTION_VALUE);
        Assert.hasText(actionValue, "Invalid action value");
        boolean currentLetGo = true;  // if false, we have to stay current node
        if (KEY_NAV_NEXT.equals(actionValue)) {  // if click next, we need to validate current node anyway
            currentLetGo = currentNode.doValidation();
            if (currentLetGo) {
                Nodes.passValidation(facRegRoot, currentPath);
            }
        }
        if (currentLetGo) {
            ParamUtil.setSessionAttr(request, KEY_JUMP_DEST_NODE, NODE_NAME_REVIEW);
            ParamUtil.setRequestAttr(request, KEY_ACTION_TYPE, KEY_ACTION_JUMP);
        } else {
            ParamUtil.setRequestAttr(request, KEY_ACTION_TYPE, KEY_ACTION_JUMP);
            ParamUtil.setRequestAttr(request, KEY_SHOW_ERROR_SWITCH, Boolean.TRUE);
            ParamUtil.setSessionAttr(request, KEY_JUMP_DEST_NODE, currentPath);
        }
    }

    public String batNodeSpecialHandle(String destNode) {
        return destNode.startsWith(NODE_NAME_FAC_BAT_INFO) ? NODE_NAME_FAC_BAT_INFO : destNode;
    }

    public static NodeGroup newFacRegisterRoot(String name) {
        Node companyInfoNode = new Node(NODE_NAME_COMPANY_INFO, new Node[0]);
        SimpleNode facSelectionNode = new SimpleNode(new FacilitySelectionDto(), NODE_NAME_FAC_SELECTION, new Node[0]);
        NodeGroup facInfoNodeGroup = newFacInfoNodeGroup(new Node[]{facSelectionNode});
        NodeGroup batNodeGroup = initBatNodeGroup(new Node[]{facSelectionNode, facInfoNodeGroup});
        SimpleNode otherAppInfoNode = new SimpleNode(new OtherApplicationInfoDto(), NODE_NAME_OTHER_INFO, new Node[]{facSelectionNode, facInfoNodeGroup, batNodeGroup});
        SimpleNode primaryDocNode = new SimpleNode(new PrimaryDocDto(), NODE_NAME_PRIMARY_DOC, new Node[]{facSelectionNode, facInfoNodeGroup, batNodeGroup, otherAppInfoNode});
        SimpleNode previewSubmitNode = new SimpleNode(new PreviewSubmitDto(), NODE_NAME_PREVIEW_SUBMIT, new Node[]{facSelectionNode, facInfoNodeGroup, batNodeGroup, otherAppInfoNode, primaryDocNode});

        return new NodeGroup.Builder().name(name)
                .addNode(companyInfoNode)
                .addNode(facSelectionNode)
                .addNode(facInfoNodeGroup)
                .addNode(batNodeGroup)
                .addNode(otherAppInfoNode)
                .addNode(primaryDocNode)
                .addNode(previewSubmitNode)
                .build();
    }

    public static NodeGroup newFacInfoNodeGroup(Node[] dependNodes) {
        SimpleNode facProfileNode = new SimpleNode(new FacilityProfileDto(), NODE_NAME_FAC_PROFILE, new Node[0]);
        SimpleNode facOperatorNode = new SimpleNode(new FacilityOperatorDto(), NODE_NAME_FAC_OPERATOR, new Node[]{facProfileNode});
        SimpleNode facAuthNode = new SimpleNode(new FacilityAuthoriserDto(), NODE_NAME_FAC_AUTH, new Node[]{facProfileNode, facOperatorNode});
        SimpleNode facAdminNode = new SimpleNode(new FacilityAdministratorDto(), NODE_NAME_FAC_ADMIN, new Node[]{facProfileNode, facOperatorNode, facAuthNode});
        SimpleNode facOfficerNode = new SimpleNode(new FacilityOfficerDto(), NODE_NAME_FAC_OFFICER, new Node[]{facProfileNode, facOperatorNode, facAuthNode, facAdminNode});
        SimpleNode facCommitteeNode = new SimpleNode(new FacilityCommitteeDto(), NODE_NAME_FAC_COMMITTEE, new Node[]{facProfileNode, facOperatorNode, facAuthNode, facAdminNode, facOfficerNode});

        return new NodeGroup.Builder().name(NODE_NAME_FAC_INFO)
                .dependNodes(dependNodes)
                .addNode(facProfileNode)
                .addNode(facOperatorNode)
                .addNode(facAuthNode)
                .addNode(facAdminNode)
                .addNode(facOfficerNode)
                .addNode(facCommitteeNode)
                .build();
    }

    public static NodeGroup initBatNodeGroup(Node[] dependNodes) {
        return new NodeGroup.Builder().name(NODE_NAME_FAC_BAT_INFO)
                .dependNodes(dependNodes)
                .addNode(new Node("error", new Node[0]))
                .build();
    }

    public static void changeBatNodeGroup(NodeGroup batNodeGroup, FacilitySelectionDto selectionDto) {
        Assert.notNull(batNodeGroup, ERR_MSG_BAT_NOT_NULL);
        Node[] subNodes = new Node[selectionDto.getActivityTypes().size()];
        List<String> activityTypes = selectionDto.getActivityTypes();
        for (int i = 0; i < activityTypes.size(); i++) {
            subNodes[i] = new SimpleNode(new BiologicalAgentToxinDto(activityTypes.get(i)), activityTypes.get(i), new Node[0]);
        }
        batNodeGroup.reorganizeNodes(subNodes);
    }

    public static List<BiologicalAgentToxinDto> getBatInfoList(NodeGroup batNodeGroup) {
        Assert.notNull(batNodeGroup, ERR_MSG_BAT_NOT_NULL);
        List<BiologicalAgentToxinDto> batList = new ArrayList<>(batNodeGroup.count());
        for (Node node : batNodeGroup.getAllNodes()) {
            assert node instanceof SimpleNode;
            batList.add((BiologicalAgentToxinDto) ((SimpleNode) node).getValue());
        }
        return batList;
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
     *
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
    public static List<SelectOption> tmpNationalityOps() {
        List<MasterCodeView> views = MasterCodeUtil.retrieveByCategory(MasterCodeUtil.CATE_ID_NATIONALITY);
        List<SelectOption> ops = new ArrayList<>(views.size());
        if(!CollectionUtils.isEmpty(views)){
            for (MasterCodeView view : views) {
                ops.add(new SelectOption(view.getCode(), view.getCodeValue()));
            }
        }
        return ops;
    }

    /* Will be removed in future, will get this from master code */
    public static List<SelectOption> tmpPersonnelRoleOps() {
        List<SelectOption> personnelRoleOps = new ArrayList<>(4);
        personnelRoleOps.add(new SelectOption("COMTPRO001", "Senior Management Representative"));
        personnelRoleOps.add(new SelectOption("COMTPRO002", "Biosafety Coordinator"));
        personnelRoleOps.add(new SelectOption("COMTPRO003", "Person in charge of safe and proper functioning of facility and equipment"));
        personnelRoleOps.add(new SelectOption("COMTPRO004", "Other qualified personnel"));
        return personnelRoleOps;
    }

    /* Will be removed in future, will get this from config mechanism */
    public List<DocSetting> getFacRegDocSettings () {
        List<DocSetting> docSettings = new ArrayList<>(9);
        docSettings.add(new DocSetting(DocConstants.DOC_TYPE_BIO_SAFETY_COORDINATOR_CERTIFICATES, "BioSafety Coordinator Certificates", true));
        docSettings.add(new DocSetting(DocConstants.DOC_TYPE_INVENTORY_FILE, "Inventory File", false));
        docSettings.add(new DocSetting(DocConstants.DOC_TYPE_GMAC_ENDORSEMENT, "GMAC Endorsement", false));
        docSettings.add(new DocSetting(DocConstants.DOC_TYPE_RISK_ASSESS_PLAN, "Risk Assessment Plan", false));
        docSettings.add(new DocSetting(DocConstants.DOC_TYPE_STANDARD_OPERATING_PROCEDURE, "Standard Operating Procedure", false));
        docSettings.add(new DocSetting(DocConstants.DOC_TYPE_EMERGENCY_RESPONSE_PLAN, "Emergency Response Plan", false));
        docSettings.add(new DocSetting(DocConstants.DOC_TYPE_BIO_SAFETY_COM, "Approval/Endorsement : Biosafety Com", false));
        docSettings.add(new DocSetting(DocConstants.DOC_TYPE_FACILITY_PLAN_LAYOUT, "Facility Plan/Layout", false));
        docSettings.add(new DocSetting(DocConstants.DOC_TYPE_OTHERS, "Others", false));
        return docSettings;
    }

    /**
     * rfc compare
     */
    public List<DiffContent> compareTwoDto(FacilityRegisterDto oldFacilityRegisterDto, FacilityRegisterDto newFacilityRegisterDto){
        List<DiffContent> diffContentList = new ArrayList<>();
        CompareTwoObject.diff(oldFacilityRegisterDto.getFacilitySelectionDto(), newFacilityRegisterDto.getFacilitySelectionDto(), diffContentList);
        CompareTwoObject.diff(oldFacilityRegisterDto.getFacilityProfileDto(), newFacilityRegisterDto.getFacilityProfileDto(), diffContentList);
        CompareTwoObject.diff(oldFacilityRegisterDto.getFacilityOperatorDto(), newFacilityRegisterDto.getFacilityOperatorDto(), diffContentList);
        CompareTwoObject.diff(oldFacilityRegisterDto.getFacilityAuthoriserDto(), newFacilityRegisterDto.getFacilityAuthoriserDto(), diffContentList, FacilityAuthoriserDto.FacilityAuthorisedPersonnel.class);
        CompareTwoObject.diff(oldFacilityRegisterDto.getFacilityAdministratorDto(), newFacilityRegisterDto.getFacilityAdministratorDto(), diffContentList, FacilityAdministratorDto.FacilityAdministratorInfo.class);
        CompareTwoObject.diff(oldFacilityRegisterDto.getFacilityOfficerDto(), newFacilityRegisterDto.getFacilityOfficerDto(), diffContentList);
        CompareTwoObject.diff(oldFacilityRegisterDto.getFacilityCommitteeDto(), newFacilityRegisterDto.getFacilityCommitteeDto(), diffContentList);
        CompareTwoObject.diffMap(oldFacilityRegisterDto.getBiologicalAgentToxinMap(), newFacilityRegisterDto.getBiologicalAgentToxinMap(), diffContentList, BiologicalAgentToxinDto.BATInfo.class);
        //docRecordInfos don't process
        return diffContentList;
    }
}

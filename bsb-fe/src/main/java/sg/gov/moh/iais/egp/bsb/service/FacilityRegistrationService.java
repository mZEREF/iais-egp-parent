package sg.gov.moh.iais.egp.bsb.service;

import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.filerepo.FileRepoDto;
import com.ecquaria.cloud.moh.iais.common.exception.IaisRuntimeException;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Maps;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import sg.gov.moh.iais.egp.bsb.client.BsbFileClient;
import sg.gov.moh.iais.egp.bsb.client.FacilityRegisterClient;
import sg.gov.moh.iais.egp.bsb.client.FileRepoClient;
import sg.gov.moh.iais.egp.bsb.common.node.Node;
import sg.gov.moh.iais.egp.bsb.common.node.NodeGroup;
import sg.gov.moh.iais.egp.bsb.common.node.Nodes;
import sg.gov.moh.iais.egp.bsb.common.node.simple.SimpleNode;
import sg.gov.moh.iais.egp.bsb.common.rfc.CompareTwoObject;
import sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants;
import sg.gov.moh.iais.egp.bsb.dto.file.DocRecordInfo;
import sg.gov.moh.iais.egp.bsb.dto.file.FileRepoSyncDto;
import sg.gov.moh.iais.egp.bsb.dto.file.NewDocInfo;
import sg.gov.moh.iais.egp.bsb.dto.file.NewFileSyncDto;
import sg.gov.moh.iais.egp.bsb.dto.declaration.DeclarationConfigInfo;
import sg.gov.moh.iais.egp.bsb.dto.declaration.DeclarationItemMainInfo;
import sg.gov.moh.iais.egp.bsb.dto.info.bat.BatBasicInfo;
import sg.gov.moh.iais.egp.bsb.dto.register.facility.*;
import sg.gov.moh.iais.egp.bsb.dto.rfc.DiffContent;
import sg.gov.moh.iais.egp.bsb.dto.validation.ValidationListResultUnit;
import sg.gov.moh.iais.egp.bsb.util.mastercode.MasterCodeHolder;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static sg.gov.moh.iais.egp.bsb.constant.FacCertifierRegisterConstants.KEY_RENEWAL_VIEW_APPROVAL_ROOT_NODE_GROUP;
import static sg.gov.moh.iais.egp.bsb.constant.FacRegisterConstants.*;
import static sg.gov.moh.iais.egp.bsb.constant.FacRegisterConstants.NODE_NAME_FAC_BAT_INFO;
import static sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants.KEY_ACTION_ADDITIONAL;
import static sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants.KEY_IND_AFTER_SAVE_AS_DRAFT;


@Service
@Slf4j
public class FacilityRegistrationService {
    private final FileRepoClient fileRepoClient;
    private final BsbFileClient bsbFileClient;
    private final FacilityRegisterClient facRegClient;
    private final DocSettingService docSettingService;

    @Autowired
    public FacilityRegistrationService(FileRepoClient fileRepoClient, BsbFileClient bsbFileClient,
                                       FacilityRegisterClient facRegClient,
                                       DocSettingService docSettingService) {
        this.fileRepoClient = fileRepoClient;
        this.bsbFileClient = bsbFileClient;
        this.facRegClient = facRegClient;
        this.docSettingService = docSettingService;
    }

    public void handleBeforeBegin(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        NodeGroup facRegRoot = getFacilityRegisterRoot(request);
        Node beforeBeginNode = facRegRoot.getNode(NODE_NAME_BEFORE_BEGIN);
        String actionType = ParamUtil.getString(request, KEY_ACTION_TYPE);
        if (KEY_ACTION_JUMP.equals(actionType)) {
            jumpHandler(request, facRegRoot, NODE_NAME_BEFORE_BEGIN, beforeBeginNode);
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
            String actionValue = ParamUtil.getString(request, KEY_ACTION_VALUE);
            if (KEY_NAV_NEXT.equals(actionValue)) {  // if click next, we need to validate current node anyway
                boolean currentLetGo = facSelectionNode.doValidation();
                if (currentLetGo) {
                    Nodes.passValidation(facRegRoot, NODE_NAME_FAC_SELECTION);

                    boolean isCf = MasterCodeConstants.CERTIFIED_CLASSIFICATION.contains(selectionDto.getFacClassification());
                    ParamUtil.setSessionAttr(request, KEY_IS_CF, isCf ? Boolean.TRUE : Boolean.FALSE);
                    boolean isUcf = MasterCodeConstants.UNCERTIFIED_CLASSIFICATION.contains(selectionDto.getFacClassification());
                    ParamUtil.setSessionAttr(request, KEY_IS_UCF, isUcf ? Boolean.TRUE : Boolean.FALSE);

                    // change root node group
                    changeRootNodeGroup(facRegRoot, isCf, isUcf);

                    // change BAT node group
                    if (isUcf) {
                        NodeGroup batGroup = (NodeGroup) facRegRoot.getNode(NODE_NAME_FAC_BAT_INFO);
                        changeBatNodeGroup(batGroup, selectionDto);
                    }

                    // set selected value in the dashboard
                    ParamUtil.setSessionAttr(request, KEY_SELECTED_CLASSIFICATION, selectionDto.getFacClassification());
                    ParamUtil.setSessionAttr(request, KEY_SELECTED_ACTIVITIES, new ArrayList<>(selectionDto.getActivityTypes()));

                    // jump
                    jump(request, facRegRoot, actionValue);
                } else {
                    ParamUtil.setRequestAttr(request, KEY_SHOW_ERROR_SWITCH, Boolean.TRUE);
                    ParamUtil.setSessionAttr(request, KEY_JUMP_DEST_NODE, NODE_NAME_FAC_SELECTION);
                }
            } else if (KEY_NAV_PREVIOUS.equals(actionValue)) {
                jump(request, facRegRoot, actionValue);
            } else {
                throw new IaisRuntimeException(ERR_MSG_INVALID_ACTION);
            }
        } else {
            throw new IaisRuntimeException(ERR_MSG_INVALID_ACTION);
        }
        ParamUtil.setSessionAttr(request, KEY_ROOT_NODE_GROUP, facRegRoot);
    }

    public void preCompInfo(BaseProcessClass bpc) {
        // do nothing now, need to prepare company info in the future
    }

    public void handleCompInfo(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        NodeGroup facRegRoot = getFacilityRegisterRoot(request);
        Node compInfoNode = facRegRoot.getNode(NODE_NAME_COMPANY_INFO);
        String actionType = ParamUtil.getString(request, KEY_ACTION_TYPE);
        if (KEY_ACTION_JUMP.equals(actionType)) {
            Nodes.passValidation(facRegRoot, NODE_NAME_COMPANY_INFO);
            jumpHandler(request, facRegRoot, NODE_NAME_COMPANY_INFO, compInfoNode);
        } else if (KEY_ACTION_SAVE_AS_DRAFT.equals(actionType)) {
            ParamUtil.setRequestAttr(request, KEY_ACTION_TYPE, KEY_ACTION_SAVE_AS_DRAFT);
            ParamUtil.setSessionAttr(request, KEY_JUMP_DEST_NODE, NODE_NAME_COMPANY_INFO);
        } else {
            throw new IaisRuntimeException(ERR_MSG_INVALID_ACTION);
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

        ParamUtil.setRequestAttr(request, KEY_OPTIONS_FAC_TYPE, MasterCodeHolder.FACILITY_TYPE.allOptions());
        ParamUtil.setRequestAttr(request, KEY_OPTIONS_ADDRESS_TYPE, MasterCodeHolder.ADDRESS_TYPE.allOptions());
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
        facAuthDto.setProtectedPlace(facProfileDto.getFacilityProtected());

        String actionType = ParamUtil.getString(request, KEY_ACTION_TYPE);
        if (KEY_ACTION_JUMP.equals(actionType)) {
            jumpHandler(request, facRegRoot, currentNodePath, facProfileNode);
        } else if (KEY_ACTION_SAVE_AS_DRAFT.equals(actionType)) {
            ParamUtil.setRequestAttr(request, KEY_ACTION_TYPE, KEY_ACTION_SAVE_AS_DRAFT);
            ParamUtil.setSessionAttr(request, KEY_JUMP_DEST_NODE, currentNodePath);
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

        ParamUtil.setRequestAttr(request, KEY_OPTIONS_NATIONALITY, MasterCodeHolder.NATIONALITY.allOptions());
        ParamUtil.setRequestAttr(request,KEY_OPTION_SALUTATION, MasterCodeHolder.SALUTATION.allOptions());
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
        } else if (KEY_ACTION_SAVE_AS_DRAFT.equals(actionType)) {
            ParamUtil.setRequestAttr(request, KEY_ACTION_TYPE, KEY_ACTION_SAVE_AS_DRAFT);
            ParamUtil.setSessionAttr(request, KEY_JUMP_DEST_NODE, currentNodePath);
        } else {
            throw new IaisRuntimeException(ERR_MSG_INVALID_ACTION);
        }
        ParamUtil.setSessionAttr(request, KEY_ROOT_NODE_GROUP, facRegRoot);
    }


    public void preFacAdminOfficer(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        NodeGroup facRegRoot = getFacilityRegisterRoot(request);
        String currentNodePath = NODE_NAME_FAC_INFO + facRegRoot.getPathSeparator() + NODE_NAME_FAC_ADMIN_OFFICER;
        SimpleNode facAdminNode = (SimpleNode) facRegRoot.at(currentNodePath);
        FacilityAdminAndOfficerDto adminOfficerDto = (FacilityAdminAndOfficerDto) facAdminNode.getValue();
        Boolean needShowError = (Boolean) ParamUtil.getRequestAttr(request, KEY_SHOW_ERROR_SWITCH);
        if (needShowError == Boolean.TRUE) {
            ParamUtil.setRequestAttr(request, KEY_VALIDATION_ERRORS, adminOfficerDto.retrieveValidationResult());
        }
        Nodes.needValidation(facRegRoot, currentNodePath);
        ParamUtil.setRequestAttr(request, NODE_NAME_FAC_ADMIN_OFFICER, adminOfficerDto);

        ParamUtil.setRequestAttr(request, KEY_OPTIONS_NATIONALITY, MasterCodeHolder.NATIONALITY.allOptions());
        ParamUtil.setRequestAttr(request,KEY_OPTION_SALUTATION, MasterCodeHolder.SALUTATION.allOptions());
    }

    public void handleFacAdminOfficer(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        NodeGroup facRegRoot = getFacilityRegisterRoot(request);
        String currentNodePath = NODE_NAME_FAC_INFO + facRegRoot.getPathSeparator() + NODE_NAME_FAC_ADMIN_OFFICER;
        SimpleNode facAdminOfficerNode = (SimpleNode) facRegRoot.at(currentNodePath);
        FacilityAdminAndOfficerDto facAdminOfficerDto = (FacilityAdminAndOfficerDto) facAdminOfficerNode.getValue();
        facAdminOfficerDto.reqObjMapping(request);

        String actionType = ParamUtil.getString(request, KEY_ACTION_TYPE);
        if (KEY_ACTION_JUMP.equals(actionType)) {
            jumpHandler(request, facRegRoot, currentNodePath, facAdminOfficerNode);
        } else if (KEY_ACTION_SAVE_AS_DRAFT.equals(actionType)) {
            ParamUtil.setRequestAttr(request, KEY_ACTION_TYPE, KEY_ACTION_SAVE_AS_DRAFT);
            ParamUtil.setSessionAttr(request, KEY_JUMP_DEST_NODE, currentNodePath);
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
        if (facCommitteeNode.isValidated()) {
            ParamUtil.setRequestAttr(request, KEY_VALID_DATA_FILE, Boolean.TRUE);
        }
        /* If DTO contains data error, we don't convert error msg to JSON. Or rather, we retrieve the error map,
         * and then render a table. */
        if (facCommitteeDto.isDataErrorExists()) {
            List<ValidationListResultUnit> resultUnitList = ValidationListResultUnit.fromDateErrorMap(facCommitteeDto.getValidationResultDto());
            ParamUtil.setRequestAttr(request, KEY_DATA_ERRORS, resultUnitList);
            ParamUtil.setRequestAttr(request, KEY_ERROR_IN_DATA_FILE, Boolean.TRUE);
        } else {
            Boolean needShowError = (Boolean) ParamUtil.getRequestAttr(request, KEY_SHOW_ERROR_SWITCH);
            if (needShowError == Boolean.TRUE) {
                ParamUtil.setRequestAttr(request, KEY_VALIDATION_ERRORS, facCommitteeDto.retrieveValidationResult());
            }
        }
        ParamUtil.setRequestAttr(request, NODE_NAME_FAC_COMMITTEE, facCommitteeDto);
    }

    public void handleFacInfoCommittee(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        NodeGroup facRegRoot = getFacilityRegisterRoot(request);
        String currentNodePath = NODE_NAME_FAC_INFO + facRegRoot.getPathSeparator() + NODE_NAME_FAC_COMMITTEE;
        SimpleNode facCommitteeNode = (SimpleNode) facRegRoot.at(currentNodePath);
        FacilityCommitteeDto facCommitteeDto = (FacilityCommitteeDto) facCommitteeNode.getValue();

        String actionType = ParamUtil.getString(request, KEY_ACTION_TYPE);

        if (KEY_ACTION_LOAD_DATA_FILE.equals(actionType)) {
            /* Do not change validated to false when jump to this node, because user can not change data in current
             * form except for upload a new file.
             * When user upload a file, we immediately clear data in the DTO, and make the node need validation. */
            Nodes.needValidation(facRegRoot, currentNodePath);
            /* We do many things in the 'if' condition expression:
             * 1, we call validation of the file type, size etc. metadata info.
             * 2, if pass, we convert dat in the file to DTO list.
             * 3, if success, we call validation of the DTOs.
             * If any step fails, it will set the ValidationResultDto properly, so in the 'pre' step, this error msg
             * will be supplied to applicant.
             */
            facCommitteeDto.reqObjMapping(request);
            if (!facCommitteeDto.validateDataFile() || !facCommitteeDto.loadFileData() || !facCommitteeDto.doValidation()) {
                ParamUtil.setRequestAttr(request, KEY_SHOW_ERROR_SWITCH, Boolean.TRUE);
            } else {
                // data are valid
                Nodes.passValidation(facRegRoot, currentNodePath);
            }
            ParamUtil.setRequestAttr(request, KEY_ACTION_TYPE, KEY_ACTION_JUMP);
            ParamUtil.setSessionAttr(request, KEY_JUMP_DEST_NODE, currentNodePath);
        } else if (KEY_ACTION_DELETE_DATA_FILE.equals(actionType)) {
            facCommitteeDto.deleteDataFile();
            Nodes.needValidation(facRegRoot, currentNodePath);
            ParamUtil.setRequestAttr(request, KEY_ACTION_TYPE, KEY_ACTION_JUMP);
            ParamUtil.setSessionAttr(request, KEY_JUMP_DEST_NODE, currentNodePath);
        } else if (KEY_ACTION_EXPAND_FILE.equals(actionType)) {
            if (!facCommitteeNode.isValidated()) {
                throw new IllegalStateException("Invalid, can not expand");
            }
            List<FacilityCommitteeFileDto> dataList = facCommitteeDto.getDataListForDisplay();
            ParamUtil.setRequestAttr(request, KEY_DATA_LIST, dataList);
            ParamUtil.setRequestAttr(request, KEY_ACTION_TYPE, KEY_ACTION_JUMP);
            ParamUtil.setSessionAttr(request, KEY_JUMP_DEST_NODE, STEP_NAME_COMMITTEE_PREVIEW);
        } else if (KEY_ACTION_JUMP.equals(actionType)) {
            jumpHandler(request, facRegRoot, currentNodePath, facCommitteeNode);
        } else if (KEY_ACTION_SAVE_AS_DRAFT.equals(actionType)) {
            ParamUtil.setRequestAttr(request, KEY_ACTION_TYPE, KEY_ACTION_SAVE_AS_DRAFT);
            ParamUtil.setSessionAttr(request, KEY_JUMP_DEST_NODE, currentNodePath);
        } else {
            throw new IaisRuntimeException(ERR_MSG_INVALID_ACTION);
        }
        ParamUtil.setSessionAttr(request, KEY_ROOT_NODE_GROUP, facRegRoot);
    }

    public void prePreview(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        String srcPath = ParamUtil.getString(request, KEY_ACTION_ADDITIONAL);
        ParamUtil.setRequestAttr(request, KEY_SOURCE_NODE_PATH, srcPath);
    }

    public void handlePreview(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        NodeGroup facRegRoot = getFacilityRegisterRoot(request);
        String actionType = ParamUtil.getString(request, KEY_ACTION_TYPE);
        if (KEY_ACTION_JUMP.equals(actionType)) {
            jumpHandler(request, facRegRoot, null, null);
        } else {
            throw new IaisRuntimeException(ERR_MSG_INVALID_ACTION);
        }
    }

    public void preFacInfoAuthoriser(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        NodeGroup facRegRoot = getFacilityRegisterRoot(request);
        String currentNodePath = NODE_NAME_FAC_INFO + facRegRoot.getPathSeparator() + NODE_NAME_FAC_AUTH;
        SimpleNode facAuthNode = (SimpleNode) facRegRoot.at(currentNodePath);
        FacilityAuthoriserDto facAuthDto = (FacilityAuthoriserDto) facAuthNode.getValue();
        if (facAuthNode.isValidated()) {
            ParamUtil.setRequestAttr(request, KEY_VALID_DATA_FILE, Boolean.TRUE);
        }
        /* If DTO contains data error, we don't convert error msg to JSON. Or rather, we retrieve the error map,
         * and then render a table. */
        if (facAuthDto.isDataErrorExists()) {
            List<ValidationListResultUnit> resultUnitList = ValidationListResultUnit.fromDateErrorMap(facAuthDto.getValidationResultDto());
            ParamUtil.setRequestAttr(request, KEY_DATA_ERRORS, resultUnitList);
            ParamUtil.setRequestAttr(request, KEY_ERROR_IN_DATA_FILE, Boolean.TRUE);
        } else {
            Boolean needShowError = (Boolean) ParamUtil.getRequestAttr(request, KEY_SHOW_ERROR_SWITCH);
            if (needShowError == Boolean.TRUE) {
                ParamUtil.setRequestAttr(request, KEY_VALIDATION_ERRORS, facAuthDto.retrieveValidationResult());
            }
        }

        ParamUtil.setRequestAttr(request, NODE_NAME_FAC_AUTH, facAuthDto);
    }

    public void handleFacInfoAuthoriser(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        NodeGroup facRegRoot = getFacilityRegisterRoot(request);
        String currentNodePath = NODE_NAME_FAC_INFO + facRegRoot.getPathSeparator() + NODE_NAME_FAC_AUTH;
        SimpleNode facAuthNode = (SimpleNode) facRegRoot.at(currentNodePath);
        FacilityAuthoriserDto facAuthDto = (FacilityAuthoriserDto) facAuthNode.getValue();
        facAuthDto.reqObjMapping(request);

        String actionType = ParamUtil.getString(request, KEY_ACTION_TYPE);

        if (KEY_ACTION_LOAD_DATA_FILE.equals(actionType)) {
            /* Do not change validated to false when jump to this node, because user can not change data in current
             * form except for upload a new file.
             * When user upload a file, we immediately clear data in the DTO, and make the node need validation. */
            Nodes.needValidation(facRegRoot, currentNodePath);
            /* We do many things in the 'if' condition expression:
             * 1, we call validation of the file type, size etc. metadata info.
             * 2, if pass, we convert dat in the file to DTO list.
             * 3, if success, we call validation of the DTOs.
             * If any step fails, it will set the ValidationResultDto properly, so in the 'pre' step, this error msg
             * will be supplied to applicant.
             */
            if (!facAuthDto.validateDataFile() || !facAuthDto.loadFileData() || !facAuthDto.doValidation()) {
                ParamUtil.setRequestAttr(request, KEY_SHOW_ERROR_SWITCH, Boolean.TRUE);
            } else {
                // data are valid
                Nodes.passValidation(facRegRoot, currentNodePath);
            }
            ParamUtil.setRequestAttr(request, KEY_ACTION_TYPE, KEY_ACTION_JUMP);
            ParamUtil.setSessionAttr(request, KEY_JUMP_DEST_NODE, currentNodePath);
        } else if (KEY_ACTION_DELETE_DATA_FILE.equals(actionType)) {
            facAuthDto.deleteDataFile();
            Nodes.needValidation(facRegRoot, currentNodePath);
            ParamUtil.setRequestAttr(request, KEY_ACTION_TYPE, KEY_ACTION_JUMP);
            ParamUtil.setSessionAttr(request, KEY_JUMP_DEST_NODE, currentNodePath);
        } else if (KEY_ACTION_EXPAND_FILE.equals(actionType)) {
            if (!facAuthNode.isValidated()) {
                throw new IllegalStateException("Invalid, can not expand");
            }
            List<FacilityAuthoriserFileDto> dataList = facAuthDto.getDataListForDisplay();
            ParamUtil.setRequestAttr(request, KEY_DATA_LIST, dataList);
            ParamUtil.setRequestAttr(request, KEY_ACTION_TYPE, KEY_ACTION_JUMP);
            ParamUtil.setSessionAttr(request, KEY_JUMP_DEST_NODE, STEP_NAME_AUTHORISER_PREVIEW);
        } else if (KEY_ACTION_JUMP.equals(actionType)) {
            jumpHandler(request, facRegRoot, currentNodePath, facAuthNode);
        } else if (KEY_ACTION_SAVE_AS_DRAFT.equals(actionType)) {
            ParamUtil.setRequestAttr(request, KEY_ACTION_TYPE, KEY_ACTION_SAVE_AS_DRAFT);
            ParamUtil.setSessionAttr(request, KEY_JUMP_DEST_NODE, currentNodePath);
        } else {
            throw new IaisRuntimeException(ERR_MSG_INVALID_ACTION);
        }
        ParamUtil.setSessionAttr(request, KEY_ROOT_NODE_GROUP, facRegRoot);
    }

    @SneakyThrows(JsonProcessingException.class)
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

        Map<String, List<BatBasicInfo>> scheduleBatMap = facRegClient.queryScheduleBasedBatBasicInfo(batDto.getActivityType());
        List<SelectOption> scheduleTypeOps = MasterCodeHolder.SCHEDULE.customOptions(scheduleBatMap.keySet().toArray(new String[0]));
        ParamUtil.setRequestAttr(request, KEY_OPTIONS_SCHEDULE, scheduleTypeOps);
        ParamUtil.setRequestAttr(request, KEY_SCHEDULE_FIRST_OPTION, scheduleTypeOps.get(0).getValue());

        // convert BatBasicInfo to SelectOption object
        Map<String, List<SelectOption>> scheduleBatOptionMap = Maps.newHashMapWithExpectedSize(scheduleBatMap.size());
        for (Map.Entry<String, List<BatBasicInfo>> entry : scheduleBatMap.entrySet()) {
            List<SelectOption> optionList = new ArrayList<>(entry.getValue().size() + 1);
            optionList.add(new SelectOption("", "Please Select"));
            for (BatBasicInfo info : entry.getValue()) {
                SelectOption option = new SelectOption();
                option.setText(info.getName());
                option.setValue(info.getId());
                optionList.add(option);
            }
            scheduleBatOptionMap.put(entry.getKey(), optionList);
        }
        ParamUtil.setRequestAttr(request, KEY_SCHEDULE_BAT_MAP, scheduleBatOptionMap);
        ObjectMapper mapper = new ObjectMapper();
        String scheduleBatMapJson = mapper.writeValueAsString(scheduleBatOptionMap);
        ParamUtil.setRequestAttr(request, KEY_SCHEDULE_BAT_MAP_JSON, scheduleBatMapJson);

        ParamUtil.setRequestAttr(request, KEY_OPTIONS_ADDRESS_TYPE, MasterCodeHolder.ADDRESS_TYPE.allOptions());
        ParamUtil.setRequestAttr(request, KEY_OPTIONS_NATIONALITY, MasterCodeHolder.NATIONALITY.allOptions());
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
        } else if (KEY_ACTION_SAVE_AS_DRAFT.equals(actionType)) {
            ParamUtil.setRequestAttr(request, KEY_ACTION_TYPE, KEY_ACTION_SAVE_AS_DRAFT);
            ParamUtil.setSessionAttr(request, KEY_JUMP_DEST_NODE, currentNodePath);
        } else {
            throw new IaisRuntimeException(ERR_MSG_INVALID_ACTION);
        }
        ParamUtil.setSessionAttr(request, KEY_ROOT_NODE_GROUP, facRegRoot);
    }

    public void tryLoadOtherAppDeclaration(OtherApplicationInfoDto otherAppInfoDto) {
        if (otherAppInfoDto.isConfigNotLoaded()) {
            if (StringUtils.hasLength(otherAppInfoDto.getDeclarationId())) {
                List<DeclarationItemMainInfo> declarationConfig = facRegClient.getDeclarationConfigInfoById(otherAppInfoDto.getDeclarationId());
                otherAppInfoDto.setDeclarationConfig(declarationConfig);
            } else {
                DeclarationConfigInfo configInfo = facRegClient.getDeclarationConfigBySpecificType(DECLARATION_TYPE, DECLARATION_SUB_TYPE);
                otherAppInfoDto.setDeclarationId(configInfo.getId());
                otherAppInfoDto.setDeclarationConfig(configInfo.getConfig());
            }
        }
    }

    public void preOtherAppInfo(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        NodeGroup facRegRoot = getFacilityRegisterRoot(request);
        SimpleNode otherAppInfoNode = (SimpleNode) facRegRoot.at(NODE_NAME_OTHER_INFO);
        OtherApplicationInfoDto otherAppInfoDto = (OtherApplicationInfoDto) otherAppInfoNode.getValue();
        // load declaration
        tryLoadOtherAppDeclaration(otherAppInfoDto);
        Boolean needShowError = (Boolean) ParamUtil.getRequestAttr(request, KEY_SHOW_ERROR_SWITCH);
        if (needShowError == Boolean.TRUE) {
            Map<String, String> errorMap = otherAppInfoDto.getValidationResultDto().getErrorMap();
            if (errorMap.containsKey("ID") || errorMap.containsKey("DTO")) {
                throw new IllegalStateException();
            } else {
                ParamUtil.setRequestAttr(request, KEY_DECLARATION_ERROR_MAP, errorMap);
            }
        }
        Nodes.needValidation(facRegRoot, NODE_NAME_OTHER_INFO);

        ParamUtil.setRequestAttr(request, KEY_DECLARATION_CONFIG, otherAppInfoDto.getDeclarationConfig());
        ParamUtil.setRequestAttr(request, KEY_DECLARATION_ANSWER_MAP, otherAppInfoDto.getAnswerMap());
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
        } else if (KEY_ACTION_SAVE_AS_DRAFT.equals(actionType)) {
            ParamUtil.setRequestAttr(request, KEY_ACTION_TYPE, KEY_ACTION_SAVE_AS_DRAFT);
            ParamUtil.setSessionAttr(request, KEY_JUMP_DEST_NODE, NODE_NAME_OTHER_INFO);
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

        ParamUtil.setRequestAttr(request, "docSettings", docSettingService.getFacRegDocSettings());

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
        } else if (KEY_ACTION_SAVE_AS_DRAFT.equals(actionType)) {
            ParamUtil.setRequestAttr(request, KEY_ACTION_TYPE, KEY_ACTION_SAVE_AS_DRAFT);
            ParamUtil.setSessionAttr(request, KEY_JUMP_DEST_NODE, NODE_NAME_PRIMARY_DOC);
        } else {
            throw new IaisRuntimeException(ERR_MSG_INVALID_ACTION);
        }
        ParamUtil.setSessionAttr(request, KEY_ROOT_NODE_GROUP, facRegRoot);
    }

    public void preApprovedFacilityCertifier(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        NodeGroup facRegRoot = getFacilityRegisterRoot(request);
        SimpleNode facCertifierNode = (SimpleNode) facRegRoot.getNode(NODE_NAME_AFC);
        FacilityAfcDto facilityCertifierDto = (FacilityAfcDto) facCertifierNode.getValue();
        Boolean needShowError = (Boolean) ParamUtil.getRequestAttr(request, KEY_SHOW_ERROR_SWITCH);
        if (needShowError == Boolean.TRUE) {
            ParamUtil.setRequestAttr(request, KEY_VALIDATION_ERRORS, facilityCertifierDto.retrieveValidationResult());
        }
        Nodes.needValidation(facRegRoot, NODE_NAME_AFC);
        ParamUtil.setRequestAttr(request,NODE_NAME_AFC,facilityCertifierDto);
        ParamUtil.setRequestAttr(request,KEY_OPTIONS_AFC, MasterCodeHolder.APPROVED_FACILITY_CERTIFIER.allOptions());
    }

    public void handleApprovedFacilityCertifier(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        NodeGroup facRegRoot = getFacilityRegisterRoot(request);
        SimpleNode facCertifierNode = (SimpleNode) facRegRoot.getNode(NODE_NAME_AFC);
        FacilityAfcDto facilityCertifierDto = (FacilityAfcDto) facCertifierNode.getValue();
        facilityCertifierDto.reqObjectMapping(request);

        String actionType = ParamUtil.getString(request, KEY_ACTION_TYPE);
        if (KEY_ACTION_JUMP.equals(actionType)) {
            jumpHandler(request, facRegRoot, NODE_NAME_AFC, facCertifierNode);
        } else if (KEY_ACTION_SAVE_AS_DRAFT.equals(actionType)) {
            ParamUtil.setRequestAttr(request, KEY_ACTION_TYPE, KEY_ACTION_SAVE_AS_DRAFT);
            ParamUtil.setSessionAttr(request, KEY_JUMP_DEST_NODE, NODE_NAME_AFC);
        } else {
            throw new IaisRuntimeException(ERR_MSG_INVALID_ACTION);
        }
        ParamUtil.setSessionAttr(request, KEY_ROOT_NODE_GROUP, facRegRoot);

    }

    public void prePreviewSubmit(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        preparePreviewData(request);
    }

    public void preparePreviewData(HttpServletRequest request) {
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
        ParamUtil.setRequestAttr(request, NODE_NAME_FAC_ADMIN_OFFICER, ((SimpleNode)facRegRoot.at(NODE_NAME_FAC_INFO + facRegRoot.getPathSeparator() + NODE_NAME_FAC_ADMIN_OFFICER)).getValue());
        ParamUtil.setRequestAttr(request, NODE_NAME_FAC_COMMITTEE, ((SimpleNode)facRegRoot.at(NODE_NAME_FAC_INFO + facRegRoot.getPathSeparator() + NODE_NAME_FAC_COMMITTEE)).getValue());

        boolean isCf = (boolean) ParamUtil.getSessionAttr(request, KEY_IS_CF);
        boolean isUcf = (boolean) ParamUtil.getSessionAttr(request, KEY_IS_UCF);
        if (isCf) {
            ParamUtil.setRequestAttr(request, NODE_NAME_AFC, ((SimpleNode) facRegRoot.at(NODE_NAME_AFC)).getValue());
        } else if (isUcf) {
            NodeGroup batNodeGroup = (NodeGroup) facRegRoot.at(NODE_NAME_FAC_BAT_INFO);
            List<BiologicalAgentToxinDto> batList = FacilityRegistrationService.getBatInfoList(batNodeGroup);
            ParamUtil.setRequestAttr(request, "batList", batList);
        }

        OtherApplicationInfoDto otherAppInfoDto = (OtherApplicationInfoDto) ((SimpleNode) facRegRoot.at(NODE_NAME_OTHER_INFO)).getValue();
        // load declaration
        tryLoadOtherAppDeclaration(otherAppInfoDto);
        ParamUtil.setRequestAttr(request, KEY_DECLARATION_CONFIG, otherAppInfoDto.getDeclarationConfig());
        ParamUtil.setRequestAttr(request, KEY_DECLARATION_ANSWER_MAP, otherAppInfoDto.getAnswerMap());

        ParamUtil.setRequestAttr(request, "docSettings", docSettingService.getFacRegDocSettings());
        PrimaryDocDto primaryDocDto = (PrimaryDocDto) ((SimpleNode)facRegRoot.at(NODE_NAME_PRIMARY_DOC)).getValue();
        Map<String, List<DocRecordInfo>> savedFiles = primaryDocDto.getExistDocTypeMap();
        Map<String, List<NewDocInfo>> newFiles = primaryDocDto.getNewDocTypeMap();
        ParamUtil.setRequestAttr(request, "savedFiles", savedFiles);
        ParamUtil.setRequestAttr(request, "newFiles", newFiles);
    }

    public void preAcknowledge(BaseProcessClass bpc) {
        //do nothing now
    }

    public void actionFilter(BaseProcessClass bpc, String appType) {
        HttpServletRequest request = bpc.request;
        // check if there is action set to override the action from request
        String actionType = (String) ParamUtil.getRequestAttr(request, KEY_ACTION_TYPE);
        if (!StringUtils.hasLength(actionType)) {
            // not set, use action from user's client
            actionType = ParamUtil.getString(request, KEY_ACTION_TYPE);
        } else {
            // set, if the action is 'save draft', we save it and route back to that page
            if (KEY_ACTION_SAVE_AS_DRAFT.equals(actionType)) {
                actionType = KEY_ACTION_JUMP;
                saveDraft(request, appType);
            }
        }
        ParamUtil.setRequestAttr(request, KEY_INDEED_ACTION_TYPE, actionType);
    }

    public void jumpFilter(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        String destNode = (String) ParamUtil.getSessionAttr(request, KEY_JUMP_DEST_NODE);
        destNode = batNodeSpecialHandle(destNode);
        ParamUtil.setRequestAttr(request, KEY_DEST_NODE_ROUTE, destNode);
    }


    /** Save new uploaded documents into FE file repo.
     * @param primaryDocDto document DTO have the specific structure
     * @return a list of DTOs can be used to sync to BE
     */
    public List<NewFileSyncDto> saveNewUploadedDoc(PrimaryDocDto primaryDocDto) {
        List<NewFileSyncDto> newFilesToSync;
        if (!primaryDocDto.getNewDocMap().isEmpty()) {
            MultipartFile[] files = primaryDocDto.getNewDocMap().values().stream().map(NewDocInfo::getMultipartFile).toArray(MultipartFile[]::new);
            List<String> repoIds = fileRepoClient.saveFiles(files).getEntity();
            newFilesToSync = primaryDocDto.newFileSaved(repoIds);
        } else {
            newFilesToSync = new ArrayList<>(0);
        }
        return newFilesToSync;
    }

    /** Save new uploaded documents of facility profile
     * @see #saveNewUploadedDoc(PrimaryDocDto)
     */
    public List<NewFileSyncDto> saveProfileNewUploadedDoc(FacilityProfileDto profileDto) {
        List<NewFileSyncDto> newFilesToSync;
        if (!profileDto.getNewDocMap().isEmpty()) {
            MultipartFile[] files = profileDto.getNewDocMap().values().stream().map(NewDocInfo::getMultipartFile).toArray(MultipartFile[]::new);
            List<String> repoIds = fileRepoClient.saveFiles(files).getEntity();
            newFilesToSync = profileDto.newFileSaved(repoIds);
        } else {
            newFilesToSync = new ArrayList<>(0);
        }
        return newFilesToSync;
    }

    public NewFileSyncDto saveCommitteeNewDataFile(FacilityCommitteeDto committeeDto) {
        if (committeeDto.getNewFile() != null) {
            MultipartFile file = committeeDto.getNewFile().getMultipartFile();
            String repoId = fileRepoClient.saveFiles(new MultipartFile[]{file}).getEntity().get(0);
            return committeeDto.newFileSaved(repoId);
        } else {
            return null;
        }
    }

    public NewFileSyncDto saveAuthoriserNewDataFile(FacilityAuthoriserDto authoriserDto) {
        if (authoriserDto.getNewFile() != null) {
            MultipartFile file = authoriserDto.getNewFile().getMultipartFile();
            String repoId = fileRepoClient.saveFiles(new MultipartFile[]{file}).getEntity().get(0);
            return authoriserDto.newFileSaved(repoId);
        } else {
            return null;
        }
    }


    /** Delete unwanted documents in FE file repo.
     * This method will clear deleted files in DTO too.
     * @param refInDto reference of the toBeDeletedRepoIds in DTO
     * @return a list of repo IDs deleted in FE file repo */
    public List<String> deleteUnwantedDoc(Set<String> refInDto) {
        List<String> toBeDeletedRepoIds = new ArrayList<>(refInDto);
        for (String id: toBeDeletedRepoIds) {
            FileRepoDto fileRepoDto = new FileRepoDto();
            fileRepoDto.setId(id);
            fileRepoClient.removeFileById(fileRepoDto);
            refInDto.remove(id);
        }
        return toBeDeletedRepoIds;
    }

    /** Sync new uploaded documents to BE; delete unwanted documents in BE too.
     * @param newFilesToSync a list of DTOs contains ID and data
     * @param toBeDeletedRepoIds a list of repo IDs to be deleted in BE
     */
    public void syncNewDocsAndDeleteFiles(List<NewFileSyncDto> newFilesToSync, List<String> toBeDeletedRepoIds) {
        // sync files to BE file-repo (save new added files, delete useless files)
        if (!CollectionUtils.isEmpty(newFilesToSync) || !CollectionUtils.isEmpty(toBeDeletedRepoIds)) {
            /* Ignore the failure of sync files currently.
             * We should add a mechanism to retry synchronization of files in the future */
            FileRepoSyncDto syncDto = new FileRepoSyncDto();
            syncDto.setNewFiles(newFilesToSync);
            syncDto.setToDeleteIds(toBeDeletedRepoIds);
            bsbFileClient.saveFiles(syncDto);
        }
    }

    public void saveDraft(HttpServletRequest request, String appType) {
        NodeGroup facRegRoot = getFacilityRegisterRoot(request);

        // save docs
        PrimaryDocDto primaryDocDto = (PrimaryDocDto) ((SimpleNode) facRegRoot.at(NODE_NAME_PRIMARY_DOC)).getValue();
        List<NewFileSyncDto> primaryDocNewFiles = saveNewUploadedDoc(primaryDocDto);
        FacilityProfileDto profileDto = (FacilityProfileDto) ((SimpleNode) facRegRoot.at(NODE_NAME_FAC_INFO + facRegRoot.getPathSeparator() + NODE_NAME_FAC_PROFILE)).getValue();
        List<NewFileSyncDto> profileNewFiles = saveProfileNewUploadedDoc(profileDto);
        FacilityCommitteeDto committeeDto = (FacilityCommitteeDto) ((SimpleNode) facRegRoot.at(NODE_NAME_FAC_INFO + facRegRoot.getPathSeparator() + NODE_NAME_FAC_COMMITTEE)).getValue();
        NewFileSyncDto committeeNewFile = saveCommitteeNewDataFile(committeeDto);
        FacilityAuthoriserDto authDto = (FacilityAuthoriserDto) ((SimpleNode) facRegRoot.at(NODE_NAME_FAC_INFO + facRegRoot.getPathSeparator() + NODE_NAME_FAC_AUTH)).getValue();
        NewFileSyncDto authoriserNewFile = saveAuthoriserNewDataFile(authDto);
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
        FacilityRegisterDto finalAllDataDto = null;
        if (appType.equals(MasterCodeConstants.APP_TYPE_NEW) || appType.equals(MasterCodeConstants.APP_TYPE_RFC)){
            finalAllDataDto = FacilityRegisterDto.from(facRegRoot);
            finalAllDataDto.setAppType(appType);
        }else if (appType.equals(MasterCodeConstants.APP_TYPE_RENEW)){
            NodeGroup viewApprovalRoot = (NodeGroup) ParamUtil.getSessionAttr(request, KEY_RENEWAL_VIEW_APPROVAL_ROOT_NODE_GROUP);
            finalAllDataDto = FacilityRegisterDto.fromRenewal(viewApprovalRoot, facRegRoot);
            finalAllDataDto.setAppType(appType);
        }
        String draftAppNo = facRegClient.saveNewFacilityDraft(finalAllDataDto);
        // set draft app No. into the NodeGroup
        FacilitySelectionDto selectionDto = (FacilitySelectionDto) ((SimpleNode) facRegRoot.at(NODE_NAME_FAC_SELECTION)).getValue();
        selectionDto.setDraftAppNo(draftAppNo);
        ParamUtil.setSessionAttr(request, KEY_ROOT_NODE_GROUP, facRegRoot);

        try {
            // delete docs
            log.info("Delete already saved documents in file-repo");
            List<String> primaryToBeDeletedRepoIds = deleteUnwantedDoc(primaryDocDto.getToBeDeletedRepoIds());
            List<String> profileToBeDeletedRepoIds = deleteUnwantedDoc(profileDto.getToBeDeletedRepoIds());
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
            syncNewDocsAndDeleteFiles(newFilesToSync, toBeDeletedRepoIds);
        } catch (Exception e) {
            log.error("Fail to sync files to BE", e);
        }

        ParamUtil.setRequestAttr(request, KEY_IND_AFTER_SAVE_AS_DRAFT, Boolean.TRUE);
    }


    /**
     * Get the root data structure of this flow
     */
    public NodeGroup getFacilityRegisterRoot(HttpServletRequest request) {
        NodeGroup root = (NodeGroup) ParamUtil.getSessionAttr(request, KEY_ROOT_NODE_GROUP);
        if (root == null) {
            root = initFacRegisterRoot(KEY_ROOT_NODE_GROUP);
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
            case KEY_NAV_PREVIOUS:
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
     * Common actions when we do 'jump', this method take 'next' as higher priority.
     * We always to validation first for the 'next' action.
     * @see #jump(HttpServletRequest, NodeGroup, String)
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
            jump(request, facRegRoot, actionValue);
        } else {
            ParamUtil.setRequestAttr(request, KEY_SHOW_ERROR_SWITCH, Boolean.TRUE);
            ParamUtil.setSessionAttr(request, KEY_JUMP_DEST_NODE, currentPath);
        }
    }

    /**
     * Jump according to action value.
     * This method will compute the wanted destination by NodeGroup and actionValue.
     * Then call {@link Nodes#jump(NodeGroup, String)} to check if we can reach there.
     * Finally, set the destination node in attribute, and if the does not equal to our idea, set an error flag too.
     * @param actionValue next, previous or node path
     */
    public void jump(HttpServletRequest request, NodeGroup facRegRoot, String actionValue) {
        String destNode = computeDestNodePath(facRegRoot, actionValue);
        String checkedDestNode = Nodes.jump(facRegRoot, destNode);
        if (!checkedDestNode.equals(destNode)) {
            ParamUtil.setRequestAttr(request, KEY_SHOW_ERROR_SWITCH, Boolean.TRUE);
        }
        ParamUtil.setSessionAttr(request, KEY_JUMP_DEST_NODE, checkedDestNode);
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

    /* This method only generate first two nodes, because following nodes will be determined by the selection */
    public static NodeGroup initFacRegisterRoot(String name) {
        Node beforeBeginNode = new Node(NODE_NAME_BEFORE_BEGIN, new Node[0]);
        SimpleNode facSelectionNode = new SimpleNode(new FacilitySelectionDto(), NODE_NAME_FAC_SELECTION, new Node[0]);

        return new NodeGroup.Builder().name(name)
                .addNode(beforeBeginNode)
                .addNode(facSelectionNode)
                .build();
    }

    public static NodeGroup newFacInfoNodeGroup(Node[] dependNodes) {
        SimpleNode facProfileNode = new SimpleNode(new FacilityProfileDto(), NODE_NAME_FAC_PROFILE, new Node[0]);
        SimpleNode facOperatorNode = new SimpleNode(new FacilityOperatorDto(), NODE_NAME_FAC_OPERATOR, new Node[]{facProfileNode});
        SimpleNode facAdminOfficerNode = new SimpleNode(new FacilityAdminAndOfficerDto(), NODE_NAME_FAC_ADMIN_OFFICER, new Node[]{facProfileNode, facOperatorNode});
        SimpleNode facCommitteeNode = new SimpleNode(new FacilityCommitteeDto(), NODE_NAME_FAC_COMMITTEE, new Node[]{facProfileNode, facOperatorNode, facAdminOfficerNode});
        SimpleNode facAuthNode = new SimpleNode(new FacilityAuthoriserDto(), NODE_NAME_FAC_AUTH, new Node[]{facProfileNode, facOperatorNode, facAdminOfficerNode, facCommitteeNode});

        return new NodeGroup.Builder().name(NODE_NAME_FAC_INFO)
                .dependNodes(dependNodes)
                .addNode(facProfileNode)
                .addNode(facOperatorNode)
                .addNode(facAdminOfficerNode)
                .addNode(facCommitteeNode)
                .addNode(facAuthNode)
                .build();
    }

    public static NodeGroup initBatNodeGroup(Node[] dependNodes) {
        return new NodeGroup.Builder().name(NODE_NAME_FAC_BAT_INFO)
                .dependNodes(dependNodes)
                .addNode(new Node("error", new Node[0]))
                .build();
    }

    public static NodeGroup certifiedFacilityNodeGroup(String name) {
        Node beforeBeginNode = new Node(NODE_NAME_BEFORE_BEGIN, new Node[0]);
        SimpleNode facSelectionNode = new SimpleNode(new FacilitySelectionDto(), NODE_NAME_FAC_SELECTION, new Node[0]);
        Node companyInfoNode = new Node(NODE_NAME_COMPANY_INFO, new Node[0]);
        NodeGroup facInfoNodeGroup = newFacInfoNodeGroup(new Node[]{facSelectionNode});
        SimpleNode otherAppInfoNode = new SimpleNode(new OtherApplicationInfoDto(), NODE_NAME_OTHER_INFO, new Node[]{facSelectionNode, facInfoNodeGroup});
        SimpleNode supportingDocNode = new SimpleNode(new PrimaryDocDto(), NODE_NAME_PRIMARY_DOC, new Node[]{facSelectionNode, facInfoNodeGroup, otherAppInfoNode});
        SimpleNode afcNode = new SimpleNode(new FacilityAfcDto(), NODE_NAME_AFC, new Node[]{facSelectionNode, facInfoNodeGroup, otherAppInfoNode, supportingDocNode});
        SimpleNode previewSubmitNode = new SimpleNode(new PreviewSubmitDto(), NODE_NAME_PREVIEW_SUBMIT, new Node[]{facSelectionNode, facInfoNodeGroup, otherAppInfoNode, supportingDocNode, afcNode});

        return new NodeGroup.Builder().name(name)
                .addNode(beforeBeginNode)
                .addNode(facSelectionNode)
                .addNode(companyInfoNode)
                .addNode(facInfoNodeGroup)
                .addNode(otherAppInfoNode)
                .addNode(supportingDocNode)
                .addNode(afcNode)
                .addNode(previewSubmitNode)
                .build();
    }

    public static NodeGroup uncertifiedFacilityNodeGroup(String name) {
        Node beforeBeginNode = new Node(NODE_NAME_BEFORE_BEGIN, new Node[0]);
        SimpleNode facSelectionNode = new SimpleNode(new FacilitySelectionDto(), NODE_NAME_FAC_SELECTION, new Node[0]);
        Node companyInfoNode = new Node(NODE_NAME_COMPANY_INFO, new Node[0]);
        NodeGroup facInfoNodeGroup = newFacInfoNodeGroup(new Node[]{facSelectionNode});
        NodeGroup batNodeGroup = initBatNodeGroup(new Node[]{facSelectionNode, facInfoNodeGroup});
        SimpleNode otherAppInfoNode = new SimpleNode(new OtherApplicationInfoDto(), NODE_NAME_OTHER_INFO, new Node[]{facSelectionNode, facInfoNodeGroup, batNodeGroup});
        SimpleNode supportingDocNode = new SimpleNode(new PrimaryDocDto(), NODE_NAME_PRIMARY_DOC, new Node[]{facSelectionNode, facInfoNodeGroup, batNodeGroup, otherAppInfoNode});
        SimpleNode previewSubmitNode = new SimpleNode(new PreviewSubmitDto(), NODE_NAME_PREVIEW_SUBMIT, new Node[]{facSelectionNode, facInfoNodeGroup, batNodeGroup, otherAppInfoNode, supportingDocNode});

        return new NodeGroup.Builder().name(name)
                .addNode(beforeBeginNode)
                .addNode(facSelectionNode)
                .addNode(companyInfoNode)
                .addNode(facInfoNodeGroup)
                .addNode(batNodeGroup)
                .addNode(otherAppInfoNode)
                .addNode(supportingDocNode)
                .addNode(previewSubmitNode)
                .build();
    }

    public static NodeGroup registeredFacilityNodeGroup(String name) {
        Node beforeBeginNode = new Node(NODE_NAME_BEFORE_BEGIN, new Node[0]);
        SimpleNode facSelectionNode = new SimpleNode(new FacilitySelectionDto(), NODE_NAME_FAC_SELECTION, new Node[0]);
        Node companyInfoNode = new Node(NODE_NAME_COMPANY_INFO, new Node[0]);
        NodeGroup facInfoNodeGroup = newFacInfoNodeGroup(new Node[]{facSelectionNode});
        SimpleNode otherAppInfoNode = new SimpleNode(new OtherApplicationInfoDto(), NODE_NAME_OTHER_INFO, new Node[]{facSelectionNode, facInfoNodeGroup});
        SimpleNode supportingDocNode = new SimpleNode(new PrimaryDocDto(), NODE_NAME_PRIMARY_DOC, new Node[]{facSelectionNode, facInfoNodeGroup, otherAppInfoNode});
        SimpleNode previewSubmitNode = new SimpleNode(new PreviewSubmitDto(), NODE_NAME_PREVIEW_SUBMIT, new Node[]{facSelectionNode, facInfoNodeGroup, otherAppInfoNode, supportingDocNode});

        return new NodeGroup.Builder().name(name)
                .addNode(beforeBeginNode)
                .addNode(facSelectionNode)
                .addNode(companyInfoNode)
                .addNode(facInfoNodeGroup)
                .addNode(otherAppInfoNode)
                .addNode(supportingDocNode)
                .addNode(previewSubmitNode)
                .build();
    }

    public static void changeRootNodeGroup(NodeGroup facRegRoot, boolean isCf, boolean isUcf) {
        Assert.notNull(facRegRoot, ERR_MSG_BAT_NOT_NULL);
        NodeGroup newNodeGroup;
        if (isCf) {
            newNodeGroup = certifiedFacilityNodeGroup(facRegRoot.getName());
        } else if (isUcf) {
            newNodeGroup = uncertifiedFacilityNodeGroup(facRegRoot.getName());
        } else {  // RF
            newNodeGroup = registeredFacilityNodeGroup(facRegRoot.getName());
        }
        Node[] subNodes = newNodeGroup.getAllNodes().toArray(new Node[0]);
        facRegRoot.reorganizeNodes(subNodes, NODE_NAME_FAC_SELECTION);
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



    /* Will be removed in future, will get this from master code */
    public static List<SelectOption> tmpPersonnelRoleOps() {
        List<SelectOption> personnelRoleOps = new ArrayList<>(4);
        personnelRoleOps.add(new SelectOption("COMTPRO001", "Senior Management Representative"));
        personnelRoleOps.add(new SelectOption("COMTPRO002", "Biosafety Coordinator"));
        personnelRoleOps.add(new SelectOption("COMTPRO003", "Person in charge of safe and proper functioning of facility and equipment"));
        personnelRoleOps.add(new SelectOption("COMTPRO004", "Other qualified personnel"));
        return personnelRoleOps;
    }


    /**
     * only use to 'rfc' module
     * rfc compare
     */
    public List<DiffContent> compareTwoDto(FacilityRegisterDto oldFacilityRegisterDto, FacilityRegisterDto newFacilityRegisterDto){
        List<DiffContent> diffContentList = new ArrayList<>();
        CompareTwoObject.diff(oldFacilityRegisterDto.getFacilitySelectionDto(), newFacilityRegisterDto.getFacilitySelectionDto(), diffContentList);
        CompareTwoObject.diff(oldFacilityRegisterDto.getFacilityProfileDto(), newFacilityRegisterDto.getFacilityProfileDto(), diffContentList);
        CompareTwoObject.diff(oldFacilityRegisterDto.getFacilityOperatorDto(), newFacilityRegisterDto.getFacilityOperatorDto(), diffContentList);
        CompareTwoObject.diff(oldFacilityRegisterDto.getFacilityAuthoriserDto(), newFacilityRegisterDto.getFacilityAuthoriserDto(), diffContentList, FacilityAuthoriserDto.FacilityAuthorisedPersonnel.class);
//        CompareTwoObject.diff(oldFacilityRegisterDto.getFacilityAdministratorDto(), newFacilityRegisterDto.getFacilityAdministratorDto(), diffContentList, FacilityAdministratorDto.FacilityEmployeeInfo.class);
//        CompareTwoObject.diff(oldFacilityRegisterDto.getFacilityOfficerDto(), newFacilityRegisterDto.getFacilityOfficerDto(), diffContentList);
        CompareTwoObject.diff(oldFacilityRegisterDto.getFacilityCommitteeDto(), newFacilityRegisterDto.getFacilityCommitteeDto(), diffContentList);
        CompareTwoObject.diffMap(oldFacilityRegisterDto.getBiologicalAgentToxinMap(), newFacilityRegisterDto.getBiologicalAgentToxinMap(), diffContentList, BiologicalAgentToxinDto.BATInfo.class);
        //docRecordInfos don't process
        return diffContentList;
    }
}

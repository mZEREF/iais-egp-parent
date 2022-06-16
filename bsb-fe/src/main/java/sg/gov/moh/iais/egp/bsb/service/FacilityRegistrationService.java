package sg.gov.moh.iais.egp.bsb.service;

import com.ecquaria.cloud.moh.iais.common.constant.AuditTrailConsts;
import com.ecquaria.cloud.moh.iais.common.dto.AuditTrailDto;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.filerepo.FileRepoDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenseeDto;
import com.ecquaria.cloud.moh.iais.common.exception.IaisRuntimeException;
import com.ecquaria.cloud.moh.iais.common.utils.MaskUtil;
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
import sg.gov.moh.iais.egp.bsb.client.DraftClient;
import sg.gov.moh.iais.egp.bsb.client.FacilityRegisterClient;
import sg.gov.moh.iais.egp.bsb.client.FileRepoClient;
import sg.gov.moh.iais.egp.bsb.client.OrganizationInfoClient;
import sg.gov.moh.iais.egp.bsb.common.node.Node;
import sg.gov.moh.iais.egp.bsb.common.node.NodeGroup;
import sg.gov.moh.iais.egp.bsb.common.node.Nodes;
import sg.gov.moh.iais.egp.bsb.common.node.simple.SimpleNode;
import sg.gov.moh.iais.egp.bsb.common.rfc.CompareTwoObject;
import sg.gov.moh.iais.egp.bsb.constant.DocConstants;
import sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants;
import sg.gov.moh.iais.egp.bsb.constant.SampleFileConstants;
import sg.gov.moh.iais.egp.bsb.dto.ResponseDto;
import sg.gov.moh.iais.egp.bsb.dto.entity.SampleFileDto;
import sg.gov.moh.iais.egp.bsb.dto.file.DocRecordInfo;
import sg.gov.moh.iais.egp.bsb.dto.file.FileRepoSyncDto;
import sg.gov.moh.iais.egp.bsb.dto.file.NewDocInfo;
import sg.gov.moh.iais.egp.bsb.dto.file.NewFileSyncDto;
import sg.gov.moh.iais.egp.bsb.dto.declaration.DeclarationConfigInfo;
import sg.gov.moh.iais.egp.bsb.dto.declaration.DeclarationItemMainInfo;
import sg.gov.moh.iais.egp.bsb.dto.info.bat.BatCodeInfo;
import sg.gov.moh.iais.egp.bsb.dto.info.common.OrgAddressInfo;
import sg.gov.moh.iais.egp.bsb.dto.register.bat.BATInfo;
import sg.gov.moh.iais.egp.bsb.dto.register.bat.BiologicalAgentToxinDto;
import sg.gov.moh.iais.egp.bsb.dto.register.facility.FacilityAdminAndOfficerDto;
import sg.gov.moh.iais.egp.bsb.dto.register.facility.FacilityAfcDto;
import sg.gov.moh.iais.egp.bsb.dto.register.facility.FacilityAuthoriserDto;
import sg.gov.moh.iais.egp.bsb.dto.register.facility.FacilityAuthoriserFileDto;
import sg.gov.moh.iais.egp.bsb.dto.register.facility.FacilityCommitteeDto;
import sg.gov.moh.iais.egp.bsb.dto.register.facility.FacilityCommitteeFileDto;
import sg.gov.moh.iais.egp.bsb.dto.register.facility.FacilityOperatorDto;
import sg.gov.moh.iais.egp.bsb.dto.register.facility.FacilityProfileDto;
import sg.gov.moh.iais.egp.bsb.dto.register.facility.FacilityRegisterDto;
import sg.gov.moh.iais.egp.bsb.dto.register.facility.FacilitySelectionDto;
import sg.gov.moh.iais.egp.bsb.dto.register.facility.OtherApplicationInfoDto;
import sg.gov.moh.iais.egp.bsb.dto.register.facility.PreviewSubmitDto;
import sg.gov.moh.iais.egp.bsb.dto.register.facility.PrimaryDocDto;
import sg.gov.moh.iais.egp.bsb.dto.rfc.DiffContent;
import sg.gov.moh.iais.egp.bsb.dto.validation.ValidationListResultUnit;
import sg.gov.moh.iais.egp.bsb.entity.DocSetting;
import sg.gov.moh.iais.egp.bsb.util.mastercode.MasterCodeHolder;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

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
    private final OrganizationInfoClient orgInfoClient;
    private final DraftClient draftClient;

    @Autowired
    public FacilityRegistrationService(FileRepoClient fileRepoClient, BsbFileClient bsbFileClient,
                                       FacilityRegisterClient facRegClient,
                                       DocSettingService docSettingService, OrganizationInfoClient orgInfoClient, DraftClient draftClient) {
        this.fileRepoClient = fileRepoClient;
        this.bsbFileClient = bsbFileClient;
        this.facRegClient = facRegClient;
        this.docSettingService = docSettingService;
        this.orgInfoClient = orgInfoClient;
        this.draftClient = draftClient;
    }

    public NodeGroup retrieveFacRegRoot(HttpServletRequest request, FacilityRegisterDto facilityRegisterDto) {
        NodeGroup facRegRoot = readRegisterDtoToNodeGroup(facilityRegisterDto, KEY_ROOT_NODE_GROUP);

        FacilitySelectionDto selectionDto = (FacilitySelectionDto) ((SimpleNode) facRegRoot.at(NODE_NAME_FAC_SELECTION)).getValue();
        boolean isRf = MasterCodeConstants.FAC_CLASSIFICATION_RF.equals(selectionDto.getFacClassification());
        boolean isPvRf = isRf && MasterCodeConstants.ACTIVITY_SP_HANDLE_PV_POTENTIAL.equals(selectionDto.getActivityTypes().get(0));

        if (!isRf) {
            // check data uploaded by committee data file
            String committeeNodePath = NODE_NAME_FAC_INFO + facRegRoot.getPathSeparator() + NODE_NAME_FAC_COMMITTEE;
            FacilityCommitteeDto facCommitteeDto = (FacilityCommitteeDto) ((SimpleNode) facRegRoot.at(committeeNodePath)).getValue();
            /* If there is no committee data, we don't need to show error message.
             * We call validation, if any error exists. The 'doValidation' method will set the errorVisible flag,
             * so the error table should be displayed. This situation means user click save as draft when user
             * upload a file contains error fields.
             * If pass validation, we set the node status to avoid not necessary validation again. */
            if (facCommitteeDto.getAmount() > 0 && facCommitteeDto.doValidation()) {
                Nodes.passValidation(facRegRoot, committeeNodePath);
            }
        }
        if (!isPvRf) {
            // check data uploaded by authoriser data file
            String authoriserNodePath = NODE_NAME_FAC_INFO + facRegRoot.getPathSeparator() + NODE_NAME_FAC_AUTH;
            FacilityAuthoriserDto facAuthDto = (FacilityAuthoriserDto) ((SimpleNode) facRegRoot.at(authoriserNodePath)).getValue();
            if (facAuthDto.getAmount() > 0 && facAuthDto.doValidation()) {
                Nodes.passValidation(facRegRoot, authoriserNodePath);
            }
        }

        ParamUtil.setSessionAttr(request, KEY_ROOT_NODE_GROUP, facRegRoot);
        return facRegRoot;
    }

    public void retrieveOrgAddressInfo(HttpServletRequest request) {
        AuditTrailDto auditTrailDto = (AuditTrailDto) ParamUtil.getSessionAttr(request, AuditTrailConsts.SESSION_ATTR_PARAM_NAME);
        assert auditTrailDto != null;
        LicenseeDto licenseeDto = orgInfoClient.getLicenseeByUenNo(auditTrailDto.getUenId());
        OrgAddressInfo orgAddressInfo = new OrgAddressInfo();
        orgAddressInfo.setUen(auditTrailDto.getUenId());
        orgAddressInfo.setCompName(licenseeDto.getName());
        orgAddressInfo.setPostalCode(licenseeDto.getPostalCode());
        orgAddressInfo.setAddressType(licenseeDto.getAddrType());
        orgAddressInfo.setBlockNo(licenseeDto.getBlkNo());
        orgAddressInfo.setFloor(licenseeDto.getFloorNo());
        orgAddressInfo.setUnitNo(licenseeDto.getUnitNo());
        orgAddressInfo.setStreet(licenseeDto.getStreetName());
        orgAddressInfo.setBuilding(licenseeDto.getBuildingName());
        ParamUtil.setSessionAttr(request, KEY_ORG_ADDRESS, orgAddressInfo);
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

    private Map<Long, FacilityRegisterDto> sortByKey(Map<Long, FacilityRegisterDto> map) {
        Map<Long, FacilityRegisterDto> result = new LinkedHashMap<>(map.size());
        map.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .forEachOrdered(e -> result.put(e.getKey(), e.getValue()));
        return result;
    }

    /**
     *  The method is used to get draft data from db ,and the draft have same classification and activities
     */
    private void getSameTypeFacilityDraftData(HttpServletRequest request, FacilitySelectionDto selectionDto) {
        FacilityRegisterDto eligibleDraftRegisterDto = (FacilityRegisterDto) ParamUtil.getSessionAttr(request, ELIGIBLE_DRAFT_REGISTER_DTO);
        // judge the action is click on Apply New Facility menu or click on Draft Application
        // if is click on draft application,do nothing
        Object requestAttr = ParamUtil.getRequestAttr(request, HAVE_SUITABLE_DRAFT_DATA);
        boolean haveSuitableDraftData = requestAttr != null && (boolean) requestAttr;
        //if draftAppNo is not null, The applicant may enter the Apply for New Facility module by clicking the Draft Application
        //when the draft dto get from session is null and draftAppNo is null,call API get draft data
        if (eligibleDraftRegisterDto == null && !StringUtils.hasLength(selectionDto.getDraftAppNo())) {
            Map<Long, FacilityRegisterDto> registerDtoMap = facRegClient.getSameClassificationAndActivityDraftData(selectionDto).getEntity();
            //get latest data
            if (!CollectionUtils.isEmpty(registerDtoMap)) {
                Map<Long, FacilityRegisterDto> suitableMap = sortByKey(registerDtoMap);
                Optional<FacilityRegisterDto> dtoOptional = suitableMap.values().stream().findFirst();
                if (dtoOptional.isPresent()) {
                    haveSuitableDraftData = true;
                    eligibleDraftRegisterDto = dtoOptional.get();
                } else {
                    haveSuitableDraftData = false;
                }
            } else {
                haveSuitableDraftData = false;
            }
        }
        if (eligibleDraftRegisterDto != null && StringUtils.hasLength(selectionDto.getDraftAppNo())) {
            haveSuitableDraftData = true;
        }
        ParamUtil.setSessionAttr(request, ELIGIBLE_DRAFT_REGISTER_DTO, eligibleDraftRegisterDto);
        ParamUtil.setRequestAttr(request, HAVE_SUITABLE_DRAFT_DATA, haveSuitableDraftData);
        if (eligibleDraftRegisterDto != null) {
            //judge whether need query the draft data again
            FacilitySelectionDto facilitySelectionDto = eligibleDraftRegisterDto.getFacilitySelectionDto();
            if (!facilitySelectionDto.getFacClassification().equals(selectionDto.getFacClassification()) || facilitySelectionDto.getActivityTypes().size() != selectionDto.getActivityTypes().size() || !facilitySelectionDto.getActivityTypes().equals(selectionDto.getActivityTypes())) {
                selectionDto.setDraftAppNo(null);
                ParamUtil.setSessionAttr(request, ELIGIBLE_DRAFT_REGISTER_DTO, null);
                getSameTypeFacilityDraftData(request, selectionDto);
            }
        }
    }

    public void handleNewFacilityServiceSelection(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        NodeGroup facRegRoot = getFacilityRegisterRoot(request);
        SimpleNode facSelectionNode = (SimpleNode) facRegRoot.getNode(NODE_NAME_FAC_SELECTION);
        FacilitySelectionDto selectionDto = (FacilitySelectionDto) facSelectionNode.getValue();
        selectionDto.reqObjMapping(request);
        //judge whether had eligible draft data
        getSameTypeFacilityDraftData(request, selectionDto);
        boolean haveSuitableDraftData = (boolean) ParamUtil.getRequestAttr(request,HAVE_SUITABLE_DRAFT_DATA);
        String actionLoadDraft = ParamUtil.getString(request, ACTION_LOAD_DRAFT);
        //if choose to load draft data,get dto from session
        if (StringUtils.hasLength(actionLoadDraft) && actionLoadDraft.equals(MasterCodeConstants.YES)) {
            FacilityRegisterDto eligibleDraftRegisterDto = (FacilityRegisterDto) ParamUtil.getSessionAttr(request, ELIGIBLE_DRAFT_REGISTER_DTO);
            // convert draft data to NodeGroup and set it into session, replace old data
            facRegRoot = retrieveFacRegRoot(request, eligibleDraftRegisterDto);
            facRegRoot.setActiveNodeKey(NODE_NAME_FAC_SELECTION);
            newFacServiceSelectionPageJumpJudge(request, false, facSelectionNode, facRegRoot, selectionDto);
        } else if (StringUtils.hasLength(actionLoadDraft) && actionLoadDraft.equals(MasterCodeConstants.NO)) {
            FacilityRegisterDto eligibleDraftRegisterDto = (FacilityRegisterDto) ParamUtil.getSessionAttr(request, ELIGIBLE_DRAFT_REGISTER_DTO);
            if (eligibleDraftRegisterDto != null) {
                //delete draft from database
                draftClient.doRemoveDraftByDraftAppNo(eligibleDraftRegisterDto.getFacilitySelectionDto().getDraftAppNo());
                //remove draft from session
                ParamUtil.setSessionAttr(request, ELIGIBLE_DRAFT_REGISTER_DTO, null);
            }
            /* When applicant select to use draft data first, the node data in session is replaced by draft data.
             * But applicant just use 'previous' to back to this step and select to not use draft, we have to re-init
             * data to replace the draft data in session. */
            facRegRoot = newFacRegNodeGroup(KEY_ROOT_NODE_GROUP);
            //selectionDto reset the value
            facSelectionNode = (SimpleNode) facRegRoot.getNode(NODE_NAME_FAC_SELECTION);
            selectionDto = (FacilitySelectionDto) facSelectionNode.getValue();
            selectionDto.reqObjMapping(request);
            facRegRoot.setActiveNodeKey(NODE_NAME_FAC_SELECTION);

            newFacServiceSelectionPageJumpJudge(request, false, facSelectionNode, facRegRoot, selectionDto);
        } else {
            String actionType = ParamUtil.getString(request, KEY_ACTION_TYPE);
            if (KEY_ACTION_JUMP.equals(actionType)) {
                String actionValue = ParamUtil.getString(request, KEY_ACTION_VALUE);
                if (!KEY_NAV_PREVIOUS.equals(actionValue) && !haveSuitableDraftData && selectionDto.getDraftAppNo() == null) {
                    /* When applicant select to use draft data first, the node data in session is replaced by draft data.
                     * But applicant just use 'previous' to back to this step and select a new facility classification
                     * and activities without draft, we have to re-init data to replace the draft data in session. */
                    facRegRoot = newFacRegNodeGroup(KEY_ROOT_NODE_GROUP);
                    //selectionDto reset the value
                    facSelectionNode = (SimpleNode) facRegRoot.getNode(NODE_NAME_FAC_SELECTION);
                    selectionDto = (FacilitySelectionDto) facSelectionNode.getValue();
                    selectionDto.reqObjMapping(request);
                    facRegRoot.setActiveNodeKey(NODE_NAME_FAC_SELECTION);
                }
            }
            newFacServiceSelectionPageJumpJudge(request, haveSuitableDraftData, facSelectionNode, facRegRoot, selectionDto);
        }
    }

    private void newFacServiceSelectionPageJumpJudge(HttpServletRequest request,boolean haveSuitableDraftData,SimpleNode facSelectionNode,NodeGroup facRegRoot,FacilitySelectionDto selectionDto){
        String actionType = ParamUtil.getString(request, KEY_ACTION_TYPE);
        if (KEY_ACTION_JUMP.equals(actionType)) {
            String actionValue = ParamUtil.getString(request, KEY_ACTION_VALUE);
            if (KEY_NAV_NEXT.equals(actionValue)) {  // if click next, we need to validate current node anyway
                //If there is no matching data, the process proceeds normally
                if (!haveSuitableDraftData) {
                    boolean currentLetGo = facSelectionNode.doValidation();
                    if (currentLetGo) {
                        handleServiceSelectionNextValidated(request, facRegRoot, selectionDto, actionValue);
                    } else {
                        ParamUtil.setRequestAttr(request, KEY_SHOW_ERROR_SWITCH, Boolean.TRUE);
                        ParamUtil.setSessionAttr(request, KEY_JUMP_DEST_NODE, NODE_NAME_FAC_SELECTION);
                    }
                } else {
                    // a message is displayed asking you whether to load draft data
                    ParamUtil.setRequestAttr(request, HAVE_SUITABLE_DRAFT_DATA, true);
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
                    handleServiceSelectionNextValidated(request, facRegRoot, selectionDto, actionValue);
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

    public void handleClassificationValidated(HttpServletRequest request, FacilitySelectionDto selectionDto){
        boolean isCf = MasterCodeConstants.CERTIFIED_CLASSIFICATION.contains(selectionDto.getFacClassification());
        ParamUtil.setSessionAttr(request, KEY_IS_CF, isCf ? Boolean.TRUE : Boolean.FALSE);
        boolean isUcf = MasterCodeConstants.UNCERTIFIED_CLASSIFICATION.contains(selectionDto.getFacClassification());
        ParamUtil.setSessionAttr(request, KEY_IS_UCF, isUcf ? Boolean.TRUE : Boolean.FALSE);
        boolean isRf = MasterCodeConstants.FAC_CLASSIFICATION_RF.equals(selectionDto.getFacClassification());
        ParamUtil.setSessionAttr(request, KEY_IS_RF, isRf ? Boolean.TRUE : Boolean.FALSE);
        boolean isFifthRf = isRf && MasterCodeConstants.ACTIVITY_SP_HANDLE_FIFTH_SCHEDULE_EXEMPTED.equals(selectionDto.getActivityTypes().get(0));
        ParamUtil.setSessionAttr(request, KEY_IS_FIFTH_RF, isFifthRf ? Boolean.TRUE : Boolean.FALSE);
        boolean isPvRf = isRf && MasterCodeConstants.ACTIVITY_SP_HANDLE_PV_POTENTIAL.equals(selectionDto.getActivityTypes().get(0));
        ParamUtil.setSessionAttr(request, KEY_IS_PV_RF, isPvRf ? Boolean.TRUE : Boolean.FALSE);

        // set selected value in the dashboard, it will also be used by latter logic and page
        ParamUtil.setSessionAttr(request, KEY_SELECTED_CLASSIFICATION, selectionDto.getFacClassification());
        ParamUtil.setSessionAttr(request, KEY_SELECTED_ACTIVITIES, new ArrayList<>(selectionDto.getActivityTypes()));
    }

    public void handleServiceSelectionNextValidated(HttpServletRequest request, NodeGroup facRegRoot, FacilitySelectionDto selectionDto, String actionValue) {
        Nodes.passValidation(facRegRoot, NODE_NAME_FAC_SELECTION);

        handleClassificationValidated(request,selectionDto);

        // change root node group
        changeRootNodeGroup(facRegRoot, selectionDto.getFacClassification(), selectionDto.getActivityTypes());

        boolean isUcf = (boolean) ParamUtil.getSessionAttr(request, KEY_IS_UCF);
        boolean isFifthRf = (boolean) ParamUtil.getSessionAttr(request, KEY_IS_FIFTH_RF);
        // change BAT node group
        if (isUcf) {
            NodeGroup batGroup = (NodeGroup) facRegRoot.getNode(NODE_NAME_FAC_BAT_INFO);
            changeBatNodeGroup(batGroup, selectionDto);
        } else if (isFifthRf) {
            NodeGroup batGroup = (NodeGroup) facRegRoot.getNode(NODE_NAME_FAC_BAT_INFO);
            changeBatNodeGroup4FifthRf(batGroup);
        }


        // impact declaration page
        SimpleNode otherAppInfoNode = (SimpleNode) facRegRoot.at(NODE_NAME_OTHER_INFO);
        OtherApplicationInfoDto otherAppInfoDto = (OtherApplicationInfoDto) otherAppInfoNode.getValue();
        otherAppInfoDto.setDeclarationId(null);
        otherAppInfoDto.setDeclarationConfig(null);
        Nodes.needValidation(facRegRoot, NODE_NAME_OTHER_INFO);

        // update impacted supporting document node
        SimpleNode primaryDocNode = (SimpleNode) facRegRoot.at(NODE_NAME_PRIMARY_DOC);
        PrimaryDocDto primaryDocDto = (PrimaryDocDto) primaryDocNode.getValue();
        primaryDocDto.setFacClassification(selectionDto.getFacClassification());
        primaryDocDto.setActivityTypes(selectionDto.getActivityTypes());
        Nodes.needValidation(facRegRoot, NODE_NAME_PRIMARY_DOC);

        // jump
        jump(request, facRegRoot, actionValue);
    }


    /** Checks if current flow is registering a new facility, if so, it's allowed to save draft.
     * Else, if current flow is editing a saved facility, it's not allowed to save draft. */
    public boolean allowSaveDraft(HttpServletRequest request) {
        return (boolean) ParamUtil.getSessionAttr(request, KEY_IS_NEW_REG_FAC);
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
            Assert.isTrue(allowSaveDraft(request), ERR_MSG_INVALID_ACTION);
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

        boolean isRf = (boolean) ParamUtil.getSessionAttr(request, KEY_IS_RF);
        if (!isRf) {
            SimpleNode facAuthNode = (SimpleNode) facRegRoot.at(NODE_NAME_FAC_INFO + facRegRoot.getPathSeparator() + NODE_NAME_FAC_AUTH);
            FacilityAuthoriserDto facAuthDto = (FacilityAuthoriserDto) facAuthNode.getValue();
            facAuthDto.setProtectedPlace(facProfileDto.getFacilityProtected());
        }

        String actionType = ParamUtil.getString(request, KEY_ACTION_TYPE);
        if (KEY_ACTION_JUMP.equals(actionType)) {
            jumpHandler(request, facRegRoot, currentNodePath, facProfileNode);
        } else if (KEY_ACTION_SAVE_AS_DRAFT.equals(actionType)) {
            Assert.isTrue(allowSaveDraft(request), ERR_MSG_INVALID_ACTION);
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
            Assert.isTrue(allowSaveDraft(request), ERR_MSG_INVALID_ACTION);
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
            Assert.isTrue(allowSaveDraft(request), ERR_MSG_INVALID_ACTION);
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
            // show other error info if exists
            String validationErrors = facCommitteeDto.retrieveValidationResult();
            if (StringUtils.hasLength(validationErrors)) {
                ParamUtil.setRequestAttr(request, KEY_VALIDATION_ERRORS, facCommitteeDto.retrieveValidationResult());
            }
        } else {
            Boolean needShowError = (Boolean) ParamUtil.getRequestAttr(request, KEY_SHOW_ERROR_SWITCH);
            if (needShowError == Boolean.TRUE) {
                ParamUtil.setRequestAttr(request, KEY_VALIDATION_ERRORS, facCommitteeDto.retrieveValidationResult());
            }
        }
        ParamUtil.setRequestAttr(request, NODE_NAME_FAC_COMMITTEE, facCommitteeDto);

        // load sample file data and set into session
        getBsbCommitteeSampleFileRecord(request);
    }

    public SampleFileDto getBsbCommitteeSampleFileRecord(HttpServletRequest request) {
        SampleFileDto committeeSampleFileDto = (SampleFileDto) ParamUtil.getSessionAttr(request, KEY_SAMPLE_COMMITTEE);
        if (committeeSampleFileDto == null) {
            committeeSampleFileDto = facRegClient.retrieveSampleFileByType(SampleFileConstants.TYPE_FACILITY_BSB_COMMITTEE_DATA_FILE);
            ParamUtil.setSessionAttr(request, KEY_SAMPLE_COMMITTEE, committeeSampleFileDto);
        }
        return committeeSampleFileDto;
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
            Assert.isTrue(allowSaveDraft(request), ERR_MSG_INVALID_ACTION);
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

        getAuthoriserSampleFileRecord(request);
    }

    public SampleFileDto getAuthoriserSampleFileRecord(HttpServletRequest request) {
        SampleFileDto committeeSampleFileDto = (SampleFileDto) ParamUtil.getSessionAttr(request, KEY_SAMPLE_AUTHORISER);
        if (committeeSampleFileDto == null) {
            committeeSampleFileDto = facRegClient.retrieveSampleFileByType(SampleFileConstants.TYPE_FACILITY_AUTHORISER_PERSONNEL_DATA_FILE);
            ParamUtil.setSessionAttr(request, KEY_SAMPLE_AUTHORISER, committeeSampleFileDto);
        }
        return committeeSampleFileDto;
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
            Assert.isTrue(allowSaveDraft(request), ERR_MSG_INVALID_ACTION);
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

        Map<String, List<BatCodeInfo>> scheduleBatMap = facRegClient.queryScheduleBasedBatBasicInfo(batDto.getActivityType(), Arrays.asList(MasterCodeConstants.APPROVAL_TYPE_POSSESS, MasterCodeConstants.APPROVAL_TYPE_LSP, MasterCodeConstants.APPROVAL_TYPE_HANDLE_FST_EXEMPTED));
        List<SelectOption> scheduleTypeOps = MasterCodeHolder.SCHEDULE.customOptions(scheduleBatMap.keySet().toArray(new String[0]));
        ParamUtil.setRequestAttr(request, KEY_OPTIONS_SCHEDULE, scheduleTypeOps);
        ParamUtil.setRequestAttr(request, KEY_SCHEDULE_FIRST_OPTION, Optional.of(scheduleTypeOps).filter(l -> !l.isEmpty()).map(l -> l.get(0)).map(SelectOption::getValue).orElse(null));

        // convert BatBasicInfo to SelectOption object
        Map<String, List<SelectOption>> scheduleBatOptionMap = Maps.newHashMapWithExpectedSize(scheduleBatMap.size());
        for (Map.Entry<String, List<BatCodeInfo>> entry : scheduleBatMap.entrySet()) {
            List<SelectOption> optionList = new ArrayList<>(entry.getValue().size() + 1);
            optionList.add(new SelectOption("", "Please Select"));
            for (BatCodeInfo info : entry.getValue()) {
                SelectOption option = new SelectOption();
                option.setText(info.getName());
                option.setValue(info.getCode());
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
            Assert.isTrue(allowSaveDraft(request), ERR_MSG_INVALID_ACTION);
            ParamUtil.setRequestAttr(request, KEY_ACTION_TYPE, KEY_ACTION_SAVE_AS_DRAFT);
            ParamUtil.setSessionAttr(request, KEY_JUMP_DEST_NODE, currentNodePath);
        } else {
            throw new IaisRuntimeException(ERR_MSG_INVALID_ACTION);
        }
        ParamUtil.setSessionAttr(request, KEY_ROOT_NODE_GROUP, facRegRoot);
    }

    public void tryLoadOtherAppDeclaration(OtherApplicationInfoDto otherAppInfoDto, FacilitySelectionDto selectionDto) {
        if (otherAppInfoDto.isConfigNotLoaded()) {
            if (StringUtils.hasLength(otherAppInfoDto.getDeclarationId())) {
                List<DeclarationItemMainInfo> declarationConfig = facRegClient.getDeclarationConfigInfoById(otherAppInfoDto.getDeclarationId());
                otherAppInfoDto.setDeclarationConfig(declarationConfig);
            } else {
                String declarationSubType;
                switch (selectionDto.getFacClassification()) {
                    case MasterCodeConstants.FAC_CLASSIFICATION_BSL3:
                        declarationSubType = MasterCodeConstants.FAC_CLASSIFICATION_BSL3;
                        break;
                    case MasterCodeConstants.FAC_CLASSIFICATION_BSL4:
                        declarationSubType = MasterCodeConstants.FAC_CLASSIFICATION_BSL4;
                        break;
                    case MasterCodeConstants.FAC_CLASSIFICATION_UF:
                    case MasterCodeConstants.FAC_CLASSIFICATION_LSPF:
                        declarationSubType = MasterCodeConstants.FAC_CLASSIFICATION_UF;
                        break;
                    case MasterCodeConstants.FAC_CLASSIFICATION_RF:
                        String activity = selectionDto.getActivityTypes().get(0);
                        if (MasterCodeConstants.ACTIVITY_SP_HANDLE_FIFTH_SCHEDULE_EXEMPTED.equals(activity)) {
                            declarationSubType = MasterCodeConstants.ACTIVITY_SP_HANDLE_FIFTH_SCHEDULE_EXEMPTED;
                        } else if (MasterCodeConstants.ACTIVITY_SP_HANDLE_PV_POTENTIAL.equals(activity)) {
                            declarationSubType = MasterCodeConstants.ACTIVITY_SP_HANDLE_PV_POTENTIAL;
                        } else {
                            throw new IllegalStateException("Invalid facility activity");
                        }
                        break;
                    default:
                        throw new IllegalStateException("Facility classification unavailable");
                }
                DeclarationConfigInfo configInfo = facRegClient.getDeclarationConfigBySpecificType(DECLARATION_TYPE, declarationSubType);
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
        FacilitySelectionDto selectionDto = (FacilitySelectionDto) ((SimpleNode) facRegRoot.getNode(NODE_NAME_FAC_SELECTION)).getValue();
        tryLoadOtherAppDeclaration(otherAppInfoDto, selectionDto);
        Boolean needShowError = (Boolean) ParamUtil.getRequestAttr(request, KEY_SHOW_ERROR_SWITCH);
        if (needShowError == Boolean.TRUE) {
            Map<String, String> errorMap = otherAppInfoDto.getValidationResultDto().getErrorMap();
            if (errorMap.containsKey("ID") || errorMap.containsKey("DTO")) {
                log.error("ID or answer DTO does not exists");
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
            Assert.isTrue(allowSaveDraft(request), ERR_MSG_INVALID_ACTION);
            ParamUtil.setRequestAttr(request, KEY_ACTION_TYPE, KEY_ACTION_SAVE_AS_DRAFT);
            ParamUtil.setSessionAttr(request, KEY_JUMP_DEST_NODE, NODE_NAME_OTHER_INFO);
        } else {
            throw new IaisRuntimeException(ERR_MSG_INVALID_ACTION);
        }
        ParamUtil.setSessionAttr(request, KEY_ROOT_NODE_GROUP, facRegRoot);
    }

    @SneakyThrows(JsonProcessingException.class)
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

        Map<String, List<DocRecordInfo>> savedFiles = primaryDocDto.getExistDocTypeMap();
        Map<String, List<NewDocInfo>> newFiles = primaryDocDto.getNewDocTypeMap();
        ParamUtil.setRequestAttr(request, KEY_FILE_MAP_SAVED, savedFiles);
        ParamUtil.setRequestAttr(request, KEY_FILE_MAP_NEW, newFiles);

        FacilitySelectionDto selectionDto = (FacilitySelectionDto) ((SimpleNode) facRegRoot.getNode(NODE_NAME_FAC_SELECTION)).getValue();
        List<DocSetting> facRegDocSetting = docSettingService.getFacRegDocSettings(selectionDto.getFacClassification(), selectionDto.getActivityTypes());
        ParamUtil.setRequestAttr(request, KEY_DOC_SETTINGS, facRegDocSetting);

        Set<String> otherDocTypes = DocSettingService.computeOtherDocTypes(facRegDocSetting, savedFiles.keySet(), newFiles.keySet());
        ParamUtil.setRequestAttr(request, KEY_OTHER_DOC_TYPES, otherDocTypes);

        List<SelectOption> docTypeOps = MasterCodeHolder.DOCUMENT_TYPE.allOptions();
        ParamUtil.setRequestAttr(request, KEY_OPTIONS_DOC_TYPES, docTypeOps);
        ObjectMapper mapper = new ObjectMapper();
        String docTypeOpsJson = mapper.writeValueAsString(docTypeOps);
        ParamUtil.setRequestAttr(request, KEY_DOC_TYPES_JSON, docTypeOpsJson);
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
            Assert.isTrue(allowSaveDraft(request), ERR_MSG_INVALID_ACTION);
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
            Assert.isTrue(allowSaveDraft(request), ERR_MSG_INVALID_ACTION);
            ParamUtil.setRequestAttr(request, KEY_ACTION_TYPE, KEY_ACTION_SAVE_AS_DRAFT);
            ParamUtil.setSessionAttr(request, KEY_JUMP_DEST_NODE, NODE_NAME_AFC);
        } else {
            throw new IaisRuntimeException(ERR_MSG_INVALID_ACTION);
        }
        ParamUtil.setSessionAttr(request, KEY_ROOT_NODE_GROUP, facRegRoot);

    }

    /** Sets print if into request, used by js function in JSP */
    public void preparePrintMaskId(HttpServletRequest request) {
        NodeGroup facRegRoot = getFacilityRegisterRoot(request);
        String maskForPrint = MaskUtil.maskValue(KEY_PRINT_MASK_PARAM, String.valueOf(facRegRoot.hashCode()));
        ParamUtil.setRequestAttr(request, KEY_PRINT_MASKED_ID, maskForPrint);
    }

    /** Validates if it is allowed to print the facility details.
     * If it is not allowed, an exception will be thrown */
    public void validatePrintMaskId(HttpServletRequest request) {
        String maskedId = ParamUtil.getString(request, "printId");
        boolean allowToPrint = false;
        if (StringUtils.hasLength(maskedId)) {
            MaskUtil.unMaskValue(KEY_PRINT_MASK_PARAM, maskedId);
            // if it can not be unmasked, an exception is thrown
            allowToPrint = true;
        }
        if (!allowToPrint) {
            throw new IaisRuntimeException("Invalid mask key, don't allow to print");
        }
    }

    public void prePreviewSubmit(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        preparePreviewData(request);
        preparePrintMaskId(request);
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

        boolean isCf = (boolean) ParamUtil.getSessionAttr(request, KEY_IS_CF);
        boolean isUcf = (boolean) ParamUtil.getSessionAttr(request, KEY_IS_UCF);
        boolean isFifthRf = (boolean) ParamUtil.getSessionAttr(request, KEY_IS_FIFTH_RF);

        ParamUtil.setRequestAttr(request, NODE_NAME_FAC_PROFILE, ((SimpleNode)facRegRoot.at(NODE_NAME_FAC_INFO + facRegRoot.getPathSeparator() + NODE_NAME_FAC_PROFILE)).getValue());
        if (isCf || isUcf) {
            ParamUtil.setRequestAttr(request, NODE_NAME_FAC_OPERATOR, ((SimpleNode) facRegRoot.at(NODE_NAME_FAC_INFO + facRegRoot.getPathSeparator() + NODE_NAME_FAC_OPERATOR)).getValue());
        }
        ParamUtil.setRequestAttr(request, NODE_NAME_FAC_ADMIN_OFFICER, ((SimpleNode)facRegRoot.at(NODE_NAME_FAC_INFO + facRegRoot.getPathSeparator() + NODE_NAME_FAC_ADMIN_OFFICER)).getValue());

        if (isCf) {
            ParamUtil.setRequestAttr(request, NODE_NAME_AFC, ((SimpleNode) facRegRoot.at(NODE_NAME_AFC)).getValue());
        } else if (isUcf || isFifthRf) {
            previewSubmitDto.setContainsBat(true);

            NodeGroup batNodeGroup = (NodeGroup) facRegRoot.at(NODE_NAME_FAC_BAT_INFO);
            List<BiologicalAgentToxinDto> batList = FacilityRegistrationService.getBatInfoList(batNodeGroup);
            ParamUtil.setRequestAttr(request, KEY_BAT_LIST, batList);

            boolean containsImport = false;
            for (BiologicalAgentToxinDto batDto : batList) {
                for (BATInfo batInfo : batDto.getBatInfos()) {
                    if (MasterCodeConstants.PROCUREMENT_MODE_IMPORT.equals(batInfo.getDetails().getProcurementMode())) {
                        containsImport = true;
                        break;
                    }
                }
                if (containsImport) {
                    break ;
                }
            }
            ParamUtil.setRequestAttr(request, KEY_BAT_CONTAINS_IMPORT, containsImport);
            previewSubmitDto.setBatContainsImport(containsImport);
        }

        FacilitySelectionDto selectionDto = (FacilitySelectionDto) ((SimpleNode) facRegRoot.getNode(NODE_NAME_FAC_SELECTION)).getValue();

        OtherApplicationInfoDto otherAppInfoDto = (OtherApplicationInfoDto) ((SimpleNode) facRegRoot.at(NODE_NAME_OTHER_INFO)).getValue();
        // load declaration
        tryLoadOtherAppDeclaration(otherAppInfoDto, selectionDto);
        ParamUtil.setRequestAttr(request, KEY_DECLARATION_CONFIG, otherAppInfoDto.getDeclarationConfig());
        ParamUtil.setRequestAttr(request, KEY_DECLARATION_ANSWER_MAP, otherAppInfoDto.getAnswerMap());

        List<DocSetting> facRegDocSetting = docSettingService.getFacRegDocSettings(selectionDto.getFacClassification(), selectionDto.getActivityTypes());
        ParamUtil.setRequestAttr(request, KEY_DOC_SETTINGS, facRegDocSetting);

        PrimaryDocDto primaryDocDto = (PrimaryDocDto) ((SimpleNode)facRegRoot.at(NODE_NAME_PRIMARY_DOC)).getValue();
        Map<String, List<DocRecordInfo>> savedFiles = primaryDocDto.getExistDocTypeMap();
        Map<String, List<NewDocInfo>> newFiles = primaryDocDto.getNewDocTypeMap();
        ParamUtil.setRequestAttr(request, KEY_FILE_MAP_SAVED, savedFiles);
        ParamUtil.setRequestAttr(request, KEY_FILE_MAP_NEW, newFiles);

        Set<String> otherDocTypes = DocSettingService.computeOtherDocTypes(facRegDocSetting, savedFiles.keySet(), newFiles.keySet());
        ParamUtil.setRequestAttr(request, KEY_OTHER_DOC_TYPES, otherDocTypes);
    }

    public void preAcknowledge(BaseProcessClass bpc) {
        preparePrintMaskId(bpc.request);
    }

    public void print(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        validatePrintMaskId(request);
        preparePreviewData(request);
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
        boolean isRf = (boolean) ParamUtil.getSessionAttr(request, KEY_IS_RF);
        boolean isPvRf = (boolean) ParamUtil.getSessionAttr(request, KEY_IS_PV_RF);

        // save docs
        PrimaryDocDto primaryDocDto = (PrimaryDocDto) ((SimpleNode) facRegRoot.at(NODE_NAME_PRIMARY_DOC)).getValue();
        List<NewFileSyncDto> primaryDocNewFiles = saveNewUploadedDoc(primaryDocDto);
        FacilityProfileDto profileDto = (FacilityProfileDto) ((SimpleNode) facRegRoot.at(NODE_NAME_FAC_INFO + facRegRoot.getPathSeparator() + NODE_NAME_FAC_PROFILE)).getValue();
        List<NewFileSyncDto> profileNewFiles = saveProfileNewUploadedDoc(profileDto);

        List<NewFileSyncDto> newFilesToSync = new ArrayList<>(primaryDocNewFiles.size() + profileNewFiles.size() + 2);
        newFilesToSync.addAll(primaryDocNewFiles);
        newFilesToSync.addAll(profileNewFiles);

        FacilityCommitteeDto committeeDto = null;
        FacilityAuthoriserDto authDto = null;
        if (!isRf) {
            committeeDto = (FacilityCommitteeDto) ((SimpleNode) facRegRoot.at(NODE_NAME_FAC_INFO + facRegRoot.getPathSeparator() + NODE_NAME_FAC_COMMITTEE)).getValue();
            NewFileSyncDto committeeNewFile = saveCommitteeNewDataFile(committeeDto);
            if (committeeNewFile != null) {
                newFilesToSync.add(committeeNewFile);
            }
        }
        if (!isPvRf) {
            authDto = (FacilityAuthoriserDto) ((SimpleNode) facRegRoot.at(NODE_NAME_FAC_INFO + facRegRoot.getPathSeparator() + NODE_NAME_FAC_AUTH)).getValue();
            NewFileSyncDto authoriserNewFile = saveAuthoriserNewDataFile(authDto);
            if (authoriserNewFile != null) {
                newFilesToSync.add(authoriserNewFile);
            }
        }


        // save data
        FacilityRegisterDto finalAllDataDto = null;
        if (appType.equals(MasterCodeConstants.APP_TYPE_NEW) || appType.equals(MasterCodeConstants.APP_TYPE_RFC)){
            finalAllDataDto = getRegisterDtoFromFacRegRoot(facRegRoot);
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
            if (!isRf && committeeDto.getToBeDeletedRepoId() != null) {
                FileRepoDto committeeDeleteDto = new FileRepoDto();
                committeeDeleteDto.setId(committeeDto.getToBeDeletedRepoId());
                fileRepoClient.removeFileById(committeeDeleteDto);
                toBeDeletedRepoIds.add(committeeDto.getToBeDeletedRepoId());
                committeeDto.setToBeDeletedRepoId(null);
            }
            if (!isPvRf && authDto.getToBeDeletedRepoId() != null) {
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
            root = newFacRegNodeGroup(KEY_ROOT_NODE_GROUP);
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




    public static NodeGroup initBatNodeGroup(Node[] dependNodes) {
        return new NodeGroup.Builder().name(NODE_NAME_FAC_BAT_INFO)
                .dependNodes(dependNodes)
                .addNode(new Node("error", new Node[0]))
                .build();
    }

    public static NodeGroup newFacRegNodeGroup(String name) {
        return newFacRegNodeGroup(name, null, null);
    }

    public static NodeGroup newFacRegNodeGroup(String name, FacilityRegisterDto facRegDto, PrimaryDocDto supportingDocDto) {
        boolean newNodeGroup = facRegDto == null || facRegDto.getFacilitySelectionDto() == null;
        boolean cf = !newNodeGroup && MasterCodeConstants.CERTIFIED_CLASSIFICATION.contains(facRegDto.getFacilitySelectionDto().getFacClassification());
        boolean ucf = !newNodeGroup && MasterCodeConstants.UNCERTIFIED_CLASSIFICATION.contains(facRegDto.getFacilitySelectionDto().getFacClassification());
        boolean rf = !newNodeGroup && MasterCodeConstants.FAC_CLASSIFICATION_RF.equals(facRegDto.getFacilitySelectionDto().getFacClassification());
        boolean fifthRf = !newNodeGroup && rf && MasterCodeConstants.ACTIVITY_SP_HANDLE_FIFTH_SCHEDULE_EXEMPTED.equals(facRegDto.getFacilitySelectionDto().getActivityTypes().get(0));

        Node beforeBeginNode = new Node(NODE_NAME_BEFORE_BEGIN, new Node[0]);
        SimpleNode facSelectionNode = new SimpleNode(newNodeGroup ? new FacilitySelectionDto() : facRegDto.getFacilitySelectionDto(), NODE_NAME_FAC_SELECTION, new Node[0]);
        Node companyInfoNode = new Node(NODE_NAME_COMPANY_INFO, new Node[0]);

        SimpleNode facProfileNode = new SimpleNode(newNodeGroup ? new FacilityProfileDto() : facRegDto.getFacilityProfileDto(), NODE_NAME_FAC_PROFILE, new Node[0]);
        SimpleNode facOperatorNode = new SimpleNode(newNodeGroup || rf ? new FacilityOperatorDto() : facRegDto.getFacilityOperatorDto(), NODE_NAME_FAC_OPERATOR, new Node[]{facProfileNode});
        SimpleNode facAdminOfficerNode = new SimpleNode(newNodeGroup ? new FacilityAdminAndOfficerDto() : facRegDto.getFacilityAdminAndOfficerDto(), NODE_NAME_FAC_ADMIN_OFFICER, new Node[]{facProfileNode, facOperatorNode});
        SimpleNode facCommitteeNode = new SimpleNode(newNodeGroup || rf ? new FacilityCommitteeDto() : facRegDto.getFacilityCommitteeDto(), NODE_NAME_FAC_COMMITTEE, new Node[]{facProfileNode, facOperatorNode, facAdminOfficerNode});
        SimpleNode facAuthNode = new SimpleNode(newNodeGroup || rf && !fifthRf ? new FacilityAuthoriserDto() : facRegDto.getFacilityAuthoriserDto(), NODE_NAME_FAC_AUTH, new Node[]{facProfileNode, facOperatorNode, facAdminOfficerNode, facCommitteeNode});
        if (rf) {
            facOperatorNode.disappear();
            facCommitteeNode.disappear();
        }
        if (rf && !fifthRf) {
            facAuthNode.disappear();
        }
        NodeGroup facInfoNodeGroup = new NodeGroup.Builder().name(NODE_NAME_FAC_INFO)
                .dependNodes(new Node[]{facSelectionNode})
                .addNode(facProfileNode)
                .addNode(facOperatorNode)
                .addNode(facAdminOfficerNode)
                .addNode(facCommitteeNode)
                .addNode(facAuthNode)
                .build();

        NodeGroup batNodeGroup;
        if (ucf || fifthRf) {
            NodeGroup.Builder batInfoNodeGroupBuilder = new NodeGroup.Builder().name(NODE_NAME_FAC_BAT_INFO)
                    .dependNodes(new Node[]{facSelectionNode, facInfoNodeGroup});
            for (Map.Entry<String, BiologicalAgentToxinDto> entry : facRegDto.getBiologicalAgentToxinMap().entrySet()) {
                SimpleNode batNode = new SimpleNode(entry.getValue(), entry.getKey(), new Node[0]);
                batInfoNodeGroupBuilder.addNode(batNode);
            }
            batNodeGroup = batInfoNodeGroupBuilder.build();
        } else {
            batNodeGroup = initBatNodeGroup(new Node[]{facSelectionNode, facInfoNodeGroup});
            batNodeGroup.disappear();
        }

        SimpleNode otherAppInfoNode = new SimpleNode(newNodeGroup ? new OtherApplicationInfoDto() : facRegDto.getOtherAppInfoDto(), NODE_NAME_OTHER_INFO, new Node[]{facSelectionNode, facInfoNodeGroup, batNodeGroup});
        SimpleNode supportingDocNode = new SimpleNode(newNodeGroup ? new PrimaryDocDto() : supportingDocDto, NODE_NAME_PRIMARY_DOC, new Node[]{facSelectionNode, facInfoNodeGroup, batNodeGroup, otherAppInfoNode});
        SimpleNode afcNode = new SimpleNode(newNodeGroup || !cf ? new FacilityAfcDto() : facRegDto.getAfcDto(), NODE_NAME_AFC, new Node[]{facSelectionNode, facInfoNodeGroup, batNodeGroup, otherAppInfoNode, supportingDocNode});
        SimpleNode previewSubmitNode = new SimpleNode(newNodeGroup ? new PreviewSubmitDto() : facRegDto.getPreviewSubmitDto(), NODE_NAME_PREVIEW_SUBMIT, new Node[]{facSelectionNode, facInfoNodeGroup, batNodeGroup, otherAppInfoNode, supportingDocNode, afcNode});
        if (!cf) {
            afcNode.disappear();
        }

        return new NodeGroup.Builder().name(name)
                .addNode(beforeBeginNode)
                .addNode(facSelectionNode)
                .addNode(companyInfoNode)
                .addNode(facInfoNodeGroup)
                .addNode(batNodeGroup)
                .addNode(otherAppInfoNode)
                .addNode(supportingDocNode)
                .addNode(afcNode)
                .addNode(previewSubmitNode)
                .build();
    }

    public static void changeRootNodeGroup(NodeGroup facRegRoot, String classification, List<String> activityTypes) {
        Assert.notNull(facRegRoot, ERR_MSG_BAT_NOT_NULL);
        if (MasterCodeConstants.CERTIFIED_CLASSIFICATION.contains(classification)) {
            Nodes.appear(facRegRoot, NODE_PATH_FAC_OPERATOR);
            Nodes.appear(facRegRoot, NODE_PATH_FAC_COMMITTEE);
            Nodes.appear(facRegRoot, NODE_PATH_FAC_AUTH);
            Nodes.disappear(facRegRoot, NODE_NAME_FAC_BAT_INFO);
            Nodes.appear(facRegRoot, NODE_NAME_AFC);
        } else if (MasterCodeConstants.UNCERTIFIED_CLASSIFICATION.contains(classification)) {
            Nodes.appear(facRegRoot, NODE_PATH_FAC_OPERATOR);
            Nodes.appear(facRegRoot, NODE_PATH_FAC_COMMITTEE);
            Nodes.appear(facRegRoot, NODE_PATH_FAC_AUTH);
            Nodes.appear(facRegRoot, NODE_NAME_FAC_BAT_INFO);
            Nodes.disappear(facRegRoot, NODE_NAME_AFC);
        } else if (MasterCodeConstants.FAC_CLASSIFICATION_RF.equals(classification)) {
            Assert.state(activityTypes.size() == 1, ERR_MSG_RF_INVALID_ACTIVITY);
            String activityType = activityTypes.get(0);
            if (MasterCodeConstants.ACTIVITY_SP_HANDLE_FIFTH_SCHEDULE_EXEMPTED.equals(activityType)) {
                Nodes.disappear(facRegRoot, NODE_PATH_FAC_OPERATOR);
                Nodes.disappear(facRegRoot, NODE_PATH_FAC_COMMITTEE);
                Nodes.appear(facRegRoot, NODE_PATH_FAC_AUTH);
                Nodes.appear(facRegRoot, NODE_NAME_FAC_BAT_INFO);
                Nodes.disappear(facRegRoot, NODE_NAME_AFC);
            } else if (MasterCodeConstants.ACTIVITY_SP_HANDLE_PV_POTENTIAL.equals(activityType)) {
                Nodes.disappear(facRegRoot, NODE_PATH_FAC_OPERATOR);
                Nodes.disappear(facRegRoot, NODE_PATH_FAC_COMMITTEE);
                Nodes.disappear(facRegRoot, NODE_PATH_FAC_AUTH);
                Nodes.disappear(facRegRoot, NODE_NAME_FAC_BAT_INFO);
                Nodes.disappear(facRegRoot, NODE_NAME_AFC);
            } else {
                throw new IllegalStateException(ERR_MSG_INVALID_ACTIVITY);
            }
        } else {
            throw new IllegalStateException(ERR_MSG_INVALID_CLASSIFICATION);
        }
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

    public static void changeBatNodeGroup4FifthRf(NodeGroup batNodeGroup) {
        Assert.notNull(batNodeGroup, ERR_MSG_BAT_NOT_NULL);
        Node[] subNodes = new Node[1];
        subNodes[0] = new SimpleNode(new BiologicalAgentToxinDto(MasterCodeConstants.ACTIVITY_SP_HANDLE_FIFTH_SCHEDULE_EXEMPTED), MasterCodeConstants.ACTIVITY_SP_HANDLE_FIFTH_SCHEDULE_EXEMPTED, new Node[0]);
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

    public static Map<String, BiologicalAgentToxinDto> getBatInfoMap(NodeGroup batNodeGroup) {
        Assert.notNull(batNodeGroup, ERR_MSG_BAT_NOT_NULL);
        Map<String, BiologicalAgentToxinDto> batMap = Maps.newLinkedHashMapWithExpectedSize(batNodeGroup.count());
        for (Node node : batNodeGroup.getAllNodes()) {
            assert node instanceof SimpleNode;
            batMap.put(node.getName(), (BiologicalAgentToxinDto) ((SimpleNode) node).getValue());
        }
        return batMap;
    }


    /**
     * Convert flow-oriented NodeGroup to a DTO which will be transferred to be saved
     */
    public static FacilityRegisterDto getRegisterDtoFromFacRegRoot(NodeGroup facRegRoot) {
        FacilityRegisterDto dto = new FacilityRegisterDto();
        FacilitySelectionDto selectionDto = (FacilitySelectionDto) ((SimpleNode) facRegRoot.at(NODE_NAME_FAC_SELECTION)).getValue();
        dto.setFacilitySelectionDto(selectionDto);

        boolean isCf = MasterCodeConstants.CERTIFIED_CLASSIFICATION.contains(selectionDto.getFacClassification());
        boolean isUcf = MasterCodeConstants.UNCERTIFIED_CLASSIFICATION.contains(selectionDto.getFacClassification());
        boolean isRf = MasterCodeConstants.FAC_CLASSIFICATION_RF.equals(selectionDto.getFacClassification());
        boolean isFifthRf = false;
        if (isRf) {
            Assert.state(selectionDto.getActivityTypes().size() == 1, ERR_MSG_RF_INVALID_ACTIVITY);
            String activityType = selectionDto.getActivityTypes().get(0);
            isFifthRf = MasterCodeConstants.ACTIVITY_SP_HANDLE_FIFTH_SCHEDULE_EXEMPTED.equals(activityType);
        }

        dto.setFacilityProfileDto((FacilityProfileDto) ((SimpleNode) facRegRoot.at(NODE_NAME_FAC_INFO + facRegRoot.getPathSeparator() + NODE_NAME_FAC_PROFILE)).getValue());
        dto.setFacilityAdminAndOfficerDto((FacilityAdminAndOfficerDto) ((SimpleNode) facRegRoot.at(NODE_NAME_FAC_INFO + facRegRoot.getPathSeparator() + NODE_NAME_FAC_ADMIN_OFFICER)).getValue());
        dto.setOtherAppInfoDto((OtherApplicationInfoDto) ((SimpleNode) facRegRoot.at(NODE_NAME_OTHER_INFO)).getValue());
        PrimaryDocDto primaryDocDto = (PrimaryDocDto) ((SimpleNode) facRegRoot.at(NODE_NAME_PRIMARY_DOC)).getValue();
        dto.setPreviewSubmitDto((PreviewSubmitDto) ((SimpleNode) facRegRoot.at(NODE_NAME_PREVIEW_SUBMIT)).getValue());

        if (isCf || isUcf) {
            dto.setFacilityOperatorDto((FacilityOperatorDto) ((SimpleNode) facRegRoot.at(NODE_NAME_FAC_INFO + facRegRoot.getPathSeparator() + NODE_NAME_FAC_OPERATOR)).getValue());
            dto.setFacilityCommitteeDto((FacilityCommitteeDto) ((SimpleNode) facRegRoot.at(NODE_NAME_FAC_INFO + facRegRoot.getPathSeparator() + NODE_NAME_FAC_COMMITTEE)).getValue());
        }
        if (isCf || isUcf || isFifthRf) {
            dto.setFacilityAuthoriserDto((FacilityAuthoriserDto) ((SimpleNode) facRegRoot.at(NODE_NAME_FAC_INFO + facRegRoot.getPathSeparator() + NODE_NAME_FAC_AUTH)).getValue());
        }


        Collection<DocRecordInfo> docRecordInfos = new ArrayList<>(dto.getFacilityProfileDto().getSavedDocMap().size() + primaryDocDto.getSavedDocMap().size() + 2);
        docRecordInfos.addAll(dto.getFacilityProfileDto().getSavedDocMap().values());
        docRecordInfos.addAll(primaryDocDto.getSavedDocMap().values());
        if ((isCf || isUcf) && dto.getFacilityCommitteeDto().getSavedFile() != null) {
            docRecordInfos.add(dto.getFacilityCommitteeDto().getSavedFile());
        }
        if ((isCf || isUcf || isFifthRf) && dto.getFacilityAuthoriserDto().getSavedFile() != null) {
            docRecordInfos.add(dto.getFacilityAuthoriserDto().getSavedFile());
        }
        dto.setDocRecordInfos(docRecordInfos);

        if (isCf) {
            dto.setAfcDto((FacilityAfcDto) ((SimpleNode) facRegRoot.at(NODE_NAME_AFC)).getValue());
        } else if (isUcf || isFifthRf) {
            NodeGroup batGroup = (NodeGroup) facRegRoot.at(NODE_NAME_FAC_BAT_INFO);
            Map<String, BiologicalAgentToxinDto> batInfoMap = getBatInfoMap(batGroup);
            dto.setBiologicalAgentToxinMap(batInfoMap);
        }
        return dto;
    }

    /**
     * Read a facility register DTO which contains all related data (usually retrieved from DB) to
     * NodeGroup for the usage of flow
     */
    public static NodeGroup readRegisterDtoToNodeGroup(FacilityRegisterDto registerDto, String name) {
        FacilitySelectionDto selectionDto = registerDto.getFacilitySelectionDto();
        boolean isCf = MasterCodeConstants.CERTIFIED_CLASSIFICATION.contains(selectionDto.getFacClassification());
        boolean isUcf = MasterCodeConstants.UNCERTIFIED_CLASSIFICATION.contains(selectionDto.getFacClassification());
        boolean isRf = MasterCodeConstants.FAC_CLASSIFICATION_RF.equals(selectionDto.getFacClassification());
        boolean isFifthRf = isRf && MasterCodeConstants.ACTIVITY_SP_HANDLE_FIFTH_SCHEDULE_EXEMPTED.equals(selectionDto.getActivityTypes().get(0));

        // split documents for profile
        Collection<DocRecordInfo> profileDocs = new ArrayList<>();
        Collection<DocRecordInfo> primaryDocs = new ArrayList<>();
        DocRecordInfo committeeDoc = null;
        DocRecordInfo authoriserDoc = null;
        for (DocRecordInfo info : registerDto.getDocRecordInfos()) {
            switch (info.getDocType()) {
                case DocConstants.DOC_TYPE_GAZETTE_ORDER:
                    profileDocs.add(info);
                    break;
                case DocConstants.DOC_TYPE_DATA_COMMITTEE:
                    committeeDoc = info;
                    break;
                case DocConstants.DOC_TYPE_DATA_AUTHORISER:
                    authoriserDoc = info;
                    break;
                default:
                    primaryDocs.add(info);
                    break;
            }
        }
        registerDto.getFacilityProfileDto().setSavedDocMap(sg.gov.moh.iais.egp.bsb.util.CollectionUtils.uniqueIndexMap(profileDocs, DocRecordInfo::getRepoId));
        if (isCf || isUcf) {
            registerDto.getFacilityCommitteeDto().setSavedFile(committeeDoc);
        }
        if (isCf || isUcf || isFifthRf) {
            registerDto.getFacilityAuthoriserDto().setSavedFile(authoriserDoc);
        }

        PrimaryDocDto primaryDocDto = new PrimaryDocDto();
        primaryDocDto.setSavedDocMap(sg.gov.moh.iais.egp.bsb.util.CollectionUtils.uniqueIndexMap(primaryDocs, DocRecordInfo::getRepoId));

        return newFacRegNodeGroup(name, registerDto, primaryDocDto);
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
        CompareTwoObject.diffMap(oldFacilityRegisterDto.getBiologicalAgentToxinMap(), newFacilityRegisterDto.getBiologicalAgentToxinMap(), diffContentList, BATInfo.class);
        //docRecordInfos don't process
        return diffContentList;
    }
}

package sg.gov.moh.iais.egp.bsb.service;

import com.ecquaria.cloud.moh.iais.common.constant.AuditTrailConsts;
import com.ecquaria.cloud.moh.iais.common.dto.AuditTrailDto;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.filerepo.FileRepoDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenseeDto;
import com.ecquaria.cloud.moh.iais.common.exception.IaisRuntimeException;
import com.ecquaria.cloud.moh.iais.common.utils.LogUtil;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Maps;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;
import sg.gov.moh.iais.egp.bsb.client.ApprovalBatAndActivityClient;
import sg.gov.moh.iais.egp.bsb.client.BsbFileClient;
import sg.gov.moh.iais.egp.bsb.client.FileRepoClient;
import sg.gov.moh.iais.egp.bsb.client.OrganizationInfoClient;
import sg.gov.moh.iais.egp.bsb.common.node.Node;
import sg.gov.moh.iais.egp.bsb.common.node.NodeGroup;
import sg.gov.moh.iais.egp.bsb.common.node.Nodes;
import sg.gov.moh.iais.egp.bsb.common.node.simple.SimpleNode;
import sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants;
import sg.gov.moh.iais.egp.bsb.dto.ResponseDto;
import sg.gov.moh.iais.egp.bsb.dto.entity.FacilityAuthoriserDto;
import sg.gov.moh.iais.egp.bsb.dto.file.DocRecordInfo;
import sg.gov.moh.iais.egp.bsb.dto.file.FileRepoSyncDto;
import sg.gov.moh.iais.egp.bsb.dto.file.NewDocInfo;
import sg.gov.moh.iais.egp.bsb.dto.file.NewFileSyncDto;
import sg.gov.moh.iais.egp.bsb.dto.info.bat.BatCodeInfo;
import sg.gov.moh.iais.egp.bsb.dto.info.common.OrgAddressInfo;
import sg.gov.moh.iais.egp.bsb.dto.info.facility.FacilityBasicInfo;
import sg.gov.moh.iais.egp.bsb.dto.register.approval.*;
import sg.gov.moh.iais.egp.bsb.dto.register.approval.PrimaryDocDto;
import sg.gov.moh.iais.egp.bsb.dto.register.bat.BiologicalAgentToxinDto;
import sg.gov.moh.iais.egp.bsb.dto.validation.ValidationResultDto;
import sg.gov.moh.iais.egp.bsb.entity.DocSetting;
import sg.gov.moh.iais.egp.bsb.util.mastercode.MasterCodeHolder;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;


import java.util.*;
import java.util.stream.Collectors;

import static sg.gov.moh.iais.egp.bsb.constant.module.ApprovalBatAndActivityConstants.*;
import static sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants.KEY_IND_AFTER_SAVE_AS_DRAFT;


@Service
@Slf4j
public class ApprovalBatAndActivityService {
    private final ApprovalBatAndActivityClient approvalBatAndActivityClient;
    private final DocSettingService docSettingService;
    private final OrganizationInfoClient orgInfoClient;
    private final FileRepoClient fileRepoClient;
    private final BsbFileClient bsbFileClient;

    @Autowired
    public ApprovalBatAndActivityService(ApprovalBatAndActivityClient approvalBatAndActivityClient, DocSettingService docSettingService,
                                         FileRepoClient fileRepoClient, BsbFileClient bsbFileClient, OrganizationInfoClient orgInfoClient) {
        this.approvalBatAndActivityClient = approvalBatAndActivityClient;
        this.docSettingService = docSettingService;
        this.fileRepoClient = fileRepoClient;
        this.bsbFileClient = bsbFileClient;
        this.orgInfoClient = orgInfoClient;
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

    public void preApprovalSelection(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        //TODO: The facility value will be obtained from another method, and the method will be deleted
        List<FacilityBasicInfo> facilityBasicInfoList = approvalBatAndActivityClient.getApprovedFacility();
        List<SelectOption> facilityIdList = new ArrayList<>(facilityBasicInfoList.size());
        if (!CollectionUtils.isEmpty(facilityBasicInfoList)) {
            for (FacilityBasicInfo fac : facilityBasicInfoList) {
                facilityIdList.add(new SelectOption(fac.getId(), fac.getName()));
            }
        }
        ParamUtil.setRequestAttr(request, SELECTION_FACILITY_ID, facilityIdList);
        ApprovalBatAndActivityDto approvalBatAndActivityDto = (ApprovalBatAndActivityDto) ParamUtil.getSessionAttr(request,KEY_APPROVAL_BAT_AND_ACTIVITY_DTO);
        if(approvalBatAndActivityDto != null){
            ParamUtil.setSessionAttr(request,KEY_APPROVAL_SELECTION_DTO,approvalBatAndActivityDto.getApprovalSelectionDto());
        }
    }

    public void handleApprovalSelection(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        // get approvalBatAndActivityDto
        ApprovalSelectionDto approvalSelectionDto = getApprovalSelectionDto(request);
        String oldProcessType = approvalSelectionDto.getProcessType();
        approvalSelectionDto.reqObjMapping(request);
        // judge jump logic
        String actionType = ParamUtil.getString(request, KEY_ACTION_TYPE);
        String actionValue = ParamUtil.getString(request, KEY_ACTION_VALUE);
        if (KEY_ACTION_JUMP.equals(actionType)) {
            if (KEY_NAV_NEXT.equals(actionValue)) {
                // do next: validate approvalSelectionDto
                ValidationResultDto validationResultDto = approvalBatAndActivityClient.validateApprovalSelectionDto(approvalSelectionDto);
                if (validationResultDto.isPass()) {
                    // get nodeGroup
                    NodeGroup approvalAppRoot;
                    // judge whether the selected processType is the same
                    String newProcessType = approvalSelectionDto.getProcessType();
                    if (org.springframework.util.StringUtils.hasLength(oldProcessType) && oldProcessType.equals(newProcessType)) {
                        // selected processType is the same
                        approvalAppRoot = (NodeGroup) ParamUtil.getSessionAttr(request, KEY_ROOT_NODE_GROUP);
                    } else {
                        // selected processType is the different
                        approvalAppRoot = newApprovalAppRoot(KEY_ROOT_NODE_GROUP, newProcessType);
                    }
                    ParamUtil.setSessionAttr(request, KEY_ROOT_NODE_GROUP, approvalAppRoot);
                    ParamUtil.setSessionAttr(request, KEY_JUMP_DEST_NODE, NODE_NAME_COMPANY_INFO);
                    ParamUtil.setSessionAttr(request, KEY_APPROVAL_SELECTION_DTO, approvalSelectionDto);
                    // dash bord display
                    ParamUtil.setSessionAttr(request, KEY_PROCESS_TYPE, approvalSelectionDto.getProcessType());
                } else {
                    ParamUtil.setRequestAttr(request, KEY_VALIDATION_ERRORS, validationResultDto.toErrorMsg());
                    ParamUtil.setSessionAttr(request, KEY_JUMP_DEST_NODE, NODE_NAME_APPROVAL_SELECTION);
                }
            } else if (KEY_NAV_BACK.equals(actionValue)) {
                // do back
                ParamUtil.setSessionAttr(request, KEY_JUMP_DEST_NODE, NODE_NAME_BEGIN);
            }
        } else {
            throw new IaisRuntimeException(ERR_MSG_INVALID_ACTION);
        }
    }


    public void handleCompanyInfo(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        String actionType = ParamUtil.getString(request, KEY_ACTION_TYPE);
        String actionValue = ParamUtil.getString(request, KEY_ACTION_VALUE);
        if (KEY_ACTION_JUMP.equals(actionType)) {
            if (KEY_NAV_NEXT.equals(actionValue)) {
                ParamUtil.setSessionAttr(request, KEY_JUMP_DEST_NODE, "appInfo_facProfile");
            } else if (KEY_NAV_BACK.equals(actionValue)) {
                ParamUtil.setSessionAttr(request, KEY_JUMP_DEST_NODE, NODE_NAME_APPROVAL_SELECTION);
            }
        } else {
            throw new IaisRuntimeException(ERR_MSG_INVALID_ACTION);
        }
    }

    public void preFacProfile(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        // data display
        ApprovalSelectionDto approvalSelectionDto = getApprovalSelectionDto(request);
        String facilityId = approvalSelectionDto.getFacilityId();
        FacProfileDto facProfileDto = getFacProfileDtoByFacilityId(facilityId);
        ParamUtil.setSessionAttr(request, KEY_FAC_PROFILE_DTO, facProfileDto);
    }

    public void handleFacProfile(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        NodeGroup approvalAppRoot = getApprovalActivityRoot(request, null);
        String currentNodePath = NODE_NAME_APP_INFO + approvalAppRoot.getPathSeparator() + NODE_NAME_FAC_PROFILE;
        Node facProfileNode = approvalAppRoot.at(currentNodePath);

        String actionType = ParamUtil.getString(request, KEY_ACTION_TYPE);
        if (KEY_ACTION_JUMP.equals(actionType)) {
            String actionValue = ParamUtil.getString(request, KEY_ACTION_VALUE);
            if (actionValue.equals(KEY_NAV_BACK)){
                ParamUtil.setSessionAttr(request, KEY_JUMP_DEST_NODE, NODE_NAME_COMPANY_INFO);
            } else {
                jumpHandler(request, approvalAppRoot, currentNodePath, facProfileNode);
            }
        } else {
            throw new IaisRuntimeException(ERR_MSG_INVALID_ACTION);
        }
        ParamUtil.setSessionAttr(request, KEY_ROOT_NODE_GROUP, approvalAppRoot);
    }

    public void prePossessBatDetails(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        NodeGroup approvalAppRoot = getApprovalActivityRoot(request, null);
        String currentNodePath = NODE_NAME_APP_INFO + approvalAppRoot.getPathSeparator() + NODE_NAME_POSSESS_BAT;
        SimpleNode possessBatNode = (SimpleNode) approvalAppRoot.at(currentNodePath);
        BiologicalAgentToxinDto approvalToPossessDto = (BiologicalAgentToxinDto) possessBatNode.getValue();
        Boolean needShowError = (Boolean) ParamUtil.getRequestAttr(request, KEY_SHOW_ERROR_SWITCH);
        if (needShowError == Boolean.TRUE) {
            ParamUtil.setRequestAttr(request, KEY_VALIDATION_ERRORS, approvalToPossessDto.retrieveValidationResult());
        }
        Nodes.needValidation(approvalAppRoot, currentNodePath);
        ParamUtil.setRequestAttr(request, KEY_BAT_INFO, approvalToPossessDto);
        ParamUtil.setRequestAttr(request, KEY_OPTIONS_ADDRESS_TYPE, MasterCodeHolder.ADDRESS_TYPE.allOptions());
        ParamUtil.setRequestAttr(request, KEY_OPTIONS_NATIONALITY, MasterCodeHolder.NATIONALITY.allOptions());
        loadAllowedScheduleAndBatOptions(request);
    }

    public void handlePossessBatDetails(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        NodeGroup approvalAppRoot = getApprovalActivityRoot(request, null);
        String currentNodePath = NODE_NAME_APP_INFO + approvalAppRoot.getPathSeparator() + NODE_NAME_POSSESS_BAT;
        SimpleNode possessBatNode = (SimpleNode) approvalAppRoot.at(currentNodePath);
        BiologicalAgentToxinDto approvalToPossessDto = (BiologicalAgentToxinDto) possessBatNode.getValue();
        approvalToPossessDto.reqObjMapping(request);

        String actionType = ParamUtil.getString(request, KEY_ACTION_TYPE);
        if (KEY_ACTION_JUMP.equals(actionType)) {
            jumpHandler(request, approvalAppRoot, currentNodePath, possessBatNode);
        } else if (KEY_ACTION_SAVE_AS_DRAFT.equals(actionType)) {
            ParamUtil.setRequestAttr(request, KEY_ACTION_TYPE, KEY_ACTION_SAVE_AS_DRAFT);
            ParamUtil.setSessionAttr(request, KEY_JUMP_DEST_NODE, currentNodePath);
        } else {
            throw new IaisRuntimeException(ERR_MSG_INVALID_ACTION);
        }
        ParamUtil.setSessionAttr(request, KEY_ROOT_NODE_GROUP, approvalAppRoot);
    }

    public void preLargeBatDetails(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        NodeGroup approvalAppRoot = getApprovalActivityRoot(request, null);
        String currentNodePath = NODE_NAME_APP_INFO + approvalAppRoot.getPathSeparator() + NODE_NAME_LARGE_BAT;
        SimpleNode largeBatNode = (SimpleNode) approvalAppRoot.at(currentNodePath);
        ApprovalToLargeDto approvalToLargeDto = (ApprovalToLargeDto) largeBatNode.getValue();
        Boolean needShowError = (Boolean) ParamUtil.getRequestAttr(request, KEY_SHOW_ERROR_SWITCH);
        if (needShowError == Boolean.TRUE) {
            ParamUtil.setRequestAttr(request, KEY_VALIDATION_ERRORS, approvalToLargeDto.retrieveValidationResult());
        }
        Nodes.needValidation(approvalAppRoot, currentNodePath);
        ParamUtil.setRequestAttr(request, KEY_BAT_INFO, approvalToLargeDto);
        ParamUtil.setRequestAttr(request, KEY_OPTIONS_ADDRESS_TYPE, MasterCodeHolder.ADDRESS_TYPE.allOptions());
        ParamUtil.setRequestAttr(request, KEY_OPTIONS_NATIONALITY, MasterCodeHolder.NATIONALITY.allOptions());
        loadAllowedScheduleAndBatOptions(request);
    }

    public void handleLargeBatDetails(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        NodeGroup approvalAppRoot = getApprovalActivityRoot(request, null);
        String currentNodePath = NODE_NAME_APP_INFO + approvalAppRoot.getPathSeparator() + NODE_NAME_LARGE_BAT;
        SimpleNode largeBatNode = (SimpleNode) approvalAppRoot.at(currentNodePath);
        ApprovalToLargeDto approvalToLargeDto = (ApprovalToLargeDto) largeBatNode.getValue();
        approvalToLargeDto.reqObjMapping(request);

        String actionType = ParamUtil.getString(request, KEY_ACTION_TYPE);
        if (KEY_ACTION_JUMP.equals(actionType)) {
            jumpHandler(request, approvalAppRoot, currentNodePath, largeBatNode);
        } else if (KEY_ACTION_SAVE_AS_DRAFT.equals(actionType)) {
            ParamUtil.setRequestAttr(request, KEY_ACTION_TYPE, KEY_ACTION_SAVE_AS_DRAFT);
            ParamUtil.setSessionAttr(request, KEY_JUMP_DEST_NODE, currentNodePath);
        } else {
            throw new IaisRuntimeException(ERR_MSG_INVALID_ACTION);
        }
        ParamUtil.setSessionAttr(request, KEY_ROOT_NODE_GROUP, approvalAppRoot);
    }

    public void preSpecialBatDetails(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        NodeGroup approvalAppRoot = getApprovalActivityRoot(request, null);
        String currentNodePath = NODE_NAME_APP_INFO + approvalAppRoot.getPathSeparator() + NODE_NAME_SPECIAL_BAT;
        SimpleNode specialBatNode = (SimpleNode) approvalAppRoot.at(currentNodePath);
        ApprovalToSpecialDto approvalToSpecialDto = (ApprovalToSpecialDto) specialBatNode.getValue();
        Boolean needShowError = (Boolean) ParamUtil.getRequestAttr(request, KEY_SHOW_ERROR_SWITCH);
        if (needShowError == Boolean.TRUE) {
            ParamUtil.setRequestAttr(request, KEY_VALIDATION_ERRORS, approvalToSpecialDto.retrieveValidationResult());
        }
        Nodes.needValidation(approvalAppRoot, currentNodePath);
        ParamUtil.setRequestAttr(request, KEY_BAT_INFO, approvalToSpecialDto);
        ParamUtil.setRequestAttr(request, KEY_OPTIONS_ADDRESS_TYPE, MasterCodeHolder.ADDRESS_TYPE.allOptions());
        ParamUtil.setRequestAttr(request, KEY_OPTIONS_NATIONALITY, MasterCodeHolder.NATIONALITY.allOptions());
        loadAllowedScheduleAndBatOptions(request);
    }

    public void handleSpecialBatDetails(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        NodeGroup approvalAppRoot = getApprovalActivityRoot(request, null);
        String currentNodePath = NODE_NAME_APP_INFO + approvalAppRoot.getPathSeparator() + NODE_NAME_SPECIAL_BAT;
        SimpleNode specialBatNode = (SimpleNode) approvalAppRoot.at(currentNodePath);
        ApprovalToSpecialDto approvalToSpecialDto = (ApprovalToSpecialDto) specialBatNode.getValue();
        approvalToSpecialDto.reqObjMapping(request);

        String actionType = ParamUtil.getString(request, KEY_ACTION_TYPE);
        if (KEY_ACTION_JUMP.equals(actionType)) {
            jumpHandler(request, approvalAppRoot, currentNodePath, specialBatNode);
        } else if (KEY_ACTION_SAVE_AS_DRAFT.equals(actionType)) {
            ParamUtil.setRequestAttr(request, KEY_ACTION_TYPE, KEY_ACTION_SAVE_AS_DRAFT);
            ParamUtil.setSessionAttr(request, KEY_JUMP_DEST_NODE, currentNodePath);
        } else {
            throw new IaisRuntimeException(ERR_MSG_INVALID_ACTION);
        }
        ParamUtil.setSessionAttr(request, KEY_ROOT_NODE_GROUP, approvalAppRoot);
    }

    @SneakyThrows(JsonProcessingException.class)
    public void preFacAuthorised(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        NodeGroup approvalAppRoot = getApprovalActivityRoot(request, null);
        String currentNodePath = NODE_NAME_APP_INFO + approvalAppRoot.getPathSeparator() + NODE_NAME_FAC_AUTHORISED;
        SimpleNode facAuthorisedNode = (SimpleNode) approvalAppRoot.at(currentNodePath);
        FacAuthorisedDto facAuthorisedDto = (FacAuthorisedDto) facAuthorisedNode.getValue();
        Boolean needShowError = (Boolean) ParamUtil.getRequestAttr(request, KEY_SHOW_ERROR_SWITCH);
        if (needShowError == Boolean.TRUE) {
            ParamUtil.setRequestAttr(request, KEY_VALIDATION_ERRORS, facAuthorisedDto.retrieveValidationResult());
        }
        Nodes.needValidation(approvalAppRoot, currentNodePath);
        ApprovalSelectionDto approvalSelectionDto = getApprovalSelectionDto(request);
        Map<String, FacilityAuthoriserDto> facilityAuthDtoMap = (Map<String, FacilityAuthoriserDto>) ParamUtil.getSessionAttr(request,KEY_USER_ID_FACILITY_AUTH_MAP);
        if(facilityAuthDtoMap == null){
            facilityAuthDtoMap = approvalBatAndActivityClient.getApprovalSelectAuthorisedPersonnelByFacId(approvalSelectionDto.getFacilityId());
            ParamUtil.setSessionAttr(request,KEY_USER_ID_FACILITY_AUTH_MAP,new HashMap<>(facilityAuthDtoMap));
        }
        ObjectMapper mapper = new ObjectMapper();
        String authPersonnelDetailMapJson = mapper.writeValueAsString(facilityAuthDtoMap);
        ParamUtil.setRequestAttr(request, KEY_AUTH_PERSONNEL_DETAIL_MAP_JSON, authPersonnelDetailMapJson);
        //generate options select facility authorised personnel
        Collection<FacilityAuthoriserDto> facilityAuthDtoList = facilityAuthDtoMap.values();
        List<SelectOption> authPersonnelOps = facilityAuthDtoList.stream().map(i->{
            SelectOption selectOption = new SelectOption();
            selectOption.setText(i.getName());
            selectOption.setValue(i.getIdNumber());
            return selectOption; }).collect(Collectors.toList());

        //view data
        ParamUtil.setRequestAttr(request,KEY_OPTIONS_AUTH_PERSONNEL,authPersonnelOps);

        List<FacilityAuthoriserDto> facAuthList = facAuthorisedDto.getFacAuthorisedDtoList();
        ParamUtil.setRequestAttr(request,"authPersonnelList",facAuthList);
    }

    public void handleFacAuthorised(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        NodeGroup approvalAppRoot = getApprovalActivityRoot(request, null);
        String currentNodePath = NODE_NAME_APP_INFO + approvalAppRoot.getPathSeparator() + NODE_NAME_FAC_AUTHORISED;
        SimpleNode facAuthorisedNode = (SimpleNode) approvalAppRoot.at(currentNodePath);
        FacAuthorisedDto facAuthorisedDto = (FacAuthorisedDto) facAuthorisedNode.getValue();
        facAuthorisedDto.reqObjMapping(request);

        String actionType = ParamUtil.getString(request, KEY_ACTION_TYPE);
        if (KEY_ACTION_JUMP.equals(actionType)) {
            jumpHandler(request, approvalAppRoot, currentNodePath, facAuthorisedNode);
        } else if (KEY_ACTION_SAVE_AS_DRAFT.equals(actionType)) {
            ParamUtil.setRequestAttr(request, KEY_ACTION_TYPE, KEY_ACTION_SAVE_AS_DRAFT);
            ParamUtil.setSessionAttr(request, KEY_JUMP_DEST_NODE, currentNodePath);
        } else {
            throw new IaisRuntimeException(ERR_MSG_INVALID_ACTION);
        }
        ParamUtil.setSessionAttr(request, KEY_ROOT_NODE_GROUP, approvalAppRoot);
    }

    public void preActivityDetails(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        NodeGroup approvalAppRoot = getApprovalActivityRoot(request, null);
        String currentNodePath = NODE_NAME_APP_INFO + approvalAppRoot.getPathSeparator() + NODE_NAME_FAC_ACTIVITY;
        SimpleNode facActivityNode = (SimpleNode) approvalAppRoot.at(currentNodePath);
        ApprovalToActivityDto approvalToActivityDto = (ApprovalToActivityDto) facActivityNode.getValue();
        Boolean needShowError = (Boolean) ParamUtil.getRequestAttr(request, KEY_SHOW_ERROR_SWITCH);
        if (needShowError == Boolean.TRUE) {
            ParamUtil.setRequestAttr(request, KEY_VALIDATION_ERRORS, approvalToActivityDto.retrieveValidationResult());
        }
        Nodes.needValidation(approvalAppRoot, currentNodePath);
        ParamUtil.setRequestAttr(request, KEY_APPROVAL_TO_ACTIVITY_DTO, approvalToActivityDto);
        // get facilityId
        ApprovalSelectionDto approvalSelectionDto = getApprovalSelectionDto(request);
        String facilityId = approvalSelectionDto.getFacilityId();
        // show that belongs to this classification without approval facActivityType
        List<String> notExistFacActivityTypeApprovalList = approvalBatAndActivityClient.getNotApprovalActivities(facilityId);
        ParamUtil.setRequestAttr(request, KEY_NOT_EXIST_FAC_ACTIVITY_TYPE_APPROVAL_LIST, notExistFacActivityTypeApprovalList);
    }

    public void handleActivityDetails(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        NodeGroup approvalAppRoot = getApprovalActivityRoot(request, null);
        String currentNodePath = NODE_NAME_APP_INFO + approvalAppRoot.getPathSeparator() + NODE_NAME_FAC_ACTIVITY;
        SimpleNode facActivityNode = (SimpleNode) approvalAppRoot.at(currentNodePath);
        ApprovalToActivityDto approvalToActivityDto = (ApprovalToActivityDto) facActivityNode.getValue();
        approvalToActivityDto.reqObjMapping(request);

        String actionType = ParamUtil.getString(request, KEY_ACTION_TYPE);
        if (KEY_ACTION_JUMP.equals(actionType)) {
            jumpHandler(request, approvalAppRoot, currentNodePath, facActivityNode);
        } else if (KEY_ACTION_SAVE_AS_DRAFT.equals(actionType)) {
            ParamUtil.setRequestAttr(request, KEY_ACTION_TYPE, KEY_ACTION_SAVE_AS_DRAFT);
            ParamUtil.setSessionAttr(request, KEY_JUMP_DEST_NODE, currentNodePath);
        } else {
            throw new IaisRuntimeException(ERR_MSG_INVALID_ACTION);
        }
        ParamUtil.setSessionAttr(request, KEY_ROOT_NODE_GROUP, approvalAppRoot);
    }

    @SneakyThrows(JsonProcessingException.class)
    public void prePrimaryDoc(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        String processType = (String) ParamUtil.getSessionAttr(request, KEY_PROCESS_TYPE);
        NodeGroup approvalAppRoot = getApprovalActivityRoot(request, null);
        SimpleNode primaryDocNode = (SimpleNode) approvalAppRoot.at(NODE_NAME_PRIMARY_DOC);
        PrimaryDocDto primaryDocDto = (PrimaryDocDto) primaryDocNode.getValue();
        Boolean needShowError = (Boolean) ParamUtil.getRequestAttr(request, KEY_SHOW_ERROR_SWITCH);
        if (needShowError == Boolean.TRUE) {
            ParamUtil.setRequestAttr(request, KEY_VALIDATION_ERRORS, primaryDocDto.retrieveValidationResult());
        }
        Nodes.needValidation(approvalAppRoot, NODE_NAME_PRIMARY_DOC);

        List<DocSetting>  approvalAppDocSettings = docSettingService.getApprovalAppDocSettings(processType);
        ParamUtil.setRequestAttr(request, KEY_DOC_SETTINGS, approvalAppDocSettings);

        Map<String, List<DocRecordInfo>> savedFiles = primaryDocDto.getExistDocTypeMap();
        Map<String, List<NewDocInfo>> newFiles = primaryDocDto.getNewDocTypeMap();
        ParamUtil.setRequestAttr(request, "savedFiles", savedFiles);
        ParamUtil.setRequestAttr(request, "newFiles", newFiles);

        Set<String> otherDocTypes = DocSettingService.computeOtherDocTypes(approvalAppDocSettings, savedFiles.keySet(), newFiles.keySet());
        ParamUtil.setRequestAttr(request, KEY_OTHER_DOC_TYPES, otherDocTypes);

        List<SelectOption> docTypeOps = MasterCodeHolder.DOCUMENT_TYPE.allOptions();
        ParamUtil.setRequestAttr(request, KEY_OPTIONS_DOC_TYPES, docTypeOps);
        ObjectMapper mapper = new ObjectMapper();
        String docTypeOpsJson = mapper.writeValueAsString(docTypeOps);
        ParamUtil.setRequestAttr(request, KEY_DOC_TYPES_JSON, docTypeOpsJson);
    }

    public void handlePrimaryDoc(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        NodeGroup approvalAppRoot = getApprovalActivityRoot(request, null);
        SimpleNode primaryDocNode = (SimpleNode) approvalAppRoot.at(NODE_NAME_PRIMARY_DOC);
        PrimaryDocDto primaryDocDto = (PrimaryDocDto) primaryDocNode.getValue();
        primaryDocDto.reqObjMapping(request);

        String actionType = ParamUtil.getString(request, KEY_ACTION_TYPE);
        if (KEY_ACTION_JUMP.equals(actionType)) {
            jumpHandler(request, approvalAppRoot, NODE_NAME_PRIMARY_DOC, primaryDocNode);
        } else if (KEY_ACTION_SAVE_AS_DRAFT.equals(actionType)) {
            ParamUtil.setRequestAttr(request, KEY_ACTION_TYPE, KEY_ACTION_SAVE_AS_DRAFT);
            ParamUtil.setSessionAttr(request, KEY_JUMP_DEST_NODE, NODE_NAME_PRIMARY_DOC);
        } else {
            throw new IaisRuntimeException(ERR_MSG_INVALID_ACTION);
        }
        ParamUtil.setSessionAttr(request, KEY_ROOT_NODE_GROUP, approvalAppRoot);
    }


    public void actionFilter(BaseProcessClass bpc, String appType) {
        HttpServletRequest request = bpc.request;
        // check if there is action set to override the action from request
        String actionType = (String) ParamUtil.getRequestAttr(request, KEY_ACTION_TYPE);
        if (!org.springframework.util.StringUtils.hasLength(actionType)) {
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

    public void jumpFilter(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        String destNode = (String) ParamUtil.getSessionAttr(request, KEY_JUMP_DEST_NODE);
        ParamUtil.setRequestAttr(request, KEY_DEST_NODE_ROUTE, destNode);
    }

    //----------------------------------------------------------------------------------------------
    public ApprovalBatAndActivityDto getEditDtoData(String appId) {
        ResponseDto<ApprovalBatAndActivityDto> responseDto = approvalBatAndActivityClient.getApprovalAppAppDataByApplicationId(appId);
        if (responseDto.ok()) {
            return responseDto.getEntity();
        } else {
            throw new IaisRuntimeException("Fail to retrieve edit approval app data");
        }
    }

    public ApprovalSelectionDto getApprovalSelectionDto(HttpServletRequest request) {
        ApprovalSelectionDto approvalSelectionDto = (ApprovalSelectionDto) ParamUtil.getSessionAttr(request, KEY_APPROVAL_SELECTION_DTO);
        if (approvalSelectionDto == null) {
            approvalSelectionDto = new ApprovalSelectionDto();
        }
        return approvalSelectionDto;
    }

    public FacProfileDto getFacProfileDtoByFacilityId(String facId) {
        ResponseDto<FacProfileDto> responseDto = approvalBatAndActivityClient.getFacProfileDtoByFacilityId(facId);
        if (responseDto.ok()) {
            return responseDto.getEntity();
        } else {
            throw new IaisRuntimeException("Fail to query facProfileDto data by facilityId");
        }
    }

    public NodeGroup getApprovalActivityRoot(HttpServletRequest request, String processType) {
        NodeGroup root = (NodeGroup) ParamUtil.getSessionAttr(request, KEY_ROOT_NODE_GROUP);
        if (root == null && org.springframework.util.StringUtils.hasLength(processType)) {
            root = newApprovalAppRoot(KEY_ROOT_NODE_GROUP, processType);
        }
        return root;
    }

    public NodeGroup newApprovalAppRoot(String name, String processType) {
        NodeGroup appInfoNodeGroup = null;
        switch (processType) {
            case MasterCodeConstants.PROCESS_TYPE_APPROVE_POSSESS:
                appInfoNodeGroup = newApprovalPossessAppInfoNodeGroup(new Node[0]);
                break;
            case MasterCodeConstants.PROCESS_TYPE_APPROVE_LSP:
                appInfoNodeGroup = newApprovalLargeAppInfoNodeGroup(new Node[0]);
                break;
            case MasterCodeConstants.PROCESS_TYPE_SP_APPROVE_HANDLE:
                appInfoNodeGroup = newApprovalSpecialAppInfoNodeGroup(new Node[0]);
                break;
            case MasterCodeConstants.PROCESS_TYPE_APPROVAL_FOR_FACILITY_ACTIVITY_TYPE:
                appInfoNodeGroup = newApprovalActivityAppInfoNodeGroup(new Node[0]);
                break;
            default:
                log.info(StringUtil.changeForLog("no such processType " + StringUtils.normalizeSpace(processType)));
                break;
        }
        if (appInfoNodeGroup == null) {
            return null;
        }
        SimpleNode primaryDocNode = new SimpleNode(new PrimaryDocDto(), NODE_NAME_PRIMARY_DOC, new Node[]{appInfoNodeGroup});
        SimpleNode previewNode = new SimpleNode(new PreviewDto(), NODE_NAME_PREVIEW, new Node[]{appInfoNodeGroup, primaryDocNode});

        return new NodeGroup.Builder().name(name)
                .addNode(appInfoNodeGroup)
                .addNode(primaryDocNode)
                .addNode(previewNode)
                .build();
    }

    private NodeGroup newApprovalPossessAppInfoNodeGroup(Node[] dependNodes) {
        Node facProfileNode = new Node(NODE_NAME_FAC_PROFILE, new Node[0]);
        SimpleNode possessBatNode = new SimpleNode(new BiologicalAgentToxinDto(), NODE_NAME_POSSESS_BAT, new Node[]{facProfileNode});

        return new NodeGroup.Builder().name(NODE_NAME_APP_INFO)
                .dependNodes(dependNodes)
                .addNode(facProfileNode)
                .addNode(possessBatNode)
                .build();
    }

    private NodeGroup newApprovalLargeAppInfoNodeGroup(Node[] dependNodes) {
        Node facProfileNode = new Node(NODE_NAME_FAC_PROFILE, new Node[0]);
        SimpleNode largeBatNode = new SimpleNode(new ApprovalToLargeDto(), NODE_NAME_LARGE_BAT, new Node[]{facProfileNode});

        return new NodeGroup.Builder().name(NODE_NAME_APP_INFO)
                .dependNodes(dependNodes)
                .addNode(facProfileNode)
                .addNode(largeBatNode)
                .build();
    }

    private NodeGroup newApprovalSpecialAppInfoNodeGroup(Node[] dependNodes) {
        Node facProfileNode = new Node(NODE_NAME_FAC_PROFILE, new Node[0]);
        SimpleNode specialBatNode = new SimpleNode(new ApprovalToSpecialDto(), NODE_NAME_SPECIAL_BAT, new Node[]{facProfileNode});
        SimpleNode facAuthorisedNode = new SimpleNode(new FacAuthorisedDto(), NODE_NAME_FAC_AUTHORISED, new Node[]{facProfileNode, specialBatNode});

        return new NodeGroup.Builder().name(NODE_NAME_APP_INFO)
                .dependNodes(dependNodes)
                .addNode(facProfileNode)
                .addNode(specialBatNode)
                .addNode(facAuthorisedNode)
                .build();
    }

    private NodeGroup newApprovalActivityAppInfoNodeGroup(Node[] dependNodes) {
        Node facProfileNode = new Node(NODE_NAME_FAC_PROFILE, new Node[0]);
        SimpleNode facActivityNode = new SimpleNode(new ApprovalToActivityDto(), NODE_NAME_FAC_ACTIVITY, new Node[]{facProfileNode});

        return new NodeGroup.Builder().name(NODE_NAME_APP_INFO)
                .dependNodes(dependNodes)
                .addNode(facProfileNode)
                .addNode(facActivityNode)
                .build();
    }

    public void saveDraft(HttpServletRequest request, String appType) {
        NodeGroup approvalAppRoot = getApprovalActivityRoot(request,"");

        // save docs
        PrimaryDocDto primaryDocDto = (PrimaryDocDto) ((SimpleNode) approvalAppRoot.at(NODE_NAME_PRIMARY_DOC)).getValue();
        List<NewFileSyncDto> newFilesToSync = saveNewUploadedDoc(primaryDocDto);


        // save data
        ApprovalBatAndActivityDto finalAllDataDto = null;
        ApprovalSelectionDto approvalSelectionDto = getApprovalSelectionDto(request);
        if (appType.equals(MasterCodeConstants.APP_TYPE_NEW) || appType.equals(MasterCodeConstants.APP_TYPE_RFC)){
            finalAllDataDto = ApprovalBatAndActivityDto.from(approvalSelectionDto,approvalAppRoot);
            finalAllDataDto.setAppType(appType);
        }
        String draftAppNo = approvalBatAndActivityClient.saveNewFacilityDraft(finalAllDataDto);
        // set draft app No. into the NodeGroup
        approvalSelectionDto.setDraftAppNo(draftAppNo);
        ParamUtil.setSessionAttr(request,KEY_APPROVAL_SELECTION_DTO,approvalSelectionDto);
        ParamUtil.setSessionAttr(request, KEY_ROOT_NODE_GROUP, approvalAppRoot);

        try {
            // delete docs
            log.info("Delete already saved documents in file-repo");
            List<String> toBeDeletedRepoIds = deleteUnwantedDoc(primaryDocDto);
            // sync docs
            syncNewDocsAndDeleteFiles(newFilesToSync, toBeDeletedRepoIds);
        } catch (Exception e) {
            log.error("Fail to sync files to BE", e);
        }

        ParamUtil.setRequestAttr(request, KEY_IND_AFTER_SAVE_AS_DRAFT, Boolean.TRUE);
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
     * @param group the node group of the approvalAppRoot
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
     * @param group the node group of the approvalAppRoot
     */
    public static boolean ifNodeSelected(NodeGroup group, String name) {
        Assert.notNull(group, ERR_MSG_NULL_GROUP);
        Assert.notNull(name, ERR_MSG_NULL_NAME);
        return name.equals(group.getActiveNodeKey());
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

    /**
     * @param group the node group contains the sub steps
     */
    public static String computeStepClassname(NodeGroup group, String name) {
        Assert.notNull(group, ERR_MSG_NULL_GROUP);
        Assert.notNull(name, ERR_MSG_NULL_NAME);
        return group.getNode(name).isValidated() || name.equals(group.getActiveNodeKey()) ? "active" : "disabled";
    }

    /**
     * common actions when we do 'jump'
     * decide the routing logic
     * will set a dest node in the request attribute;
     * will set a flag if we need to show the error messages.
     * @param approvalAppRoot root data structure of this flow
     */
    public void jumpHandler(HttpServletRequest request, NodeGroup approvalAppRoot, String currentPath, Node currentNode) {
        String actionValue = ParamUtil.getString(request, KEY_ACTION_VALUE);
        Assert.hasText(actionValue, "Invalid action value");
        boolean currentLetGo = true;  // if false, we have to stay current node
        if (KEY_NAV_NEXT.equals(actionValue)) {  // if click next, we need to validate current node anyway
            currentLetGo = currentNode.doValidation();
            if (currentLetGo) {
                Nodes.passValidation(approvalAppRoot, currentPath);
            }
        }
        if (currentLetGo) {
            String destNode = computeDestNodePath(approvalAppRoot, actionValue);
            String checkedDestNode = Nodes.jump(approvalAppRoot, destNode);
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
     * Compute the destiny node path we will go to.
     * @param approvalAppRoot the root NodeGroup
     * @param actionValue the value we received, it can be next, back, or a value specified by tab
     * @return the destiny node path, return null if we can't go the next or previous node
     */
    public String computeDestNodePath(NodeGroup approvalAppRoot, String actionValue) {
        String destNode;
        switch (actionValue) {
            case KEY_NAV_NEXT:
                destNode = Nodes.getNextNodePath(approvalAppRoot);
                break;
            case KEY_NAV_BACK:
                destNode = Nodes.getPreviousNodePath(approvalAppRoot);
                break;
            default:
                Assert.hasText(actionValue, "Action value should be a node path");
                destNode = Nodes.expandNode(approvalAppRoot, actionValue);
                break;
        }
        return destNode;
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

    /** Delete unwanted documents in FE file repo.
     * This method will remove the repoId from the toBeDeletedRepoIds set after a call of removal.
     * @param primaryDocDto document DTO have the specific structure
     * @return a list of repo IDs deleted in FE file repo
     */
    public List<String> deleteUnwantedDoc(PrimaryDocDto primaryDocDto) {
        /* Ignore the failure when try to delete FE files because this is not a big issue.
         * The not deleted file won't be retrieved, so it's just a waste of disk space */
        List<String> toBeDeletedRepoIds = new ArrayList<>(primaryDocDto.getToBeDeletedRepoIds());
        for (String id: toBeDeletedRepoIds) {
            FileRepoDto fileRepoDto = new FileRepoDto();
            fileRepoDto.setId(id);
            fileRepoClient.removeFileById(fileRepoDto);
            primaryDocDto.getToBeDeletedRepoIds().remove(id);
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

    public void preparePreviewData(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        NodeGroup approvalAppRoot = getApprovalActivityRoot(request, null);
        SimpleNode previewNode = (SimpleNode) approvalAppRoot.at(NODE_NAME_PREVIEW);
        PreviewDto previewDto = (PreviewDto) previewNode.getValue();
        Boolean needShowError = (Boolean) ParamUtil.getRequestAttr(request, KEY_SHOW_ERROR_SWITCH);
        if (needShowError == Boolean.TRUE) {
            ParamUtil.setRequestAttr(request, KEY_VALIDATION_ERRORS, previewDto.retrieveValidationResult());
        }
        Nodes.needValidation(approvalAppRoot, NODE_NAME_PREVIEW);
        ParamUtil.setRequestAttr(request, NODE_NAME_PREVIEW, previewDto);

        String processType = (String) ParamUtil.getSessionAttr(request, KEY_PROCESS_TYPE);
        switch (processType) {
            case MasterCodeConstants.PROCESS_TYPE_APPROVE_POSSESS:
                ParamUtil.setRequestAttr(request, KEY_BAT_INFO, ((SimpleNode)approvalAppRoot.at(NODE_NAME_APP_INFO + approvalAppRoot.getPathSeparator() + NODE_NAME_POSSESS_BAT)).getValue());
                break;
            case MasterCodeConstants.PROCESS_TYPE_APPROVE_LSP:
                ParamUtil.setRequestAttr(request, KEY_BAT_INFO, ((SimpleNode)approvalAppRoot.at(NODE_NAME_APP_INFO + approvalAppRoot.getPathSeparator() + NODE_NAME_LARGE_BAT)).getValue());
                break;
            case MasterCodeConstants.PROCESS_TYPE_SP_APPROVE_HANDLE:
                ParamUtil.setRequestAttr(request, KEY_BAT_INFO, ((SimpleNode)approvalAppRoot.at(NODE_NAME_APP_INFO + approvalAppRoot.getPathSeparator() + NODE_NAME_SPECIAL_BAT)).getValue());
                ParamUtil.setRequestAttr(request, KEY_FAC_AUTHORISED_DTO, ((SimpleNode)approvalAppRoot.at(NODE_NAME_APP_INFO + approvalAppRoot.getPathSeparator() + NODE_NAME_FAC_AUTHORISED)).getValue());
                break;
            case MasterCodeConstants.PROCESS_TYPE_APPROVAL_FOR_FACILITY_ACTIVITY_TYPE:
                ParamUtil.setRequestAttr(request, KEY_BAT_INFO, ((SimpleNode)approvalAppRoot.at(NODE_NAME_APP_INFO + approvalAppRoot.getPathSeparator() + NODE_NAME_FAC_ACTIVITY)).getValue());
                break;
            default:
                log.info("no such processType {}", StringUtils.normalizeSpace(processType));
                break;
        }

        List<DocSetting> approvalAppDocSettings = docSettingService.getApprovalAppDocSettings(processType);
        ParamUtil.setRequestAttr(request, "docSettings", approvalAppDocSettings);
        PrimaryDocDto primaryDocDto = (PrimaryDocDto) ((SimpleNode)approvalAppRoot.at(NODE_NAME_PRIMARY_DOC)).getValue();
        Map<String, List<DocRecordInfo>> savedFiles = primaryDocDto.getExistDocTypeMap();
        Map<String, List<NewDocInfo>> newFiles = primaryDocDto.getNewDocTypeMap();
        ParamUtil.setRequestAttr(request, "savedFiles", savedFiles);
        ParamUtil.setRequestAttr(request, "newFiles", newFiles);

        Set<String> otherDocTypes = DocSettingService.computeOtherDocTypes(approvalAppDocSettings, savedFiles.keySet(), newFiles.keySet());
        ParamUtil.setRequestAttr(request, KEY_OTHER_DOC_TYPES, otherDocTypes);
    }


    @SneakyThrows(JsonProcessingException.class)
    public void loadAllowedScheduleAndBatOptions(HttpServletRequest request) {
        ApprovalSelectionDto approvalSelectionDto = getApprovalSelectionDto(request);
        log.debug("facility id is {}", LogUtil.escapeCrlf(approvalSelectionDto.getFacilityId()));
        log.debug("process Type is {}", LogUtil.escapeCrlf(approvalSelectionDto.getProcessType()));
        Map<String, List<BatCodeInfo>> scheduleBatMap = approvalBatAndActivityClient.queryScheduleBasedBatBasicInfo(approvalSelectionDto.getFacilityId(),getApprovalTypeByProcessType(approvalSelectionDto.getProcessType()));
        List<SelectOption> scheduleTypeOps = MasterCodeHolder.SCHEDULE.customOptions(scheduleBatMap.keySet().toArray(new String[0]));
        ParamUtil.setRequestAttr(request, KEY_OPTIONS_SCHEDULE, scheduleTypeOps);
        ParamUtil.setRequestAttr(request, KEY_SCHEDULE_FIRST_OPTION, scheduleTypeOps.get(0).getValue());

        // convert BatBasicInfo to SelectOption object
        SelectOption pleaseSelect = new SelectOption("", "Please Select");
        Map<String, List<SelectOption>> scheduleBatOptionMap = Maps.newHashMapWithExpectedSize(scheduleBatMap.size());
        for (Map.Entry<String, List<BatCodeInfo>> entry : scheduleBatMap.entrySet()) {
            List<SelectOption> optionList = new ArrayList<>(entry.getValue().size());
            optionList.add(pleaseSelect);
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
    }

    public String getApprovalTypeByProcessType(String processType){
        String approvalType = "";
        switch (processType){
            case MasterCodeConstants.PROCESS_TYPE_APPROVE_POSSESS:
                approvalType = MasterCodeConstants.APPROVAL_TYPE_POSSESS;
                break;
            case MasterCodeConstants.PROCESS_TYPE_APPROVE_LSP:
                approvalType = MasterCodeConstants.APPROVAL_TYPE_LSP;
                break;
            case MasterCodeConstants.PROCESS_TYPE_SP_APPROVE_HANDLE:
                approvalType = MasterCodeConstants.APPROVAL_TYPE_SP_HANDLE;
                break;
            default:break;
        }
        return approvalType;
    }
}

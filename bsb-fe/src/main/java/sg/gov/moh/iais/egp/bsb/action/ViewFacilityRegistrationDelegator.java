package sg.gov.moh.iais.egp.bsb.action;


import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.AuditTrailConsts;
import com.ecquaria.cloud.moh.iais.common.dto.AuditTrailDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenseeDto;
import com.ecquaria.cloud.moh.iais.common.exception.IaisRuntimeException;
import com.ecquaria.cloud.moh.iais.common.utils.MaskUtil;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sg.gov.moh.iais.egp.bsb.client.FacilityRegisterClient;
import sg.gov.moh.iais.egp.bsb.client.OrganizationInfoClient;
import sg.gov.moh.iais.egp.bsb.common.node.NodeGroup;
import sg.gov.moh.iais.egp.bsb.common.node.simple.SimpleNode;
import sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants;
import sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants;
import sg.gov.moh.iais.egp.bsb.dto.ResponseDto;
import sg.gov.moh.iais.egp.bsb.dto.declaration.DeclarationItemMainInfo;
import sg.gov.moh.iais.egp.bsb.dto.file.DocRecordInfo;
import sg.gov.moh.iais.egp.bsb.dto.info.common.OrgAddressInfo;
import sg.gov.moh.iais.egp.bsb.dto.register.facility.BiologicalAgentToxinDto;
import sg.gov.moh.iais.egp.bsb.dto.register.facility.FacilityAuthoriserDto;
import sg.gov.moh.iais.egp.bsb.dto.register.facility.FacilityAuthoriserFileDto;
import sg.gov.moh.iais.egp.bsb.dto.register.facility.FacilityCommitteeDto;
import sg.gov.moh.iais.egp.bsb.dto.register.facility.FacilityCommitteeFileDto;
import sg.gov.moh.iais.egp.bsb.dto.register.facility.FacilityRegisterDto;
import sg.gov.moh.iais.egp.bsb.dto.register.facility.FacilitySelectionDto;
import sg.gov.moh.iais.egp.bsb.dto.register.facility.OtherApplicationInfoDto;
import sg.gov.moh.iais.egp.bsb.dto.register.facility.PrimaryDocDto;
import sg.gov.moh.iais.egp.bsb.entity.DocSetting;
import sg.gov.moh.iais.egp.bsb.service.DocSettingService;
import sg.gov.moh.iais.egp.bsb.service.FacilityRegistrationService;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static sg.gov.moh.iais.egp.bsb.constant.FacRegisterConstants.*;

@Delegator(value = "bsbViewFacRegAppDelegator")
@Slf4j
public class ViewFacilityRegistrationDelegator {
    private static final String MODULE_NAME = "Facility Registration";
    private static final String FUNCTION_NAME = "View Application";

    private final FacilityRegisterClient facRegClient;
    private final OrganizationInfoClient orgInfoClient;
    private final DocSettingService docSettingService;

    @Autowired
    public ViewFacilityRegistrationDelegator(FacilityRegisterClient facRegClient, OrganizationInfoClient orgInfoClient,
                                             DocSettingService docSettingService) {
        this.facRegClient = facRegClient;
        this.orgInfoClient = orgInfoClient;
        this.docSettingService = docSettingService;
    }

    public void start(BaseProcessClass bpc) { // NOSONAR
        AuditTrailHelper.auditFunction(MODULE_NAME, FUNCTION_NAME);
    }

    public void init(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        HttpSession session = request.getSession();
        session.removeAttribute(KEY_APP_ID);
        session.removeAttribute(KEY_MASKED_EDIT_APP_ID);
        session.removeAttribute(KEY_ROOT_NODE_GROUP);

        String maskedAppId = request.getParameter(KEY_APP_ID);
        String appId = MaskUtil.unMaskValue("id", maskedAppId);
        if (maskedAppId == null || appId == null || maskedAppId.equals(appId)) {
            throw new IaisRuntimeException("Invalid App ID");
        }
        ParamUtil.setSessionAttr(request, KEY_APP_ID, appId);

        // check if this app is editable
        String maskedEditAppId = request.getParameter(KEY_EDIT_APP_ID);
        String editAppId = MaskUtil.unMaskValue(KEY_EDIT_APP_ID, maskedEditAppId);
        if (maskedEditAppId != null && editAppId != null &&
                !maskedEditAppId.equals(editAppId) && appId.equals(editAppId)) {
            ParamUtil.setSessionAttr(request, KEY_MASKED_EDIT_APP_ID, maskedEditAppId);
        }
    }

    public NodeGroup getFacilityRegisterRoot(HttpServletRequest request, String appId) {
        NodeGroup root = (NodeGroup) ParamUtil.getSessionAttr(request, KEY_ROOT_NODE_GROUP);
        if (root == null) {
            ResponseDto<FacilityRegisterDto> resultDto = facRegClient.getFacilityRegistrationAppDataByApplicationId(appId);
            if (!resultDto.ok()) {
                throw new IaisRuntimeException("Fail to retrieve app data");
            }
            root = resultDto.getEntity().toFacRegRootGroup(KEY_ROOT_NODE_GROUP);
            ParamUtil.setSessionAttr(request, KEY_ROOT_NODE_GROUP, root);
        }
        return root;
    }

    public void prepareData(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        String appId = (String) ParamUtil.getSessionAttr(request, KEY_APP_ID);
        NodeGroup facRegRoot = getFacilityRegisterRoot(request, appId);

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

        ParamUtil.setRequestAttr(request, NODE_NAME_FAC_PROFILE, ((SimpleNode)facRegRoot.at(NODE_NAME_FAC_INFO + facRegRoot.getPathSeparator() + NODE_NAME_FAC_PROFILE)).getValue());
        ParamUtil.setRequestAttr(request, NODE_NAME_FAC_OPERATOR, ((SimpleNode)facRegRoot.at(NODE_NAME_FAC_INFO + facRegRoot.getPathSeparator() + NODE_NAME_FAC_OPERATOR)).getValue());
        ParamUtil.setRequestAttr(request, NODE_NAME_FAC_ADMIN_OFFICER, ((SimpleNode)facRegRoot.at(NODE_NAME_FAC_INFO + facRegRoot.getPathSeparator() + NODE_NAME_FAC_ADMIN_OFFICER)).getValue());

        FacilitySelectionDto selectionDto = (FacilitySelectionDto) ((SimpleNode) facRegRoot.at(NODE_NAME_FAC_SELECTION)).getValue();

        boolean isCf = MasterCodeConstants.CERTIFIED_CLASSIFICATION.contains(selectionDto.getFacClassification());
        ParamUtil.setRequestAttr(request, KEY_IS_CF, isCf ? Boolean.TRUE : Boolean.FALSE);
        boolean isUcf = MasterCodeConstants.UNCERTIFIED_CLASSIFICATION.contains(selectionDto.getFacClassification());
        ParamUtil.setRequestAttr(request, KEY_IS_UCF, isUcf ? Boolean.TRUE : Boolean.FALSE);
        if (isCf) {
            ParamUtil.setRequestAttr(request, NODE_NAME_AFC, ((SimpleNode) facRegRoot.at(NODE_NAME_AFC)).getValue());
        } else if (isUcf) {
            NodeGroup batNodeGroup = (NodeGroup) facRegRoot.at(NODE_NAME_FAC_BAT_INFO);
            List<BiologicalAgentToxinDto> batList = FacilityRegistrationService.getBatInfoList(batNodeGroup);
            ParamUtil.setRequestAttr(request, KEY_BAT_LIST, batList);
        }

        OtherApplicationInfoDto otherAppInfoDto = (OtherApplicationInfoDto) ((SimpleNode) facRegRoot.at(NODE_NAME_OTHER_INFO)).getValue();
        List<DeclarationItemMainInfo> config = facRegClient.getDeclarationConfigInfoById(otherAppInfoDto.getDeclarationId());
        otherAppInfoDto.setDeclarationConfig(config);
        ParamUtil.setRequestAttr(request, KEY_DECLARATION_CONFIG, otherAppInfoDto.getDeclarationConfig());
        ParamUtil.setRequestAttr(request, KEY_DECLARATION_ANSWER_MAP, otherAppInfoDto.getAnswerMap());

        List<DocSetting> facRegDocSetting = docSettingService.getFacRegDocSettings(selectionDto.getFacClassification());
        ParamUtil.setRequestAttr(request, KEY_DOC_SETTINGS, facRegDocSetting);
        PrimaryDocDto primaryDocDto = (PrimaryDocDto) ((SimpleNode)facRegRoot.at(NODE_NAME_PRIMARY_DOC)).getValue();
        Map<String, List<DocRecordInfo>> savedFiles = primaryDocDto.getExistDocTypeMap();
        ParamUtil.setRequestAttr(request, KEY_FILE_MAP_SAVED, savedFiles);
        Set<String> otherDocTypes = DocSettingService.computeOtherDocTypes(facRegDocSetting, savedFiles.keySet());
        ParamUtil.setRequestAttr(request, KEY_OTHER_DOC_TYPES, otherDocTypes);
    }

    public void handleAction(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        String actionType = ParamUtil.getString(request, ModuleCommonConstants.KEY_ACTION_TYPE);
        if (KEY_ACTION_EXPAND_FILE.equals(actionType)) {
            String actionValue = ParamUtil.getString(request, ModuleCommonConstants.KEY_ACTION_VALUE);
            if ("bsbCommittee".equals(actionValue)) {
                ParamUtil.setRequestAttr(request, KEY_INDEED_ACTION_TYPE, "committee");
            } else if ("facAuth".equals(actionValue)) {
                ParamUtil.setRequestAttr(request, KEY_INDEED_ACTION_TYPE, "authoriser");
            } else {
                throw new IaisRuntimeException("Invalid action value");
            }
        } else {
            throw new IaisRuntimeException("Invalid action type");
        }
    }

    public void preCommitteeInfo(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        String appId = (String) ParamUtil.getSessionAttr(request, KEY_APP_ID);
        NodeGroup facRegRoot = getFacilityRegisterRoot(request, appId);
        FacilityCommitteeDto facCommitteeDto = (FacilityCommitteeDto) ((SimpleNode)facRegRoot.at(NODE_NAME_FAC_INFO + facRegRoot.getPathSeparator() + NODE_NAME_FAC_COMMITTEE)).getValue();
        List<FacilityCommitteeFileDto> dataList = facCommitteeDto.getDataListForDisplay();
        ParamUtil.setRequestAttr(request, KEY_DATA_LIST, dataList);
    }

    public void preAuthoriserInfo(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        String appId = (String) ParamUtil.getSessionAttr(request, KEY_APP_ID);
        NodeGroup facRegRoot = getFacilityRegisterRoot(request, appId);
        FacilityAuthoriserDto facAuthDto = (FacilityAuthoriserDto) ((SimpleNode)facRegRoot.at(NODE_NAME_FAC_INFO + facRegRoot.getPathSeparator() + NODE_NAME_FAC_AUTH)).getValue();
        List<FacilityAuthoriserFileDto> dataList = facAuthDto.getDataListForDisplay();
        ParamUtil.setRequestAttr(request, KEY_DATA_LIST, dataList);
    }
}

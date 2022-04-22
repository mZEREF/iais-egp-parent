package sg.gov.moh.iais.egp.bsb.action;


import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.exception.IaisRuntimeException;
import com.ecquaria.cloud.moh.iais.common.utils.MaskUtil;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sg.gov.moh.iais.egp.bsb.client.FacilityRegisterClient;
import sg.gov.moh.iais.egp.bsb.common.node.NodeGroup;
import sg.gov.moh.iais.egp.bsb.common.node.simple.SimpleNode;
import sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants;
import sg.gov.moh.iais.egp.bsb.dto.ResponseDto;
import sg.gov.moh.iais.egp.bsb.dto.declaration.DeclarationItemMainInfo;
import sg.gov.moh.iais.egp.bsb.dto.file.DocRecordInfo;
import sg.gov.moh.iais.egp.bsb.dto.info.common.OrgAddressInfo;
import sg.gov.moh.iais.egp.bsb.dto.register.facility.BiologicalAgentToxinDto;
import sg.gov.moh.iais.egp.bsb.dto.register.facility.FacilityRegisterDto;
import sg.gov.moh.iais.egp.bsb.dto.register.facility.FacilitySelectionDto;
import sg.gov.moh.iais.egp.bsb.dto.register.facility.OtherApplicationInfoDto;
import sg.gov.moh.iais.egp.bsb.dto.register.facility.PrimaryDocDto;
import sg.gov.moh.iais.egp.bsb.service.DocSettingService;
import sg.gov.moh.iais.egp.bsb.service.FacilityRegistrationService;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;

import java.util.List;
import java.util.Map;

import static sg.gov.moh.iais.egp.bsb.constant.FacRegisterConstants.*;

@Delegator(value = "bsbViewFacRegAppDelegator")
@Slf4j
public class ViewFacilityRegistrationDelegator {
    private static final String MODULE_NAME = "Facility Registration";
    private static final String FUNCTION_NAME = "View Application";

    private final FacilityRegisterClient facRegClient;
    private final DocSettingService docSettingService;

    @Autowired
    public ViewFacilityRegistrationDelegator(FacilityRegisterClient facRegClient,
                                             DocSettingService docSettingService) {
        this.facRegClient = facRegClient;
        this.docSettingService = docSettingService;
    }

    public void start(BaseProcessClass bpc) { // NOSONAR
        AuditTrailHelper.auditFunction(MODULE_NAME, FUNCTION_NAME);
    }

    public void init(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        String maskedAppId = request.getParameter(KEY_APP_ID);
        String appId = MaskUtil.unMaskValue("id", maskedAppId);
        if (maskedAppId == null || appId == null || maskedAppId.equals(appId)) {
            throw new IaisRuntimeException("Invalid App ID");
        }
        ParamUtil.setRequestAttr(request, KEY_APP_ID, appId);

        // check if this app is editable
        String maskedEditAppId = request.getParameter(KEY_EDIT_APP_ID);
        String editAppId = MaskUtil.unMaskValue(KEY_EDIT_APP_ID, maskedEditAppId);
        if (maskedEditAppId != null && editAppId != null &&
                !maskedEditAppId.equals(editAppId) && appId.equals(editAppId)) {
            ParamUtil.setRequestAttr(request, KEY_MASKED_EDIT_APP_ID, maskedEditAppId);
        }
    }

    public void prepareData(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        String appId = (String) ParamUtil.getRequestAttr(request, KEY_APP_ID);

        // retrieve app data of facility registration
        ResponseDto<FacilityRegisterDto> resultDto = facRegClient.getFacilityRegistrationAppDataByApplicationId(appId);
        if (resultDto.ok()) {
            // TODO retrieve company address
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
            ParamUtil.setRequestAttr(request, KEY_ORG_ADDRESS, orgAddressInfo);


            NodeGroup facRegRoot = resultDto.getEntity().toFacRegRootGroup(KEY_ROOT_NODE_GROUP);

            ParamUtil.setRequestAttr(request, NODE_NAME_FAC_PROFILE, ((SimpleNode)facRegRoot.at(NODE_NAME_FAC_INFO + facRegRoot.getPathSeparator() + NODE_NAME_FAC_PROFILE)).getValue());
            ParamUtil.setRequestAttr(request, NODE_NAME_FAC_OPERATOR, ((SimpleNode)facRegRoot.at(NODE_NAME_FAC_INFO + facRegRoot.getPathSeparator() + NODE_NAME_FAC_OPERATOR)).getValue());
            ParamUtil.setRequestAttr(request, NODE_NAME_FAC_AUTH, ((SimpleNode)facRegRoot.at(NODE_NAME_FAC_INFO + facRegRoot.getPathSeparator() + NODE_NAME_FAC_AUTH)).getValue());
            ParamUtil.setRequestAttr(request, NODE_NAME_FAC_ADMIN_OFFICER, ((SimpleNode)facRegRoot.at(NODE_NAME_FAC_INFO + facRegRoot.getPathSeparator() + NODE_NAME_FAC_ADMIN_OFFICER)).getValue());
            ParamUtil.setRequestAttr(request, NODE_NAME_FAC_COMMITTEE, ((SimpleNode)facRegRoot.at(NODE_NAME_FAC_INFO + facRegRoot.getPathSeparator() + NODE_NAME_FAC_COMMITTEE)).getValue());

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
                ParamUtil.setRequestAttr(request, "batList", batList);
            }

            OtherApplicationInfoDto otherAppInfoDto = (OtherApplicationInfoDto) ((SimpleNode) facRegRoot.at(NODE_NAME_OTHER_INFO)).getValue();
            List<DeclarationItemMainInfo> config = facRegClient.getDeclarationConfigInfoById(otherAppInfoDto.getDeclarationId());
            otherAppInfoDto.setDeclarationConfig(config);
            ParamUtil.setRequestAttr(request, KEY_DECLARATION_CONFIG, otherAppInfoDto.getDeclarationConfig());
            ParamUtil.setRequestAttr(request, KEY_DECLARATION_ANSWER_MAP, otherAppInfoDto.getAnswerMap());

            ParamUtil.setRequestAttr(request, "docSettings", docSettingService.getFacRegDocSettings());
            PrimaryDocDto primaryDocDto = (PrimaryDocDto) ((SimpleNode)facRegRoot.at(NODE_NAME_PRIMARY_DOC)).getValue();
            Map<String, List<DocRecordInfo>> savedFiles = primaryDocDto.getExistDocTypeMap();
            ParamUtil.setRequestAttr(request, "savedFiles", savedFiles);
        } else {
            throw new IaisRuntimeException("Fail to retrieve app data");
        }
    }
}

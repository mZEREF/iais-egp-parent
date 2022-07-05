package sg.gov.moh.iais.egp.bsb.action;


import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.exception.IaisRuntimeException;
import com.ecquaria.cloud.moh.iais.common.utils.MaskUtil;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import sg.gov.moh.iais.egp.bsb.client.FacilityRegisterClient;
import sg.gov.moh.iais.egp.bsb.common.node.NodeGroup;
import sg.gov.moh.iais.egp.bsb.common.node.simple.SimpleNode;
import sg.gov.moh.iais.egp.bsb.dto.ResponseDto;
import sg.gov.moh.iais.egp.bsb.dto.file.DocRecordInfo;
import sg.gov.moh.iais.egp.bsb.dto.register.bat.BiologicalAgentToxinDto;
import sg.gov.moh.iais.egp.bsb.dto.register.facility.FacilityRegisterDto;
import sg.gov.moh.iais.egp.bsb.dto.register.facility.PrimaryDocDto;
import sg.gov.moh.iais.egp.bsb.service.FacilityRegistrationService;
import sg.gov.moh.iais.egp.bsb.util.MaskHelper;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

import static sg.gov.moh.iais.egp.bsb.constant.FacRegisterConstants.KEY_EDIT_APP_ID;
import static sg.gov.moh.iais.egp.bsb.constant.FacRegisterConstants.KEY_PROCESS_TYPE;
import static sg.gov.moh.iais.egp.bsb.constant.FacRegisterConstants.KEY_ROOT_NODE_GROUP;
import static sg.gov.moh.iais.egp.bsb.constant.FacRegisterConstants.NODE_NAME_FAC_ADMIN_OFFICER;
import static sg.gov.moh.iais.egp.bsb.constant.FacRegisterConstants.NODE_NAME_FAC_AUTH;
import static sg.gov.moh.iais.egp.bsb.constant.FacRegisterConstants.NODE_NAME_FAC_BAT_INFO;
import static sg.gov.moh.iais.egp.bsb.constant.FacRegisterConstants.NODE_NAME_FAC_COMMITTEE;
import static sg.gov.moh.iais.egp.bsb.constant.FacRegisterConstants.NODE_NAME_FAC_INFO;
import static sg.gov.moh.iais.egp.bsb.constant.FacRegisterConstants.NODE_NAME_FAC_OPERATOR;
import static sg.gov.moh.iais.egp.bsb.constant.FacRegisterConstants.NODE_NAME_FAC_PROFILE;
import static sg.gov.moh.iais.egp.bsb.constant.FacRegisterConstants.NODE_NAME_PRIMARY_DOC;
import static sg.gov.moh.iais.egp.bsb.constant.module.ViewApplicationConstants.KEY_APPROVE_NO;
import static sg.gov.moh.iais.egp.bsb.constant.module.ViewApplicationConstants.KEY_APP_ID;
import static sg.gov.moh.iais.egp.bsb.constant.module.ViewApplicationConstants.KEY_MASKED_EDIT_APP_ID;

@Delegator(value = "rfcViewFacRegAppDelegator")
@Slf4j
public class RfcViewFacilityRegistrationDelegator {
    private static final String MODULE_NAME = "RFC Facility Registration";
    private static final String FUNCTION_NAME = "RFC View Application";

    private final FacilityRegisterClient facRegClient;

    @Autowired
    public RfcViewFacilityRegistrationDelegator(FacilityRegisterClient facRegClient) {
        this.facRegClient = facRegClient;
    }

    public void start(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        AuditTrailHelper.auditFunction(MODULE_NAME, FUNCTION_NAME);
        request.getSession().removeAttribute(KEY_APPROVE_NO);
    }

    public void init(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        String maskedAppId = request.getParameter(KEY_APP_ID);
        String appId = MaskHelper.unmask("id", maskedAppId);
        ParamUtil.setRequestAttr(request, KEY_APP_ID, appId);

        // check if this app is editable
        String maskedEditAppId = request.getParameter(KEY_EDIT_APP_ID);
        if (StringUtils.hasLength(maskedEditAppId)) {
            String editAppId = MaskUtil.unMaskValue(KEY_EDIT_APP_ID, maskedEditAppId);
            if (appId.equals(editAppId)) {
                ParamUtil.setRequestAttr(request, KEY_MASKED_EDIT_APP_ID, maskedEditAppId);
            }
        }

        //rfc dashbord processType
        String maskProcessType = request.getParameter(KEY_PROCESS_TYPE);
        String processType = MaskUtil.unMaskValue(KEY_PROCESS_TYPE,maskProcessType);
        ParamUtil.setRequestAttr(request, KEY_PROCESS_TYPE, processType);

        //rfc approveNo
        String approveNo = request.getParameter(KEY_APPROVE_NO);
        ParamUtil.setSessionAttr(request,KEY_APPROVE_NO,approveNo);
    }

    public void prepareData(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        String appId = (String) ParamUtil.getRequestAttr(request, KEY_APP_ID);

        // retrieve app data of facility registration
        ResponseDto<FacilityRegisterDto> resultDto = facRegClient.getFacilityRegistrationAppDataByApprovalId(appId);
        if (resultDto.ok()) {
            NodeGroup facRegRoot = FacilityRegistrationService.readRegisterDtoToNodeGroup(resultDto.getEntity(), KEY_ROOT_NODE_GROUP);

            ParamUtil.setRequestAttr(request, NODE_NAME_FAC_PROFILE, ((SimpleNode)facRegRoot.at(NODE_NAME_FAC_INFO + facRegRoot.getPathSeparator() + NODE_NAME_FAC_PROFILE)).getValue());
            ParamUtil.setRequestAttr(request, NODE_NAME_FAC_OPERATOR, ((SimpleNode)facRegRoot.at(NODE_NAME_FAC_INFO + facRegRoot.getPathSeparator() + NODE_NAME_FAC_OPERATOR)).getValue());
            ParamUtil.setRequestAttr(request, NODE_NAME_FAC_AUTH, ((SimpleNode)facRegRoot.at(NODE_NAME_FAC_INFO + facRegRoot.getPathSeparator() + NODE_NAME_FAC_AUTH)).getValue());
            ParamUtil.setRequestAttr(request, NODE_NAME_FAC_ADMIN_OFFICER, ((SimpleNode)facRegRoot.at(NODE_NAME_FAC_INFO + facRegRoot.getPathSeparator() + NODE_NAME_FAC_ADMIN_OFFICER)).getValue());
            ParamUtil.setRequestAttr(request, NODE_NAME_FAC_COMMITTEE, ((SimpleNode)facRegRoot.at(NODE_NAME_FAC_INFO + facRegRoot.getPathSeparator() + NODE_NAME_FAC_COMMITTEE)).getValue());

            NodeGroup batNodeGroup = (NodeGroup) facRegRoot.at(NODE_NAME_FAC_BAT_INFO);
            List<BiologicalAgentToxinDto> batList = FacilityRegistrationService.getBatInfoList(batNodeGroup);
            ParamUtil.setRequestAttr(request, "batList", batList);

            PrimaryDocDto primaryDocDto = (PrimaryDocDto) ((SimpleNode)facRegRoot.at(NODE_NAME_PRIMARY_DOC)).getValue();
            Map<String, List<DocRecordInfo>> savedFiles = primaryDocDto.getExistDocTypeMap();
            ParamUtil.setRequestAttr(request, "savedFiles", savedFiles);
        } else {
            throw new IaisRuntimeException("Fail to retrieve app data");
        }
    }
}

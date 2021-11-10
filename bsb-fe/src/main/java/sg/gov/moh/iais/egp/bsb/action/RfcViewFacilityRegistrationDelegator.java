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
import sg.gov.moh.iais.egp.bsb.dto.ResponseDto;
import sg.gov.moh.iais.egp.bsb.dto.register.facility.BiologicalAgentToxinDto;
import sg.gov.moh.iais.egp.bsb.dto.register.facility.FacilityRegisterDto;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

import static sg.gov.moh.iais.egp.bsb.constant.FacRegisterConstants.*;

@Delegator(value = "rfcViewFacRegAppDelegator")
@Slf4j
public class RfcViewFacilityRegistrationDelegator {
    private static final String KEY_ROOT_NODE_GROUP = "facRegRoot";
    private static final String KEY_APP_ID = "appId";
    private static final String KEY_EDIT_APP_ID = "editId";
    private static final String KEY_MASKED_EDIT_APP_ID = "maskedEditId";
    private static final String KEY_APPROVE_NO = "approveNo";

    private final FacilityRegisterClient facRegClient;

    @Autowired
    public RfcViewFacilityRegistrationDelegator(FacilityRegisterClient facRegClient) {
        this.facRegClient = facRegClient;
    }

    public void start(BaseProcessClass bpc) { // NOSONAR
        HttpServletRequest request = bpc.request;
        AuditTrailHelper.auditFunction("Facility Registration", "View Application");
        request.getSession().removeAttribute(KEY_APPROVE_NO);
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
            NodeGroup facRegRoot = resultDto.getEntity().toFacRegRootGroup(KEY_ROOT_NODE_GROUP);

            ParamUtil.setRequestAttr(request, NODE_NAME_FAC_PROFILE, ((SimpleNode)facRegRoot.at(NODE_NAME_FAC_INFO + facRegRoot.getPathSeparator() + NODE_NAME_FAC_PROFILE)).getValue());
            ParamUtil.setRequestAttr(request, NODE_NAME_FAC_OPERATOR, ((SimpleNode)facRegRoot.at(NODE_NAME_FAC_INFO + facRegRoot.getPathSeparator() + NODE_NAME_FAC_OPERATOR)).getValue());
            ParamUtil.setRequestAttr(request, NODE_NAME_FAC_AUTH, ((SimpleNode)facRegRoot.at(NODE_NAME_FAC_INFO + facRegRoot.getPathSeparator() + NODE_NAME_FAC_AUTH)).getValue());
            ParamUtil.setRequestAttr(request, NODE_NAME_FAC_ADMIN, ((SimpleNode)facRegRoot.at(NODE_NAME_FAC_INFO + facRegRoot.getPathSeparator() + NODE_NAME_FAC_ADMIN)).getValue());
            ParamUtil.setRequestAttr(request, NODE_NAME_FAC_OFFICER, ((SimpleNode)facRegRoot.at(NODE_NAME_FAC_INFO + facRegRoot.getPathSeparator() + NODE_NAME_FAC_OFFICER)).getValue());
            ParamUtil.setRequestAttr(request, NODE_NAME_FAC_COMMITTEE, ((SimpleNode)facRegRoot.at(NODE_NAME_FAC_INFO + facRegRoot.getPathSeparator() + NODE_NAME_FAC_COMMITTEE)).getValue());

            NodeGroup batNodeGroup = (NodeGroup) facRegRoot.at(NODE_NAME_FAC_BAT_INFO);
            List<BiologicalAgentToxinDto> batList = FacilityRegistrationDelegator.getBatInfoList(batNodeGroup);
            ParamUtil.setRequestAttr(request, "batList", batList);
        } else {
            throw new IaisRuntimeException("Fail to retrieve app data");
        }
    }
}

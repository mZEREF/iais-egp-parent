package sg.gov.moh.iais.egp.bsb.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.exception.IaisRuntimeException;
import com.ecquaria.cloud.moh.iais.common.utils.MaskUtil;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sg.gov.moh.iais.egp.bsb.client.FacCertifierRegisterClient;
import sg.gov.moh.iais.egp.bsb.common.node.NodeGroup;
import sg.gov.moh.iais.egp.bsb.common.node.simple.SimpleNode;
import sg.gov.moh.iais.egp.bsb.dto.ResponseDto;
import sg.gov.moh.iais.egp.bsb.dto.register.afc.FacilityCertifierRegisterDto;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import static sg.gov.moh.iais.egp.bsb.constant.FacCertifierRegisterConstants.*;
import static sg.gov.moh.iais.egp.bsb.constant.FacCertifierRegisterConstants.NODE_NAME_ORG_FAC_ADMINISTRATOR;

/**
 * @author YiMing
 * @version 2021/10/22 10:38
 **/

@Delegator(value = "bsbViewCertRegAppDelegator")
@Slf4j
public class ViewCertifierRegistrationDelegator {
    private static final String KEY_ROOT_NODE_GROUP = "facRegRoot";
    private static final String KEY_APP_ID = "appId";
    private static final String KEY_EDIT_APP_ID = "editId";
    private static final String KEY_MASKED_EDIT_APP_ID = "maskedEditId";

    private final FacCertifierRegisterClient certifierRegisterClient;

    @Autowired
    public ViewCertifierRegistrationDelegator(FacCertifierRegisterClient certifierRegisterClient) {
        this.certifierRegisterClient = certifierRegisterClient;
    }



    public void init(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        //get maskAppId
        String maskedAppId = request.getParameter(KEY_APP_ID);
        String appId = MaskUtil.unMaskValue("id",maskedAppId);
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

    public void prepareData(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        String appId = (String) ParamUtil.getRequestAttr(request, KEY_APP_ID);

        // retrieve app data of facility registration
        ResponseDto<FacilityCertifierRegisterDto> resultDto = certifierRegisterClient.getCertifierRegistrationAppData(appId);
        if (resultDto.ok()) {
            NodeGroup facRegRoot = resultDto.getEntity().toFacilityCertRegister(KEY_ROOT_NODE_GROUP);
            ParamUtil.setRequestAttr(request, NODE_NAME_ORG_PROFILE, ((SimpleNode) facRegRoot.at(NODE_NAME_ORGANISATION_INFO + facRegRoot.getPathSeparator() + NODE_NAME_ORG_PROFILE)).getValue());
            ParamUtil.setRequestAttr(request, NODE_NAME_ORG_CERTIFYING_TEAM, ((SimpleNode) facRegRoot.at(NODE_NAME_ORGANISATION_INFO + facRegRoot.getPathSeparator() + NODE_NAME_ORG_CERTIFYING_TEAM)).getValue());
            ParamUtil.setRequestAttr(request, NODE_NAME_ORG_FAC_ADMINISTRATOR, ((SimpleNode) facRegRoot.at(NODE_NAME_ORGANISATION_INFO + facRegRoot.getPathSeparator() + NODE_NAME_ORG_FAC_ADMINISTRATOR)).getValue());
        } else {
            throw new IaisRuntimeException("Fail to retrieve app data");
        }
    }

}

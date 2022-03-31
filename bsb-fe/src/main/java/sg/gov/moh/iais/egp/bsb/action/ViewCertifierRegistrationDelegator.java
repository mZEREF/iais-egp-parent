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
import sg.gov.moh.iais.egp.bsb.dto.file.DocRecordInfo;
import sg.gov.moh.iais.egp.bsb.dto.register.afc.FacilityCertifierRegisterDto;
import sg.gov.moh.iais.egp.bsb.dto.register.afc.PrimaryDocDto;
import sg.gov.moh.iais.egp.bsb.service.DocSettingService;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

import static sg.gov.moh.iais.egp.bsb.constant.FacCertifierRegisterConstants.*;

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
    private final DocSettingService docSettingService;

    @Autowired
    public ViewCertifierRegistrationDelegator(FacCertifierRegisterClient certifierRegisterClient, DocSettingService docSettingService) {
        this.certifierRegisterClient = certifierRegisterClient;
        this.docSettingService = docSettingService;
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
            NodeGroup facRegRoot = resultDto.getEntity().toFacilityCertRegisterGroup(KEY_ROOT_NODE_GROUP);
            ParamUtil.setRequestAttr(request, NODE_NAME_COMPANY_PROFILE, ((SimpleNode) facRegRoot.at(NODE_NAME_APPLICATION_INFO + facRegRoot.getPathSeparator() + NODE_NAME_COMPANY_PROFILE)).getValue());
            ParamUtil.setRequestAttr(request, NODE_NAME_CERTIFYING_TEAM_DETAIL, ((SimpleNode) facRegRoot.at(NODE_NAME_APPLICATION_INFO + facRegRoot.getPathSeparator() + NODE_NAME_CERTIFYING_TEAM_DETAIL)).getValue());
            ParamUtil.setRequestAttr(request, NODE_NAME_ADMINISTRATOR, ((SimpleNode) facRegRoot.at(NODE_NAME_APPLICATION_INFO + facRegRoot.getPathSeparator() + NODE_NAME_ADMINISTRATOR)).getValue());

            ParamUtil.setRequestAttr(request, "docSettings", docSettingService.getFacCerRegDocSettings());
            PrimaryDocDto primaryDocDto = (PrimaryDocDto) ((SimpleNode)facRegRoot.at(NODE_NAME_SUPPORTING_DOCUMENT)).getValue();
            Map<String, List<DocRecordInfo>> savedFiles = primaryDocDto.getExistDocTypeMap();
            ParamUtil.setRequestAttr(request, "savedFiles", savedFiles);
        } else {
            throw new IaisRuntimeException("Fail to retrieve app data");
        }
    }

}

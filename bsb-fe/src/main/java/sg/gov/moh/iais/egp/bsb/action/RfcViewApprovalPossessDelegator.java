package sg.gov.moh.iais.egp.bsb.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.exception.IaisRuntimeException;
import com.ecquaria.cloud.moh.iais.common.utils.MaskUtil;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sg.gov.moh.iais.egp.bsb.client.ApprovalAppClient;
import sg.gov.moh.iais.egp.bsb.common.node.NodeGroup;
import sg.gov.moh.iais.egp.bsb.common.node.simple.SimpleNode;
import sg.gov.moh.iais.egp.bsb.dto.ResponseDto;
import sg.gov.moh.iais.egp.bsb.dto.approval.ApprovalAppDto;
import sg.gov.moh.iais.egp.bsb.dto.approval.ApprovalProfileDto;
import sg.gov.moh.iais.egp.bsb.service.ApprovalAppService;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

import static sg.gov.moh.iais.egp.bsb.constant.ApprovalAppConstants.*;

/**
 * @author : LiRan
 * @date : 2021/11/12
 */
@Delegator(value = "rfcViewApprovalPossessDelegator")
@Slf4j
public class RfcViewApprovalPossessDelegator {
    private static final String MODULE_NAME = "RFC Approval Application";
    private static final String FUNCTION_NAME = "RFC View Application";

    private final ApprovalAppClient approvalAppClient;

    @Autowired
    public RfcViewApprovalPossessDelegator(ApprovalAppClient approvalAppClient) {
        this.approvalAppClient = approvalAppClient;
    }

    public void start(BaseProcessClass bpc) { // NOSONAR
        HttpServletRequest request = bpc.request;
        AuditTrailHelper.auditFunction(MODULE_NAME, FUNCTION_NAME);
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

        // retrieve app data of approval application
        ResponseDto<ApprovalAppDto> resultDto = approvalAppClient.getApprovalAppAppDataByApprovalId(appId);
        if (resultDto.ok()) {
            NodeGroup approvalAppRoot = resultDto.getEntity().toApprovalAppRootGroup(KEY_ROOT_NODE_GROUP);

            ParamUtil.setRequestAttr(request, NODE_NAME_ACTIVITY, ((SimpleNode)approvalAppRoot.at(NODE_NAME_ACTIVITY)).getValue());

            NodeGroup batNodeGroup = (NodeGroup) approvalAppRoot.at(NODE_NAME_APPROVAL_PROFILE);
            List<ApprovalProfileDto> approvalProfileList = ApprovalAppService.getApprovalProfileList(batNodeGroup);
            ParamUtil.setRequestAttr(request, "approvalProfileList", approvalProfileList);
        } else {
            throw new IaisRuntimeException("Fail to retrieve app data");
        }
    }
}

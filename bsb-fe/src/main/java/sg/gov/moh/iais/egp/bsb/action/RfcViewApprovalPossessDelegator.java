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
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

import static sg.gov.moh.iais.egp.bsb.constant.ApprovalAppConstants.NODE_NAME_ACTIVITY;
import static sg.gov.moh.iais.egp.bsb.constant.ApprovalAppConstants.NODE_NAME_APPROVAL_PROFILE;

/**
 * @author : LiRan
 * @date : 2021/11/12
 */
@Delegator(value = "rfcViewApprovalPossessDelegator")
@Slf4j
public class RfcViewApprovalPossessDelegator {
    private static final String KEY_ROOT_NODE_GROUP = "approvalAppRoot";
    private static final String KEY_APP_ID = "appId";
    private static final String KEY_EDIT_APP_ID = "editId";
    private static final String KEY_MASKED_EDIT_APP_ID = "maskedEditId";
    private static final String KEY_PROCESS_TYPE = "processType";
    private static final String KEY_APPROVE_NO = "approveNo";

    private final ApprovalAppClient approvalAppClient;

    @Autowired
    public RfcViewApprovalPossessDelegator(ApprovalAppClient approvalAppClient) {
        this.approvalAppClient = approvalAppClient;
    }

    public void start(BaseProcessClass bpc) { // NOSONAR
        HttpServletRequest request = bpc.request;
        AuditTrailHelper.auditFunction("Approval new application RFC", "View Application");
        request.getSession().removeAttribute(KEY_APPROVE_NO);
    }

    public void init(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        String maskedAppId = request.getParameter(KEY_APP_ID);
        String maskProcessType = request.getParameter(KEY_PROCESS_TYPE);
        String appId = MaskUtil.unMaskValue("id", maskedAppId);
        String processType = MaskUtil.unMaskValue(KEY_PROCESS_TYPE,maskProcessType);
        if (maskedAppId == null || appId == null || maskedAppId.equals(appId)) {
            throw new IaisRuntimeException("Invalid App ID");
        }
        ParamUtil.setRequestAttr(request, KEY_APP_ID, appId);
        ParamUtil.setRequestAttr(request, KEY_PROCESS_TYPE, processType);
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

        // retrieve app data of approval application
        ResponseDto<ApprovalAppDto> resultDto = approvalAppClient.getApprovalAppAppDataByApprovalId(appId);
        if (resultDto.ok()) {
            NodeGroup approvalAppRoot = resultDto.getEntity().toApprovalAppRootGroup(KEY_ROOT_NODE_GROUP);

            ParamUtil.setRequestAttr(request, NODE_NAME_ACTIVITY, ((SimpleNode)approvalAppRoot.at(NODE_NAME_ACTIVITY)).getValue());

            NodeGroup batNodeGroup = (NodeGroup) approvalAppRoot.at(NODE_NAME_APPROVAL_PROFILE);
            List<ApprovalProfileDto> approvalProfileList = ApprovalAppDelegator.getApprovalProfileList(batNodeGroup);
            ParamUtil.setRequestAttr(request, "approvalProfileList", approvalProfileList);
        } else {
            throw new IaisRuntimeException("Fail to retrieve app data");
        }
    }
}

package sg.gov.moh.iais.egp.bsb.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sg.gov.moh.iais.egp.bsb.client.AppViewClient;
import sg.gov.moh.iais.egp.bsb.dto.register.approval.ApprovalBatAndActivityDto;
import sg.gov.moh.iais.egp.bsb.service.AppViewService;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import static sg.gov.moh.iais.egp.bsb.constant.module.AppViewConstants.KEY_TASK_TYPE;
import static sg.gov.moh.iais.egp.bsb.constant.module.AppViewConstants.MASK_PARAM_APP_ID;


@Slf4j
@Delegator("bsbBeViewApprovalAppDelegator")
public class BsbBeViewApprovalAppDelegator {
    private final AppViewService appViewService;
    private final AppViewClient appViewClient;

    @Autowired
    public BsbBeViewApprovalAppDelegator(AppViewService appViewService, AppViewClient appViewClient) {
        this.appViewService = appViewService;
        this.appViewClient = appViewClient;
    }

    public void start(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        HttpSession session = request.getSession();
        session.removeAttribute(MASK_PARAM_APP_ID);
        session.removeAttribute(KEY_TASK_TYPE);
        AuditTrailHelper.auditFunction("View Application", "Approval Application");
    }

    public void init(BaseProcessClass bpc) {
        appViewService.init(bpc);
    }

    public void prepareData(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        String appId = (String) ParamUtil.getSessionAttr(request, MASK_PARAM_APP_ID);
        String taskType = (String) ParamUtil.getSessionAttr(request, KEY_TASK_TYPE);
        if (taskType != null) {
            // need judge has rfi
            if (appViewClient.hasCompletedRfi(appId, taskType)) {
                ApprovalBatAndActivityDto oldDto = appViewClient.getOldApprovalBatAndActivityData(appId).getEntity();
                appViewService.retrieveRfiApprovalBatAndActivity(request, appId, oldDto);
                ParamUtil.setRequestAttr(request, "isRFI", true);
            } else {
                appViewService.retrieveApprovalBatAndActivity(request, appId);
            }
        } else {
            appViewService.retrieveApprovalBatAndActivity(request, appId);
        }
    }
}

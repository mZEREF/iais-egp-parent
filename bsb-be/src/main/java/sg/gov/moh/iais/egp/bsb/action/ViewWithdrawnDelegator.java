package sg.gov.moh.iais.egp.bsb.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.utils.MaskUtil;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sg.gov.moh.iais.egp.bsb.service.AppViewService;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;

import static sg.gov.moh.iais.egp.bsb.constant.module.AppViewConstants.FUNCTION_NAME;
import static sg.gov.moh.iais.egp.bsb.constant.module.AppViewConstants.MASK_PARAM_APP_ID;
import static sg.gov.moh.iais.egp.bsb.constant.module.AppViewConstants.MODULE_NAME;
import static sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants.KEY_DOC_DISPLAY_DTO_REPO_ID_NAME_MAP;

@Delegator("viewWithdrawnDelegator")
@Slf4j
public class ViewWithdrawnDelegator {
    private final AppViewService appViewService;

    @Autowired
    public ViewWithdrawnDelegator(AppViewService appViewService) {
        this.appViewService = appViewService;
    }

    public void start(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        request.getSession().removeAttribute(KEY_DOC_DISPLAY_DTO_REPO_ID_NAME_MAP);
        AuditTrailHelper.auditFunction(MODULE_NAME, FUNCTION_NAME);
    }

    public void prepareViewForm(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        String maskedAppId = request.getParameter(MASK_PARAM_APP_ID);
        if (org.springframework.util.StringUtils.hasLength(maskedAppId)){
            String appId = MaskUtil.unMaskValue(MASK_PARAM_APP_ID, maskedAppId);
            appViewService.retrieveWithdrawnData(request,appId);
        }
    }
}

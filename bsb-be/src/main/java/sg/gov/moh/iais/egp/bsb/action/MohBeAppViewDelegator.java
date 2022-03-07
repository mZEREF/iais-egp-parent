package sg.gov.moh.iais.egp.bsb.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.utils.MaskUtil;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import sg.gov.moh.iais.egp.bsb.constant.module.AppViewConstants;
import sg.gov.moh.iais.egp.bsb.service.AppViewService;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;

import static sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants.*;
import static sg.gov.moh.iais.egp.bsb.constant.module.AppViewConstants.*;


@Delegator
@Slf4j
public class MohBeAppViewDelegator {
    private final AppViewService appViewService;

    @Autowired
    public MohBeAppViewDelegator(AppViewService appViewService) {
        this.appViewService = appViewService;
    }

    public void start(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        request.getSession().removeAttribute(KEY_PRIMARY_DOC_DTO);
        AuditTrailHelper.auditFunction(MODULE_NAME, FUNCTION_NAME);
    }

    public void prepareViewForm(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        String maskedAppId = request.getParameter(MASK_PARAM_APP_ID);
        String maskedAppViewModuleType = request.getParameter(MASK_PARAM_APP_VIEW_MODULE_TYPE);
        if (org.springframework.util.StringUtils.hasLength(maskedAppId) && org.springframework.util.StringUtils.hasLength(maskedAppViewModuleType)){
            String appId = MaskUtil.unMaskValue(MASK_PARAM_APP_ID, maskedAppId);
            String appViewModuleType = MaskUtil.unMaskValue(MASK_PARAM_APP_VIEW_MODULE_TYPE, maskedAppViewModuleType);
            ParamUtil.setRequestAttr(request, AppViewConstants.MASK_PARAM_APP_VIEW_MODULE_TYPE, appViewModuleType);
            switch (appViewModuleType) {
                case MODULE_VIEW_NEW_FACILITY:
                    appViewService.retrieveFacReg(request, appId);
                    break;
                case MODULE_VIEW_DEREGISTRATION_FACILITY:
                    appViewService.retrieveDeregistrationFac(request, appId);
                    break;
                case MODULE_VIEW_NEW_APPROVAL_APP:
                    appViewService.retrieveApprovalApp(request, appId);
                    break;
                case MODULE_VIEW_CANCELLATION_APPROVAL_APP:
                    appViewService.retrieveCancellationApproval(request, appId);
                    break;
                case MODULE_VIEW_NEW_FAC_CER_REG:
                    appViewService.retrieveFacCerReg(request, appId);
                    break;
                case MODULE_VIEW_DEREGISTRATION_FAC_CER_REG:
                    appViewService.retrieveDeRegistrationAFC(request, appId);
                    break;
                case KEY_DATA_SUBMISSION_TYPE_CONSUME:
                case KEY_DATA_SUBMISSION_TYPE_DISPOSAL:
                case KEY_DATA_SUBMISSION_TYPE_EXPORT:
                case KEY_DATA_SUBMISSION_TYPE_TRANSFER:
                case KEY_DATA_SUBMISSION_TYPE_RECEIPT:
                case KEY_DATA_SUBMISSION_TYPE_RED_TEAMING_REPORT:
                case KEY_DATA_SUBMISSION_TYPE_BAT_INVENTORY:
                    appViewService.retrieveDataSubmissionInfo(request, appId);
                    break;
                case MODULE_VIEW_INSPECTION_FOLLOW_UP_ITEMS:
                    appViewService.retrieveInspectionFollowUpItems(request, appId);
                    break;
                default:
                    log.info("don't have such moduleType {}", StringUtils.normalizeSpace(appViewModuleType));
                    break;
            }
        }
    }
}

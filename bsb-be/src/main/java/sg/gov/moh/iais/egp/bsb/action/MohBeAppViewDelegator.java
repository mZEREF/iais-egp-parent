package sg.gov.moh.iais.egp.bsb.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import sg.gov.moh.iais.egp.bsb.dto.appview.AppViewDto;
import sg.gov.moh.iais.egp.bsb.service.AppViewService;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;

import static sg.gov.moh.iais.egp.bsb.constant.module.AppViewConstants.*;

/**
 * @author : LiRan
 * @date : 2021/12/27
 */
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
        AppViewDto appViewDto = (AppViewDto) ParamUtil.getSessionAttr(request, KEY_APP_VIEW_DTO);
        ParamUtil.setRequestAttr(request, KEY_APP_VIEW_DTO, appViewDto);
        request.getSession().removeAttribute(KEY_APP_VIEW_DTO);
        if (appViewDto != null){
            String applicationId = appViewDto.getApplicationId();
            String moduleType = appViewDto.getModuleType();
            switch (moduleType) {
                case MODULE_VIEW_NEW_FACILITY:
                    appViewService.retrieveFacReg(request, applicationId);
                    break;
                case MODULE_VIEW_DEREGISTRATION_FACILITY:
                    appViewService.retrieveDeregistrationFac(request, applicationId);
                    break;
                case MODULE_VIEW_NEW_APPROVAL_APP:
                    appViewService.retrieveApprovalApp(request, applicationId);
                    break;
                case MODULE_VIEW_CANCELLATION_APPROVAL_APP:
                    appViewService.retrieveCancellationApproval(request, applicationId);
                    break;
                case MODULE_VIEW_NEW_FAC_CER_REG:
                    appViewService.retrieveFacCerReg(request, applicationId);
                    break;
                case MODULE_VIEW_DEREGISTRATION_FAC_CER_REG:
                    appViewService.retrieveDeRegistrationAFC(request, applicationId);
                    break;
                default:
                    log.info("don't have such moduleType {}", StringUtils.normalizeSpace(moduleType));
                    break;
            }
        }
    }
}

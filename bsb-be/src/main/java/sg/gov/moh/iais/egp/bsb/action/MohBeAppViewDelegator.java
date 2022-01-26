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

import static sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants.*;
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
        if (appViewDto != null){
            String applicationId = appViewDto.getApplicationId();
            String processType = appViewDto.getProcessType();
            String appType = appViewDto.getAppType();
            switch (processType) {
                case PROCESS_TYPE_FAC_REG:
                    if (appType.equals(APP_TYPE_NEW)){
                        appViewService.retrieveFacReg(request, applicationId);
                    }else if (appType.equals(APP_TYPE_DEREGISTRATION)){
                        appViewService.retrieveDeregistrationFac(request, applicationId);
                    }
                    break;
                case PROCESS_TYPE_APPROVE_POSSESS:
                case PROCESS_TYPE_APPROVE_LSP:
                case PROCESS_TYPE_SP_APPROVE_HANDLE:
                    if (appType.equals(APP_TYPE_NEW)){
                        appViewService.retrieveApprovalApp(request, applicationId);
                    }else if (appType.equals(APP_TYPE_CANCEL)){
                        appViewService.retrieveCancellationApproval(request, applicationId);
                    }
                    break;
                case PROCESS_TYPE_FAC_CERTIFIER_REG:
                    if (appType.equals(APP_TYPE_NEW)){
                        appViewService.retrieveFacCerReg(request, applicationId);
                    }else if (appType.equals(APP_TYPE_DEREGISTRATION)){
                        appViewService.retrieveDeRegistrationAFC(request, applicationId);
                    }
                    break;
                default:
                    log.info("don't have such processType {}", StringUtils.normalizeSpace(processType));
                    break;
            }
        }
    }
}

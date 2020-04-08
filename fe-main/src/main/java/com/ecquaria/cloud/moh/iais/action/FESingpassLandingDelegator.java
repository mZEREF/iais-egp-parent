package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.RedirectUtil;
import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.helper.EngineHelper;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.dto.organization.OrgUserDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.service.OrgUserManageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import sop.iwe.SessionManager;
import sop.rbac.user.User;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;

@Delegator(value = "singpassLandingDelegator")
@Slf4j
public class FESingpassLandingDelegator {
    public static final String NRIC = "nric";

    @Value("${iais.singpass.login.callback.url}")
    private String singpassCallBackUrl;

    @Autowired
    private OrgUserManageService orgUserManageService;

    /**
     * StartStep: startStep
     *
     * @param bpc
     * @throws
     */
    public void startStep(BaseProcessClass bpc){
        AuditTrailHelper.auditFunction("FE Singpass Landing", "Login");
    }

    /**
     * StartStep: callSingpass
     *
     * @param bpc
     * @throws
     */
    public void callSingpass(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        HttpServletResponse response = bpc.response;
    }

    /**
     * StartStep: singpassCallBack
     *
     * @param bpc
     * @throws
     */
    public void singpassCallBack(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        HttpServletResponse response = bpc.response;

        String nric = ParamUtil.getString(request, "entityId");

        List<String> mohIssueUenList = orgUserManageService.getUenListByNric(nric);
        if (!IaisCommonUtils.isEmpty(mohIssueUenList)){
            ParamUtil.setSessionAttr(request, "uenList", (Serializable) mohIssueUenList);
            try {
                RedirectUtil.redirect("https://egp.sit.inter.iais.com" + EngineHelper.getContextPath()
                        + "/eservice/INTERNET/FE_Landing/1/croppass", request, response);
            } catch (IOException e) {
                log.error(e.getMessage());
            }finally {
                return;
            }
        }

        OrgUserDto userDto = orgUserManageService.createSingpassAccount(nric);

        orgUserManageService.createClientUser(userDto);

        User user = new User();
        user.setDisplayName("Internet User");
        user.setUserDomain(AppConsts.USER_DOMAIN_INTERNET);
        user.setId(userDto.getUserId());

        SessionManager.getInstance(bpc.request).imitateLogin(user, true, true);
        SessionManager.getInstance(bpc.request).initSopLoginInfo(bpc.request);

        StringBuilder url = new StringBuilder();
        url.append("https://").append(bpc.request.getServerName())
                .append("/main-web/eservice/INTERNET/MohInternetInbox");

        IaisEGPHelper.sendRedirect(request, response, url.toString());
    }
}

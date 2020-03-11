package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
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

       /* String nric = ParamUtil.getString(request, FESingpassLandingDelegator.NRIC);

        IaisApiResult<List<String>> apiResult = orgUserManageService.createSingpassAccount(nric);
        if (apiResult.isHasError()){
            int errorCode = apiResult.getErrorCode();
            if (IaisApiStatusCode.FIND_UEN_BY_SINGPASS_ID.getStatusCode() == errorCode){
                List<String> uenList = apiResult.getEntity();

                ParamUtil.setSessionAttr(request, "uenList", (Serializable) uenList);

                try {
                    RedirectUtil.redirect("https://egp.sit.inter.iais.com" + EngineHelper.getContextPath()
                            + "/eservice/INTERNET/FE_Landing/1/preload", request, response);
                } catch (IOException e) {
                    log.error(e.getMessage());
                }

            }

        }*/

        User user = new User();
        user.setDisplayName("Internet User");
        user.setMobileNo("888888");
        user.setEmail("sop6_internet@ecquaria.com");
        user.setUserDomain(AppConsts.USER_DOMAIN_INTERNET);
        user.setPassword("$2a$12$BaTEVyvwaRuop2SdFoK5jOZvK8tnycxVNx1MYVGjbd1vPEQLcaK4K");
        user.setId(AppConsts.USER_DOMAIN_INTERNET);

        SessionManager.getInstance(bpc.request).imitateLogin(user, true, true);
        SessionManager.getInstance(bpc.request).initSopLoginInfo(bpc.request);

        StringBuilder url = new StringBuilder();
        url.append("https://").append(bpc.request.getServerName())
                .append("/main-web/eservice/INTERNET/MohInternetInbox");

        IaisEGPHelper.sendRedirect(request, response, url.toString());
    }
}

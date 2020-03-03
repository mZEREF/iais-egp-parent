package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.RedirectUtil;
import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.helper.EngineHelper;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.IaisApiStatusCode;
import com.ecquaria.cloud.moh.iais.common.dto.IaisApiResult;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.LoginHelper;
import com.ecquaria.cloud.moh.iais.service.OrgUserManageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

        String nric = ParamUtil.getString(request, FESingpassLandingDelegator.NRIC);

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

        }

        User user = new User();
        user.setDisplayName(nric);
        user.setMobileNo("99999999");
        user.setEmail("internet@ecquaria.com");
        user.setUserDomain(AppConsts.USER_DOMAIN_INTERNET);
        user.setId(nric);

        LoginHelper.login(request, response, user, "/main-web");
    }
}

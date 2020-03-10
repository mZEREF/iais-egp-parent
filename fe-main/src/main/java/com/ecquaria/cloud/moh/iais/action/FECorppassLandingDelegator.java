package com.ecquaria.cloud.moh.iais.action;


import com.ecquaria.cloud.RedirectUtil;
import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.service.OrgUserManageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sop.iwe.SessionManager;
import sop.rbac.user.User;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Delegator(value = "croppassLandingDelegator")
@Slf4j
public class FECorppassLandingDelegator {

    public static final String IS_DECLARE = "isDeclare";
    public static final String IS_KEY_APPOINTMENT = "isKeyAppointment";

    @Autowired
    private OrgUserManageService orgUserManageService;



    /**
     * StartStep: callCroppass
     *
     * @param bpc
     * @throws
     */
    public void callCroppass(BaseProcessClass bpc){

    }

    /**
     * StartStep: startStep
     *
     * @param bpc
     * @throws
     */
    public void startStep(BaseProcessClass bpc){
        AuditTrailHelper.auditFunction("FE Corppass Landing", "Login");
    }

    /**
     * StartStep: croppassCallBack
     *
     * @param bpc
     * @throws
     */
    public void croppassCallBack(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        HttpServletResponse response = bpc.response;

        /*String uen = ParamUtil.getRequestString(request, "");
        String nric =  ParamUtil.getRequestString(request, "");

        Map<String, Object> userInfo =  orgUserManageService.getUserByNricAndUen(uen, nric);

        log.info(" croppass login -> user info " + userInfo.toString());
        //already have user
        if (!userInfo.isEmpty()){
            String isAdmin = (String) userInfo.get("isAdmin");
            if ("Y".equals(isAdmin)){
                User user = new User();
                user.setDisplayName((String) userInfo.get("displayName"));
                user.setMobileNo((String) userInfo.get("mobileNo"));
                user.setEmail((String) userInfo.get("email"));
                user.setUserDomain((String) userInfo.get("userDomain"));
                user.setId(AppConsts.USER_DOMAIN_INTERNET);

                LoginHelper.login(request, response, user, "/main-web");
            }
        }

        // a key appointment holder
        boolean isKeyAppointment = orgUserManageService.isKeyappointment(uen, nric);*/
        boolean isKeyAppointment = true;
        if (isKeyAppointment){
            ParamUtil.setRequestAttr(request, FECorppassLandingDelegator.IS_KEY_APPOINTMENT, "Y");
        }else {
            ParamUtil.setRequestAttr(request, FECorppassLandingDelegator.IS_KEY_APPOINTMENT, "N");
        }
    }



    /**
     * StartStep: createCropUser
     *
     * @param bpc
     * @throws
     */
    public void createCropUser(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        HttpServletResponse response = bpc.response;
        ParamUtil.setSessionAttr(bpc.request, "uenList", null);

        String uen = ParamUtil.getString(request, "");
        String nric = ParamUtil.getString(request, "");
/*
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("uen", uen);
        jsonObject.put("nric", nric);
        jsonObject.put(JsonKeyConstants.USER_ID, AppConsts.USER_DOMAIN_INTERNET);

        orgUserManageService.createCropUser(jsonObject.toString());*/

        User user = new User();
        user.setDisplayName("");
        user.setMobileNo("888888");
        user.setEmail("88888@ecquaria.com");
        user.setUserDomain(AppConsts.USER_DOMAIN_INTERNET);
        user.setPassword("password$2");
        user.setId(AppConsts.USER_DOMAIN_INTERNET);

        SessionManager.getInstance(bpc.request).imitateLogin(user, true, true);
        SessionManager.getInstance(bpc.request).initSopLoginInfo(bpc.request);

        StringBuilder url = new StringBuilder();
        url.append("https://").append(bpc.request.getServerName())
                .append("/main-web/eservice/INTERNET/MohInternetInbox");
        String tokenUrl = RedirectUtil.changeUrlToCsrfGuardUrlUrl(url.toString(), bpc.request);

       IaisEGPHelper.sendRedirect(request, response, tokenUrl);
    }

    /**
     * StartStep: isDeclare
     *
     * @param bpc
     * @throws
     */
    public void isDeclare(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;

        String[] declarationCheckbox = ParamUtil.getStrings(request, "declarationCheckbox");
        if (declarationCheckbox != null && declarationCheckbox.length > 0){
            String val = declarationCheckbox[0];
            if ("Y".equals(val)){
                ParamUtil.setRequestAttr(request, FECorppassLandingDelegator.IS_DECLARE, "Y");
            }else {
                ParamUtil.setRequestAttr(request, FECorppassLandingDelegator.IS_DECLARE, "N");
            }
        }
    }


    /**
     * StartStep: ban
     *
     * @param bpc
     * @throws
     */
    public void ban(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;


    }
}

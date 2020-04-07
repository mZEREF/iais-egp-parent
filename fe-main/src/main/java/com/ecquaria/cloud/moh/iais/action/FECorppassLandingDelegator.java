package com.ecquaria.cloud.moh.iais.action;


import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.JsonKeyConstants;
import com.ecquaria.cloud.moh.iais.common.dto.organization.OrgUserDto;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.LoginHelper;
import com.ecquaria.cloud.moh.iais.service.OrgUserManageService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import sop.rbac.user.User;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

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

        String uen = ParamUtil.getRequestString(request, "entityId");
        String nric =  ParamUtil.getRequestString(request, "corpPassId");

        ParamUtil.setSessionAttr(request, "entityId", uen);
        ParamUtil.setSessionAttr(request, "corpPassId", nric);

        Map<String, Object> userInfo =  orgUserManageService.getUserByNricAndUen(uen, nric);

        log.info(" croppass login -> user info " + userInfo.toString());
        //already have user
        if (!userInfo.isEmpty()){
            User user = new User();
            user.setDisplayName("Internet User");
            user.setUserDomain(AppConsts.USER_DOMAIN_INTERNET);
            String userId = userInfo.get("userId").toString();
            user.setId(userId);

            LoginHelper.login(request, response, user, "/main-web");
            return;
        }

        // a key appointment holder
        //boolean isKeyAppointment = orgUserManageService.isKeyappointment(uen, nric);
        boolean isKeyAppointment = false;
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

        String uen = (String) ParamUtil.getScopeAttr(request, "entityId");
        String nric = (String) ParamUtil.getScopeAttr(request, "corpPassId");

        if (!StringUtils.isEmpty(uen) && !StringUtil.isEmpty(nric)){
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("uen", uen);
            jsonObject.put("nric", nric);
            jsonObject.put(JsonKeyConstants.USER_ID, AppConsts.USER_DOMAIN_INTERNET);

            OrgUserDto orgUserDto = orgUserManageService.createCropUser(jsonObject.toString());

            User user = new User();
            user.setDisplayName("Internet User");
            user.setUserDomain(AppConsts.USER_DOMAIN_INTERNET);
            user.setId(orgUserDto.getUserId());

            LoginHelper.login(request, response, user, "/main-web");
        }

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

/*
 * This file is generated by ECQ project skeleton automatically.
 *
 *   Copyright 2019-2049, Ecquaria Technologies Pte Ltd. All rights reserved.
 *
 *   No part of this material may be copied, reproduced, transmitted,
 *   stored in a retrieval system, reverse engineered, decompiled,
 *   disassembled, localised, ported, adapted, varied, modified, reused,
 *   customised or translated into any language in any form or by any means,
 *   electronic, mechanical, photocopying, recording or otherwise,
 *   without the prior written permission of Ecquaria Technologies Pte Ltd.
 */

package com.ecquaria.cloud.moh.iais.helper;

import com.ecquaria.cloud.helper.SpringContextHelper;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenseeDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.OrgUserDto;
import com.ecquaria.cloud.moh.iais.common.utils.MiscUtil;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.dto.LoginContext;
import com.ecquaria.cloud.moh.iais.service.client.ComSystemAdminClient;
import eu.bitwalker.useragentutils.Browser;
import eu.bitwalker.useragentutils.UserAgent;
import eu.bitwalker.useragentutils.Version;
import lombok.extern.slf4j.Slf4j;
import sop.iwe.SessionManager;
import sop.rbac.user.User;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * AccessUtil
 *
 * @author suocheng
 * @date 7/23/2019
 */

@Slf4j
public class AccessUtil {
    /**
     * @description: judge is the Backend .
     *
     * @author: suocheng on 7/23/2019 2:49 PM
     * @param: []
     * @return: boolean
     */
    public static boolean isBackend() {
        return false;
    }
    /**
     * @description: judge is the Frontend
     *
     * @author: suocheng on 7/23/2019 2:50 PM
     * @param: []
     * @return: boolean
     */
    public static boolean isFrontend() {
        return true;
    }

   /**
   * @description: judge is the administrator
   * @param: 
   * @return: 
   * @author: yichen 
   */
    public static boolean isAdministrator(){
        return true;
    }

    public static boolean isSpecifiedRole(String role) {
        HttpServletRequest request = MiscUtil.getCurrentRequest();
        if (request == null) {
            return false;
        }

        LoginContext loginContext = (LoginContext) ParamUtil.getSessionAttr(request, AppConsts.SESSION_ATTR_LOGIN_USER);

        return role.equals(loginContext.getCurRoleId());
    }

    private AccessUtil() {
        throw new IllegalStateException("Utility class");
    }

    public static void initLoginUserInfo(HttpServletRequest request) {
        LoginContext loginContext = new LoginContext();
        User user = SessionManager.getInstance(request).getCurrentUser();
        ComSystemAdminClient client = SpringContextHelper.getContext().getBean(ComSystemAdminClient.class);
        OrgUserDto orgUser = client.retrieveOrgUserAccount(user.getId()).getEntity();
        if (orgUser != null) {
            loginContext.setUserId(orgUser.getId());
            loginContext.setLoginId(user.getId());
            loginContext.setUserDomain(orgUser.getUserDomain());
            loginContext.setUserName(user.getDisplayName());
            List<String> userRoles = client.retrieveUserRoles(orgUser.getId()).getEntity();
            loginContext.setOrgId(orgUser.getOrgId());

            if (userRoles != null && !userRoles.isEmpty()) {
                loginContext.getRoleIds().addAll(userRoles);
                loginContext.setCurRoleId(userRoles.get(0));
            }

            if (AppConsts.USER_DOMAIN_INTRANET.equals(orgUser.getUserDomain())) {
                List<String> wrkGrps = client.getWorkGrpsByUserId(orgUser.getId()).getEntity();
                if (wrkGrps != null && !wrkGrps.isEmpty()) {
                    loginContext.getWrkGrpIds().addAll(wrkGrps);
                }
            } else if (AppConsts.USER_DOMAIN_INTERNET.equals(orgUser.getUserDomain())) {
                LicenseeDto lDto = client.getLicenseeByOrgId(orgUser.getOrgId()).getEntity();
                if (lDto != null) {
                    loginContext.setNricNum(orgUser.getIdNumber());
                    loginContext.setLicenseeId(lDto.getId());
                    loginContext.setUenNo(lDto.getUenNo());
                    loginContext.setLicenseeEntityType(lDto.getLicenseeEntityDto().getEntityType());
                }
            }
        }
        ParamUtil.setSessionAttr(request, AppConsts.SESSION_ATTR_LOGIN_USER, loginContext);
    }
    /**
     * @description: get the login user from the session
     *
     * @author: suocheng on 2/26/2020 4:24 PM
     * @param: [request]
     * @return: com.ecquaria.cloud.moh.iais.dto.LoginContext
     */
    public static LoginContext getLoginUser(HttpServletRequest request){

        LoginContext loginContext = null;
        if(request!=null){
            loginContext = (LoginContext)ParamUtil.getSessionAttr(request,AppConsts.SESSION_ATTR_LOGIN_USER);
        }
        return loginContext;
    }
    /**
     * @description: get the login id from session
     *
     * @author: suocheng on 2/26/2020 4:30 PM
     * @param: [request]
     * @return: java.lang.String
     */
    public static String getLoginId(HttpServletRequest request){

        String loginId = null;
        LoginContext loginContext = AccessUtil.getLoginUser(request);
        if(loginContext!=null){
            loginId = loginContext.getLoginId();
        }
        return loginId;
    }

    public static String getBrowserInfo(HttpServletRequest request){
        StringBuilder uaStr = new StringBuilder();
        UserAgent userAgent = UserAgent.parseUserAgentString(request.getHeader("User-Agent"));
        if (userAgent != null){
            Browser browser = userAgent.getBrowser();
            if (browser != null){
                String browserName = browser.getName();
                Version version = browser.getVersion(request.getHeader("User-Agent"));
                if(version != null){
                    uaStr.append(browserName).append('/').append(version.getVersion());
                }else {
                    uaStr.append(browserName).append('/').append("unbeknown");
                }
            }
        }

        log.info(StringUtil.changeForLog("BrowserInfo .........." + uaStr));
        return uaStr.toString();
    }

}

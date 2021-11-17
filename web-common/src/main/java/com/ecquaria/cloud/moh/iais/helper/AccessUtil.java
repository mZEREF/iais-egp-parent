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

import com.ecquaria.cloud.helper.ConfigHelper;
import com.ecquaria.cloud.helper.SpringContextHelper;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.role.RoleConsts;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenseeDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.OrgUserDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.OrganizationDto;
import com.ecquaria.cloud.moh.iais.common.utils.*;
import com.ecquaria.cloud.moh.iais.dto.LoginContext;
import com.ecquaria.cloud.moh.iais.service.client.ComSystemAdminClient;
import com.ecquaria.cloud.moh.iais.service.client.LicenseeClient;
import com.ecquaria.cloud.moh.iais.service.client.OrgEicClient;
import com.ecquaria.cloud.privilege.PrivilegeServiceClient;
import eu.bitwalker.useragentutils.Browser;
import eu.bitwalker.useragentutils.UserAgent;
import eu.bitwalker.useragentutils.Version;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.ArrayUtils;
import sop.iwe.SessionManager;
import sop.rbac.user.User;
import sop.rbac.user.UserIdentifier;

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
                if(RoleConsts.USER_ROLE_BROADCAST.equals(userRoles.get(0)) && userRoles.size() > 1){
                    loginContext.setCurRoleId(userRoles.get(1));
                }else{
                    loginContext.setCurRoleId(userRoles.get(0));
                }
            }

            if (AppConsts.USER_DOMAIN_INTRANET.equals(orgUser.getUserDomain())) {
                List<String> wrkGrps = client.getWorkGrpsByUserId(orgUser.getId()).getEntity();
                if (wrkGrps != null && !wrkGrps.isEmpty()) {
                    loginContext.getWrkGrpIds().addAll(wrkGrps);
                }
            } else if (AppConsts.USER_DOMAIN_INTERNET.equals(orgUser.getUserDomain())) {
                LicenseeDto lDto = client.getLicenseeByOrgId(orgUser.getOrgId()).getEntity();
                if (lDto == null) {
                    LicenseeClient lc = SpringContextHelper.getContext().getBean(LicenseeClient.class);
                    OrgEicClient orgEicClient = SpringContextHelper.getContext().getBean(OrgEicClient.class);
                    OrganizationDto organ = orgEicClient.getOrganizationById(orgUser.getOrgId()).getEntity();
                    if(organ != null && StringUtil.isNotEmpty(organ.getUenNo())){
                        log.info("=====>>>>> createLicenseeByUenFromAcra corppass");
                        lc.getEntityByUEN(organ.getUenNo());
                    }else {
                        lc.imaginaryLicenseeByOrgId(orgUser.getOrgId());
                    }
                }
                lDto = client.getLicenseeByOrgId(orgUser.getOrgId()).getEntity();
                loginContext.setNricNum(orgUser.getIdNumber());
                loginContext.setLicenseeId(lDto.getId());
                loginContext.setUenNo(lDto.getUenNo());
                loginContext.setLicenseeEntityType(lDto.getLicenseeEntityDto().getEntityType());
                log.info(StringUtil.changeForLog("=====>>>>> current licensee " + JsonUtil.parseToJson(lDto)));
            }
        }
        setLoginContextPrivilege(loginContext);
        ParamUtil.setSessionAttr(request, AppConsts.SESSION_ATTR_LOGIN_USER, loginContext);
    }

    public static boolean setLoginContextPrivilege(LoginContext loginContext) {
        if(loginContext != null) {
            if(IaisCommonUtils.isNotEmpty(loginContext.getPrivileges())){
                return true;
            }
            List<String> roleIds = loginContext.getRoleIds();
            PrivilegeServiceClient privilegeServiceClient = SpringContextHelper.getContext().getBean(PrivilegeServiceClient.class);
            //todo delete
            roleIds.add(RoleConsts.USER_ROLE_DS_AR);
            if(!IaisCommonUtils.isEmpty(roleIds)) {
                UserIdentifier userIdentifier = new UserIdentifier();
                userIdentifier.setId(loginContext.getLoginId());
                userIdentifier.setUserDomain("cs_hcsa");
                String[] roleArr = roleIds.toArray(new String[roleIds.size()]);
                //get privilege Number
                Long[] privilegeNo = privilegeServiceClient.getAccessiblePrivilegeNos(userIdentifier, roleArr).getEntity();
                if(privilegeNo != null && privilegeNo.length > 0) {
                    //get Privilege
                    long[] privilegeNoArr = ArrayUtils.toPrimitive(privilegeNo);
                    loginContext.setPrivileges(privilegeServiceClient.getprivilegesByNos(privilegeNoArr).getEntity());
                }
                return true;
            }else {
                log.error("--------loginContext roleIds is null--------",loginContext);
            }
        }
        return false;
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
    public static boolean isActiveMigrated()  {
        String periodDateStr = ConfigHelper.getString(AppConsts.PERIOD_APPROVED_MIGRATED_LICENCE,"");
        boolean result = false;
            try {
                if(!StringUtil.isEmpty(periodDateStr)){
                    SimpleDateFormat df = new SimpleDateFormat(AppConsts.DEFAULT_DATE_FORMAT);
                    Date periodDate = df.parse(periodDateStr);
                    Date nowDate = new Date();
                    if(nowDate.before(periodDate)){
                        result =  true;
                    }
               }
            } catch (ParseException e) {
                log.error(e.getMessage(),e);
            }


        return  result;
    }
}

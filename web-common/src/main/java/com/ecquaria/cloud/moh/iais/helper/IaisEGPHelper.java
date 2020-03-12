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

import com.ecquaria.cloud.RedirectUtil;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.AuditTrailConsts;
import com.ecquaria.cloud.moh.iais.common.dto.AuditTrailDto;
import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.exception.IaisRuntimeException;
import com.ecquaria.cloud.moh.iais.common.utils.MiscUtil;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.dto.FilterParameter;
import com.ecquaria.cloud.moh.iais.dto.LoginContext;
import com.ecquaria.egp.api.EGPHelper;
import java.io.IOException;
import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.sqlite.date.FastDateFormat;
import sop.iwe.SessionManager;
import sop.rbac.user.User;

@Slf4j
public final class IaisEGPHelper extends EGPHelper {

    /**
     * @description: The method to set login user info into Audit trail from request
     *
     * @author: Jinhua on 2019/7/17 17:34
     * @param: [dto]
     * @return: void
     */
    public static void setAuditLoginUserInfo(AuditTrailDto dto) {
        if (dto == null) {
            return;
        }

        HttpServletRequest request = MiscUtil.getCurrentRequest();
        if (request == null) {
            return;
        }
        User user = SessionManager.getInstance(request).getCurrentUser();
        HttpSession session = request.getSession();
        if (user != null) {
            dto.setNricNumber(user.getId());
            dto.setMohUserId(user.getId());
            LoginContext loginContext = (LoginContext) ParamUtil.getSessionAttr(request, AppConsts.SESSION_ATTR_LOGIN_USER);
            if (loginContext != null) {
                dto.setMohUserGuid(loginContext.getUserId());
            }
            dto.setUserDomain(SessionManager.getInstance(request).getCurrentUserDomain());
            dto.setOperationType(AppConsts.USER_DOMAIN_INTRANET.equals(user.getUserDomain()) ?
                    AuditTrailConsts.OPERATION_TYPE_INTRANET : AuditTrailConsts.OPERATION_TYPE_INTERNET);
        }
        dto.setSessionId(session.getId());
        dto.setClientIp(MiscUtil.getClientIp(request));
        dto.setUserAgent(request.getHeader("User-Agent"));
    }

    public static String getRootPath() {
        String urlStr = IaisEGPHelper.class.getResource("").toString();
        String serverPath = urlStr.substring(urlStr.lastIndexOf("file:/") + 6).replaceAll("%20", " ");
        String path = "";
        if (serverPath.lastIndexOf("WEB-INF") > 0) {
            path = serverPath.substring(0, serverPath.lastIndexOf("WEB-INF"));
        }

        return path;
    }

    public static AuditTrailDto getCurrentAuditTrailDto() {
        AuditTrailDto dto = null;
        HttpServletRequest request = MiscUtil.getCurrentRequest();
        if (request != null) {
            dto = (AuditTrailDto) ParamUtil.getSessionAttr(request, AuditTrailConsts.SESSION_ATTR_PARAM_NAME);
        }

        if (dto == null) {
            dto = AuditTrailDto.getThreadDto();
        }

        return dto;
    }

    public static Boolean isAfterDate(Date start, Date end){
        if (start == null || end == null){
            throw new IaisRuntimeException("No has input for Date!");
        }

        return end.compareTo(start) == 1  || end.compareTo(start) == 0 ? true : false;
    }


    /**
     * use by delegator to clear session attr, prefix of param need use 'Param_'
     * @param request HttpServletRequest
     * @param delegatorClz   Delegator Class
     * @throws IllegalAccessException
     */
    public static void clearSessionAttr(HttpServletRequest request, Class<?> delegatorClz) throws IllegalAccessException {
        if(request == null || delegatorClz == null){
            return;
        }

        Field[] fields = delegatorClz.getFields();
        if(fields != null){
            for(Field field : fields){
                String fieldName = field.getName();
                if(fieldName.endsWith("_ATTR")){
                    ParamUtil.setSessionAttr(request, (String) field.get(fieldName), null);
                }
            }
        }
    }

    /**
     * Get query conditions by parameters
     * @param request HttpServletRequest
     * @param filter filter parameter
     * @return
     */
    public static SearchParam getSearchParam(HttpServletRequest request, FilterParameter filter){
        return getSearchParam(request, false, filter);
    }

    public static SearchParam getSearchParam(HttpServletRequest request,
                                             boolean isNew, FilterParameter filter){
        SearchParam searchParam = (SearchParam) ParamUtil.getSessionAttr(request, filter.getSearchAttr());
        try {
            if(searchParam == null || isNew){
                searchParam = new SearchParam(filter.getClz().getName());
                searchParam.setPageSize(filter.getPageSize());
                searchParam.setPageNo(filter.getPageNo());
                searchParam.setSort(filter.getSortField(), SearchParam.ASCENDING);
                ParamUtil.setSessionAttr(request, filter.getSearchAttr(), searchParam);
            }
        }catch (NullPointerException e){
            log.info("getSearchParam ===>>>> " + e.getMessage());
        }
        return searchParam;
    }

    /**
    * @description: Capital letter
    * @param:
    * @return:
    * @author: yichen
    */
    public static String capitalized(String str) {
        byte[] val = str.getBytes();
        val[0] = (byte) ((char) val[0] - 'a' + 'A');
        return new String(val);
    }

    /**
    * @description: format date
    * @param: 
    * @return: 
    * @author: yichen 
    */
    public static Date parseToDate(String val, String pattern) {
        if(StringUtils.isEmpty(val) || StringUtils.isEmpty(pattern)){
           throw new IaisRuntimeException("No has input for String to Date!");
        }

        try {
            return FastDateFormat.getInstance(pattern).parse(val);
        } catch (ParseException e) {
            throw new IaisRuntimeException(e.getMessage());
        }
    }

    /**
     * @description: format date
     * @param:
     * @return:
     * @author: yichen
     */
    public static String parseToString(Date val, String pattern) {
        if(val == null || StringUtils.isEmpty(pattern)){
            throw new IaisRuntimeException("No has input for Date to String!");
        }

        SimpleDateFormat formatter = new SimpleDateFormat(pattern);
        return formatter.format(val);
    }


    public static String genTokenForCallback(String submissionId, String serviceName) {
        String secKey = RedisCacheHelper.getInstance().get("iaisEdToken",
                "Callback_SecKEy__SubId_" + submissionId);
        if (StringUtil.isEmpty(secKey)) {
            secKey = String.valueOf(System.currentTimeMillis());
            RedisCacheHelper.getInstance().set("iaisEdToken",
                    "Callback_SecKEy__SubId_" + submissionId, secKey, 60L * 60L * 24L);
        }
        String token = StringUtil.digestStrSha256(serviceName + secKey);

        return token;
    }

    public static boolean verifyCallBackToken(String submissionId, String serviceName, String token) {
        String secKey = RedisCacheHelper.getInstance().get("iaisEdToken",
                "Callback_SecKEy__SubId_" + submissionId);
        String corrToken = StringUtil.digestStrSha256(serviceName + secKey);

        return token.equals(corrToken);
    }

    public static void sendRedirect(HttpServletRequest request, HttpServletResponse response, String url){
        String tokenUrl = RedirectUtil.changeUrlToCsrfGuardUrlUrl(url, request);
        try {
            response.sendRedirect(tokenUrl);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    public static List<String> getLicenseeEmailAddrs(String licenseeId) {
        //TODO:
        List<String> list = new ArrayList<>();
        list.add("jinhua@ecqsz.com");

        return list;
    }

    private IaisEGPHelper() {throw new IllegalStateException("Utility class");}
}

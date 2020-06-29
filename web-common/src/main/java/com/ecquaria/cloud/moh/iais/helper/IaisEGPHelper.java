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
import com.ecquaria.cloud.helper.SpringContextHelper;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.constant.AuditTrailConsts;
import com.ecquaria.cloud.moh.iais.common.constant.ProcessFileTrackConsts;
import com.ecquaria.cloud.moh.iais.common.constant.role.RoleConsts;
import com.ecquaria.cloud.moh.iais.common.dto.AuditTrailDto;
import com.ecquaria.cloud.moh.iais.common.dto.EicRequestTrackingDto;
import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.exception.IaisRuntimeException;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.JsonUtil;
import com.ecquaria.cloud.moh.iais.common.utils.MiscUtil;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant;
import com.ecquaria.cloud.moh.iais.dto.LoginContext;
import com.ecquaria.cloud.moh.iais.service.LicenseeService;
import com.ecquaria.egp.api.EGPHelper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.sqlite.date.FastDateFormat;
import sop.iwe.SessionManager;
import sop.rbac.user.User;
import sop.servlet.webflow.HttpHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static org.eclipse.jdt.internal.compiler.util.Util.UTF_8;

@Slf4j
public final class IaisEGPHelper extends EGPHelper {
    /**
     * Role Application status constant
     */
    private static final String[] PSO_STATUS = new String[]{
            ApplicationConsts.APPLICATION_STATUS_PENDING_PROFESSIONAL_SCREENING,
            ApplicationConsts.APPLICATION_STATUS_INSPECTOR_ROUTE_BACK,
    };

    private static final String[] ASO_STATUS = new String[]{
            ApplicationConsts.APPLICATION_STATUS_PENDING_ADMIN_SCREENING,
            ApplicationConsts.APPLICATION_STATUS_PSO_ROUTE_BACK,
            ApplicationConsts.APPLICATION_STATUS_INSPECTOR_ROUTE_BACK,
            ApplicationConsts.APPLICATION_STATUS_ROUTE_TO_DMS
    };

    private static final String[] AO1_STATUS = new String[]{
            ApplicationConsts.APPLICATION_STATUS_PENDING_APPROVAL01,
            ApplicationConsts.APPLICATION_STATUS_PENDING_EMAIL_REVIEW,
            ApplicationConsts.APPLICATION_STATUS_PENDING_INSPECTION_REPORT_REVIEW
    };

    private static final String[] AO2_STATUS = new String[]{
            ApplicationConsts.APPLICATION_STATUS_PENDING_APPROVAL02,
    };

    private static final String[] AO3_STATUS = new String[]{
            ApplicationConsts.APPLICATION_STATUS_PENDING_APPROVAL03
    };

    private static final String[] BOARDCAST_STATUS = new String[]{
            ApplicationConsts.APPLICATION_STATUS_PENDING_BROADCAST
    };

    private static final String[] INSP_LEADER_STATUS = new String[]{
            ApplicationConsts.APPLICATION_STATUS_PENDING_APPOINTMENT_SCHEDULING,
            ApplicationConsts.APPLICATION_STATUS_PENDING_RE_APPOINTMENT_SCHEDULING,
            ApplicationConsts.APPLICATION_STATUS_PENDING_INSPECTION_READINESS,
            ApplicationConsts.APPLICATION_STATUS_PENDING_INSPECTION,
            ApplicationConsts.APPLICATION_STATUS_PENDING_DRAFT_LETTER,
            ApplicationConsts.APPLICATION_STATUS_PENDING_EMAIL_SENDING,
            ApplicationConsts.APPLICATION_STATUS_PENDING_RE_DRAFT_LETTER,
            ApplicationConsts.APPLICATION_STATUS_PENDING_RECTIFICATION_REVIEW,
            ApplicationConsts.APPLICATION_STATUS_PENDING_INSPECTION_REPORT,
            ApplicationConsts.APPLICATION_STATUS_PENDING_INSPECTION_REPORT_REVISION,
            ApplicationConsts.APPLICATION_STATUS_ROUTE_TO_DMS
    };

    private static final String[] DEFAULT_STATUS = new String[]{
            ApplicationConsts.APPLICATION_STATUS_PENDING_EMAIL_SENDING
    };

    /**
     * @author: Shicheng on 2020/6/9 10:20
     * @description: get Application status By Role Id (For Supervisor Pool and Reassign)
     */
    public static List<SelectOption> getAppStatusByRoleId(String roleId) {
        String[] statusStrs;
        switch (roleId){
            case RoleConsts.USER_ROLE_PSO_LEAD:
                statusStrs = PSO_STATUS;
                break;
            case RoleConsts.USER_ROLE_ASO_LEAD:
                statusStrs = ASO_STATUS;
                break;
            case RoleConsts.USER_ROLE_AO1_LEAD:
                statusStrs = AO1_STATUS;
                break;
            case RoleConsts.USER_ROLE_AO2_LEAD:
                statusStrs = AO2_STATUS;
                break;
            case RoleConsts.USER_ROLE_AO3_LEAD:
                statusStrs = AO3_STATUS;
                break;
            case RoleConsts.USER_ROLE_BROADCAST:
                statusStrs = BOARDCAST_STATUS;
                break;
            case RoleConsts.USER_ROLE_INSPECTION_LEAD:
                statusStrs = INSP_LEADER_STATUS;
                break;
            default:
                statusStrs = DEFAULT_STATUS;
                break;
        }
        List<SelectOption> appStatusOption = MasterCodeUtil.retrieveOptionsByCodes(statusStrs);
        return appStatusOption;
    }

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

    /**
     * It could be the same day
     * @param start
     * @param end
     * @return
     */
    public static boolean isAfterDate(Date start, Date end){
        if (start == null || end == null){
            throw new IaisRuntimeException("No has input for Date!");
        }

        return end.compareTo(start) > 0  || end.compareTo(start) == 0 ? true : false;
    }

    /**
     * @Author yichen
     * Get the last second of the day
     * {@param date} 23:59:59
     **/
    public static Date getLastSecond(Date date){
        if (date == null){
            throw new IaisRuntimeException("No has input for Date!");
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        Date d = calendar.getTime();

        return d;
    }


    /**
     * It can't be the same day
     * @param start
     * @param end
     * @return
     */
    public static boolean isAfterDateSecond(Date start, Date end){
        if (start == null || end == null){
            throw new IaisRuntimeException("No has input for Date!");
        }

        return end.compareTo(start) > 0 ? true : false;
    }


    public static boolean isAfterDateSecondByStringDate(String start, String end,Boolean isSameDay){
        if(isSameDay){
            return isAfterDate(parseToDate(start),parseToDate(end));
        }else {
            return isAfterDateSecond(parseToDate(start),parseToDate(end));
        }
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
            log.error(StringUtil.changeForLog("getSearchParam ===>>>> " + e.getMessage()), e);
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
        byte[] val;
        try {
            val = str.getBytes(UTF_8);
            val[0] = (byte) ((char) val[0] - 'a' + 'A');
        } catch (UnsupportedEncodingException e) {
            throw new IaisRuntimeException(e);
        }

        return new String(val);
    }

    public static Date yesterday(Date today) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(today);
        calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) - 1);
        return calendar.getTime();
    }

    public static Date tomorrow(Date today) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(today);
        calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) + 1);
        return calendar.getTime();
    }
    /**
    * @description: format date
    * @param: 
    * @return: 
    * @author: yichen 
    */
    public static Date parseToDate(String val) {
        if(StringUtils.isEmpty(val)){
           throw new IaisRuntimeException("No has input for String to Date!");
        }

        try {
            return FastDateFormat.getInstance(AppConsts.DEFAULT_DATE_FORMAT).parse(val);
        } catch (ParseException e) {
            throw new IaisRuntimeException(e.getMessage(), e);
        }
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
            throw new IaisRuntimeException(e.getMessage(), e);
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
            response.setStatus(HttpStatus.MOVED_PERMANENTLY.value());
            response.sendRedirect(tokenUrl);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }

    public static List<String> getLicenseeEmailAddrs(String licenseeId) {
        LicenseeService licenseeService = SpringContextHelper.getContext().getBean(LicenseeService.class);

        return licenseeService.getLicenseeEmails(licenseeId);
    }

    public static List<String> getLicenseeMobiles(String licenseeId) {
        LicenseeService licenseeService = SpringContextHelper.getContext().getBean(LicenseeService.class);

        return licenseeService.getLicenseeMobiles(licenseeId);
    }

    public static void retrigerEicMethods(List<EicRequestTrackingDto> trackList) {
        trackList.forEach(e -> {
            try {
                Class actCls = Class.forName(e.getActionClsName());
                Object actObj = SpringContextHelper.getContext().getBean(actCls);
                Method med = actCls.getMethod("aaaa");
                Class dtoCls = Class.forName(e.getDtoClsName());
                Object dto = JsonUtil.parseToObject(e.getDtoObject(), dtoCls);
                if (med != null) {
                    med.invoke(actObj, dto);
                    e.setStatus(ProcessFileTrackConsts.PROCESS_FILE_TRACK_STATUS_COMPLETE);
                }
            } catch (Exception ex) {
                log.error(ex.getMessage(), ex);
            }
        });
    }

    public static void setMultipartAction(HttpServletRequest request){
        MultipartHttpServletRequest mulReq = (MultipartHttpServletRequest) request.getAttribute(HttpHandler.SOP6_MULTIPART_REQUEST);
        String currentAction = mulReq.getParameter(IaisEGPConstant.CRUD_ACTION_TYPE);
        ParamUtil.setRequestAttr(request, IaisEGPConstant.CRUD_ACTION_TYPE, currentAction);
    }

    public static Map<Integer, List<Integer>> generateUnlockMap(int row, int cell){
        Map<Integer, List<Integer>> unlockCellMap = IaisCommonUtils.genNewHashMap();
        List<Integer> list = IaisCommonUtils.genNewArrayList();
        for (int i = 0; i <= cell; i++){
            list.add(i);
        }

        for (int i = 0; i <= row; i++){
            unlockCellMap.put(i, list);
        }

        return unlockCellMap;
    }

    private IaisEGPHelper() {throw new IllegalStateException("Utility class");}

    public static int getCompareDate(Date startDate,Date endDate){
        if(startDate == null || endDate == null)
            return  -1;
        Calendar calendarStart = Calendar.getInstance();
        calendarStart.setTime(startDate);
        Calendar calendarEnd = Calendar.getInstance();
        calendarEnd.setTime( endDate);
        int day1 = calendarStart.get(Calendar.DAY_OF_YEAR);
        int day2 = calendarEnd.get(Calendar.DAY_OF_YEAR);
        int year1 = calendarStart.get(Calendar.YEAR);
        int year2 = calendarEnd.get(Calendar.YEAR);
        if (year1 != year2){
            int timeDistance = 0;
            for (int i = year1 ; i < year2 ;i++){
                if (i%4==0 && i%100!=0||i%400==0) {
                    timeDistance += 366;
                }else {
                    timeDistance += 365;
                }
            }
            return  timeDistance + (day2-day1+1);
        }else {
            return day2-day1+1;
        }
    }
}

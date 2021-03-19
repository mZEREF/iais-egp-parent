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
import com.ecquaria.cloud.moh.iais.common.constant.organization.OrganizationConstants;
import com.ecquaria.cloud.moh.iais.common.constant.role.RoleConsts;
import com.ecquaria.cloud.moh.iais.common.dto.AuditTrailDto;
import com.ecquaria.cloud.moh.iais.common.dto.EicRequestTrackingDto;
import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.exception.IaisRuntimeException;
import com.ecquaria.cloud.moh.iais.common.helper.RedisCacheHelper;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.JsonUtil;
import com.ecquaria.cloud.moh.iais.common.utils.MaskUtil;
import com.ecquaria.cloud.moh.iais.common.utils.MiscUtil;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.common.validation.SgNoValidator;
import com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant;
import com.ecquaria.cloud.moh.iais.dto.LoginContext;
import com.ecquaria.cloud.moh.iais.dto.memorypage.PaginationHandler;
import com.ecquaria.cloud.moh.iais.service.LicenseeService;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import com.ecquaria.egp.api.EGPHelper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.sqlite.date.FastDateFormat;
import sop.iwe.SessionManager;
import sop.rbac.user.User;
import sop.servlet.webflow.HttpHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.ecquaria.sz.commons.util.StringUtil.RANDOM;

@Slf4j //NOSONAR
public final class IaisEGPHelper extends EGPHelper {

    /**
     * Role Application status constant
     */
    private static final String[] PSO_STATUS = new String[]{
            ApplicationConsts.APPLICATION_STATUS_PENDING_PROFESSIONAL_SCREENING,
            ApplicationConsts.APPLICATION_STATUS_PENDING_BROADCAST,
            ApplicationConsts.APPLICATION_STATUS_INSPECTOR_ENQUIRE,
    };

    private static final String[] ASO_STATUS = new String[]{
            ApplicationConsts.APPLICATION_STATUS_PENDING_ADMIN_SCREENING,
            ApplicationConsts.APPLICATION_STATUS_PENDING_BROADCAST,
            ApplicationConsts.APPLICATION_STATUS_ROUTE_TO_DMS,
            ApplicationConsts.APPLICATION_STATUS_INSPECTOR_ENQUIRE,
            ApplicationConsts.APPLICATION_STATUS_PROFESSIONAL_SCREENING_OFFICER_ENQUIRE,
    };

    private static final String[] AO1_STATUS = new String[]{
            ApplicationConsts.APPLICATION_STATUS_PENDING_BROADCAST,
            ApplicationConsts.APPLICATION_STATUS_PENDING_EMAIL_REVIEW,
            ApplicationConsts.APPLICATION_STATUS_PENDING_APPROVAL01,
            ApplicationConsts.APPLICATION_STATUS_PENDING_INSPECTION_REPORT_REVIEW,
    };

    private static final String[] AO2_STATUS = new String[]{
            ApplicationConsts.APPLICATION_STATUS_PENDING_APPROVAL02,
            ApplicationConsts.APPLICATION_STATUS_PENDING_BROADCAST,
    };

    private static final String[] AO3_STATUS = new String[]{
            ApplicationConsts.APPLICATION_STATUS_PENDING_APPROVAL03,
    };

    private static final String[] BOARDCAST_STATUS = new String[]{
            ApplicationConsts.APPLICATION_STATUS_PENDING_BROADCAST,
    };

    private static final String[] INSP_LEADER_STATUS = new String[]{
            ApplicationConsts.APPLICATION_STATUS_PENDING_INSPECTION_READINESS,
            ApplicationConsts.APPLICATION_STATUS_PENDING_INSPECTION,
            ApplicationConsts.APPLICATION_STATUS_PENDING_DRAFT_LETTER,
            ApplicationConsts.APPLICATION_STATUS_PENDING_RE_DRAFT_LETTER,
            ApplicationConsts.APPLICATION_STATUS_PENDING_RECTIFICATION_REVIEW,
            ApplicationConsts.APPLICATION_STATUS_PENDING_INSPECTION_REPORT,
            ApplicationConsts.APPLICATION_STATUS_PENDING_INSPECTION_REPORT_REVISION,
            ApplicationConsts.APPLICATION_STATUS_ROUTE_TO_DMS,
            ApplicationConsts.APPLICATION_STATUS_PENDING_EMAIL_SENDING,
    };

    private static final String[] INSP_STATUS = new String[]{
            ApplicationConsts.APPLICATION_STATUS_PENDING_INSPECTION_READINESS,
            ApplicationConsts.APPLICATION_STATUS_PENDING_INSPECTION,
            ApplicationConsts.APPLICATION_STATUS_PENDING_DRAFT_LETTER,
            ApplicationConsts.APPLICATION_STATUS_PENDING_RE_DRAFT_LETTER,
            ApplicationConsts.APPLICATION_STATUS_PENDING_RECTIFICATION_REVIEW,
            ApplicationConsts.APPLICATION_STATUS_PENDING_INSPECTION_REPORT,
            ApplicationConsts.APPLICATION_STATUS_PENDING_INSPECTION_REPORT_REVISION,
            ApplicationConsts.APPLICATION_STATUS_ROUTE_TO_DMS,
            ApplicationConsts.APPLICATION_STATUS_PENDING_APPOINTMENT_SCHEDULING,
    };

    private static final String[] DEFAULT_STATUS = new String[]{
            ApplicationConsts.APPLICATION_STATUS_PENDING_EMAIL_SENDING,
    };

    private static final String PRS_CLIENT_ID             = "a0900db88fa94ee49d8566fd3ca414f3";

    /**
     * @author: Shicheng on 2020/11/03 13:49
     * @description: encryption Prs the signature
     */
    public static String getPrsSignature(String timeStamp) {
        String signature = PRS_CLIENT_ID + "_" + timeStamp;
        //todo encryption
        String encSignature = "" + signature;
        return encSignature;
    }

    /**
     * @author: Shicheng on 2020/6/9 10:20
     * @description: get Application status By Role Id (For Supervisor Pool and Reassign)
     */
    public static List<SelectOption> getAppStatusByRoleId(String roleId) {
        String[] statusStrs;
        switch (roleId){
            case RoleConsts.USER_ROLE_PSO_LEAD:
            case RoleConsts.USER_ROLE_PSO:
                statusStrs = PSO_STATUS;
                break;
            case RoleConsts.USER_ROLE_ASO_LEAD:
            case RoleConsts.USER_ROLE_ASO:
                statusStrs = ASO_STATUS;
                break;
            case RoleConsts.USER_ROLE_AO1_LEAD:
            case RoleConsts.USER_ROLE_AO1:
                statusStrs = AO1_STATUS;
                break;
            case RoleConsts.USER_ROLE_AO2_LEAD:
            case RoleConsts.USER_ROLE_AO2:
                statusStrs = AO2_STATUS;
                break;
            case RoleConsts.USER_ROLE_AO3_LEAD:
            case RoleConsts.USER_ROLE_AO3:
                statusStrs = AO3_STATUS;
                break;
            case RoleConsts.USER_ROLE_BROADCAST:
                statusStrs = BOARDCAST_STATUS;
                break;
            case RoleConsts.USER_ROLE_INSPECTION_LEAD:
                statusStrs = INSP_LEADER_STATUS;
                break;
            case RoleConsts.USER_ROLE_INSPECTIOR:
                statusStrs = INSP_STATUS;
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
       if (request == null){
           dto.setOperationType(AuditTrailConsts.OPERATION_TYPE_BATCH_JOB);
           return;
       }

        log.info("start set up audit trail log info..................");
        User user = SessionManager.getInstance(request).getCurrentUser();
        HttpSession session = request.getSession();
        LoginContext loginContext = (LoginContext) ParamUtil.getSessionAttr(request, AppConsts.SESSION_ATTR_LOGIN_USER);
        if (user != null && loginContext != null) {
            String nricNum = loginContext.getNricNum();
            String uenNo = loginContext.getUenNo();
            String mohUserId = loginContext.getLoginId();
            String userGuid = loginContext.getUserId();
            int loginType, operationType;
            if (AppConsts.USER_DOMAIN_INTERNET.equals(loginContext.getUserDomain())) {
                dto.setNricNumber(nricNum);
                dto.setUenId(uenNo);
                dto.setMohUserId(mohUserId);
                operationType = AuditTrailConsts.OPERATION_TYPE_INTERNET;

                if (OrganizationConstants.ID_TYPE_FIN.equals(IaisEGPHelper.checkIdentityNoType(nricNum))){
                    //for audit trail page display, issue 67866
                    dto.setEntityId(nricNum);
                }

                loginType = StringUtils.isEmpty(uenNo) ? AuditTrailConsts.LOGIN_TYPE_SING_PASS : AuditTrailConsts.LOGIN_TYPE_CORP_PASS;
            }else if (AppConsts.USER_DOMAIN_INTRANET.equals(loginContext.getUserDomain())){
                dto.setMohUserId(mohUserId);
                loginType = AuditTrailConsts.LOGIN_TYPE_MOH;
                operationType = AuditTrailConsts.OPERATION_TYPE_INTRANET;
            }else {
                loginType = AuditTrailConsts.LOGIN_TYPE_EGP;
                operationType = AuditTrailConsts.OPERATION_TYPE_BATCH_JOB;
            }

            log.info(StringUtil.changeForLog("====>>>>>>nricNum " + nricNum));
            log.info(StringUtil.changeForLog("====>>>>>>uenNo " + uenNo));
            log.info(StringUtil.changeForLog("====>>>>>>mohUserId " + mohUserId));
            log.info(StringUtil.changeForLog("====>>>>>>userGuid " + userGuid));
            log.info(StringUtil.changeForLog("====>>>>>>loginType " + loginType));
            log.info(StringUtil.changeForLog("====>>>>>>operationType " + operationType));

            dto.setMohUserGuid(userGuid);
            dto.setLoginType(loginType);
            dto.setOperationType(operationType);
            dto.setUserDomain(SessionManager.getInstance(request).getCurrentUserDomain());
        }

        if (AuditTrailConsts.OPERATION_LOGIN_FAIL != dto.getOperation()){
            dto.setSessionId(session.getId());
        }

        dto.setClientIp(MiscUtil.getClientIp(request));
        dto.setUserAgent(AccessUtil.getBrowserInfo(request));

        // TODO Remove the log later because it consumes performance
        log.info(StringUtil.changeForLog("current audit trail info" + JsonUtil.parseToJson(dto)));
    }



    public static boolean isLogin(){
        log.info(StringUtil.changeForLog("Judge the if login start ..."));
        boolean result = false;
        HttpServletRequest request = MiscUtil.getCurrentRequest();
        if (request != null) {
            LoginContext loginContext = (LoginContext) ParamUtil.getSessionAttr(request, AppConsts.SESSION_ATTR_LOGIN_USER);
            if (loginContext != null) {
                result = true;
            }else{
                log.info(StringUtil.changeForLog("isLogin do not have the loginContext..."));
            }
        }else{
            log.debug(StringUtil.changeForLog("isLogin do not have the request..."));
        }
        log.info(StringUtil.changeForLog("isLogin the  result is -->:"+result));
        log.info(StringUtil.changeForLog("Judge the if login end ..."));
        return result;
    }



    public static String getRootPath() {
        String urlStr = IaisEGPHelper.class.getResource("").toString();
        String serverPath = urlStr.substring(urlStr.lastIndexOf("file:/") + 6).replaceAll("%20", " "); //NOSONAR
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
                searchParam.setPageSize(SystemParamUtil.getDefaultPageSize());
                searchParam.setPageNo(filter.getPageNo());
                String sortType;
                if (StringUtil.isEmpty(sortType = filter.getSortType())){
                    sortType = SearchParam.ASCENDING;
                }

                searchParam.setSort(filter.getSortField(), sortType);
                if (IaisCommonUtils.isNotEmpty(filter.getSortFieldMap())){
                    for (Map.Entry<String, String> entry : filter.getSortFieldMap().entrySet()){
                        searchParam.addSort(entry.getKey(), entry.getValue());
                    }
                }

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
            val = str.getBytes(StandardCharsets.UTF_8);
            val[0] = (byte) ((char) val[0] - 'a' + 'A');
        return new String(val,StandardCharsets.UTF_8);
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

    public static String generateRandomString(int length) {
        String base = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int number = RANDOM.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        return sb.toString();
    }

    /**
     * @description: format date
     * @param:
     * @return:
     * @author: yichen
     */
    public static Date parseToDate(String val, String pattern) {
        if(val == null || StringUtils.isEmpty(pattern)){
            throw new IaisRuntimeException("No has input for String to Date!");
        }

        try {
            if (StringUtil.isEmpty(val)){
                return null;
            }
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
        String secKey = SpringContextHelper.getContext().getBean(RedisCacheHelper.class).get("iaisEdToken",
                "Callback_SecKEy__SubId_" + submissionId);
        if (StringUtil.isEmpty(secKey)) {
            secKey = String.valueOf(System.currentTimeMillis());
            SpringContextHelper.getContext().getBean(RedisCacheHelper.class).set("iaisEdToken",
                    "Callback_SecKEy__SubId_" + submissionId, secKey, 60L * 60L * 24L);
        }
        String token = StringUtil.digestStrSha256(serviceName + secKey);

        return token;
    }

    public static boolean verifyCallBackToken(String submissionId, String serviceName, String token) {
        String secKey = SpringContextHelper.getContext().getBean(RedisCacheHelper.class).get("iaisEdToken",
                "Callback_SecKEy__SubId_" + submissionId);
        String corrToken = StringUtil.digestStrSha256(serviceName + secKey);

        return token.equals(corrToken);
    }

    public static void sendRedirect(HttpServletRequest request, HttpServletResponse response, String url){
        String tokenUrl = RedirectUtil.appendCsrfGuardToken(url, request);
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
        if(startDate == null || endDate == null) {
            return  -1;
        }
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

    public static HttpHeaders getHttpHeadersForEic(MediaType mediaType, String date, String authorization, String dateSec,
                                                   String authorizationSec) {
        HttpHeaders header = new HttpHeaders();
        header.setContentType(mediaType);
        header.set("date", date);
        header.set("authorization", authorization);
        header.set("date-Secondary", dateSec);
        header.set("authorization-Secondary", authorizationSec);

        return header;
    }

    public static <T> FeignResponseEntity<T> genFeignRespFromResp(ResponseEntity<T> response, Class contentCls) {
        FeignResponseEntity resEnt = new FeignResponseEntity();
        resEnt.setEntity(response.getBody());
        resEnt.setHeaders(response.getHeaders());
        resEnt.setStatusCode(response.getStatusCodeValue());

        return resEnt;
    }

    /**
     * @description: The method to call EIC gateway by rest template
     *
     * @author: Jinhua on 2020/7/6 11:27
     * @param: [url, httpMethod, httpBody, mediaType, date, authorization, dateSec, authorizationSec, responseCls]
     * @return: com.ecquaria.cloudfeign.FeignResponseEntity<T>
     */
    public static <T> FeignResponseEntity<T> callEicGatewayWithBody(String url, HttpMethod httpMethod, Object httpBody,
                                                MediaType mediaType, String date, String authorization, String dateSec,
                                                String authorizationSec, Class<T> responseCls) {
        ResponseEntity<T> response = IaisCommonUtils.callEicGatewayWithBody(url, httpMethod, httpBody, mediaType, date, authorization,
                dateSec, authorizationSec, responseCls);
        return genFeignRespFromResp(response, null);
    }

    /**
     * @description: The method to call EIC gateway by rest template to get List
     *
     * @author: Jinhua on 2020/7/7 10:10
     * @param: [url, httpMethod, httpBody, mediaType, date, authorization, dateSec, authorizationSec, responseCls, contentCls]
     * @return: com.ecquaria.cloudfeign.FeignResponseEntity<List>
     */
    public static FeignResponseEntity<List> callEicGatewayWithBodyForList(String url, HttpMethod httpMethod, Object httpBody,
                                                                       MediaType mediaType, String date, String authorization, String dateSec,
                                                                       String authorizationSec, Class contentCls) {
        ResponseEntity<List> response = IaisCommonUtils.callEicGatewayWithBodyForList(url, httpMethod, httpBody, mediaType, date, authorization,
                dateSec, authorizationSec, contentCls);
        return genFeignRespFromResp(response, contentCls);
    }

    /**
     * @description: The method to call EIC gateway by rest template to get Search Result
     *
     * @author: Jinhua on 2020/7/7 10:10
     * @param: [url, httpMethod, httpBody, mediaType, date, authorization, dateSec, authorizationSec, responseCls, contentCls]
     * @return: com.ecquaria.cloudfeign.FeignResponseEntity<List>
     */
    public static FeignResponseEntity<SearchResult> callEicGatewayWithBodyForSearchResult(String url, HttpMethod httpMethod, Object httpBody,
                                                                          MediaType mediaType, String date, String authorization, String dateSec,
                                                                          String authorizationSec, Class contentCls) {
        ResponseEntity<SearchResult> response = IaisCommonUtils.callEicGatewayWithBodyForSearchResult(url, httpMethod, httpBody, mediaType, date, authorization,
                dateSec, authorizationSec, contentCls);
        return genFeignRespFromResp(response, contentCls);
    }

    /**
     * @description: The method to call EIC gateway by rest template
     *
     * @author: Jinhua on 2020/7/6 11:28
     * @param: [url, httpMethod, params, mediaType, date, authorization, dateSec, authorizationSec, responseCls]
     * @return: com.ecquaria.cloudfeign.FeignResponseEntity<T>
     */
    public static <T> FeignResponseEntity<T> callEicGatewayWithParam(String url, HttpMethod httpMethod, Map<String, Object> params,
                                                                    MediaType mediaType, String date, String authorization, String dateSec,
                                                                    String authorizationSec, Class<T> responseCls) {
        ResponseEntity<T> response = IaisCommonUtils.callEicGatewayWithParam(url, httpMethod, params, mediaType, date, authorization,
                dateSec, authorizationSec, responseCls);
        return genFeignRespFromResp(response, null);
    }

    /**
     * @description: The method to call EIC gateway by rest template to get List
     *
     * @author: Jinhua on 2020/7/7 10:13
     * @param: [url, httpMethod, params, mediaType, date, authorization, dateSec, authorizationSec, contentCls]
     * @return: com.ecquaria.cloudfeign.FeignResponseEntity<java.util.List>
     */
    public static FeignResponseEntity<List> callEicGatewayWithParamForList(String url, HttpMethod httpMethod, Map<String, Object> params,
                                                                             MediaType mediaType, String date, String authorization, String dateSec,
                                                                             String authorizationSec, Class contentCls) {
        ResponseEntity<List> response = IaisCommonUtils.callEicGatewayWithParamForList(url, httpMethod, params, mediaType, date, authorization,
                dateSec, authorizationSec, contentCls);
        return genFeignRespFromResp(response, contentCls);
    }

    public static String[] getPageSizeByStrings(String pageSizeStr) throws IaisRuntimeException{
        if (StringUtils.isEmpty(pageSizeStr)){
            throw new IaisRuntimeException("can not convert page size");
        }

        return pageSizeStr.split(",");
    }


    public static boolean isAsc(int[] to){
        int min = to[0];
        boolean order = true;
        for (int i = 0; i < to.length; i++){
            if (to[i] < min){
                order = false;
                break;
            }else {
                min = to[i];
            }
        }
        return order;
    }

    public static boolean isDesc(int[] to){
        int max = to[0];
        boolean order = true;
        for (int i = 0; i < to.length; i++){
            if (to[i] > max){
                order = false;
                break;
            }else {
                max = to[i];
            }
        }
        return order;
    }

    public static String getAddress(String blkNo, String streetName, String builderName, String floorNo, String unitNo, String postalCode){
        return  MiscUtil.getAddress(blkNo, streetName, builderName,
                floorNo, unitNo, postalCode);
    }

    public static String checkIdentityNoType(String identityNo){
        String upper = identityNo.toUpperCase();
        boolean b = SgNoValidator.validateNric(upper);
        boolean b1 = SgNoValidator.validateFin(upper);
        if (b){
            return OrganizationConstants.ID_TYPE_NRIC;
        }

        if (b1){
            return OrganizationConstants.ID_TYPE_FIN;
        }

        return OrganizationConstants.ID_TYPE_NRIC;
    }

    public static PaginationHandler getPageHandlerFromSession(HttpServletRequest request, String pageDiv) {
        return (PaginationHandler<?>) ParamUtil.getSessionAttr(request, pageDiv + "__SessionAttr");
    }

    public static String generateEDHRequestAuthorizationStr(){
        StringBuilder authorization = new StringBuilder();
        authorization.append("PKI_SIGN ").append("app_id=").append(',');
        authorization.append("nonce=").append(System.currentTimeMillis()).append(',');
        authorization.append("signature_method=").append("RS256").append(',');
        authorization.append("signature=").append("");
        authorization.append("timestamp=").append("System.currentTimeMillis()");
        return authorization.toString();
    }

    public static void onChangeCheckbox(HttpServletRequest request){
        String action = ParamUtil.getString(request, "action");
        String itemId = ParamUtil.getString(request, "itemId");
        String redisplayName = ParamUtil.getString(request, "forName");
        String checkboxName = ParamUtil.getString(request, "checkboxName");

        HashSet<String> set = (HashSet<String>) ParamUtil.getSessionAttr(request, redisplayName);

        Optional.ofNullable(set).orElseGet(() -> new HashSet<>());

        String unMaskVal = MaskUtil.unMaskValue(checkboxName, itemId);
        if ("checked".equals(action)){
            set.add(unMaskVal);
        }else {
            if (set.contains(unMaskVal)){
                set.remove(unMaskVal);
            }
        }
        ParamUtil.setSessionAttr(request, redisplayName, set);
    }
}

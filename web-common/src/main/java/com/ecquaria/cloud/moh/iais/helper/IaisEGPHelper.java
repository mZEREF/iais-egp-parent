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

import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.AuditTrailConsts;
import com.ecquaria.cloud.moh.iais.common.dto.AuditTrailDto;
import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.postcode.PostCodeDto;
import com.ecquaria.cloud.moh.iais.common.utils.MiscUtil;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.RestApiUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.dto.FilterParameter;
import com.ecquaria.egp.api.EGPHelper;
import lombok.extern.slf4j.Slf4j;
import sop.iwe.SessionManager;
import sop.rbac.user.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

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
        if (dto == null)
            return;

        HttpServletRequest request = MiscUtil.getCurrentRequest();
        if (request == null)
            return;

        User user = SessionManager.getInstance(request).getCurrentUser();
        HttpSession session = request.getSession();
        if (user != null) {
            dto.setNricNumber(user.getId());
            dto.setMohUserId(user.getId());
            dto.setMohUserGuid(AppConsts.USER_ID_ANONYMOUS);
            dto.setUserDomain(SessionManager.getInstance(request).getCurrentUserDomain());
        }
        dto.setSessionId(session.getId());
        dto.setClientIp(MiscUtil.getClientIp(request));
        dto.setUserAgent(request.getHeader("User-Agent"));
    }

    public static String getRootPath() {
        String urlStr = IaisEGPHelper.class.getResource("").toString();
        String serverPath = urlStr.substring(urlStr.lastIndexOf("file:/") + 6).replaceAll("%20", " ");
        String path = "";
        if (serverPath.lastIndexOf("WEB-INF") > 0)
            path = serverPath.substring(0, serverPath.lastIndexOf("WEB-INF")) ;

        return path;
    }

    public static AuditTrailDto getCurrentAuditTrailDto() {
        AuditTrailDto dto = null;
        HttpServletRequest request = MiscUtil.getCurrentRequest();
        if (request != null)
            dto = (AuditTrailDto) ParamUtil.getSessionAttr(request, AuditTrailConsts.SESSION_ATTR_PARAM_NAME);

        if (dto == null)
            dto = AuditTrailDto.getThreadDto();

        return dto;
    }
    /**
     * @description: get the PostCodeDto by the postalCode
     *
     * @author: suocheng on 8/23/2019 4:27 PM
     * @param: [postalCode]
     * @return: com.ecquaria.cloud.moh.iais.dto.PostCodeDto
     */
    public static PostCodeDto getPostCodeByCode(String postalCode) {
        Map<String, Object> map = new HashMap<>();
        map.put("searchField", "postalCode");
        map.put("filterValue", postalCode);
        return RestApiUtil.getByReqParam("postcodes", map, PostCodeDto.class);
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
                if(fieldName.startsWith("_ATTR")){
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
     * Get the record by primary key
     * @param serviceName
     * @param primaryKey
     * @param clz
     * @param <T>
     * @return
     */
    public static <T> T getRecordByPrimaryKey(String serviceName, String primaryKey, Class<? extends Serializable> clz){
        Map<String, Object> paramMapper = new HashMap<>();
        paramMapper.put("id", primaryKey);
        return (T) RestApiUtil.getByReqParam(serviceName + "/{id}", paramMapper, clz);
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
    public static Date parseToDate(String val){
        if(StringUtil.isEmpty(val)){
            return null;
        }

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        ParsePosition position = new ParsePosition(0);
        return formatter.parse(val, position);
    }

    private IaisEGPHelper() {throw new IllegalStateException("Utility class");}
}

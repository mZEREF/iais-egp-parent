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

import com.ecquaria.egp.api.EGPHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import sg.gov.moh.iais.common.dto.SearchParam;
import sg.gov.moh.iais.common.dto.SearchResult;
import sg.gov.moh.iais.common.utils.MiscUtil;
import sg.gov.moh.iais.common.utils.StringUtil;
import sg.gov.moh.iais.web.logging.dto.AuditTrailDto;
import sop.iwe.SessionManager;
import sop.rbac.user.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
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

    public static SearchResult doQuery(String uri,SearchParam param){
        Object result = callRestApi(uri,param,SearchResult.class);
        if(result==null)
            return new SearchResult();
        return (SearchResult)result;
    }

    public static String doSave(String uri, Object entity){
        Object result = callRestApi(uri,entity,String.class);
        if(result==null)
            return "";
        return (String)result;
    }

    private static Object callRestApi(String uri, Object entity,Class retrunClass){
        if(!StringUtil.isEmpty(uri) && entity!=null){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity jsonPart = new HttpEntity<>(entity, headers);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Object> resultResponseEntity =
                restTemplate.exchange(MiscUtil.getRestApiUrl()+uri, HttpMethod.POST, jsonPart, retrunClass);
        int status =  resultResponseEntity.getStatusCodeValue();
         if(status == 200){
            return resultResponseEntity.getBody();
         }
        }else {
            log.error("The uri or entity is null...");
        }
        return null;
    }

    private IaisEGPHelper() {throw new IllegalStateException("Utility class");}
}

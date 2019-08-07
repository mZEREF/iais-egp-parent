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

import com.ecquaria.cloud.moh.iais.tags.SelectOption;
import com.ecquaria.egp.api.EGPHelper;
import sg.gov.moh.iais.common.utils.MiscUtil;
import sg.gov.moh.iais.common.utils.ParamUtil;
import sg.gov.moh.iais.web.logging.dto.AuditTrailDto;
import sop.iwe.SessionManager;
import sop.rbac.user.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public final class IaisEGPHelper extends EGPHelper {

    /**
     * pre-set options to web page
     *
     * @param request httpservlet request
     * @param optMap  options name
     */
    public static void setOptionToList(HttpServletRequest request, Map<String, List<String>> optMap) {
        if (request == null || optMap == null) {
            return;
        }

        for (String s : optMap.keySet()) {
            List<SelectOption> selectList = new ArrayList<>();
            List<String> ls = optMap.get(s);
            ls.stream().forEach(i -> {
                SelectOption sp1 = new SelectOption(i, i);
                selectList.add(sp1);
            });

            ParamUtil.setRequestAttr(request, s, selectList);
        }
    }

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

    private IaisEGPHelper() {throw new IllegalStateException("Utility class");}
}

/*
 *   This file is generated by ECQ project skeleton automatically.
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
import sg.gov.moh.iais.common.utils.MiscUtil;
import sg.gov.moh.iais.web.logging.dto.AuditTrailDto;
import sop.iwe.SessionManager;
import sop.rbac.user.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

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

    private IaisEGPHelper() {throw new IllegalStateException("Utility class");}
}

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

import com.ecquaria.cloud.moh.iais.web.logging.dto.AuditTrailDto;
import sg.gov.moh.iais.common.constant.AuditTrailConsts;
import sg.gov.moh.iais.common.utils.MiscUtil;
import sg.gov.moh.iais.common.utils.ParamUtil;

import javax.servlet.http.HttpServletRequest;

/**
 * AuditTrailHelper
 *
 * @author Jinhua
 * @date 2019/8/6 17:58
 */
public class AuditTrailHelper {

    public static void auditFunction(String moduleName, String functionName) {
        HttpServletRequest request = MiscUtil.getCurrentRequest();
        AuditTrailDto dto = (AuditTrailDto) ParamUtil.getSessionAttr(request,
                AuditTrailConsts.SESSION_ATTR_PARAM_NAME);
        if (dto == null)
            dto = new AuditTrailDto();

        IaisEGPHelper.setAuditLoginUserInfo(dto);
        dto.setModule(moduleName);
        dto.setFunctionName(functionName);
        ParamUtil.setSessionAttr(request, AuditTrailConsts.SESSION_ATTR_PARAM_NAME, dto);
    }
    private AuditTrailHelper() {
        throw new IllegalStateException("Utility class");
    }
}

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

package com.ecquaria.cloud.moh.iais.validate;

import com.ecquaria.cloud.helper.SpringContextHelper;
import com.ecquaria.cloud.moh.iais.action.OrgUserAccountDelegator;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.common.validation.interfaces.CustomizeValidator;
import com.ecquaria.cloud.moh.iais.dto.OrgUserAccountDto;
import com.ecquaria.cloud.moh.iais.service.OrgUserAccountService;
import com.ecquaria.cloud.moh.iais.service.impl.OrgUserAccountServiceImpl;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * OrgUserAccountValidate
 *
 * @author suocheng
 * @date 8/1/2019
 */
public class OrgUserAccountValidate implements CustomizeValidator {

    @Override
    public Map<String, String> validate(HttpServletRequest request) {
        Map<String, String> errMap = new HashMap<>();
        OrgUserAccountDto dto = (OrgUserAccountDto) ParamUtil.getSessionAttr(request,
                OrgUserAccountDelegator.ORG_USER_DTO_ATTR);
        if (dto == null || StringUtil.isEmpty(dto.getNircNo()))
            return errMap;

        OrgUserAccountDto oua = getOrgUserAccountService().getOrgUserAccountByNircNo(dto.getNircNo());
        if (oua != null && !oua.getId().equals(dto.getId())) {
            errMap.put("nircNo", "Duplicate NRIC No.");
        }

        return errMap;
    }

    private  OrgUserAccountService getOrgUserAccountService(){
        return SpringContextHelper.getContext().getBean(OrgUserAccountServiceImpl.class);
    }
}

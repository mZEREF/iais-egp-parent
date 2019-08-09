package com.ecquaria.cloud.moh.iais.validation;

import com.ecquaria.cloud.helper.SpringContextHelper;
import com.ecquaria.cloud.moh.iais.controller.MasterCodeDelegator;
import com.ecquaria.cloud.moh.iais.dao.MasterCodeRepository;
import com.ecquaria.cloud.moh.iais.dto.MasterCodeDto;
import com.ecquaria.cloud.moh.iais.entity.MasterCode;
import sg.gov.moh.iais.common.utils.ParamUtil;
import sg.gov.moh.iais.common.utils.StringUtil;
import sg.gov.moh.iais.common.validation.interfaces.CustomizeValidator;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

public class MasterCodeValidator implements CustomizeValidator {

    private MasterCodeRepository masterCodeRepository = SpringContextHelper.getContext().getBean(MasterCodeRepository.class);
    @Override
    public Map<String, String> validate(HttpServletRequest httpServletRequest) {
        Map<String, String> errMap = new HashMap<>();
        MasterCodeDto dto = (MasterCodeDto) ParamUtil.getSessionAttr(httpServletRequest,
                MasterCodeDelegator.MASTERCODE_USER_DTO_ATTR);
        if (dto == null || StringUtil.isEmpty(dto.getMasterCodeId()))
            return errMap;

        MasterCode masterCode = masterCodeRepository.findOne(Long.valueOf(dto.getMasterCodeId()));
        if (masterCode != null && masterCode.getId()!= dto.getMasterCodeId()) {
            errMap.put("masterCodeId", "Duplicate MasterCode");
        }

        return errMap;
    }
}

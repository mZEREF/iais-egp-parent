package com.ecquaria.cloud.moh.iais.validate;

/*
 *File Name: MessageValidate
 *Creator: yichen
 *Creation time:2019/8/6 10:20
 *Describe:
 */

import com.ecquaria.cloud.moh.iais.dto.MessageDto;
import org.springframework.util.StringUtils;
import sg.gov.moh.iais.common.utils.ParamUtil;
import sg.gov.moh.iais.common.validation.interfaces.CustomizeValidator;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

public class MessageValidate implements CustomizeValidator {
    @Override
    public Map<String, String> validate(HttpServletRequest request) {
        Map<String, String> errMap = new HashMap<>();

        MessageDto dto = (MessageDto) ParamUtil.getRequestAttr(request,
                MessageDto.MESSAGE_REQUEST_DTO);
        if(dto == null && StringUtils.isEmpty(dto.getDomainType())){
            return errMap;
        }
        return errMap;
    }
}

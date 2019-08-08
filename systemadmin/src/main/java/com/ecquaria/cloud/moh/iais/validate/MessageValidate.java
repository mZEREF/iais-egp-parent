package com.ecquaria.cloud.moh.iais.validate;

/*
 *File Name: MessageValidate
 *Creator: yichen
 *Creation time:2019/8/6 10:20
 *Describe:
 */

import com.ecquaria.cloud.moh.iais.dto.MessageDto;
import sg.gov.moh.iais.common.utils.ParamUtil;
import sg.gov.moh.iais.common.validation.interfaces.CustomizeValidator;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MessageValidate implements CustomizeValidator {
    @Override
    public Map<String, String> validate(HttpServletRequest request) {
        Map<String, String> errMap = new HashMap<>();
        MessageDto dto = (MessageDto) ParamUtil.getRequestAttr(request,
                MessageDto.MESSAGE_REQUEST_DTO);
        if(dto == null){
            errMap.put("domainType" , "Is null");
            return errMap;
        }

        if(dto.getDescription().length() <= 0 || dto.getDescription().length() > 255){
            errMap.put("description" , "Description length is no correct");
            return errMap;
        }

        String regEx = "[`~!@#$%^&*()+=|{}':;',\\\\[\\\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
        Pattern pattern = Pattern.compile(regEx);
        Matcher matcher = pattern.matcher(dto.getDescription());
        if(matcher.find()){
            errMap.put("description" , "No special characters are allowed in the description");
            return errMap;
        }

        return errMap;
    }
}

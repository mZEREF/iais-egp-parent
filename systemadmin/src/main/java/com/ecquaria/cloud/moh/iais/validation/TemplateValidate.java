package com.ecquaria.cloud.moh.iais.validation;

import com.ecquaria.cloud.moh.iais.common.constant.systemadmin.MsgTemplateConstants;
import com.ecquaria.cloud.moh.iais.common.dto.templates.MsgTemplateDto;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.validation.interfaces.CustomizeValidator;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: Hc
 * @Program: iais-egp
 * @Create: 2020-03-09 12:41
 **/
public class TemplateValidate implements CustomizeValidator {

    @Override
    public Map<String, String> validate(HttpServletRequest request) {
        Map<String, String> errMap = new HashMap<>();
        MsgTemplateDto msgTemplateDto = (MsgTemplateDto) ParamUtil.getSessionAttr(request, MsgTemplateConstants.MSG_TEMPLATE_DTO);
        if ("MTTP001".equals(msgTemplateDto.getMessageType())
                ||"MTTP004".equals(msgTemplateDto.getMessageType())
                ||"MTTP003".equals(msgTemplateDto.getMessageType())){
            String msgType = ParamUtil.getString(request, MsgTemplateConstants.MSG_TEMPLATE_MSGTYPE);
            if ("MTTP002".equals(msgType)
                    ||"MTTP005".equals(msgType)){
                errMap.put("msgTypeErr","This operation is not allowed");
            }
        }
        if ("DEMD001".equals(msgTemplateDto.getDeliveryMode())){
            if ("MTTP001".equals(msgTemplateDto.getMessageType())
                    ||"MTTP004".equals(msgTemplateDto.getMessageType())
                    ||"MTTP003".equals(msgTemplateDto.getMessageType())){
                String deliveryMode = ParamUtil.getString(request, MsgTemplateConstants.MSG_TEMPLATE_DELIVERY_MODE);
                if ("DEMD002".equals(deliveryMode)){
                    errMap.put("deliveryModeErr","This operation is not allowed");
                }
            }
        }
        return errMap;
    }
}

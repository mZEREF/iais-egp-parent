package com.ecquaria.cloud.moh.iais.validation;

import com.ecquaria.cloud.moh.iais.common.constant.systemadmin.MsgTemplateConstants;
import com.ecquaria.cloud.moh.iais.common.dto.templates.MsgTemplateDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.validation.interfaces.CustomizeValidator;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @Author: Hc
 * @Program: iais-egp
 * @Create: 2020-03-09 12:41
 **/
public class TemplateValidate implements CustomizeValidator {

    @Override
    public Map<String, String> validate(HttpServletRequest request) {
        Map<String, String> errMap = IaisCommonUtils.genNewHashMap();
        MsgTemplateDto msgTemplateDto = (MsgTemplateDto) ParamUtil.getSessionAttr(request, MsgTemplateConstants.MSG_TEMPLATE_DTO);

//        if(msgTemplateDto.getRecipient() == null){
//            errMap.put("toRecipientsErr", MessageUtil.replaceMessage("GENERAL_ERR0006","To Recipients","field"));
//        }
        return errMap;
    }
}

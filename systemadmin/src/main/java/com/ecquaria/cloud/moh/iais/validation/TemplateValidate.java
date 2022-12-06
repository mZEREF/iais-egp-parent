package com.ecquaria.cloud.moh.iais.validation;

import com.ecquaria.cloud.moh.iais.common.constant.systemadmin.MsgTemplateConstants;
import com.ecquaria.cloud.moh.iais.common.dto.templates.MsgTemplateDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.common.validation.interfaces.CustomizeValidator;
import com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant;
import com.ecquaria.cloud.moh.iais.helper.MessageUtil;

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
        Boolean needRecipient = (Boolean) ParamUtil.getSessionAttr(request,"needRecipient");
        if(needRecipient){
            // To Recipients
            if(msgTemplateDto.getRecipient() == null){
                errMap.put("toRecipients", IaisEGPConstant.ERR_MANDATORY);
            }
        }
        // Template Name
        if(StringUtil.isEmpty(msgTemplateDto.getTemplateName())){
            errMap.put("templateName", IaisEGPConstant.ERR_MANDATORY);
        } else if(msgTemplateDto.getTemplateName().length()>500){
            Map<String, String> repMap = IaisCommonUtils.genNewHashMap(3);
            repMap.put("number","500");
            repMap.put("fieldNo","Template Name");
            errMap.put("templateName", MessageUtil.getMessageDesc("GENERAL_ERR0036",repMap));
        }
        // Effective Start Date
        if(msgTemplateDto.getEffectiveFrom() == null){
            errMap.put("effectiveFrom", IaisEGPConstant.ERR_MANDATORY);
        }
        // Effective End Date
        if(msgTemplateDto.getEffectiveTo() == null){
            errMap.put("effectiveTo", IaisEGPConstant.ERR_MANDATORY);
        }
        return errMap;
    }
}
